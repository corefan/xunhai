package com.servlet;

import java.io.IOException;
import java.util.List;

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
 * 游戏服创建玩家通知
 * @author ken
 * @date 2017-6-23
 */
public class CreatePlayerServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {
			doCreatePlayer(req, resp);

		} catch (Exception e) {
			LogUtil.error("创角色通知异常: ",e);
			return;
		}
	}
	
	/**
	 * 创建通知处理
	 */
	private void doCreatePlayer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, Exception {
		IAccountService accountService = GCCContext.getInstance().getServiceCollection().getAccountService();
		
		JSONObject jsonObject = this.dealMsg(req);
		if(jsonObject == null) return;
		Long userId = jsonObject.getLong("userId");
		Integer serverNo = jsonObject.getInt("serverNo");
		String sign = jsonObject.getString("sign");
		
		String realSign = MD5Service.encryptToLowerString(userId+serverNo+Config.WEB_LOGIN_KEY);
		
		if(!realSign.equalsIgnoreCase(sign)){
			return;
		}
		
		Account account = accountService.getAccountByUserId(userId);
		if(account == null)return;
		
		List<Integer> serverList = account.getServerList();
		
		if(serverList.contains(serverNo)){
			return;
		}
		
		serverList.add(serverNo);
		account.setServerList(serverList);
		
		accountService.updateAccount(account);		
	}
}
