package com.core.jetty.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.common.GameContext;
import com.constant.ChatConstant;
import com.constant.OptTypeConstant;
import com.service.IChatService;
import com.util.LogUtil;

/**
 * GM-系统公告
 * @author ken
 * @date 2017-7-25
 */
public class SystemNoticeServlet extends BaseServlet {
	
	private static final long serialVersionUID = 8948211103583090915L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String msg = "";
		initReqResp(req, resp);

		if(!checkIP(req)) return;
		
		try {
			JSONObject jsonObject = dealMsg(req);
			int optType = Integer.parseInt(jsonObject.get("optType").toString());

			switch (optType) {
			case OptTypeConstant.NOTICE_1:
				msg = sendNotice(jsonObject.getString("content"));
				break;
			case OptTypeConstant.NOTICE_2:
				 msg = operateNotice(0, optType, jsonObject.getString("content"), Integer.parseInt(jsonObject.getString("frequency")), jsonObject.getString("startTime"), jsonObject.getString("endTime"));
				break;
			case OptTypeConstant.NOTICE_3:
				msg = operateNotice(Integer.parseInt(jsonObject.getString("systemNoticeID")), optType, "", 0, "", "");
				break;
			case OptTypeConstant.NOTICE_4:
				msg = sendNoticeList();
				break;
			}

		} catch (Exception e) {
			LogUtil.error("异常:",e);
			msg = "error";
		}
		
		if(msg != ""){
			resp.getWriter().println(msg);
			resp.getWriter().flush();
		}
	}

	
	/** 发送即时系统公告 */
	private String sendNotice(String content) {
		String msg = "error";
		
		if (content == null || "".equals(content.trim())) {
			return msg;
		}
		
		IChatService chatService = GameContext.getInstance().getServiceCollection().getChatService();
		try {
			chatService.chat(0, 0, content, ChatConstant.CHAT_SYSTEM, 0, "");
			msg = "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return msg;
	}
	
	/**  公告操作 */
	private String operateNotice(int noticeId, int optType, String content, int time, String startTime, String endTime){
		JSONObject jsonObject = new JSONObject();
//		try {
//			ISystemNoticeService noticeService = GameContext.getInstance().getServiceCollection().getSystemNoticeService();
//			//增加
//			if(optType == OptTypeConstant.NOTICE_2){
//				if(content == ""){
//					jsonObject.put("addNotice", "内容不能为空");
//					return jsonObject.toString();
//				}
//				
//				if(time == 0){
//					jsonObject.put("addNotice", "公告广播时间不能为空");
//					return jsonObject.toString();
//				}
//				
//				if(startTime == ""){
//					jsonObject.put("addNotice", "公告开始时间不能为空");
//					return jsonObject.toString();
//				}
//				
//				if(endTime == ""){
//					jsonObject.put("addNotice", "公告结束时间不能为空");
//					return jsonObject.toString();
//				}
//				
//				if(DateService.getDateByString(startTime).getTime() >= DateService.getDateByString(endTime).getTime()){
//					jsonObject.put("addNotice", "开始时间不能超过结束时间");
//					return jsonObject.toString();
//				}
//				SystemNotice notice = new SystemNotice();
//				notice.setContent(content);
//				notice.setStartTime(DateService.getDateByString(startTime));
//				notice.setEndTime(DateService.getDateByString(endTime));
//				notice.setFrequency(time);
//				
//				noticeService.addSystemNotice(notice);
//				jsonObject.put("addNotice", "公告添加成功");
//			}else if(optType == OptTypeConstant.NOTICE_3){
//
//				if(noticeId != 0) {
//					noticeService.deleteSystemNoticeByID(noticeId);
//					jsonObject.put("delNotice", "公告删除成功");
//				}else{
//					jsonObject.put("delNotice", "公告ID不能为空");
//					return jsonObject.toString();
//				}
//			}
//			
//		} catch (Exception e) {
//			LogUtil.error("异常:", e);
//			return jsonObject.toString();
//		}
		
		
		return jsonObject.toString();
		
	}
	
	/**
	 *  发送公告列表
	 * @return
	 */
	private String sendNoticeList(){
		JSONObject jsonObject = new JSONObject();
		
		
//		ISystemNoticeService noticeService = GameContext.getInstance().getServiceCollection().getSystemNoticeService();
//		List<SystemNotice> systemNoticeList = noticeService.getSystemNoticeList();

		try {
//			if(systemNoticeList != null){
				List<JSONObject> list = new ArrayList<JSONObject>();
//				for (SystemNotice notice : systemNoticeList) {
//
//					JSONObject obj = new JSONObject();
//					obj.put("systemNoticeID", notice.getSystemNoticeID());
//					obj.put("content", notice.getContent().toString());
//					obj.put("startTime", DateService.dateFormat(notice.getStartTime()));
//					obj.put("endTime", DateService.dateFormat(notice.getEndTime()));
//					obj.put("frequeny", (Integer)notice.getFrequency());
//					list.add(obj);
//				}

				jsonObject.put("systemNoticeList", list.toString());
//			}

		} catch (Exception e) {
			LogUtil.error("异常:", e);
			return jsonObject.toString();
		}


		return jsonObject.toString();
	}
	
}
