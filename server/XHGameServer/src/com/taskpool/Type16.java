package com.taskpool;

import java.util.List;

import com.constant.TaskConstant;
import com.domain.task.BaseTask;
import com.domain.task.PlayerTask;

/**
 *  去商城购买N次物品
 * @author jiangqin
 * @date 2017-3-28
 */
public class Type16 extends AbstractTask {

	private static final long serialVersionUID = -6137146531197423207L;

	
	@Override
	public void acceptTask(BaseTask baseTask, PlayerTask playerTask) {	
		if (baseTask.getId() == playerTask.getTaskId()){
			if(playerTask.getCurrentNum() >= baseTask.getConditionList().get(1)) {	
				playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
			}
		}		
	}

	@Override
	public boolean executeTask(BaseTask baseTask, PlayerTask playerTask, List<Integer> conditionList) {
		if (baseTask.getConditionList().get(0).equals(conditionList.get(0))){
			playerTask.setCurrentNum(playerTask.getCurrentNum() + conditionList.get(1));
			if(playerTask.getCurrentNum() >= baseTask.getConditionList().get(1)){
				playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
			}
		}		
		return true;
	}

}
