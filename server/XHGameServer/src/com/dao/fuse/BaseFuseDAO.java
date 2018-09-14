package com.dao.fuse;

import java.util.List;

import com.db.BaseSqlSessionTemplate;
import com.domain.fuse.BaseCompose;

/**
 * 物品合成规则
 * @author jiangqin
 * @date 2017-3-23
 */
public class BaseFuseDAO extends BaseSqlSessionTemplate{
	
	
	/** 取合成配置表 */
	public List<BaseCompose> listBaseCompose(){
		String baseComposeSql = "select * from compose";
		return this.selectList(baseComposeSql, BaseCompose.class);
	}
	
}
