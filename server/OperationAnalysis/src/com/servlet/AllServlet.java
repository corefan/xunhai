package com.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.common.DateService;
import com.common.GCCContext;
import com.constant.OptTypeConstant;
import com.domain.User;
import com.service.IDataAnalysisService;
import com.service.IUserService;
import com.util.LogUtil;

public class AllServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7229418127463996918L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int optType = Integer.parseInt(req.getParameter("optType"));

		switch (optType) {
		case OptTypeConstant.PLAYER_11:
			// 返回首页
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
			break;
		case OptTypeConstant.PLAYER_16:
			// 查询服务器列表
			getServerList(req, resp);
			break;
		case OptTypeConstant.PLAYER_17:
			// 选择服务器
			choiceServer(req, resp);
			break;
		default:
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
			break;
		}
	}

	/**
	 * 查询服务器列表
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void getServerList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
		User user = (User) req.getSession().getAttribute(req.getSession().getId());
		Integer gameID = (Integer) req.getSession().getAttribute("gameID");
		if (gameID == null || gameID == 0) gameID = 10;
		
		JSONObject serverData = userService.getServerConfListByRID(user, gameID);
		
		int selectModel = 0; // 服务器选择模式 0：未选  1：全服  2：多服
		if (req.getSession().getAttribute("gameSite") != null && !((String) req.getSession().getAttribute("gameSite")).equals("")) {
			selectModel = 2;
		} else if (req.getSession().getAttribute("agent") != null && !((String) req.getSession().getAttribute("agent")).equals("")) {
			selectModel = 1;
		}
			
		req.setAttribute("serverData", serverData);
		req.setAttribute("selectModel", selectModel);

		// 跳转到页面
		req.getRequestDispatcher("/selectServer.jsp").forward(req, resp);
	}

	/**
	 * 选择服务器
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void choiceServer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int selectType = Integer.parseInt(req.getParameter("selectType"));
		if (selectType == 1) {
			// 全服选择模式
			String[] agentList = req.getParameterValues("agent");
			String agent = "";
			if (agentList != null && agentList.length > 0) {
				agent = arrayToString(agentList, "");
			}
			req.getSession().setAttribute("agent", agent);
			req.getSession().setAttribute("gameSite", "");
			req.getSession().setAttribute("gameSites", "");
			req.getSession().setAttribute("gameSiteName", "");
		} else if (selectType == 2) {
			// 多服选择模式 
			String[] gameSiteList = req.getParameterValues("gameSite");
			String[] gameSiteIDList; 
			String[] gameSiteNameList; 
			String gameSite = "";
			String gameSites = "";
			String gameSiteName = "";
			if (gameSiteList != null && gameSiteList.length > 0) {
				gameSiteIDList = new String[gameSiteList.length];
				gameSiteNameList = new String[gameSiteList.length];
				for (int i = 0; i < gameSiteList.length; i++) {
					String[] strArr = gameSiteList[i].split("@");
					gameSiteIDList[i] = strArr[0];
					gameSiteNameList[i] = strArr[1];
				}
				gameSite = arrayToString(gameSiteIDList, "");
				gameSites = arrayToString(gameSiteIDList, "'");
				gameSiteName = arrayToString(gameSiteNameList, "");
			}
			
			req.getSession().setAttribute("agent", "");
			req.getSession().setAttribute("gameSite", gameSite);
			req.getSession().setAttribute("gameSites", gameSites);
			req.getSession().setAttribute("gameSiteName", gameSiteName);
		}
		
		if ((req.getSession().getAttribute("agent") != null && !req.getSession().getAttribute("agent").equals("")) || 
				(req.getSession().getAttribute("gameSite") != null && !req.getSession().getAttribute("gameSite").equals(""))) {
			fiveMinutesData(req, resp);
		} else {
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
		}
	}
	
	/**
	 * 查看5分钟（注册， 在线， 充值）数据
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void fiveMinutesData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject fiveOnlineData = new JSONObject();
		JSONObject fiveRegisterData = new JSONObject();
		JSONObject fivePayData = new JSONObject();
		IDataAnalysisService service = GCCContext.getInstance().getServiceCollection().getDataAnalysisService();
		try {
			Date[] dateArr = getDate2(req, 2, -1);
			Date startDate = dateArr[1];
			Date targetDate = dateArr[0];

			String gameSite = (String) req.getSession().getAttribute("gameSite");
			String agent = (String) req.getSession().getAttribute("agent");
			int gameID = Integer.parseInt(req.getSession().getAttribute("gameID").toString());

			boolean newDataFlag = true;
			Map<String, Object> fiveDataMap = service.checkFiveMinData(agent, gameID);
			if (fiveDataMap != null) {
				newDataFlag = false;
				
				fiveOnlineData = (JSONObject) fiveDataMap.get("fiveOnlineData");
				fiveRegisterData = (JSONObject) fiveDataMap.get("fiveRegisterData");
				fivePayData = (JSONObject) fiveDataMap.get("fivePayData");
			}
			
			if (newDataFlag) {
				fiveOnlineData = service.fiveMinOnLine(startDate, targetDate, gameSite, agent);
				fiveRegisterData = service.fiveRegister(startDate, targetDate, gameSite, agent);
				fivePayData = service.fivePay(DateService.dateFormatYMD(startDate), DateService.dateFormatYMD(targetDate), gameSite, agent);
			}

			req.setAttribute("pageIndex", 101);
			req.setAttribute("fiveOnlineData", fiveOnlineData);
			req.setAttribute("fiveRegisterData", fiveRegisterData);
			req.setAttribute("fivePayData", fivePayData);
			req.setAttribute("startDate", DateService.dateFormatYMD(startDate));
			req.setAttribute("endDate", DateService.dateFormatYMD(targetDate));

			// 跳转到页面
			req.getRequestDispatcher("/fiveMinutes.jsp").forward(req, resp);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
		}
	}
	
	private String arrayToString(String[] arr, String splitSymbol) {
		String result = "";
		for (String obj:arr) {
			result += splitSymbol + obj + splitSymbol + ",";
		}
		if (result.length() > 1) {	
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}
}