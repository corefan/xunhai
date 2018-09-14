package com.dao.epigraph;

import java.util.List;

import com.db.GameSqlSessionTemplate;
import com.domain.epigraph.PlayerWeaponEffect;


/**
 * 武器铭文效果
 * @author jiangqin
 * @date 2017-7-18
 */
public class PlayerWeaponEffectDAO extends GameSqlSessionTemplate{
	/**
	 * 创建玩家武器效果数据
	 * */
	public void createPlayerWeaponEffect(PlayerWeaponEffect playerWeaponEffect) {
		this.insert_noreturn(playerWeaponEffect.getInsertSql());
	}

	/**
	 * 取玩家铭文效果列表
	 */
	public List<PlayerWeaponEffect> listBaseEpigraph (long playerId){
		String sql = "select * from player_weapon_effect WHERE playerId="+playerId;
		return this.selectList(sql, PlayerWeaponEffect.class);
	}	
	
}
