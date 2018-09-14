package com.core.jetty.web.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.common.Config;
import com.core.jetty.core.WebConstant;
import com.util.HttpUtil;
import com.util.LogUtil;

public class BaseServlet extends HttpServlet {

	private static final long serialVersionUID = 4948409059400291790L;

	/** 初始化 */
	public void initReqResp(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");

		resp.setContentType("text/html;charset=utf-8");
		resp.setCharacterEncoding("UTF-8");
		resp.setBufferSize(WebConstant.RESP_BUFFER_SIZE);
		resp.setContentLength(WebConstant.RESP_BUFFER_SIZE);
		resp.setStatus(HttpServletResponse.SC_OK);
		

	}

	/** 检测ip **/
	public boolean checkIP(HttpServletRequest req){
		String IP = HttpUtil.getRequestIp(req);
		
		if (Config.WEB_CHARGE_IP_LIST != null && !Config.WEB_CHARGE_IP_LIST.contains("*")) {
			if (!Config.WEB_CHARGE_IP_LIST.contains(IP)){
				return false;
			}
		}
		return true;
	}
	
	/** 解析消息 */
	public JSONObject dealMsg(HttpServletRequest req) {
		
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
	
	public void postData(HttpServletResponse response, String data)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		//response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		out.print(data);
		out.flush();
		out.close();
	}
}
