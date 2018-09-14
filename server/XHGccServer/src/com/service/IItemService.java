package com.service;

import java.util.List;

import com.domain.AppItem;
import com.domain.BaseItem;

/** 申请物品serivce*/
public interface IItemService {

	/** 添加申请物品数据*/
	public void insertAppitem(AppItem appItem);
	
	/** 获取appItem*/
	public AppItem getAppItemID(int appItemID);
	
	/** 获取申请数据*/
	public List<AppItem> getItemList();
	
	/**
	 * 获得发放日志列表
	 */
	public List<AppItem> getAppLogList(String agent);
	
	/** 更新数据*/
	public void updateAppItem(AppItem appItem);
	
	/** 根据ID 查找*/
	public BaseItem getItemByID(int itemID);
	
	/** 根据名称查找*/
	public BaseItem getItemByName(String itemName);
	
	public List<BaseItem> getItemListByName(String name);
	
	public String sendAppItemData(List<AppItem> appItemList);
	
}
