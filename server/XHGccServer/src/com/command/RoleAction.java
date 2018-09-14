package com.command;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.common.GCCContext;
import com.common.GameCCSocketService;
import com.constant.OptTypeConstant;
import com.core.Connection;
import com.domain.MessageObj;
import com.domain.Role;
import com.service.IUserService;

/**
 * 角色Action
 * @author ken
 *
 */
public class RoleAction {
	
	GameCCSocketService gameCCSocketService = GCCContext.getInstance().getServiceCollection().getGameCCSocketService();
	
	/** 
	 * 创建角色
	 */
	public void createRole(MessageObj msgObj, Connection connection) throws JSONException, UnsupportedEncodingException  {
		
		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
		
		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));
		
		String name = param.getString("name");
		String authorityIDs = param.getString("authorityIDs");
		
		JSONObject jsonObject = new JSONObject();
		
		Role role = userService.createRole(name, authorityIDs);
		if (role == null) {
			jsonObject.put("result", 0);
		}else{
			jsonObject.put("result", 1);
			jsonObject.put("role", role);
		}
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.SYSTEM_5, jsonObject.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);
		
	}
	
	/** 
	 *更新角色
	 */
	public void updateRole(MessageObj msgObj, Connection connection) throws JSONException, UnsupportedEncodingException  {
		
		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
		
		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));
		
		Integer roleID = param.getInt("roleID");
		String name = param.getString("name");
		String authorityIDs = param.getString("authorityIDs");
		
		JSONObject jsonObject = new JSONObject();
		
		Role role = userService.updateRole(roleID, name, authorityIDs);
		
		if (role == null) {
			jsonObject.put("result", 0);
		}else{
			jsonObject.put("result", 1);
			jsonObject.put("role", role);
		}
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.SYSTEM_6, jsonObject.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);
		
	}
	
	/** 
	 * 删除角色 
	 */
	public void deleteRole(MessageObj msgObj, Connection connection) throws JSONException, UnsupportedEncodingException  {
		
		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
		
		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));
		
		Integer roleID = param.getInt("roleID");
		
		JSONObject jsonObject = new JSONObject();
	
		userService.deleteRole(roleID);
		
		jsonObject.put("roleID", roleID);
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.SYSTEM_7, jsonObject.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);
		
	}
	
	/** 
	 * 得到角色列表 
	 */
	public void getRoleList(MessageObj msgObj, Connection connection) throws JSONException, UnsupportedEncodingException  {
		
		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
		
		JSONObject jsonObject = new JSONObject();
			
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		List<Role> roleList = userService.getRoleList();
		for (Role role : roleList) {
			JSONObject json = new JSONObject();
			json.put("roleID", role.getRoleID());
			json.put("name", role.getName());
			
			jsonList.add(json);
		}
		
		jsonObject.put("dataList", jsonList.toString());
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.SYSTEM_8, jsonObject.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);
		
	}

}
