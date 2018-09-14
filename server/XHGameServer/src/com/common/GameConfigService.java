package com.common;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

import com.util.LogUtil;

public class GameConfigService {

	private Properties prop = null;
	
	private GameConfigService() {
		try {
			prop = new Properties();
			
			File projectFolder = new File(Thread.currentThread().getContextClassLoader().getResource("").toString().replace("%20", " "));
			projectFolder = projectFolder.getParentFile();
			String projectFolderPath = projectFolder.getPath();
			String projectFolderName = projectFolderPath.substring(5, projectFolderPath.length());
			
			Config.HOT_BIN_DIR = projectFolderName+File.separator+"hotbin"+File.separator;
			
			File file = new File(projectFolderName+File.separator+"conf"+File.separator+"game_config.properties");
			FileInputStream fis = new FileInputStream(file);
			prop.load(fis);
			
			// 配置Log4j
			PropertyConfigurator.configure(projectFolderName+File.separator+"conf"+File.separator + "log4j.properties");
		} catch (Exception e) {
			LogUtil.error(e);
		}
	}
	
	public static GameConfigService getInstance() {
		return new GameConfigService();
	}
	
	public String getValue(String key) {
		return prop.getProperty(key);
	}
	
}
