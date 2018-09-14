package com.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.io.Buffer;
import org.json.JSONObject;

import com.common.CacheService;
import com.common.GCCContext;
import com.common.JSONService;
import com.constant.CacheConstant;
import com.constant.OptTypeConstant;
import com.constant.PathConstant;
import com.core.GameCCEventListener;
import com.domain.BaseServerConfig;
import com.service.IBaseDataService;
import com.service.IChatService;
import com.service.IWebService;

/**
 * @author ken
 * 2014-12-26
 * 聊天	
 */
public class ChatService implements IChatService {

	public void initChatCache() {
		
		// key：gameSite
		Map<String, List<JSONObject>> chatMap = new ConcurrentHashMap<String, List<JSONObject>>();
		// 聊天改变条数
		Map<String, Integer> chatChangeMap = new ConcurrentHashMap<String, Integer>();
		
		try {
			List<BaseServerConfig> gameSiteVariableList = GCCContext.getInstance().getServiceCollection().getBaseDataService().getServerList();

		if (gameSiteVariableList != null) {
			for (BaseServerConfig gsv : gameSiteVariableList) {
				chatMap.put(gsv.getGameSite(), new ArrayList<JSONObject>());
				chatChangeMap.put(gsv.getGameSite(), 0);
			}
		}
		
		CacheService.putToCache(CacheConstant.CHAT_INFO_CACHE, chatMap);
		CacheService.putToCache(CacheConstant.CHAT_CHANGE_CACHE, chatChangeMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void reqChatLogFromGS(final String gameSite) throws Exception {
		
		IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
		IWebService webService = GCCContext.getInstance().getServiceCollection().getWebService();
		
		String url = baseDataService.getUrlByGameSitePath(gameSite, PathConstant.CHAT_LOG);

		GameCCEventListener eventListener = new GameCCEventListener(null, gameSite, true) {
			StringBuffer resultData = new StringBuffer();
			@Override
			public void onResponseContent(Buffer content) throws IOException {

				String result = content.toString("UTF-8");
				resultData.append(result);
				try {
					JSONObject json = new JSONObject(resultData.toString());
					List<JSONObject> chatLogList = JSONService.stringToJSONList(json.get("dataList").toString());
					
					addChatLog(gameSite, chatLogList);
					
				} catch (Exception e) {
					
				}
			}
		};

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("optType", OptTypeConstant.CHAT_MONITOR);

		webService.sendDateForWeb(jsonObject, url, eventListener);
	}
	
	@SuppressWarnings("unchecked")
	private void addChatLog(String gameSite, List<JSONObject> chatLogList) {
		
		Map<String, List<JSONObject>> chatMap = (Map<String, List<JSONObject>>) CacheService.getFromCache(CacheConstant.CHAT_INFO_CACHE);
		if (chatMap != null) {
			List<JSONObject> jsonList = chatMap.get(gameSite);
			if (jsonList == null) {
				chatMap.put(gameSite, new ArrayList<JSONObject>());
			}
			
			Map<String, Integer> chatChangeMap = (Map<String, Integer>) CacheService.getFromCache(CacheConstant.CHAT_CHANGE_CACHE);
			if (chatLogList.isEmpty()) {
				chatChangeMap.put(gameSite, 0);
				return;
			}
			
			synchronized (jsonList) {
				chatChangeMap.put(gameSite, chatLogList.size());
				jsonList.addAll(chatLogList);
				
				if (jsonList.size() >= 200) {
					jsonList.remove(jsonList.size() - 200);
				}
			}
		}
	}
}
