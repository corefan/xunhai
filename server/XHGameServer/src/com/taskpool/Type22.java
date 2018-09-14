package com.taskpool;

import java.util.List;

import com.common.GameContext;
import com.constant.TaskConstant;
import com.domain.player.PlayerExt;
import com.domain.task.BaseTask;
import com.domain.task.PlayerTask;
import com.service.IPlayerService;

/**
 *  天梯引导
 * @author jiangqin
 * @date 2017-4-27
 */

public class Type22 extends AbstractTask {

	private static final long serialVersionUID = -6137146531197423207L;

	
	@Override
	public void acceptTask(BaseTask baseTask, PlayerTask playerTask) {
		if (baseTask.getId() == playerTask.getTaskId()){
			IPlayerService playerService = GameContext.getInstance().getServiceCollection().getPlayerService();
			PlayerExt playerExt = playerService.getPlayerExtById(playerTask.getPlayerId());
			if(playerExt == null) return;		
			if(playerExt.getCurLayerId() >= baseTask.getConditionList().get(0)){
				playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
			}
		}
	}

	@Override
	public boolean executeTask(BaseTask baseTask, PlayerTask playerTask, List<Integer> conditionList) {
		if(conditionList.get(0) >= baseTask.getConditionList().get(0)){
			playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
		}
		return true;
	}

}
