package com.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpEventListener;
import org.eclipse.jetty.client.HttpEventListenerWrapper;
import org.eclipse.jetty.client.HttpExchange;
import org.eclipse.jetty.http.HttpMethods;
import org.eclipse.jetty.io.Buffer;
import org.eclipse.jetty.io.ByteArrayBuffer;
import org.json.JSONObject;

import com.core.HttpClientFactory;

/**
 * http辅助类
 * @author ken
 * @date 2017-7-21
 */
public class HttpUtil {

	  /**
	   * 发送http post请求  
	   */
	   public static String httpsRequest(String requestUrl, String param, String content_type) {  
	          try {  
	               
	              URL url = new URL(requestUrl);  
	              HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
	              
	              conn.setDoOutput(true);  
	              conn.setDoInput(true);  
	              conn.setUseCaches(false);  
	              conn.setRequestMethod("POST");  
	              conn.setRequestProperty("content-type", content_type);  

	              // 当outputStr不为null时向输出流写数据  
	              if (null != param) {  
	                  OutputStream outputStream = conn.getOutputStream();  
	                  // 注意编码格式  
	                  outputStream.write(param.getBytes("UTF-8"));  
	                  outputStream.close();  
	              }  
	              // 从输入流读取返回内容  
	              InputStream inputStream = conn.getInputStream();  
	              InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
	              BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
	              String str = null;  
	              StringBuffer buffer = new StringBuffer();  
	              while ((str = bufferedReader.readLine()) != null) {  
	                  buffer.append(str);  
	              }  
	              // 释放资源  
	              bufferedReader.close();  
	              inputStreamReader.close();  
	              inputStream.close();  
	              inputStream = null;  
	              conn.disconnect();  
	              return buffer.toString();  
	          } catch (ConnectException ce) {  
	              System.out.println("连接超时：{}"+ ce);  
	          } catch (Exception e) {  
	              System.out.println("https请求异常：{}"+ e);  
	          }  
	          return null;  
	      }
    
    
	/**  
	 * 发送数据
	 * 不等待返回
	 * */
	public static void sendData_noWaitBack(JSONObject jsonObject,String url) {

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
