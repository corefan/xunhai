package com.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.cache.CacheService;
import com.cache.CacheSynDBService;
import com.common.GameContext;
import com.common.ServiceCollection;
import com.constant.CacheConstant;
import com.constant.CacheSynConstant;
import com.constant.ConfigConstant;
import com.constant.RankConstant;
import com.dao.rank.RankDAO;
import com.domain.GameEntity;
import com.domain.MessageObj;
import com.domain.rank.BattleValueRank;
import com.domain.rank.EquipRank;
import com.domain.rank.GoldRank;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.RankProto.S_BattleValueRank;
import com.message.RankProto.S_EquipRank;
import com.message.RankProto.S_GoldRank;
import com.service.IBatchExcuteService;
import com.service.IGuildService;
import com.service.IProtoBuilderService;
import com.service.IRankService;
import com.util.LogUtil;

/**
 * 排行榜
 * @author ken
 * @date 2017-5-8
 */
public class RankService implements IRankService {

	private RankDAO rankDAO = new RankDAO();
	
	@Override
	public void initCache() {
		try {
			this.refreshRank();
		} catch (Exception e) {
			LogUtil.error("异常：", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void refreshRank() {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IBatchExcuteService batchExcuteService = serviceCollection.getBatchExcuteService();
		IGuildService guildService = serviceCollection.getGuildService();
		
		try {
			Set<GameEntity> lists = CacheSynDBService.getFromFiveUpdateOneCache(CacheSynConstant.PLAYER_PROPERTY);
			if(lists != null && !lists.isEmpty()){
				batchExcuteService.batchUpdate(lists);
			}
		} catch (Exception e) {
			LogUtil.error("异常：", e);
		}
		
		try {
			Set<GameEntity> lists = CacheSynDBService.getFromFiveUpdateOneCache(CacheSynConstant.PLAYER_WEALTH);
			if(lists != null && !lists.isEmpty()){
				batchExcuteService.batchUpdate(lists);
			}
		} catch (Exception e) {
			LogUtil.error("异常：", e);
		}
		
		try {
			Set<GameEntity> lists = CacheSynDBService.getFromFiveUpdateTwoCache(CacheSynConstant.PLAYER_EQUIPMENT);
			if(lists != null && !lists.isEmpty()){
				batchExcuteService.batchUpdate(lists);
			}
		} catch (Exception e) {
			LogUtil.error("异常：", e);
		}
		
		int totalNum = serviceCollection.getCommonService().getConfigValue(ConfigConstant.TOTAL_RANK_NUM);
		
		//战力榜
		Map<Integer, List<BattleValueRank>> battleValueRankMap = (Map<Integer, List<BattleValueRank>>)CacheService.getFromCache(CacheConstant.BATTLEVALUE_RANK_MAP);
		if(battleValueRankMap == null){
			battleValueRankMap = new ConcurrentHashMap<Integer, List<BattleValueRank>>();
			CacheService.putToCache(CacheConstant.BATTLEVALUE_RANK_MAP, battleValueRankMap);
		}
		
		for(int career = 0; career <= 3; career++){
			List<BattleValueRank> lists = rankDAO.listBattleValueRanks(career, totalNum);
			int rank = 1;
			for(BattleValueRank model : lists){
				model.setRank(rank);
				model.setGuildName(guildService.getGuildName(model.getPlayerId()));
				
				rank++;
			}
			battleValueRankMap.put(career, lists);
		}

		//装备榜
		Map<Integer, List<EquipRank>> equipRankMap = (Map<Integer, List<EquipRank>>)CacheService.getFromCache(CacheConstant.EQUIP_RANK_MAP);
		if(equipRankMap == null){
			equipRankMap = new ConcurrentHashMap<Integer, List<EquipRank>>();
			CacheService.putToCache(CacheConstant.EQUIP_RANK_MAP, equipRankMap);
		}
		
		for(int career = 0; career <= 3; career++){
			List<EquipRank> lists = rankDAO.listEquipRanks(career, totalNum);
			int rank = 1;
			for(EquipRank model : lists){
				model.setRank(rank);
				model.setGuildName(guildService.getGuildName(model.getPlayerId()));
				
				rank++;
			}
			equipRankMap.put(career, lists);
		}
		
		//财富榜
		Map<Integer, List<GoldRank>> goldRankMap = (Map<Integer, List<GoldRank>>)CacheService.getFromCache(CacheConstant.GOLD_RANK_MAP);
		if(goldRankMap == null){
			goldRankMap = new ConcurrentHashMap<Integer, List<GoldRank>>();
			CacheService.putToCache(CacheConstant.GOLD_RANK_MAP, goldRankMap);
		}
		
		for(int career = 0; career <= 3; career++){
			List<GoldRank> lists = rankDAO.listGoldRanks(career, totalNum);
			int rank = 1;
			for(GoldRank model : lists){
				model.setRank(rank);
				model.setGuildName(guildService.getGuildName(model.getPlayerId()));
				
				rank++;
			}
			goldRankMap.put(career, lists);
		}
	}
	
	@Override
	public void getRankList(long playerId, int type, int career, int start, int offset) {
		if(type == RankConstant.TYPE_RANK_1){
			
			this.getBattleValueRankList(playerId, career, start, offset);
		}else if(type == RankConstant.TYPE_RANK_2){
			
			this.getEquipRankList(playerId, career, start, offset);
		}else if(type == RankConstant.TYPE_RANK_3){
			
			this.getGoldRankList(playerId, career, start, offset);
		}
	}
	
	/**
	 * 战力榜
	 */
	@SuppressWarnings("unchecked")
	private void getBattleValueRankList(long playerId, int career, int start, int offset){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		
		Map<Integer, List<BattleValueRank>> battleValueRankMap = (Map<Integer, List<BattleValueRank>>)CacheService.getFromCache(CacheConstant.BATTLEVALUE_RANK_MAP);
		List<BattleValueRank> lists = battleValueRankMap.get(career);
		
		List<BattleValueRank> pageList = new ArrayList<BattleValueRank>();
		
		int myRank = 0;
		int myValue = 0;
		if (lists != null && lists.size() > 0) {
			for(BattleValueRank model : lists){
				if(model.getPlayerId() == playerId){
					myRank = model.getRank();
					myValue = model.getValue();
					break;
				}
			}
			
			int value = serviceCollection.getCommonService().getConfigValue(ConfigConstant.RANK_NUM);
			
			if(start <= lists.size()) {
				int fromIndex = start - 1;
				if(fromIndex < 0) fromIndex = 0;
				int toIndex = fromIndex + offset;
				if(toIndex > value) toIndex = value;
				if(toIndex > lists.size()) toIndex = lists.size();
				
				pageList = lists.subList(fromIndex, toIndex);
			}

		}
		
		S_BattleValueRank.Builder builder = S_BattleValueRank.newBuilder();
		builder.setMyRank(myRank);
		builder.setMyValue(myValue);
		for(BattleValueRank model : pageList){
			builder.addRankList(protoBuilderService.buildBattleValueRankMsg(model));
		}
		MessageObj msg = new MessageObj(MessageID.S_BattleValueRank_VALUE, builder.build().toByteArray());
		serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId,  msg);
	}
	
	
	/**
	 * 装备榜
	 */
	@SuppressWarnings("unchecked")
	private void getEquipRankList(long playerId, int career, int start, int offset){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		
		Map<Integer, List<EquipRank>> equipRankMap = (Map<Integer, List<EquipRank>>)CacheService.getFromCache(CacheConstant.EQUIP_RANK_MAP);
		List<EquipRank> lists = equipRankMap.get(career);
		
		List<EquipRank> pageList = new ArrayList<EquipRank>();
		
		int myRank = 0;
		int myValue = 0;
		if (lists != null && lists.size() > 0) {
			for(EquipRank model : lists){
				if(model.getPlayerId() == playerId){
					myRank = model.getRank();
					myValue = model.getValue();
					break;
				}
			}
			
			int value = serviceCollection.getCommonService().getConfigValue(ConfigConstant.RANK_NUM);
			
			if(start <= lists.size()) {
				int fromIndex = start - 1;
				if(fromIndex < 0) fromIndex = 0;
				int toIndex = fromIndex + offset;
				if(toIndex > value) toIndex = value;
				if(toIndex > lists.size()) toIndex = lists.size();
				
				pageList = lists.subList(fromIndex, toIndex);
			}
		}
		
		S_EquipRank.Builder builder = S_EquipRank.newBuilder();
		builder.setMyRank(myRank);
		builder.setMyValue(myValue);
		for(EquipRank model : pageList){
			builder.addRankList(protoBuilderService.buildEquipRankMsg(model));
		}
		MessageObj msg = new MessageObj(MessageID.S_EquipRank_VALUE, builder.build().toByteArray());
		serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId,  msg);
	}
	
	/**
	 * 财富榜
	 */
	@SuppressWarnings("unchecked")
	private void getGoldRankList(long playerId, int career, int start, int offset){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		
		Map<Integer, List<GoldRank>> goldRankMap = (Map<Integer, List<GoldRank>>)CacheService.getFromCache(CacheConstant.GOLD_RANK_MAP);
		List<GoldRank> lists = goldRankMap.get(career);
		
		List<GoldRank> pageList = new ArrayList<GoldRank>();
		
		int myRank = 0;
		int myValue = 0;
		if (lists != null && lists.size() > 0) {
			for(GoldRank model : lists){
				if(model.getPlayerId() == playerId){
					myRank = model.getRank();
					myValue = model.getValue();
					break;
				}
			}
			
			int value = serviceCollection.getCommonService().getConfigValue(ConfigConstant.RANK_NUM);
			
			if(start <= lists.size()) {
				int fromIndex = start - 1;
				if(fromIndex < 0) fromIndex = 0;
				int toIndex = fromIndex + offset;
				if(toIndex > value) toIndex = value;
				if(toIndex > lists.size()) toIndex = lists.size();
				
				pageList = lists.subList(fromIndex, toIndex);
			}
		}
		
		S_GoldRank.Builder builder = S_GoldRank.newBuilder();
		builder.setMyRank(myRank);
		builder.setMyValue(myValue);
		for(GoldRank model : pageList){
			builder.addRankList(protoBuilderService.buildGoldRankMsg(model));
		}
		MessageObj msg = new MessageObj(MessageID.S_GoldRank_VALUE, builder.build().toByteArray());
		serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId,  msg);
	}
}
