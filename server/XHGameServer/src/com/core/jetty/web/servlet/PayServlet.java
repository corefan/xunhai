package com.core.jetty.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.common.Config;
import com.common.GameContext;
import com.common.MD5Service;
import com.common.ServiceCollection;
import com.domain.player.Player;
import com.service.IPayService;
import com.service.IPlayerService;
import com.util.HttpUtil;
import com.util.LogUtil;

/**
 * 充值
 * @author ken
 * @date 2017-6-17
 */
public class PayServlet extends BaseServlet {

	private static final long serialVersionUID = 2578968023647233490L;

	public PayServlet() {
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		doPay(request, response);
	}
	
	/**
	 * 充值处理
	 */
	private void doPay(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			
			ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
			IPlayerService playerService = serviceCollection.getPlayerService();
			IPayService payService = serviceCollection.getPayService();
			
			//ILogService logService = serviceCollection.getLogService();
			
			JSONObject result = new JSONObject();
			
			JSONObject jsonObject = dealMsg(request);
			if (jsonObject == null){
				result.put("result", 1);
				postData(response, result.toString());
				return;
			}
			
			String content = jsonObject.getString("content"); // 订单信息
			String contents[] = content.split("\\|");

			if (contents.length != 7) {
				// 1.参数有误
				result.put("result", 1);
				postData(response, result.toString());
				return;
			}
			try {
				Long userId = Long.valueOf(contents[0]); // 玩家账号
				String site = contents[1]; // 游戏站点
				long playerId = Long.valueOf(contents[2]); // 玩家编号
				Integer payItemId = Integer.valueOf(contents[3]); // 商品编号
				Integer payType = Integer.valueOf(contents[4]); // 支付类型
				Integer money = Integer.valueOf(contents[5]); // 金额
				String sign = contents[6]; // 签名
	
				String payIP = HttpUtil.getRequestIp(request);
				
				if (Config.WEB_CHARGE_IP_LIST != null && !Config.WEB_CHARGE_IP_LIST.contains("*")) {
					if (!Config.WEB_CHARGE_IP_LIST.contains(payIP)){
						// 8.不在充值白名单
						result.put("result", 8);
						postData(response, result.toString());
						
						System.out.println("不在充值白名单:"+payIP);
						return;
					}
				}
				
				Player player = playerService.getPlayerByID(playerId);
				if(player == null || player.getCareer() == 0){
					// 3.角色不存在
					result.put("result", 3);
					postData(response, result.toString());
					return;
				}
				
				if (money < 1) {
					// 4.金额有误
					result.put("result", 4);
					postData(response, result.toString());
					return;
				}
				
				
				String _sign = MD5Service.encryptToUpperString(userId + site + playerId + payItemId + payType + money + Config.WEB_CHARGE_KEY);
				if (!_sign.equalsIgnoreCase(sign)) {
					// 5.Sign错误
					result.put("result", 5);
					postData(response, result.toString());
					return;
				}
				
				
				//调发货接口
				payService.pay(playerId, payItemId);
				
//				PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
//				// 创建充值记录
//				int insertResult = logService.createPayLog_sp(playerId, player.getUserName(), player.getPlayerName(), playerProperty.getLevel(), 
//						orderNo, addDiamond, Double.parseDouble(money), payType, payIP, site);
//				
//				int insertResult = 0;
//				if (insertResult == 1) {
//					// 6.保存失败
//					result.put("result", 6);
//					postData(response, result.toString());
//					return;
//				} else if (insertResult == 2) {
//					// 7.订单已经存在
//					result.put("result", 7);
//					postData(response, result.toString());
//					return;
//				}
				
				result.put("result", 0);
				postData(response, result.toString());
				return;
			} catch (Exception e) {
				postData(response, result.toString());
				return;
			}
		
		} catch (Exception e) {
			LogUtil.error("异常: ",e);
		}
	}

}
