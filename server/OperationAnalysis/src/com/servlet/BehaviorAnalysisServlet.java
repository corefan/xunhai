package com.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.common.GCCContext;
import com.constant.OptTypeConstant;
import com.service.IBehaviorAnalysisService;
import com.service.IDataAnalysisService;
import com.util.LogUtil;

/**
 * 游戏情况
 * @author ken
 * @date 2017-7-27
 */
public class BehaviorAnalysisServlet extends BaseServlet {

	private static final long serialVersionUID = -3110631104951995142L;

	protected void doAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (req.getSession().getAttribute("canUseBeaviour") == null 
				|| Boolean.parseBoolean(req.getSession().getAttribute("canUseBeaviour").toString()) == false) {
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
			return;
		}
		
		int optType = Integer.parseInt(req.getParameter("optType"));
		
		switch (optType) {
		case OptTypeConstant.BEHAVIOR_ANALYSIS_1:
			// 精力使用情况
			energyUseData(req, resp);
			break;
		case OptTypeConstant.BEHAVIOR_ANALYSIS_2:
			// 每日情况
			dailyInstanceData(req, resp);
			break;
		case OptTypeConstant.BEHAVIOR_ANALYSIS_3:
			// 多人副本使用情况
			multiplayerInstanceData(req, resp);
			break;
		case OptTypeConstant.BEHAVIOR_ANALYSIS_4:
			// 恶魔城情况
			castlevaniaData(req, resp);
			break;
		case OptTypeConstant.BEHAVIOR_ANALYSIS_5:
			// 王者争霸情况
			kingCompetitionData(req, resp);
			break;
		case OptTypeConstant.BEHAVIOR_ANALYSIS_6:
			// 竞技场情况
			arenaData(req, resp);
			break;
		case OptTypeConstant.BEHAVIOR_ANALYSIS_7:
			// 炼金情况
			alchemyData(req, resp);
			break;
		case OptTypeConstant.BEHAVIOR_ANALYSIS_8:
			// 日常任务情况
			daliyTaskData(req, resp);
			break;
		case OptTypeConstant.BEHAVIOR_ANALYSIS_9:
			// 公会任务情况
			guildTaskData(req, resp);
			break;
		case OptTypeConstant.BEHAVIOR_ANALYSIS_10:
			// 诅咒殿堂时长分布
			templeData(req, resp);
			break;
		case OptTypeConstant.BEHAVIOR_ANALYSIS_11:
			// 游戏节点分析
			gameStepData(req, resp);
			break;
		default:
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
			break;
		}
	}
	
	/**
	 * 精力使用情况
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void energyUseData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject jsonObject = new JSONObject();
		IBehaviorAnalysisService service = GCCContext.getInstance().getServiceCollection().getBehaviorAnalysisService();
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

			String gameSite = (String) req.getSession().getAttribute("gameSite");
			String agent = (String) req.getSession().getAttribute("agent");

			jsonObject = service.energyUse(startDate, endDate, gameSite, agent);

			req.setAttribute("pageIndex", 301);
			req.setAttribute("data", jsonObject);
			req.setAttribute("startDate", startDate);
			req.setAttribute("endDate", endDate);
			
			// 跳转到页面
			req.getRequestDispatcher("/energyUse.jsp").forward(req, resp);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
		}
	}
		
	/**
	 * 每日副本情况
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void dailyInstanceData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject jsonObject = new JSONObject();
		IBehaviorAnalysisService service = GCCContext.getInstance().getServiceCollection().getBehaviorAnalysisService();
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

			String gameSite = (String) req.getSession().getAttribute("gameSite");
			String agent = (String) req.getSession().getAttribute("agent");

			jsonObject = service.dailyInstance(startDate, endDate, gameSite, agent);
			
			req.setAttribute("pageIndex", 302);
			req.setAttribute("data", jsonObject);
			req.setAttribute("startDate", startDate);
			req.setAttribute("endDate", endDate);

			// 跳转到页面
			req.getRequestDispatcher("/dailyInstance.jsp").forward(req, resp);
//		} catch (Exception e) {
//			LogUtil.error("异常:", e);
//			req.getRequestDispatcher("/index.jsp").forward(req, resp);
//		}
	}
	
	/**
	 * 多人副本情况
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void multiplayerInstanceData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject jsonObject = new JSONObject();
		IBehaviorAnalysisService service = GCCContext.getInstance().getServiceCollection().getBehaviorAnalysisService();
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
			

			String gameSite = (String) req.getSession().getAttribute("gameSite");
			String agent = (String) req.getSession().getAttribute("agent");

			jsonObject = service.multiplayerInstance(startDate, endDate, gameSite, agent);

			req.setAttribute("pageIndex", 303);
			req.setAttribute("data", jsonObject);
			req.setAttribute("startDate", startDate);
			req.setAttribute("endDate", endDate);
			
			// 跳转到页面
			req.getRequestDispatcher("/multiplayerInstance.jsp").forward(req, resp);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
		}
	}
	
	/**
	 * 恶魔城情况
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void castlevaniaData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject basicData = new JSONObject();
		JSONObject bossData = new JSONObject();
		IBehaviorAnalysisService service = GCCContext.getInstance().getServiceCollection().getBehaviorAnalysisService();
//		try {
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
			
			String gameSite = (String) req.getSession().getAttribute("gameSite");
			String agent = (String) req.getSession().getAttribute("agent");

			basicData = service.castlevania(startDate, endDate, gameSite, agent);
			bossData = service.castlevaniaBoss(startDate, endDate, gameSite, agent);

			req.setAttribute("pageIndex", 304);
			req.setAttribute("basicData", basicData);
			req.setAttribute("bossData", bossData);
			req.setAttribute("startDate", startDate);
			req.setAttribute("endDate", endDate);
			
			// 跳转到页面
			req.getRequestDispatcher("/castlevania.jsp").forward(req, resp);
//		} catch (Exception e) {
//			LogUtil.error("异常:", e);
//			req.getRequestDispatcher("/index.jsp").forward(req, resp);
//		}
	}
	
	/**
	 * 王者争霸情况
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void kingCompetitionData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject jsonObject = new JSONObject();
		IBehaviorAnalysisService service = GCCContext.getInstance().getServiceCollection().getBehaviorAnalysisService();
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
			

			String gameSite = (String) req.getSession().getAttribute("gameSite");
			String agent = (String) req.getSession().getAttribute("agent");

			jsonObject = service.kingCompetition(startDate, endDate, gameSite, agent);
			
			req.setAttribute("pageIndex", 305);
			req.setAttribute("data", jsonObject);
			req.setAttribute("startDate", startDate);
			req.setAttribute("endDate", endDate);
			
			// 跳转到页面
			req.getRequestDispatcher("/kingCompetition.jsp").forward(req, resp);
//		} catch (Exception e) {
//			LogUtil.error("异常:", e);
//			req.getRequestDispatcher("/index.jsp").forward(req, resp);
//		}
	}
	
	/**
	 * 竞技场情况
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void arenaData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject jsonObject = new JSONObject();
		IBehaviorAnalysisService service = GCCContext.getInstance().getServiceCollection().getBehaviorAnalysisService();
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
			

			String gameSite = (String) req.getSession().getAttribute("gameSite");
			String agent = (String) req.getSession().getAttribute("agent");

			jsonObject = service.arena(startDate, endDate, gameSite, agent);

			req.setAttribute("pageIndex", 306);
			req.setAttribute("data", jsonObject);
			req.setAttribute("startDate", startDate);
			req.setAttribute("endDate", endDate);
			
			// 跳转到页面
			req.getRequestDispatcher("/arena.jsp").forward(req, resp);
//		} catch (Exception e) {
//			LogUtil.error("异常:", e);
//			req.getRequestDispatcher("/index.jsp").forward(req, resp);
//		}
	}
	
	/**
	 * 炼金情况
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void alchemyData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject jsonObject = new JSONObject();
		IBehaviorAnalysisService service = GCCContext.getInstance().getServiceCollection().getBehaviorAnalysisService();
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
			

			String gameSite = (String) req.getSession().getAttribute("gameSite");
			String agent = (String) req.getSession().getAttribute("agent");

			jsonObject = service.alchemy(startDate, endDate, gameSite, agent);

			req.setAttribute("pageIndex", 307);
			req.setAttribute("data", jsonObject);
			req.setAttribute("startDate", startDate);
			req.setAttribute("endDate", endDate);
			
			// 跳转到页面
			req.getRequestDispatcher("/alchemy.jsp").forward(req, resp);
//		} catch (Exception e) {
//			LogUtil.error("异常:", e);
//			req.getRequestDispatcher("/index.jsp").forward(req, resp);
//		}
	}
	
	/**
	 * 日常任务环数
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void daliyTaskData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject jsonObject = new JSONObject();
		IBehaviorAnalysisService service = GCCContext.getInstance().getServiceCollection().getBehaviorAnalysisService();
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
			

			String gameSite = (String) req.getSession().getAttribute("gameSite");
			String agent = (String) req.getSession().getAttribute("agent");

			jsonObject = service.dailyTask(startDate, endDate, gameSite, agent);

			req.setAttribute("pageIndex", 308);
			req.setAttribute("data", jsonObject);
			req.setAttribute("startDate", startDate);
			req.setAttribute("endDate", endDate);
			
			// 跳转到页面
			req.getRequestDispatcher("/daliyTask.jsp").forward(req, resp);
//		} catch (Exception e) {
//			LogUtil.error("异常:", e);
//			req.getRequestDispatcher("/index.jsp").forward(req, resp);
//		}
	}
	
	/**
	 * 公会任务环数
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void guildTaskData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject jsonObject = new JSONObject();
		IBehaviorAnalysisService service = GCCContext.getInstance().getServiceCollection().getBehaviorAnalysisService();
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
			

			String gameSite = (String) req.getSession().getAttribute("gameSite");
			String agent = (String) req.getSession().getAttribute("agent");

			jsonObject = service.guildTask(startDate, endDate, gameSite, agent);

			req.setAttribute("pageIndex", 309);
			req.setAttribute("data", jsonObject);
			req.setAttribute("startDate", startDate);
			req.setAttribute("endDate", endDate);
			
			// 跳转到页面
			req.getRequestDispatcher("/guildTask.jsp").forward(req, resp);
//		} catch (Exception e) {
//			LogUtil.error("异常:", e);
//			req.getRequestDispatcher("/index.jsp").forward(req, resp);
//		}
	}
	
	/**
	 * 诅咒殿堂时长分布
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void templeData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject jsonObject = new JSONObject();
		IBehaviorAnalysisService service = GCCContext.getInstance().getServiceCollection().getBehaviorAnalysisService();
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
			
			String gameSite = (String) req.getSession().getAttribute("gameSite");
			String agent = (String) req.getSession().getAttribute("agent");

			jsonObject = service.temple(startDate, endDate, gameSite, agent);

			req.setAttribute("pageIndex", 310);
			req.setAttribute("data", jsonObject);
			req.setAttribute("startDate", startDate);
			req.setAttribute("endDate", endDate);
			
			// 跳转到页面
			req.getRequestDispatcher("/temple.jsp").forward(req, resp);
//		} catch (Exception e) {
//			LogUtil.error("异常:", e);
//			req.getRequestDispatcher("/index.jsp").forward(req, resp);
//		}
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

			req.setAttribute("pageIndex", 311);
			req.setAttribute("data", jsonObject);

			// 跳转到页面
			req.getRequestDispatcher("/gameStep.jsp").forward(req, resp);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
		}
	}
}
