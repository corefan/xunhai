package com.domain.player;

import java.util.ArrayList;
import java.util.List;

import com.domain.GameEntity;
import com.util.SplitStringUtil;

/**
 * 玩家每日数据
 * @author ken
 * @date 2016-12-27
 */
public class PlayerDaily extends GameEntity {

	private static final long serialVersionUID = -7401469751043028605L;
	
	/** 玩家编号 */
	private long playerId;
	/** 当天玩家在线时长（秒）*/
	private int everyOnlineTime;	
	/** 已领取在线奖励领取列表*/
	private  List<Integer> rewardIdList = new ArrayList<Integer>();	
	/** 已领取在线奖励领取列表子串*/
	private String rewardIdStr;
	
	/** 已接取(悬赏)每日任务次数*/
	private int dailyTaskNum;
	/** 每日任务免费刷新次数*/
	private int dailyRefNum;

	/** 已接取猎妖任务次数*/
	private int huntTaskNum;
	
	/** 日常副本次数*/
	private int instanceNum;	
	
	/** 每日福利奖励领取状态*/
	private int dailyRewardState;	
	
	/** 月卡奖励领取状态*/
	private int monthCardAwardState;	
	/** 月卡到期时间*/
	private long monthCardVaildTime;
	
	/** 每日进行侍魂殿次数*/
	private int tiantiNum;
	
	/** 侍魂殿已领奖励次数*/
	private int pkRewardNum;
	
	/** 今日累计充值金额 */
    private int todayPay;
    
    /** 已领取每日累计充值奖励字串*/
    private String drRrewardIdStr;
    /** 已领取每日累计充值奖励列表*/
	private  List<Integer> drRrewardIdList = new ArrayList<Integer>();	
	
	/** 首次转盘抽奖状态 (0: 未使用, 1:已使用)*/
	private int fristTurntableState;

	/** 使用三倍经验药水次数*/
	private int useDrugOneItem;
	/** 经验丹使用次数*/
	private int useDrugTwoItem;
	
	/** vip每日福利领取状态(0:未领取, 1:已领取)*/
	private int vipWelfareState;

	/** 今日捐献1编号次数*/
	private int donate1Times;
	/** 今日捐献2编号次数*/
	private int donate2Times;
	/** 今日捐献3编号次数*/
	private int donate3Times;
	/** 今日捐献4编号次数*/
	private int donate4Times;
	
	/** 是否已经领取过城战俸禄*/
	private int salaryFlag;
	/** 是否已经领取过城战礼包*/
	private int giftFlag;
	
	@Override
	public String getInsertSql() {
		return null;
	}
	
	/**
	 * 获得更新sql
	 * */
	public String getUpdateSql() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE player_daily SET ");
		sql.append(" everyOnlineTime=");
		sql.append(everyOnlineTime);
		sql.append(",");
		sql.append(" rewardIdStr=");
		if(rewardIdStr == null){
			sql.append(rewardIdStr);	
		}else{
			sql.append("'");
			sql.append(rewardIdStr);
			sql.append("'");
		}
		sql.append(",");
		sql.append(" dailyTaskNum=");
		sql.append(dailyTaskNum);
		sql.append(",");
		sql.append(" dailyRefNum=");
		sql.append(dailyRefNum);
		sql.append(",");
		sql.append(" huntTaskNum=");
		sql.append(huntTaskNum);
		sql.append(",");
		sql.append(" instanceNum=");
		sql.append(instanceNum);
		sql.append(",");
		sql.append(" dailyRewardState=");
		sql.append(dailyRewardState);	
		sql.append(",");
		sql.append(" monthCardAwardState=");
		sql.append(monthCardAwardState);
		sql.append(",");	
		sql.append(" monthCardVaildTime = ");
		sql.append(monthCardVaildTime);
		sql.append(",");	
		sql.append(" tiantiNum = ");
		sql.append(tiantiNum);
		sql.append(",");	
		sql.append(" pkRewardNum = ");
		sql.append(pkRewardNum);
		sql.append(",");	
		sql.append(" todayPay = ");
		sql.append(todayPay);
		sql.append(",");
		sql.append(" drRrewardIdStr=");
		if(drRrewardIdStr == null){
			sql.append(drRrewardIdStr);	
		}else{
			sql.append("'");
			sql.append(drRrewardIdStr);
			sql.append("'");
		}
		sql.append(",");
		sql.append(" fristTurntableState = ");
		sql.append(fristTurntableState);	
		sql.append(",");
		sql.append(" useDrugOneItem = ");
		sql.append(useDrugOneItem);	
		sql.append(",");
		sql.append(" useDrugTwoItem = ");
		sql.append(useDrugTwoItem);			
		sql.append(",");
		sql.append(" vipWelfareState = ");
		sql.append(vipWelfareState);
		sql.append(",");
		sql.append(" donate1Times = ");
		sql.append(donate1Times);	
		sql.append(",");
		sql.append(" donate2Times = ");
		sql.append(donate2Times);	
		sql.append(",");
		sql.append(" donate3Times = ");
		sql.append(donate3Times);	
		sql.append(",");
		sql.append(" donate4Times = ");
		sql.append(donate4Times);	
		sql.append(",");
		sql.append(" salaryFlag = ");
		sql.append(salaryFlag);	
		sql.append(",");
		sql.append(" giftFlag = ");
		sql.append(giftFlag);
		sql.append(" WHERE playerId=");
		sql.append(playerId);
		
		return sql.toString();
	}

	/**
	 * 0点重置
	 * */
	public static String getUpdateAllSql() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE player_daily SET ");
		sql.append(" everyOnlineTime=0");
		sql.append(",");		
		sql.append(" rewardIdStr=null");
		sql.append(",");	
		sql.append(" dailyTaskNum=0");
		sql.append(",");	
		sql.append(" dailyRefNum=0");
		sql.append(",");	
		sql.append(" huntTaskNum=0");
		sql.append(",");	
		sql.append(" instanceNum=0");
		sql.append(",");	
		sql.append(" dailyRewardState=0");		
		sql.append(",");	
		sql.append(" monthCardAwardState=0");	
		sql.append(",");	
		sql.append(" tiantiNum=0");
		sql.append(",");	
		sql.append(" pkRewardNum=0");
		sql.append(",");	
		sql.append(" todayPay=0");
		sql.append(",");	
		sql.append(" drRrewardIdStr=null");
		sql.append(",");	
		sql.append(" fristTurntableState=0");
		sql.append(",");	
		sql.append(" useDrugOneItem = 0");
		sql.append(",");
		sql.append(" useDrugTwoItem = 0");		
		sql.append(",");
		sql.append(" vipWelfareState = 0");	
		sql.append(",");
		sql.append(" donate1Times = 0");
		sql.append(",");
		sql.append(" donate2Times = 0");	
		sql.append(",");
		sql.append(" donate3Times = 0");	
		sql.append(",");
		sql.append(" donate4Times = 0");	
		sql.append(",");
		sql.append(" salaryFlag = 0");	
		sql.append(",");
		sql.append(" giftFlag = 0");	
		return sql.toString();
	}
	
	/**
	 * 0点重置数据
	 * */
	public void dispose() {
		everyOnlineTime = 0;
		rewardIdStr = null;
		rewardIdList.clear();
		dailyTaskNum = 0;
		dailyRefNum = 0;
		huntTaskNum = 0;
		instanceNum = 0;
		dailyRewardState = 0;
		monthCardAwardState = 0;
		tiantiNum = 0;
		pkRewardNum = 0;
		todayPay = 0;
		drRrewardIdStr = null;
		drRrewardIdList.clear();
		fristTurntableState = 0;
		useDrugOneItem = 0;		
		useDrugTwoItem = 0;
		vipWelfareState = 0;
		donate1Times = 0;
		donate2Times = 0;
		donate3Times = 0;
		donate4Times = 0;
		salaryFlag = 0;
		giftFlag = 0;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public int getEveryOnlineTime() {
		return everyOnlineTime;
	}

	public void setEveryOnlineTime(int everyOnlineTime) {
		this.everyOnlineTime = everyOnlineTime;	
	}

	public List<Integer> getRewardIdList() {
		return rewardIdList;
	}

	public void setRewardIdList(List<Integer> rewardIdList) {
		this.rewardIdList = rewardIdList;
		
		this.rewardIdStr = rewardIdList.toString();
	}

	public String getRewardIdStr() {
		return rewardIdStr;
	}

	public void setRewardIdStr(String rewardIdStr) {
		this.rewardIdStr = rewardIdStr;		
		
		List<Integer> list = SplitStringUtil.getIntList(rewardIdStr);
		if(list != null){
			this.rewardIdList = list;
		}else{
			this.rewardIdList.clear();
		}		
	}

	public int getDailyTaskNum() {
		return dailyTaskNum;
	}

	public void setDailyTaskNum(int dailyTaskNum) {
		this.dailyTaskNum = dailyTaskNum;
	}

	public int getDailyRefNum() {
		return dailyRefNum;
	}

	public void setDailyRefNum(int dailyRefNum) {
		this.dailyRefNum = dailyRefNum;
	}

	public int getHuntTaskNum() {
		return huntTaskNum;
	}

	public void setHuntTaskNum(int huntTaskNum) {
		this.huntTaskNum = huntTaskNum;
	}

	public int getInstanceNum() {
		return instanceNum;
	}

	public void setInstanceNum(int instanceNum) {
		this.instanceNum = instanceNum;
	}

	public int getDailyRewardState() {
		return dailyRewardState;
	}

	public void setDailyRewardState(int dailyRewardState) {
		this.dailyRewardState = dailyRewardState;
	}

	public int getMonthCardAwardState() {
		return monthCardAwardState;
	}

	public void setMonthCardAwardState(int monthCardAwardState) {
		this.monthCardAwardState = monthCardAwardState;
	}	

	public long getMonthCardVaildTime() {
		return monthCardVaildTime;
	}

	public void setMonthCardVaildTime(long monthCardVaildTime) {
		this.monthCardVaildTime = monthCardVaildTime;
	}

	public int getTiantiNum() {
		return tiantiNum;
	}

	public void setTiantiNum(int tiantiNum) {
		this.tiantiNum = tiantiNum;
	}

	public int getPkRewardNum() {
		return pkRewardNum;
	}

	public void setPkRewardNum(int pkRewardNum) {
		this.pkRewardNum = pkRewardNum;
	}

	public int getTodayPay() {
		return todayPay;
	}

	public void setTodayPay(int todayPay) {
		this.todayPay = todayPay;
	}

	public List<Integer> getDrRrewardIdList() {
		return drRrewardIdList;
	}

	public void setDrRrewardIdList(List<Integer> drRrewardIdList) {
		this.drRrewardIdList = drRrewardIdList;
		
		this.drRrewardIdStr = drRrewardIdList.toString();
	}

	public String getDrRrewardIdStr() {
		return drRrewardIdStr;
	}

	public void setDrRrewardIdStr(String drRrewardIdStr) {
		this.drRrewardIdStr = drRrewardIdStr;
		
		List<Integer> list = SplitStringUtil.getIntList(drRrewardIdStr);
		if(list != null){
			this.drRrewardIdList = list;
		}else{
			this.drRrewardIdList.clear();
		}
	}

	public int getFristTurntableState() {
		return fristTurntableState;
	}

	public void setFristTurntableState(int fristTurntableState) {
		this.fristTurntableState = fristTurntableState;
	}

	public int getUseDrugOneItem() {
		return useDrugOneItem;
	}

	public int getUseDrugTwoItem() {
		return useDrugTwoItem;
	}

	public void setUseDrugOneItem(int useDrugOneItem) {
		this.useDrugOneItem = useDrugOneItem;
	}

	public void setUseDrugTwoItem(int useDrugTwoItem) {
		this.useDrugTwoItem = useDrugTwoItem;
	}

	public int getVipWelfareState() {
		return vipWelfareState;
	}

	public void setVipWelfareState(int vipWelfareState) {
		this.vipWelfareState = vipWelfareState;
	}

	public int getDonate1Times() {
		return donate1Times;
	}

	public void setDonate1Times(int donate1Times) {
		this.donate1Times = donate1Times;
	}

	public int getDonate2Times() {
		return donate2Times;
	}

	public void setDonate2Times(int donate2Times) {
		this.donate2Times = donate2Times;
	}

	public int getDonate3Times() {
		return donate3Times;
	}

	public void setDonate3Times(int donate3Times) {
		this.donate3Times = donate3Times;
	}

	public int getDonate4Times() {
		return donate4Times;
	}

	public void setDonate4Times(int donate4Times) {
		this.donate4Times = donate4Times;
	}

	public int getSalaryFlag() {
		return salaryFlag;
	}

	public void setSalaryFlag(int salaryFlag) {
		this.salaryFlag = salaryFlag;
	}

	public int getGiftFlag() {
		return giftFlag;
	}

	public void setGiftFlag(int giftFlag) {
		this.giftFlag = giftFlag;
	}
	
}
