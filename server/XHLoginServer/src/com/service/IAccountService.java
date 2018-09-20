package com.service;

import java.util.Map;

import com.domain.Account;

/**
 * 账号系统
 * @author ken
 * @date 2017-6-22
 */
public interface IAccountService {

	/**
	 * 初始账号
	 */
	void initCache();
	
	/**
	 * 账号缓存
	 */
	Map<String, Account> getAccountMap();
	
	/**
	 * 创建账号
	 */
	Account createAccount(long userId, String userName, String passWord, String telephone, int tourist, int appId);
	
	/**
	 * 更新账号
	 */
	void updateAccount(Account account);
	
	/**
	 * 根据账号名称取记录
	 */
	Account getAccountByUserName(String userName);
	
	/**
	 * 根据账号编号取记录
	 */
	Account getAccountByUserId(long userId);
	
	/**
	 * 根据手机号码取账号
	 */
	Account getAccountByTelephone(String telephone);
}
