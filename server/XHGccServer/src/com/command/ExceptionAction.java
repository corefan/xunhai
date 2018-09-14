package com.command;

import java.io.UnsupportedEncodingException;

import com.common.GCCContext;
import com.common.GameCCSocketService;
import com.constant.OptTypeConstant;
import com.core.Connection;
import com.domain.MessageObj;
import com.util.LogUtil;

/**
 * 异常
 * @author ken
 *
 */
public class ExceptionAction {
	
	GameCCSocketService gameCCSocketService = GCCContext.getInstance().getServiceCollection().getGameCCSocketService();
	
	/**
	 * 发送异常信息
	 */
	public void sendException(Exception exception, Connection connection) {
		
		MessageObj resultMsg;
		try {
			resultMsg = new MessageObj(OptTypeConstant.EXCEPTION, exception.getMessage().getBytes("UTF8"));
			gameCCSocketService.sendData(connection, resultMsg);
		} catch (UnsupportedEncodingException e) {
			LogUtil.error("异常:",e);
			e.printStackTrace();
		}
	}

}
