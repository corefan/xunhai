package com.domain.task;

import com.domain.GameEntity;

/**
 * 玩家任务
 * @author ken
 * @date 2017-2-20
 */
public class PlayerTask extends GameEntity {

	private static final long serialVersionUID = 7416536055847040363L;
	
	/** 玩家任务编号 */
	private long id;
	/** 玩家编号 */
	private long playerId;
	/** 任务编号 */
	private int taskId;
	/** 任务类型(1:主线任务2:支线任务) */
	private int type;
	/** 条件类型*/
	private int conditionType;
	/** 当前任务完成值 */
	private int currentNum;
	/** 提交状态(0:不可提交1:可提交) */
	private int taskState;
	
	/** 是否已删除(1.已删 0.未删) */
	private int deleteFlag;

	
	public String getInsertSql() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO player_task ");
		sql.append("(id, playerId, taskId, type, conditionType,currentNum,taskState,deleteFlag) VALUES");
		sql.append(" (");
		sql.append(id);
		sql.append(",");
		sql.append(playerId);
		sql.append(",");
		sql.append(taskId);
		sql.append(",");
		sql.append(type);
		sql.append(",");
		sql.append(conditionType);
		sql.append(",");
		sql.append(currentNum);
		sql.append(",");
		sql.append(taskState);
		sql.append(",");
		sql.append(deleteFlag);
		sql.append(")");		
		return sql.toString();
	}
	/**
	 * 得到更新sql
	 * */
	public String getUpdateSql() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE player_task SET ");
		sql.append("currentNum = ");
		sql.append(currentNum);
		sql.append(",");
		sql.append("taskState = ");
		sql.append(taskState);
		sql.append(",");
		sql.append("deleteFlag = ");
		sql.append(deleteFlag);
		sql.append(" WHERE id = ");
		sql.append(id);
		
		return sql.toString();
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getConditionType() {
		return conditionType;
	}

	public void setConditionType(int conditionType) {
		this.conditionType = conditionType;
	}

	public int getCurrentNum() {
		return currentNum;
	}

	public void setCurrentNum(int currentNum) {
		this.currentNum = currentNum;
	}

	public int getTaskState() {
		return taskState;
	}
	
	public void setTaskState(int taskState) {
		this.taskState = taskState;
	}
	
	public int getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

}
