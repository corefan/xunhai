package com.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.common.Config;
import com.common.DateService;
import com.common.GCCContext;
import com.common.MD5Service;
import com.domain.Account;
import com.service.IAccountService;
import com.util.LogUtil;

/**
 * 游客转正
 * @author ken
 * @date 2017-6-22
 */
public class BindingServlet extends BaseServlet {

	private static final long serialVersionUID = -7480498859861903440L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {
			doBinding(req, resp);

		} catch (Exception e) {
			LogUtil.error("游客绑定异常: ",e);
			return;
		}
	}
	
	/**
	 * 游客绑定
	 */
	private void doBinding(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, Exception {
		
		IAccountService accountService = GCCContext.getInstance().getServiceCollection().getAccountService();
		
		JSONObject result = new JSONObject();
		
		String userName = req.getParameter("userName");
		String newUserName = req.getParameter("newUserName");
		String newPassWord = req.getParameter("newPassWord");
		String time = req.getParameter("time");
		String sign = req.getParameter("sign");
		
		if(userName == null || userName.trim().equals("") ||newUserName == null || newUserName.trim().equals("")
				||newPassWord == null || newPassWord.trim().equals("")
				||time == null || time.trim().equals("")
				||sign == null || sign.trim().equals("")){
			//1:参数有误
			result.put("result", 1);
			this.postData(resp, result.toString());
			return;
		}
		
		int pwdLenth = newPassWord.length();
		if(pwdLenth < 6 || pwdLenth > 15){
			//2:密码长度有误
			result.put("result", 2);
			this.postData(resp, result.toString());
			return;
		}
		
		Account account = accountService.getAccountByUserName(userName.trim());
		if(account == null || account.getTourist() != 1){
			//3:账号有误
			result.put("result", 3);
			this.postData(resp, result.toString());
			return;
		}
		
		// 验证sign
		String realSign = MD5Service.encryptToLowerString(userName+newUserName+newPassWord+time+Config.WEB_LOGIN_KEY);
		if (!realSign.equalsIgnoreCase(sign)){
			//1:参数有误
			result.put("result", 1);
			this.postData(resp, result.toString());
			return;
		}
		
		Account oldAccount = accountService.getAccountByUserName(newUserName.trim());
		if(oldAccount != null){
			//4:该手机已绑定
			result.put("result", 4);
			this.postData(resp, result.toString());
			return;
		}
		
		account.setTourist(0);
		account.setUserName(newUserName);
		account.setPassWord(newPassWord);
		account.setUpdateTime(DateService.getCurrentUtilDate());
		accountService.updateAccount(account);
		
		accountService.getAccountMap().remove(userName.trim());
		accountService.getAccountMap().put(newUserName, account);
		
		result.put("result", 0);
		this.postData(resp, result.toString());
		
	}
	
}
