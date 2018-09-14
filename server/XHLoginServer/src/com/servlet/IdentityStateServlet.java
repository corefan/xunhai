package com.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.common.GCCContext;
import com.domain.Account;
import com.service.IAccountService;
import com.util.CommonUtil;
import com.util.LogUtil;

/**
 * 实名认证
 * @author ken
 * @date 2017-6-22
 */
public class IdentityStateServlet extends HttpServlet {

	private static final long serialVersionUID = -2705179782107549782L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {
			doIdState(req, resp);

		} catch (Exception e) {
			LogUtil.error("实名认证异常: ",e);
			return;
		}
	}
	
	/**
	 * 实名认证
	 */
	private void doIdState(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, Exception {
		IAccountService accountService = GCCContext.getInstance().getServiceCollection().getAccountService();
		
		JSONObject result = new JSONObject();	
		
		JSONObject jsonObject = CommonUtil.dealMsg(req);
		if(jsonObject == null) return;
		
		Long userId = jsonObject.getLong("userId");
		
		if(userId == null || userId < 1){
			result.put("result", 0);
			CommonUtil.postData(resp, result.toString());
			return;
		}
		
		Account account = accountService.getAccountByUserId(userId);
		if(account == null){
			//4:账号未注册
			result.put("result", 0);
			CommonUtil.postData(resp, result.toString());
			return;
		}
		
		if(account.getIdentity() == null || account.getIdentity().trim().equals("")){
			result.put("result", 0);
			CommonUtil.postData(resp, result.toString());
			return;
		}
		
		result.put("result", 1);
		CommonUtil.postData(resp, result.toString());		
	}
	
}
