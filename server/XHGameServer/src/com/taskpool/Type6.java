package com.taskpool;

import java.util.List;
import java.util.Map;

import com.common.GameContext;
import com.constant.TaskConstant;
import com.domain.task.BaseTask;
import com.domain.task.PlayerTask;
import com.domain.wakan.PlayerWakan;
import com.service.IWakanService;


/**
 * 强化某部位装备到N级
 * @author jiangqin
 * @date 2017-4-25
 */
 
public class Type6 extends AbstractTask {

	private static final long serialVersionUID = -6137146531197423207L;

	
	@Override
	public void acceptTask(BaseTask baseTask, PlayerTask playerTask) {
	
		if (baseTask.getId() != playerTask.getTaskId()) return;
		
		IWakanService wakanService = GameContext.getInstance().getServiceCollection().getWakanService();
		Map<Integer, PlayerWakan>  playerWakanMap = wakanService.getPlayerWakanMap(playerTask.getPlayerId());	
		if(playerWakanMap == null) return;
		
		PlayerWakan  playerWakan = null;
		int posId = baseTask.getConditionList().get(0);
		if(posId > 0){					
			playerWakan = wakanService.getPlayerWakanByPosId(playerTask.getPlayerId(), posId);
			if(playerWakan != null && playerWakan.getWakanLevel() >= baseTask.getConditionList().get(1)){
				playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
			}	
		}else{
			for(Map.Entry<Integer, PlayerWakan> entry : playerWakanMap.entrySet()){
				playerWakan = entry.getValue();
				if(playerWakan.getWakanLevel() >= baseTask.getConditionList().get(1)){
					playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
					break;
				}
			}		
		}		
	}

	@Override
	public boolean executeTask(BaseTask baseTask, PlayerTask playerTask, List<Integer> conditionList){
		IWakanService wakanService = GameContext.getInstance().getServiceCollection().getWakanService();
		Map<Integer, PlayerWakan> playerWakanMap = wakanService.getPlayerWakanMap(playerTask.getPlayerId());
		if(playerWakanMap == null) return false;
		
		int posId = baseTask.getConditionList().get(0);
		if(posId > 0){		
			if(!baseTask.getConditionList().get(0).equals(conditionList.get(0))) return false;			
			if(conditionList.get(1) >= baseTask.getConditionList().get(1)){
				playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
			}			
		}else{			
			for(Map.Entry<Integer, PlayerWakan> entry : playerWakanMap.entrySet()){
				PlayerWakan model = entry.getValue();
				if(model.getWakanLevel() >= baseTask.getConditionList().get(1)){
					playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
					break;
				}
			}
		}
				
		return true;
	}
}
