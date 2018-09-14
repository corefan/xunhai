package com.common;

import java.nio.BufferOverflowException;

import com.core.Connection;
import com.domain.MessageObj;
import com.util.LogUtil;

import io.netty.channel.Channel;

/**
 * @author ken
 * 2013-10-30
 */
public class GameCCSocketService {


	/**
	 * 发送数据
	 */
	public void sendData(Channel con, MessageObj msg) {
		try {
			if (con != null && con.isOpen()) {
				con.writeAndFlush(msg.getBuf().retain());
			}
		} catch (BufferOverflowException e) {
			LogUtil.error("Buffer Overflow : ", e);
		} catch (Exception e) {
			LogUtil.error("Write data occur error : ", e);
		}
	}

	/**
	 * 发送数据
	 */
	public void sendData(Connection con, MessageObj msg) {
		sendData(con.getCon(), msg);

		msg.clear();
		con.getExceptionStr().setLength(0);
		msg = null;
	}

}
