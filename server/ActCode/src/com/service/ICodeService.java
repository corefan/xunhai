package com.service;

import org.json.JSONObject;

import com.domain.ActCode;

/**
 * @author barsk
 * 2014-5-10
 * 激活码	
 */
public interface ICodeService {

	/** 初始化激活码 */
	public void initActCodeCache();
	
	/** 得到激活码 */
	public ActCode getActCodeByCode(String code);

	/** 更新激活码 */
	public void updateActCode(ActCode actCode);
	
	/** 使用激活码 */
	public JSONObject useCode(Integer playerID, String site, String code) throws Exception;
	
}
