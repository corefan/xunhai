package com.dao;

import java.util.List;
import java.util.Map;

import com.db.SqlSessionTemplate;
import com.domain.ActCode;
import com.domain.CodeUseLog;

/**
 * @author barsk
 * 2014-5-10
 * 激活码	
 */
public class CodeDAO extends SqlSessionTemplate {

	/**
	 * 更新激活码
	 * */
	public void updateActCode(ActCode actCode) {
		this.update(actCode.getUpdateSql());
	}
	
	/**
	 * 得到激活码列表
	 * */
	public List<ActCode> getCodeList() {
		return this.selectList("SELECT CODE_ID AS codeID, CODE AS code, SITE AS site, TYPE AS type, REWARD_ID AS rewardID, " +
				"TYPE_NUM AS typeNum, EXCLUSIVE AS exclusive FROM T_ACT_CODE WHERE STATE = 0", ActCode.class);
	}
	
	/**
	 * 创建激活码使用日志
	 * */
	public void createCodeLog(CodeUseLog log) {
		this.insert(log.getInsertSql());
	}
	
	/**
	 * 根据玩家编号，激活码类型得到使用日志
	 * */
	public boolean checkUseTypeCode(Integer playerID, String site, int type) {
		
		Map<String,Object> map = this.selectOne("SELECT * FROM T_CODE_USE_LOG WHERE PLAYER_ID = "+playerID+" AND SITE = '"+site+"' AND TYPE="+type+" LIMIT 0,1");
		
		return map == null ? false : true;
	}
	
}
