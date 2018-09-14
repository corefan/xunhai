package com.domain.puppet;

import java.util.HashMap;
import java.util.Map;

/**
 * 玩家场景模型
 * @author ken
 * @date 2016-12-28
 */
public class PlayerPuppet extends BasePuppet {

	private static final long serialVersionUID = -8872406158903203033L;

	/** 职业*/
	private int career;
	/** 武器外形 (装备基础ID)*/
	private int weaponStyle;	
	/** 翅膀外形 */
	private int wingStyle;	
	/** 采集唯一*/
	private int playerCollectId;
	/** 开始采集时间*/
	private long collectTime;	
	/** 奖励刷新点记录*/
	private long collectRewardRefTime;	
	/** 掉落物拾取标识*(副本掉落)*/
	private boolean isPickUp;	
			
	/** 下线时间*/
	private long logoutTime;
	
	/** pk值*/
	private int pkVlaue;
	/** 名字颜色(1->白名, 2->灰名, 3->红名)*/
	private int nameColor;
	/** 灰名更新时间*/
	private long grayUpdateTime;
	/** pk值更新时间   上线重新计算*/
	private long pkValueUpdateTime;
	
	/** 城战地图 加经验*/
	private long guildFightUpdateTime;
	
	/** 段位*/
	private int stage;	
	
	/** 玩家竞技场初始物品*/
	private Map<Integer, Integer> initItemMap = new HashMap<Integer, Integer>();
	
	/** 是否是机器人*/
	private boolean isRebot;
	
	public int getCareer() {
		return career;
	}
	public void setCareer(int career) {
		this.career = career;
	}
	public int getWeaponStyle() {
		return weaponStyle;
	}
	public void setWeaponStyle(int weaponStyle) {
		this.weaponStyle = weaponStyle;
	}
	public long getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(long collectTime) {
		this.collectTime = collectTime;
	}
	public long getLogoutTime() {
		return logoutTime;
	}
	public void setLogoutTime(long logoutTime) {
		this.logoutTime = logoutTime;
	}
	public long getCollectRewardRefTime() {
		return collectRewardRefTime;
	}
	public void setCollectRewardRefTime(long collectRewardRefTime) {
		this.collectRewardRefTime = collectRewardRefTime;
	}
	public int getPlayerCollectId() {
		return playerCollectId;
	}
	public void setPlayerCollectId(int playerCollectId) {
		this.playerCollectId = playerCollectId;
	}
	public int getPkVlaue() {
		return pkVlaue;
	}
	public void setPkVlaue(int pkVlaue) {
		if(pkVlaue <= 0){
			pkVlaue = 0;
			this.pkValueUpdateTime = 0;
		}
		if(pkVlaue > 10000){
			pkVlaue = 10000;
		}
		this.pkVlaue = pkVlaue;
	}
	public int getNameColor() {
		return nameColor;
	}
	public void setNameColor(int nameColor) {
		this.nameColor = nameColor;
	}
	public long getGrayUpdateTime() {
		return grayUpdateTime;
	}
	public void setGrayUpdateTime(long grayUpdateTime) {
		this.grayUpdateTime = grayUpdateTime;
	}
	public long getPkValueUpdateTime() {
		return pkValueUpdateTime;
	}
	public void setPkValueUpdateTime(long pkValueUpdateTime) {
		this.pkValueUpdateTime = pkValueUpdateTime;
	}
	public int getStage() {
		return stage;
	}
	public void setStage(int stage) {
		this.stage = stage;
	}
	public int getWingStyle() {
		return wingStyle;
	}
	public void setWingStyle(int wingStyle) {
		this.wingStyle = wingStyle;
	}
	public boolean isPickUp() {
		return isPickUp;
	}
	public void setPickUp(boolean isPickUp) {
		this.isPickUp = isPickUp;
	}
	public boolean isRebot() {
		return isRebot;
	}
	public void setRebot(boolean isRebot) {
		this.isRebot = isRebot;
	}
	public Map<Integer, Integer> getInitItemMap() {
		return initItemMap;
	}
	public void setInitItemMap(Map<Integer, Integer> initItemMap) {
		this.initItemMap = initItemMap;
	}
	public long getGuildFightUpdateTime() {
		return guildFightUpdateTime;
	}
	public void setGuildFightUpdateTime(long guildFightUpdateTime) {
		this.guildFightUpdateTime = guildFightUpdateTime;
	}	
	
}
