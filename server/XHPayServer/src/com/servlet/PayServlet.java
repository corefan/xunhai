package com.servlet;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jetty.server.AsyncContinuation;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
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
import com.util.NetworkUtil;
import com.util.PayCommonUtil;

/**
 * @author ken
 * 2015-12-2
 * 充值	
 */
@WebServlet(asyncSupported = true)
public class PayServlet extends AbstractServlet {

	private static final long serialVersionUID = -6625781709891785883L;

	//异步
	private AsyncContinuation asyncContext;

	private AlipayClient alipayClient;
	
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
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {
			
			if (resp.isCommitted()) return;
			initReqResp(req, resp);
			
			if(Config.AGENT.equals("xh")){
				JSONObject jsonObject = dealMsg(req);
				if(jsonObject == null) return;
				int payTpye = jsonObject.getInt("payType");
				String content = jsonObject.getString("content");
				String cpOrderId = jsonObject.getString("cpOrderId");
				
				if(payTpye == 1){
					//支付宝
					getAliPayInfo(cpOrderId, content, resp);
				}else if(payTpye == 2){
					//微信
					getWXPayInfo(cpOrderId, content, req, resp);
				}	
				
			}else if(Config.AGENT.equals("donghai")){
				this.donghaiPay(req, resp);
			}else if(Config.AGENT.equals("yunyou")){
				
				this.yunyouPay(req, resp);
			}
		} catch (Exception e) {
			LogUtil.error(e);
		}
	}

	/**
	 * 支付宝支付
	 */
	private void getAliPayInfo(String cpOrderId, String content, HttpServletResponse resp) throws ServletException, IOException, JSONException {
		
		if (cpOrderId == null || "".equals(cpOrderId.trim()) || content == null || "".equals(content.trim())) return;
		
		String contents[] = content.split("\\|");
		if (contents.length != 7) {
			// 参数个数不对
			LogUtil.error("参数个数不对----------content="+content);
			return;
		}

		Long playerId = Long.valueOf(contents[2]); // 玩家编号
		Integer payItemId = Integer.valueOf(contents[3]); // 商品编号
		Integer payType = Integer.valueOf(contents[4]); // 支付类型
		String money = contents[5]; // 金额
		
		//实例化客户端
		if(alipayClient == null){
			alipayClient = new DefaultAlipayClient(Config.Ali_PAY_URL, Config.Ali_APP_ID, Config.Ali_PRIVATE_KEY, "json", "utf-8", Config.Ali_PUBLIC_KEY, "RSA2");
		}
		
		//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody(content);
		model.setSubject("大唐诛仙-游戏充值");
		model.setOutTradeNo(cpOrderId);
		model.setTimeoutExpress("30m");
		model.setTotalAmount(money);
		model.setProductCode("QUICK_MSECURITY_PAY");
		request.setBizModel(model);
		request.setNotifyUrl(Config.Ali_CALL_BACK_URL);
		
		int state = 0;
		String payInfo = "";
		JSONObject json = new JSONObject();
		try {
	        //这里和普通的接口调用不同，使用的是sdkExecute
	        AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
	        //System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
	        payInfo = response.getBody();
	        json.put("playerId", playerId);
	        json.put("payItemId", payItemId);
	        json.put("payType", payType);
	        json.put("payInfo", payInfo);
	        json.put("cpOrderId", cpOrderId);
	        
		} catch (AlipayApiException e) {
		       LogUtil.error("支付宝唤起支付异常：",e);
		       state = -1;
		       
		}
        json.put("state", state);
		this.postData(resp, json.toString());
		
	}
	
	/**
	 * 微信支付
	 */
	private void getWXPayInfo(String cpOrderId, String content, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, JSONException {
			if (cpOrderId == null || "".equals(cpOrderId.trim()) || content == null || "".equals(content.trim())) return;
			
			String contents[] = content.split("\\|");
			if (contents.length != 7) {
				// 参数个数不对
				
				System.out.println("参数个数不对----------content"+content);
				return;
			}
	
			Long playerId = Long.valueOf(contents[2]); // 玩家编号
			Integer payItemId = Integer.valueOf(contents[3]); // 商品编号
			Integer payType = Integer.valueOf(contents[4]); // 支付类型
			Integer money = Integer.valueOf(contents[5]); // 金额
			
			SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();  
	        parameterMap.put("appid", Config.WX_APP_ID);  
	        parameterMap.put("mch_id", Config.WX_MCH_ID);  
	        parameterMap.put("device_info", "WEB");  
	        parameterMap.put("nonce_str", PayCommonUtil.getRandomString(32));  
	        parameterMap.put("sign_type", "MD5"); 
	        parameterMap.put("body", "华夏征途-游戏充值");  
	        parameterMap.put("attach", content);  
	        parameterMap.put("out_trade_no", cpOrderId);  
	        parameterMap.put("fee_type", "CNY");  
	        parameterMap.put("total_fee", String.valueOf(money*100));  //微信金额单位为分
	        parameterMap.put("spbill_create_ip", NetworkUtil.getIpAddress(req)); 
	        Date date = DateService.getCurrentUtilDate();
	        parameterMap.put("time_start", DateService.dateFormt(date, "yyyyMMddHHmmss"));
	        Date finishDate = DateService.addDateByType(date, Calendar.MINUTE, 30);
	        parameterMap.put("time_expire", DateService.dateFormt(finishDate, "yyyyMMddHHmmss"));
	        parameterMap.put("notify_url", Config.WX_CALL_BACK_URL);  
	        parameterMap.put("trade_type", "APP");  
	        parameterMap.put("limit_pay", "no_credit");  
	        String firstSign = PayCommonUtil.createSign("UTF-8", parameterMap);  
	        parameterMap.put("sign", firstSign);  
	        String requestXML = PayCommonUtil.getRequestXml(parameterMap);  
	        
			int state = 0;
			String payInfo = "";
			JSONObject json = new JSONObject();
	        try {
	        	//统一下单
		        String result = PayCommonUtil.httpsRequest("https://api.mch.weixin.qq.com/pay/unifiedorder", "POST",  requestXML);
		        
				Map<String, Object>  map = PayCommonUtil.getMapFromXML(result);
				
				if(map.get("return_code").toString().equalsIgnoreCase("SUCCESS") && map.get("result_code").toString().equalsIgnoreCase("SUCCESS")){
					
			        JSONObject jsonObject = new JSONObject();  
			        SortedMap<String, Object> param = new TreeMap<String, Object>();  
			        param.put("appid", Config.WX_APP_ID);  
			        param.put("partnerid", Config.WX_MCH_ID);  
			        param.put("prepayid", map.get("prepay_id"));  
			        param.put("package", "Sign=WXPay");  
			        param.put("noncestr", PayCommonUtil.getRandomString(32));  
			        param.put("timestamp", System.currentTimeMillis());  
			        //二次签名
			        String secondSign = PayCommonUtil.createSign("UTF-8", param);  
			        param.put("sign", secondSign);  
			        jsonObject.put("param",param);
			        
			        payInfo= jsonObject.toString();
			        
			        json.put("playerId", playerId);
			        json.put("payItemId", payItemId);
			        json.put("payType", payType);
			        json.put("payInfo", payInfo);
			        json.put("cpOrderId", cpOrderId);
			        
				}else{
					state = -1;
				}
			} catch (ParserConfigurationException e) {
				state = -1;
			    LogUtil.error("微信唤起支付异常：",e);
			} catch (SAXException e) {
				state = -1;
				LogUtil.error("微信唤起支付异常：",e);
			}   
 
	        json.put("state", state);
			this.postData(resp, json.toString());
	}
	
	/**
	 * 东海运营支付回调  https://github.com/donghaigame/DHSDKServerDemo 
	 */
	private void donghaiPay(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, Exception {
		
		JSONObject msgJson = dealMsg2(req);
		
		String reqUrl = msgJson.toString();
		
		String cpOrderId = msgJson.getString("cpOrderId");
		String userId = msgJson.getString("userId");
		String orderId = msgJson.getString("orderId");     //运营商的订单号
		String gameId = msgJson.getString("gameId");
		String subGameId = msgJson.getString("subGameId");
		String platform = msgJson.getString("platform");
		String totalFee = msgJson.getString("totalFee");
		String orderStatus = msgJson.getString("orderStatus");
		String endtime = msgJson.getString("endtime");
		String randStr = msgJson.getString("randStr");
		String customInfo = msgJson.getString("customInfo");
		String sign = msgJson.getString("sign");
		
		if(cpOrderId == null || userId == null || orderId == null || gameId == null || subGameId == null 
				|| platform == null || totalFee == null || orderStatus == null || endtime == null 
				|| randStr == null || sign == null){
			//参数有误
			this.postData(resp, "failure");
			return;
		}
		
		String contents[] = customInfo.split("\\|");
		if (contents.length != 7) {
			// 自定义参数个数不对
			LogUtil.error("自定义参数个数不对----------customInfo="+customInfo);
			this.postData(resp, "failure");
			return;
		}
		
		String pay_key = "abb87c51514ecf7e660ca389c1c8144e";
		
		
		StringBuilder param = new StringBuilder();
		param.append("cpOrderId="+cpOrderId);
		param.append("customInfo="+customInfo);
		param.append("endtime="+endtime);
		param.append("gameId="+gameId);
		param.append("orderId="+orderId);
		param.append("orderStatus="+orderStatus);
		param.append("platform="+platform);
		param.append("randStr="+randStr);
		param.append("subGameId="+subGameId);
		param.append("totalFee="+totalFee);
		param.append("userId="+userId);
		param.append(pay_key);
		
		String mySign = MD5Service.encryptToLowerString(param.toString());
		
		if(mySign.equalsIgnoreCase(sign)){
			
			if(orderStatus.equals("1")){
				
				try {
					
					Long myUserId = Long.valueOf(contents[0]); // 玩家账号
					String site = contents[1]; // 游戏站点
					Long playerId = Long.valueOf(contents[2]); // 玩家编号
					String payItemId = contents[3]; // 商品编号
					Integer payType = Integer.valueOf(contents[4]); // 支付类型
					String money = contents[5]; // 金额
					String sign1 = contents[6]; // 签名
					
					IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
				
					BaseAgentConfig baseAgentConfig = baseDataService.getBaseAgentConfig(Config.AGENT);
					if (baseAgentConfig == null){
						this.postData(resp, "failure");
						LogUtil.error("东海运营支付游戏运营商不存在："+Config.AGENT);
						return;
					}
					
					BaseServerConfig gameConfigVariable = baseDataService.getBaseServerConfig(site);
					if (gameConfigVariable == null){
						this.postData(resp, "failure");
						LogUtil.error("东海运营支付游戏站点不存在："+site);
						return;
					}
					
					String sign2 = MD5Service.encryptToUpperString(myUserId + site + playerId + payItemId + payType + money +baseAgentConfig.getChargeKey());
					if(!sign1.equals(sign2)){
						this.postData(resp, "failure");
						LogUtil.error("东海运营支付签名有误："+sign2);
						return;
					}
					
					int money1 = Integer.valueOf(totalFee) / 100;
					if(!Integer.valueOf(money).equals(money1)){
						this.postData(resp, "failure");
						LogUtil.error("东海运营支付金额有误："+money1);
						return;
					}
					
					this.postData(resp, "success");
					
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("content", customInfo);
					
					sendData_noWaitBack(jsonObject, this.getUrlByGameSitePath(gameConfigVariable.getGameInnerIp(),gameConfigVariable.getWebPort(), PathConstant.PAY));
					
					IPayService payService = GCCContext.getInstance().getServiceCollection().getPayService();
					PayLog payLog = payService.getPayLogByOutOrderNo(cpOrderId);
					if(payLog == null){
						payService.insertPayLog(Long.valueOf(userId), playerId, cpOrderId, orderId, money1, payType, platform+"_"+payItemId, site, reqUrl);
					}else{
						payLog.setOrderNo(orderId);
						payLog.setPayUrl(reqUrl);
						payLog.setUpdateTime(DateService.getCurrentUtilDate());
						payLog.setState(1);
						payService.updatePayLog(payLog);
					}
				} catch (Exception e) {
					LogUtil.error("东海运营支付发货异常：", e);
				}
			}
		}else{
			//签名不正确
			this.postData(resp, "failure");
			LogUtil.error("东海运营支付  签名不正确");
		}
	}
	
	/**
	 * 云游支付回调
	 */
	private void yunyouPay(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, Exception {
		String reqUrl = req.getQueryString();
		
		String data = req.getParameter("data");
		if(data == null || "".equals(data.trim())){
			//参数有误
			this.postData(resp, "failure");
			return;
		}
		
		JSONObject paramJsion = new JSONObject(data);
		
		String uid = paramJsion.getString("uid");
		String game_trade_no = paramJsion.getString("game_trade_no"); //cp的订单号
		String out_trade_no = paramJsion.getString("out_trade_no");   //运营商的订单号
		String trade_status = paramJsion.getString("trade_status");
		String time = paramJsion.getString("time");
		String price = paramJsion.getString("price");
		String pay = paramJsion.getString("pay");
		String sign = paramJsion.getString("sign");
		
		if(uid == null || game_trade_no == null || out_trade_no == null || trade_status == null || time == null 
				|| price == null || pay == null || sign == null){
			//参数有误
			this.postData(resp, "failure");
			return;
		}
		
		String content = game_trade_no.split("@")[1];
		if(content == null){
			//参数有误
			this.postData(resp, "failure");
			return;
		}
		
		String contents[] = content.split("\\|");
		if (contents.length != 7) {
			// 自定义参数个数不对
			LogUtil.error("自定义参数个数不对----------game_trade_no="+game_trade_no);
			this.postData(resp, "failure");
			return;
		}
		
		String pay_key = "192006250b4c09247ec02edce69f6a2d";
		
		StringBuilder param = new StringBuilder();
		param.append("uid="+uid);
		param.append("&game_trade_no="+game_trade_no);
		param.append("&out_trade_no="+out_trade_no);
		param.append("&price="+price);
		param.append("&trade_status="+trade_status);
		param.append("&time="+time);
		param.append("&pay="+pay);
		param.append("&key="+pay_key);
		
		String mySign = MD5Service.encryptToLowerString(param.toString());
		
		if(mySign.equalsIgnoreCase(sign)){
			if(trade_status.equals("TRADE_SUCCESS")){
				
				try {
					Long myUserId = Long.valueOf(contents[0]); // 玩家账号
					String site = contents[1]; // 游戏站点
					Long playerId = Long.valueOf(contents[2]); // 玩家编号
					String payItemId = contents[3]; // 商品编号
					Integer payType = Integer.valueOf(contents[4]); // 支付类型
					String money = contents[5]; // 金额
					String sign1 = contents[6]; // 签名
					
					IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
				
					BaseAgentConfig baseAgentConfig = baseDataService.getBaseAgentConfig(Config.AGENT);
					if (baseAgentConfig == null){
						this.postData(resp, "failure");
						LogUtil.error("云游运营支付游戏运营商不存在："+Config.AGENT);
						return;
					}
					
					BaseServerConfig gameConfigVariable = baseDataService.getBaseServerConfig(site);
					if (gameConfigVariable == null){
						this.postData(resp, "failure");
						LogUtil.error("云游运营支付游戏站点不存在："+site);
						return;
					}
					
					String sign2 = MD5Service.encryptToUpperString(myUserId + site + playerId + payItemId + payType + money +baseAgentConfig.getChargeKey());
					if(!sign1.equals(sign2)){
						this.postData(resp, "failure");
						LogUtil.error("云游运营支付签名有误："+sign2);
						return;
					}
					
					int money1 = Integer.valueOf(price);
					if(!Integer.valueOf(money).equals(money1)){
						this.postData(resp, "failure");
						LogUtil.error("云游运营支付金额有误："+money1);
						return;
					}
					
					this.postData(resp, "success");
					
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("content", content);
					
					sendData_noWaitBack(jsonObject, this.getUrlByGameSitePath(gameConfigVariable.getGameInnerIp(),gameConfigVariable.getWebPort(), PathConstant.PAY));
					
					IPayService payService = GCCContext.getInstance().getServiceCollection().getPayService();
					PayLog payLog = payService.getPayLogByOutOrderNo(game_trade_no);
					if(payLog == null){
						payService.insertPayLog(myUserId, playerId, game_trade_no, out_trade_no, money1, Integer.valueOf(pay), payItemId, site, reqUrl);
					}else{
						payLog.setOrderNo(out_trade_no);
						payLog.setPayUrl(reqUrl);
						payLog.setUpdateTime(DateService.getCurrentUtilDate());
						payLog.setState(1);
						payService.updatePayLog(payLog);
					}
				} catch (Exception e) {
					LogUtil.error("云游运营支付发货异常：", e);
				}
			}
		}else{
			//签名不正确
			this.postData(resp, "failure");
			LogUtil.error("云游运营支付  签名不正确");
		}
	}
}
