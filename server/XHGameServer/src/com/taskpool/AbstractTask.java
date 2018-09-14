package com.taskpool;

import java.io.Serializable;
import java.util.List;

import com.domain.task.BaseTask;
import com.domain.task.PlayerTask;

/**
 * 任务抽象类
 * @author ken
 * @date 2017-2-21
 */
public abstract class AbstractTask implements Serializable {

	private static final long serialVersionUID = 7823607801642129236L;

	public abstract void acceptTask(BaseTask baseTask, PlayerTask playerTask) ;
	
	public abstract boolean executeTask(BaseTask baseTask, PlayerTask playerTask, List<Integer> conditionList);
}
