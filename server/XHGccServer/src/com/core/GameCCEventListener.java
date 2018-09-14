package com.core;

import java.io.IOException;

import org.eclipse.jetty.client.HttpEventListenerWrapper;

import com.command.ExceptionAction;
import com.common.GCCContext;
import com.common.GameException;
import com.domain.BaseServerConfig;
import com.service.IBaseDataService;

public class GameCCEventListener extends HttpEventListenerWrapper {

	private Connection connection;
	private String gameSite = "";
	private boolean sendException;
	
	public GameCCEventListener(Connection connection, String gameSite, boolean sendException){
		this.connection = connection;
		this.gameSite = gameSite;
		this.sendException = sendException;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	@Override
	public void onConnectionFailed(Throwable ex) {
		super.onConnectionFailed(ex);
		if (connection == null) return;
		
		IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
		BaseServerConfig gsv = baseDataService.getServerConfByGameSite(gameSite);
		if(sendException) {
			ExceptionAction exceptionAction = GCCContext.getInstance().getActionCollection().getExceptionAction();
			exceptionAction.sendException(new GameException(gsv.getServerName()+"-------服务连接失败"), connection);
		}
		connection.getExceptionStr().append(gsv.getServerName()+"-------服务连接失败\n");
	}
	
	@Override
	public void onExpire() {
		super.onExpire();
		
		if (connection == null) return;
		
		IBaseDataService baseDataService = GCCContext.getInstance().getServiceCollection().getBaseDataService();
		BaseServerConfig gsv = baseDataService.getServerConfByGameSite(gameSite);
		if(sendException) {
			ExceptionAction exceptionAction = GCCContext.getInstance().getActionCollection().getExceptionAction();
			exceptionAction.sendException(new GameException(gsv.getServerName()+"-------服务请求超时"), connection);
		}
		connection.getExceptionStr().append(gsv.getServerName()+"-------服务请求超时\n");
	}

	@Override
	public void onResponseComplete() throws IOException {
		super.onResponseComplete();
	}

	
}
