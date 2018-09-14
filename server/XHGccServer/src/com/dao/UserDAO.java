package com.dao;

import java.util.List;

import com.db.GccSqlSessionTemplate;
import com.domain.Authority;
import com.domain.Role;
import com.domain.RoleAuthority;
import com.domain.User;

/**
 * @author ken
 * 2014-3-10
 * 用户dao
 */
public class UserDAO extends GccSqlSessionTemplate {

	/**
	 * 创建权限
	 * */
	public void createAuthority(Authority authority) {
		String sql = authority.getInsertSql();
		int authorityID = this.insert(sql).intValue();
		authority.setAuthorityID(authorityID);
	}

	/**
	 * 修改权限
	 * */
	public void updateAuthority(Authority authority) {
		this.update(authority.getUpdateSql());
	}
	
	/**
	 * 修改权限
	 * */
	public void deleteAuthority(int authorityID) {
		this.delete("DELETE FROM t_authority WHERE AUTHORITY_ID="+authorityID);
	}
	
	/**
	 * 创建角色权限
	 * */
	public void createRoleAuthority(RoleAuthority roleAuthority) {
		int id = this.insert(roleAuthority.getInsertSql()).intValue();
		roleAuthority.setRoleAuthorityID(id);
	}
	
	/**
	 * 删除角色权限
	 * */
	public void deleteRoleAuthority(int roleID) {
		this.delete("DELETE FROM t_role_authority WHERE ROLE_ID="+roleID);
	}
	
	/**
	 * 创建角色
	 * */
	public void createRole(Role role) {
		int roleID = this.insert(role.getInsertSql()).intValue();
		role.setRoleID(roleID);
	}
	
	/**
	 * 更新角色
	 * */
	public void updateRole(Role role) {
		this.update(role.getUpdateSql());
	}
	
	/**
	 * 删除角色
	 * */
	public void deleteRole(int roleID) {
		this.delete("DELETE FROM t_role WHERE ROLE_ID="+roleID);
	}
	
	
	/**
	 * 创建用户
	 * */
	public void createUser(User user) {
		int userID = this.insert(user.getInsertSql()).intValue();
		user.setUserID(userID);
	}
	
	/**
	 * 更新用户
	 * */
	public void updateUser(User user) {
		this.update(user.getUpdateSql());
	}
	
	/**
	 * 删除用户
	 * */
	public void deleteUser(int userID) {
		this.delete("DELETE FROM t_user_gcc WHERE USER_ID = "+userID);
	}
	
	/**
	 * 得到角色列表
	 * */
	public List<Role> getRoleList() {
		String sql = "SELECT ROLE_ID AS roleID, NAME AS name FROM t_role";
		return this.selectList(sql, Role.class);
	}
	
	/**
	 * 得到权限列表
	 * */
	public List<Authority> getAuthorityList() {
		String sql = "SELECT AUTHORITY_ID AS authorityID, NAME AS name, FUNCTION_TYPE as functionType, FUNCTION_NAME AS functionName, TYPE AS type FROM t_authority";
		return this.selectList(sql, Authority.class);
	}
	
	/**
	 * 得到角色权限列表
	 * */
	public List<Integer> getRoleAuthorityList(int roleID) {
		String sql = "SELECT AUTHORITY_ID AS authorityID FROM t_role_authority WHERE ROLE_ID = "+roleID;
		return this.selectList(sql, null);
	}
	
	/**
	 * 得到用户列表
	 * */
	public List<User> getUserList() {
		String sql = "SELECT USER_ID AS userID, USER_NAME AS userName, AGENT AS agent, PASSWORD AS password, SITE AS site, ROLE_ID AS roleID, STATE AS state, DELETE_FLAG AS deleteFlag FROM t_user_gcc";
		return this.selectList(sql, User.class);
	}
	
}
