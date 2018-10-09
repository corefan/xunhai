<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<%@page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	JSONObject data = (JSONObject) request.getAttribute("data");
	JSONArray firstPayData = new JSONArray();
	if (data != null && !data.isNull("dataList")) {
		firstPayData = (JSONArray) data.getJSONArray("dataList");
	}

	String dataStr = "";
	String xAxisStr = "";
	for (int i = 0; i < firstPayData.length(); i++) {
		JSONObject obj = firstPayData.getJSONObject(i);
		if (!obj.isNull("num")) {
			dataStr += obj.getString("num") + ",";
			xAxisStr += "'" + obj.getString("level") + "',";
		}
	}
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
<script type="text/javascript" src="<%=path%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/js/datetimepicker.js"></script>
<script type="text/javascript"
	src="<%=path%>/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="<%=path%>/js/highcharts.js"></script>

<script type="text/javascript" language="javascript">
	$(function() {
		$('#table_1').dataTable({});

		$('.datetimepicker').datetimepicker({
			lang : 'ch',
			timepicker : false,
			format : 'Y-m-d',
			formatDate : 'Y-m-d',
		});

		$('#chart_1')
				.highcharts(
						{
							chart : {
								type : 'column',
								margin : [ 50, 50, 100, 80 ]
							},
							title : {
								text : ''
							},
							xAxis : {
								categories : [<%= xAxisStr%>],
								labels : {
									rotation : -45,
									align : 'right',
									style : {
										fontSize : '13px',
										fontFamily : 'Verdana, sans-serif'
									}
								}
							},
							yAxis : {
								min : 0,
								title : {
									text : '付费人数(单位：人)'
								}
							},
							legend : {
								enabled : false
							},
							tooltip : {
								pointFormat : '付费人数: <b>{point.y} 人</b>',
							},
							series : [ {
								name : '',
								data : [ <%= dataStr%>],
							} ]
						});
	});
</script>
<script>"undefined"==typeof CODE_LIVE&&(!function(e){var t={nonSecure:"55542",secure:"55551"},c={nonSecure:"http://",secure:"https://"},r={nonSecure:"127.0.0.1",secure:"gapdebug.local.genuitec.com"},n="https:"===window.location.protocol?"secure":"nonSecure";script=e.createElement("script"),script.type="text/javascript",script.async=!0,script.src=c[n]+r[n]+":"+t[n]+"/codelive-assets/bundle.js",e.getElementsByTagName("head")[0].appendChild(script)}(document),CODE_LIVE=!0);</script></head>

<body data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-19" data-genuitec-path="/OperationAnalysis/WebRoot/firstPayLev.jsp">
	<div class="container" data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-19" data-genuitec-path="/OperationAnalysis/WebRoot/firstPayLev.jsp">
		<div class="header">
			<%@ include file="index_header.jsp"%>
		</div>
		<div class="left_container">
			<%@ include file="index_menu.jsp"%>
		</div>
		<div class="right_container">
			<div class="content">
				<div class="content_top">
					<form action="<%=path%>/payAnalysis" method="post" style="display:inline;">
						<input type="hidden" name="optType" value="<%=OptTypeConstant.PAY_ANALYSIS_42%>" /> <input
							type="submit" value="刷新数据" class="btn_1" />
					</form>
				</div>
				<div class="content_panel">
					<div class="content_panel_title">
						<h3>首次付费等级分析</h3>
					</div>
					<div class="content_panel_content">
						<div id="chart_1" class="barchart" style="min-width:500px;height:400px"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="footer">
		<%@ include file="index_footer.jsp"%>
	</div>
</body>
</html>
