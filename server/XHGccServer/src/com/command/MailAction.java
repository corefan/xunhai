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
import com.service.IBaseDataService;
import com.service.IUserService;
import com.service.IWebService;

/**
 * 邮件Action
 * @author ken
 *
 */
public class MailAction {

	GameCCSocketService gameCCSocketService = GCCContext.getInstance().getServiceCollection().getGameCCSocketService();
	IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
	IWebService webService = GCCContext.getInstance().getServiceCollection().getWebService();

	/**
	 * 发送邮件(不带附件)
	 */
	public void sendMailNoAttachment(MessageObj msgObj, Connection connection) throws Exception  {

		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();

		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));

		String gameSite = param.getString("gameSite");
		String agent = param.getString("agent");
		String mailContent = param.getString("mailContent");
		String receivers = param.getString("receivers");

		List<String> gameSiteList = userService.gameSiteUrl(gameSite, agent);
		if(gameSiteList != null){
			for (String site : gameSiteList) {
				String gameUrl = baseDataService.getUrlByGameSitePath(site, PathConstant.MAIL);
				this.sendMail(receivers, mailContent, connection, gameUrl, site);
			}
		}

		JSONObject result = new JSONObject();
		String exceptionStr = connection.getExceptionStr().toString();
		if(!"".equals(exceptionStr)){
			exceptionStr += "其余服操作成功";
			result.put("exceptionStr", exceptionStr);
		}

		MessageObj resultMsg = new MessageObj(OptTypeConstant.ITEM_8, result.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);

	}

	private void sendMail(String receivers, String content, Connection connection, String url, String site) {

		try {
			GameCCEventListener eventListener = new GameCCEventListener(connection, site, false) {
				@Override
				public void onResponseContent(Buffer content) throws IOException {
					//					String result = content.toString("UTF-8");
					//					MessageObj resultMsg = new MessageObj(OptTypeConstant.ITEM_8, result.getBytes("UTF8"));
					//					gameCCSocketService.sendData(getConnection(), resultMsg);
				}

				@Override
				public void onResponseComplete() throws IOException {
					// TODO Auto-generated method stub
					super.onResponseComplete();
				}

			};

			JSONObject jsonObject = new JSONObject();

			jsonObject.put("playerID", receivers);
			jsonObject.put("content", content);
			jsonObject.put("optType", OptTypeConstant.ITEM_8);

			webService.sendDateForWeb(jsonObject, url, eventListener);
		} catch (Exception e) {
		}

	}

}
