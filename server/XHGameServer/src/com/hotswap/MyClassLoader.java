package com.hotswap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

import com.util.LogUtil;

public class MyClassLoader extends ClassLoader {

	private String basedir; // 需要该类加载器直接加载的类文件的基目录  
	private HashSet<String> dynaclazns; // 需要由该类加载器直接加载的类名  
	private long fileTime; // 文件对比时间

	public MyClassLoader(String basedir, long fileTime) {  
		super(null); // 指定父类加载器为 null  
		this.basedir = basedir;  
		this.fileTime = fileTime;
		this.dynaclazns = new HashSet<String>();  
	}  

	public HashSet<String> getHotClazs(String targetDir, String prefix) {

		HashSet<String> nameSet = new HashSet<String>();

		try {
			File file = new File(basedir+targetDir);
			if (file.exists()) {
				File[] files = file.listFiles();
				if (files.length > 0) {
					for (File f : files) {
						// 比较文件时间和设定时间
						if (f.lastModified() >= fileTime) {
							String fileName = f.getName();
							// -6 去掉 .class
							String className = prefix + fileName.substring(0, fileName.length() - 6);
							loadDirectly(className);  
							dynaclazns.add(className); 
							nameSet.add(className);
						}
					}
				}
			} else {
				LogUtil.error("getHotClazs方法： 热更新文件夹不存在！");
			}
		} catch (Exception e) {
			LogUtil.error("异常 : ",e);
		}

		return nameSet;
	}

	private Class<?> loadDirectly(String name) {  
		Class<?> cls = null;  
		StringBuffer sb = new StringBuffer(basedir);  
		String classname = name.replace('.', File.separatorChar) + ".class";  
		sb.append(File.separator + classname);  
		File classF = new File(sb.toString());  
		try {  
			cls = instantiateClass(name, new FileInputStream(classF),  
					classF.length());  
		} catch (FileNotFoundException e) {  
			LogUtil.error("异常:",e);
		}  
		return cls;  
	}  

	private Class<?> instantiateClass(String name, InputStream fin, long len) {  
		byte[] raw = new byte[(int) len];  
		try {  
			fin.read(raw);  
			fin.close();  
		} catch (IOException e) {  
			LogUtil.error("异常:",e);
		}  

		return defineClass(name, raw, 0, raw.length);  
	}  

	public Class<?> loadClass(String name, boolean resolve)  
			throws ClassNotFoundException {  
		Class<?> cls = null;  
		cls = findLoadedClass(name);  
		if (!this.dynaclazns.contains(name) && cls == null)  
			cls = getSystemClassLoader().loadClass(name);  
		if (cls == null)  
			throw new ClassNotFoundException(name);  
		if (resolve)  
			resolveClass(cls);  
		return cls;  
	}  

}
