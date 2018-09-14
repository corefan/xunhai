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

	JSONObject basicData = (JSONObject) request.getAttribute("basicData");
	JSONArray castlevaniaData = new JSONArray();
	String dataStr = "";
	if (basicData != null && !basicData.isNull("dataList")) {
		castlevaniaData = (JSONArray) basicData.getJSONArray("dataList");
		if (castlevaniaData.length() > 0) {
			JSONObject obj = castlevaniaData.getJSONObject(castlevaniaData.length() - 1);
			dataStr = obj.getString("A0") + "," + obj.getString("A1") + "," + obj.getString("A2") + ","
					+ obj.getString("A3") + "," + obj.getString("A4") + ","
					+ obj.getString("A5") + "," + obj.getString("A6") + ","
					+ obj.getString("A7") + "," + obj.getString("A8") + ","
					+ obj.getString("A9") + "," + obj.getString("A10");
		}
	}
	
	JSONObject bossData = (JSONObject) request.getAttribute("bossData");
	JSONArray castlevaniaBossData = new JSONArray();
	if (bossData != null && !bossData.isNull("dataList")) {
		castlevaniaBossData = (JSONArray) bossData.getJSONArray("dataList");
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
		
		$('#table_2').dataTable({
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

		$('#chart_1').highcharts({
			chart : {
				type : 'column'
			},
			title : {
				text : '恶魔城情况分布图',
			},
			subtitle : {
				text : '<%= y + " 年" + m + " 月 " + d + " 日"%>'
			},
			legend : {
				enabled : false
			},
			xAxis : {
				categories : ['0次', '1次', '2次', '3次', '4次', '5次', '6次']
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
					<form action="<%=path %>/behaviorAnalysis" method="post">
						<input type="hidden" name="optType" value="<%= OptTypeConstant.BEHAVIOR_ANALYSIS_4%>" />
						<input type="text" class="datetimepicker" name="startDate" value="<%= startDate %>" /> <span> - </span>
						<input type="text" class="datetimepicker" name="endDate" value="<%= endDate %>" /><span>&nbsp;&nbsp;</span>
						<input type="submit" value="确定" class="btn_1" />
					</form>
				</div>
				<div class="content_panel">
					<div class="content_panel_title">
						<h3>恶魔城情况</h3>
					</div>
					<div class="content_panel_content">
						<div id="chart_1" class="chart" style="min-width:500px;height:400px"></div>
						<table id="table_1" class="display" cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>日期</th>
									<th>条件人数</th>
									<th>0次</th>
									<th>1次</th>
									<th>2次</th>
									<th>3次</th>
									<th>4次</th>
									<th>5次</th>
									<th>6次</th>
									<th>7次</th>
									<th>8次</th>
									<th>9次</th>
									<th>10次</th>
								</tr>
							</thead>
	
							<tbody>
								<%
									for (int i = 0; i < castlevaniaData.length(); i++) {
										JSONObject obj = castlevaniaData.getJSONObject(i);
										int num = Integer.parseInt(obj.getString("num"));
										double a0 = Double.parseDouble(obj.getString("A0"));
										double a1 = Double.parseDouble(obj.getString("A1"));
										double a2 = Double.parseDouble(obj.getString("A2"));
										double a3 = Double.parseDouble(obj.getString("A3"));
										double a4 = Double.parseDouble(obj.getString("A4"));
										double a5 = Double.parseDouble(obj.getString("A5"));
										double a6 = Double.parseDouble(obj.getString("A6"));
										double a7 = Double.parseDouble(obj.getString("A7"));
										double a8 = Double.parseDouble(obj.getString("A8"));
										double a9 = Double.parseDouble(obj.getString("A9"));
										double a10 = Double.parseDouble(obj.getString("A10"));
								%>
								<tr>
									<td><%=obj.getString("date")%></td>
									<td><%=obj.getString("num")%></td>
									<td><%=Math.round(num * a0 / 100)  + " (" + a0 + "%)"%></td>
									<td><%=Math.round(num * a1 / 100)  + " (" + a1 + "%)"%></td>
									<td><%=Math.round(num * a2 / 100)  + " (" + a2 + "%)"%></td>
									<td><%=Math.round(num * a3 / 100)  + " (" + a3 + "%)"%></td>
									<td><%=Math.round(num * a4 / 100)  + " (" + a4 + "%)"%></td>
									<td><%=Math.round(num * a5 / 100)  + " (" + a5 + "%)"%></td>
									<td><%=Math.round(num * a6 / 100)  + " (" + a6 + "%)"%></td>
									<td><%=Math.round(num * a7 / 100)  + " (" + a7 + "%)"%></td>
									<td><%=Math.round(num * a8 / 100)  + " (" + a8 + "%)"%></td>
									<td><%=Math.round(num * a9 / 100)  + " (" + a9 + "%)"%></td>
									<td><%=Math.round(num * a10 / 100)  + " (" + a10 + "%)"%></td>
								</tr>
								<%
									}
								%>
							</tbody>
						</table>
						
						<%
							int dataSize = castlevaniaBossData.length();
							int startIndex = 0;
							if (dataSize > 7) {
								startIndex = dataSize - 7;
							}
							String[] rows = { "", "", "", "", "", "", "", "", "", "", "", "", "", ""
									, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" };
							int validDataSize = dataSize - startIndex;
							for (int i = 0; i < validDataSize; i++) {
								JSONObject obj = castlevaniaBossData.getJSONObject(startIndex);
								rows[0] += "<th>" + obj.getString("date") + "</th>";
								rows[1] += "<td>" + obj.getString("sum") + "</td>";
								for (int j = 1; j < 31; j++) {
									rows[j + 1] += "<td>" + obj.getString("A" + j) + "</td>";
								}
								startIndex++;
							}
						%>
						<table id="table_2" class="display" cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>时间</th>
									<%=rows[0]%>
								</tr>
							</thead>
							<tbody>
								<tr>
									<th>击杀总次数</th>
									<%=rows[1]%>
								</tr>
								<tr>
									<th>丛林守护者</th>
									<%=rows[2]%>
								</tr>
								<tr>
									<th>熔岩巨兽</th>
									<%=rows[3]%>
								</tr>
								<tr>
									<th>堕落菲尼克</th>
									<%=rows[4]%>
								</tr>
								<tr>
									<th>遗忘者</th>
									<%=rows[5]%>
								</tr>
								<tr>
									<th>暗黑魔龙</th>
									<%=rows[6]%>
								</tr>
								<tr>
									<th>暗黑女巫</th>
									<%=rows[7]%>
								</tr>
								<tr>
									<th>幽灵公主</th>
									<%=rows[8]%>
								</tr>
								<tr>
									<th>炎狼</th>
									<%=rows[9]%>
								</tr>
								<tr>
									<th>利爪兽</th>
									<%=rows[10]%>
								</tr>
								<tr>
									<th>巨斧牛魔</th>
									<%=rows[11]%>
								</tr>
								<tr>
									<th>大袍男爵</th>
									<%=rows[12]%>
								</tr>
								<tr>
									<th>变异红龙</th>
									<%=rows[13]%>
								</tr>
								<tr>
									<th>树精卫士</th>
									<%=rows[14]%>
								</tr>
								<tr>
									<th>火炎巨兽</th>
									<%=rows[15]%>
								</tr>
								<tr>
									<th>吸血男爵</th>
									<%=rows[16]%>
								</tr>
								<tr>
									<th>亡灵法师</th>
									<%=rows[17]%>
								</tr>
								<tr>
									<th>火焰之王</th>
									<%=rows[18]%>
								</tr>
								<tr>
									<th>魔神之仆</th>
									<%=rows[19]%>
								</tr>
								<tr>
									<th>亡灵统领</th>
									<%=rows[20]%>
								</tr>
								<tr>
									<th>狼王杜蕾斯</th>
									<%=rows[21]%>
								</tr>
								<tr>
									<th>迅猛兽</th>
									<%=rows[22]%>
								</tr>
								<tr>
									<th>牛魔战将</th>
									<%=rows[23]%>
								</tr>
								<tr>
									<th>暗影术士</th>
									<%=rows[24]%>
								</tr>
								<tr>
									<th>地狱魔龙</th>
									<%=rows[25]%>
								</tr>
								<tr>
									<th>变异树精</th>
									<%=rows[26]%>
								</tr>
								<tr>
									<th>觉醒者</th>
									<%=rows[27]%>
								</tr>
								<tr>
									<th>德莱文</th>
									<%=rows[28]%>
								</tr>
								<tr>
									<th>魔化贤者</th>
									<%=rows[29]%>
								</tr>
								<tr>
									<th>红龙拉克丝</th>
									<%=rows[30]%>
								</tr>
								<tr>
									<th>魅惑女王</th>
									<%=rows[31]%>
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
