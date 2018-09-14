package com.domain.bag;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.domain.GameEntity;
import com.util.SplitStringUtil;



/**
 * 玩家装备
 * @author ken
 * @date 2017-1-5
 */
public class PlayerEquipment extends GameEntity {

	
	private static final long serialVersionUID = -7319665105789751938L;
	
	/** 装备自增编号 */
	private long playerEquipmentId;
	/** 玩家编号 */
	private long playerId;
	/** 装备基础编号 */
	private Integer equipmentId;
	/** 装备部位 */
	private int equipType;
	/** 是否绑定(1:是 0：否) */
	private int isBinding;
	/** 状态(0: 已删除  1:背包 2:穿戴 4:售卖中 ) */
	private int state = 1;
	/** 删除时间 */
	private Date deleteTime;
	/** 孔位数*/
	private int holeNum;	
	/** 装备附加属性*/
	private String addAttr;	
	private Map<Integer, Integer> addAttrMap = new HashMap<Integer, Integer>();
	
	/** 评分*/
	private int score;
	/** 强化等级*/
	private int strongLv;
	
	public String getInsertSql() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO player_equipment ");
		sql.append("(playerEquipmentId, playerId, equipmentId, equipType, isBinding, state, deleteTime, holeNum, addAttr,score) VALUES"); //
		sql.append(" (");
		sql.append(playerEquipmentId);
		sql.append(",");
		sql.append(playerId);
		sql.append(",");
		sql.append(equipmentId);
		sql.append(",");
		sql.append(equipType);
		sql.append(",");
		sql.append(isBinding);
		sql.append(",");
		sql.append(state);
		sql.append(",");
		if (deleteTime == null) {
			sql.append(deleteTime);
		} else {
			sql.append("'");
			sql.append(new Timestamp(deleteTime.getTime()));
			sql.append("'");
		}
		sql.append(",");		
		sql.append(holeNum);
		sql.append(",");	
		if (addAttr == null) {
			sql.append(addAttr);
		}else{
			sql.append("'");
			sql.append(addAttr);
			sql.append("'");
		}		
		sql.append(",");		
		sql.append(score);
		sql.append(")");
		
		return sql.toString();
	}
	
	/**
	 * 得到更新sql
	 * */
	public String getUpdateSql() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE player_equipment SET ");
		sql.append("playerId = ");
		sql.append(playerId);
		sql.append(",");
		sql.append("equipmentId = ");
		sql.append(equipmentId);
		sql.append(",");
		sql.append("equipType = ");
		sql.append(equipType);
		sql.append(",");
		sql.append("isBinding = ");
		sql.append(isBinding);
		sql.append(",");
		sql.append("state = ");
		sql.append(state);
		sql.append(",");
		sql.append(" deleteTime = ");
		if (deleteTime == null) {
			sql.append(deleteTime);
		} else {
			sql.append("'");
			sql.append(new Timestamp(deleteTime.getTime()));
			sql.append("'");
		}
		sql.append(",");
		sql.append("holeNum = ");		
		sql.append(holeNum);	
		sql.append(",");
		sql.append("addAttr = ");	
		if (addAttr == null) {
			sql.append(addAttr);
		}else{
			sql.append("'");
			sql.append(addAttr);
			sql.append("'");
		}				
		sql.append(",");
		sql.append("score = ");		
		sql.append(score);	
		sql.append(",");
		sql.append("strongLv = ");		
		sql.append(strongLv);
		sql.append(" WHERE playerEquipmentId = ");
		sql.append(playerEquipmentId);
		
		return sql.toString();
	}

	public long getPlayerEquipmentId() {
		return playerEquipmentId;
	}

	public void setPlayerEquipmentId(long playerEquipmentId) {
		this.playerEquipmentId = playerEquipmentId;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public Integer getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(Integer equipmentId) {
		this.equipmentId = equipmentId;
	}

	public int getEquipType() {
		return equipType;
	}

	public void setEquipType(int equipType) {
		this.equipType = equipType;
	}

	public int getIsBinding() {
		return isBinding;
	}

	public void setIsBinding(int isBinding) {
		this.isBinding = isBinding;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	public int getHoleNum() {
		return holeNum;
	}

	public void setHoleNum(int holeNum) {
		this.holeNum = holeNum;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getStrongLv() {
		return strongLv;
	}

	public void setStrongLv(int strongLv) {
		this.strongLv = strongLv;
	}

	public String getAddAttr() {
		return addAttr;
	}

	public void setAddAttr(String addAttr) {
		this.addAttr = addAttr;
		
		Map<Integer, Integer> map = SplitStringUtil.getIntIntMap(addAttr);
		if(map != null){
			this.addAttrMap = map;
		}else{
			this.addAttrMap.clear();
		}
	}

	public Map<Integer, Integer> getAddAttrMap() {
		return addAttrMap;
	}

	public void setAddAttrMap(Map<Integer, Integer> addAttrMap) {
		this.addAttrMap = addAttrMap;
		
		this.addAttr = SplitStringUtil.getStringByIntIntMap(addAttrMap);
	}

}
