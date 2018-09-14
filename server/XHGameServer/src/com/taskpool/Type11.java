package com.taskpool;

import java.util.List;

import com.common.GameContext;
import com.constant.ExceptionConstant;
import com.constant.TaskConstant;
import com.domain.GameException;
import com.domain.puppet.PlayerPuppet;
import com.domain.task.BaseTask;
import com.domain.task.PlayerTask;
import com.service.ISceneService;

/**
 *  和平模式切换
 * @author jiangqin
 * @date 2017-3-28
 */
public class Type11 extends AbstractTask {

	private static final long serialVersionUID = -6137146531197423207L;

	
	@Override
	public void acceptTask(BaseTask baseTask, PlayerTask playerTask) {	
		ISceneService sceneService = GameContext.getInstance().getServiceCollection().getSceneService();	
		PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerTask.getPlayerId());
		if(playerPuppet == null) throw new GameException(ExceptionConstant.PLAYER_1111);
		
		if(playerPuppet.getPkMode() == baseTask.getConditionList().get(0)){
			playerTask.setTaskState(TaskConstant.TASK_STATE_YES);	
		}	
	}

	@Override
	public boolean executeTask(BaseTask baseTask, PlayerTask playerTask, List<Integer> conditionList) {
		if (baseTask.getConditionList().get(0).equals(conditionList.get(0))){			
			playerTask.setTaskState(TaskConstant.TASK_STATE_YES);			
		}		
		return true;
	}

}
