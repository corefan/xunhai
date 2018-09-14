package com.util;

/**
 * @author ken
 * 2014-5-20
 * 数据分析	
 */
public class DataAnalysisUtil {

	/** 根据游戏节点编号得到节点名字 */
	public static String getStepName(int gameStep) {
		
		switch (gameStep) {
		case 10000:
			return "进入小圈圈";
		case 10010:
			return "连接socket";
		case 10020:
			return "选择职业";
		case 10030:
			return "进入游戏加载";
		case 10040:
			return "进入新手副本";
		case 10050:
			return "镜头移动到女神";
		case 10060:
			return "获得第一个技能";
		case 10070:
			return "释放第一个技能完毕";
		case 10080:
			return "boss攻击";
		case 10090:
			return "boss召唤";
		case 10100:
			return "释放大招";
		case 10110:
			return "boss消失";
		case 10120:
			return "走向女神";
		case 10130:
			return "女神消失";
		case 10140:
			return "进入内城";
		}
		
		return "未知节点";
	}
}
