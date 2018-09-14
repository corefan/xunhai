package com.dao.enemy;

import java.util.List;

import com.db.GameSqlSessionTemplate;
import com.domain.enemy.PlayerEnemy;

public class PlayerEnemyDAO extends GameSqlSessionTemplate{

	/** 创建仇敌信息*/
	public void createPlayerEnemy(PlayerEnemy playerEnemy) {
		this.insert_noreturn(playerEnemy.getInsertSql());
	}	
	
	/** 取玩家仇敌列表*/
	public List<PlayerEnemy> listPlayerEnemy (long playerId){
		String sql = "select * from player_enemy WHERE playerId="+playerId+"  and deleteFlag = 0";
		return this.selectList(sql, PlayerEnemy.class);
	}	
	
	/** 定时清理无效数据 */
	public void quartzDelete(){		
		this.delete("DELETE FROM player_enemy WHERE deleteFlag = 1");
	}
}
