package com.core.jetty.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.common.GameContext;
import com.common.GameSocketService;
import com.common.JSONService;
import com.constant.MailConstant;
import com.constant.OptTypeConstant;
import com.constant.RewardTypeConstant;
import com.domain.player.Player;
import com.service.IGMService;
import com.service.IMailService;
import com.util.LogUtil;
import com.util.ResourceUtil;
import com.util.SplitStringUtil;

/**
 * GM-物品管理servlet
 * @author ken
 * @date 2017-7-24
 */
public class ItemServlet extends BaseServlet {

	private static final long serialVersionUID = 830378062338977327L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		initReqResp(req, resp);

		if(!checkIP(req)) return;
		
		String msg = "";
		
		try {
			JSONObject jsonObject = dealMsg(req);
			if (jsonObject == null) return;
			
			int optType = Integer.parseInt(jsonObject.get("optType").toString());
			switch (optType) {
			case OptTypeConstant.ITEM_2:
				sendItemToPlayerList(jsonObject);
				break;
			case OptTypeConstant.ITEM_3:
				sendItemToAll(jsonObject);
				break;
			case OptTypeConstant.ITEM_8:
				sendMailToPlayerList(jsonObject);
				break;
			}
			
			msg = "success";
			Thread.sleep(6000);
		} catch (Exception e) {
			LogUtil.error("异常:",e);
			msg = "error";
		}
		resp.getWriter().println(msg);
		resp.getWriter().flush();
	}

	private String buildAttachment(List<JSONObject> objList) throws Exception {
		
		
		int[][] rewards = new int[objList.size()][4];
		
		for(int i = 0; i< objList.size(); i++){
			JSONObject obj = objList.get(i);
			
			int itemId = obj.getInt("itemId");
			int type = obj.getInt("type");
			int itemNum = obj.getInt("itemNum");
			
			if(type > 1) type = RewardTypeConstant.ITEM;
			int[] items = new int[]{type,itemId,itemNum,1};
			rewards[i] = items;
			
		}
		
		return SplitStringUtil.getStringByIntIntList(rewards);
	}
	
	/**
	 * 向全服玩家发送物品 (一周内上线过的)
	 * */
	private void sendItemToAll(JSONObject jsonObject) throws Exception {
		IMailService mailService = GameContext.getInstance().getServiceCollection().getMailService();
		
		String itemAtta = jsonObject.getString("anex");
		List<JSONObject> itemJsonList = JSONService.stringToJSONList(itemAtta);
		String attachment = buildAttachment(itemJsonList);
		
		String theme = jsonObject.getString("title");
		String content = jsonObject.getString("content");
		mailService.sendItemToAll(theme, content, attachment);
		
	}
	
	/**
	 * 向指定玩家发送物品
	 * */
	private void sendItemToPlayerList(JSONObject jsonObject) throws Exception {
		
		IGMService gmService = GameContext.getInstance().getServiceCollection().getGmService();
		IMailService mailService = GameContext.getInstance().getServiceCollection().getMailService();
		
		String receiveNames = jsonObject.getString("receiveNames");
		String itemList = jsonObject.getString("itemList");
		String title = jsonObject.getString("title");
		String content = jsonObject.getString("content");
		if (title == null || "".equals(title.trim())) {
			title = ResourceUtil.getValue("system_send");
		}
		if (content == null || "".equals(content.trim())) {
			content = ResourceUtil.getValue("system_send_item");
		}
		
		List<JSONObject> itemJsonList = JSONService.stringToJSONList(itemList);
		
		String attachment = this.buildAttachment(itemJsonList);
		
		String[] names = receiveNames.split(";");
		for (String name : names) {
			if (name.length() >= 2 && name.length() < 24) {
				Player player = gmService.getPlayerByName_GM(name);
				if (player != null) {
					mailService.systemSendMail(player.getPlayerId(), title, content, attachment, 0);
				}
			}
		}
	}
	
	/** 向指定玩家发送邮件(不带附件)*/
	public void sendMailToPlayerList(JSONObject jsonObject) throws Exception{
		IMailService mailService = GameContext.getInstance().getServiceCollection().getMailService();
		GameSocketService gameSocketService = GameContext.getInstance().getServiceCollection().getGameSocketService();
		
		String playerID = jsonObject.get("playerID").toString();
		String mailContent = jsonObject.get("content").toString();
		
		
		List<Long> playerIds = new ArrayList<Long>();
		
		//全服
		if(playerID == null || playerID.equals("")){
		
			playerIds = gameSocketService.getOnLinePlayerIDList();
			
			
		}else{
			String[] playerIDList = playerID.split(",");
			for (String id : playerIDList) {
				if(id == null || id.equals("")) continue;
				playerIds.add(Long.valueOf(id));
			}
		}
		
		for(Long pid : playerIds){
			mailService.systemSendMail(pid, MailConstant.SYSTEM_MAIL_SENDER_NAME, mailContent, null, 0);
		}
		
	}

}
