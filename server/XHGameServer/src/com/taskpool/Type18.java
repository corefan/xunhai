package com.taskpool;

import java.util.List;

import com.common.GameContext;
import com.constant.TaskConstant;
import com.domain.family.PlayerFamily;
import com.domain.task.BaseTask;
import com.domain.task.PlayerTask;
import com.service.IFamilyService;

/**
 *  加入一个家族
 * @author jiangqin
 * @date 2017-3-28
 */
public class Type18 extends AbstractTask {

	private static final long serialVersionUID = -6137146531197423207L;

	
	@Override
	public void acceptTask(BaseTask baseTask, PlayerTask playerTask) {	
		if (baseTask.getId() == playerTask.getTaskId()){
			IFamilyService familyService = GameContext.getInstance().getServiceCollection().getFamilyService();
			PlayerFamily playerFamily = familyService.getPlayerFamily(playerTask.getPlayerId());
			if (playerFamily != null && playerFamily.getPlayerFamilyId() > 0){			
				playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
			}	
		}
	}

	@Override
	public boolean executeTask(BaseTask baseTask, PlayerTask playerTask, List<Integer> conditionList) {			
		playerTask.setTaskState(TaskConstant.TASK_STATE_YES);		
		return true;
	}

}
