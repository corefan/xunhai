package com.taskpool;

import java.util.List;

import com.common.GameContext;
import com.constant.TaskConstant;
import com.domain.friend.PlayerFriend;
import com.domain.task.BaseTask;
import com.domain.task.PlayerTask;
import com.service.IFriendService;

/**
 * 添加好友
 * @author jiangqin
 * @date 2017-4-27
 */
public class Type19 extends AbstractTask {

	private static final long serialVersionUID = -6137146531197423207L;

	
	@Override
	public void acceptTask(BaseTask baseTask, PlayerTask playerTask) {
		if (baseTask.getId() == playerTask.getTaskId()){
			IFriendService friendService = GameContext.getInstance().getServiceCollection().getFriendService();
			List<PlayerFriend> list= friendService.listPlayerFriend(playerTask.getPlayerId());
			if(list.size() >= baseTask.getConditionList().get(0)){
				playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
			}
		}
	}

	@Override
	public boolean executeTask(BaseTask baseTask, PlayerTask playerTask, List<Integer> conditionList) {
		playerTask.setCurrentNum(playerTask.getCurrentNum() + conditionList.get(0));
		if(playerTask.getCurrentNum() >= baseTask.getConditionList().get(0)){
			playerTask.setTaskState(TaskConstant.TASK_STATE_YES);
		}
		return true;
	}

}
