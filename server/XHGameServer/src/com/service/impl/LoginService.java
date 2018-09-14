package com.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.cache.CacheService;
import com.common.Config;
import com.common.DateService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.LockService;
import com.common.LogoutCacheService;
import com.common.MD5Service;
import com.common.ServiceCollection;
import com.constant.CacheConstant;
import com.constant.ConfigConstant;
import com.constant.ExceptionConstant;
import com.constant.HttpConstant;
import com.constant.ItemConstant;
import com.constant.LockConstant;
import com.constant.SceneConstant;
import com.core.Connection;
import com.core.Connection.ConnectionState;
import com.core.GameMessage;
import com.core.jedis.RedisUtil;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.Position;
import com.domain.bag.PlayerBag;
import com.domain.bag.PlayerDrug;
import com.domain.bag.PlayerEquipment;
import com.domain.base.BaseNewRole;
import com.domain.config.BaseServerConfig;
import com.domain.epigraph.PlayerWeaponEffect;
import com.domain.family.PlayerFamily;
import com.domain.furnace.PlayerFurnace;
import com.domain.map.BaseMap;
import com.domain.player.Player;
import com.domain.player.PlayerDaily;
import com.domain.player.PlayerExt;
import com.domain.player.PlayerProperty;
import com.domain.player.PlayerWealth;
import com.domain.puppet.PlayerPuppet;
import com.domain.sign.PlayerSign;
import com.domain.skill.PlayerSkill;
import com.domain.task.PlayerTask;
import com.message.LoginProto.C_LoginAgain;
import com.message.LoginProto.C_LoginGame;
import com.message.LoginProto.LoginMsg;
import com.message.LoginProto.S_CreatePlayer;
import com.message.LoginProto.S_EnterComplete;
import com.message.LoginProto.S_EnterGame;
import com.message.LoginProto.S_ExitGame;
import com.message.LoginProto.S_LoginGame;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.PlayerProto.PlayerMsg;
import com.scene.SceneModel;
import com.service.IBagService;
import com.service.ICommonService;
import com.service.IEpigraphService;
import com.service.IEquipmentService;
import com.service.IFamilyService;
import com.service.IGameConfigCacheService;
import com.service.ILogService;
import com.service.ILoginService;
import com.service.IMailService;
import com.service.IPlayerService;
import com.service.IProtoBuilderService;
import com.service.ISceneService;
import com.service.ISignService;
import com.service.ISkillService;
import com.service.ITaskService;
import com.service.ITeamService;
import com.service.IVipService;
import com.util.CommonUtil;
import com.util.HttpUtil;
import com.util.LogUtil;

/**
 * 登录系统
 * @author ken
 * @date 2016-12-22
 */
public class LoginService implements ILoginService {

	
	@Override
	public void login(GameMessage gameMessage) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IGameConfigCacheService configCacheService = serviceCollection.getGameCfgCacheService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		C_LoginGame c_login = C_LoginGame.parseFrom(gameMessage.getData());
		long userId = c_login.getUserId();
		String key = c_login.getKey(); //权限人员可以不用验证
		String time = c_login.getTime();
		String sign = c_login.getSign();
		int serverNo = c_login.getServerNo();
		
		// 服务器状态验证
		BaseServerConfig serverConfig = configCacheService.getBaseServerConfigByServerNo(serverNo);
		if (serverConfig == null) return;
		
		//登录检测
		if (!Config.DEFAULT_LOGIN_KEY.equals(key)) {
			
			if (sign == null || "".equalsIgnoreCase(sign.trim())) {
				return;
			}
			// 正常签名
			String genSign = MD5Service.encryptToUpperString(userId+key+time+Config.WEB_LOGIN_KEY);
			if (!genSign.equalsIgnoreCase(sign)) {
				// 签名错误
				return;
			}


			// 非正常状态下，不能提前进入
			if (serverConfig.getSeverState() < 1 || serverConfig.getSeverState() > 3) {
				return;
			} 
			
			//维护中
			if(serverConfig.getEndStopDate() != null && System.currentTimeMillis() < DateService.parseDate(serverConfig.getEndStopDate()).getTime()){
				return;
			}
			
			//是否已经被封停了
			try {
				String redisIPKey = "gcc@"+Config.AGENT+"@banIpSet";
				Set<String> ipList = RedisUtil.getListValueOfS(redisIPKey);
				
				if(ipList != null){
					for (String ip : ipList) {
						if (gameMessage.getConnection().getConnectIP().equals(ip)) {
							return;
						}
					}
				}
			} catch (Exception e) {
				LogUtil.error("登录检测ip错误",e);
			}
		} 
		
		List<Player> players = playerService.listPlayerByUserId(userId);
		
		synchronized (LockConstant.PLAYER_LOGIN + userId) {
			
			//顶号登录
			Connection oldCon = gameSocketService.getConnection(userId);
			if(oldCon != null){
				if(!oldCon.getConnectIP().equals(gameMessage.getConnection().getConnectIP())){
					LogUtil.error("顶号： 前ip"+oldCon.getConnectIP()+" 后ip"+gameMessage.getConnection().getConnectIP());
					//顶号
					S_ExitGame.Builder builder1 = S_ExitGame.newBuilder();
					MessageObj msg1 = new MessageObj(MessageID.S_ExitGame_VALUE, builder1.build().toByteArray());
					gameSocketService.sendData(oldCon, msg1);
				}
				oldCon.setReconnectd(true);
			}
			
			gameSocketService.addConnectionCache(userId, gameMessage.getConnection());
			
			boolean newRegFlag = true;
			S_LoginGame.Builder builder = S_LoginGame.newBuilder();
			for(Player model : players){
				if(!model.getSite().equals(serverConfig.getGameSite())){
					continue;
				}
				
				PlayerMsg.Builder msg = protoBuilderService.buildPlayerMsg(model);
				if(msg == null)continue;
				
				builder.addPlayerMsgs(msg);
				
				newRegFlag = false;
			}
			MessageObj msg = new MessageObj(MessageID.S_LoginGame_VALUE, builder.build().toByteArray());
			gameSocketService.sendData(gameMessage.getConnection(), msg);
			
			if(newRegFlag){
				try {
					ILogService logService = serviceCollection.getLogService();
					logService.createRegisterLog(userId, serverConfig.getGameSite());
				} catch (Exception e) {
					LogUtil.error("注册日志异常：",e);
				}
			}

		}
		
	}

	@Override
	public void loginAgain(GameMessage gameMessage) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IGameConfigCacheService configCacheService = serviceCollection.getGameCfgCacheService();
		
		C_LoginAgain c_login = C_LoginAgain.parseFrom(gameMessage.getData());
		long userId = c_login.getUserId();
		String key = c_login.getKey();
		String time = c_login.getTime();
		String sign = c_login.getSign();
		int serverNo = c_login.getServerNo();
		
		long playerId = c_login.getPlayerId();
		
		//登录检测
		if (!Config.DEFAULT_LOGIN_KEY.equals(key)) {
			
			if (sign == null || "".equalsIgnoreCase(sign.trim())) {
				return;
			}
			// 正常签名
			String genSign = MD5Service.encryptToUpperString(userId+key+time+Config.WEB_LOGIN_KEY);
			if (!genSign.equalsIgnoreCase(sign)) {
				// 签名错误
				return;
			}

			// 服务器状态验证
			BaseServerConfig serverConfig = configCacheService.getBaseServerConfigByServerNo(serverNo);
			if (serverConfig == null) return;

			// 非正常状态下，不能提前进入
			if (serverConfig.getSeverState() < 1 || serverConfig.getSeverState() > 3) {
				return;
			} 
		} 
		
		synchronized (LockConstant.PLAYER_LOGIN + userId) {
			//顶号登录
			Connection oldCon = gameSocketService.getConnection(userId);
			if(oldCon != null){
				if(!oldCon.getConnectIP().equals(gameMessage.getConnection().getConnectIP())){
					S_ExitGame.Builder builder1 = S_ExitGame.newBuilder();
					MessageObj msg1 = new MessageObj(MessageID.S_ExitGame_VALUE, builder1.build().toByteArray());
					gameSocketService.sendData(oldCon, msg1);
				}
				oldCon.setReconnectd(true);
			}
			
			gameSocketService.addConnectionCache(userId, gameMessage.getConnection());
			
			this.enterGame(playerId, 0);
		}
		
	}
	
	@Override
	public void createPlayer(long userId, int serverNo, int career, String playerName, long telePhone) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		IGameConfigCacheService configCacheService = serviceCollection.getGameCfgCacheService();
		
		synchronized (LockConstant.PLAYER + userId) {
			
			if(career <= 0) throw new GameException(ExceptionConstant.CREATE_1101);
			
			playerName = CommonUtil.replaceInput(playerName);
			if (playerName == null || "".equals(playerName.trim())) throw new GameException(ExceptionConstant.CREATE_1102);

			if (playerName.length() < 2 || playerName.length() > 7) throw new GameException(ExceptionConstant.CREATE_1103);

			if (CommonUtil.checkInput(playerName)) throw new GameException(ExceptionConstant.CREATE_1104);
			
			BaseServerConfig serverConfig = configCacheService.getBaseServerConfigByServerNo(serverNo);
			if (serverConfig == null) throw new GameException(ExceptionConstant.CREATE_1107);
			
			Player player = playerService.createPlayer_sp(userId, serverConfig.getGameSite(), serverNo, playerName, career, telePhone);
			
			if(player != null){				
				
				List<Player> lists = playerService.listPlayerByUserId(userId);
				
				CacheService.putToCache(CacheConstant.PLAYER_CACHE + player.getPlayerId(), player);
				playerService.getPlayerIDCache().add(player.getPlayerId());
				
				PlayerExt playerExt = playerService.getPlayerExtById(player.getPlayerId());
				
				BaseNewRole baseNewRole = serviceCollection.getCommonService().getBaseNewRole(career);
				
				playerExt.setWeaponStyle(baseNewRole.getWeaponStyle());
				playerExt.setDressStyle(baseNewRole.getDressStyle());
				
				S_CreatePlayer.Builder builder = S_CreatePlayer.newBuilder();
				builder.setPlayerMsg(protoBuilderService.buildPlayerMsg(player));
				MessageObj msg = new MessageObj(MessageID.S_CreatePlayer_VALUE, builder.build().toByteArray());
				gameSocketService.sendDataToPlayerByUserId(userId, msg);
				
				this.sendToLoginServer(userId, serverNo, lists);
				lists.add(player);
				
				try {
					//创建角色日志
					ILogService logService = serviceCollection.getLogService();
					logService.createPlayerLog(userId, player.getPlayerId(), serverConfig.getGameSite());
				} catch (Exception e) {
					LogUtil.error("创建角色日志异常：",e);
				}
			}
			
		}
	}

	/**
	 * 创号通知登录服
	 */
	private void sendToLoginServer(long userId, int serverNo, List<Player> lists){

		try {
			
			for(Player pl : lists){
				if(pl.getServerNo() == serverNo) return;				
			}			
			
			String sign = MD5Service.encryptToLowerString(userId+serverNo+Config.WEB_LOGIN_KEY);
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("userId", userId);
			jsonObject.put("serverNo", serverNo);
			jsonObject.put("sign", sign);
			// 创号通知登录服		 
			HttpUtil.sendData_noWaitBack(jsonObject, Config.ACCOUNT_URL+HttpConstant.CREATE_PLAYER);
		} catch (Exception e) {
			LogUtil.error("发送创号通知异常：",e);
		}
	
	}
	
	@Override
	public void enterGame(long playerId, long telePhone) throws Exception {
		if(playerId <= 0){
			System.out.println("enterGame null  with playerId is 0");
			return;
		}
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		ICommonService commonService = serviceCollection.getCommonService();
		
		synchronized (LockConstant.PLAYER_ENTER_GAME + playerId) {
			
			Player player = playerService.getPlayerByID(playerId);
			if(player == null) {
				System.out.println(" player is null with id is "+ playerId);
				return;
			}
			
			// 玩家手机绑定号码设置
			if(telePhone > 0){
				player.setTelePhone(telePhone);
			}			
			
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
			PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);
			PlayerDaily playerDaily = playerService.getPlayerDailyById(playerId);
			
			if(playerProperty.getHpMax() <= 0){
				//初始裸体属性
				commonService.dealInitProperty(playerProperty, playerExt, playerWealth, player.getCareer());
				playerService.updatePlayerWealth(playerWealth);
				playerService.updatePlayerProperty(playerProperty);
			}
			
			//进游戏前处理
			this.beforeLogin(playerExt);
			
			S_EnterGame.Builder builder = S_EnterGame.newBuilder();
			LoginMsg.Builder loginMsg = LoginMsg.newBuilder();
			
			
			loginMsg.setPlayerCommonMsg(protoBuilderService.buildPlayerCommonMsg(player, playerProperty, playerExt, playerWealth, playerDaily));
			if(playerExt.getMapId() == 0){
				playerExt.setMapId(1000);
			}
			loginMsg.setMapId(playerExt.getMapId());
			
			// 服务器时间
			loginMsg.setServerTime(DateService.getCurrentUtilDate().getTime());
			
			builder.setLoginMsg(loginMsg);
			
			Connection connection = gameSocketService.getConnection(player.getUserId());
			if(connection == null)  throw new GameException(ExceptionConstant.ERROR_2);
			
			connection.setPlayerId(playerId);
			connection.setState(ConnectionState.INGAME);
			
			gameSocketService.sendData(connection, new MessageObj(
					MessageID.S_EnterGame_VALUE, builder.build()
							.toByteArray()));
			
			playerExt.setLoginIP(connection.getConnectIP());
			//进游戏后处理
			this.doLogin(playerExt);
			
			try {
				//登录日志
				ILogService logService = serviceCollection.getLogService();
				logService.createLoginLog(player.getUserId(), Config.AGENT, player.getSite());
			} catch (Exception e) {
				LogUtil.error("登录日志异常：",e);
			}
		}
	}
	
	/**
	 * 进游戏后处理
	 */
	private void doLogin(PlayerExt playerExt){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		ITeamService teamService = serviceCollection.getTeamService();
		IFamilyService familyService = serviceCollection.getFamilyService();
		IVipService vipService = serviceCollection.getVipService();
	
		//移除登出缓存队列
		LogoutCacheService.removeCacheForLogin(playerExt.getPlayerId());
		
		//加入玩家锁
		LockService.putToPlayerSelfLock(playerExt.getPlayerId());
		
		//队伍
		teamService.dealLogin(playerExt);
		
		//天梯
		serviceCollection.getTiantiService().doLogin(playerExt.getPlayerId());
		
		//家族上线处理
		familyService.dealLogin(playerExt.getPlayerId());
		
		//VIP检测
		vipService.dealLogin(playerExt);	
		
		//更新入库
		serviceCollection.getPlayerService().updatePlayerExt(playerExt);
	}
 
	// TODO  根据开放功能或者等级  取对应数据
	@Override
	public void enterComplete(Connection connection) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		IPlayerService playerService = serviceCollection.getPlayerService();
		IBagService bagService = serviceCollection.getBagService();
		IEquipmentService equipmentService = serviceCollection.getEquipmentService();
		ISkillService skillService = serviceCollection.getSkillService();
		IFamilyService familyService = serviceCollection.getFamilyService();
		ISignService signService = serviceCollection.getSignService();
		
		long playerId = connection.getPlayerId();
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_ENTER_COMPELTE)) {
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
			
			S_EnterComplete.Builder builder = S_EnterComplete.newBuilder();
			builder.setTestSwitch(Config.TEST_SWITCH ? 1:0);
			builder.setEquipmentGrid(playerExt.getTradeGridNum());
			builder.setCurLayerId(playerExt.getCurLayerId());
			
			// 家族
			int CREATE_FAMILY_LEVEL= serviceCollection.getCommonService().getConfigValue(ConfigConstant.CREATE_FAMILY_LEVEL);
			if(playerProperty.getLevel() >= CREATE_FAMILY_LEVEL){
				PlayerFamily playerFamily = familyService.getPlayerFamily(playerId);
				if(playerFamily != null){
					builder.setPlayerFamilyId(playerFamily.getPlayerFamilyId());
				}		
			}

			// 签到数据
			PlayerSign playerSign = signService.getPlayerSign(playerId);
			builder.setSignMsg(protoBuilderService.buildSignMsg(playerSign));
			
			
			//背包
			builder.setBagGrid(playerExt.getBagGrid());
			List<PlayerBag> playerBags = bagService.getPlayerBagListByPlayerID(playerId);
			for(PlayerBag playerBag : playerBags){
				builder.addListPlayerBags(protoBuilderService.buildPlayerBagMsg(playerBag));
			}
			
			//装备
			List<PlayerEquipment> playerEquipmentList = equipmentService.getPlayerEquipmentList(playerId);
			for(PlayerEquipment pe : playerEquipmentList){
				if(pe.getState() > 0 && pe.getState() != ItemConstant.EQUIP_STATE_TRADE){
					builder.addListPlayerEquipments(protoBuilderService.buildPlayerEquipmentMsg(pe));
				}
			}
			
			//药品栏
			List<PlayerDrug> playerDrugList = bagService.listPlayerDrugs(playerId);
			for(PlayerDrug model : playerDrugList){
				if(model.getType() == ItemConstant.HP_DRUG_TYPE){
					builder.addHpDrugLumns(protoBuilderService.buildDrugLumnMsg(model));
				}else{
					builder.addMpDrugLumns(protoBuilderService.buildDrugLumnMsg(model));
				}				
			}
			
			//技能
			List<PlayerSkill> playerSkills = skillService.listPlayerSkills(playerId);			
			for(PlayerSkill model : playerSkills){
				builder.addListPlayerSkills(protoBuilderService.buildPlayerSkillMsg(model));
			}
			
			//邮件
			IMailService mailService = serviceCollection.getMailService();
			builder.setHaveNewMail(mailService.isHaveNewMail(playerId));
			builder.setHaveMailTotalNum(mailService.getPlayerMailInboxList(playerId).size());			
			
			//任务
			ITaskService taskService = serviceCollection.getTaskService();
			Map<Integer, PlayerTask> taskMap = taskService.getPlayerTaskMapByPlayerId(playerId);
			if(taskMap == null || taskMap.isEmpty()){
				//初始玩家任务
				List<Integer> initTaskIds = new ArrayList<Integer>();
				initTaskIds.add(31000);
				taskService.createPlayerTask(playerId, initTaskIds);
			}
			for(Map.Entry<Integer, PlayerTask> entry : taskMap.entrySet()){
				builder.addListPlayerTasks(protoBuilderService.buildPlayerTaskMsg(entry.getValue()));
			}
			
			// 玩家武器铭文信息
			IEpigraphService epigraphService = serviceCollection.getEpigraphService();
			Map<Integer, PlayerWeaponEffect> playerWeaponEffectMap = epigraphService.getPlayerWeaponEffectMap(playerId);
			for(Map.Entry<Integer, PlayerWeaponEffect> entry : playerWeaponEffectMap.entrySet()){
				builder.addListPlayerWeaponEffect(protoBuilderService.buildPlayerWeaponEffectMsg(entry.getValue()));
			}
			
			//诛仙阁
			List<PlayerFurnace> lists = serviceCollection.getFurnaceService().getPlayerFurnaceList(playerId);
			for(PlayerFurnace model : lists){
				if(model.getFurnaceId() < 1) continue;
				
				builder.addFurnaceList(protoBuilderService.buildPlayerFurnaceMsg(model));
			}
			
			MessageObj msg = new MessageObj(MessageID.S_EnterComplete_VALUE, builder.build().toByteArray());
			gameSocketService.sendData(connection, msg);
		}		
	}

	/**
	 * 进游戏前处理
	 */
	private void beforeLogin(PlayerExt playerExt) {
		ServiceCollection serviceCollection = GameContext.getInstance()
				.getServiceCollection();
		ISceneService sceneService = serviceCollection.getSceneService();
		
		// 累计登录时间计算
		boolean isCurrentDay = DateService.isCurrentDay(playerExt.getLoginTime());	
		if(!isCurrentDay){			
			playerExt.setAddLoginDay(playerExt.getAddLoginDay() + 1);			
		}		
		
		playerExt.setLoginTime(DateService.getCurrentUtilDate());
		
		//死亡上线处理
		PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerExt.getPlayerId());
		if(playerPuppet == null && playerExt.getHp() <= 0){
			PlayerProperty playerProperty = serviceCollection.getPlayerService().getPlayerPropertyById(playerExt.getPlayerId());
			playerExt.setHp(playerProperty.getHpMax());
			int mapId = playerExt.getMapId();
			if(mapId != 1000){
				mapId = 1001;
			}
			BaseMap baseMap = sceneService.getBaseMap(mapId);
			Position position = baseMap.getRevivePositions().get(0);
			playerExt.setX(position.getX());
			playerExt.setY(position.getY());
			playerExt.setZ(position.getZ());
			playerExt.setMapId(mapId);
		}
		
		//红名检查
		if(playerExt.getPkVlaue() >= 300){
			playerExt.setNameColor(3);
		}
		
		//场景检测
		String sceneGuid = playerExt.getSceneGuid();
		if(sceneGuid == null || sceneGuid.equals("")){
			BaseMap baseMap = sceneService.getBaseMap(playerExt.getMapId());
			if(baseMap.isInstance()){
				playerExt.setMapId(playerExt.getLastMapId());
				playerExt.setX(playerExt.getLastX());
				playerExt.setY(playerExt.getLastY());
				playerExt.setZ(playerExt.getLastZ());
			}
			return;
		}
		
		SceneModel sceneModel = sceneService.getSceneModel(sceneGuid);
		if(sceneModel.getSceneState() != SceneConstant.SCENE_STATE_COMMON){
			playerExt.setMapId(playerExt.getLastMapId());
			playerExt.setX(playerExt.getLastX());
			playerExt.setY(playerExt.getLastY());
			playerExt.setZ(playerExt.getLastZ());
		}else{
			//城战中下线  回到主城
			BaseMap baseMap = sceneService.getBaseMap(playerExt.getMapId());
			if(baseMap.isGuild()){
				playerExt.setMapId(playerExt.getLastMapId());
				playerExt.setX(playerExt.getLastX());
				playerExt.setY(playerExt.getLastY());
				playerExt.setZ(playerExt.getLastZ());
			}
		}
		
	}
	
	@Override
	public void logout(Connection connection) throws Exception {
		connection.setState(ConnectionState.EXIT);
		
		long playerId = connection.getPlayerId();
		
		ServiceCollection serviceCollection = GameContext.getInstance()
				.getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		ISceneService sceneService = serviceCollection.getSceneService();
		
		playerService.dealExitData(playerId);
		
		PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerId);
		if(playerPuppet == null) return;
		
		String sceneGuid = playerPuppet.getSceneGuid();
		if(sceneGuid == null || sceneGuid.equals("")) return;
		
		SceneModel sceneModel = sceneService.getSceneModel(sceneGuid);
		if(sceneModel.getSceneState() == SceneConstant.SCENE_STATE_DISTROY) return;
		
		if(sceneModel.getMapType() == SceneConstant.GUILD_SCENE){
			this.exitGame(playerId);
		}else{
			playerPuppet.setLogoutTime(System.currentTimeMillis());
		}
	}

	@Override
	public void exitGame(long playerId) {
		ServiceCollection serviceCollection = GameContext.getInstance()
				.getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		ISceneService sceneService = serviceCollection.getSceneService();
		
		//地图中或副本退出处理
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_SCENE)) {
			try {
				Player player = playerService.getPlayerByID(playerId);
				if(player != null){
					Connection con = serviceCollection.getGameSocketService().getConnection(player.getUserId());
					if(con != null && con.getState().equals(ConnectionState.EXIT)){
						playerService.exit(con);
					}	
				}
				
				sceneService.quitScene(playerId);
				sceneService.deletePlayerPuppet(playerId);
			} catch (Exception e) {
				LogUtil.error("移除场景个人数据：", e);
			}
		}		
	}

}
