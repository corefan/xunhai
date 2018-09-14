package com.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cache.CacheSynDBService;
import com.service.IActivityService;
import com.service.IBagService;
import com.service.IBatchExcuteService;
import com.service.IBattleService;
import com.service.IBuffService;
import com.service.IChatService;
import com.service.ICollectService;
import com.service.ICommonService;
import com.service.IEnemyService;
import com.service.IEpigraphService;
import com.service.IEquipmentService;
import com.service.IFamilyService;
import com.service.IFashionService;
import com.service.IFriendService;
import com.service.IFurnaceService;
import com.service.IFuseService;
import com.service.IGMService;
import com.service.IGameConfigCacheService;
import com.service.IGuildService;
import com.service.IInstanceService;
import com.service.ILogService;
import com.service.ILoginService;
import com.service.IMailService;
import com.service.IMarketService;
import com.service.IMonsterService;
import com.service.IPayService;
import com.service.IPlayerService;
import com.service.IPropertyService;
import com.service.IProtoBuilderService;
import com.service.IRankService;
import com.service.IRebotService;
import com.service.IRewardService;
import com.service.ISceneService;
import com.service.ISignService;
import com.service.ISkillService;
import com.service.ISmsService;
import com.service.ISynDataService;
import com.service.ITaskService;
import com.service.ITeamService;
import com.service.ITiantiService;
import com.service.ITowerService;
import com.service.ITradeService;
import com.service.IVipService;
import com.service.IWakanService;
import com.service.IWeekActivityService;
import com.service.IWingService;
import com.service.impl.ActivityService;
import com.service.impl.BagService;
import com.service.impl.BatchExcuteService;
import com.service.impl.BattleService;
import com.service.impl.BuffService;
import com.service.impl.ChatService;
import com.service.impl.CollectService;
import com.service.impl.CommonService;
import com.service.impl.EnemyService;
import com.service.impl.EpigraphService;
import com.service.impl.EquipmentService;
import com.service.impl.FamilyService;
import com.service.impl.FashionService;
import com.service.impl.FriendService;
import com.service.impl.FurnaceService;
import com.service.impl.FuseService;
import com.service.impl.GMService;
import com.service.impl.GameConfigCacheService;
import com.service.impl.GuildService;
import com.service.impl.InstanceService;
import com.service.impl.LogService;
import com.service.impl.LoginService;
import com.service.impl.MailService;
import com.service.impl.MarketService;
import com.service.impl.MonsterService;
import com.service.impl.PayService;
import com.service.impl.PlayerService;
import com.service.impl.PropertyService;
import com.service.impl.ProtoBuilderService;
import com.service.impl.RankService;
import com.service.impl.RebotService;
import com.service.impl.RewardService;
import com.service.impl.SceneService;
import com.service.impl.SignService;
import com.service.impl.SkillService;
import com.service.impl.SmsService;
import com.service.impl.SynDataService;
import com.service.impl.TaskService;
import com.service.impl.TeamService;
import com.service.impl.TiantiService;
import com.service.impl.TowerService;
import com.service.impl.TradeService;
import com.service.impl.VipService;
import com.service.impl.WakanService;
import com.service.impl.WeekActivityService;
import com.service.impl.WingService;
import com.taskpool.TaskPool;
import com.util.CommonUtil;


/**
 * 2013-10-30
 * service容器
 */
public class ServiceCollection {

	private GameSocketService gameSocketService = new GameSocketService();

	private IBatchExcuteService batchExcuteService = new BatchExcuteService();
	private ISynDataService synDataService = new SynDataService();
	private IGameConfigCacheService gameCfgCacheService = new GameConfigCacheService();
	private ILoginService loginService = new LoginService();
	private IPlayerService playerService = new PlayerService();
	private IProtoBuilderService protoBuilderService = new ProtoBuilderService();
	private ISceneService sceneService = new SceneService();
	private ICommonService commonService = new CommonService();
	private IBagService bagService = new BagService();
	private IEquipmentService equipmentService = new EquipmentService();
	private IMonsterService monsterService = new MonsterService();
	private IBattleService battleService = new BattleService();
	private ISkillService skillService = new SkillService();
	private IFashionService fashionService = new FashionService();
	private IFriendService friendService = new FriendService();
	private IMailService mailService = new MailService();
	private IRewardService rewardService = new RewardService();
	private IPropertyService propertyService = new PropertyService();
	private ITaskService taskService = new TaskService();
	private IChatService chatService = new ChatService();
	private IWakanService wakanService = new WakanService();
	private IEpigraphService epigraphService = new EpigraphService();
	private IInstanceService instanceService = new InstanceService();
	private ITeamService teamService = new TeamService();	
	private ICollectService collectService = new CollectService();
	private ITowerService towerService = new TowerService();
	private ITradeService tradeService = new TradeService();
	private IFuseService fuseService = new FuseService();
	private IBuffService buffService = new BuffService();	
	private IFamilyService familyService = new FamilyService();
	private ITiantiService tiantiService = new TiantiService();	
	private IMarketService marketService = new MarketService();
	private ISignService signService = new SignService();
	private IActivityService activityService = new ActivityService();	
	private IWingService wingService = new WingService();
	private IRankService rankService = new RankService();	
	private IEnemyService enemyService = new EnemyService();
	private IWeekActivityService weekActivityService = new WeekActivityService();
	private IVipService vipService = new VipService();
	private IPayService payService = new PayService();
	private ISmsService smsService = new SmsService();
	private IGMService gmService = new GMService();
	private ILogService logService = new LogService();
	private IRebotService rebotService = new RebotService();
	private IGuildService guildService = new GuildService();
	private IFurnaceService furnaceService = new FurnaceService();

	/************ 热更新后的对象 *************/
	private Map<String, Object> hotServiceMap = new ConcurrentHashMap<String, Object>();

	public Map<String, Object> getHotServiceMap() {
		return hotServiceMap;
	}

	public void initialize(){
		// 初始化基础表缓存
		this.initBaseCache();
		// 初始化其他缓存
		this.initOtherCache();
		// 加载玩家数据  
		playerService.loadPlayerData();
		
	}
	
	/** 重新加载Servcie class */
	public void reLoadServiceClass(Object obj) {
		hotServiceMap.put(obj.getClass().getSimpleName(), obj);
	}
	
	
	/** 
	 * 基础表缓存
	 * */
	public void initBaseCache() {
		// 初始化
		CommonUtil.init();
		
		//公共配置
		commonService.initBaseCache();
		//综合奖励配置
		rewardService.initBaseCache();
		//采集
		collectService.initBaseCache();
		//场景
		sceneService.initBaseCache();
		//背包
		bagService.initBaseCache();
		//装备
		equipmentService.initBaseCache();
		//怪物
		monsterService.initBaseCache();
		//技能
		skillService.initBaseCache();
		//时装
		fashionService.initBaseCache();
		//任务
		taskService.initBaseCache();
		//注灵
		wakanService.initBaseCache();
		//铭文
		epigraphService.initBaseCache();
		//组队
		teamService.initBaseCache();		
		//大荒塔
		towerService.initBaseCache();
		//物品合成
		fuseService.initBaseCache();
		//buff
		buffService.initBaseCache();
		//天梯
		tiantiService.initBaseCache();		
		// 商城
		marketService.initBaseCache();		
		// 签到
		signService.initBaseSign();		
		// 活动
		activityService.initBaseActivity();
		// 羽翼
		wingService.initBaseWing();
		//周活动
		weekActivityService.initBaseCache();
		// 聊天
		chatService.initBaseCache();		
		// 副本
		instanceService.initBaseCache();
		// vip
		vipService.initBaseCache();
		//充值
		payService.initBaseCache();		
		//帮派
		guildService.initBaseCache();
		//熔炉
		furnaceService.initBaseCache();
		
		//机器人
		rebotService.initBaseCache();
		
//		// 日志库
//		logService.initGameSite_agent_tableData();
		
	}

	/**
	 * 初始化其他缓存
	 * */
	public void initOtherCache() {
		// 初始化连接缓存
		gameSocketService.initChannelCache();

		// 初始化定时更新缓存
		CacheSynDBService.initCacheMap();
		/** 锁常量 */
		LockService.initPlayerLockCache();
		
		// 任务池初始
		TaskPool.initTaskPool();
		
		/** 玩家 */
		playerService.initCache();
		
		//场景
		sceneService.initCache();
		
		//装备 
		equipmentService.initCache();
		
		// 邮件
		mailService.initMailCache();
		
		//组队
		teamService.initCache();		
		
		//天梯
		tiantiService.initCache();		

		//家族
		familyService.initCache();
		
		//帮派
		guildService.initCache();
		
		//排行榜
		rankService.initCache();
		
		//交易行
		tradeService.initCache();
		
		//综合奖励
		rewardService.initCache();
	}

	public GameSocketService getGameSocketService() {
		return gameSocketService;
	}
	
	public IBatchExcuteService getBatchExcuteService() {
		Object hotObj = hotServiceMap.get(batchExcuteService.getClass().getSimpleName());
		if (hotObj != null) return (IBatchExcuteService) hotObj;
		return batchExcuteService;
	}

	public ISynDataService getSynDataService() {
		Object hotObj = hotServiceMap.get(synDataService.getClass().getSimpleName());
		if (hotObj != null) return (ISynDataService) hotObj;
		return synDataService;
	}

	public IGameConfigCacheService getGameCfgCacheService() {
		Object hotObj = hotServiceMap.get(gameCfgCacheService.getClass().getSimpleName());
		if (hotObj != null) return (IGameConfigCacheService) hotObj;
		return gameCfgCacheService;
	}

	public ILoginService getLoginService() {
		Object hotObj = hotServiceMap.get(loginService.getClass().getSimpleName());
		if (hotObj != null) return (ILoginService) hotObj;
		return loginService;
	}

	public IPlayerService getPlayerService() {
		Object hotObj = hotServiceMap.get(playerService.getClass().getSimpleName());
		if (hotObj != null) return (IPlayerService) hotObj;
		return playerService;
	}

	public IProtoBuilderService getProtoBuilderService() {
		Object hotObj = hotServiceMap.get(protoBuilderService.getClass().getSimpleName());
		if (hotObj != null) return (IProtoBuilderService) hotObj;
		return protoBuilderService;
	}

	public ISceneService getSceneService() {
		Object hotObj = hotServiceMap.get(sceneService.getClass().getSimpleName());
		if (hotObj != null) return (ISceneService) hotObj;
		return sceneService;
	}

	public ICommonService getCommonService() {
		Object hotObj = hotServiceMap.get(commonService.getClass().getSimpleName());
		if (hotObj != null) return (ICommonService) hotObj;
		return commonService;
	}

	public IBagService getBagService() {
		Object hotObj = hotServiceMap.get(bagService.getClass().getSimpleName());
		if (hotObj != null) return (IBagService) hotObj;
		return bagService;
	}

	public IEquipmentService getEquipmentService() {
		Object hotObj = hotServiceMap.get(equipmentService.getClass().getSimpleName());
		if (hotObj != null) return (IEquipmentService) hotObj;
		return equipmentService;
	}

	public IMonsterService getMonsterService() {
		Object hotObj = hotServiceMap.get(monsterService.getClass().getSimpleName());
		if (hotObj != null) return (IMonsterService) hotObj;
		return monsterService;
	}

	public IBattleService getBattleService() {
		Object hotObj = hotServiceMap.get(battleService.getClass().getSimpleName());
		if (hotObj != null) return (IBattleService) hotObj;
		return battleService;
	}

	public ISkillService getSkillService() {
		Object hotObj = hotServiceMap.get(skillService.getClass().getSimpleName());
		if (hotObj != null) return (ISkillService) hotObj;
		return skillService;
	}

	public IFashionService getFashionService() {
		Object hotObj = hotServiceMap.get(fashionService.getClass().getSimpleName());
		if (hotObj != null) return (IFashionService) hotObj;
		return fashionService;
	}

	public IFriendService getFriendService() {
		Object hotObj = hotServiceMap.get(friendService.getClass().getSimpleName());
		if (hotObj != null) return (IFriendService) hotObj;
		return friendService;
	}

	public IMailService getMailService() {
		Object hotObj = hotServiceMap.get(mailService.getClass().getSimpleName());
		if (hotObj != null) return (IMailService) hotObj;
		return mailService;
	}

	public IRewardService getRewardService() {
		Object hotObj = hotServiceMap.get(rewardService.getClass().getSimpleName());
		if (hotObj != null) return (IRewardService) hotObj;
		return rewardService;
	}

	public IPropertyService getPropertyService() {
		Object hotObj = hotServiceMap.get(propertyService.getClass().getSimpleName());
		if (hotObj != null) return (IPropertyService) hotObj;
		return propertyService;
	}

	public ITaskService getTaskService() {
		Object hotObj = hotServiceMap.get(taskService.getClass().getSimpleName());
		if (hotObj != null) return (ITaskService) hotObj;
		return taskService;
	}

	public IChatService getChatService() {
		Object hotObj = hotServiceMap.get(chatService.getClass().getSimpleName());
		if (hotObj != null) return (IChatService) hotObj;
		return chatService;
	}

	public IWakanService getWakanService() {
		Object hotObj = hotServiceMap.get(wakanService.getClass().getSimpleName());
		if (hotObj != null) return (IWakanService) hotObj;
		return wakanService;
	}

	public IEpigraphService getEpigraphService() {
		Object hotObj = hotServiceMap.get(epigraphService.getClass().getSimpleName());
		if (hotObj != null) return (IEpigraphService) hotObj;
		return epigraphService;
	}
	public IInstanceService getInstanceService() {
		Object hotObj = hotServiceMap.get(instanceService.getClass().getSimpleName());
		if (hotObj != null) return (IInstanceService) hotObj;
		return instanceService;
	}

	public ITeamService getTeamService() {
		Object hotObj = hotServiceMap.get(teamService.getClass().getSimpleName());
		if (hotObj != null) return (ITeamService) hotObj;
		return teamService;
	}	
	
	public ICollectService getCollectService() {
		Object hotObj = hotServiceMap.get(collectService.getClass().getSimpleName());
		if (hotObj != null) return (ICollectService) hotObj;
		return collectService;
	}

	public ITowerService getTowerService() {
		Object hotObj = hotServiceMap.get(towerService.getClass().getSimpleName());
		if (hotObj != null) return (ITowerService) hotObj;
		return towerService;
	}

	public ITradeService getTradeService() {
		Object hotObj = hotServiceMap.get(tradeService.getClass().getSimpleName());
		if (hotObj != null) return (ITradeService) hotObj;
		return tradeService;
	}
	
	public IFuseService getFuseService() {
		Object hotObj = hotServiceMap.get(fuseService.getClass().getSimpleName());
		if (hotObj != null) return (IFuseService) hotObj;
		return fuseService;
	}

	public IBuffService getBuffService() {
		Object hotObj = hotServiceMap.get(buffService.getClass().getSimpleName());
		if (hotObj != null) return (IBuffService) hotObj;
		return buffService;
	}
	
	public ITiantiService getTiantiService() {
		Object hotObj = hotServiceMap.get(tiantiService.getClass().getSimpleName());
		if (hotObj != null) return (ITiantiService) hotObj;
		return tiantiService;
	}
	
	public IFamilyService getFamilyService() {
		Object hotObj = hotServiceMap.get(familyService.getClass().getSimpleName());
		if (hotObj != null) return (IFamilyService) hotObj;
		return familyService;
	}

	public IMarketService getMarketService() {
		Object hotObj = hotServiceMap.get(marketService.getClass().getSimpleName());
		if (hotObj != null) return (IMarketService) hotObj;
		return marketService;
	}		

	public ISignService getSignService() {
		Object hotObj = hotServiceMap.get(signService.getClass().getSimpleName());
		if (hotObj != null) return (ISignService) hotObj;
		return signService;
	}

	public IActivityService getActivityService() {
		Object hotObj = hotServiceMap.get(activityService.getClass().getSimpleName());
		if (hotObj != null) return (IActivityService) hotObj;
		return activityService;
	}

	public IWingService getWingService() {
		Object hotObj = hotServiceMap.get(wingService.getClass().getSimpleName());
		if (hotObj != null) return (IWingService) hotObj;
		return wingService;
	}

	public IRankService getRankService() {
		Object hotObj = hotServiceMap.get(rankService.getClass().getSimpleName());
		if (hotObj != null) return (IRankService) hotObj;
		return rankService;
	}
	

	public IEnemyService getEnemyService() {
		Object hotObj = hotServiceMap.get(enemyService.getClass().getSimpleName());
		if (hotObj != null) return (IEnemyService) hotObj;
		return enemyService;
	}

	public IWeekActivityService getWeekActivityService() {
		Object hotObj = hotServiceMap.get(weekActivityService.getClass().getSimpleName());
		if (hotObj != null) return (IWeekActivityService) hotObj;
		return weekActivityService;
	}

	public IVipService getVipService() {
		Object hotObj = hotServiceMap.get(vipService.getClass().getSimpleName());
		if (hotObj != null) return (IVipService) hotObj;
		return vipService;
	}

	public IPayService getPayService() {
		Object hotObj = hotServiceMap.get(payService.getClass().getSimpleName());
		if (hotObj != null) return (IPayService) hotObj;
		return payService;
	}

	public ISmsService getSmsService() {
		Object hotObj = hotServiceMap.get(smsService.getClass().getSimpleName());
		if (hotObj != null) return (ISmsService) hotObj;
		return smsService;
	}

	public IGMService getGmService() {
		Object hotObj = hotServiceMap.get(gmService.getClass().getSimpleName());
		if (hotObj != null) return (IGMService) hotObj;
		return gmService;
	}

	public ILogService getLogService() {
		Object hotObj = hotServiceMap.get(logService.getClass().getSimpleName());
		if (hotObj != null) return (ILogService) hotObj;
		return logService;
	}


	public IRebotService getRebotService() {
		Object hotObj = hotServiceMap.get(rebotService.getClass().getSimpleName());
		if (hotObj != null) return (IRebotService) hotObj;
		return rebotService;
	}

	public IGuildService getGuildService() {
		Object hotObj = hotServiceMap.get(guildService.getClass().getSimpleName());
		if (hotObj != null) return (IGuildService) hotObj;
		return guildService;
	}

	public IFurnaceService getFurnaceService() {
		Object hotObj = hotServiceMap.get(furnaceService.getClass().getSimpleName());
		if (hotObj != null) return (IFurnaceService) hotObj;
		return furnaceService;
	}
	
}
