package com.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.common.DateService;
import com.common.GCCContext;
import com.domain.User;
import com.service.IDataAnalysisService;
import com.service.IUserService;
import com.util.LogUtil;

/**
 * 登录操作
 * @author ken
 * @date 2017-7-27
 */
public class LoginServlet extends BaseServlet {

	private static final long serialVersionUID = -7569970349706844219L;


	@Override
	protected void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		String userName = request.getParameter("username");
		String password = request.getParameter("password");
		if (userName == null || password == null) {
			request.getRequestDispatcher("/login.jsp").forward(request, response);
			return;
		}
		int gameID = Integer.parseInt(request.getParameter("gameID").toString());

		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
		
		User user = userService.login(userName, password);
		
		if (user == null) {
			request.setAttribute("tips", "账号或密码错误！");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
			return;
		} else {
			try {
				JSONObject serverData = userService.getServerConfListByRID(user, gameID);
				JSONArray agentSites = (JSONArray) serverData.getJSONArray("agentSite");
				if (agentSites.length() == 0) {
					request.setAttribute("tips", "账号或密码错误！");
					request.getRequestDispatcher("/login.jsp").forward(request, response);
					return;
				}
				String agentStr = "";
				for (int i = 0; i < agentSites.length(); i++) {
					JSONObject agentSite = agentSites.getJSONObject(i);
					agentStr += agentSite.getString("agent") + ",";
				}
				if (agentStr.length() > 1) {
					agentStr = agentStr.substring(0, agentStr.length() - 1);
				}
				request.getSession().setAttribute(request.getSession().getId(), user);
				request.getSession().setAttribute("agent", agentStr);
				request.getSession().setAttribute("gameSite", "");
				request.getSession().setAttribute("gameSiteName", "");
				request.getSession().setAttribute("gameID", gameID);
				
				// 所有数据
				boolean allData = false;
				if (user.getRoleID() == 1 || gameID == 20) {
					request.getSession().setAttribute("canUseBeaviour", true);
					request.getSession().setAttribute("canUsePay", true);
					
					allData = true;
				} else {
					request.getSession().setAttribute("canUseBeaviour", true);
					request.getSession().setAttribute("canUsePay", true);
				}
				
				if (agentStr.length() > 0) {
					fiveMinutesData(request, response, allData);
				} else {
					request.getRequestDispatcher("/index.jsp").forward(request, response);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				request.getRequestDispatcher("/index.jsp").forward(request, response);
			}
		}
	}
	
	/**
	 * 查看5分钟（注册， 在线， 充值）数据
	 */
	public void fiveMinutesData(HttpServletRequest req, HttpServletResponse resp, boolean allData) throws ServletException, IOException {
		JSONObject fiveOnlineData = new JSONObject();
		JSONObject fiveRegisterData = new JSONObject();
		JSONObject fivePayData = new JSONObject();
		IDataAnalysisService service = GCCContext.getInstance().getServiceCollection().getDataAnalysisService();
		try {
			Date[] dateArr = getDate2(req, 2,  -1);
			Date startDate = dateArr[1];
			Date targetDate = dateArr[0];

			String gameSite = (String) req.getSession().getAttribute("gameSite");
			String agent = (String) req.getSession().getAttribute("agent");
			int gameID = Integer.parseInt(req.getSession().getAttribute("gameID").toString());

			boolean newDataFlag = true;
			if (allData) {
				Map<String, Object> fiveDataMap = service.checkFiveMinData(agent, gameID);
				if (fiveDataMap != null) {
					newDataFlag = false;
					
					fiveOnlineData = (JSONObject) fiveDataMap.get("fiveOnlineData");
					fiveRegisterData = (JSONObject) fiveDataMap.get("fiveRegisterData");
					fivePayData = (JSONObject) fiveDataMap.get("fivePayData");
				}
			}
			
			if (newDataFlag) {
				fiveOnlineData = service.fiveMinOnLine(startDate, targetDate, gameSite, agent);
				fiveRegisterData = service.fiveRegister(startDate, targetDate, gameSite, agent);
				fivePayData = service.fivePay(DateService.dateFormatYMD(startDate), DateService.dateFormatYMD(targetDate), gameSite, agent);
				
				// 缓存数据
				service.cacheFiveMinData(fiveOnlineData, fiveRegisterData, fivePayData, agent, gameID);
			}

			req.setAttribute("pageIndex", 101);
			req.setAttribute("fiveOnlineData", fiveOnlineData);
			req.setAttribute("fiveRegisterData", fiveRegisterData);
			req.setAttribute("fivePayData", fivePayData);
			req.setAttribute("startDate", DateService.dateFormatYMD(startDate));
			req.setAttribute("endDate", DateService.dateFormatYMD(targetDate));

			// 跳转到页面
			req.getRequestDispatcher("/fiveMinutes.jsp").forward(req, resp);
			
			if (allData) {
				
			}
		} catch (Exception e) {
			LogUtil.error("异常:", e);
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
		}
	}
	
}