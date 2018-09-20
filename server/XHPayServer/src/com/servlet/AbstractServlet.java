package com.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpEventListener;
import org.eclipse.jetty.client.HttpEventListenerWrapper;
import org.eclipse.jetty.client.HttpExchange;
import org.eclipse.jetty.http.HttpMethods;
import org.eclipse.jetty.io.Buffer;
import org.eclipse.jetty.io.ByteArrayBuffer;
import org.eclipse.jetty.server.AsyncContinuation;
import org.json.JSONException;
import org.json.JSONObject;

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
		// 设置contentLength后google内核的浏览器有问题 ERR_CONTENT_LENGTH_MISMATCH
		//asyncContext.getResponse().setContentLength(WebConstant.RESP_BUFFER_SIZE);
		asyncContext.setTimeout(10000);
		
		return asyncContext;
	}
	
	/**  
	 * 发送数据：等待返回
	 * */
	public void sendData(JSONObject jsonObject,String url) {

		try {

			//System.out.println("url:"+url);
			//url = "http://192.168.1.128:12010/pay";
			Buffer buffer = new ByteArrayBuffer(jsonObject.toString().getBytes("UTF-8"));

			HttpEventListener eventListener = new HttpEventListenerWrapper() {
				@Override
				public void onResponseContent(Buffer content)
						throws IOException {
					try {
						String result = content.toString("UTF-8");
						//System.out.println(result+" cccccccccc");
						getContext().getResponse().setContentType("text/html;charset=utf-8");
						//getContext().getResponse().setContentLength(result.length());
						getContext().getResponse().setCharacterEncoding("UTF-8");
						getContext().getResponse().getWriter().println(result);
						getContext().getResponse().getWriter().flush();
						//System.out.println(result+"xxx");
						
					} catch (Exception e) {
						LogUtil.error("返回数据异常:",e);
					}
				}

				@Override
				public void onResponseComplete() throws IOException {
					//System.out.println("请求结束:"+System.currentTimeMillis());
					getContext().complete();
				}

				@Override
				public void onExpire() {
					//System.out.println("超时:"+System.currentTimeMillis());
					if (getContext().isSuspended() || getContext().isSuspending()) {
						getContext().complete();
					}
				}

				@Override
				public void onException(Throwable ex) {
					//super.onException(ex);
					//System.out.println("异常:"+System.currentTimeMillis());
					//getContext().complete();
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
	
	public void postData(HttpServletResponse response, String result) throws IOException {
		response.setCharacterEncoding("UTF-8");
		//response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		out.print(result);
		out.flush();
		out.close();
	}
	/**
	 * 根据gameSite得到url
	 * */
	public String getUrlByGameSitePath(String ip, int webPort, String path) {
		
		return "http://"+ip+":"+webPort+path;
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
	
	/** 解析消息   multipart/form-data格式
	 * @throws IOException 
	 * @throws FileUploadException 
	 * @throws JSONException */
	public JSONObject dealMsg2(HttpServletRequest req) throws FileUploadException, IOException, JSONException {
		
		JSONObject jsonObject = new JSONObject();
		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload();
		// Parse the request
		FileItemIterator iter = upload.getItemIterator(req);
		while (iter.hasNext()) {
		    FileItemStream item = iter.next();
		    String name = item.getFieldName();
		    InputStream stream = item.openStream();
		    if (item.isFormField()) {
		        jsonObject.put(name, Streams.asString(stream));
		    } 
		}

		return jsonObject;
	}

	protected String GetEncode(String str)
	{
		try {
			return URLEncoder.encode(str,"utf-8");
		} catch (Exception e) {
		}
		return "";
	}
	
	protected String GetDecode(String str)
	{
		try {
			return URLDecoder.decode(str,"utf-8");
		} catch (Exception e) {
		}
		return "";
	}
	
	protected String getMD5(String str) {
	    try {
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        md.update(str.getBytes());
	        return new BigInteger(1, md.digest()).toString(16);
	    } catch (Exception e) {

	    }
	    
	    return "";
	}
}
