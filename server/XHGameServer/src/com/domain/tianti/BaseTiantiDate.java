package com.domain.tianti;

import java.io.Serializable;
import java.util.Date;

/**
 * 天梯赛季日期配置
 * @author ken
 * @date 2017-4-14
 */
public class BaseTiantiDate implements Serializable {

	private static final long serialVersionUID = 4124876886834732145L;

	private int id;
	private String startDate;	
	private String endDate;	
	
	private Date startTime;	
	private Date endTime;	
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
