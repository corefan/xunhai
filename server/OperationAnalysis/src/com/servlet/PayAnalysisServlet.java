package com.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.common.DateService;
import com.common.GCCContext;
import com.constant.OptTypeConstant;
import com.service.IDataAnalysisService;
import com.util.LogUtil;

/**
 * @author ken
 * 2014-5-8
 * 付费分析	
 */
public class PayAnalysisServlet extends BaseServlet {

	private static final long serialVersionUID = 2047559756141712771L;

	@Override
	protected void doAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (req.getSession().getAttribute("canUsePay") == null 
				|| Boolean.parseBoolean(req.getSession().getAttribute("canUsePay").toString()) == false) {
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
			return;
		}
		
		int optType = Integer.parseInt(req.getParameter("optType"));

		switch (optType) {
		case OptTypeConstant.PAY_ANALYSIS_31:
			// 商城销量统计
			shopSellData(req, resp);
			break;
		case OptTypeConstant.PAY_ANALYSIS_32:
			// 元宝消耗分布 
			diamondCostData(req, resp);
			break;
		case OptTypeConstant.PAY_ANALYSIS_41:
			// 首次付费分析
			firstPayData(req, resp);
			break;
		case OptTypeConstant.PAY_ANALYSIS_42:
			// 首次付费等级分析
			firstPayLevData(req, resp);
			break;
		default:
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
			break;
		}
	}
	
	/**
	 * 商城销量统计
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void shopSellData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject jsonObject = new JSONObject();
		IDataAnalysisService service = GCCContext.getInstance().getServiceCollection().getDataAnalysisService();
		try {
			Date[] dateArr = getDate2(req, 2,  -6);
			Date startDate = dateArr[0];
			Date endDate = dateArr[1];
		
			String gameSite = (String) req.getSession().getAttribute("gameSite");
			String agent = (String) req.getSession().getAttribute("agent");

			jsonObject = service.shopSell(startDate, endDate, gameSite, agent);

			req.setAttribute("pageIndex", 201);
			req.setAttribute("data", jsonObject);
			req.setAttribute("startDate", DateService.dateFormatYMD(startDate));
			req.setAttribute("endDate", DateService.dateFormatYMD(endDate));

			// 跳转到页面
			req.getRequestDispatcher("/shopSell.jsp").forward(req, resp);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
		}
	}
	
	/**
	 *  钻石消耗分布
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void diamondCostData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject jsonObject = new JSONObject();
		IDataAnalysisService service = GCCContext.getInstance().getServiceCollection().getDataAnalysisService();
		try {
			Date[] dateArr = getDate2(req, 2,  -6);
			Date startDate = dateArr[0];
			Date endDate = dateArr[1];

			String gameSite = (String) req.getSession().getAttribute("gameSite");
			String agent = (String) req.getSession().getAttribute("agent");

			jsonObject = service.diamondCost(startDate, endDate, gameSite, agent);

			req.setAttribute("pageIndex", 202);
			req.setAttribute("data", jsonObject);
			req.setAttribute("startDate", DateService.dateFormatYMD(startDate));
			req.setAttribute("endDate", DateService.dateFormatYMD(endDate));

			// 跳转到页面
			req.getRequestDispatcher("/diamondCost.jsp").forward(req, resp);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
		}
	}
	
	/**
	 * 首次付费分析
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void firstPayData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject jsonObject = new JSONObject();
		IDataAnalysisService service = GCCContext.getInstance().getServiceCollection().getDataAnalysisService();
		try {
			Date[] dateArr = getDate2(req, 2,  -6);
			Date startDate = dateArr[0];
			Date endDate = dateArr[1];
			
			String gameSite = (String) req.getSession().getAttribute("gameSite");
			String agent = (String) req.getSession().getAttribute("agent");

			jsonObject = service.payOne(startDate, endDate, gameSite, agent);
			
			req.setAttribute("pageIndex", 203);
			req.setAttribute("data", jsonObject);
			req.setAttribute("startDate", DateService.dateFormatYMD(startDate));
			req.setAttribute("endDate", DateService.dateFormatYMD(endDate));
			
			// 跳转到页面
			req.getRequestDispatcher("/firstPay.jsp").forward(req, resp);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
		}
	}
	
	/**
	 * 首次付费等级分析
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void firstPayLevData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject jsonObject = new JSONObject();
		IDataAnalysisService service = GCCContext.getInstance().getServiceCollection().getDataAnalysisService();
		try {
			String gameSite = (String) req.getSession().getAttribute("gameSite");
			String agent = (String) req.getSession().getAttribute("agent");

			jsonObject = service.payTwo(gameSite, agent);

			req.setAttribute("pageIndex", 204);
			req.setAttribute("data", jsonObject);

			// 跳转到页面
			req.getRequestDispatcher("/firstPayLev.jsp").forward(req, resp);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
		}
	}
}
