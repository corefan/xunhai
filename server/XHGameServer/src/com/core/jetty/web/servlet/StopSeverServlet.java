package com.core.jetty.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.common.MD5Service;
import com.service.impl.StopServerService;
import com.util.LogUtil;

/**
 * 停服指令
 * @author ken
 * @date 2016-12-27
 */
@SuppressWarnings("serial")
public class StopSeverServlet extends BaseServlet {

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
		
		JSONObject jsonObject = dealMsg(req);
		if (jsonObject == null) return;
		
		try {
			String time = jsonObject.getString("time");
			String key = jsonObject.getString("key");
			int stopMin = jsonObject.getInt("stopMin");
			int endStopMin = jsonObject.getInt("endStopMin");
			
			String _key = MD5Service.encryptToUpperString("stopServer" + time);
			if(!_key.equals(key)){
				return;
			}
			
			if(stopMin < 0 || endStopMin < 0) return;
			
//		String host = req.getRemoteHost();
//		if (!GameServer.isLocalAddress(host)) {
//			return;
//		}
			
			System.out.println("--------开始执行停服--------");
			
			StopServerService stopServerService = new StopServerService();
			stopServerService.handleStopServer(stopMin, endStopMin);
			
		} catch (NumberFormatException e) {
			LogUtil.error("停服异常:",e);
		} catch (JSONException e) {
			LogUtil.error("停服异常:",e);
		} catch (Exception e) {
			LogUtil.error("停服异常:",e);
		}
	}

}
