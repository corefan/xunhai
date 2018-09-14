package com.dao.task;

import java.util.List;

import com.db.GameSqlSessionTemplate;
import com.domain.task.PlayerTask;

/**
 * 玩家任务DAO
 * @author ken
 * @date 2017-2-20
 */
public class PlayerTaskDAO extends GameSqlSessionTemplate {

	/**
	 * 创建玩家任务
	 * */
	public void createPlayerTask(PlayerTask playerTask) {
		this.insert_noreturn(playerTask.getInsertSql());
	}
	
	/**
	 * 获得玩家任务列表
	 * */
	public List<PlayerTask> listPlayerTasks(long playerId) {
		String sql = "SELECT * FROM player_task  WHERE playerId="+playerId+" AND deleteFlag = 0";

		return this.selectList(sql, PlayerTask.class);
	}
	
	/**
	 * 调度清理已删除的任务
	 * */
	public void quartzDeletePlayerTask() {
		this.delete("DELETE FROM player_task WHERE deleteFlag = 1");
	}
}
