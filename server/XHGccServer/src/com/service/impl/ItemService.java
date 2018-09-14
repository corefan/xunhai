package com.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.common.CacheService;
import com.common.DateService;
import com.common.GCCContext;
import com.common.JSONService;
import com.constant.CacheConstant;
import com.dao.AppItemDao;
import com.domain.AppItem;
import com.domain.BaseItem;
import com.service.IItemService;
import com.util.LogUtil;

public class ItemService implements IItemService{
	
	private AppItemDao appItemDao = new AppItemDao();

	@Override
	public void insertAppitem(AppItem appItem) {
		appItemDao.insertAppItem(appItem);
	}

	@Override
	public AppItem getAppItemID(int appItemID) {
		List<AppItem> appItemList = getItemList();
		for (AppItem appItem : appItemList) {
			if(appItem.getAppItemId() == appItemID) return appItem;
		}
		
		return null;
	}

	@Override
	public List<AppItem> getItemList() {
		List<AppItem> appItemList = appItemDao.getAppItemList();
		if(appItemList == null) appItemList  = new ArrayList<AppItem>();
		return appItemList;
	}
	
	@Override
	public List<AppItem> getAppLogList(String agent) {
		List<AppItem> appItemList = appItemDao.getAppItemLogList(agent);
		if(appItemList == null) appItemList = new ArrayList<AppItem>();
		return appItemList;
	}

	@Override
	public void updateAppItem(AppItem appItem) {
		appItemDao.updateAppItem(appItem);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseItem getItemByID(int itemID) {
		Map<Integer, BaseItem> itemMap = (Map<Integer, BaseItem>) CacheService.getFromCache(CacheConstant.B_ITEM_MAP);
		
		return itemMap.get(itemID);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseItem getItemByName(String itemName) {
		Map<Integer, BaseItem> itemMap = (Map<Integer, BaseItem>) CacheService.getFromCache(CacheConstant.B_ITEM_MAP);
		for(Map.Entry<Integer, BaseItem> entry : itemMap.entrySet()){
			BaseItem model = entry.getValue();
			if(model.getName().equals(itemMap)){
				return model;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BaseItem> getItemListByName(String name) {
		List<BaseItem> lists = new ArrayList<BaseItem>();
		
		Map<Integer, BaseItem> itemMap = (Map<Integer, BaseItem>) CacheService.getFromCache(CacheConstant.B_ITEM_MAP);
		for(Map.Entry<Integer, BaseItem> entry : itemMap.entrySet()){
			BaseItem model = entry.getValue();
			if(model.getName().indexOf(name) != -1){
				lists.add(model);
			}
		}
		return lists;
	}
	
	public String sendAppItemData(List<AppItem> appItemList){
		
		JSONObject jsonObject = new JSONObject();
		List<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
		try {
			if(appItemList != null){
				for (AppItem item : appItemList) {
					JSONObject object = new JSONObject();
					
					object.put("appItemID", item.getAppItemId());
					object.put("appType", item.getAppType());
					object.put("state", item.getState());
					object.put("stateInfo", item.getStateInfo());
					object.put("appDate", DateService.dateFormat(item.getAppDate()));
					object.put("content", item.getContent());
					object.put("detail", item.getDetail());
					object.put("gameSite", item.getGameSite());
					object.put("itemList", item.getItemList());
					object.put("reason", item.getReason());	
					object.put("receiveNames", item.getPlayerIDList());
					object.put("title", item.getTitle());
					object.put("name", item.getName());
					object.put("userID", item.getAppPlayerID());
					object.put("agent", item.getAgent());
					StringBuffer sb = new StringBuffer();
					int appType = item.getAppType();
					sb.append(item.getName()).append(item.getGameSite());
					if(appType == 1){
						sb.append("像所有玩家发送道具为");
					}else{
						sb.append("向指定玩家【");
						String[] playerList =item.getPlayerIDList().split(";");
						for (String string : playerList) {
							sb.append(string + ",");
						}
						sb.append("】发送物品为");
					}
					
					
					object.put("showTip", parseItemList(item.getItemList()));
					jsonObjectList.add(object);
				}
			}
			
			jsonObject.put("appItemList", jsonObjectList.toString());
		} catch (Exception e) {
			LogUtil.error("异常：", e);
		}
		
		return jsonObject.toString();
	}
	
	private String parseItemList(String itemList){
		StringBuilder sb = new StringBuilder();
		IItemService itemService = GCCContext.getInstance().getServiceCollection().getItemService();
		List<JSONObject> jsonObjectList = JSONService.stringToJSONList(itemList);
		try {
			for (JSONObject jsonObject : jsonObjectList) {
				sb.append("物品名称：" + itemService.getItemByID(jsonObject.getInt("itemId")).getName());
				sb.append(",");
				sb.append("数量：" + jsonObject.getInt("itemNum"));
				sb.append(",");
			}
		} catch (Exception e) {
			
		}
		
		return sb.toString();
	}
}
