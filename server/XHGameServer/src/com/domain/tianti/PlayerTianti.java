package com.domain.tianti;

import java.util.ArrayList;
import java.util.List;

import com.domain.GameEntity;
import com.util.SplitStringUtil;

/**
 * 天梯记录
 * @author ken
 * @date 2017-4-14
 */
public class PlayerTianti extends GameEntity {

	private static final long serialVersionUID = 1508677318452351271L;

	/** 玩家编号*/
	private long playerId;
	/** 排名*/
	private int rank;
	/** 击杀数*/
	private int killNum;
	/** 死亡数*/
	private int deadNum;
	/** 段位*/
	private int stage;
	/** 星级*/
	private int star;
	/** 积分*/
	private int score;
	/** 连胜连败次数*/
	private int winNum;
	
	/** 变动时间*/
	private long updateTime;
	
	/** 匹配积分区间*/
	private int minMatchScore;
	private int maxMatchScore;
	/** 匹配时间*/
	private long matchTime;
	
	/** 玩家已领取段位奖励记录*/
	private String rewardStageStr;
	private List<Integer> rewardStageList = new ArrayList<Integer>();
	
	@Override
	public String getInsertSql() {
		return null;
	}
	
	/**
	 * 得到更新sql
	 * */
	public String getUpdateSql() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE player_tianti SET ");
		sql.append("rank = ");
		sql.append(rank);
		sql.append(",");
		sql.append("killNum = ");
		sql.append(killNum);
		sql.append(",");
		sql.append("deadNum = ");
		sql.append(deadNum);
		sql.append(",");
		sql.append("stage = ");
		sql.append(stage);
		sql.append(",");
		sql.append("star = ");
		sql.append(star);
		sql.append(",");
		sql.append("score = ");
		sql.append(score);
		sql.append(",");
		sql.append("winNum = ");
		sql.append(winNum);
		sql.append(",");
		sql.append("updateTime = ");
		sql.append(updateTime);
		sql.append(",");
		sql.append("rewardStageStr = ");
		if(rewardStageStr == null){
			sql.append(rewardStageStr);	
		}else{
			sql.append("'");
			sql.append(rewardStageStr);
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

	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public int getKillNum() {
		return killNum;
	}
	public void setKillNum(int killNum) {
		this.killNum = killNum;
	}
	public int getDeadNum() {
		return deadNum;
	}
	public void setDeadNum(int deadNum) {
		this.deadNum = deadNum;
	}
	public int getStage() {
		return stage;
	}
	public void setStage(int stage) {
		this.stage = stage;
	}
	public int getStar() {
		return star;
	}
	public void setStar(int star) {
		this.star = star;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		if(score < 0) score = 0;
		this.score = score;
	}

	public int getWinNum() {
		return winNum;
	}

	public void setWinNum(int winNum) {
		this.winNum = winNum;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public int getMinMatchScore() {
		return minMatchScore;
	}

	public void setMinMatchScore(int minMatchScore) {
		if(minMatchScore < 0) minMatchScore = 0;
		this.minMatchScore = minMatchScore;
	}

	public int getMaxMatchScore() {
		return maxMatchScore;
	}

	public void setMaxMatchScore(int maxMatchScore) {
		this.maxMatchScore = maxMatchScore;
	}

	public long getMatchTime() {
		return matchTime;
	}

	public void setMatchTime(long matchTime) {
		this.matchTime = matchTime;
	}

	public List<Integer> getRewardStageList() {
		return rewardStageList;
	}

	public void setRewardStageList(List<Integer> rewardStageList) {
		this.rewardStageList = rewardStageList;
		
		this.rewardStageStr = this.rewardStageList.toString();
	}

	public String getRewardStageStr() {
		return rewardStageStr;
	}

	public void setRewardStageStr(String rewardStageStr) {
		this.rewardStageStr = rewardStageStr;
		
		List<Integer> list  = SplitStringUtil.getIntList(this.rewardStageStr);
		if(list != null){
			this.rewardStageList = list;
		}else{
			this.rewardStageList.clear();
		}
	}

}
