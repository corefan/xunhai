package com.action;

import com.common.GameContext;
import com.common.ServiceCollection;
import com.core.GameMessage;
import com.message.TaskProto.C_AbandonTask;
import com.message.TaskProto.C_AcceptDailyTask;
import com.message.TaskProto.C_CompleteTask;
import com.message.TaskProto.C_RefrshDailyTask;
import com.message.TaskProto.C_SubmitTask;
import com.service.ITaskService;

/**
 * 任务接口
 * @author ken
 * @date 2017-2-20
 */
public class TaskAction {
	
	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	
	/**
	 * 提交任务
	 */
	
	public void submitTask(GameMessage gameMessage) throws Exception {
	
		ITaskService taskService = serviceCollection.getTaskService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_SubmitTask param = C_SubmitTask.parseFrom(gameMessage.getData());
		int taskId = param.getTaskId();
		taskService.submitTask(playerId, taskId);
	}

	/**
	 * 直接完成任务
	 */
	
	public void completeTask(GameMessage gameMessage) throws Exception {
		
		ITaskService taskService = serviceCollection.getTaskService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_CompleteTask param = C_CompleteTask.parseFrom(gameMessage.getData());
		int taskId = param.getTaskId();
		
		taskService.completeTask(playerId, taskId);
	}

	/**
	 * 获取每日任务列表
	 */
	
	public void getDailyTaskList(GameMessage gameMessage) throws Exception {

		ITaskService taskService = serviceCollection.getTaskService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		taskService.getDailyTaskList(playerId);
		
	}

	/**
	 * 手动刷新每日任务列表
	 */
	
	public void refrshDailyTask(GameMessage gameMessage) throws Exception {

		ITaskService taskService = serviceCollection.getTaskService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_RefrshDailyTask param = C_RefrshDailyTask.parseFrom(gameMessage.getData());
		
		int type = param.getType();
		
		taskService.refreshDailyTask(playerId, type);
	}

	/**
	 * 接受每日任务
	 */
	
	public void acceptDailyTask(GameMessage gameMessage) throws Exception {

		ITaskService taskService = serviceCollection.getTaskService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_AcceptDailyTask param = C_AcceptDailyTask.parseFrom(gameMessage.getData());
		int taskId = param.getTaskId();
		taskService.acceptDailyTask(playerId, taskId);
	}

	/**
	 * 放弃任务
	 */
	
	public void abandonTask(GameMessage gameMessage) throws Exception {
		ITaskService taskService = serviceCollection.getTaskService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_AbandonTask param = C_AbandonTask.parseFrom(gameMessage.getData());
		int taskId = param.getTaskId();
		
		taskService.abandonTask(playerId, taskId);
	}

	/**
	 * 接受环任务
	 */
	
	public void acceptWeekTask(GameMessage gameMessage) throws Exception {
		
		ITaskService taskService = serviceCollection.getTaskService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		taskService.acceptWeekTask(playerId);
	}

}
