package com.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CrossDomainServlet extends HttpServlet {

	private static final long serialVersionUID = 887437907226094461L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		resp.getWriter().println(getMsg());
	}

	private String getMsg() {
		
		StringBuffer xmlBuffer = new StringBuffer();
		xmlBuffer.append("<cross-domain-policy>");
		xmlBuffer.append("<allow-access-from domain=\"");
		xmlBuffer.append("*");
		xmlBuffer.append("\" to-ports=\"");
		xmlBuffer.append("*");
		xmlBuffer.append("\"/>");
		xmlBuffer.append("</cross-domain-policy>");
		return xmlBuffer.toString();
	}
	
	@Override
	public void init() throws ServletException {
	}

}
