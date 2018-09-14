package com.dao;

import java.util.List;

import com.constant.AppItemConstant;
import com.db.GccSqlSessionTemplate;
import com.domain.AppItem;

/** 申请物品DAO*/
public class AppItemDao extends GccSqlSessionTemplate{
	
	/** 申请物品列表*/
	public List<AppItem> getAppItemList(){
		String selSql = "SELECT APP_ITEM_ID AS appItemId, APP_TYPE AS appType, GAME_SITE AS gameSite,PLAYER_ID_LIST AS playerIDList, APP_DATE AS appDate, ITEMLIST AS itemList, CONTENT AS content, DETAIL AS detail, REASON AS reason, STATE AS state, STATE_INFO AS stateInfo, TITLE AS title, NAME AS name, APP_PLAYER_ID AS appPlayerID, AGENT AS agent FROM t_app_item WHERE STATE = " + AppItemConstant.APP_ITEM_APP;
		return this.selectList(selSql, AppItem.class);
	}
	
	/** 发放日志列表*/
	public List<AppItem> getAppItemLogList(String agent){
		String selSql = "SELECT APP_ITEM_ID AS appItemId, APP_TYPE AS appType, GAME_SITE AS gameSite,PLAYER_ID_LIST AS playerIDList, APP_DATE AS appDate, ITEMLIST AS itemList, CONTENT AS content, DETAIL AS detail, REASON AS reason, STATE AS state, STATE_INFO AS stateInfo, TITLE AS title, NAME AS name, APP_PLAYER_ID AS appPlayerID, AGENT AS agent FROM t_app_item WHERE AGENT='"+agent+"' AND STATE IN (3,4) AND NAME != 'qidian' ORDER BY APP_DATE DESC LIMIT 20";
		return this.selectList(selSql, AppItem.class);
	}
	
	/** 插入数据*/
	public void insertAppItem(AppItem appItem){
		String insertSql = appItem.getInsertSql();
		Long appItemId = this.insert(insertSql);
		appItem.setAppItemId(appItemId.intValue());
	}
	
	/** 更新数据*/
	public void updateAppItem(AppItem appItem){
		String updateSql = "UPDATE t_app_item SET STATE = " + appItem.getState() + ", STATE_INFO = '"+appItem.getStateInfo()+"' WHERE APP_ITEM_ID = " + appItem.getAppItemId();
		this.update(updateSql);
	}

	
}
