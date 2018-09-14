package com.common;


import com.service.IBaseDataService;
import com.service.IBatchExcuteService;
import com.service.IChatService;
import com.service.IItemService;
import com.service.ILogService;
import com.service.IUserService;
import com.service.IWebService;
import com.service.impl.BaseDataService;
import com.service.impl.BatchExcuteService;
import com.service.impl.ChatService;
import com.service.impl.ItemService;
import com.service.impl.LogService;
import com.service.impl.UserService;
import com.service.impl.WebService;

/**
 * @author ken
 * 2014-3-10
 * service容器
 */
/**
 * @author Administrator
 *
 */
public class ServiceCollection {

	private IUserService userService = new UserService();
	private IBaseDataService baseDataService = new BaseDataService();
	private ILogService logService = new LogService();
	private IBatchExcuteService batchExcuteService = new BatchExcuteService();
	private IItemService itemService = new ItemService();
	private GameCCSocketService gameCCSocketService = new GameCCSocketService();
	private IWebService webService = new WebService();
	private IChatService chatService = new ChatService();


	/**
	 * 初始化缓存数据
	 * */
	public void initCache() {
		userService.initUserCache();
		baseDataService.initData();
		chatService.initChatCache();
		CacheSynDBService.initCacheMap();
	}
	
	public IUserService getUserService() {
		return userService;
	}

	public IBaseDataService getBaseDataService() {
		return baseDataService;
	}

	public ILogService getLogService() {
		return logService;
	}
	
	public IBatchExcuteService getBatchExcuteService() {
		return batchExcuteService;
	}

	public void setBatchExcuteService(IBatchExcuteService batchExcuteService) {
		this.batchExcuteService = batchExcuteService;
	}
	
	public IItemService getItemService() {
		return itemService;
	}

	public void setItemService(IItemService itemService) {
		this.itemService = itemService;
	}

	public GameCCSocketService getGameCCSocketService() {
		return gameCCSocketService;
	}

	public IWebService getWebService() {
		return webService;
	}

	public IChatService getChatService() {
		return chatService;
	}

}
