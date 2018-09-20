package com.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.common.CacheService;
import com.common.DateService;
import com.constant.CacheConstant;
import com.dao.AccountDAO;
import com.domain.Account;
import com.service.IAccountService;
import com.util.IDUtil;
import com.util.LogUtil;

/**
 * 账号系统
 * @author ken
 * @date 2017-6-22
 */
public class AccountService implements IAccountService {

	private AccountDAO accountDAO = new AccountDAO();
	
	@Override
	public void initCache() {
		Map<String, Account> map = new ConcurrentHashMap<String, Account>();
		List<Account> lists = accountDAO.listAccounts();
		for(Account model : lists){
			map.put(model.getUserName(), model);
		}
		CacheService.putToCache(CacheConstant.ACCOUNT_CAHCE, map);
	}

	/**
	 * 取账号集合
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Account> getAccountMap(){
		return (Map<String, Account>)CacheService.getFromCache(CacheConstant.ACCOUNT_CAHCE);
	}
	
	@Override
	public Account createAccount(long userId, String userName, String passWord,
			String telephone, int tourist, int appId) {
		
		try {
			if(userId == 0){
				userId = IDUtil.geneteId(Account.class);
			}
			Account account = new Account();
			account.setUserId(userId);
			account.setUserName(userName);
			account.setPassWord(passWord);
			account.setTelephone(telephone);
			account.setTourist(tourist);
			account.setAppId(appId);
			account.setCreateTime(DateService.getCurrentUtilDate());
			
			accountDAO.createAccount(account);
			
			this.getAccountMap().put(userName, account);
			
			return account;
		} catch (Exception e) {
			LogUtil.error("注册账号异常：", e);
		}
		return null;
	}

	@Override
	public void updateAccount(Account account) {
		
		try {
			accountDAO.updateAccount(account);
		} catch (Exception e) {
			LogUtil.error("更新账号异常：", e);
		}
	}

	@Override
	public Account getAccountByUserName(String userName) {
		return this.getAccountMap().get(userName);
	}

	@Override
	public Account getAccountByUserId(long userId) {
		Map<String, Account> map = this.getAccountMap();
		for(Map.Entry<String, Account> entry : map.entrySet()){
			Account account = entry.getValue();
			if(account.getUserId() == userId){
				return account;
			}
		}
		return null;
	}

	@Override
	public Account getAccountByTelephone(String telephone) {
		Map<String, Account> map = this.getAccountMap();
		for(Map.Entry<String, Account> entry : map.entrySet()){
			Account account = entry.getValue();
			if(telephone.equals(account.getTelephone())){
				return account;
			}
		}
		return null;
	}

}
