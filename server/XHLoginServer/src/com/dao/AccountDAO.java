package com.dao;

import java.util.List;

import com.db.GccSqlSessionTemplate;
import com.domain.Account;

/**
 * 账号DAO
 * @author ken
 * @date 2017-6-22
 */
public class AccountDAO extends GccSqlSessionTemplate {

	/**
	 * 创建账号
	 * */
	public void createAccount(Account account) {
		this.insert_noreturn(account.getInsertSql());
	}
	
	/**
	 * 更新账号
	 */
	public void updateAccount(Account account) {
		this.update(account.getUpdateSql());
	}
	 
	/**
	 * 取账号
	 */
	public List<Account> listAccounts(){
		String sql = "select * from account";
		return this.selectList(sql, Account.class);
	}
}
