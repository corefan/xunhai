package com.command;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.common.GCCContext;
import com.common.GameCCSocketService;
import com.common.GameException;
import com.constant.OptTypeConstant;
import com.core.Connection;
import com.domain.MessageObj;
import com.domain.User;
import com.service.ILogService;
import com.service.IUserService;

/**
 * 用户Action
 * @author ken
 *
 */
public class UserAction {
	
	GameCCSocketService gameCCSocketService = GCCContext.getInstance().getServiceCollection().getGameCCSocketService();
	
	/** 
	 * 创建用户 
	 */
	public void createUser(MessageObj msgObj, Connection connection) throws JSONException, UnsupportedEncodingException  {
		
		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
		
		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));
		
		String userName = param.getString("userName");
		String password = param.getString("passWord");
		Integer roleID = param.getInt("roleID");
		
		if (userName == null) throw new GameException("用户名不能为空");
		
		if(roleID == null || roleID <= 0) throw new GameException("角色不能为空");
		
		if ("qidian".equals(userName)) return;
		
		JSONObject jsonObject = new JSONObject();
		
		User user = userService.createUser(userName, password, roleID);
		
		if (user == null) {
			jsonObject.put("result", 0);
		}else{
			jsonObject.put("result", 1);
			jsonObject.put("user", user);
		}
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.SYSTEM_1, jsonObject.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);
		
	}
	
	/** 
	 *更新用户
	 */
	public void updateUser(MessageObj msgObj, Connection connection) throws JSONException, UnsupportedEncodingException  {
		
		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
		
		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));
		
		String userName = param.getString("userName");
		String password = param.getString("password");
		Integer userID = param.getInt("userID");
		Integer roleID = param.getInt("roleID");
		
		JSONObject jsonObject = new JSONObject();
		
		// 顶级账号不能修改
		if (userID == 1) return;
		
		User user = userService.updateUser(userID, userName, password, roleID);
		
		if (user == null) {
			jsonObject.put("result", 0);
		}else{
			jsonObject.put("result", 1);
			jsonObject.put("user", user);
		}
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.SYSTEM_2, jsonObject.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);
		
	}
	
	/** 
	 * 删除用户 
	 */
	public void deleteUser(MessageObj msgObj, Connection connection) throws JSONException, UnsupportedEncodingException  {
		
		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
		
		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));
		
		Integer userID = param.getInt("userID");
		
		JSONObject jsonObject = new JSONObject();
			
		// 顶级账号不能修改
		if (userID == 1) return;
		
		userService.deleteUser(userID);
		
		jsonObject.put("userID", userID);
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.SYSTEM_3, jsonObject.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);
		
	}
	
	/** 得到用户列表 */
	public void getUserList(MessageObj msgObj, Connection connection) throws JSONException, UnsupportedEncodingException  {
		
		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
		
		JSONObject jsonObject = new JSONObject();
			
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		List<User> userList = userService.getUserList();
		for (User usr : userList) {
			if(usr.getUserName().equals("qidian")) continue;
			
			JSONObject json = new JSONObject();
			
			json.put("userID", usr.getUserID());
			json.put("userName", usr.getUserName());
			json.put("password", usr.getPassword());
			json.put("roleID", usr.getRoleID());
			json.put("roleName", userService.getRolebyID(usr.getRoleID()).getName());
			
			jsonList.add(json);
		}
		
		jsonObject.put("dataList", jsonList.toString());
			
		MessageObj resultMsg = new MessageObj(OptTypeConstant.SYSTEM_4, jsonObject.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);
		
	}
	
	/** 
	 * 最近操作日志 
	 */
	public void recentOptLogList(MessageObj msgObj, Connection connection) throws JSONException, UnsupportedEncodingException  {
		
		ILogService logService = GCCContext.getInstance().getServiceCollection().getLogService();
		
		JSONObject jsonObject = logService.getRecentOptLogList();
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.OPT_LOG_1, jsonObject.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);
		
	}
	
	/** 
	 * 玩家最近操作日志
	 */
	public void getRecentOptLogListByUserName(MessageObj msgObj, Connection connection) throws JSONException, UnsupportedEncodingException  {
		
		ILogService logService = GCCContext.getInstance().getServiceCollection().getLogService();
		
		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));
		String userName = param.getString("name");
		
		JSONObject jsonObject = logService.getRecentOptLogListByUserName(userName);
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.OPT_LOG_2, jsonObject.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);
		
	}

}
