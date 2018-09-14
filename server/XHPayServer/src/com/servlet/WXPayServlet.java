package com.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jetty.server.AsyncContinuation;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import com.common.Config;
import com.common.DateService;
import com.common.GCCContext;
import com.common.MD5Service;
import com.constant.PathConstant;
import com.domain.PayLog;
import com.domain.config.BaseAgentConfig;
import com.domain.config.BaseServerConfig;
import com.service.IBaseDataService;
import com.service.IPayService;
import com.util.LogUtil;
import com.util.PayCommonUtil;

/**
 * 支付宝充值异步回调
 * @author ken
 * @date 2017-6-20
 */
public class WXPayServlet extends AbstractServlet {
	
	private static final long serialVersionUID = -3738399512950345746L;
	
	//异步
	private AsyncContinuation asyncContext;
	
	@Override
	public AsyncContinuation getContext() {
		return asyncContext;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
		
		if (resp.isCommitted()) return;
		initReqResp(req, resp);
		
		if(Config.AGENT.equals("xh")){
			doPay(req, resp);
		}
	}
	
	/**
	 * 处理微信支付回调
	 */
	private void doPay(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
		InputStream inStream = req.getInputStream();  
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();  
		byte[] buffer = new byte[1024];  
		int len = 0;  
		while ((len = inStream.read(buffer)) != -1) {  
		    outSteam.write(buffer, 0, len);  
		}  
		

		String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息  
		
		//System.out.println("支付回调："+result);  
		
		outSteam.close();  
		inStream.close();  
		
		try {
			Map<String, Object> map = PayCommonUtil.getMapFromXML(result);  
			
			if(map.get("return_code").toString().equalsIgnoreCase("SUCCESS") && map.get("result_code").toString().equalsIgnoreCase("SUCCESS")){
			    if (verifyWeixinNotify(map)) {  
			    	String trade_no = (String)map.get("transaction_id");
					String out_trade_no = (String)map.get("out_trade_no");
					String attach = (String)map.get("attach");
					
					try {
						
						String contents[] = attach.split("\\|");
						
						Long userId = Long.valueOf(contents[0]); // 玩家账号
						String site = contents[1]; // 游戏站点
						Long playerId = Long.valueOf(contents[2]); // 玩家编号
						String payItemId = contents[3]; // 商品编号
						Integer payType = Integer.valueOf(contents[4]); // 支付类型
						Integer money = Integer.valueOf(contents[5]); // 金额
						String sign1 = contents[6]; // 签名
						
						IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();

						BaseAgentConfig baseAgentConfig = baseDataService.getBaseAgentConfig(Config.AGENT);
						if (baseAgentConfig == null){
							LogUtil.error("微信支付游戏运营商不存在："+Config.AGENT);
							return;
						}
						
						BaseServerConfig gameConfigVariable = baseDataService.getBaseServerConfig(site);
						if (gameConfigVariable == null){
							LogUtil.error("微信支付游戏站点不存在："+site);
							return;
						}
						
						String sign2 = MD5Service.encryptToUpperString(userId + site + playerId + payItemId + payType + money +baseAgentConfig.getChargeKey());
						if(!sign1.equals(sign2)){
							LogUtil.error("微信支付签名有误："+sign2);
							return;
						}
						
						this.postData(resp, this.getOKMsg()); // 告诉微信服务器，我收到信息了，不要在调用回调action了  
						
						//开始通知游戏服发货
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("content", attach);
						
						
						sendData_noWaitBack(jsonObject, this.getUrlByGameSitePath(gameConfigVariable.getGameInnerIp(),gameConfigVariable.getWebPort(), PathConstant.PAY));
						
						IPayService payService = GCCContext.getInstance().getServiceCollection().getPayService();
						PayLog payLog = payService.getPayLogByOutOrderNo(out_trade_no);
						if(payLog == null){
							payService.insertPayLog(userId, playerId, out_trade_no, trade_no, Integer.valueOf(money), payType, payItemId, site, map.toString());
						}else{
							payLog.setOrderNo(trade_no);
							payLog.setPayUrl(map.toString());
							payLog.setUpdateTime(DateService.getCurrentUtilDate());
							payLog.setState(1);
							payService.updatePayLog(payLog);
						}
					} catch (Exception e) {
						LogUtil.error("微信支付发货异常：", e);
					}
					
			    }else{
			    	LogUtil.error("微信支付回调签名有误："+map.toString());
			    } 
			}else{
				LogUtil.error("微信支付回调支付不成功："+map.toString());
			}
		} catch (ParserConfigurationException e) {
			LogUtil.error("微信支付回调异常：",e);
		} catch (SAXException e) {
			LogUtil.error("微信支付回调异常：",e);
		}
		
	}
	
	/**
	 * 验证回调签名
	 */
	private boolean verifyWeixinNotify(Map<String, Object> map) {  
        SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();  
        String sign = (String) map.get("sign");  
        for (Object keyValue : map.keySet()) {  
            if(!keyValue.toString().equals("sign")){  
                parameterMap.put(keyValue.toString(), map.get(keyValue));  
            }  
              
        }  
        String createSign = PayCommonUtil.createSign("UTF-8", parameterMap);  
        if(createSign.equals(sign)){  
            return true;  
        }else{  
            return false;  
        }  
          
    }  
	
	/**
	 * 回调接受成功 回包给微信
	 */
	private String getOKMsg(){
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		sb.append("<return_code><![CDATA[SUCCESS]]></return_code>");
		sb.append("<return_msg><![CDATA[OK]]></return_msg>");
		sb.append("</xml>");
		return sb.toString();
	}
}
