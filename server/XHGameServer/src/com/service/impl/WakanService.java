package com.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.cache.BaseCacheService;
import com.cache.CacheService;
import com.cache.CacheSynDBService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.LockService;
import com.common.ServiceCollection;
import com.constant.CacheConstant;
import com.constant.CacheSynConstant;
import com.constant.ChatConstant;
import com.constant.ConfigConstant;
import com.constant.ExceptionConstant;
import com.constant.LockConstant;
import com.constant.TaskConstant;
import com.constant.WakanConstant;
import com.dao.wakan.BaseWakanDao;
import com.dao.wakan.PlayerWakanDao;
import com.domain.GameEntity;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.bag.BaseItem;
import com.domain.chat.Notice;
import com.domain.player.Player;
import com.domain.player.PlayerProperty;
import com.domain.player.PlayerWealth;
import com.domain.wakan.BaseAwake;
import com.domain.wakan.BaseWakan;
import com.domain.wakan.PlayerWakan;
import com.message.ChatProto.ParamType;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.WakanProto.S_TakeWakan;
import com.message.WakanProto.WakanMsg;
import com.service.IBagService;
import com.service.IChatService;
import com.service.IEquipmentService;
import com.service.IPlayerService;
import com.service.IProtoBuilderService;
import com.service.IRewardService;
import com.service.IWakanService;
import com.util.IDUtil;
import com.util.SplitStringUtil;


/**
 * 注灵系统
 * @author ken
 * @date 2017-1-19
 */
public class WakanService implements IWakanService {	
		private PlayerWakanDao playerWakanDAO =  new PlayerWakanDao();
		private BaseWakanDao baseWakanDAO = new BaseWakanDao();
		
		@Override
		public void initBaseCache() {			
			//注灵配置表读取			
			Map<Integer, BaseWakan> baseWakanMap = new HashMap<Integer,BaseWakan>();
			List<BaseWakan> baseWakans = baseWakanDAO.listBaseWakans();
			for(BaseWakan model: baseWakans){	
				Map<Integer, List<List<Integer>>> posIdPropertyMap = new HashMap<Integer, List<List<Integer>>>();
				posIdPropertyMap.put(WakanConstant.HEAD, SplitStringUtil.getIntIntList(model.getAttHead()));
				posIdPropertyMap.put(WakanConstant.UPBODY, SplitStringUtil.getIntIntList(model.getAttClothes()));			
				posIdPropertyMap.put(WakanConstant.DOWNBODY, SplitStringUtil.getIntIntList(model.getAttTrousers()));
				posIdPropertyMap.put(WakanConstant.HAND, SplitStringUtil.getIntIntList(model.getAttCuff()));
				posIdPropertyMap.put(WakanConstant.FINGER, SplitStringUtil.getIntIntList(model.getAttRing()));
				posIdPropertyMap.put(WakanConstant.NECK, SplitStringUtil.getIntIntList(model.getAttNecklace()));
				posIdPropertyMap.put(WakanConstant.WEAPON01, SplitStringUtil.getIntIntList(model.getAttArm1()));
				posIdPropertyMap.put(WakanConstant.WEAPON02, SplitStringUtil.getIntIntList(model.getAttArm2()));	
				
				model.setTypeToAttrMap(posIdPropertyMap);			
				baseWakanMap.put(model.getLevel(), model);				
			}		
			BaseCacheService.putToBaseCache(CacheConstant.BASE_WAKAN, baseWakanMap);
			
			//觉醒配置表读取
			Map<Integer, BaseAwake> baseAwakeMap = new HashMap<Integer,BaseAwake>();
			List<BaseAwake> baseAwakes = baseWakanDAO.listBaseAwakes();
			for(BaseAwake baseAwake: baseAwakes){			
				baseAwake.setAwakenPropertyList(SplitStringUtil.getIntIntList(baseAwake.getAttAwaken()));
				
				baseAwakeMap.put(baseAwake.getNeedLevel(), baseAwake);
			}
			BaseCacheService.putToBaseCache(CacheConstant.BASE_AWAKE, baseAwakeMap);
			
			// 装备位描述
			Map<Integer, String> basePosIdMap = new HashMap<Integer,String>();
			basePosIdMap.put(WakanConstant.HEAD, "头盔之灵");
			basePosIdMap.put(WakanConstant.UPBODY, "衣服之灵");
			basePosIdMap.put(WakanConstant.DOWNBODY, "鞋子之灵");
			basePosIdMap.put(WakanConstant.HAND, "护腕之灵");
			basePosIdMap.put(WakanConstant.NECK, "项链之灵");
			basePosIdMap.put(WakanConstant.HEAD, "头盔之灵");
			basePosIdMap.put(WakanConstant.FINGER, "戒指之灵");
			basePosIdMap.put(WakanConstant.WEAPON01, "武器之灵");
			basePosIdMap.put(WakanConstant.WEAPON02, "法宝之灵");
			BaseCacheService.putToBaseCache(CacheConstant.BASE_POS_ID, basePosIdMap);
		}	
	
		@SuppressWarnings("unchecked")
		@Override
		public Map<Integer, PlayerWakan> getPlayerWakanMap(long playerId){			
			Map<Integer, PlayerWakan> map = (Map<Integer, PlayerWakan>) CacheService.getFromCache(CacheConstant.PLAYER_WAKAN + playerId);
			if(map == null){
				map = new ConcurrentHashMap<Integer, PlayerWakan>();				
				List<PlayerWakan> lists= playerWakanDAO.listPlayerWakans(playerId);{
					for(PlayerWakan model: lists){
						map.put(model.getPosId(), model);
					}
				}
				CacheService.putToCache(CacheConstant.PLAYER_WAKAN+playerId, map);
			}
			return map;
		}
		
		/**
		 * 同步缓存更新
		 */	
		private void updatePlayerWakan(PlayerWakan playerWakan) {
			Set<GameEntity> lists = CacheSynDBService.getFromFiveUpdateTwoCache(CacheSynConstant.PLAYER_WAKAN);
			if (!lists.contains(playerWakan)) {
				lists.add(playerWakan);
			}
		}		
	
		@Override
		public void deleteCache(long playerId) {
			CacheService.deleteFromCache(CacheConstant.PLAYER_WAKAN+playerId);
		}

		@SuppressWarnings("unchecked")
		@Override
		public void takeWakan(long playerId, int posId, List<Integer> itemList) throws Exception {	
			if(playerId < 1 || posId < 1 || itemList == null || itemList.isEmpty()) throw new GameException(ExceptionConstant.ERROR_10000);			
			
			ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();		
			GameSocketService gameSocketService = serviceCollection.getGameSocketService();
			IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
			
			synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_WAKAN)) {				
				// 获取玩家装备位注灵信息
				Map<Integer, PlayerWakan> map = this.getPlayerWakanMap(playerId);
				PlayerWakan playerWakan = map.get(posId);
				if(playerWakan == null){
					playerWakan = new PlayerWakan();
					playerWakan.setId(IDUtil.geneteId(PlayerWakan.class));
					playerWakan.setPosId(posId);
					playerWakan.setPlayerId(playerId);
					this.playerWakanDAO.createPlayerWakan(playerWakan);
					map.put(posId, playerWakan);
				}
				
				// 根据灵石的灵力值，判断注灵等级是否升级
				int TAKE_WAKAN_LIMIT = serviceCollection.getCommonService().getConfigValue(ConfigConstant.TAKE_WAKAN_LIMIT);
				if (playerWakan.getWakanLevel() >= TAKE_WAKAN_LIMIT) throw new GameException(ExceptionConstant.WAKAN_2201);
				
				IPlayerService playerService = 	serviceCollection.getPlayerService();
				PlayerWealth playerWealth = playerService.getPlayerWealthById(playerId);
				PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);
				
				BaseWakan baseWakan = this.getBaseWakan(playerWakan.getPosId(), playerWakan.getWakanLevel() + 1);				
				if(playerWealth.getGold() < baseWakan.getExpGold())throw new GameException(ExceptionConstant.PLAYER_1112);	
				if(playerProperty.getLevel() < baseWakan.getNeedLevel()) throw new GameException(ExceptionConstant.PLAYER_1110);	
				
				IRewardService rewardService = serviceCollection.getRewardService();
				IBagService bagService = serviceCollection.getBagService();					
				List<List<Integer>> items = new ArrayList<List<Integer>>();
				int addValue = 0;
				for(Integer itemId : itemList){					
					List<Integer> list = new ArrayList<Integer>();
					list.add(itemId);
					list.add(1);
					items.add(list);	
					
					// 获取物品信息
					BaseItem baseItem = bagService.getBaseItemById(itemId);
					addValue = addValue + baseItem.getEffectValue();	
				}
				
				// 扣除道具
				rewardService.deductItemList(playerId, items);								
				playerService.addGold_syn(playerId, - baseWakan.getExpGold());					
				
				// 判断是否触发暴击 (相加总灵值/当前等级需要灵值）*（1+已有灵值/当前等级需要灵值）
				double chcValue = (addValue * 1.0 / baseWakan.getNeedMana()) * (1 + playerWakan.getWakanValue() * 1.0/ baseWakan.getNeedMana()); 
				boolean levelFlag = false;	
				int isCrit = 0;
				int newValue = playerWakan.getWakanValue() + addValue;			
				if(Math.random() < chcValue){					
					levelFlag = true;
					isCrit = 1;
				}else{					
					// 根据玩家当前的等级获取升级需要的灵力值， 判断是否能升级				
					if (newValue >= baseWakan.getNeedMana()){
						// 注灵升级
						levelFlag = true;								
					}				
				}	
				
				if(levelFlag){	
					playerWakan.setWakanLevel(playerWakan.getWakanLevel() + 1);	
					playerWakan.setWakanValue(0);
					
					// 根据玩家装备判断玩家装备位是否穿戴装备
					IEquipmentService equipmentService = serviceCollection.getEquipmentService();
					boolean isTake = equipmentService.isTakeEquipment(playerWakan.getPlayerId(), playerWakan.getPosId());
					if(isTake){
						//根据装备位 和 装备位注灵等级 获取注灵改变的属性列表
						List<List<Integer>> attrList = this.getWakanAttr(playerWakan.getPosId(), playerWakan.getWakanLevel());				
						List<List<Integer>> lastAttrList = this.getWakanAttr(playerWakan.getPosId(), playerWakan.getWakanLevel() - 1);
						 
						this.addProperty(playerWakan.getPlayerId(), attrList, lastAttrList);
					}						
					
					// 达到18级, 广播
					int TAKE_WAKAN_NOTICE_LEVEL = serviceCollection.getCommonService().getConfigValue(ConfigConstant.TAKE_WAKAN_NOTICE_LEVEL);
					if(playerWakan.getWakanLevel() >= TAKE_WAKAN_NOTICE_LEVEL){			
						IChatService chatService = serviceCollection.getChatService();
						Player player = serviceCollection.getPlayerService().getPlayerByID(playerWakan.getPlayerId());
						Map<Integer, String> basePosIdMap = (Map<Integer, String>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_POS_ID);
						List<Notice> paramList = new ArrayList<Notice>();						
						Notice notice1 = new Notice(ParamType.PLAYER, playerWakan.getPlayerId(), 0, player.getPlayerName());
						Notice notice2 = new Notice(ParamType.PARAM, 0, 0, basePosIdMap.get(playerWakan.getPosId()));
						Notice notice3 = new Notice(ParamType.PARAM, 0, 0, playerWakan.getWakanLevel()+"");
						
						paramList.add(notice1);
						paramList.add(notice2);
						paramList.add(notice3);
						chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_23, paramList, gameSocketService.getOnLinePlayerIDList());
					}
					
					this.isAwake(playerWakan);			
					
				}else{
					playerWakan.setWakanValue(newValue);
				}
				
				this.updatePlayerWakan(playerWakan);
				
				// 任务触发
				List<Integer> conditionList = new ArrayList<Integer>();				
				conditionList.add(playerWakan.getPosId());
				conditionList.add(playerWakan.getWakanLevel());
				GameContext.getInstance().getServiceCollection().getTaskService().executeTask(playerWakan.getPlayerId(), TaskConstant.TYPE_6, conditionList);
	
				S_TakeWakan.Builder builder = S_TakeWakan.newBuilder();			
				WakanMsg.Builder wakanMsg = protoBuilderService.buildWakanMsg(playerWakan);	
				builder.setIsCrit(isCrit);
				builder.setWakanMsg(wakanMsg);		
				MessageObj msg = new MessageObj(MessageID.S_TakeWakan_VALUE, builder.build().toByteArray());
				gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);	
			}
		}
		
		/**
		 * 根据装备位和注灵等级获取配置数据
		 */		
		private List<List<Integer>> getWakanAttr(int posId, int level){			
			BaseWakan baseWakan = this.getBaseWakan(posId, level);
			Map<Integer, List<List<Integer>>> propertyMap = baseWakan.getTypeToAttrMap();
			return propertyMap.get(posId);
		}
		
		/**
		 * 根据装备位和注灵等级获取配置数据
		 */		
		@SuppressWarnings("unchecked")
		@Override
		public BaseWakan getBaseWakan(int posId, int level){
			Map<Integer, BaseWakan> baseWakanMap = (Map<Integer, BaseWakan>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_WAKAN);
				
			return baseWakanMap.get(level);
		}
		
		/**
		 * 根据装备位获取觉醒配置数据
		 */		
		@SuppressWarnings("unchecked")
		private BaseAwake getAwake(int level){
			Map<Integer, BaseAwake> baseAwakeMap = (Map<Integer, BaseAwake>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_AWAKE);
					
			return  baseAwakeMap.get(level);
		}			
		
		// 判断是否达成觉醒, 如达成觉醒则添加玩家属性值
		private void isAwake(PlayerWakan playerWakan) throws Exception{
			
			//获取玩家所有装备位等级
			Map<Integer, PlayerWakan> map = this.getPlayerWakanMap(playerWakan.getPlayerId());
			
			//判断玩家其他装备位等级是否大于玩家当前升级的装备位等级
			for(int i = 1; i < 9; i++){
				if (map.get(i) == null) return;
				if (map.get(i).getWakanLevel() < playerWakan.getWakanLevel()) return;
			}
			
			//if(playerWakan.getWakanLevel() % 3 != 0) return;
			
			//根据玩家当前装备位等级获取配置觉醒配置信息
			BaseAwake baseAwake = this.getAwake(playerWakan.getWakanLevel());
			if(baseAwake == null) return;
						
			//改变玩家属性值
			ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
			serviceCollection.getPropertyService().addProValue(playerWakan.getPlayerId(), baseAwake.getAwakenPropertyList(), 1, true, true);
			
			// 触发任务
			List<Integer> conditionList = new ArrayList<Integer>();
			conditionList.add(baseAwake.getId());		
			GameContext.getInstance().getServiceCollection().getTaskService().executeTask(playerWakan.getPlayerId(), TaskConstant.TYPE_33, conditionList);
						
			// 达到4段, 广播
			if(baseAwake.getId() > 3){			
				IChatService chatService = serviceCollection.getChatService();
				Player player = serviceCollection.getPlayerService().getPlayerByID(playerWakan.getPlayerId());
				List<Notice> paramList = new ArrayList<Notice>();
				
				Notice notice1 = new Notice(ParamType.PLAYER, playerWakan.getPlayerId(), 0, player.getPlayerName());
				Notice notice2 = new Notice(ParamType.PARAM, 0, 0, baseAwake.getId()+"");
				
				paramList.add(notice1);
				paramList.add(notice2);
				
				
				GameSocketService gameSocketService = serviceCollection.getGameSocketService();
				chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_3, paramList, gameSocketService.getOnLinePlayerIDList());
			}
		}	

		/** 获取玩家某装备位上的注灵信息 */	
		@Override
		public PlayerWakan getPlayerWakanByPosId(long playerId, int PosId){
			Map<Integer, PlayerWakan> playerWakanMap = this.getPlayerWakanMap(playerId);	
			
			return playerWakanMap.get(PosId);
		}

		@Override
		public void changeProValueByEquipment(long playerId, int posId, int sign){			
			PlayerWakan playerWaken = this.getPlayerWakanByPosId(playerId, posId);
			if(playerWaken != null){				
				List<List<Integer>> attrList = this.getWakanAttr(posId, playerWaken.getWakanLevel());
				GameContext.getInstance().getServiceCollection().getPropertyService().addProValue(playerId, attrList, sign, true, false);
			}
		}	

		/**
		 * 计算需要增加的属性值
		 * attrList - lastAttrList
		 */
		private void addProperty(long playerId, List<List<Integer>> attrList, List<List<Integer>> lastAttrList){
			if (attrList == null) return;
			Map<Integer, Integer> addProMap = new HashMap<Integer, Integer>();				
			if (lastAttrList != null){
				for(int i = 0; i < attrList.size(); i++){
					List<Integer> lists = attrList.get(i);
					int propertyId = lists.get(0);
					int propertyValue = lists.get(1);
					
					List<Integer> lists2 = lastAttrList.get(i);				
					addProMap.put(propertyId, propertyValue - lists2.get(1));
				}	
			}else{
				for(int i = 0; i<attrList.size(); i++){
					addProMap.put(attrList.get(i).get(0), attrList.get(i).get(1));
				}
			}	
			
			//改变玩家属性值
			ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();			
			serviceCollection.getPropertyService().addProValue(playerId, addProMap, true, true);	
		}
		
		/**获取玩家觉醒数据**/
		@SuppressWarnings("unchecked")
		public BaseAwake getBaseAwake(long playerId){
			//获取玩家所有装备位等级
			Map<Integer, PlayerWakan> map = this.getPlayerWakanMap(playerId);
			//判断玩家装备位是否注灵
			for(int i = 1; i < 9; i++){
				if (map.get(i) == null) return null;
			}
			
			BaseAwake target = null;
			Map<Integer, BaseAwake> baseAwakeMap = (Map<Integer, BaseAwake>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_AWAKE);
			for(Map.Entry<Integer, BaseAwake> entry : baseAwakeMap.entrySet()){
				BaseAwake mode = entry.getValue();
				boolean find = true;
				//判断玩家装备位等级是否大于觉醒需要的等级
				for(int i = 1; i < 9; i++){
					if (map.get(i).getWakanLevel() < mode.getNeedLevel()){
						find = false;
						break;
					}
				}
				if(find){
					if(target == null || target.getNeedLevel() < mode.getNeedLevel()){
						target = mode;
					}
				}
			}
			return target;
		}
	}


