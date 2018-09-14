package com.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.common.Config;
import com.common.DateService;
import com.common.GameContext;
import com.common.ServiceCollection;
import com.core.jedis.RedisUtil;
import com.domain.config.BaseServerConfig;
import com.domain.log.CostLog;
import com.domain.log.FiveOnlineLog;
import com.domain.log.LoginLog;
import com.domain.log.MarketLog;
import com.domain.log.OnlineTimeLog;
import com.domain.log.RegisterLog;
import com.domain.player.Player;
import com.service.IGameConfigCacheService;
import com.service.ILogService;
import com.service.IPlayerService;
import com.util.IDUtil;

/**
 * 日志
 * @author jiangqin
 * @date 2017-8-7
 */
public class LogService  implements ILogService{

	@Override
	public void createCostLog(long playerId, int type, String costName,	int value) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();		
		Player player = playerService.getPlayerByID(playerId);
		
		CostLog costLog = new CostLog();
		costLog.setLogId(IDUtil.geneteId(CostLog.class));
		costLog.setUserId(player.getUserId());
		costLog.setPlayerId(playerId);		
		costLog.setPlayerName(player.getPlayerName());		
		costLog.setType(type);
		costLog.setCostName(costName);
		costLog.setValue(value);
		costLog.setCreateTime(new Date());
		costLog.setAgent(Config.AGENT);
		costLog.setGameSite(player.getSite());		
		
		// 添加到缓存
		String key = "log@"+player.getSite()+"@CostLogList@"+DateService.dateFormatYMD(new Date());
		RedisUtil.addListValueOfL(key, RedisUtil.serialize(costLog), 31 * DateService.DAY_SEC);
	}

	@Override
	public void createMarketLog(long playerId, int itemId, String itemName,
			int price, int num) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();		
		Player player = playerService.getPlayerByID(playerId);
		
		MarketLog marketLog = new MarketLog();
		marketLog.setLogId(IDUtil.geneteId(MarketLog.class));
		marketLog.setUserId(player.getUserId());
		marketLog.setPlayerId(playerId);		
		marketLog.setPlayerName(player.getPlayerName());		
		marketLog.setItemId(itemId);
		marketLog.setItemName(itemName);
		marketLog.setPrice(price);
		marketLog.setNum(num);		
		marketLog.setCreateTime(new Date());
		marketLog.setAgent(Config.AGENT);
		marketLog.setGameSite(player.getSite());		
		
		// 添加到缓存
		String key = "log@"+player.getSite()+"@MarketLogList@"+DateService.dateFormatYMD(new Date());
		RedisUtil.addListValueOfL(key, RedisUtil.serialize(marketLog), 31 * DateService.DAY_SEC);
	}

	@Override
	public void createFiveOnlineLog() {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IGameConfigCacheService gameConfigCacheService = serviceCollection.getGameCfgCacheService();
		
		Date curDate = DateService.getCurrentUtilDate();
		
		//各个合服的在线人数
		Map<String, Integer> pnumMap = new HashMap<String, Integer>();
		List<Long> playerIds= serviceCollection.getGameSocketService().getOnLinePlayerIDList();
		
		List<BaseServerConfig> servers = gameConfigCacheService.listMergeServers();
		if(servers.size() > 1){
			for(Long pid : playerIds){
				Player p = serviceCollection.getPlayerService().getPlayerByID(pid);
				if(p != null){
					Integer num = pnumMap.get(p.getSite());
					if(num == null){
						num = 0;
					}
					num++;
					pnumMap.put(p.getSite(), num);
				}
			}
		}else{
			pnumMap.put(Config.GAME_SITE, playerIds.size());
		}
		
		//找出合服的服务器
		for(BaseServerConfig model : servers){
			int num = 0;
			
			if(pnumMap.containsKey(model.getGameSite())){
				num = pnumMap.get(model.getGameSite());
			}
			
			FiveOnlineLog fiveOnlineLog = new FiveOnlineLog();
			fiveOnlineLog.setLogId(IDUtil.geneteId(FiveOnlineLog.class));
			fiveOnlineLog.setNum(num);
			fiveOnlineLog.setAgent(model.getAgent());
			fiveOnlineLog.setGameSite(model.getGameSite());
			fiveOnlineLog.setCreateTime(curDate);
			
			String key = "log@"+model.getGameSite()+"@FiveOnlineLogList@"+DateService.dateFormatYMD(new Date());
			RedisUtil.addListValueOfL(key, RedisUtil.serialize(fiveOnlineLog), 15 * DateService.DAY_SEC);
		}

//		int regNum = logDAO.getRegNum(model.getGameSite());
//		int[] arr = playerService.getCreateNum(model.getGameSite());
//		int pNum = arr[0];
//		int createNum = arr[1];
	}

	@Override
	public void createLoginLog(long userId, String agent, String gameSite) {
		LoginLog loginLog = new LoginLog();
		loginLog.setLogId(IDUtil.geneteId(LoginLog.class));
		loginLog.setUserId(userId);
		loginLog.setAgent(agent);
		loginLog.setGameSite(gameSite);
		loginLog.setCreateTime(new Date());
		
		//统计进游戏的日志
		String key = "log@"+gameSite+"@LoginLogList@"+DateService.dateFormatYMD(new Date());
		RedisUtil.addListValueOfL(key, RedisUtil.serialize(loginLog), 31 * DateService.DAY_SEC);	
	}

	@Override
	public void createGameStepLog(long playerId, int taskId) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IPlayerService playerService = serviceCollection.getPlayerService();		
		Player player = playerService.getPlayerByID(playerId);
		if(player == null) return;
		
		//统计游戏步骤
		String key = "log@"+player.getSite()+"@GameStepMap";
		RedisUtil.addValueOfMap(key, taskId+"", 1, -1);	
	}

	@Override
	public void createFirstPayLvLog(String gameSite, int level) {

		//首冲等级统计
		String key = "log@"+gameSite+"@FirstLvMap";
		RedisUtil.addValueOfMap(key, level+"", 1, -1);	
	}
	
	@Override
	public void createRegisterLog(long userId, String gameSite) {
		RegisterLog log = new RegisterLog();
		log.setLogId(IDUtil.geneteId(RegisterLog.class));
		log.setUserId(userId);
		log.setCreateTime(new Date());
		log.setAgent(Config.AGENT);
		log.setGameSite(gameSite);
		
		//注册日志
		String key = "log@"+gameSite+"@RegisterLogList@"+DateService.dateFormatYMD(new Date());
		RedisUtil.addListValueOfL(key, RedisUtil.serialize(log), 31 * DateService.DAY_SEC);
		
		//统计注册人数
		String key2 = "log@"+gameSite+"@RegisterNumSet";
		RedisUtil.addListValueOfS(key2, -1, RedisUtil.serialize(userId));
	}
	
	@Override
	public void createPlayerLog(long userId, long playerId, String gameSite) {
		//统计创过角的用户数
		String key = "log@"+gameSite+"@CreatePlayerSet";
		RedisUtil.addListValueOfS(key, -1, RedisUtil.serialize(userId));
		
		//统计总角色数
		String key2 = "log@"+gameSite+"@TotalPlayerNum";
		RedisUtil.incrValue(key2);
	}	

	@Override
	public void createOnlineTimeLog(Date curDate) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IGameConfigCacheService gameConfigCacheService = serviceCollection.getGameCfgCacheService();
		IPlayerService playerService = serviceCollection.getPlayerService();
		
		
		List<BaseServerConfig> servers = gameConfigCacheService.listMergeServers();
		
		for(BaseServerConfig model : servers){
			
			String date = DateService.dateFormatYMD(curDate);
			Map<String, Object> map = playerService.getOnlineTimeNum(model.getGameSite(), date);
			int loginNum = Integer.parseInt(map.get("loginNum").toString());
			int num1 = Integer.parseInt(map.get("num1").toString());
			int num5 = Integer.parseInt(map.get("num5").toString());
			int num10 = Integer.parseInt(map.get("num10").toString());
			int num20 = Integer.parseInt(map.get("num20").toString());
			int num30 = Integer.parseInt(map.get("num30").toString());
			int num40 = Integer.parseInt(map.get("num40").toString());
			int num50 = Integer.parseInt(map.get("num50").toString());
			int num60 = Integer.parseInt(map.get("num60").toString());
			int h5 = Integer.parseInt(map.get("h5").toString());
			int h10 = Integer.parseInt(map.get("h10").toString());
			int uph10 = Integer.parseInt(map.get("uph10").toString());
			
			OnlineTimeLog onlineTimeLog = new OnlineTimeLog();
			onlineTimeLog.setLogId(IDUtil.geneteId(OnlineTimeLog.class));
			onlineTimeLog.setCreateTime(new Date());
			onlineTimeLog.setAgent(model.getAgent());
			onlineTimeLog.setGameSite(model.getGameSite());
			onlineTimeLog.setLoginNum(loginNum);
			onlineTimeLog.setNum1(num1);
			onlineTimeLog.setNum5(num5);
			onlineTimeLog.setNum10(num10);
			onlineTimeLog.setNum20(num20);
			onlineTimeLog.setNum30(num30);
			onlineTimeLog.setNum40(num40);
			onlineTimeLog.setNum50(num50);
			onlineTimeLog.setNum60(num60);
			onlineTimeLog.setH5(h5);
			onlineTimeLog.setH10(h10);
			onlineTimeLog.setUph10(uph10);
			String key = "log@"+model.getGameSite()+"@OnlineTimeLogList@"+date;
			RedisUtil.addListValueOfL(key, RedisUtil.serialize(onlineTimeLog), 31 * DateService.DAY_SEC);
		}
		
	}

}
