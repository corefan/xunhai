package com.dao;

import java.util.List;

import com.db.DAOTemplate;
import com.domain.BaseGameStep;
import com.domain.ServerConf;

public class BaseDataDAO extends DAOTemplate {
	

	private static final String GET_SERVER_CONF_LIST = "SELECT * FROM config_server";

	/**
	 * 得到服务器列表
	 * */
	public List<ServerConf> getServerConfList() {
		return jdbcTempldate.queryForBeanList(GET_SERVER_CONF_LIST, ServerConf.class);
	}

	/**
	 * 得到主线任务节点
	 */
	public List<BaseGameStep> getBaseGameSteps(){
		String sql = "SELECT id, taskName FROM xh_base.task WHERE type = 1 and reward IS NOT NULL";
		return jdbcTempldate.queryForBeanList(sql, BaseGameStep.class);
	}
}
