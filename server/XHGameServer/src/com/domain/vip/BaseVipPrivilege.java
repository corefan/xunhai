package com.domain.vip;

import java.io.Serializable;

/**
 * vip 特权基础数据
 * @author jiangqin
 * @date 2017-6-15
 */
public class BaseVipPrivilege implements Serializable{

	private static final long serialVersionUID = 5202756188419121778L;
	
	/**VIP特权ID*/
	private int id;		
	/**VIP1*/
	private int vip1;	
	/**VIP2*/
	private int vip2;	
	/**VIP3*/
	private int vip3;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getVip1() {
		return vip1;
	}
	public void setVip1(int vip1) {
		this.vip1 = vip1;
	}
	public int getVip2() {
		return vip2;
	}
	public void setVip2(int vip2) {
		this.vip2 = vip2;
	}
	public int getVip3() {
		return vip3;
	}
	public void setVip3(int vip3) {
		this.vip3 = vip3;
	}	
}
