package com.core.jedis;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import com.util.LogUtil;

/**
 * 读取redis配置文件
 * @author ken
 * @date 2018年3月22日
 */
public class RedisPoolService {
	
	private Properties prop = null;
	
	private RedisPoolService() {
		
		try {
			prop = new Properties();
			
			File projectFolder = new File(Thread.currentThread().getContextClassLoader().getResource("").toString().replace("%20", " "));
			projectFolder = projectFolder.getParentFile();
			String projectFolderPath = projectFolder.getPath();
			String projectFolderName = projectFolderPath.substring(5, projectFolderPath.length());
			
			File file = new File(projectFolderName+File.separator+"conf"+File.separator+"redis.properties");
			FileInputStream fis = new FileInputStream(file);
			prop.load(fis);
			
		} catch (Exception e) {
			LogUtil.error("读取redis配置异常", e);
		}
	}
	
	
	public static RedisPoolService getInstance (){
		return new RedisPoolService();
	}
	
	public String getValue(String key){
		return prop.getProperty(key);
	}
	
	
}
