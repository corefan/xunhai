package com.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;


/**
 * 系统公告
 * @author ken.li
 *
 */
public class SystemNotice implements Serializable{

	private static final long serialVersionUID = 5084466524608199139L;

	/** 系统公告编号 */
	private Long systemNoticeID;
	
	/** 系统公告内容 */
	private String content;
	
	/** 开始时间 */
	private Date startTime;
	/** 结束时间 */
	private Date endTime;
	/** 频率(间隔:秒) */
	private Integer frequency;
	

	/**
	 * 得到插入sql
	 * */
	public String getInsertSql() {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("INSERT INTO T_SYSTEM_NOTICE(UUID, TITLE,CONTENT,TYPE,START_TIME,END_TIME,FREQUENCY,LINK,STATE) ");
		sql.append(" VALUES");
		sql.append("(");
		
		sql.append(",");
		
		sql.append(",");
		sql.append("'");
		sql.append(content);
		sql.append("'");
		sql.append(",");
		
		sql.append(",");
		if (startTime == null) {
			sql.append(startTime);
		} else {
			sql.append("'");
			sql.append(new Timestamp(startTime.getTime()));
			sql.append("'");
		}
		sql.append(",");
		if (endTime == null) {
			sql.append(endTime);
		} else {
			sql.append("'");
			sql.append(new Timestamp(endTime.getTime()));
			sql.append("'");
		}
		sql.append(",");
		sql.append(frequency);
		sql.append(",");
		
		sql.append(",");
		sql.append(")");
		
		return sql.toString();
	}
	
	/**
	 * 得到更新sql
	 * */
	public String getUpdateSql() {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("UPDATE T_SYSTEM_NOTICE SET ");
		sql.append("TITLE = ");
		
		sql.append(",");
		sql.append("CONTENT = ");
		sql.append("'");
		sql.append(content);
		sql.append("'");
		sql.append(",");
		sql.append("TYPE = ");
		
		sql.append(",");
		sql.append("START_TIME = ");
		if (startTime == null) {
			sql.append(startTime);
		} else {
			sql.append("'");
			sql.append(new Timestamp(startTime.getTime()));
			sql.append("'");
		}
		sql.append(",");
		sql.append("END_TIME = ");
		if (endTime == null) {
			sql.append(endTime);
		} else {
			sql.append("'");
			sql.append(new Timestamp(endTime.getTime()));
			sql.append("'");
		}
		sql.append(",");
		sql.append("FREQUENCY = ");
		sql.append(frequency);
		sql.append(",");
		sql.append("LINK = ");
		
		sql.append(",");
		sql.append("STATE = ");
		sql.append(" WHERE SYSTEM_NOTICE_ID = ");
		sql.append(systemNoticeID);
		
		return sql.toString();
	}
	
	public Long getSystemNoticeID() {
		return systemNoticeID;
	}

	public void setSystemNoticeID(Long systemNoticeID) {
		this.systemNoticeID = systemNoticeID;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	
}
