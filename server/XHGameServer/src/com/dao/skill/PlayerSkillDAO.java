package com.dao.skill;

import java.util.List;

import com.db.GameSqlSessionTemplate;
import com.domain.skill.PlayerSkill;

/**
 * 玩家技能dao
 * @author ken
 * @date 2017-2-6
 */
public class PlayerSkillDAO extends GameSqlSessionTemplate {

	/**
	 * 创建玩家技能
	 * */
	public void createPlayerSkill(PlayerSkill playerSkill) {
		this.insert_noreturn(playerSkill.getInsertSql());		
	}
	
	/**
	 * 获得玩家技能列表
	 * */
	public List<PlayerSkill> listPlayerSkills(long playerId) {
		String sql = "SELECT * FROM player_skill  WHERE playerId="+playerId;

		return this.selectList(sql, PlayerSkill.class);
	}
	
}
