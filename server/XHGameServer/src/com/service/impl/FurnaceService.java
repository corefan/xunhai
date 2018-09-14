package com.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cache.BaseCacheService;
import com.cache.CacheService;
import com.cache.CacheSynDBService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.LockService;
import com.common.ServiceCollection;
import com.constant.CacheConstant;
import com.constant.CacheSynConstant;
import com.constant.ExceptionConstant;
import com.constant.LockConstant;
import com.dao.furnace.BaseFurnaceDAO;
import com.dao.furnace.PlayerFurnaceDAO;
import com.domain.GameEntity;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.furnace.BaseFurnace;
import com.domain.furnace.PlayerFurnace;
import com.message.FurnaceProto.S_UpgradeFurnace;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.IFurnaceService;
import com.service.IPropertyService;
import com.service.IProtoBuilderService;
import com.util.IDUtil;
import com.util.SplitStringUtil;

/**
 * 熔炉系统
 * @author ken
 * @date 2018年4月23日
 */
public class FurnaceService implements IFurnaceService {

	private  BaseFurnaceDAO baseFurnaceDAO = new BaseFurnaceDAO();
	private  PlayerFurnaceDAO playerFurnaceDAO = new PlayerFurnaceDAO();
	
	@Override
	public void initBaseCache() {
		Map<Integer, List<BaseFurnace>> map = new HashMap<Integer, List<BaseFurnace>>();
		List<BaseFurnace> lists = baseFurnaceDAO.listBaseFurnaces();
		for(BaseFurnace model : lists){
			model.setCurPropertyList(SplitStringUtil.getIntIntList(model.getCurProperty()));
			model.setNextPropertyList(SplitStringUtil.getIntIntList(model.getNextProperty()));
			
			List<BaseFurnace> list = map.get(model.getFurnaceId());
			if(list == null){
				list = new ArrayList<BaseFurnace>();
				map.put(model.getFurnaceId(), list);
			}
			
			list.add(model);
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_FURNACE, map);
	}

	@SuppressWarnings("unchecked")
	private List<BaseFurnace> listBaseFurnacesById(int furnaceId){
		Map<Integer, List<BaseFurnace>> map = (Map<Integer, List<BaseFurnace>>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_FURNACE);
		return map.get(furnaceId);
	}
	
	private BaseFurnace getBaseFurnace(int furnaceId, int stage, int star){
		List<BaseFurnace> lists = this.listBaseFurnacesById(furnaceId);
		for(BaseFurnace model : lists){
			if(model.getStage() == stage && model.getStar() == star){
				return model;
			}
		}
		return null;
	}
	
	@Override
	public void deleteCache(long playerId) {
		CacheService.deleteFromCache(CacheConstant.PLAYER_FURNACE+playerId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PlayerFurnace> getPlayerFurnaceList(long playerId) {
		List<PlayerFurnace> lists = (List<PlayerFurnace>)CacheService.getFromCache(CacheConstant.PLAYER_FURNACE+playerId);
		if(lists == null){
			lists = playerFurnaceDAO.listPlayerFurnaces(playerId);
			CacheService.putToCache(CacheConstant.PLAYER_FURNACE+playerId, lists);
		}
		return lists;
	}
	
	/**
	 * 同步缓存更新
	 */
	private void updatePlayerFurnace(PlayerFurnace model){
		Set<GameEntity> lists =  CacheSynDBService.getFromFiveUpdateThreeCache(CacheSynConstant.PLAYER_FURNACE);
		if (!lists.contains(model)) {
			lists.add(model);
		}
	}

	@Override
	public PlayerFurnace getPlayerFurnace(long playerId, int furnaceId) {
		List<PlayerFurnace> lists = this.getPlayerFurnaceList(playerId);
		for(PlayerFurnace model : lists){
			if(model.getFurnaceId() == furnaceId){
				return model;
			}
		}
		
		PlayerFurnace model = new PlayerFurnace();
		model.setId(IDUtil.geneteId(PlayerFurnace.class));
		model.setPlayerId(playerId);
		model.setFurnaceId(furnaceId);
		playerFurnaceDAO.createPlayerFurnace(model);
		
		lists.add(model);
		
		return model;
	}
	
	

	@Override
	public PlayerFurnace upgradeFurnace(long playerId, int furnaceId) throws Exception {
		if(furnaceId < 1 && furnaceId > 5) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPropertyService propertyService = serviceCollection.getPropertyService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_FURNACE)) {
			PlayerFurnace playerFurnace = this.getPlayerFurnace(playerId, furnaceId);
			
			BaseFurnace baseFurnace = this.getBaseFurnace(playerFurnace.getFurnaceId(), playerFurnace.getStage(), playerFurnace.getStar());
			if(playerFurnace.getPiece() < baseFurnace.getNeedPiece()){
				throw new GameException(ExceptionConstant.FURNACE_3800);
			}
			if(playerFurnace.getStage() == 0){//激活
				playerFurnace.setStage(1);
				playerFurnace.setStar(1);
				
				//添加属性
				propertyService.addProValue(playerId, baseFurnace.getNextPropertyList(), 1, true, true);
			}else{//升级
				BaseFurnace nextbaseFurnace = this.getBaseFurnace(playerFurnace.getFurnaceId(), playerFurnace.getStage(), playerFurnace.getStar() + 1);
				if(nextbaseFurnace == null){
					nextbaseFurnace = this.getBaseFurnace(playerFurnace.getFurnaceId(), playerFurnace.getStage() + 1, 1);
					if(nextbaseFurnace == null){
						throw new GameException(ExceptionConstant.FURNACE_3801);		
					}
					playerFurnace.setStage(playerFurnace.getStage() + 1);
					playerFurnace.setStar(1);
				}else{
					playerFurnace.setStar(playerFurnace.getStar() + 1);
				}	
				
				//添加属性
				Map<Integer, Integer> addProMap = new HashMap<Integer, Integer>();
				for(int i = 0; i < baseFurnace.getNextPropertyList().size(); i++){
					List<Integer> lists = baseFurnace.getNextPropertyList().get(i);
					int propertyId = lists.get(0);
					int propertyValue = lists.get(1);
					
					List<Integer> lists2 = baseFurnace.getCurPropertyList().get(i);				
					addProMap.put(propertyId, propertyValue - lists2.get(1));
				}
				propertyService.addProValue(playerId, addProMap, true, true);
			}
			playerFurnace.setPiece(playerFurnace.getPiece() - baseFurnace.getNeedPiece());
			this.updatePlayerFurnace(playerFurnace);
			
			return playerFurnace;
		}
	}

	@Override
	public void addFurnacePiece(long playerId, int furnaceId, int value){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		
		PlayerFurnace playerFurnace = this.getPlayerFurnace(playerId, furnaceId);
		playerFurnace.setPiece(playerFurnace.getPiece() + value);
		this.updatePlayerFurnace(playerFurnace);
		
		S_UpgradeFurnace.Builder builder = S_UpgradeFurnace.newBuilder();
		builder.setPlayerFurnace(protoBuilderService.buildPlayerFurnaceMsg(playerFurnace));
		MessageObj msg = new MessageObj(MessageID.S_UpgradeFurnace_VALUE, builder.build().toByteArray());
		gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
	}

}
