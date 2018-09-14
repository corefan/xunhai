package com.command;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.common.CacheService;
import com.common.GCCContext;
import com.common.GameCCSocketService;
import com.constant.CacheConstant;
import com.constant.OptTypeConstant;
import com.core.Connection;
import com.domain.Authority;
import com.domain.MessageObj;
import com.domain.User;
import com.service.IBaseDataService;
import com.service.IUserService;

/**
 * 登陆
 * @author ken
 *
 */
public class LoginAction {
	
	GameCCSocketService gameCCSocketService = GCCContext.getInstance().getServiceCollection().getGameCCSocketService();
	
	/**
	 * 登陆游戏
	 */
	public void loginGame(MessageObj msgObj, Connection connection) throws Exception {
		
		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
		IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
		
		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));
		String userName = param.getString("userName");
		String password = param.getString("password");
		
		User user = userService.login(userName, password);
		
		JSONObject resultJson = new JSONObject();
		
		if(user == null){
			resultJson.put("loginFlag", 0);
		}else{
			int roleID = user.getRoleID();
			resultJson.put("loginFlag", 1);
			resultJson.put("userName", user.getUserName());
			resultJson.put("roleID", roleID);
			resultJson.put("authorityList", getRoleAuthorityList(roleID));
			resultJson.put("gameSiteList", baseDataService.getServerConfList());
			resultJson.put("agent", user.getAgent());
			
			connection.setUserID(user.getUserID());
			
			userService.recodeLoginLog(1, user.getUserID(), user.getUserName(), "用户登陆", user.getUserName() + "登陆了", connection.getHostAddress());
		}
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.LOGIN, resultJson.toString().getBytes("UTF8"));
		
		gameCCSocketService.sendData(connection, resultMsg);
			
		
	}
	
	/**
	 * 角色权限
	 */
	public String getRoleAuthorityList(int roleID) throws JSONException{
		
		JSONObject jsonObject = new JSONObject();
		List<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
		Map<Integer, List<Authority>> map = listConvertMap(getAuthority(roleID));
		
		
		Iterator<Integer> it = map.keySet().iterator();
		while (it.hasNext()) {
			JSONObject obj = new JSONObject();
			int type = it.next();
			obj.put("type", type);
			List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
			List<Authority> authorityList = map.get(type);
			for (Authority authority : authorityList) {
				JSONObject o = new JSONObject();
				o.put("type", authority.getFunctionType());
				o.put("label", authority.getFunctionName());
				jsonObjects.add(o);
			}
			obj.put("authorityList", jsonObjects.toString());
			
			jsonObjectList.add(obj);
		}
		
		jsonObject.put("roleAuthorityList", jsonObjectList.toString());
		
		return jsonObject.toString();
		
	}
		
		private Map<Integer, List<Authority>> listConvertMap(List<Integer> authorityList){
			Map<Integer, List<Authority>> map = new HashMap<Integer, List<Authority>>();
			
			for (Integer integer : authorityList) {
				Authority authority = getAuthorityByID(integer);
				List<Authority> authoritys = map.get(authority.getType());
				if(authoritys == null){
					authoritys = new ArrayList<Authority>(); 
					map.put(authority.getType(), authoritys);
				}
				authoritys.add(authority);
			}
			
			return map;
		}
		
		/**
		 * 根据权限ID获取权限
		 */
		@SuppressWarnings("unchecked")
		private Authority getAuthorityByID(int authorityID){
			List<Authority> authorityList = (List<Authority>) CacheService.getFromCache(CacheConstant.AUTHORITY_CACHE);
			for (Authority authority : authorityList) {
				if(authority.getAuthorityID() == authorityID) return authority;
			}
			
			return null;
		}
		
		/**
		 * 根据角色ID获取权限列表
		 */
		@SuppressWarnings("unchecked")
		private List<Integer> getAuthority(int roleID){
			Map<Integer,List<Integer>> roleAuthorityMap = (Map<Integer, List<Integer>>) CacheService.getFromCache(CacheConstant.ROLE_ID_AUTHORITY_IDLIST);
			return roleAuthorityMap.get(roleID);
		}

}
