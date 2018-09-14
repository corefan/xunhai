package com.service;

import com.domain.ActCode;

/**
 * 激活码
 * @author ken
 * @date 2018年8月9日
 */
public interface IActCodeService {

	/** 初始化激活码 */
	public void initCache();
	
	/** 得到激活码 */
	public ActCode getActCodeByCode(String code);

	/** 更新激活码 */
	public void updateActCode(ActCode actCode);
	
}
