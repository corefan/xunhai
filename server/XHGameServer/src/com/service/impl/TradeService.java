package com.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.cache.CacheService;
import com.cache.CacheSynDBService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.LockService;
import com.common.ServiceCollection;
import com.constant.CacheConstant;
import com.constant.CacheSynConstant;
import com.constant.ConfigConstant;
import com.constant.ExceptionConstant;
import com.constant.InOutLogConstant;
import com.constant.ItemConstant;
import com.constant.LockConstant;
import com.constant.RewardTypeConstant;
import com.constant.TaskConstant;
import com.dao.trading.PlayerTradeDAO;
import com.domain.GameEntity;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.bag.BaseEquipment;
import com.domain.bag.BaseItem;
import com.domain.bag.PlayerBag;
import com.domain.bag.PlayerEquipment;
import com.domain.player.PlayerExt;
import com.domain.player.PlayerWealth;
import com.domain.trading.PlayerTradeBag;
import com.message.BagProto.S_SynBagItem;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.TradingProto.S_OffShelf;
import com.message.TradingProto.S_ReUpShelf;
import com.message.TradingProto.S_SynTradeList;
import com.message.TradingProto.S_TradeBuy;
import com.message.TradingProto.S_TradeSell;
import com.service.IBagService;
import com.service.ICommonService;
import com.service.IEquipmentService;
import com.service.IMailService;
import com.service.IPlayerService;
import com.service.IProtoBuilderService;
import com.service.IRewardService;
import com.service.ITradeService;
import com.util.IDUtil;
import com.util.LogUtil;
import com.util.ResourceUtil;
import com.util.SplitStringUtil;

/**
 * 交易行
 * @author jiangqin
 * @date 2017-3-25
 */
public class TradeService implements ITradeService{
	
	private PlayerTradeDAO playerTradeDAO = new PlayerTradeDAO();
	

	@Override
	public void initCache() {
		this.getALLPlayerTradeBagMap();
	}
	
	
	@Override
	public void getTradeListPag(long playerId, int type, int typeId, int start, int offset) {
		if (playerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();	
		IEquipmentService equipmentService = serviceCollection.getEquipmentService();		
		IBagService bagService = serviceCollection.getBagService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		Map<Long, PlayerTradeBag> map = this.getPlayerTradeBagMapByType(type);		
		List<PlayerTradeBag> listBag = new ArrayList<PlayerTradeBag>();			
		long curTime = System.currentTimeMillis() + 60000;
		for( Map.Entry<Long, PlayerTradeBag> entry : map.entrySet()){
			PlayerTradeBag playerTradeBag = entry.getValue();
			if (playerTradeBag.getState() == 0) continue;	
			
			if(curTime >= playerTradeBag.getOverTime()) continue;
			
			if (type == ItemConstant.GOODS_TYPE_EQUPMENT){
				PlayerEquipment	playerEquipment = equipmentService.getPlayerEquipmentByID(playerTradeBag.getPlayerId(), playerTradeBag.getItemId());
				if(playerEquipment == null){
					System.out.println("getTradeListPag  playerTradeBag.getPlayerId(): " + playerTradeBag.getPlayerId() + ",　 playerTradeBag.getItemId() :" + playerTradeBag.getItemId()  + "is null");
					continue;
				}
				if(playerEquipment.getState() != ItemConstant.EQUIP_STATE_TRADE) continue;
				
				BaseEquipment baseEquipment = equipmentService.getBaseEquipmentById(playerEquipment.getEquipmentId());								
				if (typeId > 0){
					if(typeId != baseEquipment.getEquipType()) continue;
				}			
				listBag.add(playerTradeBag);
								
			}else{
				BaseItem baseItem = bagService.getBaseItemById((int)playerTradeBag.getItemId());
				if(typeId > 0){
					if (baseItem.getTradeType() != typeId) continue;
				}			
				listBag.add(playerTradeBag);	
			}															
		}	
		
		
		List<PlayerTradeBag>  synList = null;
		if(start <= listBag.size()) {
			int fromIndex = start - 1;
			if(fromIndex < 0) fromIndex=0;
			int toIndex = fromIndex + offset;
			if(toIndex > listBag.size()) toIndex = listBag.size();
			
			synList = listBag.subList(fromIndex, toIndex);
		}else{
			synList = new ArrayList<PlayerTradeBag>();
		}

		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();		
		S_SynTradeList.Builder builder = S_SynTradeList.newBuilder();
		for(PlayerTradeBag playerTradeBag : synList){
			builder.addListPlayerTradebag(protoBuilderService.buildPlayerTradeBagMsg(playerTradeBag));
		
			if (playerTradeBag.getGoodsType() == ItemConstant.GOODS_TYPE_EQUPMENT){
				PlayerEquipment model = equipmentService.getPlayerEquipmentByID(playerTradeBag.getPlayerId(), playerTradeBag.getItemId());
				builder.addListPlayerTradeEquipment(protoBuilderService.buildPlayerTradeEquipmentMsg(model));
			}
		}		
		
		MessageObj msg = new MessageObj(MessageID.S_SynTradeList_VALUE, builder.build().toByteArray());
		gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);		
	}
	

	@Override
	public int extendGrid(long playerId) throws Exception{
		if (playerId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
			
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_TRADE)) {
			int TRADE_GRID_LIMIT = serviceCollection.getCommonService().getConfigValue(ConfigConstant.TRADE_GRID_LIMIT);
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);
			if(playerExt.getTradeGridNum() >= TRADE_GRID_LIMIT) throw new GameException(ExceptionConstant.Trade_2509);
			
			int TRADE_EXTEND_GRID = serviceCollection.getCommonService().getConfigValue(ConfigConstant.TRADE_EXTEND_GRID);	
			PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);
			if (playerWealth.getDiamond() < TRADE_EXTEND_GRID) throw new GameException(ExceptionConstant.PLAYER_1113);
			playerService.addDiamond_syn(playerId, - TRADE_EXTEND_GRID, InOutLogConstant.DIAMOND_OF_5);
					
			playerExt.setTradeGridNum(playerExt.getTradeGridNum() + 1);
			playerService.updatePlayerExt(playerExt);
			
			return playerExt.getTradeGridNum();	
		}

	}

	@Override
	public void offShelf(long playerId, long playerTradeBagId) throws Exception{
		if (playerId < 1 || playerTradeBagId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		IBagService bagService = serviceCollection.getBagService();		
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_TRADE)) {
			PlayerTradeBag playerTradeBag = this.getPlayerTradeBagById(playerTradeBagId);	
			if(playerTradeBag == null) throw new GameException(ExceptionConstant.Trade_2510);
			if(playerTradeBag.getPlayerId() != playerId) throw new GameException(ExceptionConstant.Trade_2511);
			if(playerTradeBag.getItemId() < 0) throw new GameException(ExceptionConstant.Trade_2510);	
			if(playerTradeBag.getGoodsType() == ItemConstant.GOODS_TYPE_EQUPMENT){
				//验证背包是否满
				Integer itemIndex = bagService.getNewItemIndexByPlayerId(playerId);
				if(itemIndex == null){
					throw new GameException(ExceptionConstant.BAG_1304);
				}
				
				PlayerBag playerBag = bagService.createPlayerBag(playerId, playerTradeBag.getItemId(), playerTradeBag.getGoodsType(), playerTradeBag.getIsBinding(), 1, itemIndex);
				IEquipmentService equipmentService = serviceCollection.getEquipmentService();
				PlayerEquipment playerEquipment = equipmentService.getPlayerEquipmentByID(playerId, playerTradeBag.getItemId());
				
				//更新玩家的装备
				playerEquipment.setState(ItemConstant.EQUIP_STATE_BACKPACK);
				equipmentService.updatePlayerEquipment(playerEquipment);

				S_SynBagItem.Builder builder = S_SynBagItem.newBuilder();
				builder.addListPlayerBags(protoBuilderService.buildPlayerBagMsg(playerBag));
				builder.addListPlayerEquipments(protoBuilderService.buildPlayerEquipmentMsg(playerEquipment));
				MessageObj msg = new MessageObj(MessageID.S_SynBagItem_VALUE, builder.build().toByteArray());
				serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msg);
			
			}else{
				IRewardService rewardService = serviceCollection.getRewardService();
				rewardService.fetchRewardOne(playerId, RewardTypeConstant.ITEM, (int)playerTradeBag.getItemId(), playerTradeBag.getNum(), playerTradeBag.getIsBinding());
			}
			
			playerTradeBag.setState(0);
			
			S_OffShelf.Builder builder = S_OffShelf.newBuilder();
			builder.setPlayerTradeBag(protoBuilderService.buildPlayerTradeBagMsg(playerTradeBag));		
			MessageObj msg = new MessageObj(MessageID.S_OffShelf_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
			
			// 闲置交易背包
			playerTradeBag.reset();	
			this.updatePlayerTradeBag(playerTradeBag);	
		}
	}
	
	
	// 寄售
	@Override
	public void tradeSell(long playerId, long playerBagId,  int num, int price) throws Exception{	
		if (playerId < 1 || price < 1 || playerBagId < 1 || num < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		ICommonService commonService = serviceCollection.getCommonService();
		IEquipmentService equipmentService = serviceCollection.getEquipmentService();		
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		IBagService bagService = serviceCollection.getBagService();
		IPlayerService playerService = serviceCollection.getPlayerService();
				
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_TRADE)) {
			
			PlayerBag playerBag = bagService.getPlayerBagById(playerId, playerBagId);
			
			// 若背包物品使绑定的，不能上架
			if(playerBag.getIsBinding() == ItemConstant.ITEM_IS_BINDING)throw new GameException(ExceptionConstant.Trade_2500);
						
			// 货架上限			
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);			
			Map<Long, PlayerTradeBag> playerTradeBagMap = this.getPlayerTradMap(playerId);			
			if(!playerTradeBagMap.isEmpty()){
				int sellNum = playerTradeBagMap.size();
				if (playerExt.getTradeGridNum() <= sellNum) throw new GameException(ExceptionConstant.Trade_2501);				
				playerTradeBagMap = null;
			}			
					
			// 上架费				
			int TRADE_SELL_TAX = commonService.getConfigValue(ConfigConstant.TRADE_SELL_TAX);
			PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);
			int slottingFee =(int) Math.ceil(price*num*0.01*TRADE_SELL_TAX) ;
			if(playerWealth.getGold() < slottingFee) throw new GameException(ExceptionConstant.PLAYER_1112);
			
			PlayerTradeBag playerTradeBag = null;
			PlayerEquipment playerEquipment = null;
			if(playerBag.getGoodsType() == ItemConstant.GOODS_TYPE_EQUPMENT){
				playerEquipment = equipmentService.getPlayerEquipmentByID(playerId, playerBag.getItemId());
				BaseEquipment baseEquipment = equipmentService.getBaseEquipmentById(playerEquipment.getEquipmentId());
				if(baseEquipment == null){
					LogUtil.error("BaseEquipment is null with id is "+ playerEquipment.getEquipmentId());
					return;
				}
				
				if(baseEquipment.getIsTrade() == 0) throw new GameException(ExceptionConstant.Trade_2500);		
				if(playerEquipment.getState() != ItemConstant.EQUIP_STATE_BACKPACK) throw new GameException(ExceptionConstant.EQUIP_1401);	
				
				// 最高最低价限制 
				if (price > baseEquipment.getTradeMaxPrice() || price < baseEquipment.getTradeMinPrice()) throw new GameException(ExceptionConstant.Trade_2502);
		
			}else{
				BaseItem baseItem = bagService.getBaseItemById((int)playerBag.getItemId());
				// 最高最低价限制 
				if (price > baseItem.getTradeMaxPrice() || price < baseItem.getTradeMinPrice()) throw new GameException(ExceptionConstant.Trade_2502);
	
			}			
			
			if(playerBag.getNum() < num) throw new GameException(ExceptionConstant.BAG_1306);		
			
			// 改变玩家宝玉数据(扣除手续费)			
			playerService.addGold_syn(playerId, - slottingFee);
			
			// 生成交易背包数据
			if(playerBag.getGoodsType() ==  RewardTypeConstant.EQUIPMENT){
				playerTradeBag = getPlayerTradeBag(playerId, playerBag.getItemId(), ItemConstant.GOODS_TYPE_EQUPMENT,
						playerBag.getIsBinding(), num, price);
			}else{
				playerTradeBag = getPlayerTradeBag(playerId, playerBag.getItemId(), RewardTypeConstant.ITEM,
						playerBag.getIsBinding(), num, price);
			}
		
			bagService.updateNumByPlayerBagId(playerId, playerBag, playerBag.getNum() - num);

			// 玩家背包数据的改变
			S_SynBagItem.Builder builder1 = S_SynBagItem.newBuilder();
			// 玩家交易数据改变
			S_TradeSell.Builder builder2  = S_TradeSell.newBuilder();
			if(playerEquipment != null){
				// 改变装备状态
				playerEquipment.setState(ItemConstant.EQUIP_STATE_DELETE);
				builder1.addListPlayerEquipments(protoBuilderService.buildPlayerEquipmentMsg(playerEquipment));
				
				playerEquipment.setState(ItemConstant.EQUIP_STATE_TRADE);
				equipmentService.updatePlayerEquipment(playerEquipment);
				
				builder2.setPlayerTradeEquipment(protoBuilderService.buildPlayerTradeEquipmentMsg(playerEquipment));
			}
		
			builder1.addListPlayerBags(protoBuilderService.buildPlayerBagMsg(playerBag));			
			MessageObj msg1 = new MessageObj(MessageID.S_SynBagItem_VALUE, builder1.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg1);
			
			builder2.setPlayerTradeBag(protoBuilderService.buildPlayerTradeBagMsg(playerTradeBag));
			MessageObj msg2 = new MessageObj(MessageID.S_TradeSell_VALUE, builder2.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg2);
			
			// 寄售任务		
			serviceCollection.getTaskService().executeTask(playerId, TaskConstant.TYPE_29, null);
			
			//添加今日税收额
			serviceCollection.getGuildService().addCurRevenue(slottingFee);
		}
	}

	/**  创建或设置新的交易背包数据 */	
	private PlayerTradeBag getPlayerTradeBag(long playerId, long itemId, int goodsType, int isBinding, int num, int price) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		int TRADE_OVER_TIME = serviceCollection.getCommonService().getConfigValue(ConfigConstant.TRADE_OVER_TIME);		
		PlayerTradeBag playerTradeBag  = this.getFreeTradeBag();			
			if( playerTradeBag == null){					
				playerTradeBag = new PlayerTradeBag();
				playerTradeBag.setPlayerTradeBagId(IDUtil.geneteId(PlayerTradeBag.class));
				playerTradeBag.setGoodsType(goodsType);
				playerTradeBag.setIsBinding(isBinding);
				playerTradeBag.setItemId(itemId);
				playerTradeBag.setNum(num);
				playerTradeBag.setOverTime(System.currentTimeMillis() + TRADE_OVER_TIME);
				playerTradeBag.setPlayerId(playerId);
				playerTradeBag.setPrice(price);
				playerTradeBag.setState(ItemConstant.STATE_BACKPACK);
				playerTradeDAO.createPlayerTradeBag(playerTradeBag);
				this.getALLPlayerTradeBagMap().put(playerTradeBag.getPlayerTradeBagId(), playerTradeBag);
			}else{
				playerTradeBag.setGoodsType(goodsType);
				playerTradeBag.setIsBinding(isBinding);
				playerTradeBag.setItemId(itemId);
				playerTradeBag.setNum(num);
				playerTradeBag.setOverTime(System.currentTimeMillis() + TRADE_OVER_TIME);
				playerTradeBag.setPlayerId(playerId);
				playerTradeBag.setPrice(price);
				playerTradeBag.setState(ItemConstant.STATE_BACKPACK);
				this.updatePlayerTradeBag(playerTradeBag);
			}
			
			return playerTradeBag;
	}
	
	// 购买物品
	@Override
	public void systemItemBuy(long playerId, int itemId, int num) throws Exception{
		if (playerId < 1 || itemId < 1 || num < 1) throw new GameException(ExceptionConstant.ERROR_10000); 
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_TRADE)) {
			// 读取物品基础表		
			IBagService bagService = serviceCollection.getBagService();
			BaseItem baseItem = bagService.getBaseItemById(itemId);			
			IPlayerService playerService = serviceCollection.getPlayerService();	
			
			boolean isEquip = false;
			int totalSpendGoldNum = 0;
			if(baseItem == null){				
				isEquip = true;
				BaseEquipment baseEquipment = serviceCollection.getEquipmentService().getBaseEquipmentById(itemId);
				totalSpendGoldNum = (int)Math.ceil(baseEquipment.getBuyPrice() * num);
			}else{
				totalSpendGoldNum = (int)Math.ceil(baseItem.getBuyPrice() * num);
			}	
			
			// 判断玩家的钱是否足够
			PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);
			if (playerWealth.getGold() < totalSpendGoldNum) throw new GameException(ExceptionConstant.PLAYER_1112);
			
						
			// 改变玩家的背包信息 添加物品
			IRewardService rewardService = serviceCollection.getRewardService();
			if(isEquip){
				rewardService.fetchRewardOne(playerId, RewardTypeConstant.EQUIPMENT, itemId, num, ItemConstant.ITEM_NOT_BINDING);
			}else{
				rewardService.fetchRewardOne(playerId, RewardTypeConstant.ITEM, itemId, num, ItemConstant.ITEM_NOT_BINDING);
			}
			
			// 更新玩家财富信息		
			playerService.addGold_syn(playerId, - totalSpendGoldNum);
			
			// 任务触发					
			List<Integer> conditionList = new ArrayList<Integer>();	
			conditionList.add(itemId);
			conditionList.add(num);
			serviceCollection.getTaskService().executeTask(playerId, TaskConstant.TYPE_16, conditionList);
		}	
	}
		
	@Override
	public void tradeBuy(long playerId, long playerTradeBagId, int num) throws Exception{
		if (playerId < 1 || playerTradeBagId < 1 || num < 1) throw new GameException(ExceptionConstant.ERROR_10000); 

		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();		
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		IRewardService rewardService = serviceCollection.getRewardService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IMailService mailService = serviceCollection.getMailService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_TRADE)) {			
			IEquipmentService equipmentService = serviceCollection.getEquipmentService();	
			IPlayerService playerService = serviceCollection.getPlayerService();
			ICommonService commonService = serviceCollection.getCommonService();
			IBagService bagService = serviceCollection.getBagService();
			PlayerEquipment playerEquipment = null;			
			synchronized("tradeBuy" + playerTradeBagId){
				PlayerTradeBag playerTradeBag = this.getPlayerTradeBagById(playerTradeBagId);
				if(playerTradeBag.getNum() < num) throw new GameException(ExceptionConstant.Trade_2507);
				
				if(System.currentTimeMillis() >= playerTradeBag.getOverTime()) throw new GameException(ExceptionConstant.Trade_2507);
				
				int price = (int)Math.ceil(playerTradeBag.getPrice() * num);
				
				// 判断玩家的钱是否足够
				PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);		
				if (playerWealth.getGold() < price) throw new GameException(ExceptionConstant.PLAYER_1112);
				
				
				long sellPlayerId = playerTradeBag.getPlayerId();
				String itemName = null;
				if (playerTradeBag.getGoodsType() == ItemConstant.GOODS_TYPE_EQUPMENT){
					//验证背包是否满
					Integer itemIndex = bagService.getNewItemIndexByPlayerId(playerId);
					if(itemIndex == null){
						throw new GameException(ExceptionConstant.BAG_1304);
					}
					
					playerEquipment = equipmentService.getPlayerEquipmentByID(playerTradeBag.getPlayerId(), playerTradeBag.getItemId());
					if(playerEquipment == null) throw new GameException(ExceptionConstant.BAG_1300);	
					
					BaseEquipment baseEquipment = equipmentService.getBaseEquipmentById(playerEquipment.getEquipmentId());
					itemName = baseEquipment.getName();
					
					playerEquipment.setPlayerId(playerId);
					playerEquipment.setState(ItemConstant.EQUIP_STATE_BACKPACK);
					equipmentService.updatePlayerEquipment(playerEquipment);
					equipmentService.getPlayerEquipmentList(playerId).add(playerEquipment);
					equipmentService.getPlayerEquipmentList(playerTradeBag.getPlayerId()).remove(playerEquipment);
					
					// 买家背包信息(增)
					PlayerBag playerBag = bagService.createPlayerBag(playerId, playerTradeBag.getItemId(), playerTradeBag.getGoodsType(), playerTradeBag.getIsBinding(), num, itemIndex);
					
					// 玩家背包数据的改变
					S_SynBagItem.Builder builderBag = S_SynBagItem.newBuilder();
					builderBag.addListPlayerEquipments(protoBuilderService.buildPlayerEquipmentMsg(playerEquipment));
					builderBag.addListPlayerBags(protoBuilderService.buildPlayerBagMsg(playerBag));			
					MessageObj msgBag = new MessageObj(MessageID.S_SynBagItem_VALUE, builderBag.build().toByteArray());
					gameSocketService.sendDataToPlayerByPlayerId(playerId, msgBag);
					
				}else{					
					// 买家背包信息(增)
					rewardService.fetchRewardOne(playerId,  RewardTypeConstant.ITEM, (int)playerTradeBag.getItemId(), num, playerTradeBag.getIsBinding());
				
					BaseItem baseItem = bagService.getBaseItemById((int)playerTradeBag.getItemId());
					itemName = baseItem.getName();
				}
				
				//更新卖家财富信息				
				// playerService.addGold_syn(sellPlayerId, price);					

				int TRADE_SELLED_TAX = commonService.getConfigValue(ConfigConstant.TRADE_SELLED_TAX);
				
				// 卖出手续费
				int slottingFee = (int)Math.max(1, Math.floor(price * TRADE_SELLED_TAX * 0.01));
				int sellPrice = price - slottingFee;
				
				// 发邮件
				int[][] rewards = new int[1][4];
				int[] items1 = new int[]{RewardTypeConstant.MONEY, 0, sellPrice, 0};
				rewards[0] = items1;
				String rewardStr = SplitStringUtil.getStringByIntIntList(rewards);
				mailService.systemSendMail(sellPlayerId, ResourceUtil.getValue("trade_theme"), 
												ResourceUtil.getValue("trade_content", itemName, num, slottingFee), rewardStr, 0);
				
				// 更新买家财富信息		
				playerService.addGold_syn(playerId, - (int)price);			
				
				// 添加玩家购买的数据									
				playerTradeBag.setNum(playerTradeBag.getNum() - num);
				
				if(playerTradeBag.getNum() <= 0){
					playerTradeBag.setState(0);
				}
				// 更新卖家交易数据					
				S_TradeBuy.Builder builder = S_TradeBuy.newBuilder();
				builder.setPlayerTradeBag(protoBuilderService.buildPlayerTradeBagMsg(playerTradeBag));			
				MessageObj msg = new MessageObj(MessageID.S_TradeBuy_VALUE, builder.build().toByteArray());
				serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(sellPlayerId, msg);	
				
				// 更新交易行数据					
				S_SynTradeList.Builder builderTrade = S_SynTradeList.newBuilder();
				builderTrade.addListPlayerTradebag(protoBuilderService.buildPlayerTradeBagMsg(playerTradeBag));
				if(playerEquipment != null){
					builderTrade.addListPlayerTradeEquipment(protoBuilderService.buildPlayerTradeEquipmentMsg(playerEquipment));
				}
				
				MessageObj msgSynTradeList = new MessageObj(MessageID.S_SynTradeList_VALUE, builderTrade.build().toByteArray());
				serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId, msgSynTradeList); 
				
				if(playerTradeBag.getNum() <= 0){
					playerTradeBag.reset();
				}
				this.updatePlayerTradeBag(playerTradeBag);
				
				// 任务触发					
				List<Integer> conditionList = new ArrayList<Integer>();	
				conditionList.add((int)playerTradeBag.getItemId());
				conditionList.add(num);
				serviceCollection.getTaskService().executeTask(playerId, TaskConstant.TYPE_16, conditionList);
			}	
		}
	}

	/** 获取交易行闲置的背包格子*/
	private PlayerTradeBag getFreeTradeBag() {
		Map<Long, PlayerTradeBag> map = this.getALLPlayerTradeBagMap();	
		if (map == null) return null;
		for(Map.Entry<Long, PlayerTradeBag> entry: map.entrySet()){
			PlayerTradeBag playerTradeBag = entry.getValue();
			if(playerTradeBag.getState() == 0 && playerTradeBag.getPlayerId() == 0) return playerTradeBag;		
		}					
		
		return null;
	}	

	/** 更新玩家的某个交易背包数据*/
	private void updatePlayerTradeBag(PlayerTradeBag playerTradeBag) {
		Set<GameEntity> lists = CacheSynDBService.getFromFiveUpdateTwoCache(CacheSynConstant.PLAYER_TRADE_BAG);
		if (!lists.contains(playerTradeBag)) {
			lists.add(playerTradeBag);
		}		
	}
	
	/** 获取某个玩家的所有交易背包数据  非引用*/
	@Override
	public Map<Long, PlayerTradeBag> getPlayerTradMap(long playerId){		
		 Map<Long, PlayerTradeBag> playerTradeBagMap = this.getALLPlayerTradeBagMap();
		if(playerTradeBagMap == null) return null;	
		
		Map<Long, PlayerTradeBag> map = new ConcurrentHashMap<Long, PlayerTradeBag>();			
		for(Map.Entry<Long, PlayerTradeBag> entry: playerTradeBagMap.entrySet()){
			PlayerTradeBag playerTradeBag = entry.getValue();
			if (playerTradeBag.getPlayerId() != playerId) continue;
			map.put(playerTradeBag.getPlayerTradeBagId(), playerTradeBag);		
		}	
		
		return map;		
	}	
	
	/** 获取某个交易背包数据*/
	private PlayerTradeBag getPlayerTradeBagById(long playerTradeBagId){		
		Map<Long, PlayerTradeBag> playerTradeBagMap = this.getALLPlayerTradeBagMap();
		if(playerTradeBagMap == null) return null;
		
		return playerTradeBagMap.get(playerTradeBagId);	
	}
	
	/** 获取交易行里面所有背包数据*/
	@SuppressWarnings("unchecked")
	@Override
	public Map<Long, PlayerTradeBag> getALLPlayerTradeBagMap(){
		 Map<Long, PlayerTradeBag> map = (Map<Long, PlayerTradeBag>)CacheService.getFromCache(CacheConstant.PLAYER_TRADE_BAG);
		if(map == null){
			map = new ConcurrentHashMap<Long, PlayerTradeBag>();			
			List<PlayerTradeBag> list =	playerTradeDAO.listPlayerTradeBag();	
			
			
			for (PlayerTradeBag playerTradeBag : list){
				map.put(playerTradeBag.getPlayerTradeBagId(), playerTradeBag);
			}
			CacheService.putToCache(CacheConstant.PLAYER_TRADE_BAG, map);
		}
		
		return map;		
	}
	
	/** 根据物品类型交易行里面背包数据*/
	private Map<Long, PlayerTradeBag> getPlayerTradeBagMapByType(int type){		
		Map<Long, PlayerTradeBag> map = this.getALLPlayerTradeBagMap();
		Map<Long, PlayerTradeBag> mapType = new ConcurrentHashMap<Long, PlayerTradeBag>();
		for (Map.Entry<Long, PlayerTradeBag> entry : map.entrySet()){
			PlayerTradeBag playerTradeBag = entry.getValue();
			if(playerTradeBag.getGoodsType() != type) continue;
			mapType.put(playerTradeBag.getPlayerTradeBagId(), playerTradeBag);
		}			
		
		return mapType;		
	}
	
	/** 删除某个玩家的所有交易背包数据*/
	public void removePlayerTradeBag(long playerId){		
		Map<Long, PlayerTradeBag> playerTradeBagMap = this.getALLPlayerTradeBagMap();
		if(playerTradeBagMap == null) return;			
					
		Iterator<Map.Entry<Long, PlayerTradeBag>> iterator = playerTradeBagMap.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry<Long, PlayerTradeBag> map = iterator.next();
			PlayerTradeBag model = map.getValue();
			if (model.getPlayerId() == playerId){
				iterator.remove();
			}
		}
		try {
			playerTradeDAO.quartzDeleteTradeBag(playerId);
		} catch (Exception e) {
			LogUtil.error(e);
		}
	}

	@Override
	public void reUpShelf(long playerId, long playerTradeBagId) throws Exception {
		if(playerId < 1 || playerTradeBagId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		ICommonService commonService = serviceCollection.getCommonService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		IPlayerService playerService = serviceCollection.getPlayerService();

		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_TRADE)) {
			PlayerTradeBag playerTradeBag = this.getPlayerTradeBagById(playerTradeBagId);
		
			if(playerTradeBag == null) throw new GameException(ExceptionConstant.Trade_2512);
			
			// 上架费			
			int TRADE_SELL_TAX = commonService.getConfigValue(ConfigConstant.TRADE_SELL_TAX);
			PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);
			double slottingFee = Math.ceil(playerTradeBag.getPrice()*playerTradeBag.getNum()*TRADE_SELL_TAX*0.01) ;
			if(playerWealth.getGold() < slottingFee) throw new GameException(ExceptionConstant.PLAYER_1112);
			
			// 改变玩家金币数据(扣除手续费)			
			playerService.addGold_syn(playerId, - (int)slottingFee);
			
			int TRADE_OVER_TIME = serviceCollection.getCommonService().getConfigValue(ConfigConstant.TRADE_OVER_TIME);
			playerTradeBag.setOverTime(System.currentTimeMillis() + TRADE_OVER_TIME);
			this.updatePlayerTradeBag(playerTradeBag);
			
			S_ReUpShelf.Builder builder = S_ReUpShelf.newBuilder();
			builder.setPlayerTradeBag(protoBuilderService.buildPlayerTradeBagMsg(playerTradeBag));
			MessageObj msg = new MessageObj(MessageID.S_ReUpShelf_VALUE, builder.build().toByteArray());
			gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		}

	}
}

