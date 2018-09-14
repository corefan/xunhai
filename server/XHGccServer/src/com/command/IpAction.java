package com.command;

import java.io.IOException;

import org.eclipse.jetty.io.Buffer;
import org.json.JSONObject;

import com.common.GCCContext;
import com.common.GameCCSocketService;
import com.constant.IpConstant;
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
public class IpAction {

	GameCCSocketService gameCCSocketService = GCCContext.getInstance().getServiceCollection().getGameCCSocketService();
	IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
	IWebService webService = GCCContext.getInstance().getServiceCollection().getWebService();

	/**
	 * IP操作
	 */
	public void banIpOperate(MessageObj msgObj, Connection connection) throws Exception  {

		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();

		User user = userService.getUserbyID(connection.getUserID());

		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));

		String gameSite = param.getString("gameSite");
		Integer ipState = param.getInt("ipState");
		String ip = param.getString("ip");
		String content = "";

		String url = baseDataService.getUrlByGameSitePath(gameSite, PathConstant.PLAYER);

		GameCCEventListener eventListener = new GameCCEventListener(connection, gameSite, true) {
			StringBuffer resultData = new StringBuffer();
			@Override
			public void onResponseContent(Buffer content) throws IOException {

				String result = content.toString("UTF-8");
				resultData.append(result);
				try {
					JSONObject json = new JSONObject(resultData.toString());
					MessageObj resultMsg = new MessageObj(OptTypeConstant.BAN_IP_OPERATE, json.toString().getBytes("UTF8"));
					gameCCSocketService.sendData(getConnection(), resultMsg);
				} catch (Exception e) {

				}
			}

		};

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("optType", OptTypeConstant.BAN_IP_OPERATE);
		jsonObject.put("ip", ip);
		jsonObject.put("ipState", ipState);

		webService.sendDateForWeb(jsonObject, url, eventListener);

		if(ipState == IpConstant.IP_STATE_COMM){
			content = user.getUserName() + "解除了" + ip;
		}else{
			content = user.getUserName() + "封了" + ip;
		}

		ILogService logService = GCCContext.getInstance().getServiceCollection().getLogService();
		logService.recordOptLog(user.getUserID(), user.getUserName(), OptTypeConstant.BAN_IP_OPERATE, content, content, connection.getHostAddress());

	}

	/**
	 * IP列表
	 */
	public void getBanIpList(MessageObj msgObj, Connection connection) throws Exception  {

		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));

		String gameSite = param.getString("gameSite");

		String url = baseDataService.getUrlByGameSitePath(gameSite, PathConstant.PLAYER);

		GameCCEventListener eventListener = new GameCCEventListener(connection, gameSite, false) {
			@Override
			public void onResponseContent(Buffer content) throws IOException {

				String result = content.toString("UTF-8");

				MessageObj resultMsg = new MessageObj(OptTypeConstant.BAN_IP_LIST, result.getBytes("UTF8"));
				gameCCSocketService.sendData(getConnection(), resultMsg);

			}

		};

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("optType", OptTypeConstant.BAN_IP_LIST);

		webService.sendDateForWeb(jsonObject, url, eventListener);

	}

}
