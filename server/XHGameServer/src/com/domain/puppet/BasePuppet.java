package com.domain.puppet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.constant.BattleConstant;
import com.domain.buff.Buff;

/**
 * 场景单位基础类 （1：npc 2:玩家  3：怪物）
 * @author ken
 * @date 2016-12-28
 */
public class BasePuppet extends AbstractPuppet{

	private static final long serialVersionUID = -8650055259215070811L;
	

	private Object useSkillLock = new Object();
	
	private Object lifeLock = new Object(); 
	
	/** 全局编号*/
	private String guid;
	/** 实体编号*/
	private long eid;
	/** 单位名称*/
	private String name;
	/** 单位类型*/
	private int type; 
	/** 等级*/
	private int level;
	/** 外形*/
	private int dressStyle;

	/** 所在场景*/
	private String sceneGuid;
	/** 线路*/
	private int line;
	/** 地图编号*/
	private int mapId;
	/** 区域格子编号*/
	private int gridId;
	/** 坐标 横向*/
	private int x;
	/** 坐标 高度*/
	private int y;
	/** 坐标 纵向*/
	private int z;
	/** 方向*/
	private int direction;
	
	/** pk模式*/
	private int pkMode;
	
	/**0中立   1：进攻方  2：防守方 */
	private int pkType;
	
	/** 单位移动状态*/
	private PuppetState puppetState;
	
	/** 战斗状态 @BattleConstant*/
	private int state = BattleConstant.STATE_NORMAL;
	/** 上次攻击时间 */
	private long attackTime;
	
//	/** 释放技能队列 */
//	private BlockingQueue<SkillInfo> releaseSkillQueue = new LinkedBlockingQueue<SkillInfo>();
	
	/** 敌人列表*/
	private Map<String, EnemyModel> enemyMap = new ConcurrentHashMap<String, EnemyModel>();
	
	/** buff列表*/
	private Map<Integer, Buff> buffMap = new ConcurrentHashMap<Integer, Buff>();
	
	/** 免疫别人施加的buff*/	
	private int immuneDebuff;
	
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public long getEid() {
		return eid;
	}
	public void setEid(long eid) {
		this.eid = eid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getDressStyle() {
		return dressStyle;
	}
	public void setDressStyle(int dressStyle) {
		this.dressStyle = dressStyle;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		if(x <= 0){
			System.out.println("guid="+ guid+" x="+x);
			return;
		}
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
		if(z <= 0){
			System.out.println("guid="+ guid+" z="+z);
			return;
		}
		this.z = z;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public PuppetState getPuppetState() {
		return puppetState;
	}
	public void setPuppetState(PuppetState puppetState) {
		this.puppetState = puppetState;
	}
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	public int getMapId() {
		return mapId;
	}
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
//	public BlockingQueue<SkillInfo> getReleaseSkillQueue() {
//		return releaseSkillQueue;
//	}
//	public void setReleaseSkillQueue(BlockingQueue<SkillInfo> releaseSkillQueue) {
//		this.releaseSkillQueue = releaseSkillQueue;
//	}
	public Object getUseSkillLock() {
		return useSkillLock;
	}
	public void setUseSkillLock(Object useSkillLock) {
		this.useSkillLock = useSkillLock;
	}
	public long getAttackTime() {
		return attackTime;
	}
	public void setAttackTime(long attackTime) {
		this.attackTime = attackTime;
	}
	public Map<String, EnemyModel> getEnemyMap() {
		return enemyMap;
	}
	public void setEnemyMap(Map<String, EnemyModel> enemyMap) {
		this.enemyMap = enemyMap;
	}
	public String getSceneGuid() {
		return sceneGuid;
	}
	public void setSceneGuid(String sceneGuid) {
		this.sceneGuid = sceneGuid;
	}
	public int getGridId() {
		return gridId;
	}
	public void setGridId(int gridId) {
		if(gridId < 1){
			System.out.println("setGridId is <0 "+gridId);
			gridId = 1;
		}
		this.gridId = gridId;
	}
	public Map<Integer, Buff> getBuffMap() {
		return buffMap;
	}
	public void setBuffMap(Map<Integer, Buff> buffMap) {
		this.buffMap = buffMap;
	}
	public int getPkMode() {
		return pkMode;
	}
	public void setPkMode(int pkMode) {
		this.pkMode = pkMode;
	}
	public int getImmuneDebuff() {
		return immuneDebuff;
	}
	public void setImmuneDebuff(int immuneDebuff) {
		this.immuneDebuff = immuneDebuff;
	}
	public Object getLifeLock() {
		return lifeLock;
	}
	public void setLifeLock(Object lifeLock) {
		this.lifeLock = lifeLock;
	}
	public int getPkType() {
		return pkType;
	}
	public void setPkType(int pkType) {
		this.pkType = pkType;
	}

}
