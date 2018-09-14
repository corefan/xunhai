package com.util;

import java.util.HashMap;
import java.util.Map;

import com.domain.PayLog;



/**
 * 唯一ID生成器
 * @author ken
 * @date 2017-7-17
 */
public class IDUtil {

	private static int MAX_SEQ = 32767;
	private static Map<String, IDUtil> ids = new HashMap<>();
	private static byte[] glock = new byte[0];
	private static long time = 1500296019093l;

	private long lastTime;// 上次记录的秒数
	private long realTime;
	private int seq = 0;// 自增序列
	private long worldID; //服务器编号
	private byte[] lock = new byte[0];

	public IDUtil(long time) {
		lastTime = (System.currentTimeMillis() - time) / 1000;
		realTime = lastTime << 15;
		int random = (int)(Math.random() * 100);
		worldID = ((long) random) << 44;
	}
	
	public static long geneteId(Class<?> claz) {
		return getIDUtil(claz.getName()).getID();
	}
	
	private static IDUtil getIDUtil(String className) {
		IDUtil util = ids.get(className);
		if (util == null) {
			synchronized (glock) {
				util = ids.get(className);
				if (util == null) {
					util = new IDUtil(time);
					ids.put(className, util);
				}
			}
		}
		return util;
	}

	private long getID() {
		int num;
		long time;
		synchronized (lock) {
			num = ++seq;
			time = realTime;
			if (seq >= MAX_SEQ) {
				lastTime++;
				realTime = lastTime << 15;
				seq = 0;
			}
			return worldID | time | num;
		}

	}
	
	public static void main(String[] args) {
		for(int i = 0; i< 100; i++){
			System.out.println(geneteId(PayLog.class));
		}
		
		//System.out.println(3223423423l | 0 | 0);
//		System.out.println((175904268369100900l << 48));
//		
//		System.out.println((new Date().getTime() - 1500296019093l) /1000);
	}
}
