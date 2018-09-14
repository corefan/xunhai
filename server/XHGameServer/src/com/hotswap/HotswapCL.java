package com.hotswap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

public class HotswapCL extends ClassLoader {

	
	private String basedir; // 需要该类加载器直接加载的类文件的基目录  
    private HashSet<String> dynaclazns; // 需要由该类加载器直接加载的类名  
  
    /**
     * 1.要想实现同一个类的不同版本的共存，那么这些不同版本必须由不同的类加载器进行加载，
     * 因此就不能把这些类的加载工作委托给系统加载器来完成，因为它们只有一份。
     * 2.为了做到这一点，就不能采用系统默认的类加载器委托规则，
     * 也就是说我们定制的类加载器的父加载器必须设置为 null。
     * */
    public HotswapCL(String basedir, String[] clazns) {  
        super(null); // 指定父类加载器为 null  
        this.basedir = basedir;  
        dynaclazns = new HashSet<String>();  
        loadClassByMe(clazns);  
    }  
  
    private void loadClassByMe(String[] clazns) {  
        for (int i = 0; i < clazns.length; i++) {  
            loadDirectly(clazns[i]);  
            dynaclazns.add(clazns[i]);  
        }  
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
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return cls;  
    }  
  
    private Class<?> instantiateClass(String name, InputStream fin, long len) {  
        byte[] raw = new byte[(int) len];  
        try {  
            fin.read(raw);  
            fin.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
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
