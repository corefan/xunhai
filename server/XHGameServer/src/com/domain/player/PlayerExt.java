package com.domain.player;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.domain.GameEntity;
import com.domain.buff.Buff;


/**
 * 玩家扩展信息
 */
public class PlayerExt extends GameEntity {

	private static final long serialVersionUID = -4053469940745010421L;
	
	/** 玩家编号 */
	private long playerId;
	/** 最近登录时间 */
	private Date loginTime;
	/** 最近登录ip */
	private String loginIP;
	/** 最近离线时间 */
	private Date exitTime;
	/** 武器样式 */
	private int weaponStyle;
	/** 服装样式 */
	private int dressStyle;
	/** 翅膀样式 */
	private int wingStyle;
	/** 所在线路 */
	private int line;
	/** 所在地图 */
	private int mapId;
	/** 坐标x */
	private int x;
	/** 坐标y */
	private int y;
	/** 坐标z */
	private int z;
	
	/** 方向*/
	private int direction;
	
	/** 所在地图 */
	private int lastMapId;
	/** 坐标x */
	private int lastX;
	/** 坐标y */
	private int lastY;
	/** 坐标z */
	private int lastZ;
	
	/** 当前血量 */
	private int hp;
	/** 当前魔法 */
	private int mp; 
	
	/** 背包格子数*/
	private int bagGrid;	
	
	/** 当前所在场景*/
	private String sceneGuid;
	
	/** pk模式*/
	private int pkMode = 1;
	/** pk值*/
	private int pkVlaue;
	/** 名字颜色*/
	private int nameColor = 1;
	
	/** 组队编号*/
	private int teamId;		

	/** 当前大荒塔层数*/
	private int curLayerId;
	/** 玩家货架*/
	private int tradeGridNum;
	
	/** 玩家剩余buff*/
	private Map<Integer, Buff> remainBuffMap = new ConcurrentHashMap<Integer, Buff>();
	
	/** 当前已接环任务次数*/
	private int weekTaskNum;
	
	/** 本周已接环任务次数*/
	private int weekTotalNum;
	
	/** 累计登陆天数*/
	private int addLoginDay;

	@Override
	public String getInsertSql() {
		return null;
	}
	
	/**
	 * 得到更新sql
	 * */
	public String getUpdateSql() {

		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE player_ext SET ");
		sql.append("loginTime = ");
		if(loginTime == null){
			sql.append(loginTime);
		}else{
			sql.append("'");
			sql.append(new Timestamp(loginTime.getTime()));
			sql.append("'");
		}
		sql.append(",");
		sql.append("loginIP = ");
		if(loginIP == null){
			sql.append(loginIP);
		}else{
			sql.append("'");
			sql.append(loginIP);
			sql.append("'");
		}
		sql.append(",");
		sql.append("exitTime = ");
		if(exitTime == null){
			sql.append(exitTime);
		}else{
			sql.append("'");
			sql.append(new Timestamp(exitTime.getTime()));
			sql.append("'");
		}
		sql.append(",");
		sql.append(" weaponStyle = ");
		sql.append(weaponStyle);
		sql.append(",");
		sql.append(" dressStyle = ");
		sql.append(dressStyle);
		sql.append(",");
		sql.append(" wingStyle = ");
		sql.append(wingStyle);
		sql.append(",");
		sql.append(" line = ");
		sql.append(line);
		sql.append(",");
		sql.append(" mapId = ");
		sql.append(mapId);
		sql.append(",");
		sql.append(" x = ");
		sql.append(x);
		sql.append(",");
		sql.append(" y = ");
		sql.append(y);
		sql.append(",");
		sql.append(" z = ");
		sql.append(z);
		sql.append(",");
		sql.append(" lastMapId = ");
		sql.append(lastMapId);
		sql.append(",");
		sql.append(" lastX = ");
		sql.append(lastX);
		sql.append(",");
		sql.append(" lastY = ");
		sql.append(lastY);
		sql.append(",");
		sql.append(" lastZ = ");
		sql.append(lastZ);
		sql.append(",");
		sql.append(" direction = ");
		sql.append(direction);
		sql.append(",");
		sql.append(" hp = ");
		sql.append(hp);
		sql.append(",");
		sql.append(" mp = ");
		sql.append(mp);
		sql.append(",");
		sql.append(" bagGrid = ");
		sql.append(bagGrid);
		sql.append(",");
		sql.append(" pkMode = ");
		sql.append(pkMode);
		sql.append(",");
		sql.append(" pkVlaue = ");
		sql.append(pkVlaue);
		sql.append(",");
		sql.append(" curLayerId = ");
		sql.append(curLayerId);
		sql.append(",");		
		sql.append(" tradeGridNum = ");
		sql.append(tradeGridNum);
		sql.append(",");		
		sql.append(" weekTaskNum = ");
		sql.append(weekTaskNum);
		sql.append(",");		
		sql.append(" weekTotalNum = ");
		sql.append(weekTotalNum);
		sql.append(",");		
		sql.append(" addLoginDay = ");
		sql.append(addLoginDay);
		sql.append(" WHERE playerId = ");
		sql.append(playerId);

		return sql.toString();
	}


	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getMp() {
		return mp;
	}

	public void setMp(int mp) {
		this.mp = mp;
	}
	

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Date getExitTime() {
		return exitTime;
	}

	public void setExitTime(Date exitTime) {
		this.exitTime = exitTime;
	}

	public int getWeaponStyle() {
		return weaponStyle;
	}

	public void setWeaponStyle(int weaponStyle) {
		this.weaponStyle = weaponStyle;
	}

	public int getDressStyle() {
		return dressStyle;
	}

	public void setDressStyle(int dressStyle) {
		this.dressStyle = dressStyle;
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


	public int getBagGrid() {
		return bagGrid;
	}

	public void setBagGrid(int bagGrid) {
		this.bagGrid = bagGrid;
	}

	public String getSceneGuid() {
		return sceneGuid;
	}

	public void setSceneGuid(String sceneGuid) {
		this.sceneGuid = sceneGuid;
	}

	public int getPkMode() {
		return pkMode;
	}

	public void setPkMode(int pkMode) {
		this.pkMode = pkMode;
	}

	public int getPkVlaue() {
		return pkVlaue;
	}

	public void setPkVlaue(int pkVlaue) {
		this.pkVlaue = pkVlaue;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public int getNameColor() {
		return nameColor;
	}

	public void setNameColor(int nameColor) {
		this.nameColor = nameColor;
	}

	public int getLastMapId() {
		return lastMapId;
	}

	public void setLastMapId(int lastMapId) {
		this.lastMapId = lastMapId;
	}

	public int getLastX() {
		return lastX;
	}

	public void setLastX(int lastX) {
		this.lastX = lastX;
	}

	public int getLastY() {
		return lastY;
	}

	public void setLastY(int lastY) {
		this.lastY = lastY;
	}

	public int getLastZ() {
		return lastZ;
	}

	public void setLastZ(int lastZ) {
		this.lastZ = lastZ;
	}

	public int getCurLayerId() {
		return curLayerId;
	}

	public void setCurLayerId(int curLayerId) {
		if(curLayerId < 1){
			curLayerId = 1;
		}
		if(curLayerId > 9999){
			curLayerId = 9999;
		}
		this.curLayerId = curLayerId;
	}

	public Map<Integer, Buff> getRemainBuffMap() {
		return remainBuffMap;
	}

	public void setRemainBuffMap(Map<Integer, Buff> remainBuffMap) {
		this.remainBuffMap = remainBuffMap;
	}

	public int getWeekTaskNum() {
		return weekTaskNum;
	}

	public void setWeekTaskNum(int weekTaskNum) {
		this.weekTaskNum = weekTaskNum;
	}

	public int getWingStyle() {
		return wingStyle;
	}

	public void setWingStyle(int wingStyle) {
		this.wingStyle = wingStyle;
	}

	public int getTradeGridNum() {
		return tradeGridNum;
	}

	public void setTradeGridNum(int tradeGridNum) {
		this.tradeGridNum = tradeGridNum;
	}

	public int getWeekTotalNum() {
		return weekTotalNum;
	}

	public void setWeekTotalNum(int weekTotalNum) {
		this.weekTotalNum = weekTotalNum;
	}

	public int getAddLoginDay() {
		return addLoginDay;
	}

	public void setAddLoginDay(int addLoginDay) {
		this.addLoginDay = addLoginDay;
	}

	public String getLoginIP() {
		return loginIP;
	}

	public void setLoginIP(String loginIP) {
		this.loginIP = loginIP;
	}

}
