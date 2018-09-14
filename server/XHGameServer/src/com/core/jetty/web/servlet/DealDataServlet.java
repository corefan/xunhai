package com.core.jetty.web.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.common.Config;
import com.common.DateService;
import com.common.GameContext;
import com.common.GameSocketService;
import com.constant.OptTypeConstant;
import com.hotswap.MyClassLoader;
import com.util.LogUtil;

/**
 * 处理数据
 * @author ken
 * @date 2017-7-20
 */
public class DealDataServlet extends BaseServlet {

	private static final long serialVersionUID = -422877013684426058L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		initReqResp(req, resp);
		
		if(!checkIP(req)) return;
		
		int msg = 0;
		try {

			JSONObject jsonObject = dealMsg(req);
			if (jsonObject == null) return;

			int optType = Integer.parseInt(jsonObject.get("optType").toString());
			switch (optType) {
			case OptTypeConstant.DEAL_DATA_1:
				msg = refreshBaseCache();
				break;
			case OptTypeConstant.DEAL_DATA_2:
				msg = refreshConfigCache();
				break;
			case OptTypeConstant.DEAL_DATA_3:
				msg = synCacheData();
				break;
			case OptTypeConstant.DEAL_DATA_8:
				msg = hotUpdateClass();
				break;
			}

		} catch (Exception e) {
			LogUtil.error("异常:",e);
		}

		try {
			JSONObject result = new JSONObject();
			result.put("result", msg);
			this.postData(resp, result.toString());
		} catch (JSONException e) {
			LogUtil.error("异常:",e);
		}
	
	}

	/**
	 * 同步缓存数据
	 * */
	private int synCacheData() {
		int msg = 0;
		try {
			GameContext.getInstance().getServiceCollection().getSynDataService().synCache_beforeClose();
			LogUtil.info("同步缓存数据完成:"+DateService.getCurrentUtilDate());
		} catch (Exception e) {
			LogUtil.error("异常:", e);
			msg = -1;
		}

		return msg;
	}

	/**
	 * 刷新基础缓存
	 * */
	private int refreshBaseCache() {
		int msg =0;
		try {
			GameContext.getInstance().getServiceCollection().initBaseCache();
			LogUtil.info("基础数据刷新完成:"+DateService.getCurrentUtilDate());
			// 告诉client更新自己的基础数据文件
			noticeCientRefreshBaseData();
		} catch (Exception e) {
			LogUtil.error("异常:", e);
			msg = -1;
		}

		return msg;
	}

	/** 通知client刷新基础数据 */
	private void noticeCientRefreshBaseData() {

		GameSocketService gameSocketService = GameContext.getInstance().getServiceCollection().getGameSocketService();

//		S_REFRESH_BASE_DATA.Builder builder = S_REFRESH_BASE_DATA.newBuilder();
//
//		gameSocketService.sendDataToAll(new MessageObj(MessageID.S_REFRESH_BASE_DATA_VALUE, builder.build().toByteArray()));
	}

	/**
	 * 刷新配置文件
	 * */
	private int refreshConfigCache() {
		int msg = 0;
		// 初始化配置    
		try {
			Config.init();
			LogUtil.info("配置数据刷新完成:"+DateService.getCurrentUtilDate());
		} catch (Exception e) {
			LogUtil.error("异常:",e);
			msg = -1;
		}

		return msg;
	}

	/** 热更新
	 *  注意： 有时候更新service class不成功时，可能是action里面定义service时 不是在方法里面，而是全局变量；
	 *  放在放里面获得service就可以了。
	 *  eg： action里面的xxx()方法
	 *  IRelationService relationService = serviceCollection.getRelationService();
	 *  
	 *  */
	private int hotUpdateClass() {

		int msg = 0;
		// 初始化配置    
		try {
			// 外网真实环境   /data/update/gs/hotbin/
			//String hotbinDir = "data"+File.separatorChar+"update"+File.separatorChar+"gs"+File.separatorChar+"hotbin"+File.separatorChar;

			// 168英文主干  /data/gs_trunk_en/hotbin/
			//String hotbinDir = "data"+File.separatorChar+"gs_trunk_en"+File.separatorChar+"hotbin"+File.separatorChar;

			// 本机
			//String hotbinDir = "G:"+File.separatorChar+"trunk"+File.separatorChar+"en"+File.separatorChar+"hotbin"+File.separatorChar;

			if (Config.HOT_BIN_DIR != null) {

				// 得到文件时间
				long fileTime = getFileTime();
				if (fileTime == 0) {
					System.out.println(" file time is error !");
					return -1;
				}

				MyClassLoader classLoader = new MyClassLoader(Config.HOT_BIN_DIR, fileTime);

				// service
				dealHotUpdate(classLoader, "com"+File.separatorChar+"service"+File.separatorChar+"impl", "com.service.impl.", 1);
//				// action
//				dealHotUpdate(classLoader, "com"+File.separatorChar+"action", "com.action.", 2);

				System.out.println("hot update class finished!: "+DateService.dateFormat(new Date()));
			} else {
				LogUtil.error("hotUpdateClass: hot class dir not exist!");
				msg = -1;
			}

		} catch (Exception e) {
			LogUtil.error("异常:",e);
			msg = -1;
		}

		return msg;
	}

	private void dealHotUpdate(MyClassLoader classLoader, String targetDir, String prefix, int type) throws Exception {

		Set<String> names = classLoader.getHotClazs(targetDir, prefix);
		if (!names.isEmpty()) {
			for (String className : names) {
				Class<?> cls = classLoader.loadClass(className, false);
				GameContext.getInstance().refreshCollection(cls, type);
				System.out.println("hotUpdateClass: "+className);
			}
		}

	}

	private long getFileTime() {

		try {

			Properties prop = new Properties();
			File file = new File(Config.HOT_BIN_DIR+"config.properties");
			if (file.exists()) {
				InputStream is = new FileInputStream(file);
				prop.load(is);
			} else {
				return 0;
			}

			String timeStr = prop.getProperty("fileDate");
			Date fileDate = DateService.getDateByString(timeStr);
			System.out.println("fileDate: "+timeStr);
			// 一天前的
			if (fileDate.before(DateService.subDateByType(DateService.getCurrentUtilDate(), Calendar.DAY_OF_MONTH, 1))) return 0;

			return fileDate.getTime();

		} catch (Exception e) {
			LogUtil.error("异常:",e);
		}

		return 0;
	}
}
