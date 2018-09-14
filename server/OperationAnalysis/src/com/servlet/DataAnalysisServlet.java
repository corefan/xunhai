package com.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONObject;

import com.common.DateService;
import com.common.GCCContext;
import com.constant.OptTypeConstant;
import com.service.IDataAnalysisService;
import com.util.ExcelUtil;
import com.util.LogUtil;

/**
 * 运营分析
 * @author ken
 * @date 2017-7-27
 */
public class DataAnalysisServlet extends BaseServlet {

	private static final long serialVersionUID = 1585942654980812280L;

	@Override
	protected void doAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int optType = Integer.parseInt(req.getParameter("optType"));

		switch (optType) {
		case OptTypeConstant.DATA_ANALYSIS_2:
			// 查看5分钟（注册， 在线， 充值）数据 
			fiveMinutesData(req, resp);
			break;
		case OptTypeConstant.DATA_ANALYSIS_4:
			// 留存数据
			retainData(req, resp);
			break;
//		case OptTypeConstant.DATA_ANALYSIS_15:
//			// 游戏节点分析
//			gameStepData(req, resp);
//			break;
		case OptTypeConstant.DATA_ANALYSIS_16:
			// 游戏每日钻石库存
			stockDiamondData(req, resp);
			break;
		case OptTypeConstant.DATA_ANALYSIS_18:
			// 游戏区看盘
			serverData(req, resp);
			break;
		case OptTypeConstant.DATA_ANALYSIS_19:
			// 指标趋势看盘
			trendData(req, resp);
			break;
		case OptTypeConstant.DATA_ANALYSIS_20:
			// 活跃度监控
			livenessData(req, resp);
			break;
		case OptTypeConstant.DATA_ANALYSIS_21:
			// 在线时长分布
			onlineTimeData(req, resp);
			break;
		case OptTypeConstant.DATA_ANALYSIS_22:
			// 登陆用户构成
			loginPlayerData(req, resp);
			break;
		default:
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
			break;
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

	/**
	 * 查看留存数据
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void retainData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject jsonObject = new JSONObject();
		IDataAnalysisService service = GCCContext.getInstance().getServiceCollection().getDataAnalysisService();
		try {
			Date[] dateArr = getDate2(req, 2, -6);
			Date startDate = dateArr[0];
			Date endDate = dateArr[1];

			String gameSite = (String) req.getSession().getAttribute("gameSite");
			String agent = (String) req.getSession().getAttribute("agent");

			jsonObject = service.showRetain(startDate, endDate, gameSite, agent);

			req.setAttribute("pageIndex", 102);
			req.setAttribute("data", jsonObject);
			req.setAttribute("startDate",  DateService.dateFormatYMD(startDate));
			req.setAttribute("endDate",  DateService.dateFormatYMD(endDate));

			// 跳转到页面
			req.getRequestDispatcher("/retain.jsp").forward(req, resp);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
		}
	}

	/**
	 * 游戏节点分析
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void gameStepData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject jsonObject = new JSONObject();
		IDataAnalysisService service = GCCContext.getInstance().getServiceCollection().getDataAnalysisService();
		try {
			String gameSite = (String) req.getSession().getAttribute("gameSite");
			String agent = (String) req.getSession().getAttribute("agent");

			jsonObject = service.gameStep(gameSite, agent);

			req.setAttribute("pageIndex", 103);
			req.setAttribute("data", jsonObject);

			// 跳转到页面
			req.getRequestDispatcher("/gameStep.jsp").forward(req, resp);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
		}
	}

	/**
	 * 游戏每日钻石库存
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void stockDiamondData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject jsonObject = new JSONObject();
		IDataAnalysisService service = GCCContext.getInstance().getServiceCollection().getDataAnalysisService();
		try {
			String[] dateArr = getDate(req, 2, -1, -6);
			String startDate;
			String endDate;
			if (dateArr[0].compareTo(dateArr[1]) > 0) {
				startDate = dateArr[1];
				endDate = dateArr[0];
			} else {
				startDate = dateArr[0];
				endDate = dateArr[1];
			}

			if (startDate == null || endDate == null) {
				Date date = new Date();
				endDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.set(Calendar.DAY_OF_YEAR, (calendar.get(Calendar.DAY_OF_YEAR) - 6));
				startDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
			}

			String gameSite = (String) req.getSession().getAttribute("gameSite");
			String agent = (String) req.getSession().getAttribute("agent");

			jsonObject = service.diamondData(startDate, endDate, gameSite, agent);

			String export = (String) req.getParameter("export");
			
			if (export != null && export.equals("excel")) {
				// 导出为Excel文件
				HSSFWorkbook excel = ExcelUtil.buildDiamondStockExcel(jsonObject);

				String exportFileName = startDate + "_" + endDate + "_钻石库存分析.xls";

				if (req.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
					// 火狐
					exportFileName = new String(exportFileName.getBytes("UTF-8"), "ISO8859-1");
				} else if (req.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
					// IE
					exportFileName = URLEncoder.encode(exportFileName, "UTF-8");
				}

				resp.setHeader("Content-Disposition", "attachment;filename=" + exportFileName);
				resp.setContentType("application/vnd.ms-excel;charset=UTF-8");

				OutputStream out = resp.getOutputStream();
				excel.write(out);
				out.flush();
				out.close();
				return;
			}

			req.setAttribute("pageIndex", 104);
			req.setAttribute("data", jsonObject);
			req.setAttribute("startDate", startDate);
			req.setAttribute("endDate", endDate);

			// 跳转到页面
			req.getRequestDispatcher("/diamondInventory.jsp").forward(req, resp);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
		}
	}

	/**
	 * 游戏区看盘
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void serverData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject jsonObject = new JSONObject();
		IDataAnalysisService service = GCCContext.getInstance().getServiceCollection().getDataAnalysisService();
		try {

			String gameSite = (String) req.getSession().getAttribute("gameSite");
			String agent = (String) req.getSession().getAttribute("agent");

			jsonObject = service.showGameData(agent, gameSite);

			req.setAttribute("pageIndex", 105);
			req.setAttribute("data", jsonObject);

			// 跳转到页面
			req.getRequestDispatcher("/server.jsp").forward(req, resp);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
		}
	}

	/**
	 * 指标趋势看盘
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void trendData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject jsonObject = new JSONObject();
		IDataAnalysisService service = GCCContext.getInstance().getServiceCollection().getDataAnalysisService();
		try {
			Date[] dateArr = getDate2(req, 2, -6);
			Date startDate = dateArr[0];
			Date endDate = dateArr[1];


			String gameSite = (String) req.getSession().getAttribute("gameSite");
			String agent = (String) req.getSession().getAttribute("agent");

			jsonObject = service.showIndexTrendPlate(startDate, endDate, agent, gameSite);

			req.setAttribute("pageIndex", 106);
			req.setAttribute("data", jsonObject);
			req.setAttribute("startDate", DateService.dateFormatYMD(startDate));
			req.setAttribute("endDate", DateService.dateFormatYMD(endDate));

			// 跳转到页面
			req.getRequestDispatcher("/trend.jsp").forward(req, resp);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
		}
	}

	/**
	 * 活跃度监控
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void livenessData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject jsonObject = new JSONObject();
		IDataAnalysisService service = GCCContext.getInstance().getServiceCollection().getDataAnalysisService();
//		try {
			String[] dateArr = getDate(req, 2, -1, -6);
			String startDate;
			String endDate;
			if (dateArr[0].compareTo(dateArr[1]) > 0) {
				startDate = dateArr[1];
				endDate = dateArr[0];
			} else {
				startDate = dateArr[0];
				endDate = dateArr[1];
			}

			if (startDate == null || endDate == null) {
				Date date = new Date();
				endDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.set(Calendar.DAY_OF_YEAR, (calendar.get(Calendar.DAY_OF_YEAR) - 6));
				startDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
			}

			String gameSite = (String) req.getSession().getAttribute("gameSite");
			String agent = (String) req.getSession().getAttribute("agent");

			jsonObject = service.activeMonitor(startDate, endDate, agent, gameSite);

			req.setAttribute("pageIndex", 107);
			req.setAttribute("data", jsonObject);
			req.setAttribute("startDate", startDate);
			req.setAttribute("endDate", endDate);
			
			// 跳转到页面
			req.getRequestDispatcher("/liveness.jsp").forward(req, resp);
//		} catch (Exception e) {
//			LogUtil.error("异常:", e);
//			req.getRequestDispatcher("/index.jsp").forward(req, resp);
//		}
	}

	/**
	 * 在线时长分布
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void onlineTimeData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject jsonObject = new JSONObject();
		IDataAnalysisService service = GCCContext.getInstance().getServiceCollection().getDataAnalysisService();
		try {
			Date[] dateArr = getDate2(req, 2, -6);
			Date startDate = dateArr[0];
			Date endDate = dateArr[1];

			String gameSite = (String) req.getSession().getAttribute("gameSite");
			String agent = (String) req.getSession().getAttribute("agent");

			jsonObject = service.onlineTime(startDate, endDate, agent, gameSite);

			req.setAttribute("pageIndex", 108);
			req.setAttribute("data", jsonObject);
			req.setAttribute("startDate", DateService.dateFormatYMD(startDate));
			req.setAttribute("endDate", DateService.dateFormatYMD(endDate));

			// 跳转到页面
			req.getRequestDispatcher("/onlineTime.jsp").forward(req, resp);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
		}
	}

	/**
	 * 登录用户构成
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void loginPlayerData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject jsonObject = new JSONObject();
		IDataAnalysisService service = GCCContext.getInstance().getServiceCollection().getDataAnalysisService();
		try {
			Date[] dateArr = getDate2(req, 2, -6);
			Date startDate = dateArr[0];
			Date endDate = dateArr[1];

			String gameSite = (String) req.getSession().getAttribute("gameSite");
			String agent = (String) req.getSession().getAttribute("agent");

			jsonObject = service.loginUserContent(startDate, endDate, agent, gameSite);

			req.setAttribute("pageIndex", 109);
			req.setAttribute("data", jsonObject);
			req.setAttribute("startDate", DateService.dateFormatYMD(startDate));
			req.setAttribute("endDate", DateService.dateFormatYMD(endDate));

			// 跳转到页面
			req.getRequestDispatcher("/loginPlayer.jsp").forward(req, resp);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
		}
	}

	
}
