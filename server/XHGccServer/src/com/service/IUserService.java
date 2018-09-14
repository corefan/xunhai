package com.service;

import java.util.List;

import com.domain.Authority;
import com.domain.Role;
import com.domain.User;

/**
 * @author ken
 * 2014-3-10
 * 用户service接口
 */
public interface IUserService {

	/**
	 * 初始化用户缓存
	 * */
	public void initUserCache();
	
	/**
	 * 根据角色得到权限列表
	 * */
	public List<Integer> getAuthorityIDListByRoleID(int roleID);
	
	public List<User> getUserList();
	
	public User getUserbyID(int userID);
	
	public User getUserbyUserName(String userName);
	
	public List<Role> getRoleList();
	
	public Role getRolebyName(String name);
	
	public Role getRolebyID(int roleID);
	
	public List<Authority> getAuthorityList();
	
	public Authority getAuthoritybyName(String name);
	
	public Authority getAuthoritybyID(int id);
	
	/**
	 * 登陆
	 * */
	public User login(String userName, String password);
	
	/**
	 * 创建用户
	 * */
	public User createUser(String userName, String password, int roleID);
	
	/**
	 * 更新用户
	 * */
	public User updateUser(int userID, String userName, String password, int roleID);
	
	/**
	 * 删除用户
	 * */
	public void deleteUser(int userID);
	
	/**
	 * 创建角色
	 * */
	public Role createRole(String name, String authorityIDs);
	
	/**
	 * 更新角色
	 * */
	public Role updateRole(int roleID, String name, String authorityIDs);
	
	/**
	 * 删除角色
	 * */
	public void deleteRole(int roleID);
	
	/**
	 * 创建权限
	 * */
	public Authority createAuthority(String name);
	
	/**
	 * 更新权限
	 * */
	public Authority updateAuthority(int authorityID, String name);
	
	/**
	 * 删除权限
	 * */
	public void deleteAuthority(int authorityID);
	
	public List<String > gameSiteUrl(String gameSite, String agent);
	
	/** 
	 * 记录登陆日志
	 **/
	public void recodeLoginLog(int opt, int userID, String userName, String content, String detail, String optIP);

}
