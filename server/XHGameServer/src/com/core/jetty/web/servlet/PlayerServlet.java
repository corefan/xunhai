package com.core.jetty.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.common.Config;
import com.common.DateService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.common.LogoutCacheService;
import com.common.ServiceCollection;
import com.constant.IpConstant;
import com.constant.ItemConstant;
import com.constant.OptTypeConstant;
import com.core.Connection;
import com.core.jedis.RedisUtil;
import com.domain.bag.BaseEquipment;
import com.domain.bag.PlayerBag;
import com.domain.bag.PlayerEquipment;
import com.domain.player.Player;
import com.domain.player.PlayerExt;
import com.domain.player.PlayerProperty;
import com.domain.player.PlayerWealth;
import com.service.IBagService;
import com.service.IEquipmentService;
import com.service.IGMService;
import com.util.LogUtil;
import com.util.PatternUtil;

/**
 * GM-玩家servlet
 * @author ken
 * @date 2017-7-24
 */
public class PlayerServlet extends BaseServlet {

	private static final long serialVersionUID = 92661446554138423L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		initReqResp(req, resp);
		
		if(!checkIP(req)) return;
		
		String result = null;
		try {

			JSONObject jsonObject = dealMsg(req);
			if (jsonObject == null) return;

			int optType = Integer.parseInt(jsonObject.get("optType").toString());

			switch (optType) {
			case OptTypeConstant.PLAYER_1:
				result = showOnLineNum();
				break;
			case OptTypeConstant.PLAYER_2:
				result = searchPlayer(jsonObject);
				break;
			case OptTypeConstant.PLAYER_3:
				result = fenghao(jsonObject);
				break;
			case OptTypeConstant.PLAYER_4:
				result = jiefeng(jsonObject);
				break;
			case OptTypeConstant.PLAYER_5:
				result = deletePlayerCache(jsonObject);
				break;
			case OptTypeConstant.PLAYER_LOG_6:
				result = backpackList(jsonObject);
				break;
			case OptTypeConstant.PLAYER_LOG_7:
			case OptTypeConstant.PLAYER_LOG_8:
			case OptTypeConstant.PLAYER_LOG_9:
				result = sendPlayerLog(jsonObject);
				break;
			case OptTypeConstant.PLAYER_10:
				result = searchPlayerInfo(jsonObject);
				break;
			case OptTypeConstant.PLAYER_15:
				result = modifyPlayerType(jsonObject);
				break;
			case OptTypeConstant.PLAYER_17:
				result = banTalk(jsonObject);
				break;
			case OptTypeConstant.PLAYER_18:
				result = unBanTalk(jsonObject);
				break;
			case OptTypeConstant.BAN_IP_OPERATE:
				result = dealIp(jsonObject.getString("ip"), Integer.parseInt(jsonObject.getString("ipState")));
				break;
			case OptTypeConstant.BAN_IP_LIST:
				result = sendBanIpList();
				break;
			}

		} catch (JSONException e) {
			LogUtil.error("异常:",e);
			result = "error";
		}
		
		if (result != null) {
			resp.getWriter().println(result);
			resp.getWriter().flush();
		}
	}

	/**
	 * 查看玩家在线人数
	 * */
	private String showOnLineNum() {

		JSONObject jsonObject = new JSONObject();
		try {
			int onlineNum = GameContext.getInstance().getServiceCollection().getGameSocketService().getOnLinePlayerIDList().size();
			jsonObject.put("onlineNum", onlineNum);
		} catch (Exception e) {
			LogUtil.error("异常:",e);
			return jsonObject.toString();
		}
		return jsonObject.toString();
	}

	/**
	 * 查询玩家信息
	 * */
	private String searchPlayer(JSONObject jsonObject) {

		IGMService gmService = GameContext.getInstance().getServiceCollection().getGmService();

		GameSocketService gameSocketService = GameContext.getInstance().getServiceCollection().getGameSocketService();
		
		JSONObject resultJson = new JSONObject();

		List<JSONObject> resultList = new ArrayList<JSONObject>();

		if (!jsonObject.has("searchType")) return resultList.toString();
		if (!jsonObject.has("searchValue")) return resultList.toString();

		try {
			
			int searchType = Integer.parseInt(jsonObject.getString("searchType"));
			String searchValue = jsonObject.getString("searchValue");

			if (searchValue == null || "".equals(searchValue.trim())) return resultList.toString();

			//TODO 直接查库比较好。  不然会产生多余缓存数据
			List<Player> playerList = new ArrayList<Player>();
			if (searchType == 1) {
				//playerList = playerService.getPlayerListByPlayerName(searchValue); 模糊搜索
				Player player =gmService.getPlayerByName_GM(searchValue);
				if (player != null) {
					playerList.add(player);
				}
			} else if (searchType == 2) {
				playerList = gmService.listPlayerByUserId_GM(Long.valueOf(searchValue));
			} else if (searchType == 3) {
				Player player = gmService.getPlayerByID_GM(Long.valueOf(searchValue));
				if (player != null) {
					playerList.add(player);
				}
			}

			for (Player player : playerList) {

				PlayerExt playerExt = gmService.getPlayerExtById_GM(player.getPlayerId());
				PlayerWealth playerWealth = gmService.getPlayerWealthById_GM(player.getPlayerId());
				PlayerProperty playerProperty = gmService.getPlayerPropertyById_GM(player.getPlayerId());
				
				
				JSONObject resultObj = new JSONObject();
				resultObj.put("playerID", player.getPlayerId());
				resultObj.put("userName", player.getUserId());
				resultObj.put("playerName", player.getPlayerName());
				resultObj.put("createTime", DateService.dateFormat(player.getCreateTime()));
				resultObj.put("career", player.getCareer());
				resultObj.put("diamond", playerWealth.getDiamond());
				resultObj.put("money", playerWealth.getGold());
				
				int state = player.getDeleteFlag();  //玩家状态
				
				if(state == 0){
					resultObj.put("stateName", "正常");
				}else{
					resultObj.put("stateName", "已删除");
				}
				
				resultObj.put("state", state);
				
				resultObj.put("logoutTime", DateService.dateFormat(playerExt.getExitTime()));
				
				if (gameSocketService.checkOnLine(player.getPlayerId())) {
					resultObj.put("onLine", "在线");
					resultObj.put("onLineFlag", 1);
				} else {
					resultObj.put("onLine", "离线");
					resultObj.put("onLineFlag", 0);

				}
				String loginIP = playerExt.getLoginIP();
				if(loginIP == null){
					loginIP = "";
				}
				resultObj.put("loginIP", loginIP);
				resultObj.put("level", playerProperty.getLevel());
				resultObj.put("battleValue", playerProperty.getBattleValue());
				resultObj.put("site", player.getSite());
				resultList.add(resultObj);
			}

			resultJson.put("host", Config.GAME_HOST);
			resultJson.put("gamePort", Config.GAME_PORT);
			resultJson.put("assert", Config.ASSETS);
			resultJson.put("gameSite", Config.GAME_SITE);
			resultJson.put("size", resultList.size());
			resultJson.put("playerList", resultList.toString());

		} catch (Exception e) {
			LogUtil.error("异常:",e);
			return resultJson.toString();

		}

		return resultJson.toString();
	}

	private String modifyPlayerType(JSONObject jsonObject){
//		try {
//			int playerID = (Integer) jsonObject.get("playerID");
//			int playerType = (Integer)jsonObject.get("playerType");
//			Player player = playerService.getPlayerByID(playerID);
//			if (player == null) return jsonObject.toString();
//			
//			player.setType(playerType);
//
//			playerService.updatePlayerDB(player);
//			playerService.synPlayerType(playerID, playerType);
//
//		} catch (Exception e) {
//
//			LogUtil.error("异常:", e);
//			return jsonObject.toString();
//
//		}

		return jsonObject.toString();
	}
	
	/** 禁言*/
	private String banTalk(JSONObject jsonObject){
		
//		Date now = new Date();
//		try {
//			int playerID = (Integer)jsonObject.get("playerID");
//			int banTime = (Integer)jsonObject.get("banTime");
//			
//			PlayerOtherExt ext = playerService.getPlayerOtherExtByID(playerID);
//			ext.setBanChatEndTime(DateService.getDateAddTime(now, banTime * 60 * 1000));
//			playerService.updatePlayerOtherExt(ext);
//			
//			
//		} catch (Exception e) {
//			LogUtil.error("异常:", e);
//			return jsonObject.toString();
//		}
		
		
		return jsonObject.toString();
	}
	
	/** 解禁*/
	private String unBanTalk(JSONObject jsonObject){
//		
//		try {
//			int playerID = (Integer)jsonObject.get("playerID");
//			
//			PlayerOtherExt ext = playerService.getPlayerOtherExtByID(playerID);
//			ext.setBanChatEndTime(null);
//			playerService.updatePlayerOtherExt(ext);
//			
//			
//		} catch (Exception e) {
//			LogUtil.error("异常:", e);
//			return jsonObject.toString();
//		}
//		
		
		return jsonObject.toString();
	}

	/**
	 * 查询玩家信息
	 * */
	private String searchPlayerInfo(JSONObject jsonObject) {

		IGMService gmService = GameContext.getInstance().getServiceCollection().getGmService();

		JSONObject resultJson = new JSONObject();

		List<JSONObject> resultList = new ArrayList<JSONObject>();

		if (!jsonObject.has("searchValue")) return resultList.toString();

		try {

			int searchType = 1;
			if (jsonObject.has("searchType")) {
				searchType = Integer.parseInt(jsonObject.getString("searchType"));
			}
			String searchValue = jsonObject.getString("searchValue");

			if (searchValue == null || "".equals(searchValue.trim())) return resultList.toString();

			//TODO 直接查库比较好。  不然会产生多余缓存数据
			List<Player> playerList = new ArrayList<Player>();
			if (searchType == 1) {
				//playerList = playerService.getPlayerListByPlayerName(searchValue); 模糊搜索
				Player player =gmService.getPlayerByName_GM(searchValue);
				if (player != null) {
					playerList.add(player);
				}
			} else if (searchType == 2) {
				playerList = gmService.listPlayerByUserId_GM(Long.valueOf(searchValue));
			} else if (searchType == 3) {
				Player player = gmService.getPlayerByID_GM(Long.valueOf(searchValue));
				if (player != null) {
					playerList.add(player);
				}
			}
			
			for (Player player : playerList) {

				JSONObject resultObj = new JSONObject();
				resultObj.put("playerId", player.getPlayerId());
				resultObj.put("userName", player.getUserId());
				resultObj.put("name", player.getPlayerName());

				resultList.add(resultObj);
			}
			resultJson.put("playerList", resultList.toString());

		} catch (Exception e) {
			LogUtil.error("异常:",e);
			return resultJson.toString();
		}

		return resultJson.toString();
	}

	/**
	 * 封号
	 * */
	private String fenghao(JSONObject jsonObject) {

		JSONObject resultObj = new JSONObject();

//		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
//		IPlayerService playerService = serviceCollection.getPlayerService();
//		GameSocketService gameSocketService = serviceCollection.getGameSocketService();
//
//		try {
//			if (!jsonObject.has("playerID")) {
//				resultObj.put("resultFlag", 0);
//				return resultObj.toString();
//			}
//
//			int playerID = jsonObject.getInt("playerID");
//			Player player = playerService.getPlayerByID_GM(playerID);
//			if (player == null) {
//				resultObj.put("resultFlag", 0);
//				return resultObj.toString();
//			}
//			// 封号
//			playerService.updatePlayerBanLogin(playerID, null);
//			// 踢下线
//			Connection con = gameSocketService.getPlayerIDConnection(playerID);
//			if (con != null) {
//				GameContext.getInstance().getActionCollection().getCommonAction().exit(con);
//			}
//
//			resultObj.put("resultFlag", 1);
//			resultObj.put("resultMsg", "操作成功");
//
//		} catch (Exception e) {
//			LogUtil.error("异常:",e);
//			try {
//				resultObj.put("resultFlag", 0);
//			} catch (JSONException e1) {
//				LogUtil.error("异常:",e);
//				return resultObj.toString();
//			}
//			return resultObj.toString();
//		}

		return resultObj.toString();
	}

	/**
	 * 解封
	 * */
	private String jiefeng(JSONObject jsonObject) {

		JSONObject resultObj = new JSONObject();

//		try {
//			if (!jsonObject.has("playerID")) {
//				resultObj.put("resultFlag", 0);
//				resultObj.put("resultMsg", "解封失败");
//				return resultObj.toString();
//			}
//
//			int playerID = jsonObject.getInt("playerID");
//			Player player = playerService.getPlayerByID_GM(playerID);
//			if (player == null)  {
//				resultObj.put("resultFlag", 0);
//				resultObj.put("resultMsg", "解封失败");
//				return resultObj.toString();
//			}
//			PlayerOtherExt otherExt = playerService.getPlayerOtherExtByID(playerID);
//			if (otherExt.getBanLoginEndTime() != null) {
//				Date startTime = DateService.addDateByType(otherExt.getBanLoginEndTime(), Calendar.YEAR, -10);
//				if (System.currentTimeMillis()/1000 - startTime.getTime()/1000 < 5*60) {
//					resultObj.put("resultFlag", 0);
//					resultObj.put("resultMsg", "封号5分钟后才能解封");
//					return resultObj.toString();
//				}
//			}
//			
//			// 解封
//			playerService.updatePlayerBanLogin(playerID, -1);
//			LogoutCacheService.deleteOnlineCache_one(playerID);
//			LogoutCacheService.deleteOnlineCache_two(playerID);
//			LogoutCacheService.deleteOnlineCache_three(playerID);
//
//			resultObj.put("resultFlag", 1);
//			resultObj.put("resultMsg", "操作成功");
//
//		} catch (Exception e) {
//			LogUtil.error("异常:",e);
//			try {
//				resultObj.put("resultFlag", 0);
//				resultObj.put("resultMsg", "解封失败");
//			} catch (JSONException e1) {
//				LogUtil.error("异常:",e);
//				return resultObj.toString();
//			}
//			return resultObj.toString();
//		}

		return resultObj.toString();
	}

	/**
	 * 删除玩家缓存
	 * */
	private String deletePlayerCache(JSONObject jsonObject) {
		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IGMService gmService = serviceCollection.getGmService();
		JSONObject resultObj = new JSONObject();

		try {
			if (!jsonObject.has("playerID")) {
				resultObj.put("resultFlag", 0);
				resultObj.put("resultMsg", "删除失败");
				
				return resultObj.toString();
			}

			long playerID = jsonObject.getLong("playerID");
			Player player = gmService.getPlayerByID_GM(playerID);
			if (player.getType() != 6) {
				resultObj.put("resultFlag", 0);
				resultObj.put("resultMsg", "请先封停账号");
				return resultObj.toString();
			}

			LogoutCacheService.deleteOnlineCache_one(playerID);
			LogoutCacheService.deleteOnlineCache_two(playerID);
			LogoutCacheService.deleteOnlineCache_three(playerID);

			resultObj.put("resultFlag", 1);
			resultObj.put("resultMsg", "操作成功");

		} catch (Exception e) {
			LogUtil.error("异常:",e);

			try {
				resultObj.put("resultFlag", 0);
				resultObj.put("resultMsg", "删除失败");
			} catch (JSONException e1) {
				LogUtil.error("异常:",e);
				return resultObj.toString();
			}
			return resultObj.toString();
		}

		return resultObj.toString();
	}

	/** 玩家背包物品 */
	private String backpackList(JSONObject jsonObject) {

		ServiceCollection serviceCollection = GameContext.getInstance().getServiceCollection();
		IBagService bagService = serviceCollection.getBagService();
		IEquipmentService equipmentService = serviceCollection.getEquipmentService();
		
		JSONObject resultObj = new JSONObject();

		try {
			if (!jsonObject.has("playerID")) {
				resultObj.put("resultFlag", 0);
				return resultObj.toString();
			}
			
			long playerID = jsonObject.getLong("playerID");

			List<JSONObject> jsonList = new ArrayList<JSONObject>();
			List<PlayerBag> pbList = bagService.getPlayerBagListByPlayerID(playerID);
			for (PlayerBag pb : pbList) {
				
				if(pb.getState() <= 0){
					continue;
				}
				
				String des = "背包栏";
				JSONObject json = new JSONObject();
				if (pb.getGoodsType() > ItemConstant.GOODS_TYPE_EQUPMENT) {
					json.put("num", pb.getNum());
					json.put("name", bagService.getBaseItemById((int)pb.getItemId()).getName());
				} 

				json.put("des", des);
				jsonList.add(json);
			}
			
			List<PlayerEquipment> elist = equipmentService.getPlayerEquipmentList(playerID);
			for(PlayerEquipment peq : elist){
				if(peq.getState() <= 0){
					continue;
				}
				String des = "背包栏";
				JSONObject json = new JSONObject();
				json.put("num", 1);

				BaseEquipment baseEquipment = equipmentService.getBaseEquipmentById(peq.getEquipmentId());
				if(baseEquipment == null){
					System.out.println("equipment is null with id is "+peq.getEquipmentId());
					continue;
				}
				json.put("name", baseEquipment.getName());
				
				if(peq.getState() == ItemConstant.EQUIP_STATE_DRESS){
					des = "装备栏";
				}else if(peq.getState() == ItemConstant.EQUIP_STATE_TRADE){
					des = "交易行";
				}

				json.put("des", des);
				jsonList.add(json);
			}
			

			resultObj.put("dataList", jsonList.toString());
		} catch (Exception e) {
			LogUtil.error("异常:",e);
			
			try {
				resultObj.put("resultFlag", 0);
			} catch (JSONException e1) {
			}
			return resultObj.toString();
		}

		return resultObj.toString();
	}

	/**
	 * 玩家日志查询
	 */
	private String sendPlayerLog(JSONObject jsonObject) {
		JSONObject obj = new JSONObject();
//		try {
//			int type = jsonObject.getInt("optType");
//			int playerID = jsonObject.getInt("playerID");
//			switch (type) {
//			case OptTypeConstant.PLAYER_LOG_7:
//				obj = logService.showLoginLogByPlayerId(playerID);
//			break;
//			case OptTypeConstant.PLAYER_LOG_8:
//				obj = logService.showCostLOGByPlayerId(playerID);
//				break;
//			case OptTypeConstant.PLAYER_LOG_9:
//				obj = playerService.showPayLogByPlayerId(playerID);
//				break;
//			
//			}
//
//		} catch (Exception e) {
//			LogUtil.error("异常:",e);
//			
//			try {
//				obj.put("resultFlag", 0);
//			} catch (JSONException e1) {
//			}
//			return obj.toString();
//		}

		return obj.toString();
	}
	
	/**  IP处理 */
	private String dealIp(String ip, int ipState){
		
		JSONObject jsonObject = new JSONObject();
		try {
			if(!PatternUtil.isHardRegexpValidate(ip, PatternUtil.ipRegex)){
				jsonObject.put("ipState", "ip格式不正确");
				return jsonObject.toString();
			}
			if(ipState == IpConstant.IP_STATE_SEAL){
				
				try {
					String key2 = "gcc@"+Config.AGENT+"@banIpSet";
					RedisUtil.addListValueOfS(key2, 31 * DateService.DAY_SEC, ip);
				} catch (Exception e) {
					LogUtil.error("封停ip错误",e);
				}
				
				long userId = 0;
				Map<Long, Connection> map =  GameSocketService.getPlayerConnectionMapCache();
				for(Map.Entry<Long, Connection> entry : map.entrySet()){
					Connection con = entry.getValue();
					if(con.getConnectIP().equals(ip.trim())){
						userId = entry.getKey();
						
						LogoutCacheService.deleteOnlineCache_three(con.getPlayerId());
						
						con.destroy();
					}
				}
				if(userId > 0){
					map.remove(userId);
					
				}
			}else{
				
				try {
					String key2 = "gcc@"+Config.AGENT+"@banIpSet";
					RedisUtil.removeValueOfSet(key2, ip.trim());
				} catch (Exception e) {
					LogUtil.error("解封ip错误",e);
				}
			}
			
			jsonObject.put("ipState", "修改成功");
			
				
		} catch (Exception e) {
			
		}
			
		return jsonObject.toString();
		
	}
	
	private String sendBanIpList(){
		JSONObject jsonObject = new JSONObject();
		
		try {
			String key = "gcc@"+Config.AGENT+"@banIpSet";
			Set<String> ipList = RedisUtil.getListValueOfS(key);
			
			List<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
			if(ipList != null){
				for (String ip : ipList) {
					JSONObject object = new JSONObject();
					object.put("ip", ip);
					object.put("ipState", 1);
					jsonObjectList.add(object);
				}
			}
			jsonObject.put("banIpList", jsonObjectList.toString());
		} catch (Exception e) {
			LogUtil.error("获取封停ip列表错误",e);
		}
		
		return jsonObject.toString();
	}

}
