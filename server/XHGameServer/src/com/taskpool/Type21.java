package com.taskpool;

import java.util.List;

import com.constant.TaskConstant;
import com.domain.task.BaseTask;
import com.domain.task.PlayerTask;

/**
 *  天梯引导
 * @author jiangqin
 * @date 2017-4-27
 */

public class Type21 extends AbstractTask {

	private static final long serialVersionUID = -6137146531197423207L;

	
	@Override
	public void acceptTask(BaseTask baseTask, PlayerTask playerTask) {

	}

	@Override
	public boolean executeTask(BaseTask baseTask, PlayerTask playerTask, List<Integer> conditionList) {
		playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
	
		return true;
	}

}
