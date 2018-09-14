package com.service.impl;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.json.JSONException;

import com.cache.BaseCacheService;
import com.cache.CacheService;
import com.cache.CacheSynDBService;
import com.common.Config;
import com.common.DateService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.LockService;
import com.common.LogoutCacheService;
import com.common.ServiceCollection;
import com.constant.BattleConstant;
import com.constant.CacheConstant;
import com.constant.CacheSynConstant;
import com.constant.ChatConstant;
import com.constant.ConfigConstant;
import com.constant.ExceptionConstant;
import com.constant.LockConstant;
import com.constant.PlayerConstant;
import com.constant.ProdefineConstant;
import com.constant.RewardTypeConstant;
import com.constant.TaskConstant;
import com.core.Connection;
import com.dao.player.PlayerDAO;
import com.db.GameSqlSessionTemplate;
import com.domain.GameEntity;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.activity.BaseTurntable;
import com.domain.bag.PlayerEquipment;
import com.domain.base.BaseProperty;
import com.domain.chat.Notice;
import com.domain.family.Family;
import com.domain.family.PlayerFamily;
import com.domain.friend.PlayerFriend;
import com.domain.furnace.PlayerFurnace;
import com.domain.guild.Guild;
import com.domain.guild.PlayerGuild;
import com.domain.instance.PlayerInstance;
import com.domain.player.Player;
import com.domain.player.PlayerDaily;
import com.domain.player.PlayerExt;
import com.domain.player.PlayerOptional;
import com.domain.player.PlayerProperty;
import com.domain.player.PlayerWealth;
import com.domain.puppet.BasePuppet;
import com.domain.puppet.PlayerPuppet;
import com.domain.team.Team;
import com.domain.team.TeamPlayer;
import com.domain.vip.PlayerVip;
import com.domain.wakan.PlayerWakan;
import com.message.ActivityProto.S_GetOpenServerData;
import com.message.ActivityProto.S_GetPayActData;
import com.message.ActivityProto.S_GetTurntableData;
import com.message.ActivityProto.S_SynRewardList;
import com.message.ChatProto.ParamType;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.PlayerProto.S_QuickTips;
import com.message.PlayerProto.S_SynPlayerProperty;
import com.message.PlayerProto.S_SynPlayerTitle;
import com.message.PlayerProto.SynPlayerPropertyMsg;
import com.message.SceneProto.S_ShowPlayer;
import com.message.VipProto.S_GetDailyRewardState;
import com.service.IActivityService;
import com.service.IBagService;
import com.service.IChatService;
import com.service.ICollectService;
import com.service.ICommonService;
import com.service.IEnemyService;
import com.service.IEpigraphService;
import com.service.IEquipmentService;
import com.service.IFamilyService;
import com.service.IFashionService;
import com.service.IFriendService;
import com.service.IFurnaceService;
import com.service.IGuildService;
import com.service.IInstanceService;
import com.service.ILogService;
import com.service.IMailService;
import com.service.IMarketService;
import com.service.IPlayerService;
import com.service.IProtoBuilderService;
import com.service.ISceneService;
import com.service.ISignService;
import com.service.ISkillService;
import com.service.ITaskService;
import com.service.ITeamService;
import com.service.ITiantiService;
import com.service.ITradeService;
import com.service.IVipService;
import com.service.IWakanService;
import com.service.IWingService;
import com.util.IDUtil;
import com.util.LogUtil;
import com.util.PlayerUtil;
import com.util.ResourceUtil;

/**
 * 玩家系统
 * @author ken
 * @date 2016-12-24
 */
public class PlayerService implements IPlayerService {

	private PlayerDAO playerDao = new PlayerDAO();
	
	@Override
	public void initCache() {
		CacheService.putToCache(CacheConstant.LOGIN_PLAYER_ID_SET, new CopyOnWriteArraySet<Integer>());
		
		// 开服初始一次成长基金人数
		this.calBuyGrowthFundNum();
	}

	@Override
	public void deleteCache(long playerId) {
		CacheService.deleteFromCache(CacheConstant.PLAYER_CACHE + playerId);
		CacheService.deleteFromCache(CacheConstant.PLAYER_EXT_CACHE + playerId);
		CacheService.deleteFromCache(CacheConstant.PLAYER_PROPERTY_CACHE + playerId);
		CacheService.deleteFromCache(CacheConstant.PLAYER_DAILY_CACHE + playerId);
		CacheService.deleteFromCache(CacheConstant.PLAYER_WEALTH_CACHE + playerId);
		getPlayerIDCache().remove(playerId);
	}

	@SuppressWarnings("unchecked")
	public Set<Long> getPlayerIDCache() {
		return (Set<Long>) CacheService.getFromCache(CacheConstant.LOGIN_PLAYER_ID_SET); 
	}

	private void addPlayerIDCache(long playerId) {
		getPlayerIDCache().add(playerId);
	}
	
	
	public Player getPlayerByID(long playerId){
		Player player = (Player) CacheService.getFromCache(CacheConstant.PLAYER_CACHE + playerId);
		if(player == null){
			player = playerDao.getPlayerByPlayerId(playerId);
			if(player != null){
				CacheService.putToCache(CacheConstant.PLAYER_CACHE+playerId, player);
				this.addPlayerIDCache(playerId);
			}
		}
		return player;
	}
	

	/**
	 * 计算全服购买基金总和(本服购买人数=真实购买人数+开服小时数*10)
	 */
	@Override
	public void calBuyGrowthFundNum(){
		
		// 真实购买基金人数
		int num = playerDao.getBuyGrowthFundNum();
		
		// 开服时间到现在的间隔		
		int hour = (DateService.difTime(Config.OPEN_SERVER_DATE) / DateService.HOUR_SEC);
		CacheService.putToCache(CacheConstant.BUY_GROWTH_FUND_NUM_CACHE, Integer.valueOf(num + Math.min(2000, hour * 5)));
	}	
	

	@Override
	public int getBuyGrowthFundNum(){
		Integer num = (Integer)CacheService.getFromCache(CacheConstant.BUY_GROWTH_FUND_NUM_CACHE);
		if(num == null) return 0;
		return num.intValue();
	}
	
	
	@Override
	public PlayerExt getPlayerExtById(long playerId) {
		PlayerExt playerExt = (PlayerExt) CacheService
				.getFromCache(CacheConstant.PLAYER_EXT_CACHE + playerId);
		if (playerExt == null) {
			playerExt = playerDao.getPlayerExtById(playerId);
			if (playerExt != null) {
				CacheService.putToCache(CacheConstant.PLAYER_EXT_CACHE + playerId,
						playerExt);

			}
		}
		return playerExt;
	}

	@Override
	public PlayerProperty getPlayerPropertyById(long playerId) {
		PlayerProperty playerProperty = (PlayerProperty) CacheService
				.getFromCache(CacheConstant.PLAYER_PROPERTY_CACHE + playerId);
		if (playerProperty == null) {
			playerProperty = playerDao.getPlayerPropertyById(playerId);
			if (playerProperty != null) {
				CacheService.putToCache(CacheConstant.PLAYER_PROPERTY_CACHE + playerId,
						playerProperty);

			}
		}
		return playerProperty;
	}
	
	
	@Override
	public PlayerOptional getPlayerOptionalById(long playerId) {
		PlayerOptional playerOptional = (PlayerOptional) CacheService.getFromCache(CacheConstant.PLAYER_OPTIONAL_CACHE + playerId);
		if (playerOptional == null) {
			playerOptional = playerDao.getPlayerOptionalById(playerId);
			if (playerOptional != null) {
				CacheService.putToCache(CacheConstant.PLAYER_OPTIONAL_CACHE + playerId,	playerOptional);
			}
		}
		return playerOptional;
	}
	
	@Override
	public PlayerDaily getPlayerDailyById(long playerId) {
		PlayerDaily playerDaily = (PlayerDaily) CacheService
				.getFromCache(CacheConstant.PLAYER_DAILY_CACHE + playerId);
		if (playerDaily == null) {
			playerDaily = playerDao.getPlayerDailyById(playerId);
			if (playerDaily != null) {
				CacheService.putToCache(CacheConstant.PLAYER_DAILY_CACHE + playerId,
						playerDaily);

			}
		}
		return playerDaily;
	}
	
	@Override
	public PlayerWealth getPlayerWealthById(long playerId) {
		PlayerWealth playerWealth = (PlayerWealth) CacheService.getFromCache(CacheConstant.PLAYER_WEALTH_CACHE + playerId);
		if (playerWealth == null) {
			playerWealth = playerDao.getPlayerWealthById(playerId);
			if (playerWealth != null) {
				CacheService.putToCache(CacheConstant.PLAYER_WEALTH_CACHE + playerId,playerWealth);

			}
		}
		return playerWealth;
	}

	@Override
	public void updatePlayerDaily(PlayerDaily playerDaily) {
		Set<GameEntity> set =  CacheSynDBService.getFromFiveUpdateOneCache(CacheSynConstant.PLAYER_DAILY);
		if (!set.contains(playerDaily)) {
			set.add(playerDaily);
		}
	}
	
	@Override
	public void updatePlayerExt(PlayerExt playerExt) {
		Set<GameEntity> set =  CacheSynDBService.getFromFiveUpdateOneCache(CacheSynConstant.PLAYER_EXT);
		if (!set.contains(playerExt)) {
			set.add(playerExt);
		}
		
	}

	@Override
	public void updatePlayerProperty(PlayerProperty playerProperty) {
		Set<GameEntity> set =  CacheSynDBService.getFromFiveUpdateOneCache(CacheSynConstant.PLAYER_PROPERTY);
		if (!set.contains(playerProperty)) {
			set.add(playerProperty);
		}
	}
	
	
	@Override
	public void updatePlayerOptional(PlayerOptional playerOptional) {
		Set<GameEntity> set =  CacheSynDBService.getFromFiveUpdateOneCache(CacheSynConstant.PLAYER_OPTIONAL);
		if (!set.contains(playerOptional)) {
			set.add(playerOptional);
		}
	}
	

	@Override
	public void updatePlayerWealth(PlayerWealth playerWealth) {
		Set<GameEntity> set =  CacheSynDBService.getFromFiveUpdateOneCache(CacheSynConstant.PLAYER_WEALTH);
		if (!set.contains(playerWealth)) {
			set.add(playerWealth);
		}
	}	
	
	@Override
	public void updatePlayer(Player player) {
		Set<GameEntity> set = CacheSynDBService.getFromFiveUpdateOneCache(CacheSynConstant.PLAYER);
		if (!set.contains(player)) {
			set.add(player);
		}		
	}

	
	/**
	 * 创建角色入库
	 * @param userName
	 * @param site
	 * @return
	 */
	public Player createPlayer_sp(long userId, String site, int serverNo, String playerName, int career, long telePhone) throws Exception{
		if (site == null || "".equals(site.trim())) {
			throw new GameException(ExceptionConstant.LOGIN_1001);
		}

		Player player = null;
		java.sql.Connection conn = null;
		CallableStatement callableStatement = null;

		// 事物处理
		try {
			conn = GameSqlSessionTemplate.getInstance().getConnection();

			callableStatement = conn.prepareCall("{call sp_user_player(?,?,?,?,?,?,?,?)}");

			long playerId = IDUtil.geneteId(Player.class);
			// 设置传入值
			callableStatement.setLong(1, playerId);
			callableStatement.setLong(2, userId);
			callableStatement.setString(3, site);
			callableStatement.setInt(4, serverNo);
			callableStatement.setString(5, playerName);
			callableStatement.setInt(6, career);
			callableStatement.setLong(7, telePhone);
			
			// 注册返回值类型
			callableStatement.registerOutParameter(8, Types.INTEGER);

			callableStatement.execute();

			int result = callableStatement.getInt(8);

			if (result == 0) { // 成功
				player = new Player();
				player.setPlayerId(playerId);
				player.setUserId(userId);
				player.setSite(site);
				player.setServerNo(serverNo);
				player.setPlayerName(playerName);
				player.setCareer(career);
				player.setGuid(PlayerUtil.getGuid(PlayerConstant.PLAYER, player.getPlayerId()));
				player.setType(1);
				player.setTelePhone(telePhone);
				player.setCreateTime(new Date(System.currentTimeMillis()));
			} else {
				if (result == 1) {
					LogUtil.error("职业已存在 :"+career,null);
					throw new GameException(ExceptionConstant.CREATE_1105);
				}if (result == 2) {
					LogUtil.error("角色昵称已存在 :"+playerName,null);
					throw new GameException(ExceptionConstant.CREATE_1106);
				} else {
					LogUtil.error("创建角色异常 :"+result+" userId="+userId,null);
					throw new GameException(ExceptionConstant.CREATE_1107);
				}
			}
		} finally {
			if (callableStatement != null) {
				try {
					callableStatement.close();
				} catch (SQLException e) {
					LogUtil.error("异常:",e);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LogUtil.error("异常:",e);
				}
			}
		}

		return player;
	}

	@SuppressWarnings("unchecked")
	public List<Player> listPlayerByUserId(long userId) {
		List<Player> lists = (List<Player>) CacheService.getFromCache(CacheConstant.ACCOUNT_PLAYER_CACHE + userId);
		if(lists == null){
			lists = playerDao.listPlayerByUserId(userId);
			if(lists == null){
				lists = new ArrayList<Player>();
			}
			CacheService.putToCache(CacheConstant.ACCOUNT_PLAYER_CACHE+userId, lists);
			
			
			for(Player player : lists){
				Player p = (Player) CacheService.getFromCache(CacheConstant.PLAYER_CACHE + player.getPlayerId());
				if(p == null){
					CacheService.putToCache(CacheConstant.PLAYER_CACHE + player.getPlayerId(), player);
				}
			}
		}
		return lists;
	}
	
	/**
	 * 启动服务器  加载活跃玩家数据 TODO 
	 */
	@Override
	public void loadPlayerData() {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IEquipmentService equipmentService = serviceCollection.getEquipmentService();
		IBagService bagService = serviceCollection.getBagService();
		ISkillService skillService = serviceCollection.getSkillService();
		IMailService mailService = serviceCollection.getMailService();
		IFriendService friendService = serviceCollection.getFriendService();
		IWakanService wakanService = serviceCollection.getWakanService();
		ITaskService taskService = serviceCollection.getTaskService();
		IInstanceService instanceService = serviceCollection.getInstanceService();
		IEpigraphService epigraphService = serviceCollection.getEpigraphService();
		ITiantiService tiantiService = serviceCollection.getTiantiService();
		IWingService wingService = serviceCollection.getWingService();
		ISignService signService = serviceCollection.getSignService();
		IVipService vipService = serviceCollection.getVipService();
		IFashionService fashionService = serviceCollection.getFashionService();
		IMarketService marketService = serviceCollection.getMarketService();
		IEnemyService enemyService = serviceCollection.getEnemyService();
		IFamilyService familyService = serviceCollection.getFamilyService();
		IActivityService activityService = serviceCollection.getActivityService();
		IGuildService guildService = serviceCollection.getGuildService();
		IFurnaceService furnaceService = serviceCollection.getFurnaceService();
		
		List<PlayerExt> playerExtList = this.getActivePlayerExtList();
	
		if (!playerExtList.isEmpty()) {
			for (PlayerExt playerExt : playerExtList) {
				try {
					CacheService.putToCache(CacheConstant.PLAYER_EXT_CACHE+playerExt.getPlayerId(), playerExt);
					
					this.getPlayerByID(playerExt.getPlayerId());
					this.getPlayerPropertyById(playerExt.getPlayerId());
					this.getPlayerDailyById(playerExt.getPlayerId());
					tiantiService.getPlayerTianti(playerExt.getPlayerId());
					this.getPlayerOptionalById(playerExt.getPlayerId());
					
					// 装备
					equipmentService.getPlayerEquipmentList(playerExt.getPlayerId());
					// 背包
					bagService.getAllPlayerBagListByPlayerID(playerExt.getPlayerId());
					// 药品栏
					bagService.listPlayerDrugs(playerExt.getPlayerId());
					//技能
					skillService.listPlayerSkills(playerExt.getPlayerId());
					//邮件
					mailService.getPlayerMailInboxList(playerExt.getPlayerId());
					//好友 (1:好友， 2:江湖好友)
					friendService.getFriendList(playerExt.getPlayerId(), 1);
					friendService.getFriendList(playerExt.getPlayerId(), 2);
					//注灵
					wakanService.getPlayerWakanMap(playerExt.getPlayerId());
					//任务
					taskService.getPlayerTaskMapByPlayerId(playerExt.getPlayerId());
					//副本
					instanceService.listPlayerInstances(playerExt.getPlayerId());
					//武器铭文
					epigraphService.getPlayerWeaponEffectMap(playerExt.getPlayerId());
					// 羽翼
					wingService.getPlayerWingMap(playerExt.getPlayerId());
					// 签到
					signService.getPlayerSign(playerExt.getPlayerId());					
					// vip
					vipService.getPlayerVip(playerExt.getPlayerId());					
					// 时装
					fashionService.getFashionList(playerExt.getPlayerId());
					// 商城
					marketService.getPlayerMarketMap(playerExt.getPlayerId());
					// 仇敌
					enemyService.getPlayerEnemyList(playerExt.getPlayerId());					
					// 家族
					familyService.getPlayerFamily(playerExt.getPlayerId());					
					//运营活动
					activityService.getPlayerTomb(playerExt.getPlayerId());
					//帮派
					guildService.getPlayerGuild(playerExt.getPlayerId());
					//熔炉
					furnaceService.getPlayerFurnaceList(playerExt.getPlayerId());
				} catch (Exception e) {
					LogUtil.error("异常:",e);
				}
			}
		}
	}

	/** 得到活跃的玩家扩展列表 */
	private List<PlayerExt> getActivePlayerExtList(){
		return playerDao.getActivePlayerExtList(2);
	}
	
	@Override
	public void exit(Connection connection) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		gameSocketService.destroyConnection(connection);
		
		//防变速齿轮
		CacheService.getTimeCacheMap().remove(connection.getUserId());
		CacheService.getTimeErrorMap().remove(connection.getUserId());
	}
	
	@Override
	public void closeServerDealExit(List<Long> playerIDList) {
		if (playerIDList != null) {
			for (Long playerId : playerIDList) {
				updatePlayerExitData(playerId);
			}
		}		
	}
	
	/**
	 * 游戏中下线数据处理
	 */
	public void dealExitData(long playerId){
		
		//下线基础数据处理
		updatePlayerExitData(playerId);
		
		//加入离线缓存队列
		LogoutCacheService.putToCache(playerId, System.currentTimeMillis());
		
	}
	
	
	/**
	 * 下线基础数据处理
	 */
	private void updatePlayerExitData(long playerId){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		ISceneService sceneService = serviceCollection.getSceneService();
		ITeamService teamService = serviceCollection.getTeamService();
		ICollectService collectService = serviceCollection.getCollectService();
		IFamilyService familyService = serviceCollection.getFamilyService();
		ITiantiService tiantiService = serviceCollection.getTiantiService();
		
		PlayerExt playerExt = this.getPlayerExtById(playerId);
		if(playerExt == null) return;
		
		PlayerDaily playerDaily = this.getPlayerDailyById(playerId);
		
		//在线时间处理
		this.handleOnlineTime(playerDaily, playerExt);
		
		//数据存储
		PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerId);
		if(playerPuppet != null){
			playerExt.setHp(playerPuppet.getHp());
			playerExt.setMp(playerPuppet.getMp());
			playerExt.setWeaponStyle(playerPuppet.getWeaponStyle());
			playerExt.setDressStyle(playerPuppet.getDressStyle());
			playerExt.setWingStyle(playerPuppet.getWingStyle());
			playerExt.setPkVlaue(playerPuppet.getPkVlaue());
			playerExt.setNameColor(playerPuppet.getNameColor());
			
			playerExt.setMapId(playerPuppet.getMapId());
			playerExt.setX(playerPuppet.getX());
			playerExt.setY(playerPuppet.getY());
			playerExt.setZ(playerPuppet.getZ());
			playerExt.setLine(playerPuppet.getLine());
			playerExt.setDirection(playerPuppet.getDirection());
			
			// 玩家下线，采集信息处理
			collectService.clearCollect(playerPuppet);	
			
			//下线buff处理			
			serviceCollection.getBuffService().dealExit(playerExt, playerPuppet);
		}
		
		//离线状态与时间设置
		playerExt.setExitTime(DateService.getCurrentUtilDate());		
		
		//组队
		teamService.dealExit(playerExt);
		
		// 家族
		familyService.dealExit(playerId);	
		
		// 竞技场
		tiantiService.dealExit(playerId);
		
		this.updatePlayerExt(playerExt);	
		
	}

	/**
	 * 处理在线奖励信息
	 */
	private void handleOnlineTime(PlayerDaily playerDailyData, PlayerExt playerExt) {

		Date loginTime = playerExt.getLoginTime();
		int difTime = difTime(loginTime);

		playerDailyData.setEveryOnlineTime(playerDailyData.getEveryOnlineTime() + difTime);
		this.updatePlayerDaily(playerDailyData);
		
//		playerExt.setTotalOnlineTime(playerExt.getTotalOnlineTime() + DateService.difTime(loginTime));


		
	}
	
	public int difTime(Date loginTime) {
		int difTime = 0;
		Date now = new Date();
		//当天
		if(DateService.isCurrentDay(loginTime)){

			difTime = (int) ((now.getTime() - loginTime.getTime()) / 1000);

		}else{
			Date startTime = DateService.getDayOfStartTime(now);
			difTime = (int) ((now.getTime() - startTime.getTime()) / 1000);
		}

		return difTime;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void handlePlayerInfoForDaily() throws JSONException {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IInstanceService instanceService = serviceCollection.getInstanceService();
		
		try {
			playerDao.updateAllPlayerDailyData();
			instanceService.quarztDaily();
		} catch (Exception e) {
			LogUtil.error("处理玩家日结异常handlePlayerInfoForDaily：", e);
		}
		
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();		
		List<Long> playerIDSetClone = new ArrayList<Long>(this.getPlayerIDCache());
		
		for(Long playerId : playerIDSetClone){
			PlayerDaily playerDaily = (PlayerDaily) CacheService.getFromCache(CacheConstant.PLAYER_DAILY_CACHE + playerId);
			if (playerDaily != null) {
				// 月卡刷新
				if(playerDaily.getMonthCardVaildTime() > 0){
					// 日结刷新月卡奖励状态
					playerDaily.setMonthCardAwardState(0);
					
					// 月卡过期时间(月结)
					if(DateService.isCurrentDay(new Date(playerDaily.getMonthCardVaildTime()))){
						playerDaily.setMonthCardVaildTime(0);	
					}					
				}
				
				boolean online = gameSocketService.checkOnLine(playerId);
				if(online){
					PlayerOptional playerOptional = (PlayerOptional) CacheService.getFromCache(CacheConstant.PLAYER_OPTIONAL_CACHE + playerId);
					PlayerWealth playerWealth = (PlayerWealth) CacheService.getFromCache(CacheConstant.PLAYER_WEALTH_CACHE + playerId);
					PlayerExt playerExt = (PlayerExt) CacheService.getFromCache(CacheConstant.PLAYER_EXT_CACHE + playerId);
					
					// 同步在线奖励状态列表
					S_SynRewardList.Builder builder = S_SynRewardList.newBuilder();
					builder.setOnlineTime(0);
					if(playerDaily.getRewardIdList() != null){
						for(Integer rewardId : playerDaily.getRewardIdList()){				
							builder.addRewardList(protoBuilderService.buildRewardMsg(rewardId, 0));
						}
					}
					
					MessageObj msg = new MessageObj(MessageID.S_SynRewardList_VALUE, builder.build().toByteArray());
					gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);	
					
					// 每日任务刷新
					playerService.synPlayerProperty(playerId, ProdefineConstant.DAILY_TASK_NUM, 0);
					
					
					// 同步每日奖励状态
					S_GetDailyRewardState.Builder builder1 = S_GetDailyRewardState.newBuilder();
					builder1.setState(0);					
					MessageObj msg1 = new MessageObj(MessageID.S_GetDailyRewardState_VALUE, builder1.build().toByteArray());
					gameSocketService.sendDataToPlayerByPlayerId(playerId, msg1);				
					
					// 同步每日累计充值奖励
					playerDaily.setTodayPay(0);
					if(!playerDaily.getDrRrewardIdList().isEmpty()){						
						playerDaily.getDrRrewardIdList().clear();
						playerDaily.setDrRrewardIdList(playerDaily.getDrRrewardIdList());
					}
					
					S_GetPayActData.Builder builderPAD = S_GetPayActData.newBuilder();
					builderPAD.setBuyGrowthFundNum(playerService.getBuyGrowthFundNum());
					builderPAD.setDailyRrecharge(playerDaily.getTodayPay());					
					builderPAD.setTotalRrecharge(playerWealth.getTotalPay());					
					builderPAD.setIsbuyGrowthFund(playerOptional.getIsBuyGrowthFund());
					builderPAD.addAllDrRewardList(playerDaily.getDrRrewardIdList());
					builderPAD.addAllGfRewardList(playerOptional.getGfRewardIdList());
					builderPAD.addAllNwRewardList(playerOptional.getNwRewardIdList());
					builderPAD.addAllTrRewardList(playerOptional.getTrRewardIdList());
					
					MessageObj msgPAD = new MessageObj(MessageID.S_GetPayActData_VALUE, builderPAD.build().toByteArray());
			 		gameSocketService.sendDataToPlayerByPlayerId(playerId, msgPAD);
			 		
			 		// 同步转盘数据	
			 		playerDaily.setFristTurntableState(0);
					Map<Integer, BaseTurntable> turnRewardMap = (Map<Integer, BaseTurntable>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_TURNTABLE_REWARD);
					S_GetTurntableData.Builder builderTD = S_GetTurntableData.newBuilder();
					builderTD.setFristTurntableState(playerDaily.getFristTurntableState());
					builderTD.addAllTrIdList(turnRewardMap.keySet());
					MessageObj msgTD = new MessageObj(MessageID.S_GetTurntableData_VALUE, builderTD.build().toByteArray());
			 		gameSocketService.sendDataToPlayerByPlayerId(playerId, msgTD);
				
			 		// 累计登录天数处理
					playerExt.setAddLoginDay(playerExt.getAddLoginDay() + 1);
					playerExt.setLoginTime(DateService.getCurrentUtilDate());
					
					S_GetOpenServerData.Builder builderOSD = S_GetOpenServerData.newBuilder();
					builderOSD.setAddLoginDay(playerExt.getAddLoginDay());
					builderOSD.addAllRewardList(playerOptional.getOsRewardIdList());
					
					int OPEN_SERVER_SEVEN_VALID_TIME= serviceCollection.getCommonService().getConfigValue(ConfigConstant.OPEN_SERVER_SEVEN_VALID_TIME); 
					int day = DateService.difDate(Config.OPEN_SERVER_DATE, new Date());
					if(day <= OPEN_SERVER_SEVEN_VALID_TIME){
						builderOSD.setState(1);
					}			
					MessageObj msgOSD = new MessageObj(MessageID.S_GetOpenServerData_VALUE, builderOSD.build().toByteArray());
			 		gameSocketService.sendDataToPlayerByPlayerId(playerId, msgOSD);
				}				
				
				playerDaily.dispose();					
			}
			
			//重置副本次数
			Map<Integer, PlayerInstance> insMap = (Map<Integer, PlayerInstance>)CacheService.getFromCache(CacheConstant.PLAYER_INSTANCE_CACHE + playerId);
			if(insMap != null){
				for(Map.Entry<Integer, PlayerInstance> entry : insMap.entrySet()){
					PlayerInstance model = entry.getValue();
					model.setEnterCount(0);
				}		
			}		
		}
		
	}
	
	@Override
	public void handlePlayerInfoForWeek() {
		
		try {
			playerDao.updateAllPlayerWeekData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		List<Long> playerIDSetClone = new ArrayList<Long>(this.getPlayerIDCache());
		for(Long playerId : playerIDSetClone){
			PlayerExt playerExt = (PlayerExt) CacheService.getFromCache(CacheConstant.PLAYER_EXT_CACHE + playerId);
			
			//环任务次数
			if(playerExt != null && playerExt.getWeekTotalNum() != 0){
				playerExt.setWeekTotalNum(0);			
			}
		}
	}

	@Override
	public int addGold_syn(long playerId, int addGold) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		
		PlayerWealth playerWealth = this.getPlayerWealthById(playerId);
		int newGold = playerWealth.getGold() + addGold;
		
		if(newGold > 2000000000){
			newGold = 2000000000;
			
			// 飘字		
			serviceCollection.getCommonService().sendNoticeMsg(playerId, ResourceUtil.getValue("gold_to_limit"));		
		}
		
		playerWealth.setGold(newGold);
		this.updatePlayerWealth(playerWealth);
		
		synPlayerProperty(playerId, ProdefineConstant.GOLD, playerWealth.getGold());
		
		return addGold;
	}

	@Override
	public int addDiamond_syn(long playerId, int addDiamond, String costName) {
		PlayerWealth playerWealth = this.getPlayerWealthById(playerId);
		
		int newDiamond = playerWealth.getDiamond() + addDiamond;
		
		playerWealth.setDiamond(newDiamond);
		synPlayerProperty(playerId, ProdefineConstant.DIAMOND, playerWealth.getDiamond());
		
		if(addDiamond < 0){
			//累计消耗
			playerWealth.setTotalSpend(playerWealth.getTotalSpend() + Math.abs(addDiamond));
			
			try {
				// 创建消耗日志
				ILogService logService =  GameContext.getInstance().getServiceCollection().getLogService();
				logService.createCostLog(playerId, RewardTypeConstant.DIAMOND, costName, Math.abs(addDiamond));
			} catch (Exception e) {
				LogUtil.error("元宝消耗日志异常：",e);
			}
		}
		
		this.updatePlayerWealth(playerWealth);
		
		return addDiamond;
	}
	
	@Override
	public int addStone_syn(long playerId, int addStone) {
		PlayerWealth playerWealth = this.getPlayerWealthById(playerId);
		
		int newStone = playerWealth.getStone() + addStone;
		
		playerWealth.setStone(newStone);
		this.updatePlayerWealth(playerWealth);
		
		synPlayerProperty(playerId, ProdefineConstant.STONE, playerWealth.getStone());
		return addStone;
	}
	
	@Override
	public void synPlayerProperty(long playerId, int propertyId, int propertyValue) {
		S_SynPlayerProperty.Builder builder = S_SynPlayerProperty.newBuilder();
		builder.setGuid(PlayerUtil.getGuid(PlayerConstant.PLAYER, playerId));
		SynPlayerPropertyMsg.Builder msg = SynPlayerPropertyMsg.newBuilder();
		msg.setPropertyId(propertyId);
		msg.setPropertyValue(propertyValue);
		builder.addPlayerPropertyMsg(msg);
		GameSocketService gameSocketService = GameContext.getInstance()
				.getServiceCollection().getGameSocketService();
		gameSocketService.sendDataToPlayerByPlayerId(playerId, new MessageObj(
				MessageID.S_SynPlayerProperty_VALUE, builder.build()
						.toByteArray()));
	}

	@Override
	public void synPlayerProperty(long playerId, Map<Integer, Integer> propertyMap) {
		S_SynPlayerProperty.Builder builder = S_SynPlayerProperty.newBuilder();
		builder.setGuid(PlayerUtil.getGuid(PlayerConstant.PLAYER, playerId));
		
		for(Map.Entry<Integer, Integer> entry : propertyMap.entrySet()){
			SynPlayerPropertyMsg.Builder msg = SynPlayerPropertyMsg.newBuilder();
			msg.setPropertyId(entry.getKey());
			msg.setPropertyValue(entry.getValue());
			builder.addPlayerPropertyMsg(msg);
		}

		GameSocketService gameSocketService = GameContext.getInstance()
				.getServiceCollection().getGameSocketService();
		gameSocketService.sendDataToPlayerByPlayerId(playerId, new MessageObj(
				MessageID.S_SynPlayerProperty_VALUE, builder.build()
						.toByteArray()));
	}
	
	@Override
	public void synPlayerPropertyToAll(BasePuppet basePuppet, int propertyId, int propertyValue) {
		
		S_SynPlayerProperty.Builder builder = S_SynPlayerProperty.newBuilder();
		builder.setGuid(basePuppet.getGuid());
		SynPlayerPropertyMsg.Builder msg = SynPlayerPropertyMsg.newBuilder();
		msg.setPropertyId(propertyId);
		msg.setPropertyValue(propertyValue);
		builder.addPlayerPropertyMsg(msg);
		
		List<Long> playerIds = GameContext.getInstance().getServiceCollection().getSceneService().getNearbyPlayerIds(basePuppet);
		
		GameSocketService gameSocketService = GameContext.getInstance()
				.getServiceCollection().getGameSocketService();
		gameSocketService.sendDataToPlayerList(playerIds, new MessageObj(
				MessageID.S_SynPlayerProperty_VALUE, builder.build()
						.toByteArray()));
	}

	@Override
	public void synPlayerPropertyToAll(BasePuppet basePuppet, Map<Integer, Integer> propertyMap) {
		S_SynPlayerProperty.Builder builder = S_SynPlayerProperty.newBuilder();
		builder.setGuid(basePuppet.getGuid());
		
		for(Map.Entry<Integer, Integer> entry : propertyMap.entrySet()){
			SynPlayerPropertyMsg.Builder msg = SynPlayerPropertyMsg.newBuilder();
			msg.setPropertyId(entry.getKey());
			msg.setPropertyValue(entry.getValue());
			builder.addPlayerPropertyMsg(msg);
		}
		
		List<Long> playerIds = GameContext.getInstance().getServiceCollection().getSceneService().getNearbyPlayerIds(basePuppet);
		
		GameSocketService gameSocketService = GameContext.getInstance()
				.getServiceCollection().getGameSocketService();
		gameSocketService.sendDataToPlayerList(playerIds, new MessageObj(
				MessageID.S_SynPlayerProperty_VALUE, builder.build()
						.toByteArray()));
	}

	@Override
	public void addPlayerExp(long playerId, int addExp){
		
		if(addExp <= 0) return;
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		ICommonService commonService = serviceCollection.getCommonService();

		PlayerProperty playerProperty = this.getPlayerPropertyById(playerId);
		if(playerProperty.getLevel() >= 100) return;
		
		PlayerPuppet playerPuppet = serviceCollection.getSceneService().getPlayerPuppet(playerId);
		if(playerPuppet == null || playerPuppet.getState() != BattleConstant.STATE_NORMAL){
			return;
		}
		
		Player player = this.getPlayerByID(playerId);
		PlayerExt playerExt = this.getPlayerExtById(playerId);

		int newExp = playerProperty.getExp() + addExp;
		boolean levelFlag = false;

		// 升级
		while (newExp >= commonService.getBaseProperty(player.getCareer(), playerProperty.getLevel()).getNeedexp()) {
			if (playerProperty.getLevel() == 100) {
				newExp = 0;
				break;
			} else {
				// 玩家升级
				newExp = newExp - commonService.getBaseProperty(player.getCareer(), playerProperty.getLevel()).getNeedexp();
				playerLevel(player.getCareer(), playerProperty, playerExt);
				levelFlag = true;
			}
		}
		
		playerProperty.setExp(newExp);
		
		Map<Integer, Integer> propertyMap = new HashMap<Integer, Integer>();
		propertyMap.put(ProdefineConstant.EXP, playerProperty.getExp());
		
		if (levelFlag) {
			//升级
			if(playerPuppet != null){
				playerPuppet.setLevel(playerProperty.getLevel()); 
				playerPuppet.setHp(playerProperty.getHpMax());
				playerPuppet.setMp(playerProperty.getMpMax());
			}
			
			propertyMap.put(ProdefineConstant.LEVEL, playerProperty.getLevel());
			propertyMap.put(ProdefineConstant.HP, playerProperty.getHpMax());
			propertyMap.put(ProdefineConstant.MP, playerProperty.getMpMax());
			
			try {
				//执行升级任务
				List<Integer> conditionList = new ArrayList<Integer>();
				conditionList.add(playerProperty.getLevel());
				serviceCollection.getTaskService().executeTask(playerId, TaskConstant.TYPE_2, conditionList);
			} catch (Exception e) {
				LogUtil.error("执行升级任务异常：", e);
			}
			
			//队伍同步等级
			serviceCollection.getTeamService().synUpLevel(playerExt);
		}
		
		this.updatePlayerProperty(playerProperty);
		
		// 满级飘字
		if(playerProperty.getLevel() >= 100){			
			// 组合消息内容
			IChatService chatService = serviceCollection.getChatService();
			List<Notice> paramList = new ArrayList<Notice>();
			Notice notice1 = new Notice(ParamType.PLAYER, playerId, 0, player.getPlayerName());
			Notice notice2 = new Notice(ParamType.PARAM, 0, 0, playerProperty.getLevel()+"");
			
			paramList.add(notice1);
			paramList.add(notice2);
			
			GameSocketService gameSocketService = serviceCollection.getGameSocketService();
			chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_17, paramList, gameSocketService.getOnLinePlayerIDList());
		}
		
		if(playerPuppet != null){
			this.synPlayerPropertyToAll(playerPuppet, propertyMap);
		}

	}

	/**
	 * 玩家升级
	 */
	private void playerLevel(int career, PlayerProperty playerProperty, PlayerExt playerExt){

		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		
		playerProperty.setLevel(playerProperty.getLevel() + 1);
		
		BaseProperty baseProperty = serviceCollection.getCommonService().getBaseProperty(career, playerProperty.getLevel());
		BaseProperty lastBaseProperty = serviceCollection.getCommonService().getBaseProperty(career, playerProperty.getLevel() - 1);
		
		Map<Integer, Integer>  addProMap = new HashMap<Integer, Integer>();
		addProMap.put(ProdefineConstant.STRENGTH, baseProperty.getStrength() - lastBaseProperty.getStrength());
		addProMap.put(ProdefineConstant.INTELLIGENCE, baseProperty.getIntelligence() - lastBaseProperty.getIntelligence());
		addProMap.put(ProdefineConstant.ENDURANCE, baseProperty.getEndurance() - lastBaseProperty.getEndurance());
		addProMap.put(ProdefineConstant.SPIRIT, baseProperty.getSpirit() - lastBaseProperty.getSpirit());
		addProMap.put(ProdefineConstant.LUCKY, baseProperty.getLucky() - lastBaseProperty.getLucky());
		addProMap.put(ProdefineConstant.DMG_DEEP_PER_PANEL, baseProperty.getDmgDeepPer() - lastBaseProperty.getDmgDeepPer());
		addProMap.put(ProdefineConstant.DMG_REDUCT_PER_PANEL, baseProperty.getDmgReductPer() - lastBaseProperty.getDmgReductPer());
		addProMap.put(ProdefineConstant.DMG_CRIT_PER_PANEL, baseProperty.getDmgCritPer() - lastBaseProperty.getDmgCritPer());
		
		serviceCollection.getPropertyService().addProValue(playerProperty.getPlayerId(), addProMap, true, true);
		
		try {
			//触发任务
			serviceCollection.getTaskService().touchTaskByLevel(playerProperty.getPlayerId(), playerProperty.getLevel());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public Player getPlayerByName(String playerName) {
		 Player player = playerDao.getPlayerByPlayerName(playerName);		 
		 if(player != null) return player;	
		 
		 
		try {
			 long playerId = Long.valueOf(playerName);
			 if(playerName.length() < 10 || playerId < 0) return null;	
			 return this.getPlayerByID(playerId);	
			 
		} catch (NumberFormatException e) {
			return null;
		}
		
	}
	
	@Override
	public int[] getCreateNum(String gameSite) {
		return playerDao.getCreateNum(gameSite);
	}

	@Override
	public Map<String, Object> getOnlineTimeNum(String gameSite, String date) {
		return playerDao.getOnlineTimeNum(gameSite, date);
	}
	
	@Override
	public void getShowPlayer(long playerId, long showPlayerId){
		if(playerId < 1 || showPlayerId < 1) return;
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		IEquipmentService equipmentService = serviceCollection.getEquipmentService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IWakanService wakanService = serviceCollection.getWakanService();
		IVipService vipService = serviceCollection.getVipService();
		IFamilyService familyService = serviceCollection.getFamilyService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IGuildService guildService = serviceCollection.getGuildService();
		
		Player player = playerService.getPlayerByID(showPlayerId);
		if(player == null) return;
		
		PlayerExt playerExt = playerService.getPlayerExtById(showPlayerId);
		PlayerProperty playerProperty = playerService.getPlayerPropertyById(showPlayerId);
		
		S_ShowPlayer.Builder builder = S_ShowPlayer.newBuilder();
		builder.setPlayerId(showPlayerId);
		builder.setPlayerName(player.getPlayerName());
		builder.setSeverNo(player.getServerNo());
		builder.setCareer(player.getCareer());
		builder.setGuid(player.getGuid());
		
		//vip
		PlayerVip playerVip = vipService.getPlayerVip(showPlayerId);
		if(playerVip != null){
			builder.setVipLevel(playerVip.getLevel());
		}
		
		//家族
		int CREATE_FAMILY_LEVEL= serviceCollection.getCommonService().getConfigValue(ConfigConstant.CREATE_FAMILY_LEVEL);
		if(playerProperty.getLevel() >= CREATE_FAMILY_LEVEL){
			PlayerFamily playerFamily = familyService.getPlayerFamily(showPlayerId);
			if(playerFamily != null && playerFamily.getPlayerFamilyId() > 0){
				builder.setPlayerFamilyId(playerFamily.getPlayerFamilyId());
				builder.setFamilySortId(playerFamily.getFamilySortId());
				Family family = familyService.getFamily(playerFamily.getPlayerFamilyId());
				if(family != null){
					builder.setFamilyName(family.getFamilyName());
				}
			}	
		}
		
		//帮派
		int GUILD_NEED_LEVEL= serviceCollection.getCommonService().getConfigValue(ConfigConstant.GUILD_NEED_LEVEL);
		if(playerProperty.getLevel() >= GUILD_NEED_LEVEL){
			PlayerGuild playerGuild = guildService.getPlayerGuild(playerId);
			if(playerGuild != null && playerGuild.getGuildId() > 0){
				Guild guild = guildService.getGuildById(playerGuild.getGuildId());
				if(guild != null){
					builder.setGuildId(guild.getGuildId());
					builder.setGuildName(guild.getGuildName());
				}
			}
		}		
		
		//属性
		List<SynPlayerPropertyMsg.Builder> proMsgs = protoBuilderService.buildSynPlayerPropertyMsgList(playerProperty, playerExt);
		for(SynPlayerPropertyMsg.Builder synMsg : proMsgs){
			builder.addPlayerPropertyMsg(synMsg);
		}
		
		//外形
		int wingStyle = playerExt.getWingStyle();
		int weaponStyle = playerExt.getWeaponStyle();
		int dressStyle = playerExt.getDressStyle();
		if(gameSocketService.checkOnLine(showPlayerId)){
			PlayerPuppet playerPuppet = serviceCollection.getSceneService().getPlayerPuppet(showPlayerId);
			if(playerPuppet != null){
				wingStyle = playerPuppet.getWingStyle();
				weaponStyle = playerPuppet.getWeaponStyle();
				dressStyle = playerPuppet.getDressStyle();	
			}
		}
		
		SynPlayerPropertyMsg.Builder msg62 = SynPlayerPropertyMsg.newBuilder();
		msg62.setPropertyId(ProdefineConstant.WING_STYLE);
		msg62.setPropertyValue(wingStyle);
		builder.addPlayerPropertyMsg(msg62);
		
		SynPlayerPropertyMsg.Builder msg47 = SynPlayerPropertyMsg.newBuilder();
		msg47.setPropertyId(ProdefineConstant.WEAPON_STYLE);
		msg47.setPropertyValue(weaponStyle);
		builder.addPlayerPropertyMsg(msg47);
		
		SynPlayerPropertyMsg.Builder msg48 = SynPlayerPropertyMsg.newBuilder();
		msg48.setPropertyId(ProdefineConstant.DRESS_STYLE);
		msg48.setPropertyValue(dressStyle);
		builder.addPlayerPropertyMsg(msg48);
		
		//装备
		List<PlayerEquipment> playerEquipmentList = equipmentService.getPlayerEquipmentList(showPlayerId);
		for(PlayerEquipment pe : playerEquipmentList){
			if(pe.getState() == 2){
				builder.addListPlayerEquipments(protoBuilderService.buildPlayerEquipmentMsg(pe));				
			}
		}
		
		//注灵
		Map<Integer, PlayerWakan> playerWakanMap = wakanService.getPlayerWakanMap(showPlayerId);
		for(Map.Entry<Integer, PlayerWakan> entry : playerWakanMap.entrySet()){
			PlayerWakan model = entry.getValue();
			builder.addListWakans(protoBuilderService.buildWakanMsg(model));
		}
		
		//诛仙阁
		List<PlayerFurnace> lists = serviceCollection.getFurnaceService().getPlayerFurnaceList(showPlayerId);
		for(PlayerFurnace model : lists){
			builder.addFurnaceList(protoBuilderService.buildPlayerFurnaceMsg(model));
		}
		
		MessageObj msg = new MessageObj(MessageID.S_ShowPlayer_VALUE, builder.build().toByteArray());
		serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);		
	}
	
	@Override
	public void deletePlayer(long playerId) throws Exception{
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IFriendService friendService = serviceCollection.getFriendService();
		ITiantiService tiantiService = serviceCollection.getTiantiService();		
		ITradeService tradeService = serviceCollection.getTradeService();
		ITeamService teamService = serviceCollection.getTeamService();
		ISceneService sceneService = serviceCollection.getSceneService();
		IFamilyService familyService = serviceCollection.getFamilyService();
		IGuildService guildService = serviceCollection.getGuildService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER)) {
			Player player = this.getPlayerByID(playerId);	
			PlayerExt playerExt = this.getPlayerExtById(playerId);
			if(player == null) return;
			
			PlayerFamily playerFamily = familyService.getPlayerFamily(playerId);
			if(playerFamily != null && playerFamily.getPlayerFamilyId() > 0) throw new GameException(ExceptionConstant.FAMILY_2614);
			
			PlayerGuild playerGuild = guildService.getPlayerGuild(playerId);
			if(playerGuild != null && playerGuild.getGuildId() > 0)  throw new GameException(ExceptionConstant.GUILD_3726);
			try {
				sceneService.quitScene(playerId);
				sceneService.deletePlayerPuppet(playerId);
			} catch (Exception e) {
				LogUtil.error("删除角色 -- 删除场景信息错误",e);
			}
			
			List<Player> players = this.listPlayerByUserId(player.getUserId());
			if(players != null){
				Iterator<Player> iterator = players.iterator();
				while(iterator.hasNext()){
					Player model = iterator.next();
					if(model.getPlayerId() == playerId){
						iterator.remove();
					}
				}
			}
			
			playerDao.deletePlayer(playerId);			
					
			try {
				//删除好友
				List<PlayerFriend> friends = friendService.listPlayerFriend(playerId);
				if(!friends.isEmpty()){
					List<PlayerFriend> friends1 = new ArrayList<PlayerFriend>(friends);
					for(PlayerFriend pf : friends1){
						friendService.deleteFriend(playerId, pf.getFriendPlayerId());
					}
				}
			} catch (Exception e) {
				LogUtil.error("删除角色 -- 删除好友信息错误",e);
			}		
			
			try {
				//从队伍中删除
				if(playerExt.getTeamId() > 0){
					Team team = teamService.getTeam(playerExt.getTeamId());
					if(team != null){
						long captainId = 0;
						Map<Long, TeamPlayer> teamPlayerMap = team.getTeamPlayerMap();
						for(Map.Entry<Long, TeamPlayer> entry : teamPlayerMap.entrySet()){
							TeamPlayer tp = entry.getValue(); 
							if(tp.isCaptain()){
								captainId = tp.getPlayerId();
								break;
							}
						}
						if(captainId > 0){
							teamService.kickTeamPlayer(captainId, playerId);
						}
					}
				}
			} catch (Exception e1) {
				LogUtil.error("删除角色 -- 删除队伍信息错误",e1);
			}
			
			try {
				//删除天梯
				tiantiService.deletePlayerTian(playerId);
			} catch (Exception e) {
				LogUtil.error("删除角色 -- 删除天梯信息错误",e);
			}
			
			try {
				//删除拍卖行
				tradeService.removePlayerTradeBag(playerId);
			} catch (Exception e) {
				LogUtil.error("删除角色 -- 删除拍卖行信息错误",e);
			}
			
			//删除缓存
			LogoutCacheService.deleteOnlineCache_one(playerId);
			LogoutCacheService.deleteOnlineCache_three(playerId);
		}
		
	}

	@Override
	public void quickTips(long playerId, long tipPlayerId) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		IPlayerService playerService = serviceCollection.getPlayerService();
		IFamilyService familyService = serviceCollection.getFamilyService();
		
		Player player = playerService.getPlayerByID(tipPlayerId);
		if(player == null) throw new GameException(ExceptionConstant.PLAYER_1115);		
		PlayerExt playerExt = playerService.getPlayerExtById(tipPlayerId);
		PlayerProperty playerProperty = playerService.getPlayerPropertyById(tipPlayerId);
		if(playerProperty == null) throw new GameException(ExceptionConstant.PLAYER_1115);
		
		S_QuickTips.Builder builder = S_QuickTips.newBuilder();		
		builder.setPlayerId(tipPlayerId);
		builder.setPlayerName(player.getPlayerName());
		builder.setCareer(player.getCareer());
		builder.setPlayerLevel(playerProperty.getLevel());
		builder.setTeamId(playerExt.getTeamId());
		
		int CREATE_FAMILY_LEVEL= serviceCollection.getCommonService().getConfigValue(ConfigConstant.CREATE_FAMILY_LEVEL);
		if(playerProperty.getLevel() >= CREATE_FAMILY_LEVEL){
			PlayerFamily playerFamily = familyService.getPlayerFamily(tipPlayerId);
			if(playerFamily != null && playerFamily.getPlayerFamilyId() > 0) {
				Family family = familyService.getFamily(playerFamily.getPlayerFamilyId());
				if(family != null){
					builder.setFamilyName(family.getFamilyName());	
				}		
			}		
		}
		
		MessageObj msg = new MessageObj(MessageID.S_QuickTips_VALUE, builder.build().toByteArray());
		serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);			
	}

	@Override
	public void synPlayerTitle(long playerId, int type, int sortId, String title) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		ISceneService sceneService = serviceCollection.getSceneService();
		
		PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerId);
		if(playerPuppet == null) return;
		
		S_SynPlayerTitle.Builder builder = S_SynPlayerTitle.newBuilder();
		builder.setGuid(playerPuppet.getGuid());
		builder.setType(type);
		builder.setSortId(sortId);
		builder.setTitle(title);
		
		List<Long> playerIds = sceneService.getNearbyPlayerIds(playerPuppet);
		gameSocketService.sendDataToPlayerList(playerIds, new MessageObj(
				MessageID.S_SynPlayerTitle_VALUE, builder.build().toByteArray()));		
	}

}
