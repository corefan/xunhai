/**
 * 
 */
package com.service.impl;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.cache.BaseCacheService;
import com.cache.CacheService;
import com.common.Config;
import com.common.DateService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.LockService;
import com.common.RandomService;
import com.common.ServiceCollection;
import com.constant.BattleConstant;
import com.constant.CacheConstant;
import com.constant.ExceptionConstant;
import com.constant.LockConstant;
import com.constant.PlayerConstant;
import com.constant.ProdefineConstant;
import com.constant.SceneConstant;
import com.constant.SkillConstant;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.base.BaseNewRole;
import com.domain.base.BaseProperty;
import com.domain.battle.SkillEffect;
import com.domain.battle.WigSkillInfo;
import com.domain.config.BaseServerConfig;
import com.domain.map.BaseMap;
import com.domain.player.Player;
import com.domain.player.PlayerExt;
import com.domain.player.PlayerProperty;
import com.domain.puppet.BasePuppet;
import com.domain.puppet.MonsterPuppet;
import com.domain.puppet.PlayerPuppet;
import com.domain.puppet.PuppetState;
import com.domain.skill.BaseSkill;
import com.domain.skill.DamagModel;
import com.domain.skill.PlayerSkill;
import com.message.BattleProto.S_SkillResult;
import com.message.BattleProto.S_SynSkill;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.SceneProto.S_AddPlayerPuppets;
import com.scene.SceneModel;
import com.service.ICommonService;
import com.service.IGameConfigCacheService;
import com.service.IPlayerService;
import com.service.IProtoBuilderService;
import com.service.IRebotService;
import com.service.ISceneService;
import com.service.ISkillService;
import com.util.CommonUtil;
import com.util.IDUtil;
import com.util.NineGridUtil;
import com.util.PlayerUtil;
import com.util.SerialNumberUtil;

/**
 * @author jiangqin
 * @date 2017-8-15
 */
public class RebotService implements IRebotService{
	@Override
	public void initBaseCache() {
		// 装备位描述
		Map<Integer, Integer> baseRebotSkill = new HashMap<Integer, Integer>();
		baseRebotSkill.put(11000, 100000);
		baseRebotSkill.put(11010, 101000);
		baseRebotSkill.put(11020, 102000);
		baseRebotSkill.put(11030, 103000);
		baseRebotSkill.put(11040, 104000);
		baseRebotSkill.put(11100, 110000);
		baseRebotSkill.put(11110, 111000);
		baseRebotSkill.put(11120, 112000);
		baseRebotSkill.put(11130, 113000);					
		baseRebotSkill.put(11140, 114000);
		baseRebotSkill.put(11200, 120000);
		baseRebotSkill.put(11210, 121000);					
		baseRebotSkill.put(11220, 122000);
		baseRebotSkill.put(11230, 123000);					
		baseRebotSkill.put(11240, 124000);
		BaseCacheService.putToBaseCache(CacheConstant.BASE_REBOT_ID, baseRebotSkill);		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void createRebot(long playerId, int num) throws Exception {
		if(num <= 0) return;
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		PlayerExt playerExt = playerService.getPlayerExtById(playerId);
		
		for(int i = 0; i < num; i++){
			int career = RandomService.getRandomNum(1, 3);	
			
			Player rebot = this.createRebot(SerialNumberUtil.getRobotOtherID(), Config.SEVER_NO, career, "t" + SerialNumberUtil.getRobotOtherNameID());
			
			this.initRebot(playerId, rebot.getPlayerId()); 
			
			this.openAllSkill(rebot.getPlayerId());	
			
			this.enterScene(playerId, rebot.getPlayerId(), playerExt.getMapId());			
		
		}	
		Map<Long, PlayerPuppet> map = (Map<Long, PlayerPuppet>)CacheService.getFromCache(CacheConstant.PLAYER_PUPPET_CACHE);
		System.out.println("当前在线人数为："+map.size());
	}

	private Player createRebot(int userId, int serverNo, int career, String playerName) throws Exception {
	    ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IGameConfigCacheService configCacheService = serviceCollection.getGameCfgCacheService();
		
		synchronized (LockConstant.PLAYER + userId) {
			
			if(career <= 0) throw new GameException(ExceptionConstant.CREATE_1101);
			
			playerName = CommonUtil.replaceInput(playerName);
			if (playerName == null || "".equals(playerName.trim())) throw new GameException(ExceptionConstant.CREATE_1102);

			if (playerName.length() < 2 || playerName.length() > 7) throw new GameException(ExceptionConstant.CREATE_1103);

			if (CommonUtil.checkInput(playerName)) throw new GameException(ExceptionConstant.CREATE_1104);
			
			BaseServerConfig serverConfig = configCacheService.getBaseServerConfigByServerNo(serverNo);
			if (serverConfig == null) throw new GameException(ExceptionConstant.CREATE_1107);
			
			
			int playerId = SerialNumberUtil.getRobotPlayerId();
			Player rebot = (Player)CacheService.getFromCache(CacheConstant.PLAYER_CACHE + playerId);
			if(rebot == null){
				rebot = new Player();
			}
			
			rebot.setPlayerId(playerId);
			rebot.setUserId(userId);
			rebot.setSite(serverConfig.getGameSite());
			rebot.setServerNo(serverNo);
			rebot.setPlayerName(playerName);
			rebot.setCareer(career);
			rebot.setGuid(PlayerUtil.getGuid(PlayerConstant.PLAYER, rebot.getPlayerId()));
			rebot.setType(1);
			rebot.setCreateTime(new Date(System.currentTimeMillis()));
			
			CacheService.putToCache(CacheConstant.PLAYER_CACHE + rebot.getPlayerId(), rebot);
			if(!playerService.getPlayerIDCache().contains(rebot.getPlayerId())){
				playerService.getPlayerIDCache().add(rebot.getPlayerId());
			}				
			
			PlayerExt rebotExt = (PlayerExt)CacheService.getFromCache(CacheConstant.PLAYER_EXT_CACHE + playerId);
			if(rebotExt == null){
				rebotExt = new PlayerExt();
				rebotExt.setPlayerId(playerId);
			}
					
			BaseNewRole baseNewRole = serviceCollection.getCommonService().getBaseNewRole(career);				
			rebotExt.setWeaponStyle(baseNewRole.getWeaponStyle());
			rebotExt.setDressStyle(baseNewRole.getDressStyle());				
			CacheService.putToCache(CacheConstant.PLAYER_EXT_CACHE + rebot.getPlayerId(), rebotExt);
			
			PlayerProperty rebotProperty = (PlayerProperty)CacheService.getFromCache(CacheConstant.PLAYER_PROPERTY_CACHE + playerId);
			if(rebotProperty == null)	{
				rebotProperty = new PlayerProperty();
				rebotProperty.setPlayerId(playerId);
			}
					
			rebotProperty.setLevel(30);
			CacheService.putToCache(CacheConstant.PLAYER_PROPERTY_CACHE + rebot.getPlayerId(), rebotProperty);	
			
			//创建角色日志TODO
			
			
			return rebot;
		}		
		
	}

	private void initRebot(long playerId, long rebotPlayerId) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		ICommonService commonService = serviceCollection.getCommonService();
			
		Player rebot =(Player)CacheService.getFromCache(CacheConstant.PLAYER_CACHE + rebotPlayerId);
		PlayerExt rebotExt =(PlayerExt)CacheService.getFromCache(CacheConstant.PLAYER_EXT_CACHE + rebotPlayerId);
		PlayerProperty rebotProperty =(PlayerProperty)CacheService.getFromCache(CacheConstant.PLAYER_PROPERTY_CACHE + rebotPlayerId);
		
		//初始裸体属性
		BaseProperty baseProperty = commonService.getBaseProperty(rebot.getCareer(), rebotProperty.getLevel());
		BaseNewRole baseNewRole = commonService.getBaseNewRole(rebot.getCareer());
			
		//初始属性
		rebotProperty.setMoveSpeed(baseNewRole.getMoveSpeed());
		Map<Integer, Integer> addProMap = new HashMap<Integer, Integer>();
		addProMap.put(ProdefineConstant.STRENGTH, baseProperty.getStrength());
		addProMap.put(ProdefineConstant.INTELLIGENCE, baseProperty.getIntelligence());
		addProMap.put(ProdefineConstant.ENDURANCE, baseProperty.getEndurance());
		addProMap.put(ProdefineConstant.SPIRIT, baseProperty.getSpirit());
		addProMap.put(ProdefineConstant.LUCKY, baseProperty.getLucky());
		addProMap.put(ProdefineConstant.DMG_DEEP_PER_PANEL, baseProperty.getDmgDeepPer());
		addProMap.put(ProdefineConstant.DMG_REDUCT_PER_PANEL, baseProperty.getDmgReductPer());
		addProMap.put(ProdefineConstant.DMG_CRIT_PER_PANEL, baseProperty.getDmgCritPer());
		serviceCollection.getPropertyService().addProValue(rebotProperty.getPlayerId(), addProMap, false, false);
		serviceCollection.getPropertyService().calTotalBattleValue(rebotProperty);
		
		rebotExt.setHp(rebotProperty.getHpMax());
		rebotExt.setMp(rebotProperty.getMpMax());
		rebotExt.setDressStyle(baseNewRole.getDressStyle());
		rebotExt.setWeaponStyle(baseNewRole.getWeaponStyle());
		rebotExt.setMapId(baseNewRole.getMapName());
		rebotExt.setX(baseNewRole.getX());
		rebotExt.setY(baseNewRole.getY());
		rebotExt.setZ(baseNewRole.getZ());
		rebotExt.setLastMapId(rebotExt.getMapId());
		rebotExt.setLastX(rebotExt.getX());
		rebotExt.setLastY(rebotExt.getY());
		rebotExt.setLastZ(rebotExt.getZ());
		rebotExt.setDirection(baseNewRole.getDirection());
		rebotExt.setBagGrid(baseNewRole.getBagGrid());
		rebotExt.setTradeGridNum(baseNewRole.getMaxTradeGrid());
		rebotExt.setLine(1);
		
		rebotProperty.setHpMax(1000000);
		rebotProperty.setMpMax(1000000);
		rebotExt.setHp(1000000);
		rebotExt.setMp(1000000);
		rebotExt.setLoginTime(DateService.getCurrentUtilDate());	
		
	}

	@SuppressWarnings("unchecked")
	private void enterScene(long playerId, long rebotPlayerId, int mapId) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		ISceneService sceneService = serviceCollection.getSceneService();

		synchronized (LockService.getPlayerLockByType(rebotPlayerId, LockConstant.PLAYER_SCENE)) {
	
				Player rebot = (Player) CacheService.getFromCache(CacheConstant.PLAYER_CACHE + rebotPlayerId);
				PlayerExt rebotExt = (PlayerExt)CacheService.getFromCache(CacheConstant.PLAYER_EXT_CACHE + rebotPlayerId);
				int line = rebotExt.getLine();
				
				BaseMap baseMap = sceneService.getBaseMap(mapId);
				if(baseMap == null){
					System.out.println("enterScene this baseMap is null with mapId is "+ mapId);
					return;
				}
				
				String sceneGuid = null;
				if (baseMap.getMapType() == SceneConstant.MAIN_CITY) {

					sceneGuid = PlayerUtil.getSceneGuid(SceneConstant.TYPE_CITY, mapId, line);
				} else if (baseMap.getMapType() == SceneConstant.WORLD_SCENE) {

					sceneGuid = PlayerUtil.getSceneGuid(SceneConstant.TYPE_WORLD, mapId, line);
				}
				
				Map<String, SceneModel> sceneModelMap = (Map<String, SceneModel>)CacheService.getFromCache(CacheConstant.SCENE_CACHE);
				SceneModel sceneModel = sceneModelMap.get(sceneGuid);			
				synchronized (sceneModel.getLock()) {					
					rebotExt.setSceneGuid(sceneGuid);
					
					// 初始化战斗对象
					PlayerPuppet rebotPuppet = sceneService.getPlayerPuppet(rebotPlayerId);
					
					if(rebotPuppet == null){
						PlayerProperty rebotProperty = playerService.getPlayerPropertyById(rebotPlayerId);
						rebotPuppet = new PlayerPuppet();
						rebotPuppet.setGuid(rebot.getGuid());
						rebotPuppet.setEid(rebot.getPlayerId());
						rebotPuppet.setName(rebot.getPlayerName());
						rebotPuppet.setType(PlayerConstant.PLAYER);
						rebotPuppet.setLevel(rebotProperty.getLevel());
						rebotPuppet.setDressStyle(rebotExt.getDressStyle());
						rebotPuppet.setWeaponStyle(rebotExt.getWeaponStyle());
						rebotPuppet.setWingStyle(rebotExt.getWingStyle());
						rebotPuppet.setCareer(rebot.getCareer());
						rebotPuppet.setPuppetState(PuppetState.STAND);
						
						rebotPuppet.setHpMax(rebotProperty.getHpMax());
						rebotPuppet.setMpMax(rebotProperty.getMpMax());
						rebotPuppet.setHp(rebotExt.getHp());
						rebotPuppet.setMp(rebotExt.getMp());
						rebotPuppet.setPkMode(rebotExt.getPkMode());
						rebotPuppet.setPkVlaue(rebotExt.getPkVlaue());
						rebotPuppet.setNameColor(rebotExt.getNameColor());
						if(rebotExt.getPkVlaue() > 0){
							rebotPuppet.setPkValueUpdateTime(System.currentTimeMillis());
						}
						
						rebotPuppet.setP_attack(rebotProperty.getP_attack());
						rebotPuppet.setM_attack(rebotProperty.getM_attack());
						rebotPuppet.setP_damage(rebotProperty.getP_damage());
						rebotPuppet.setM_damage(rebotProperty.getM_damage());
						rebotPuppet.setCrit(rebotProperty.getCrit());
						rebotPuppet.setTough(rebotProperty.getTough());
						
						rebotPuppet.setDmgDeepPer(rebotProperty.getDmgDeepPer());
						rebotPuppet.setDmgReductPer(rebotProperty.getDmgReductPer());
						rebotPuppet.setDmgCritPer(rebotProperty.getDmgCritPer());
						rebotPuppet.setMoveSpeed(rebotProperty.getMoveSpeed());
							
						// 随机出生点
						PlayerPuppet pPuppet = sceneService.getPlayerPuppet(playerId);

						int[] point = CommonUtil.getRandomPoint(baseMap, pPuppet.getX(), pPuppet.getZ(), 500);
						rebotPuppet.setX(point[0]);
						rebotPuppet.setY(rebotExt.getY());
						rebotPuppet.setZ(point[1]);						
						
						Map<Long, PlayerPuppet> map = (Map<Long, PlayerPuppet>)CacheService.getFromCache(CacheConstant.PLAYER_PUPPET_CACHE);
						map.put(rebotPlayerId, rebotPuppet);
					}
					
					rebotPuppet.setState(BattleConstant.STATE_NORMAL);					
					rebotPuppet.setLogoutTime(0);
					rebotPuppet.setMapId(mapId);
					rebotPuppet.setLine(line);
					rebotPuppet.setSceneGuid(sceneGuid);
					rebotPuppet.setDirection(rebotExt.getDirection());
					rebotPuppet.setPickUp(false);
					rebotPuppet.setRebot(true);
					
					rebotExt.setMapId(rebotPuppet.getMapId());
					rebotExt.setX(rebotPuppet.getX());
					rebotExt.setY(rebotPuppet.getY());
					rebotExt.setZ(rebotPuppet.getZ());

					int gridId = NineGridUtil.calInGrid(rebotPuppet.getX(), rebotPuppet.getZ(), sceneModel.getColNum());
					rebotPuppet.setGridId(gridId);
					
					Map<String, PlayerPuppet> playerMap = sceneModel.getPlayerPuppetMap().get(gridId);
					if(playerMap == null){
						playerMap = new ConcurrentHashMap<String, PlayerPuppet>();
						sceneModel.getPlayerPuppetMap().put(gridId, playerMap);
					}
					playerMap.put(rebotPuppet.getGuid(), rebotPuppet);
					
					List<Long> playerIds = sceneModel.getPlayerIdMap().get(gridId);
					if(playerIds == null){
						playerIds = Collections.synchronizedList(new ArrayList<Long>());
						sceneModel.getPlayerIdMap().put(gridId, playerIds);
					}
					if (!playerIds.contains(rebotPlayerId)) {
						playerIds.add(rebotPlayerId);
					}
				
					// 进入场景通知给别人					
					if(!playerIds.isEmpty()){
						S_AddPlayerPuppets.Builder builder1 = S_AddPlayerPuppets.newBuilder();
						builder1.addListPlayerPuppets(protoBuilderService.buildPlayerPuppetMsg(rebotPuppet));
						MessageObj msg1 = new MessageObj(MessageID.S_AddPlayerPuppets_VALUE, builder1.build().toByteArray());
						serviceCollection.getGameSocketService().sendDataToPlayerList(playerIds, msg1);
					}
				}				
		}
	}

	private int synSkill(BasePuppet basePuppet, int skillId,
			int type, int direction, int x, int y, int z, String targetId) throws Exception {
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		ISkillService skillService = serviceCollection.getSkillService();		
		SceneModel sceneModel = serviceCollection.getSceneService().getSceneModel(basePuppet.getSceneGuid());
		if(sceneModel.getSceneState() == SceneConstant.SCENE_STATE_DISTROY) return 0;
		
		BaseSkill baseSkill = skillService.getBaseSkill(skillId);
		List<Long> playerIds = serviceCollection.getSceneService().getNearbyPlayerIds(basePuppet);
		if(playerIds != null && !playerIds.isEmpty()){
			int wigId = 0;
			if(baseSkill.getN32LifeTime() > 0){
				
				int gridId = NineGridUtil.calInGrid(x, z, sceneModel.getColNum());
				BlockingQueue<WigSkillInfo> wigInfos = sceneModel.getWigSkillMap().get(gridId);
				if(wigInfos == null){
					wigInfos = new LinkedBlockingQueue<WigSkillInfo>();
					sceneModel.getWigSkillMap().put(gridId, wigInfos);
				}
				WigSkillInfo wigSkillInfo = new WigSkillInfo(basePuppet.getGuid(), skillId, x, y, z, gridId, System.currentTimeMillis() + baseSkill.getN32LifeTime());
				wigInfos.offer(wigSkillInfo);
				
				wigId = wigSkillInfo.getWigId();
			}
			
			S_SynSkill.Builder builder = S_SynSkill.newBuilder();
			builder.setGuid(basePuppet.getGuid());
			builder.setSkillId(skillId);
			builder.setType(type);
			builder.setDirection(direction);
			builder.setTargetPoint(serviceCollection.getProtoBuilderService().buildVector3Msg(x,y,z));
			builder.setTargetId(targetId);
			builder.setWigId(wigId);
			
			MessageObj msg = new MessageObj(MessageID.S_SynSkill_VALUE, builder.build().toByteArray());
			serviceCollection.getGameSocketService().sendDataToPlayerList(playerIds, msg);	
			
			return wigId;
		}
		return 0;
	}
	
	/**
	 *  处理机器人ai
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void dealRebotAI(PlayerPuppet rebotPuppet, SceneModel model){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		ISceneService sceneService = serviceCollection.getSceneService();
		ISkillService skillService = serviceCollection.getSkillService();
		
		if(!rebotPuppet.isRebot()) return;
		
		int random = RandomService.getRandomNum(0, 1);
		if(random == 0){
			try {				
				
				if(System.currentTimeMillis() - rebotPuppet.getAttackTime() < 1000) return;
				
				List<PlayerSkill> playerSkills = (List<PlayerSkill>)CacheService.getFromCache(CacheConstant.PLAYER_SKILL+rebotPuppet.getEid());
				if(playerSkills == null || playerSkills.isEmpty()) return;
				Collections.shuffle(playerSkills); 
				PlayerSkill playerSkill = playerSkills.get(0);
			
				// 找目标
				PlayerPuppet targetPuppet = this.getTargetPuppet(rebotPuppet, sceneService.getNearbyPlayerIdsByGridId(model, rebotPuppet.getGridId()));
				if(targetPuppet == null){
					return;
				}
				
				BaseSkill baseSkill = skillService.getBaseSkill(playerSkill.getSkillId());
				
				//攻击范围范围  TODO 
				int x = targetPuppet.getX();
				int z = targetPuppet.getZ();
				int space = (x - rebotPuppet.getX())*(x-rebotPuppet.getX()) + (z-rebotPuppet.getZ())*(z-rebotPuppet.getZ());
				
				if(space > baseSkill.getfReleaseDist()*baseSkill.getfReleaseDist()){
					return;
				}			
				
				int wigId = this.synSkill(rebotPuppet, playerSkill.getSkillId(), baseSkill.getSkillType(), targetPuppet.getDirection(), targetPuppet.getX(), targetPuppet.getY(), targetPuppet.getZ(), targetPuppet.getEid() + "");
				
				List<BasePuppet> targers = new ArrayList<BasePuppet>();
				targers.add(targetPuppet);
				
				Map<Integer, Integer> baseRebotSkill = (Map<Integer, Integer>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_REBOT_ID);
				this.dealSkill(rebotPuppet, playerSkill.getSkillId(), targers, baseRebotSkill.get(playerSkill.getSkillId()), wigId);
				
			} catch (Exception e) {					
				e.printStackTrace();
			}
		}else{
			if(System.currentTimeMillis() - rebotPuppet.getAttackTime() < 1000) return;
			
			BaseMap baseMap = sceneService.getBaseMap(rebotPuppet.getMapId());
			int[] point = CommonUtil.getRandomPoint(baseMap, rebotPuppet.getX(), rebotPuppet.getZ(), 500);
			sceneService.synPosition(rebotPuppet.getEid(), 1, point[0], rebotPuppet.getY(), point[1], 0, true);
		}	
		rebotPuppet.setAttackTime(System.currentTimeMillis());
	}
	
	
	@SuppressWarnings("unchecked")
	private void openAllSkill(long playerId){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		Player rebot = playerService.getPlayerByID(playerId);
		
		BaseNewRole baseNewRole = serviceCollection.getCommonService().getBaseNewRole(rebot.getCareer());
		List<Integer> skillIDs = baseNewRole.getInitSkillList();
		
		List<PlayerSkill> lists = (List<PlayerSkill>)CacheService.getFromCache(CacheConstant.PLAYER_SKILL+playerId);
		
		if(lists == null){
			lists = new ArrayList<PlayerSkill>();
			CacheService.putToCache(CacheConstant.PLAYER_SKILL+playerId, lists);
		}

		Map<Integer, BaseSkill> skillMap = (Map<Integer, BaseSkill>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_SKILL);
		for (Integer skillId : skillIDs) {
			
			BaseSkill baseSkill = skillMap.get(skillId);
			
			if(baseSkill.getbIfNomalAttack() == 1) continue;
			
			PlayerSkill playerSkill  = this.studyPlayerSkill(playerId, skillId);
			lists.add(playerSkill);			
		}
	}

	private PlayerSkill studyPlayerSkill(long playerId, int skillId){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		ISkillService skillService = serviceCollection.getSkillService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_SKILL)) {
			BaseSkill baseSkill = skillService.getBaseSkill(skillId);
			if(baseSkill == null) throw new GameException(ExceptionConstant.SKILL_1600);
			
			PlayerProperty rebotProperty = serviceCollection.getPlayerService().getPlayerPropertyById(playerId);
			rebotProperty.setSkillLv(rebotProperty.getSkillLv() + 1);
			
			PlayerSkill playerSkill = new PlayerSkill();
			playerSkill.setId(IDUtil.geneteId(PlayerSkill.class));
			playerSkill.setPlayerId(playerId);
			playerSkill.setSkillId(skillId);
			playerSkill.setLevel(1);
			playerSkill.setSkillIndex(baseSkill.getSkillIndex());
			
			return playerSkill;
		}
		
	}


	private PlayerPuppet getTargetPuppet(PlayerPuppet rebotPuppet, List<Long> targetIds){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		ISceneService sceneService = serviceCollection.getSceneService();
		for(Long id : targetIds){
			PlayerPuppet targetPuppet = sceneService.getPlayerPuppet(id);
			if(targetPuppet.getType() != PlayerConstant.PLAYER || 
					targetPuppet.getEid() > Long.valueOf("10000000000000") ||
					rebotPuppet.getEid() == id) continue;
			
			return targetPuppet;
		}
		
		return null;
	}
	
	/**
	 * 处理技能
	 */
	private void dealSkill(BasePuppet basePuppet, int skillId, List<BasePuppet> targetList, int accountModelId, int wigId){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		ISkillService skillService = serviceCollection.getSkillService();
		ISceneService sceneService = serviceCollection.getSceneService();
		
		DamagModel damagModel = skillService.getDamagModel(accountModelId);
		
		if(damagModel == null) return;
		
		List<BasePuppet> listTargets = new ArrayList<BasePuppet>();
		List<Integer> listDmgs = new ArrayList<Integer>();
		
		S_SkillResult.Builder builder = S_SkillResult.newBuilder();
		for(BasePuppet target : targetList){
			
			// 过滤死亡者
			if(target.getHp() < 1){
				return;
			}
			
			int[] resultArr = calDamge(basePuppet, target, damagModel);
			int dmg = 0;
			int fightType = 0;
			if(resultArr != null){
				dmg = resultArr[0];
				fightType = resultArr[1];
				SkillEffect effect = new SkillEffect(target.getGuid(), dmg, target.getHp(), fightType);
				builder.addSkillEffect(protoBuilderService.buildSkillEffectMsg(effect));
			}		
			
			listTargets.add(target);
			listDmgs.add(dmg);
		}
		
		builder.setGuid(basePuppet.getGuid());
		builder.setSkillId(skillId);
		
		builder.setWigId(wigId);
		builder.setAccountModelId(accountModelId);
		
		MessageObj msg = new MessageObj(MessageID.S_SkillResult_VALUE, builder.build().toByteArray());
		gameSocketService.sendDataToPlayerList(sceneService.getNearbyPlayerIds(basePuppet), msg);
		
		this.attack(basePuppet, listTargets, listDmgs);
	}
	
	/**
	 * 攻击
	 */
	private void attack(BasePuppet basePuppet, List<BasePuppet> listTargets, List<Integer> listDmgs){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		ISceneService sceneService = serviceCollection.getSceneService();
		
		SceneModel sceneModel = sceneService.getSceneModel(basePuppet.getSceneGuid());
		if(sceneModel.getSceneState() == SceneConstant.SCENE_STATE_DISTROY) return;
		
		
		for(int i = 0; i< listTargets.size(); i++){
			BasePuppet target = listTargets.get(i);
			int dmg = listDmgs.get(i);
			// 受伤
			if(dmg > 0){
				if(target.getType() == PlayerConstant.PLAYER){
					PlayerPuppet targeter = (PlayerPuppet)target;
					
					playerService.synPlayerPropertyToAll(targeter, ProdefineConstant.HP, targeter.getHp());
					playerService.synPlayerPropertyToAll(targeter, ProdefineConstant.MP, targeter.getMp());
				}
			}
		}	
	
	}

	/**
	 * 伤害计算
	 */
	private int[] calDamge(BasePuppet attacker, BasePuppet target, DamagModel damagModel){
		int damage = 0;
		int fightResult = BattleConstant.FIGHT_RESULT_NOMAL;
		
		if(target.getType() == PlayerConstant.MONSTER){
			MonsterPuppet monsterPuppet = (MonsterPuppet)target;
			if(monsterPuppet.getAiState() == BattleConstant.AI_STATE_GOBACK || monsterPuppet.getAiState() == BattleConstant.AI_STATE_IMMUNE){
				return new int[]{0, BattleConstant.FIGHT_RESULT_MISS};
			}
		}
		
		if(damagModel.geteEffectCate() == SkillConstant.DAMAGE_P_ATTACK){
			// 基础伤害
			double hurt = attacker.getP_attack() - target.getP_damage() + target.getP_damage() * Math.pow(Math.E, 1-Math.pow(Math.E, attacker.getP_attack() * 1.0 / target.getP_damage()));
	
			// 技能系数
			hurt = hurt * (damagModel.getN32EffectRate()/1000.0) + damagModel.getN32EffectAdd();
			
			// 暴击判定
			double critRandom = (attacker.getCrit() - target.getTough())*0.001;
			if(Math.random() < critRandom){
				hurt = hurt * (attacker.getDmgCritPer() * 0.0001);
				
				fightResult = BattleConstant.FIGHT_RESULT_CRIT;
			}
			
			// 伤害加深减免
			hurt = hurt * (1 + (attacker.getDmgDeepPer() - target.getDmgReductPer())*0.0001);
			
			double random = Math.random()*0.2 + 0.9;
			damage = (int)(Math.ceil((hurt*random)));
			damage = Math.max(damage, 1);
			target.setHp(target.getHp() - damage);
		}else if(damagModel.geteEffectCate() == SkillConstant.DAMAGE_M_ATTACK){
			// 基础伤害
			double hurt = attacker.getM_attack() - target.getM_damage() + target.getM_damage() * Math.pow(Math.E, 1-Math.pow(Math.E, attacker.getM_attack() * 1.0 / target.getM_damage()));
	
			// 技能系数
			hurt = hurt * (damagModel.getN32EffectRate()/1000.0) + damagModel.getN32EffectAdd();
			
			// 暴击判定
			double critRandom = (attacker.getCrit() - target.getTough())*0.001;
			if(Math.random() < critRandom){
				hurt = hurt * (attacker.getDmgCritPer() * 0.0001);
				
				fightResult = BattleConstant.FIGHT_RESULT_CRIT;
			}
			
			// 伤害加深减免
			hurt = hurt * (1 + (attacker.getDmgDeepPer() - target.getDmgReductPer())*0.0001);
			
			double random = Math.random()*0.2 + 0.9;
			damage = (int)(Math.ceil((hurt*random)));
			damage = Math.max(damage, 1);
			target.setHp(target.getHp() - damage);
		}else if(damagModel.geteEffectCate() == SkillConstant.DAMAGE_M_ATTACK_RESUME){
			damage = (int)((attacker.getM_attack()*(damagModel.getN32EffectRate()/1000.0) + damagModel.getN32EffectAdd()));
			damage = Math.max(0, damage);
			target.setHp(target.getHp() + damage);
			damage = -damage;
		}else{
			return null;
		}
		
		int[] arr = new int[2];
		arr[0] = damage;
		arr[1] = fightResult;
		return arr;
	}
}


