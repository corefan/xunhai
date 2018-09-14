package com.taskpool;

import java.util.List;

import com.common.GameContext;
import com.constant.TaskConstant;
import com.domain.skill.PlayerSkill;
import com.domain.task.BaseTask;
import com.domain.task.PlayerTask;
import com.service.ISkillService;

/**
 * 技能升级
 * @author ken
 * @date 2017-2-21
 */
public class Type9 extends AbstractTask {

	private static final long serialVersionUID = -6137146531197423207L;

	
	@Override
	public void acceptTask(BaseTask baseTask, PlayerTask playerTask) {
		if (baseTask.getId() != playerTask.getTaskId()) return;	
		
		ISkillService skillService = GameContext.getInstance().getServiceCollection().getSkillService();
		int skillIndex = baseTask.getConditionList().get(0);
		if(skillIndex >  0){
			PlayerSkill playerSkill = skillService.getPlayerSkillByIndex(playerTask.getPlayerId(), skillIndex);
			if(playerSkill != null && playerSkill.getLevel() >= baseTask.getConditionList().get(1)){
				playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
			}	
		}else{
			List<PlayerSkill> playerSkills = skillService.listPlayerSkills(playerTask.getPlayerId());			
			for(PlayerSkill model : playerSkills){
				if(model.getLevel() > baseTask.getConditionList().get(1)){
					playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
					break;
				}
			}			
		}	
	}

	@Override
	public boolean executeTask(BaseTask baseTask, PlayerTask playerTask, List<Integer> conditionList) {
		ISkillService skillService = GameContext.getInstance().getServiceCollection().getSkillService();
		
		int skillIndex = baseTask.getConditionList().get(0);
		if(skillIndex > 0){
			PlayerSkill playerSkill = skillService.getPlayerSkillByIndex(playerTask.getPlayerId(), skillIndex); 
			if(playerSkill != null && conditionList.get(1) >= baseTask.getConditionList().get(1)){
				playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
			}		
		}else{
			List<PlayerSkill> playerSkills = skillService.listPlayerSkills(playerTask.getPlayerId());			
			for(PlayerSkill model : playerSkills){
				if(model.getLevel() > baseTask.getConditionList().get(1)){
					playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
					return true;
				}
			}		
		}
		
		return true;
	}

}
