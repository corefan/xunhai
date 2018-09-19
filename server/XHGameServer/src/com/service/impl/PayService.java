package com.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpEventListener;
import org.eclipse.jetty.client.HttpEventListenerWrapper;
import org.eclipse.jetty.client.HttpExchange;
import org.eclipse.jetty.http.HttpMethods;
import org.eclipse.jetty.io.Buffer;
import org.eclipse.jetty.io.ByteArrayBuffer;
import org.json.JSONException;
import org.json.JSONObject;

import com.cache.BaseCacheService;
import com.common.Config;
import com.common.DateService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.LockService;
import com.common.MD5Service;
import com.common.ServiceCollection;
import com.constant.CacheConstant;
import com.constant.ExceptionConstant;
import com.constant.InOutLogConstant;
import com.constant.LockConstant;
import com.constant.PayConstant;
import com.core.jetty.core.HttpClientFactory;
import com.dao.pay.BasePayDAO;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.activity.BaseChargeActivity;
import com.domain.pay.BasePay;
import com.domain.player.Player;
import com.domain.player.PlayerDaily;
import com.domain.player.PlayerOptional;
import com.domain.player.PlayerProperty;
import com.domain.player.PlayerWealth;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.PlayerProto.S_FinishPay;
import com.message.PlayerProto.S_GetFristPayIdList;
import com.message.PlayerProto.S_Pay;
import com.service.IActivityService;
import com.service.ILogService;
import com.service.IPayService;
import com.service.IPlayerService;
import com.service.IVipService;
import com.util.IDUtil;
import com.util.LogUtil;
import com.util.ResourceUtil;

/**
 * 支付
 * @author ken
 * @date 2017-6-20
 */
public class PayService implements IPayService {

	private BasePayDAO basePayDAO = new BasePayDAO();

	
	/** 支付宝支付*/
	public static final int PAY_TYPE_1 = 1;
	/** 微信支付*/
	public static final int PAY_TYPE_2 = 2;
	/** 苹果支付*/
	public static final int PAY_TYPE_3 = 3;
	/** 其他*/
	public static final int PAY_TYPE_4 = 4;
	
	@Override
	public void initBaseCache() {
		
		Map<Integer, BasePay> map = new HashMap<Integer, BasePay>();
		List<BasePay> basePays = basePayDAO.listBasePays();
		for(BasePay model : basePays){
			map.put(model.getId(), model);
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_PAY, map);
	}

	/**
	 * 根据商品编号取配置
	 */
	@SuppressWarnings("unchecked")
	private BasePay getBasePay(int payItemId){
		Map<Integer, BasePay> map = (Map<Integer, BasePay>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_PAY);
		return map.get(payItemId);
	}
	
	//http://192.168.0.150:9000/pay?payType=1&content=10000|sk_0001|6124|1|1|1|B469DF29FD8AEFAC930DA9AEC6B05499
	// userId|site|playerId|payItemId|payType|money|sign
	@Override
	public void getPayInfo(long playerId, int payItemId, int payType) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_PAY)) {	
			Player player = playerService.getPlayerByID(playerId);
			if(player == null) return;
			
			BasePay basePay = this.getBasePay(payItemId);
			if(basePay == null) return;
			
//			int money = 1; //TODO test
			
			int money = basePay.getPrice();
//			if(Config.TEST_SWITCH){
//				 throw new GameException(ExceptionConstant.PAY_3403);
//			}
			
			String cpOrderId = String.valueOf(IDUtil.geneteId(PayService.class));
			
			StringBuilder content = new StringBuilder(1 << 8);
			content.append(player.getUserId()).append("|");
			content.append(player.getSite()).append("|");
			content.append(player.getPlayerId()).append("|");
			content.append(payItemId).append("|");
			content.append(payType).append("|");
			content.append(money).append("|");
			content.append(cpOrderId).append("|");
			String sign = MD5Service.encryptToUpperString(player.getUserId() + player.getSite() + player.getPlayerId() + payItemId + payType + money + cpOrderId + Config.WEB_CHARGE_KEY);
			content.append(sign);
			
			
			if("yunyou".equals(Config.AGENT)){
				//因为云游这边支付回调没有扩展参数， 所以只能绑定到订单号这里
				cpOrderId = cpOrderId + "@"+content.toString();
			}
			
			if(payType == PAY_TYPE_3){
				S_Pay.Builder builder = S_Pay.newBuilder();
				builder.setPayItemId(payItemId);
				builder.setPayType(payType);
				builder.setPayInfo(content.toString());
				builder.setCpOrderId(cpOrderId);
				MessageObj msg = new MessageObj(MessageID.S_Pay_VALUE, builder.build().toByteArray());
				serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);
			}else{
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("payType", payType);
					jsonObject.put("content", content.toString());
					jsonObject.put("cpOrderId", cpOrderId);
					sendData(jsonObject);
				} catch (Exception e) {
					LogUtil.error("异常:",e);
				}
			}
			
		}
	}
	
	/**
	 * 支付信息返回
	 */
	private void offerPayInfo(JSONObject jsonObject) throws JSONException{
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		
		int state = jsonObject.getInt("state");
		if(state != 0){
			return;
		}
		
		long playerId = jsonObject.getLong("playerId");
		int payItemId = jsonObject.getInt("payItemId");
		int payType = jsonObject.getInt("payType");
		String payInfo = jsonObject.getString("payInfo");
		if(payType == PAY_TYPE_2){
			JSONObject json = new JSONObject(payInfo);
			payInfo = json.getString("param");
		}
		String cpOrderId = jsonObject.getString("cpOrderId");
		
		S_Pay.Builder builder = S_Pay.newBuilder();
		builder.setPayItemId(payItemId);
		builder.setPayType(payType);
		builder.setPayInfo(payInfo);
		builder.setCpOrderId(cpOrderId);
		
		MessageObj msg = new MessageObj(MessageID.S_Pay_VALUE, builder.build().toByteArray());
		serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);
	}
	
	
	/**
	 * 发送到支付服
	 */
	private void sendData(JSONObject jsonObject) throws Exception {
		
		Buffer buffer = new ByteArrayBuffer(jsonObject.toString().getBytes());

		HttpEventListener eventListener = new HttpEventListenerWrapper() {
			@Override
			public void onResponseContent(Buffer content) 
					throws IOException {
				try {
					String result = content.toString("UTF-8");
					JSONObject jsonObject = new JSONObject(result);
					offerPayInfo(jsonObject);
					
				} catch (Exception e) {
					LogUtil.error("返回数据异常:",e);
				}
			}

			@Override
			public void onResponseComplete() throws IOException {
				super.onResponseComplete();
			}

			@Override
			public void onExpire() {
				super.onExpire();
			}

			@Override
			public void onException(Throwable ex) {
				super.onException(ex);
			}
		};
		
		HttpExchange exchange = new HttpExchange();
		exchange.setEventListener(eventListener);
		exchange.setURL(Config.PAY_URL);
		exchange.setMethod(HttpMethods.POST);
		exchange.setRequestContentType("text/html;charset=utf-8");
		exchange.setRequestContent(buffer);
		
		HttpClient httpClient = HttpClientFactory.getInstance();
		httpClient.send(exchange);
		
	}

	@Override
	public void pay(long playerId, Integer payItemId) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IVipService vipService = serviceCollection.getVipService();
		IActivityService activityService = serviceCollection.getActivityService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PAY)) {
			
			Player player = playerService.getPlayerByID(playerId);
			if(player == null) return;
			
			BasePay basePay = this.getBasePay(payItemId);
			if(basePay == null) return;

			PlayerOptional playerOptional = playerService.getPlayerOptionalById(playerId);			
			if(playerOptional == null) return;	
			
			// 设置首冲奖励状态
			if(playerOptional.getFristPayRewardState() == 0){
				playerOptional.setFristPayRewardState(1);
				
				PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
				try {
					ILogService logService = serviceCollection.getLogService();
					logService.createFirstPayLvLog(player.getSite(), playerProperty.getLevel());
				} catch (Exception e) {
					LogUtil.error("首冲等级日志异常：",e);
				}
			}				
			
			int addDiamond = 0;			
			if(!playerOptional.getFristPayIdList().contains(payItemId)){
				playerOptional.getFristPayIdList().add(payItemId);
				playerOptional.setFristPayIdList(playerOptional.getFristPayIdList());
				
				addDiamond = basePay.getGold() + basePay.getPremium();
			}else{
				addDiamond = basePay.getGold();
			}	
			
			String costName = "";
			PlayerDaily playerDaily = playerService.getPlayerDailyById(playerId);
			if(basePay.getType() == PayConstant.PAY_TYPE_MONTH_CARD){
				if(playerDaily.getMonthCardVaildTime() == 0){
					playerDaily.setMonthCardVaildTime(System.currentTimeMillis());
				}
				
				playerDaily.setMonthCardVaildTime(playerDaily.getMonthCardVaildTime() + (DateService.DAY_MILLISECOND * 30));
				
				costName = InOutLogConstant.DIAMOND_OF_14;
			}else if(basePay.getType() == PayConstant.PAY_TYPE_VIP){
				// 激活vip
				vipService.ActiviteVip(playerId, payItemId);
				
				costName = InOutLogConstant.DIAMOND_OF_6;
			}else if(basePay.getType() == PayConstant.PAY_TYPE_ARTIFACT){
				
				// 购买神器
				activityService.buyArtifact(playerId);
				
				costName = InOutLogConstant.DIAMOND_OF_15;	
			}
			
			playerService.updatePlayerOptional(playerOptional);
			
			playerDaily.setTodayPay(playerDaily.getTodayPay() + basePay.getPrice());
			playerService.updatePlayerDaily(playerDaily);
				
			PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);
			playerWealth.setTotalPay(playerWealth.getTotalPay() + basePay.getPrice());
			
			// 判断是否在七天累计充值活动时间内
			BaseChargeActivity baseChargeActivity = activityService.getBaseChargeActivity();
			if(baseChargeActivity != null){
				playerWealth.setSevenPay(playerWealth.getSevenPay() + basePay.getPrice());
				
				activityService.getSevenPayReward(playerId, baseChargeActivity);
			}
			
			playerService.addDiamond_syn(playerId, addDiamond, costName);
			
			//邮件通知 
			serviceCollection.getMailService().systemSendMail(playerId, ResourceUtil.getValue("mail_1"), ResourceUtil.getValue("pay_email_1", basePay.getPrice()),  "", 0);
			
			S_FinishPay.Builder builder = S_FinishPay.newBuilder();
			builder.setPayItemId(payItemId);
			MessageObj msg = new MessageObj(MessageID.S_FinishPay_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);		
		}
	}

	@Override
	public void getFristPayIdList(long playerId) throws Exception{
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PAY)) {
			PlayerOptional playerOptional = playerService.getPlayerOptionalById(playerId);			
			if(playerOptional == null) throw new GameException(ExceptionConstant.PAY_3400);
			
			S_GetFristPayIdList.Builder builder = S_GetFristPayIdList.newBuilder();
			builder.addAllPayItemId(playerOptional.getFristPayIdList());
			MessageObj msg = new MessageObj(MessageID.S_GetFristPayIdList_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}
	}
}
