package com.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.common.Config;
import com.common.GCCContext;
import com.common.MD5Service;
import com.domain.Account;
import com.service.IAccountService;
import com.util.CommonUtil;
import com.util.LogUtil;

/**
 * 实名认证
 * @author ken
 * @date 2017-6-22
 */
public class IdentityCheckServlet extends HttpServlet {

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
			doIdCheck(req, resp);

		} catch (Exception e) {
			LogUtil.error("实名认证异常: ",e);
			return;
		}
	}
	
	/**
	 * 实名认证
	 */
	private void doIdCheck(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, Exception {
		IAccountService accountService = GCCContext.getInstance().getServiceCollection().getAccountService();
		
		JSONObject result = new JSONObject();	
		
		JSONObject jsonObject = CommonUtil.dealMsg(req);
		if(jsonObject == null) return;
		
		Long userId = jsonObject.getLong("userId");
		String realName = jsonObject.getString("realName");
		String identity = jsonObject.getString("identity");
		long time = jsonObject.getLong("time");
		String sign = jsonObject.getString("sign");	
				
		if(userId == null || userId < 1 || realName == null || realName.trim().equals("") 
				|| identity == null || identity.trim().equals("")			
				|| sign == null || sign.trim().equals("")){
				
				//1:参数有误
				result.put("result", 1);
				CommonUtil.postData(resp, result.toString());
				return;
		}
		
		Account account = accountService.getAccountByUserId(userId);
		if(account == null){
			//4:账号未注册
			result.put("result", 3);
			CommonUtil.postData(resp, result.toString());
			return;
		}
		
		if(account.getIdentity() != null){
			//2:账号已认证过
			result.put("result", 2);
			CommonUtil.postData(resp, result.toString());
			return;
		}
		
		// 验证sign
		String realSign = MD5Service.encryptToUpperString(userId + realName + identity + time + Config.WEB_LOGIN_KEY);
		if (!realSign.equalsIgnoreCase(sign)){
			//1:参数有误
			result.put("result", 1);
			CommonUtil.postData(resp, result.toString());
			return;
		}
	
		account.setIdentity(identity);
		account.setRealName(realName);	
		accountService.updateAccount(account);		
	
		result.put("result", 0);
		CommonUtil.postData(resp, result.toString());		
	}
	
}
