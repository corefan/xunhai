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
import com.constant.ChatConstant;
import com.constant.MailConstant;
import com.constant.OptTypeConstant;
import com.domain.chat.Notice;
import com.message.ChatProto.ParamType;
import com.service.IChatService;
import com.service.IMailService;
import com.util.LogUtil;

/**
 * 发送邮件
 * @author ken
 * @date 2017-2-17
 */
public class MailServlet extends BaseServlet {
	
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
			
			int optType = jsonObject.getInt("optType");
			switch (optType) {
			
			case OptTypeConstant.ITEM_8:
				sendMailToPlayerList(jsonObject);
				break;
			}
			
			msg = "success";
		} catch (Exception e) {
			LogUtil.error("异常:",e);
			msg = "error";
		}
		
//		resp.getWriter().println(msg);
//		resp.getWriter().flush();
		
		this.postData(resp, msg);
	}

	/** 向指定玩家发送邮件*/
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
		
		
		//聊天压测
//		ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
//		exec.scheduleAtFixedRate(new StopServerNoticeTask(), 100, 100, TimeUnit.MILLISECONDS);
		
	
	}
	class StopServerNoticeTask implements Runnable {
		public void run() {
			IChatService chatService = GameContext.getInstance().getServiceCollection().getChatService();
			try {
				chatService.chat(0, 0, "聊天测试"+System.currentTimeMillis(), ChatConstant.CHAT_SYSTEM, 0, "");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			List<Notice> paramList = new ArrayList<Notice>();
			Notice notice1 = new Notice(ParamType.PLAYER, 123173033902081l, 0, "廉雪松");
			Notice notice2 = new Notice(ParamType.PARAM, 0, 0, 99+"");
			
			paramList.add(notice1);
			paramList.add(notice2);
			
			GameSocketService gameSocketService = GameContext.getInstance().getServiceCollection().getGameSocketService();
			chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_17, paramList, gameSocketService.getOnLinePlayerIDList());
		}
	}

}
