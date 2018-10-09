<%@page import="com.domain.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String agent = (String)request.getSession().getAttribute("agent");
	String gameSite = (String)request.getSession().getAttribute("gameSiteName");
	String server = "";
	String[] serverArray = {}; 
	if (!agent.equals("")) {
		serverArray = agent.split(",");
	} else if (!gameSite.equals("")) {
		serverArray = gameSite.split(",");
	}
	
	int n = serverArray.length;
	
	if (n > 8) 
		n = 9;
	for (int i = 0; i < n; i++) {
		if (i == 8) {
			server += "....." + "、";
		} else {
			server += serverArray[i] + "、";
		}
	}
	
	n = server.length();
	if (n > 0) {
		server = server.substring(0, n - 1);
	}
	
	String username = "";
	User user = (User)request.getSession().getAttribute(request.getSession().getId());
	if (user != null) {
		username = user.getUserName();
	}
%>
<link type="text/css" href="<%= path%>/css/index_header.css" rel="stylesheet"  data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-27" data-genuitec-path="/OperationAnalysis/WebRoot/index_header.jsp"/>

<div>
	<div class="header_top">
		<span class="systemname">运营分析系统  v1.0.0</span>
		<div class="top_menu">
			<div class="top_menu_item">
				<a href="<%= path%>/logout"><span class="icon icon-quit"></span> 注销 </a>
			</div>
		</div>
	</div>
	<div class="header_bottom">
		<div class="bottom_left_menu">
			<div class="bottom_left_menu_item">
				<a href="<%= path%>/all?optType=111"><b>HOME</b></a>
			</div>
			<div class="bottom_left_menu_item">
				<a href="<%= path%>/all?optType=116" id="btn_selectSer">选择服务器 </a>
			</div>
		</div>
		<div class="server_state">
			<span>当前服务器 【<%= server%>】</span>
		</div>
		<span class="username"><span class="icon icon-profile"></span>&nbsp;欢迎您，<%=username %>！</span>
	</div>
</div>