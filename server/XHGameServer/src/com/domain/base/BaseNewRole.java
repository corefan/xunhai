package com.domain.base;

import java.io.Serializable;
import java.util.List;

/**
 * 初始角色配置表
 * @author ken
 * @date 2016-12-30
 */
public class BaseNewRole implements Serializable {

	private static final long serialVersionUID = 596248120617408664L;

	private int career; //职业
	private int dressStyle; //角色外形
	private int weaponStyle; //武器外形
	private int mapName; //初始地图
	private String position; //初始坐标
	private int x; //坐标
	private int y;//坐标
	private int z;//坐标
	private int direction; //初始朝向
	private int diamond; //钻石（玩家充值的人民币）
	private int stone; //宝玉（游戏内货币）
	private int gold; //金币（游戏内货币）
	private int bagGrid; //初始背包格子数
	private int maxTradeGrid; //初始货架格子数
	private int moveSpeed; //初始速度
	
	
	private String bageitems;//包裹物品（类型，模型ID,数量,是否绑定) 1为绑定；0为不绑定
	private List<List<Integer>> bageitemList; 
	
	private String initSkills;//初始技能
	private List<Integer> initSkillList; 
	
	public int getCareer() {
		return career;
	}
	public void setCareer(int career) {
		this.career = career;
	}
	public int getDressStyle() {
		return dressStyle;
	}
	public void setDressStyle(int dressStyle) {
		this.dressStyle = dressStyle;
	}
	public int getWeaponStyle() {
		return weaponStyle;
	}
	public void setWeaponStyle(int weaponStyle) {
		this.weaponStyle = weaponStyle;
	}
	public int getMapName() {
		return mapName;
	}
	public void setMapName(int mapName) {
		this.mapName = mapName;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getZ() {
		return z;
	}
	public void setZ(int z) {
		this.z = z;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public int getDiamond() {
		return diamond;
	}
	public void setDiamond(int diamond) {
		this.diamond = diamond;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public int getBagGrid() {
		return bagGrid;
	}
	public void setBagGrid(int bagGrid) {
		this.bagGrid = bagGrid;
	}
	public String getBageitems() {
		return bageitems;
	}
	public void setBageitems(String bageitems) {
		this.bageitems = bageitems;
	}
	public List<List<Integer>> getBageitemList() {
		return bageitemList;
	}
	public void setBageitemList(List<List<Integer>> bageitemList) {
		this.bageitemList = bageitemList;
	}
	public String getInitSkills() {
		return initSkills;
	}
	public void setInitSkills(String initSkills) {
		this.initSkills = initSkills;
	}
	public List<Integer> getInitSkillList() {
		return initSkillList;
	}
	public void setInitSkillList(List<Integer> initSkillList) {
		this.initSkillList = initSkillList;
	}
	public int getStone() {
		return stone;
	}
	public void setStone(int stone) {
		this.stone = stone;
	}
	public int getMaxTradeGrid() {
		return maxTradeGrid;
	}
	public void setMaxTradeGrid(int maxTradeGrid) {
		this.maxTradeGrid = maxTradeGrid;
	}
	public int getMoveSpeed() {
		return moveSpeed;
	}
	public void setMoveSpeed(int moveSpeed) {
		this.moveSpeed = moveSpeed;
	}
 
}
