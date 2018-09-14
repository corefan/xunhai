package com.dao;

import java.util.List;

import com.db.DAOTemplate;
import com.domain.User;

public class UserDAO extends DAOTemplate {

	/**
	 * 得到用户列表
	 * */
	public List<User> getUserList() {
		String sql = "SELECT USER_ID AS userID, USER_NAME AS userName, PASSWORD AS password, AGENT AS agent, SITE AS site, ROLE_ID AS roleID, STATE AS state, DELETE_FLAG AS deleteFlag FROM t_user_dataanalysis";
		return jdbcTempldate.queryForBeanList(sql, User.class);
	}
}
