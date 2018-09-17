package com.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.common.Config;
import com.common.DateService;
import com.common.GCCContext;
import com.common.MD5Service;
import com.domain.Account;
import com.domain.config.BaseServerConfig;
import com.service.IAccountService;
import com.service.IBaseDataService;
import com.util.CommonUtil;
import com.util.HttpUtil;
import com.util.LogUtil;

/**
 * 账户登录
 * @author ken
 * 2014-3-8
 */
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 5681739853641641114L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {
			if("xh".equals(Config.AGENT)){
				doLogin(req, resp);
			}else if("donghai".equals(Config.AGENT)){
				donghaiLogin(req, resp);
			}else if("yunyou".equals(Config.AGENT)){
				yunyouLogin(req, resp);
			}
			

		} catch (Exception e) {
			LogUtil.error("登陆异常: ",e);
			return;
		}
	}

	/**
	 * 处理账户登录
	 */
	private void doLogin(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, Exception {

		IAccountService accountService = GCCContext.getInstance().getServiceCollection().getAccountService();
		IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
		
		
		JSONObject result = new JSONObject();
		
		String userName = req.getParameter("userName");
		String passWord = req.getParameter("passWord");
		String tourist = req.getParameter("tourist");
		String time = req.getParameter("time");
		String sign = req.getParameter("sign");
		
		
		if(userName == null || userName.trim().equals("")){
			//1:账号为空
			result.put("result", 1);
			CommonUtil.postData(resp, result.toString());
			return;
		}
		
		if(passWord == null || passWord.trim().equals("")){
			//2:密码为空
			result.put("result", 2);
			CommonUtil.postData(resp, result.toString());
			return;
		}
		
		int pwdLenth = passWord.length();
		if(pwdLenth < 6 || pwdLenth > 15){
			//3:密码长度有误
			result.put("result", 3);
			CommonUtil.postData(resp, result.toString());
			return;
		}
		
		if(time == null || time.trim().equals("")  || sign == null || sign.trim().equals("")
				|| tourist == null || tourist.trim().equals("")){
			//6 登录失败
			result.put("result", 6);
			CommonUtil.postData(resp, result.toString());
			return;
		}
		
		// 验证sign
		String realSign = MD5Service.encryptToLowerString(userName+passWord+tourist+time+Config.WEB_LOGIN_KEY);
		if (!realSign.equalsIgnoreCase(sign)){
			//6 登录失败
			result.put("result", 6);
			CommonUtil.postData(resp, result.toString());
			return;
		}
		
		Account account = accountService.getAccountByUserName(userName.trim());
		if(account == null){
			if(tourist.equals("0")){
				//4:账号未注册
				result.put("result", 4);
				CommonUtil.postData(resp, result.toString());
				return;
			}else{
				account = accountService.createAccount(0, userName, passWord, null, 1);
			}
		}else{
			if(!account.getPassWord().trim().equals(passWord)){
				//5密码错误
				result.put("result", 5);
				CommonUtil.postData(resp, result.toString());
				return;
			}
		}
		
		//玩家已创角的服务器列表
		List<Integer> svrList = account.getServerList();
		
		//所有服务器列表
		List<BaseServerConfig> serverList = baseDataService.listServers();
		
		String key = CommonUtil.randomLoginKey(account.getUserName());
		result.put("result", 0);
		result.put("userId", account.getUserId());
		result.put("key", key);
		result.put("time", time);
		if(account.getTelephone() == null){
			result.put("telePhone", 0);
		}else{
			result.put("telePhone", account.getTelephone());
		}
		
		result.put("sign", MD5Service.encryptToUpperString(account.getUserId()+key+time+Config.WEB_LOGIN_KEY));
		
		long curTime = System.currentTimeMillis();
		
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		for(BaseServerConfig model : serverList){
			Integer serverNo = model.getServerNo();
			JSONObject json = new JSONObject();
			json.put("serverNo", model.getServerNo());
			json.put("serverName", model.getServerName());
			json.put("gameHost", model.getGameHost());
			json.put("gamePort", model.getGamePort());
			json.put("loginFlag", svrList.contains(serverNo)? 1 : 0);
			json.put("endStopTime", 0);
			json.put("severType", model.getSeverType());
			long openServerDate = DateService.getDateByString(model.getOpenServerDate()).getTime();
			json.put("openServerDate", openServerDate);
			int serverState = model.getSeverState();
			if(model.getEndStopDate() != null){
				long endStopTime = DateService.getDateByString(model.getEndStopDate()).getTime();
				if(curTime < endStopTime){
					serverState = 4;
					json.put("endStopTime", endStopTime);
				}
			}
			json.put("severState", serverState);
			
			
			jsonList.add(json);
		}
		result.put("serverList", jsonList.toString());
		CommonUtil.postData(resp, result.toString());
	}

	/**
	 * 东海运营  处理账户登录 https://github.com/donghaigame/DHSDKServerDemo
	 */
	private void donghaiLogin(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, Exception {
		
		String userId = req.getParameter("userId");
		String userName = req.getParameter("userName");
		String time = req.getParameter("time");
		String sign = req.getParameter("sign");
		
		JSONObject result = new JSONObject();
		
		if(userId == null || "".equals(userId.trim()) 
		   || userName == null || "".equals(userName.trim())
		   || time == null || "".equals(time.trim())
		   || sign == null || "".equals(sign.trim())){
			//登录失败
			LogUtil.error("donghaiLogin 参数有误");
			result.put("result", 6);
			CommonUtil.postData(resp, result.toString());
			return;
		}
		
		// 验证sign
		String realSign = MD5Service.encryptToLowerString(userId+userName+time+Config.WEB_LOGIN_KEY);
		if (!realSign.equalsIgnoreCase(sign)){
			//6 登录失败
			LogUtil.error("donghaiLogin sign验证有误");
			
			result.put("result", 6);
			CommonUtil.postData(resp, result.toString());
			return;
		}
		IAccountService accountService = GCCContext.getInstance().getServiceCollection().getAccountService();
		IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
		
		
		Account account = accountService.getAccountByUserName(userName.trim());
		if(account == null){
			account = accountService.createAccount(Long.valueOf(userId), userName, "123456", null, 1);
		}
		
		//玩家已创角的服务器列表
		List<Integer> svrList = account.getServerList();
		
		//所有服务器列表
		List<BaseServerConfig> serverList = baseDataService.listServers();
		
		long time1 = System.currentTimeMillis();
		String key = CommonUtil.randomLoginKey(account.getUserName());
		
		result.put("result", 0);
		result.put("userId", account.getUserId());
		result.put("key", key);
		result.put("time", time1);
		if(account.getTelephone() == null){
			result.put("telePhone", 0);
		}else{
			result.put("telePhone", account.getTelephone());
		}
		
		result.put("sign", MD5Service.encryptToUpperString(account.getUserId()+key+time1+Config.WEB_LOGIN_KEY));
		
		long curTime = System.currentTimeMillis();
		
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		for(BaseServerConfig model : serverList){
			Integer serverNo = model.getServerNo();
			JSONObject json = new JSONObject();
			json.put("serverNo", model.getServerNo());
			json.put("serverName", model.getServerName());
			json.put("gameHost", model.getGameHost());
			json.put("gamePort", model.getGamePort());
			json.put("loginFlag", svrList.contains(serverNo)? 1 : 0);
			json.put("endStopTime", 0);
			json.put("severType", model.getSeverType());
			long openServerDate = DateService.getDateByString(model.getOpenServerDate()).getTime();
			json.put("openServerDate", openServerDate);
			int serverState = model.getSeverState();
			if(model.getEndStopDate() != null){
				long endStopTime = DateService.getDateByString(model.getEndStopDate()).getTime();
				if(curTime < endStopTime){
					serverState = 4;
					json.put("endStopTime", endStopTime);
				}
			}
			json.put("severState", serverState);
			
			
			jsonList.add(json);
		}
		result.put("serverList", jsonList.toString());
		CommonUtil.postData(resp, result.toString());
		
	}
	
	/**
	 * 云游运营  处理账户登录  http://lll.lygames.cc/index.php?m=index&c=user&a=Token&uid=客户端传给的uid&token=token 码
	 */
	private void yunyouLogin(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, Exception {
		
		String uid = req.getParameter("uid");
		String token = req.getParameter("token");
		
		JSONObject result = new JSONObject();
		
		if(uid == null || "".equals(uid.trim()) 
		   || token == null || "".equals(token.trim())){
			//登录失败
			result.put("result", 6);
			CommonUtil.postData(resp, result.toString());
			return;
		}
		
		synchronized (uid) {
			
			String url = "http://lll.lygames.cc/index.php";
			
			StringBuilder param = new StringBuilder();
			param.append("?");
			param.append("m=index");
			param.append("&c=user");
			param.append("&a=Token");
			param.append("&uid=").append(uid);
			param.append("&token=").append(token);
			
			String js = HttpUtil.sendGet(url, param.toString());
			if(js == null){
				//登录失败
				result.put("result", 6);
				CommonUtil.postData(resp, result.toString());
				return;
			}
			
			JSONObject resultJson = new JSONObject(js);
			if(resultJson.getString("isSuccess") .equals("1")){
				IAccountService accountService = GCCContext.getInstance().getServiceCollection().getAccountService();
				IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
				
				Account account = accountService.getAccountByUserName(uid);
				if(account == null){
					account = accountService.createAccount(0, uid, "123456", null, 1);
				}
				
				//玩家已创角的服务器列表
				List<Integer> svrList = account.getServerList();
				
				//所有服务器列表
				List<BaseServerConfig> serverList = baseDataService.listServers();
				
				long time = System.currentTimeMillis();
				String key = CommonUtil.randomLoginKey(account.getUserName());
				
				result.put("result", 0);
				result.put("userId", account.getUserId());
				result.put("key", key);
				result.put("time", time);
				if(account.getTelephone() == null){
					result.put("telePhone", 0);
				}else{
					result.put("telePhone", account.getTelephone());
				}
				
				result.put("sign", MD5Service.encryptToUpperString(account.getUserId()+key+time+Config.WEB_LOGIN_KEY));
				
				long curTime = System.currentTimeMillis();
				
				List<JSONObject> jsonList = new ArrayList<JSONObject>();
				for(BaseServerConfig model : serverList){
					Integer serverNo = model.getServerNo();
					JSONObject json = new JSONObject();
					json.put("serverNo", model.getServerNo());
					json.put("serverName", model.getServerName());
					json.put("gameHost", model.getGameHost());
					json.put("gamePort", model.getGamePort());
					json.put("loginFlag", svrList.contains(serverNo)? 1 : 0);
					json.put("endStopTime", 0);
					json.put("severType", model.getSeverType());
					long openServerDate = DateService.getDateByString(model.getOpenServerDate()).getTime();
					json.put("openServerDate", openServerDate);
					int serverState = model.getSeverState();
					if(model.getEndStopDate() != null){
						long endStopTime = DateService.getDateByString(model.getEndStopDate()).getTime();
						if(curTime < endStopTime){
							serverState = 4;
							json.put("endStopTime", endStopTime);
						}
					}
					json.put("severState", serverState);
					
					
					jsonList.add(json);
				}
				result.put("serverList", jsonList.toString());
				CommonUtil.postData(resp, result.toString());
			}else{
				//登录失败
				result.put("result", 6);
				CommonUtil.postData(resp, result.toString());
			}
			
		}
	}
}
