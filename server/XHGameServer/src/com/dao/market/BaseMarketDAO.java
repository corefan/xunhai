/**
 * 
 */
package com.dao.market;

import java.util.List;

import com.db.BaseSqlSessionTemplate;
import com.domain.market.BaseMarket;

/**
 * @author jiangqin
 * @date 2017-4-18
 */
public class BaseMarketDAO extends BaseSqlSessionTemplate{

	/**
	 * 商城基础配置
	 */
	public List<BaseMarket> listBaseMarket(){
		String sql = "select * from market";
		return this.selectList(sql, BaseMarket.class);
	}
	
}
