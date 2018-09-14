package com.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.common.CacheService;
import com.common.DateService;
import com.common.GCCContext;
import com.constant.CacheConstant;
import com.dao.UserDAO;
import com.domain.Authority;
import com.domain.OptLog;
import com.domain.Role;
import com.domain.RoleAuthority;
import com.domain.User;
import com.service.ILogService;
import com.service.IUserService;

/**
 * @author ken
 * 2014-3-10
 * 用户service
 */
public class UserService implements IUserService {

	private UserDAO userDAO = new UserDAO();
	
	public void initUserCache() {
		
		List<Authority> authorityList = userDAO.getAuthorityList();
		List<Role> roleList = userDAO.getRoleList();
		List<User> userList = userDAO.getUserList();
		
		
		Map<Integer,List<Integer>> roleAuthorityMap = new HashMap<Integer, List<Integer>>();
		for (Role role : roleList) {
			List<Integer> roleAuthorityList = userDAO.getRoleAuthorityList(role.getRoleID());
			roleAuthorityMap.put(role.getRoleID(), roleAuthorityList);
		}
		
		CacheService.putToCache(CacheConstant.AUTHORITY_CACHE, authorityList);
		CacheService.putToCache(CacheConstant.ROLE_CACHE, roleList);
		CacheService.putToCache(CacheConstant.USER_CACHE, userList);
		CacheService.putToCache(CacheConstant.ROLE_ID_AUTHORITY_IDLIST, roleAuthorityMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> getAuthorityIDListByRoleID(int roleID) {
		Map<Integer,List<Integer>> roleAuthorityMap = (Map<Integer, List<Integer>>) CacheService.getFromCache(CacheConstant.ROLE_ID_AUTHORITY_IDLIST);
		return roleAuthorityMap.get(roleID);
	}
	
	@SuppressWarnings("unchecked")
	public List<User> getUserList() {
		return (List<User>) CacheService.getFromCache(CacheConstant.USER_CACHE);
	}
	
	public User getUserbyID(int userID) {
		List<User> userList = getUserList();
		for (User user : userList) {
			if (user.getUserID() == userID) {
				return user;
			}
		}
		return null;
	}
	
	public User getUserbyUserName(String userName) {
		List<User> userList = getUserList();
		for (User user : userList) {
			if (user.getUserName().equals(userName)) {
				return user;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Role> getRoleList() {
		return (List<Role>) CacheService.getFromCache(CacheConstant.ROLE_CACHE);
	}
	
	public Role getRolebyName(String name) {
		List<Role> roleList = getRoleList();
		for (Role role : roleList) {
			if (role.getName().equals(name)) {
				return role;
			}
		}
		return null;
	}
	
	public Role getRolebyID(int roleID) {
		List<Role> roleList = getRoleList();
		for (Role role : roleList) {
			if (role.getRoleID() == roleID) {
				return role;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Authority> getAuthorityList() {
		return (List<Authority>) CacheService.getFromCache(CacheConstant.AUTHORITY_CACHE);
	}
	
	public Authority getAuthoritybyName(String name) {
		List<Authority> list = getAuthorityList();
		for (Authority auth : list) {
			if (auth.getName().equals(name)) {
				return auth;
			}
		}
		return null;
	}
	
	public Authority getAuthoritybyID(int id) {
		List<Authority> list = getAuthorityList();
		for (Authority auth : list) {
			if (auth.getAuthorityID() == id) {
				return auth;
			}
		}
		return null;
	}
	
	public User login(String userName, String password) {
		
		User loginUser = null;
		
			List<User> userList  = getUserList();
			for (User user : userList) {
				if (user.getUserName().equals(userName) && user.getPassword().equals(password)) {
					loginUser = user;
					break;
				}
			}
		
		return loginUser;
		
	}

	@Override
	public User createUser(String userName, String password, int roleID) {
		
		User user = getUserbyUserName(userName);
		if (user != null) return null;
		
		user = new User();
		user.setUserName(userName);
		user.setPassword(password);
		user.setRoleID(roleID);
		user.setState(1);
		user.setSite("qdgame");
		user.setDeleteFlag(0);
		
		userDAO.createUser(user);
		
		getUserList().add(user);
		
		return user;
	}

	@Override
	public User updateUser(int userID, String userName, String password, int roleID) {
		
		User user = getUserbyID(userID);
		if (user == null) return null;
		
		User user2 = getUserbyUserName(userName);
		if (user2 != null && user2.getUserID() != userID) {
			return null;
		}
		
		if (password == null || "".equals(password.trim())) return null;
		
		user.setUserName(userName);
		user.setPassword(password);
		user.setRoleID(roleID);
		
		userDAO.updateUser(user);
		
		return user;
	}

	@Override
	public void deleteUser(int userID) {
		User user = getUserbyID(userID);
		if (user == null) return ;
		
		userDAO.deleteUser(userID);
		
		getUserList().remove(user);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Role createRole(String name, String authorityIDs) {
		
		Role role = getRolebyName(name);
		if (role != null) return null;
		
		role = new Role();
		role.setName(name);
		
		userDAO.createRole(role);
		
		List<Integer> authorityIDList = new ArrayList<Integer>();
		String[] auths = authorityIDs.split(",");
		for (String id : auths) {
			RoleAuthority ra = new RoleAuthority();
			ra.setRoleID(role.getRoleID());
			ra.setAuthorityID(Integer.parseInt(id));
			
			userDAO.createRoleAuthority(ra);
			
			authorityIDList.add(Integer.parseInt(id));
		}
		
		getRoleList().add(role);
		
		Map<Integer,List<Integer>> roleAuthorityMap = (Map<Integer, List<Integer>>) CacheService.getFromCache(CacheConstant.ROLE_ID_AUTHORITY_IDLIST);
		roleAuthorityMap.put(role.getRoleID(), authorityIDList);
		
		
		return role;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Role updateRole(int roleID, String name, String authorityIDs) {
		
		Role role = getRolebyID(roleID);
		Role role2 = getRolebyName(name);
		if (role2 != null && role.getRoleID() != role2.getRoleID()) return null;
		
		role.setName(name);
		userDAO.updateRole(role);
		
		userDAO.deleteRoleAuthority(roleID);
		
		List<Integer> authorityIDList = new ArrayList<Integer>();
		
		String[] auths = authorityIDs.split(",");
		for (String id : auths) {
			RoleAuthority ra = new RoleAuthority();
			ra.setRoleID(role.getRoleID());
			ra.setAuthorityID(Integer.parseInt(id));
			
			userDAO.createRoleAuthority(ra);
			
			authorityIDList.add(Integer.parseInt(id));
		}
		
		Map<Integer,List<Integer>> roleAuthorityMap = (Map<Integer, List<Integer>>) CacheService.getFromCache(CacheConstant.ROLE_ID_AUTHORITY_IDLIST);
		roleAuthorityMap.put(role.getRoleID(), authorityIDList);
		
		return role;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteRole(int roleID) {
		
		Role role = getRolebyID(roleID);
		if (role == null) return;
		
		userDAO.deleteRole(roleID);
		userDAO.deleteRoleAuthority(roleID);
		
		getRoleList().remove(role);
		
		Map<Integer,List<Integer>> roleAuthorityMap = (Map<Integer, List<Integer>>) CacheService.getFromCache(CacheConstant.ROLE_ID_AUTHORITY_IDLIST);
		roleAuthorityMap.remove(roleID);
	}

	@Override
	public Authority createAuthority(String name) {
		
		Authority auth = getAuthoritybyName(name);
		if (auth != null) return null;
		
		auth = new Authority();
		auth.setName(name);
		
		userDAO.createAuthority(auth);
		
		getAuthorityList().add(auth);
		
		return auth;
	}

	@Override
	public Authority updateAuthority(int authorityID, String name) {
		
		Authority authority = getAuthoritybyID(authorityID);
		
		Authority auth = getAuthoritybyName(name);
		
		if (auth != null && authority.getAuthorityID() != auth.getAuthorityID()) return null;
		
		authority.setName(name);
		userDAO.updateAuthority(authority);
		
		return authority;
	}

	@Override
	public void deleteAuthority(int authorityID) {
		
		Authority authority = getAuthoritybyID(authorityID);
		if (authority == null) return;
		
		userDAO.deleteAuthority(authorityID);
		
		getAuthorityList().remove(authority);
		
		
	}
	
	@Override
	public List<String > gameSiteUrl(String gameSite, String agent){
		List<String> gameSiteList = new ArrayList<String>();
		if(gameSite == null || "".equals(gameSite.trim())){
			Map<String, List<String>> agentMap = GCCContext.getInstance().getServiceCollection().getBaseDataService().getAgentMap();
			if(agentMap != null){
				String [] str = agent.split(",");
				for(String s:str){
					List<String> list = agentMap.get(s);
					if(list!=null) gameSiteList.addAll(list);
				}
				return gameSiteList;
			}
		}else{
			gameSiteList.add(gameSite);
		}
		
		return gameSiteList;
	}
	
	
	public void recodeLoginLog(int opt, int userID, String userName, String content, String detail, String optIP){
		ILogService logService = GCCContext.getInstance().getServiceCollection().getLogService();
		
		OptLog optLog = new OptLog();
		optLog.setCreateTime(DateService.getCurrentUtilDate());
		optLog.setOpt(opt);
		optLog.setOptIP(optIP);
		optLog.setUserID(userID);
		optLog.setUserName(userName);
		optLog.setContent(content);
		optLog.setDetail(detail);
		
		logService.insert(userID, optLog);
	}
	
}
