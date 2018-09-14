package com.core;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import com.util.LogUtil;

/**
 * http
 * @author ken
 * @date 2018年8月20日
 */
public class HttpClientFactory {

	private static HttpClient httpClient;

	private HttpClientFactory(){

	}

	public static HttpClient getInstance() {

		try {
			if (httpClient == null) {
				httpClient = new HttpClient();
				httpClient.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);
				httpClient.setMaxConnectionsPerAddress(500);
				httpClient.setThreadPool(new QueuedThreadPool(100));
				httpClient.setRequestBufferSize(2048);
				httpClient.setResponseBufferSize(1024*128);
				httpClient.setTimeout(10000);
				httpClient.start();
			}
		} catch (Exception e) {
			LogUtil.error("异常：",e);
		}

		return httpClient;
	}

}
