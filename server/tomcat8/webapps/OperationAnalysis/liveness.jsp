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
	String dataStr_1 = ""; // 0-19
	String dataStr_2 = ""; // 20-49
	String dataStr_3 = ""; // 50-89
	String dataStr_4 = ""; // 90-139
	String dataStr_5 = ""; // 140-199
	String dataStr_6 = ""; // 200+
	if (data != null && !data.isNull("activeMonitor")) {
		livenessData = (JSONArray) data.getJSONArray("activeMonitor");
		for (int i = 0; i < livenessData.length(); i++) {
			JSONObject obj = livenessData.getJSONObject(i);
			String date = obj.getString("CREATE_TIME");
			xAxisStr += "'" + (date.substring(5, date.length())).replace("-", "/")
					+ "',";
			dataStr_1 += obj.getString("A1") + ",";
			dataStr_2 += obj.getString("A2") + ",";
			dataStr_3 += obj.getString("A3") + ",";
			dataStr_4 += obj.getString("A4") + ",";
			dataStr_5 += obj.getString("A5") + ",";
			dataStr_6 += obj.getString("A6") + ",";
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
								name : '200+',
								data : [ <%= dataStr_6%> ]
							}, {
								name : '140-199',
								data : [ <%= dataStr_5%> ]
							}, {
								name : '90-139',
								data : [ <%= dataStr_4%> ]
							}, {
								name : '50-89',
								data : [ <%= dataStr_3%> ]
							}, {
								name : '20-49',
								data : [ <%= dataStr_2%> ]
							}, {
								name : '0-19',
								data : [ <%= dataStr_1%> ]
							} ]
						});
	});
</script>
</head>

<body>
	<div class="container">
		<div class="header">
			<%@ include file="index_header.jsp"%>
		</div>
		<div class="left_container">
			<%@ include file="index_menu.jsp"%>
		</div>
		<div class="right_container">
			<div class="content">
				<div class="content_top">
					<form action="<%=path%>/dataAnalysis" method="post">
						<input type="hidden" name="optType" value="<%= OptTypeConstant.DATA_ANALYSIS_20%>" /> <input
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
							String[] rows = { "", "", "", "", "", "", "", "" };
							int validDataSize = dataSize - startIndex;
							for (int i = 0; i < validDataSize; i++) {
								JSONObject obj = livenessData.getJSONObject(startIndex);
								int num = Integer.parseInt(obj.getString("LOGINNUM"));
								double a1 = Double.parseDouble(obj.getString("A1"));
								double a2 = Double.parseDouble(obj.getString("A2"));
								double a3 = Double.parseDouble(obj.getString("A3"));
								double a4 = Double.parseDouble(obj.getString("A4"));
								double a5 = Double.parseDouble(obj.getString("A5"));
								double a6 = Double.parseDouble(obj.getString("A6"));
								rows[0] += "<th>" + obj.getString("CREATE_TIME") + "</th>";
								rows[1] += "<td>" + num + "</td>";
								rows[2] += "<td>" + Math.round(num * a1 / 100)  + " (" + a1 + "%)" + "</td>";
								rows[3] += "<td>" + Math.round(num * a2 / 100)  + " (" + a2 + "%)" + "</td>";
								rows[4] += "<td>" + Math.round(num * a3 / 100)  + " (" + a3 + "%)" + "</td>";
								rows[5] += "<td>" + Math.round(num * a4 / 100)  + " (" + a4 + "%)" + "</td>";
								rows[6] += "<td>" + Math.round(num * a5 / 100)  + " (" + a5 + "%)" + "</td>";
								rows[7] += "<td>" + Math.round(num * a6 / 100)  + " (" + a6 + "%)" + "</td>";
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
									<th>登录人数</th>
									<%=rows[1]%>
								</tr>
								<tr>
									<th>0-19</th>
									<%=rows[2]%>
								</tr>
								<tr>
									<th>20-49</th>
									<%=rows[3]%>
								</tr>
								<tr>
									<th>50-89</th>
									<%=rows[4]%>
								</tr>
								<tr>
									<th>90-139</th>
									<%=rows[5]%>
								</tr>
								<tr>
									<th>140-199</th>
									<%=rows[6]%>
								</tr>
								<tr>
									<th>200+</th>
									<%=rows[7]%>
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
