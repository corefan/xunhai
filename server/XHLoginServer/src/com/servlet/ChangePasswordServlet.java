/**
 * 
 */
package com.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.common.GCCContext;
import com.common.MD5Service;
import com.domain.Account;
import com.service.IAccountService;
import com.util.LogUtil;

/**
 * 修改密码
 * @author jiangqin
 * @date 2017-9-20
 */
public class ChangePasswordServlet  extends BaseServlet{

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
			dochangePwd(req, resp);

		} catch (Exception e) {
			LogUtil.error("找回密码异常: ",e);
			return;
		}
	}
	
	/**
	 * 找回密码
	 */
	private void dochangePwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, Exception {
		
		IAccountService accountService = GCCContext.getInstance().getServiceCollection().getAccountService();
		
		String userName = req.getParameter("userName");
		String oldPwd = req.getParameter("oldPwd");
		String newPwd = req.getParameter("newPwd");
		String sign = req.getParameter("sign");
		
		JSONObject result = new JSONObject();
		if(userName == null || userName.trim().equals("") 
				||oldPwd == null || oldPwd.trim().equals("")
				||newPwd == null || newPwd.trim().equals("")
				||sign == null || sign.trim().equals("")){
			//1:参数有误
			result.put("result", 1);
			this.postData(resp, result.toString());
			return;
		}
		
		// 验证sign
		String realSign = MD5Service.encryptToLowerString(userName+oldPwd+newPwd);
		if (!realSign.equalsIgnoreCase(sign)){
			//1:参数有误
			result.put("result", 1);
			this.postData(resp, result.toString());
			return;
		}
		
		Account account = accountService.getAccountByUserName(userName.trim());
		if(account == null){
			//2:账号有误
			result.put("result", 2);
			this.postData(resp, result.toString());
			return;
		}		
		
		if(!account.getPassWord().equals(oldPwd)){
			//2:旧密码有误
			result.put("result", 3);
			this.postData(resp, result.toString());
			return;
		}
		
		if(newPwd.trim().length() < 6 || newPwd.trim().length() > 15){
			//4:密码长度有误
			result.put("result", 4);
			this.postData(resp, result.toString());
			return;
		}
		
		// 更新密码
		account.setPassWord(newPwd);
		accountService.updateAccount(account);
		
		result.put("result", 0);
		this.postData(resp, result.toString());
	}
}
