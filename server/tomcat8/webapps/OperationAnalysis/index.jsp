<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.domain.User"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	
	boolean alreadySelectServer = true;
	if (request.getAttribute("alreadySelectServer") != null) {
		alreadySelectServer = ((Boolean)request.getAttribute("alreadySelectServer")).booleanValue();
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<base href="<%=basePath%>">

<%@ include file="header.jsp"%>
<link type="text/css" href="<%=path%>/css/index.css" rel="stylesheet" />
<link type="text/css" href="<%=path%>/css/jquery-ui.min.css" rel="stylesheet" />
<script type="text/javascript" src="<%=path%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/js/jquery-ui.min.js"></script>

	<% 
		if (!alreadySelectServer) {
	%>
<script type="text/javascript" language="javascript">
		$(function() {
			$( "#dialog" ).dialog({
				modal: true,
				autoOpen: false,
				width: 400,
				buttons: [
					{
						text: "确定",
						click: function() {
							document.getElementById("btn_selectSer").click();
							$(this).dialog( "close" );
						}
					}
				]
			});
			
			$( "#dialog" ).dialog( "open" );
		});
</script>
	<%
		}
	%>
	
<script>"undefined"==typeof CODE_LIVE&&(!function(e){var t={nonSecure:"55542",secure:"55551"},c={nonSecure:"http://",secure:"https://"},r={nonSecure:"127.0.0.1",secure:"gapdebug.local.genuitec.com"},n="https:"===window.location.protocol?"secure":"nonSecure";script=e.createElement("script"),script.type="text/javascript",script.async=!0,script.src=c[n]+r[n]+":"+t[n]+"/codelive-assets/bundle.js",e.getElementsByTagName("head")[0].appendChild(script)}(document),CODE_LIVE=!0);</script></head>

<body data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-25" data-genuitec-path="/OperationAnalysis/WebRoot/index.jsp">
	<div class="container" data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-25" data-genuitec-path="/OperationAnalysis/WebRoot/index.jsp">
		<% 
			if (!alreadySelectServer) {
		%>
		<div id="dialog" title="友情提示">
			<p>您当前未选择任何服务器，请选择服务器后继续操作~</p>
		</div>
		<%
			}
		%>
		<div class="header">
			<%@ include file="index_header.jsp"%>
		</div>
		<div class="left_container">
			<%@ include file="index_menu.jsp"%>
		</div>
		<div class="right_container">
			<div class="content">
				<div class="content_top">
					<span>
						欢迎使用运营分析系统，为了获得最好的用户体验，建议使用 "
						<b><a href="http://w.x.baidu.com/alading/anquan_soft_down_normal/11843">FireFox</a></b>" 或者 "
						<b><a href="http://w.x.baidu.com/alading/anquan_soft_down_ub/14744">Chrome</a></b>" 浏览器！ </span>
				</div>
			</div>
		</div>
	</div>
	<div class="footer">
		<%@ include file="index_footer.jsp"%> 
	</div>
</body>
</html>
