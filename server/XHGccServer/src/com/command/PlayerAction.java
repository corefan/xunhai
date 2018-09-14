package com.command;


import java.io.IOException;

import org.eclipse.jetty.io.Buffer;
import org.json.JSONObject;

import com.common.GCCContext;
import com.common.GameCCSocketService;
import com.constant.OptTypeConstant;
import com.constant.PathConstant;
import com.core.Connection;
import com.core.GameCCEventListener;
import com.domain.MessageObj;
import com.domain.User;
import com.service.IBaseDataService;
import com.service.ILogService;
import com.service.IUserService;
import com.service.IWebService;

/**
 * 用户Action
 * @author ken
 *
 */
public class PlayerAction {

	GameCCSocketService gameCCSocketService = GCCContext.getInstance().getServiceCollection().getGameCCSocketService();
	IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
	IWebService webService = GCCContext.getInstance().getServiceCollection().getWebService();

	/**
	 * 查看在线人数
	 * */
	public void showOnLineNum(MessageObj msgObj, Connection connection) throws Exception  {

		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));

		String gameSite = param.getString("gameSite"); 

		String url = baseDataService.getUrlByGameSitePath(gameSite, PathConstant.PLAYER);

		GameCCEventListener eventListener = new GameCCEventListener(connection, gameSite, true) {
			@Override
			public void onResponseContent(Buffer content) throws IOException {

				String result = content.toString("UTF-8");

				MessageObj resultMsg = new MessageObj(OptTypeConstant.PLAYER_1, result.getBytes("UTF8"));
				gameCCSocketService.sendData(getConnection(), resultMsg);

			}
		};

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("optType", OptTypeConstant.PLAYER_1);

		webService.sendDateForWeb(jsonObject, url, eventListener);

	}

	/**
	 * 查询玩家信息
	 * */
	public void searchPlayer(MessageObj msgObj, Connection connection) throws Exception  {

		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));

		String gameSite = param.getString("gameSite"); 
		String searchType = param.getString("searchType"); 
		String searchValue = param.getString("searchValue"); 

		String url = baseDataService.getUrlByGameSitePath(gameSite, PathConstant.PLAYER);

		GameCCEventListener eventListener = new GameCCEventListener(connection, gameSite, true) {
			StringBuffer resultData = new StringBuffer();
			@Override
			public void onResponseContent(Buffer content) throws IOException {

				String result = content.toString("UTF-8");
				resultData.append(result);
				try {
					JSONObject json = new JSONObject(resultData.toString());
					MessageObj resultMsg = new MessageObj(OptTypeConstant.PLAYER_2, json.toString().getBytes("UTF8"));
					gameCCSocketService.sendData(getConnection(), resultMsg);
				} catch (Exception e) {
					
				}
			}
		};

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("optType", OptTypeConstant.PLAYER_2);
		jsonObject.put("searchType", searchType);
		jsonObject.put("searchValue",searchValue);

		webService.sendDateForWeb(jsonObject, url, eventListener);

	}

	/**
	 * 封号
	 */
	public void fenghao(MessageObj msgObj, Connection connection) throws Exception {

		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));

		String gameSite = param.getString("gameSite"); 
		Integer playerID = param.getInt("playerID"); 

		String url = baseDataService.getUrlByGameSitePath(gameSite, PathConstant.PLAYER);

		GameCCEventListener eventListener = new GameCCEventListener(connection, gameSite, true) {
			@Override
			public void onResponseContent(Buffer content) throws IOException {

				String result = content.toString("UTF-8");

				MessageObj resultMsg = new MessageObj(OptTypeConstant.PLAYER_3, result.getBytes("UTF8"));
				gameCCSocketService.sendData(getConnection(), resultMsg);

			}
		};

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("optType", OptTypeConstant.PLAYER_3);
		jsonObject.put("playerID", playerID);

		webService.sendDateForWeb(jsonObject, url, eventListener);


		//记录操作日志
		String ip = connection.getHostAddress();
		ILogService logService = GCCContext.getInstance().getServiceCollection().getLogService();
		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
		User user = userService.getUserbyID(connection.getUserID());
		logService.recordOptLog(user.getUserID(), user.getUserName(), OptTypeConstant.PLAYER_3, "封号", jsonObject.toString(), ip);

	}

	/**
	 * 解封
	 */
	public void jiefeng(MessageObj msgObj, Connection connection) throws Exception {

		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));

		String gameSite = param.getString("gameSite"); 
		Integer playerID = param.getInt("playerID"); 

		String url = baseDataService.getUrlByGameSitePath(gameSite, PathConstant.PLAYER);

		GameCCEventListener eventListener = new GameCCEventListener(connection, gameSite, true) {
			@Override
			public void onResponseContent(Buffer content) throws IOException {

				String result = content.toString("UTF-8");

				MessageObj resultMsg = new MessageObj(OptTypeConstant.PLAYER_4, result.getBytes("UTF8"));
				gameCCSocketService.sendData(getConnection(), resultMsg);

			}
		};

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("optType", OptTypeConstant.PLAYER_4);
		jsonObject.put("playerID", playerID);

		webService.sendDateForWeb(jsonObject, url, eventListener);


		//记录操作日志
		String ip = connection.getHostAddress();
		ILogService logService = GCCContext.getInstance().getServiceCollection().getLogService();
		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
		User user = userService.getUserbyID(connection.getUserID());
		logService.recordOptLog(user.getUserID(), user.getUserName(), OptTypeConstant.PLAYER_4, "解封", jsonObject.toString(), ip);
	}

	/**
	 * 删除玩家缓存
	 */
	public void deletePlayerCache(MessageObj msgObj, Connection connection) throws Exception {

		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));

		String gameSite = param.getString("gameSite"); 
		Integer playerID = param.getInt("playerID"); 

		String url = baseDataService.getUrlByGameSitePath(gameSite, PathConstant.PLAYER);

		GameCCEventListener eventListener = new GameCCEventListener(connection, gameSite, true) {
			@Override
			public void onResponseContent(Buffer content) throws IOException {

				String result = content.toString("UTF-8");

				MessageObj resultMsg = new MessageObj(OptTypeConstant.PLAYER_5, result.getBytes("UTF8"));
				gameCCSocketService.sendData(getConnection(), resultMsg);

			}
		};

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("optType", OptTypeConstant.PLAYER_5);
		jsonObject.put("playerID", playerID);

		webService.sendDateForWeb(jsonObject, url, eventListener);


		//记录操作日志
		String ip = connection.getHostAddress();
		ILogService logService = GCCContext.getInstance().getServiceCollection().getLogService();
		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
		User user = userService.getUserbyID(connection.getUserID());
		logService.recordOptLog(user.getUserID(), user.getUserName(), OptTypeConstant.PLAYER_5, "删除玩家缓存", jsonObject.toString(), ip);
	}

	/**
	 * 玩家背包物品查询
	 */
	public void showPlayerBackpack(MessageObj msgObj, Connection connection) throws Exception {

		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));

		String gameSite = param.getString("gameSite"); 
		Long playerID = param.getLong("playerID"); 

		String url = baseDataService.getUrlByGameSitePath(gameSite, PathConstant.PLAYER);

		GameCCEventListener eventListener = new GameCCEventListener(connection, gameSite, true) {
			StringBuffer resultData = new StringBuffer();
			@Override
			public void onResponseContent(Buffer content) throws IOException {

				String result = content.toString("UTF-8");
				resultData.append(result);
				try {
					JSONObject json = new JSONObject(resultData.toString());
					MessageObj resultMsg = new MessageObj(OptTypeConstant.PLAYER_6, json.toString().getBytes("UTF8"));
					gameCCSocketService.sendData(getConnection(), resultMsg);
				} catch (Exception e) {
					
				}
			}
		};

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("optType", OptTypeConstant.PLAYER_6);
		jsonObject.put("playerID", playerID);

		webService.sendDateForWeb(jsonObject, url, eventListener);

	}

	/**
	 * 玩家登陆日志查询
	 */
	public void showPlayerLoginLog(MessageObj msgObj, Connection connection) throws Exception {

		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));

		String gameSite = param.getString("gameSite"); 
		Integer playerID = param.getInt("playerID"); 

		String url = baseDataService.getUrlByGameSitePath(gameSite, PathConstant.PLAYER);

		GameCCEventListener eventListener = new GameCCEventListener(connection, gameSite, true) {
			StringBuffer resultData = new StringBuffer();
			@Override
			public void onResponseContent(Buffer content) throws IOException {

				String result = content.toString("UTF-8");
				resultData.append(result);
				try {
					JSONObject json = new JSONObject(resultData.toString());
					MessageObj resultMsg = new MessageObj(OptTypeConstant.PLAYER_7, json.toString().getBytes("UTF8"));
					gameCCSocketService.sendData(getConnection(), resultMsg);
				} catch (Exception e) {
					
				}
			}
		};

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("optType", OptTypeConstant.PLAYER_7);
		jsonObject.put("playerID", playerID);

		webService.sendDateForWeb(jsonObject, url, eventListener);

	}

	/**
	 * 玩家消费日志查询
	 */
	public void showPlayerBuyLog(MessageObj msgObj, Connection connection) throws Exception {

		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));

		String gameSite = param.getString("gameSite"); 
		Integer playerID = param.getInt("playerID"); 

		String url = baseDataService.getUrlByGameSitePath(gameSite, PathConstant.PLAYER);

		GameCCEventListener eventListener = new GameCCEventListener(connection, gameSite, true) {
			StringBuffer resultData = new StringBuffer();
			@Override
			public void onResponseContent(Buffer content) throws IOException {

				String result = content.toString("UTF-8");
				resultData.append(result);
				try {
					JSONObject json = new JSONObject(resultData.toString());
					MessageObj resultMsg = new MessageObj(OptTypeConstant.PLAYER_8, json.toString().getBytes("UTF8"));
					gameCCSocketService.sendData(getConnection(), resultMsg);
				} catch (Exception e) {
					
				}
			}
		};

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("optType", OptTypeConstant.PLAYER_8);
		jsonObject.put("playerID", playerID);

		webService.sendDateForWeb(jsonObject, url, eventListener);

	}

	/**
	 * 玩家充值日志查询
	 */
	public void showPlayerRechargeLog(MessageObj msgObj, Connection connection) throws Exception {

		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));

		String gameSite = param.getString("gameSite"); 
		Integer playerID = param.getInt("playerID"); 

		String url = baseDataService.getUrlByGameSitePath(gameSite, PathConstant.PLAYER);

		GameCCEventListener eventListener = new GameCCEventListener(connection, gameSite, true) {
			StringBuffer resultData = new StringBuffer();
			@Override
			public void onResponseContent(Buffer content) throws IOException {

				String result = content.toString("UTF-8");
				resultData.append(result);
				try {
					JSONObject json = new JSONObject(resultData.toString());
					MessageObj resultMsg = new MessageObj(OptTypeConstant.PLAYER_9, json.toString().getBytes("UTF8"));
					gameCCSocketService.sendData(getConnection(), resultMsg);
				} catch (Exception e) {
					
				}
			}
		};

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("optType", OptTypeConstant.PLAYER_9);
		jsonObject.put("playerID", playerID);

		webService.sendDateForWeb(jsonObject, url, eventListener);

	}
	
	/**
	 * 查询玩家
	 */
	public void searchPlayerInfo(MessageObj msgObj, Connection connection) throws Exception {

		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));

		String gameSite = param.getString("gameSite"); 
		String searchType = param.getString("searchType"); 
		String searchValue = param.getString("searchValue"); 

		if (searchValue == null || "".equals(searchValue.trim())) {
			return;
		}

		String url = baseDataService.getUrlByGameSitePath(gameSite, PathConstant.PLAYER);

		GameCCEventListener eventListener = new GameCCEventListener(connection, gameSite, true) {
			StringBuffer resultData = new StringBuffer();
			@Override
			public void onResponseContent(Buffer content) throws IOException {

				String result = content.toString("UTF-8");
				resultData.append(result);
				try {
					JSONObject json = new JSONObject(resultData.toString());
					MessageObj resultMsg = new MessageObj(OptTypeConstant.PLAYER_10, json.toString().getBytes("UTF8"));
					gameCCSocketService.sendData(getConnection(), resultMsg);
				} catch (Exception e) {
					
				}
			}
		};

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("optType", OptTypeConstant.PLAYER_10);
		jsonObject.put("searchType", searchType);
		jsonObject.put("searchValue", searchValue);

		webService.sendDateForWeb(jsonObject, url, eventListener);

	}

	/** 
	 * 修改玩家类型 
	 */
	public void updatePlayerType(MessageObj msgObj, Connection connection) throws Exception {

		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));

		String gameSite = param.getString("gameSite"); 
		Integer playerID = param.getInt("playerID"); 
		Integer optType = param.getInt("optType"); 
		Integer playerType = param.getInt("playerType"); 
		String name = param.getString("name"); 
		String typeName = param.getString("typeName"); 

		String url = baseDataService.getUrlByGameSitePath(gameSite, PathConstant.PLAYER);

		GameCCEventListener eventListener = new GameCCEventListener(connection, gameSite, true) {
			@Override
			public void onResponseContent(Buffer content) throws IOException {

				String result = content.toString("UTF-8");

				MessageObj resultMsg = new MessageObj(OptTypeConstant.PLAYER_15, result.getBytes("UTF8"));
				gameCCSocketService.sendData(getConnection(), resultMsg);

			}
		};

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("optType", OptTypeConstant.PLAYER_15);
		jsonObject.put("playerID", playerID);
		jsonObject.put("playerType", playerType);

		webService.sendDateForWeb(jsonObject, url, eventListener);

		IUserService userService = GCCContext.getInstance().getServiceCollection().getUserService();
		String userName = userService.getUserbyID(connection.getUserID()).getUserName();
		String detail = userName + "把【" +name + "】" + "修改为" + typeName;

		ILogService logService = GCCContext.getInstance().getServiceCollection().getLogService();
		logService.recordOptLog(connection.getUserID(), userName, optType, userName + "修改了玩家类型", detail, connection.getHostAddress());

	}

	/**
	 * 禁言
	 */
	public void banTalk(MessageObj msgObj, Connection connection) throws Exception {

		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));

		String gameSite = param.getString("gameSite"); 
		Integer playerID = param.getInt("playerID"); 
		Integer banTime = param.getInt("banTime"); 

		String url = baseDataService.getUrlByGameSitePath(gameSite, PathConstant.PLAYER);

		GameCCEventListener eventListener = new GameCCEventListener(connection, gameSite, true) {
			@Override
			public void onResponseContent(Buffer content) throws IOException {

				String result = content.toString("UTF-8");

				MessageObj resultMsg = new MessageObj(OptTypeConstant.PLAYER_17, result.getBytes("UTF8"));
				gameCCSocketService.sendData(getConnection(), resultMsg);

			}
		};

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("optType", OptTypeConstant.PLAYER_17);
		jsonObject.put("playerID", playerID);
		jsonObject.put("banTime", banTime);

		webService.sendDateForWeb(jsonObject, url, eventListener);

	}

	/**
	 * 解禁言
	 */
	public void unBanTalk(MessageObj msgObj, Connection connection) throws Exception {

		JSONObject param = new JSONObject(new String(msgObj.getData(),"UTF-8"));

		String gameSite = param.getString("gameSite"); 
		Integer playerID = param.getInt("playerID"); 

		String url = baseDataService.getUrlByGameSitePath(gameSite, PathConstant.PLAYER);

		GameCCEventListener eventListener = new GameCCEventListener(connection, gameSite, true) {
			@Override
			public void onResponseContent(Buffer content) throws IOException {

				String result = content.toString("UTF-8");

				MessageObj resultMsg = new MessageObj(OptTypeConstant.PLAYER_18, result.getBytes("UTF8"));
				gameCCSocketService.sendData(getConnection(), resultMsg);

			}
		};

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("optType", OptTypeConstant.PLAYER_18);
		jsonObject.put("playerID", playerID);

		webService.sendDateForWeb(jsonObject, url, eventListener);

	}


}
