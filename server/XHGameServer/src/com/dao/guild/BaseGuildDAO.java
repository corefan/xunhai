package com.dao.guild;

import java.util.List;

import com.db.BaseSqlSessionTemplate;
import com.domain.guild.BaseGuild;
import com.domain.guild.BaseGuildBuy;
import com.domain.guild.BaseGuildDonate;
import com.domain.guild.BaseGuildSkill;

/**
 * 帮派基础表配置
 * @author ken
 * @date 2018年4月3日
 */
public class BaseGuildDAO extends BaseSqlSessionTemplate {

	/**
	 *帮派基础表
	 */
	public List<BaseGuild> listBaseGuilds(){
		String fuseSql = "select * from guild";
		return this.selectList(fuseSql, BaseGuild.class);
	}
	
	/**
	 *捐献基础表
	 */
	public List<BaseGuildDonate> listBaseGuildDonates(){
		String fuseSql = "select * from guilddonate";
		return this.selectList(fuseSql, BaseGuildDonate.class);
	}
	
	/**
	 *帮派技能基础表
	 */
	public List<BaseGuildSkill> listBaseGuildSkills(){
		String sql = "select * from guildskill";
		return this.selectList(sql, BaseGuildSkill.class);
	}
	
	/**
	 *帮派优惠购买基础表
	 */
	public List<BaseGuildBuy> listBaseGuildBuys(){
		String sql = "select * from guildbuy";
		return this.selectList(sql, BaseGuildBuy.class);
	}
}
