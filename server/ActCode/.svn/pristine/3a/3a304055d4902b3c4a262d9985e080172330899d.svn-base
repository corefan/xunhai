package com.common;


/**
 * @author barsk
 * 2013-11-4
 * 系统异常
 */
public class GameException extends RuntimeException {

	private static final long serialVersionUID = -1207966546445713442L;
	private int flag = 0;

	public GameException() {
	}

	public GameException(String str) {
		super(str);
	}
	
	public GameException(String str, int flag) {
		super(str);
		this.flag = flag;
	}

	public GameException(Throwable ex) {
		super(ex);
	}

	public int getFlag() {
		return flag;
	}

}
