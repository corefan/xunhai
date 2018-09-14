package com.domain.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.constant.SceneConstant;
import com.domain.Position;
import com.domain.Reward;

/**
 * 地图表
 * @author ken
 * @date 2016-12-30
 */
public class BaseMap implements Serializable {

	private static final long serialVersionUID = -4243440028468452618L;

	/** 地图编号*/
	private int map_id;
	/** 地图类型 1：主城 2：野外 3：大荒塔*/
	private int mapType;
	/** 地图名字*/
	private String map_name;
	/** 地图模式  1：和平  2：安全  3：pk*/
	private int pkModel;
	/** 开启等级*/
	private int openLevel;
	/** 副本类型*/
	private int openTask;
	/** 玩家上限数量*/
	private int playernum;
	/** 最大进入次数*/
	private int maxCount;
	/** 存在时间*/
	private int lifeTime;
	/** 销毁等待时间*/
	private int waitingTime;
	
	/** 出生点*/
	private List<Position> revivePositions;
	/** 地图布置的怪物*/
	private List<MonsterInfo> monsterInfos;	
	/** 地图布置的采集物*/
	private List<Integer> collectIds = new ArrayList<Integer>();
	/** 地图总行数*/
	private int mapRow;
	/** 地图总列数*/
	private int mapColumn;
	/** 地图网格*/
	private int[][] blocks;
	/** 区域格子*/
	private Map<Integer, List<Integer>> gridMap;
	/** 区域格子总列数*/
	private int colNum;
	/** 进入副本消耗*/
	private String expendStr;
	/** 进入副本消耗列表*/
	private List<Reward> expendList;
	/** 副本奖励类型*/
	private int rewardType;
	
	/** 是否使用九宫格*/
	private boolean nineGrid;
	
	/**
	 * 坐标是否在阻挡块上
	 */
	public boolean isBlock(int x, int z){
		if(this.blocks == null){
			System.out.println("the blocks is null with mapId is "+map_id);
		}
		int a =(int) Math.ceil(z * 1.0 / SceneConstant.CELL_SIZE);
		if(a > this.blocks.length) return true;
		
		int[] arr = this.blocks[a - 1];
		
		int b =(int) Math.ceil(x * 1.0 / SceneConstant.CELL_SIZE);
		if(b > arr.length) return true;
		
		return arr[b - 1] == SceneConstant.IS_BLOAK ? true : false;
	}
	
	/**
	 * 是否在副本中
	 */
	public boolean isInstance(){
		return this.mapType > SceneConstant.WORLD_SCENE;
	}
	
	
	/**
	 * 是否任务副本
	 */
	public boolean isTaskInstance(){
		return this.mapType == SceneConstant.INSTANCE_SCENE && this.openTask > 0;
	}
	
	/**
	 * 是否在野外地图
	 */
	public boolean isWorldScene(){
		return this.mapType == SceneConstant.WORLD_SCENE;
	}
	
	/**
	 * 野外pk地图
	 */
	public boolean isWorldSceneForPK(){
		return this.mapType == SceneConstant.WORLD_SCENE && this.pkModel == SceneConstant.PK_MODEL_PK;
	}
	
	/**
	 * 幻境地图
	 */
	public boolean isHuanjing(){
		return this.map_id >= 12101 && this.map_id <= 12115;
	}
	
	/**
	 * 神境仙域地图
	 */
	public boolean isShenjing(){
		return this.map_id >= 12001 && this.map_id <= 12009;	
	}
	
	/**
	 * 城战地图
	 */
	public boolean isGuild(){
		return this.mapType == SceneConstant.GUILD_SCENE;	
	}
	
	public int getMap_id() {
		return map_id;
	}
	public void setMap_id(int map_id) {
		this.map_id = map_id;
	}
	public int getMapType() {
		return mapType;
	}
	public void setMapType(int mapType) {
		this.mapType = mapType;
	}
	public String getMap_name() {
		return map_name;
	}
	public void setMap_name(String map_name) {
		this.map_name = map_name;
	}

	public List<Position> getRevivePositions() {
		return revivePositions;
	}

	public void setRevivePositions(List<Position> revivePositions) {
		this.revivePositions = revivePositions;
	}

	public List<MonsterInfo> getMonsterInfos() {
		return monsterInfos;
	}

	public void setMonsterInfos(List<MonsterInfo> monsterInfos) {
		this.monsterInfos = monsterInfos;
	}

	public int[][] getBlocks() {
		return blocks;
	}

	public void setBlocks(int[][] blocks) {
		this.blocks = blocks;
	}

	public int getMapRow() {
		return mapRow;
	}

	public void setMapRow(int mapRow) {
		this.mapRow = mapRow;
	}

	public int getMapColumn() {
		return mapColumn;
	}

	public void setMapColumn(int mapColumn) {
		this.mapColumn = mapColumn;
	}

	public int getPkModel() {
		return pkModel;
	}

	public void setPkModel(int pkModel) {
		this.pkModel = pkModel;
	}

	public int getOpenLevel() {
		return openLevel;
	}

	public void setOpenLevel(int openLevel) {
		this.openLevel = openLevel;
	}

	public int getOpenTask() {
		return openTask;
	}

	public void setOpenTask(int openTask) {
		this.openTask = openTask;
	}

	public int getPlayernum() {
		return playernum;
	}

	public void setPlayernum(int playernum) {
		this.playernum = playernum;
	}

	public int getLifeTime() {
		return lifeTime;
	}

	public void setLifeTime(int lifeTime) {
		this.lifeTime = lifeTime;
	}

	public int getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(int waitingTime) {
		this.waitingTime = waitingTime;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public List<Integer> getCollectIds() {
		return collectIds;
	}

	public void setCollectIds(List<Integer> collectIds) {
		this.collectIds = collectIds;
	}

	public Map<Integer, List<Integer>> getGridMap() {
		return gridMap;
	}

	public void setGridMap(Map<Integer, List<Integer>> gridMap) {
		this.gridMap = gridMap;
	}

	public int getColNum() {
		return colNum;
	}

	public void setColNum(int colNum) {
		this.colNum = colNum;
	}

	public String getExpendStr() {
		return expendStr;
	}

	public void setExpendStr(String expendStr) {
		this.expendStr = expendStr;
	}

	public List<Reward> getExpendList() {
		return expendList;
	}

	public void setExpendList(List<Reward> expendList) {
		this.expendList = expendList;
	}

	public int getRewardType() {
		return rewardType;
	}

	public void setRewardType(int rewardType) {
		this.rewardType = rewardType;
	}

	public boolean isNineGrid() {
		return nineGrid;
	}

	public void setNineGrid(boolean nineGrid) {
		this.nineGrid = nineGrid;
	}

}
