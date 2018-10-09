<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String tips = (String)request.getAttribute("tips");
	if (tips == null) {
		tips = "";
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<base href="<%=basePath%>">
	<%@ include file="header.jsp"%>
	<link type="text/css" href="<%=path %>/css/login.css" rel="stylesheet" />
<script>"undefined"==typeof CODE_LIVE&&(!function(e){var t={nonSecure:"55542",secure:"55551"},c={nonSecure:"http://",secure:"https://"},r={nonSecure:"127.0.0.1",secure:"gapdebug.local.genuitec.com"},n="https:"===window.location.protocol?"secure":"nonSecure";script=e.createElement("script"),script.type="text/javascript",script.async=!0,script.src=c[n]+r[n]+":"+t[n]+"/codelive-assets/bundle.js",e.getElementsByTagName("head")[0].appendChild(script)}(document),CODE_LIVE=!0);</script></head>

<body data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-32" data-genuitec-path="/OperationAnalysis/WebRoot/login.jsp">
	<div id="container" data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-32" data-genuitec-path="/OperationAnalysis/WebRoot/login.jsp">
		<div id="loginform">
			<div id="header">
				<p>运营分析系统</p>
			</div>
			<div id="content">
				<form action="<%=path %>/login" method="post">
					<span>账  号：</span></span><input type="text" name="username" class="txt"><br/> 
					<span>密  码：</span><input type="password" name="password" class="txt"><br/>
					<select  id="gameID" name="gameID">  
    					<option style="font-size: 30" value="10">大唐诛仙</option> 
					</select>  <br/>
					<input type="submit" value="登录" class="btn_1" />&nbsp;&nbsp
					<input type="reset" value="重置" class="btn_1" /><br/>
					<span class="error"><%= tips %></span><br/>
				</form>
			</div>
		</div>
	</div>
</body>
</html>
