 package com.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cache.BaseCacheService;
import com.cache.CacheService;
import com.common.DateService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.LockService;
import com.common.ServiceCollection;
import com.constant.BattleConstant;
import com.constant.CacheConstant;
import com.constant.ChatConstant;
import com.constant.ExceptionConstant;
import com.constant.FamilyConstant;
import com.constant.GuildConstant;
import com.constant.InOutLogConstant;
import com.constant.LockConstant;
import com.constant.PlayerConstant;
import com.constant.SceneConstant;
import com.dao.map.BaseMapDAO;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.Position;
import com.domain.Reward;
import com.domain.battle.DropItemInfo;
import com.domain.battle.WigSkillInfo;
import com.domain.chat.Notice;
import com.domain.collect.BaseCollect;
import com.domain.collect.Collect;
import com.domain.guild.Guild;
import com.domain.guild.GuildFight;
import com.domain.map.BaseMap;
import com.domain.map.Huanjing;
import com.domain.map.MonsterInfo;
import com.domain.map.Transfer;
import com.domain.monster.BaseRefreshMonster;
import com.domain.player.Player;
import com.domain.player.PlayerExt;
import com.domain.player.PlayerProperty;
import com.domain.puppet.BasePuppet;
import com.domain.puppet.BeckonPuppet;
import com.domain.puppet.EnemyModel;
import com.domain.puppet.MonsterPuppet;
import com.domain.puppet.PlayerPuppet;
import com.domain.puppet.PuppetState;
import com.domain.tianti.BaseDropItem;
import com.domain.tianti.PlayerTianti;
import com.domain.tower.BaseTower;
import com.domain.vip.PlayerVip;
import com.message.ChatProto.ParamType;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.SceneProto.PlayerPuppetMsg;
import com.message.SceneProto.S_AddPlayerPuppets;
import com.message.SceneProto.S_EnterScene;
import com.message.SceneProto.S_GetSceneElementList;
import com.message.SceneProto.S_RemovePuppets;
import com.message.SceneProto.S_SynMonsterState;
import com.message.SceneProto.S_SynPosition;
import com.message.SceneProto.S_UpdatePosition;
import com.scene.SceneModel;
import com.scene.SceneServer;
import com.service.IBuffService;
import com.service.IChatService;
import com.service.ICollectService;
import com.service.IGuildService;
import com.service.IMonsterService;
import com.service.IPlayerService;
import com.service.IProtoBuilderService;
import com.service.ISceneService;
import com.service.ITowerService;
import com.util.CommonUtil;
import com.util.LogUtil;
import com.util.NineGridUtil;
import com.util.PlayerUtil;
import com.util.SerialNumberUtil;
import com.util.SplitStringUtil;

/**
 * 场景系统
 * @author ken
 * @date 2016-12-28
 */
public class SceneService implements ISceneService {

	private BaseMapDAO baseMapDAO = new BaseMapDAO();
	
	
	@Override
	public void initBaseCache() {
		
		ICollectService collectService = GameContext.getInstance().getServiceCollection().getCollectService();
		Map<Integer, BaseMap> map = new HashMap<Integer, BaseMap>();
		Map<Integer, Transfer> transferMap = new HashMap<Integer, Transfer>();
		
		List<BaseMap> baseMaps = baseMapDAO.listBaseMaps();
		for(BaseMap model : baseMaps){
			// 进入副本消耗		
			if(model.getExpendStr() != null){			
				model.setExpendList(SplitStringUtil.getRewardInfo(model.getExpendStr()));
			}		
			
			try {
				//地图资源数据
				JSONObject json = CommonUtil.initSceneInfo(model.getMap_id());
				
				//出生点数据
				if(json != null && json.has("playerSpawn")){
					JSONArray positions = json.getJSONArray("playerSpawn");
					
					List<Position> lists = new ArrayList<Position>();
					for(int i = 0; i < positions.length(); i++){
						JSONObject js = (JSONObject)positions.get(i);
						JSONObject positionJS = js.getJSONObject("location");
						int x = (int) (positionJS.getDouble("x") * 100);
						int y = (int) (positionJS.getDouble("y") * 100);
						int z = (int) (positionJS.getDouble("z") * 100);
						if(x == 0 && y == 0 && z == 0){
							System.out.println("json playerSpawn location is null with mapId is "+model.getMap_id());
						}
						Position position = new Position(x, y, z);
						lists.add(position);
					}
					model.setRevivePositions(lists);
				}
				
				//怪物数据
				if (json != null && json.has("monsterSpawn")) {
					JSONArray monsters = json.getJSONArray("monsterSpawn");
					
					List<MonsterInfo> monsterInfos = new ArrayList<MonsterInfo>();
					for (int i = 0; i < monsters.length(); i++) {
						JSONObject monsterJS = (JSONObject) monsters.get(i);
						int refreshMonsterId = monsterJS.getInt("model");
						JSONObject positionJS = monsterJS.getJSONObject("location");
						int x = (int) (positionJS.getDouble("x") * 100);
						int y = (int) (positionJS.getDouble("y") * 100);
						int z = (int) (positionJS.getDouble("z") * 100);
						
						if(x == 0 && y == 0 && z == 0){
							System.out.println("json monsterSpawn location is null with mapId is "+model.getMap_id());
						}
						
						JSONObject rotationJS = monsterJS.getJSONObject("rotation");
						int direction = rotationJS.getInt("y");
						MonsterInfo monsterInfo = new MonsterInfo(refreshMonsterId, x, y, z, direction);
						monsterInfos.add(monsterInfo);
						
					}
					model.setMonsterInfos(monsterInfos);
				}
				
				//网格数据
				if (json != null && json.has("block")) {
					JSONArray rows = json.getJSONArray("block");
					model.setMapRow(rows.length());
					
					int[][] blockInfos = new int[rows.length()][];
					for(int i = 0; i< rows.length(); i++){
						JSONArray columns = (JSONArray)rows.get(i);
						int[] arr = new int[columns.length()];
						for(int k = 0; k< columns.length(); k++){
							int column = (Integer)columns.get(k);
							arr[k] = column;
						}
						blockInfos[i] = arr;
					}
					
					model.setMapColumn(blockInfos[0].length);
					model.setBlocks(blockInfos);
					
					//初始区域格子
					NineGridUtil.initGrid(model);
				}
				
				//传送门
				if (json != null && json.has("transfer")) {					
					JSONArray transfers = json.getJSONArray("transfer");
					for (int i = 0; i < transfers.length(); i++) {
						JSONObject monsterJS = (JSONObject) transfers.get(i);
						int transferId = monsterJS.getInt("model");							
							// 传送数据
							int toMapId = monsterJS.getInt("toScene");
							String[] toLocations = monsterJS.getString("toLocation").split(",");
							int x = (int) (Double.valueOf(toLocations[0]) * 100);
							int y = (int) (Double.valueOf(toLocations[1]) * 100);
							int z = (int) (Double.valueOf(toLocations[2]) * 100);
							
							Transfer transfer = new Transfer(transferId, toMapId, x, y, z);
							transferMap.put(transferId, transfer);
						}
					}			
				
				
				// 高级采集
				List<BaseCollect> seniorlist = collectService.getBaseCollectByType(BattleConstant.COLLECT_SENIOR);
				for(BaseCollect baseCollect : seniorlist){
					if(baseCollect.getMapId() != model.getMap_id())continue;
					model.getCollectIds().add(baseCollect.getId());
				}				
				
				// 任务采集
				List<BaseCollect> taskList = collectService.getBaseCollectByType(BattleConstant.COLLECT_TASK);
				for(BaseCollect baseCollect : taskList){
					if(baseCollect.getMapId() != model.getMap_id())continue;
					model.getCollectIds().add(baseCollect.getId());				
				}				
			} catch (JSONException e) {
				e.printStackTrace();
			}			

			map.put(model.getMap_id(), model);		
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_MAP, map);
		
		BaseCacheService.putToBaseCache(CacheConstant.BASE_TRANSFER, transferMap);
		
		
		Map<Integer,Map<Integer, BaseDropItem>> baseDropItemMap = new HashMap<Integer,Map<Integer, BaseDropItem>>();
		List<BaseDropItem> listBaseDropItem = baseMapDAO.listBaseDropItem();
		for(BaseDropItem baseDropItem : listBaseDropItem){		
			baseDropItem.setPosList(SplitStringUtil.getIntList(baseDropItem.getPosition()));
						
			List<List<Integer>> dropList = SplitStringUtil.getIntIntList(baseDropItem.getDropItem());
			for(List<Integer> di : dropList){
				int groupId = di.get(0);
				Reward reward = new Reward(di.get(1), di.get(2), di.get(3), di.get(5), di.get(4));
				
				List<Reward> drList = baseDropItem.getDropItemMap().get(groupId);
				if(drList == null){
					drList = new ArrayList<>();
				}
				drList.add(reward);
				
				baseDropItem.getDropItemMap().put(groupId, drList);
			}
			
			Map<Integer, BaseDropItem> dimap = baseDropItemMap.get(baseDropItem.getMapId());
			if(dimap == null){
				dimap = new HashMap<Integer, BaseDropItem>();
				baseDropItemMap.put(baseDropItem.getMapId(), dimap);
			}			
			dimap.put(baseDropItem.getId(), baseDropItem);
		}	
		
		BaseCacheService.putToBaseCache(CacheConstant.BASE_DROP_ITEM, baseDropItemMap);
		
	}
	
	/**
	 * 创建场景缓存
	 */
	private SceneModel createScene(String sceneGuid){
		SceneModel sceneModel = new SceneModel();
		sceneModel.setSceneGuid(sceneGuid);
		sceneModel.setSceneId(SerialNumberUtil.getSceneId());
		
		return sceneModel;
	}
	
	/**
	 * 初始场景数据
	 */
	private void initScene(SceneModel sceneModel,BaseMap baseMap, int line, int curLayerId){
		this.destroy(sceneModel);
		
		sceneModel.setMapId(baseMap.getMap_id());
		sceneModel.setMapName(baseMap.getMap_name());
		sceneModel.setLine(line);
		sceneModel.setMapType(baseMap.getMapType());
		sceneModel.setGridMap(baseMap.getGridMap());
		sceneModel.setColNum(baseMap.getColNum());
		sceneModel.setMapColumn(baseMap.getMapColumn());
		sceneModel.setMapRow(baseMap.getMapRow());
		sceneModel.setLifeTime(baseMap.getLifeTime());
		sceneModel.setWaitingTime(baseMap.getWaitingTime());
		sceneModel.setSceneState(SceneConstant.SCENE_STATE_COMMON);
		
		if(baseMap.getLifeTime() > 0){
			sceneModel.setEndTime(System.currentTimeMillis() + baseMap.getLifeTime());
		}
		
		if(baseMap.isInstance()){
			sceneModel.setExecAIFlag(false);
		}
		
		List<MonsterInfo> monsterInfos = baseMap.getMonsterInfos();
		if (monsterInfos != null && !monsterInfos.isEmpty()) {
			
			IMonsterService monsterService = GameContext.getInstance().getServiceCollection().getMonsterService();
			for (MonsterInfo monsterInfo : monsterInfos) {
				BaseRefreshMonster baseRefreshMonster =  monsterService.getBaseRefreshMonster(monsterInfo.getRefreshMonsterId());
				if(baseRefreshMonster == null){
					System.out.println("this baseRefreshMonster is null with id is "+monsterInfo.getRefreshMonsterId()+"   mapId is "+baseMap.getMap_id());
					continue;
				}
				baseRefreshMonster.setxRefresh(monsterInfo.getX());
				baseRefreshMonster.setyRefresh(monsterInfo.getY());
				baseRefreshMonster.setzRefresh(monsterInfo.getZ());
				
				baseRefreshMonster.setDirection(monsterInfo.getDirection());
				
				monsterService.refreshMonsters(sceneModel, baseRefreshMonster.getID(), curLayerId, false);

			}
		}
		
		//大荒塔第一波怪物
		if(curLayerId > 0){
			if(baseMap.getMapType() == SceneConstant.TOWER_SCENE){
				sceneModel.setCurMonNum(1);
				
				IMonsterService monsterService = GameContext.getInstance().getServiceCollection().getMonsterService();
				ITowerService towerService = GameContext.getInstance().getServiceCollection().getTowerService();
				BaseTower baseTower = towerService.getBaseTowerById(curLayerId);
				
				BaseRefreshMonster baseRefreshMonster = baseTower.getRefMonList().get(0);
				monsterService.refreshMonsters(sceneModel, baseRefreshMonster.getID(), curLayerId, false);	
			}
			
		}
		
		// 初始场景采集数据
		ICollectService collectService = GameContext.getInstance().getServiceCollection().getCollectService();
		for (Integer collectId : baseMap.getCollectIds()){			
			BaseCollect baseCollect = collectService.getBaseCollectById(collectId);
			if (baseCollect == null) continue;
			collectService.createCollect(sceneModel, baseCollect);
		}
		
		//初始普通采集数据
		Map<Integer, Boolean> gCollectMap = collectService.getGeneralCollectCacheMap(line);
		for(Map.Entry<Integer, Boolean> entry : gCollectMap.entrySet()){
			BaseCollect baseCollect = collectService.getBaseCollectById(entry.getKey());
			if(baseCollect.getMapId() == baseMap.getMap_id()){
				collectService.createCollect(sceneModel, baseCollect);
			}
		}		
				
		// 场景掉落
		if(sceneModel.getMapType() == SceneConstant.TIANTI_SCENE){
			List<BaseDropItem> list = this.getBaseDropItemByMapId(sceneModel.getMapId());
			List<BaseDropItem> copyList = new ArrayList<BaseDropItem>(list);
			Collections.shuffle(copyList);
			sceneModel.setBaseDropItems(copyList);	
		}
		
		SceneServer.getInstance().addSceneModel(sceneModel);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initCache() {
		CacheService.putToCache(CacheConstant.PLAYER_PUPPET_CACHE, new ConcurrentHashMap<Long, PlayerPuppet>());
		
		//启动场景线程
		SceneServer.getInstance().start();
		
		Map<String, SceneModel> sceneModelMap = new ConcurrentHashMap<String, SceneModel>();
		Map<Integer, BaseMap> map = (Map<Integer, BaseMap>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_MAP);
		for(Map.Entry<Integer, BaseMap> entry : map.entrySet()){
			BaseMap baseMap = entry.getValue();
			//初始世界场景
			if (baseMap.getMapType() == SceneConstant.MAIN_CITY) {
				for (int line = 1; line <= SceneConstant.MAX_LINE; line++) {
					SceneModel sceneModel = this.createScene(PlayerUtil.getSceneGuid(baseMap.getMapType(), baseMap.getMap_id(), line));
					this.initScene(sceneModel, baseMap, line, 0);
					sceneModelMap.put(sceneModel.getSceneGuid(), sceneModel);
				}
			}
		}
		CacheService.putToCache(CacheConstant.SCENE_CACHE, sceneModelMap);
		
		//幻境开启
		Date endDate = DateService.addDateByType(DateService.getCurrentUtilDate(), Calendar.MINUTE, 30);
		Huanjing model = new Huanjing();
		model.setState(1);
		model.setEndTime(endDate.getTime());
		CacheService.putToCache(CacheConstant.HUANJING_CACHE, model);
	}
	
	/**
	 * 根据地图编号取地图表
	 */
	@SuppressWarnings("unchecked")
	public BaseMap getBaseMap(int mapId){
		Map<Integer, BaseMap> map = (Map<Integer, BaseMap>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_MAP);
		return map.get(mapId);
	}
	

	@SuppressWarnings("unchecked")
	private List<BaseDropItem> getBaseDropItemByMapId(int mapId){
		Map<Integer,Map<Integer, BaseDropItem>> baseDropItemMap = (Map<Integer,Map<Integer, BaseDropItem>>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_DROP_ITEM);
		Map<Integer, BaseDropItem> bdlmap = baseDropItemMap.get(mapId);		
		return new ArrayList<>(bdlmap.values());
	}	
	
	@SuppressWarnings("unchecked")
	@Override
	public Transfer getTransfer(int transferId) {
		Map<Integer, Transfer> map = (Map<Integer, Transfer>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_TRANSFER);
		return map.get(transferId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public SceneModel getSceneModel(String sceneGuid) {
		Map<String, SceneModel> sceneModelMap = (Map<String, SceneModel>)CacheService.getFromCache(CacheConstant.SCENE_CACHE);
		if(sceneModelMap == null) return null;
		
		return sceneModelMap.get(sceneGuid);
	}
	
	/**
	 * 进入场景前
	 */
	@Override
	public void enterScene(long playerId, int mapId, int transferId, boolean bLogin, String sceneGuid, int param) throws Exception {
		this.enterScene(playerId, mapId, transferId, null, bLogin, sceneGuid, param);
	}
	
	/**
	 * 进入场景
	 */
	@SuppressWarnings("unchecked")
	private void enterScene(long playerId, int mapId, int transferId, Position toPosition, boolean bLogin, String sceneGuid, int param) throws Exception{
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();

		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_SCENE)) {

			Player player = playerService.getPlayerByID(playerId);
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			
			if(mapId != playerExt.getMapId()){
				bLogin = false;
			}
			
			int line = playerExt.getLine();
			
			BaseMap baseMap = this.getBaseMap(mapId);

			if(baseMap == null){
				System.out.println("enterScene this baseMap is null with mapId is "+ mapId);
				return;
			}
			
			//TODO 数据清理检测补丁
//			if(bLogin){
//				SceneServer.getInstance().removePlayer(playerId, player.getGuid());
//			}
			
			if (baseMap.getMapType() == SceneConstant.MAIN_CITY) {

				sceneGuid = PlayerUtil.getSceneGuid(SceneConstant.TYPE_CITY, mapId, line);
			} else if (baseMap.getMapType() == SceneConstant.WORLD_SCENE) {
				if(sceneGuid == null){
					sceneGuid = PlayerUtil.getSceneGuid(SceneConstant.TYPE_WORLD, mapId, line);
				}
			} else if (baseMap.getMapType() == SceneConstant.TOWER_SCENE) {

				sceneGuid = PlayerUtil.getSceneGuid(SceneConstant.TYPE_TOWER, playerId);
			} else if(baseMap.getMapType() == SceneConstant.INSTANCE_SCENE){
				
				if(!bLogin){ 
					
					// 进入副本消耗
					if(baseMap.getExpendList() != null){
						serviceCollection.getRewardService().expendJudgment(playerId, baseMap.getExpendList(), true, InOutLogConstant.DIAMOND_OF_3);
					}
											
				}else{
					sceneGuid = playerExt.getSceneGuid();
				}

			} else if(baseMap.getMapType() == SceneConstant.TIANTI_SCENE){
				
				if(!bLogin){ 
					// 进入侍魂殿消耗

				}else{
					sceneGuid = playerExt.getSceneGuid();
				}
			}else if(baseMap.getMapType() == SceneConstant.GUILD_SCENE){
				
				sceneGuid = PlayerUtil.getSceneGuid(SceneConstant.TYPE_GUILD, mapId);
			}

			if(!bLogin){
				//先退出当前地图
				quitScene(playerId);
			}
			
			
			Map<String, SceneModel> sceneModelMap = (Map<String, SceneModel>)CacheService.getFromCache(CacheConstant.SCENE_CACHE);
			SceneModel sceneModel = sceneModelMap.get(sceneGuid);
			if(sceneModel == null){
				synchronized (sceneGuid) {
					if(sceneModel == null){
						sceneModel = this.createScene(sceneGuid);
						this.initScene(sceneModel, baseMap, line, param);
						sceneModelMap.put(sceneGuid, sceneModel);
					}
				}
			}
			if(sceneModel.getSceneState() != SceneConstant.SCENE_STATE_COMMON){
				this.initScene(sceneModel, baseMap, line, param);
			}
			
			playerExt.setSceneGuid(sceneGuid);
			
			// 初始化战斗对象
			PlayerPuppet playerPuppet = this.getPlayerPuppet(playerId);
			if(playerPuppet == null){
				playerPuppet = this.initPlayerPuppet(playerId);
				Map<Long, PlayerPuppet> map = (Map<Long, PlayerPuppet>)CacheService.getFromCache(CacheConstant.PLAYER_PUPPET_CACHE);
				map.put(playerId, playerPuppet);
			}
			
			if(!bLogin){
				if(toPosition == null){
					if(transferId > 0){
						// 传送门切场景
						Transfer transfer = this.getTransfer(transferId);
						if(transfer == null){
							System.out.println("Transfer is null with transferId is "+transferId+" and mapId is "+playerExt.getMapId());
							return;
						}
						playerPuppet.setX(transfer.getToX());
						playerPuppet.setY(transfer.getToY());
						playerPuppet.setZ(transfer.getToZ());
						
						if(baseMap.getMap_id() == GuildConstant.MAP_GUILD_7002){
							//进入诛仙台
							serviceCollection.getGuildService().enterZhuXianTai(playerId, playerPuppet.getPkType(), true);
						}
					}else{
						BaseMap fromBaseMap = this.getBaseMap(playerPuppet.getMapId());
						if(fromBaseMap.isInstance()){
							if(baseMap.isInstance()){
								
							}else{
								playerPuppet.setX(playerExt.getLastX());
								playerPuppet.setY(playerExt.getLastY());
								playerPuppet.setZ(playerExt.getLastZ());
								
								// 玩家从噬魂殿出来
								if(fromBaseMap.getMapType() == SceneConstant.TIANTI_SCENE){							
									playerPuppet.setHp(playerPuppet.getHpMax());								
									playerPuppet.setMp(playerPuppet.getMpMax());
								}
							}
						}else{
							if(baseMap.isInstance()){
								playerExt.setLastMapId(playerPuppet.getMapId());
								playerExt.setLastX(playerPuppet.getX());
								playerExt.setLastY(playerPuppet.getY());
								playerExt.setLastZ(playerPuppet.getZ());
							}else{
								//幻境地图是否开启 
								if(baseMap.isHuanjing() && !fromBaseMap.isHuanjing()){
									Huanjing model = (Huanjing)CacheService.getFromCache(CacheConstant.HUANJING_CACHE);
									if(model.getState() == 0){
										throw new GameException(ExceptionConstant.SCENE_1211);
									}
								}
							}
							//进入噬魂殿
							if(baseMap.getMapType() == SceneConstant.TIANTI_SCENE){
								Position position = baseMap.getRevivePositions().get(param);
								playerPuppet.setX(position.getX());
								playerPuppet.setY(position.getY());
								playerPuppet.setZ(position.getZ());
								
								playerPuppet.setPkMode(BattleConstant.PK_MODE_ALL);								
								playerPuppet.setHp(playerPuppet.getHpMax());								
								playerPuppet.setMp(playerPuppet.getMpMax());
								
							}else if(baseMap.getMapType() == SceneConstant.GUILD_SCENE){
								Position position = baseMap.getRevivePositions().get(param - 1);
								playerPuppet.setX(position.getX());
								playerPuppet.setY(position.getY());
								playerPuppet.setZ(position.getZ());
								
								playerPuppet.setPkMode(BattleConstant.PK_MODE_GUILD);								
								playerPuppet.setPkType(param);
								
								playerPuppet.setGuildFightUpdateTime(System.currentTimeMillis());
								
							}else{
								Collections.shuffle(baseMap.getRevivePositions());
								Position position = baseMap.getRevivePositions().get(0);
								playerPuppet.setX(position.getX());
								playerPuppet.setY(position.getY());
								playerPuppet.setZ(position.getZ());	
							}

						}
					}
				}else{
					// 传送道具切场景
					playerPuppet.setX(toPosition.getX());
					playerPuppet.setY(toPosition.getY());
					playerPuppet.setZ(toPosition.getZ());
				}
			}
			
			playerPuppet.setState(BattleConstant.STATE_NORMAL);
			if(playerPuppet.getHp() == 0){
				if(baseMap.getMapType() == SceneConstant.MAIN_CITY){
					playerPuppet.setHp(playerPuppet.getHpMax());
				}else{
					playerPuppet.setState(BattleConstant.STATE_DEAD);
				}
			}
			
			playerPuppet.setLogoutTime(0);
			playerPuppet.setMapId(mapId);
			playerPuppet.setLine(line);
			playerPuppet.setSceneGuid(sceneGuid);
			playerPuppet.setDirection(playerExt.getDirection());
			playerPuppet.setPickUp(false);
			
			playerExt.setMapId(playerPuppet.getMapId());
			playerExt.setX(playerPuppet.getX());
			playerExt.setY(playerPuppet.getY());
			playerExt.setZ(playerPuppet.getZ());

			int gridId = NineGridUtil.calInGrid(playerPuppet.getX(), playerPuppet.getZ(), sceneModel.getColNum());
			playerPuppet.setGridId(gridId);
			
			
			// 通知切图
			S_EnterScene.Builder builder = S_EnterScene.newBuilder();
			builder.setMapId(mapId);
			builder.setEndTime(sceneModel.getEndTime());
			builder.setPlayerPuppet(protoBuilderService.buildPlayerPuppetMsg(playerPuppet));
			
			//守城帮主npc展示
			if(baseMap.getMapType() == SceneConstant.MAIN_CITY){
				IGuildService guildService = serviceCollection.getGuildService();
				GuildFight guildFight = guildService.getGuildFightCache();
				if(guildFight.getGuildId() > 0){
					Guild guild = guildService.getGuildById(guildFight.getGuildId());
					if(guild != null){
						builder.setHeaderId(guild.getHeaderId());
					}
				}
			}
			
			MessageObj msg = new MessageObj(MessageID.S_EnterScene_VALUE,builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByUserId(player.getUserId(), msg);

		}
	}
	
	@Override
	public void transfer(long playerId, int mapId, Position toPosition) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_SCENE)) {
			PlayerPuppet playerPuppet = this.getPlayerPuppet(playerId);
			if(playerPuppet == null) throw new GameException(ExceptionConstant.PLAYER_1111);
			
			//副本中是否不能传送？
			BaseMap baseMap = this.getBaseMap(playerPuppet.getMapId());
			if(baseMap.isInstance()){
				if(!baseMap.isGuild()){
					 throw new GameException(ExceptionConstant.SCENE_1203);
				}
				
			}
			
			//神境地图需要vip
			if(baseMap.isShenjing()){
				PlayerVip playerVip = serviceCollection.getVipService().getPlayerVip(playerId);
				if(playerVip == null || playerVip.getLevel() < 1){
					throw new GameException(ExceptionConstant.SCENE_1210);
				}
			}
			
			//幻境地图是否开启 
			if(baseMap.isHuanjing()){
				Huanjing model = (Huanjing)CacheService.getFromCache(CacheConstant.HUANJING_CACHE);
				if(model.getState() == 0){
					throw new GameException(ExceptionConstant.SCENE_1212);
				}
			}
			
			this.enterScene(playerId, mapId, 0, toPosition, false, null, 0);
		}
	}
	
	/**
	 * 场景完成后  （设置进场景数据， 获取周边数据， 推送自己给周边， 通知给队友）
	 */
	@Override
	public void getSceneElementList(Long playerId) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_SCENE)) {
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			
			if(playerExt == null || playerExt.getSceneGuid() == null) return;
			
			SceneModel sceneModel = this.getSceneModel(playerExt.getSceneGuid());
			if(sceneModel.getSceneState() == SceneConstant.SCENE_STATE_DISTROY) return;
			
			synchronized (sceneModel.getLock()) {
				
				PlayerPuppet playerPuppet = this.getPlayerPuppet(playerId);
				
				if(playerPuppet == null) return;
				
				//把自己加载进场景数据
				int myGridId = playerPuppet.getGridId();
				Map<String, PlayerPuppet> pMap = sceneModel.getPlayerPuppetMap().get(myGridId);
				if(pMap == null){
					pMap = new ConcurrentHashMap<String, PlayerPuppet>();
					sceneModel.getPlayerPuppetMap().put(myGridId, pMap);
				}
				pMap.put(playerPuppet.getGuid(), playerPuppet);
				
				List<Long> playerIds = sceneModel.getPlayerIdMap().get(myGridId);
				if(playerIds == null){
					playerIds = Collections.synchronizedList(new ArrayList<Long>());
					sceneModel.getPlayerIdMap().put(myGridId, playerIds);
				}
				if (!playerIds.contains(playerId)) {
					playerIds.add(playerId);
				}
				
				S_GetSceneElementList.Builder builder = S_GetSceneElementList.newBuilder();
				
				List<Integer> gridIds = sceneModel.getGridMap().get(playerPuppet.getGridId());
				if(gridIds == null){
					System.out.println("gridIds is null whit gridId is"+playerPuppet.getGridId());
					return;
				}
				
				for(Integer gridId : gridIds){
					// 其他玩家
					Map<String, PlayerPuppet> playerMap = sceneModel.getPlayerPuppetMap().get(gridId);
					if(playerMap != null && !playerMap.isEmpty()){
						for(Map.Entry<String, PlayerPuppet> entry : playerMap.entrySet()){
							if (entry.getKey().equals(playerPuppet.getGuid())) {
								continue;
							}
							PlayerPuppetMsg.Builder msg = protoBuilderService.buildPlayerPuppetMsg(entry.getValue());
							if(msg == null) continue;
							
							builder.addListPlayerPuppets(msg);
						}
					}
					
					//怪物
					Map<String, MonsterPuppet> monsterMap = sceneModel.getMonsterPuppetMap().get(gridId);
					if(monsterMap != null && !monsterMap.isEmpty()){
						for(Map.Entry<String, MonsterPuppet> entry : monsterMap.entrySet()){
							MonsterPuppet m = entry.getValue();
							if(m.getState() == BattleConstant.STATE_NORMAL){
								if(sceneModel.getMapType() > SceneConstant.WORLD_SCENE){
									m.setPreAiTime(System.currentTimeMillis());
								}
								builder.addListMonsterPuppets(protoBuilderService.buildMonsterPuppetMsg(m));
							}
						}	
					}
					
					//召唤怪
					Map<String, BeckonPuppet> beckonMap = sceneModel.getBeckonPuppetMap().get(gridId);
					if(beckonMap != null && !beckonMap.isEmpty()){
						for(Map.Entry<String, BeckonPuppet> entry : beckonMap.entrySet()){
							BeckonPuppet m = entry.getValue();
							if(m.getState() == BattleConstant.STATE_NORMAL){
								builder.addListBeckonPuppets(protoBuilderService.buildBeckonPuppetMsg(m));
							}
						}	
					}
					
					
					//掉落
					Map<Integer, DropItemInfo> dropMap = sceneModel.getDropItemMap().get(gridId);
					if(dropMap != null && !dropMap.isEmpty()){
						for(Map.Entry<Integer, DropItemInfo> entry : dropMap.entrySet()){
							DropItemInfo dropItem = entry.getValue();
							if(dropItem.getState() == BattleConstant.DROP_NORMAL){
								builder.addListDropItemInfos(protoBuilderService.buildDropItemInfoMsg(dropItem));
							}
						}
					}
					
					//地效持续技能
					BlockingQueue<WigSkillInfo> wigInfos = sceneModel.getWigSkillMap().get(gridId);
					if(wigInfos != null && !wigInfos.isEmpty()){
						for(WigSkillInfo info : wigInfos){
							if(!info.isDeleteFlag()){
								builder.addListWigSkillInfos(protoBuilderService.buildWigSkillInfoMsg(info));
							}
						}
					}					
					
					//采集信息列表
					Map<Integer, Collect> collectMap = sceneModel.getCollectMap().get(gridId);
	 				if(collectMap != null && !collectMap.isEmpty()){
						for(Map.Entry<Integer, Collect> entry : collectMap.entrySet()){
							Collect collect = entry.getValue();
							if(collect.getState() != BattleConstant.COLLECT_NORMAL) continue;						
							builder.addListCollectItemInfos(protoBuilderService.buildCollectMsg(collect));							
						}
					}	
				}
	
				//下发周边元素
				MessageObj msg = new MessageObj(MessageID.S_GetSceneElementList_VALUE, builder.build().toByteArray());
				gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
					
				// 进入场景通知给别人
				List<Long> nearbyPlayerIds = new ArrayList<Long>(this.getNearbyPlayerIds(playerPuppet));
				nearbyPlayerIds.remove(playerId);
				if(!nearbyPlayerIds.isEmpty()){
					this.synSelfToNearbyPlayer(playerPuppet, nearbyPlayerIds);
				}
				
				// 切换场景同步队员位置信息给其他成员
				serviceCollection.getTeamService().synTeam(playerId);	
				
				//开始执行副本怪物ai 
				sceneModel.setExecAIFlag(true);
			}
		}
	}
	
	@Override
	public void quitScene(Long playerId) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		IBuffService buffService = serviceCollection.getBuffService();		
		
		PlayerPuppet playerPuppet = this.getPlayerPuppet(playerId);
		if(playerPuppet == null) {
//			LogUtil.error("quitScene playerPuppet is null!  playerId="+ playerId);
			return;
		}	
		
		// 清除 buff 			
		buffService.breakAddHpMp(playerId);
				
		SceneModel sceneModel = this.getSceneModel(playerPuppet.getSceneGuid());
		
		synchronized (sceneModel.getLock()) {
			
			if(sceneModel.getSceneState() == SceneConstant.SCENE_STATE_COMMON){
				if(sceneModel.getMapType() == SceneConstant.TIANTI_SCENE){
					serviceCollection.getTiantiService().end(sceneModel, 0, playerId);
				}
			}else if(sceneModel.getSceneState() == SceneConstant.SCENE_STATE_END){
				
			}else{
				return;
			}
			Map<String, PlayerPuppet> playerMap = sceneModel.getPlayerPuppetMap().get(playerPuppet.getGridId());
			if(playerMap == null || playerMap.isEmpty()) return;
			
			playerMap.remove(playerPuppet.getGuid());
			
			List<Long> playerIds = sceneModel.getPlayerIdMap().get(playerPuppet.getGridId());
			if(playerIds != null){
				if (playerIds.contains(playerId)) {
					playerIds.remove(playerId);
				}
			}
			
			//通知其他人
			this.removeSelfToNearby(playerPuppet.getGuid(), this.getNearbyPlayerIds(playerPuppet));
			//移除召唤怪
			serviceCollection.getMonsterService().removeBeckonPuppet(playerPuppet, sceneModel);
			
			//场景没有人，消耗副本
			if(sceneModel.getMapType() > SceneConstant.WORLD_SCENE){
				if(sceneModel.getMapType() == SceneConstant.GUILD_SCENE){
					//城战地图
					if(sceneModel.getMapId() == GuildConstant.MAP_GUILD_7002){
						serviceCollection.getGuildService().enterZhuXianTai(playerId, playerPuppet.getPkType(), false);
					}
				}else{
					if(sceneModel.getMapId() == GuildConstant.MAP_GUILD_7003){
						//帮派凌烟阁
					}else if(sceneModel.getMapId() == GuildConstant.MAP_GUILD_7004){
						//帮派领地
						if(sceneModel.getPlayerIdMap().isEmpty() && sceneModel.getMonsterPuppetMap().isEmpty()){
							this.destroy(sceneModel);
						}
					}else if(sceneModel.getMapId() == FamilyConstant.MAP_FAMILY_8001){
						//家族副本
					}else{
						//其他副本还要里面没人就销毁
						boolean nobody = true;
						for(Map.Entry<Integer, List<Long>> entry : sceneModel.getPlayerIdMap().entrySet()){
							if(entry.getValue() != null && !entry.getValue().isEmpty()){
								nobody = false;
								break;
							}
						}
						if(nobody){
							this.destroy(sceneModel);
						}	
					}

				}

			}
		}
	}	

	@Override
	public BasePuppet getBasePuppet(String sceneGuid, String guid, int fighterType) {
		
		if(fighterType == 0){
			fighterType = PlayerUtil.getType(guid);
		}
		
		SceneModel sceneModel = this.getSceneModel(sceneGuid);
		if(sceneModel.getSceneState() == SceneConstant.SCENE_STATE_DISTROY) return null;
		
		if(fighterType == PlayerConstant.PLAYER){
			for(Map.Entry<Integer, Map<String, PlayerPuppet>> entry : sceneModel.getPlayerPuppetMap().entrySet()){
				PlayerPuppet p = entry.getValue().get(guid);
				if(p != null){
					return p;
				}
			}
		}else if(fighterType == PlayerConstant.MONSTER){
			for(Map.Entry<Integer, Map<String, MonsterPuppet>> entry : sceneModel.getMonsterPuppetMap().entrySet()){
				MonsterPuppet m = entry.getValue().get(guid);
				if(m != null){
					return m;
				}
			}
			
		}else if(fighterType == PlayerConstant.BECKON){
			for(Map.Entry<Integer, Map<String, BeckonPuppet>> entry : sceneModel.getBeckonPuppetMap().entrySet()){
				BeckonPuppet b = entry.getValue().get(guid);
				if(b != null){
					return b;
				}
			}
		}
		return null;
	}
	
	@Override
	public List<Long> getScenePlayerIds(SceneModel sceneModel) {
		List<Long> playerIds = new ArrayList<Long>();
		for(Map.Entry<Integer, List<Long>> entry : sceneModel.getPlayerIdMap().entrySet()){
			playerIds.addAll(entry.getValue());
		}
		return playerIds;
	}
	
	@Override
	public List<Long> getNearbyPlayerIds(BasePuppet basePuppet) {
		SceneModel sceneModel = this.getSceneModel(basePuppet.getSceneGuid());
		
		return this.getNearbyPlayerIdsByGridId(sceneModel, basePuppet.getGridId());
	}

	@Override
	public List<Long> getNearbyPlayerIdsByGridId(SceneModel sceneModel, int gridId) {
		List<Long> playerIds = new ArrayList<Long>();
		if(gridId <= 0) return playerIds;
		
		List<Integer> gridIds = sceneModel.getGridMap().get(gridId);
		if(gridIds == null){
			System.out.println("getNearbyPlayerIdsByGridId is null with gridId is"+gridId+" mapId is "+sceneModel.getMapId());
			return playerIds;
		}
		for(Integer id : gridIds){
			List<Long> lists = sceneModel.getPlayerIdMap().get(id);
			if(lists != null){
				List<Long> copyIds = new CopyOnWriteArrayList<Long>(lists);
				
				List<Long> rmIds = new ArrayList<Long>();
				for(Long pid : copyIds){
					PlayerPuppet pp = this.getPlayerPuppet(pid);
					if(pp == null || pp.getMapId() != sceneModel.getMapId()){
						rmIds.add(pid);
						continue;
					}
					playerIds.add(pid);
				}
				if(!rmIds.isEmpty()){
					//TODO 无效地图玩家数据 补丁
					lists.removeAll(rmIds);
				}
			}
		}
		return playerIds;
	}
	
	@Override
	public void synPosition(Long playerId, int newState, int newX, int newY, int newZ, int newDiretion, boolean move){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IProtoBuilderService protoBuilderService  = serviceCollection.getProtoBuilderService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		PlayerPuppet playerPuppet = this.getPlayerPuppet(playerId);
		if(playerPuppet == null) return;
		
		if(playerPuppet.getState() != BattleConstant.STATE_NORMAL) return;
		
		if(playerPuppet.getVertigo() == 1 || playerPuppet.getFixed() == 1){
			//眩晕或者定身不能移动
			return;
		}
		
		SceneModel sceneModel = this.getSceneModel(playerPuppet.getSceneGuid());
		if(sceneModel.getSceneState() == SceneConstant.SCENE_STATE_DISTROY) return;
		
		int oldGridId = playerPuppet.getGridId(); //旧区域格
		
		playerPuppet.setDirection(newDiretion);
		playerPuppet.setX(newX);
		playerPuppet.setY(newY);
		playerPuppet.setZ(newZ);
		playerPuppet.setPuppetState(PuppetState.values()[newState]);
		
		//新区域格
		int newGridId = NineGridUtil.calInGrid(playerPuppet.getX(), playerPuppet.getZ(), sceneModel.getColNum());
		
		List<Integer> oldAroundGrids = sceneModel.getGridMap().get(oldGridId);
		List<Integer> newAroundGrids = sceneModel.getGridMap().get(newGridId);
		if(oldAroundGrids == null || newAroundGrids == null){
			return;
		}
		
		List<Long> playerIds = new ArrayList<Long>();
		List<Integer> ids = null;
		
		if(move){
			ids = NineGridUtil.getGridList(oldAroundGrids, newAroundGrids, true);
			for(Integer gid : ids){
				List<Long> l = sceneModel.getPlayerIdMap().get(gid);
				if(l!= null && !l.isEmpty()){
					playerIds.addAll(l);
				}
			}
			if(!playerIds.isEmpty()){
				List<Long> newIDList = new ArrayList<Long>(playerIds);
				newIDList.remove(playerId);
				if (!newIDList.isEmpty()){
					S_SynPosition.Builder builder = S_SynPosition.newBuilder();
					builder.setGuid(playerPuppet.getGuid());
					builder.setState(newState);
					builder.setPosition(protoBuilderService.buildVector3Msg(newX, newY, newZ));
					builder.setDirection(newDiretion);
					gameSocketService.sendDataToPlayerList(newIDList,  
							new MessageObj(MessageID.S_SynPosition_VALUE, builder.build().toByteArray()));
				}
			}
		}


		if(oldGridId != newGridId){
			playerPuppet.setGridId(newGridId);
			
			Map<String, PlayerPuppet> oldMap = sceneModel.getPlayerPuppetMap().get(oldGridId);
			if(oldMap != null){
				oldMap.remove(playerPuppet.getGuid());
			}
			
			Map<String, PlayerPuppet> pMap = sceneModel.getPlayerPuppetMap().get(newGridId);
			if(pMap == null){
				pMap = new ConcurrentHashMap<String, PlayerPuppet>();
				sceneModel.getPlayerPuppetMap().put(newGridId, pMap);
			}
			pMap.put(playerPuppet.getGuid(), playerPuppet);
			
			List<Long> oldPlayerIds = sceneModel.getPlayerIdMap().get(oldGridId);
			if(oldPlayerIds != null){
				oldPlayerIds.remove(playerId);
			}
			
			List<Long> newPlayerIds = sceneModel.getPlayerIdMap().get(newGridId);
			if(newPlayerIds == null){
				newPlayerIds = Collections.synchronizedList(new ArrayList<Long>());
				sceneModel.getPlayerIdMap().put(newGridId, newPlayerIds);
			}
			if(!newPlayerIds.contains(playerId)){
				newPlayerIds.add(playerId);
			}
			
			//离开视野
			playerIds.clear();
			ids = NineGridUtil.getGridList(oldAroundGrids, newAroundGrids, false);
			List<String> guids = new ArrayList<String>();
			List<Integer> dropIds = new ArrayList<Integer>();
			List<Integer> collectIds = new ArrayList<Integer>();
			for(Integer gid : ids){
				List<Long> l = sceneModel.getPlayerIdMap().get(gid);
				if(l != null && !l.isEmpty()){
					playerIds.addAll(l);
				}
				
				Map<String, PlayerPuppet> pM = sceneModel.getPlayerPuppetMap().get(gid);
				if(pM != null){
					for(Map.Entry<String, PlayerPuppet> entry : pM.entrySet()){
						guids.add(entry.getKey());
					}
				}
				
				Map<String, MonsterPuppet> monM = sceneModel.getMonsterPuppetMap().get(gid);
				if(monM != null && !monM.isEmpty()){
					for(Map.Entry<String, MonsterPuppet> entry : monM.entrySet()){
						guids.add(entry.getKey());
					}
				}
				
				Map<String, BeckonPuppet> beckonM = sceneModel.getBeckonPuppetMap().get(gid);
				if(beckonM != null && !beckonM.isEmpty()){
					for(Map.Entry<String, BeckonPuppet> entry : beckonM.entrySet()){
						guids.add(entry.getKey());
					}
				}
				
				Map<Integer, DropItemInfo> dropM = sceneModel.getDropItemMap().get(gid);
				if(dropM != null && !dropM.isEmpty()){
					for(Map.Entry<Integer, DropItemInfo> entry : dropM.entrySet()){
						dropIds.add(entry.getKey());
					}
				}
				
				Map<Integer, Collect> cMd = sceneModel.getCollectMap().get(gid);
				if(cMd != null && !cMd.isEmpty()){
					for(Map.Entry<Integer, Collect> entry : cMd.entrySet()){
						collectIds.add(entry.getKey());
					}
				}

			}
			
			//移除别人
			if(!guids.isEmpty()){
				this.removeNearbyToSelf(playerId, guids);
			}
			
			//别人移除自己
			if(!playerIds.isEmpty()){
				this.removeSelfToNearby(playerPuppet.getGuid(), playerIds);
			}
			
			//移除掉落
			if(!dropIds.isEmpty()){
				List<Long> pids = new ArrayList<Long>();
				pids.add(playerId);
				serviceCollection.getBattleService().removeDropItems(dropIds, pids);
			}
			
			//移除采集物
			if(!collectIds.isEmpty()){
				List<Long> pids = new ArrayList<Long>();
				pids.add(playerId);
				serviceCollection.getCollectService().offerRemoveCollect(collectIds, pids);
			}
			//进入视野
			playerIds.clear();
			ids = NineGridUtil.getGridList(newAroundGrids, oldAroundGrids, false);
			
			List<PlayerPuppet> playerPuppets = new ArrayList<PlayerPuppet>();
			List<MonsterPuppet> monsterPuppets = new ArrayList<MonsterPuppet>();
			List<BeckonPuppet> beckonPuppets = new ArrayList<BeckonPuppet>();
			List<DropItemInfo> drops = new ArrayList<DropItemInfo>();
			List<Collect> collects = new ArrayList<Collect>();
			for(Integer gid : ids){
				List<Long> l = sceneModel.getPlayerIdMap().get(gid);
				if(l!= null && !l.isEmpty())  playerIds.addAll(l);	                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
				
				Map<String, PlayerPuppet> pM = sceneModel.getPlayerPuppetMap().get(gid);
				if(pM != null){
					for(Map.Entry<String, PlayerPuppet> entry : pM.entrySet()){
						playerPuppets.add(entry.getValue());
					}
				}

				Map<String, MonsterPuppet> monM = sceneModel.getMonsterPuppetMap().get(gid);
				if(monM != null){
					for(Map.Entry<String, MonsterPuppet> entry : monM.entrySet()){
						MonsterPuppet m = entry.getValue();
						if(m.getState() == BattleConstant.STATE_NORMAL){
							monsterPuppets.add(entry.getValue());
						}
					}
				}
				
				Map<String, BeckonPuppet> beckonM = sceneModel.getBeckonPuppetMap().get(gid);
				if(beckonM != null){
					for(Map.Entry<String, BeckonPuppet> entry : beckonM.entrySet()){
						BeckonPuppet m = entry.getValue();
						if(m.getState() == BattleConstant.STATE_NORMAL){
							beckonPuppets.add(entry.getValue());
						}
					}
				}

				Map<Integer, DropItemInfo> dM = sceneModel.getDropItemMap().get(gid);
				if(dM != null){
					for(Map.Entry<Integer, DropItemInfo> entry : dM.entrySet()){
						DropItemInfo info = entry.getValue();
						if(info.getState() == BattleConstant.DROP_NORMAL){
							drops.add(info);
						}
					}
				}

				Map<Integer, Collect> cM = sceneModel.getCollectMap().get(gid);
				if(cM != null){
					for(Map.Entry<Integer, Collect> entry : cM.entrySet()){
						Collect collect = entry.getValue();
						if(collect.getState() == BattleConstant.COLLECT_NORMAL){
							collects.add(collect);
						}
					}
				}

			}
			
			//自己推送被周边人
			if(!playerIds.isEmpty()){
				this.synSelfToNearbyPlayer(playerPuppet, playerIds);	
			}
			
			//把新视野玩家推送给自己
			if(!playerPuppets.isEmpty()){
				this.synNearbyPlayerToSelf(playerId, playerPuppets);
			}
			
			//把新视野怪物推送给自己
			if(!monsterPuppets.isEmpty()){
				serviceCollection.getMonsterService().synNearbyMonsterToSelf(playerId, monsterPuppets);
			}
			
			//把新视野召唤怪推送给自己
			if(!beckonPuppets.isEmpty()){
				serviceCollection.getMonsterService().synNearbyBeckonToSelf(playerId, beckonPuppets);
			}
			
			//把掉落推送给自己
			if(!drops.isEmpty()){
				List<Long> pids = new ArrayList<Long>();
				pids.add(playerId);
				serviceCollection.getBattleService().offerDropItems(drops, pids);
			}
			
			//采集物推送给自己
			if(!collects.isEmpty()){
				List<Long> pids = new ArrayList<Long>();
				pids.add(playerId);
				serviceCollection.getCollectService().offerAddCollect(collects, pids);
			}
		}
		
	}
	
	@Override
	public void synPosition(String sceneGuid, String targetGuid, int newState,
			int newX, int newY, int newZ, int newDiretion, boolean move) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IProtoBuilderService protoBuilderService  = serviceCollection.getProtoBuilderService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IMonsterService monsterService = serviceCollection.getMonsterService();
		
		BasePuppet basePuppet = this.getBasePuppet(sceneGuid, targetGuid, 0);
		if(basePuppet == null || basePuppet.getState() != BattleConstant.STATE_NORMAL) return;
		if(basePuppet.getVertigo() == 1 || basePuppet.getFixed() == 1){
			//眩晕或者定身不能移动
			return;
		}
		
		SceneModel sceneModel = this.getSceneModel(basePuppet.getSceneGuid());
		if(sceneModel.getSceneState() == SceneConstant.SCENE_STATE_DISTROY) return;
		
		int oldGridId = basePuppet.getGridId(); //旧区域格
		
		basePuppet.setDirection(newDiretion);
		basePuppet.setX(newX);
		basePuppet.setY(newY);
		basePuppet.setZ(newZ);
		basePuppet.setPuppetState(PuppetState.values()[newState]);		
		
		
		//新区域格
		int newGridId = NineGridUtil.calInGrid(basePuppet.getX(), basePuppet.getZ(), sceneModel.getColNum());
		
		List<Integer> oldAroundGrids = sceneModel.getGridMap().get(oldGridId);
		List<Integer> newAroundGrids = sceneModel.getGridMap().get(newGridId);
		if(oldAroundGrids == null || newAroundGrids == null){
			return;
		}
		
		List<Long> playerIds = new ArrayList<Long>();
		List<Integer> ids = null;
		if(move){
			ids = NineGridUtil.getGridList(oldAroundGrids, newAroundGrids, true);
			for(Integer gid : ids){
				List<Long> l = sceneModel.getPlayerIdMap().get(gid);
				if(l!= null && !l.isEmpty()){
					playerIds.addAll(l);
				}
			}
			if(!playerIds.isEmpty()){
				List<Long> newIDList = new ArrayList<Long>(playerIds);
				if (!newIDList.isEmpty()){
					S_SynPosition.Builder builder = S_SynPosition.newBuilder();
					builder.setGuid(basePuppet.getGuid());
					builder.setState(newState);
					builder.setPosition(protoBuilderService.buildVector3Msg(newX, newY, newZ));
					builder.setDirection(newDiretion);
					gameSocketService.sendDataToPlayerList(newIDList,  
							new MessageObj(MessageID.S_SynPosition_VALUE, builder.build().toByteArray()));
				}
			}
		}
		
		if(oldGridId != newGridId){
			basePuppet.setGridId(newGridId);
			
			if(basePuppet.getType() == PlayerConstant.BECKON){
				Map<String, BeckonPuppet> oldMap = sceneModel.getBeckonPuppetMap().get(oldGridId);
				if(oldMap != null) {
					oldMap.remove(basePuppet.getGuid());
				}
				
				Map<String, BeckonPuppet> pMap = sceneModel.getBeckonPuppetMap().get(newGridId);
				if(pMap == null){
					pMap = new ConcurrentHashMap<String, BeckonPuppet>();
					sceneModel.getBeckonPuppetMap().put(newGridId, pMap);
				}
				pMap.put(basePuppet.getGuid(), (BeckonPuppet)basePuppet);
			}else{
				//以后可以新加宠物同步
			}

			//离开视野
			playerIds.clear();
			ids = NineGridUtil.getGridList(oldAroundGrids, newAroundGrids, false);
			for(Integer gid : ids){
				List<Long> l = sceneModel.getPlayerIdMap().get(gid);
				if(l != null && !l.isEmpty()){
					playerIds.addAll(l);
				}
			}
			//别人移除自己
			if(!playerIds.isEmpty()){
				this.removeSelfToNearby(basePuppet.getGuid(), playerIds);
			}
			
			//进入视野
			playerIds.clear();
			ids = NineGridUtil.getGridList(newAroundGrids, oldAroundGrids, false);
			
			for(Integer gid : ids){
				List<Long> l = sceneModel.getPlayerIdMap().get(gid);
				if(l!= null && !l.isEmpty())  playerIds.addAll(l);	
			}
			//自己推送被周边人
			if(!playerIds.isEmpty()){
				if(basePuppet.getType() == PlayerConstant.BECKON){
					monsterService.synBeckonToNearby((BeckonPuppet)basePuppet, playerIds);
				}
			}
		}
	}

	@Override
	public void updatePosition(String sceneGuid, String targetGuid,
			Position newPosition, int newDiretion){
		if(sceneGuid == null) return;
		
		ServiceCollection serviceCollection = GameContext.getInstance()
				.getServiceCollection();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		SceneModel sceneModel = this.getSceneModel(sceneGuid);
		if(sceneModel.getSceneState() == SceneConstant.SCENE_STATE_DISTROY) return;
		
		BasePuppet basePuppet = this.getBasePuppet(sceneGuid, targetGuid, 0);
		
		if(basePuppet == null || basePuppet.getState() != BattleConstant.STATE_NORMAL) return;
		
		int newX = newPosition.getX();
		int newY = newPosition.getY();
		int newZ = newPosition.getZ();
		if(basePuppet.getType() == PlayerConstant.PLAYER){
//			System.out.println("玩家被击退后的位置  X|Y|Z : " + newX + "|"+ newY + "|" +  newZ);
			this.synPosition(basePuppet.getEid(), PuppetState.STAND.ordinal(), newX, newY, newZ, newDiretion, false);
		}else if(basePuppet.getType() == PlayerConstant.BECKON){
			this.synPosition(sceneGuid, targetGuid, PuppetState.STAND.ordinal(), newX, newY, newZ, newDiretion, false);
		}else if(basePuppet.getType() == PlayerConstant.MONSTER){
//			System.out.println("击退后  怪物的位置  : " + newX + "|"+ newY + "|" +  newZ);
			MonsterPuppet monster = (MonsterPuppet)basePuppet;
			monster.getNodeList().clear();
			serviceCollection.getMonsterService().updatePosition(sceneModel, monster, newX, newY, newZ);
		}
		
		S_UpdatePosition.Builder builder = S_UpdatePosition.newBuilder();
		builder.setGuid(targetGuid);
		builder.setPosition(protoBuilderService.buildVector3Msg(newPosition));
		builder.setDirection(newDiretion);
		
		List<Long> playerIds = this.getNearbyPlayerIds(basePuppet);
		gameSocketService.sendDataToPlayerList(playerIds,  
				new MessageObj(MessageID.S_UpdatePosition_VALUE, builder.build().toByteArray()));
	}
	
	/**
	 * 初始场景玩家
	 */
	private PlayerPuppet initPlayerPuppet(long playerId){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		Player player = playerService.getPlayerByID(playerId);
		PlayerExt playerExt = playerService.getPlayerExtById(playerId);
		PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
		PlayerTianti playerTianti = serviceCollection.getTiantiService().getPlayerTianti(playerId);
		
		PlayerPuppet model = new PlayerPuppet();
		model.setGuid(player.getGuid());
		model.setEid(player.getPlayerId());
		model.setName(player.getPlayerName());
		model.setType(PlayerConstant.PLAYER);
		model.setLevel(playerProperty.getLevel());
		model.setDressStyle(playerExt.getDressStyle());
		model.setWeaponStyle(playerExt.getWeaponStyle());
		model.setWingStyle(playerExt.getWingStyle());
		model.setCareer(player.getCareer());
		model.setPuppetState(PuppetState.STAND);
		
		model.setHpMax(playerProperty.getHpMax());
		model.setMpMax(playerProperty.getMpMax());
		model.setHp(playerExt.getHp());
		model.setMp(playerExt.getMp());
		model.setPkMode(playerExt.getPkMode());
		model.setPkVlaue(playerExt.getPkVlaue());
		model.setNameColor(playerExt.getNameColor());
		if(playerExt.getPkVlaue() > 0){
			model.setPkValueUpdateTime(System.currentTimeMillis());
		}
		
		model.setP_attack(playerProperty.getP_attack());
		model.setM_attack(playerProperty.getM_attack());
		model.setP_damage(playerProperty.getP_damage());
		model.setM_damage(playerProperty.getM_damage());
		model.setCrit(playerProperty.getCrit());
		model.setTough(playerProperty.getTough());
		
		model.setDmgDeepPer(playerProperty.getDmgDeepPer());
		model.setDmgReductPer(playerProperty.getDmgReductPer());
		model.setDmgCritPer(playerProperty.getDmgCritPer());
		model.setMoveSpeed(playerProperty.getMoveSpeed());
		
		model.setX(playerExt.getX());
		model.setY(playerExt.getY());
		model.setZ(playerExt.getZ());
		
		model.setStage(playerTianti.getStage());
	
		return model;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public PlayerPuppet getPlayerPuppet(long playerId) {
		Map<Long, PlayerPuppet> map = (Map<Long, PlayerPuppet>)CacheService.getFromCache(CacheConstant.PLAYER_PUPPET_CACHE);
		
		return map.get(playerId);
	}

	/**
	 * 删除场景玩家缓存
	 */
	@SuppressWarnings("unchecked")
	public void deletePlayerPuppet(long playerId) {
		Map<Long, PlayerPuppet> map = (Map<Long, PlayerPuppet>)CacheService.getFromCache(CacheConstant.PLAYER_PUPPET_CACHE);
	    map.remove(playerId);
	}

	@Override
	public void resetPuppet(BasePuppet basePuppet) {
		basePuppet.setState(BattleConstant.STATE_NORMAL);
		basePuppet.setPuppetState(PuppetState.STAND);
		basePuppet.setHp(basePuppet.getHpMax());
		basePuppet.setMp(basePuppet.getMpMax());
		basePuppet.setAttackTime(System.currentTimeMillis());
		basePuppet.setEnemyMap(new ConcurrentHashMap<String, EnemyModel>());
		
	}
	
	@Override
	public void destroy(SceneModel sceneModel){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		
		sceneModel.setSceneState(SceneConstant.SCENE_STATE_DISTROY);
		
		//摧毁副本需要踢人出去
		if(sceneModel.getMapType() >= SceneConstant.INSTANCE_SCENE){
			for(Map.Entry<Integer, List<Long>> entry : sceneModel.getPlayerIdMap().entrySet()){
				List<Long> playerIds = new CopyOnWriteArrayList<Long>(entry.getValue());
				for(long playerId : playerIds){
					try {
						PlayerExt playerExt = serviceCollection.getPlayerService().getPlayerExtById(playerId);
						
						PlayerPuppet p = this.getPlayerPuppet(playerId);
						if(p != null && p.getHp() <= 0){
							this.resetPuppet(p);
						}
						this.enterScene(playerId, playerExt.getLastMapId(), 0, false, null, 0);
					} catch (Exception e) {
						LogUtil.error("摧毁副本异常：", e);
					}
				}
			}
		}
		
		sceneModel.clear();
		
		SceneServer.getInstance().removeSceneModel(sceneModel);
	}

	/**
	 * 把自己的数据推送被周边
	 */
	private void synSelfToNearbyPlayer(PlayerPuppet playerPuppet, List<Long> nearbyPlayerIds){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		S_AddPlayerPuppets.Builder builder = S_AddPlayerPuppets.newBuilder();
		builder.addListPlayerPuppets(protoBuilderService.buildPlayerPuppetMsg(playerPuppet));
		MessageObj msg = new MessageObj(MessageID.S_AddPlayerPuppets_VALUE, builder.build().toByteArray());
		serviceCollection.getGameSocketService().sendDataToPlayerList(nearbyPlayerIds, msg);
	}
	
	/**
	 * 把周边玩家推送给自己
	 */
	private void synNearbyPlayerToSelf(long playerId, List<PlayerPuppet> playerPuppets){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		
		S_AddPlayerPuppets.Builder builder = S_AddPlayerPuppets.newBuilder();
		for(PlayerPuppet model : playerPuppets){
			PlayerPuppetMsg.Builder msg = protoBuilderService.buildPlayerPuppetMsg(model);
			if(msg == null) continue;
			
			builder.addListPlayerPuppets(msg);
		}
		
		MessageObj msg = new MessageObj(MessageID.S_AddPlayerPuppets_VALUE, builder.build().toByteArray());
		GameContext.getInstance().getServiceCollection().getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);
	}	
	
	/**
	 * 离开视野移除周边单位
	 */
	public void removeNearbyToSelf(long playerId, List<String> guids){
		S_RemovePuppets.Builder builder = S_RemovePuppets.newBuilder();
		builder.addAllGuids(guids);
		MessageObj msg = new MessageObj(MessageID.S_RemovePuppets_VALUE, builder.build().toByteArray());
		GameContext.getInstance().getServiceCollection().getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);
	}
	
	/**
	 * 通知周边玩家移除自己
	 */
	public void removeSelfToNearby(String guid, List<Long> nearbyPlayerIds){
		S_RemovePuppets.Builder builder1 = S_RemovePuppets.newBuilder();
		builder1.addGuids(guid);
		MessageObj msg1 = new MessageObj(MessageID.S_RemovePuppets_VALUE, builder1.build().toByteArray());
		GameContext.getInstance().getServiceCollection().getGameSocketService().sendDataToPlayerList(nearbyPlayerIds, msg1);
	}

	@Override
	public void checkPuppets(Long playerId, List<String> guids) {
		if(playerId < 1 || guids.isEmpty()) return;
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		PlayerExt playerExt = playerService.getPlayerExtById(playerId);
		SceneModel sceneModel = this.getSceneModel(playerExt.getSceneGuid());
		
		List<String> removeGuids = new ArrayList<String>();
		
		for(String guid : guids){
			int fighterType = PlayerUtil.getType(guid);
			
			boolean bFind = false;
			if(fighterType == PlayerConstant.MONSTER){
				for(Map.Entry<Integer, Map<String, MonsterPuppet>> entry : sceneModel.getMonsterPuppetMap().entrySet()){
					MonsterPuppet m = entry.getValue().get(guid);
					if(m != null){
						bFind = true;
						break;
					}
				}
				
			}else if(fighterType == PlayerConstant.BECKON){
				for(Map.Entry<Integer, Map<String, BeckonPuppet>> entry : sceneModel.getBeckonPuppetMap().entrySet()){
					BeckonPuppet b = entry.getValue().get(guid);
					if(b != null){
						bFind = true;
						break;
					}
				}
			}else{
				continue;
			}
			if(!bFind){
				removeGuids.add(guid);
			}
			
		}

		if(!removeGuids.isEmpty()){
			this.removeNearbyToSelf(playerId, removeGuids);
		}
	}

	@Override
	public void synMonsterState(Long playerId){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		
		PlayerPuppet playerPuppet = this.getPlayerPuppet(playerId);
		if(playerPuppet == null || playerPuppet.getState() != BattleConstant.STATE_NORMAL) return;
		
		SceneModel sceneModel = this.getSceneModel(playerPuppet.getSceneGuid());
		if(sceneModel.getSceneState() == SceneConstant.SCENE_STATE_DISTROY) return;
		if(sceneModel.getMapType() != SceneConstant.WORLD_SCENE) return;
	
		// 同步怪物状态
		S_SynMonsterState.Builder builder = S_SynMonsterState.newBuilder();
		
		BaseMap baseMap = this.getBaseMap(playerPuppet.getMapId());
		List<MonsterInfo> list = baseMap.getMonsterInfos();
		if(list == null) return;
		
		for(MonsterInfo monsterInfo : baseMap.getMonsterInfos()){
			int state = 1;
			
			for(Map.Entry<Integer, Map<String, MonsterPuppet>> entry : sceneModel.getMonsterPuppetMap().entrySet()){
				Map<String, MonsterPuppet> monMap = entry.getValue();
				
				for(Map.Entry<String, MonsterPuppet> entry2 : monMap.entrySet()){
					MonsterPuppet monsterPuppet = entry2.getValue();
					
					// BOSS才下发
					if(monsterPuppet.getRefreshMonsterId() == monsterInfo.getRefreshMonsterId() && 
							monsterPuppet.getMonsterType() == BattleConstant.MONSTER_TYPE_3) {
						state = 0;
						break;
					}	
				}				
			}
			
			builder.addMonsterStates(protoBuilderService.buildMonsterStateMsg(monsterInfo.getRefreshMonsterId(), state));	
		}
		
		MessageObj msg = new MessageObj(MessageID.S_SynMonsterState_VALUE, builder.build().toByteArray());
		GameContext.getInstance().getServiceCollection().getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg); 
	}

	@Override
	public void tenMinuteQuarzt() {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IChatService chatService = serviceCollection.getChatService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		Date curDate = DateService.getCurrentUtilDate();
	
		//幻境开启与关闭
		Huanjing model = (Huanjing)CacheService.getFromCache(CacheConstant.HUANJING_CACHE);
		if(model.getEndTime() <= curDate.getTime()){
			if(model.getState() == 0){
				//开启半小时
				model.setState(1);	
				Date endDate = DateService.addDateByType(curDate, Calendar.MINUTE, 30);
				model.setEndTime(endDate.getTime());
				
				List<Notice> paramList = new ArrayList<Notice>();				
				Notice notice1 = new Notice(ParamType.PARAM, 0, 0, "30");
				paramList.add(notice1);
				chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_42, paramList, gameSocketService.getOnLinePlayerIDList());
			}else{
				//关闭一小时
				model.setState(0);	
				Date endDate = DateService.addDateByType(curDate, Calendar.MINUTE, 60);
				model.setEndTime(endDate.getTime());
				
				List<Notice> paramList = new ArrayList<Notice>();				
				Notice notice1 = new Notice(ParamType.PARAM, 0, 0, "60");
				paramList.add(notice1);
				chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_43, paramList, gameSocketService.getOnLinePlayerIDList());
			}
		}
	}
}
