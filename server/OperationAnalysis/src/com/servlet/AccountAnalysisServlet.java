package com.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONObject;

import com.common.GCCContext;
import com.constant.OptTypeConstant;
import com.service.IDataAnalysisService;
import com.util.ExcelUtil;
import com.util.LogUtil;

/**
 * @author ken
 * 2014-5-8
 * 付费分析	
 */
public class AccountAnalysisServlet extends BaseServlet {

	private static final long serialVersionUID = 2047559756141712771L;

	@Override
	protected void doAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int optType = Integer.parseInt(req.getParameter("optType"));

		switch (optType) {
		case OptTypeConstant.ACCOUNT_ANALYSIS_1:
			// 大客户管理
			keyAccountData(req, resp);
			break;
		case OptTypeConstant.ACCOUNT_ANALYSIS_2:
			// 充值订单
			payOrderData(req, resp);
			break;
		case OptTypeConstant.ACCOUNT_ANALYSIS_3:
			// 客户详细信息
			accountInfoData(req, resp);
			break;
		case OptTypeConstant.ACCOUNT_ANALYSIS_4:
			// 区服充值数据
			serverPayData(req, resp);
			break;
		default:
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
			break;
		}
	}
	
	/**
	 * 大客户管理
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void keyAccountData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject jsonObject = new JSONObject();
		IDataAnalysisService service = GCCContext.getInstance().getServiceCollection().getDataAnalysisService();
		try {
			String gameSite = (String) req.getSession().getAttribute("gameSite");
			String agent = (String) req.getSession().getAttribute("agent");
			
			jsonObject = service.keyAccount(gameSite, agent);
			
			req.setAttribute("pageIndex", 401);
			req.setAttribute("data", jsonObject);
			
			// 跳转到页面
			req.getRequestDispatcher("/keyAccount.jsp").forward(req, resp);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
		}
	}
	
	/**
	 * 充值订单
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void payOrderData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject jsonObject = new JSONObject();
		IDataAnalysisService service = GCCContext.getInstance().getServiceCollection().getDataAnalysisService();
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String export = (String) req.getParameter("export");
			String gameSite = (String) req.getSession().getAttribute("gameSite");
			String agent = (String) req.getSession().getAttribute("agent");
			
			String startDate;
			String endDate;
			
			if (export != null && export.equals("excel")) {
				startDate = getDate(req, 1, 0, 0)[0];
				
				if (startDate == null) {
					Date date = new Date();
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(date);
					startDate = dateFormat.format(calendar.getTime());
				}
				
				Calendar calendar = Calendar.getInstance(); 
				calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(startDate));
				calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));  
				startDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
				
				calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));  
				endDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
				
				jsonObject = service.payOrder(startDate, endDate, gameSite, agent);
				
				// 导出为Excel文件
				HSSFWorkbook excel = ExcelUtil.payOrderExcel(jsonObject);
				String exportFileName = startDate.substring(0, 4) + "年" + startDate.substring(5, 7) + "月充值订单报表.xls";
				
				if (req.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
					// 火狐
					exportFileName = new String(exportFileName.getBytes("UTF-8"), "ISO8859-1");
				} else if (req.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
					// IE
					exportFileName = URLEncoder.encode(exportFileName, "UTF-8");
				}else{
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
			
			String[] dateArr = getDate(req, 2, 0, 0);
			
			if (dateArr[0].compareTo(dateArr[1]) > 0) {
				startDate = dateArr[1];
				endDate = dateArr[0];
			} else {
				startDate = dateArr[0];
				endDate = dateArr[1];
			}
			
			if (startDate == null || endDate == null) {
				Date date = new Date();
				endDate = dateFormat.format(date);

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.set(Calendar.DAY_OF_YEAR, (calendar.get(Calendar.DAY_OF_YEAR) - 6));
				startDate = dateFormat.format(calendar.getTime());
			}
			
			long startTime = dateFormat.parse(startDate).getTime();
			long endTime = dateFormat.parse(endDate).getTime();
			
			if ((endTime - startTime) / 1000 / 60 / 60 / 24 % 365 > 7) {
				Date date = new Date();
				date = dateFormat.parse(startDate);

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.set(Calendar.DAY_OF_YEAR, (calendar.get(Calendar.DAY_OF_YEAR) + 6));
				endDate = dateFormat.format(calendar.getTime());
			}
			
			jsonObject = service.payOrder(startDate, endDate, gameSite, agent);
			req.setAttribute("pageIndex", 402);
			req.setAttribute("data", jsonObject);
			req.setAttribute("startDate", startDate);
			req.setAttribute("endDate", endDate);
			// 跳转到页面
			req.getRequestDispatcher("/payOrder.jsp").forward(req, resp);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
		}
	}
	
	/**
	 * 客户信息
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void accountInfoData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject jsonObject = new JSONObject();
		IDataAnalysisService service = GCCContext.getInstance().getServiceCollection().getDataAnalysisService();
		try {
			String gameSite = (String) req.getParameter("gameSite");
			String playerId = (String) req.getParameter("id");
			
			jsonObject = service.accountInfo(gameSite, playerId);

			req.setAttribute("data", jsonObject);
			
			// 跳转到页面
			req.getRequestDispatcher("/playerInfo.jsp").forward(req, resp);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
		}
	}
	
	/**
	 * 区服充值数据
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void serverPayData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject jsonObject = new JSONObject();
		IDataAnalysisService service = GCCContext.getInstance().getServiceCollection().getDataAnalysisService();
		try {
			String[] dateArr = getDate(req, 2, 0, -6);
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
			
			String gameSite = (String) req.getSession().getAttribute("gameSites");
			String agent = (String) req.getSession().getAttribute("agent");

			jsonObject = service.serverPay(startDate, endDate, gameSite, agent);

			req.setAttribute("pageIndex", 403);
			req.setAttribute("data", jsonObject);
			req.setAttribute("startDate", startDate);
			req.setAttribute("endDate", endDate);
			
			// 跳转到页面
			req.getRequestDispatcher("/serverPay.jsp").forward(req, resp);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
		}
	}
}
