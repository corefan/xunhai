package com.taskpool;

import java.util.List;

import com.common.GameContext;
import com.constant.TaskConstant;
import com.domain.bag.PlayerDrug;
import com.domain.task.BaseTask;
import com.domain.task.PlayerTask;
import com.service.IBagService;

/**
 *  装备药品
 * @author jiangqin
 * @date 2017-3-28
 */
public class Type13 extends AbstractTask {

	private static final long serialVersionUID = -6137146531197423207L;

	
	@Override
	public void acceptTask(BaseTask baseTask, PlayerTask playerTask) {
		IBagService bagService = GameContext.getInstance().getServiceCollection().getBagService();
		if (baseTask.getId() == playerTask.getTaskId()){
			List<PlayerDrug> lists = bagService.listPlayerDrugs(playerTask.getPlayerId());
			if(lists != null && !lists.isEmpty()){
				for(PlayerDrug model : lists){						
					if(model.getItemId() > 0 ) {
						playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
					}						
				}
			}
		}
	}

	@Override
	public boolean executeTask(BaseTask baseTask, PlayerTask playerTask, List<Integer> conditionList) {
		playerTask.setTaskState(TaskConstant.TASK_STATE_YES);		
		return true;
	}

}
