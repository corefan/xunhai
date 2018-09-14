package com.action;

import java.util.Map;

import com.common.GameContext;
import com.common.GameSocketService;
import com.common.ServiceCollection;
import com.core.GameMessage;
import com.domain.MessageObj;
import com.domain.market.PlayerMarket;
import com.message.MarketProto.C_MarketBuy;
import com.message.MarketProto.S_GetMarketItemList;
import com.message.MarketProto.S_MarketBuy;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.IMarketService;
import com.service.IProtoBuilderService;

/**
 * 商城接口
 * @author jiangqin
 * @date 2017-4-21
 */
public class MarketAction {

	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	private GameSocketService gameSocketService = serviceCollection.getGameSocketService();
	
	/**	 获取商城物品列表信息 */
	
	public void GetMaketItemList(GameMessage gameMessage) throws Exception {
		IMarketService marketService = serviceCollection.getMarketService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		Map<Integer, PlayerMarket> playerMarketMap = marketService.getPlayerMarketMap(playerId);		
		S_GetMarketItemList.Builder builder = S_GetMarketItemList.newBuilder();
		for(Map.Entry<Integer, PlayerMarket> entry : playerMarketMap.entrySet()){
			PlayerMarket playerMarket = entry.getValue();
			builder.addMarketItemList(protoBuilderService.buildMarketItemMsg(playerMarket));
		}		
		MessageObj msg = new MessageObj(MessageID.S_GetMarketItemList_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}
	
	/** 商城购买*/
	
	public void MaketBuyItem(GameMessage gameMessage) throws Exception {
		IMarketService marketService = serviceCollection.getMarketService();		
		long playerId = gameMessage.getConnection().getPlayerId();
		C_MarketBuy param = C_MarketBuy.parseFrom(gameMessage.getData());
		
		int marketId = param.getMarketId();
		int num = param.getNum();		
		boolean useFlag = param.getUseFlag();
		int curBuyNum = marketService.marketBuy(playerId, marketId, num, useFlag);
		
		S_MarketBuy.Builder builder = S_MarketBuy.newBuilder();
		builder.setCurBuyNum(curBuyNum);
		builder.setMarketId(marketId);
		MessageObj msg = new MessageObj(MessageID.S_MarketBuy_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);	
	}
}
