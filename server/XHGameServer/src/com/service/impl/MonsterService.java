package com.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.cache.BaseCacheService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.RandomService;
import com.common.ServiceCollection;
import com.constant.BattleConstant;
import com.constant.CacheConstant;
import com.constant.ChatConstant;
import com.constant.GuildConstant;
import com.constant.PlayerConstant;
import com.constant.ProdefineConstant;
import com.constant.SceneConstant;
import com.constant.SkillConstant;
import com.constant.VipConstant;
import com.dao.monster.BaseMonsterDAO;
import com.domain.MessageObj;
import com.domain.ai.AStar;
import com.domain.ai.Node;
import com.domain.chat.Notice;
import com.domain.guild.Guild;
import com.domain.map.BaseMap;
import com.domain.monster.BaseAiDetermine;
import com.domain.monster.BaseMonster;
import com.domain.monster.BaseRefreshMonster;
import com.domain.player.PlayerExt;
import com.domain.player.PlayerProperty;
import com.domain.puppet.BasePuppet;
import com.domain.puppet.BeckonPuppet;
import com.domain.puppet.EnemyModel;
import com.domain.puppet.MonsterPuppet;
import com.domain.puppet.PlayerPuppet;
import com.domain.puppet.PuppetState;
import com.domain.skill.BaseSkill;
import com.domain.team.Team;
import com.domain.team.TeamPlayer;
import com.message.ChatProto.ParamType;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.SceneProto.S_AddBeckonPuppets;
import com.message.SceneProto.S_AddMonsterPuppets;
import com.message.SceneProto.S_SynPosition;
import com.scene.SceneModel;
import com.service.IChatService;
import com.service.IMonsterService;
import com.service.IPlayerService;
import com.service.IProtoBuilderService;
import com.service.ISceneService;
import com.service.IVipService;
import com.util.CommonUtil;
import com.util.ComparatorUtil;
import com.util.LogUtil;
import com.util.NineGridUtil;
import com.util.PlayerUtil;
import com.util.SerialNumberUtil;
import com.util.SplitStringUtil;

/**
 * 怪物管理
 * @author ken
 * @date 2017-1-7
 */
public class MonsterService implements IMonsterService {

	private BaseMonsterDAO baseMonsterDAO = new BaseMonsterDAO();
	
	@Override
	public void initBaseCache() {

		
		//ai判定表
		Map<Integer, BaseAiDetermine> aiDeterMap = new HashMap<Integer, BaseAiDetermine>();
		List<BaseAiDetermine> aiDeters = baseMonsterDAO.listBaseAiDetermines();
		for(BaseAiDetermine model : aiDeters){
			model.setActionCd(model.getActionCd() * 1000); //转毫秒
			aiDeterMap.put(model.getId(), model);
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_AI_DETERMINE, aiDeterMap);
		
		//怪物表
		Map<Integer, BaseMonster> map = new HashMap<Integer, BaseMonster>();
		List<BaseMonster> monList = baseMonsterDAO.listBaseMonsters();
		for(BaseMonster model : monList){
			List<BaseAiDetermine> aiList = new ArrayList<BaseAiDetermine>();
			List<Integer> lists = SplitStringUtil.getIntList(model.getAi());
			if(lists != null){
				for(Integer aiId : lists){
					BaseAiDetermine baseAiDetermine = aiDeterMap.get(aiId);
					aiList.add(baseAiDetermine);
				}
			}

			model.setDropInfoList(SplitStringUtil.getDropInfo(model.getDropInfo()));
			model.setAiList(aiList);
			map.put(model.getId(), model);
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_MONSTER, map);
		
		//刷怪配置
		Map<Integer, BaseRefreshMonster> reMonsterMap = new HashMap<Integer, BaseRefreshMonster>();
		List<BaseRefreshMonster> reMonList = baseMonsterDAO.listBaseRefreshMonsters();
		for(BaseRefreshMonster model : reMonList){
			model.setPatrolTimeList(SplitStringUtil.getIntList(model.getPatrolTime()));
			model.setMonsterInputList(SplitStringUtil.getIntIntList(model.getMonsterInput()));
			reMonsterMap.put(model.getID(), model);
			
//			//TODO 排查刷怪录入
//			for(List<Integer> list : model.getMonsterInputList()){
//				BaseMonster m = map.get(list.get(0));
//				if(m == null){
//					System.out.println("monster is null with monsterId is "+list.get(0)+" and refId is "+model.getID());
//				}
//			}
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_REFRESH_MONSTER, reMonsterMap);
	}

	/**
	 * 取怪物配置
	 */
	@SuppressWarnings("unchecked")
	public BaseMonster getBaseMonster(int monsterId){
		Map<Integer, BaseMonster> map = (Map<Integer, BaseMonster>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_MONSTER);
		return map.get(monsterId);
	}
	
	/**
	 * 取刷怪配置
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BaseRefreshMonster getBaseRefreshMonster(int refMonsterId) {
		Map<Integer, BaseRefreshMonster> map = (Map<Integer, BaseRefreshMonster>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_REFRESH_MONSTER);
		return map.get(refMonsterId);
	}


	@SuppressWarnings("unchecked")
	@Override
	public BaseAiDetermine getBaseAiDetermine(int aiId) {
		Map<Integer, BaseAiDetermine> map = (Map<Integer, BaseAiDetermine>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_AI_DETERMINE);
		return map.get(aiId);
	}

	@Override
	public void dealAI(MonsterPuppet monster, SceneModel sceneModel){
		if (monster.getState() != BattleConstant.STATE_NORMAL) return;
		
		ISceneService sceneService = GameContext.getInstance().getServiceCollection().getSceneService();
		
		long currentTime = System.currentTimeMillis();
		if(monster.getAiCdMap() != null && monster.getAiCdMap().values() != null && monster.getAiCdMap().values().size() > 0){
			Iterator<Map.Entry<Integer, Long>> iterator = monster.getAiCdMap().entrySet().iterator();
			while(iterator.hasNext()){
				Map.Entry<Integer, Long> m = iterator.next();
				if(m.getValue() <= currentTime){
					iterator.remove();
				}
			}
		}
		
		if (currentTime - monster.getAttackTime() < 0){
			monster.getNodeList().clear();
			return;
		}
		
		//如果有移动路线，则250毫秒更新一下当前位置
		if (!monster.getNodeList().isEmpty() && currentTime - monster.getLastUpdatePosTime() > 50000.0/monster.getMoveSpeed()) {
			monster.setLastUpdatePosTime(currentTime);
			
			Node node = monster.getNodeList().remove(0);
			int x = node.getX() * SceneConstant.CELL_SIZE;
			int z = node.getY() * SceneConstant.CELL_SIZE;
			
			//更新位置
			this.updatePosition(sceneModel, monster, x, monster.getY(), z);
			
			//复位点到了   
			if(monster.getAiState() == BattleConstant.AI_STATE_GOBACK && monster.getNodeList().isEmpty()){
				monster.setAiState(BattleConstant.AI_STATE_NORMAL);
				monster.setPatrolTime(RandomService.getRandomNum(monster.getPatrolTimeList().get(0), monster.getPatrolTimeList().get(1)));
			}
		}
		
		
		if(monster.getAiState() == BattleConstant.AI_STATE_GOBACK){
			return;
		}
		
		if(monster.getAiState() == BattleConstant.AI_STATE_BATTLE){

			// 怪物当前位置与进入战斗时位置距离
			int maxSpace = (monster.getX()-monster.getxBattle()) * (monster.getX()-monster.getxBattle())
					+ (monster.getZ() - monster.getzBattle()) * (monster.getZ() -monster.getzBattle());
			
			//超过追击的范围 回去  
			if (maxSpace >= monster.getPursuitRange() * monster.getPursuitRange()) {
				monster.setAiState(BattleConstant.AI_STATE_GOBACK);
				//System.out.println("复位--------------------------"+monster.getGuid());
				//清除敌人列表
				monster.getEnemyMap().clear();
				
				//加血
				monster.setHp(monster.getHpMax());
				monster.setMp(monster.getMpMax());
				Map<Integer, Integer> propertyMap = new HashMap<Integer, Integer>();
				propertyMap.put(ProdefineConstant.HP, monster.getHp());
				propertyMap.put(ProdefineConstant.MP, monster.getMp());
				GameContext.getInstance().getServiceCollection().getPlayerService().synPlayerPropertyToAll(monster, propertyMap);
				
				this.move(monster, monster.getxBattle(), monster.getzBattle(), 0, sceneModel);
				return;
			}
		}

		List<Long> nearbyPlayerIds = sceneService.getNearbyPlayerIds(monster);
		if(nearbyPlayerIds.isEmpty()){
			monster.getEnemyMap().clear();
			return;
		}
		
		List<BasePuppet> roundPuppets = new ArrayList<BasePuppet>();
		if(monster.getAiState() == BattleConstant.AI_STATE_PATROL || monster.getAiState() == BattleConstant.AI_STATE_NORMAL || monster.getAiState() == BattleConstant.AI_STATE_IMMUNE){
			
			//主动怪 警戒范围的敌人
			if(monster.getEvasiveStyle() == 1){
				for (long playerId : nearbyPlayerIds) {
					PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerId);
					// 判断状态
					if (playerPuppet == null || playerPuppet.getState() != BattleConstant.STATE_NORMAL) continue;
					
					if(playerPuppet.getMapId() != monster.getMapId()) continue;
					
					// 距离
					int space = (monster.getX()-playerPuppet.getX())*(monster.getX()-playerPuppet.getX()) + (monster.getZ()-playerPuppet.getZ())*(monster.getZ()-playerPuppet.getZ());
					
					if (space <= monster.getWarmRange()*monster.getWarmRange()){ //警戒范围内
						//System.out.println("进入警戒范围！！！！！！！！！！");
						roundPuppets.add(playerPuppet);
						
						if(!monster.getEnemyMap().containsKey(playerPuppet.getGuid())){
							this.addMonsterEnemy(0, playerPuppet, monster);
						}

					} 
					
				}
			}
		}
		
		//加入敌人   并移除无效敌人
		Set<String> guids = new HashSet<String>();
		for(Map.Entry<String, EnemyModel> entry : monster.getEnemyMap().entrySet()){
			EnemyModel enemyModel = entry.getValue();
			BasePuppet target = null;
			if(enemyModel.getTargetType() == PlayerConstant.PLAYER){
				target = sceneService.getPlayerPuppet(enemyModel.getPlayerId());
			}else if(enemyModel.getTargetType() == PlayerConstant.BECKON){
				for(Map.Entry<Integer, Map<String, BeckonPuppet>> entry2 : sceneModel.getBeckonPuppetMap().entrySet()){
					BeckonPuppet b = entry2.getValue().get(enemyModel.getTargetGuid());
					if(b != null){
						target = b;
						break;
					}
				}
			}
			if(target == null){
				guids.add(enemyModel.getTargetGuid());
				continue;
			}
			if (target.getState() != BattleConstant.STATE_NORMAL) {
				guids.add(enemyModel.getTargetGuid());
				continue;
			}
			
			if(target.getMapId() != monster.getMapId()){
				guids.add(enemyModel.getTargetGuid());
				continue;
			}
			
			roundPuppets.add(target);
		}
		
		if(!guids.isEmpty()){
			for(String guid : guids){
				monster.getEnemyMap().remove(guid);
			}
			
			if(sceneModel.getMapType() == SceneConstant.WORLD_SCENE && monster.getEnemyMap().isEmpty()){
				//加血
				monster.setHp(monster.getHpMax());
				monster.setMp(monster.getMpMax());
				Map<Integer, Integer> propertyMap = new HashMap<Integer, Integer>();
				propertyMap.put(ProdefineConstant.HP, monster.getHp());
				propertyMap.put(ProdefineConstant.MP, monster.getMp());
				GameContext.getInstance().getServiceCollection().getPlayerService().synPlayerPropertyToAll(monster, propertyMap);
				
				return;
			}
		}
		
		//选取目标
		BasePuppet atkTarget = choiceTarget(monster, roundPuppets);

		if(atkTarget == null){
			if (currentTime - monster.getLastMoveTime() >= monster.getPatrolTime() && monster.getPatrolRange() > 0) {
				//巡逻运动 
//				System.out.println("---------------------开始巡逻运动");
				monster.setAiState(BattleConstant.AI_STATE_PATROL);
				monster.setPatrolTime(RandomService.getRandomNum(monster.getPatrolTimeList().get(0), monster.getPatrolTimeList().get(1)));
				BaseMap baseMap = sceneService.getBaseMap(monster.getMapId());
	            int[] point = CommonUtil.getRandomPoint(baseMap, monster.getxBirth(), monster.getzBirth(), monster.getPatrolRange());
	           
	            this.move(monster, point[0], point[1], 0, sceneModel);
			}

			return;
		}
		//选取ai
		BaseAiDetermine ai = monster.getCurAI();
		if(ai == null){
			ai = choiceAI(monster, roundPuppets);
			monster.setCurAI(ai);
		}
		
		if(ai == null) return;
		
		int skillId = ai.getActionId();
		BaseSkill baseSkill = GameContext.getInstance().getServiceCollection().getSkillService().getBaseSkill(skillId);
		
		if (atkTarget.getState() == BattleConstant.STATE_NORMAL){
			if(monster.getAiState() != BattleConstant.AI_STATE_BATTLE){
				monster.setAiState(BattleConstant.AI_STATE_BATTLE);
				//System.out.println("更新战斗位置**********************************");
				monster.setxBattle(monster.getX());
				monster.setyBattle(monster.getY());
				monster.setzBattle(monster.getZ());
				
			}
			// 判断两个战斗对象的距离
			if(baseSkill.getfReleaseDist() == 0){
				
				monster.getAiCdMap().put(ai.getId(), System.currentTimeMillis() + ai.getActionCd());
				
				//发动攻击
				this.attackTarget(monster, atkTarget, baseSkill);
				
				return;
			}
			int space = (monster.getX() - atkTarget.getX())* (monster.getX() - atkTarget.getX())+ (monster.getZ() - atkTarget.getZ()) * (monster.getZ() - atkTarget.getZ()) + 2000;
			if (space <= baseSkill.getfReleaseDist() * baseSkill.getfReleaseDist()) {
				//System.out.println("space:"+Math.sqrt(space)+"   monster.getX():"+monster.getX()+" monster.getZ():"+monster.getZ());
				
				if(monster.getTargetX() != atkTarget.getX() || monster.getTargetZ() != atkTarget.getZ()){
					//敌人移动了再次检索一个攻击范围内的距离
					
					int dist = 300;
					if(baseSkill.geteSkillTargetCate() == SkillConstant.TARGET_ENEMY){
						dist = baseSkill.getfReleaseDist() - 50;
					}
					monster.setTargetX(atkTarget.getX());
					monster.setTargetZ(atkTarget.getZ());
					BaseMap baseMap = sceneService.getBaseMap(monster.getMapId());
					int[] pos = CommonUtil.getRandomPoint2(baseMap, atkTarget.getX(), atkTarget.getZ(), dist);
					this.move(monster, pos[0], pos[1], 0, sceneModel);
	
				}
				
				monster.getAiCdMap().put(ai.getId(), System.currentTimeMillis() + ai.getActionCd());
				
				//发动攻击
				this.attackTarget(monster, atkTarget, baseSkill);
				
				return;
			}
			
			//距离太远， 追击
			this.move(monster, atkTarget.getX(), atkTarget.getZ(), baseSkill.getfReleaseDist(), sceneModel);
		}
		
	}
	
	/**
	 * 选择ai
	 */
	private BaseAiDetermine choiceAI(MonsterPuppet monster, List<BasePuppet> roundPuppets){
		List<BaseAiDetermine> useAiList = new ArrayList<BaseAiDetermine>();
		for(BaseAiDetermine aiModel : monster.getAiList()){
			if(monster.getAiCdMap().containsKey(aiModel.getId())){
				continue;
			}
			
			if(aiModel.getDecisionType() == 1){
				//自身血量低于%
				if(monster.getHp() * 100.0 / monster.getHpMax() >= aiModel.getDecisionValue()){
					continue;
				}
			}else if(aiModel.getDecisionType() == 2){
				//玩家血量低于%
				
			}else if(aiModel.getDecisionType() == 3){
				//怪物血量低于%
				
			}else if(aiModel.getDecisionType() == 4){
				//与玩家距离小于
				boolean bFind = false;
				for(BasePuppet basePuppet : roundPuppets){
					int space = (monster.getX()-basePuppet.getX())*(monster.getX()-basePuppet.getX()) + (monster.getZ()-basePuppet.getZ())*(monster.getZ()-basePuppet.getZ());
					if(space <= aiModel.getDecisionValue() * aiModel.getDecisionValue()){
						bFind = true;
						break;
					}
				}
				if(!bFind){
					continue;
				}
			}else if(aiModel.getDecisionType() == 5){
				//与玩家距离大于
				boolean bFind = false;
				for(BasePuppet basePuppet : roundPuppets){
					int space = (monster.getX()-basePuppet.getX())*(monster.getX()-basePuppet.getX()) + (monster.getZ()-basePuppet.getZ())*(monster.getZ()-basePuppet.getZ());
					if(space >= aiModel.getDecisionValue() * aiModel.getDecisionValue()){
						bFind = true;
						break;
					}
				}
				if(!bFind){
					continue;
				}
			}
			
			useAiList.add(aiModel);
		}
		
		if(useAiList.isEmpty()) return null;
		
		BaseAiDetermine ai = null;
		if(useAiList.size() == 1){
			ai = useAiList.get(0);
		}else{
			List<BaseAiDetermine> lists = new ArrayList<BaseAiDetermine>();
			int lv = 0;
			int allRate = 0;
			for(BaseAiDetermine model : useAiList){
				if(model.getActionLevel() > lv){
					lists.clear();
					lv = model.getActionLevel();
				}
				if(model.getActionLevel() == lv){
					lists.add(model);
					allRate += model.getWeight();
				}
			}
			
			if(lists.size() == 1){
				ai = lists.get(0);
			}else{
				int random = RandomService.getRandomNum(allRate);
				int rate = 0;
				for(BaseAiDetermine model : lists){
					if(random <= model.getWeight() + rate){
						ai = model;
						break;
					}
					rate += model.getWeight();
				}
			}
		}

		return ai;
	}
	/**
	 * 选取目标
	 */
	private BasePuppet choiceTarget(MonsterPuppet monster, List<BasePuppet> roundPuppets){
		if(roundPuppets.isEmpty()) return null;
		//仇恨最高
		Collection<EnemyModel> enemyList = monster.getEnemyMap().values();
		if(enemyList != null && !enemyList.isEmpty()){
			List<EnemyModel> enemys = new ArrayList<EnemyModel>();
			enemys.addAll(enemyList);
			Collections.sort(enemys, new ComparatorUtil(ComparatorUtil.DOWN));
			
			for(EnemyModel enemy : enemys){
				for (BasePuppet basePuppet : roundPuppets){
					if (basePuppet.getState() != BattleConstant.STATE_NORMAL) continue;
					if(enemy.getTargetGuid().equals(basePuppet.getGuid())){
						return basePuppet;
					}
				}
			}
		}
		for (BasePuppet basePuppet : roundPuppets) {
			if (basePuppet.getState() != BattleConstant.STATE_NORMAL) continue;
			return basePuppet;
		}
		return null;
	}
	
	@Override
	public void refreshMonsters(SceneModel sceneModel, int refMonsterId, int curLayerId, boolean offer) {
		BaseRefreshMonster refMonster = this.getBaseRefreshMonster(refMonsterId);
		if(refMonster == null) return;
		
		this.refreshMonsters(sceneModel, refMonsterId, refMonster.getxRefresh(), refMonster.getyRefresh(), refMonster.getzRefresh(), curLayerId, offer);
	}

	@Override
	public void refreshMonsters(SceneModel sceneModel, int refMonsterId, int x, int y, int z, int curLayerId, boolean offer) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		ISceneService sceneService = serviceCollection.getSceneService();
		
		List<MonsterPuppet> lists = this.initMonsterPuppets(sceneModel, refMonsterId, x, y, z, curLayerId);
		
		for(MonsterPuppet monsterPuppet : lists){
			Map<String, MonsterPuppet> mMap = sceneModel.getMonsterPuppetMap().get(monsterPuppet.getGridId());
			if(mMap == null){
				mMap = new ConcurrentHashMap<String, MonsterPuppet>();
				sceneModel.getMonsterPuppetMap().put(monsterPuppet.getGridId(), mMap);
			}
			mMap.put(monsterPuppet.getGuid(), monsterPuppet);
			
			if(offer){
				this.synMonsterToNearby(monsterPuppet, sceneService.getNearbyPlayerIds(monsterPuppet));
				this.offerBossNotice(sceneModel, monsterPuppet);
			}
			
		}
		lists = null;
	}
	
	/**
	 * 怪物移动
	 */
	private void move(MonsterPuppet monster, int targetPosX, int targetPosY, int keepDis, SceneModel sceneModel) {
		
		if(monster.getVertigo() == 1 || monster.getFixed() == 1){
			//眩晕或者定身不能移动
			monster.getNodeList().clear();
			monster.setTargetX(0);
			monster.setTargetZ(0);
			monster.setMoveToX(0);
			monster.setMoveToZ(0);
			return;
		}
		
		monster.setLastMoveTime(System.currentTimeMillis());
		
		AStar star = new AStar(sceneModel.getMapRow(), sceneModel.getMapColumn());
		
		int zGrid = (int)Math.ceil(monster.getZ() * 1.0/SceneConstant.CELL_SIZE);
		int xGrid = (int)Math.ceil(monster.getX() * 1.0/SceneConstant.CELL_SIZE);
		int zTargetGrid = (int)Math.ceil(targetPosY * 1.0/SceneConstant.CELL_SIZE);
		int xTargetGrid = (int)Math.ceil(targetPosX * 1.0/SceneConstant.CELL_SIZE);
		List<Node> nodeList = star.search(zGrid , xGrid, zTargetGrid, xTargetGrid);
		star = null;
		if (!nodeList.isEmpty()) {
			if (keepDis > 0) {
				// 保持距离多少个格子
				int kGrid = (int)Math.ceil(keepDis * 1.0/SceneConstant.CELL_SIZE);
				int index = nodeList.size() - kGrid;
				
				if(index <= 0){
					nodeList.remove(nodeList.size() - 1);
				}else{
					for(int i = 0; i < kGrid; i++){
						nodeList.remove(nodeList.size() - 1);
					}
				}
				
			}

			if (!nodeList.isEmpty() && nodeList.size() > 0) {
					monster.setNodeList(nodeList);
					
					int x = nodeList.get(nodeList.size()-1).getX()  * SceneConstant.CELL_SIZE;
					int z = nodeList.get(nodeList.size()-1).getY()  * SceneConstant.CELL_SIZE;
					
					if(monster.getMoveToX() == x && monster.getMoveToZ() == z){
						//同一目的地， 不走动了
						return;
					}
					
					monster.setMoveToX(x);
					monster.setMoveToZ(z);
					
					//同步位置 
					ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
					IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
					GameSocketService gameSocketService = serviceCollection.getGameSocketService();
					S_SynPosition.Builder builder = S_SynPosition.newBuilder();
					builder.setGuid(monster.getGuid());
					builder.setState(PuppetState.MOVING.ordinal());
					builder.setPosition(protoBuilderService.buildVector3Msg(x, monster.getY(), z));
					builder.setDirection(monster.getDirection());
					
					List<Long> nearyPlayerIds = serviceCollection.getSceneService().getNearbyPlayerIds(monster);
					
					MessageObj msg = new MessageObj(MessageID.S_SynPosition_VALUE, builder.build().toByteArray());
					gameSocketService.sendDataToPlayerList(nearyPlayerIds, msg);
			}
		}
	}

	/**
	 * 怪物攻击
	 */
	private void attackTarget(MonsterPuppet monster, BasePuppet target, BaseSkill baseSkill) {

		monster.getNodeList().clear();
		
		if (System.currentTimeMillis() - monster.getAttackTime() < 0) return;
		
		if(monster.getVertigo() == 1){
			//眩晕不能放技能
			return;
		}
		
//		System.out.print("怪物攻击 位置  X|Y|Z :" +  monster.getX() +"|" +  monster.getY() + "|"+ monster.getZ());
//		System.out.print(",  目标 位置  X|Y|Z :" +  target.getX() +"|" +  target.getY() + "|"+ target.getZ());
//		System.out.println(",  技能ID  X :" +  baseSkill.getBaseSkillId());
		
		monster.setCurAI(null);
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		
		monster.setAttackTime(System.currentTimeMillis() + baseSkill.getN32SkillLastTime() + 250);
	
		//释放技能
		try {
			serviceCollection.getBattleService().synSkill(monster, baseSkill.getUn32SkillID(), 0, 0, target.getX(), target.getY(), target.getZ(), target.getGuid());
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	@Override
	public void addPlayerExp(long playerId, int monsterId) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IVipService vipService = serviceCollection.getVipService();	
		
		double addExp = this.getBaseMonster(monsterId).getExperience();
	
		PlayerExt playerExt = playerService.getPlayerExtById(playerId);
		
		BaseMap baseMap = serviceCollection.getSceneService().getBaseMap(playerExt.getMapId());
		if(baseMap.isHuanjing()){
			//幻境地图怪物经验加成0.5
			addExp = addExp * 1.5;
		}
		
		if(playerExt.getTeamId() > 0){
			
			Team team = serviceCollection.getTeamService().getTeam(playerExt.getTeamId());
			if(team != null){
				GameSocketService gameSocketService = serviceCollection.getGameSocketService();
				List<Long> onlinePlayerIds = new ArrayList<Long>();
				int totalLevel = 0;
				for(Map.Entry<Long, TeamPlayer> entry : team.getTeamPlayerMap().entrySet()){
					TeamPlayer tp = entry.getValue();
					if(tp != null){
						PlayerExt p = playerService.getPlayerExtById(tp.getPlayerId());
						if(gameSocketService.checkOnLine(tp.getPlayerId()) && p.getMapId() == playerExt.getMapId()){
							onlinePlayerIds.add(tp.getPlayerId());
							
							PlayerProperty playerProperty = playerService.getPlayerPropertyById(tp.getPlayerId());
							totalLevel = totalLevel + playerProperty.getLevel();
						}
					}
				}		
				
				if(onlinePlayerIds.size() > 0){
					addExp = (int)Math.ceil(addExp * (0.7 + onlinePlayerIds.size()*0.3));
					
					for(Long id : onlinePlayerIds){
						PlayerProperty playerProperty = playerService.getPlayerPropertyById(id);
						int exp = (int) Math.ceil(addExp * playerProperty.getLevel()/totalLevel);
						
						// vip特权加杀怪经验
						int rate = 100 + vipService.getVipPrivilegeValue(id, VipConstant.VIP_PRIVILEGE_2);
						
						// 经验卡加经验		
						PlayerPuppet playerPuppet = serviceCollection.getSceneService().getPlayerPuppet(id);
						if(playerPuppet == null) return;
						if(playerPuppet.getExpRate() > 0){
							rate = rate + playerPuppet.getExpRate();
						}
						
						// 经验加成
						int addExpEnd = (int)Math.ceil(exp * rate * 0.01);							
						playerService.addPlayerExp(id, addExpEnd);
					}		
				}
			}
		}else{
			// vip特权加杀怪经验				
			int rate = 100 + vipService.getVipPrivilegeValue(playerId, VipConstant.VIP_PRIVILEGE_2);
			
			// 经验卡加经验		
			PlayerPuppet playerPuppet = serviceCollection.getSceneService().getPlayerPuppet(playerId);
			if(playerPuppet == null) return;
			
			if(playerPuppet.getExpRate() > 0){
				rate = rate + playerPuppet.getExpRate();
			}
			
			// 经验加成
			addExp = addExp * rate * 0.01;
			
			playerService.addPlayerExp(playerId, (int)addExp);
		}

	}

	
	/**
	 * 初始场景怪物
	 */
	private List<MonsterPuppet> initMonsterPuppets(SceneModel sceneModel, int refreshMonsterId, int x, int y, int z, int curLayerId){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		ISceneService sceneService = serviceCollection.getSceneService();
		
		BaseMap baseMap = sceneService.getBaseMap(sceneModel.getMapId());
		BaseRefreshMonster refMonster = this.getBaseRefreshMonster(refreshMonsterId);
		
		List<MonsterPuppet> lists = new ArrayList<MonsterPuppet>();
		if(refMonster.getMonsterInputList() == null) return lists;
		
		 List<List<Integer>> miList = new ArrayList<List<Integer>>();
		if(refMonster.getRandom() == 0){
			miList = refMonster.getMonsterInputList();
		}else{
			if(refMonster.getMonsterInputList().size() < 2){
				miList = refMonster.getMonsterInputList();
			}else{
				int allRate = 0;
				for(List<Integer> l : refMonster.getMonsterInputList()){
					allRate += l.get(2);
				}
				
				int random = RandomService.getRandomNum(allRate);
				int rate = 0;
				for(List<Integer> l : refMonster.getMonsterInputList()){
					
					if(random <= l.get(2) + rate){
						miList.add(l);
						break;
					}
					rate += l.get(2);
				}
			}
		}
		
		for(List<Integer> monList : miList){
			int monsterId = monList.get(0);
			int num = monList.get(1);
			for(int i = 0; i< num; i++){
				MonsterPuppet model = new MonsterPuppet();
				
				BaseMonster monster =  this.getBaseMonster(monsterId);
				if(monster == null){
					LogUtil.error("monster is null with monsterId is " + monsterId+" and refreshMonsterId is "+refreshMonsterId);
				}
				model.setRefreshMonsterId(refMonster.getID());
				model.setGuid(PlayerUtil.getGuid(PlayerConstant.MONSTER, SerialNumberUtil.getLongNum()));
				model.setEid(monsterId);
				model.setName(monster.getName());
				model.setType(PlayerConstant.MONSTER);
				model.setEvasiveStyle(monster.getEvasiveStyle());
				model.setMonsterType(monster.getMonsterType());
				model.setLevel(monster.getLv());
				model.setDressStyle(monster.getSculptResid());
				model.setPuppetState(PuppetState.STAND);
				int[] point = CommonUtil.getRandomPoint(baseMap, x, z, refMonster.getRefreshRange());
				model.setxBirth(point[0]);
				model.setyBirth(y);
				model.setzBirth(point[1]);
				model.setX(model.getxBirth());
				model.setY(model.getyBirth());
				model.setZ(model.getzBirth());
				model.setGridId(NineGridUtil.calInGrid(model.getX(), model.getZ(), sceneModel.getColNum()));
				
				model.setDirection(refMonster.getDirection());
				
				model.setHpMax(monster.getHp());

				if(curLayerId > 0){
					if(baseMap.getMapType() == SceneConstant.TOWER_SCENE){
						//大荒塔独特血量
						model.setHpMax((int)(this.towerMonsterHp(curLayerId) * monster.getHp()));
					}else if(baseMap.getOpenTask() == SceneConstant.INSTANCE_TYPE_42){
						//环任务副本独特血量
						model.setHpMax((int)(monster.getHp() * this.weekTaskPro(curLayerId)));
					}else if(baseMap.getOpenTask() == SceneConstant.INSTANCE_TYPE_43 || baseMap.getOpenTask() == SceneConstant.INSTANCE_TYPE_44){
						//秘境和活动副本独特血量
						model.setHpMax((int)(this.actMonsterHp(curLayerId) * monster.getHp()));					
					}					
				}
				
				model.setHp(model.getHpMax());
				
				model.setP_attack(monster.getP_attack());
				model.setM_attack(monster.getM_attack());
				model.setP_damage(monster.getP_damage());
				model.setM_damage(monster.getM_damage());
				model.setCrit(monster.getCrt());
				model.setTough(monster.getTough());
				
				if(curLayerId > 0){
					if(baseMap.getMapType() == SceneConstant.TOWER_SCENE){
						//大荒塔独特属性加成
						double pro = this.towerMonsterPro(curLayerId);
						model.setP_attack((int)(monster.getP_attack() * pro));
						model.setM_attack((int)(monster.getM_attack()* pro));
						model.setP_damage((int)(monster.getP_damage()* pro));
						model.setM_damage((int)(monster.getM_damage()* pro));
						model.setCrit((int)(monster.getCrt()* pro));
						model.setTough((int)(monster.getTough()* pro));
						
						model.setLevel(this.towerMonsterLv(curLayerId));
						
						//大荒塔怪物buff
						int buffId = serviceCollection.getTowerService().towerBuff(curLayerId);
						if(buffId > 0){
							List<Integer> buffIds = new ArrayList<Integer>();
							buffIds.add(buffId);
							serviceCollection.getBuffService().addBuff(model, model, buffIds);
						}
					}else if(baseMap.getOpenTask() == SceneConstant.INSTANCE_TYPE_42){
						//环任务副本独特属性加成
						double pro =  this.weekTaskPro(curLayerId);
						model.setP_attack((int)(monster.getP_attack() * pro));
						model.setM_attack((int)(monster.getM_attack()* pro));
					}else if(baseMap.getOpenTask() == SceneConstant.INSTANCE_TYPE_43 || baseMap.getOpenTask() == SceneConstant.INSTANCE_TYPE_44){
						//秘境和活动副本独特属性加成
						double pro = this.actMonsterPro(curLayerId);
						model.setP_attack((int)(monster.getP_attack() * pro));
						model.setM_attack((int)(monster.getM_attack()* pro));
						model.setP_damage((int)(monster.getP_damage()* pro));
						model.setM_damage((int)(monster.getM_damage()* pro));
						model.setCrit((int)(monster.getCrt()* pro));
						model.setTough((int)(monster.getTough()* pro));
						
						model.setLevel(curLayerId);
					}
				}
				
				model.setDmgDeepPer(monster.getDmgDeepPer());
				model.setDmgReductPer(monster.getDmgReductPer());
				model.setDmgCritPer(monster.getDmgCritPer());
				model.setMoveSpeed(monster.getSpeed());
				model.setImmuneDebuff(monster.getImmuneDebuff());
				
				model.setWarmRange(refMonster.getWarmRange());
				model.setPatrolRange(refMonster.getPatrolRange());
				model.setPursuitRange(refMonster.getPursuitRange());
				model.setPatrolTimeList(refMonster.getPatrolTimeList());
				model.setPatrolTime(RandomService.getRandomNum(model.getPatrolTimeList().get(0), model.getPatrolTimeList().get(1)));
				model.setRefreshTime(refMonster.getRefreshTime());
				
				model.setMapId(sceneModel.getMapId());
				model.setLine(sceneModel.getLine());
				model.setSceneGuid(sceneModel.getSceneGuid());
				
				model.setAiList(monster.getAiList());
				
				if(baseMap.isInstance()){
					model.setAiState(BattleConstant.AI_STATE_IMMUNE);
				}
				
				model.setPreAiTime(System.currentTimeMillis());
				
				lists.add(model);
			}
		}

		return lists;
	}
	
	/**
	 * 重置怪物数据
	 */
	@Override
	public void resetMonsterPuppet(SceneModel sceneModel, MonsterPuppet model){
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IMonsterService monsterService = serviceCollection.getMonsterService();
		ISceneService sceneService = serviceCollection.getSceneService();
		
		synchronized (model.getLifeLock()) {
			if(model.getHp() > 0){
				LogUtil.error(model.getName() +" is not dead! resetMonsterPuppet");
				return;
			}
			
			BaseRefreshMonster refMonster = monsterService.getBaseRefreshMonster(model.getRefreshMonsterId());
			
			if(refMonster.getRandom() == 1){
				this.refreshMonsters(sceneModel, model.getRefreshMonsterId(), 0, true);
				return;
			}
			
			BaseMap baseMap = sceneService.getBaseMap(sceneModel.getMapId());
			
			model.setGuid(PlayerUtil.getGuid(PlayerConstant.MONSTER, SerialNumberUtil.getLongNum()));
			model.setPuppetState(PuppetState.STAND);
			int[] point = CommonUtil.getRandomPoint(baseMap, refMonster.getxRefresh(), refMonster.getzRefresh(), refMonster.getRefreshRange());
			model.setxBirth(point[0]);
			model.setyBirth(refMonster.getyRefresh());
			model.setzBirth(point[1]);
			model.setX(model.getxBirth());
			model.setY(model.getyBirth());
			model.setZ(model.getzBirth());
			model.setGridId(NineGridUtil.calInGrid(model.getX(), model.getZ(), sceneModel.getColNum()));
			model.setDirection(refMonster.getDirection());
			
			model.setHp(model.getHpMax());
			model.setMp(model.getMpMax());
			model.setPreAiTime(System.currentTimeMillis());
			model.setLastMoveTime(System.currentTimeMillis());
			model.setLastUpdatePosTime(System.currentTimeMillis());
			model.getNodeList().clear();
			model.getAiCdMap().clear();
			model.setAiState(BattleConstant.AI_STATE_NORMAL);
			model.setCurAI(null);
			model.setDeadTime(0);
			model.setState(BattleConstant.STATE_NORMAL);
			model.setAttackTime(0);
			model.getEnemyMap().clear();
			model.getBuffMap().clear();
			model.setFixed(0);
			model.setVertigo(0);
			model.setInvisible(0);
			
			// 重设属性值
			BaseMonster monster =  monsterService.getBaseMonster((int)model.getEid());
			if(monster == null){
				LogUtil.error("monster is null with monsterId is " + model.getEid()+" and refreshMonsterId is "+model.getRefreshMonsterId());
			}		
			model.setP_attack(monster.getP_attack());
			model.setM_attack(monster.getM_attack());
			model.setP_damage(monster.getP_damage());
			model.setM_damage(monster.getM_damage());
			model.setCrit(monster.getCrt());
			model.setTough(monster.getTough());
			model.setDmgDeepPer(monster.getDmgDeepPer());
			model.setDmgReductPer(monster.getDmgReductPer());
			model.setDmgCritPer(monster.getDmgCritPer());
			model.setMoveSpeed(monster.getSpeed());
			model.setImmuneDebuff(monster.getImmuneDebuff());

			Map<String, MonsterPuppet> mMap = sceneModel.getMonsterPuppetMap().get(model.getGridId());
			if(mMap == null){
				mMap = new ConcurrentHashMap<String, MonsterPuppet>();
				sceneModel.getMonsterPuppetMap().put(model.getGridId(), mMap);
			}
			mMap.put(model.getGuid(), model);
			
			this.synMonsterToNearby(model, sceneService.getNearbyPlayerIds(model));
			
			this.offerBossNotice(sceneModel, model);
		}
		
	}
	
	/**
	 *  boss 系统广播
	 */
	private void offerBossNotice(SceneModel sceneModel, MonsterPuppet model){
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IChatService chatService = serviceCollection.getChatService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		if(sceneModel.getMapType() == SceneConstant.WORLD_SCENE &&
				model.getMonsterType() == BattleConstant.MONSTER_TYPE_3){
			if(sceneModel.getMapId() == GuildConstant.MAP_GUILD_7004){
				//帮派领地  【{0}】都护府在都护府领地召唤出强大的BOSS{1}
				long guildId = PlayerUtil.getLongId(sceneModel.getSceneGuid());
				Guild guild = serviceCollection.getGuildService().getGuildById(guildId);
				if(guild != null){
					List<Notice> paramList = new ArrayList<Notice>();			
					Notice notice1 = new Notice(ParamType.PARAM, 0, 0, guild.getGuildName());
					paramList.add(notice1);
					Notice notice2 = new Notice(ParamType.PARAM, 0, 0, model.getName());
					paramList.add(notice2);
					chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_60, paramList, serviceCollection.getGameSocketService().getOnLinePlayerIDList());	
				}

			}else{
				List<Notice> paramList = new ArrayList<Notice>();				
				Notice notice1 = new Notice(ParamType.PARAM, 0, 0, model.getName());
				Notice notice2 = new Notice(ParamType.SCENE, sceneModel.getMapId(), 0, sceneModel.getMapName());
				paramList.add(notice1);
				paramList.add(notice2);
				
				chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_41, paramList, gameSocketService.getOnLinePlayerIDList());
			}

		}
		
	}
	/**
	 * 把周边怪物推送给自己
	 */
	public void synNearbyMonsterToSelf(long playerId, List<MonsterPuppet> monsterPuppets){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		
		S_AddMonsterPuppets.Builder builder = S_AddMonsterPuppets.newBuilder();
		for(MonsterPuppet model : monsterPuppets){
			builder.addListMonsterPuppets(protoBuilderService.buildMonsterPuppetMsg(model));
		}
		
		MessageObj msg = new MessageObj(MessageID.S_AddMonsterPuppets_VALUE, builder.build().toByteArray());
		GameContext.getInstance().getServiceCollection().getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);
	}
	
	/**
	 * 把怪物推送给周边玩家
	 */
	public void synMonsterToNearby(MonsterPuppet monsterPuppet, List<Long> playerIds){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		
		S_AddMonsterPuppets.Builder builder = S_AddMonsterPuppets.newBuilder();
		builder.addListMonsterPuppets(protoBuilderService.buildMonsterPuppetMsg(monsterPuppet));
		
		MessageObj msg = new MessageObj(MessageID.S_AddMonsterPuppets_VALUE, builder.build().toByteArray());
		
		GameContext.getInstance().getServiceCollection().getGameSocketService().sendDataToPlayerList(playerIds, msg);
	}

	/**
	 * 初始召唤怪
	 */
	public void initBeckonPuppet(SceneModel sceneModel,  PlayerPuppet playerPuppet, int monsterId) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IMonsterService monsterService = serviceCollection.getMonsterService();
		ISceneService sceneService = serviceCollection.getSceneService();
		
		BaseMap baseMap = sceneService.getBaseMap(sceneModel.getMapId());
		
		BeckonPuppet model = new BeckonPuppet();
		BaseMonster monster =  monsterService.getBaseMonster(monsterId);
		if(monster == null){
			LogUtil.error("monster is null with monsterId is " + monsterId);
			return;
		}
		
		model.setOwnerGuid(playerPuppet.getGuid());
		model.setPlayerId(playerPuppet.getEid());
		model.setGuid(PlayerUtil.getGuid(PlayerConstant.BECKON, SerialNumberUtil.getLongNum()));
		model.setEid(monsterId);
		model.setName(monster.getName());
		model.setType(PlayerConstant.BECKON);
		model.setLevel(monster.getLv());
		model.setDressStyle(monster.getSculptResid());
		model.setPuppetState(PuppetState.STAND);
		int[] point = CommonUtil.getRandomPoint2(baseMap, playerPuppet.getX(), playerPuppet.getZ(), 200);
		model.setX(point[0]);
		model.setY(playerPuppet.getY());
		model.setZ(point[1]);
		model.setGridId(NineGridUtil.calInGrid(model.getX(), model.getZ(), sceneModel.getColNum()));
		
		model.setHpMax(monster.getHp());
		model.setHp(model.getHpMax());
		
		model.setP_attack(monster.getP_attack());
		model.setM_attack(monster.getM_attack());
		model.setP_damage(monster.getP_damage());
		model.setM_damage(monster.getM_damage());
		model.setCrit(monster.getCrt());
		model.setTough(monster.getTough());
		
		model.setDmgDeepPer(monster.getDmgDeepPer());
		model.setDmgReductPer(monster.getDmgReductPer());
		model.setDmgCritPer(monster.getDmgCritPer());
		model.setMoveSpeed(monster.getSpeed());
		
		model.setMapId(sceneModel.getMapId());
		model.setLine(sceneModel.getLine());
		model.setSceneGuid(sceneModel.getSceneGuid());
		
		model.setCreateTime(System.currentTimeMillis());
		
		model.setPkMode(playerPuppet.getPkMode());
		
		Map<String, BeckonPuppet> mMap = sceneModel.getBeckonPuppetMap().get(model.getGridId());
		if(mMap == null){
			mMap = new ConcurrentHashMap<String, BeckonPuppet>();
			sceneModel.getBeckonPuppetMap().put(model.getGridId(), mMap);
		}
		mMap.put(model.getGuid(), model);
		
		this.synBeckonToNearby(model, GameContext.getInstance().getServiceCollection().getSceneService().getNearbyPlayerIds(model));
	}

	/**
	 * 把召唤怪物推送给周边玩家
	 */
	public void synBeckonToNearby(BeckonPuppet beckonPuppet, List<Long> playerIds){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		
		S_AddBeckonPuppets.Builder builder = S_AddBeckonPuppets.newBuilder();
		builder.addListBeckonPuppets(protoBuilderService.buildBeckonPuppetMsg(beckonPuppet));
		
		MessageObj msg = new MessageObj(MessageID.S_AddBeckonPuppets_VALUE, builder.build().toByteArray());
		GameContext.getInstance().getServiceCollection().getGameSocketService().sendDataToPlayerList(playerIds, msg);
	}
	
	/**
	 * 把周边召唤怪物推送给自己
	 */
	public void synNearbyBeckonToSelf(long playerId, List<BeckonPuppet> beckonPuppets){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		
		S_AddBeckonPuppets.Builder builder = S_AddBeckonPuppets.newBuilder();
		for(BeckonPuppet model : beckonPuppets){
			builder.addListBeckonPuppets(protoBuilderService.buildBeckonPuppetMsg(model));
		}
		
		MessageObj msg = new MessageObj(MessageID.S_AddBeckonPuppets_VALUE, builder.build().toByteArray());
		GameContext.getInstance().getServiceCollection().getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);
	}

	@Override
	public void removeBeckonPuppet(PlayerPuppet p, SceneModel sceneModel) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		ISceneService sceneService = serviceCollection.getSceneService();
		
		Map<Integer, Map<String, BeckonPuppet>> mMap = sceneModel.getBeckonPuppetMap();
		if(mMap == null) return;
		
		BeckonPuppet beckon = null;
		for(Map.Entry<Integer, Map<String, BeckonPuppet>> entry : mMap.entrySet()){
			Map<String, BeckonPuppet> m = entry.getValue();
			for(Map.Entry<String, BeckonPuppet> entry2 : m.entrySet()){
				BeckonPuppet bp = entry2.getValue();
				if(bp.getPlayerId() == p.getEid()){
					beckon = bp;
					break;
				}
			}
			if(beckon != null){
				beckon.setState(BattleConstant.STATE_DEAD);
				m.remove(beckon.getGuid());
				sceneService.removeSelfToNearby(beckon.getGuid(), sceneService.getNearbyPlayerIds(beckon));
				return;
			}
		}
	
	}
	
	/**
	 * 更新怪物位置
	 */
	public void updatePosition(SceneModel sceneModel, MonsterPuppet monster, int newX, int newY, int newZ) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		
		if(monster == null || monster.getState() != BattleConstant.STATE_NORMAL) return;
		
		if(sceneModel.getSceneState() == SceneConstant.SCENE_STATE_DISTROY) return;

		int oldGridId = monster.getGridId(); //旧区域格
		
		monster.setX(newX);
		monster.setY(newY);
		monster.setZ(newZ);		
		
		//新区域格
		int newGridId = NineGridUtil.calInGrid(newX, newZ, sceneModel.getColNum());
		
		if(oldGridId != newGridId){
			List<Integer> oldAroundGrids = sceneModel.getGridMap().get(oldGridId);
			List<Integer> newAroundGrids = sceneModel.getGridMap().get(newGridId);
			if(oldAroundGrids == null || newAroundGrids == null){
				return;
			}
			
			monster.setGridId(newGridId);
			
			List<Long> playerIds = new ArrayList<Long>();
			List<Integer> ids = null;
			
			Map<String, MonsterPuppet> oldMap = sceneModel.getMonsterPuppetMap().get(oldGridId);
			if(oldMap != null){
				oldMap.remove(monster.getGuid());
			}
			
			Map<String, MonsterPuppet> pMap = sceneModel.getMonsterPuppetMap().get(newGridId);
			if(pMap == null){
				pMap = new ConcurrentHashMap<String, MonsterPuppet>();
				sceneModel.getMonsterPuppetMap().put(newGridId, pMap);
			}
			pMap.put(monster.getGuid(), monster);

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
				serviceCollection.getSceneService().removeSelfToNearby(monster.getGuid(), playerIds);
			}
			
			BaseMap baseMap =  serviceCollection.getSceneService().getBaseMap(sceneModel.getMapId());
			if(sceneModel.getMapType() == SceneConstant.WORLD_SCENE && baseMap.getPkModel() == 3){
				playerIds = serviceCollection.getSceneService().getScenePlayerIds(sceneModel);
			}else{
				//进入视野
				playerIds.clear();
				ids = NineGridUtil.getGridList(newAroundGrids, oldAroundGrids, false);
				
				for(Integer gid : ids){
					List<Long> l = sceneModel.getPlayerIdMap().get(gid);
					if(l!= null && !l.isEmpty())  playerIds.addAll(l);	
				}
			}
			
			//自己推送被周边人
			if(!playerIds.isEmpty()){
				this.synMonsterToNearby(monster, playerIds);
			}
		}	
	}
	
	/**
	 * 设置怪物仇恨列表
	 */
	public void addMonsterEnemy(int dmg, BasePuppet basePuppet, BasePuppet monster){
		EnemyModel enemy = monster.getEnemyMap().get(basePuppet.getGuid());
		if(enemy == null){
			enemy = new EnemyModel();
			enemy.setTargetType(basePuppet.getType());
			enemy.setTargetGuid(basePuppet.getGuid());
			monster.getEnemyMap().put(basePuppet.getGuid(), enemy);
		}
		
		if(basePuppet.getType() == PlayerConstant.PLAYER){
			enemy.setPlayerId(basePuppet.getEid());
		}else if(basePuppet.getType() == PlayerConstant.BECKON){
			enemy.setPlayerId(((BeckonPuppet)basePuppet).getPlayerId());
			dmg = dmg*2; //召唤兽仇恨值翻倍
		}
		enemy.setTotalDmg(enemy.getTotalDmg() + dmg);
	}	
	
	/** 
	 * 大荒塔怪物等级
	 * 轮数*3+7
	 */
	private int towerMonsterLv(int curLayerId){
		int lun = (int)Math.ceil(curLayerId * 1.0 / 13);
		return lun*3+7;
	} 
	
	/** 
	 * 大荒塔怪物血量系数
	 * (1+(avgLv-10)*0.1+0.0016*(avgLv-10)^2)*(1+(avgLv-10)*0.01)
	 * 
	 */
	private double towerMonsterHp(int curLayerId){		
		int level = this.towerMonsterLv(curLayerId);		
		return this.actMonsterHp(level);
	} 
	
	/** 
	 * 大荒塔怪物属性系数
	 * (1+(avgLv-10)*0.1+0.0016*(avgLv-10)^2)*(1+(avgLv-10)*0.01)
	 */
	private double towerMonsterPro(int curLayerId){
		int level = this.towerMonsterLv(curLayerId);
		return this.actMonsterPro(level);
	}
	
	/** 
	 * 环任务副本怪物属性系数
	 * 1 + weekTaskNum*0.005
	 */
	private double weekTaskPro(int weekTaskNum){
		return 1 + weekTaskNum*0.005;
	} 
	
	/** 
	 * 活动副本怪物血量系数
	 * (1+(avgLv-10)*0.1+0.0022*(avgLv-10)^2)*(1+(avgLv-10)*0.01)
	 */
	private double actMonsterHp(int avgLv){
		return (1 + (avgLv - 10)*0.1 + 0.0022*Math.pow((avgLv - 10), 2))*(1+(avgLv-10)*0.01);
	} 
	
	/** 
	 * 活动副本怪物属性系数
	 * (1+(avgLv-10)*0.1+0.0022*(avgLv-10)^2)
	 */
	private double actMonsterPro(int avgLv){
		return (1+(avgLv-10)*0.1+0.0022*Math.pow((avgLv - 10), 2));
	}


}
