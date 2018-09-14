package com.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.AsyncContinuation;
import org.json.JSONObject;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
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

/**
 * 支付宝充值异步回调
 * @author ken
 * @date 2017-6-20
 */
public class AliPayServlet extends AbstractServlet {
	
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
	 * 处理支付宝充值回调
	 */
	private void doPay(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map<String, String[]> requestParams = req.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
		    String name = (String) iter.next();
		    String[] values = (String[]) requestParams.get(name);
		    String valueStr = "";
		    for (int i = 0; i < values.length; i++) {
		        valueStr = (i == values.length - 1) ? valueStr + values[i]
		                    : valueStr + values[i] + ",";
		  }
		  //乱码解决，这段代码在出现乱码时使用。
		  //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
		  params.put(name, valueStr);
		 }
		//切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
		//boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
		try {
			boolean flag = AlipaySignature.rsaCheckV1(params, Config.Ali_PUBLIC_KEY, "utf-8", "RSA2");
			
			//System.out.println("成功支付回调："+flag+" params="+params);
			
			if(flag){
				
				//out_trade_no 自己的唯一订单号
				//trade_no  支付宝订单号
				//trade_status 支付状态
				//（WAIT_BUYER_PAY：交易创建，等待买家付款    TRADE_CLOSED：未付款交易超时关闭，或支付完成后全额退款  TRADE_SUCCESS：交易支付成功  TRADE_FINISHED:交易结束，不可退款 ）
				//body 自己上传的消息内容
				
				String out_trade_no = params.get("out_trade_no");
				String trade_no = params.get("trade_no");
				String trade_status = params.get("trade_status");
				String body = params.get("body");
				
				int state = 0;
				if(trade_status.equals("TRADE_CLOSED")){
					state = 1;
				}else if(trade_status.equals("TRADE_SUCCESS")){
					state = 2;
				}else if(trade_status.equals("TRADE_FINISHED")){
					state = 3;
				}else{
					LogUtil.error("支付宝充值回调状态有误："+trade_status);
					return;
				}
				
				String contents[] = body.split("\\|");
				
				Long userId = Long.valueOf(contents[0]); // 玩家账号
				String site = contents[1]; // 游戏站点
				Long playerId = Long.valueOf(contents[2]); // 玩家编号
				String payItemId = contents[3]; // 商品编号
				Integer payType = Integer.valueOf(contents[4]); // 支付类型
				Integer money = Integer.valueOf(contents[5]); // 金额
				String sign1 = contents[6]; // 签名
				
				if(state == 2){
					
					try {
						
						IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
						
						BaseAgentConfig baseAgentConfig = baseDataService.getBaseAgentConfig(Config.AGENT);
						if (baseAgentConfig == null){
							LogUtil.error("支付宝支付游戏运营商不存在："+Config.AGENT);
							return;
						}
						
						BaseServerConfig gameConfigVariable = baseDataService.getBaseServerConfig(site);
						if (gameConfigVariable == null){
							LogUtil.error("支付宝支付站点不存在："+site);
							return;
						}
						
						String sign2 = MD5Service.encryptToUpperString(userId + site + playerId + payItemId + payType + money +baseAgentConfig.getChargeKey());
						if(!sign1.equals(sign2)){
							LogUtil.error("支付宝支付签名有误："+sign2);
							return;
						}
						
						this.postData(resp, "success");
						
						//开始通知游戏服发货
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("content", body);
						
						sendData_noWaitBack(jsonObject, this.getUrlByGameSitePath(gameConfigVariable.getGameInnerIp(),gameConfigVariable.getWebPort(), PathConstant.PAY));
						
						
					} catch (Exception e) {
						LogUtil.error("支付宝充值发货异常：", e);
					}
				}
				
				IPayService payService = GCCContext.getInstance().getServiceCollection().getPayService();
				PayLog payLog = payService.getPayLogByOutOrderNo(out_trade_no);
				if(payLog == null){
					payService.insertPayLog(userId, playerId, out_trade_no, trade_no, Integer.valueOf(money), payType, payItemId, site, params.toString());
				}else{
					payLog.setOrderNo(trade_no);
					payLog.setPayUrl(params.toString());
					payLog.setUpdateTime(DateService.getCurrentUtilDate());
					payLog.setState(1);
					payService.updatePayLog(payLog);
				}
			}
		} catch (AlipayApiException e) {
			LogUtil.error("支付宝回调验证异常：", e);
		}
	}
}
