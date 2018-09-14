package com.dao;

import java.util.List;

import com.db.GccSqlSessionTemplate;
import com.domain.config.BaseAgentConfig;
import com.domain.config.BaseServerConfig;


/**
 * @author ken
 * 2014-3-24
 * 基础数据
 */
public class BaseDataDAO extends GccSqlSessionTemplate {

	/**
	 * 通过站点取服务器配置
	 */
	public BaseServerConfig getBaseServerConfig(String gameSite){
		
		String sql = "select * from config_server where gameSite = '"+gameSite+"'";
		
		return this.selectOne(sql, BaseServerConfig.class);
	}
	
	/**
	 * 通过服务器编号取服务器配置
	 */
	public BaseServerConfig getBaseServerConfigByServerNo(int serverNo){
		
		String sql = "select * from config_server where serverNo = "+serverNo;
		
		return this.selectOne(sql, BaseServerConfig.class);
	}
	
	/**
	 * 通过平台取平台配置
	 */
	public BaseAgentConfig getBaseAgentConfig(String agent){
		
		String sql = "select * from config_agent where agent = '"+agent+"'";
		
		return this.selectOne(sql, BaseAgentConfig.class);
	}
	
	/**
	 * 取所有服务器配置
	 */
	public List<BaseServerConfig> listBaseServerConfigs(){
		String sql = "select * from config_server ";
		
		return this.selectList(sql, BaseServerConfig.class);
	}
	
	/**
	 * 取所有平台配置
	 */
	public List<BaseAgentConfig> listBaseAgentConfigs(){
		String sql = "select * from config_agent ";
		
		return this.selectList(sql,BaseAgentConfig.class);
	}
}
