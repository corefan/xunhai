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
	if (data != null && !data.isNull("dataList")) {
		dailyInstanceData = (JSONArray) data.getJSONArray("dataList");
	}
	
	String data_1 = "";
	String data_2 = "";
	String data_3 = "";
	if (dailyInstanceData.length() > 0) {
		JSONObject obj = dailyInstanceData.getJSONObject(dailyInstanceData.length() - 1);
		data_1 = "['0次'," + obj.getString("A0") + "], ['1次'," + obj.getString("A1") 
			+ "], ['2次'," + obj.getString("A2") + "], ['3次'," + obj.getString("A3")
			+ "], ['4次'," + obj.getString("A4") + "], ['5次'," + obj.getString("A5")
			+ "], ['6次'," + obj.getString("A6") + "], ['7次'," + obj.getString("A7")
			+ "], ['8次'," + obj.getString("A8") + "], ['9次'," + obj.getString("A9")
			+ "], ['10次'," + obj.getString("A10") + "], ['11次'," + obj.getString("A11")
			+ "], ['12次'," + obj.getString("A12") + "], ['13次'," + obj.getString("A13")
			+ "], ['14次'," + obj.getString("A14") + "], ['15次'," + obj.getString("A15")
			+ "], ['16次'," + obj.getString("A16") + "], ['17次'," + obj.getString("A17")
			+ "], ['18次'," + obj.getString("A18") + "], ['19次'," + obj.getString("A19")
			+ "], ['20次'," + obj.getString("A20") + "]"; 
		data_2 = "['0次'," + obj.getString("B0") + "], ['1次'," + obj.getString("B1") 
			+ "], ['2次'," + obj.getString("B2") + "], ['3次'," + obj.getString("B3")
			+ "], ['4次'," + obj.getString("B4") + "], ['5次'," + obj.getString("B5")
			+ "], ['6次'," + obj.getString("B6") + "], ['7次'," + obj.getString("B7")
			+ "], ['8次'," + obj.getString("B8") + "], ['9次'," + obj.getString("B9")
			+ "], ['10次'," + obj.getString("B10") + "], ['11次'," + obj.getString("B11")
			+ "], ['12次'," + obj.getString("B12") + "], ['13次'," + obj.getString("B13")
			+ "], ['14次'," + obj.getString("B14") + "], ['15次'," + obj.getString("B15")
			+ "], ['16次'," + obj.getString("B16") + "], ['17次'," + obj.getString("B17")
			+ "], ['18次'," + obj.getString("B18") + "], ['19次'," + obj.getString("B19")
			+ "], ['20次'," + obj.getString("B20") + "]"; 
		data_3 = "['0次'," + obj.getString("C0") + "], ['1次'," + obj.getString("C1") 
			+ "], ['2次'," + obj.getString("C2") + "], ['3次'," + obj.getString("C3")
			+ "], ['4次'," + obj.getString("C4") + "], ['5次'," + obj.getString("C5")
			+ "], ['6次'," + obj.getString("C6") + "], ['7次'," + obj.getString("C7")
			+ "], ['8次'," + obj.getString("C8") + "], ['9次'," + obj.getString("C9")
			+ "], ['10次'," + obj.getString("C10") + "], ['11次'," + obj.getString("C11")
			+ "], ['12次'," + obj.getString("C12") + "], ['13次'," + obj.getString("C13")
			+ "], ['14次'," + obj.getString("C14") + "], ['15次'," + obj.getString("C15")
			+ "], ['16次'," + obj.getString("C16") + "], ['17次'," + obj.getString("C17")
			+ "], ['18次'," + obj.getString("C18") + "], ['19次'," + obj.getString("C19")
			+ "], ['20次'," + obj.getString("C20") + "]"; 
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

<body data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-12" data-genuitec-path="/OperationAnalysis/WebRoot/dailyInstance.jsp">
	<div class="container" data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-12" data-genuitec-path="/OperationAnalysis/WebRoot/dailyInstance.jsp">
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
							String[] rows = { "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
											"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
											"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
											"", "", "", "", "", "", "", "", "", "", "", "", "", "" };
							int validDataSize = dataSize - startIndex;
							for (int i = 0; i < validDataSize; i++) {
								JSONObject obj = dailyInstanceData.getJSONObject(startIndex);
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
								double a11 = Double.parseDouble(obj.getString("A11"));
								double a12 = Double.parseDouble(obj.getString("A12"));
								double a13 = Double.parseDouble(obj.getString("A13"));
								double a14 = Double.parseDouble(obj.getString("A14"));
								double a15 = Double.parseDouble(obj.getString("A15"));
								double a16 = Double.parseDouble(obj.getString("A16"));
								double a17 = Double.parseDouble(obj.getString("A17"));
								double a18 = Double.parseDouble(obj.getString("A18"));
								double a19 = Double.parseDouble(obj.getString("A19"));
								double a20 = Double.parseDouble(obj.getString("A20"));
								double b0 = Double.parseDouble(obj.getString("B0"));
								double b1 = Double.parseDouble(obj.getString("B1"));
								double b2 = Double.parseDouble(obj.getString("B2"));
								double b3 = Double.parseDouble(obj.getString("B3"));
								double b4 = Double.parseDouble(obj.getString("B4"));
								double b5 = Double.parseDouble(obj.getString("B5"));
								double b6 = Double.parseDouble(obj.getString("B6"));
								double b7 = Double.parseDouble(obj.getString("B7"));
								double b8 = Double.parseDouble(obj.getString("B8"));
								double b9 = Double.parseDouble(obj.getString("B9"));
								double b10 = Double.parseDouble(obj.getString("B10"));
								double b11 = Double.parseDouble(obj.getString("B11"));
								double b12 = Double.parseDouble(obj.getString("B12"));
								double b13 = Double.parseDouble(obj.getString("B13"));
								double b14 = Double.parseDouble(obj.getString("B14"));
								double b15 = Double.parseDouble(obj.getString("B15"));
								double b16 = Double.parseDouble(obj.getString("B16"));
								double b17 = Double.parseDouble(obj.getString("B17"));
								double b18 = Double.parseDouble(obj.getString("B18"));
								double b19 = Double.parseDouble(obj.getString("B19"));
								double b20 = Double.parseDouble(obj.getString("B20"));
								double c0 = Double.parseDouble(obj.getString("C0"));
								double c1 = Double.parseDouble(obj.getString("C1"));
								double c2 = Double.parseDouble(obj.getString("C2"));
								double c3 = Double.parseDouble(obj.getString("C3"));
								double c4 = Double.parseDouble(obj.getString("C4"));
								double c5 = Double.parseDouble(obj.getString("C5"));
								double c6 = Double.parseDouble(obj.getString("C6"));
								double c7 = Double.parseDouble(obj.getString("C7"));
								double c8 = Double.parseDouble(obj.getString("C8"));
								double c9 = Double.parseDouble(obj.getString("C9"));
								double c10 = Double.parseDouble(obj.getString("C10"));
								double c11 = Double.parseDouble(obj.getString("C11"));
								double c12 = Double.parseDouble(obj.getString("C12"));
								double c13 = Double.parseDouble(obj.getString("C13"));
								double c14 = Double.parseDouble(obj.getString("C14"));
								double c15 = Double.parseDouble(obj.getString("C15"));
								double c16 = Double.parseDouble(obj.getString("C16"));
								double c17 = Double.parseDouble(obj.getString("C17"));
								double c18 = Double.parseDouble(obj.getString("C18"));
								double c19 = Double.parseDouble(obj.getString("C19"));
								double c20 = Double.parseDouble(obj.getString("C20"));
								rows[0] += "<th>" + obj.getString("date") + "</th>";
								rows[1] += "<td>" + obj.getString("num") + "</td>";
								rows[2] += "<td>" + Math.round(num * a0 / 100)  + " (" + a0 + "%)" + "</td>";
								rows[3] += "<td>" + Math.round(num * a1 / 100)  + " (" + a1 + "%)" + "</td>";
								rows[4] += "<td>" + Math.round(num * a2 / 100)  + " (" + a2 + "%)" + "</td>";
								rows[5] += "<td>" + Math.round(num * a3 / 100)  + " (" + a3 + "%)" + "</td>";
								rows[6] += "<td>" + Math.round(num * a4 / 100)  + " (" + a4 + "%)" + "</td>";
								rows[7] += "<td>" + Math.round(num * a5 / 100)  + " (" + a5 + "%)" + "</td>";
								rows[8] += "<td>" + Math.round(num * a6 / 100)  + " (" + a6 + "%)" + "</td>";
								rows[9] += "<td>" + Math.round(num * a7 / 100)  + " (" + a7 + "%)" + "</td>";
								rows[10] += "<td>" + Math.round(num * a8 / 100)  + " (" + a8 + "%)" + "</td>";
								rows[11] += "<td>" + Math.round(num * a9 / 100)  + " (" + a9 + "%)" + "</td>";
								rows[12] += "<td>" + Math.round(num * a10 / 100)  + " (" + a10 + "%)" + "</td>";
								rows[13] += "<td>" + Math.round(num * a11 / 100)  + " (" + a11 + "%)" + "</td>";
								rows[14] += "<td>" + Math.round(num * a12 / 100)  + " (" + a12 + "%)" + "</td>";
								rows[15] += "<td>" + Math.round(num * a13 / 100)  + " (" + a13 + "%)" + "</td>";
								rows[16] += "<td>" + Math.round(num * a14 / 100)  + " (" + a14 + "%)" + "</td>";
								rows[17] += "<td>" + Math.round(num * a15 / 100)  + " (" + a15 + "%)" + "</td>";
								rows[18] += "<td>" + Math.round(num * a16 / 100)  + " (" + a16 + "%)" + "</td>";
								rows[19] += "<td>" + Math.round(num * a17 / 100)  + " (" + a17 + "%)" + "</td>";
								rows[20] += "<td>" + Math.round(num * a18 / 100)  + " (" + a18 + "%)" + "</td>";
								rows[21] += "<td>" + Math.round(num * a19 / 100)  + " (" + a19 + "%)" + "</td>";
								rows[22] += "<td>" + Math.round(num * a20 / 100)  + " (" + a20 + "%)" + "</td>";
								rows[23] += "<td>" + Math.round(num * b0 / 100)  + " (" + b0 + "%)" + "</td>";
								rows[24] += "<td>" + Math.round(num * b1 / 100)  + " (" + b1 + "%)" + "</td>";
								rows[25] += "<td>" + Math.round(num * b2 / 100)  + " (" + b2 + "%)" + "</td>";
								rows[26] += "<td>" + Math.round(num * b3 / 100)  + " (" + b3 + "%)" + "</td>";
								rows[27] += "<td>" + Math.round(num * b4 / 100)  + " (" + b4 + "%)" + "</td>";
								rows[28] += "<td>" + Math.round(num * b5 / 100)  + " (" + b5 + "%)" + "</td>";
								rows[29] += "<td>" + Math.round(num * b6 / 100)  + " (" + b6 + "%)" + "</td>";
								rows[30] += "<td>" + Math.round(num * b7 / 100)  + " (" + b7 + "%)" + "</td>";
								rows[31] += "<td>" + Math.round(num * b8 / 100)  + " (" + b8 + "%)" + "</td>";
								rows[32] += "<td>" + Math.round(num * b9 / 100)  + " (" + b9 + "%)" + "</td>";
								rows[33] += "<td>" + Math.round(num * b10 / 100)  + " (" + b10 + "%)" + "</td>";
								rows[34] += "<td>" + Math.round(num * b11 / 100)  + " (" + b11 + "%)" + "</td>";
								rows[35] += "<td>" + Math.round(num * b12 / 100)  + " (" + b12 + "%)" + "</td>";
								rows[36] += "<td>" + Math.round(num * b13 / 100)  + " (" + b13 + "%)" + "</td>";
								rows[37] += "<td>" + Math.round(num * b14 / 100)  + " (" + b14 + "%)" + "</td>";
								rows[38] += "<td>" + Math.round(num * b15 / 100)  + " (" + b15 + "%)" + "</td>";
								rows[39] += "<td>" + Math.round(num * b16 / 100)  + " (" + b16 + "%)" + "</td>";
								rows[40] += "<td>" + Math.round(num * b17 / 100)  + " (" + b17 + "%)" + "</td>";
								rows[41] += "<td>" + Math.round(num * b18 / 100)  + " (" + b18 + "%)" + "</td>";
								rows[42] += "<td>" + Math.round(num * b19 / 100)  + " (" + b19 + "%)" + "</td>";
								rows[43] += "<td>" + Math.round(num * b20 / 100)  + " (" + b20 + "%)" + "</td>";
								rows[44] += "<td>" + Math.round(num * c0 / 100)  + " (" + c0 + "%)" + "</td>";
								rows[45] += "<td>" + Math.round(num * c1 / 100)  + " (" + c1 + "%)" + "</td>";
								rows[46] += "<td>" + Math.round(num * c2 / 100)  + " (" + c2 + "%)" + "</td>";
								rows[47] += "<td>" + Math.round(num * c3 / 100)  + " (" + c3 + "%)" + "</td>";
								rows[48] += "<td>" + Math.round(num * c4 / 100)  + " (" + c4 + "%)" + "</td>";
								rows[49] += "<td>" + Math.round(num * c5 / 100)  + " (" + c5 + "%)" + "</td>";
								rows[50] += "<td>" + Math.round(num * c6 / 100)  + " (" + c6 + "%)" + "</td>";
								rows[51] += "<td>" + Math.round(num * c7 / 100)  + " (" + c7 + "%)" + "</td>";
								rows[52] += "<td>" + Math.round(num * c8 / 100)  + " (" + c8 + "%)" + "</td>";
								rows[53] += "<td>" + Math.round(num * c9 / 100)  + " (" + c9 + "%)" + "</td>";
								rows[54] += "<td>" + Math.round(num * c10 / 100)  + " (" + c10 + "%)" + "</td>";
								rows[55] += "<td>" + Math.round(num * c11 / 100)  + " (" + c11 + "%)" + "</td>";
								rows[56] += "<td>" + Math.round(num * c12 / 100)  + " (" + c12 + "%)" + "</td>";
								rows[57] += "<td>" + Math.round(num * c13 / 100)  + " (" + c13 + "%)" + "</td>";
								rows[58] += "<td>" + Math.round(num * c14 / 100)  + " (" + c14 + "%)" + "</td>";
								rows[59] += "<td>" + Math.round(num * c15 / 100)  + " (" + c15 + "%)" + "</td>";
								rows[60] += "<td>" + Math.round(num * c16 / 100)  + " (" + c16 + "%)" + "</td>";
								rows[61] += "<td>" + Math.round(num * c17 / 100)  + " (" + c17 + "%)" + "</td>";
								rows[62] += "<td>" + Math.round(num * c18 / 100)  + " (" + c18 + "%)" + "</td>";
								rows[63] += "<td>" + Math.round(num * c19 / 100)  + " (" + c19 + "%)" + "</td>";
								rows[64] += "<td>" + Math.round(num * c20 / 100)  + " (" + c20 + "%)" + "</td>";
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
								<%
									for (int i = 2; i < 23; i++) {
								 %>
								<tr>
									<th>异界空间_<%=i-2 %>次</th>
									<%=rows[i]%>
								</tr>
								<%
									}
								 %>
								 <%
									for (int i = 23; i < 44; i++) {
								 %>
								<tr>
									<th>宝藏宫殿_<%=i-23 %>次</th>
									<%=rows[i]%>
								</tr>
								<%
									}
								 %>
								 <%
									for (int i = 44; i < 65; i++) {
								 %>
								<tr>
									<th>守护女神_<%=i-44 %>次</th>
									<%=rows[i]%>
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
