package com.taskpool;

import java.util.List;

import com.constant.TaskConstant;
import com.domain.task.BaseTask;
import com.domain.task.PlayerTask;

/**
 *  进行N次分解
 * @author jiangqin
 * @date 2017-3-28
 */
public class Type14 extends AbstractTask {

	private static final long serialVersionUID = -6137146531197423207L;

	
	@Override
	public void acceptTask(BaseTask baseTask, PlayerTask playerTask) {	
	
				
	}

	@Override
	public boolean executeTask(BaseTask baseTask, PlayerTask playerTask, List<Integer> conditionList) {
		if (baseTask.getConditionList().get(0).equals(conditionList.get(0))){
			playerTask.setCurrentNum(playerTask.getCurrentNum() + 1);
			if(playerTask.getCurrentNum() >= baseTask.getConditionList().get(1)){
				playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
			}
		}		
		return true;
	}

}
