package com.domain;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 激活码
 * @author ken
 * @date 2018年8月9日
 */
public class ActCode extends GameEntity {

	private static final long serialVersionUID = 9136023605666589080L;

	/** 激活码编号 */
	private long id;
	/** 激活码 */
	private String code;
	/** 站点 */
	private String agent;
	/** 类型 */
	private int type;
	/** 奖励编号 */
	private int rewardId;
	/** 是否专属 */
	private int exclusive;
	/** 状态(0.未使用 1.已使用) */
	private int state;
	/** 使用者账号 */
	private String userName;
	/** 创建时间 */
	private Date createTime;
	/** 使用时间 */
	private Date useTime;
	
	/** 得到创建sql */
	public String getInsertSql() {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("INSERT INTO actcode (id,code,agent,type,rewardId,exclusive,state,createTime) VALUES ");
		sb.append("(");
		sb.append(id);
		sb.append(",");
		sb.append("'");
		sb.append(code);
		sb.append("'");
		sb.append(",");
		sb.append("'");
		sb.append(agent);
		sb.append("'");
		sb.append(",");
		sb.append(type);
		sb.append(",");
		sb.append(rewardId);
		sb.append(",");
		sb.append(exclusive);
		sb.append(",");
		sb.append(state);
		sb.append(",");
		sb.append("'");
		sb.append(new Timestamp(createTime.getTime()));
		sb.append("'");
		sb.append(")");
		
		return sb.toString();
	}
	
	/** 得到更新sql */
	public String getUpdateSql() {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("UPDATE actcode SET ");
		sb.append(" state = ");
		sb.append(state);
		sb.append(",");
		sb.append(" userName = ");
		sb.append("'");
		sb.append(userName);
		sb.append("'");
		sb.append(",");
		sb.append(" useTime = ");
		sb.append("'");
		sb.append(new Timestamp(System.currentTimeMillis()));
		sb.append("'");
		sb.append(" WHERE id = "+id);
		
		return sb.toString();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getRewardId() {
		return rewardId;
	}

	public void setRewardId(int rewardId) {
		this.rewardId = rewardId;
	}

	public int getExclusive() {
		return exclusive;
	}

	public void setExclusive(int exclusive) {
		this.exclusive = exclusive;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUseTime() {
		return useTime;
	}

	public void setUseTime(Date useTime) {
		this.useTime = useTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	

}
