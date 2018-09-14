package com.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

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
import com.util.LogUtil;

/**
 * 游戏服创建玩家通知
 * @author ken
 * @date 2017-6-23
 */
public class CreatePlayerServlet extends HttpServlet {

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
		
		JSONObject jsonObject = dealMsg(req);
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
	
	/** 解析消息 */
	private JSONObject dealMsg(HttpServletRequest req) {
		
		JSONObject jsonObject = null;
		OutputStream os = null;
		InputStream is = null;
		try {
			String msg = null;

			os = new ByteArrayOutputStream();
			is = req.getInputStream();
			if (is != null) {
				byte[] b = new byte[1024];
				int len = 0;
				while ((len = is.read(b)) != -1) {
					os.write(b,0,len);
				}
				msg = os.toString();
			}
			
			String result = new String(msg.getBytes(Charset.defaultCharset()), "UTF-8");
			jsonObject = new JSONObject(result);
			
		} catch (Exception e) {
			LogUtil.error("异常:",e);
		} finally {
			try {
				is.close();
				os.close();
			} catch (IOException e) {
				LogUtil.error("异常:",e);
			}
		}

		return jsonObject;
	}
}
