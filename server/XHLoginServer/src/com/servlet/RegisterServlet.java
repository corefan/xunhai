package com.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.common.Config;
import com.common.GCCContext;
import com.common.MD5Service;
import com.domain.Account;
import com.service.IAccountService;
import com.util.LogUtil;

/**
 * 注册账号
 * @author ken
 * @date 2017-6-22
 */
public class RegisterServlet extends BaseServlet {

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
			doRegister(req, resp);

		} catch (Exception e) {
			LogUtil.error("注册异常: ",e);
			return;
		}
	}
	
	/**
	 * 注册
	 */
	private void doRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, Exception {
		
		IAccountService accountService = GCCContext.getInstance().getServiceCollection().getAccountService();
		
		JSONObject result = new JSONObject();
		try {
			String userName = req.getParameter("userName");
			String passWord = req.getParameter("passWord");
			String time = req.getParameter("time");
			String sign = req.getParameter("sign");
			
			if(userName == null || userName.trim().equals("")){
				//1:账号为空
				result.put("result", 1);
				this.postData(resp, result.toString());
				return;
			}
			
			if(passWord == null || passWord.trim().equals("")){
				//2:密码为空
				result.put("result", 2);
				this.postData(resp, result.toString());
				return;
			}
			
			passWord = passWord.trim();
			
			int pwdLenth = passWord.length();
			if(pwdLenth < 6 || pwdLenth > 15){
				//3:密码长度有误
				result.put("result", 3);
				this.postData(resp, result.toString());
				return;
			}
			
			if(time == null || time.trim().equals("")  || sign == null || sign.trim().equals("")){
				//5 注册失败
				result.put("result", 5);
				this.postData(resp, result.toString());
				return;
			}
			
			Account account = accountService.getAccountByUserName(userName.trim());
			if(account != null){
				//4:账号已被注册
				result.put("result", 4);
				this.postData(resp, result.toString());
				return;
			}
			
			// 验证sign
			String realSign = MD5Service.encryptToLowerString(userName+passWord+time+Config.WEB_LOGIN_KEY);
			if (!realSign.equalsIgnoreCase(sign)){
				//5 注册失败
				result.put("result", 5);
				this.postData(resp, result.toString());
				return;
			}
			
			account = accountService.createAccount(0, userName, passWord, null, 0, 0);
			
			//0 注册成功
			result.put("result", 0);
			result.put("userId", account.getUserId());
			this.postData(resp, result.toString());
		} catch (Exception e) {
			LogUtil.error("注册失败：", e);
			//5 注册失败
			result.put("result", 5);
			this.postData(resp, result.toString());
		}
	}
	
}
