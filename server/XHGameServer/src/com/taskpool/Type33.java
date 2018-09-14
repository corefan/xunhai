/**
 * 
 */
package com.taskpool;

import java.util.List;

import com.common.GameContext;
import com.constant.TaskConstant;
import com.domain.task.BaseTask;
import com.domain.task.PlayerTask;
import com.domain.wakan.BaseAwake;
import com.service.IWakanService;

/**
 * 注灵觉醒等级
 * @author songlin
 * @date 2018-4-10
 */
public class Type33  extends AbstractTask{

	private static final long serialVersionUID = 2800416416020935073L;

	@Override
	public void acceptTask(BaseTask baseTask, PlayerTask playerTask) {
		
		if (baseTask.getId() == playerTask.getTaskId()){	
			IWakanService wakanService =  GameContext.getInstance().getServiceCollection().getWakanService();
			int needLevel = baseTask.getConditionList().get(0);
			
			BaseAwake mode = wakanService.getBaseAwake(playerTask.getPlayerId());
			if(mode != null && mode.getId() >= needLevel){
				playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
			}
		}		
	}

	@Override
	public boolean executeTask(BaseTask baseTask, PlayerTask playerTask, List<Integer> conditionList) {
		if(playerTask.getTaskId() != baseTask.getId()) return false;
		
		if(conditionList.get(0) >= baseTask.getConditionList().get(0)){
			playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
			return true;
		}
		
		return false;
	}

}
