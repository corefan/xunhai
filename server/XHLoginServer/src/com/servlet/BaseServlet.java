package com.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.core.WebConstant;
import com.util.LogUtil;

public class BaseServlet extends HttpServlet {

	private static final long serialVersionUID = -6394361372932933593L;
	

	
	/** 初始化 */
	protected void initReqResp(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");

		resp.setContentType("text/html;charset=utf-8");
		resp.setCharacterEncoding("UTF-8");
		resp.setBufferSize(WebConstant.RESP_BUFFER_SIZE);
		resp.setStatus(HttpServletResponse.SC_OK);
	}
	
	/**
	 * 发送信息
	 */
	protected void postData(HttpServletResponse response, String result) throws IOException {
		response.setCharacterEncoding("UTF-8");
		//response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		out.print(result);
		out.flush();
		out.close();
	}
	
	
	/** 解析消息 */
	protected JSONObject dealMsg(HttpServletRequest req) {
		
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
	
	/**
	 * md5
	 */
	protected String getMD5(String str) {
	    try {
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        md.update(str.getBytes());
	        return new BigInteger(1, md.digest()).toString(16);
	    } catch (Exception e) {

	    }
	    
	    return "";
	}
	
	protected String GetEncode(String str)
	{
		try {
			return URLEncoder.encode(str,"utf-8");
		} catch (Exception e) {
		}
		return "";
	}
	
	/**
	 * 时间搓  秒
	 */
	protected int ParseNow()
    {
    	return (int) (Calendar.getInstance().getTimeInMillis() / 1000);
    }
}
