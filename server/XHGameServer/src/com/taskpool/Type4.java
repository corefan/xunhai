package com.taskpool;

import java.util.List;

import com.constant.TaskConstant;
import com.domain.task.BaseTask;
import com.domain.task.PlayerTask;

/**
 * 指定地图杀某种怪物N只
 * @author ken
 * @date 2017-2-21
 */
public class Type4 extends AbstractTask {

	private static final long serialVersionUID = -6137146531197423207L;

	
	@Override
	public void acceptTask(BaseTask baseTask, PlayerTask playerTask) {
		
	}

	@Override
	public boolean executeTask(BaseTask baseTask, PlayerTask playerTask, List<Integer> conditionList) {
		if(baseTask.getConditionList().get(0).equals(conditionList.get(0)) && baseTask.getConditionList().get(1).equals(conditionList.get(1))){
			playerTask.setCurrentNum(playerTask.getCurrentNum() + 1);
			if(playerTask.getCurrentNum() >= baseTask.getConditionList().get(2)){
				playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
			}
			return true;
		}
		return false;
	}

}
