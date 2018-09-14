package com.taskpool;

import java.util.List;

import com.common.GameContext;
import com.constant.TaskConstant;
import com.domain.player.PlayerProperty;
import com.domain.task.BaseTask;
import com.domain.task.PlayerTask;
import com.service.IPlayerService;

/**
 * 玩家升级
 * @author ken
 * @date 2017-2-21
 */
public class Type2 extends AbstractTask {

	private static final long serialVersionUID = -6137146531197423207L;

	
	@Override
	public void acceptTask(BaseTask baseTask, PlayerTask playerTask) {
		if (baseTask.getId() == playerTask.getTaskId()){
			IPlayerService playerService = GameContext.getInstance().getServiceCollection().getPlayerService();
			PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerTask.getPlayerId());
			playerTask.setCurrentNum(playerProperty.getLevel());
			if(playerTask.getCurrentNum() >= baseTask.getConditionList().get(0)){
				playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
			}
		}
	}

	@Override
	public boolean executeTask(BaseTask baseTask, PlayerTask playerTask, List<Integer> conditionList) {
		playerTask.setCurrentNum(conditionList.get(0));
		if(playerTask.getCurrentNum() >= baseTask.getConditionList().get(0)){
			playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
		}
		return true;
	}

}
