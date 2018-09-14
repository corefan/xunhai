package com.domain.activity;

import java.util.ArrayList;
import java.util.List;

import com.domain.GameEntity;
import com.util.SplitStringUtil;

/**
 * 玩家
 * @author ken
 * @date 2017-10-25
 */
public class PlayerTomb extends GameEntity {

	private static final long serialVersionUID = 8569254132009772081L;

	/** 玩家编号*/
	private long playerId;
	
	/** 抽取的奖励池*/	
	private String tombIdStr;
	private List<Integer> tombIdList = new ArrayList<Integer>();
	
	/** 已领取的奖励*/
	private String usedTombIdStr;
	private List<Integer> usedTombIdList = new ArrayList<Integer>();

	/** 已完成陵墓数*/
	private int tombNum;
	/** 绿色宝数*/
	private int greenNum;
	/** 蓝色宝数*/
	private int blueNum;
	/** 紫色宝数*/
	private int violetNum;
	/** 橙色宝数*/
	private int orangeNum;
	
	public String getInsertSql() {
		
		StringBuilder sql = new StringBuilder(1 << 8);
		
		sql.append("INSERT INTO player_tomb ");
		sql.append("(playerId, tombIdStr, usedTombIdStr, tombNum, greenNum, blueNum, violetNum, orangeNum) VALUES");
		sql.append(" (");
		sql.append(playerId);
		sql.append(",");
		if(tombIdStr == null){
			sql.append(tombIdStr);	
		}else{
			sql.append("'");
			sql.append(tombIdStr);
			sql.append("'");
		}
		sql.append(",");
		if(usedTombIdStr == null){
			sql.append(usedTombIdStr);	
		}else{
			sql.append("'");
			sql.append(usedTombIdStr);
			sql.append("'");
		}
		sql.append(",");
		sql.append(tombNum);
		sql.append(",");
		sql.append(greenNum);
		sql.append(",");
		sql.append(blueNum);
		sql.append(",");
		sql.append(violetNum);
		sql.append(",");
		sql.append(orangeNum);
		sql.append(")");
		
		return sql.toString();
	}
	
	/**
	 * 得到更新sql
	 * */
	public String getUpdateSql() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE player_tomb SET ");
		sql.append("tombIdStr = ");
		if(tombIdStr == null){
			sql.append(tombIdStr);	
		}else{
			sql.append("'");
			sql.append(tombIdStr);
			sql.append("'");
		}
		sql.append(",");
		sql.append("usedTombIdStr = ");
		if(usedTombIdStr == null){
			sql.append(usedTombIdStr);	
		}else{
			sql.append("'");
			sql.append(usedTombIdStr);
			sql.append("'");
		}	
		sql.append(",");
		sql.append("tombNum = ");
		sql.append(tombNum);
		sql.append(",");
		sql.append("greenNum = ");
		sql.append(greenNum);
		sql.append(",");
		sql.append("blueNum = ");
		sql.append(blueNum);
		sql.append(",");
		sql.append("violetNum = ");
		sql.append(violetNum);
		sql.append(",");
		sql.append("orangeNum = ");
		sql.append(orangeNum);
		sql.append(" WHERE playerId = ");
		sql.append(playerId);
		
		return sql.toString();
	}
	
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public String getTombIdStr() {
		return tombIdStr;
	}
	public void setTombIdStr(String tombIdStr) {
		this.tombIdStr = tombIdStr;
		
		List<Integer> list = SplitStringUtil.getIntList(tombIdStr);
		if(list != null){
			this.tombIdList = list;
		}else{
			this.tombIdList.clear();
		}
	}
	public List<Integer> getTombIdList() {
		return tombIdList;
	}
	public void setTombIdList(List<Integer> tombIdList) {
		this.tombIdList = tombIdList;
		
		this.tombIdStr = tombIdList.toString();
	}
	public String getUsedTombIdStr() {
		return usedTombIdStr;
	}
	public void setUsedTombIdStr(String usedTombIdStr) {
		this.usedTombIdStr = usedTombIdStr;
		
		List<Integer> list = SplitStringUtil.getIntList(usedTombIdStr);
		if(list != null){
			this.usedTombIdList = list;
		}else{
			this.usedTombIdList.clear();
		}
	}
	public List<Integer> getUsedTombIdList() {
		return usedTombIdList;
	}
	public void setUsedTombIdList(List<Integer> usedTombIdList) {
		this.usedTombIdList = usedTombIdList;
		
		this.usedTombIdStr = usedTombIdList.toString();
	}

	public int getTombNum() {
		return tombNum;
	}

	public void setTombNum(int tombNum) {
		this.tombNum = tombNum;
	}

	public int getGreenNum() {
		return greenNum;
	}

	public void setGreenNum(int greenNum) {
		this.greenNum = greenNum;
	}

	public int getBlueNum() {
		return blueNum;
	}

	public void setBlueNum(int blueNum) {
		this.blueNum = blueNum;
	}

	public int getVioletNum() {
		return violetNum;
	}

	public void setVioletNum(int violetNum) {
		this.violetNum = violetNum;
	}

	public int getOrangeNum() {
		return orangeNum;
	}

	public void setOrangeNum(int orangeNum) {
		this.orangeNum = orangeNum;
	}
	
}
