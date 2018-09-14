/**
 * 
 */
package com.taskpool;

import java.util.List;

import com.common.GameContext;
import com.common.ServiceCollection;
import com.constant.TaskConstant;
import com.domain.player.PlayerExt;
import com.domain.task.BaseTask;
import com.domain.task.PlayerTask;
import com.service.IPlayerService;

/**
 * 通关到大荒塔n层
 * @author jiangqin
 * @date 2017-8-18
 */
public class Type26  extends AbstractTask{

	private static final long serialVersionUID = 2800416416020935073L;

	@Override
	public void acceptTask(BaseTask baseTask, PlayerTask playerTask) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		PlayerExt playerExt = playerService.getPlayerExtById(playerTask.getPlayerId());
		if (baseTask.getId() == playerTask.getTaskId()){
			if((playerExt.getCurLayerId() - 1) >= baseTask.getConditionList().get(0)){
				playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
			}
		}
	}

	@Override
	public boolean executeTask(BaseTask baseTask, PlayerTask playerTask, List<Integer> conditionList) {
		if (baseTask.getId() == playerTask.getTaskId()){
			if((conditionList.get(0) - 1) >= baseTask.getConditionList().get(0)){
				playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
			}
		}
		return true;
	}

}
