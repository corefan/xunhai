package com.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.AsyncContinuation;

import com.core.WebConstant;

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
		//resp.setContentLength(WebConstant.RESP_BUFFER_SIZE);
		resp.setStatus(HttpServletResponse.SC_OK);
		
	}
	
	/** 验证context */
	public boolean checkContext() {
		boolean flag = false;
		AsyncContinuation asyncContext = getContext();
		if (asyncContext != null) {
			//System.out.println(asyncContext.isContinuation()+" ddd ");
			if (asyncContext.isSuspended() 
					|| asyncContext.isSuspending() 
					|| asyncContext.isComplete() 
					|| asyncContext.isCompleting() 
					|| asyncContext.isExpired()) flag = true;
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
		//asyncContext.getResponse().setContentLength(WebConstant.RESP_BUFFER_SIZE);
		asyncContext.setTimeout(10000);
		
		return asyncContext;
	}
	
}
