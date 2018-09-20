package com.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.common.GCCContext;
import com.domain.Account;
import com.service.IAccountService;
import com.util.LogUtil;

/**
 * 获取账号的登录appid
 * @author ken
 * @date 2017-6-22
 */
public class AppIdServlet extends BaseServlet {

	private static final long serialVersionUID = -7162734103137003255L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {
			doAppId(req, resp);

		} catch (Exception e) {
			LogUtil.error("获取登录appid异常: ",e);
			return;
		}
	}
	
	/**
	 * 获取登录appid
	 */
	private void doAppId(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, Exception {
		
		IAccountService accountService = GCCContext.getInstance().getServiceCollection().getAccountService();
		
		Account account = null;
		String userId = req.getParameter("userId");
		if(userId != null){
			account = accountService.getAccountByUserId(Long.valueOf(userId));
		}else{
			String userName = req.getParameter("userName");
			if(userName != null){
				account = accountService.getAccountByUserName(userName);
			}
		}
		
		if(account == null){
			this.postData(resp, "0");
		}else{
			this.postData(resp, account.getAppId()+"");
		}
		
	}
	
}
