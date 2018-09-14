package com.taskpool;

import java.util.List;

import com.common.GameContext;
import com.constant.TaskConstant;
import com.domain.player.PlayerExt;
import com.domain.task.BaseTask;
import com.domain.task.PlayerTask;
import com.service.IPlayerService;

/**
 *  进行组队操作
 * @author jiangqin
 * @date 2017-3-28
 */
public class Type17 extends AbstractTask {

	private static final long serialVersionUID = -6137146531197423207L;

	
	@Override
	public void acceptTask(BaseTask baseTask, PlayerTask playerTask) {	
		if (baseTask.getId() == playerTask.getTaskId()){
			IPlayerService playerService = GameContext.getInstance().getServiceCollection().getPlayerService();
			PlayerExt playerExt = playerService.getPlayerExtById(playerTask.getPlayerId());	
			if(playerExt == null) return;
			if (playerExt.getTeamId() > 0){			
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
