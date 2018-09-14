package com.action;

import java.util.Map;

import com.common.GameContext;
import com.common.GameSocketService;
import com.common.ServiceCollection;
import com.constant.ItemConstant;
import com.core.GameMessage;
import com.domain.MessageObj;
import com.domain.bag.PlayerEquipment;
import com.domain.trading.PlayerTradeBag;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.TradingProto.C_GetTradeList;
import com.message.TradingProto.C_OffShelf;
import com.message.TradingProto.C_ReUpShelf;
import com.message.TradingProto.C_SystemItemBuy;
import com.message.TradingProto.C_TradeBuy;
import com.message.TradingProto.C_TradeSell;
import com.message.TradingProto.S_ExtendGrid;
import com.message.TradingProto.S_GetPlayerTradeList;
import com.service.IEquipmentService;
import com.service.IProtoBuilderService;
import com.service.ITradeService;

/**
 * 交易所接口
 * @author ken
 * @date 2018年7月11日
 */
public class TradeAction {
	private ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
	private GameSocketService gameSocketService = serviceCollection.getGameSocketService();
	
	/**
	 * 寄售-获取交易物品列表
	 */
	
	public void getTradeList(GameMessage gameMessage) throws Exception {
		ITradeService tradeService = serviceCollection.getTradeService();
		long playerId = gameMessage.getConnection().getPlayerId();
		C_GetTradeList param = C_GetTradeList.parseFrom(gameMessage.getData());
		int typeId = param.getTypeId();
		int type = param.getType();
		int satrt = param.getStart();
		int offset = param.getOffset();		
		tradeService.getTradeListPag(playerId, type, typeId, satrt, offset);		
	}

	/**
	 * 寄售-扩展货架
	 */
	
	public void extendGrid(GameMessage gameMessage) throws Exception {
		ITradeService tradeService = serviceCollection.getTradeService();
		
		long playerId = gameMessage.getConnection().getPlayerId();
		
		int maxNum = tradeService.extendGrid(playerId);
		
		S_ExtendGrid.Builder builder = S_ExtendGrid.newBuilder();
		builder.setGridNum(maxNum);
		MessageObj msg = new MessageObj(MessageID.S_ExtendGrid_VALUE, builder.build().toByteArray());
		gameSocketService.sendData(gameMessage.getConnection(), msg);
	}

	/**
	 * 寄售-下架
	 */
	
	public void offShelf(GameMessage gameMessage) throws Exception {
		ITradeService tradeService = serviceCollection.getTradeService();		
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_OffShelf param = C_OffShelf.parseFrom(gameMessage.getData());
		long playerBagId = param.getPlayerBagId();
		tradeService.offShelf(playerId, playerBagId);
	}

	/**
	 * 寄售-出售物品
	 */
	
	public void tradeSell(GameMessage gameMessage) throws Exception {
		ITradeService tradeService = serviceCollection.getTradeService();		
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_TradeSell param = C_TradeSell.parseFrom(gameMessage.getData());
		long playerBagId = param.getPlayerBagId();		
		int price = param.getPrice();
		int num = param.getNum();
		tradeService.tradeSell(playerId, playerBagId, num, price);		
	}

	/**
	 * 寄售-购买物品
	 */
	
	public void tradeBuy(GameMessage gameMessage) throws Exception {
		ITradeService tradeService = serviceCollection.getTradeService();		
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_TradeBuy param = C_TradeBuy.parseFrom(gameMessage.getData());
		long playerBagId = param.getPlayerBagId();	
		int num = param.getNum();
		
		tradeService.tradeBuy(playerId, playerBagId, num);		
	}

	/**
	 * 系统-购买物品
	 */
	
	public void systemItemBuy(GameMessage gameMessage) throws Exception {
		ITradeService tradeService = serviceCollection.getTradeService();		
		long playerId = gameMessage.getConnection().getPlayerId();
		C_SystemItemBuy param = C_SystemItemBuy.parseFrom(gameMessage.getData());
		int itemId = param.getItemId();
		int num = param.getNum();
		tradeService.systemItemBuy(playerId, itemId, num);
	}

	/**
	 * 寄售-获取个人交易物品列表
	 */
	
	public void getPlayerTradeList(GameMessage gameMessage) throws Exception {
		ITradeService tradeService = serviceCollection.getTradeService();		
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();	
		IEquipmentService equipmentService = serviceCollection.getEquipmentService();
		long playerId = gameMessage.getConnection().getPlayerId();
		
		Map<Long, PlayerTradeBag> playerTradeBagMap = tradeService.getPlayerTradMap(playerId);			
		if(playerTradeBagMap != null) {
			S_GetPlayerTradeList.Builder builder = S_GetPlayerTradeList.newBuilder();			
			for(Map.Entry<Long, PlayerTradeBag> entry: playerTradeBagMap.entrySet()){
				PlayerTradeBag playerTradeBag = entry.getValue();
				builder.addListPlayerTradebag(protoBuilderService.buildPlayerTradeBagMsg(playerTradeBag));
			
				if (playerTradeBag.getState() == 0) continue;
				if (playerTradeBag.getGoodsType() == ItemConstant.GOODS_TYPE_EQUPMENT){
					PlayerEquipment playerEquipment = equipmentService.getPlayerEquipmentByID(playerTradeBag.getPlayerId(), playerTradeBag.getItemId());
					if(playerEquipment.getState() != ItemConstant.EQUIP_STATE_TRADE) continue;
					builder.addListPlayerTradeEquipment(protoBuilderService.buildPlayerTradeEquipmentMsg(playerEquipment));
				}				
			}	
			
			MessageObj msg = new MessageObj(MessageID.S_GetPlayerTradeList_VALUE, builder.build().toByteArray());
			gameSocketService.sendData(gameMessage.getConnection(), msg);
			
			playerTradeBagMap = null;		
		}
	}

	/**
	 * 过期交易物品重新上架
	 */
	
	public void reUpShelf(GameMessage gameMessage) throws Exception {
		ITradeService tradeService = serviceCollection.getTradeService();		
		long playerId = gameMessage.getConnection().getPlayerId();
		
		C_ReUpShelf param = C_ReUpShelf.parseFrom(gameMessage.getData());
		long playerTradeBagId = param.getPlayerTradeBagId();
		
		tradeService.reUpShelf(playerId, playerTradeBagId);		
	}
	
}
