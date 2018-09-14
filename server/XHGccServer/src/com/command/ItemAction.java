package com.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.io.Buffer;
import org.json.JSONObject;

import com.common.CacheService;
import com.common.DateService;
import com.common.GCCContext;
import com.common.GameCCSocketService;
import com.common.GameException;
import com.common.JSONService;
import com.constant.AppItemConstant;
import com.constant.CacheConstant;
import com.constant.OptTypeConstant;
import com.constant.PathConstant;
import com.core.Connection;
import com.core.GameCCEventListener;
import com.domain.AppItem;
import com.domain.BaseItem;
import com.domain.MessageObj;
import com.domain.User;
import com.service.IBaseDataService;
import com.service.IItemService;
import com.service.ILogService;
import com.service.IUserService;
import com.service.IWebService;

/**
 * 物品Action
 * @author ken
 *
 */
public class ItemAction {
	
	GameCCSocketService gameCCSocketService = GCCContext.getInstance().getServiceCollection().getGameCCSocketService();
	IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
	IWebService webService = GCCContext.getInstance().getServiceCollection().getWebService();
	
	/**
	 * 获得物品列表
	 */
	@SuppressWarnings("unchecked")
	public void getItemEquipmentList(MessageObj msgObj, Connection connection) throws Exception  {
		
		JSONObject jsonObject = new JSONObject();
		List<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
		Map<Integer, BaseItem> itemMap = (Map<Integer, BaseItem>) CacheService.getFromCache(CacheConstant.B_ITEM_MAP);
		for(Map.Entry<Integer, BaseItem> entry : itemMap.entrySet()){
			BaseItem item = entry.getValue();
			JSONObject obj = new JSONObject();
			obj.put("id", item.getId());
			obj.put("name", item.getName());
			obj.put("type", item.getGoodsType());
			jsonObjectList.add(obj);
		}

		
		jsonObject.put("pageList", jsonObjectList.toString());
		
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.ITEM_1, jsonObject.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);

	}
	
	/**
	 * 申请发放物品
	 */
	public void applySendItem(MessageObj msgObj, Connection connection) throws Exception  {
		
		IItemService itemService = GCCContext.getInstance().getServiceCollection().getItemService();
		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
		
		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));
		User user = userService.getUserbyID(connection.getUserID());
		
		itemService.insertAppitem(newAppItem(param, user));
		
		JSONObject result = new JSONObject();
		String exceptionStr = connection.getExceptionStr().toString();
		if(!"".equals(exceptionStr)){
			exceptionStr += "其余服操作成功";
			result.put("exceptionStr", exceptionStr);
		}
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.ITEM_4, result.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);

	}
	
	private AppItem newAppItem(JSONObject param, User user) throws Exception {
		int appType = param.getInt("appType");
		String playerNameList = param.getString("receiveNames");
		String itemList = param.getString("itemList");
		String content = param.getString("content");
		String reason = param.getString("reason");
		String title = param.getString("title");
		String gameSite = param.getString("gameSite");
		String agent = param.getString("agent");
		
		if("".equals(itemList.trim())){
			throw new GameException("发放物品不能为空");
		}
		if(appType == AppItemConstant.APPIONT_PLAYER && "".equals(playerNameList.trim())){
			throw new GameException("发放玩家不能为空");
		}
		if(gameSite == null || gameSite.trim().equals("")){
			throw new GameException("运营商不能为空");
		}
		
		AppItem appItem = new AppItem();
		appItem.setAppDate(DateService.getCurrentUtilDate());
		appItem.setAppType(appType);
		appItem.setContent(content);
		appItem.setGameSite(gameSite);
		appItem.setItemList(itemList);
		appItem.setPlayerIDList(playerNameList);
		appItem.setReason(reason);
		appItem.setState(AppItemConstant.APP_ITEM_APP);
		appItem.setTitle(title);
		appItem.setName(user.getUserName());
		appItem.setAppPlayerID(user.getUserID());
		appItem.setAgent(agent);
		
		if(appType == AppItemConstant.ALL_PLAYER){
			appItem.setDetail(user.getUserName() + "向所有玩家发送" + itemList);
		}else{
			appItem.setDetail(user.getUserName() + "向" + playerNameList + "发送物品");
		}
		
		return appItem;
	}
	
	/**
	 * 获得申请发放列表
	 */
	public void getApplyList(MessageObj msgObj, Connection connection) throws Exception  {
		
		IItemService itemService = GCCContext.getInstance().getServiceCollection().getItemService();
		
		User user = GCCContext.getInstance().getServiceCollection().getUserService().getUserbyID(connection.getUserID());
		
		if(user == null) return;
		
		List<AppItem> lists = new ArrayList<AppItem>();
		
		List<AppItem> itemList = itemService.getItemList();
		for(AppItem item : itemList){
			if(user.getAgent().indexOf(item.getAgent()) != -1){
				lists.add(item);
			}
		}
		String result = itemService.sendAppItemData(lists);
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.ITEM_6, result.getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);

	}
	
	/**
	 * 获得发放日志
	 */
	public void getApplyLogList(MessageObj msgObj, Connection connection) throws Exception  {
		
		IItemService itemService = GCCContext.getInstance().getServiceCollection().getItemService();
		
		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));
		
		String agent = param.getString("agent");
		
		String result = itemService.sendAppItemData(itemService.getAppLogList(agent));
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.ITEM_9, result.getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);

	}
	
	/**
	 * 查询物品
	 */
	public void showItemInfo(MessageObj msgObj, Connection connection) throws Exception  {
		
		IItemService itemService = GCCContext.getInstance().getServiceCollection().getItemService();
		
		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));
		
		String itemContent = param.getString("itemContent");
		
		List<BaseItem>itemList = itemService.getItemListByName(itemContent);
		
		JSONObject jsonObject = new JSONObject();
		List<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
		
		if(itemList != null){
			for (BaseItem item : itemList) {
				JSONObject obj = new JSONObject();
				obj.put("id", item.getId());
				obj.put("name", item.getName());
				obj.put("type", item.getGoodsType());
				jsonObjectList.add(obj);
			}
		}
			
		jsonObject.put("itemList", jsonObjectList.toString());
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.ITEM_7, jsonObject.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);

	}
	
	/**
	 * 审核同意发送物品(部分)
	 */
	public void verifyAppItem(MessageObj msgObj, Connection connection) throws Exception  {
		
		IItemService itemService = GCCContext.getInstance().getServiceCollection().getItemService();
		IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
		ILogService logService = GCCContext.getInstance().getServiceCollection().getLogService();
		
		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));
		
		int appItemID = param.getInt("appItemID");
		int state = param.getInt("state");
		
		AppItem item = itemService.getAppItemID(appItemID);
		
		User user = userService.getUserbyID(connection.getUserID());
		
		if(state == AppItemConstant.APP_ITEM_PASS){
			//审核通过 发放物品
			int type = item.getAppType();
			String gameSite = item.getGameSite();
			String agent = item.getAgent();
			
			//指定玩家
			if(type == AppItemConstant.APPIONT_PLAYER){
				String url = baseDataService.getUrlByGameSitePath(item.getGameSite(), PathConstant.ITEM);
				sendItemToPlayerList(connection, item.getTitle(), item.getContent(), user.getUserID(), user.getUserName(), OptTypeConstant.ITEM_2, 
						item.getItemList(), item.getPlayerIDList(), url, item.getGameSite());
			}else{
				List<String> gameSiteList = userService.gameSiteUrl(gameSite, agent);
				if(gameSiteList != null){
					for (String site : gameSiteList) {
						String gameUrl = baseDataService.getUrlByGameSitePath(site, PathConstant.ITEM);
						sendItemToAll(connection, OptTypeConstant.ITEM_3, item.getTitle(),item.getContent(), item.getItemList(), user, gameUrl, site);
					}
				}
			}
		}
		
		String content = "";
		StringBuffer sb = new StringBuffer();
		int appType = item.getAppType();
		sb.append(item.getName()).append(item.getGameSite());
		if(appType == 1){
			sb.append("的所有玩家发送道具为");
		}else{
			sb.append("向指定玩家【");
			String[] playerList =item.getPlayerIDList().split(";");
			for (String string : playerList) {
				sb.append(string + ",");
			}
			sb.append("】发送物品");
		}
		
		if(state == AppItemConstant.APP_ITEM_PASS){
			content = user.getUserName() + "审核通过该申请物品";
			item.setState(AppItemConstant.APP_ITEM_SEND);
		}else if(state == AppItemConstant.APP_ITEM_REFUSE){
			content = user.getUserName() + "拒绝该申请物品";
			item.setState(AppItemConstant.APP_ITEM_REFUSE);
			item.setStateInfo("已拒绝");
		}
		
		logService.recordOptLog(user.getUserID(), user.getUserName(), state, content, sb.toString(), connection.getHostAddress());
		
		JSONObject result = new JSONObject();
		String exceptionStr = connection.getExceptionStr().toString();
		if(!"".equals(exceptionStr)){
			exceptionStr += "其余服操作成功";
			result.put("exceptionStr", exceptionStr);
		}else{
			result.put("exceptionStr", "发送成功");
		}
		
		item.setStateInfo(result.getString("exceptionStr"));
		itemService.updateAppItem(item);
		
		MessageObj resultMsg = new MessageObj(OptTypeConstant.ITEM_5, result.toString().getBytes("UTF8"));
		gameCCSocketService.sendData(connection, resultMsg);
		
	}
	
	/**
	 * 发送物品
	 * */
	private void sendItemToPlayerList(Connection connection, String title, String content, int userID, String userName, int optType, 
			String itemList, String receiveNames, String url, String gameSite) throws Exception {
		
		IItemService itemService = GCCContext.getInstance().getServiceCollection().getItemService();
		ILogService logService = GCCContext.getInstance().getServiceCollection().getLogService();

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("optType", optType);
		jsonObject.put("itemList", itemList);
		jsonObject.put("receiveNames", receiveNames);
		jsonObject.put("title", title);
		jsonObject.put("content", content);
		jsonObject.put("url", url);
		
		//解析itemList
		String detail = "";
		List<JSONObject> jsonObjectList = JSONService.stringToJSONList(itemList);
		if(jsonObjectList != null){
			for (JSONObject obj : jsonObjectList) {
				BaseItem item = itemService.getItemByID(obj.getInt("itemId"));
				if(item == null) continue;
				detail += item.getName() + "*" + obj.getString("itemNum") + ";";
			}
		}
		
		GameCCEventListener eventListener = new GameCCEventListener(connection, gameSite, false) {
			@Override
			public void onResponseContent(Buffer content) throws IOException {
				String result = content.toString("UTF-8");
				MessageObj resultMsg = new MessageObj(OptTypeConstant.ITEM_8, result.getBytes("UTF8"));
				gameCCSocketService.sendData(getConnection(), resultMsg);
				}
			};
			
		webService.sendDateForWeb(jsonObject, url, eventListener);
		
		logService.recordOptLog(userID, userName, optType, userName + "向" + receiveNames + "发送物品", detail, connection.getHostAddress());
		
	}
	
	/**
	 * 全服发送物品
	 * */
	private void sendItemToAll(Connection connection, int optType, String title, String content, String anex, User user, String url, String gameSite) throws Exception{
		
		ILogService logService = GCCContext.getInstance().getServiceCollection().getLogService();
				
		//附件,标题，内容不能为空
		if(anex == "" || title == "" || content == "") return;
		JSONObject jsonObject = new JSONObject();
		
		jsonObject.put("optType", optType);
		jsonObject.put("title", title);
		jsonObject.put("content", content);
		jsonObject.put("anex", anex);
		jsonObject.put("url", url);

		GameCCEventListener eventListener = new GameCCEventListener(connection, gameSite, false) {
			@Override
			public void onResponseContent(Buffer content) throws IOException {
				String result = content.toString("UTF-8");
				MessageObj resultMsg = new MessageObj(OptTypeConstant.ITEM_8, result.getBytes("UTF8"));
				gameCCSocketService.sendData(getConnection(), resultMsg);
				}
			};
			
		webService.sendDateForWeb(jsonObject, url, eventListener);
		
		logService.recordOptLog(user.getUserID(), user.getUserName(), optType, "全服发送物品", jsonObject.toString(), connection.getHostAddress());
		
	}
	
}
