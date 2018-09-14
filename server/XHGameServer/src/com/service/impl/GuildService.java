package com.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.eclipse.jetty.util.ConcurrentHashSet;

import com.cache.BaseCacheService;
import com.cache.CacheService;
import com.cache.CacheSynDBService;
import com.common.DateService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.LockService;
import com.common.ServiceCollection;
import com.constant.ActivityConstant;
import com.constant.CacheConstant;
import com.constant.CacheSynConstant;
import com.constant.ChatConstant;
import com.constant.ConfigConstant;
import com.constant.ExceptionConstant;
import com.constant.GuildConstant;
import com.constant.InOutLogConstant;
import com.constant.LockConstant;
import com.constant.PlayerConstant;
import com.constant.RewardConstant;
import com.constant.RewardTypeConstant;
import com.constant.SceneConstant;
import com.dao.guild.BaseGuildDAO;
import com.dao.guild.PlayerGuildDAO;
import com.domain.GameEntity;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.chat.Notice;
import com.domain.guild.BaseGuild;
import com.domain.guild.BaseGuildBuy;
import com.domain.guild.BaseGuildDonate;
import com.domain.guild.BaseGuildSkill;
import com.domain.guild.Guild;
import com.domain.guild.GuildBuy;
import com.domain.guild.GuildFight;
import com.domain.guild.GuildWar;
import com.domain.guild.PlayerGuild;
import com.domain.guild.Union;
import com.domain.map.BaseMap;
import com.domain.player.Player;
import com.domain.player.PlayerDaily;
import com.domain.player.PlayerExt;
import com.domain.player.PlayerProperty;
import com.domain.player.PlayerWealth;
import com.domain.puppet.MonsterPuppet;
import com.domain.puppet.PlayerPuppet;
import com.domain.reward.BaseReward;
import com.domain.weekactivity.BaseWeekActivity;
import com.message.ChatProto.ParamType;
import com.message.GuildProto.ApplyUnionMsg;
import com.message.GuildProto.DonateTimesMsg;
import com.message.GuildProto.GuildFightMsg;
import com.message.GuildProto.GuildMsg;
import com.message.GuildProto.GuildPlayerMsg;
import com.message.GuildProto.GuildSkillMsg;
import com.message.GuildProto.GuildWarMsg;
import com.message.GuildProto.S_GetDonateTimes;
import com.message.GuildProto.S_GetGuild;
import com.message.GuildProto.S_GetGuildFightData;
import com.message.GuildProto.S_GetGuildFights;
import com.message.GuildProto.S_GetGuildList;
import com.message.GuildProto.S_GetGuildPlayerList;
import com.message.GuildProto.S_GetGuildSkills;
import com.message.GuildProto.S_GetGuildWarList;
import com.message.GuildProto.S_GetManorData;
import com.message.GuildProto.S_GetRevenueData;
import com.message.GuildProto.S_GetUnions;
import com.message.GuildProto.S_OfferInviteJoin;
import com.message.GuildProto.UnionMsg;
import com.message.MessageProto.MessageEnum.MessageID;
import com.scene.SceneModel;
import com.service.IChatService;
import com.service.ICommonService;
import com.service.IGuildService;
import com.service.IMailService;
import com.service.IPlayerService;
import com.service.IPropertyService;
import com.service.IProtoBuilderService;
import com.service.IRewardService;
import com.service.ISceneService;
import com.service.IWeekActivityService;
import com.util.CommonUtil;
import com.util.IDUtil;
import com.util.LogUtil;
import com.util.PlayerUtil;
import com.util.ResourceUtil;
import com.util.SplitStringUtil;

/**
 * 帮会系统
 * @author ken
 * @date 2018年3月31日
 */
public class GuildService implements IGuildService {

	private BaseGuildDAO baseGuildDAO = new BaseGuildDAO();
	private PlayerGuildDAO playerGuildDAO = new PlayerGuildDAO();
	
	@Override
	public void initBaseCache() {
		Map<Integer, BaseGuild> map = new HashMap<Integer, BaseGuild>();
		List<BaseGuild> lists = baseGuildDAO.listBaseGuilds();
		for(BaseGuild model : lists){
			map.put(model.getLevel(), model);
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_GUILD, map);
		
		Map<Integer, BaseGuildDonate> donateMap = new HashMap<Integer, BaseGuildDonate>();
		List<BaseGuildDonate> listDanotes = baseGuildDAO.listBaseGuildDonates();
		for(BaseGuildDonate model : listDanotes){
			donateMap.put(model.getId(), model);
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_GUILD_DONATE, donateMap);
		
		Map<Integer, Map<Integer, BaseGuildSkill>> skillMap = new HashMap<Integer, Map<Integer,BaseGuildSkill>>();
		List<BaseGuildSkill> skills = baseGuildDAO.listBaseGuildSkills();
		for(BaseGuildSkill model : skills){
			model.setAddPropertyList(SplitStringUtil.getIntList(model.getAddProperty()));
			
			Map<Integer, BaseGuildSkill> map2 = skillMap.get(model.getType());
			if(map2 == null){
				map2 = new HashMap<Integer, BaseGuildSkill>();
				skillMap.put(model.getType(), map2);
			}
			map2.put(model.getLevel(), model);
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_GUILD_SKILL, skillMap);
		
		Map<Integer, BaseGuildBuy> buyMap = new HashMap<Integer, BaseGuildBuy>();
		List<BaseGuildBuy> buys = baseGuildDAO.listBaseGuildBuys();
		for(BaseGuildBuy model : buys){
			buyMap.put(model.getItemId(), model);
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_GUILD_BUY, buyMap);
	}

	@Override
	public void initCache() {
		List<GuildWar> guildWars = playerGuildDAO.listGuildWars();
		
		Map<Long, Guild> map = new ConcurrentHashMap<Long, Guild>();
		
		GuildFight guildFightData = playerGuildDAO.getGuildFight();
		if(guildFightData == null){
			guildFightData = new GuildFight();
			guildFightData.setId(IDUtil.geneteId(GuildFight.class));
			playerGuildDAO.createGuildFight(guildFightData);
		}
		
		List<Guild> guilds = playerGuildDAO.listGuilds();
		for(Guild model : guilds){
			
			List<PlayerGuild> playerGuilds = playerGuildDAO.listPlayerGuild(model.getGuildId());
			for(PlayerGuild pg : playerGuilds){
				CacheService.putToCache(CacheConstant.PLAYER_GUILD+pg.getPlayerId(), pg);
				
				model.getPlayerIds().offer(pg.getPlayerId());
				if(pg.getRoleId() == GuildConstant.ROLE_0){
					model.getTraineeIds().offer(pg.getPlayerId());
				}
			}
			
			for(GuildWar gw : guildWars){
				if(model.getGuildId() == gw.getGuildId()){
					model.getGuildWarMap().put(gw.getTargetGuildId(), gw);
				}else if(model.getGuildId() == gw.getTargetGuildId()){
					model.getGuildWarMap().put(gw.getGuildId(), gw);
				}
			}
			
			map.put(model.getGuildId(), model);
			
			if(model.isApplyFlag()){
				guildFightData.getApplySet().add(model.getGuildId());
			}
		}
		
		//帮派列表
		CacheService.putToCache(CacheConstant.GUILD_MAP, map);
		
		//城战数据
		CacheService.putToCache(CacheConstant.GUILD_FIGHT_DATA, guildFightData);
		
		//城战联盟列表
		Set<Union> unionSet = new ConcurrentHashSet<Union>();
		List<Union> unions = playerGuildDAO.listUnions();
		for(Union union : unions){
			unionSet.add(union);
		}
		CacheService.putToCache(CacheConstant.GUILD_UNION_LIST, unionSet);
		
		
		Map<Integer, GuildBuy> guildBuyMap = new ConcurrentHashMap<Integer, GuildBuy>();
		List<GuildBuy> guildBuys = playerGuildDAO.listGuildBuys();
		for(GuildBuy model : guildBuys){
			guildBuyMap.put(model.getItemId(), model);
		}
		CacheService.putToCache(CacheConstant.GUILD_BUY_MAP, guildBuyMap);
		
		guilds = null;
		unions = null;
		guildBuys = null;
	}

	@Override
	public void deleteCache(long playerId) {
		CacheService.deleteFromCache(CacheConstant.PLAYER_GUILD+playerId);
	}

	/**
	 * 根据等级取帮派配置
	 */
	@SuppressWarnings("unchecked")
	private BaseGuild getBaseGuild(int level){
		Map<Integer, BaseGuild> map = (Map<Integer, BaseGuild>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_GUILD);
		return map.get(level);
	}
	
	/**
	 * 根据编号取捐献配置
	 */
	@SuppressWarnings("unchecked")
	private BaseGuildDonate getBaseGuildDonate(int id){
		Map<Integer, BaseGuildDonate> map = (Map<Integer, BaseGuildDonate>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_GUILD_DONATE);
		return map.get(id);
	}
	
	/**
	 * 取帮派技能配置
	 */
	@SuppressWarnings("unchecked")
	private BaseGuildSkill getBaseGuildSkill(int type, int level){
		Map<Integer, Map<Integer, BaseGuildSkill>> skillMap = (Map<Integer, Map<Integer, BaseGuildSkill>>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_GUILD_SKILL);
		Map<Integer, BaseGuildSkill> map = skillMap.get(type);
		if(map != null){
			return map.get(level);
		}
		return null;
	}
	
	/**
	 * 获取优惠购买物品
	 */
	@SuppressWarnings("unchecked")
	private BaseGuildBuy getBaseGuildBuy(int itemId){
		Map<Integer, BaseGuildBuy> map = (Map<Integer, BaseGuildBuy>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_GUILD_BUY);
		return map.get(itemId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<Integer, GuildBuy> getGuidBuyMap() {
		return (Map<Integer, GuildBuy>)CacheService.getFromCache(CacheConstant.GUILD_BUY_MAP);
	}
	
	/**
	 * 获取优惠购买记录
	 */
	private GuildBuy getGuildBuy(int itemId){
		Map<Integer, GuildBuy> guildBuyMap = this.getGuidBuyMap();
		GuildBuy model = guildBuyMap.get(itemId);
		if(model == null){
			model = new GuildBuy();
			model.setItemId(itemId);
			model.setBuyNum(0);
			playerGuildDAO.createGuildBuy(model);
			guildBuyMap.put(itemId, model);
		}
		return model;
	}
	
	/**
	 * 同步缓存to数据库
	 */
	public void updateGuild(Guild guild){
		Set<GameEntity> lists =  CacheSynDBService.getFromFiveUpdateOneCache(CacheSynConstant.GUILD);
		if (!lists.contains(guild)) {
			lists.add(guild);
		}
	}
	
	/**
	 * 同步缓存to数据库
	 */
	private void updateGuildWar(GuildWar model){
		Set<GameEntity> lists =  CacheSynDBService.getFromFiveUpdateOneCache(CacheSynConstant.GUILD_WAR);
		if (!lists.contains(model)) {
			lists.add(model);
		}
	}
	
	/**
	 * 同步缓存to数据库
	 */
	private void updateGuildFight(GuildFight model){
		Set<GameEntity> lists =  CacheSynDBService.getFromFiveUpdateOneCache(CacheSynConstant.GUILD_FIGHT);
		if (!lists.contains(model)) {
			lists.add(model);
		}
	}
	
	/**
	 * 同步缓存to数据库
	 */
	private void updateGuildBuy(GuildBuy model){
		Set<GameEntity> lists =  CacheSynDBService.getFromFiveUpdateOneCache(CacheSynConstant.GUILD_BUY);
		if (!lists.contains(model)) {
			lists.add(model);
		}
	}
	
	/**
	 * 同步缓存to数据库
	 */
	public void updatePlayerGuild(PlayerGuild playerGuild){
		Set<GameEntity> lists =  CacheSynDBService.getFromFiveUpdateOneCache(CacheSynConstant.PLAYER_GUILD);
		if (!lists.contains(playerGuild)) {
			lists.add(playerGuild);
		}
	}
	
	@SuppressWarnings("unchecked")
	private Map<Long, Guild> getGuildMap(){
		return (Map<Long, Guild>)CacheService.getFromCache(CacheConstant.GUILD_MAP);
	}
	
	/**
	 * 根据帮派编号找出帮派信息
	 */
	public Guild getGuildById(long guildId){
		return this.getGuildMap().get(guildId);
	}
	
	@Override
	public String getGuildName(long playerId) {
		String guildName = "";
		PlayerGuild playerGuild = this.getPlayerGuild(playerId);
		if(playerGuild != null && playerGuild.getGuildId() > 0){
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild != null){
				guildName = guild.getGuildName();
			}
		}
		return guildName;
	}
	
	/**
	 * 根据名称找出帮派信息
	 */
	private Guild getGuildByName(String guildName){
		for(Map.Entry<Long, Guild> entry : this.getGuildMap().entrySet()){
			Guild model = entry.getValue();
			if(model.getGuildName().equalsIgnoreCase(guildName)){
				return model;
			}
		}
		return null;
	}
	
	/**
	 * 取玩家帮派信息
	 */
	public PlayerGuild getPlayerGuild(long playerId){
		PlayerGuild model = (PlayerGuild)CacheService.getFromCache(CacheConstant.PLAYER_GUILD+playerId);
		if(model == null){
			model = playerGuildDAO.getPlayerGuild(playerId);
			if(model != null){
				CacheService.putToCache(CacheConstant.PLAYER_GUILD+playerId, model);
			}
		}
		return model;
	}
	
	@Override
	public void getGuildList(long playerId) throws Exception {
 		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection(); 
 		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
 		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
 		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			boolean hasGuilded = false;
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild != null && playerGuild.getGuildId() > 0){
				hasGuilded = true;
			}
			S_GetGuildList.Builder builder = S_GetGuildList.newBuilder();
			for(Map.Entry<Long, Guild> entry : this.getGuildMap().entrySet()){
				Guild guild = entry.getValue();
				
				GuildMsg.Builder msg = protoBuilderService.buildGuildMsg(guild);
				//没有入帮情况下
				if(!hasGuilded){
					boolean applyFlag = guild.getApplyList().contains(playerId);
					msg.setApplyFlag(applyFlag ? 1 : 0);	
				}

				builder.addGuilds(msg);
			}
			
			MessageObj msg = new MessageObj(MessageID.S_GetGuildList_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}
	}
	
	@Override
	public void getGuildPlayerList(long playerId) throws Exception {
 		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection(); 
 		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
 		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);
			} 
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);	
			}
			
			S_GetGuildPlayerList.Builder builder = S_GetGuildPlayerList.newBuilder();
			
			GuildPlayerMsg.Builder playerMsg = protoBuilderService.buildGuildPlayerMsg(playerGuild);
			builder.addGuildPlayers(playerMsg);
			
			int onlineNum = 1;
			Iterator<Long> iterator =  guild.getPlayerIds().iterator();
			while(iterator.hasNext()){
				Long pid = iterator.next();
				PlayerGuild pg = this.getPlayerGuild(pid);
				if(pg == null || pg.getGuildId() < 1){
					iterator.remove();
					continue;
				} 
				if(pid.equals(playerId)) continue;
				
				GuildPlayerMsg.Builder playerMsg1 = protoBuilderService.buildGuildPlayerMsg(pg);
				builder.addGuildPlayers(playerMsg1);
				if(playerMsg1.getExitTime() <= 0){
					onlineNum++;
				}
			}
			builder.setOnlineNum(onlineNum);
			
			MessageObj msg = new MessageObj(MessageID.S_GetGuildPlayerList_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}
	}
	
	@Override
	public void createGuild(long playerId, String guildName, String notice) throws Exception {
 		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection(); 
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			
			if (guildName == null || "".equals(guildName.trim())) {
				throw new GameException(ExceptionConstant.GUILD_3700);
			}

			guildName = CommonUtil.replaceInput(guildName);

			if (guildName.length() < 4  || guildName.length() > 12) {
				throw new GameException(ExceptionConstant.GUILD_3701);
			}

			if (CommonUtil.checkInput(guildName)) {
				throw new GameException(ExceptionConstant.GUILD_3702);
			}
			
			if (notice == null || "".equals(notice.trim())) {
				throw new GameException(ExceptionConstant.GUILD_3705);
			}
			
			// 验证名称重复
			if (this.getGuildByName(guildName) != null) {
				throw new GameException(ExceptionConstant.GUILD_3703);
			}
			
			Player player = playerService.getPlayerByID(playerId);
			if(player == null) return;
			
			BaseGuild baseGuild = this.getBaseGuild(1);
			if(baseGuild == null) return;
			
			PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
			PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);
			
			// 所需等级
			int GUILD_NEED_LEVEL= serviceCollection.getCommonService().getConfigValue(ConfigConstant.GUILD_NEED_LEVEL);
			if (playerProperty.getLevel() < GUILD_NEED_LEVEL) {
				throw new GameException(ExceptionConstant.PLAYER_1110);
			}
			
			//所需元宝
			int GUILD_NEED_DIAMOND= serviceCollection.getCommonService().getConfigValue(ConfigConstant.GUILD_NEED_DIAMOND);
			if (playerWealth.getDiamond() < GUILD_NEED_DIAMOND) {
				throw new GameException(ExceptionConstant.PLAYER_1113);
			}
			
			//所需金币
			int GUILD_NEED_GOLD= serviceCollection.getCommonService().getConfigValue(ConfigConstant.GUILD_NEED_GOLD);
			if (playerWealth.getGold() < GUILD_NEED_GOLD) {
				throw new GameException(ExceptionConstant.PLAYER_1112);
			}
			
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild != null){
				if(playerGuild.getGuildId() > 0){
					throw new GameException(ExceptionConstant.GUILD_3704);
				}
			}
			
			Guild guild = new Guild();
			guild.setGuildId(IDUtil.geneteId(Guild.class));
			guild.setGuildName(guildName);
			guild.setNotice(notice);
			guild.setHeaderId(playerId);
			guild.setHeaderName(player.getPlayerName());
			guild.setLevel(1);
			guild.setMaxNum(baseGuild.getMaxNum());
			guild.setBattleValue(playerProperty.getBattleValue());
			guild.setAutoJoin(1);
			guild.setAutoMinLv(1);
			guild.setAutoMaxLv(100);
			guild.setMoney(baseGuild.getNeedMoney());
			guild.setBuildNum(baseGuild.getNeedBuildNum());
			guild.setCreateTime(new Date());
			playerGuildDAO.createGuild(guild);
			this.getGuildMap().put(guild.getGuildId(), guild);
			
			synchronized (guild.getLock()) {
				
				//{0}创建了都护府
				IChatService chatService = serviceCollection.getChatService();
				List<Notice> paramList = new ArrayList<Notice>();			
				Notice notice1 = new Notice(ParamType.PLAYER, 0, 0, player.getPlayerName());	
				paramList.add(notice1);			
				List<Long> pIds = new ArrayList<Long>();
				pIds.add(playerId);
				chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_27, paramList, pIds);
				
				
				// 消耗金币
				playerService.addGold_syn(playerId, -GUILD_NEED_GOLD);
				playerService.addDiamond_syn(playerId, -GUILD_NEED_DIAMOND, InOutLogConstant.DIAMOND_OF_11);
				
				this.addPlayerGuild(playerId, GuildConstant.ROLE_3, guild);
				
			}
		}
	}

	/**
	 * 进入帮派
	 */
	private void addPlayerGuild(long playerId, int role, Guild guild){
 		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection(); 
 		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IPlayerService playerService = serviceCollection.getPlayerService();
 		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
 		
 		Player player = playerService.getPlayerByID(playerId);
 		
		PlayerGuild playerGuild = this.getPlayerGuild(playerId);
		if(playerGuild == null){
			playerGuild = new PlayerGuild();
			playerGuild.setPlayerId(playerId);
			playerGuild.setGuildId(guild.getGuildId());
			playerGuild.setRoleId(role);
			playerGuild.setJoinTime(new Date());
			playerGuildDAO.createPlayerGuild(playerGuild);
			CacheService.putToCache(CacheConstant.PLAYER_GUILD+playerId, playerGuild);
			
		}else{
			playerGuild.setGuildId(guild.getGuildId());
			playerGuild.setRoleId(role);
			playerGuild.setJoinTime(new Date());
			this.updatePlayerGuild(playerGuild);
		}
		
		//加入成员列表
		guild.getPlayerIds().offer(playerId);
		
		if(role == GuildConstant.ROLE_0){
			//加入见习成员列表
			guild.getTraineeIds().offer(playerId);
			//重新计算战力
			this.calGuildBattleValue(guild);
		}
		
		if(gameSocketService.checkOnLine(playerId)){
			//下发帮派信息
			S_GetGuild.Builder builder = S_GetGuild.newBuilder();
			builder.setGuild(protoBuilderService.buildGuildMsg(guild));
			builder.setRoleId(playerGuild.getRoleId());
			builder.setContribution(playerGuild.getContribution());
			MessageObj msg = new MessageObj(MessageID.S_GetGuild_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByUserId(player.getUserId(), msg);
			
			//同步帮派名称显示
			playerService.synPlayerTitle(playerId, PlayerConstant.PLAYER_TITLE_TYPE_2, role, guild.getGuildName());
		}else{
			serviceCollection.getMailService().systemSendMail(playerId, ResourceUtil.getValue("guild"), ResourceUtil.getValue("guild_join", guild.getGuildName()), "", 0);
		}
		
		// {0}加入了都护府
		IChatService chatService = serviceCollection.getChatService();
		List<Notice> paramList = new ArrayList<Notice>();			
		Notice notice1 = new Notice(ParamType.PLAYER, playerId, 0, player.getPlayerName());	
		paramList.add(notice1);			
		List<Long> pIds = new ArrayList<Long>();
		pIds.addAll(guild.getPlayerIds());
		chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_28, paramList, pIds);
		

	}
	
	/**
	 * 退出帮派
	 * @param type 1:自己退出  2：被踢出  3：转正失败 4：解散
	 */
	private void resetPlayerGuid(Guild guild, PlayerGuild playerGuild, int type){
 		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection(); 
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		Player player = playerService.getPlayerByID(playerGuild.getPlayerId());
		// {0}离开了都护府
		IChatService chatService = serviceCollection.getChatService();
		List<Notice> paramList = new ArrayList<Notice>();			
		Notice notice1 = new Notice(ParamType.PLAYER, player.getPlayerId(), 0, player.getPlayerName());	
		paramList.add(notice1);			
		List<Long> pIds = new ArrayList<Long>();
		pIds.addAll(guild.getPlayerIds());
		
		switch (type) {
		case 1:
			chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_29, paramList, pIds);
			break;
		case 2:
			chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_30, paramList, pIds);
			break;
		case 3:
			chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_38, paramList, pIds);
			break;
		}
		
		playerGuild.setGuildId(0);
		playerGuild.setJoinTime(null);
		playerGuild.setRoleId(0);
		playerGuild.setTicket(0);
		playerGuild.setContribution(0);
		playerGuild.setWeekBuildNum(0);
		playerGuild.setWeekMoney(0);
		this.updatePlayerGuild(playerGuild);
		
		Long playerId = playerGuild.getPlayerId();
		guild.getPlayerIds().remove(playerId);
	
		//同步帮派名称显示
		playerService.synPlayerTitle(playerId, PlayerConstant.PLAYER_TITLE_TYPE_2, 0, "");
		
		if(type < 3){
			if(playerGuild.getRoleId() == GuildConstant.ROLE_0){
				guild.getTraineeIds().remove(playerId);		
				
			}else if(playerGuild.getRoleId() == GuildConstant.ROLE_2){
				guild.setAssistantNum(guild.getAssistantNum() - 1);
			}
			
			//重新计算战力
			this.calGuildBattleValue(guild);	
		}
		
	}
	
	/**
	 * 删除帮派信息
	 */
	private void deleteGuid(Guild guild){
 		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection(); 
 		ISceneService sceneService = serviceCollection.getSceneService();
 		
		guild.setDeleteFlag(1);
		guild.getApplyList().clear();
		guild.getPlayerIds().clear();
		guild.getTraineeIds().clear();
		guild.getSkillMap().clear();
		
		//清理宣战信息
		for(Map.Entry<Long, GuildWar> entry : guild.getGuildWarMap().entrySet()){
			GuildWar model = entry.getValue();
			model.setDeleteFlag(1);
			this.updateGuildWar(model);
			
			Guild targetGuild = this.getGuildById(entry.getKey());
			if(targetGuild != null){
				targetGuild.getGuildWarMap().remove(guild.getGuildId());
			}
		}
		guild.getGuildWarMap().clear();
		guild.getItemMap().clear();
		
		if(guild.isDefend()){
			GuildFight guildFight = this.getGuildFightCache();
			guildFight.setGuildId(0);
			guildFight.setDefendName("");
		}
		
		String sceneGuid = PlayerUtil.getSceneGuid(SceneConstant.TYPE_GUILD, guild.getGuildId());
		SceneModel sceneModel = sceneService.getSceneModel(sceneGuid);
		if(sceneModel != null){
			sceneService.destroy(sceneModel);
		}
		this.updateGuild(guild);
	}
	
	@Override
	public void modifyNotice(long playerId, String notice) throws Exception {
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			if (notice == null || "".equals(notice.trim())) {
				throw new GameException(ExceptionConstant.GUILD_3705);
			}
			
			if (notice.length() > 100) {
				throw new GameException(ExceptionConstant.GUILD_3727);
			}
			
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);
			} 
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);	
			}
			
			if(playerGuild.getRoleId() < GuildConstant.ROLE_2){
				throw new GameException(ExceptionConstant.GUILD_3710);
			}
			
			notice = notice.replace("'", "");
			notice = notice.replace("%", "");
			if (CommonUtil.checkSensitive(notice)) {
				throw new GameException(ExceptionConstant.GUILD_3728);
			}
			guild.setNotice(notice);
			this.updateGuild(guild);
		}
	}

	@Override
	public void applyGuild(long playerId, long guildId) throws Exception {
 		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection(); 
		IPlayerService playerService = serviceCollection.getPlayerService();
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			
			Guild guild = this.getGuildById(guildId);
			if(guild == null) throw new GameException(ExceptionConstant.GUILD_3706);
			
			//判断人数是否满
			if(guild.getPlayerIds().size() >= guild.getMaxNum()) 
				throw new GameException(ExceptionConstant.GUILD_3708);
			
			// 所需等级
			int GUILD_NEED_LEVEL= serviceCollection.getCommonService().getConfigValue(ConfigConstant.GUILD_NEED_LEVEL);
			PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
			if (playerProperty.getLevel() < GUILD_NEED_LEVEL) {
				throw new GameException(ExceptionConstant.PLAYER_1110);
			}
			
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild != null){
				if(playerGuild.getGuildId() > 0){
					throw new GameException(ExceptionConstant.GUILD_3704);
				}
			}
			
			LinkedBlockingQueue<Long> lists = guild.getApplyList();
			if(lists.contains(playerId)){
				throw new GameException(ExceptionConstant.GUILD_3707);	
			}
			
			if(guild.getAutoJoin() == 1){
				if(playerProperty.getLevel() >= guild.getAutoMinLv() 
						&& playerProperty.getLevel() <= guild.getAutoMaxLv()){
					//自动加入
					this.addPlayerGuild(playerId, GuildConstant.ROLE_0, guild);
					return;
				}
			}
			
			//加入申请列表
			lists.offer(playerId);
		}
		
	}
	

	@Override
	public List<Long> quickApply(long playerId) throws Exception {
 		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection(); 
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild != null){
				if(playerGuild.getGuildId() > 0){
					throw new GameException(ExceptionConstant.GUILD_3704);
				}
			}
			
			PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
			// 所需等级
			int GUILD_NEED_LEVEL= serviceCollection.getCommonService().getConfigValue(ConfigConstant.GUILD_NEED_LEVEL);
			if (playerProperty.getLevel() < GUILD_NEED_LEVEL) {
				throw new GameException(ExceptionConstant.PLAYER_1110);
			}
			
			List<Guild> tempGuilds = new ArrayList<Guild>();
			List<Long> applyGuilds = new ArrayList<Long>();
			for(Map.Entry<Long, Guild> entry : this.getGuildMap().entrySet()){
				Guild model = entry.getValue();
				if(model.getPlayerIds().size() >= model.getMaxNum()) continue;
				
				if(model.getApplyList().contains(playerId)) continue;
				
				if(model.getAutoJoin() == 1){
					if(playerProperty.getLevel() >= model.getAutoMinLv() 
							&& playerProperty.getLevel() <= model.getAutoMaxLv()){
						//自动加入
						this.addPlayerGuild(playerId, GuildConstant.ROLE_0, model);
						
						tempGuilds = null;
						return applyGuilds;
					}
				}
				
				tempGuilds.add(model);
			}
			
			for(Guild model : tempGuilds){
				model.getApplyList().offer(playerId);
				
				applyGuilds.add(model.getGuildId());
			}
			
			tempGuilds = null;
			return applyGuilds;
		}
	}

	@Override
	public List<Long> getApplyList(long playerId) throws Exception {
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);
			} 
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);	
			}
			
			List<Long> playerIds = new ArrayList<Long>();
			playerIds.addAll(guild.getApplyList());
			return playerIds;
		}
	}
	
	@Override
	public void agreeApply(long playerId, Long applyId) throws Exception {
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);
			}
			
			if(playerGuild.getRoleId() < GuildConstant.ROLE_2){
				throw new GameException(ExceptionConstant.GUILD_3710);
			}
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			synchronized (guild.getLock()) {
				if(guild.getPlayerIds().size() >= guild.getMaxNum()){
					throw new GameException(ExceptionConstant.GUILD_3708);	
				}
				
				if(!guild.getApplyList().contains(applyId)){
					throw new GameException(ExceptionConstant.GUILD_3711);
				}else{
					//删除申请记录
					guild.getApplyList().remove(applyId);
				}
				
				PlayerGuild applyGuild = this.getPlayerGuild(applyId);
				if(applyGuild != null){
					if(applyGuild.getGuildId() > 0){
						throw new GameException(ExceptionConstant.GUILD_3711);	
					}
				}

				//进入帮派
				this.addPlayerGuild(applyId, GuildConstant.ROLE_0, guild);
			}

		}
	}

	@Override
	public void refuseApply(long playerId, long applyId) throws Exception {
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);
			}
			
			if(playerGuild.getRoleId() < GuildConstant.ROLE_2){
				throw new GameException(ExceptionConstant.GUILD_3710);
			}
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			
			if(!guild.getApplyList().contains(applyId)){
				throw new GameException(ExceptionConstant.GUILD_3711);
			}else{
				//删除申请记录
				guild.getApplyList().remove();
			}
		}
	}
	
	@Override
	public void clearApplys(long playerId) throws Exception{
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);
			}
			
			if(playerGuild.getRoleId() < GuildConstant.ROLE_2){
				throw new GameException(ExceptionConstant.GUILD_3710);
			}
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			
			guild.getApplyList().clear();
		}
	}

	@Override
	public void autoAgreeApply(long playerId, int selected, int autoMinLv, int autoMaxLv) throws Exception {
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			
			if(autoMinLv > autoMaxLv || autoMinLv < 1 || autoMaxLv > 100){
				throw new GameException(ExceptionConstant.ERROR_10000);
			}
			
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);
			}
			
			if(playerGuild.getRoleId() < GuildConstant.ROLE_2){
				throw new GameException(ExceptionConstant.GUILD_3710);
			}
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			
			if(guild.getAutoJoin() == selected && guild.getAutoMinLv() == autoMinLv
					&& guild.getAutoMaxLv() == autoMaxLv){
				throw new GameException(ExceptionConstant.GUILD_3722);
			}
			
			if(selected == 1){
				guild.setAutoJoin(selected);
				guild.setAutoMinLv(autoMinLv);
				guild.setAutoMaxLv(autoMaxLv);
			}else{
				guild.setAutoJoin(selected);
			}
			this.updateGuild(guild);
		}
	}
	
	@Override
	public void inviteJoin(long playerId, long invitedId) throws Exception {
 		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection(); 
 		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);
			}
			
			if(playerGuild.getRoleId() < GuildConstant.ROLE_2){
				throw new GameException(ExceptionConstant.GUILD_3710);
			}
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			
			if(guild.getPlayerIds().size() >= guild.getMaxNum()){
				throw new GameException(ExceptionConstant.GUILD_3708);	
			}
			
			if(!gameSocketService.checkOnLine(invitedId)){
				throw new GameException(ExceptionConstant.PLAYER_1111);
			}
			
			PlayerProperty inviteProperty = playerService.getPlayerPropertyById(invitedId);
			// 所需等级
			int GUILD_NEED_LEVEL= serviceCollection.getCommonService().getConfigValue(ConfigConstant.GUILD_NEED_LEVEL);
			if (inviteProperty.getLevel() < GUILD_NEED_LEVEL) {
				throw new GameException(ExceptionConstant.PLAYER_1110);
			}
			
			PlayerGuild invitedGuild = this.getPlayerGuild(invitedId);
			if(invitedGuild != null){
				if(invitedGuild.getGuildId() > 0){
					throw new GameException(ExceptionConstant.GUILD_3711);	
				}
			}
			
			// 通知被邀请者
			Player player = playerService.getPlayerByID(playerId);
			S_OfferInviteJoin.Builder builder = S_OfferInviteJoin.newBuilder();
			builder.setGuildId(guild.getGuildId());
			builder.setGuildName(guild.getGuildName());
			builder.setInviteId(playerId);
			builder.setInviteName(player.getPlayerName());
			MessageObj msg = new MessageObj(MessageID.S_OfferInviteJoin_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(invitedId, msg);
		}
		
	}

	@Override
	public void agreeInvite(long playerId, long guildId) throws Exception {
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild != null){
				if(playerGuild.getGuildId() > 0){
					throw new GameException(ExceptionConstant.GUILD_3704);	
				}
			}
			
			Guild guild = this.getGuildById(guildId);
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			synchronized (guild.getLock()) {
				LinkedBlockingQueue<Long> playerIds = guild.getPlayerIds();
				if(playerIds.size() >= guild.getMaxNum()){
					throw new GameException(ExceptionConstant.GUILD_3708);	
				}
				
				this.addPlayerGuild(playerId, GuildConstant.ROLE_0, guild);
			}

		}
	}

	@Override
	public void refuseInvite(long playerId, long guildId) throws Exception {
// 		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection(); 
// 		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
//		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
//			
//		}
	}

	@Override
	public void quitGuild(Long playerId) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection(); 
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);		
			}
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			
			if(playerGuild.getTicket() > 0){
				throw new GameException(ExceptionConstant.GUILD_3713);
			}
			
			//帮派活动中不可退出
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			
			BaseMap baseMap = serviceCollection.getSceneService().getBaseMap(playerExt.getMapId());
			if(baseMap.isGuild()) throw new GameException(ExceptionConstant.GUILD_3752);
			
			if(guild.isDefend()){
				GuildFight guildFight = this.getGuildFightCache();
				if(guildFight.getState() > 0){
					throw new GameException(ExceptionConstant.GUILD_3752);
				}
			}
			
			synchronized (guild.getLock()) {
				if(guild.getPlayerIds().size() > 1){
					if(playerGuild.getRoleId() == GuildConstant.ROLE_3){
						throw new GameException(ExceptionConstant.GUILD_3712);	
					}
				}
				this.resetPlayerGuid(guild, playerGuild, 1);
				
				if(guild.getPlayerIds().size() == 0){
					this.deleteGuid(guild);
					this.getGuildMap().remove(guild.getGuildId());
				}
			}
		}
	}

	@Override
	public void changeGuilder(long playerId, long targetId) throws Exception {
 		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection(); 
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);		
			}
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			
			if(playerGuild.getRoleId() != GuildConstant.ROLE_3){
				throw new GameException(ExceptionConstant.GUILD_3714);
			}
			
			if(playerId == targetId){
				throw new GameException(ExceptionConstant.GUILD_3716);
			}
			
			Player target = playerService.getPlayerByID(targetId);
			if(target == null){
				throw new GameException(ExceptionConstant.PLAYER_1115);
			}
			
			PlayerGuild targetGuild = this.getPlayerGuild(targetId);
			if(targetGuild == null || targetGuild.getGuildId() < 1 
					|| targetGuild.getGuildId() != playerGuild.getGuildId()){
				throw new GameException(ExceptionConstant.GUILD_3711);		
			}
			
			//帮派活动中不可转让
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			BaseMap baseMap = serviceCollection.getSceneService().getBaseMap(playerExt.getMapId());
			if(baseMap.isGuild()) throw new GameException(ExceptionConstant.GUILD_3752);
			
			int day = DateService.difDate(targetGuild.getJoinTime(), new Date());
			if(day <= 3)throw new GameException(ExceptionConstant.GUILD_3715);
			
			guild.setHeaderId(targetId);
			guild.setHeaderName(target.getPlayerName());
			this.updateGuild(guild);
			
			playerGuild.setRoleId(GuildConstant.ROLE_1);
			this.updatePlayerGuild(playerGuild);
			
			targetGuild.setRoleId(GuildConstant.ROLE_3);
			this.updatePlayerGuild(targetGuild);

			//{0}成为了新的【都护】
			IChatService chatService = serviceCollection.getChatService();
			List<Notice> paramList = new ArrayList<Notice>();			
			Notice notice1 = new Notice(ParamType.PLAYER, targetId, 0, target.getPlayerName());	
			paramList.add(notice1);			
			List<Long> pIds = new ArrayList<Long>();
			pIds.addAll(guild.getPlayerIds());
			chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_31, paramList, pIds);
		}
	}

	@Override
	public void kickGuild(long playerId, Long targetId) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection(); 
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);		
			}
			
			if(playerGuild.getRoleId() < GuildConstant.ROLE_2){
				throw new GameException(ExceptionConstant.GUILD_3710);
			}
			
			if(playerId == targetId){
				throw new GameException(ExceptionConstant.GUILD_3717);
			}
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			
			PlayerGuild targetGuild = this.getPlayerGuild(targetId);
			if(targetGuild == null || targetGuild.getGuildId() < 1 
					|| targetGuild.getGuildId() != playerGuild.getGuildId()){
				throw new GameException(ExceptionConstant.GUILD_3711);		
			}
			
			if(playerGuild.getRoleId() <= targetGuild.getRoleId()){
				throw new GameException(ExceptionConstant.GUILD_3710);
			}
			
			//帮派活动中不可踢出
			PlayerExt targetExt = playerService.getPlayerExtById(targetId);
			BaseMap baseMap = serviceCollection.getSceneService().getBaseMap(targetExt.getMapId());
			if(baseMap.isGuild()) throw new GameException(ExceptionConstant.GUILD_3752);
			
			if(targetGuild.getTicket() > 0){
				throw new GameException(ExceptionConstant.GUILD_3718);
			}
			
			synchronized (guild.getLock()) {
				this.resetPlayerGuid(guild, targetGuild, 2);
			}
		}
	}

	@Override
	public void changeGuildRole(Long playerId, Long targetId, int newRoleId) throws Exception {
 		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection(); 
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			if(newRoleId < 0 || newRoleId > GuildConstant.ROLE_2){
				throw new GameException(ExceptionConstant.GUILD_3710);
			}
				
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);		
			}
			
			if(playerGuild.getRoleId() < GuildConstant.ROLE_2){
				throw new GameException(ExceptionConstant.GUILD_3710);
			}
			
			if(playerId.equals(targetId)){
				throw new GameException(ExceptionConstant.GUILD_3719);
			}
			
			Player target = playerService.getPlayerByID(targetId);
			if(target == null){
				throw new GameException(ExceptionConstant.PLAYER_1115);
			}
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			
			PlayerGuild targetGuild = this.getPlayerGuild(targetId);
			if(targetGuild == null || targetGuild.getGuildId() < 1 
					|| targetGuild.getGuildId() != playerGuild.getGuildId()){
				throw new GameException(ExceptionConstant.GUILD_3711);		
			}
			
			if(targetGuild.getRoleId() == newRoleId){
				throw new GameException(ExceptionConstant.GUILD_3720);
			}
			
			if(playerGuild.getRoleId() <= targetGuild.getRoleId()){
				throw new GameException(ExceptionConstant.GUILD_3710);
			}
			
			if(newRoleId >= playerGuild.getRoleId()){
				throw new GameException(ExceptionConstant.GUILD_3710);
			}
			
			BaseGuild baseGuild = this.getBaseGuild(guild.getLevel());
			if(newRoleId == GuildConstant.ROLE_2){
				if(guild.getAssistantNum() >= baseGuild.getAssistantNum()){
					throw new GameException(ExceptionConstant.GUILD_3721);
				}
				guild.setAssistantNum(guild.getAssistantNum() + 1);
				this.updateGuild(guild);
			}else{
				if(targetGuild.getRoleId() == GuildConstant.ROLE_2){
					guild.setAssistantNum(guild.getAssistantNum() - 1);
					this.updateGuild(guild);
				}
			}
			if(targetGuild.getRoleId() == GuildConstant.ROLE_0){
				guild.getTraineeIds().remove(targetId);
			}
			targetGuild.setRoleId(newRoleId);
			this.updatePlayerGuild(targetGuild);
			
			//{0}被任免为【{1}】
			IChatService chatService = serviceCollection.getChatService();
			List<Notice> paramList = new ArrayList<Notice>();			
			Notice notice1 = new Notice(ParamType.PLAYER, targetId, 0, target.getPlayerName());	
			Notice notice2 = new Notice(ParamType.PARAM, 0, 0, this.getRoleName(newRoleId));
			paramList.add(notice1);
			paramList.add(notice2);
			List<Long> pIds = new ArrayList<Long>();
			pIds.addAll(guild.getPlayerIds());
			chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_32, paramList, pIds);
		}
	}

	/**
	 * 帮派职位
	 */
	public String getRoleName(int roleId){
		switch (roleId) {
		case GuildConstant.ROLE_0:
			
			return ResourceUtil.getValue("guild_role_0");
		case GuildConstant.ROLE_1:
			
			return ResourceUtil.getValue("guild_role_1");
		case GuildConstant.ROLE_2:
			
			return ResourceUtil.getValue("guild_role_2");
		case GuildConstant.ROLE_3:
			
			return ResourceUtil.getValue("guild_role_3");
		}
		return "";
	}
	/**
	 * 计算帮派战力
	 */
	private void calGuildBattleValue(Guild guild){
 		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection(); 
 		IPlayerService playerService = serviceCollection.getPlayerService();
 		
		int value = 0;
		for(Long pid : guild.getPlayerIds()){
			PlayerProperty playerProperty = playerService.getPlayerPropertyById(pid);
			if(playerProperty == null){
				continue;
			}
			
			value += playerProperty.getBattleValue();
		}
		
		guild.setBattleValue(value);
		this.updateGuild(guild);
	}

	@Override
	public void upgradeGuild(long playerId) throws Exception {
 		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection(); 
 		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
 		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
 		IPlayerService playerService = serviceCollection.getPlayerService();
 		
		PlayerGuild playerGuild = this.getPlayerGuild(playerId);
		if(playerGuild == null || playerGuild.getGuildId() < 1){
			throw new GameException(ExceptionConstant.GUILD_3709);		
		}
		
		if(playerGuild.getRoleId() < GuildConstant.ROLE_2){
			throw new GameException(ExceptionConstant.GUILD_3710);
		}
		
		Guild guild = this.getGuildById(playerGuild.getGuildId());
		if(guild == null){
			throw new GameException(ExceptionConstant.GUILD_3706);
		}
	
		Player player = playerService.getPlayerByID(playerId);
		
		synchronized (guild.getOprLock()) {
			int GUILD_MAX_LEVEL= serviceCollection.getCommonService().getConfigValue(ConfigConstant.GUILD_MAX_LEVEL);
			
			if(guild.getLevel() >= GUILD_MAX_LEVEL){
				throw new GameException(ExceptionConstant.GUILD_3723);
			}
			
			BaseGuild baseGuild = this.getBaseGuild(guild.getLevel() + 1);
			if(baseGuild == null){
				throw new GameException(ExceptionConstant.GUILD_3723);
			} 
			
			if(guild.getMoney() < baseGuild.getNeedMoney()){
				throw new GameException(ExceptionConstant.GUILD_3724);
			}
			
			if(guild.getBuildNum() < baseGuild.getNeedBuildNum()){
				throw new GameException(ExceptionConstant.GUILD_3725);
			}
			
			guild.setMoney(guild.getMoney() - baseGuild.getNeedMoney());
			guild.setBuildNum(guild.getBuildNum() - baseGuild.getNeedBuildNum());
			
			guild.setLevel(guild.getLevel() + 1);
			guild.setMaxNum(baseGuild.getMaxNum());
			
			this.updateGuild(guild);
			
			//下发帮派信息
			S_GetGuild.Builder builder = S_GetGuild.newBuilder();
			builder.setGuild(protoBuilderService.buildGuildMsg(guild));
			builder.setRoleId(playerGuild.getRoleId());
			builder.setContribution(playerGuild.getContribution());
			MessageObj msg = new MessageObj(MessageID.S_GetGuild_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByUserId(player.getUserId(), msg);
			
			//{0}将都护府等级升为：{1}
			IChatService chatService = serviceCollection.getChatService();
			List<Notice> paramList = new ArrayList<Notice>();			
			Notice notice1 = new Notice(ParamType.PLAYER, playerId, 0, player.getPlayerName());	
			Notice notice2 = new Notice(ParamType.PARAM, 0, 0, guild.getLevel()+"");
			paramList.add(notice1);
			paramList.add(notice2);
			List<Long> pIds = new ArrayList<Long>();
			pIds.addAll(guild.getPlayerIds());
			chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_35, paramList, pIds);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void getDonateTimes(long playerId) throws Exception {
 		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection(); 
 		IPlayerService playerService = serviceCollection.getPlayerService();
 		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
 		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			S_GetDonateTimes.Builder builder = S_GetDonateTimes.newBuilder();
			
			PlayerDaily playerDaily = playerService.getPlayerDailyById(playerId);
			Map<Integer, BaseGuildDonate> map = (Map<Integer, BaseGuildDonate>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_GUILD_DONATE);
			
			for(Integer id : map.keySet()){
				int num = 0;
				switch (id) {
				case 1:
					num = playerDaily.getDonate1Times();
					break;
				case 2:
					num = playerDaily.getDonate2Times();
					break;
				case 3:
					num = playerDaily.getDonate3Times();
					break;
				case 4:
					num = playerDaily.getDonate4Times();
					break;
				}
				DonateTimesMsg.Builder msg = DonateTimesMsg.newBuilder();
				msg.setId(id);
				msg.setTimes(num);
				builder.addDonateTimes(msg);
			}
			MessageObj msg = new MessageObj(MessageID.S_GetDonateTimes_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
			
		}
	}
	
	@Override
	public void donate(Long playerId, int id) throws Exception {
 		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection(); 
 		IPlayerService playerService = serviceCollection.getPlayerService();
		IChatService chatService = serviceCollection.getChatService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			BaseGuildDonate baseGuildDonate = this.getBaseGuildDonate(id);
			if(baseGuildDonate == null){
				throw new GameException(ExceptionConstant.ERROR_10000);	
			}
			
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);		
			}
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			
			if(baseGuildDonate.getLimitTimes() > 0){
				PlayerDaily playerDaily = playerService.getPlayerDailyById(playerId);
				int num = 0;
				switch (baseGuildDonate.getId()) {
				case 1:
					num = playerDaily.getDonate1Times();
					break;
				case 2:
					num = playerDaily.getDonate2Times();
					break;
				case 3:
					num = playerDaily.getDonate3Times();
					break;
				case 4:
					num = playerDaily.getDonate4Times();
					break;
				}
				if(num >= baseGuildDonate.getLimitTimes()){
					throw new GameException(ExceptionConstant.GUILD_3729);
				}
			}
			
			Player player = playerService.getPlayerByID(playerId);
			PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);
			
			if(baseGuildDonate.getMoneyType() == RewardTypeConstant.MONEY){
				//所需金币
				if (playerWealth.getGold() < baseGuildDonate.getValue()) {
					throw new GameException(ExceptionConstant.PLAYER_1112);
				}
				playerService.addGold_syn(playerId, -baseGuildDonate.getValue());

			}else if(baseGuildDonate.getMoneyType() == RewardTypeConstant.DIAMOND){
				//所需元宝
				if (playerWealth.getDiamond() < baseGuildDonate.getValue()) {
					throw new GameException(ExceptionConstant.PLAYER_1113);
				}
				
				playerService.addDiamond_syn(playerId, -baseGuildDonate.getValue(), InOutLogConstant.DIAMOND_OF_12);
			}
	
			if(baseGuildDonate.getLimitTimes() > 0){
				PlayerDaily playerDaily = playerService.getPlayerDailyById(playerId);
				switch (baseGuildDonate.getId()) {
				case 1:
					playerDaily.setDonate1Times(playerDaily.getDonate1Times() + 1);
					break;
				case 2:
					playerDaily.setDonate2Times(playerDaily.getDonate2Times() + 1);
					break;
				case 3:
					playerDaily.setDonate3Times(playerDaily.getDonate3Times() + 1);
					break;
				case 4:
					playerDaily.setDonate4Times(playerDaily.getDonate4Times() + 1);
					break;
				}
				playerService.updatePlayerDaily(playerDaily);
			}
			
			playerGuild.setWeekBuildNum(playerGuild.getWeekBuildNum() + baseGuildDonate.getBuildNum());
			playerGuild.setWeekMoney(playerGuild.getWeekMoney() + baseGuildDonate.getMoney());
			playerGuild.setContribution(playerGuild.getContribution() + baseGuildDonate.getContribution());
			
			List<Long> pIds = new ArrayList<Long>();
			pIds.addAll(guild.getPlayerIds());
			
			//{0}捐献了{1}元宝
			if(baseGuildDonate.getMoneyType() == RewardTypeConstant.DIAMOND){
				List<Notice> paramList = new ArrayList<Notice>();			
				Notice notice3 = new Notice(ParamType.PLAYER, playerId, 0, player.getPlayerName());	
				Notice notice4 = new Notice(ParamType.PARAM, 0, 0, baseGuildDonate.getValue()+"");
				paramList.add(notice3);
				paramList.add(notice4);
				chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_34, paramList, pIds);
			}
			
			//贡献足够， 转正
			if(playerGuild.getRoleId() == GuildConstant.ROLE_0){
				int GUILD_PROMOTE= serviceCollection.getCommonService().getConfigValue(ConfigConstant.GUILD_PROMOTE);
				
				if(playerGuild.getContribution() >= GUILD_PROMOTE){
					playerGuild.setRoleId(GuildConstant.ROLE_1);
					guild.getTraineeIds().remove(playerId);
					
					//{0}见习期表现优秀，转正为【{1}】
					List<Notice> paramList = new ArrayList<Notice>();			
					Notice notice1 = new Notice(ParamType.PLAYER, playerId, 0, player.getPlayerName());	
					Notice notice2 = new Notice(ParamType.PARAM, 0, 0, this.getRoleName(GuildConstant.ROLE_1));
					paramList.add(notice1);
					paramList.add(notice2);
					chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_33, paramList, pIds);
				}
			}
			this.updatePlayerGuild(playerGuild);
			
			guild.setBuildNum(guild.getBuildNum() + baseGuildDonate.getBuildNum());
			guild.setMoney(guild.getMoney() + baseGuildDonate.getMoney());
			this.updateGuild(guild);
			
		}
	}

	@Override
	public void getGuildWarList(long playerId) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection(); 
 		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
 		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
 		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);
			}
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			
			long curTime = System.currentTimeMillis();
			S_GetGuildWarList.Builder builder = S_GetGuildWarList.newBuilder();
			for(Map.Entry<Long, Guild> entry : this.getGuildMap().entrySet()){
				Guild targetGuild = entry.getValue();
				
				if(targetGuild.getGuildId() == playerGuild.getGuildId()) continue;
				
				GuildWarMsg.Builder msg = protoBuilderService.buildGuildWarMsg(targetGuild);
				
				GuildWar targetGuildWar= targetGuild.getGuildWarMap().get(playerGuild.getGuildId());
				if(targetGuildWar != null){
					long endWarTime = targetGuildWar.getEndWarTime().getTime();
					if(curTime > endWarTime-3000){
						//已经结束宣战状态
						targetGuildWar.setDeleteFlag(1);
						this.updateGuildWar(targetGuildWar);
						targetGuild.getGuildWarMap().remove(playerGuild.getGuildId());
						
						guild.getGuildWarMap().remove(entry.getKey());
					}else{
						msg.setEndWarTime(endWarTime);
					}
				}
				
				builder.addGuilds(msg);
			}
			
			MessageObj msg = new MessageObj(MessageID.S_GetGuildWarList_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}
	}

	@Override
	public long guildWar(long playerId, long guildId) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection(); 
		IChatService chatService = serviceCollection.getChatService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);
			}
			
			if(playerGuild.getGuildId() == guildId){
				throw new GameException(ExceptionConstant.ERROR_10000);
			}
			
			if(playerGuild.getRoleId() < GuildConstant.ROLE_2){
				throw new GameException(ExceptionConstant.GUILD_3710);
			}
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			
			Guild targetGuild = this.getGuildById(guildId);
			if(targetGuild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			
			int GUILD_WAR_MONEY= serviceCollection.getCommonService().getConfigValue(ConfigConstant.GUILD_WAR_MONEY);
			
			if(guild.getMoney() < GUILD_WAR_MONEY){
				throw new GameException(ExceptionConstant.GUILD_3724);
			}
			
			GuildWar guildWar = guild.getGuildWarMap().get(guildId);
			if(guildWar != null){
				throw new GameException(ExceptionConstant.GUILD_3733);
			}
			
			guildWar = new GuildWar();
			guildWar.setId(IDUtil.geneteId(GuildWar.class));
			guildWar.setGuildId(playerGuild.getGuildId());
			guildWar.setTargetGuildId(guildId);
			guildWar.setEndWarTime(DateService.getDateAddTime(DateService.getCurrentUtilDate(), DateService.HOUR_MIL*2));
			playerGuildDAO.createGuildWar(guildWar);
			guild.getGuildWarMap().put(guildId, guildWar);
			targetGuild.getGuildWarMap().put(playerGuild.getGuildId(), guildWar);
			
			//扣除帮派资金
			guild.setMoney(guild.getMoney() - GUILD_WAR_MONEY);
			this.updateGuild(guild);
			
			//都护府进入战争状态，敌对都护府为{0}！
			List<Notice> paramList = new ArrayList<Notice>();			
			Notice notice1 = new Notice(ParamType.PARAM, 0, 0, targetGuild.getGuildName()+"");
			paramList.add(notice1);
			List<Long> pIds = new ArrayList<Long>();
			pIds.addAll(guild.getPlayerIds());
			chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_39, paramList, pIds);
			
			List<Notice> paramList2 = new ArrayList<Notice>();			
			Notice notice2 = new Notice(ParamType.PARAM, 0, 0, guild.getGuildName()+"");
			paramList2.add(notice2);
			List<Long> pIds2 = new ArrayList<Long>();
			pIds2.addAll(targetGuild.getPlayerIds());
			chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_39, paramList2, pIds2);
			
			return guildWar.getEndWarTime().getTime();
		}
	}
	
	@Override
	public boolean isGuildWar(long playerId, long targetId) {
		PlayerGuild playerGuild = this.getPlayerGuild(playerId);
		if(playerGuild != null && playerGuild.getGuildId() > 0){
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild != null){
				PlayerGuild targetGuild = this.getPlayerGuild(targetId);
				if(targetGuild != null && targetGuild.getGuildId() > 0){
					if(guild.getGuildWarMap().containsKey(targetGuild.getGuildId())){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public int upgradeGuildSkill(long playerId, int type) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
		
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);
			}
			
			if(playerGuild.getRoleId() < GuildConstant.ROLE_2){
				throw new GameException(ExceptionConstant.GUILD_3710);
			}
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			synchronized (guild.getOprLock()) {
				Integer level = guild.getSkillMap().get(type);
				if(level == null || level == 0){
					level = 1;
				}else{
					level += 1;
				}
				
				int GUILD_SKILL_LEVEL= serviceCollection.getCommonService().getConfigValue(ConfigConstant.GUILD_SKILL_LEVEL);
				if(level > GUILD_SKILL_LEVEL){
					throw new GameException(ExceptionConstant.SKILL_1601);
				}
					
				BaseGuildSkill baseGuildSkill = this.getBaseGuildSkill(type, level);
				if(baseGuildSkill == null) throw new GameException(ExceptionConstant.ERROR_10000);
				
				if(guild.getLevel() < baseGuildSkill.getGuildLv()){
					throw new GameException(ExceptionConstant.GUILD_3730);
				}
				
				if(guild.getBuildNum() < baseGuildSkill.getGuildBuildNum()){
					throw new GameException(ExceptionConstant.GUILD_3725);
				}
				
				if(guild.getMoney() < baseGuildSkill.getGuildMoney()){
					throw new GameException(ExceptionConstant.GUILD_3724);
				}
				
				guild.getSkillMap().put(type, level);
				guild.setSkillMap(guild.getSkillMap());

				guild.setMoney(guild.getMoney() - baseGuildSkill.getGuildMoney());
				guild.setBuildNum(guild.getBuildNum() - baseGuildSkill.getGuildBuildNum());
				this.updateGuild(guild);
				
				return level;	
			}

		}
	
	}

	@Override
	public int studyGuildSkill(long playerId, int type) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IPropertyService propertyService = serviceCollection.getPropertyService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
		
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);
			}
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			
			Integer gLevel = guild.getSkillMap().get(type);
			if(gLevel == null || gLevel == 0){
				throw new GameException(ExceptionConstant.GUILD_3731);
			}
			
			Integer level = playerGuild.getSkillMap().get(type);
			if(level == null || level == 0){
				level = 1;
			}else{
				level += 1;
			}
			
			int GUILD_SKILL_LEVEL= serviceCollection.getCommonService().getConfigValue(ConfigConstant.GUILD_SKILL_LEVEL);
			if(level > GUILD_SKILL_LEVEL){
				throw new GameException(ExceptionConstant.SKILL_1601);
			}
			
			if(level > gLevel){
				throw new GameException(ExceptionConstant.GUILD_3732);
			}
				
			BaseGuildSkill baseGuildSkill = this.getBaseGuildSkill(type, level);
			if(baseGuildSkill == null) throw new GameException(ExceptionConstant.ERROR_10000);

			PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
			if(playerProperty.getLevel() < baseGuildSkill.getPlayerLv()){
				throw new GameException(ExceptionConstant.PLAYER_1110);	
			}
			
			PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);
			
			if(playerWealth.getGold() < baseGuildSkill.getMoney()){
				throw new GameException(ExceptionConstant.PLAYER_1112);
			}
			
			if(playerGuild.getContribution() < baseGuildSkill.getContribute()){
				throw new GameException(ExceptionConstant.PLAYER_1116);
			}
			
			playerService.addGold_syn(playerId, -baseGuildSkill.getMoney());
			playerGuild.setContribution(playerGuild.getContribution() - baseGuildSkill.getContribute());
			
			playerGuild.getSkillMap().put(type, level);
			playerGuild.setSkillMap(playerGuild.getSkillMap());
			
			this.updatePlayerGuild(playerGuild);
			
			//学习技能添加属性
			Map<Integer, Integer> addProMap = new HashMap<Integer, Integer>();
			int curProId = baseGuildSkill.getAddPropertyList().get(0);
			int curProValue = baseGuildSkill.getAddPropertyList().get(1);
			if(level == 1){
				addProMap.put(curProId, curProValue);
			}else{
				BaseGuildSkill lastBaseGuildSkill = this.getBaseGuildSkill(type, level - 1);
				addProMap.put(curProId, curProValue - lastBaseGuildSkill.getAddPropertyList().get(1));
			}
			
			propertyService.addProValue(playerId, addProMap, true, true);
			
			
			return level;
		}
	}
	
	@Override
	public void getGuildSkills(long playerId) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection(); 
 		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
 		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);
			}
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			
			S_GetGuildSkills.Builder builder = S_GetGuildSkills.newBuilder();
			
			for(Map.Entry<Integer, Integer> entry : playerGuild.getSkillMap().entrySet()){
				GuildSkillMsg.Builder gmsg = GuildSkillMsg.newBuilder();
				gmsg.setType(entry.getKey());
				gmsg.setLevel(entry.getValue());
				builder.addPlayerGuildSkills(gmsg);
			}
			
			for(Map.Entry<Integer, Integer> entry : guild.getSkillMap().entrySet()){
				GuildSkillMsg.Builder gmsg = GuildSkillMsg.newBuilder();
				gmsg.setType(entry.getKey());
				gmsg.setLevel(entry.getValue());
				builder.addGuildSkills(gmsg);
			}
			
			MessageObj msg = new MessageObj(MessageID.S_GetGuildSkills_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}
	}
	
	@Override
	public void minuteQuartz() {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IWeekActivityService weekActivityService = serviceCollection.getWeekActivityService();
		IChatService chatService = serviceCollection.getChatService();
		IMailService mailService = serviceCollection.getMailService();
		ISceneService sceneService = serviceCollection.getSceneService();
		
		Date curDate = DateService.getCurrentUtilDate();
		
		for(Map.Entry<Long, Guild> entry : this.getGuildMap().entrySet()){
			Guild guild = entry.getValue();
			
			Iterator<Map.Entry<Long, GuildWar>> iterator = guild.getGuildWarMap().entrySet().iterator();
			while(iterator.hasNext()){
				Map.Entry<Long, GuildWar> entry2 = iterator.next();
				GuildWar gw = entry2.getValue();
				if(gw.getEndWarTime().getTime() <= curDate.getTime()){
					iterator.remove();
					
					gw.setDeleteFlag(1);
					this.updateGuildWar(gw);
				}
			}
		}
		
		GuildFight fightData = this.getGuildFightCache();
		if(fightData.getState() == 0){
			// 0：平常状态 
			boolean isDay = false; //是否活动当天
			int week = DateService.getWeekDay(curDate);
			BaseWeekActivity activity = weekActivityService.getBaseWeekActivity(ActivityConstant.ACTIVITY_230);
			if(activity.getWeek() == week){
				isDay = true;
			}else{
				BaseWeekActivity activity2 = weekActivityService.getBaseWeekActivity(ActivityConstant.ACTIVITY_231);
				if(activity2.getWeek() == week){
					isDay = true;
				}
			}
			
			if(isDay && DateService.isInTime(activity.getStartHour(), activity.getStartMin(), activity.getEndHour(), activity.getEndMin())){
				fightData.setState(1);
				
				if(fightData.getGuildId() > 0){
					Guild guild = this.getGuildById(fightData.getGuildId());
					if(guild == null){
						fightData.setGuildId(0);
						fightData.setDefendName("");
					}
				}
				this.updateGuildFight(fightData);
				
				//大唐都护府城战已开放报名，请各都护前往“长安城战长老”处进行报名
				fightData.setNoticeTime(curDate.getTime());
				chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_47, null, serviceCollection.getGameSocketService().getOnLinePlayerIDList());
				System.out.println("大唐都护府城战已开放报名，请各都护前往“长安城战长老”处进行报名   "+DateService.dateFormat(curDate));
			}
		}else if(fightData.getState() == 1){
			//1：报名状态  
			if(DateService.isInTime(19, 0, 20, 0)){
				fightData.setState(2);
				this.updateGuildFight(fightData);
				
				//筛选联盟或帮派 
				if(fightData.getGuildId() < 1){
					//第一次以最多攻城令的帮派为守城方
					Guild guild1 = null;
					for(Long guildId : fightData.getApplySet()){
						Guild guild = this.getGuildById(guildId);
						if(guild1 == null){
							guild1 = guild;
							continue;
						}
						
						if(guild.getAllItemNum() > guild1.getAllItemNum()){
							guild1 = guild;
						}
					}
					if(guild1 != null){
						guild1.setDefend(true);
						this.updateGuild(guild1);
						
						fightData.setGuildId(guild1.getGuildId());
						fightData.setDefendName(guild1.getGuildName());
						this.updateGuildFight(fightData);
					}
				}
				if(fightData.getGuildId() > 0){
					//选择最多攻城令的联盟或帮派为攻城方
					if(fightData.getAttackId() < 1){
						int atkNum = 0;
						Guild atkGuild = null;
						Map<Long, Integer> itemMap = new HashMap<Long, Integer>();
						for(Long guildId : fightData.getApplySet()){
							Guild guild = this.getGuildById(guildId);
							if(guild.isDefend()){
								continue;
							}
							
							if(guild.getUnionId() > 0){
								Integer num = itemMap.get(guild.getUnionId());
								if(num == null){
									num = 0;
								}
								num += guild.getAllItemNum();
								itemMap.put(guild.getUnionId(), num);
							}else{
								if(atkGuild == null){
									atkGuild = guild;
									atkNum = guild.getAllItemNum();
									continue;
								}
								
								if(guild.getAllItemNum() > atkGuild.getAllItemNum()){
									atkGuild = guild;
									atkNum = guild.getAllItemNum();
								}
							}
						}
						
						Union atkUnion = null;
						for(Map.Entry<Long, Integer> entry : itemMap.entrySet()){
							if(entry.getValue() < atkNum){
								continue;
							}
							
							Union union = this.getUnion(entry.getKey());
							if(atkUnion == null){
								atkUnion = union;
								atkNum = entry.getValue();
								continue;
							}
							if(entry.getValue() > atkNum){
								atkUnion = union;
								atkNum = entry.getValue();
							}
						}
						int type = 0;
						if(atkUnion != null){
							type = 1;
							fightData.setAttackId(atkUnion.getUnionId());
							fightData.setAttackName(atkUnion.getName());
							
							fightData.setAtkGuildId(atkUnion.getGuildId());
							fightData.setAtkGuildName(atkUnion.getGuildName());
						}else if(atkGuild != null){
							type = 2;
							fightData.setAttackId(atkGuild.getGuildId());
							fightData.setAttackName(atkGuild.getGuildName());
							
							fightData.setAtkGuildId(atkGuild.getGuildId());
							fightData.setAtkGuildName(atkGuild.getGuildName());
						}else{
							fightData.setAttackId(0);
							fightData.setAttackName("");
							
							fightData.setAtkGuildId(0);
							fightData.setAtkGuildName("");
						}
						this.updateGuildFight(fightData);
						
						if(type > 0){
							//退还没选上的攻城令
							for(Long guildId : fightData.getApplySet()){
								Guild guild = this.getGuildById(guildId);
								if(guild.isDefend()){
									continue;
								}
								
								if(type == 1){
									if(guild.getUnionId() == fightData.getAttackId()){
										continue;
									}
								}else if(type == 2){
									if(guild.getGuildId() == fightData.getAttackId()){
										continue;
									}
								}
								
								int GUILD_FIGHT_ITEM = serviceCollection.getCommonService().getConfigValue(ConfigConstant.GUILD_FIGHT_ITEM);
								for(Map.Entry<Long, Integer> entry : guild.getItemMap().entrySet()){
									Integer num = entry.getValue();
									if(num > 0){
										int[][] rewards = new int[1][4];
										int[] items1 = new int[]{RewardTypeConstant.ITEM, GUILD_FIGHT_ITEM, num, 0};
										rewards[0] = items1;
										String rewardStr = SplitStringUtil.getStringByIntIntList(rewards);
										mailService.systemSendMail(entry.getKey(), ResourceUtil.getValue("guild_fight"), ResourceUtil.getValue("guild_fight_2"), rewardStr, 0);
									}
								}
								
								guild.getItemMap().clear();
								guild.setItemMap(guild.getItemMap());
								guild.setAllItemNum(0);
								this.updateGuild(guild);
							}
						}
				
						//大唐都护府城战将于20:00开始！攻城方【{0}】，防守方【{1}】，请做好准备。
						fightData.setNoticeTime(curDate.getTime());
						
						List<Notice> paramList = new ArrayList<Notice>();			
						Notice notice1 = new Notice(ParamType.PARAM, 0, 0, fightData.getAttackName());
						paramList.add(notice1);
						Notice notice2 = new Notice(ParamType.PARAM, 0, 0, fightData.getDefendName());
						paramList.add(notice2);
						chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_48, paramList, serviceCollection.getGameSocketService().getOnLinePlayerIDList());
						System.out.println("大唐都护府城战将于20:00开始！攻城方【{0}】，防守方【{1}】，请做好准备。   "+DateService.dateFormat(curDate));
					}
				}

			}else{
				if(curDate.getTime() >= fightData.getNoticeTime() + 2*DateService.HOUR_MIL){
					//大唐都护府城战已开放报名，请各都护前往“长安城战长老”处进行报名
					fightData.setNoticeTime(curDate.getTime());
					chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_47, null, serviceCollection.getGameSocketService().getOnLinePlayerIDList());
					System.out.println("大唐都护府城战已开放报名，请各都护前往“长安城战长老”处进行报名。   "+DateService.dateFormat(curDate));
				}
			}
		}else if(fightData.getState() == 2){
			//2：战前准备  
			if(DateService.isInTime(20, 0, 21, 0)){
				fightData.setState(3);
				this.updateGuildFight(fightData);
				
				//大唐都护府城战正式开始，请踊跃参加。
				fightData.setNoticeTime(curDate.getTime());
				chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_49, null, serviceCollection.getGameSocketService().getOnLinePlayerIDList());
				System.out.println("大唐都护府城战正式开始，请踊跃参加。   "+DateService.dateFormat(curDate));
			}else{
				if(fightData.getGuildId() > 0){
					if(curDate.getTime() >= fightData.getNoticeTime() + 19*DateService.MINITE_MIL){
						fightData.setNoticeTime(curDate.getTime());
						
						//大唐都护府城战将于20:00开始！攻城方【{0}】，防守方【{1}】，请做好准备。
						List<Notice> paramList = new ArrayList<Notice>();			
						Notice notice1 = new Notice(ParamType.PARAM, 0, 0, fightData.getAttackName());
						paramList.add(notice1);
						Notice notice2 = new Notice(ParamType.PARAM, 0, 0, fightData.getDefendName());
						paramList.add(notice2);
						chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_48, paramList, serviceCollection.getGameSocketService().getOnLinePlayerIDList());
						System.out.println("大唐都护府城战将于20:00开始！攻城方【{0}】，防守方【{1}】，请做好准备。   "+DateService.dateFormat(curDate));
					}
				}
			}
		}else if(fightData.getState() == 3){
			//3：开始状态 
			if(!DateService.isInTime(20, 0, 21, 0)){
				//活动结束 TODO
				fightData.setState(0);
				fightData.setNoticeTime(0);
				fightData.setAttackId(0);
				fightData.setAttackName("");
				fightData.setAttackNum(0);
				fightData.setDefendNum(0);
				
				//更换最后的守城方
				if(fightData.isOccupy()){
					Guild oleGuild = this.getGuildById(fightData.getGuildId());
					oleGuild.setDefend(false);
					this.updateGuild(oleGuild);
					
					Guild newGuild = this.getGuildById(fightData.getAtkGuildId());
					newGuild.setDefend(true);
					this.updateGuild(newGuild);
					
					fightData.setGuildId(fightData.getAtkGuildId());
					fightData.setDefendName(fightData.getAtkGuildName());
				}
				
				fightData.setOccupy(false);
				fightData.setAtkGuildId(0);
				fightData.setAtkGuildName("");
				
				//重置帮派城战数据
				for(Long guildId : fightData.getApplySet()){
					Guild guild = this.getGuildById(guildId);
					guild.setApplyFlag(false);
					guild.setUnionId(0);
					guild.setUnionName(null);
					guild.getItemMap().clear();
					guild.setItemMap(guild.getItemMap());
					guild.setAllItemNum(0);
					this.updateGuild(guild);
				}
				fightData.getApplySet().clear();
				
				this.updateGuildFight(fightData);
				
				try {
					//清理联盟数据
					playerGuildDAO.deleteUnions();
					CacheService.putToCache(CacheConstant.GUILD_UNION_LIST, new ConcurrentHashSet<Union>());
				} catch (Exception e) {
					LogUtil.error("清理联盟数据异常：", e);
				}
				
				if(fightData.getGuildId() < 1){
					//大唐都护府城战已经结束，长安城尚未被占领
					chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_51, null, serviceCollection.getGameSocketService().getOnLinePlayerIDList());	
					System.out.println("大唐都护府城战已经结束，长安城尚未被占领   "+DateService.dateFormat(curDate));
				}else{
					//大唐都护府城战已经结束，【{0}】都护府占领了长安城
					List<Notice> paramList = new ArrayList<Notice>();			
					Notice notice1 = new Notice(ParamType.PARAM, 0, 0, fightData.getDefendName());
					paramList.add(notice1);
					chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_58, paramList, serviceCollection.getGameSocketService().getOnLinePlayerIDList());
					System.out.println("大唐都护府城战已经结束，【{0}】都护府占领了长安城   "+DateService.dateFormat(curDate));
				}
				
				String sceneGuid1 = PlayerUtil.getSceneGuid(SceneConstant.TYPE_GUILD, GuildConstant.MAP_GUILD_7001);
				SceneModel sceneModel1 = sceneService.getSceneModel(sceneGuid1);
				if(sceneModel1 != null){
					sceneService.destroy(sceneModel1);
					System.out.println("摧毁长安城战地图  "+DateService.dateFormat(curDate));
				}
				
				String sceneGuid2 = PlayerUtil.getSceneGuid(SceneConstant.TYPE_GUILD, GuildConstant.MAP_GUILD_7002);
				SceneModel sceneModel2 = sceneService.getSceneModel(sceneGuid2);
				if(sceneModel2 != null){
					sceneService.destroy(sceneModel2);
					System.out.println("摧毁诛仙台地图   "+DateService.dateFormat(curDate));	
				}
			
			}else{
				if(DateService.isInTime(20, 57, 21, 0)){
					if(fightData.getNoticeTime() < curDate.getTime()){
						fightData.setNoticeTime(curDate.getTime() + 180000);
						chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_57, null, serviceCollection.getGameSocketService().getOnLinePlayerIDList());
						System.out.println("大唐都护府城战即将在3分钟后结束。   "+DateService.dateFormat(curDate));
					}
				}else{
					if(curDate.getTime() >= fightData.getNoticeTime() + 15*DateService.MINITE_MIL){
						//大唐都护府城战正式开始，请踊跃参加。
						fightData.setNoticeTime(curDate.getTime());
						chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_49, null, serviceCollection.getGameSocketService().getOnLinePlayerIDList());
						System.out.println("大唐都护府城战正式开始，请踊跃参加。   "+DateService.dateFormat(curDate));
					}
				}
	
			}
		}
	}
	
	@Override
	public void hourQuartz() {
		Date curDate = DateService.getCurrentUtilDate();
		
		for(Map.Entry<Long, Guild> entry : this.getGuildMap().entrySet()){
			Guild guild = entry.getValue();
			LinkedBlockingQueue<Long> traineeIds = guild.getTraineeIds();
			if(traineeIds.size() > 0){
				boolean bFind = false;
				
				Iterator<Long> iterator = traineeIds.iterator();
				while(iterator.hasNext()){
					Long playerId = iterator.next();
					PlayerGuild playerGuild = this.getPlayerGuild(playerId);
					if(playerGuild == null || playerGuild.getJoinTime() == null){
						iterator.remove();
						continue;
					}
					int difDay = DateService.difDate(playerGuild.getJoinTime(), curDate);
					if(difDay > 2){
						//2天以上清理见习成员
						bFind = true;
						iterator.remove();
						
						this.resetPlayerGuid(guild, playerGuild,  3);
					}
				}
				
				if(bFind){
					//重新计算战力
					this.calGuildBattleValue(guild);				
				}
			}
		}
	}

	@Override
	public void dailyQuartz() {
 		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection(); 
		IChatService chatService = serviceCollection.getChatService();
		IMailService mailService = serviceCollection.getMailService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		ICommonService commonService = serviceCollection.getCommonService();
		
		try {
			playerGuildDAO.quartzDelete();
		} catch (Exception e) {
			LogUtil.error("清理帮派异常：", e);
		}
		
		List<Long> removeList = new ArrayList<Long>();
		for(Map.Entry<Long, Guild> entry : this.getGuildMap().entrySet()){
			Guild guild = entry.getValue();
			BaseGuild baseGuild = this.getBaseGuild(guild.getLevel());
			if(guild.getBuildNum() < baseGuild.getCostBuildNum()){
				//退级
				if(guild.getLevel() <= 1){
					//解散
					removeList.add(guild.getGuildId());
					continue;
				}
				guild.setLevel(guild.getLevel() -1);
				guild.setMaxNum(baseGuild.getMaxNum());
				guild.setBuildNum(guild.getBuildNum() + baseGuild.getNeedBuildNum());
				
				//都护府等级降为：{1}
				List<Notice> paramList = new ArrayList<Notice>();			
				Notice notice1 = new Notice(ParamType.PARAM, 0, 0, guild.getLevel()+"");
				paramList.add(notice1);
				List<Long> pIds = new ArrayList<Long>();
				pIds.addAll(guild.getPlayerIds());
				chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_36, paramList, pIds);
			}
			guild.setBuildNum(guild.getBuildNum() - baseGuild.getCostBuildNum());
			guild.setMoney(guild.getMoney() - baseGuild.getCostMoney());
			guild.setCallNum(0);
			
			this.updateGuild(guild);
			
			//日常维护消耗了{0}都护府资金，{1}建设度。
			List<Notice> paramList = new ArrayList<Notice>();			
			Notice notice1 = new Notice(ParamType.PARAM, 0, 0, baseGuild.getCostMoney()+"");
			Notice notice2 = new Notice(ParamType.PARAM, 0, 0, baseGuild.getCostBuildNum()+"");
			paramList.add(notice1);
			paramList.add(notice2);
			List<Long> pIds = new ArrayList<Long>();
			pIds.addAll(guild.getPlayerIds());
			chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_40, paramList, pIds);
		}
		
		if(removeList.size() > 0){
			for(Long gid : removeList){
				Guild guild = this.getGuildMap().remove(gid);
				
				LinkedBlockingQueue<Long> playerIds = guild.getPlayerIds();
				
				// 都护府因建设度不足，被迫解散！
				List<Long> pIds = new ArrayList<Long>();
				pIds.addAll(playerIds);
				chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_37, null, pIds);
				
				for(Long pid : playerIds){
					
					PlayerGuild playerGuild = this.getPlayerGuild(pid);
					if(playerGuild != null){
						this.resetPlayerGuid(guild, playerGuild, 4);
						if(!gameSocketService.checkOnLine(pid)){
							mailService.systemSendMail(pid, ResourceUtil.getValue("guild"), ResourceUtil.getValue("guild_disband", DateService.dateFormatYMD(new Date())), "", 0);
						}
					}
				}
				
				this.deleteGuid(guild);
			}
		}
		
		
		GuildFight guildFight = this.getGuildFightCache();
		guildFight.setAllRevenue(guildFight.getCurRevenue());
		guildFight.setCurRevenue(0);
		guildFight.setSalaryNum(0);
		
		//帮主百分比
		int GUILD_REVENUE_PERCENT = commonService.getConfigValue(ConfigConstant.GUILD_REVENUE_PERCENT);
		//俸禄份数
		int GUILD_SALARY_NUM = commonService.getConfigValue(ConfigConstant.GUILD_SALARY_NUM);
		
		//税收
		double revenue = guildFight.getAllRevenue() * GUILD_REVENUE_PERCENT * 0.01;
		if(guildFight.getGuildId() > 0){
			revenue += guildFight.getRevenue();
		}
		int GUILD_REVENUE_LIMIT = commonService.getConfigValue(ConfigConstant.GUILD_REVENUE_LIMIT);
		if(revenue > GUILD_REVENUE_LIMIT){
			revenue = GUILD_REVENUE_LIMIT;
		}
		guildFight.setRevenue((int)revenue);
		
		//每份俸禄额
		double salary = guildFight.getAllRevenue() * (1 - GUILD_REVENUE_PERCENT * 0.01) / GUILD_SALARY_NUM;
		if(salary > GUILD_REVENUE_LIMIT){
			salary = GUILD_REVENUE_LIMIT;
		}
		guildFight.setSalary((int)salary);
		
		guildFight.setOpenFB(0);
		
		this.updateGuildFight(guildFight);
		
		
		Map<Integer, GuildBuy> guildBuyMap = this.getGuidBuyMap();
		for(Map.Entry<Integer, GuildBuy> entry : guildBuyMap.entrySet()){
			GuildBuy model = entry.getValue();
			model.setBuyNum(0);
		}
	}

	@Override
	public void weekQuartz() {
		try {
			playerGuildDAO.weekQuartz();
		} catch (Exception e) {
			LogUtil.error("playerGuild更新异常：", e);
		}
		
		for(Map.Entry<Long, Guild> entry : this.getGuildMap().entrySet()){
			Guild guild = entry.getValue();
			LinkedBlockingQueue<Long> playerIds = guild.getPlayerIds();
			for(Long pid : playerIds){
				PlayerGuild playerGuild = this.getPlayerGuild(pid);
				if(playerGuild != null){
					boolean bFind = false;
					if(playerGuild.getWeekBuildNum() != 0){
						playerGuild.setWeekBuildNum(0);
						bFind = true;
					}
					if(playerGuild.getWeekMoney() != 0){
						playerGuild.setWeekMoney(0);
						bFind = true;
					}
					if(bFind){
						this.updatePlayerGuild(playerGuild);
					}
				}
			}
		}
	}

	/**
	 * 获取当前城战信息
	 */
	@Override
	public GuildFight getGuildFightCache(){
		return (GuildFight)CacheService.getFromCache(CacheConstant.GUILD_FIGHT_DATA);
	}
	
	@Override
	public void getGuildFightData(long playerId) {
 		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
 		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
 		
 		GuildFight guildFightData = this.getGuildFightCache();
 		
		S_GetGuildFightData.Builder builder = S_GetGuildFightData.newBuilder();
		builder.setState(guildFightData.getState());
		builder.setDefendName(guildFightData.getDefendName());
		builder.setAttackName(guildFightData.getAttackName());
		MessageObj msg = new MessageObj(MessageID.S_GetGuildFightData_VALUE, builder.build().toByteArray());
		gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
	}
	
	
	@Override
	public void applyGuildFight(long playerId) throws Exception {
 		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
 		IPlayerService playerService = serviceCollection.getPlayerService();
 		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD_FIGHT)) {
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);
			}
			
			if(playerGuild.getRoleId() < GuildConstant.ROLE_2){
				throw new GameException(ExceptionConstant.GUILD_3710);
			}
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			
			//报名时间点
			GuildFight guildFightData = this.getGuildFightCache();
			if(guildFightData.getState() != 1){
				throw new GameException(ExceptionConstant.GUILD_3747);
			}
			
			//限制人数
			int GUILD_APPLY_NUM= serviceCollection.getCommonService().getConfigValue(ConfigConstant.GUILD_APPLY_NUM);
			if(guild.getPlayerIds().size() < GUILD_APPLY_NUM){
				throw new GameException(ExceptionConstant.GUILD_3735);
			}
			
			PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);
			
			//报名所需金币
			int GUILD_APPLY_MONEY= serviceCollection.getCommonService().getConfigValue(ConfigConstant.GUILD_APPLY_MONEY);
			if(playerWealth.getGold() < GUILD_APPLY_MONEY){
				throw new GameException(ExceptionConstant.PLAYER_1112);
			}
			
		    //守城方
			if(guild.isDefend()){
				throw new GameException(ExceptionConstant.GUILD_3737);
			}
			
			Set<Long> lists = guildFightData.getApplySet();
			if(lists.contains(guild.getGuildId())){
				throw new GameException(ExceptionConstant.GUILD_3736);
			}
			lists.add(guild.getGuildId());
			
			playerService.addGold_syn(playerId, -GUILD_APPLY_MONEY);
			
			guild.setApplyFlag(true);
			this.updateGuild(guild);
		}
	}

	/**
	 * 取联盟
	 */
	@SuppressWarnings("unchecked")
	private Union getUnion(long unionId){
		Set<Union> unionList = (Set<Union>)CacheService.getFromCache(CacheConstant.GUILD_UNION_LIST);
		
		for(Union union : unionList){
			if(union.getUnionId() == unionId){
				return union;
			}
		}
		
		return null;
	}
	
	@Override
	public void getGuildFights(long playerId) throws Exception {
 		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
 		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
 		
 		S_GetGuildFights.Builder builder = S_GetGuildFights.newBuilder();
 		
 		GuildFight guildFightData = this.getGuildFightCache();
		for(Long guildId : guildFightData.getApplySet()){
			Guild guild = this.getGuildById(guildId);
			if(guild == null) continue;
			
			GuildFightMsg.Builder msg = GuildFightMsg.newBuilder();
			msg.setGuildName(guild.getGuildName());
			
			if(guild.getUnionId() > 0){
				Union union = this.getUnion(guild.getUnionId());
				if(union != null){
					msg.setUnionName(union.getName());
					msg.setCreateFlag(guild.getGuildId() == union.getGuildId() ? 1 : 0);
				}
			}
			
			builder.addGuildFights(msg);
		}
		
		MessageObj msg = new MessageObj(MessageID.S_GetGuildFights_VALUE, builder.build().toByteArray());
		gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void createUnion(long playerId, String unionName) throws Exception {
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD_FIGHT)) {
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);
			}
			
			if(playerGuild.getRoleId() < GuildConstant.ROLE_2){
				throw new GameException(ExceptionConstant.GUILD_3710);
			}
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			
			GuildFight guildFightData = this.getGuildFightCache();
			if(guildFightData.getState() != 1){
				throw new GameException(ExceptionConstant.GUILD_3748);
			}
			
			if(!guildFightData.getApplySet().contains(guild.getGuildId())){
				throw new GameException(ExceptionConstant.GUILD_3738);
			}
			
			if(guild.getUnionId() > 0){
				throw new GameException(ExceptionConstant.GUILD_3739);
			}
			
			if (unionName == null || "".equals(unionName.trim())) {
				throw new GameException(ExceptionConstant.GUILD_3740);
			}

			unionName = CommonUtil.replaceInput(unionName);

			if (unionName.length() < 4  || unionName.length() > 12) {
				throw new GameException(ExceptionConstant.GUILD_3741);
			}

			if (CommonUtil.checkInput(unionName)) {
				throw new GameException(ExceptionConstant.GUILD_3742);
			}
			
			Set<Union> unionList = (Set<Union>)CacheService.getFromCache(CacheConstant.GUILD_UNION_LIST);
			
			// 验证名称重复
			for(Union union : unionList){
				if(union.getName().equals(unionName)){
					throw new GameException(ExceptionConstant.GUILD_3743);
				}
			}
			
			Union union = new Union();
			union.setUnionId(IDUtil.geneteId(Union.class));
			union.setName(unionName);
			union.setCreatorId(playerId);
			union.setGuildId(guild.getGuildId());
			union.setGuildName(guild.getGuildName());
			
			playerGuildDAO.createGuildUnion(union);
			unionList.add(union);
			
			guild.setUnionId(union.getUnionId());
			guild.setUnionName(union.getName());
			this.updateGuild(guild);
			
			this.getUnions(playerId);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void getUnions(long playerId) throws Exception {
 		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
 		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD_FIGHT)) {
			
	 		S_GetUnions.Builder builder = S_GetUnions.newBuilder();
	 		
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild != null && playerGuild.getGuildId() > 0){
				Guild guild = this.getGuildById(playerGuild.getGuildId());
				if(guild == null){
					throw new GameException(ExceptionConstant.GUILD_3706);
				}
				
				if(guild.getUnionId() > 0){
					builder.setMyUnionId(guild.getUnionId());
					Union union = this.getUnion(guild.getUnionId());
					if(union != null && union.getGuildId() == guild.getGuildId()){
						for(Long gid : union.getApplyList()){
							Guild g = this.getGuildById(gid);
							if(g == null) continue;
							
							ApplyUnionMsg.Builder msg = ApplyUnionMsg.newBuilder();
							msg.setGuildId(g.getGuildId());
							msg.setGuildName(g.getGuildName());
							msg.setAgreeFlag(playerGuild.getRoleId() < GuildConstant.ROLE_2 ? 0 : 1);
							builder.addApplys(msg);
						}
					}
				}
	
			}
			
			Set<Union> unionList = (Set<Union>)CacheService.getFromCache(CacheConstant.GUILD_UNION_LIST);
			
			for(Union union : unionList){
				UnionMsg.Builder msg = UnionMsg.newBuilder();
				msg.setUnionId(union.getUnionId());
				msg.setUnionName(union.getName());
				msg.setApplyFlag(union.getApplyList().contains(playerGuild.getGuildId()) ? 1 : 0);
				
				builder.addUnions(msg);
			}
			
			MessageObj msg = new MessageObj(MessageID.S_GetUnions_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}
		
	}
	
	@Override
	public void applyUnion(long playerId, long unionId) throws Exception {
 		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
 		IChatService chatService = serviceCollection.getChatService();
 		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD_FIGHT)) {
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);
			}
			
			if(playerGuild.getRoleId() < GuildConstant.ROLE_2){
				throw new GameException(ExceptionConstant.GUILD_3710);
			}
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			
			if(guild.getUnionId() > 0){
				throw new GameException(ExceptionConstant.GUILD_3739);
			}
			
			GuildFight guildFightData = this.getGuildFightCache();
			if(guildFightData.getState() != 1){
				throw new GameException(ExceptionConstant.GUILD_3748);
			}
			
			if(!guildFightData.getApplySet().contains(guild.getGuildId())){
				throw new GameException(ExceptionConstant.GUILD_3751);
			}
			
			Union union = this.getUnion(unionId);
			if(union == null) {
				throw new GameException(ExceptionConstant.GUILD_3744);
			}

			if(union.isFull()){
				throw new GameException(ExceptionConstant.GUILD_3749);	
			}
			
			union.getApplyList().add(guild.getGuildId());
			
			//【{0}】都护府请求与贵都护府进行联盟， 请前往处理
			List<Notice> paramList = new ArrayList<Notice>();			
			Notice notice1 = new Notice(ParamType.PARAM, 0, 0, guild.getGuildName());
			paramList.add(notice1);
			List<Long> pIds = new ArrayList<Long>();
			pIds.add(union.getCreatorId());
			chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_46, paramList, pIds);
		}
	}
	
	@Override
	public void agreeJoinUnion(long playerId, Long guildId) throws Exception {
 		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
 		IChatService chatService = serviceCollection.getChatService();
 		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD_FIGHT)) {
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);
			}
			
			if(playerGuild.getRoleId() < GuildConstant.ROLE_2){
				throw new GameException(ExceptionConstant.GUILD_3710);
			}
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			
			Guild targetGuild = this.getGuildById(guildId);
			if(targetGuild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			
			GuildFight guildFightData = this.getGuildFightCache();
			if(guildFightData.getState() != 1){
				throw new GameException(ExceptionConstant.GUILD_3748);
			}
			
			if(!guildFightData.getApplySet().contains(guild.getGuildId())){
				throw new GameException(ExceptionConstant.GUILD_3751);
			}
			
			Union union = this.getUnion(guild.getUnionId());
			if(union == null) {
				throw new GameException(ExceptionConstant.GUILD_3744);
			}
			
			if(union.getGuildId() != guild.getGuildId()){
				throw new GameException(ExceptionConstant.GUILD_3745);
			}
			
			boolean find = union.getApplyList().remove(guildId);
			if(!find){
				throw new GameException(ExceptionConstant.GUILD_3746);
			}
			
			if(targetGuild.getUnionId() > 0){
				throw new GameException(ExceptionConstant.GUILD_3746);
			}
			
			targetGuild.setUnionId(union.getUnionId());
			targetGuild.setUnionName(union.getName());
			this.updateGuild(targetGuild);
			
			union.setFull(true);
			
			//都护府加入了【{0}】联盟
			List<Notice> paramList = new ArrayList<Notice>();			
			Notice notice1 = new Notice(ParamType.PARAM, 0, 0, union.getName()+"");
			paramList.add(notice1);
			List<Long> pIds = new ArrayList<Long>();
			pIds.addAll(targetGuild.getPlayerIds());
			chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_54, paramList, pIds);
		}
	}

	@Override
	public void submitItem(long playerId, int itemNum) throws Exception {
		
		if(itemNum < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
 		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
 		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD_FIGHT)) {
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);
			}
			
			if(playerGuild.getRoleId() < GuildConstant.ROLE_2){
				throw new GameException(ExceptionConstant.GUILD_3710);
			}
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			
			if(guild.isDefend()){
				throw new GameException(ExceptionConstant.GUILD_3750);
			}
			
			GuildFight guildFightData = this.getGuildFightCache();
			if(guildFightData.getState() != 1){
				throw new GameException(ExceptionConstant.GUILD_3748);
			}
			
			if(!guildFightData.getApplySet().contains(guild.getGuildId())){
				throw new GameException(ExceptionConstant.GUILD_3751);
			}
			
			synchronized (guild.getOprLock()) {
				// 攻城令
				int GUILD_FIGHT_ITEM = serviceCollection.getCommonService().getConfigValue(ConfigConstant.GUILD_FIGHT_ITEM);
				serviceCollection.getRewardService().deductItem(playerId, GUILD_FIGHT_ITEM, itemNum, true);
				
				Integer num = guild.getItemMap().get(playerId);
				if(num == null){
					num = 0;
				}
				num +=itemNum;
				guild.getItemMap().put(playerId, num);
				guild.setItemMap(guild.getItemMap());
				guild.setAllItemNum(guild.getAllItemNum() + itemNum);
				this.updateGuild(guild);
			}
		}
	}
	
	@Override
	public void enterGuildFight(long playerId) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		ISceneService sceneService = serviceCollection.getSceneService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD_FIGHT)) {
			PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerId);
			if(playerPuppet == null) throw new GameException(ExceptionConstant.PLAYER_1111);
			
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);
			}
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			
			int pkType = 0;
			GuildFight guildFightData = this.getGuildFightCache();
			if(guild.isDefend()){
				if(guildFightData.getState() < 2){
					throw new GameException(ExceptionConstant.GUILD_3747);
				}
				pkType = 2;
			}else{
				
				if(!guild.isApplyFlag()){
					throw new GameException(ExceptionConstant.GUILD_3751);
				}
				
				if(guildFightData.getState() == 2){
					throw new GameException(ExceptionConstant.GUILD_3764);
				}
				
				if(guildFightData.getState() != 3){
					throw new GameException(ExceptionConstant.GUILD_3747);
				}
				pkType = 1;
			}
			
			sceneService.enterScene(playerId, GuildConstant.MAP_GUILD_7001, 0, false, null, pkType);
		}
	}

	@Override
	public void enterZhuXianTai(long playerId, int pkType, boolean enter) {
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD_FIGHT)) {
			GuildFight guildFightData = this.getGuildFightCache();
			if(guildFightData.getState() != 3){
				return;
			}
			
			if(pkType == 1){
				//进攻方
				if(enter){
					guildFightData.setAttackNum(guildFightData.getAttackNum() + 1);
					
				}else{
					guildFightData.setAttackNum(guildFightData.getAttackNum() - 1);
				}
			}else{
				//防守方
				if(enter){
					guildFightData.setDefendNum(guildFightData.getDefendNum() + 1);
				}else{
					guildFightData.setDefendNum(guildFightData.getDefendNum() - 1);
				}
			}
			
			//如果诛仙台攻城人数被清空  并且之前被占领    则守城方占领回来
			if(guildFightData.getAttackNum() < 1 && guildFightData.getDefendNum() > 0){
				if(guildFightData.isOccupy()){
					guildFightData.setOccupy(false);
					this.updateGuildFight(guildFightData);
					
					ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
					IChatService chatService = serviceCollection.getChatService();
					
					List<Notice> paramList = new ArrayList<Notice>();			
					Notice notice1 = new Notice(ParamType.PARAM, 0, 0, guildFightData.getDefendName());
					paramList.add(notice1);
					Notice notice2 = new Notice(ParamType.PARAM, 0, 0, guildFightData.getAtkGuildName());
					paramList.add(notice2);
					chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_53, paramList, serviceCollection.getGameSocketService().getOnLinePlayerIDList());
					System.out.println("【{0}】都护府成功从【{1}】都护府手中夺取长安城。   "+DateService.dateFormat(DateService.getCurrentUtilDate()));
				}
			}
			
			//如果诛仙台守城方人数被清空  并且之前没被占领    则攻城方占领回来
			if(guildFightData.getDefendNum() < 1 && guildFightData.getAttackNum() > 0){
				if(!guildFightData.isOccupy()){
					guildFightData.setOccupy(true);
					this.updateGuildFight(guildFightData);
					
					ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
					IChatService chatService = serviceCollection.getChatService();
					
					List<Notice> paramList = new ArrayList<Notice>();			
					Notice notice1 = new Notice(ParamType.PARAM, 0, 0, guildFightData.getAtkGuildName());
					paramList.add(notice1);
					Notice notice2 = new Notice(ParamType.PARAM, 0, 0, guildFightData.getDefendName());
					paramList.add(notice2);
					chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_53, paramList, serviceCollection.getGameSocketService().getOnLinePlayerIDList());
					System.out.println("【{0}】都护府成功从【{1}】都护府手中夺取长安城。   "+DateService.dateFormat(DateService.getCurrentUtilDate()));
				}
			}
			
			System.out.println("诛仙台当前人数：  攻城方="+guildFightData.getAttackNum()+" 守城方="+guildFightData.getDefendNum());
		}
	}

	@Override
	public void addCurRevenue(int addValue) {
		GuildFight guildFight = this.getGuildFightCache();
		guildFight.setCurRevenue(guildFight.getCurRevenue() + addValue);
		this.updateGuildFight(guildFight);
	}

	@Override
	public void getRevenueData(long playerId) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		ICommonService commonService = serviceCollection.getCommonService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			PlayerDaily playerDaily = playerService.getPlayerDailyById(playerId);
			if(playerDaily == null) return;
			
			GuildFight guildFight = this.getGuildFightCache();
			
			//份子数
			int GUILD_SALARY_NUM = commonService.getConfigValue(ConfigConstant.GUILD_SALARY_NUM);
			
			S_GetRevenueData.Builder builder = S_GetRevenueData.newBuilder();
			if(guildFight.getGuildId() > 0){
				Guild guild = this.getGuildById(guildFight.getGuildId());
				if(guild != null){
					builder.setGuildName(guild.getGuildName());
					builder.setHeaderName(guild.getHeaderName());
				}
			}
			builder.setAllRevenue(guildFight.getAllRevenue());
			builder.setRevenue(guildFight.getRevenue());
			builder.setSalaryNum(GUILD_SALARY_NUM - guildFight.getSalaryNum());
			builder.setSalary(guildFight.getSalary());
			builder.setOpenFB(guildFight.getOpenFB() == 0 ? 0 : 1);
			
			MessageObj msg = new MessageObj(MessageID.S_GetRevenueData_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}
	}

	@Override
	public void receiveRevenue(long playerId) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);		
			}
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			
			if(playerGuild.getRoleId() != GuildConstant.ROLE_3){
				throw new GameException(ExceptionConstant.GUILD_3753);
			}
			
			if(!guild.isDefend()){
				throw new GameException(ExceptionConstant.GUILD_3754);
			}
			
			GuildFight guildFight = this.getGuildFightCache();
			
			if(guildFight.getRevenue() < 1){
				throw new GameException(ExceptionConstant.GUILD_3755);
			}
			
			playerService.addGold_syn(playerId, guildFight.getRevenue());
			
			guildFight.setRevenue(0);
			this.updateGuildFight(guildFight);
			
		}
	}

	@Override
	public int receiveSalary(long playerId) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		ICommonService commonService = serviceCollection.getCommonService();	
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);		
			}
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			
			if(!guild.isDefend()){
				throw new GameException(ExceptionConstant.GUILD_3754);
			}
			
			PlayerDaily playerDaily = playerService.getPlayerDailyById(playerId);
			if(playerDaily.getSalaryFlag() == 1){
				throw new GameException(ExceptionConstant.GUILD_3756);
			}
			
			//份子数
			int GUILD_SALARY_NUM = commonService.getConfigValue(ConfigConstant.GUILD_SALARY_NUM);
			
			GuildFight guildFight = this.getGuildFightCache();
			
			if(guildFight.getSalary() < 1){
				throw new GameException(ExceptionConstant.GUILD_3758);
			}
			
			if(guildFight.getSalaryNum() >= GUILD_SALARY_NUM){
				throw new GameException(ExceptionConstant.GUILD_3757);
			}
	
			playerService.addGold_syn(playerId, guildFight.getSalary());
			
			playerDaily.setSalaryFlag(1);
			playerService.updatePlayerDaily(playerDaily);
			
			guildFight.setSalaryNum(guildFight.getSalaryNum() + 1);
			this.updateGuildFight(guildFight);
			
			return GUILD_SALARY_NUM - guildFight.getSalaryNum();
		}
		
	}

	@Override
	public void receiveGift(long playerId) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IRewardService rewardService = serviceCollection.getRewardService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);		
			}
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			
//			if(playerGuild.getRoleId() != GuildConstant.ROLE_3){
//				throw new GameException(ExceptionConstant.GUILD_3753);
//			}
			
			if(!guild.isDefend()){
				throw new GameException(ExceptionConstant.GUILD_3754);
			}
			
			PlayerDaily playerDaily = playerService.getPlayerDailyById(playerId);
			if(playerDaily.getGiftFlag() == 1){
				throw new GameException(ExceptionConstant.GUILD_3756);
			}
			
			int day = DateService.getCurrWeekDay();
			if(day != 1 && day != 3 && day != 5){
				throw new GameException(ExceptionConstant.GUILD_3759);
			}
			
			GuildFight guildFight = this.getGuildFightCache();
			if(guildFight.getState() == 3){
				throw new GameException(ExceptionConstant.GUILD_3760);
			}
			
			BaseReward baseReward = rewardService.getBaseReward(RewardConstant.REWARD_GUILD_FIGHT, 1601);
			rewardService.fetchRewardList(playerId, baseReward.getRewardList());
			
			playerDaily.setGiftFlag(1);
			playerService.updatePlayerDaily(playerDaily);
			
		}
	}

	@Override
	public int guildBuy(long playerId, int itemId, int itemNum) throws Exception {
		if(itemId < 1 || itemNum < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);		
			}
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			
			if(!guild.isDefend()){
				throw new GameException(ExceptionConstant.GUILD_3754);
			}
			
			GuildFight guildFight = this.getGuildFightCache();
			if(guildFight.getState() == 3){
				throw new GameException(ExceptionConstant.GUILD_3760);
			}
			
			BaseGuildBuy baseGuildBuy = this.getBaseGuildBuy(itemId);
			if(baseGuildBuy == null){
				throw new GameException(ExceptionConstant.BAG_1300);
			}
			
			synchronized (baseGuildBuy.getBuyLock()) {
				PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);
				
				if(playerWealth.getGold() < baseGuildBuy.getCurPrice()){
					 throw new GameException(ExceptionConstant.PLAYER_1112);
				}
				
				int curBuyNum = 0;
				if(baseGuildBuy.getLimitNum() > 0){
					GuildBuy guildBuy = this.getGuildBuy(itemId);
					
					curBuyNum = guildBuy.getBuyNum() + itemNum;
					
					if(curBuyNum >= baseGuildBuy.getLimitNum()){
						throw new GameException(ExceptionConstant.MARKET_2701);
					}
					
					// 添加物品
					serviceCollection.getRewardService().fetchRewardOne(playerId, RewardTypeConstant.ITEM, itemId, itemNum, 0);
					
					guildBuy.setBuyNum(curBuyNum);
					this.updateGuildBuy(guildBuy);
				}else{
					
					// 添加物品
					serviceCollection.getRewardService().fetchRewardOne(playerId, RewardTypeConstant.ITEM, itemId, itemNum, 0);
				}
				
				playerService.addGold_syn(playerId, -baseGuildBuy.getCurPrice());
				
				return curBuyNum;
			}
		}
	}

	@Override
	public void guildFB(long playerId, int type) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);		
			}
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			
			if(!guild.isDefend()){
				throw new GameException(ExceptionConstant.GUILD_3754);
			}
			
			GuildFight guildFight = this.getGuildFightCache();
			if(guildFight.getState() == 3){
				throw new GameException(ExceptionConstant.GUILD_3760);
			}
			
			if(type == 1){
				if(playerGuild.getRoleId() < GuildConstant.ROLE_2){
					throw new GameException(ExceptionConstant.GUILD_3710);
				}		
				
				if(guildFight.getOpenFB() > 0){
					throw new GameException(ExceptionConstant.GUILD_3761);
				}
				
				guildFight.setOpenFB(1);
				this.updateGuildFight(guildFight);
				
				//伟大的【{0}】都护府开启了【凌烟阁】副本，请占城都护府成员前往狩猎
				List<Notice> paramList = new ArrayList<Notice>();			
				Notice notice1 = new Notice(ParamType.PARAM, 0, 0, guild.getGuildName());
				paramList.add(notice1);
				serviceCollection.getChatService().synNotice(ChatConstant.CHAT_NOTICE_MAG_55, paramList, serviceCollection.getGameSocketService().getOnLinePlayerIDList());
				
			}else{
				if(guildFight.getOpenFB() == 0){
					throw new GameException(ExceptionConstant.GUILD_3762);
				}
				
				if(guildFight.getOpenFB() == 2){
					throw new GameException(ExceptionConstant.GUILD_3763);
				}
				
				String sceneGuid = PlayerUtil.getSceneGuid(SceneConstant.TYPE_GUILD, GuildConstant.MAP_GUILD_7003);
				serviceCollection.getSceneService().enterScene(playerId, GuildConstant.MAP_GUILD_7003, 0, false, sceneGuid, 0);
			}
		}
	}

	@Override
	public void endGuildFB() {
		GuildFight guildFight = this.getGuildFightCache();
		guildFight.setOpenFB(2);
		this.updateGuildFight(guildFight);
	}

	@Override
	public void getManorData(long playerId) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);		
			}
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			
			S_GetManorData.Builder builder = S_GetManorData.newBuilder();
			builder.setCallNum(guild.getCallNum());
			builder.setFeedNum(guild.getFeedNum());
			MessageObj msg = new MessageObj(MessageID.S_GetManorData_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}
	}
	
	@Override
	public void guildManor(long playerId) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);		
			}
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			
			String sceneGuid = PlayerUtil.getSceneGuid(SceneConstant.TYPE_GUILD, guild.getGuildId());
			serviceCollection.getSceneService().enterScene(playerId, GuildConstant.MAP_GUILD_7004, 0, false, sceneGuid, 0);
		}
	}

	@Override
	public void callManorBoss(long playerId) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		ISceneService sceneService = serviceCollection.getSceneService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			
			PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerId);
			if(playerPuppet == null) return;
			
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);		
			}
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			
			if(playerGuild.getRoleId() < GuildConstant.ROLE_2){
				throw new GameException(ExceptionConstant.GUILD_3710);
			}
			
			int MONOR_BOSS_NUM= serviceCollection.getCommonService().getConfigValue(ConfigConstant.MONOR_BOSS_NUM); 
			
			if(guild.getCallNum() >= MONOR_BOSS_NUM){
				throw new GameException(ExceptionConstant.GUILD_3765);
			}
			
			int MONOR_FEED_MAX= serviceCollection.getCommonService().getConfigValue(ConfigConstant.MONOR_FEED_MAX); 
			if(guild.getFeedNum() < MONOR_FEED_MAX){
				throw new GameException(ExceptionConstant.GUILD_3766);
			}
			SceneModel sceneModel = sceneService.getSceneModel(playerPuppet.getSceneGuid());
			boolean bFind = false;
			for(Map.Entry<Integer, Map<String, MonsterPuppet>> entry : sceneModel.getMonsterPuppetMap().entrySet()){
				Map<String, MonsterPuppet> map = entry.getValue();
				if(map != null && !map.isEmpty()){
					bFind = true;
					break;
				}
			}
			if(bFind){
				throw new GameException(ExceptionConstant.GUILD_3767);
			}
			
			guild.setCallNum(guild.getCallNum() + 1);
			guild.setFeedNum(0);
			this.updateGuild(guild);
			
			serviceCollection.getMonsterService().refreshMonsters(sceneModel, GuildConstant.REFRESH_ID_7004, GuildConstant.REFRESH_POS_7004[0], GuildConstant.REFRESH_POS_7004[1], GuildConstant.REFRESH_POS_7004[2], 0, true);
		
		}
	}

	@Override
	public int feedManorBoss(long playerId, int itemNum) throws Exception {
		
		if(itemNum < 1) throw new GameException(ExceptionConstant.ERROR_10000);	
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_GUILD)) {
			PlayerGuild playerGuild = this.getPlayerGuild(playerId);
			if(playerGuild == null || playerGuild.getGuildId() < 1){
				throw new GameException(ExceptionConstant.GUILD_3709);		
			}
			
			Guild guild = this.getGuildById(playerGuild.getGuildId());
			if(guild == null){
				throw new GameException(ExceptionConstant.GUILD_3706);
			}
			
			synchronized (guild.getOprLock()) {
				int MONOR_FEED_MAX= serviceCollection.getCommonService().getConfigValue(ConfigConstant.MONOR_FEED_MAX); 
				
				if(guild.getFeedNum() >= MONOR_FEED_MAX){
					throw new GameException(ExceptionConstant.GUILD_3768);
				}
				
				int num = guild.getFeedNum() + itemNum;
				if(num > MONOR_FEED_MAX){
					itemNum = MONOR_FEED_MAX - guild.getFeedNum();
					num = MONOR_FEED_MAX;
				}
				
				int MONOR_FEED_ITEM = serviceCollection.getCommonService().getConfigValue(ConfigConstant.MONOR_FEED_ITEM);
				serviceCollection.getRewardService().deductItem(playerId, MONOR_FEED_ITEM, itemNum, true);
				
				int MONOR_FEED_BUILD = serviceCollection.getCommonService().getConfigValue(ConfigConstant.MONOR_FEED_BUILD);
				
				guild.setBuildNum(MONOR_FEED_BUILD * itemNum);
				guild.setFeedNum(num);
				this.updateGuild(guild);
				
				int MONOR_FEED_CONTRU = serviceCollection.getCommonService().getConfigValue(ConfigConstant.MONOR_FEED_CONTRU);
				playerGuild.setContribution(playerGuild.getContribution() + itemNum * MONOR_FEED_CONTRU);
				this.updatePlayerGuild(playerGuild);
				
				return guild.getFeedNum();
			}
			
		}
	}


}
