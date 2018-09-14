package com.taskpool;

import java.util.List;

import com.common.GameContext;
import com.common.RandomService;
import com.constant.TaskConstant;
import com.domain.battle.BaseTaskItem;
import com.domain.task.BaseTask;
import com.domain.task.PlayerTask;
import com.service.ITaskService;
import com.util.LogUtil;
import com.util.ResourceUtil;

/**
 *  任务物品获取
 * @author jiangqin
 * @date 2017-3-28
 */
public class Type12 extends AbstractTask {

	private static final long serialVersionUID = -6137146531197423207L;

	
	@Override
	public void acceptTask(BaseTask baseTask, PlayerTask playerTask) {	
	
		if (baseTask.getId() == playerTask.getTaskId()){
			if(playerTask.getCurrentNum() >= baseTask.getConditionList().get(2)) {	
				playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
			}
		}		
	}

	@Override
	public boolean executeTask(BaseTask baseTask, PlayerTask playerTask, List<Integer> conditionList) {		
		ITaskService taskService = GameContext.getInstance().getServiceCollection().getTaskService();
		BaseTaskItem baseTaskItem = null;
		try {
			baseTaskItem = taskService.getBaseTaskItem(baseTask.getConditionList().get(1));
		} catch (Exception e) {
			LogUtil.error("baseTask.getConditionList() is error with taskId = "+baseTask.getId());
		}
		
		if (baseTaskItem != null){
			boolean isGetItem = getTaskItemId(baseTaskItem, conditionList.get(1));	
			
			if(isGetItem){
				playerTask.setCurrentNum(playerTask.getCurrentNum() + 1);
				if(playerTask.getCurrentNum() >= baseTask.getConditionList().get(2)){
					playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
				}
				
				//获得物品飘字
				if(baseTaskItem.getItemName() != null){
					GameContext.getInstance().getServiceCollection().getCommonService().sendNoticeMsg(playerTask.getPlayerId(), ResourceUtil.getValue("item_1", baseTaskItem.getItemName()));		
				}	
			}						
		}			
				
		return true;
	}

	/** 获取的任务物品ID*/
	private boolean getTaskItemId(BaseTaskItem baseTaskItem, int monsterId) {	
	
		int n = RandomService.getRandomNum(10000);		
		for(int i = 0; i < baseTaskItem.getWeightList().size(); i++){				
			if(baseTaskItem.getWeightList().get(i).get(0).equals(monsterId) ){
				if(n < baseTaskItem.getWeightList().get(i).get(1)){
					return true;
				}
			}			
		}
		
		return false;
	}

}
