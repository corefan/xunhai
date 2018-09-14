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
</head>

<body>
	<div id="container">
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
