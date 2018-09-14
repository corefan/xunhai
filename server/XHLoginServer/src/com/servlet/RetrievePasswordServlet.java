/**
 * 
 */
package com.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.common.GCCContext;
import com.common.MD5Service;
import com.domain.Account;
import com.service.IAccountService;
import com.service.ISmsService;
import com.util.CommonUtil;
import com.util.LogUtil;

/**
 * 找回密码
 * @author jiangqin
 * @date 2017-7-22
 */
public class RetrievePasswordServlet  extends HttpServlet{

	private static final long serialVersionUID = 1681377961924517250L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {
			doRetPwd(req, resp);

		} catch (Exception e) {
			LogUtil.error("找回密码异常: ",e);
			return;
		}
	}
	
	/**
	 * 找回密码
	 */
	private void doRetPwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, Exception {
		
		IAccountService accountService = GCCContext.getInstance().getServiceCollection().getAccountService();
		ISmsService smsService = GCCContext.getInstance().getServiceCollection().getSmsService();
	
		String userName = req.getParameter("userName");
		String telePhone = req.getParameter("telePhone");
		String time = req.getParameter("time");
		String sign = req.getParameter("sign");
		
		JSONObject result = new JSONObject();
		if(userName == null || userName.trim().equals("") 
				||telePhone == null || telePhone.trim().equals("")
				||time == null || time.trim().equals("")
				||sign == null || sign.trim().equals("")){
			//1:参数有误
			result.put("result", 1);
			CommonUtil.postData(resp, result.toString());
			return;
		}
		
		Account account = accountService.getAccountByUserName(userName.trim());
		if(account == null){
			//3:账号有误
			result.put("result", 3);
			CommonUtil.postData(resp, result.toString());
			return;
		}
		
		if(account.getTelephone() == null || "".equals(account.getTelephone())){
			//5:账号尚未绑定手机号
			result.put("result", 5);
			CommonUtil.postData(resp, result.toString());
			return;
		}
		
		if(!CommonUtil.isChinaPhoneLegal(telePhone)){
			//2:电话号码有误
			result.put("result", 2);
			CommonUtil.postData(resp, result.toString());
			return;
		}
		
		if(!account.getTelephone().equals(telePhone)){
			//2:电话号码有误
			result.put("result", 2);
			CommonUtil.postData(resp, result.toString());
			return;
		}
		
		// 验证sign
		String realSign = MD5Service.encryptToLowerString(userName+telePhone+time);
		if (!realSign.equalsIgnoreCase(sign)){
			//1:参数有误
			result.put("result", 1);
			CommonUtil.postData(resp, result.toString());
			return;
		}		
	
		SendSmsResponse sendSmsResponse = smsService.sendSmsRetPwd(telePhone, userName, account.getPassWord());
	    if(sendSmsResponse.getCode() != null && !sendSmsResponse.getCode().equals("OK")) {
	    	result.put("result", 4);
			CommonUtil.postData(resp, result.toString());
		 	return;
	    }
		
		result.put("result", 0);
		CommonUtil.postData(resp, result.toString());
	}
}
