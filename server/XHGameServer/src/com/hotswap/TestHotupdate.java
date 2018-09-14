package com.hotswap;

import java.io.File;
import java.util.Set;

import com.common.GameContext;
import com.util.LogUtil;

public class TestHotupdate {

	private void dealHotUpdate(MyClassLoader classLoader, String targetDir, String prefix, int type) throws Exception {

		Set<String> names = classLoader.getHotClazs(targetDir, prefix);
		if (!names.isEmpty()) {
			for (String className : names) {
				Class<?> cls = classLoader.loadClass(className, false);
				GameContext.getInstance().refreshCollection(cls, type);
			}
		}

	}

	public void test3() {

		try {
			String hotbinDir = "G:"+File.separatorChar+"trunk"+File.separatorChar+"en"+File.separatorChar+"hotbin"+File.separatorChar;
			MyClassLoader classLoader = new MyClassLoader(hotbinDir,0);

			// service
			dealHotUpdate(classLoader, "com"+File.separatorChar+"service"+File.separatorChar+"impl", "com.service.impl.", 1);
			// action
			dealHotUpdate(classLoader, "com"+File.separatorChar+"action", "com.action.", 2);
			
		} catch (Exception e) {
			LogUtil.error("异常", e);
		}
	}

	public void testHotSwap() {

		try {
			String binUrl = System.getProperty("user.dir")+File.separatorChar+"bin"+File.separatorChar;
			HotswapCL hotswapCL = new HotswapCL(binUrl, new String[]{"com.service.impl.MallService"});

			Class<?> cls = hotswapCL.loadClass("com.service.impl.MallService", false);
			GameContext.getInstance().refreshCollection(cls, 1);

		} catch (Exception e) {
			LogUtil.error("异常", e);
		}
	}

	public void testHotSwap2() {

		try {
			String binUrl = System.getProperty("user.dir")+File.separatorChar+"bin"+File.separatorChar;
			HotswapCL hotswapCL = new HotswapCL(binUrl, new String[]{"com.action.CommonAction"});

			Class<?> cls = hotswapCL.loadClass("com.action.CommonAction", false);
			GameContext.getInstance().refreshCollection(cls, 2);

		} catch (Exception e) {
			LogUtil.error("异常", e);
		}
	}
}
