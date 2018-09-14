package com.domain.sign;

import java.util.ArrayList;
import java.util.List;

import com.domain.GameEntity;
import com.util.SplitStringUtil;

/**
 * 玩家签到数据
 * @author jiangqin
 * @date 2017-4-22
 */
public class PlayerSign extends GameEntity {
	
	private static final long serialVersionUID = 7465022698283639690L;

	/** 玩家编号*/;
	private long playerId; 
	
	/** 已签到次数(包含补签次数)*/;
	private int signNum; 
	
	/** 已补签次数(仅用于计算补签消耗)*/;
	private int reSignNum; 
	
	/** 当天签到状态*/;
	private int state;
	
	/** 连续签到天数*/
	private int conSignDay;
	
	/** 已领取签到天数奖励子串*/
	private String conSignRewardStr;
	
	private List<Integer> conSignRewardList = new ArrayList<Integer>();
	
	public String getInsertSql() {
		
		StringBuilder sql = new StringBuilder(1 << 8);
		
		sql.append("INSERT INTO player_sign ");
		sql.append("(playerId, signNum, reSignNum, state, conSignDay, conSignRewardStr) VALUES");
		sql.append(" (");
		sql.append(playerId);
		sql.append(",");
		sql.append(signNum);
		sql.append(",");
		sql.append(reSignNum);
		sql.append(",");
		sql.append(state);		
		sql.append(",");
		sql.append(conSignDay);	
		sql.append(",");
		if(conSignRewardStr == null){
			sql.append(conSignRewardStr);	
		}else{
			sql.append("'");
			sql.append(conSignRewardStr);
			sql.append("'");
		}
		sql.append(")");
		
		return sql.toString();
	}
	
	/**
	 * 得到更新sql
	 * */
	public String getUpdateSql() {
		
		StringBuilder sql = new StringBuilder(1 << 8);
		
		sql.append("UPDATE player_sign SET ");
		sql.append("signNum = ");
		sql.append(signNum);
		sql.append(",");		
		sql.append("reSignNum = ");
		sql.append(reSignNum);
		sql.append(",");
		sql.append("state = ");
		sql.append(state);
		sql.append(",");
		sql.append("conSignDay = ");
		sql.append(conSignDay);		
		sql.append(",");
		sql.append("conSignRewardStr = ");
		if (conSignRewardStr == null) {
			sql.append(conSignRewardStr);
		} else {
			sql.append("'");
			sql.append(conSignRewardStr);
			sql.append("'");
		}
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

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getSignNum() {
		return signNum;
	}

	public void setSignNum(int signNum) {
		this.signNum = signNum;
	}

	public int getConSignDay() {
		return conSignDay;
	}

	public void setConSignDay(int conSignDay) {
		this.conSignDay = conSignDay;
	} 
	
	public int getReSignNum() {
		return reSignNum;
	}

	public void setReSignNum(int reSignNum) {
		this.reSignNum = reSignNum;
	}

	public String getConSignRewardStr() {
		return conSignRewardStr;
	}

	public void setConSignRewardStr(String conSignRewardStr) {
		this.conSignRewardStr = conSignRewardStr;
		
		List<Integer> list  = SplitStringUtil.getIntList(this.conSignRewardStr);
		if(list != null){
			this.conSignRewardList = list;
		}else{
			this.conSignRewardList.clear();
		}
	}

	public List<Integer> getConSignRewardList() {
		return conSignRewardList;
	}

	public void setConSignRewardList(List<Integer> conSignRewardList) {
		this.conSignRewardList = conSignRewardList;
		
		this.conSignRewardStr = this.conSignRewardList.toString();
	}
}
