package com.domain.puppet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.constant.BattleConstant;
import com.domain.ai.Node;
import com.domain.monster.BaseAiDetermine;

/**
 * 怪物场景模型
 * @author ken
 * @date 2016-12-28
 */
public class MonsterPuppet extends BasePuppet {

	private static final long serialVersionUID = -4454859755012942064L;

	/** 刷怪配置编号*/
	private int refreshMonsterId;
	/** 怪物类型   (1普通小怪,2精英 3.BOSS,4只跑不打 5木桩类怪物)*/
	private int monsterType;
	/** 怪物攻击模式(1主动攻击, 2被动攻击 )*/
	private int evasiveStyle;
	/** 警戒距离半径*/
	private int warmRange;
	/** 追击距离*/
	private int pursuitRange;
	/** 巡逻半径*/
	private int patrolRange;
	/** 巡逻时间 毫秒*/
	private int patrolTime;
	/** 巡逻时间间隔上下限  毫秒*/
	private List<Integer> patrolTimeList;
	/** 出生点 */
	private int xBirth;
	/** 出生点 */
	private int yBirth;
	/** 出生点 */
	private int zBirth;
	/** ai组 */
	private List<BaseAiDetermine> aiList;
	
	/** 进入战斗的位置 */
	private int xBattle;
	/** 进入战斗的位置 */
	private int yBattle;
	/** 进入战斗的位置 */
	private int zBattle;
	
	/** 上次移动时间 */
	private long lastMoveTime;
	/** 上次更新坐标时间 */
	private long lastUpdatePosTime;
	/** 移动路线 */
	private List<Node> nodeList = new ArrayList<Node>();
	/** ai cd列表 */
	private Map<Integer, Long> aiCdMap = new ConcurrentHashMap<Integer, Long>();
	
	/** 怪物当前状态*/
	private int aiState = BattleConstant.AI_STATE_NORMAL;

	private BaseAiDetermine curAI;
	
	/** ai准备时间*/
	private long preAiTime;
	/** 死亡时间 */
	private long deadTime;
	/** 复活时间  <0 不刷新*/
	private int refreshTime;
	
	/** 敌人目标位置*/
	private int targetX;
	private int targetZ;
	
	/** 实际要走向的位置*/
	private int moveToX;
	private int moveToZ;
	
	public int getWarmRange() {
		return warmRange;
	}

	public void setWarmRange(int warmRange) {
		this.warmRange = warmRange;
	}

	public int getPursuitRange() {
		return pursuitRange;
	}

	public void setPursuitRange(int pursuitRange) {
		this.pursuitRange = pursuitRange;
	}

	public int getPatrolRange() {
		return patrolRange;
	}

	public void setPatrolRange(int patrolRange) {
		this.patrolRange = patrolRange;
	}

	public int getPatrolTime() {
		return patrolTime;
	}

	public void setPatrolTime(int patrolTime) {
		this.patrolTime = patrolTime;
	}

	public List<Integer> getPatrolTimeList() {
		return patrolTimeList;
	}

	public void setPatrolTimeList(List<Integer> patrolTimeList) {
		this.patrolTimeList = patrolTimeList;
	}

	public int getxBirth() {
		return xBirth;
	}

	public void setxBirth(int xBirth) {
		this.xBirth = xBirth;
	}

	public int getyBirth() {
		return yBirth;
	}

	public void setyBirth(int yBirth) {
		this.yBirth = yBirth;
	}

	public int getzBirth() {
		return zBirth;
	}

	public void setzBirth(int zBirth) {
		this.zBirth = zBirth;
	}

	public List<BaseAiDetermine> getAiList() {
		return aiList;
	}

	public void setAiList(List<BaseAiDetermine> aiList) {
		this.aiList = aiList;
	}

	public int getxBattle() {
		return xBattle;
	}

	public void setxBattle(int xBattle) {
		this.xBattle = xBattle;
	}

	public int getyBattle() {
		return yBattle;
	}

	public void setyBattle(int yBattle) {
		this.yBattle = yBattle;
	}

	public int getzBattle() {
		return zBattle;
	}

	public void setzBattle(int zBattle) {
		this.zBattle = zBattle;
	}

	public long getLastMoveTime() {
		return lastMoveTime;
	}

	public void setLastMoveTime(long lastMoveTime) {
		this.lastMoveTime = lastMoveTime;
	}

	public long getLastUpdatePosTime() {
		return lastUpdatePosTime;
	}

	public void setLastUpdatePosTime(long lastUpdatePosTime) {
		this.lastUpdatePosTime = lastUpdatePosTime;
	}

	public List<Node> getNodeList() {
		return nodeList;
	}

	public void setNodeList(List<Node> nodeList) {
		this.nodeList = nodeList;
	}

	public Map<Integer, Long> getAiCdMap() {
		return aiCdMap;
	}

	public void setAiCdMap(Map<Integer, Long> aiCdMap) {
		this.aiCdMap = aiCdMap;
	}

	public int getAiState() {
		return aiState;
	}

	public void setAiState(int aiState) {
		this.aiState = aiState;
	}

	public int getRefreshMonsterId() {
		return refreshMonsterId;
	}

	public void setRefreshMonsterId(int refreshMonsterId) {
		this.refreshMonsterId = refreshMonsterId;
	}

	public int getEvasiveStyle() {
		return evasiveStyle;
	}

	public void setEvasiveStyle(int evasiveStyle) {
		this.evasiveStyle = evasiveStyle;
	}

	public BaseAiDetermine getCurAI() {
		return curAI;
	}

	public void setCurAI(BaseAiDetermine curAI) {
		this.curAI = curAI;
	}

	public long getPreAiTime() {
		return preAiTime;
	}

	public void setPreAiTime(long preAiTime) {
		this.preAiTime = preAiTime;
	}

	public long getDeadTime() {
		return deadTime;
	}

	public void setDeadTime(long deadTime) {
		this.deadTime = deadTime;
	}

	public int getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(int refreshTime) {
		this.refreshTime = refreshTime;
	}

	public int getMonsterType() {
		return monsterType;
	}

	public void setMonsterType(int monsterType) {
		this.monsterType = monsterType;
	}

	public int getTargetX() {
		return targetX;
	}

	public int getTargetZ() {
		return targetZ;
	}

	public void setTargetX(int targetX) {
		this.targetX = targetX;
	}

	public void setTargetZ(int targetZ) {
		this.targetZ = targetZ;
	}

	public int getMoveToX() {
		return moveToX;
	}

	public void setMoveToX(int moveToX) {
		this.moveToX = moveToX;
	}

	public int getMoveToZ() {
		return moveToZ;
	}

	public void setMoveToZ(int moveToZ) {
		this.moveToZ = moveToZ;
	}

}
