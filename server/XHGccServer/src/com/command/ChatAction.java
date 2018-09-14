package com.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.common.CacheService;
import com.common.GCCContext;
import com.common.GameCCSocketService;
import com.constant.CacheConstant;
import com.constant.OptTypeConstant;
import com.core.Connection;
import com.domain.MessageObj;
import com.service.IBaseDataService;
import com.service.IWebService;

/**
 * @author ken
 * 2014-12-26
 * 聊天监控	
 */
public class ChatAction {

	GameCCSocketService gameCCSocketService = GCCContext.getInstance().getServiceCollection().getGameCCSocketService();
	IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
	IWebService webService = GCCContext.getInstance().getServiceCollection().getWebService();

	/**
	 * 请求聊天信息
	 * */
	@SuppressWarnings("unchecked")
	public void reqChatInfo(MessageObj msgObj, Connection connection) throws Exception  {

		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));

		String gameSite = param.getString("gameSite");
		// 1.首次请求 0.非首次
		String firstFlag = "1";
		if (param.has("firstFlag")) {
			firstFlag = param.getString("firstFlag");
		}
		
		Map<String, List<JSONObject>> chatMap = (Map<String, List<JSONObject>>) CacheService.getFromCache(CacheConstant.CHAT_INFO_CACHE);
		Map<String, Integer> chatChangeMap = (Map<String, Integer>) CacheService.getFromCache(CacheConstant.CHAT_CHANGE_CACHE);
		if (chatMap == null || chatChangeMap == null) {
			noData(connection, gameSite);
			return;
		}
		
		List<JSONObject> jsonList = chatMap.get(gameSite);
		if (jsonList == null || jsonList.isEmpty()) {
			noData(connection, gameSite);
			return;
		}
		
		Integer changeSize = chatChangeMap.get(gameSite);
		// 最近30条
		if (changeSize == null || "1".equalsIgnoreCase(firstFlag)) changeSize = 30;
		
		if ("0".equalsIgnoreCase(firstFlag) && changeSize == 0) {
			noData(connection, gameSite);
			return;
		}
		
		synchronized (jsonList) {
			
			JSONObject resultJson = new JSONObject();
			List<JSONObject> resultList = new ArrayList<JSONObject>();
			
			if (changeSize > jsonList.size()) changeSize = jsonList.size();
			for (int i=jsonList.size() - changeSize; i < jsonList.size(); i++) {
				resultList.add(jsonList.get(i));
			}
			
			resultJson.put("dataList", resultList.toString());
			resultJson.put("gameSite", gameSite);
			
			MessageObj resultMsg = new MessageObj(OptTypeConstant.CHAT_MONITOR, resultJson.toString().getBytes("UTF8"));
			gameCCSocketService.sendData(connection, resultMsg);
		}
		
	}
	
	public void noData(Connection connection, String gameSite) throws Exception {
		
		JSONObject resultJson = new JSONObject();
		List<JSONObject> resultList = new ArrayList<JSONObject>();
		
		resultJson.put("dataList", resultList.toString());
		resultJson.put("gameSite", gameSite);
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.CHAT_MONITOR, resultJson.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);
		
	}
}
