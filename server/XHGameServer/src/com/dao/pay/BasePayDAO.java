package com.dao.pay;

import java.util.List;

import com.db.BaseSqlSessionTemplate;
import com.domain.pay.BasePay;

/**
 * 充值DAO
 * @author ken
 * @date 2017-6-23
 */
public class BasePayDAO extends BaseSqlSessionTemplate {

	/**
	 * 充值配置
	 */
	public List<BasePay> listBasePays(){
		String sql = "select * from charge";
		return this.selectList(sql, BasePay.class);
	}
}
