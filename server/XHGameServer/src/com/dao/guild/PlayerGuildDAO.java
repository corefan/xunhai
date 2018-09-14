package com.dao.guild;

import java.util.List;

import com.db.GameSqlSessionTemplate;
import com.domain.guild.Guild;
import com.domain.guild.GuildBuy;
import com.domain.guild.GuildFight;
import com.domain.guild.GuildWar;
import com.domain.guild.PlayerGuild;
import com.domain.guild.Union;

/**
 * 帮派DAO
 * @author ken
 * @date 2018年4月2日
 */
public class PlayerGuildDAO extends GameSqlSessionTemplate {

	/**
	 * 创建帮派
	 */
	public void createGuild(Guild guild) {
		this.insert_noreturn(guild.getInsertSql());
	}	
	
	/**
	 * 获取帮派列表
	 */
	public List<Guild> listGuilds(){		
		String sql = "SELECT * FROM guild  WHERE deleteFlag = 0";
		return this.selectList(sql, Guild.class);
	}	
	
	/**
	 * 创建宣战
	 */
	public void createGuildWar(GuildWar guildWar) {
		this.insert_noreturn(guildWar.getInsertSql());
	}
	
	/**
	 * 宣战列表
	 */
	public List<GuildWar> listGuildWars(){
		String sql = "SELECT * FROM guild_war  WHERE deleteFlag = 0";
		return this.selectList(sql, GuildWar.class);
	}
	
	/**
	 * 定时清理无效数据
	 */
	public void quartzDelete(){		
		this.delete("DELETE FROM guild WHERE deleteFlag = 1");
		this.delete("DELETE FROM guild_war WHERE deleteFlag = 1");
		this.update("UPDATE guild_buy SET buyNum = 0");
	}	
	
	/**
	 * 周定时更新
	 */
	public void weekQuartz(){
		String sql = "update player_guild set weekMoney = 0, weekBuildNum = 0";
		this.update(sql);
	}
	
	/**
	 * 创建玩家帮派信息
	 */
	public void createPlayerGuild(PlayerGuild playerGuild){
		this.insert_noreturn(playerGuild.getInsertSql());
	}
	
	/**
	 * 获取玩家帮派信息
	 */
	public PlayerGuild getPlayerGuild(long playerId){
		String sql = "SELECT * FROM player_guild WHERE playerId="+playerId;
		return this.selectOne(sql, PlayerGuild.class);
	}	
	
	/**
	 * 获取玩家帮派信息
	 */
	public List<PlayerGuild> listPlayerGuild(long guildId){
		String sql = "SELECT * FROM player_guild WHERE guildId="+guildId;
		return this.selectList(sql, PlayerGuild.class);
	}
	
	/**
	 * 创建联盟
	 */
	public void createGuildUnion(Union union){
		this.insert_noreturn(union.getInsertSql());
	}
	
	/**
	 * 获取联盟列表
	 */
	public List<Union> listUnions(){		
		String sql = "SELECT * FROM guild_union";
		return this.selectList(sql, Union.class);
	}
	
	/**
	 * 清理联盟记录
	 */
	public void deleteUnions(){
		this.delete("DELETE FROM guild_union");
	}
	
	/**
	 * 创建城战数据
	 */
	public void createGuildFight(GuildFight guildFight){
		this.insert_noreturn(guildFight.getInsertSql());
	}
	
	/**
	 * 获取城战数据
	 */
	public GuildFight getGuildFight(){
		String sql = "SELECT * FROM guild_fight";
		return this.selectOne(sql, GuildFight.class);
	}
	
	/**
	 * 优惠购买记录
	 */
	public List<GuildBuy> listGuildBuys(){
		String sql = "SELECT * FROM guild_buy";
		return this.selectList(sql, GuildBuy.class);
	}
	
	/**
	 * 创建优惠购买记录
	 */
	public void createGuildBuy(GuildBuy guildBuy){
		this.insert_noreturn(guildBuy.getInsertSql());
	}
}
