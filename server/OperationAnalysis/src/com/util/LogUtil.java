package com.util;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * 日志工具类
 *
 */
public final class LogUtil {

	private LogUtil() {

	}

	public static void debug(String message) {
		if (isDebug()) {
			getDebugLogger().debug(message);
		}
	}

	public static void debug(String message, Throwable throwable) {
		if (isDebug()) {
			getDebugLogger().debug(message, throwable);
		}
	}

	public static void debug(Throwable throwable) {
		if (isDebug()) {
			getDebugLogger().debug(throwable.getMessage(), throwable);
		}
	}

	public static void info(String message) {
		if (isInfo()) {
			getInfoLogger().info(message);
		}
	}

	public static void info(String message, Throwable throwable) {
		if (isInfo()) {
			getInfoLogger().info(message, throwable);
		}
	}

	public static void info(Throwable throwable) {
		if (isInfo()) {
			getInfoLogger().info(throwable.getMessage(), throwable);
		}
	}

	public static void warn(String message) {
		if (isWarn()) {
			getWarnLogger().warn(message);
		}
	}

	public static void warn(String message, Throwable throwable) {
		if (isWarn()) {
			getWarnLogger().warn(message, throwable);
		}
	}

	public static void warn(Throwable throwable) {
		if (isWarn()) {
			getWarnLogger().warn(throwable.getMessage(), throwable);
		}
	}

	public static void error(String message) {
		if (isError()) {
			getErrorLogger().error(message);
		}
	}

	public static void error(String message, Throwable throwable) {
		if (isError()) {
			getErrorLogger().error(message, throwable);
		}
	}

	public static void error(Throwable throwable) {
		if (isError()) {
			getErrorLogger().error(throwable.getMessage(), throwable);
		}
	}

	public static void fatal(String message) {
		if (isFatal()) {
			getFatalLogger().fatal(message);
		}
	}

	public static void fatal(String message, Throwable throwable) {
		if (isFatal()) {
			getFatalLogger().fatal(message, throwable);
		}
	}

	public static void fatal(Throwable throwable) {
		if (isFatal()) {
			getFatalLogger().fatal(throwable.getMessage(), throwable);
		}
	}

	private static Logger getDebugLogger() {
		return Logger.getLogger("debugLogger");
	}

	private static Logger getInfoLogger() {
		return Logger.getLogger("infoLogger");
	}

	private static Logger getWarnLogger() {
		return Logger.getLogger("warnLogger");
	}

	private static Logger getErrorLogger() {
		return Logger.getLogger("errorLogger");
	}

	private static Logger getFatalLogger() {
		return Logger.getLogger("fatalLogger");
	}

	private static Boolean isDebug() {
		return Logger.getRootLogger().getLevel().toInt() <= Level.DEBUG_INT;
	}

	private static Boolean isInfo() {
		return Logger.getRootLogger().getLevel().toInt() <= Level.INFO_INT;
	}

	private static Boolean isWarn() {
		return Logger.getRootLogger().getLevel().toInt() <= Level.WARN_INT;
	}

	private static Boolean isError() {
		return Logger.getRootLogger().getLevel().toInt() <= Level.ERROR_INT;
	}

	private static Boolean isFatal() {
		return Logger.getRootLogger().getLevel().toInt() <= Level.FATAL_INT;
	}

}
