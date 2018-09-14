package cn.ibrother.tools.data.common;

/**
 * 辅助方法类
 * @author oyxz
 * @since 2011-10-13
 * @version 1.0
 */
import java.io.File;

public class Helper {	
	public static String getConfigPath(String sFileName) {
		return System.getProperty("user.dir") + File.separator + 
			Constants.CONFIG_PATH + File.separator + sFileName;
	}
	
}
