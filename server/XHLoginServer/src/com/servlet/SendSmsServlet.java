/**
 * 
 */
package com.servlet;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.common.CacheService;
import com.common.GCCContext;
import com.constant.CacheConstant;
import com.domain.Account;
import com.service.IAccountService;
import com.service.ISmsService;
import com.util.CommonUtil;
import com.util.LogUtil;

/**
 * 发送手机验证码
 * @author jiangqin
 * @date 2017-7-20
 */
public class SendSmsServlet extends HttpServlet{

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
			doSendSms(req, resp);

		} catch (Exception e) {
			LogUtil.error("发送手机验证码异常: ",e);
			return;
		}
	}
	
	
	/**
	 * 发送手机验证码
	 */
	@SuppressWarnings("unchecked")
	private void doSendSms(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, Exception {
		IAccountService accountService = GCCContext.getInstance().getServiceCollection().getAccountService();
		ISmsService smsService = GCCContext.getInstance().getServiceCollection().getSmsService();
		
		JSONObject jsonObject = CommonUtil.dealMsg(req);
		if(jsonObject == null) return;
		String telePhone = jsonObject.getString("telePhone");
		
		JSONObject result = new JSONObject();
		if(telePhone == null || telePhone.trim().equals("")){
			//1:参数有误
			result.put("result", 1);
			CommonUtil.postData(resp, result.toString());
			return;
		}
		
		Account acc = accountService.getAccountByTelephone(telePhone);
		if(acc != null){
			//2:该手机号已经绑定其他账号
			result.put("result", 2);
			CommonUtil.postData(resp, result.toString());
			return;
		}
		
		// 生成验证码
		String code = SendSmsServlet.getSix();
		SendSmsResponse sendSmsResponse =  smsService.sendSms(telePhone, code);
		
	    if(sendSmsResponse.getCode() != null && !sendSmsResponse.getCode().equals("OK")) {
		    result.put("result", 3);
		    CommonUtil.postData(resp, result.toString());
		 	return;
	    }
		
	    Map<String, String> map = (Map<String, String>)CacheService.getFromCache(CacheConstant.BIND_PHONE_CAHCE);
	   
	    if(map == null){
	    	map = new ConcurrentHashMap<String, String>();
	    	CacheService.putToCache(CacheConstant.BIND_PHONE_CAHCE, map);
	    }
	    
		map.put(telePhone, code);
	    
		result.put("result", 0);
		result.put("bizId", sendSmsResponse.getBizId());
		CommonUtil.postData(resp, result.toString());
	}
	
	
	   /** 
	    * 产生随机的六位数 
	    * @return 
	    */  
	   private static String getSix(){  
	       return (int)((Math.random()*9+1)*100000) + "";  
	   }  


}
