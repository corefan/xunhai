package com.util;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 拷贝帮助类
 *
 */
public class CloneUtil {

	/**
	 * 深度拷贝
	 * */
	public static Object deepClone(Object obj) {
		if (obj == null) {
			return obj;
		}
		Object o = null;
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);

			bais = new ByteArrayInputStream(baos.toByteArray());
			ois = new ObjectInputStream(bais);
			o = ois.readObject();
		} catch (IOException e) {
			LogUtil.error(e);
		} catch (ClassNotFoundException e) {
			LogUtil.error(e);
		} finally {
			try {
				if (bais != null) {
					bais.close();
					bais = null;
				}
				if (baos != null) {
					baos.close();
					baos = null;
				}
				if (ois != null ) {
					ois.close();
					ois = null;
				}
				if (oos != null ) { 
					oos.close();
					oos = null;
				}
			} catch (Exception e) {
				LogUtil.error(e);
			}
		}
		return o;

	}
}
