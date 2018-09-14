package com.util;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Properties;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import flex.messaging.util.Base64.Encoder;

public class RSAUtil {
	
	public static void main(String[] args) {
		//new RewardUtil();
		
		try {
			System.out.println(encrypt("XHgame@2018"));
			//System.out.println(decrypt("Ezx//lKSiJ3+VoPCZHc2AAfHiBDTzta0PXCCk8RvY7qHPYmbuyFN660ElZbOrBfF35hlT3ZcwEcLliPXuMQcWSouR+BBQrYxYuJ2XDSGqR8tr5Jr8IOUJ/Cj8WhTIiwvw748aIdkhLSxAyZ5baCH60MdDnZnDLyyTFGiXt16jJk="));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static Properties config;

	static {
		config = new Properties();
		try {
			config = new Properties();
			File projectFolder = new File(Thread.currentThread().getContextClassLoader().getResource("").toString().replace("%20", " "));
			projectFolder = projectFolder.getParentFile();
			String projectFolderPath = projectFolder.getPath();
			String projectFolderName = projectFolderPath.substring(5, projectFolderPath.length());
			File file = new File(projectFolderName+File.separator+"conf"+File.separator+"key.properties");
			FileInputStream fis = new FileInputStream(file);
			config.load(fis);
		} catch (Exception e) {
			LogUtil.error(e);
		}
	}
	
	/**
	 * 公钥加密
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String str) throws Exception {
		return encrypt(config.getProperty("publicKey"), config.getProperty("publicExponent"), str);
	}
	
	/**
	 * 公钥加密
	 * @param pubModString
	 * @param pubPubExpString
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String pubModString, String pubPubExpString, String str) throws Exception {
		byte[] pubModBytesNew = Base64.decodeBase64(pubModString);
		byte[] pubPubExpBytesNew = Base64.decodeBase64(pubPubExpString);
		RSAPublicKey recoveryPubKey = generateRSAPublicKey(pubModBytesNew, pubPubExpBytesNew);
		byte[] raw = encrypt(recoveryPubKey, str.getBytes());
		Encoder encoder = new Encoder(raw.length);
		encoder.encode(raw);
		return encoder.drain();
	}

	/**
	 * 生成公钥key
	 * @param modulus
	 * @param publicExponent
	 * @return
	 * @throws Exception
	 */
	private static RSAPublicKey generateRSAPublicKey(byte modulus[], byte publicExponent[]) throws Exception {
		KeyFactory keyFac = KeyFactory.getInstance("RSA", new BouncyCastleProvider());
		RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicExponent));
		return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);
	}

	/**
	 * 实现公钥加密
	 * @param publicKey
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	private static byte[] encrypt(RSAPublicKey publicKey, byte obj[]) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(1, publicKey);
		return cipher.doFinal(obj);
	}

	/**
	 * 私钥解密
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String str) throws Exception {
		return decrypt(config.getProperty("privateKeyModulus"), config.getProperty("privateExponent"), str);
	}
	
	/**
	 * 私钥解密
	 * @param priModBytesNew
	 * @param priPriExpBytesNew
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String priModBytesNew, String priPriExpBytesNew, String str) throws Exception {
		byte[] enRaw = Base64.decodeBase64(str.getBytes());
		byte[] data = decrypt(priModBytesNew, priPriExpBytesNew, enRaw);
		return new String(data);
	}

	/**
	 * 实现私钥解密
	 * @param priModBytesNew
	 * @param priPriExpBytesNew
	 * @param clipherString
	 * @return
	 * @throws Exception
	 */
	private static byte[] decrypt(String priModBytesNew, String priPriExpBytesNew, byte clipherString[]) throws Exception {
		byte[] byteModBytesNew = Base64.decodeBase64(priModBytesNew);
		byte[] bytePriExpBytesNew = Base64.decodeBase64(priPriExpBytesNew);
		RSAPrivateKey recoveryPriKey = generateRSAPrivateKey(byteModBytesNew, bytePriExpBytesNew);
		return decrypt(recoveryPriKey, clipherString);
	}

	/**
	 * 生成私钥key
	 * @param modulus
	 * @param privateExponent
	 * @return
	 * @throws Exception
	 */
	private static RSAPrivateKey generateRSAPrivateKey(byte modulus[], byte privateExponent[]) throws Exception {
		KeyFactory keyFac = KeyFactory.getInstance("RSA", new BouncyCastleProvider());
		RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus), new BigInteger(privateExponent));
		return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);
	}

	/**
	 * 最终私钥解密
	 * @param privateKey
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	private static byte[] decrypt(RSAPrivateKey privateKey, byte obj[]) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(2, privateKey);
		return cipher.doFinal(obj);
	}

}
