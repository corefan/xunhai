package com.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.common.GameContext;
import com.common.GameSocketService;
import com.common.LockService;
import com.common.ServiceCollection;
import com.constant.BattleConstant;
import com.constant.ConfigConstant;
import com.constant.ExceptionConstant;
import com.constant.GuildConstant;
import com.constant.InOutLogConstant;
import com.constant.ItemConstant;
import com.constant.LockConstant;
import com.constant.ProdefineConstant;
import com.constant.SceneConstant;
import com.constant.TaskConstant;
import com.core.GameMessage;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.bag.BaseItem;
import com.domain.bag.PlayerBag;
import com.domain.bag.PlayerEquipment;
import com.domain.base.BaseProperty;
import com.domain.guild.Guild;
import com.domain.guild.PlayerGuild;
import com.domain.map.BaseMap;
import com.domain.player.Player;
import com.domain.player.PlayerExt;
import com.domain.player.PlayerProperty;
import com.domain.puppet.PlayerPuppet;
import com.domain.task.BaseTask;
import com.domain.task.PlayerTask;
import com.domain.tianti.BaseTiantiScore;
import com.domain.tianti.PlayerTianti;
import com.message.BagProto.S_SynBagItem;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.TestProto.C_Test;
import com.scene.SceneModel;
import com.service.IBagService;
import com.service.IBuffService;
import com.service.ICommonService;
import com.service.IEquipmentService;
import com.service.IGuildService;
import com.service.IInstanceService;
import com.service.IMonsterService;
import com.service.IPayService;
import com.service.IPlayerService;
import com.service.IPropertyService;
import com.service.IProtoBuilderService;
import com.service.IRebotService;
import com.service.ISceneService;
import com.service.ITaskService;
import com.service.ITiantiService;

/**
 * 外挂
 * @author ken
 * @date 2017-1-5
 */
public class TestAction {

	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	private GameSocketService gameSocketService = serviceCollection.getGameSocketService();
	
	/**
	 * 测试接口
	 */
	
	public void test(GameMessage gameMessage) throws Exception {
		
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_Test param = C_Test.parseFrom(gameMessage.getData());
		int type = param.getType();
		int param1 = param.getParam1();
		int param2 = param.getParam2();

		switch (type) {
		case 1:
			this.testGetItem(playerId, param1, param2);
			break;
		case 2:
			this.testUpLevel(playerId, param1);
			break;
		case 3:
			this.testGotoScene(playerId, param1);
			break;
		case 4:
			this.testAddExp(playerId, param1);
			break;
		case 5:
			this.testComleteTask(playerId, param1);
			break;
		case 6:
			this.testAcceptTask(playerId, param1);
			break;
		case 7:
			this.testAddGold(playerId, param1);
			break;
		case 8:
			this.testAddDiamond(playerId, param1);
			break;
		case 9:
			this.testSetAttack(playerId, param1);
			break;
		case 10:
			this.testSetMoveSpeed(playerId, param1);
			break;
		case 11:
			this.testRefreshMonster(playerId, param1);
			break;
		case 12:
			this.testSetHpMax(playerId, param1);
			break;
		case 13:
			this.testOpenSkills(playerId);
			break;
		case 14:
			this.testEnterTower(playerId, param1);
			break;
		case 15:	
			this.testAddTiantiScore(playerId, param1);
			break;
		case 16:
			this.testComleteWeekTask(playerId, param1);			
			break;
		case 17:
			this.testPay(playerId, param1);
			break;			
		case 18:
			this.testAddBuff(playerId, param1);			
			break;
		case 19:
			this.testAddGuildMoney(playerId, param1);			
			break;
		case 20:
			this.testAddContribute(playerId, param1);			
			break;
		case 21:
			this.testCreateRobot(playerId, param1);			
			break;
		}
	}

	/**
	 * 添加buff
	 */
	private void testAddBuff(long playerId, int buffId) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		IBuffService buffService = serviceCollection.getBuffService();
		
		buffService.addBuffById(playerId, buffId);
	}

	/**
	 * 添加帮派资金与建设度
	 */
	private void testAddGuildMoney(long playerId, int value) throws Exception{
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		IGuildService guildService = serviceCollection.getGuildService();
		
		PlayerGuild playerGuild = guildService.getPlayerGuild(playerId);
		if(playerGuild == null || playerGuild.getGuildId() < 1){
			throw new GameException(ExceptionConstant.GUILD_3709);		
		} 
		Guild guild = guildService.getGuildById(playerGuild.getGuildId());
		if(guild == null){
			throw new GameException(ExceptionConstant.GUILD_3706);
		}
		
		guild.setBuildNum(guild.getBuildNum() + value);
		guild.setMoney(guild.getMoney() + value);
		guildService.updateGuild(guild);
	}
	
	/**
	 * 添加贡献
	 */
	private void testAddContribute(long playerId, int value) throws Exception{
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		IGuildService guildService = serviceCollection.getGuildService();
		
		PlayerGuild playerGuild = guildService.getPlayerGuild(playerId);
		if(playerGuild == null || playerGuild.getGuildId() < 1){
			throw new GameException(ExceptionConstant.GUILD_3709);		
		} 
		Guild guild = guildService.getGuildById(playerGuild.getGuildId());
		if(guild == null){
			throw new GameException(ExceptionConstant.GUILD_3706);
		}
		playerGuild.setContribution(playerGuild.getContribution() + value);
		guildService.updatePlayerGuild(playerGuild);
	}
	/**
	 * 发送物品
	 */
	private void testGetItem(long playerId, int itemId, int num)throws Exception {
		if(num <= 0) return;
		
		IBagService bagService = serviceCollection.getBagService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		IEquipmentService equipmentService = serviceCollection.getEquipmentService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_BAG)) {
			BaseItem item = bagService.getBaseItemById(itemId);
			
			S_SynBagItem.Builder builder = S_SynBagItem.newBuilder();
			if(item == null){

				Integer itemIndex = bagService.getNewItemIndexByPlayerId(playerId);
				if(itemIndex == null){
					throw new GameException(ExceptionConstant.BAG_1304);
				}
				PlayerEquipment playerEquipment = equipmentService.createPlayerEquipment(playerId, itemId, 0);
				PlayerBag playerBag = bagService.createPlayerBag(playerId, playerEquipment.getPlayerEquipmentId(), ItemConstant.GOODS_TYPE_EQUPMENT, playerEquipment.getIsBinding(), 1, itemIndex);
				builder.addListPlayerBags(protoBuilderService.buildPlayerBagMsg(playerBag));
				builder.addListPlayerEquipments(protoBuilderService.buildPlayerEquipmentMsg(playerEquipment));
			
			}else{
				List<PlayerBag> lists = bagService.addPlayerBag_check(playerId, item, num, 0);
				for(PlayerBag playerBag : lists){
					builder.addListPlayerBags(protoBuilderService.buildPlayerBagMsg(playerBag));
				}
			}

			MessageObj msg = new MessageObj(MessageID.S_SynBagItem_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}
	}

	/**
	 * 升级
	 */
	private void testUpLevel(long playerId, int level) throws Exception {

		if(level < 1 || level > 100) return;
		
		IPlayerService playerService = serviceCollection.getPlayerService();
		ICommonService commonService = serviceCollection.getCommonService();
		
		PlayerPuppet playerPuppet = serviceCollection.getSceneService().getPlayerPuppet(playerId);
		if(playerPuppet == null) return;
		
		Player player = playerService.getPlayerByID(playerId);
		PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
		
		int oldLv = playerProperty.getLevel();
		if(oldLv == level) return;
		
		playerProperty.setExp(0);
		playerProperty.setLevel(level);
		
		BaseProperty baseProperty = commonService.getBaseProperty(player.getCareer(), playerProperty.getLevel());
		BaseProperty lastBaseProperty = commonService.getBaseProperty(player.getCareer(), oldLv);
		
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
		
		playerPuppet.setLevel(playerProperty.getLevel());
		playerPuppet.setHp(playerProperty.getHpMax());
		playerPuppet.setMp(playerProperty.getMpMax());
		
		Map<Integer, Integer> propertyMap = new HashMap<Integer, Integer>();
		propertyMap.put(ProdefineConstant.EXP, playerProperty.getExp());
		propertyMap.put(ProdefineConstant.LEVEL, playerProperty.getLevel());
		propertyMap.put(ProdefineConstant.HP, playerProperty.getHpMax());
		propertyMap.put(ProdefineConstant.MP, playerProperty.getMpMax());
		
		playerService.updatePlayerProperty(playerProperty);
		playerService.synPlayerPropertyToAll(playerPuppet, propertyMap);
		
		List<Integer> conditionList = new ArrayList<Integer>();
		conditionList.add(playerProperty.getLevel());
		serviceCollection.getTaskService().executeTask(playerId, TaskConstant.TYPE_2, conditionList);
		
		for(int i = oldLv; i<=level; i++){
			serviceCollection.getTaskService().touchTaskByLevel(playerId, i);
		}
		
		//队伍同步等级
		PlayerExt playerExt = playerService.getPlayerExtById(playerId);
		serviceCollection.getTeamService().synUpLevel(playerExt);
		
	}

	/**
	 * 增加经验
	 */
	private void testAddExp(long playerId, int addExp) throws Exception {
		if(addExp < 1) return;
		
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		playerService.addPlayerExp(playerId, addExp);
	}

	/**
	 * 切换场景
	 */
	private void testGotoScene(long playerId, int mapId) throws Exception {
		ISceneService sceneService = serviceCollection.getSceneService();
		
		IInstanceService instanceService = serviceCollection.getInstanceService();
		BaseMap baseMap = sceneService.getBaseMap(mapId);
		if(baseMap == null) throw new GameException(ExceptionConstant.ERROR_10000);
		
		if(baseMap.getMapType() == SceneConstant.INSTANCE_SCENE){
							
			// 进入副本消耗
			if(baseMap.getExpendList() != null){
				serviceCollection.getRewardService().expendJudgment(playerId, baseMap.getExpendList(), true, InOutLogConstant.DIAMOND_OF_3);
			}
										
			instanceService.enterInstance(playerId, mapId);

		}else if(baseMap.getMapType() == SceneConstant.GUILD_SCENE){
			
			sceneService.enterScene(playerId, GuildConstant.MAP_GUILD_7001, 0, false, null, 1);
		}else{
			sceneService.enterScene(playerId, mapId, 0, false, null, 0);
		}		
	}

	/**
	 * 完成当前任务
	 */
	private void testComleteTask(long playerId, int taskId) throws Exception {
		ITaskService taskService = serviceCollection.getTaskService();
		Map<Integer, PlayerTask> playerTaskMap = taskService.getPlayerTaskMapByPlayerId(playerId);
		
		if(taskId > 0){
			for (Entry<Integer, PlayerTask> entry : playerTaskMap.entrySet()) {
				PlayerTask model = entry.getValue();
				if(model.getTaskId() == taskId && model.getTaskState() == TaskConstant.TASK_STATE_NO){
					
					model.setTaskState(TaskConstant.TASK_STATE_YES);
					taskService.synPlayerTask(model);
					break;
				}
			}
		}else{
			for (Entry<Integer, PlayerTask> entry : playerTaskMap.entrySet()) {
				PlayerTask model = entry.getValue();
				if(model.getType() == TaskConstant.MAIN_TASK && model.getTaskState() == TaskConstant.TASK_STATE_NO){
					
					model.setTaskState(TaskConstant.TASK_STATE_YES);
					taskService.synPlayerTask(model);
					break;
				}
			}
		}
	}

	/**
	 * 接受任务
	 */
	private void testAcceptTask(long playerId, int taskId) throws Exception {
		if(taskId <= 0) throw new GameException(ExceptionConstant.TASK_2100);
		ITaskService taskService = serviceCollection.getTaskService();

		BaseTask baseTask = taskService.getBaseTask(taskId);
		if(baseTask == null) throw new GameException(ExceptionConstant.TASK_2100);
		
		int curTaskId = 0;

		if(baseTask.getType() == TaskConstant.MAIN_TASK){
			Map<Integer, PlayerTask> playerTaskMap = taskService.getPlayerTaskMapByPlayerId(playerId);
			for(Map.Entry<Integer, PlayerTask> map : playerTaskMap.entrySet()){
				PlayerTask model = map.getValue();
				if(model.getType() == TaskConstant.MAIN_TASK){
					
					curTaskId = map.getKey();
					break;
				}
			}
			
			taskService.deletePlayerTask(playerId, curTaskId);
		}
		try {
			List<Integer> newTaskList = new ArrayList<Integer>();
			newTaskList.add(taskId);
			taskService.acceptTask(playerId, curTaskId, newTaskList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void testAddGold(long playerId, int addGold) throws Exception {
		IPlayerService playerService = serviceCollection.getPlayerService();
		playerService.addGold_syn(playerId, addGold);
	}


	private void testAddDiamond(long playerId, int addDiamond) throws Exception {
		IPlayerService playerService = serviceCollection.getPlayerService();
		playerService.addDiamond_syn(playerId, addDiamond, InOutLogConstant.DIAMOND_OF_0);
	}

	/**
	 * 设置攻击
	 */
	private void testSetAttack(long playerId, int attackValue) throws Exception {
		IPropertyService propertyService = serviceCollection.getPropertyService();
		
		PlayerProperty playerProperty = serviceCollection.getPlayerService().getPlayerPropertyById(playerId);
		
		Map<Integer, Integer> addProMap = new HashMap<Integer, Integer>();
		addProMap.put(ProdefineConstant.P_ATTACK_PANEL, attackValue - playerProperty.getP_attack());
		addProMap.put(ProdefineConstant.M_ATTACK_PANEL, attackValue - playerProperty.getM_attack());
		propertyService.addProValue(playerId, addProMap, true, true);
	}

	/**
	 * 设置移速
	 */
	private void testSetMoveSpeed(long playerId, int moveSpeed) throws Exception {
		IPropertyService propertyService = serviceCollection.getPropertyService();
		
		PlayerProperty playerProperty = serviceCollection.getPlayerService().getPlayerPropertyById(playerId);
		
		Map<Integer, Integer> addProMap = new HashMap<Integer, Integer>();
		addProMap.put(ProdefineConstant.MOVE_SPEED_PANEL, moveSpeed - playerProperty.getMoveSpeed());
		propertyService.addProValue(playerId, addProMap, true, true);
	}

	/**
	 * 设置最大血量
	 */
	private void testSetHpMax(long playerId, int maxHp)throws Exception{
		
		PlayerPuppet playerPuppet = serviceCollection.getSceneService().getPlayerPuppet(playerId);
		if(playerPuppet == null || playerPuppet.getState() != BattleConstant.STATE_NORMAL) throw new GameException(ExceptionConstant.BAG_1307);
		
		Map<Integer, Integer> propertyMap = new HashMap<Integer, Integer>();
	
		playerPuppet.setHpMax(maxHp);
		playerPuppet.setHp(maxHp);
		playerPuppet.setMpMax(maxHp);
		playerPuppet.setMp(maxHp);
		
		propertyMap.put(ProdefineConstant.HP, maxHp);
		propertyMap.put(ProdefineConstant.HP_MAX, maxHp);
		propertyMap.put(ProdefineConstant.MP, maxHp);
		propertyMap.put(ProdefineConstant.MP_MAX, maxHp);
		
		serviceCollection.getPlayerService().synPlayerPropertyToAll(playerPuppet, propertyMap);
	}
	
	/**
	 * 召唤怪物
	 */
	private void testRefreshMonster(long playerId, int refreshId){
 		ISceneService sceneService = serviceCollection.getSceneService();
		IMonsterService monsterService = serviceCollection.getMonsterService();
		
		PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerId);
		if(playerPuppet == null) return;
		
		SceneModel sceneModel = sceneService.getSceneModel(playerPuppet.getSceneGuid());
		
		monsterService.refreshMonsters(sceneModel, refreshId, playerPuppet.getX(), playerPuppet.getY(), playerPuppet.getZ(), 0, true);
	}
	
	/**
	 * 开启所有技能
	 */
	private void testOpenSkills(long playerId)throws Exception{
		serviceCollection.getSkillService().testOpenAllPlayerSkills(playerId);
	}
	
	/**
	 * 进入大荒塔
	 */
	private void testEnterTower(long playerId, int curLayerId)throws Exception{
		if(curLayerId < 1 || curLayerId > 9999){
			return;
		}
		PlayerExt playerExt = serviceCollection.getPlayerService().getPlayerExtById(playerId);
		playerExt.setCurLayerId(curLayerId);
		serviceCollection.getTowerService().enterTower(playerId);
	}
	
	/**
	 * 添加天梯分数  
	 */
	private void testAddTiantiScore(long playerId, int addTiantiScore)throws Exception{ 
		
		ITiantiService tiantiService = serviceCollection.getTiantiService();		
		PlayerTianti atkTianti = tiantiService.getPlayerTianti(playerId);	
		int atkScore = Math.max(atkTianti.getScore() + addTiantiScore, 0);
		
		try {
			// 任务触发					
			List<Integer> conditionList = new ArrayList<Integer>();				
			conditionList.add(addTiantiScore);
			serviceCollection.getTaskService().executeTask(playerId, TaskConstant.TYPE_20, conditionList);
		} catch (Exception e) {
			e.printStackTrace();
		}				
		
		BaseTiantiScore baseAtkScore = tiantiService.getBaseTiantiScore(atkScore);
		atkTianti.setStar(baseAtkScore.getStar());
		atkTianti.setStage(baseAtkScore.getStage());
		atkTianti.setScore(atkScore);
		atkTianti.setUpdateTime(System.currentTimeMillis());
		tiantiService.updatePlayerTianti(atkTianti);
		
		PlayerPuppet playerPuppet = serviceCollection.getSceneService().getPlayerPuppet(playerId);
		playerPuppet.setStage(atkTianti.getStage());
		serviceCollection.getPlayerService().synPlayerPropertyToAll(playerPuppet, ProdefineConstant.STAGE, playerPuppet.getStage());
	}
	
	
	/**
	 * 完成环任务
	 */
	private void testComleteWeekTask(long playerId, int weekTaskNum) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		ITaskService taskService = serviceCollection.getTaskService();
		PlayerExt playerExt = playerService.getPlayerExtById(playerId);
		
		int WEEK_TASK_NUM = serviceCollection.getCommonService().getConfigValue(ConfigConstant.WEEK_TASK_NUM);
		
		if(weekTaskNum >= WEEK_TASK_NUM){
			throw new GameException(ExceptionConstant.TASK_2107);
		}
		
		PlayerTask  playerTask = taskService.getPlayerTaskByType(playerId, TaskConstant.WEEK_TASK);
		if(playerTask == null) throw new GameException(ExceptionConstant.TASK_2100);
		
		playerExt.setWeekTaskNum(weekTaskNum);
		playerService.updatePlayerExt(playerExt);
		playerService.synPlayerProperty(playerId, ProdefineConstant.WEEK_TASK_NUM, playerExt.getWeekTaskNum());
		
		playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
		taskService.submitTask(playerId, playerTask.getTaskId());
	}

	/**
	 * 测试支付
	 */
	private void testPay(long playerId, int payItemId) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPayService payService = serviceCollection.getPayService();
		
		payService.pay(playerId, payItemId);		
	}	
	
	/**
	 * 测试机器人
	 */
	private void testCreateRobot(long playerId, int num) throws Exception{
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IRebotService rebotService = serviceCollection.getRebotService();
		
		rebotService.createRebot(playerId, num) ;
	}
}
