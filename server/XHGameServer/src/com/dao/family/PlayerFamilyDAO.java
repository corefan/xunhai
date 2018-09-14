package com.dao.family;

import java.util.List;

import com.db.GameSqlSessionTemplate;
import com.domain.family.Family;
import com.domain.family.PlayerFamily;

/**
 * 玩家家族
 * @author jiangqin
 * @date 2017-4-6
 */
public class PlayerFamilyDAO extends GameSqlSessionTemplate{
	
	/**
	 * 获取玩家家族信息
	 */
	public PlayerFamily getPlayerFamily(long playerId){
		String sql = "SELECT * FROM player_family WHERE playerId="+playerId;
		return this.selectOne(sql, PlayerFamily.class);
	}	
	
	
	/**
	 * 获取玩家家族信息
	 */
	public List<PlayerFamily> listPlayerFamily(long playerFamilyId){
		String sql = "SELECT * FROM player_family WHERE playerFamilyId="+playerFamilyId;
		return this.selectList(sql, PlayerFamily.class);
	}
	
	
	/**
	 * 创建家族
	 */
	public void createFamily(Family family) {
		this.insert_noreturn(family.getInsertSql());
	}	
	
	/**
	 * 获取家族列表
	 */
	public List<Family> listFamilys(){		
		String familySql = "SELECT * FROM family  WHERE deleteFlag = 0";
		return this.selectList(familySql, Family.class);
	}	
	
	/**
	 * 定时清理无效数据
	 */
	public void quartzDelete(){		
		this.delete("DELETE FROM family WHERE deleteFlag = 1");
		this.update("UPDATE family SET openFB = 0");
	}	
	
	/**
	 *  调度解散家族
	 */
	public void quartzDisbandFamily(String familyIds){
		this.delete("DELETE FROM family WHERE playerFamilyId in "+familyIds);
		
		String sql2 = "UPDATE player_family SET playerFamilyId = 0, familyPosId = 0, familySortId = 0, " +
		              "familyTitle = null where playerFamilyId > 0 and playerFamilyId in "+familyIds;
         this.update(sql2);
         
	}
}
