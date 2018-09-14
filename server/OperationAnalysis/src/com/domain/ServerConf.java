package com.domain;


/**
 * @author Administrator 服务器配置
 */
public class ServerConf {

	private int gameID;
	
	/** 代理商 */
	private String agent;
	
	/** 游戏站点 */
	private String gameSite;
	
	/** 开始时间 */
	private String openServerDate;
	
	/** 游戏区名称 */
	private String serverName;
	
	/** 服务器状态*/
	private int state;

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getGameSite() {
		return gameSite;
	}

	public void setGameSite(String gameSite) {
		this.gameSite = gameSite;
	}

	public String getOpenServerDate() {
		return openServerDate;
	}

	public void setOpenServerDate(String openServerDate) {
		this.openServerDate = openServerDate;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getGameID() {
		return gameID;
	}

	public void setGameID(int gameID) {
		this.gameID = gameID;
	}

}
