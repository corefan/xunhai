package com.command;

import java.util.List;

import org.json.JSONObject;

import com.common.GCCContext;
import com.common.GameCCSocketService;
import com.common.MD5Service;
import com.constant.OptTypeConstant;
import com.constant.PathConstant;
import com.core.Connection;
import com.core.GameCCEventListener;
import com.domain.MessageObj;
import com.domain.User;
import com.service.IBaseDataService;
import com.service.ILogService;
import com.service.IUserService;
import com.service.IWebService;

/**
 * IpAction
 * @author ken
 *
 */
public class DealDataAction {
	
	GameCCSocketService gameCCSocketService = GCCContext.getInstance().getServiceCollection().getGameCCSocketService();
	IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
	IWebService webService = GCCContext.getInstance().getServiceCollection().getWebService();
	
	/**
	 * 刷新基础缓存
	 */
	public void refreshBaseCache(MessageObj msgObj, Connection connection) throws Exception  {
		
		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
		ILogService logService = GCCContext.getInstance().getServiceCollection().getLogService();
		
		User user = userService.getUserbyID(connection.getUserID());
		if (!user.getUserName().equals("qidian") && !user.getUserName().equals("yunwei")) return;
		
		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));
		
		String gameSite = param.getString("gameSite");
		String agent = param.getString("agent");
		
		List<String> gameSiteList = userService.gameSiteUrl(gameSite, agent);
		
		if(gameSiteList != null){
			for (String string : gameSiteList) {
				String gameUrl = baseDataService.getUrlByGameSitePath(string, PathConstant.DEAL_DATA);
				
				GameCCEventListener eventListener = new GameCCEventListener(connection, string, false);
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("optType", OptTypeConstant.DEAL_DATA_1);
				
				webService.sendDateForWeb(jsonObject, gameUrl, eventListener);
				logService.recordOptLog(user.getUserID(), user.getUserName(), OptTypeConstant.DEAL_DATA_1, "刷新基础缓存", jsonObject.toString(), connection.getHostAddress());
			}
		}
		
		JSONObject result = new JSONObject();
		String exceptionStr = connection.getExceptionStr().toString();
		if(!"".equals(exceptionStr)){
			exceptionStr += "其余服操作成功";
			result.put("exceptionStr", exceptionStr);
		}
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.DEAL_DATA_1, result.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);
		
	}
	
	/**
	 * 刷新配置文件
	 */
	public void refreshConfigCache(MessageObj msgObj, Connection connection) throws Exception  {
		
		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
		ILogService logService = GCCContext.getInstance().getServiceCollection().getLogService();
		
		User user = userService.getUserbyID(connection.getUserID());
		if (!user.getUserName().equals("qidian") && !user.getUserName().equals("yunwei")) return;
		
		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));
		
		String gameSite = param.getString("gameSite");
		String agent = param.getString("agent");
		
		List<String> gameSiteList = userService.gameSiteUrl(gameSite, agent);
		
		if(gameSiteList != null){
			for (String string : gameSiteList) {
				String gameUrl = baseDataService.getUrlByGameSitePath(string, PathConstant.DEAL_DATA);
				
				GameCCEventListener eventListener = new GameCCEventListener(connection, string, false);
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("optType", OptTypeConstant.DEAL_DATA_2);
				
				webService.sendDateForWeb(jsonObject, gameUrl, eventListener);
				logService.recordOptLog(user.getUserID(), user.getUserName(), OptTypeConstant.DEAL_DATA_2, "刷新配置文件缓存", jsonObject.toString(), connection.getHostAddress());
			}
		}
		
		JSONObject result = new JSONObject();
		String exceptionStr = connection.getExceptionStr().toString();
		if(!"".equals(exceptionStr)){
			exceptionStr += "其余服操作成功";
			result.put("exceptionStr", exceptionStr);
		}
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.DEAL_DATA_2, result.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);
		
	}
	
	/**
	 * 同步缓存数据 synCacheData
	 */
	public void synCacheData(MessageObj msgObj, Connection connection) throws Exception  {
		
		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
		ILogService logService = GCCContext.getInstance().getServiceCollection().getLogService();
		
		User user = userService.getUserbyID(connection.getUserID());
		if (!user.getUserName().equals("qidian") && !user.getUserName().equals("yunwei")) return;
		
		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));
		
		String gameSite = param.getString("gameSite");
		String agent = param.getString("agent");
		
		List<String> gameSiteList = userService.gameSiteUrl(gameSite, agent);
		
		if(gameSiteList != null){
			for (String string : gameSiteList) {
				String gameUrl = baseDataService.getUrlByGameSitePath(string, PathConstant.DEAL_DATA);
				
				GameCCEventListener eventListener = new GameCCEventListener(connection, string, false);
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("optType", OptTypeConstant.DEAL_DATA_3);
				
				webService.sendDateForWeb(jsonObject, gameUrl, eventListener);
				logService.recordOptLog(user.getUserID(), user.getUserName(), OptTypeConstant.DEAL_DATA_3, "同步缓存数据", jsonObject.toString(), connection.getHostAddress());
			}
		}
		
		JSONObject result = new JSONObject();
		String exceptionStr = connection.getExceptionStr().toString();
		if(!"".equals(exceptionStr)){
			exceptionStr += "其余服操作成功";
			result.put("exceptionStr", exceptionStr);
		}
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.DEAL_DATA_3, result.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);
		
	}

	/**
	 * 热更新class
	 */
	public void hotUpdateClass(MessageObj msgObj, Connection connection) throws Exception  {
		
		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
		ILogService logService = GCCContext.getInstance().getServiceCollection().getLogService();
		
		User user = userService.getUserbyID(connection.getUserID());
		if (!user.getUserName().equals("qidian") && !user.getUserName().equals("yunwei")) return;
		
		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));
		
		String gameSite = param.getString("gameSite");
		String agent = param.getString("agent");
		
		List<String> gameSiteList = userService.gameSiteUrl(gameSite, agent);
		
		if(gameSiteList != null){
			for (String string : gameSiteList) {
				String gameUrl = baseDataService.getUrlByGameSitePath(string, PathConstant.DEAL_DATA);
				
				GameCCEventListener eventListener = new GameCCEventListener(connection, string, false);
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("optType", OptTypeConstant.DEAL_DATA_8);
				
				webService.sendDateForWeb(jsonObject, gameUrl, eventListener);
				logService.recordOptLog(user.getUserID(), user.getUserName(), OptTypeConstant.DEAL_DATA_8, "热更新class", jsonObject.toString(), connection.getHostAddress());
			}
		}
		
		JSONObject result = new JSONObject();
		String exceptionStr = connection.getExceptionStr().toString();
		if(!"".equals(exceptionStr)){
			exceptionStr += "其余服操作成功";
			result.put("exceptionStr", exceptionStr);
		}
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.DEAL_DATA_8, result.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);
		
	}
	
	/**
	 * 停服维护
	 */
	public void stopServer(MessageObj msgObj, Connection connection) throws Exception  {
		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
		ILogService logService = GCCContext.getInstance().getServiceCollection().getLogService();
		
		User user = userService.getUserbyID(connection.getUserID());
		if (!user.getUserName().equals("qidian") && !user.getUserName().equals("yunwei")) return;
		
		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));
		
		String gameSite = param.getString("gameSite");
		String agent = param.getString("agent");
		int stopMin = param.getInt("stopMin");
		int endStopMin = param.getInt("endStopMin");
		
		if(stopMin < 0 || endStopMin < 0) return;
		
		List<String> gameSiteList = userService.gameSiteUrl(gameSite, agent);
		
		if(gameSiteList != null){
			long time = System.currentTimeMillis();
			String key = MD5Service.encryptToUpperString("stopServer" + time);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("time", time);
			jsonObject.put("key", key);
			jsonObject.put("stopMin", stopMin);
			jsonObject.put("endStopMin", endStopMin);
			
			for (String string : gameSiteList) {
				String gameUrl = baseDataService.getUrlByGameSitePath(string, PathConstant.STOP_SERVER);
				
				GameCCEventListener eventListener = new GameCCEventListener(connection, string, false);
				
				webService.sendDateForWeb(jsonObject, gameUrl, eventListener);
				logService.recordOptLog(user.getUserID(), user.getUserName(), OptTypeConstant.DEAL_DATA_10, "停服维护", jsonObject.toString(), connection.getHostAddress());
			}
		}
		
		JSONObject result = new JSONObject();
		String exceptionStr = connection.getExceptionStr().toString();
		if(!"".equals(exceptionStr)){
			exceptionStr += "其余服操作成功";
			result.put("exceptionStr", exceptionStr);
		}
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.DEAL_DATA_10, result.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);
	}
}
