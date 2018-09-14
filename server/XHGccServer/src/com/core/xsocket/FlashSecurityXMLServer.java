package com.core.xsocket;

import java.util.concurrent.TimeUnit;

import org.xsocket.WorkerPool;
import org.xsocket.connection.IServer;
import org.xsocket.connection.Server;


public class FlashSecurityXMLServer {

	private static IServer server = null;
	
	private static final int PORT = 843;
	
	public void start() throws Exception {
		server = new Server(PORT, new FlashSecurityXMLHandler());
		
		WorkerPool workerPool = new WorkerPool(30, 100, 1800, TimeUnit.SECONDS, 2000, false);
		workerPool.prestartAllCoreThreads();
		
		server.setWorkerpool(workerPool);
		server.start();
		
		System.out.println("flashSecurityServer start complete");
	}
	
	public void stop() throws Exception {
		server.close();
	}
	
}
