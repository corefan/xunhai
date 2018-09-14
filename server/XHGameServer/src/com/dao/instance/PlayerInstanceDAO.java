package com.dao.instance;

import java.util.List;

import com.db.GameSqlSessionTemplate;
import com.domain.instance.PlayerInstance;

/**
 * 副本dao
 * @author ken
 * @date 2017-3-8
 */
public class PlayerInstanceDAO extends GameSqlSessionTemplate {

	/**
	 * 创建玩家副本
	 * */
	public void createPlayerInstance(PlayerInstance model) {
		this.insert_noreturn(model.getInsertSql());
	}
	
	/**
	 * 获得玩家副本列表
	 * */
	public List<PlayerInstance> listPlayerInstances(long playerId) {
		String sql = "SELECT * FROM player_instance  WHERE playerId="+playerId;
		return this.selectList(sql, PlayerInstance.class);
	}
	
	/**
	 * 日结重置次数
	 */
	public void quarztDaily(){
		String sql = "UPDATE player_instance SET  enterCount = 0";
		this.update(sql);
	}
}
