package com.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.common.GCCContext;
import com.common.JSONService;
import com.constant.OptTypeConstant;
import com.service.IOpenService;
import com.util.LogUtil;

/**
 * @author ken
 * 2015-12-1
 * 开放信息	
 */
public class OpenServlet extends HttpServlet {

	private static final long serialVersionUID = -1669386222535048915L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		int optType = Integer.parseInt(req.getParameter("optType"));

		switch (optType) {
		case OptTypeConstant.OPEN_150:
			// 当前5分钟的在线人数
			open_150(req, resp);
			break;
		case OptTypeConstant.OPEN_151:
			// 当前5分钟的在线人数
			open_151(req, resp);
			break;
		case OptTypeConstant.OPEN_152:
			// 当前5分钟的在线人数
			open_152(req, resp);
			break;
		case OptTypeConstant.OPEN_155:
			// dba调用
			open_155(req, resp);
			break;
		}
	}

	/**
	 * 当前5分钟的在线人数
	 * */ 
	private void open_150(HttpServletRequest req, HttpServletResponse resp) {

		IOpenService openService = GCCContext.getInstance().getServiceCollection().getOpenService();

		String agent = req.getParameter("agent");

		if (agent == null || "".equals(agent)) return;

		int num = 0;
		try {
			num = openService.getCurrentOnlineNum(agent);

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("num", num);
			resp.getWriter().println(jsonObject.toString());
			resp.getWriter().flush();
			return;
		} catch (Exception e) {
			LogUtil.error("异常:", e);
		}
	}

	// 当前5分钟的在线人数
	private void open_151(HttpServletRequest req, HttpServletResponse resp) {

		IOpenService openService = GCCContext.getInstance().getServiceCollection().getOpenService();

		String agent = req.getParameter("agent");

		if (agent == null || "".equals(agent)) return;

		try {
			List<Map<String, Object>> resultMapList = openService.getCurrentOnlineNum_agent(agent);

			List<JSONObject> jsonList = new ArrayList<JSONObject>();
			if (resultMapList != null) {
				for (Map<String, Object> map : resultMapList) {
					JSONObject json = new JSONObject();
					json.put("gameSite", map.get("gameSite"));
					json.put("num", map.get("num"));

					jsonList.add(json);
				}
			}

			resp.getWriter().println(JSONService.JSONListToString(jsonList));
			resp.getWriter().flush();
			return;
		} catch (Exception e) {
			LogUtil.error("异常:", e);
		}
	}

	// 当前区服在线人数
	private void open_152(HttpServletRequest req, HttpServletResponse resp) {

		IOpenService openService = GCCContext.getInstance().getServiceCollection().getOpenService();

		String gameSite = req.getParameter("gameSite");

		if (gameSite == null || "".equals(gameSite)) return;

		int num = 0;
		try {
			num = openService.getCurrentOnlineNum_site(gameSite);

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("num", num);
			jsonObject.put("errorcode", 1);

			if (num == -1) {
				jsonObject.put("errorcode", 0);
			}

			resp.getWriter().println(jsonObject.toString());
			resp.getWriter().flush();
			return;
		} catch (Exception e) {
			LogUtil.error("异常:", e);
		}
	}

	/**
	 * dba要调用的函数
	 * */
	private void open_155(HttpServletRequest req, HttpServletResponse resp) {

		// http://172.18.188.243:8080/OperationAnalysis/open?key=ABC123XYZ&optType=155&gameId=10&gameSite=boyo_0050
		/**
		 * exec sp_combineReset 10, 'amz_0015,amz_0028'
			注：  上述的10为gameId:
			    10是王者之路海外英文
    			20是王者之路boyo
		 * */

		IOpenService openService = GCCContext.getInstance().getServiceCollection().getOpenService();

		String result = "error";
		try {
			String gameId = req.getParameter("gameId");
			String gameSite = req.getParameter("gameSite");

			result = openService.execDBAFunc_1(Integer.parseInt(gameId), gameSite);

		} catch (Exception e) {
			LogUtil.error("异常: ",e);

			result = "error";
		}

		try {
			resp.getWriter().println(result);
			resp.getWriter().flush();
		} catch (IOException e) {
			LogUtil.error("异常: ",e);
		}
	}

}
