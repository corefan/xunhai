package com.service.impl;

import java.util.List;
import java.util.Map;

import com.dao.OpenDataDAO;
import com.service.IOpenService;
import com.util.DAOFactory;
import com.util.LogUtil;

/**
 * @author ken
 * 2015-12-1
 * 开放数据	
 */
public class OpenService implements IOpenService {

	private OpenDataDAO openDataDAO = DAOFactory.getOpenDataDAO();
	
	@Override
	public int getCurrentOnlineNum(String agent) {
		return openDataDAO.getCurrentOnlineNum(agent);
	}
	
	public List<Map<String, Object>> getCurrentOnlineNum_agent(String agent) {
		return openDataDAO.getCurrentOnlineNum_agent(agent);
	}
	
	public int getCurrentOnlineNum_site(String gameSite) {
		return openDataDAO.getCurrentOnlineNum_site(gameSite);
	}

	public String execDBAFunc_1(int gameId, String gameSite) {
		
		try {
			openDataDAO.execDBAFunc_1(gameId, gameSite);
		} catch (Exception e) {
			LogUtil.error("异常: ",e);
			return "error";
		}
		
		return "success";
	}
}
