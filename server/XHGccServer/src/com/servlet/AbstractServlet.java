package com.servlet;

import java.io.IOException;

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

import com.common.GCCContext;
import com.core.HttpClientFactory;
import com.core.WebConstant;
import com.util.LogUtil;

public abstract class AbstractServlet extends HttpServlet {

	private static final long serialVersionUID = -7605100679483270776L;

	/** 得到异步context (向游戏服发数据, 需要 等待服务器返回列表，需要开异步)*/
	public abstract AsyncContinuation getContext();
	
	/** 初始化 */
	public void initReqResp(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		
		resp.setContentType("text/html;charset=utf-8");
		resp.setCharacterEncoding("UTF-8");
		resp.setBufferSize(WebConstant.RESP_BUFFER_SIZE);
		resp.setContentLength(WebConstant.RESP_BUFFER_SIZE);
		resp.setStatus(HttpServletResponse.SC_OK);
		
	}
	
	/** 验证context */
	public boolean checkContext() {
		boolean flag = false;
		AsyncContinuation asyncContext = getContext();
		if (asyncContext != null) {
			if (asyncContext.isSuspended() || asyncContext.isSuspending()) flag = true;
		}
		
		return flag;
	}
	
	/** 初始化context */
	public AsyncContinuation initContext(HttpServletRequest req) throws ServletException, IOException {
		
		AsyncContinuation asyncContext = (AsyncContinuation) req.startAsync();
		
		req.setCharacterEncoding("UTF-8");
		
		asyncContext.getResponse().setContentType("text/html;charset=utf-8");
		asyncContext.getResponse().setCharacterEncoding("UTF-8");
		asyncContext.getResponse().setBufferSize(WebConstant.RESP_BUFFER_SIZE);
		asyncContext.getResponse().setContentLength(WebConstant.RESP_BUFFER_SIZE);
		asyncContext.setTimeout(5000);
		
		return asyncContext;
	}
	
	/** 
	 * 记录操作日志 
	 * */
	public void recordOptLog(Integer userID, String userName, int opt, String content, String detail) {
		String ip = "未知ip";
		if (getContext() != null && getContext().getRequest().getRemoteHost() != null) {
			ip = getContext().getRequest().getRemoteHost();
		}
		GCCContext.getInstance().getServiceCollection().getLogService().recordOptLog(userID, userName, opt, content, detail, ip);
	}
	
	/**  
	 * 发送数据：等待返回
	 * */
	public void sendData(JSONObject jsonObject,String url) {

		try {

			Buffer buffer = new ByteArrayBuffer(jsonObject.toString().getBytes("UTF-8"));

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
					System.out.println("请求结束:"+System.currentTimeMillis());
					getContext().complete();
				}

				@Override
				public void onExpire() {
					System.out.println("超时:"+System.currentTimeMillis());
					if (getContext().isSuspended() || getContext().isSuspending()) {
						getContext().complete();
					}
				}

				@Override
				public void onException(Throwable ex) {
					super.onException(ex);
					//System.out.println("异常:"+System.currentTimeMillis());
					getContext().complete();
				}

				@Override
				public void onConnectionFailed(Throwable ex) {
					super.onConnectionFailed(ex);
					getContext().complete();
				}
			};
			
			HttpExchange exchange = new HttpExchange();
			exchange.setEventListener(eventListener);
			exchange.setURL(url);
			exchange.setMethod(HttpMethods.POST);
			exchange.setRequestContentType("text/html;charset=utf-8");
			exchange.setRequestContent(buffer);
			
			HttpClient httpClient = HttpClientFactory.getInstance();
			httpClient.send(exchange);
		} catch (Exception e) {
			LogUtil.error("异常:",e);
			getContext().complete();
		}
	
	}
	
	/**  
	 * 发送数据
	 * 不等待返回
	 * */
	public void sendData_noWaitBack(JSONObject jsonObject,String url) {

		try {

			Buffer buffer = new ByteArrayBuffer(jsonObject.toString().getBytes("UTF-8"));

			HttpEventListener eventListener = new HttpEventListenerWrapper() {
				@Override
				public void onResponseContent(Buffer content)
						throws IOException {
				}
			};
			
			HttpExchange exchange = new HttpExchange();
			exchange.setEventListener(eventListener);
			exchange.setURL(url);
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
