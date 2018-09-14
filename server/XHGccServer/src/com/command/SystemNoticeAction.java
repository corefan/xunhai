package com.command;

import java.io.IOException;
import java.util.List;

import org.eclipse.jetty.io.Buffer;
import org.json.JSONObject;

import com.common.GCCContext;
import com.common.GameCCSocketService;
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
 * 系统消息Action
 * @author ken
 *
 */
public class SystemNoticeAction {
	
	GameCCSocketService gameCCSocketService = GCCContext.getInstance().getServiceCollection().getGameCCSocketService();
	IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
	IWebService webService = GCCContext.getInstance().getServiceCollection().getWebService();
	
	/**
	 * 发送即时系统公告
	 */
	public void sendNotice(MessageObj msgObj, Connection connection) throws Exception  {
		
		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
		ILogService logService = GCCContext.getInstance().getServiceCollection().getLogService();
		
		User user = userService.getUserbyID(connection.getUserID());
		
		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));
		
		String gameSite = param.getString("gameSite");
		String agent = param.getString("agent");
		String content = param.getString("content");
		
		List<String> gameSiteList = userService.gameSiteUrl(gameSite, agent);
		
		if(gameSiteList != null){
			for (String string : gameSiteList) {
				String gameUrl = baseDataService.getUrlByGameSitePath(string, PathConstant.NOTICE);
				
				GameCCEventListener eventListener = new GameCCEventListener(connection, gameSite, false);
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("optType", OptTypeConstant.NOTICE_1);
				jsonObject.put("content", content);
				
				webService.sendDateForWeb(jsonObject, gameUrl, eventListener);
				logService.recordOptLog(user.getUserID(), user.getUserName(), OptTypeConstant.NOTICE_1, "发送系统公告", jsonObject.toString(), connection.getHostAddress());
			}
		}
		
		JSONObject result = new JSONObject();
		String exceptionStr = connection.getExceptionStr().toString();
		if(!"".equals(exceptionStr)){
			exceptionStr += "其余服操作成功";
			result.put("exceptionStr", exceptionStr);
		}
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.NOTICE_1, result.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);
		
	}
	
	/**
	 * 增加系统公告
	 */
	public void addNotice(MessageObj msgObj, Connection connection) throws Exception  {
		
		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
		
		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));
		
		String gameSite = param.getString("gameSite");
		String agent = param.getString("agent");
		String content = param.getString("content");
		Integer time = param.getInt("frequency");
		String startTime = param.getString("startTime");
		String endTime = param.getString("endTime");
		
		List<String> gameSiteList = userService.gameSiteUrl(gameSite, agent);
		
		if(gameSiteList != null){
			for (String string : gameSiteList) {
				String gameUrl = baseDataService.getUrlByGameSitePath(string, PathConstant.NOTICE);
				
				GameCCEventListener eventListener = new GameCCEventListener(connection, string, false) {
				};
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("optType", OptTypeConstant.NOTICE_2);
				jsonObject.put("content", content);
				jsonObject.put("frequency", time);
				jsonObject.put("startTime", startTime);
				jsonObject.put("endTime", endTime);
				
				webService.sendDateForWeb(jsonObject, gameUrl, eventListener);
			}
		}
		
		JSONObject result = new JSONObject();
		String exceptionStr = connection.getExceptionStr().toString();
		if(!"".equals(exceptionStr)){
			exceptionStr += "其余服操作成功";
			result.put("exceptionStr", exceptionStr);
		}
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.NOTICE_2, result.toString().getBytes("UTF-8"));
		gameCCSocketService.sendData(connection, resultMsg);
		
	}
	
	/**
	 * 删除系统公告
	 */
	public void deleteNotice(MessageObj msgObj, Connection connection) throws Exception  {
		
		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
		
		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));
		
		String gameSite = param.getString("gameSite");
		String agent = param.getString("agent");
		Integer systemNoticeId = param.getInt("systemNoticeId");
		
		List<String> gameSiteList = userService.gameSiteUrl(gameSite, agent);
		
		if(gameSiteList != null){
			for (String string : gameSiteList) {
				String gameUrl = baseDataService.getUrlByGameSitePath(string, PathConstant.NOTICE);
				
				GameCCEventListener eventListener = new GameCCEventListener(connection, string, false) {
				};
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("optType", OptTypeConstant.NOTICE_3);
				jsonObject.put("systemNoticeID", systemNoticeId);
				
				webService.sendDateForWeb(jsonObject, gameUrl, eventListener);
			}
		}
		
		JSONObject result = new JSONObject();
		String exceptionStr = connection.getExceptionStr().toString();
		if(!"".equals(exceptionStr)){
			exceptionStr += "其余服操作成功";
			result.put("exceptionStr", exceptionStr);
		}
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.NOTICE_3, result.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);
		
	}
	
	/**
	 * 获得公告列表
	 */
	public void getNoticeList(MessageObj msgObj, Connection connection) throws Exception  {
		
		
		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
		
		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));
		
		String gameSite = param.getString("gameSite");
		
		List<String> gameSiteList = userService.gameSiteUrl(gameSite, "");
		
		if(gameSiteList != null){
			
			for (String string : gameSiteList) {
				String gameUrl = baseDataService.getUrlByGameSitePath(string, PathConstant.NOTICE);
				
				GameCCEventListener eventListener = new GameCCEventListener(connection, string, true){
					StringBuffer resultData = new StringBuffer();
					@Override
					public void onResponseContent(Buffer content) throws IOException {

						String result = content.toString("UTF-8");
						resultData.append(result);
						try {
							JSONObject json = new JSONObject(resultData.toString());
							MessageObj resultMsg = new MessageObj(OptTypeConstant.NOTICE_4, json.toString().getBytes("UTF8"));
							gameCCSocketService.sendData(getConnection(), resultMsg);
						} catch (Exception e) {
							
						}
					}
				};
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("optType", OptTypeConstant.NOTICE_4);
				
				webService.sendDateForWeb(jsonObject, gameUrl, eventListener);
			}
		}
		
	}

}
