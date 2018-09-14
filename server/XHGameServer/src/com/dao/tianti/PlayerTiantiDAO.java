package com.dao.tianti;

import java.util.List;

import com.db.GameSqlSessionTemplate;
import com.domain.tianti.PlayerTianti;

/**
 * 天梯dao
 * @author ken
 * @date 2017-4-14
 */
public class PlayerTiantiDAO extends GameSqlSessionTemplate {

	/**
	 * 根据编号取玩家天梯
	 */
	public PlayerTianti getPlayerTianti(long playerId){
		String sql = "select * from player_tianti where playerId = "+playerId;
		return this.selectOne(sql, PlayerTianti.class);
	}
	
	/**
	 * 200名玩家排序
	 */
	public List<PlayerTianti> listRankPlayerTianti(){
		String sql = "select * from player_tianti where winNum != 0 order by score DESC, updateTime ASC LIMIT 200";
		return this.selectList(sql, PlayerTianti.class);
	}
	
	/**
	 * 全部记录排序
	 */
	public List<PlayerTianti> listAllRankPlayerTianti(){
		String sql = "select * from player_tianti where winNum != 0 order by score DESC, updateTime ASC ";
		return this.selectList(sql, PlayerTianti.class);
	}
	
	/**
	 * 赛季结束重置
	 */
	public void resetAll(){
		String sql = "update player_tianti set rank = 0, killNum = 0, deadNum = 0, stage = 1, star = 1, score = 1000, winNum = 0, updateTime = 0, rewardStageStr = null";
		this.update(sql);
	}
	
	/**
	 * 删除天梯记录
	 */
	public void deletePlayerTianti(long playerId){
		String sql = "delete from player_tianti where playerId = "+playerId;
		this.update(sql);
	}
}
