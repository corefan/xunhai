package com.qidian.flashsecurity.xsocket;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.channels.ClosedChannelException;

import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.connection.IDataHandler;
import org.xsocket.connection.IDisconnectHandler;
import org.xsocket.connection.INonBlockingConnection;

public class FlashSecurityXMLHandler implements IDataHandler, IDisconnectHandler {

	private String policyXml;
	
	public FlashSecurityXMLHandler() {
		StringBuffer xmlBuffer = new StringBuffer();
		xmlBuffer.append("<cross-domain-policy>");
		xmlBuffer.append("<allow-access-from domain=\"");
		xmlBuffer.append("*");
		xmlBuffer.append("\" to-ports=\"");
		xmlBuffer.append("*");
		xmlBuffer.append("\"/>");
		xmlBuffer.append("</cross-domain-policy>");
		xmlBuffer.append("\0");
		policyXml = xmlBuffer.toString();
	}
	
	@Override
	public boolean onData(INonBlockingConnection nbc) throws IOException,
			BufferUnderflowException, ClosedChannelException,
			MaxReadSizeExceededException {
		nbc.write(policyXml);
		return true;
	}

	@Override
	public boolean onDisconnect(INonBlockingConnection nbc) throws IOException {
		nbc.close();
		return true;
	}
	
}
