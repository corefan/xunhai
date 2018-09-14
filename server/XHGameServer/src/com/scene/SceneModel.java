package com.scene;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.domain.battle.DropItemInfo;
import com.domain.battle.WigSkillInfo;
import com.domain.collect.Collect;
import com.domain.monster.RefreshMonsterInfo;
import com.domain.puppet.BeckonPuppet;
import com.domain.puppet.MonsterPuppet;
import com.domain.puppet.PlayerPuppet;
import com.domain.tianti.BaseDropItem;

/**
 * 场景
 * @author ken
 * @date 2017-2-27
 */
public class SceneModel implements Serializable {

	private static final long serialVersionUID = -8914612956763223214L;

	/** 加入锁 */
	private Object lock = new Object();
	
	/** 场景自增编号*/
	private int sceneId;
	
	/** 场景唯一编号   type_id*/
	private String sceneGuid;
	/** 地图编号*/
	private int mapId;
	/** 地图名称*/
	private String mapName;
	/** 地图线路*/
	private int line;
	/** 地图类型*/
	private int mapType;
	/** 存在时间*/
	private int lifeTime;
	/** 销毁等待时间*/
	private int waitingTime;
	/** 副本状态*/
	private int sceneState;
	
	/** 副本结束时间搓*/
	private long endTime;
	
	/** 当前怪物波数*/
	private int curMonNum;
	
	/** 是否开始执行怪物ai*/
	private boolean execAIFlag = true;
	
	/** 执行掉落时间*/
	private long dropItemTime;
	
	/** 区域格子*/
	private Map<Integer, List<Integer>> gridMap;
	/** 区域格子总列数*/
	private int colNum;
	/** 地图总行数*/
	private int mapRow;
	/** 地图总列数*/
	private int mapColumn;
	
	/** 区域场景玩家列表   */
	private Map<Integer, Map<String, PlayerPuppet>> playerPuppetMap = new ConcurrentHashMap<Integer,Map<String, PlayerPuppet>>();
	
	/** 区域场景怪物列表   */
	private Map<Integer, Map<String, MonsterPuppet>> monsterPuppetMap = new ConcurrentHashMap<Integer, Map<String, MonsterPuppet>>();
	
	/** 区域场景召唤怪列表   */
	private Map<Integer, Map<String, BeckonPuppet>> beckonPuppetMap = new ConcurrentHashMap<Integer, Map<String, BeckonPuppet>>();
	
	/** 玩家编号列表   */
	private Map<Integer, List<Long>> playerIdMap= new ConcurrentHashMap<Integer, List<Long>>();

	/** 刷怪列表 */
	private Map<Integer, RefreshMonsterInfo> refMonsterMap = new ConcurrentHashMap<Integer, RefreshMonsterInfo>();
	private BlockingQueue<MonsterPuppet> deadList = new LinkedBlockingQueue<MonsterPuppet>();
	
	/** 掉落列表 */
	private Map<Integer, Map<Integer, DropItemInfo>> dropItemMap = new ConcurrentHashMap<Integer, Map<Integer, DropItemInfo>>();
	/** 掉落移除列表 */
	private List<Integer> removeDrops = new ArrayList<Integer>();
	
	/** 地效持续技能*/
	private Map<Integer, BlockingQueue<WigSkillInfo>> wigSkillMap = new ConcurrentHashMap<Integer, BlockingQueue<WigSkillInfo>>();
	
	/** 采集信息列表*/
	private Map<Integer, Map<Integer, Collect>> collectMap = new ConcurrentHashMap<Integer, Map<Integer, Collect>>();	
	/** 高级采集信息刷新列表*/
	private List<Collect> collectList  = new ArrayList<Collect>();
	
	/** 地图掉落*/
	private List<BaseDropItem> baseDropItems = new ArrayList<BaseDropItem>();
	
	/** 已掉落过的掉落配置下标*/
	private int index;
	
	/** 已刷完的pkBuff*/
	private List<Integer> pkBuff = new ArrayList<Integer>();
	
	/**
	 * 重置
	 */
	public void clear(){
		endTime = 0;
		lifeTime = 0;
		waitingTime = 0;
		curMonNum = 0;
		execAIFlag = true;
		index = 0;
		playerPuppetMap.clear();
		monsterPuppetMap.clear();
		beckonPuppetMap.clear();
		playerIdMap.clear();
		refMonsterMap.clear();
		deadList.clear();
		dropItemMap.clear();
		wigSkillMap.clear();
		removeDrops.clear();
		collectMap.clear();
		collectList.clear();
		pkBuff.clear();		
	}

	public String getSceneGuid() {
		return sceneGuid;
	}

	public void setSceneGuid(String sceneGuid) {
		this.sceneGuid = sceneGuid;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public Map<Integer, Map<String, PlayerPuppet>> getPlayerPuppetMap() {
		return playerPuppetMap;
	}

	public void setPlayerPuppetMap(
			Map<Integer, Map<String, PlayerPuppet>> playerPuppetMap) {
		this.playerPuppetMap = playerPuppetMap;
	}

	public Map<Integer, Map<String, MonsterPuppet>> getMonsterPuppetMap() {
		return monsterPuppetMap;
	}

	public void setMonsterPuppetMap(
			Map<Integer, Map<String, MonsterPuppet>> monsterPuppetMap) {
		this.monsterPuppetMap = monsterPuppetMap;
	}

	public Map<Integer, List<Long>> getPlayerIdMap() {
		return playerIdMap;
	}

	public void setPlayerIdMap(Map<Integer, List<Long>> playerIdMap) {
		this.playerIdMap = playerIdMap;
	}

	public Map<Integer, RefreshMonsterInfo> getRefMonsterMap() {
		return refMonsterMap;
	}

	public void setRefMonsterMap(Map<Integer, RefreshMonsterInfo> refMonsterMap) {
		this.refMonsterMap = refMonsterMap;
	}

	public Map<Integer, Map<Integer, DropItemInfo>> getDropItemMap() {
		return dropItemMap;
	}

	public void setDropItemMap(Map<Integer, Map<Integer, DropItemInfo>> dropItemMap) {
		this.dropItemMap = dropItemMap;
	}

	public Map<Integer, BlockingQueue<WigSkillInfo>> getWigSkillMap() {
		return wigSkillMap;
	}

	public void setWigSkillMap(Map<Integer, BlockingQueue<WigSkillInfo>> wigSkillMap) {
		this.wigSkillMap = wigSkillMap;
	}

	public int getMapType() {
		return mapType;
	}

	public void setMapType(int mapType) {
		this.mapType = mapType;
	}
	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
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

	public int getSceneState() {
		return sceneState;
	}

	public void setSceneState(int sceneState) {
		this.sceneState = sceneState;
	}

	public List<Integer> getRemoveDrops() {
		return removeDrops;
	}

	public void setRemoveDrops(List<Integer> removeDrops) {
		this.removeDrops = removeDrops;
	}

	public int getSceneId() {
		return sceneId;
	}

	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}

	public int getCurMonNum() {
		return curMonNum;
	}

	public void setCurMonNum(int curMonNum) {
		this.curMonNum = curMonNum;
	}

	public Map<Integer, Map<Integer, Collect>> getCollectMap() {
		return collectMap;
	}

	public void setCollectMap(Map<Integer, Map<Integer, Collect>> collectMap) {
		this.collectMap = collectMap;
	}

	public List<Collect> getCollectList() {
		return collectList;
	}

	public void setCollectList(List<Collect> collectList) {
		this.collectList = collectList;
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

	public BlockingQueue<MonsterPuppet> getDeadList() {
		return deadList;
	}

	public void setDeadList(BlockingQueue<MonsterPuppet> deadList) {
		this.deadList = deadList;
	}

	public Map<Integer, Map<String, BeckonPuppet>> getBeckonPuppetMap() {
		return beckonPuppetMap;
	}

	public void setBeckonPuppetMap(Map<Integer, Map<String, BeckonPuppet>> beckonPuppetMap) {
		this.beckonPuppetMap = beckonPuppetMap;
	}

	public boolean isExecAIFlag() {
		return execAIFlag;
	}

	public void setExecAIFlag(boolean execAIFlag) {
		this.execAIFlag = execAIFlag;
	}

	public Object getLock() {
		return lock;
	}

	public void setLock(Object lock) {
		this.lock = lock;
	}
	
	public long getDropItemTime() {
		return dropItemTime;
	}

	public void setDropItemTime(long dropItemTime) {
		this.dropItemTime = dropItemTime;
	}	

	public List<BaseDropItem> getBaseDropItems() {
		return baseDropItems;
	}

	public void setBaseDropItems(List<BaseDropItem> baseDropItems) {
		this.baseDropItems = baseDropItems;
	}

	public List<Integer> getPkBuff() {
		return pkBuff;
	}

	public void setPkBuff(List<Integer> pkBuff) {
		this.pkBuff = pkBuff;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
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

}
