package com.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Locale;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONObject;

import sun.misc.BASE64Decoder;

/**
 * 苹果支付验证辅助类
 * @author ken
 * @date 2017-7-12
 */
public class IOS_Verify {

	private static class TrustAnyTrustManager implements X509TrustManager {  
        
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {  
        }  
      
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {  
        }  
      
        public X509Certificate[] getAcceptedIssuers() {  
            return new X509Certificate[]{};  
        }  
    }  
      
    private static class TrustAnyHostnameVerifier implements HostnameVerifier {  
        public boolean verify(String hostname, SSLSession session) {  
            return true;  
        }  
    }  
    private static final String url_sandbox="https://sandbox.itunes.apple.com/verifyReceipt";  
    private static final String url_verify="https://buy.itunes.apple.com/verifyReceipt";  
      
      
    /** 
     * 苹果服务器验证 
     * @param receipt 账单 
     * @url 要验证的地址 
     * @return null 或返回结果 
     * 沙盒   https://sandbox.itunes.apple.com/verifyReceipt 
     *  
     */  
    public static String buyAppVerify(String receipt)  
    {  
       String url=url_verify; 
       String environment = getEnvironment(receipt);
       if(environment !=null && environment.equals("Sandbox")){  
           url=url_sandbox;  
       }  
       String buyCode=getBASE64(receipt);  
       try{  
           SSLContext sc = SSLContext.getInstance("SSL");  
           sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());  
           URL console = new URL(url);  
           HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();  
           conn.setSSLSocketFactory(sc.getSocketFactory());  
           conn.setHostnameVerifier(new TrustAnyHostnameVerifier());  
           conn.setRequestMethod("POST");  
           conn.setRequestProperty("content-type", "text/json");  
           conn.setRequestProperty("Proxy-Connection", "Keep-Alive");  
           conn.setDoInput(true);  
           conn.setDoOutput(true);  
           BufferedOutputStream hurlBufOus=new BufferedOutputStream(conn.getOutputStream());  
             
           String str= String.format(Locale.CHINA,"{\"receipt-data\":\"" + buyCode+"\"}");  
           hurlBufOus.write(str.getBytes());  
           hurlBufOus.flush();  
                     
            InputStream is = conn.getInputStream();  
            BufferedReader reader=new BufferedReader(new InputStreamReader(is));  
            String line = null;  
            StringBuffer sb = new StringBuffer();  
            while((line = reader.readLine()) != null){  
              sb.append(line);  
            }  
  
            return sb.toString();  
       }catch(Exception ex)  
       {  
           ex.printStackTrace();  
       }  
       return null;  
    }  
      
    /** 
     * 根据原始收据返回苹果的验证地址: 
     *  * 沙箱    https://sandbox.itunes.apple.com/verifyReceipt 
     * 真正的地址   https://buy.itunes.apple.com/verifyReceipt 
     * @param receipt 
     * @return Sandbox 测试单   Real 正式单 
     */  
    private static String getEnvironment(String receipt)  
    {  
        try{  
            JSONObject job = new JSONObject(receipt);
            if(job.has("environment")){  
                String evvironment=job.getString("environment");  
                return evvironment;  
            }  
        }catch(Exception ex){  
            ex.printStackTrace();  
        }  
        return "Real";  
    }  
      
    /** 
     * 用BASE64加密 
     * @param str 
     * @return 
     */  
    public static String getBASE64(String str) {  
        byte[] b = str.getBytes();  
        String s = null;  
        if (b != null) {  
            s = new sun.misc.BASE64Encoder().encode(b);  
        }  
        return s;  
    }  
  
    /** 
     * 解密BASE64字窜 
     * @param s 
     * @return 
     */  
    public static String getFromBASE64(String s) {  
        byte[] b = null;  
        if (s != null) {  
            BASE64Decoder decoder = new BASE64Decoder();  
            try {  
                b = decoder.decodeBuffer(s);  
                return new String(b);  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return new String(b);  
    }  
      
}
