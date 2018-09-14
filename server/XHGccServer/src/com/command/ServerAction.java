package com.command;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.common.GCCContext;
import com.common.GameCCSocketService;
import com.constant.OptTypeConstant;
import com.core.Connection;
import com.domain.BaseServerConfig;
import com.domain.MessageObj;
import com.service.IBaseDataService;

/**
 * 服务器Action
 * @author ken
 *
 */
public class ServerAction {
	
	GameCCSocketService gameCCSocketService = GCCContext.getInstance().getServiceCollection().getGameCCSocketService();
	
	/** 
	 * 创建服务器 
	 */
	public void createServer(MessageObj msgObj, Connection connection) throws Exception {
		
		IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
		
		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));
		
		BaseServerConfig model = new BaseServerConfig();
		model.setServerNo(param.getInt("serverNo"));
		model.setServerName(param.getString("serverName"));
		model.setAgent(param.getString("agent"));
		model.setGameSite(param.getString("gameSite"));
		model.setGameHost(param.getString("gameHost"));
		model.setGameInnerIp(param.getString("gameInnerIp"));
		model.setGamePort(param.getInt("gamePort"));
		model.setWebPort(param.getInt("webPort"));
		model.setAssets(param.getString("assets"));
		model.setOpenServerDate(param.getString("openServerDate"));
		model.setMegerServerDate(param.getString("megerServerDate"));
		model.setState(param.getInt("state"));
		model.setSeverState(param.getInt("severState"));
		model.setSeverType(param.getInt("severType"));
		JSONObject jsonObject = new JSONObject();
		
		baseDataService.createServer(model);
		
		jsonObject.put("result", 1);
		jsonObject.put("model", model);
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.SERVER_1, jsonObject.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);
		
	}
	
	/** 
	 *更新服务器
	 */
	public void updateServer(MessageObj msgObj, Connection connection) throws Exception {
		
		IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
		
		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));
		
		BaseServerConfig model = new BaseServerConfig();
		model.setServerNo(param.getInt("serverNo"));
		model.setServerName(param.getString("serverName"));
		model.setAgent(param.getString("agent"));
		model.setGameSite(param.getString("gameSite"));
		model.setGameHost(param.getString("gameHost"));
		model.setGameInnerIp(param.getString("gameInnerIp"));
		model.setGamePort(param.getInt("gamePort"));
		model.setWebPort(param.getInt("webPort"));
		model.setAssets(param.getString("assets"));
		model.setOpenServerDate(param.getString("openServerDate"));
		model.setMegerServerDate(param.getString("megerServerDate"));
		model.setState(param.getInt("state"));
		model.setSeverState(param.getInt("severState"));
		model.setSeverType(param.getInt("severType"));
		JSONObject jsonObject = new JSONObject();
			
		baseDataService.updateServer(model, param.getString("oldGameSite"));
		
		jsonObject.put("result", 1);
		jsonObject.put("model", model);
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.SERVER_2, jsonObject.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);
		
	}
	
	/** 
	 * 删除服务器 
	 */
	public void deleteServer(MessageObj msgObj, Connection connection) throws Exception {
		
		IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
		
		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));
		
		JSONObject jsonObject = new JSONObject();
		baseDataService.deleteServer(param.getString("gameSite"));
		jsonObject.put("gameSite", param.getString("gameSite"));
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.SERVER_3, jsonObject.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);
		
	}
	
	/** 得到服务器列表 */
	public void getServerList(MessageObj msgObj, Connection connection) throws Exception {
		
		IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
		
		JSONObject jsonObject = new JSONObject();
			
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		List<BaseServerConfig> userList = baseDataService.getServerList();
		for (BaseServerConfig usr : userList) {
			JSONObject json = new JSONObject();
			
			json.put("serverNo", usr.getServerNo());
			json.put("serverName",usr.getServerName());
			json.put("agent",usr.getAgent());
			json.put("gameSite",usr.getGameSite());
			json.put("gameHost",usr.getGameHost());
			json.put("gameInnerIp",usr.getGameInnerIp());
			json.put("gamePort",usr.getGamePort());
			json.put("webPort",usr.getWebPort());
			json.put("assets",usr.getAssets());
			json.put("openServerDate",usr.getOpenServerDate());
			json.put("megerServerDate",usr.getMegerServerDate());
			json.put("state",usr.getState());
			json.put("severState",usr.getSeverState());
			json.put("severType",usr.getSeverType());
			
			jsonList.add(json);
		}
		
		jsonObject.put("dataList", jsonList.toString());
			
		MessageObj resultMsg = new MessageObj(OptTypeConstant.SERVER_4, jsonObject.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);
		
	}


}
