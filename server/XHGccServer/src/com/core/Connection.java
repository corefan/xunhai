package com.core;

import java.io.Serializable;


import com.util.LogUtil;

import io.netty.channel.Channel;


/**
 * @author ken
 * 2013-10-30
 * socket连接封装类
 */
public class Connection implements Serializable {

	private static final long serialVersionUID = -3325831226110985184L;
	
	/** 连接编号 */
	private Integer conID;
	/** 用户编号 */
    private Integer userID;
    /** nio抽象连接 */
    private Channel con; 
    /** 连接状态 */
    private ConnectionState state;
    /** 连接地址 */
    private String hostAddress;
    /** 异常信息 */
    private StringBuffer exceptionStr;

    /** 连接ip*/
    private String connectIP;
    
    /**
     * 连接状态
     */
    public enum ConnectionState {
        CONNECTED, AUTHOR, INGAME, EXIT
    }

    public Integer getConID() {
		return conID;
	}

	public void setConID(Integer conID) {
		this.conID = conID;
	}

	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	public ConnectionState getState() {
        return state;
    }

    public void setState(ConnectionState state) {
        this.state = state;
    }

    public String getHostAddress() {
        return hostAddress;
    }
    
    public void destroy() {
        try {
            getCon().close();
        } catch (Exception e) {
            LogUtil.error("destroy connection occur error ", e);
        }
    }

    public boolean isClosed() {
        return !getCon().isOpen();
    }

    public boolean isInGame() {
        return getState() == ConnectionState.INGAME || getConID() != null;
    }

	public Channel getCon() {
		return con;
	}

	public void setCon(Channel con) {
		this.con = con;
	}

	public void setHostAddress(String hostAddress) {
		this.hostAddress = hostAddress;
	}

	public StringBuffer getExceptionStr() {
		return exceptionStr;
	}

	public void setExceptionStr(StringBuffer exceptionStr) {
		this.exceptionStr = exceptionStr;
	}

	public Connection(Integer conID, Channel con) {
		this.conID = conID;
		this.con = con;
	}
	
	public Connection(Channel con) {
		this.con = con;
		this.state = ConnectionState.CONNECTED;
		this.exceptionStr = new StringBuffer();
	}

	public String getConnectIP() {
		return connectIP;
	}

	public void setConnectIP(String connectIP) {
		this.connectIP = connectIP;
	}

}
