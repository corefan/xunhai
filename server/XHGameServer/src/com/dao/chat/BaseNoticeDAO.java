package com.dao.chat;

import java.util.List;

import com.db.BaseSqlSessionTemplate;
import com.domain.chat.BaseNotice;

public class BaseNoticeDAO extends BaseSqlSessionTemplate{
	
	/** 系统公告配置 */
	public List<BaseNotice> listBaseNotice(){
		String sql = "select * from notice";
		return this.selectList(sql, BaseNotice.class);
	}
}
