package com.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.AsyncContinuation;
import org.json.JSONException;
import org.json.JSONObject;

import com.common.Config;
import com.common.GCCContext;
import com.common.MD5Service;
import com.constant.PathConstant;
import com.domain.PayLog;
import com.domain.config.BaseAgentConfig;
import com.domain.config.BaseServerConfig;
import com.service.IBaseDataService;
import com.service.IPayService;
import com.util.IOS_Verify;
import com.util.LogUtil;

/**
 * 苹果支付
 * @author ken
 * @date 2017-7-13
 */
public class ApplePayServlet extends AbstractServlet {

	private static final long serialVersionUID = 4089164982966246579L;

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
		
		try {
			if (resp.isCommitted()) return;
			initReqResp(req, resp);
			
			if(Config.AGENT.equals("xh")){
				doPay(req, resp);
			}
		} catch (JSONException e) {
			LogUtil.error(e);
		}
	}
	
	
	/**
	 * 处理苹果支付验证
	 * @throws JSONException 
	 */
	private void doPay(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException, JSONException{
		//苹果客户端传上来的收据,是最原据的收据  
        String receipt = req.getParameter("receipt");  
        String content = req.getParameter("payInfo");
        
    	if (receipt == null || "".equals(receipt.trim()) || content == null || "".equals(content.trim())) return;
    	
		String contents[] = content.split("\\|");
		if (contents.length != 8) {
			// 参数个数不对
			LogUtil.error("苹果支付参数个数不对----------content="+content);
			return;
		}
		
		Long userId = Long.valueOf(contents[0]); // 玩家账号
		String site = contents[1]; // 游戏站点
		Long playerId = Long.valueOf(contents[2]); // 玩家编号
		String payItemId = contents[3]; // 商品编号
		Integer payType = Integer.valueOf(contents[4]); // 支付类型
		String money = contents[5]; // 金额
		String cpOrderId = contents[6]; //自己生的唯一订单号
		String sign1 = contents[7]; // 签名
		
        //返回app结果
		JSONObject result = new JSONObject();
		
        //查询数据库，看是否是己经验证过的账号  
		IPayService payService = GCCContext.getInstance().getServiceCollection().getPayService();
		PayLog payLog = payService.getPayLogByOutOrderNo(cpOrderId);
		if(payLog != null){
	        //账单己验证过  
            result.put("state", -2);
            this.postData(resp, result.toString());
			return;
		}
		
       	String verifyResult=IOS_Verify.buyAppVerify(receipt); 
       	
        if(verifyResult==null){  
            //苹果服务器没有返回验证结果  
            result.put("state", -1);
            this.postData(resp, result.toString());
        }else{  
            //跟苹果验证有返回结果------------------  
            JSONObject job = new JSONObject(verifyResult);
            String states=job.getString("status");  
            if(states.equals("0")){  //验证成功  
            	
                String r_receipt=job.getString("receipt");  //下面可以参考返回参数
                JSONObject returnJson = new JSONObject(r_receipt);  
                //产品ID  
                String product_id=returnJson.getString("product_id");  
                if(product_id == null){
                	//产品编号不一致
                    result.put("state", -1);
                    this.postData(resp, result.toString());
                    
                    LogUtil.error("苹果支付产品编号null：product_id = "+product_id);
                    return;
                }
                
                if(!product_id.equals(payItemId)){
                  	//产品编号不一致
                      result.put("state", -1);
                      this.postData(resp, result.toString());
                      
                      LogUtil.error("苹果支付产品编号不一致：payItemId = "+ payItemId+" and product_id = "+product_id);
                      return;
                }
              
				try {
					IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
				
					BaseAgentConfig baseAgentConfig = baseDataService.getBaseAgentConfig(Config.AGENT);
					if (baseAgentConfig == null){
	                    result.put("state", -1);
	                    this.postData(resp, result.toString());
						LogUtil.error("苹果支付游戏运营商不存在："+Config.AGENT);
						return;
					}
					
					BaseServerConfig gameConfigVariable = baseDataService.getBaseServerConfig(site);
					if (gameConfigVariable == null){
	                    result.put("state", -1);
	                    this.postData(resp, result.toString());
						LogUtil.error("苹果支付充值站点不存在："+site);
						return;
					}
					
					String sign2 = MD5Service.encryptToUpperString(userId + site + playerId + payItemId + payType + money + cpOrderId + baseAgentConfig.getChargeKey());
					if(!sign1.equals(sign2)){
	                    result.put("state", -1);
	                    this.postData(resp, result.toString());
						LogUtil.error("苹果支付签名有误："+sign2);
						return;
					}
					
					//开始通知游戏服发货
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("content", content);
					
	                String transaction_id = returnJson.getString("transaction_id");  
	                //跟苹果的服务器验证成功  
	                result.put("state", 0); 
	                this.postData(resp, result.toString());
	                
	                
					sendData_noWaitBack(jsonObject, this.getUrlByGameSitePath(gameConfigVariable.getGameInnerIp(),gameConfigVariable.getWebPort(), PathConstant.PAY));
					
	                //插入支付记录
	        		payService.insertPayLog(userId, playerId, cpOrderId, transaction_id, Integer.valueOf(money), payType, product_id, Config.AGENT, site, returnJson.toString());
				} catch (Exception e) {
					LogUtil.error("苹果支付充值发货异常：", e);
				}
				
        		
            }else{  
                //验证失败 
            	result.put("state", -1);
                this.postData(resp, result.toString());
            }  
        }  
	}
	
// 大概返回参数
//	<pre name="code" class="javascript">{  
//	    "receipt": {  
//	        "original_purchase_date_pst": "2016-04-28 03:18:49 America/Los_Angeles",  
//	        "purchase_date_ms": "1461838729285",  
//	        "unique_identifier": "d4e721ec67ef2feca7fbdbd25a45cfb37e10ea7b",  
//	        "original_transaction_id": "1000000208620470",  
//	        "bvrs": "1.1",  
//	        "transaction_id": "1000000208620470",  
//	        "quantity": "1",  
//	        "unique_vendor_identifier": "8E19EEC4-33D7-4536-B62E-112BAC68EECD",  
//	        "item_id": "1108798151",  
//	        "product_id": "1244",  
//	        "purchase_date": "2016-04-28 10:18:49 Etc/GMT",  
//	        "original_purchase_date": "2016-04-28 10:18:49 Etc/GMT",  
//	        "purchase_date_pst": "2016-04-28 03:18:49 America/Los_Angeles",  
//	        "bid": "com.doctorHys",  
//	        "original_purchase_date_ms": "1461838729285"  
//	    },  
//	    "status": 0  
//	}  
}
