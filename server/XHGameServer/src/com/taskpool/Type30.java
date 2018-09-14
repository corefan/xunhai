/**
 * 
 */
package com.taskpool;

import java.util.List;

import com.constant.TaskConstant;
import com.domain.task.BaseTask;
import com.domain.task.PlayerTask;

/**
 * 好友引导
 * @author jiangqin
 * @date 2017-8-18
 */
public class Type30  extends AbstractTask{

	private static final long serialVersionUID = 2800416416020935073L;

	@Override
	public void acceptTask(BaseTask baseTask, PlayerTask playerTask) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean executeTask(BaseTask baseTask, PlayerTask playerTask, List<Integer> conditionList) {
		if(playerTask.getTaskId() != baseTask.getId()) return false;
		
		if(conditionList.get(0) == baseTask.getConditionList().get(0)){
			playerTask.setCurrentNum(playerTask.getCurrentNum() + conditionList.get(1));
			if(playerTask.getCurrentNum() > baseTask.getConditionList().get(1)){
				playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
			}
		}		
		
		return true;
	}

}
