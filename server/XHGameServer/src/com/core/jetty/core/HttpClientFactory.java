package com.core.jetty.core;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import com.util.LogUtil;

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
				httpClient.setThreadPool(new QueuedThreadPool(5));
				httpClient.setRequestBufferSize(WebConstant.RESP_BUFFER_SIZE);
				httpClient.setResponseBufferSize(WebConstant.RESP_BUFFER_SIZE);
				httpClient.setTimeout(5000);
				httpClient.start();
			}
		} catch (Exception e) {
			LogUtil.error("异常:",e);
		}

		return httpClient;
	}

}
