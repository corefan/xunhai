package com.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dao.*;

public class DAOFactory {

	private static ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

	/**
	 * 获取UserDAO
	 * @return
	 */
	public static UserDAO getUserDAO() {
		return (UserDAO) context.getBean("userDAO");
	}

	/**
	 * 获取BaseDataDAO
	 * @return
	 */
	public static BaseDataDAO getBaseDataDAO() {
		return (BaseDataDAO) context.getBean("baseDataDAO");
	}
	
	/**
	 * 获取BaseDataDAO
	 * @return
	 */
	public static PayAnalysisDAO getPayAnalysisDAO() {
		return (PayAnalysisDAO) context.getBean("payAnalysisDAO");
	}
	
	/**
	 * 获取BehaviorDAO
	 * @return
	 */
	public static BehaviorAnalysisDAO getBehaviorAnalysisDAO() {
		return (BehaviorAnalysisDAO) context.getBean("behaviorAnalysisDAO");
	}
	
	/**
	 * 获取BatchExcuteDAO
	 * @return
	 */
	public static BatchExcuteDAO getBatchExcuteDAO() {
		return (BatchExcuteDAO) context.getBean("batchExcuteDAO");
	}
	
	public static OpenDataDAO getOpenDataDAO() {
		return (OpenDataDAO) context.getBean("openDataDAO");
	}
}
