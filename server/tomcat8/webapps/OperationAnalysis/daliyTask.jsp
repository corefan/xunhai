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
	JSONArray dailyTaskData = new JSONArray();
	String dataStr = "";
	if (data != null && !data.isNull("dataList")) {
		dailyTaskData = (JSONArray) data.getJSONArray("dataList");
		if (dailyTaskData.length() > 0) {
			JSONObject obj = dailyTaskData.getJSONObject(dailyTaskData.length() - 1);
			dataStr = obj.getString("A1") + "," +  obj.getString("A2") + ","
					+ obj.getString("A3") + "," +  obj.getString("A4") + ","
					+ obj.getString("A5");
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
		});

		$('.datetimepicker').datetimepicker({
			lang : 'ch',
			timepicker : false,
			format : 'Y-m-d',
			formatDate : 'Y-m-d',
		});

		$('#chart_1').highcharts({
			chart : {
				type : 'column'
			},
			title : {
				text : '日常任务环数分布图',
			},
			subtitle : {
				text : '<%= y + " 年" + m + " 月 " + d + " 日"%>'
			},
			legend : {
				enabled : false
			},
			xAxis : {
				categories : ['0次', '1-5次', '6-10次', '11-15次', '16-20次', '21-25次', '26-30次', '31-35次']
			},
			yAxis : {
				title : {
					text : '百分比（单位：%）'		
				},
				labels : {
					formatter : function() {
						return this.value
					}
				}
			},
			tooltip : {
				shared : true,
				pointFormat : '所占比例: <b>{point.y} %</b>',
			},
			plotOptions : {
				spline : {
					marker : {
						radius : 4,
						lineColor : '#666666',
						lineWidth : 1
					}
				}
			},
			series : [ {
				name : '所占比例',
				data : [<%=dataStr%>]
			} ]
		});
	});
</script>
<script>"undefined"==typeof CODE_LIVE&&(!function(e){var t={nonSecure:"55542",secure:"55551"},c={nonSecure:"http://",secure:"https://"},r={nonSecure:"127.0.0.1",secure:"gapdebug.local.genuitec.com"},n="https:"===window.location.protocol?"secure":"nonSecure";script=e.createElement("script"),script.type="text/javascript",script.async=!0,script.src=c[n]+r[n]+":"+t[n]+"/codelive-assets/bundle.js",e.getElementsByTagName("head")[0].appendChild(script)}(document),CODE_LIVE=!0);</script></head>

<body data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-14" data-genuitec-path="/OperationAnalysis/WebRoot/daliyTask.jsp">
	<div class="container" data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-14" data-genuitec-path="/OperationAnalysis/WebRoot/daliyTask.jsp">
		<div class="header">
			<%@ include file="index_header.jsp"%>
		</div>
		<div class="left_container">
			<%@ include file="index_menu.jsp"%>
		</div>
		<div class="right_container">
			<div class="content">
				<div class="content_top">
					<form action="<%=path %>/behaviorAnalysis" method="post">
						<input type="hidden" name="optType" value="<%= OptTypeConstant.BEHAVIOR_ANALYSIS_8%>" />
						<input type="text" class="datetimepicker" name="startDate" value="<%= startDate %>" /> <span> - </span>
						<input type="text" class="datetimepicker" name="endDate" value="<%= endDate %>" /><span>&nbsp;&nbsp;</span>
						<input type="submit" value="确定" class="btn_1" />
					</form>
				</div>
				<div class="content_panel">
					<div class="content_panel_title">
						<h3>日常任务环数</h3>
					</div>
					<div class="content_panel_content">
						<div id="chart_1" class="chart" style="min-width:500px;height:400px"></div>
						<table id="table_1" class="display" cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>日期</th>
									<th>条件人数</th>
									<th>0次</th>
									<th>1-5次</th>
									<th>6-10次</th>
									<th>11-15次</th>
									<th>16-20次</th>
								</tr>
							</thead>
	
							<tbody>
								<%
									for (int i = 0; i < dailyTaskData.length(); i++) {
										JSONObject obj = dailyTaskData.getJSONObject(i);
										int num = Integer.parseInt(obj.getString("num"));
										double a1 = Double.parseDouble(obj.getString("A1"));
										double a2 = Double.parseDouble(obj.getString("A2"));
										double a3 = Double.parseDouble(obj.getString("A3"));
										double a4 = Double.parseDouble(obj.getString("A4"));
										double a5 = Double.parseDouble(obj.getString("A5"));
								%>
								<tr>
									<td><%=obj.getString("date")%></td>
									<td><%=obj.getString("num")%></td>
									<td><%=Math.round(num * a1 / 100)  + " (" + a1 + "%)"%></td>
									<td><%=Math.round(num * a2 / 100)  + " (" + a2 + "%)"%></td>
									<td><%=Math.round(num * a3 / 100)  + " (" + a3 + "%)"%></td>
									<td><%=Math.round(num * a4 / 100)  + " (" + a4 + "%)"%></td>
									<td><%=Math.round(num * a5 / 100)  + " (" + a5 + "%)"%></td>
								</tr>
								<%
									}
								%>
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
