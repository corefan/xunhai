package com.domain;

import java.io.Serializable;

/**
 * 游戏配置变量
 * @author ken
 * @date 2016-12-20
 */
public class BaseServerConfig implements Serializable {
	
	private static final long serialVersionUID = -4186327621242344372L;
	
	/**游戏服编号*/
	private int  serverNo;
	/**游戏服名称*/
	private String serverName;
	/**代理商*/
	private String agent;
	/**游戏站点*/
	private String gameSite;
	
	/**主机ＩＰ*/	
	private String gameHost;
	/**主机内网ＩＰ*/
	private String gameInnerIp;
	/**游戏开放端口*/
	private int gamePort;
	/**web服务端口*/
	private int webPort;
	
	/**资源地址*/
	private String assets;

	/**开服日期*/
	private String openServerDate;
	/** 合服时间*/
	private String megerServerDate;
	
	/** 服务器是否开启(1.开启 0.关闭) */
	private int state;
	/** 服务器状态(0.测试 1.流畅2：拥挤3.火爆  4.维护中 5.关闭) */
	private int severState;
	/** 服务器类型(0.普通 1.新服 2.推荐) */
	private int severType;
	
	/** 插入sql*/
	public String getInsertSql(){
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO config_server(serverNo, serverName, agent, gameSite, gameHost,gameInnerIp,gamePort,webPort,assets,openServerDate,megerServerDate,state,severState,severType) VALUES(");
		sb.append(serverNo);
		sb.append(",");
		sb.append("'").append(serverName).append("'");
		sb.append(",");
		sb.append("'").append(agent).append("'");
		sb.append(",");
		sb.append("'").append(gameSite).append("'");
		sb.append(",");
		sb.append("'").append(gameHost).append("'");
		sb.append(",");
		sb.append("'").append(gameInnerIp).append("'");
		sb.append(",");
		sb.append(gamePort);
		sb.append(",");
		sb.append(webPort);
		sb.append(",");
		sb.append("'").append(assets).append("'");
		sb.append(",");
		sb.append("'").append(openServerDate).append("'");
		sb.append(",");
		sb.append("'").append(megerServerDate).append("'");	
		sb.append(",");
		sb.append(state);
		sb.append(",");
		sb.append(severState);
		sb.append(",");
		sb.append(severType);
		sb.append(")");
		return sb.toString();
	}
	
	/**
	 * 得到更新sql
	 * */
	public String getUpdateSql(String oleGameSite) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("UPDATE config_server SET ");
		sb.append("serverNo = ");
		sb.append(serverNo);
		sb.append(",");
		sb.append("serverName = ");
		sb.append("'");
		sb.append(serverName);
		sb.append("'");
		sb.append(",");
		sb.append("agent = ");
		sb.append("'");
		sb.append(agent);
		sb.append("'");
		sb.append(",");
		sb.append("gameSite = ");
		sb.append("'");
		sb.append(gameSite);
		sb.append("'");
		sb.append(",");
		sb.append("gameHost = ");
		sb.append("'");
		sb.append(gameHost);
		sb.append("'");
		sb.append(",");
		sb.append("gameInnerIp = ");
		sb.append("'");
		sb.append(gameInnerIp);
		sb.append("'");
		sb.append(",");
		sb.append("gamePort = ");
		sb.append(gamePort);
		sb.append(",");
		sb.append("webPort = ");
		sb.append(webPort);
		sb.append(",");
		sb.append("assets = ");
		if(assets == null){
			sb.append(assets);
		}else{
			sb.append("'");
			sb.append(assets);
			sb.append("'");
		}
		sb.append(",");
		sb.append("openServerDate = ");
		sb.append("'");
		sb.append(openServerDate);
		sb.append("'");
		sb.append(",");
		sb.append("megerServerDate = ");
		if(megerServerDate == null){
			sb.append(megerServerDate);
		}else{
			sb.append("'");
			sb.append(megerServerDate);
			sb.append("'");
		}
		sb.append(",");
		sb.append("state = ");
		sb.append(state);
		sb.append(",");
		sb.append("severState = ");
		sb.append(severState);
		sb.append(",");
		sb.append("severType = ");
		sb.append(severType);
		sb.append(" WHERE gameSite=");
		sb.append("'");
		sb.append(oleGameSite);
		sb.append("'");
		return sb.toString();
	}
	
	public int getServerNo() {
		return serverNo;
	}
	public void setServerNo(int serverNo) {
		this.serverNo = serverNo;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
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
	public String getGameHost() {
		return gameHost;
	}
	public void setGameHost(String gameHost) {
		this.gameHost = gameHost;
	}
	public String getGameInnerIp() {
		return gameInnerIp;
	}
	public void setGameInnerIp(String gameInnerIp) {
		this.gameInnerIp = gameInnerIp;
	}
	public int getGamePort() {
		return gamePort;
	}
	public void setGamePort(int gamePort) {
		this.gamePort = gamePort;
	}
	public int getWebPort() {
		return webPort;
	}
	public void setWebPort(int webPort) {
		this.webPort = webPort;
	}
	public String getAssets() {
		return assets;
	}
	public void setAssets(String assets) {
		this.assets = assets;
	}
	public String getOpenServerDate() {
		return openServerDate;
	}
	public void setOpenServerDate(String openServerDate) {
		this.openServerDate = openServerDate;
	}
	public String getMegerServerDate() {
		return megerServerDate;
	}
	public void setMegerServerDate(String megerServerDate) {
		this.megerServerDate = megerServerDate;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getSeverState() {
		return severState;
	}
	public void setSeverState(int severState) {
		this.severState = severState;
	}
	public int getSeverType() {
		return severType;
	}
	public void setSeverType(int severType) {
		this.severType = severType;
	}
}
