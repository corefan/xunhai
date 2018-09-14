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
	JSONArray retaindData = new JSONArray();
	
	String dataStr = "0,0,0,0,0,0,0,0,0";
	if (data != null) {
		retaindData = (JSONArray) data.getJSONArray("dataList");
		if (retaindData.length() > 0) {
			JSONObject obj = retaindData.getJSONObject(retaindData.length() - 1);
			dataStr = obj.getString("rate1") + "," + obj.getString("rate2") 
				+ "," + obj.getString("rate3") + "," + obj.getString("rate4")
				+ "," + obj.getString("rate5") + "," + obj.getString("rate6")
				+ "," + obj.getString("rate7") + "," + obj.getString("rate14")
				+ "," + obj.getString("rate30");
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
		
		var table = $('#table_1').DataTable();
		$('#table_1 tbody').on('click', 'tr', function () {
			var dateStr = "";
			var dataArr = new Array();
	        if (!$(this).hasClass('selected')) {
	            table.$('tr.selected').removeClass('selected');
	            $(this).addClass('selected');
	        }
	        $(this).children().each(function(index){
	        	if (index == 0) {
	        		var dateArr = (this.innerHTML).split("-");
	        		dateStr = Number(dateArr[0]) + " 年 " + Number(dateArr[1]) + " 月 " + Number(dateArr[2]) + " 日";
	        	} else if (index > 1) {
	        		dataArr.push(Number((this.innerHTML).replace('%', '')));
	        	}
	        }
	        );
	        var chart = $('#chart_1').highcharts();
        	chart.series[0].setData(dataArr);
        	chart.setTitle({ text: '留存率曲线图'}, { text: dateStr});
	    } );
		
		$('.datetimepicker').datetimepicker({
			lang : 'ch',
			timepicker : false,
			format : 'Y-m-d',
			formatDate : 'Y-m-d',
		});
		
		$('#chart_1').highcharts({
			chart : {
				type : 'spline'
			},
			title : {
				text : '留存率曲线图'
			},
			subtitle : {
				text : '<%= y + " 年" + m + " 月 " + d + " 日"%>'
			},
			legend : {
				enabled : false
			},
			xAxis : {
				categories : ["次日","3日","4日","5日","6日","7日","8日","双周","月"]
			},
			yAxis : {
				title : {
					text : '留存率'
				},
				labels : {
					formatter : function() {
						return this.value
					}
				}
			},
			tooltip : {
				crosshairs : true,
				shared : true,
				valueSuffix : " %"
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
				name : '留存率',
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
					<form action="<%=path %>/dataAnalysis" method="post">
						<input type="hidden" name="optType" value="<%= OptTypeConstant.DATA_ANALYSIS_4%>" />
						<input type="text" class="datetimepicker" name="startDate" value="<%= startDate %>" /> <span> - </span>
						<input type="text" class="datetimepicker" name="endDate" value="<%= endDate %>" /><span>&nbsp;&nbsp;</span>
						<input type="submit" value="确定" class="btn_1" />
					</form>
				</div>
				<div class="content_panel">
					<div class="content_panel_title">
						<h3>留存分析</h3>
					</div>
					<div class="content_panel_content">
						<div id="chart_1" class="chart" style="min-width:500px;height:400px"></div>
						<table id="table_1" class="display" cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>日期</th>
									<th>注册人数</th>
									<th>次日留存率</th>
									<th>3日留存率</th>
									<th>4日留存率</th>
									<th>5日留存率</th>
									<th>6日留存率</th>
									<th>7日留存率</th>
									<!-- <th>8日留存率</th> -->
									<th>双周留存率</th>
									<th>月留存率</th>
								</tr>
							</thead>
	
							<tbody>
								<%
									for (int i = 0; i < retaindData.length(); i++) {
										JSONObject obj = retaindData.getJSONObject(i);
								%>
								<tr>
									<td><%=obj.getString("time")%></td>
									<td><%=obj.getString("registerNum")%></td>
									<td><%=obj.getString("rate1") + "%"%></td>
									<td><%=obj.getString("rate2") + "%"%></td>
									<td><%=obj.getString("rate3") + "%"%></td>
									<td><%=obj.getString("rate4") + "%"%></td>
									<td><%=obj.getString("rate5") + "%"%></td>
									<td><%=obj.getString("rate6") + "%"%></td>
									<!-- <td><%=obj.getString("rate7") + "%"%></td> -->
									<td><%=obj.getString("rate14") + "%"%></td>
									<td><%=obj.getString("rate30") + "%"%></td>
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
