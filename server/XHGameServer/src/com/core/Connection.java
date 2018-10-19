package com.core;

import io.netty.channel.Channel;

import java.io.Serializable;

import com.util.LogUtil;


/**
 * 2013-10-30
 * socket连接封装类
 */
public class Connection implements Serializable {

	private static final long serialVersionUID = -3325831226110985184L;
	
	/** 账号编号 */
    private long userId;
    /** 玩家编号 */
    private long playerId;
    /** nio抽象连接 */
    private Channel netty4;
    /** 连接状态 */
    private ConnectionState state;
    
    private boolean reconnectd;
    
    /** 空闲开始时间 */
    private long freeStartTime;
    
    /** 连接ip*/
    private String connectIP;
    
    /** 连接服务器编号*/
    private int serverNo;
    
    /**
     * 连接状态(已连接  登录  游戏中  退出)
     */
    public enum ConnectionState {
        CONNECTED, LOGIN, INGAME, EXIT
    }
    

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public ConnectionState getState() {
        return state;
    }

    public void setState(ConnectionState state) {
        this.state = state;
    }

    public void destroy() {
        try {
        	getNetty4().close();
        } catch (Exception e) {
            LogUtil.error("destroy connection occur error ", e);
        }
    }

    public boolean isClosed() {
    	return !getNetty4().isOpen();
    }

	public Channel getNetty4() {
		return netty4;
	}

	public void setNetty4(Channel netty4) {
		this.netty4 = netty4;
	}

	
	public Connection(Channel con) {
		this.netty4 = con;
		this.state = ConnectionState.CONNECTED;
	}


	public long getFreeStartTime() {
		return freeStartTime;
	}

	public void setFreeStartTime(long freeStartTime) {
		this.freeStartTime = freeStartTime;
	}

	public boolean isReconnectd() {
		return reconnectd;
	}

	public void setReconnectd(boolean reconnectd) {
		this.reconnectd = reconnectd;
	}

	public String getConnectIP() {
		return connectIP;
	}

	public void setConnectIP(String connectIP) {
		this.connectIP = connectIP;
	}

	public int getServerNo() {
		return serverNo;
	}

	public void setServerNo(int serverNo) {
		this.serverNo = serverNo;
	}
 
}
