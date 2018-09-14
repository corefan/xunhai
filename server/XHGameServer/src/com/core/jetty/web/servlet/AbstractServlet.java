package com.core.jetty.web.servlet;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpEventListener;
import org.eclipse.jetty.client.HttpEventListenerWrapper;
import org.eclipse.jetty.client.HttpExchange;
import org.eclipse.jetty.http.HttpMethods;
import org.eclipse.jetty.io.Buffer;
import org.eclipse.jetty.io.ByteArrayBuffer;
import org.eclipse.jetty.server.AsyncContinuation;
import org.json.JSONObject;

import com.core.jetty.core.HttpClientFactory;
import com.core.jetty.core.WebConstant;
import com.util.LogUtil;

public abstract class AbstractServlet extends HttpServlet {

	private static final long serialVersionUID = -7605100679483270776L;

	/** 得到异步context */
	public abstract AsyncContext getContext();
	
	/** 得到url */
	public abstract String getUrl(); 
	
	/** 初始化 */
	public void initReqResp(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		
		resp.setContentType("text/html;charset=utf-8");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentLength(WebConstant.RESP_BUFFER_SIZE);
		resp.setBufferSize(WebConstant.RESP_BUFFER_SIZE);
		resp.setStatus(HttpServletResponse.SC_OK);
		
	}
	
	/** 验证context */
	public boolean checkContext() {
		boolean flag = false;
		AsyncContext asyncContext = getContext();
		if (asyncContext != null) {
			AsyncContinuation continuation = (AsyncContinuation) asyncContext;
			if (continuation.isSuspended()) flag = true;
		}
		
		return flag;
	}
	
	/** 发送数据 */
	public void sendData(JSONObject jsonObject) {

		try {

			Buffer buffer = new ByteArrayBuffer(jsonObject.toString().getBytes());

			HttpEventListener eventListener = new HttpEventListenerWrapper() {
				@Override
				public void onResponseContent(Buffer content)
						throws IOException {
					try {
						String result = content.toString("UTF-8");
						getContext().getResponse().setContentType("text/html;charset=utf-8");
						getContext().getResponse().setCharacterEncoding("UTF-8");
						getContext().getResponse().getWriter().println(result);
					} catch (Exception e) {
						LogUtil.error("返回数据异常:",e);
					}
				}

				@Override
				public void onResponseComplete() throws IOException {
					super.onResponseComplete();
					getContext().complete();
				}

				@Override
				public void onExpire() {
					super.onExpire();
					getContext().complete();
				}

				@Override
				public void onException(Throwable ex) {
					super.onException(ex);
					getContext().complete();
				}
			};
			
			HttpExchange exchange = new HttpExchange();
			exchange.setEventListener(eventListener);
			exchange.setURL(getUrl());
			exchange.setMethod(HttpMethods.POST);
			exchange.setRequestContentType("text/html;charset=utf-8");
			exchange.setRequestContent(buffer);
			
			HttpClient httpClient = HttpClientFactory.getInstance();
			httpClient.send(exchange);
		} catch (Exception e) {
			LogUtil.error("异常:",e);
		}
	
	}
}
