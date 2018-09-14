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
import com.constant.ChatConstant;
import com.constant.ExceptionConstant;
import com.constant.LockConstant;
import com.constant.ProdefineConstant;
import com.dao.fashion.BaseFashionDAO;
import com.dao.fashion.PlayerFashionDAO;
import com.domain.GameEntity;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.base.BaseNewRole;
import com.domain.chat.Notice;
import com.domain.fashion.BaseFashion;
import com.domain.fashion.PlayerFashion;
import com.domain.player.Player;
import com.domain.puppet.PlayerPuppet;
import com.message.ChatProto.ParamType;
import com.message.FashionProto.S_AddFashion;
import com.message.MessageProto.MessageEnum.MessageID;
import com.service.IBuffService;
import com.service.IChatService;
import com.service.ICommonService;
import com.service.IFashionService;
import com.service.IPlayerService;
import com.service.IPropertyService;
import com.util.IDUtil;
import com.util.SplitStringUtil;

/**
 * 时装系统
 * @author ken
 * @date 2017-2-13
 */
public class FashionService implements IFashionService {

	private BaseFashionDAO baseFashiionDAO = new BaseFashionDAO();
	private PlayerFashionDAO playerFashionDAO = new PlayerFashionDAO();
	
	@Override
	public void initBaseCache() {

		Map<Integer, BaseFashion> fashionMap = new HashMap<Integer, BaseFashion>();
		List<BaseFashion> fashions = baseFashiionDAO.listBaseFashions();
		
		for(BaseFashion model : fashions){
			model.setBasePropertyList(SplitStringUtil.getIntIntList(model.getBaseProperty()));
			model.setBuffIdList(SplitStringUtil.getIntList(model.getBuffId()));
			fashionMap.put(model.getFashionId(), model);
		}
		
		BaseCacheService.putToBaseCache(CacheConstant.BASE_FASHION, fashionMap);
	}

	/**
	 * 取时装配置
	 */
	@SuppressWarnings("unchecked")
	private BaseFashion getBaseFashion(int fashionId){
		Map<Integer, BaseFashion> fashionMap = (Map<Integer, BaseFashion>) BaseCacheService.getFromBaseCache(CacheConstant.BASE_FASHION);
		return fashionMap.get(fashionId);
	}

	@SuppressWarnings("unchecked")
	public List<PlayerFashion> getFashionList(long playerId) {
		List<PlayerFashion> lists = (List<PlayerFashion>)CacheService.getFromCache(CacheConstant.PLAYER_FASHION+playerId);
		if(lists == null){
			lists = playerFashionDAO.listPlayerFashions(playerId);
			CacheService.putToCache(CacheConstant.PLAYER_FASHION+playerId, lists);
		}
		return lists;
	}
	
	/**
	 * 取时装记录
	 */
	public PlayerFashion getPlayerFashion(long playerId, int fashionId){
		 List<PlayerFashion> lists = this.getFashionList(playerId);
		 for(PlayerFashion model : lists){
			 if(model.getFashionId() == fashionId) return model;
		 }
		 return null;
	}
	
	/**
	 * 同步缓存更新
	 */
	private void updatePlayerFashion(PlayerFashion playerFashion){
		Set<GameEntity> lists =  CacheSynDBService.getFromFiveUpdateThreeCache(CacheSynConstant.PLAYER_FASHION);
		if (!lists.contains(playerFashion)) {
			lists.add(playerFashion);
		}
	}
	
	@Override
	public void addFashion_syn(long playerId, int fashionId) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		BaseFashion baseFashion = this.getBaseFashion(fashionId);
		if(baseFashion == null){				
			 throw new GameException(ExceptionConstant.FASHION_1700);
		}
		
		PlayerFashion model = this.getPlayerFashion(playerId, fashionId);
		if(model != null){				
			 throw new GameException(ExceptionConstant.FASHION_1701);
		}		
		
		Player player = playerService.getPlayerByID(playerId);
		if(player.getCareer() != baseFashion.getCareer()) throw new GameException(ExceptionConstant.CREATE_1101);
					
		model = new PlayerFashion();
		model.setId(IDUtil.geneteId(PlayerFashion.class));
		model.setPlayerId(playerId);			
		model.setFashionId(fashionId);
		model.setOwnDate(System.currentTimeMillis());
		model.setDressFlag(0);
		playerFashionDAO.createPlayerFashion(model);
		this.getFashionList(playerId).add(model);
		
		S_AddFashion.Builder builder = S_AddFashion.newBuilder();			
		builder.setFashionId(fashionId);	
		MessageObj msg = new MessageObj(MessageID.S_AddFashion_VALUE, builder.build().toByteArray());
		gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
		
		//时装公告
		IChatService chatService = serviceCollection.getChatService();
		List<Notice> paramList = new ArrayList<Notice>();
		Notice notice1 = new Notice(ParamType.PLAYER, playerId, 0, player.getPlayerName());
		Notice notice2 = new Notice(ParamType.FASHION, baseFashion.getFashionId(), 0, "");
		
		paramList.add(notice1);
		paramList.add(notice2);
		
		chatService.synNotice(ChatConstant.CHAT_NOTICE_MAG_20, paramList, gameSocketService.getOnLinePlayerIDList());
	}
	

	@Override
	public void putonFashion(long playerId, int fashionId) throws Exception {
		if(playerId < 1 || fashionId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPropertyService propertyService = serviceCollection.getPropertyService();
		IPlayerService playerService = serviceCollection.getPlayerService();
				
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_FASHION)) {
			
			PlayerFashion model = this.getPlayerFashion(playerId, fashionId);
			if(model == null){			
				 throw new GameException(ExceptionConstant.FASHION_1702);
			}
			
			BaseFashion baseFashion = this.getBaseFashion(fashionId);
			if(baseFashion == null){				
				 throw new GameException(ExceptionConstant.FASHION_1700);
			}
			
			if(model.getDressFlag() == 1){
				 throw new GameException(ExceptionConstant.FASHION_1703);
			}
			
			List<PlayerFashion> lists = this.getFashionList(playerId);			
			for(PlayerFashion pf : lists){
				if(pf.getDressFlag() == 1 && pf.getFashionId() != fashionId){
					pf.setDressFlag(0);
					this.updatePlayerFashion(pf);
					
					//取掉属性
					BaseFashion basePf = this.getBaseFashion(pf.getFashionId());
					propertyService.addProValue(playerId, basePf.getBasePropertyList(), -1, false, false);
					break;
				}
			}
			
			model.setDressFlag(1);
			this.updatePlayerFashion(model);
			//添加属性
			propertyService.addProValue(playerId, baseFashion.getBasePropertyList(), 1, true, true);
		
			//更换外形			
			PlayerPuppet playerPuppet = GameContext.getInstance().getServiceCollection().getSceneService().getPlayerPuppet(playerId);
			if(playerPuppet != null){
				playerPuppet.setDressStyle(baseFashion.getDressStyle());
				playerService.synPlayerPropertyToAll(playerPuppet, ProdefineConstant.DRESS_STYLE, baseFashion.getDressStyle());
			}

			//同步队伍信息
			serviceCollection.getTeamService().synTeam(playerId);
			
			IBuffService buffService = GameContext.getInstance().getServiceCollection().getBuffService();
			if(baseFashion.getBuffIdList() != null){
				for(Integer buffId : baseFashion.getBuffIdList()){
					buffService.addBuffById(playerId, buffId);
				}
			}		
		}
	}


	@Override
	public void putdownFashion(long playerId, int fashionId) throws Exception {
		if(playerId < 1 || fashionId < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPropertyService propertyService = serviceCollection.getPropertyService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_FASHION)) {
			
			PlayerFashion model = this.getPlayerFashion(playerId, fashionId);
			if(model == null){
				throw new GameException(ExceptionConstant.FASHION_1702);
			}
			
			BaseFashion baseFashion = this.getBaseFashion(fashionId);
			if(baseFashion == null) {
				throw new GameException(ExceptionConstant.FASHION_1700);
			}
			
			if(model.getDressFlag() == 0) {
				 throw new GameException(ExceptionConstant.FASHION_1704);
			}
			
			model.setDressFlag(0);
			this.updatePlayerFashion(model);
			//取掉属性
			propertyService.addProValue(playerId, baseFashion.getBasePropertyList(), -1, true, true);
			
			IPlayerService playerService = GameContext.getInstance().getServiceCollection().getPlayerService();
			ICommonService commonService = GameContext.getInstance().getServiceCollection().getCommonService();
			Player player = playerService.getPlayerByID(playerId);
			PlayerPuppet playerPuppet = GameContext.getInstance().getServiceCollection().getSceneService().getPlayerPuppet(playerId);
			if(playerPuppet != null){
				if(baseFashion.getDressStyle() > 0){				
					BaseNewRole baseNewRole = commonService.getBaseNewRole(player.getCareer());				
					playerPuppet.setDressStyle(baseNewRole.getDressStyle());
					playerService.synPlayerPropertyToAll(playerPuppet, ProdefineConstant.DRESS_STYLE, baseNewRole.getDressStyle());
				}		
				
				IBuffService buffService = GameContext.getInstance().getServiceCollection().getBuffService();
				if(baseFashion.getBuffIdList() != null){
					for(Integer buffId : baseFashion.getBuffIdList()){					
						buffService.removeBuffById(playerPuppet, buffId);
					}
				}
			}

			//同步队伍信息
			serviceCollection.getTeamService().synTeam(playerId);
		}
	}


	@Override
	public void deleteCache(long playerId) {
		CacheService.deleteFromCache(CacheConstant.PLAYER_FASHION+playerId);
	}
	

	@Override
	public void dealLogin(long playerId) {
		IBuffService buffService = GameContext.getInstance().getServiceCollection().getBuffService();
		
		// 检测时装是否穿戴
		int fashionID = 0;
		List<PlayerFashion> lists = this.getFashionList(playerId);			
		for(PlayerFashion pf : lists){
			if(pf.getDressFlag() == 1){					
				fashionID = pf.getFashionId();
				break;				
			}
		}
		
		if(fashionID > 0){
			BaseFashion baseFashion = this.getBaseFashion(fashionID);		
			if(baseFashion.getBuffIdList() != null){
				for(Integer buffId : baseFashion.getBuffIdList()){
					buffService.addBuffById(playerId, buffId);
				}
			}		
		}		
	}

}
