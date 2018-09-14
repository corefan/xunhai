package com.taskpool;

import java.util.List;

import com.constant.TaskConstant;
import com.domain.task.BaseTask;
import com.domain.task.PlayerTask;

/**
 * 进行N次合成
 * @author jiangqin
 * @date 2017-3-28
 */
public class Type15 extends AbstractTask {

	private static final long serialVersionUID = -6137146531197423207L;

	
	@Override
	public void acceptTask(BaseTask baseTask, PlayerTask playerTask) {	
		if (baseTask.getId() == playerTask.getTaskId()){
			if(playerTask.getCurrentNum() >= baseTask.getConditionList().get(0)) {	
				playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
			}
		}			
	}

	@Override
	public boolean executeTask(BaseTask baseTask, PlayerTask playerTask, List<Integer> conditionList) {
		playerTask.setCurrentNum(playerTask.getCurrentNum() + conditionList.get(0));
		if(playerTask.getCurrentNum() >= baseTask.getConditionList().get(0)){
			playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
		}
		
		return true;
	}
}
