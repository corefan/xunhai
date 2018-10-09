<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<%@page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	JSONObject data = (JSONObject) request.getAttribute("data");
	JSONArray livenessData = new JSONArray();
	
	String xAxisStr = "";
	String dataStr_1 = ""; // 无挂机
	String dataStr_2 = ""; // 10m
	String dataStr_3 = ""; // 30m
	String dataStr_4 = ""; // 60m
	String dataStr_5 = ""; // 90m
	String dataStr_6 = ""; // 120m
	String dataStr_7 = ""; // 3h
	String dataStr_8 = ""; // 4h
	String dataStr_9 = ""; // 5h
	String dataStr_10 = ""; // 5h+
	
	if (data != null && !data.isNull("dataList")) {
		livenessData = (JSONArray) data.getJSONArray("dataList");
		for (int i = 0; i < livenessData.length(); i++) {
			JSONObject obj = livenessData.getJSONObject(i);
			String date = obj.getString("CREATE_DATE");
			xAxisStr += "'" + (date.substring(5, date.length())).replace("-", "/")
					+ "',";
			double sum = Double.parseDouble(obj.getString("SUMNUM"));
			dataStr_1 +=  (Math.round(Double.parseDouble(obj.getString("SUMNUM1")) / sum * 10000) / 100.0) + ",";
			dataStr_2 += (Math.round(Double.parseDouble(obj.getString("A10")) / sum * 10000) / 100.0) + ",";
			dataStr_3 += (Math.round(Double.parseDouble(obj.getString("A30")) / sum * 10000) / 100.0) + ",";
			dataStr_4 += (Math.round(Double.parseDouble(obj.getString("A60")) / sum * 10000) / 100.0) + ",";
			dataStr_5 += (Math.round(Double.parseDouble(obj.getString("A90")) / sum * 10000) / 100.0) + ",";
			dataStr_6 += (Math.round(Double.parseDouble(obj.getString("A120")) / sum * 10000) / 100.0) + ",";
			dataStr_7 += (Math.round(Double.parseDouble(obj.getString("A180")) / sum * 10000) / 100.0) + ",";
			dataStr_8 += (Math.round(Double.parseDouble(obj.getString("A240")) / sum * 10000) / 100.0) + ",";
			dataStr_9 += (Math.round(Double.parseDouble(obj.getString("A300")) / sum * 10000) / 100.0) + ",";
			dataStr_10 += (Math.round(Double.parseDouble(obj.getString("A380")) / sum * 10000) / 100.0) + ",";
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
		$('#table_1').dataTable({
			"bLengthChange" : false,
			"bInfo" : false,
			"bPaginate" : false,
			"bFilter":false,
			"bSort":false,
		});

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
								type : 'area'
							},
							title : {
								text : ''
							},
							subtitle : {
								text : ''
							},
							xAxis : {
								categories : [ <%= xAxisStr%> ],
								tickmarkPlacement : 'on',
								title : {
									enabled : false
								}
							},
							yAxis : {
								title : {
									text : '百分比(单位:%)'
								}
							},
							tooltip : {
								pointFormat : '<span style="color:{series.color}">{series.name}</span>: <b> {point.percentage:.1f}%</b><br>',
								shared : true
							},
							plotOptions : {
								area : {
									stacking : 'percent',
									lineColor : '#ffffff',
									lineWidth : 1,
									marker : {
										lineWidth : 1,
										lineColor : '#ffffff'
									}
								}
							},
							series : [ {
								name : '5h+',
								data : [ <%= dataStr_10%> ]
							},{
								name : '5h',
								data : [ <%= dataStr_9%> ]
							}, {
								name : '4h',
								data : [ <%= dataStr_8%> ]
							}, {
								name : '3h',
								data : [ <%= dataStr_7%> ]
							}, {
								name : '120m',
								data : [ <%= dataStr_6%> ]
							}, {
								name : '90m',
								data : [ <%= dataStr_5%> ]
							}, {
								name : '60m',
								data : [ <%= dataStr_4%> ]
							}, {
								name : '30m',
								data : [ <%= dataStr_3%> ]
							}, {
								name : '10m',
								data : [ <%= dataStr_2%> ]
							}, {
								name : '无挂机',
								data : [ <%= dataStr_1%> ]
							} ]
						});
	});
</script>
<script>"undefined"==typeof CODE_LIVE&&(!function(e){var t={nonSecure:"55542",secure:"55551"},c={nonSecure:"http://",secure:"https://"},r={nonSecure:"127.0.0.1",secure:"gapdebug.local.genuitec.com"},n="https:"===window.location.protocol?"secure":"nonSecure";script=e.createElement("script"),script.type="text/javascript",script.async=!0,script.src=c[n]+r[n]+":"+t[n]+"/codelive-assets/bundle.js",e.getElementsByTagName("head")[0].appendChild(script)}(document),CODE_LIVE=!0);</script></head>

<body data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-45" data-genuitec-path="/OperationAnalysis/WebRoot/temple.jsp">
	<div class="container" data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-45" data-genuitec-path="/OperationAnalysis/WebRoot/temple.jsp">
		<div class="header">
			<%@ include file="index_header.jsp"%>
		</div>
		<div class="left_container">
			<%@ include file="index_menu.jsp"%>
		</div>
		<div class="right_container">
			<div class="content">
				<div class="content_top">
					<form action="<%=path%>/behaviorAnalysis" method="post">
						<input type="hidden" name="optType" value="<%= OptTypeConstant.BEHAVIOR_ANALYSIS_10%>" /> <input
							type="text" class="datetimepicker" name="startDate"
							value="<%=request.getAttribute("startDate")%>" /> <span>
							- </span> <input type="text" class="datetimepicker" name="endDate"
							value="<%=request.getAttribute("endDate")%>" /><span>&nbsp;&nbsp;</span>
						<input type="submit" value="确定" class="btn_1" />
					</form>
				</div>
				<div class="content_panel">
					<div class="content_panel_title">
						<h3>活跃度监控</h3>
					</div>
					<div class="content_panel_content">
						<div id="chart_1" class="chart" style="min-width:500px;height:400px"></div>
						<%
							int dataSize = livenessData.length();
							int startIndex = 0;
							if (dataSize > 7) {
								startIndex = dataSize - 7;
							}
							String[] rows = { "", "", "", "", "", "", "", "", "", "", "", "" };
							int validDataSize = dataSize - startIndex;
							for (int i = 0; i < validDataSize; i++) {
								JSONObject obj = livenessData.getJSONObject(startIndex);
								int sum = Integer.parseInt(obj.getString("SUMNUM"));
								double a1 = Double.parseDouble(obj.getString("SUMNUM1"));
								double a2 = Double.parseDouble(obj.getString("A10"));
								double a3 = Double.parseDouble(obj.getString("A30"));
								double a4 = Double.parseDouble(obj.getString("A60"));
								double a5 = Double.parseDouble(obj.getString("A90"));
								double a6 = Double.parseDouble(obj.getString("A120"));
								double a7 = Double.parseDouble(obj.getString("A180"));
								double a8 = Double.parseDouble(obj.getString("A240"));
								double a9 = Double.parseDouble(obj.getString("A300"));
								double a10 = Double.parseDouble(obj.getString("A380"));
								rows[0] += "<th>" + obj.getString("CREATE_DATE") + "</th>";
								rows[1] += "<td>" + sum + "</td>";
								rows[2] += "<td>" + a1  + " (" + Math.round(a1 / sum * 10000) / 100.0 + "%)" + "</td>";
								rows[3] += "<td>" + a2  + " (" + Math.round(a2 / sum * 10000) / 100.0 + "%)" + "</td>";
								rows[4] += "<td>" + a3  + " (" + Math.round(a3 / sum * 10000) / 100.0 + "%)" + "</td>";
								rows[5] += "<td>" + a4  + " (" + Math.round(a4 / sum * 10000) / 100.0 + "%)" + "</td>";
								rows[6] += "<td>" + a5  + " (" + Math.round(a5 / sum * 10000) / 100.0 + "%)" + "</td>";
								rows[7] += "<td>" + a6  + " (" + Math.round(a6 / sum * 10000) / 100.0 + "%)" + "</td>";
								rows[8] += "<td>" + a7  + " (" + Math.round(a7 / sum * 10000) / 100.0 + "%)" + "</td>";
								rows[9] += "<td>" + a8  + " (" + Math.round(a8 / sum * 10000) / 100.0 + "%)" + "</td>";
								rows[10] += "<td>" + a9  + " (" + Math.round(a9 / sum * 10000) / 100.0 + "%)" + "</td>";
								rows[11] += "<td>" + a10  + " (" + Math.round(a10 / sum * 10000) / 100.0 + "%)" + "</td>";
								startIndex++;
							}
						%>
						<table id="table_1" class="display" cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>日期</th>
									<%=rows[0]%>
								</tr>
							</thead>
							<tbody>
								<tr>
									<th>条件人数</th>
									<%=rows[1]%>
								</tr>
								<tr>
									<th>无挂机</th>
									<%=rows[2]%>
								</tr>
								<tr>
									<th>10m</th>
									<%=rows[3]%>
								</tr>
								<tr>
									<th>30m</th>
									<%=rows[4]%>
								</tr>
								<tr>
									<th>60m</th>
									<%=rows[5]%>
								</tr>
								<tr>
									<th>90m</th>
									<%=rows[6]%>
								</tr>
								<tr>
									<th>120m</th>
									<%=rows[7]%>
								</tr>
								<tr>
									<th>3h</th>
									<%=rows[8]%>
								</tr>
								<tr>
									<th>4h</th>
									<%=rows[9]%>
								</tr>
								<tr>
									<th>5h</th>
									<%=rows[10]%>
								</tr>
								<tr>
									<th>5h+</th>
									<%=rows[11]%>
								</tr>
							</tbody>
						</table>
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
