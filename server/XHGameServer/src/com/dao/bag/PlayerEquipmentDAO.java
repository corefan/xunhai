package com.dao.bag;

import java.util.List;

import com.db.GameSqlSessionTemplate;
import com.domain.bag.PlayerEquipment;

/**
 * 玩家装备DAO
 * @author ken
 * @date 2017-1-4
 */
public class PlayerEquipmentDAO extends GameSqlSessionTemplate {
	/**
	 * 创建玩家装备
	 */
	public void createPlayerEquipment(PlayerEquipment playerEquipment) {
		this.insert_noreturn(playerEquipment.getInsertSql());
	}
	
	/**
	 * 调度删除玩家装备数据
	 * */
	public void quartzDeletePlayerEquipment() {
		this.delete("DELETE FROM player_equipment WHERE state = 0 AND deleteTime is not null AND DATE_SUB(NOW(),INTERVAL 7 DAY) > deleteTime");
	}
	
	/**
	 * 获得玩家装备列表
	 */
	public List<PlayerEquipment> getPlayerEquipmentList(long playerId) {
		String sql = "SELECT * FROM player_equipment  WHERE playerId="+playerId+" AND state > 0";

		return this.selectList(sql, PlayerEquipment.class);
	}
	
	
	/**
	 * 取所有装备
	 */
	public List<PlayerEquipment> getPlayerEquipmentStateList() {
		String sql = "SELECT * FROM player_equipment  ";

		return this.selectList(sql, PlayerEquipment.class);
	}
}
