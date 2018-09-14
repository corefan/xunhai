package com.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.common.DateService;

/**
 * BaseServlet
 * @author ken
 * @date 2018年3月29日
 */
public class BaseServlet extends HttpServlet {
	
	private static final long serialVersionUID = -6203625286536278208L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if ((req.getSession().getAttribute("agent") != null && req.getSession().getAttribute("agent").equals("")) 
				 && (req.getSession().getAttribute("gameSite") != null && req.getSession().getAttribute("gameSite").equals(""))) {
			req.setAttribute("alreadySelectServer", false);
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
			return;
		}
		
		doAction(req, resp);
	}
	
	protected void doAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}

	/**
	 * 从request中获取日期
	 * @param req
	 * @param resultNum 获取日期的类型    1：获取一个日期   else：获取两个日期
	 * @param difVal_2 日期之间的差值
	 * @return
	 */
	protected String[] getDate(HttpServletRequest req, int type, int difVal_1, int difVal_2) {
		String[] result = null;
		if (type == 1) {
			result = new String[1];
			result[0] = (String) req.getParameter("startDate");
			if (result[0] == null) {
				Date date = new Date();
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.set(Calendar.DAY_OF_YEAR, (calendar.get(Calendar.DAY_OF_YEAR) + difVal_1));
				
				result[0] = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
			}
		} else {
			result = new String[2];
			result[0] = (String) req.getParameter("startDate");
			result[1] = (String) req.getParameter("endDate");
			if (result[0] == null || result[1] == null) {
				
				Date maxTime = new Date();
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(maxTime);
				//calendar.set(Calendar.DAY_OF_YEAR, (calendar.get(Calendar.DAY_OF_YEAR) + difVal_1));
				result[0] = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
				
				calendar.set(Calendar.DAY_OF_YEAR, (calendar.get(Calendar.DAY_OF_YEAR) + difVal_2));
				result[1] = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
			}
		}
		return result;
	}
	
	/**
	 * 从request中获取日期
	 * @param req
	 * @param resultNum 获取日期的类型    1：获取一个日期   else：获取两个日期
	 * @param difVal_2 日期之间的差值
	 * @return
	 */
	protected Date[] getDate2(HttpServletRequest req, int type, int difVal_2){
		Date[] result = null;
		if (type == 1) {
			result = new Date[1];
			String startDate = (String) req.getParameter("startDate");
			if (startDate == null) {
				Date date = new Date();
				
				result[0] = DateService.getDayOfStartTime(date);
			}else{
				result[0] = DateService.getDayOfStartTime(DateService.getDateByString(startDate));;
			}
			
		} else {
			result = new Date[2];
			String startDate = (String) req.getParameter("startDate");
			String endDate  = (String) req.getParameter("endDate");
			if (startDate == null || endDate == null) {
			
				Date date = new Date();
				result[0] = date;

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.set(Calendar.DAY_OF_YEAR, (calendar.get(Calendar.DAY_OF_YEAR) + difVal_2));
				result[1] = calendar.getTime();

			}else{
				
				Date date = DateService.getDateByString(startDate);
				result[0] = date;

				Date date2 = DateService.getDateByString(endDate);
				result[1] = date2;
			}
			
			if (result[0].compareTo(result[1]) > 0) {
				Date temp = result[0];
				result[0] = result[1];
				result[1] = temp;
			}
			
			result[0] = DateService.getDayOfStartTime(result[0]);
			result[1] = DateService.getDayOfEndTime(result[1]);
		}
		return result;
	}
}
