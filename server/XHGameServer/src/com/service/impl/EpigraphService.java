
package com.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.cache.BaseCacheService;
import com.cache.CacheService;
import com.cache.CacheSynDBService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.LockService;
import com.common.RandomService;
import com.common.ServiceCollection;
import com.constant.CacheConstant;
import com.constant.CacheSynConstant;
import com.constant.ExceptionConstant;
import com.constant.ItemConstant;
import com.constant.LockConstant;
import com.constant.TaskConstant;
import com.constant.WakanConstant;
import com.dao.epigraph.BaseEpigrapDAO;
import com.dao.epigraph.PlayerWeaponEffectDAO;
import com.domain.GameEntity;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.bag.BaseItem;
import com.domain.bag.PlayerBag;
import com.domain.bag.PlayerEquipment;
import com.domain.epigraph.BaseEpigraph;
import com.domain.epigraph.PlayerWeaponEffect;
import com.domain.player.Player;
import com.domain.player.PlayerProperty;
import com.domain.skill.PlayerSkill;
import com.message.EquipmentProto.S_SynPlayerWeaponEffect;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.IBagService;
import com.service.IEpigraphService;
import com.service.IEquipmentService;
import com.service.IPlayerService;
import com.service.IProtoBuilderService;
import com.service.IRewardService;
import com.service.ISkillService;
import com.service.ITaskService;
import com.util.IDUtil;
import com.util.SplitStringUtil;

/**
 * 铭文系统
 * @author jiangqin
 * @date 2017-2-18
 */
public class EpigraphService implements IEpigraphService {
	
	private BaseEpigrapDAO baseEpigrapDAO = new BaseEpigrapDAO();
	private PlayerWeaponEffectDAO playerWeaponEffectDao = new PlayerWeaponEffectDAO();
	
	@Override
	public void initBaseCache() {
		Map<Integer, BaseEpigraph> baseEpigraphMap = new HashMap<Integer, BaseEpigraph>();
		List<BaseEpigraph> listBaseEpigraph = baseEpigrapDAO.listBaseEpigraph();
		for(BaseEpigraph model : listBaseEpigraph){
			model.setRandTypeList(SplitStringUtil.getIntIntList(model.getRandTypes()));
			model.setRandSkillList(SplitStringUtil.getIntIntList(model.getRandSkills()));
			model.setRandAttrList(SplitStringUtil.getIntIntList(model.getRandAttrs()));
			baseEpigraphMap.put(model.getId(), model);
		}	
		BaseCacheService.putToBaseCache(CacheConstant.BASE_EPIGRAPH, baseEpigraphMap);
	}
	
	@SuppressWarnings("unchecked")
	private BaseEpigraph getBaseEpigraphById(int Id){
		Map<Integer, BaseEpigraph> baseEpigraphMap = (Map<Integer, BaseEpigraph>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_EPIGRAPH);
		if (baseEpigraphMap == null) return null;
		return baseEpigraphMap.get(Id);
	}
	
	@Override
	public void epigraph(long playerId, long playerEquipmentId, int holeId, long playerBagId) throws Exception {		
		if(playerEquipmentId < 1 || playerBagId < 1 || holeId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();			
		IEquipmentService equipmentService =serviceCollection.getEquipmentService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_EPIGRAPH)) {
			
			PlayerEquipment playerEquipment = equipmentService.getPlayerEquipmentByID(playerId, playerEquipmentId);
			if (playerEquipment == null) throw new GameException(ExceptionConstant.EQUIP_1400);
			if (playerEquipment.getState() != ItemConstant.EQUIP_STATE_DRESS)throw new GameException(ExceptionConstant.EQUIP_1401);
			if (playerEquipment.getEquipType() != WakanConstant.WEAPON01)throw new GameException(ExceptionConstant.EQUIP_1405);
			if (playerEquipment.getHoleNum() < holeId) throw new GameException(ExceptionConstant.EQUIP_1406);
			
			IPlayerService playerService = serviceCollection.getPlayerService();
			Player player = playerService.getPlayerByID(playerId);
			
			IBagService bagService = serviceCollection.getBagService();		
			PlayerBag playerBag = bagService.getPlayerBagById(playerId, playerBagId);
			if(playerBag == null || playerBag.getNum() < 1) throw new GameException(ExceptionConstant.BAG_1303);
			
			BaseItem baseItem = bagService.getBaseItemById((int)playerBag.getItemId());
			if(baseItem.getNeedJob() > 0){
				if(baseItem.getNeedJob() != player.getCareer()) throw new GameException(ExceptionConstant.BAG_1308);
			}		
			
			PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
			if(baseItem.getLevel() > playerProperty.getLevel()) throw new GameException(ExceptionConstant.PLAYER_1110);
		
			// 读取改变效果表
			BaseEpigraph baseEpigraph = this.getBaseEpigraphById(baseItem.getEffectValue());
			if(baseEpigraph == null) throw new GameException(ExceptionConstant.BAG_1307);
			
			int type = this.getRandType(baseEpigraph.getRandTypeList());
			if(type <= 0) return;
			
			//扣除道具
			IRewardService rewardService = serviceCollection.getRewardService();			
			rewardService.deductItemByPlayerBagId(playerId, playerBagId, 1);
			
			ISkillService skillService = serviceCollection.getSkillService();
			
			
			Map<Integer, PlayerWeaponEffect> map = this.getPlayerWeaponEffectMap(playerId);
			
			List<Integer> randList = this.getRandList(playerId, type, baseEpigraph);
			
			if(type == 1){
				PlayerSkill playerSkill = skillService.getPlayerSkillByIndex(playerId, randList.get(0));
				if(playerSkill == null){
					type = 2;
					randList = this.getRandList(playerId, type, baseEpigraph);
				}
			}
			int baseId = 0;
			int proValue = 0;
			List<PlayerSkill> changeListPlayerSkill = new ArrayList<PlayerSkill>();
			List<PlayerWeaponEffect> playerWeaponEffectList = new ArrayList<PlayerWeaponEffect>();
			
			if(type == 1){
				baseId = randList.get(1); //技能基础编号
				proValue = randList.get(0); //技能下标
				
				PlayerSkill playerSkill = skillService.getPlayerSkillByIndex(playerId, proValue);
				
				int mwSkillId = playerSkill.getLevel() * 10000 + baseId;
				playerSkill.setMwSkillId(mwSkillId);						
				skillService.updatePlayerSkill(playerSkill);
				
				changeListPlayerSkill.add(playerSkill);					
				
				//如果其他铭文孔也铭文了相同技能下标的铭文， 则销毁掉
				for(Map.Entry<Integer, PlayerWeaponEffect> entry : map.entrySet()){
					PlayerWeaponEffect model = entry.getValue();
					if(model.getHoldId() == holeId){
						if(model.getType() == 1){
							if(model.getProValue() != proValue){
								PlayerSkill playerSkillOld = skillService.getPlayerSkillByIndex(playerId, model.getProValue());
								playerSkillOld.setMwSkillId(0);
								skillService.updatePlayerSkill(playerSkillOld);
								changeListPlayerSkill.add(playerSkillOld);	
							}
						}else if(model.getType() == 2){
							Map<Integer, Integer> addProMap = new HashMap<Integer, Integer>();
							
							if(model != null && model.getEffectId() > 0){
								//旧属性要减去
								addProMap.put(model.getBaseId(), -model.getProValue());
							}
							serviceCollection.getPropertyService().addProValue(playerId, addProMap, true, true);
						}
					}else{
						if(model.getType() == 1 && model.getProValue() == proValue){
							model.clear();
							this.updatePlayerWeaponEffect(model);
							playerWeaponEffectList.add(model);
						}
					}
				}
				
				skillService.synChangeListPlayerSkill(playerId, changeListPlayerSkill);
			}else{
				baseId = randList.get(0); //属性编号
				proValue = randList.get(1); //属性值
				
				
				PlayerWeaponEffect playerWeaponEffect = map.get(holeId);
				
				Map<Integer, Integer> addProMap = new HashMap<Integer, Integer>();
				
				if(playerWeaponEffect != null && playerWeaponEffect.getEffectId() > 0){
					if(playerWeaponEffect.getType() == 1){
						PlayerSkill playerSkillOld = skillService.getPlayerSkillByIndex(playerId, playerWeaponEffect.getProValue());
						playerSkillOld.setMwSkillId(0);
						skillService.updatePlayerSkill(playerSkillOld);
						changeListPlayerSkill.add(playerSkillOld);	
						skillService.synChangeListPlayerSkill(playerId, changeListPlayerSkill);
					}else{
						//旧属性要减去
						addProMap.put(playerWeaponEffect.getBaseId(), -playerWeaponEffect.getProValue());
					}
				}
				
				Integer totalPro = addProMap.get(baseId);
				if(totalPro == null){
					totalPro = 0;
				}
				totalPro += proValue;
				addProMap.put(baseId, totalPro);
				
				serviceCollection.getPropertyService().addProValue(playerId, addProMap, true, true);
			}
			
			
			// 改变玩家铭文信息并更新缓存数据	
			PlayerWeaponEffect playerWeaponEffect = map.get(holeId);
			if(playerWeaponEffect == null){
				playerWeaponEffect = new PlayerWeaponEffect();
				playerWeaponEffect.setId(IDUtil.geneteId(PlayerWeaponEffect.class));
				playerWeaponEffect.setPlayerId(playerId);
				playerWeaponEffect.setHoldId(holeId);
				playerWeaponEffect.setEffectId(baseEpigraph.getId());
				playerWeaponEffect.setType(type);
				playerWeaponEffect.setBaseId(baseId);
				playerWeaponEffect.setProValue(proValue);
				playerWeaponEffectDao.createPlayerWeaponEffect(playerWeaponEffect);
				map.put(holeId, playerWeaponEffect);			
			}else{
				playerWeaponEffect.setEffectId(baseEpigraph.getId());
				playerWeaponEffect.setType(type);
				playerWeaponEffect.setBaseId(baseId);
				playerWeaponEffect.setProValue(proValue);
				this.updatePlayerWeaponEffect(playerWeaponEffect);
			}
			playerWeaponEffectList.add(playerWeaponEffect);
			// 同步玩家铭文改变的效果信息
			this.synPlayerWeaponEffect(playerId, playerWeaponEffectList);
			
			// 任务
			ITaskService taskService = GameContext.getInstance().getServiceCollection().getTaskService();
			List<Integer> conditionList = new ArrayList<Integer>();				
			conditionList.add(1);
			taskService.executeTask(playerId, TaskConstant.TYPE_7, conditionList);
		}		
	}
	
	/**
	 * 更新玩家的铭文技能信息
	 * @param 玩家技能列表
	 * @param 装备最新铭文信息0	
	 */
	@Override
	public void clearPlayerWeaponEffect(long playerId){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		ISkillService skillService = serviceCollection.getSkillService();	
		// 根据铭文效果ID 确定改变的类型
	
		Map<Integer, PlayerWeaponEffect>  map = this.getPlayerWeaponEffectMap(playerId);	
		if(map.isEmpty()) return;
		
		List<PlayerSkill> changeListPlayerSkill = new ArrayList<PlayerSkill>();
		List<PlayerWeaponEffect> playerWeaponEffectList = new ArrayList<PlayerWeaponEffect>();
		
		Map<Integer, Integer> addProMap = new HashMap<Integer, Integer>();
		
		for(Map.Entry<Integer, PlayerWeaponEffect> entry : map.entrySet()){
			PlayerWeaponEffect playerWeaponEffect = entry.getValue();	
			
			if(playerWeaponEffect.getEffectId() <= 0) continue;
			
			//把被装备铭文的技能类型的铭文ID清0
			if(playerWeaponEffect.getType() == 1) {
				PlayerSkill playerSkill = skillService.getPlayerSkillByIndex(playerId, playerWeaponEffect.getProValue());
				if(playerSkill != null) {
					playerSkill.setMwSkillId(0);	
					skillService.updatePlayerSkill(playerSkill);
					changeListPlayerSkill.add(playerSkill);	
				}																
			}else if(playerWeaponEffect.getType() == 2){
				Integer proValue = addProMap.get(playerWeaponEffect.getBaseId());
				if(proValue == null){
					proValue = 0;
				}
				proValue -= playerWeaponEffect.getProValue();
				addProMap.put(playerWeaponEffect.getBaseId(), proValue);
			}				
			playerWeaponEffect.clear();
			this.updatePlayerWeaponEffect(playerWeaponEffect);
			playerWeaponEffectList.add(playerWeaponEffect);
		}
		
		// 玩家技能改变通知
		if(!changeListPlayerSkill.isEmpty()){
			skillService.synChangeListPlayerSkill(playerId, changeListPlayerSkill);
		}
		// 同步玩家铭文改变的效果信息
		if(!playerWeaponEffectList.isEmpty()){
			this.synPlayerWeaponEffect(playerId, playerWeaponEffectList);
		}
		//铭文属性卸载
		if(!addProMap.isEmpty()){
			serviceCollection.getPropertyService().addProValue(playerId, addProMap, true, true);
		}
	}


	/**
	 * 随机类型   1：技能 2：属性
	 */
	private int getRandType(List<List<Integer>> randTypeList){
		if(randTypeList.size() == 1) return randTypeList.get(0).get(0);	
		int weightSum = 0;		
		for(int i = 0; i < randTypeList.size(); i++){				
			weightSum = weightSum + randTypeList.get(i).get(1);
		}	
		
		int m = 0;
		int n = RandomService.getRandomNum(weightSum);		
		for(int i = 0; i < randTypeList.size(); i++){				
			if(n< m + randTypeList.get(i).get(1)){
				return  randTypeList.get(i).get(0);
			}
			 m += randTypeList.get(i).get(1);  
		}	
		return 0;
	} 
	
	/**
	 * 从技能池或属性池 随机一条
	 */
	private List<Integer> getRandList(long playerId, int type, BaseEpigraph baseEpigraph){
		
		if(type == 1){
			int weightSum = 0;		
			for(int i = 0; i < baseEpigraph.getRandSkillList().size(); i++){				
				weightSum = weightSum + baseEpigraph.getRandSkillList().get(i).get(2);
			}	
			
			int m = 0;
			int n = RandomService.getRandomNum(weightSum);		
			for(int i = 0; i < baseEpigraph.getRandSkillList().size(); i++){				
				if(n< m + baseEpigraph.getRandSkillList().get(i).get(2)){
					return baseEpigraph.getRandSkillList().get(i);
				}
				 m += baseEpigraph.getRandSkillList().get(i).get(2);  
			}
		}else if (type == 2){
			int weightSum = 0;		
			for(int i = 0; i < baseEpigraph.getRandAttrList().size(); i++){				
				weightSum = weightSum + baseEpigraph.getRandAttrList().get(i).get(2);
			}	
			
			int m = 0;
			int n = RandomService.getRandomNum(weightSum);		
			for(int i = 0; i < baseEpigraph.getRandAttrList().size(); i++){				
				if(n< m + baseEpigraph.getRandAttrList().get(i).get(2)){
					return  baseEpigraph.getRandAttrList().get(i);
				}
				 m += baseEpigraph.getRandAttrList().get(i).get(2);  
			}
		}
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public Map<Integer, PlayerWeaponEffect> getPlayerWeaponEffectMap(long playerId){
		Map<Integer, PlayerWeaponEffect> map = (Map<Integer, PlayerWeaponEffect>)CacheService.getFromCache(CacheConstant.PLAYER_WEAPON_EFFECT + playerId);
		if (map == null){
			map = new ConcurrentHashMap<Integer, PlayerWeaponEffect>();
			List<PlayerWeaponEffect> list = playerWeaponEffectDao.listBaseEpigraph(playerId);
			for(PlayerWeaponEffect playerWeaponEffect : list){
				map.put(playerWeaponEffect.getHoldId(), playerWeaponEffect);
			}
			
			CacheService.putToCache(CacheConstant.PLAYER_WEAPON_EFFECT + playerId, map);
		}
		return map;
	}	

	private void updatePlayerWeaponEffect(PlayerWeaponEffect playerWeaponEffect) {
		Set<GameEntity> updateCacheList = CacheSynDBService.getFromFiveUpdateTwoCache(CacheSynConstant.PLAYER_WEAPON_EFFECT);
		
		if (!updateCacheList.contains(playerWeaponEffect)) {
			updateCacheList.add(playerWeaponEffect);
		}
		
	}	
	
	/**
	 * 同步铭文信息
	 */
	private void synPlayerWeaponEffect(long playerId, List<PlayerWeaponEffect> playerWeaponEffectList){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		
		S_SynPlayerWeaponEffect.Builder builder = S_SynPlayerWeaponEffect.newBuilder();	
		for(PlayerWeaponEffect playerWeaponEffect : playerWeaponEffectList){
			builder.addEffectMsg(protoBuilderService.buildPlayerWeaponEffectMsg(playerWeaponEffect));		
		}
		
		MessageObj msg = new MessageObj(MessageID.S_SynPlayerWeaponEffect_VALUE, builder.build().toByteArray());
		gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
	}

	@Override
	public void deleteCache(long playerId) {
		CacheService.deleteFromCache(CacheConstant.PLAYER_WEAPON_EFFECT + playerId);
	}
	
}
