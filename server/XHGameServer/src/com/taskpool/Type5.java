package com.taskpool;

import java.util.List;

import com.common.GameContext;
import com.constant.ItemConstant;
import com.constant.TaskConstant;
import com.domain.bag.PlayerEquipment;
import com.domain.task.BaseTask;
import com.domain.task.PlayerTask;
import com.service.IEquipmentService;

/**
 * 穿戴某部位装备
 * @author ken
 * @date 2017-2-21
 */
public class Type5 extends AbstractTask {

	private static final long serialVersionUID = -6137146531197423207L;

	
	@Override
	public void acceptTask(BaseTask baseTask, PlayerTask playerTask) {
		if (baseTask.getId() == playerTask.getTaskId()){
			IEquipmentService equipmentService = GameContext.getInstance().getServiceCollection().getEquipmentService();		
			List<PlayerEquipment> list = equipmentService.getPlayerEquipmentList(playerTask.getPlayerId());
			for (PlayerEquipment playerEquipment : list){
				if(playerEquipment.getState() == ItemConstant.EQUIP_STATE_DRESS &&
						playerEquipment.getEquipType() == baseTask.getConditionList().get(0)){
					playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
				}
			}
		}
	}

	@Override
	public boolean executeTask(BaseTask baseTask, PlayerTask playerTask, List<Integer> conditionList) {
		if(baseTask.getConditionList().get(0).equals(conditionList.get(0))){
			playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
		}
		
		return true;
	}

}
