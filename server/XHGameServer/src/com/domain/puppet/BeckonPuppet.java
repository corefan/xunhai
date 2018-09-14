package com.domain.puppet;

/**
 * 召唤怪
 * @author ken
 * @date 2017-6-7
 */
public class BeckonPuppet extends BasePuppet {

	private static final long serialVersionUID = -86087614248720885L;

	/** 主人*/
	private String ownerGuid;
	private long playerId;
	
	/** 创建时间*/
	private long createTime;
	/** 死亡时间 */
	private long deadTime;
	
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public long getDeadTime() {
		return deadTime;
	}
	public void setDeadTime(long deadTime) {
		this.deadTime = deadTime;
	}
	public String getOwnerGuid() {
		return ownerGuid;
	}
	public void setOwnerGuid(String ownerGuid) {
		this.ownerGuid = ownerGuid;
	}
	
}
