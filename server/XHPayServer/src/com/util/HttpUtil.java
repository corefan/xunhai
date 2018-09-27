package com.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

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
	
	   /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
//            // 获取所有响应头字段
//            Map<String, List<String>> map = connection.getHeaderFields();
//            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
	
	public static void main(String[] args) {
		String str= HttpUtil.httpsRequest("http://sdk.171game.com:8003/AppId", "userName=xx95276197", "application/x-www-form-urlencoded");
		System.out.println(str);
	}
}
