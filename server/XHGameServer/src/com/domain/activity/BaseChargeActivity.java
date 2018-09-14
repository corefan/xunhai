package com.domain.activity;

import java.io.Serializable;
import java.util.List;

/**
 * 七天累计充值时间配置
 * @author jiangqin
 * @date 2017-11-22
 */
public class BaseChargeActivity implements Serializable{
	
	private static final long serialVersionUID = 20903862787335085L;
	
	/** 编号 */
	private int  id;
	/** 活动开始时间*/
	private String startDate;	
	/** 活动结束时间*/
	private String endDate;
	/** 活动奖励*/
	private String rewardStr;
	private List<Integer> rewardList;
	
	public int getId() {
		return id;
	}
	public String getStartDate() {
		return startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getRewardStr() {
		return rewardStr;
	}
	public void setRewardStr(String rewardStr) {
		this.rewardStr = rewardStr;
	}
	public List<Integer> getRewardList() {
		return rewardList;
	}
	public void setRewardList(List<Integer> rewardList) {
		this.rewardList = rewardList;
	}
}
