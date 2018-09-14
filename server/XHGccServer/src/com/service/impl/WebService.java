package com.service.impl;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpEventListener;
import org.eclipse.jetty.client.HttpExchange;
import org.eclipse.jetty.http.HttpMethods;
import org.eclipse.jetty.io.Buffer;
import org.eclipse.jetty.io.ByteArrayBuffer;
import org.json.JSONObject;


import com.core.HttpClientFactory;
import com.service.IWebService;

public class WebService implements IWebService {

	public void sendDateForWeb(JSONObject jsonObject, String url, HttpEventListener eventListener) throws Exception {
		
		Buffer buffer = new ByteArrayBuffer(jsonObject.toString().getBytes("UTF-8"));
		
		HttpExchange exchange = new HttpExchange(); 
		exchange.setEventListener(eventListener);
		exchange.setURL(url);
		exchange.setMethod(HttpMethods.POST);
		exchange.setRequestContentType("text/html;charset=UTF-8");
		exchange.setRequestContent(buffer);
		
		HttpClient httpClient = HttpClientFactory.getInstance();
		httpClient.send(exchange);
		
	}

}
