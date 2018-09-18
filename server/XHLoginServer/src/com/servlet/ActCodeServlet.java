/**
 * 
 */
package com.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.common.Config;
import com.common.GCCContext;
import com.common.MD5Service;
import com.domain.Account;
import com.domain.ActCode;
import com.service.IAccountService;
import com.service.IActCodeService;
import com.util.LogUtil;

/**
 * 使用激活码
 * @author ken
 * @date 2018年8月10日
 */
public class ActCodeServlet  extends BaseServlet{

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
			useActCode(req, resp);

		} catch (Exception e) {
			LogUtil.error("激活码异常: ",e);
			return;
		}
	}
	
	/**
	 * 使用激活码
	 */
	private void useActCode(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, Exception {
		
		IActCodeService actCodeService = GCCContext.getInstance().getServiceCollection().getActCodeService();
		IAccountService accountService = GCCContext.getInstance().getServiceCollection().getAccountService();
		
		JSONObject jsonObject = this.dealMsg(req);
		if(jsonObject == null) return;
		
		Long userId = jsonObject.getLong("userId");
		String code = jsonObject.getString("code");
		String agent = jsonObject.getString("agent");
		String sign = jsonObject.getString("sign");
		
		JSONObject result = new JSONObject();
		if(userId == null || userId <= 0
				||code == null || code.trim().equals("")
				||agent == null || agent.trim().equals("")
				||sign == null || sign.trim().equals("")){
			//1:参数有误
			result.put("result", 1);
			this.postData(resp, result.toString());
			return;
		}
		
		// 验证sign
		String realSign = MD5Service.encryptToLowerString(userId+code+agent+Config.WEB_LOGIN_KEY);
		if (!realSign.equalsIgnoreCase(sign)){
			//1:参数有误
			result.put("result", 1);
			this.postData(resp, result.toString());
			return;
		}
		
	    Account account = accountService.getAccountByUserId(userId);
	    if(account == null){
			//1:参数有误
	    	result.put("result", 1);
	    	this.postData(resp, result.toString());
			return;
		}
	    
		ActCode actCode = actCodeService.getActCodeByCode(code);
		if (actCode == null) {
			//2:激活码不存在
			result.put("result", 2);
			this.postData(resp, result.toString());
			return;
		}	
		
		if(actCode.getExclusive() == 1){
			if(!agent.equalsIgnoreCase(actCode.getAgent())){
				//2:激活码不存在
				result.put("result", 2);
				this.postData(resp, result.toString());
				return;	
			}
		}
		
		if (actCode.getState() == 1) {
			//3:激活码已使用过
			result.put("result", 3);
			this.postData(resp, result.toString());
			return;
		}	
		
		// 更新
		try {
			actCode.setState(1);
			actCode.setUseTime(new Date());
			actCode.setUserName(account.getUserName());
			actCodeService.updateActCode(actCode);
		} catch (Exception e) {
			LogUtil.error("使用激活码错误：", e);
		}
		
		result.put("result", 0);
		result.put("type", actCode.getType());
		result.put("rewardId", actCode.getRewardId());
		this.postData(resp, result.toString());
	}
}
