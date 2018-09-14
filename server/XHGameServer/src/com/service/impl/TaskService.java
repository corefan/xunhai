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
import com.common.LockService;
import com.common.RandomService;
import com.common.ServiceCollection;
import com.constant.CacheConstant;
import com.constant.CacheSynConstant;
import com.constant.ConfigConstant;
import com.constant.ExceptionConstant;
import com.constant.InOutLogConstant;
import com.constant.ItemConstant;
import com.constant.LockConstant;
import com.constant.ProdefineConstant;
import com.constant.RewardTypeConstant;
import com.constant.TaskConstant;
import com.constant.VipConstant;
import com.dao.task.BaseTaskDAO;
import com.dao.task.PlayerTaskDAO;
import com.domain.GameEntity;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.Reward;
import com.domain.bag.BaseEquipment;
import com.domain.battle.BaseTaskItem;
import com.domain.player.Player;
import com.domain.player.PlayerDaily;
import com.domain.player.PlayerExt;
import com.domain.player.PlayerProperty;
import com.domain.player.PlayerWealth;
import com.domain.task.BaseActTask;
import com.domain.task.BaseTask;
import com.domain.task.BaseWeekTaskReward;
import com.domain.task.PlayerTask;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.TaskProto.S_AbandonTask;
import com.message.TaskProto.S_SubmitTask;
import com.message.TaskProto.S_SynDailyTaskList;
import com.message.TaskProto.S_SynTaskTrack;
import com.service.ICommonService;
import com.service.ILogService;
import com.service.IPlayerService;
import com.service.IProtoBuilderService;
import com.service.IRewardService;
import com.service.ITaskService;
import com.service.IVipService;
import com.taskpool.AbstractTask;
import com.taskpool.TaskPool;
import com.util.IDUtil;
import com.util.LogUtil;
import com.util.SplitStringUtil;

/**
 * 任务系统
 * @author ken
 * @date 2017-2-20
 */
public class TaskService implements ITaskService {

	private BaseTaskDAO baseTaskDAO = new BaseTaskDAO();
	private PlayerTaskDAO playerTaskDAO = new PlayerTaskDAO();
	
	@Override
	public void initBaseCache() {

		Map<Integer, BaseTask> taskMap = new HashMap<Integer, BaseTask>();
		List<BaseTask> lists = baseTaskDAO.listBaseTasks();
		for(BaseTask model : lists){
			model.setNextTaskList(SplitStringUtil.getIntList(model.getNextTask()));
			model.setConditionList(SplitStringUtil.getIntList(model.getCondition()));
			model.setRewardList(SplitStringUtil.getRewardInfo(model.getReward()));
			taskMap.put(model.getId(), model);
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_TASK, taskMap);
		
		
		Map<Integer, BaseTaskItem> taskItemMap = new HashMap<Integer, BaseTaskItem>();
		List<BaseTaskItem> listBaseTaskItem = baseTaskDAO.listBaseTaskItem();
		for(BaseTaskItem baseTaskItem : listBaseTaskItem){
			baseTaskItem.setWeight(baseTaskItem.getWeight());
			baseTaskItem.setWeightList(SplitStringUtil.getIntIntList(baseTaskItem.getWeight()));
			taskItemMap.put(baseTaskItem.getItemId(), baseTaskItem);
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_TASK_ITEM, taskItemMap);
		
		Map<Integer, List<BaseActTask>> actTaskMap = new HashMap<Integer, List<BaseActTask>>();
		List<BaseActTask> listActTasks = baseTaskDAO.listBaseActTasks();
		for(BaseActTask model : listActTasks){
			model.setLevelLimitList(SplitStringUtil.getIntList(model.getLevelLimit()));
			model.setTaskIdList(SplitStringUtil.getIntIntList(model.getTaskIds()));
			
			int allRate = 0;
			for(List<Integer> l : model.getTaskIdList()){
				allRate += l.get(1);
			}
			model.setAllRate(allRate);
			
			List<BaseActTask> tasks = actTaskMap.get(model.getTaskType());
			if(tasks == null){
				tasks = new ArrayList<BaseActTask>();
				actTaskMap.put(model.getTaskType(), tasks);
			}
			tasks.add(model);
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_TASK_ACT, actTaskMap);
		
		Map<Integer, BaseWeekTaskReward> weekRewardMap = new HashMap<Integer, BaseWeekTaskReward>();
		List<BaseWeekTaskReward> rewards = baseTaskDAO.listBaseWeekTaskRewards();
		for(BaseWeekTaskReward model : rewards){
			weekRewardMap.put(model.getWeekTaskNum(), model);
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_WEEK_TASK_REWARD, weekRewardMap);
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public BaseTask getBaseTask(int taskId){
		Map<Integer, BaseTask> taskMap = (Map<Integer, BaseTask>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_TASK);
		return taskMap.get(taskId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseTaskItem getBaseTaskItem(int itemId){
		Map<Integer, BaseTaskItem> taskItemMap =(Map<Integer, BaseTaskItem>) BaseCacheService.getFromBaseCache(CacheConstant.BASE_TASK_ITEM);
		return taskItemMap.get(itemId);
	}
	
	/**
	 * 环任务特殊奖励配置
	 */
	@SuppressWarnings("unchecked")
	private BaseWeekTaskReward getBaseWeekTaskReward(int weekTaskNum){
		Map<Integer, BaseWeekTaskReward> weekRewardMap = (Map<Integer, BaseWeekTaskReward>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_WEEK_TASK_REWARD);
		return weekRewardMap.get(weekTaskNum);
	}
	
	/**
	 * 获取活动任务配置
	 */
	@SuppressWarnings("unchecked")
	private BaseActTask getBaseActTask(int taskType, int playerLevel){
		Map<Integer, List<BaseActTask>> actTaskMap = (Map<Integer, List<BaseActTask>>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_TASK_ACT);
		List<BaseActTask> lists = actTaskMap.get(taskType);
		for(BaseActTask model : lists){
			if(playerLevel >= model.getLevelLimitList().get(0) && playerLevel <= model.getLevelLimitList().get(1)){
				return model;
			}
		}
		return null;
	}
	
	@Override
	public void deleteCache(long playerId) {
		CacheService.deleteFromCache(CacheConstant.PLAYER_TASK+playerId);
		CacheService.deleteFromCache(CacheConstant.PLAYER_DAILY_TASK+playerId);
	}

	@Override
	public void quartzDeletePlayerTask() {
		playerTaskDAO.quartzDeletePlayerTask();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Integer, PlayerTask> getPlayerTaskMapByPlayerId(long playerId){
		Map<Integer, PlayerTask> playerTaskMap = (Map<Integer, PlayerTask>) CacheService.getFromCache(CacheConstant.PLAYER_TASK+playerId);
		if(playerTaskMap == null){
			playerTaskMap = new ConcurrentHashMap<Integer, PlayerTask>();
			List<PlayerTask> playerTaskList = playerTaskDAO.listPlayerTasks(playerId);
			for (PlayerTask pt : playerTaskList) {
				playerTaskMap.put(pt.getTaskId(), pt);
			}
			CacheService.putToCache(CacheConstant.PLAYER_TASK+playerId, playerTaskMap);
		}

		return playerTaskMap;
	}
	
	/**
	 * 取任务数据
	 */
	private PlayerTask getPlayerTask(long playerId, int taskId){
		Map<Integer, PlayerTask> playerTaskMap = this.getPlayerTaskMapByPlayerId(playerId);

		return playerTaskMap.get(taskId);
	}
	
	/**
	 * 更新缓存
	 */
	private void updatePlayerTask(PlayerTask playerTask){
		Set<GameEntity> lists = CacheSynDBService.getFromFiveUpdateThreeCache(CacheSynConstant.PLAYER_TASK);
		if (!lists.contains(playerTask)) {
			lists.add(playerTask);
		}
	}

	@Override
	public void submitTask(long playerId, int taskId) throws Exception {		
		if(playerId < 1 || taskId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		ICommonService commonService = serviceCollection.getCommonService();
		IRewardService rewardService = serviceCollection.getRewardService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IVipService vipService = serviceCollection.getVipService();		
		
		BaseTask baseTask = this.getBaseTask(taskId);
		if(baseTask == null) {
			LogUtil.error("submitTask baseTask is null with id ="+taskId);
			throw new GameException(ExceptionConstant.TASK_2100);
		}
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_TASK)) {
			
			PlayerTask playerTask = this.getPlayerTask(playerId, taskId);
			if(playerTask == null) {
				return;
			}
			
			if(playerTask.getTaskState() != TaskConstant.TASK_STATE_YES){
				throw new GameException(ExceptionConstant.TASK_2101);
			}
			PlayerExt playerExt = serviceCollection.getPlayerService().getPlayerExtById(playerId);
			
			boolean completeFlag = false;
			//获取任务奖励
			if(baseTask.getRewardList() != null && !baseTask.getRewardList().isEmpty()){
				List<Reward> rewards = new ArrayList<Reward>();
				for(Reward reward : baseTask.getRewardList()){
					if(reward.getType() == RewardTypeConstant.EQUIPMENT){
						BaseEquipment baseEquipment = serviceCollection.getEquipmentService().getBaseEquipmentById(reward.getId());
						Player player = serviceCollection.getPlayerService().getPlayerByID(playerId);
						if (baseEquipment.getNeedJob() > 0 && baseEquipment.getNeedJob() != player.getCareer()){
							continue;
						}	
					}
					
					//环任务特殊奖励
					if(baseTask.getType() == TaskConstant.WEEK_TASK){
						if(reward.getType() == RewardTypeConstant.MONEY || reward.getType() == RewardTypeConstant.EXPERIENCE){
							int num = (int)(reward.getNum() * this.calWeekReward(playerExt.getWeekTaskNum()));
							Reward newReward = new Reward(reward.getType(), reward.getId(), num, reward.getRate(), reward.getBlind());
							rewards.add(newReward);
							continue;
						}
					}
					rewards.add(reward);
				}
				//环任务特殊奖励
				if(baseTask.getType() == TaskConstant.WEEK_TASK){
					BaseWeekTaskReward baseWeekTaskReward = this.getBaseWeekTaskReward(playerExt.getWeekTaskNum());
					if(baseWeekTaskReward != null && baseWeekTaskReward.getItemId() > 0){
						
						Reward newReward = new Reward(RewardTypeConstant.ITEM, baseWeekTaskReward.getItemId(), 1, 0, ItemConstant.ITEM_IS_BINDING);
						rewards.add(newReward);
					}
				}
				rewardService.fetchRewardList(playerId, rewards);
				completeFlag = true;
			}
			
			//删除玩家当前任务
			this.deletePlayerTask(playerId, taskId);
			
			//开放菜单权限 TODO 
			List<Integer> nextTaskList = baseTask.getNextTaskList();
			if(completeFlag){
				if(baseTask.getType() == TaskConstant.DAILY_TASK){
					// 触发悬赏任务
					this.executeTask(playerId, TaskConstant.TYPE_24, null);
				
					int DAILY_TASK_NUM = commonService.getConfigValue(ConfigConstant.DAILY_TASK_NUM) + vipService.getVipPrivilegeValue(playerId, VipConstant.VIP_PRIVILEGE_16);
					
					PlayerDaily playerDaily = playerService.getPlayerDailyById(playerId);
					if(playerDaily.getDailyTaskNum() < DAILY_TASK_NUM) {
						
						//如果每日任务 则重新刷新每日任务列表					
						PlayerProperty playerProperty = serviceCollection.getPlayerService().getPlayerPropertyById(playerId);
						this.initDailyTasks(playerId, playerProperty.getLevel());
					}else{						
						CacheService.deleteFromCache(CacheConstant.PLAYER_DAILY_TASK + playerId);
					}					
					
				}else if(baseTask.getType() == TaskConstant.WEEK_TASK){
					// 环任务次数
					this.executeTask(playerId, TaskConstant.TYPE_25, null);
					
					// 环任务次数设置
					int WEEK_TASK_NUM = serviceCollection.getCommonService().getConfigValue(ConfigConstant.WEEK_TASK_NUM);
					if(playerExt.getWeekTaskNum() < WEEK_TASK_NUM){
						//如果每日任务 则自动接取环任务					
						nextTaskList = this.autoAcceptWeekTask(playerId, taskId);
					}						
				}else if(baseTask.getType() == TaskConstant.HUNT_TASK){
					// 猎妖任务
					List<Integer> conditionList = new ArrayList<Integer>();
					conditionList.add(1);								
					serviceCollection.getTaskService().executeTask(playerId, TaskConstant.TYPE_27, conditionList);
				}
			}
			
			//触发新任务
			this.acceptTask(playerId, taskId, nextTaskList);
			
			//更新游戏节点
			if(baseTask.getType() == TaskConstant.MAIN_TASK && completeFlag){
				try {
					ILogService logService = serviceCollection.getLogService();
					logService.createGameStepLog(playerId, taskId);
				} catch (Exception e) {
					LogUtil.error("新人节点日志异常：",e);
				}
			}
		}
	}

	/**
	 * 触发新任务
	 */
	public void acceptTask(long playerId, int curTaskId, List<Integer> newTaskList){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		
		S_SubmitTask.Builder builder = S_SubmitTask.newBuilder();
		builder.setTaskId(curTaskId);
		
		//触发新任务
		if(newTaskList != null && !newTaskList.isEmpty()){
			List<PlayerTask> lists = null;
			try {
				lists = this.createPlayerTask(playerId, newTaskList);
				if(!lists.isEmpty()){
					IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
					for(PlayerTask model : lists){
						builder.addPlayerTask(protoBuilderService.buildPlayerTaskMsg(model));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		MessageObj msg = new MessageObj(MessageID.S_SubmitTask_VALUE, builder.build().toByteArray());
		serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);
	}
	
	/**
	 * 删除任务
	 */
	public void deletePlayerTask(long playerId, int taskId){
		
		Map<Integer, PlayerTask> map = this.getPlayerTaskMapByPlayerId(playerId);
		
		PlayerTask model = map.get(taskId);
		if(model == null){
			System.out.println("deletePlayerTask  task is null with id is "+taskId);
			return;
		}
		model.setDeleteFlag(1);
		map.remove(taskId);
		
		this.updatePlayerTask(model);
	}
	
	
	/**
	 * 创建玩家任务
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public List<PlayerTask> createPlayerTask(long playerId, List<Integer> newTaskIds){
		List<PlayerTask> lists = new ArrayList<PlayerTask>();
		for(Integer taskId : newTaskIds){
			BaseTask baseTask = this.getBaseTask(taskId);
			if(baseTask == null){
				System.out.println("task is null with id is "+taskId);
				return lists;
			}
			PlayerTask playerTask = new PlayerTask();
			playerTask.setId(IDUtil.geneteId(PlayerTask.class));
			playerTask.setPlayerId(playerId);
			playerTask.setTaskId(taskId);
			playerTask.setType(baseTask.getType());
			playerTask.setConditionType(baseTask.getConditionType());
			
			try {
				Class<? extends AbstractTask> clz = TaskPool.getTask(baseTask.getConditionType());
				AbstractTask task = clz.newInstance();
				task.acceptTask(baseTask, playerTask);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
			playerTaskDAO.createPlayerTask(playerTask);
			this.getPlayerTaskMapByPlayerId(playerId).put(taskId, playerTask);
			
			lists.add(playerTask);
		}
		return lists;
	}

	@Override
	public void completeTask(long playerId, int taskId) throws Exception {
		PlayerTask playerTask = this.getPlayerTask(playerId, taskId);
		if(playerTask == null) {
			LogUtil.error("completeTask playerTask is null with id ="+taskId);
			throw new GameException(ExceptionConstant.TASK_2100);
		}
		
		if(playerTask.getTaskState() != TaskConstant.TASK_STATE_NO){
			throw new GameException(ExceptionConstant.TASK_2102);
		}
		
//		if(playerTask.getConditionType() != TaskConstant.TYPE_1 &&
//				playerTask.getConditionType() != TaskConstant.TYPE_23){
//			throw new GameException(ExceptionConstant.TASK_2100);
//		}
		
		playerTask.setTaskState(TaskConstant.TASK_STATE_YES);

		this.synPlayerTask(playerTask);
		
	}
	
	/**
	 * 同步玩家任务
	 */
	public void synPlayerTask(PlayerTask playerTask){
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		
		this.updatePlayerTask(playerTask);
		
		S_SynTaskTrack.Builder builder = S_SynTaskTrack.newBuilder();
		builder.setPlayerTask(protoBuilderService.buildPlayerTaskMsg(playerTask));
		
		MessageObj msg = new MessageObj(MessageID.S_SynTaskTrack_VALUE, builder.build().toByteArray());
		serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerTask.getPlayerId(), msg);
	}

	@Override
	public void executeTask(long playerId, int conditionType, List<Integer> conditionList) throws Exception{
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.EXECUTE_TASK)) {
			Map<Integer, PlayerTask> playerTaskMap = this.getPlayerTaskMapByPlayerId(playerId);
			for(Map.Entry<Integer, PlayerTask> entry : playerTaskMap.entrySet()){
				PlayerTask playerTask = entry.getValue();
				if(playerTask.getConditionType() != conditionType){
					continue;
				}
				if(playerTask.getTaskState() == TaskConstant.TASK_STATE_YES){
					continue;
				}
				BaseTask baseTask = this.getBaseTask(playerTask.getTaskId());
				if(baseTask == null){
					System.out.println("executeTask task is null with id is "+playerTask.getTaskId());
					return;
				}
				Class<? extends AbstractTask> clz = TaskPool.getTask(conditionType);
				AbstractTask task = clz.newInstance();
				boolean bChange = task.executeTask(baseTask, playerTask, conditionList);
				
				if(bChange){
					this.synPlayerTask(playerTask);	
				}				
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void touchTaskByLevel(long playerId, int level) {
		Map<Integer, BaseTask> taskMap = (Map<Integer, BaseTask>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_TASK);
		for(Map.Entry<Integer, BaseTask> entry : taskMap.entrySet()){
			BaseTask baseTask = entry.getValue();
			if(baseTask.getTouchLevel() == level){
				int curTaskId = 0;

				if(baseTask.getType() == TaskConstant.MAIN_TASK){
					Map<Integer, PlayerTask> playerTaskMap = this.getPlayerTaskMapByPlayerId(playerId);
					for(Map.Entry<Integer, PlayerTask> map : playerTaskMap.entrySet()){
						PlayerTask model = map.getValue();
						if(model.getType() == TaskConstant.MAIN_TASK){
							
							curTaskId = map.getKey();
							break;
						}
					}
					
					this.deletePlayerTask(playerId, curTaskId);
				}
				
				try {
					List<Integer> tasks = new ArrayList<Integer>();
					tasks.add(entry.getKey());
					this.acceptTask(playerId, curTaskId, tasks);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public PlayerTask getPlayerTaskByType(long playerId, int taskType) {
		Map<Integer, PlayerTask> playerTaskMap = this.getPlayerTaskMapByPlayerId(playerId);
		for(Map.Entry<Integer, PlayerTask> map : playerTaskMap.entrySet()){
			PlayerTask model = map.getValue();
			if(model.getType() == taskType){
				
				return model;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void getDailyTaskList(long playerId) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IVipService vipService = serviceCollection.getVipService();
		
		PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
		if(playerProperty == null) return;
		
		PlayerDaily playerDaily = playerService.getPlayerDailyById(playerId);
		List<Integer> lists = (List<Integer>)CacheService.getFromCache(CacheConstant.PLAYER_DAILY_TASK + playerId);
		if(lists == null){
			lists = this.initDailyTasks(playerId, playerProperty.getLevel());
		}
		
		int value = vipService.getVipPrivilegeValue(playerId, VipConstant.VIP_PRIVILEGE_3);
		int DAILY_TASK_REF_NUM = serviceCollection.getCommonService().getConfigValue(ConfigConstant.DAILY_TASK_REF_NUM) + value;
		
		S_SynDailyTaskList.Builder builder = S_SynDailyTaskList.newBuilder();
		builder.setDailyTaskNum(playerDaily.getDailyTaskNum());
		builder.setDailyRefNum(DAILY_TASK_REF_NUM - playerDaily.getDailyRefNum());
		for(Integer taskId : lists){
			builder.addTaskIds(taskId);
		}
		MessageObj msg = new MessageObj(MessageID.S_SynDailyTaskList_VALUE, builder.build().toByteArray());
		serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);
	}
	
	/**
	 * 刷新每日任务列表
	 */
	private List<Integer> initDailyTasks(long playerId, int level){
		List<Integer> lists = new ArrayList<Integer>();
		BaseActTask baseActTask = this.getBaseActTask(1, level);
		
		
		for(int index = 1; index <= 4; index++){
			int taskId = this.getRandomTaskId(baseActTask, 0);
			if(taskId == 0) continue;
			
			lists.add(taskId);
		}
		
		CacheService.putToCache(CacheConstant.PLAYER_DAILY_TASK + playerId, lists);
		
		return lists;
	}
	
	/**
	 * 随机出一条每日任务
	 */
	private int getRandomTaskId(BaseActTask baseActTask, int lastTaskId){
		double rate = RandomService.getRandomNum(baseActTask.getAllRate());
		
		int m = 0;
		
		for(List<Integer> l : baseActTask.getTaskIdList()){
			int taskId = l.get(0);
			if(taskId != lastTaskId && rate< m + l.get(1)){
				return  l.get(0);
			}
			 m += l.get(1);  
		}
	
		int index = RandomService.getRandomNum(baseActTask.getTaskIdList().size() - 2);
		
		return baseActTask.getTaskIdList().get(index).get(0);
	}

	@Override
	public void acceptDailyTask(long playerId, int taskId) throws Exception {
		if(playerId < 1 || taskId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_DAILY_TASK)) {
			
			PlayerTask  playerTask = this.getPlayerTaskByType(playerId, TaskConstant.DAILY_TASK);
			if(playerTask != null) throw new GameException(ExceptionConstant.TASK_2109);
			
			IVipService vipService = serviceCollection.getVipService();
			int DAILY_TASK_NUM = serviceCollection.getCommonService().getConfigValue(ConfigConstant.DAILY_TASK_NUM) + vipService.getVipPrivilegeValue(playerId, VipConstant.VIP_PRIVILEGE_16);
			
			PlayerDaily playerDaily = playerService.getPlayerDailyById(playerId);
			if(playerDaily.getDailyTaskNum() >= DAILY_TASK_NUM) {
				throw new GameException(ExceptionConstant.TASK_2103);
			}
			
			playerDaily.setDailyTaskNum(playerDaily.getDailyTaskNum() + 1);
			playerService.updatePlayerDaily(playerDaily);
			
			playerService.synPlayerProperty(playerId, ProdefineConstant.DAILY_TASK_NUM, playerDaily.getDailyTaskNum());
			
			List<Integer> newTaskList = new ArrayList<Integer>();
			newTaskList.add(taskId);
			this.acceptTask(playerId, 0, newTaskList);			
		}
	}

	@Override
	public void refreshDailyTask(long playerId, int type) throws Exception {
		if(playerId < 1 || type < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IVipService vipService = serviceCollection.getVipService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_DAILY_TASK)) {
			
			// vip特权增加每日任务刷新次数
			PlayerDaily playerDaily = playerService.getPlayerDailyById(playerId);
			
			int value = vipService.getVipPrivilegeValue(playerId, VipConstant.VIP_PRIVILEGE_3);
			int DAILY_TASK_REF_NUM = serviceCollection.getCommonService().getConfigValue(ConfigConstant.DAILY_TASK_REF_NUM) + value;
			
			if (type == 1){				
				if(playerDaily.getDailyRefNum() >= DAILY_TASK_REF_NUM) throw new GameException(ExceptionConstant.TASK_2104);
				playerDaily.setDailyRefNum(playerDaily.getDailyRefNum() + 1);
				playerService.updatePlayerDaily(playerDaily);
			}else if (type == 2){
				PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);
				int REFRESH_DAILY_TASK_DIAMOND = serviceCollection.getCommonService().getConfigValue(ConfigConstant.REFRESH_DAILY_TASK_DIAMOND);
				if(playerWealth.getDiamond() < REFRESH_DAILY_TASK_DIAMOND) throw new GameException(ExceptionConstant.PLAYER_1113);
				playerService.addDiamond_syn(playerId, -REFRESH_DAILY_TASK_DIAMOND, InOutLogConstant.DIAMOND_OF_9);
			}
						
			PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
			List<Integer> lists = this.initDailyTasks(playerId, playerProperty.getLevel());
			
			S_SynDailyTaskList.Builder builder = S_SynDailyTaskList.newBuilder();
			builder.setDailyTaskNum(playerDaily.getDailyTaskNum());
			builder.setDailyRefNum(DAILY_TASK_REF_NUM - playerDaily.getDailyRefNum());
			for(Integer taskId : lists){
				builder.addTaskIds(taskId);
			}
			MessageObj msg = new MessageObj(MessageID.S_SynDailyTaskList_VALUE, builder.build().toByteArray());
			serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);
		}
	}

	@Override
	public void abandonTask(long playerId, int taskId) throws Exception {
		if(playerId < 1 || taskId < 1) throw new GameException(ExceptionConstant.ERROR_10000);		
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		PlayerTask playerTask = this.getPlayerTask(playerId, taskId);
		if(playerTask == null) {
			LogUtil.error("abandonTask playerTask is null with id ="+taskId);
			throw new GameException(ExceptionConstant.TASK_2100);
		}
		
		this.deletePlayerTask(playerId, taskId);
		
		S_AbandonTask.Builder builder = S_AbandonTask.newBuilder();
		builder.setTaskId(taskId);
		MessageObj msg = new MessageObj(MessageID.S_AbandonTask_VALUE, builder.build().toByteArray());
		serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);
		
		
		if(playerTask.getType() == TaskConstant.DAILY_TASK){
			//如果每日任务 则重新刷新每日任务列表
			
			PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
			this.initDailyTasks(playerId, playerProperty.getLevel());
		}else if(playerTask.getType() == TaskConstant.WEEK_TASK){
			int WEEK_TASK__NUM = serviceCollection.getCommonService().getConfigValue(ConfigConstant.WEEK_TASK_NUM);
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			if(playerExt.getWeekTaskNum() < WEEK_TASK__NUM){
				playerExt.setWeekTaskNum(0);
				playerService.updatePlayerExt(playerExt);
				
				playerService.synPlayerProperty(playerId, ProdefineConstant.WEEK_TASK_NUM, playerExt.getWeekTaskNum());
			}			
		}
	}

	@Override
	public void acceptHuntTask(long playerId, int itemType) throws Exception {
		if(playerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IVipService vipService = serviceCollection.getVipService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_HUNT_TASK)) {
			int value = vipService.getVipPrivilegeValue(playerId, VipConstant.VIP_PRIVILEGE_15);
			int HUNT_TASK_NUM = serviceCollection.getCommonService().getConfigValue(ConfigConstant.HUNT_TASK_NUM) + value;
			
			PlayerDaily playerDaily = playerService.getPlayerDailyById(playerId);
			if(playerDaily.getHuntTaskNum() >= HUNT_TASK_NUM) {
				throw new GameException(ExceptionConstant.TASK_2105);
			}
			
			PlayerTask  playerTask = this.getPlayerTaskByType(playerId, TaskConstant.HUNT_TASK);
			if(playerTask != null) throw new GameException(ExceptionConstant.TASK_2106);
			
			playerDaily.setHuntTaskNum(playerDaily.getHuntTaskNum() + 1);
			playerService.updatePlayerDaily(playerDaily);
		
			PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
			BaseActTask baseActTask = this.getBaseActTask(3, playerProperty.getLevel());
			
			int taskId = 0;
			
			for(List<Integer> l : baseActTask.getTaskIdList()){
				if(l.get(0).equals(itemType)){
					taskId = l.get(1);
					break;
				}
			}
				
			List<Integer> newTaskList = new ArrayList<Integer>();
			newTaskList.add(taskId);
			this.acceptTask(playerId, 0, newTaskList);
		}
	}

	@Override
	public void acceptWeekTask(long playerId) throws Exception {
		if(playerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_WEEK_TASK)) {
			
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
	
			int WEEK_TOTAL_NUM = serviceCollection.getCommonService().getConfigValue(ConfigConstant.WEEK_TOTAL_NUM);
			if(playerExt.getWeekTotalNum() >= WEEK_TOTAL_NUM){
				throw new GameException(ExceptionConstant.TASK_2107);
			}
			
			PlayerTask  playerTask = this.getPlayerTaskByType(playerId, TaskConstant.WEEK_TASK);
			if(playerTask != null) throw new GameException(ExceptionConstant.TASK_2108);
			
			playerExt.setWeekTaskNum(1);
			playerExt.setWeekTotalNum(playerExt.getWeekTotalNum() + 1);
			playerService.updatePlayerExt(playerExt);
			playerService.synPlayerProperty(playerId, ProdefineConstant.WEEK_TASK_NUM, playerExt.getWeekTaskNum());
			
			PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
			BaseActTask baseActTask = this.getBaseActTask(2, playerProperty.getLevel());
			
			int taskId = this.getRandomTaskId(baseActTask, 0);
			List<Integer> newTaskList = new ArrayList<Integer>();
			newTaskList.add(taskId);
			this.acceptTask(playerId, 0, newTaskList);
		}
	}
	
	/**
	 * 自动接受环任务
	 */
	private List<Integer> autoAcceptWeekTask(long playerId, int lastTaskId){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_WEEK_TASK)) {
		
			int WEEK_TASK_NUM = serviceCollection.getCommonService().getConfigValue(ConfigConstant.WEEK_TASK_NUM);			
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);			
			if(playerExt.getWeekTaskNum() >= WEEK_TASK_NUM){
				return null;
			}
		
			playerExt.setWeekTaskNum(playerExt.getWeekTaskNum() + 1);			
			playerService.updatePlayerExt(playerExt);
			playerService.synPlayerProperty(playerId, ProdefineConstant.WEEK_TASK_NUM, playerExt.getWeekTaskNum());
			
			PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
			BaseActTask baseActTask = this.getBaseActTask(2, playerProperty.getLevel());
			
			int taskId = this.getRandomTaskId(baseActTask, lastTaskId);
			List<Integer> newTaskList = new ArrayList<Integer>();
			newTaskList.add(taskId);
			
			return newTaskList;
			
		}
		
	}
	
	/**
	 * 环任务金币与经验奖励系数
	 */
	private double calWeekReward(int weekTaskNum){
		double v = weekTaskNum % 10;
		if(v == 0){
			v = 10;
		}
		return (1 + v * 0.1) * (1 + (Math.ceil(weekTaskNum * 0.1) - 1) * 0.2);
	}

}
