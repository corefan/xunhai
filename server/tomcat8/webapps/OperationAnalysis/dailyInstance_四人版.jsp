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
	JSONArray dailyInstanceData = new JSONArray();
	if (data != null) {
		dailyInstanceData = (JSONArray) data.getJSONArray("dataList");
	}
	
	String data_1 = "";
	String data_2 = "";
	String data_3 = "";
	if (dailyInstanceData.length() > 0) {
		JSONObject obj = dailyInstanceData.getJSONObject(dailyInstanceData.length() - 1);
		data_1 = "['0次'," + obj.getString("A0") + "], ['1次'," + obj.getString("A1") 
			+ "], ['2次'," + obj.getString("A2") + "], ['3次'," + obj.getString("A3")
			+ "], ['4次'," + obj.getString("A4") + "]"; 
		data_2 = "['0次'," + obj.getString("B0") + "], ['1次'," + obj.getString("B1") 
			+ "], ['2次'," + obj.getString("B2") + "], ['3次'," + obj.getString("B3")
			+ "], ['4次'," + obj.getString("B4") + "]"; 
		data_3 = "['0次'," + obj.getString("C0") + "], ['1次'," + obj.getString("C1") 
			+ "], ['2次'," + obj.getString("C2") + "], ['3次'," + obj.getString("C3")
			+ "], ['4次'," + obj.getString("C4") + "]"; 
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
		
		$('#chart_1').highcharts({
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false
            },
            title: {
                text: '异界空间通关次数饼状图'
            },
            subtitle : {
				text : '<%= y + " 年" + m + " 月 " + d + " 日"%>'
			},
            tooltip: {
        	    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: true
                }
            },
            series: [{
                type: 'pie',
                name: '所占比例：',
                data: [<%=data_1%>]
            }]
        });
        
        $('#chart_2').highcharts({
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false
            },
            title: {
                text: '宝藏宫殿通关次数饼状图'
            },
            subtitle : {
				text : '<%= y + " 年" + m + " 月 " + d + " 日"%>'
			},
            tooltip: {
        	    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: true
                }
            },
            series: [{
                type: 'pie',
                name: '所占比例：',
                data: [<%=data_2%>],
            }]
        });
        
        $('#chart_3').highcharts({
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false
            },
            title: {
                text: '守护女神通关次数饼状图'
            },
            subtitle : {
				text : '<%= y + " 年" + m + " 月 " + d + " 日"%>'
			},
            tooltip: {
        	    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: true
                }
            },
            series: [{
                type: 'pie',
                name: '所占比例：',
                data: [<%=data_3%>],
            }]
        });
	});
</script>
<script>"undefined"==typeof CODE_LIVE&&(!function(e){var t={nonSecure:"55542",secure:"55551"},c={nonSecure:"http://",secure:"https://"},r={nonSecure:"127.0.0.1",secure:"gapdebug.local.genuitec.com"},n="https:"===window.location.protocol?"secure":"nonSecure";script=e.createElement("script"),script.type="text/javascript",script.async=!0,script.src=c[n]+r[n]+":"+t[n]+"/codelive-assets/bundle.js",e.getElementsByTagName("head")[0].appendChild(script)}(document),CODE_LIVE=!0);</script></head>

<body data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-13" data-genuitec-path="/OperationAnalysis/WebRoot/dailyInstance_四人版.jsp">
	<div class="container" data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-13" data-genuitec-path="/OperationAnalysis/WebRoot/dailyInstance_四人版.jsp">
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
						<input type="hidden" name="optType" value="<%= OptTypeConstant.BEHAVIOR_ANALYSIS_2%>" />
						<input type="text" class="datetimepicker" name="startDate" value="<%= startDate %>" /> <span> - </span>
						<input type="text" class="datetimepicker" name="endDate" value="<%= endDate %>" /><span>&nbsp;&nbsp;</span>
						<input type="submit" value="确定" class="btn_1" />
					</form>
				</div>
				<div class="content_panel">
					<div class="content_panel_title">
						<h3>每日副本情况</h3>
					</div>
					<div class="content_panel_content">
						<div id="chart_1" class="chart" style="min-width:200px;height:400px;width:33%;float:left;"></div>
						<div id="chart_2" class="chart" style="min-width:200px;height:400px;width:33%;float:left;"></div>
						<div id="chart_3" class="chart" style="min-width:200px;height:400px;width:33%;float:left;"></div>
						<%
							int dataSize = dailyInstanceData.length();
							int startIndex = 0;
							if (dataSize > 7) {
								startIndex = dataSize - 7;
							}
							String[] rows = { "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" };
							int validDataSize = dataSize - startIndex;
							for (int i = 0; i < validDataSize; i++) {
								JSONObject obj = dailyInstanceData.getJSONObject(startIndex);
								int num = Integer.parseInt(obj.getString("num"));
								double a0 = Double.parseDouble(obj.getString("A0"));
								double a1 = Double.parseDouble(obj.getString("A1"));
								double a2 = Double.parseDouble(obj.getString("A2"));
								double a3 = Double.parseDouble(obj.getString("A3"));
								double a4 = Double.parseDouble(obj.getString("A4"));
								double b0 = Double.parseDouble(obj.getString("B0"));
								double b1 = Double.parseDouble(obj.getString("B1"));
								double b2 = Double.parseDouble(obj.getString("B2"));
								double b3 = Double.parseDouble(obj.getString("B3"));
								double b4 = Double.parseDouble(obj.getString("B4"));
								double c0 = Double.parseDouble(obj.getString("C0"));
								double c1 = Double.parseDouble(obj.getString("C1"));
								double c2 = Double.parseDouble(obj.getString("C2"));
								double c3 = Double.parseDouble(obj.getString("C3"));
								double c4 = Double.parseDouble(obj.getString("C4"));
								rows[0] += "<th>" + obj.getString("date") + "</th>";
								rows[1] += "<td>" + obj.getString("num") + "</td>";
								rows[2] += "<td>" + Math.round(num * a0 / 100)  + " (" + a0 + "%)" + "</td>";
								rows[3] += "<td>" + Math.round(num * a1 / 100)  + " (" + a1 + "%)" + "</td>";
								rows[4] += "<td>" + Math.round(num * a2 / 100)  + " (" + a2 + "%)" + "</td>";
								rows[5] += "<td>" + Math.round(num * a3 / 100)  + " (" + a3 + "%)" + "</td>";
								rows[6] += "<td>" + Math.round(num * a4 / 100)  + " (" + a4 + "%)" + "</td>";
								rows[7] += "<td>" + Math.round(num * b0 / 100)  + " (" + b0 + "%)" + "</td>";
								rows[8] += "<td>" + Math.round(num * b1 / 100)  + " (" + b1 + "%)" + "</td>";
								rows[9] += "<td>" + Math.round(num * b2 / 100)  + " (" + b2 + "%)" + "</td>";
								rows[10] += "<td>" + Math.round(num * b3 / 100)  + " (" + b3 + "%)" + "</td>";
								rows[11] += "<td>" + Math.round(num * b4 / 100)  + " (" + b4 + "%)" + "</td>";
								rows[12] += "<td>" + Math.round(num * c0 / 100)  + " (" + c0 + "%)" + "</td>";
								rows[13] += "<td>" + Math.round(num * c1 / 100)  + " (" + c1 + "%)" + "</td>";
								rows[14] += "<td>" + Math.round(num * c2 / 100)  + " (" + c2 + "%)" + "</td>";
								rows[15] += "<td>" + Math.round(num * c3 / 100)  + " (" + c3 + "%)" + "</td>";
								rows[16] += "<td>" + Math.round(num * c4 / 100)  + " (" + c4 + "%)" + "</td>";
								startIndex++;
							}
						%>
						<table id="table_1" class="display" cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>时间</th>
									<%=rows[0]%>
								</tr>
							</thead>
							<tbody>
								<tr>
									<th>条件人数</th>
									<%=rows[1]%>
								</tr>
								<tr>
									<th>异界空间_0次</th>
									<%=rows[2]%>
								</tr>
								<tr>
									<th>异界空间_1次</th>
									<%=rows[3]%>
								</tr>
								<tr>
									<th>异界空间_2次</th>
									<%=rows[4]%>
								</tr>
								<tr>
									<th>异界空间_3次</th>
									<%=rows[5]%>
								</tr>
								<tr>
									<th>异界空间_4次</th>
									<%=rows[6]%>
								</tr>
								<tr>
									<th>宝藏宫殿_0次</th>
									<%=rows[7]%>
								</tr>
								<tr>
									<th>宝藏宫殿_1次</th>
									<%=rows[8]%>
								</tr>
								<tr>
									<th>宝藏宫殿_2次</th>
									<%=rows[9]%>
								</tr>
								<tr>
									<th>宝藏宫殿_3次</th>
									<%=rows[10]%>
								</tr>
								<tr>
									<th>宝藏宫殿_4次</th>
									<%=rows[11]%>
								</tr>
								<tr>
									<th>守护女神_0次</th>
									<%=rows[12]%>
								</tr>
								<tr>
									<th>守护女神_1次</th>
									<%=rows[13]%>
								</tr>
								<tr>
									<th>守护女神_2次</th>
									<%=rows[14]%>
								</tr>
								<tr>
									<th>守护女神_3次</th>
									<%=rows[15]%>
								</tr>
								<tr>
									<th>守护女神_4次</th>
									<%=rows[16]%>
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
