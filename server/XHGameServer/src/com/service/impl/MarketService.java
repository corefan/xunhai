package com.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.cache.BaseCacheService;
import com.cache.CacheService;
import com.cache.CacheSynDBService;
import com.common.DateService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.LockService;
import com.common.ServiceCollection;
import com.constant.CacheConstant;
import com.constant.CacheSynConstant;
import com.constant.ExceptionConstant;
import com.constant.InOutLogConstant;
import com.constant.ItemConstant;
import com.constant.LockConstant;
import com.constant.RewardTypeConstant;
import com.dao.market.BaseMarketDAO;
import com.dao.market.PlayerMarketDAO;
import com.domain.GameEntity;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.bag.BaseItem;
import com.domain.market.BaseMarket;
import com.domain.market.PlayerMarket;
import com.domain.player.PlayerWealth;
import com.domain.vip.PlayerVip;
import com.message.MarketProto.S_GetMarketItemList;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.IBagService;
import com.service.ILogService;
import com.service.IMarketService;
import com.service.IPlayerService;
import com.service.IProtoBuilderService;
import com.service.IVipService;
import com.util.IDUtil;
import com.util.LogUtil;

/**
 * @author jiangqin
 * @date 2017-4-18
 */
public class MarketService implements IMarketService {
	private BaseMarketDAO baseMarketDAO = new BaseMarketDAO();	
	private PlayerMarketDAO playerMarketDAO = new PlayerMarketDAO();

	@Override
	public void initBaseCache() {
		Map<Integer, BaseMarket> baseMarketMap = new HashMap<Integer, BaseMarket>();		
		List<BaseMarket> baseMarketLists = baseMarketDAO.listBaseMarket();		
		for(BaseMarket baseMarket : baseMarketLists){			
			baseMarketMap.put(baseMarket.getMarketId(), baseMarket);
		}		
		BaseCacheService.putToBaseCache(CacheConstant.BASE_MARKET, baseMarketMap);
	}		


	@Override
	public int marketBuy(long playerId, int marketId, int num, boolean useFlag)throws Exception {		
		if (playerId == 0 || marketId <= 0 || num <= 0) throw new GameException(ExceptionConstant.ERROR_10000);
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();		
		IPlayerService playerService = serviceCollection.getPlayerService();
		IVipService vipService = serviceCollection.getVipService();
		IBagService bagService = serviceCollection.getBagService();
		
		BaseMarket baseMarket = this.getBaseMarketById(marketId);
		if(baseMarket == null){
			LogUtil.error("baseMarket is null with id is "+marketId);
			throw new GameException(ExceptionConstant.ERROR_10000);
		}
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_MARKET)) {	
			// vip限购
			if(baseMarket.getVipLimit() > 0){
				PlayerVip playerVip = vipService.getPlayerVip(playerId);			
				if(playerVip == null || playerVip.getLevel() == 0) throw new GameException(ExceptionConstant.MARKET_2702);
			}			
			
			if(baseMarket.getOnTime() != null && baseMarket.getDownTime() != null){
				Date onTime = DateService.getDateString(baseMarket.getOnTime());
				Date downTime = DateService.getDateString(baseMarket.getDownTime());
				if (!DateService.isInTime(onTime, downTime)) throw new GameException(ExceptionConstant.MARKET_2700);
			}	
			
			int curBuyNum = 0;
			if(baseMarket.getLimitNum() > 0){	
				// 限购次数		
				PlayerMarket playerMarket = this.getPlayerMarketById(playerId, marketId);		
				if(playerMarket == null){
					playerMarket = new PlayerMarket();
					playerMarket.setId(IDUtil.geneteId(PlayerMarket.class));
					playerMarket.setMarketId(marketId);
					playerMarket.setPlayerId(playerId);
					this.getPlayerMarketMap(playerId).put(playerMarket.getMarketId(), playerMarket);				
					playerMarketDAO.createPlayerMarket(playerMarket);	
				}
				curBuyNum = playerMarket.getCurBuyNum() + num;
				
				if (curBuyNum > baseMarket.getLimitNum()) throw new GameException(ExceptionConstant.MARKET_2701);
			}		
			
			//这样检查空间有问题，在背包满了，但可以叠加的情况下，会抛出异常。修改为在BagService添加物品时检查
//			int freeGridNum = bagService.getFreeGridNumByPlayerID(playerId);
//			if (freeGridNum < 1) {
//				throw new GameException(ExceptionConstant.BAG_1304);
//			}
			
			// 扣钱
			PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);
			int sellPrice = baseMarket.getPrice() * num;
			if (baseMarket.getDiscount() > 0){
				sellPrice = (int)Math.ceil(baseMarket.getPrice() * baseMarket.getDiscount() * num * 0.01);				
			}			
			
			int diamond = 0;
			int gold = 0;
			int stone = 0;
			switch(baseMarket.getMoneyType()){			
				case RewardTypeConstant.MONEY: 
					if(playerWealth.getGold() < sellPrice) throw new GameException(ExceptionConstant.PLAYER_1112);
					gold = sellPrice;					
					break;
				case RewardTypeConstant.DIAMOND: 
					if(playerWealth.getDiamond() < sellPrice) throw new GameException(ExceptionConstant.PLAYER_1113);
					diamond = sellPrice;
					break;
				case RewardTypeConstant.STONE: 
					if(playerWealth.getStone() < sellPrice) throw new GameException(ExceptionConstant.PLAYER_1114);
					stone = sellPrice;
					break;
				default:
					LogUtil.error("商城货币配置有误。。");
					break;						
			}
		
			BaseItem baseItem = bagService.getBaseItemById(baseMarket.getItemId());
			
			if(useFlag){
				//快捷使用
				this.quickBuyAndUse(playerId, baseItem, num);
			}else{
				// 添加物品
				serviceCollection.getRewardService().fetchRewardOne(playerId, baseMarket.getItemType(), baseMarket.getItemId(), num, 0);
			}
				
		    // 扣钱
			if(gold > 0){
				playerService.addGold_syn(playerId, -gold);
			}
		
			if(diamond > 0){
				playerService.addDiamond_syn(playerId, -diamond, InOutLogConstant.DIAMOND_OF_4);
			}
			
			if(stone > 0){
				playerService.addStone_syn(playerId, -stone);
			}
					
			if(baseMarket.getLimitNum() > 0){
				// 限购次数		
				PlayerMarket playerMarket = this.getPlayerMarketById(playerId, marketId);	
				
				playerMarket.setCurBuyNum(curBuyNum);
				this.updatePlayerMarket(playerMarket);
			}
			
			try {
				// 商城日志创建
				ILogService logService = serviceCollection.getLogService();			
				logService.createMarketLog(playerId,  baseMarket.getItemId(), baseItem.getName(), diamond, num);
			} catch (Exception e1) {
				LogUtil.error("商城日志异常：",e1);
			}
			
			return curBuyNum;
		}
	}

	/**
	 * 快捷购买并使用
	 */
	private void quickBuyAndUse(long playerId, BaseItem baseItem, int num){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();		
		
		int effectValue = baseItem.getEffectValue();
		switch (baseItem.getEffectType()) {
		case ItemConstant.EFFECT_TYPE_20:
			serviceCollection.getFurnaceService().addFurnacePiece(playerId, 1, effectValue * num);
			break;
		case ItemConstant.EFFECT_TYPE_21:
			serviceCollection.getFurnaceService().addFurnacePiece(playerId, 2, effectValue * num);
			break;
		case ItemConstant.EFFECT_TYPE_22:
			serviceCollection.getFurnaceService().addFurnacePiece(playerId, 3, effectValue * num);
			break;
		case ItemConstant.EFFECT_TYPE_23:
			serviceCollection.getFurnaceService().addFurnacePiece(playerId, 4, effectValue * num);
			break;
		case ItemConstant.EFFECT_TYPE_24:
			serviceCollection.getFurnaceService().addFurnacePiece(playerId, 5, effectValue * num);
			break;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void quartzDaily() {		
		try {
			playerMarketDAO.updateAllPlayerMarketData();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();	
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		List<Long> playerIDSetClone = new ArrayList<Long>(playerService.getPlayerIDCache());
		for(long playerId : playerIDSetClone){
			Map<Integer, PlayerMarket> playerMarketMap = (Map<Integer, PlayerMarket>)CacheService.getFromCache(CacheConstant.PLAYER_MARKET + playerId);
			if(playerMarketMap == null || playerMarketMap.isEmpty()) continue;
			
			S_GetMarketItemList.Builder builder = S_GetMarketItemList.newBuilder();	
			for(Map.Entry<Integer, PlayerMarket> entry : playerMarketMap.entrySet()){
				PlayerMarket playerMarket = entry.getValue();
				playerMarket.setCurBuyNum(0);
				
				builder.addMarketItemList(protoBuilderService.buildMarketItemMsg(playerMarket));
			}	
			
			if(gameSocketService.checkOnLine(playerId)){
				MessageObj msg = new MessageObj(MessageID.S_GetMarketItemList_VALUE, builder.build().toByteArray());
				gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
			}

		}		
	}
	
	/** 获取玩家商城缓存*/
	@SuppressWarnings("unchecked")
	public Map<Integer, PlayerMarket> getPlayerMarketMap(long playerId){		
		Map<Integer, PlayerMarket> playerMarketMap = (Map<Integer, PlayerMarket>)CacheService.getFromCache(CacheConstant.PLAYER_MARKET + playerId);
		if(playerMarketMap == null){
			playerMarketMap = new ConcurrentHashMap<Integer, PlayerMarket>();
			List<PlayerMarket> list = playerMarketDAO.getPlayerMarketByPlayerId(playerId);
			for(PlayerMarket playerMarket : list){				
				playerMarketMap.put(playerMarket.getMarketId(), playerMarket);
			}				
			CacheService.putToCache(CacheConstant.PLAYER_MARKET + playerId, playerMarketMap);
		}		
		return 	playerMarketMap;
	}	
	
	/** 获取玩家商城某个物品的交易信息*/
	private PlayerMarket getPlayerMarketById(long playerId, int marketId){
		if(this.getPlayerMarketMap(playerId) == null) return null;
		return this.getPlayerMarketMap(playerId).get(marketId);
	}
	
	/** 获取商城某个物品的基础信息*/
	@SuppressWarnings("unchecked")
	private BaseMarket getBaseMarketById(int marketId){
		Map<Integer, BaseMarket> baseMarketMap = (Map<Integer, BaseMarket>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_MARKET);
		return baseMarketMap.get(marketId);
	}
	
	/** 更新玩家某个物品的商城交易信息*/
	private void updatePlayerMarket(PlayerMarket playerMarket) {
		Set<GameEntity> updateCacheList = CacheSynDBService.getFromFiveUpdateThreeCache(CacheSynConstant.PLAYER_MARKET);
		if (!updateCacheList.contains(playerMarket)) {
			updateCacheList.add(playerMarket);
		}		
	}

	@Override
	public void deleteCache(long playerId) {
		CacheService.deleteFromCache(CacheConstant.PLAYER_MARKET + playerId);			
	}


}
