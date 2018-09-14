package com.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cache.BaseCacheService;
import com.cache.CacheService;
import com.cache.CacheSynDBService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.LockService;
import com.common.ServiceCollection;
import com.constant.CacheConstant;
import com.constant.CacheSynConstant;
import com.constant.ExceptionConstant;
import com.constant.LockConstant;
import com.constant.TaskConstant;
import com.dao.skill.BaseSkillDAO;
import com.dao.skill.PlayerSkillDAO;
import com.domain.GameEntity;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.bag.BaseItem;
import com.domain.base.BaseNewRole;
import com.domain.player.Player;
import com.domain.player.PlayerProperty;
import com.domain.player.PlayerWealth;
import com.domain.skill.BaseSkill;
import com.domain.skill.BaseSkillUp;
import com.domain.skill.DamagModel;
import com.domain.skill.PlayerSkill;
import com.domain.skill.RangeModel;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.SkillProto.S_CreatePlayerSkill;
import com.message.SkillProto.S_SynPlayerSkill;
import com.service.IBagService;
import com.service.IPlayerService;
import com.service.IProtoBuilderService;
import com.service.IRewardService;
import com.service.ISkillService;
import com.util.IDUtil;
import com.util.SplitStringUtil;

/**
 * 技能系统
 * @author ken
 * @date 2017-1-19
 */
public class SkillService implements ISkillService {

	private BaseSkillDAO baseSkillDAO = new BaseSkillDAO();
	private PlayerSkillDAO playerSkillDAO = new PlayerSkillDAO();
	
	@Override
	public void initBaseCache() {

		//技能结算表
		Map<Integer, DamagModel> damageMap = new HashMap<Integer, DamagModel>();
		List<DamagModel> damageLists = baseSkillDAO.listDamagModels();
		for(DamagModel model : damageLists){
			model.setBuffIdList(SplitStringUtil.getIntList(model.getBuffIds()));
			model.setMyBuffIdList(SplitStringUtil.getIntList(model.getMyBuffIds()));
			damageMap.put(model.getUn32SkillModelID(), model);
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_DAMAGE_MODEL, damageMap);
		
		//对地持续范围技
		Map<Integer, Integer> n32LifeTimeMap = new HashMap<Integer, Integer>();
		List<RangeModel> rangeLists = baseSkillDAO.listRangeModels();
		for(RangeModel model : rangeLists){
			n32LifeTimeMap.put(model.getUn32SkillModelID(), model.getN32LifeTime());
		}
		
		//技能总表
		Map<Integer, BaseSkill> skillMap = new HashMap<Integer, BaseSkill>();
		List<BaseSkill> skillLists = baseSkillDAO.listBaseSkills();
		for(BaseSkill model : skillLists){
			model.setAsSkillModels(SplitStringUtil.getIntList(model.getAsSkillModelList()));
			model.setN32Delays(SplitStringUtil.getIntList(model.getN32Delay()));
			
			if(model.getAsSkillModels() != null){
				for(Integer modelId : model.getAsSkillModels()){
					if(n32LifeTimeMap.containsKey(modelId)){
						model.setN32LifeTime(n32LifeTimeMap.get(modelId));
						break;
					}
				}
			}
			
			skillMap.put(model.getUn32SkillID(), model);
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_SKILL, skillMap);
		
		Map<Integer, BaseSkillUp> baseSkillUpMap = new HashMap<Integer, BaseSkillUp>();
		List<BaseSkillUp> skillUps = baseSkillDAO.listBaseSkillUps();
		for(BaseSkillUp model : skillUps){
			baseSkillUpMap.put(model.getId(), model);
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_SKILL_UP, baseSkillUpMap);
	}
	
	@Override
	public void deleteCache(long playerId) {
		CacheService.deleteFromCache(CacheConstant.PLAYER_SKILL+playerId);
	}
	
	/**
	 * 取技能结算模块配置
	 */
	@SuppressWarnings("unchecked")
	public DamagModel getDamagModel(int skillModelId){
		Map<Integer, DamagModel> damageMap = (Map<Integer, DamagModel>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_DAMAGE_MODEL);
		return damageMap.get(skillModelId);
	}

	/**
	 * 取技能配置
	 */
	@SuppressWarnings("unchecked")
	public BaseSkill getBaseSkill(int skillId){
		Map<Integer, BaseSkill> skillMap = (Map<Integer, BaseSkill>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_SKILL);
		return skillMap.get(skillId);
	}
	
	/**
	 * 取技能升级配置
	 */
	@SuppressWarnings("unchecked")
	private BaseSkillUp getBaseSkillUp(int skillId){
		Map<Integer, BaseSkillUp> map = (Map<Integer, BaseSkillUp>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_SKILL_UP);
		return map.get(skillId);
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<PlayerSkill> listPlayerSkills(long playerId) {
		List<PlayerSkill> lists = (List<PlayerSkill>)CacheService.getFromCache(CacheConstant.PLAYER_SKILL+playerId);
		if(lists == null){
			lists = playerSkillDAO.listPlayerSkills(playerId);
			CacheService.putToCache(CacheConstant.PLAYER_SKILL+playerId, lists);
		}
		return lists;
	}
	
	/**
	 * 根据技能类型ID 找出玩家技能信息
	 */
	@Override
	public PlayerSkill getPlayerSkillByIndex(long playerId, int skillIndex){	
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();		
		ISkillService skillService = serviceCollection.getSkillService();
		List<PlayerSkill> listPlayerSkills = skillService.listPlayerSkills(playerId);
		for (PlayerSkill playerSkill : listPlayerSkills){
			if (playerSkill.getSkillIndex() == skillIndex){
				return playerSkill;
			}
		}
		return null;
	}			
		
	
	/**
	 * 取玩家技能
	 */
	public PlayerSkill getPlayerSkill(long playerId, int skillId){
		List<PlayerSkill> lists = this.listPlayerSkills(playerId);
		for(PlayerSkill model : lists){
			if(model.getSkillId() == skillId) return model;
		}
		return null;
	}
	
	/**
	 * 同步缓存更新
	 */
	@Override
	public void updatePlayerSkill(PlayerSkill playerSkill){
		Set<GameEntity> lists =  CacheSynDBService.getFromFiveUpdateTwoCache(CacheSynConstant.PLAYER_SKILL);
		if (!lists.contains(playerSkill)) {
			lists.add(playerSkill);
		}
	}

	@Override
	public PlayerSkill upgradePlayerSkill(long playerId, int skillId) throws Exception {
		if(playerId < 1 || skillId < 1) throw new GameException(ExceptionConstant.ERROR_10000);		
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_SKILL)) {
			PlayerSkill playerSkill = this.getPlayerSkill(playerId, skillId);
			if(playerSkill == null){			
				throw new GameException(ExceptionConstant.SKILL_1600);
			}
			
			// 升级后的技能id
			int skillUpId = skillId + 10000;			
			BaseSkillUp baseSkillUp = this.getBaseSkillUp(skillUpId);
			if(baseSkillUp == null){
				System.out.println("upgradePlayerSkill baseSkillUp 技能基础ID:" + skillId + "null");
				throw new GameException(ExceptionConstant.SKILL_1601);
			}
			
			BaseSkill baseSkill = this.getBaseSkill(skillId);
			if(baseSkill == null) throw new GameException(ExceptionConstant.SKILL_1600);
			
			if(playerSkill.getLevel() >= baseSkill.getLevelMax()) throw new GameException(ExceptionConstant.SKILL_1601);
			
			PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
			
			if(playerProperty.getLevel() < baseSkillUp.getNeedLevel()) throw new GameException(ExceptionConstant.PLAYER_1110);
			
			PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);
			// 升级消耗金币
			if(playerWealth.getGold() < baseSkillUp.getNeedMoney()) throw new GameException(ExceptionConstant.PLAYER_1112);			//扣除熟练度
			if(playerSkill.getMastery() < baseSkillUp.getNeedMastery()) throw new GameException(ExceptionConstant.SKILL_1603);
			

			playerService.addGold_syn(playerId, -baseSkillUp.getNeedMoney());
			
			playerSkill.setMastery(playerSkill.getMastery() - baseSkillUp.getNeedMastery());
			playerSkill.setSkillId(skillUpId);
			playerSkill.setLevel(playerSkill.getLevel() + 1);
			
			if(playerSkill.getMwSkillId() > 0){
				playerSkill.setMwSkillId(playerSkill.getMwSkillId() + 10000);
			}
			this.updatePlayerSkill(playerSkill);			
			
			playerProperty.setSkillLv(playerProperty.getSkillLv() + 1);			
			serviceCollection.getPropertyService().synBattleValue(playerProperty);			
			
			//升级任务
			List<Integer> conditionList = new ArrayList<Integer>();
			conditionList.add(baseSkill.getSkillIndex());
			conditionList.add(playerSkill.getLevel());
			serviceCollection.getTaskService().executeTask(playerId, TaskConstant.TYPE_9, conditionList);
			
			return playerSkill;
		}	
	}

	@Override
	public PlayerSkill studyPlayerSkill(long playerId, int skillId, boolean initFlag) throws Exception {
		if(playerId < 1 || skillId < 1) throw new GameException(ExceptionConstant.ERROR_10000);		
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_SKILL)) {
			BaseSkill baseSkill = this.getBaseSkill(skillId);
			if(baseSkill == null) {
				System.out.println("studyPlayerSkill 技能基础ID:" + skillId + "null");
				throw new GameException(ExceptionConstant.SKILL_1600);
			}	
			
			PlayerSkill playerSkill = this.getPlayerSkillByIndex(playerId, baseSkill.getSkillIndex());
			if(playerSkill != null){				
				throw new GameException(ExceptionConstant.SKILL_1602);
			}
			
			BaseSkillUp baseSkillUp = this.getBaseSkillUp(skillId);
			if(baseSkillUp == null){
				System.out.println("studyPlayerSkill baseSkillUp 技能基础ID:" + skillId + "null");
				throw new GameException(ExceptionConstant.SKILL_1600);
			}
			
			//学习技能消耗
			PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);
			if(playerWealth.getGold() < baseSkillUp.getNeedMoney()) throw new GameException(ExceptionConstant.PLAYER_1112);
			playerService.addGold_syn(playerId, -baseSkillUp.getNeedMoney());
			
			PlayerProperty playerProperty = serviceCollection.getPlayerService().getPlayerPropertyById(playerId);
			playerProperty.setSkillLv(playerProperty.getSkillLv() + 1);
			
			playerSkill = new PlayerSkill();
			playerSkill.setId(IDUtil.geneteId(PlayerSkill.class));
			playerSkill.setPlayerId(playerId);
			playerSkill.setSkillId(skillId);
			playerSkill.setLevel(1);
			playerSkill.setSkillIndex(baseSkill.getSkillIndex());
			playerSkillDAO.createPlayerSkill(playerSkill);
			
			this.listPlayerSkills(playerId).add(playerSkill);
			
			if(!initFlag){
				//学习技能任务触发			
				List<Integer> conditionList = new ArrayList<Integer>();
				conditionList.add(baseSkill.getSkillIndex());
				serviceCollection.getTaskService().executeTask(playerId, TaskConstant.TYPE_8, conditionList);
				
				serviceCollection.getPropertyService().synBattleValue(playerProperty);
			}

			return playerSkill;
		}
		
	}

	@Override
	public void testOpenAllPlayerSkills(long playerId) throws Exception{
		if(playerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);		
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		Player player = playerService.getPlayerByID(playerId);
		
		BaseNewRole baseNewRole = serviceCollection.getCommonService().getBaseNewRole(player.getCareer());
		List<Integer> skillIDs = baseNewRole.getInitSkillList();
		
		for (Integer skillId : skillIDs) {
			BaseSkill baseSkill = this.getBaseSkill(skillId);
			if(baseSkill.getbIfNomalAttack() == 1) continue;
			
			PlayerSkill playerSkill = this.getPlayerSkillByIndex(playerId, baseSkill.getSkillIndex());
			if(playerSkill != null){
				continue;
			}
			PlayerSkill model = studyPlayerSkill(playerId, skillId, false);
			model.setMastery(99);
			this.updatePlayerSkill(model);
			
			S_CreatePlayerSkill.Builder builder = S_CreatePlayerSkill.newBuilder();
			builder.setPlayerSkill(protoBuilderService.buildPlayerSkillMsg(model));
			MessageObj msg = new MessageObj(MessageID.S_CreatePlayerSkill_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}
	}
	

	@Override
	public void addSkillMastery(long playerId, int skillId, int itemId)throws Exception {
		if(playerId < 1 || skillId < 1 || itemId < 1) throw new GameException(ExceptionConstant.ERROR_10000);		
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IBagService bagService = serviceCollection.getBagService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_SKILL)) {
			BaseSkill baseSkill = this.getBaseSkill(skillId);			
			if(baseSkill == null) throw new GameException(ExceptionConstant.SKILL_1600);
			
			if(baseSkill.getSkillIndex() <= 0){
				System.out.println("addSkillMastery 技能ID 有误" + skillId);
				return;
			}
			
			PlayerSkill playerSkill = this.getPlayerSkill(playerId, skillId);
			if(playerSkill == null) throw new GameException(ExceptionConstant.SKILL_1600);		
			
			if(playerSkill.getLevel() >= baseSkill.getLevelMax()) throw new GameException(ExceptionConstant.SKILL_1601);

			BaseSkillUp baseSkillUp = this.getBaseSkillUp(skillId);
			if(baseSkillUp == null) throw new GameException(ExceptionConstant.SKILL_1600);
			
			BaseItem baseItem = bagService.getBaseItemById(itemId);
			if(baseItem == null) throw new GameException(ExceptionConstant.BAG_1300);
			
			Player player = playerService.getPlayerByID(playerId);
			if(baseItem.getNeedJob() > 0){
				if(baseItem.getNeedJob() != player.getCareer()) throw new GameException(ExceptionConstant.BAG_1308);
			}
			
			PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
			if(baseItem.getLevel() > playerProperty.getLevel()) throw new GameException(ExceptionConstant.PLAYER_1110);
			
			
			IRewardService rewardService = serviceCollection.getRewardService();			
			rewardService.deductItem(playerId, itemId, 1, true);
			
			double addValue = baseItem.getEffectValue() * baseSkillUp.getRatio() * 0.001;			
			playerSkill.setMastery(playerSkill.getMastery() + (int)addValue);			
			this.updatePlayerSkill(playerSkill);
			
			List<PlayerSkill> changeListPlayerSkill = new ArrayList<PlayerSkill>();
			changeListPlayerSkill.add(playerSkill);
			this.synChangeListPlayerSkill(playerId, changeListPlayerSkill);			
		}
	}

	
	@Override
	public void addMastery(long playerId, int skillId) {
		if(playerId < 1 || skillId < 1) return;	
		
//		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		
		BaseSkill baseSkill = this.getBaseSkill(skillId);
		if(baseSkill.getSkillIndex() <= 0) return;
		
		PlayerSkill playerSkill = this.getPlayerSkillByIndex(playerId, baseSkill.getSkillIndex());
		if(playerSkill == null) return;
		
		BaseSkillUp baseSkillUp = this.getBaseSkillUp(skillId);
		
		playerSkill.setMastery(playerSkill.getMastery() + baseSkillUp.getAddMastery());
		this.updatePlayerSkill(playerSkill);
		
//		S_SynPlayerSkill.Builder builder = S_SynPlayerSkill.newBuilder();
//		builder.addPlayerSkill(serviceCollection.getProtoBuilderService().buildPlayerSkillMsg(playerSkill));
//		MessageObj msg = new MessageObj(MessageID.S_SynPlayerSkill_VALUE, builder.build().toByteArray());
//		serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);
	}

	/**
	 * 同步改变的技能信息
	 */	
	@Override
	public void synChangeListPlayerSkill(long playerId, List<PlayerSkill> changeListPlayerSkill){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		
		S_SynPlayerSkill.Builder builder = S_SynPlayerSkill.newBuilder();
		for(PlayerSkill playerSkill :  changeListPlayerSkill){
			builder.addPlayerSkill(protoBuilderService.buildPlayerSkillMsg(playerSkill));
		}		
		
		MessageObj msg = new MessageObj(MessageID.S_SynPlayerSkill_VALUE, builder.build().toByteArray());
		serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);
	}

}
	
	