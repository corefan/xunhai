package com.dao.rank;

import java.util.List;

import com.db.GameSqlSessionTemplate;
import com.domain.rank.BattleValueRank;
import com.domain.rank.EquipRank;
import com.domain.rank.GoldRank;

/**
 * 排行榜dao
 * @author ken
 * @date 2017-5-8
 */
public class RankDAO extends GameSqlSessionTemplate {

	/**
	 * 战力榜
	 */
	public List<BattleValueRank> listBattleValueRanks(int career, int tatalNum){
		String careerSelect = "";
		if(career > 0){
			careerSelect = " AND p.career = " + career;
		}
		String sql = "SELECT p.playerId, p.playerName, p.career, pp.level, pp.battleValue AS value FROM player p LEFT JOIN player_property pp " +
				"ON p.playerId=pp.playerId WHERE p.deleteFlag = 0 " + careerSelect + 
				" AND pp.battleValue > 0" +
				" ORDER BY value DESC, p.playerId ASC LIMIT "+tatalNum;
		
		return this.selectList(sql, BattleValueRank.class);
	}
	
	/**
	 * 装备榜
	 */
	public List<EquipRank> listEquipRanks(int career, int tatalNum){
		String careerSelect = "";
		if(career > 0){
			careerSelect = " AND p.career = " + career;
		}
		String sql = "SELECT p.playerId, p.playerName, p.career, pp.level, SUM(pe.score) value FROM player_equipment pe LEFT JOIN player p" +
				" ON pe.playerId = p.playerId LEFT JOIN player_property pp" +
				" ON pe.playerId = pp.playerId WHERE p.deleteFlag = 0 " + careerSelect + 
				" AND pe.state = 2 AND pe.deleteTime IS NULL" +
				" GROUP BY pe.playerId ORDER BY value DESC, p.playerId ASC LIMIT "+tatalNum;
		
		return this.selectList(sql, EquipRank.class);
	}
	
	/**
	 * 财富榜
	 */
	public List<GoldRank> listGoldRanks(int career, int tatalNum){
		String careerSelect = "";
		if(career > 0){
			careerSelect = " AND p.career = " + career;
		}
		
		String sql = "SELECT p.playerId, p.playerName, p.career, pp.level, pw.gold AS value FROM player_wealth pw LEFT JOIN player p" +
				" ON pw.playerId = p.playerId LEFT JOIN player_property pp" +
				" ON pw.playerId = pp.playerId WHERE p.deleteFlag = 0 " + careerSelect + 
				" AND pw.gold > 0" +
				" ORDER BY value DESC, p.playerId ASC LIMIT "+tatalNum;
		
		return this.selectList(sql, GoldRank.class);
	}
}
