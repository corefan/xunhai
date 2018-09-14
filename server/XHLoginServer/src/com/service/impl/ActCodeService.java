package com.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.common.CacheService;
import com.constant.CacheConstant;
import com.dao.ActCodeDAO;
import com.domain.ActCode;
import com.service.IActCodeService;

/**
 * 激活码
 * @author ken
 * @date 2018年8月9日
 */
public class ActCodeService implements IActCodeService {
	
	private ActCodeDAO ActCodeDAO = new ActCodeDAO();
	
	@Override
	public void initCache() {
		Map<String, ActCode> codeMap = new ConcurrentHashMap<String, ActCode>(1 << 22);
		List<ActCode> codeList = ActCodeDAO.listActCodes();
		for (ActCode code :codeList) {
			codeMap.put(code.getCode(), code);
		}
		
		CacheService.putToCache(CacheConstant.ACTCODE_CAHCE, codeMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ActCode getActCodeByCode(String code) {
		Map<String, ActCode> codeMap = (Map<String, ActCode>) CacheService.getFromCache(CacheConstant.ACTCODE_CAHCE);
		return codeMap.get(code);
	}

	@Override
	public void updateActCode(ActCode actCode) {

		ActCodeDAO.updateActCode(actCode);
	}

}
