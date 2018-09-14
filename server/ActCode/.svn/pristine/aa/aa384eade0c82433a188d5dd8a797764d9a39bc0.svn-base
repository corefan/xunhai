package com.db;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;


import com.util.LogUtil;

public class DBConfigService {

	
	Properties prop = null;
	
	private DBConfigService() {
		try {
			prop = new Properties();
			File projectFolder = new File(Thread.currentThread().getContextClassLoader().getResource("").toString().replace("%20", " "));
			projectFolder = projectFolder.getParentFile();
			String projectFolderPath = projectFolder.getPath();
			String projectFolderName = projectFolderPath.substring(5, projectFolderPath.length());
			File file = new File(projectFolderName+File.separator+"conf"+File.separator+"db.properties");
			FileInputStream fis = new FileInputStream(file);
			prop.load(fis);
		} catch (Exception e) {
		    LogUtil.error("异常", e);
		}
	}
	
	public static DBConfigService getInstance() {
		return new DBConfigService();
	}
	
	public String getValue(String key) {
		return prop.getProperty(key);
	}
	
}
