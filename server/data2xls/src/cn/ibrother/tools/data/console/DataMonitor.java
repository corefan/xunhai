package cn.ibrother.tools.data.console;

import org.apache.log4j.xml.DOMConfigurator;
import org.logicalcobwebs.proxool.configuration.JAXPConfigurator;

import cn.ibrother.tools.data.common.Constants;
import cn.ibrother.tools.data.common.Helper;
import cn.ibrother.tools.data.parse.Data2Excel;
import cn.ibrother.tools.data.parse.Excel2Data;
import cn.ibrother.tools.data.init.CmdParameter;
import cn.ibrother.tools.data.init.SystemInit;

import com.beust.jcommander.JCommander;
import com.kingsoft.commons.io.Logger;

/**
 * 数据导入,导出操作入口
 * @author ken
 * @since 2016-10-13
 * @version 1.0
 */
public class DataMonitor {
	public static void main(String[] args) {
		Logger log = new Logger(DataMonitor.class);
		try {
			// 启动log4j初始化
			System.out.println("initializing log4j...");
			String log4jConfigPath = Helper.getConfigPath(Constants.LOG_CONFIG_FILE);
			DOMConfigurator.configure(log4jConfigPath);			
					
			// 全局变量初始化
			//System.out.println("initializing global context...");
			SystemInit.init();
			
			// 启动连接池初始化
			//System.out.println("initializing proxool...");
			Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");	
			JAXPConfigurator.configure(Helper.getConfigPath(Constants.PROXOOL_CONFIG_FILE), false);
			
			// 导入测试
//			String[] argv = { "-act", "2", "-db", "xh_base", "-path", 
//				"E:/xunhai/development/database/excel/"};
			
			// 导出测试
//			 String[] argv = { "-act", "1", "-db", "db_base_pokemon", "-path", 
//		 		"E:/pokemon/ServerConfigs/"};
			
			// 导入指定文件测试
//			String[] argv = { "-act", "2", "-db", "xh_base", "-path", 
//		 		"E:/xunhai/development/database/excel/", 
//		 		"-file", "task.xlsx","-gameName",""};
			
			// 执行解析动作
			CmdParameter cmdParameter = new CmdParameter();
			new JCommander(cmdParameter, args);			
			
//			log.info("-act=", cmdParameter.act);
//			log.info("-db=", cmdParameter.db);
//			log.info("-path=", cmdParameter.path);
//			log.info("-file=", cmdParameter.file);
//			log.info("-gameName=", cmdParameter.gameName);
			
			if (cmdParameter.act.intValue() == 1) {
				// 从mysql数据库导出数据到excel
				Data2Excel d2e = new Data2Excel();
				d2e.run(cmdParameter);
				
				//log.info(String.format("total export table:%d", d2e.getCount()));
			} else if (cmdParameter.act.intValue() == 2) {
				// 将excel数据导入到mysql中
				Excel2Data e2d = new Excel2Data();
				e2d.run(cmdParameter);
				
				//log.info(String.format("total import table:%d", e2d.getCount()));
			}
			
			log.info("run ok.");
	        System.exit(0);
		} catch (Exception ex) {
			log.error(ex);
			log.info("run error.");
			System.exit(1);
		}
	}
}