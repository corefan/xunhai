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
import com.domain.Authority;
import com.domain.MessageObj;
import com.service.IUserService;

/**
 * 权限Action
 * @author ken
 *
 */
public class AuthorityAction {
	
	GameCCSocketService gameCCSocketService = GCCContext.getInstance().getServiceCollection().getGameCCSocketService();
	
	/**
	 * 获得权限列表
	 */
	public void getAuthorityList(MessageObj msgObj, Connection connection) throws JSONException, UnsupportedEncodingException  {
		
		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
		
		JSONObject jsonObject = new JSONObject();
		
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		List<Authority> list = userService.getAuthorityList();
		for (Authority auth : list) {
			JSONObject json = new JSONObject();
			json.put("authorityID", auth.getAuthorityID());
			json.put("name", auth.getFunctionName());
			
			jsonList.add(json);
		}
		jsonObject.put("dataList", jsonList.toString());
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.SYSTEM_14, jsonObject.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);
		
	}
	
	/** 
	 *创建权限
	 */
	public void createAuthority(MessageObj msgObj, Connection connection) throws JSONException, UnsupportedEncodingException {
		
		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
		
		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));
		
		String name = param.getString("name");
		
		JSONObject jsonObject = new JSONObject();
		
		Authority authority = userService.createAuthority(name);
		if (authority == null) {
			jsonObject.put("result", 0);
		}else{
			jsonObject.put("result", 1);
			jsonObject.put("authority", authority);
		}
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.SYSTEM_11, jsonObject.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);
		
	}
	
	/** 
	 * 更新权限 
	 */
	public void updateAuthority(MessageObj msgObj, Connection connection) throws JSONException, UnsupportedEncodingException {
		
		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));
		
		int authorityID = param.getInt("authorityID");
		String name = param.getString("name");
		
		JSONObject jsonObject = new JSONObject();
		
		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
		
		Authority authority = userService.updateAuthority(authorityID, name);
		if (authority == null) {
			jsonObject.put("result", 0);
		}else{
			jsonObject.put("result", 1);
			jsonObject.put("authority", authority);
		}
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.SYSTEM_12, jsonObject.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);
		
	}
	
	/**
	 * 删除权限 
	 */
	public void deleteAuthority(MessageObj msgObj, Connection connection) throws JSONException, UnsupportedEncodingException {
		
		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));
		
		int authorityID = param.getInt("authorityID");
		
		JSONObject jsonObject = new JSONObject();
		
		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
		userService.deleteAuthority(authorityID);
		
		jsonObject.put("authorityID", authorityID);
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.SYSTEM_13, jsonObject.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);
		
	}

}
