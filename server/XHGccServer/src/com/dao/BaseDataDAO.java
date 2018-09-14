package com.dao;

import java.util.List;

import com.db.GccSqlSessionTemplate;
import com.domain.BaseServerConfig;


/**
 * @author ken
 * 2014-3-24
 * 基础数据
 */
public class BaseDataDAO extends GccSqlSessionTemplate {

	
	
	/** 获取游戏配置*/
	public List<BaseServerConfig> getGameSiteVariableList(){
		String sql = "select * from config_server ";
		return this.selectList(sql, BaseServerConfig.class);
	}
	
	/**
	 * 创建服务器记录
	 * @param model
	 */
	public void createServer(BaseServerConfig model){
		this.insert(model.getInsertSql());
	}
	
	/**
	 * 更新服务器记录
	 * @param model
	 * @param gameSite
	 */
	public void updateServer(BaseServerConfig model, String gameSite){
		this.update(model.getUpdateSql(gameSite));
		
	}
	
	/**
	 * 删除服务器记录
	 * @param gameSite
	 */
	public void deleteServer(String gameSite){
		this.delete("DELETE FROM config_server WHERE gameSite = '"+gameSite+"'");
	}
}
