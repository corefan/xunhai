package com.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.common.CacheService;
import com.common.GCCContext;
import com.constant.CacheConstant;
import com.dao.UserDAO;
import com.domain.ServerConf;
import com.domain.User;
import com.service.IBaseDataService;
import com.service.IUserService;
import com.util.DAOFactory;
import com.util.LogUtil;

/**
 * @author ken 2014-3-10 用户service
 */
public class UserService implements IUserService {

	private UserDAO userDAO = DAOFactory.getUserDAO();

	public void initUserCache() {
		List<User> userList = userDAO.getUserList();

		CacheService.putToCache(CacheConstant.USER_CACHE, userList);
	}

	public User login(String userName, String password) {
		User loginUser = null;
		List<User> userList = getUserList();
		for (User user : userList) {
			if (user.getUserName().equals(userName) && user.getPassword().equals(password)) {
				loginUser = user;
				break;
			}
		}
		return loginUser;
	}

	@SuppressWarnings("unchecked")
	public List<User> getUserList() {
		return (List<User>) CacheService.getFromCache(CacheConstant.USER_CACHE);
	}

	@Override
	@SuppressWarnings("unchecked")
	public JSONObject getServerConfListByRID(User user, int gameID) {
		JSONObject jsonObject = new JSONObject();
		List<ServerConf> serverConfList = (List<ServerConf>) CacheService.getFromCache(CacheConstant.B_GAME_LIST);
		IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
		
		List<JSONObject> list = new ArrayList<JSONObject>();
		try {
			Map<String, List<ServerConf>> map = baseDataService.scListToMap(serverConfList, gameID);
			Set<String> set = map.keySet();
			for (String string : set) {
				JSONObject obj = new JSONObject();
				obj.put("agent", string);
				List<ServerConf> confList = map.get(string);
				List<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
				for(ServerConf server : confList){
					if (server.getState() == 1) {
						JSONObject o = new JSONObject();
						o.put("name", server.getServerName());
						o.put("gameSite", server.getGameSite());
						o.put("openDate", server.getOpenServerDate());
						jsonObjectList.add(o);
					}
				}
				obj.put("gameServerList", jsonObjectList);

				list.add(obj);
			}

			jsonObject.put("agentSite", list);
		} catch (Exception e) {
			LogUtil.error("异常:", e);
			return jsonObject;
		}

		return jsonObject;
	}

}
