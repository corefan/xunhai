package com.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import com.common.RandomService;
import com.common.ServiceCollection;
import com.constant.ActivityConstant;
import com.constant.CacheConstant;
import com.constant.CacheSynConstant;
import com.constant.ExceptionConstant;
import com.constant.ItemConstant;
import com.constant.LockConstant;
import com.constant.ProdefineConstant;
import com.constant.SceneConstant;
import com.constant.TaskConstant;
import com.constant.VipConstant;
import com.dao.tianti.BaseTiantiDAO;
import com.dao.tianti.PlayerTiantiDAO;
import com.domain.GameEntity;
import com.domain.GameException;
import com.domain.MessageObj;
import com.domain.Reward;
import com.domain.bag.BaseItem;
import com.domain.battle.DropItemInfo;
import com.domain.map.BaseMap;
import com.domain.player.Player;
import com.domain.player.PlayerDaily;
import com.domain.player.PlayerExt;
import com.domain.player.PlayerProperty;
import com.domain.puppet.PlayerPuppet;
import com.domain.tianti.BaseDropItem;
import com.domain.tianti.BaseTiantiDate;
import com.domain.tianti.BaseTiantiPKReward;
import com.domain.tianti.BaseTiantiReward;
import com.domain.tianti.BaseTiantiScore;
import com.domain.tianti.PlayerTianti;
import com.domain.weekactivity.BaseWeekActivity;
import com.message.MessageProto.MessageEnum.MessageID;
import com.message.TiantiProto.RankMsg;
import com.message.TiantiProto.S_CancelMatch;
import com.message.TiantiProto.S_GetRankPageList;
import com.message.TiantiProto.S_GetTianti;
import com.message.TiantiProto.S_SynEndTiantiPkPlayer;
import com.message.TiantiProto.S_SynLoadPkPlayer;
import com.message.TiantiProto.S_UseTiantiItem;
import com.scene.SceneModel;
import com.service.IBagService;
import com.service.IBattleService;
import com.service.IBuffService;
import com.service.IMailService;
import com.service.IPlayerService;
import com.service.IProtoBuilderService;
import com.service.IRewardService;
import com.service.ISceneService;
import com.service.ITaskService;
import com.service.ITiantiService;
import com.service.IVipService;
import com.service.IWeekActivityService;
import com.util.LogUtil;
import com.util.PlayerUtil;
import com.util.ResourceUtil;
import com.util.SplitStringUtil;

/**
 * 天梯系统
 * @author ken
 * @date 2017-4-14
 */
public class TiantiService implements ITiantiService {

	private BaseTiantiDAO baseTiantiDAO = new BaseTiantiDAO();
	private PlayerTiantiDAO playerTiantiDAO = new PlayerTiantiDAO();
	
	@Override
	public void initBaseCache() {
		List<BaseTiantiScore> scores = baseTiantiDAO.listBaseTiantiScores();
		for(BaseTiantiScore model : scores){
			model.setStageRewardList(SplitStringUtil.getIntIntList(model.getStageReward()));
			model.setRankRewardList(SplitStringUtil.getIntIntList(model.getRankReward()));
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_TIANTI_SCORE, scores);
		
		List<BaseTiantiDate> baseTiantiDates = baseTiantiDAO.getBaseTiantiDate();
		for(BaseTiantiDate model : baseTiantiDates){
			model.setStartTime(DateService.getDateString(model.getStartDate()));
			model.setEndTime(DateService.getDateString(model.getEndDate()));
		}

		BaseCacheService.putToBaseCache(CacheConstant.BASE_TIANTI_DATE_LIST, baseTiantiDates);
		
		List<BaseTiantiReward> rewards = baseTiantiDAO.listBaseTiantiRewards();
		for(BaseTiantiReward model : rewards){
			model.setRewardList(SplitStringUtil.getIntIntList(model.getReward()));
		}
		BaseCacheService.putToBaseCache(CacheConstant.BASE_TIANTI_REWARD, rewards);
		
		Map<Integer, BaseTiantiPKReward> pkRewardMap = new HashMap<>();
		List<BaseTiantiPKReward> pkRewards = baseTiantiDAO.listBaseTiantiPKRewards();
		for(BaseTiantiPKReward model : pkRewards){
			model.setInitItemList(SplitStringUtil.getRewardInfo(model.getInitItem()));
			
			List<List<Integer>> failLists = SplitStringUtil.getIntIntList(model.getFailReward());
			for(List<Integer> fr : failLists){
				int groupId = fr.get(0);
				Reward reward = new Reward(fr.get(1), fr.get(2), fr.get(3), fr.get(5), fr.get(4));
				
				List<Reward> frList = model.getFailRewardMap().get(groupId);
				if(frList == null){
					frList = new ArrayList<>();
				}
				frList.add(reward);
				
				model.getFailRewardMap().put(groupId, frList);
			}
			
			List<List<Integer>> winLists = SplitStringUtil.getIntIntList(model.getWinReward());
			for(List<Integer> wr : winLists){
				int groupId = wr.get(0);
				Reward reward = new Reward(wr.get(1), wr.get(2), wr.get(3), wr.get(5), wr.get(4));
				
				List<Reward> wrList = model.getWinRewardMap().get(groupId);
				
				if(wrList == null){
					wrList = new ArrayList<>();
				}
				wrList.add(reward);
				
				model.getWinRewardMap().put(groupId, wrList);
			}	
			
			pkRewardMap.put(model.getId(), model);
		}
		
		BaseCacheService.putToBaseCache(CacheConstant.BASE_TIANTI_PK_REWARD, pkRewardMap);
	}
	
	@Override
	public void initCache() {
		try {
			this.refreshRank();
		} catch (Exception e) {
			LogUtil.error("初始噬魂殿排行榜异常：", e);
		}
		
		CacheService.putToCache(CacheConstant.PLAYER_TIANTI_MATCH, new ConcurrentHashMap<Long, PlayerTianti>());
	}

	/**
	 * 根据积分取配置
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BaseTiantiScore getBaseTiantiScore(int score){
		List<BaseTiantiScore> lists = (List<BaseTiantiScore>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_TIANTI_SCORE);
		for(BaseTiantiScore model : lists){
			if(score >= model.getMinScore() && score <= model.getMaxScore()){
				return model;
			}
		}
		return null;
	}	
	
	/**
	 * 根据段位取奖励配置
	 */
	@SuppressWarnings("unchecked")
	private BaseTiantiScore getBaseTiantiScoreByStage(int stage){
		List<BaseTiantiScore> lists = (List<BaseTiantiScore>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_TIANTI_SCORE);
		for(BaseTiantiScore model : lists){
			if(model.getStage() == stage && model.getStar() == 0){
				return model;
			}
		}
		return null;
	}

	/**
	 * 赛季日期配置
	 */
	@SuppressWarnings("unchecked")
	private List<BaseTiantiDate> getBaseTiantiDates(){
		return (List<BaseTiantiDate>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_TIANTI_DATE_LIST);
	}	
	
	/**
	 * pk奖励配置
	 */
	@SuppressWarnings("unchecked")
	private BaseTiantiPKReward getBaseTiantiPKReward(int level){
		Map<Integer, BaseTiantiPKReward> pkRewardMap = (Map<Integer, BaseTiantiPKReward>)BaseCacheService.getFromBaseCache(CacheConstant.BASE_TIANTI_PK_REWARD);
		 
		return pkRewardMap.get(level);
	}
	
	/**
	 * 取排名奖励
	 */
	@SuppressWarnings("unchecked")
	private BaseTiantiReward getBaseTiantiReward(int rank){
		List<BaseTiantiReward> rewards = (List<BaseTiantiReward>) BaseCacheService.getFromBaseCache(CacheConstant.BASE_TIANTI_REWARD);
		for(BaseTiantiReward model : rewards){
			if(rank >= model.getMinRank() && rank <= model.getMaxRank()){
				return model;
			}
		}
		return null;
	}	
	
	@Override
	public void deleteCache(long playerId) {
		CacheService.deleteFromCache(CacheConstant.PLAYER_TIANTI + playerId);
	}

	/** 
	 * 同步缓存
	 */
	@Override
	public void updatePlayerTianti(PlayerTianti playerTianti){
		Set<GameEntity> lists = CacheSynDBService.getFromFiveUpdateThreeCache(CacheSynConstant.PLAYER_TIANTI);
		if (!lists.contains(playerTianti)) {
			lists.add(playerTianti);
		}
	}

	@Override
	public PlayerTianti getPlayerTianti(long playerId) {
		PlayerTianti playerTianti = (PlayerTianti) CacheService
				.getFromCache(CacheConstant.PLAYER_TIANTI + playerId);
		if (playerTianti == null) {
			playerTianti = playerTiantiDAO.getPlayerTianti(playerId);
			if (playerTianti != null) {
				
				CacheService.putToCache(CacheConstant.PLAYER_TIANTI + playerId, playerTianti);
			}
		}
		return playerTianti;
	}

	@Override
	public void getTiantiPanelData(long playerId) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		PlayerTianti playerTianti = this.getPlayerTianti(playerId);
		if(playerTianti == null){
			System.out.println("玩家天梯数据为null playerId :" + playerId);
			return;
		}
		
		PlayerDaily playerDaily = playerService.getPlayerDailyById(playerId);
		S_GetTianti.Builder builder = S_GetTianti.newBuilder();
		builder.setStage(playerTianti.getStage());
		builder.setStar(playerTianti.getStar());
		builder.setKillNum(playerTianti.getKillNum());
		builder.setDeadNum(playerTianti.getDeadNum());
		builder.addAllStages(playerTianti.getRewardStageList());
		builder.setTiantiNum(playerDaily.getPkRewardNum());
		builder.setScore(playerTianti.getScore());
		
		long curTime = System.currentTimeMillis();
		BaseTiantiDate baseDate = null;
		for(BaseTiantiDate model : this.getBaseTiantiDates()){
			if(curTime <= model.getEndTime().getTime()){
				baseDate = model;
				break;
			}
		}
		
		if(baseDate == null){
			LogUtil.error("没有下一赛季的配置");
		}else{
			builder.setStartTime(baseDate.getStartTime().getTime());
			builder.setEndTime(baseDate.getEndTime().getTime());
		}

		MessageObj msg = new MessageObj(MessageID.S_GetTianti_VALUE, builder.build().toByteArray());
		serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId,  msg);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void getRankPageList(long playerId, int start, int offset) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		
		int myRank = 0;
		int totalNum = 0;
		List<PlayerTianti> pageList = new ArrayList<PlayerTianti>();
		List<PlayerTianti> lists = (List<PlayerTianti>)CacheService.getFromCache(CacheConstant.TIANTI_RANK);
		if(lists != null){
			totalNum = lists.size();
			
			for(PlayerTianti model : lists){
				if(model.getPlayerId() == playerId){
					myRank = model.getRank();
					break;
				}
			}
			
			if(start <= lists.size()) {
				int fromIndex = start - 1;
				if(fromIndex < 0) fromIndex = 0;
				int toIndex = fromIndex + offset;
				if(toIndex > lists.size()) toIndex = lists.size();
				
				
				pageList = lists.subList(fromIndex, toIndex);
			}

		}
		
		S_GetRankPageList.Builder builder = S_GetRankPageList.newBuilder();
		builder.setMyRank(myRank);
		builder.setTotalNum(totalNum);
		for(PlayerTianti model : pageList){
			RankMsg.Builder msg = this.buildRankMsg(model);
			if(msg == null) continue;
			builder.addRankList(msg);
		}
		MessageObj msg = new MessageObj(MessageID.S_GetRankPageList_VALUE, builder.build().toByteArray());
		serviceCollection.getGameSocketService().sendDataToPlayerByPlayerId(playerId,  msg);
	}

	/**
	 * 玩家排行数据
	 */
	private RankMsg.Builder buildRankMsg(PlayerTianti model){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		Player player = playerService.getPlayerByID(model.getPlayerId());
		if(player == null) return null;
		
		PlayerProperty playerProperty = playerService.getPlayerPropertyById(model.getPlayerId());
		
		RankMsg.Builder msg = RankMsg.newBuilder();
		msg.setRank(model.getRank());
		msg.setPlayerId(model.getPlayerId());
		msg.setPlayerName(player.getPlayerName());
		msg.setCareer(player.getCareer());
		msg.setLevel(playerProperty.getLevel());
		msg.setStage(model.getStage());
		msg.setStar(model.getStar());
		return msg;
	}
	

	/**
	 * 	胜利者获得：30 -（胜利者积分-失败者积分）/10 + 连胜次数*1 (连胜次数大于10，取10次)
	 *	失败者扣去：25 -（胜利者积分-失败者积分）/10 
	 */
	private int calScore(int atkScore, int tgtScore, int WinNum, boolean suc){
		if(suc){
			return Math.max(1, 30 - (atkScore - tgtScore) / 10 + Math.min(10, WinNum) * 1);
		}
		
		return Math.max(1, 25 - (atkScore - tgtScore) / 10);
	}

	/**
	 * pk结算
	 */
	private void kill(long winnerId, long failId, int waitingTime, int useTime) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		IVipService vipService = serviceCollection.getVipService();
		
		
		PlayerTianti winTianti = this.getPlayerTianti(winnerId);
		if(winTianti == null) return;
		
		//击杀数
		winTianti.setKillNum(winTianti.getKillNum() + 1); 
		
		//连胜次数
		if(winTianti.getWinNum() >= 0){
			winTianti.setWinNum(Math.min(winTianti.getWinNum() + 1, 10));
		}else{
			winTianti.setWinNum(1);
		}
		
		PlayerTianti failTianti = this.getPlayerTianti(failId);
		if(failTianti == null) return;
		//死亡数
		failTianti.setDeadNum(failTianti.getDeadNum() + 1);
		
		//连败次数
		if(failTianti.getWinNum() >= 0){
			failTianti.setWinNum(-1);
		}else{
			failTianti.setWinNum(Math.max(failTianti.getWinNum() - 1, -10));
		}
		
		int winScore = winTianti.getScore();
		int failScore = failTianti.getScore();
		
		//赢者获得积分
		int wScore = this.calScore(winScore, failScore,  winTianti.getWinNum(), true);
		winTianti.setScore(winTianti.getScore() + wScore);
		winTianti.setUpdateTime(System.currentTimeMillis());
		BaseTiantiScore baseAtkScore = this.getBaseTiantiScore(winTianti.getScore());
		winTianti.setStage(baseAtkScore.getStage());
		winTianti.setStar(baseAtkScore.getStar());
		this.updatePlayerTianti(winTianti);
		
		PlayerPuppet winPuppet = serviceCollection.getSceneService().getPlayerPuppet(winnerId);
		if(winPuppet != null && winPuppet.getStage() != winTianti.getStage()){
			winPuppet.setStage(winTianti.getStage());
			serviceCollection.getPlayerService().synPlayerPropertyToAll(winPuppet, ProdefineConstant.STAGE, winPuppet.getStage());			
		}
		
		// 清空竞技场物品
		winPuppet.getInitItemMap().clear();
		PlayerExt wplayerExt = playerService.getPlayerExtById(winnerId);
		winPuppet.setPkMode(wplayerExt.getPkMode());
		
		//败者扣除积分
		int fScore = this.calScore(winScore, failScore, failTianti.getWinNum(), false);
		failTianti.setScore(failTianti.getScore() - fScore);
		failTianti.setUpdateTime(System.currentTimeMillis());
		BaseTiantiScore baseTgtScore = this.getBaseTiantiScore(failTianti.getScore());
		failTianti.setStage(baseTgtScore.getStage());
		failTianti.setStar(baseTgtScore.getStar());
		this.updatePlayerTianti(failTianti);
		
		PlayerPuppet failPuppet = serviceCollection.getSceneService().getPlayerPuppet(failId);
		if(failPuppet != null && failPuppet.getStage() != failTianti.getStage()){
			failPuppet.setStage(failTianti.getStage());
			serviceCollection.getPlayerService().synPlayerPropertyToAll(failPuppet, ProdefineConstant.STAGE, failPuppet.getStage());			
		}
		
		// 清空竞技场物品
		failPuppet.getInitItemMap().clear();
		PlayerExt fplayerExt = playerService.getPlayerExtById(failId);
		failPuppet.setPkMode(fplayerExt.getPkMode());
			
		// 奖励发放
		List<Reward> winRewards = null;
		List<Reward> failRewards = null;
		PlayerDaily failPlayerDaily = playerService.getPlayerDailyById(failTianti.getPlayerId());
		int value = vipService.getVipPrivilegeValue(failTianti.getPlayerId(), VipConstant.VIP_PRIVILEGE_17);
		if(failPlayerDaily.getPkRewardNum() < 11 + value){
			PlayerProperty fplayerProperty = playerService.getPlayerPropertyById(failTianti.getPlayerId());
			failRewards = this.givePkReward(failTianti.getPlayerId(), fplayerProperty.getLevel(), 1);
			
			failPlayerDaily.setPkRewardNum(failPlayerDaily.getPkRewardNum() + 1);
			playerService.updatePlayerDaily(failPlayerDaily);
		}
		
		//失败玩家数据
		S_SynEndTiantiPkPlayer.Builder failBuilder = S_SynEndTiantiPkPlayer.newBuilder();
		Player failPlayer = playerService.getPlayerByID(failId);
		failBuilder.setPkPlayers(protoBuilderService.buildEndPKPlayerMsg(failPlayer.getGuid(), failId, 1, useTime,
				-fScore, failTianti.getWinNum(), failRewards, waitingTime));
		MessageObj failMsg = new MessageObj(MessageID.S_SynEndTiantiPkPlayer_VALUE, failBuilder.build().toByteArray());
		gameSocketService.sendDataToPlayerByPlayerId(failId, failMsg);
		
		PlayerDaily winPlayerDaily = playerService.getPlayerDailyById(winTianti.getPlayerId());
		int value1 = vipService.getVipPrivilegeValue(winTianti.getPlayerId(), VipConstant.VIP_PRIVILEGE_17);
		if(winPlayerDaily.getPkRewardNum() < 11 + value1){
			PlayerProperty wplayerProperty = playerService.getPlayerPropertyById(winTianti.getPlayerId());
			winRewards = this.givePkReward(winTianti.getPlayerId(), wplayerProperty.getLevel(), 0);
			
			winPlayerDaily.setPkRewardNum(winPlayerDaily.getPkRewardNum() + 1);
			playerService.updatePlayerDaily(winPlayerDaily);
		}
		
		// pk结束同步结果数据 
		// 胜利玩家数据
		S_SynEndTiantiPkPlayer.Builder winBuilder = S_SynEndTiantiPkPlayer.newBuilder();
		Player winPlayer = playerService.getPlayerByID(winnerId);		
		winBuilder.setPkPlayers(protoBuilderService.buildEndPKPlayerMsg(winPlayer.getGuid(), winnerId, 0, useTime,
				wScore, winTianti.getWinNum(), winRewards, waitingTime));
		MessageObj winMsg = new MessageObj(MessageID.S_SynEndTiantiPkPlayer_VALUE, winBuilder.build().toByteArray());
		gameSocketService.sendDataToPlayerByPlayerId(winnerId, winMsg);
	}

	/**
	 * 发放奖励
	 */
	private List<Reward> givePkReward(long playerId, int level, int state) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IRewardService rewardService = serviceCollection.getRewardService();
		
		BaseTiantiPKReward pkReward = this.getBaseTiantiPKReward(level);
		Map<Integer, List<Reward>> rewardMap = new HashMap<>();
		
		if (state == 0){
			rewardMap = pkReward.getWinRewardMap();
		}else{
			rewardMap = pkReward.getFailRewardMap();
		}
		
		List<Reward> rewardList = new ArrayList<Reward>();
		for(Map.Entry<Integer, List<Reward>> entry : rewardMap.entrySet()){			
			List<Reward> rewards = entry.getValue();
			if(rewards.isEmpty()) continue;
			rewardList.add(rewardService.globalRandom(rewards));
		}
		
		//背包满发邮件
		try {
			rewardService.fetchRewardList_nocheck(playerId, rewardList);
		} catch (Exception e) {
			LogUtil.error("TiantiService givePkReward error : " + e);
		}
		
		return rewardList;
	}
	
	/**
	 * 刷新排行
	 */
	public void refreshRank(){
		//是否赛季中
		boolean acting = false;
		long curTime = System.currentTimeMillis();
		for(BaseTiantiDate model : this.getBaseTiantiDates()){
			if(curTime >= model.getStartTime().getTime() && curTime <= model.getEndTime().getTime()){
				acting = true;
				break;
			}
		}
		if(!acting) return;
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IWeekActivityService weekActivityService = serviceCollection.getWeekActivityService();
		
		//是否活动时间内
		BaseWeekActivity activity = weekActivityService.getBaseWeekActivity(ActivityConstant.ACTIVITY_104);
		if(!DateService.isInTime(activity.getStartHour(), activity.getStartMin(), Math.min(activity.getEndHour() + 1, 23), activity.getEndMin())){
			return;
		}
		
		try {
			Set<GameEntity> lists = CacheSynDBService.getFromFiveUpdateThreeCache(CacheSynConstant.PLAYER_TIANTI);
			if(lists != null && !lists.isEmpty()){
				serviceCollection.getBatchExcuteService().batchUpdate(lists);
			}
		} catch (Exception e) {
			LogUtil.error("异常：", e);
		}
		
		List<PlayerTianti> lists = playerTiantiDAO.listRankPlayerTianti();
		int rank = 1;
		for(PlayerTianti model : lists){
			model.setRank(rank);
			
			rank++;
		}
		CacheService.putToCache(CacheConstant.TIANTI_RANK, lists);
	}

	@Override
	public void checkDate() {
		
		//是否赛季结束日
		boolean overFlag = false;
		for(BaseTiantiDate model : this.getBaseTiantiDates()){
			if(DateService.isCurrentDay(model.getEndTime())){
				overFlag = true;
				break;
			}
		}
		
		if(overFlag){
			ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
			GameSocketService gameSocketService = serviceCollection.getGameSocketService();
			IMailService mailService = serviceCollection.getMailService();
			
			try {
				Set<GameEntity> lists = CacheSynDBService.getFromFiveUpdateThreeCache(CacheSynConstant.PLAYER_TIANTI);
				if(lists != null && !lists.isEmpty()){
					serviceCollection.getBatchExcuteService().batchUpdate(lists);
				}
			} catch (Exception e) {
				LogUtil.error("checkDate异常：", e);
			}
			
			List<Long> onlineIds = gameSocketService.getOnLinePlayerIDList();
			
			List<PlayerTianti> lists = playerTiantiDAO.listAllRankPlayerTianti();
			int rank = 1;
			for(PlayerTianti model : lists){
				//排名奖励
				if(rank <= 3){
					BaseTiantiReward rankReward = this.getBaseTiantiReward(rank);
					if(rankReward != null){
						mailService.systemSendMail(model.getPlayerId(), ResourceUtil.getValue("tianti_3"), ResourceUtil.getValue("tianti_4", rank), rankReward.getReward(), 0);	
					}
				}
				
				rank++;
				
				PlayerTianti playerTianti = (PlayerTianti) CacheService
						.getFromCache(CacheConstant.PLAYER_TIANTI + model.getPlayerId());
				if(playerTianti != null){
					if(onlineIds.contains(model.getPlayerId())){
						if(playerTianti.getStage() != model.getStage()){
							playerTianti.setStage(model.getStage());
							PlayerPuppet playerPuppet = serviceCollection.getSceneService().getPlayerPuppet(model.getPlayerId());
							if(playerPuppet != null){
								playerPuppet.setStage(playerTianti.getStage());
								serviceCollection.getPlayerService().synPlayerPropertyToAll(playerPuppet, ProdefineConstant.STAGE, playerPuppet.getStage());				
							}
						}
						
						// 赛季结束清排行榜信息						
						this.reset(playerTianti);
					}
				}
			}
			lists = null;
			playerTiantiDAO.resetAll();			
		}
	}

	/**
	 * 赛季结束，重置 
	 */
	private void reset(PlayerTianti pt){
		pt.setRank(0);
		pt.setDeadNum(0);
		pt.setKillNum(0);
		pt.setScore(1000);
		pt.setStage(1);
		pt.setStar(1);
		pt.setWinNum(0);
		pt.setUpdateTime(0);
		pt.setWinNum(0);
		pt.setMinMatchScore(0);
		pt.setMaxMatchScore(0);
		pt.setMatchTime(0);
		pt.setRewardStageStr(null);
		this.updatePlayerTianti(pt);
	}

	@Override
	public void doLogin(long playerId) {
//		if(!DateService.isActOpen(this.getBaseTiantiDate().getStartTime(), this.getBaseTiantiDate().getEndTime())){
//			PlayerTianti pt = this.getPlayerTianti(playerId);
//			this.reset(pt);
//		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deletePlayerTian(long playerId) {
		List<PlayerTianti> lists = (List<PlayerTianti>)CacheService.getFromCache(CacheConstant.TIANTI_RANK);
		if(lists == null) return;
		Iterator<PlayerTianti> iterator = lists.iterator();
		boolean bFind = false;
		while(iterator.hasNext()){
			PlayerTianti model = iterator.next();
			if(model.getPlayerId() == playerId){
				iterator.remove();
				
				bFind = true;
				break;
			}
		}
		
		if(bFind){
			try {
				playerTiantiDAO.deletePlayerTianti(playerId);
				
				int rank = 1;
				for(PlayerTianti model : lists){
					model.setRank(rank);
					
					rank++;
				}
			} catch (Exception e) {
				LogUtil.error("删除天梯数据异常：", e);
			}
		}
	}

	@Override
	public void match(long playerId) throws Exception{
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();
		IWeekActivityService weekActivityService = serviceCollection.getWeekActivityService();
		
		synchronized (LockService.getPlayerLockByType(playerId, LockConstant.PLAYER_TIANTI)) {
			PlayerExt playerExt = playerService.getPlayerExtById(playerId);		
			BaseMap baseMap = serviceCollection.getSceneService().getBaseMap(playerExt.getMapId());
			
			if(baseMap.getMapType() != SceneConstant.MAIN_CITY) throw new GameException(ExceptionConstant.TAINTI_3602);
			if(baseMap.isInstance()) throw new GameException(ExceptionConstant.SCENE_1203);
			if(playerExt.getTeamId() > 0) throw new GameException(ExceptionConstant.TEAM_2312);
			if(this.isPKMacth(playerId)) throw new GameException(ExceptionConstant.TAINTI_3601);
			
			//是否赛季时间内 		
			boolean acting = false; 
			long curTime = System.currentTimeMillis();
			for(BaseTiantiDate model : this.getBaseTiantiDates()){
				if(curTime >= model.getStartTime().getTime() && curTime <= model.getEndTime().getTime()){
					acting = true;
					break;
				}
			}
			if(!acting) throw new GameException(ExceptionConstant.ACTIVITY_2801);
			
			//是否活动时间内
			BaseWeekActivity activity = weekActivityService.getBaseWeekActivity(ActivityConstant.ACTIVITY_104);
			if(!DateService.isInTime(activity.getStartHour(), activity.getStartMin(), activity.getEndHour(), activity.getEndMin())){
				throw new GameException(ExceptionConstant.ACTIVITY_2801);
			}
			
			PlayerTianti playerTianti = this.getPlayerTianti(playerId);
			
			int matchScore = playerTianti.getScore()+30 * Math.min(playerTianti.getWinNum(), 5);
			playerTianti.setMinMatchScore(matchScore - 20);
			playerTianti.setMaxMatchScore(matchScore + 20);
		
			playerTianti.setMatchTime(System.currentTimeMillis());
			
			this.getMatchMap().put(playerId, playerTianti);		
		}
	}

	@Override
	public void cancelMatch(long playerId) throws Exception {
		this.getMatchMap().remove(playerId);
	}
	
	/**
	 * 获取匹配列表
	 */
	@SuppressWarnings("unchecked")
	private Map<Long, PlayerTianti> getMatchMap(){
		return (Map<Long, PlayerTianti>)CacheService.getFromCache(CacheConstant.PLAYER_TIANTI_MATCH);
	}
	
	@Override
	public synchronized void systemMatch() {
		
		Map<Long, PlayerTianti> map = this.getMatchMap();
		if(map == null || map.isEmpty()) return;
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		ISceneService sceneService = serviceCollection.getSceneService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		long curTime = System.currentTimeMillis();		
		Map<Long, PlayerTianti> copyMap = new ConcurrentHashMap<Long, PlayerTianti>(map);
		
		//从匹配池中找到合适人选进入pk
		PlayerTianti matcher = null;
		for(Map.Entry<Long, PlayerTianti> entry : copyMap.entrySet()){
			
			if(!map.containsKey(entry.getKey())) continue;
			PlayerTianti model = entry.getValue();
		
			for(Map.Entry<Long, PlayerTianti> entry2 : map.entrySet()){
				if(entry.getKey().equals(entry2.getKey())){
					continue;
				}	
				
				PlayerTianti other = entry2.getValue();				
				if(other.getScore() >= model.getMinMatchScore() && other.getScore() <= model.getMaxMatchScore()){
					matcher = other;				
					break;
				}				
			}
			
			if(matcher == null){
				//这次没匹配到人				
				if(curTime - model.getMatchTime() > 60000){
					
					//现在没有机器人  
					map.remove(model.getPlayerId());
					// 没有匹配到玩家，提示前端中断匹配 TODO
					S_CancelMatch.Builder builder = S_CancelMatch.newBuilder();
					MessageObj msg = new MessageObj(MessageID.S_CancelMatch_VALUE, builder.build().toByteArray());
					gameSocketService.sendDataToPlayerByPlayerId(model.getPlayerId(), msg);
					
				}else{
					//扩大匹配积分
					model.setMinMatchScore(model.getMinMatchScore() - 20);
					model.setMaxMatchScore(model.getMaxMatchScore() + 20);
				}
			}else{
				map.remove(model.getPlayerId());
				map.remove(matcher.getPlayerId());			
				
				//进入pk地图,loading
				try {
					String sceneGuid = PlayerUtil.getSceneGuid(SceneConstant.TYPE_TIANTI, model.getPlayerId());
					int mapId = SceneConstant.TIANTI_MAP_IDS[RandomService.getRandomNum(3)];
					
					// 同步加载页面信息数据
					List<PlayerTianti> pkPlayerlists = new ArrayList<>();
					pkPlayerlists.add(model);
					pkPlayerlists.add(matcher);
					
					
					// 取玩家的平均等级（向上取整）
					PlayerProperty playerProperty1 = playerService.getPlayerPropertyById(model.getPlayerId());		
					PlayerProperty playerProperty2 = playerService.getPlayerPropertyById(matcher.getPlayerId());
					int level = (int)Math.floor((playerProperty2.getLevel() + playerProperty1.getLevel())/2);
					
					this.initLoadPkPlayer(pkPlayerlists, level);
										
					sceneService.enterScene(model.getPlayerId(), mapId, 0, false, sceneGuid, 0);
					PlayerDaily playerDaily = playerService.getPlayerDailyById(model.getPlayerId());					
					playerDaily.setTiantiNum(playerDaily.getTiantiNum() + 1);
					playerService.updatePlayerDaily(playerDaily);
					
					sceneService.enterScene(matcher.getPlayerId(), mapId, 0, false, sceneGuid, 1);
					PlayerDaily playerDaily2 = playerService.getPlayerDailyById(matcher.getPlayerId());						
					playerDaily2.setTiantiNum(playerDaily2.getTiantiNum() + 1);
					playerService.updatePlayerDaily(playerDaily2);
					
					matcher = null;
				} catch (Exception e) {
					LogUtil.error("进入pk地图异常：",e);
				}			
			}				
		}	
	}
	
	@Override
	public void end(SceneModel sceneModel, long winnerId, long failId) {
		synchronized (sceneModel.getSceneGuid()) {
			if(sceneModel.getSceneState() == SceneConstant.SCENE_STATE_END){
				return;
			}
			
			// 消耗的时间
			int useTime = (int)(sceneModel.getLifeTime() - (sceneModel.getEndTime() - System.currentTimeMillis()))/1000;
			
			sceneModel.setSceneState(SceneConstant.SCENE_STATE_END);
			sceneModel.setEndTime(System.currentTimeMillis());
		
			// 清理pkBuff
			this.removePKbuff(sceneModel);
					
			if(winnerId == 0){
				Map<String, PlayerPuppet> pMap = sceneModel.getPlayerPuppetMap().get(1);
				if(pMap == null) return;
				
				if(failId > 0){
					for(Map.Entry<String, PlayerPuppet> entry : pMap.entrySet()){
						PlayerPuppet p = entry.getValue();
						if(p.getEid() == failId) continue;
						
						winnerId = p.getEid();
					}
				}else{
					//血量比率大，积分少者胜 
					PlayerPuppet p1 = null;
					for(Map.Entry<String, PlayerPuppet> entry : pMap.entrySet()){
						PlayerPuppet p = entry.getValue();
						if(p1 == null){
							p1 = p;
							continue;
						}
						double p1Rate = p1.getHp() * 1.0 /p1.getHpMax();
						double p2Rate = p.getHp() * 1.0 / p.getHpMax();
						if(p1Rate > p2Rate){
							winnerId = p1.getEid();
							failId = p.getEid();
						}else if (p1Rate < p2Rate){
							winnerId = p.getEid();
							failId = p1.getEid();
						}else{
							PlayerTianti pt1 = this.getPlayerTianti(p1.getEid());
							PlayerTianti pt2 = this.getPlayerTianti(p.getEid());
							
							if(pt1.getScore() <= pt2.getScore()){
								winnerId = p1.getEid();
								failId = p.getEid();
							}else{
								winnerId = p.getEid();
								failId = p1.getEid();
							}
						}
						break;
					}
				}
			}			
			this.kill(winnerId, failId, sceneModel.getWaitingTime(), useTime);
		}	
	}
	
	/**
	 * 初始竞技场玩家物品，同步加载页面数据
	 */
	private void initLoadPkPlayer(List<PlayerTianti> pkPlayerlists, int level){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IProtoBuilderService protoBuilderService = serviceCollection.getProtoBuilderService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		ISceneService sceneService = serviceCollection.getSceneService();
		ITaskService taskService = serviceCollection.getTaskService();
		
		List<Long> list = new ArrayList<>();
		S_SynLoadPkPlayer.Builder builder = S_SynLoadPkPlayer.newBuilder();		
		
		for(PlayerTianti playerTianti : pkPlayerlists){
			long playerId = playerTianti.getPlayerId();
			Player player = playerService.getPlayerByID(playerId);
			PlayerProperty playerProperty = playerService.getPlayerPropertyById(playerId);			
			PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerId);
			
			// 将初始物品添加到玩家身上去
			BaseTiantiPKReward pkReward = this.getBaseTiantiPKReward(level);
			for(Reward reward : pkReward.getInitItemList()){
				playerPuppet.getInitItemMap().put(reward.getId(), reward.getNum());
			}
			
			builder.addPkPlayers(protoBuilderService.buildLoadPkPlayerMsg(player.getGuid(), 
					playerId, player.getPlayerName(), playerProperty.getLevel(),  player.getCareer(), playerTianti.getStage(), playerTianti.getStar()));
			
			list.add(playerId);				

			// 触发噬魂殿任务
			try {
				List<Integer> conditionList = new ArrayList<Integer>();
				conditionList.add(1);	
				taskService.executeTask(playerId, TaskConstant.TYPE_20, conditionList);
			} catch (Exception e) {
				LogUtil.error("taskService TaskConstant.TYPE_20 error :" + e);
			}
		}
		
		MessageObj msg = new MessageObj(MessageID.S_SynLoadPkPlayer_VALUE, builder.build().toByteArray());
		gameSocketService.sendDataToPlayerList(list, msg);
	}
  
	@Override
	public void useTiantiItem(long playerId, int itemId, int num) throws Exception {
		if(playerId < 1 || itemId < 1 || num < 1) throw new GameException(ExceptionConstant.ERROR_10000);
		
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
		IBagService bagService = serviceCollection.getBagService();
		ISceneService sceneService = serviceCollection.getSceneService();
		
		PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerId);
		if(playerPuppet == null) throw new GameException(ExceptionConstant.ERROR_10000);
		
		Map<Integer, Integer> initItemMap = playerPuppet.getInitItemMap();
		if(initItemMap == null || initItemMap.get(itemId) == null) throw new GameException(ExceptionConstant.ERROR_10000);
		if(initItemMap.get(itemId) < num)  throw new GameException(ExceptionConstant.BAG_1306);
		
		BaseItem baseItem = bagService.getBaseItemById(itemId);
		if(baseItem == null) throw new GameException(ExceptionConstant.BAG_1303);
		
		int effectValue = baseItem.getEffectValue();
		int result = 0;
		switch (baseItem.getEffectType()) {
		case ItemConstant.EFFECT_TYPE_1:
			result = bagService.useAddHpItem(playerId, effectValue * num);
			break;
		case ItemConstant.EFFECT_TYPE_2:
			result = bagService.useAddMpItem(playerId, effectValue * num);
			break;		
		default:
			break;
		}
		
		if(result != 0) throw new GameException(ExceptionConstant.BAG_1307); 
		
		int value = initItemMap.get(itemId) - num;
		initItemMap.put(itemId, value);
		
		S_UseTiantiItem.Builder builder = S_UseTiantiItem.newBuilder();
		builder.setItemId(itemId);
		builder.setNum(value);
		
		MessageObj msg = new MessageObj(MessageID.S_UseTiantiItem_VALUE, builder.build().toByteArray());
		gameSocketService.sendDataToPlayerByPlayerId(playerId, msg);
	}

	@Override
	public void weekQuartz() {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IMailService mailService = serviceCollection.getMailService();
		IWeekActivityService weekActivityService = serviceCollection.getWeekActivityService();
		
		//是否赛季中
		boolean acting = false;
		long curTime = System.currentTimeMillis();
		for(BaseTiantiDate model : this.getBaseTiantiDates()){
			if(curTime >= model.getStartTime().getTime() && curTime <= model.getEndTime().getTime()){
				acting = true;
				break;
			}
		}
		if(!acting) return;		
		
		//是否活动时间内
		BaseWeekActivity activity = weekActivityService.getBaseWeekActivity(ActivityConstant.ACTIVITY_104);
		if(!DateService.isInTime(activity.getStartHour(), activity.getStartMin(), Math.min(activity.getEndHour() + 1, 23), activity.getEndMin())){
			return;
		}
		
		try {
			Set<GameEntity> lists = CacheSynDBService.getFromFiveUpdateThreeCache(CacheSynConstant.PLAYER_TIANTI);
			if(lists != null && !lists.isEmpty()){
				serviceCollection.getBatchExcuteService().batchUpdate(lists);
			}
		} catch (Exception e) {
			LogUtil.error("checkDate异常：", e);
		}
		
		List<PlayerTianti> lists = playerTiantiDAO.listAllRankPlayerTianti();	
		for(PlayerTianti model : lists){
			//排位奖励
			BaseTiantiScore baseTiantiScore = this.getBaseTiantiScore(model.getScore());
			if(baseTiantiScore != null){
				mailService.systemSendMail(model.getPlayerId(), ResourceUtil.getValue("tianti_1"), ResourceUtil.getValue("tianti_2"), baseTiantiScore.getRankReward(), 0);
			}		
		}	 
	}

	@Override
	public void getStageReward(long playerId, int stage) throws Exception {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IRewardService rewardService = serviceCollection.getRewardService();
		
		PlayerTianti playerTianti = this.getPlayerTianti(playerId);
		if(playerTianti == null) throw new GameException(ExceptionConstant.ERROR_10000);
		if(playerTianti.getRewardStageList().contains(stage)) throw new GameException(ExceptionConstant.TAINTI_3600);
	
		BaseTiantiScore baseTiantiScore = this.getBaseTiantiScoreByStage(stage);
		if(baseTiantiScore != null){
			List<Reward> rewards = new ArrayList<Reward>();
			for(List<Integer> r : baseTiantiScore.getStageRewardList()){
				Reward reward = new Reward();
				reward.setType(r.get(0));
				reward.setId(r.get(1));
				reward.setNum(r.get(2));
				reward.setBlind(r.get(3));
				rewards.add(reward);
			}
			
			rewardService.fetchRewardList(playerId, rewards);			
			playerTianti.getRewardStageList().add(stage);
			playerTianti.setRewardStageList(playerTianti.getRewardStageList());
			
			this.updatePlayerTianti(playerTianti);	
		}
		
	}

	@Override
	public boolean isPKMacth(long playerId) {
		
		if(this.getMatchMap().containsKey(playerId)) return true;
		
		return false;
	}
	
	@Override
	public void refreshDropItem(BaseDropItem baseDropItem, SceneModel sceneModel){
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IRewardService rewardService = serviceCollection.getRewardService();
		IBattleService battleService = serviceCollection.getBattleService();	
		Map<Integer, List<DropItemInfo>> dropItems = new HashMap<Integer, List<DropItemInfo>>();
		
		List<Reward> rewardList = new ArrayList<Reward>();
		for(Map.Entry<Integer, List<Reward>> drMap : baseDropItem.getDropItemMap().entrySet()){			
			List<Reward> rewards = drMap.getValue();
			if(rewards.isEmpty()) continue;
			// 随机掉落物品
			rewardList.add(rewardService.globalRandom(rewards));
		}		
		
		//System.out.println("掉落ID :" + baseDropItem.getId() + "掉落位置 :" + baseDropItem.getPosition());
		
		// 创建掉落
		int index = 1;
		BaseMap baseMap = serviceCollection.getSceneService().getBaseMap(sceneModel.getMapId());
		for(Reward reward : rewardList){
			index = battleService.createDrop(dropItems, "", baseDropItem.getPosList().get(0),  baseDropItem.getPosList().get(1), 
					baseDropItem.getPosList().get(2), reward.getType(), reward.getId(), reward.getNum(), 0, null, index, baseMap,
					sceneModel,	70, 0);
		}
		
		// 同步给前端
		if(!dropItems.isEmpty()){
			for(Map.Entry<Integer, List<DropItemInfo>> entry : dropItems.entrySet()){
				List<Long> playerIds = serviceCollection.getSceneService().getNearbyPlayerIdsByGridId(sceneModel, entry.getKey());
				if(!playerIds.isEmpty()){
					battleService.offerDropItems(entry.getValue(), playerIds);
				}
			}		
		}							
	}

	@Override
	public void addPKbuff(SceneModel senceModel, int buffId) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		ISceneService sceneService = serviceCollection.getSceneService();
		IBuffService buffService = serviceCollection.getBuffService();
		
		for(Long playerId : sceneService.getScenePlayerIds(senceModel)){			
			buffService.addBuffById(playerId, buffId);
		}	
		
		senceModel.getPkBuff().add(buffId);
	}
	
	private void removePKbuff(SceneModel sceneModel) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IBuffService buffService = serviceCollection.getBuffService();
		ISceneService sceneService = serviceCollection.getSceneService();
		
		if(sceneModel.getPkBuff() != null && !sceneModel.getPkBuff().isEmpty()){
			for(int buffId : sceneModel.getPkBuff()){			
				for(Long playerId : sceneService.getScenePlayerIds(sceneModel)){	
					PlayerPuppet playerPuppet = sceneService.getPlayerPuppet(playerId);					
					buffService.removeBuffById(playerPuppet, buffId);
				}
			}
		}
	}

	@Override
	public void dealExit(long playerId) {
		Map<Long, PlayerTianti> map = this.getMatchMap();
		if(map == null || map.isEmpty()) return;
		
		// 清理匹配状态
		if(this.isPKMacth(playerId)){
			map.remove(playerId);
		}		
	}
}
