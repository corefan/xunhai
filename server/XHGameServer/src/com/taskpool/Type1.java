package com.taskpool;

import java.util.List;

import com.domain.task.BaseTask;
import com.domain.task.PlayerTask;

/**
 * npc对话
 * @author ken
 * @date 2017-2-21
 */
public class Type1 extends AbstractTask {

	private static final long serialVersionUID = -6137146531197423207L;

	
	@Override
	public void acceptTask(BaseTask baseTask, PlayerTask playerTask) {
		
	}

	@Override
	public boolean executeTask(BaseTask baseTask, PlayerTask playerTask, List<Integer> conditionList) {
		return true;
	}

}
