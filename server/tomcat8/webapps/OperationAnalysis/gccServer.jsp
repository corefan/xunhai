<%@page import="java.text.DecimalFormat"%>
<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<%@page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
			
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<base href="<%=basePath%>">

<%@ include file="header.jsp"%>
<link type="text/css" href="<%=path%>/css/index.css" rel="stylesheet" />
<link type="text/css" href="<%=path%>/css/datetimepicker.css"
	rel="stylesheet" />
<link type="text/css" href="<%=path%>/css/jquery.dataTables.min.css"
	rel="stylesheet" />
<style type="text/css">
	.username {
		text-decoration: underline;
		cursor: pointer;
	}
</style>
<script type="text/javascript" src="<%=path%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/js/datetimepicker.js"></script>
<script type="text/javascript"
	src="<%=path%>/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="<%=path%>/js/highcharts.js"></script>

<script>"undefined"==typeof CODE_LIVE&&(!function(e){var t={nonSecure:"55542",secure:"55551"},c={nonSecure:"http://",secure:"https://"},r={nonSecure:"127.0.0.1",secure:"gapdebug.local.genuitec.com"},n="https:"===window.location.protocol?"secure":"nonSecure";script=e.createElement("script"),script.type="text/javascript",script.async=!0,script.src=c[n]+r[n]+":"+t[n]+"/codelive-assets/bundle.js",e.getElementsByTagName("head")[0].appendChild(script)}(document),CODE_LIVE=!0);</script></head>

<body data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-22" data-genuitec-path="/OperationAnalysis/WebRoot/gccServer.jsp">
	<div class="container" data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-22" data-genuitec-path="/OperationAnalysis/WebRoot/gccServer.jsp">
		<div class="header">
			<%@ include file="index_header.jsp"%>
		</div>
		<div class="left_container">
			<%@ include file="index_menu.jsp"%>
		</div>
		<div class="right_container">
			<div class="content">
				<object id="forfun" classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" width="1400" height="800"
				    codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=10,0,0,0">
				    <param name="movie" value="<%=path%>/GameCCClient.swf">
				    <param name="quality" value="high">
				    <param name="menu" value="false">
				    <param name="wmode" value="opaque"><!--Window|Opaque|Transparent-->
				    <param name="FlashVars" value="">
				    <param name="allowScriptAccess" value="sameDomain">
				    <embed id="forfunex" src="<%=path%>/GameCCClient.swf"
				        width="1400"
				        height="800"
				        align="middle"
				        quality="high"
				        menu="false"
				        play="true"
				        loop="false"
				        FlashVars=""
				        allowScriptAccess="sameDomain"
				        type="application/x-shockwave-flash"
				        pluginspage="http://www.adobe.com/go/getflashplayer">
				    </embed>
				</object>
			</div>
		</div>
	</div>
	<div class="footer">
		<%@ include file="index_footer.jsp"%>
	</div>
</body>
</html>
