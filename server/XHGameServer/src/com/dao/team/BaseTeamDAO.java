package com.dao.team;

import java.util.List;

import com.db.BaseSqlSessionTemplate;
import com.domain.team.BaseTeam;

/**
 * 组队DAO 
 * @author ken
 * @date 2017-3-2
 */
public class BaseTeamDAO extends BaseSqlSessionTemplate {

	private static final String sql = "select * from teamtarget";
	
	/**
	 * 取组队配置表
	 */
	public List<BaseTeam> listBaseTeams(){
		return this.selectList(sql, BaseTeam.class);
	}
}
