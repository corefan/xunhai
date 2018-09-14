package com.domain;


/**
 * 2013-11-4
 * 系统异常
 */
public class GameException extends RuntimeException {

	private static final long serialVersionUID = -1207966546445713442L;
	/** 错误码 */
	private int codeID;

	public GameException() {
	}

	public GameException(Throwable ex) {
		super(ex);
	}
	
	public GameException(int codeID) {
		this.codeID = codeID;
	}
	
	public int getCodeID() {
		return codeID;
	}

	public void setCodeID(int codeID) {
		this.codeID = codeID;
	}

}
