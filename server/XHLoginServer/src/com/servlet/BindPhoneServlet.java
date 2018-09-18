/**
 * 
 */
package com.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.common.CacheService;
import com.common.GCCContext;
import com.constant.CacheConstant;
import com.domain.Account;
import com.service.IAccountService;
import com.service.ISmsService;
import com.util.LogUtil;

/**
 * 手机绑定
 * @author jiangqin
 * @date 2017-7-20
 */
public class BindPhoneServlet extends BaseServlet{

	private static final long serialVersionUID = 2396999795398462861L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {
			doBindPhone(req, resp);

		} catch (Exception e) {
			LogUtil.error("手机绑定异常: ",e);
			return;
		}
	}
	
	
	/**
	 * 手机绑定处理
	 */
	@SuppressWarnings("unchecked")
	private void doBindPhone(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, Exception {
		IAccountService accountService = GCCContext.getInstance().getServiceCollection().getAccountService();
		ISmsService smsService = GCCContext.getInstance().getServiceCollection().getSmsService();
		
		JSONObject jsonObject = this.dealMsg(req);
		if(jsonObject == null) return;		
		Long userId = jsonObject.getLong("userId");
		String telePhone = jsonObject.getString("telePhone");
		String bizId = jsonObject.getString("bizId");
		String code = jsonObject.getString("code");
		 
		JSONObject result = new JSONObject();
		if(userId == null || userId < 1 || telePhone == null 
				||telePhone.trim().equals("")
				||code == null || code.trim().equals("")
				||code == null || code.trim().equals("")){
			//1:参数有误
			result.put("result", 1);
			this.postData(resp, result.toString());
			return;
		}
	    
	    // 账号不存在
	    Account account = accountService.getAccountByUserId(userId);
	    if(account == null){
			//1:账号未注册
	    	result.put("result", 1);
	    	this.postData(resp, result.toString());
			return;
		}
	    
	    Map<String, String> map = (Map<String, String>)CacheService.getFromCache(CacheConstant.BIND_PHONE_CAHCE);
	    if(map == null || map.isEmpty()){
	    	// 手机未获取验证码
    	    result.put("result", 3);
    	    this.postData(resp, result.toString());
		 	return;
	    }
		
	    String _code = map.get(telePhone);
	    if(_code == null){
	    	// 手机未获取验证码 或验证码过期
	    	result.put("result", 3);
	    	this.postData(resp, result.toString());
		 	return;
	    }
	    
	    // 验证码错误
	    if(!_code.equals(code)){
	    	result.put("result", 3);
	    	this.postData(resp, result.toString());
		 	return;
	    }
	    
	    // 验证码查询
		QuerySendDetailsResponse querySendDetailsResponse =  smsService.querySendDetails(telePhone, bizId);		
	    if(querySendDetailsResponse.getCode() != null && !querySendDetailsResponse.getCode().equals("OK")) {
		    result.put("result", 3);
		    this.postData(resp, result.toString());
		 	return;
	    }
	    
	    // 绑定手机号
	    account.setTelephone(telePhone);
	    accountService.updateAccount(account);
	    
		result.put("result", 0);
		this.postData(resp, result.toString());		
	}
}
