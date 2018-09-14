package com.qidian.flashsecurity.xsocket;



public class FlashSecurityServerXSocket {
	public static void main(String[] args) throws Exception {
		try {
			// 开启安全策略服务
			FlashSecurityXMLServer flashSecurityXMLServer = new FlashSecurityXMLServer();
			flashSecurityXMLServer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
