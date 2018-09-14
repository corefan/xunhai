package com.taskpool;

import java.util.List;

import com.common.GameContext;
import com.constant.TaskConstant;
import com.domain.skill.PlayerSkill;
import com.domain.task.BaseTask;
import com.domain.task.PlayerTask;
import com.service.ISkillService;

/**
 * 技能学习
 * @author ken
 * @date 2017-2-21
 */
public class Type8 extends AbstractTask {

	private static final long serialVersionUID = -6137146531197423207L;

	
	@Override
	public void acceptTask(BaseTask baseTask, PlayerTask playerTask) {
		if (baseTask.getId() == playerTask.getTaskId()) return;
		
		ISkillService skillService = GameContext.getInstance().getServiceCollection().getSkillService();
		PlayerSkill playerSkill = skillService.getPlayerSkillByIndex(playerTask.getPlayerId(), baseTask.getConditionList().get(0)); 
		if(playerSkill != null){
			playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
		}			
				
	}

	@Override
	public boolean executeTask(BaseTask baseTask, PlayerTask playerTask, List<Integer> conditionList) {
		ISkillService skillService = GameContext.getInstance().getServiceCollection().getSkillService();
		PlayerSkill playerSkill = skillService.getPlayerSkillByIndex(playerTask.getPlayerId(), baseTask.getConditionList().get(0)); 
		if(playerSkill != null){			
			playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
		}
	
		return true;
	}

}
