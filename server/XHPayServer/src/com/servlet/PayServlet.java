package com.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import org.json.XML;
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
import com.util.HttpUtil;
import com.util.LogUtil;
import com.util.NetworkUtil;
import com.util.PayCommonUtil;
import com.util.QuickSDKUtil;

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
				//东海
				this.donghaiPay(req, resp);
			}else if(Config.AGENT.equals("yunyou")){
				//云游
				this.yunyouPay(req, resp);
			}else if(Config.AGENT.equals("zhongfu")){
				//中富
				this.zhongfuPay(req, resp);
			}else if(Config.AGENT.equals("juliang")){
				//中富-聚量
				this.juliangPay(req, resp);
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
		
		synchronized (reqUrl) {
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
			        int rs = this.sucPay(customInfo, "0", Integer.valueOf(totalFee) / 100, orderId, cpOrderId, reqUrl);
			        if(rs == 0){
			        	this.postData(resp, "success");
			        }else{
			        	this.postData(resp, "failure");
			        }
				}
			}else{
				//签名不正确
				LogUtil.error("东海运营支付  签名不正确");
				this.postData(resp, "failure");
			}
		}
		
	}
	
	/**
	 * 云游支付回调
	 */
	private void yunyouPay(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, Exception {
		
		String reqUrl = req.getQueryString();
		
		if(reqUrl == null) return;
		
		System.out.println("reqUrl: "+reqUrl);
		synchronized (reqUrl) {
			String appid = req.getParameter("app"); //剑雨江湖
			if(appid != null){
				if(appid.equals("275")){  //剑雨江湖
					String at = req.getParameter("at"); //时间搓
					String cbi = req.getParameter("cbi"); //支付信息
					String cn = req.getParameter("cn"); //渠道id
					String fee = req.getParameter("fee"); //支付金额 分
					String kr = req.getParameter("kr");     //随机数
					String pt = req.getParameter("pt"); // 支付方式
					String res = req.getParameter("res"); //支付结果 0成功
					String st = req.getParameter("st"); // 时间搓
					String tid = req.getParameter("tid");   //sdk订单号
					String ud = req.getParameter("ud"); //cp订单号
					String uid = req.getParameter("uid"); //userId
					String ver = req.getParameter("ver");   //版本号
					String sign = req.getParameter("sign"); //签名
					
					Map<String, String> params = new HashMap<String, String>();
					params.put("app", appid);
					params.put("at", at);
					params.put("cbi", cbi);
					params.put("cn", cn);
					params.put("fee", fee);
					params.put("kr", kr);
					params.put("pt", pt);
					params.put("res", res);
					params.put("st", st);
					params.put("tid", tid);
					params.put("ud", ud);
					params.put("uid", uid);
					params.put("ver", ver);
					
					List<String> keys = new ArrayList<String>(params.keySet());
					Collections.sort(keys);
					
					String preStr = new String();
					for(String key : keys){
						preStr += String.format("%s=%s&", key, params.get(key));
					}
					preStr = preStr.substring(0, preStr.length() - 1);
					
					String payKey = "b26a126a3ba449d7a556f1b75c5dd6b7";
					
					String mdsign = MD5Service.encryptToLowerString(preStr + payKey);
					
			        //签名失败
			        if(!mdsign.equalsIgnoreCase(sign))
			        {
			        	LogUtil.error("云游运营支付  签名不正确 appid="+appid);
						this.postData(resp, "ERROR");
						return;
			        }
			        
			        int rs = this.sucPay(cbi, appid, Integer.valueOf(fee) / 100, tid, ud, reqUrl);
			        if(rs == 0){
			        	this.postData(resp, "SUCCESS");
			        }else{
			        	this.postData(resp, "ERROR");
			        }
				}
			}else{
				String data = req.getParameter("data"); //大唐降魔传  大唐仙侠  降魔苍穹
				if(data != null){
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
					        int rs = this.sucPay(content, "0", Integer.valueOf(price), out_trade_no, null, reqUrl);
					        if(rs == 0){
					        	this.postData(resp, "success");
					        }else{
					        	this.postData(resp, "failure");
					        }
						}
					}else{
						//签名不正确
						this.postData(resp, "failure");
						LogUtil.error("云游运营支付  签名不正确");
					}
				}else{
					String userid = req.getParameter("userid"); //诛仙青云录
					if(userid != null){
						String goodsid = req.getParameter("goodsid"); //支付信息 
						String goodsname = req.getParameter("goodsname"); //商品名称
						String bundleid = req.getParameter("bundleid"); //应用bundle identifier
						String orderno = req.getParameter("orderno"); //交易订单号
						String fee = req.getParameter("fee");     //支付金额单位：分
						String paytime = req.getParameter("paytime"); // 支付时间
						String paytype = req.getParameter("paytype"); //支付方式：0:微信，1支付宝  6:ApplePay
						String timestamp = req.getParameter("timestamp"); // 时间戳
						String sign = req.getParameter("sign"); // 签名
						String result = req.getParameter("result"); // SUCCESS
						
						Map<String, String> params = new HashMap<String, String>();
						params.put("userid", userid);
						params.put("goodsid", goodsid);
						params.put("goodsname", goodsname);
						params.put("bundleid", bundleid);
						params.put("orderno", orderno);
						params.put("fee", fee);
						params.put("paytime", paytime);
						params.put("paytype", paytype);
						params.put("timestamp", timestamp);
						params.put("result", result);
						
						List<String> keys = new ArrayList<String>(params.keySet());
						Collections.sort(keys);
						
						String preStr = new String();
						for(String key : keys){
							preStr += String.format("%s=%s&", key, params.get(key));
						}
						preStr = preStr.substring(0, preStr.length() - 1);
						
						String payKey = "Wykj4iHo452mVthczm9jKNAWsIfUzqgp";
						String mdsign = MD5Service.encryptToUpperString(preStr + "&key="+payKey);
						
						JSONObject json = new JSONObject();
				        //签名失败
				        if(!mdsign.equalsIgnoreCase(sign))
				        {
				        	LogUtil.error("云游运营支付  签名不正确 appid="+appid);
				        	json.put("returncode", "FAIL");
				        	json.put("returnmsg", "签名不对");
							this.postData(resp, json.toString());
							return;
				        }
				        
				        int rs = this.sucPay(goodsid, appid, Integer.valueOf(fee) / 100, orderno, null, reqUrl);
				        if(rs == 0){
				        	json.put("returncode", "SUCCESS");
							this.postData(resp, json.toString());
				        }else{
				        	LogUtil.error("云游运营支付  支付异常 appid="+appid);
				        	json.put("returncode", "FAIL");
				        	json.put("returnmsg", "支付异常");
							this.postData(resp, json.toString());
				        }
					}
				}
			}

		}
		
	}
	
	/**
	 * 中富支付回调
	 */
	private void zhongfuPay(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, Exception {
		
		String reqUrl = req.getQueryString();
		
		if(reqUrl == null){
			reqUrl = req.getParameterMap().toString();
		}
		
		System.out.println("reqUrl="+reqUrl);
		
		if(reqUrl == null) return;
		
		synchronized (reqUrl) {
			String appid = req.getParameter("appid");   //游戏ID  
			if(appid == null){
				appid = req.getParameter("app"); //百转修仙
				if(appid == null){
					appid = req.getParameter("game_id"); //大唐山海缘
					if(appid == null){
						appid = req.getParameter("gameid");
						if(appid == null){
							appid = req.getParameter("app_id");
							if(appid == null){
								IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
								
								BaseAgentConfig baseAgentConfig = baseDataService.getBaseAgentConfig(Config.AGENT);
								if (baseAgentConfig == null){
									LogUtil.error("运营商="+Config.AGENT+" appid="+appid+"  游戏运营商不存在");
									return;
								}
								
								String userName = req.getParameter("Account"); //大唐修仙传
								if(userName == null){
									userName = req.getParameter("userId"); //焚天决
								}
								if(userName != null){
									try {
										appid = HttpUtil.httpsRequest(baseAgentConfig.getAccountUrl() + PathConstant.APPID, "userName="+userName, "application/x-www-form-urlencoded");
									} catch (Exception e) {
										LogUtil.error("获取登录appid异常：", e);
										return;
									}
								}
							}
						}
	
					}
				}
			}
			if(appid != null){
				if(appid.equals("1271")){ //仙剑长安
					String amount = req.getParameter("amount"); //充值金额（人民币元）
					String charid = req.getParameter("charid"); //角色ID
					String cporderid = req.getParameter("cporderid"); //cp的订单号
					String extinfo = req.getParameter("extinfo"); //支付信息
					String gold = req.getParameter("gold");     //游戏币数量
					String orderid = req.getParameter("orderid"); //平台的订单ID
					String serverid = req.getParameter("serverid"); //服务器ID
					String time = req.getParameter("time"); //时间戳 int
					String uid = req.getParameter("uid");   //用户ID 
					String sign = req.getParameter("sign"); //签名
					
					String Pay_Key = "fbb289d1e994d7706ceb105111b920de";
					String code = String.format("amount=%s&appid=%s&charid=%s&cporderid=%s&extinfo=%s&gold=%s&orderid=%s&serverid=%s&time=%s&uid=%s%s",
							GetEncode(amount), GetEncode(appid), GetEncode(charid), GetEncode(cporderid),
							GetEncode(extinfo), GetEncode(gold), GetEncode(orderid), GetEncode(serverid),
			            time, GetEncode(uid), Pay_Key);

					String md5 = getMD5(code);

			        //签名失败
			        if(!md5.equals(sign))
			        {
						//签名不正确
			        	LogUtil.error("中富运营支付  签名不正确 appid="+appid);
						this.postData(resp, "ERROR");
						return;
			        }
			        
			        int rs = this.sucPay(GetDecode(extinfo), appid, Double.valueOf(amount).intValue(), orderid, cporderid, reqUrl);
			        if(rs == 0){
			        	this.postData(resp, "SUCCESS");
			        }else{
			        	this.postData(resp, "ERROR");
			        }
				}else if(appid.equals("324") || appid.equals("53")
						|| appid.equals("106") || appid.equals("323")
						|| appid.equals("325")){ //百转修仙  幻域修仙 仙道至尊 仙侠大劫主 修仙道主
					 
					String at = req.getParameter("at"); //时间搓
					String cbi = req.getParameter("cbi"); //支付信息
					String cn = req.getParameter("cn"); //渠道id
					String fee = req.getParameter("fee"); //支付金额 分
					String kr = req.getParameter("kr");     //随机数
					String pt = req.getParameter("pt"); // 支付方式
					String res = req.getParameter("res"); //支付结果 0成功
					String st = req.getParameter("st"); // 时间搓
					String tid = req.getParameter("tid");   //sdk订单号
					String ud = req.getParameter("ud"); //cp订单号
					String uid = req.getParameter("uid"); //userId
					String ver = req.getParameter("ver");   //版本号
					String sign = req.getParameter("sign"); //签名
					
					Map<String, String> params = new HashMap<String, String>();
					params.put("app", appid);
					params.put("at", at);
					params.put("cbi", cbi);
					params.put("cn", cn);
					params.put("fee", fee);
					params.put("kr", kr);
					params.put("pt", pt);
					params.put("res", res);
					params.put("st", st);
					params.put("tid", tid);
					params.put("ud", ud);
					params.put("uid", uid);
					params.put("ver", ver);
					
					List<String> keys = new ArrayList<String>(params.keySet());
					Collections.sort(keys);
					
					String preStr = new String();
					for(String key : keys){
						preStr += String.format("%s=%s&", key, params.get(key));
					}
					preStr = preStr.substring(0, preStr.length() - 1);
					
					String payKey = "9ef751477c704f8fad7fe55321f8e82b";
					if(appid.equals("53")){
						payKey = "56a8abc23651451298793229da237437";
					}else if(appid.equals("106")){
						payKey = "40212600241b4ed6aaeea12a65cd6a9a";
					}else if(appid.equals("323")){
						payKey = "7248df52fa054e3693499d694e17674e";
					}else if(appid.equals("325")){
						payKey = "a98a2616080a4f8b90a1d7c76217a57d";
					}
					
					String mdsign = MD5Service.encryptToLowerString(preStr + payKey);
					
			        //签名失败
			        if(!mdsign.equalsIgnoreCase(sign))
			        {
			        	LogUtil.error("中富运营支付  签名不正确 appid="+appid);
						this.postData(resp, "ERROR");
						return;
			        }
			        
			        int rs = this.sucPay(cbi, appid, Integer.valueOf(fee) / 100, tid, ud, reqUrl);
			        if(rs == 0){
			        	this.postData(resp, "SUCCESS");
			        }else{
			        	this.postData(resp, "ERROR");
			        }
				}else if(appid.equals("799") || appid.equals("150000007") 
						|| appid.equals("150000008")|| appid.equals("150000009") 
						|| appid.equals("150000010") ){ //大唐山海缘  妖魔大陆  御剑降魔录  斩妖奇侠 诛妖 
					
					String trade_no = req.getParameter("out_trade_no"); //订单号
					String price = req.getParameter("price"); //价格元
					String extend = req.getParameter("extend"); //支付信息
					String sign = req.getParameter("sign");     //随机数
					
					String payKey = "DZQ!@#9527";
					if(appid.equals("150000007")){
						payKey = "df107f3eb75ff9289b15a843128124e9";
					}else if(appid.equals("150000008")){
						payKey = "5bb9abb0cbaba7c4d92e161fa0d795d9";
					}else if(appid.equals("150000009")){
						payKey = "2e33bdadb32162e07a314cc66ec0076f";
					}else if(appid.equals("150000010")){
						payKey = "bbd631205e45f2a01cfc096277d04d33";
					}
					
					String mdsign = MD5Service.encryptToLowerString(appid + trade_no + price + extend + payKey);
					
			        //签名失败
			        if(!mdsign.equalsIgnoreCase(sign))
			        {
			        	LogUtil.error("中富运营支付  签名不正确 appid="+appid);
						this.postData(resp, "0");
						return;
			        }
			        
			        int rs = this.sucPay(extend, appid, Integer.valueOf(price), trade_no, null, reqUrl);
			        if(rs == 0){
			        	this.postData(resp, "1");
			        }else{
			        	this.postData(resp, "0");
			        }
				}else if(appid.equals("1") || appid.equals("2")){ //大唐修仙传 风暴国度
					String Payorderid = req.getParameter("Payorderid"); //cp订单号
					String Paycid = req.getParameter("Paycid"); //订单号
					String Account = req.getParameter("Account"); //Uid
					String Sid = req.getParameter("Sid"); //区服id
					String Roleid = req.getParameter("Roleid"); //角色id
					String Goodsid = req.getParameter("Goodsid");     //道具id
					String Money = req.getParameter("Money"); // 人民币 单位元
					String Coins = req.getParameter("Coins"); //游戏币
					String Time = req.getParameter("Time"); // 当前的时间戳
					String Custominfo = req.getParameter("Custominfo");   //自定义项
					String Sign = req.getParameter("Sign"); //签名
					
					String payKey = "73bf3052ba76a1e3cf2d53823e035b13";
					if(appid.equals("2")){
						payKey = "fcd39ca8cf0b9b245d49b55fedd575db";
					}
					
					String mdsign = MD5Service.encryptToLowerString(Payorderid + Paycid + Account + Sid + Roleid + Goodsid + Money + Coins + Time + Custominfo + payKey);
					
					JSONObject json = new JSONObject();
					
			        //签名失败
			        if(!mdsign.equalsIgnoreCase(Sign))
			        {
			    		LogUtil.error("中富运营支付  签名不正确appid= "+appid);
			        	json.put("Result", 6);
						this.postData(resp, json.toString());
						return;
			        }
			        
			        int rs = this.sucPay(Custominfo, appid, Integer.valueOf(Money), Paycid, Payorderid, reqUrl);
			        
			        if(rs == 0){
			        	json.put("Result", 1);
			        	
			        }else{
			        	json.put("Result", 8);
			        }
			        this.postData(resp, json.toString());
				}else if(appid.equals("10080") || appid.equals("10012")){ //焚天决 御仙诀
					String userId = req.getParameter("userId"); //充值用户账号
					String goodsId = req.getParameter("goodsId"); //支付信息 
					String goodsName = req.getParameter("goodsName"); //商品名称
					String payOrderId = req.getParameter("payOrderId"); //支付流水号
					String payPrice = req.getParameter("payPrice"); //支付金额（单位:分）
					String payStatus = req.getParameter("payStatus");     //0 失败，1 成功
					String applePay = req.getParameter("applePay"); // 1 苹果支付
					String sign = req.getParameter("sign"); //签名
					
					Map<String, String> params = new HashMap<String, String>();
					params.put("userId", userId);
					params.put("goodsId", goodsId);
					params.put("goodsName", goodsName);
					params.put("payOrderId", payOrderId);
					params.put("payPrice", payPrice);
					params.put("payStatus", payStatus);
					if(applePay != null){
						params.put("applePay", applePay);
					}
					
					List<String> keys = new ArrayList<String>(params.keySet());
					Collections.sort(keys);
					
					String preStr = new String();
					for(String key : keys){
						preStr += String.format("%s=%s&", key, params.get(key));
					}
					preStr = preStr.substring(0, preStr.length() - 1);
					
					String payKey = "LNLFQHKHPALYUWHK";
					if(appid.equals("10012")){
						payKey = "FNAFOUAPTJODFKPN";
					}
					String mdsign = MD5Service.encryptToLowerString(preStr + "&appKey="+payKey);
					
			        //签名失败
			        if(!mdsign.equalsIgnoreCase(sign))
			        {
			        	LogUtil.error("中富运营支付  签名不正确 appid="+appid);
						this.postData(resp, "ERROR");
						return;
			        }
			        
			        int rs = this.sucPay(goodsId, appid, Integer.valueOf(payPrice) / 100, payOrderId, null, reqUrl);
			        if(rs == 0){
			        	this.postData(resp, "0");
			        }else{
			        	this.postData(resp, "0"); //因为他们除了0   其他都会继续回调
			        }
				}else if(appid.equals("6327") || appid.equals("6328") 
						|| appid.equals("6333") || appid.equals("6010")
						|| appid.equals("6089") || appid.equals("6094")
						|| appid.equals("6095")){ //太古封神 太古伏魔录 武动九州 仙侠幻梦录 隋唐修仙传  逍遥仙途 修仙侠隐
					
					String cp_order_id = req.getParameter("cp_order_id"); //cp平台订单号
					String mem_id = req.getParameter("mem_id"); //玩家ID
					String order_id = req.getParameter("order_id"); //平台订单号
					String order_status = req.getParameter("order_status"); //平台订单状态 1 未支付 2成功支付 3支付失败
					String pay_time = req.getParameter("pay_time"); //订单下单时间
					String product_id = req.getParameter("product_id");     //商品id
					String product_name = req.getParameter("product_name"); // 商品名称
					String product_price = req.getParameter("product_price"); //商品价格(元);保留两位小数
					String sign = req.getParameter("sign"); //签名
					String ext = req.getParameter("ext"); //支付信息
					
					
					String app_key = "957fd6a0159072e37bea8011bb80e7c0";
					if(appid.equals("6328")){
						app_key = "79500a2d1c3db0bf9cc8e8637ef9b270";
					}else if(appid.equals("6333")){
						app_key = "76267feade4396e62b9d62af4b6dbce4";
					}else if(appid.equals("6010")){
						app_key = "2cff258feccca00617a71301bc2b68d7";
					}else if(appid.equals("6089")){
						app_key = "dea52672cfed4d7b4e071967a9a2da45";
					}else if(appid.equals("6094")){
						app_key = "1093cacfbd5c8be8e5ab6e7c7f2ee2dd";
					}else if(appid.equals("6095")){
						app_key = "ca8c3511907b7937161660d5dfdfa551";
					}
					
					String code = String.format("app_id=%s&cp_order_id=%s&mem_id=%s&order_id=%s&order_status=%s&pay_time=%s&product_id=%s&product_name=%s&product_price=%s&app_key=%s",
							appid, GetEncode(cp_order_id), mem_id, order_id,
							order_status, pay_time, GetEncode(product_id), GetEncode(product_name),
							GetEncode(product_price), app_key);
					
					String md5 = getMD5(code);
					
					System.out.println(code);
					System.out.println("md5sign="+md5);
			        //签名失败
			        if(!md5.equalsIgnoreCase(sign))
			        {
			        	LogUtil.error("中富运营支付  签名不正确 appid="+appid);
						this.postData(resp, "FAILURE");
						return;
			        }
			        
			        int rs = this.sucPay(GetDecode(ext), appid, Double.valueOf(product_price).intValue(), order_id, cp_order_id, reqUrl);
			        if(rs == 0){
			        	this.postData(resp, "SUCCESS");
			        }else{
			        	this.postData(resp, "FAILURE");
			        }
				}else if(appid.equals("2018000008") || appid.equals("2018000009")){  //苍穹传奇 仙侠轩辕世界
					String tradeno = req.getParameter("tradeno"); //平台订单号
					String orderid = req.getParameter("orderid"); //cp平台订单号
					String orderinfo = req.getParameter("orderinfo"); //支付信息
					String goodid = req.getParameter("goodid"); //商品编号
					String goodname = req.getParameter("goodname"); //商品名称
					String goodinfo = req.getParameter("goodinfo");     //商品信息
					String goodsprice = req.getParameter("goodsprice"); // 商品价格
					String payamount = req.getParameter("payamount"); //实际支付金额（单位为元，支持小数点后两位）
					String state  = req.getParameter("state"); //支付状态(success为成功，fail为失败)
					String md5sign = req.getParameter("md5sign"); //签名
					
					String appkey = "aa52b72559547bfd4135605bed186f29";
					if(appid.equals("2018000009")){
						appkey = "baa5834a6effd194113d956f7206929d";
					}
					
					String mdsign = MD5Service.encryptToLowerString(tradeno+appid+orderid+orderinfo+goodid+goodname+goodinfo+goodsprice+payamount+state+appkey);
					
			        //签名失败
			        if(!mdsign.equalsIgnoreCase(md5sign))
			        {
			        	LogUtil.error("中富运营支付  签名不正确 appid="+appid);
						this.postData(resp, "ERROR");
						return;
			        }
			        
			        int rs = this.sucPay(orderinfo, appid, Double.valueOf(payamount).intValue(), tradeno, orderid, reqUrl);
			        if(rs == 0){
			        	this.postData(resp, "SUCCESS");
			        }else{
			        	this.postData(resp, "FAIL"); //因为他们除了0   其他都会继续回调
			        }
					
				}else{
					LogUtil.error("中富运营支付  appid不存在： "+appid);
				}
			}else{
				String nt_data  = req.getParameter("nt_data"); //快接数据
				if(nt_data != null){    //古剑苍穹 九州剑雨  太古仙盟 天剑侠客 天之剑
					String sign  = req.getParameter("sign"); //玩家ID
					String md5Sign = req.getParameter("md5Sign"); //平台订单号
					
					//这里很尴尬  给的参数区分不了不同包   只能一个一个解析了
					JSONObject xmlJSONObj = null;
					String md5key = "";
					
					String str = QuickSDKUtil.decode(nt_data, "07550676324174117925930002558519");
					try {
						xmlJSONObj = XML.toJSONObject(str);
						md5key = "27523387538379448798008024519711";
						appid = "11";
					} catch (JSONException e) {
						str = QuickSDKUtil.decode(nt_data, "84505581291115801244340540856714");
						try {
							xmlJSONObj = XML.toJSONObject(str);
							md5key = "17572494306565578911426925592634";
							appid = "14";
						} catch (JSONException e2) {
							str = QuickSDKUtil.decode(nt_data, "44552088617872811848287346117042");
							try {
								xmlJSONObj = XML.toJSONObject(str);
								md5key = "40771567645374583975758210390715";
								appid = "10";
							} catch (JSONException e3) {
								str = QuickSDKUtil.decode(nt_data, "73653686589855263073152202153410");
								try {
									xmlJSONObj = XML.toJSONObject(str);
									md5key = "78604470269711452294716748372580";
									appid = "13";
								} catch (JSONException e4) {
									str = QuickSDKUtil.decode(nt_data, "57153425302963564449419371104520");
									try {
										xmlJSONObj = XML.toJSONObject(str);
										md5key = "23658812931576220725819630675887";
										appid = "12";
									} catch (JSONException e5) {
								     	LogUtil.error("中富运营支付  str不正确 str="+str);
										this.postData(resp, "SUCCESS");
										return;	
									}
								}
							}
						}
					}
		   
					String mdsign = MD5Service.encryptToLowerString(nt_data + sign + md5key);
			        //签名失败
			        if(!mdsign.equalsIgnoreCase(md5Sign))
			        {
			        	LogUtil.error("中富运营支付  签名不正确 appid="+appid);
						this.postData(resp, "SUCCESS");
						return;
			        }
			        
			        JSONObject js = xmlJSONObj.getJSONObject("quick_message").getJSONObject("message");
			        String out_order_no = js.getString("out_order_no"); //cp订单号
			        String order_no = js.getString("order_no"); //sdk订单号
			        String amount = js.getString("amount"); //用户支付金额
			        String extras_params = js.getString("extras_params"); //支付信息
			        
			        int rs = this.sucPay(extras_params, appid, Double.valueOf(amount).intValue(), order_no, out_order_no, null);
			        if(rs == 0){
			        	this.postData(resp, "SUCCESS");
			        }else{
			        	this.postData(resp, "SUCCESS");
			        }
			        
				}else{
					LogUtil.error("中富运营支付  appid有误 "+appid);
				}
			}
		}
	}
	
	/**
	 * 中富-聚量支付回调
	 */
	private void juliangPay(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, Exception {
		
		String reqUrl = req.getQueryString();
		
		System.out.println("reqUrl:  "+reqUrl);
		
		if(reqUrl == null) return;
		
		synchronized (reqUrl) {
			String app = req.getParameter("app"); //十六进制字符串形式的应用 ID
			String cbi = req.getParameter("cbi"); //支付信息 
			String ct = req.getParameter("ct"); //支付完成时间
			String fee = req.getParameter("fee"); //金额（分）
			String pt = req.getParameter("pt"); //付费时间
			String sdk = req.getParameter("sdk");     //渠道在易接服务器的 ID
			String ssid = req.getParameter("ssid"); // cp订单号
			String st = req.getParameter("st"); //是否支付成功标志， 1 标示支付成功
			String tcd = req.getParameter("tcd"); //订单在易接服务器上的订单号
			String uid = req.getParameter("uid"); //渠道平台上的唯一标记
			String ver = req.getParameter("ver"); //协议版本号
			String sign = req.getParameter("sign"); //签名
			
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("app", app);
			params.put("cbi", cbi);
			params.put("ct", ct);
			params.put("fee", fee);
			params.put("pt", pt);
			params.put("sdk", sdk);
			params.put("ssid", ssid);
			params.put("st", st);
			params.put("tcd", tcd);
			params.put("uid", uid);
			params.put("ver", ver);
			
			List<String> keys = new ArrayList<String>(params.keySet());
			Collections.sort(keys);
			
			String preStr = new String();
			for(String key : keys){
				preStr += String.format("%s=%s&", key, params.get(key));
			}
			preStr = preStr.substring(0, preStr.length() - 1);
			
			String appid = "3";
			String payKey = "RJ205B7RCT4CP5H0GL5PMQ27FLG57VZF";
			if(app.equals("9B6403A0C0DE0D67")){
				payKey = "TQWA7SHLXQWWQALQCBVNWCVTZI34BKK6";
				appid = "4";
			}
			String mdsign = MD5Service.encryptToLowerString(preStr + payKey);
			
	        //签名失败
	        if(!mdsign.equalsIgnoreCase(sign))
	        {
	        	LogUtil.error("中富-聚量运营支付  签名不正确 appid="+appid);
				this.postData(resp, "FAILURE");
				return;
	        }
	        
	        
			if(!st.equals("1")){
		       	LogUtil.error("中富-聚量运营支付  支付不成功 appid="+appid+" st="+st);
				this.postData(resp, "FAILURE");
				return;
			}
			
	        int rs = this.sucPay(cbi, appid, Integer.valueOf(fee) / 100, tcd, null, reqUrl);
	        if(rs == 0){
	        	this.postData(resp, "SUCCESS");
	        }else{
	        	this.postData(resp, "FAILURE");
	        }
		}
		
	}
	
	/**
	 * 支付发货 0成功  1失败
	 */
	private int sucPay(String content, String appid, int ptMoney, String orderId, String cpOrderId, String reqUrl){
		String contents[] = content.split("\\|");
		if (contents.length != 8) {
			// 自定义参数个数不对
			LogUtil.error("运营商="+Config.AGENT+" appid="+appid+"  自定义参数个数不对content="+content);
			return 1;
		}
		try {
			
			Long userId = Long.valueOf(contents[0]); // 玩家账号
			String site = contents[1]; // 游戏站点
			Long playerId = Long.valueOf(contents[2]); // 玩家编号
			String payItemId = contents[3]; // 商品编号
			Integer payType = Integer.valueOf(contents[4]); // 支付类型
			String money = contents[5]; // 金额
			String myCpOrderId = contents[6]; // cp订单号
			String sign1 = contents[7]; // 签名
			
			if(cpOrderId == null){
				cpOrderId = myCpOrderId;
			}
			IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
		
			BaseAgentConfig baseAgentConfig = baseDataService.getBaseAgentConfig(Config.AGENT);
			if (baseAgentConfig == null){
				LogUtil.error("运营商="+Config.AGENT+" appid="+appid+"  游戏运营商不存在");
				return 1;
			}
			
			BaseServerConfig gameConfigVariable = baseDataService.getBaseServerConfig(site);
			if (gameConfigVariable == null){
				LogUtil.error("运营商="+Config.AGENT+" appid="+appid+"  游戏站点不存在");
				return 1;
			}
			
			String sign2 = MD5Service.encryptToUpperString(userId + site + playerId + payItemId + payType + money + cpOrderId + baseAgentConfig.getChargeKey());
			if(!sign1.equals(sign2)){
				LogUtil.error("运营商="+Config.AGENT+" appid="+appid+" 签名有误sign="+sign2);
				return 1;
			}
			
			if(!Integer.valueOf(money).equals(ptMoney)){
				LogUtil.error("运营商="+Config.AGENT+" appid="+appid+" 支付金额有误ptMoney="+ptMoney);
				return 1;
			}
			
			IPayService payService = GCCContext.getInstance().getServiceCollection().getPayService();
			PayLog payLog = payService.getPayLogByOutOrderNo(cpOrderId);
			if(payLog == null){
				if(reqUrl == null){
					reqUrl = content;
				}
				
				payService.insertPayLog(Long.valueOf(userId), playerId, cpOrderId, orderId, ptMoney, payType, payItemId, Config.AGENT+"_"+appid, site, reqUrl);
			}else{
				
				LogUtil.error("运营商="+Config.AGENT+" appid="+appid+" 订单已存在cpOrderId="+cpOrderId);
				return 1;
			}
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("content", content);
			sendData_noWaitBack(jsonObject, this.getUrlByGameSitePath(gameConfigVariable.getGameInnerIp(),gameConfigVariable.getWebPort(), PathConstant.PAY));
			
			return 0;
		} catch (Exception e) {
			LogUtil.error("运营商="+Config.AGENT+" appid="+appid+" 发货异常:", e);
			return 1;
			
		}
	}
}
