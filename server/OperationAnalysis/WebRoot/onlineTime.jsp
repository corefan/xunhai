<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<%@page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
			
	String startDate = (String)request.getAttribute("startDate");
	String endDate = (String)request.getAttribute("endDate");
	int y = 0;
	int m = 0;
	int d = 0;
	if (endDate != null) {
		y = Integer.parseInt(endDate.substring(0, 4));
		m = Integer.parseInt(endDate.substring(5, 7));
		d = Integer.parseInt(endDate.substring(8, endDate.length()));
	}

	JSONObject data = (JSONObject) request.getAttribute("data");
	JSONArray onlineTimeData = new JSONArray();
	if (data != null) {
		onlineTimeData = (JSONArray) data
				.getJSONArray("onlineTimeList");
	}

	String dataStr = "";
	if (onlineTimeData.length() > 0) {
		JSONObject obj = onlineTimeData.getJSONObject(onlineTimeData.length() - 1);
		dataStr += obj.getString("num1") + "," + obj.getString("num5")
		+ "," + obj.getString("num10") + "," + obj.getString("num20") + "," + obj.getString("num30") 
		+ "," + obj.getString("num40") + "," + obj.getString("num50") + "," + obj.getString("num60") 
		+ "," + obj.getString("h5") + "," + obj.getString("h10") + "," + obj.getString("uph10");
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
			"bFilter" : false,
			"bSort":false,
		});

		$('.datetimepicker').datetimepicker({
			lang : 'ch',
			timepicker : false,
			format : 'Y-m-d',
			formatDate : 'Y-m-d',
		});

		$('#chart_1').highcharts(
				{
					chart : {
						type : 'column',
						margin : [ 50, 50, 100, 80 ]
					},
					title : {
						text : '在线时长分布图',
					},
					subtitle : {
						text : '<%= y + " 年" + m + " 月 " + d + " 日"%>'
					},
					xAxis : {
						categories : [ '1m', '5m', '10m', '20m', '30m', '40m',
								'50m', '60m', '5h', '10h', '10h↑' ],
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
							text : '时长人数(单位：人)'
						}
					},
					legend : {
						enabled : false
					},
					tooltip : {
						pointFormat : '时长人数: <b>{point.y} </b>',
					},
					series : [ {
						name : '',
						data : [<%=dataStr%>],
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
						<input type="hidden" name="optType" value="<%= OptTypeConstant.DATA_ANALYSIS_21%>" /> <input
							type="text" class="datetimepicker" name="startDate"
							value="<%=startDate%>" /> <span>
							- </span> <input type="text" class="datetimepicker" name="endDate"
							value="<%=endDate%>" /><span>&nbsp;&nbsp;</span>
						<input type="submit" value="确定" class="btn_1" />
					</form>
				</div>
				<div class="content_panel">
					<div class="content_panel_title">
						<h3>在线时长分析</h3>
					</div>
					<div class="content_panel_content">
						<div id="chart_1" class="chart"
							style="min-width:500px;height:400px;margin-bottom:-60px"></div>
						<%
							int dataSize = onlineTimeData.length();
							int startIndex = 0;
							if (dataSize > 7) {
								startIndex = dataSize - 7;
							}
							String[] rows = { "", "", "", "", "", "", "", "", "", "", "", "", "" };
							int validDataSize = dataSize - startIndex;
							for (int i = 0; i < validDataSize; i++) {
								JSONObject obj = onlineTimeData.getJSONObject(startIndex);
								int num = Integer.parseInt(obj.getString("loginNum"));
								int num1 = Integer.parseInt(obj.getString("num1"));
								int num5 = Integer.parseInt(obj.getString("num5"));
								int num10 = Integer.parseInt(obj.getString("num10"));
								int num20 = Integer.parseInt(obj.getString("num20"));
								int num30 = Integer.parseInt(obj.getString("num30"));
								int num40 = Integer.parseInt(obj.getString("num40"));
								int num50 = Integer.parseInt(obj.getString("num50"));
								int num60 = Integer.parseInt(obj.getString("num60"));
								int h5 = Integer.parseInt(obj.getString("h5"));
								int h10 = Integer.parseInt(obj.getString("h10"));
								int uph10 = Integer.parseInt(obj.getString("uph10"));
								rows[0] += "<th>" + obj.getString("date") + "</th>";
								rows[1] += "<td>" + num + "</td>";
								rows[2] += "<td>" + num1  + " (" + Math.round(num1 * 100.0 / num) + "%)" + "</td>";
								rows[3] += "<td>" + num5  + " (" + Math.round(num5 * 100.0 / num) + "%)" + "</td>";
								rows[4] += "<td>" + num10  + " (" + Math.round(num10 * 100.0 / num) + "%)" + "</td>";
								rows[5] += "<td>" + num20  + " (" + Math.round(num20 * 100.0 / num) + "%)" + "</td>";
								rows[6] += "<td>" + num30  + " (" + Math.round(num30 * 100.0 / num) + "%)" + "</td>";
								rows[7] += "<td>" + num40  + " (" + Math.round(num40 * 100.0 / num) + "%)" + "</td>";
								rows[8] += "<td>" + num50  + " (" + Math.round(num50 * 100.0 / num) + "%)" + "</td>";
								rows[9] += "<td>" + num60  + " (" + Math.round(num60 * 100.0 / num) + "%)" + "</td>";
								rows[10] += "<td>" + h5  + " (" + Math.round(h5 * 100.0 / num) + "%)" + "</td>";
								rows[11] += "<td>" + h10  + " (" + Math.round(h10 * 100.0 / num) + "%)" + "</td>";
								rows[12] += "<td>" + uph10  + " (" + Math.round(uph10 * 100.0 / num) + "%)" + "</td>";
								
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
									<th>登录玩家数</th>
									<%=rows[1]%>
								</tr>
								<tr>
									<th>1分钟人数</th>
									<%=rows[2]%>
								</tr>
								<tr>
									<th>5分钟人数</th>
									<%=rows[3]%>
								</tr>
								<tr>
									<th>10分钟人数</th>
									<%=rows[4]%>
								</tr>
								<tr>
									<th>20分钟人数</th>
									<%=rows[5]%>
								</tr>
								<tr>
									<th>30分钟人数</th>
									<%=rows[6]%>
								</tr>
								<tr>
									<th>40分钟人数</th>
									<%=rows[7]%>
								</tr>
								<tr>
									<th>50分钟人数</th>
									<%=rows[8]%>
								</tr>
								<tr>
									<th>60分钟人数</th>
									<%=rows[9]%>
								</tr>
								<tr>
									<th>5小时人数</th>
									<%=rows[10]%>
								</tr>
								<tr>
									<th>10小时人数</th>
									<%=rows[11]%>
								</tr>
								<tr>
									<th>10小时以上人数</th>
									<%=rows[12]%>
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
