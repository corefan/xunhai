package com.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.common.Config;
import com.common.MD5Service;


/**
 * 微信支付帮助类
 * @author ken
 * @date 2017-7-5
 */
public class PayCommonUtil {

    //随机字符串生成  
    public static String getRandomString(int length) { //length表示生成字符串的长度      
           String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";         
           Random random = new Random();         
           StringBuffer sb = new StringBuffer();         
           for (int i = 0; i < length; i++) {         
               int number = random.nextInt(base.length());         
               sb.append(base.charAt(number));         
           }         
           return sb.toString();         
    }
    
    //请求xml组装  
    public static String getRequestXml(SortedMap<String,Object> parameters){  
          StringBuffer sb = new StringBuffer();  
          sb.append("<xml>");  
          Set es = parameters.entrySet();  
          Iterator it = es.iterator();  
          while(it.hasNext()) {  
              Map.Entry entry = (Map.Entry)it.next();  
              String key = (String)entry.getKey();  
              String value = (String)entry.getValue();  
              if ("attach".equalsIgnoreCase(key)||"body".equalsIgnoreCase(key)||"sign".equalsIgnoreCase(key)) {  
                  sb.append("<"+key+">"+"<![CDATA["+value+"]]></"+key+">");  
              }else {  
                  sb.append("<"+key+">"+value+"</"+key+">");  
              }  
          }  
          sb.append("</xml>");  
          return sb.toString();  
      } 
    
    //生成签名  
    public static String createSign(String characterEncoding,SortedMap<String,Object> parameters){  
          StringBuffer sb = new StringBuffer();  
          Set es = parameters.entrySet();  
          Iterator it = es.iterator();  
          while(it.hasNext()) {  
              Map.Entry entry = (Map.Entry)it.next();  
              String k = (String)entry.getKey();  
              Object v = entry.getValue();  
              if(null != v && !"".equals(v)  
                      && !"sign".equals(k) && !"key".equals(k)) {  
                  sb.append(k + "=" + v + "&");  
              }  
          }  
          sb.append("key=" + Config.WX_KEY);  
          String sign = null;
		try {
			sign = MD5Service.encryptToUpperString(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}  
          return sign;  
      }  
    
    /**
     * 解析xml
     */
    public static Map<String,Object> getMapFromXML(String xmlString) throws ParserConfigurationException, IOException, SAXException {
        //这里用Dom的方式解析回包的最主要目的是防止API新增回包字段
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream is =  new ByteArrayInputStream(xmlString.getBytes());
        Document document = builder.parse(is);
        //获取到document里面的全部结点
        NodeList allNodes = document.getFirstChild().getChildNodes();
        Node node;
        Map<String, Object> map = new HashMap<String, Object>();
        int i=0;
        while (i < allNodes.getLength()) {
            node = allNodes.item(i);
            if(node instanceof Element){
                map.put(node.getNodeName(),node.getTextContent());
            }
            i++;
        }
        return map;
    }
    
    
    //请求方法  
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
    
    public static void main(String[] args) {
//    	String str = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg><appid><![CDATA[wx13842d984810c06b]]></appid><mch_id><![CDATA[1355962702]]></mch_id><device_info><![CDATA[WEB]]></device_info><nonce_str><![CDATA[uUaaXpCCnERVXE6R]]></nonce_str><sign><![CDATA[E9A1087F5A0C5F8EBCF26F3C7A842022]]></sign><result_code><![CDATA[SUCCESS]]></result_code><openid><![CDATA[oD7UhwESE3wh22Uz6PqdCXRyR-pA]]></openid><is_subscribe><![CDATA[Y]]></is_subscribe><trade_type><![CDATA[NATIVE]]></trade_type><bank_type><![CDATA[CFT]]></bank_type><total_fee>2</total_fee><fee_type><![CDATA[CNY]]></fee_type><transaction_id><![CDATA[4002722001201610247590660332]]></transaction_id><out_trade_no><![CDATA[1477296302523]]></out_trade_no><attach><![CDATA[]]></attach><time_end><![CDATA[20161024155820]]></time_end><trade_state><![CDATA[SUCCESS]]></trade_state><cash_fee>2</cash_fee></xml>";
//    	try {
//			getMapFromXML(str);
//		} catch (ParserConfigurationException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (SAXException e) {
//			e.printStackTrace();
//		}
    	  try {
        JSONObject jsonObject = new JSONObject();  
        SortedMap<String, Object> param = new TreeMap<String, Object>();  
        param.put("appid", Config.WX_APP_ID);  
        param.put("partnerid", Config.WX_MCH_ID);  
        param.put("prepayid", "xxx");  
        param.put("package", "Sign=WXPay");  
        param.put("noncestr", PayCommonUtil.getRandomString(32));  
        param.put("timestamp", System.currentTimeMillis());  
        String secondSign = PayCommonUtil.createSign("UTF-8", param);  
        param.put("sign", secondSign);  
      
			jsonObject.put("param",param);
			
			System.out.println(jsonObject.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
