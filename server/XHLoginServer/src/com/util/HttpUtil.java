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

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpEventListener;
import org.eclipse.jetty.client.HttpEventListenerWrapper;
import org.eclipse.jetty.client.HttpExchange;
import org.eclipse.jetty.http.HttpMethods;
import org.eclipse.jetty.io.Buffer;
import org.eclipse.jetty.io.ByteArrayBuffer;
import org.json.JSONObject;

import com.core.HttpClientFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * http辅助类
 * @author ken
 * @date 2017-7-21
 */
public class HttpUtil {

	/**
	 * 发送https Post  （免证书）
	 */
	 public static String httpsPost (String url, String param, String content_type, String base64) {
         StringBuffer buffer = new StringBuffer();

         // 声明SSL上下文
         javax.net.ssl.SSLContext ssl_context = null;
         try {
             ssl_context = javax.net.ssl.SSLContext.getInstance("SSL");
             ssl_context.init(null, new javax.net.ssl.TrustManager[]{
                 new javax.net.ssl.X509TrustManager() {
                     @Override
                     public void checkClientTrusted (java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                     }
                     @Override
                     public void checkServerTrusted (java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                     }
                     @Override
                     public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                         return null;
                     }
                 }   
             }, null);
         } catch (Exception e) {
             e.printStackTrace();
         }
         javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(ssl_context.getSocketFactory());

         // 实例化主机名验证接口
         javax.net.ssl.HostnameVerifier hostname_verifier = new javax.net.ssl.HostnameVerifier(){
             @Override
             public boolean verify(String hostname, javax.net.ssl.SSLSession session) {
                 return true;
             }
         };
         javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(hostname_verifier);

         javax.net.ssl.HttpsURLConnection http_url_connection = null;
         try {
        	 byte[] parameter = param.getBytes("utf-8");
             http_url_connection = (javax.net.ssl.HttpsURLConnection)(new java.net.URL(url)).openConnection();
             http_url_connection.setDoInput(true);
             http_url_connection.setDoOutput(true);
             http_url_connection.setRequestMethod("POST");
//             http_url_connection.setRequestProperty("Content-Length", String.valueOf(parameter.length));
             http_url_connection.setRequestProperty("Content-Type", content_type);
             http_url_connection.setUseCaches(false);
             if (null!=base64) {
                 http_url_connection.setRequestProperty("Authorization", "Basic "+new String(java.util.Base64.getEncoder().encode(base64.getBytes("utf-8")), "utf-8"));
             }

             // write request.
             java.io.BufferedOutputStream output_stream = new java.io.BufferedOutputStream(http_url_connection.getOutputStream());
             output_stream.write(parameter);
             output_stream.flush();
             output_stream.close();
             output_stream = null;

             java.io.InputStreamReader input_stream_reader = new java.io.InputStreamReader(http_url_connection.getInputStream(), "utf-8");
             java.io.BufferedReader buffered_reader = new java.io.BufferedReader(input_stream_reader);
             buffer = new StringBuffer();
             String line;
             while ((line = buffered_reader.readLine()) != null) {
                 buffer.append(line);
             }
             line=null;
             input_stream_reader.close();
             input_stream_reader = null;
             buffered_reader.close();
             buffered_reader = null;
             http_url_connection.disconnect();
         } catch (Exception e) {  
             e.printStackTrace();  
         }

         return buffer.toString();
     }
    
	  /**
	   * 发送http请求  
	   */
	   public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {  
	          try {  
	               
	              URL url = new URL(requestUrl);  
	              HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
	              
	              conn.setDoOutput(true);  
	              conn.setDoInput(true);  
	              conn.setUseCaches(false);  
	              // 设置请求方式（GET/POST）  
	              conn.setRequestMethod(requestMethod);  
	              conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");  

	              // 当outputStr不为null时向输出流写数据  
	              if (null != outputStr) {  
	                  OutputStream outputStream = conn.getOutputStream();  
	                  // 注意编码格式  
	                  outputStream.write(outputStr.getBytes("UTF-8"));  
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
//	            // 获取所有响应头字段
//	            Map<String, List<String>> map = connection.getHeaderFields();
//	            // 遍历所有的响应头字段
//	            for (String key : map.keySet()) {
//	                System.out.println(key + "--->" + map.get(key));
//	            }
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
	  * okhttp3  post
	  */
	  public static void okHttpPost(String url, String param, String type) throws IOException {
	        OkHttpClient client = HttpUtil.getUnsafeOkHttpClient();
	        MediaType mediaType = MediaType.parse(type);
	        RequestBody body = RequestBody.create(mediaType, param);
	        Request request = new Request.Builder().url(url).post(body).build();
	        client.newCall(request).enqueue(new Callback() {
	            @Override
	            public void onFailure(Call call, IOException e) {
	                LogUtil.error("fail:",e);
	            }

	            @Override
	            public void onResponse(Call call, Response response) throws IOException {

	            	System.out.println(response.body().string());
	            }
	        });
	        
	    }
	  
	  /**
	     * 获取OkHttpClient
	     *
	     * @return OkHttpClient
	     */
	    public static OkHttpClient getUnsafeOkHttpClient() {

	        try {
	            final TrustManager[] trustAllCerts = new TrustManager[]{
	                    new X509TrustManager() {
	                        @Override
	                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
	                        }

	                        @Override
	                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
	                        }

	                        @Override
	                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                            return new java.security.cert.X509Certificate[]{};
	                        }
	                    }
	            };

	            final SSLContext sslContext = SSLContext.getInstance("SSL");
	            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
	            final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
	            OkHttpClient.Builder builder = new OkHttpClient.Builder();
	            builder.sslSocketFactory(sslSocketFactory);

	            builder.hostnameVerifier(new HostnameVerifier() {
	                @Override
	                public boolean verify(String hostname, SSLSession session) {
	                    return true;
	                }
	            });

	            return builder.build();
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }

	    }
	    
	    public static void main(String[] args) {
	    	String param = "?gameId=86&subGameId=149&accessToken=17354d08e8fb4c1d8e9764ce8d99e9d83d62ac50337847d3baaba19dafddebb5&sign=d92a8364cbb7418ff4382a99ac5279fa";
			try {
				//String str = HttpUtil.httpsRequest("https://api.sdk.dhios.cn/open/verifyAccessToken", "POST", param);
				//String str = HttpUtil.httpsPost("https://api.sdk.dhios.cn/open/verifyAccessToken", param, "application/x-www-urlencoded", null);
				//String str = HttpUtil.SendHttpsPOST("https://api.sdk.dhios.cn/open/verifyAccessToken", param);
				 HttpUtil.okHttpPost("https://api.sdk.dhios.cn/open/verifyAccessToken", param, "application/x-www-urlencoded");
//				System.out.println(str);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    }
}
