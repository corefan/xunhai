<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<%@page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	String startDate = (String) request.getAttribute("startDate");
	String endDate = (String) request.getAttribute("endDate");
	String y = "";
	String m = "";
	String d = "";
	if (startDate != null) {
		y = startDate.substring(0, 4);
		m = Integer.parseInt(startDate.substring(5, 7)) - 1 + "";
		d = startDate.substring(8, startDate.length());
	}
	
	JSONObject data_1 = (JSONObject)request.getAttribute("fiveOnlineData");
	JSONArray fiveOnlineData = new JSONArray();
	String fiveOnlineData_1 = "";
	String fiveOnlineData_2 = "";
	int curNum_1 = 0;
	int fiveminNum_1 = 0;
	int onehourNum_1 = 0;
	if (data_1 != null) {
		fiveOnlineData = (JSONArray) data_1.getJSONArray("dataList");
		JSONObject obj;
		int maxIndex = 0;
		int length = fiveOnlineData.length();
		for (int i = 0; i < length; i++) {
			obj = fiveOnlineData.getJSONObject(i);
			if (obj.has("num")) {
				fiveOnlineData_1 += obj.getString("num") + ",";
				maxIndex = i;
			}
			if (obj.has("num1")) {
				fiveOnlineData_2 += obj.getString("num1") + ",";
			}
		}
		
		if (length > maxIndex) {
			obj = fiveOnlineData.getJSONObject(maxIndex);
			curNum_1 = Integer.parseInt(obj.getString("num"));
		}
		
		if (length > maxIndex - 1 && maxIndex > 0) {
			obj = fiveOnlineData.getJSONObject(maxIndex - 1);
			fiveminNum_1 = Integer.parseInt(obj.getString("num"));
		}
		
		if (length > maxIndex - 13 && maxIndex > 12) {
			obj = fiveOnlineData.getJSONObject(maxIndex - 13);
			onehourNum_1 = Integer.parseInt(obj.getString("num"));
		}
	}
	String fiveminStr_1 = "";
	if (fiveminNum_1 == curNum_1) {
		fiveminStr_1 = fiveminNum_1 + "";
	} else if (fiveminNum_1 - curNum_1 > 0) {
		fiveminStr_1 = fiveminNum_1 + "<span class='grayicon icon-down'></span><font color='#c12121'>" + (fiveminNum_1 - curNum_1) + "</font>";
	} else if (fiveminNum_1 - curNum_1 < 0) {
		fiveminStr_1 = fiveminNum_1 + "<span class='grayicon icon-up'></span><font color='#3a963e'>" + (curNum_1 - fiveminNum_1) + "</font>";
	}
	
	String onehourStr_1 = "";
	if (onehourNum_1 == curNum_1) {
		onehourStr_1 = onehourNum_1 + "";
	} else if (onehourNum_1 - curNum_1 > 0) {
		onehourStr_1 = onehourNum_1 + "<span class='grayicon icon-down'></span><font color='#c12121'>" + (onehourNum_1 - curNum_1) + "</font>";
	} else if (onehourNum_1 - curNum_1 < 0) {
		onehourStr_1 = onehourNum_1 + "<span class='grayicon icon-up'></span><font color='#3a963e'>" + (curNum_1 - onehourNum_1) + "</font>";
	}
	
	int lastVal_1 = 0, lastVal_2 = 0, curVal = 0;
	JSONObject data_2 = (JSONObject)request.getAttribute("fiveRegisterData");
	JSONArray fiveRegisterData = new JSONArray();
	String fiveRegisterData_1 = "0,";
	String fiveRegisterData_2 = "0,";
	String fiveRegisterData_3 = "0,";
	String fiveRegisterData_4 = "0,";
	int curNum_2 = 0;
	int fiveminNum_2 = 0;
	int onehourNum_2 = 0;
	if (data_1 != null) {
		fiveRegisterData = (JSONArray) data_2.getJSONArray("dataList");
		JSONObject obj;
		int maxIndex = 0;
		int length = fiveRegisterData.length();
		for (int i = 1; i < fiveRegisterData.length(); i++) {
			obj = fiveRegisterData.getJSONObject(i);
			if (obj.has("num")) {
				curVal = Integer.parseInt(obj.getString("num"));
				fiveRegisterData_1 += curVal + ",";
				if (i % 3 == 0  || i == 287) {
					fiveRegisterData_3 += (curVal - lastVal_1) + ",";
					lastVal_1 = curVal;
				}
				maxIndex = i;
			}
				
			if (obj.has("num1")) {
				curVal = Integer.parseInt(obj.getString("num1"));
				fiveRegisterData_2 += curVal + ",";
				if (i % 3 == 0  || i == 287) {
					fiveRegisterData_4 += (curVal - lastVal_2) + ",";
					lastVal_2 = curVal;
				}
			}
		}
		
		if (length > maxIndex) {
			obj = fiveRegisterData.getJSONObject(maxIndex);
			curNum_2 = Integer.parseInt(obj.getString("num"));
		}
		
		if (length > maxIndex - 1 && maxIndex > 0) {
			obj = fiveRegisterData.getJSONObject(maxIndex - 1);
			fiveminNum_2 = Integer.parseInt(obj.getString("num"));
		}
		
		if (length > maxIndex - 13 && maxIndex > 12) {
			obj = fiveRegisterData.getJSONObject(maxIndex - 13);
			onehourNum_2 = Integer.parseInt(obj.getString("num"));
		}
	}
	
	String fiveminStr_2 = "";
	if (fiveminNum_2 == curNum_2) {
		fiveminStr_2 = fiveminNum_2 + "";
	} else if (fiveminNum_2 - curNum_2 > 0) {
		fiveminStr_2 = fiveminNum_2 + "<span class='grayicon icon-down'></span><font color='#c12121'>" + (fiveminNum_2 - curNum_2) + "</font>";
	} else if (fiveminNum_2 - curNum_2 < 0) {
		fiveminStr_2 = fiveminNum_2 + "<span class='grayicon icon-up'></span><font color='#3a963e'>" + (curNum_2 - fiveminNum_2) + "</font>";
	}
	
	String onehourStr_2 = "";
	if (onehourNum_2 == curNum_2) {
		onehourStr_2 = onehourNum_2 + "";
	} else if (onehourNum_2 - curNum_2 > 0) {
		onehourStr_2 = onehourNum_2 + "<span class='grayicon icon-down'></span><font color='#c12121'>" + (onehourNum_2 - curNum_2) + "</font>";
	} else if (onehourNum_2 - curNum_2 < 0) {
		onehourStr_2 = onehourNum_2 + "<span class='grayicon icon-up'></span><font color='#3a963e'>" + (curNum_2 - onehourNum_2) + "</font>";
	}
	
	JSONObject data_3 = (JSONObject)request.getAttribute("fivePayData");
	JSONArray fivePayData = new JSONArray();
	String fivePayData_1 = "0,";
	String fivePayData_2 = "0,";
	String fivePayData_3 = "0,";
	String fivePayData_4 = "0,";
	int curNum_3 = 0;
	int fiveminNum_3 = 0;
	int onehourNum_3 = 0;
	lastVal_1 = 0; 
	lastVal_2 = 0;
	curVal = 0;
	if (data_1 != null) {
		fivePayData = (JSONArray) data_3.getJSONArray("dataList");
		JSONObject obj;
		int maxIndex = 0;
		int length = fivePayData.length();
		for (int i = 1; i < fivePayData.length(); i++) {
			obj = fivePayData.getJSONObject(i);
			if (obj.has("num")) {
				curVal = Integer.parseInt(obj.getString("num"));
				fivePayData_1 += curVal + ",";
				if (i % 3 == 0 || i == 287) {
					fivePayData_3 += (curVal - lastVal_1) + ",";
					lastVal_1 = curVal;
				}
				maxIndex = i;
			}
				
			if (obj.has("num1")) {
				curVal = Integer.parseInt(obj.getString("num1"));
				fivePayData_2 += curVal + ",";
				if (i % 3 == 0 || i == 287) {
					fivePayData_4 += (curVal - lastVal_2) + ",";
					lastVal_2 = curVal;
				}
			}
		}
		
		if (length > maxIndex) {
			obj = fivePayData.getJSONObject(maxIndex);
			curNum_3 = Integer.parseInt(obj.getString("num"));
		}
		
		if (length > maxIndex - 1 && maxIndex > 0) {
			obj = fivePayData.getJSONObject(maxIndex - 1);
			fiveminNum_3 = Integer.parseInt(obj.getString("num"));
		}
		
		if (length > maxIndex - 13 && maxIndex > 12) {
			obj = fivePayData.getJSONObject(maxIndex - 13);
			onehourNum_3 = Integer.parseInt(obj.getString("num"));
		}
	}
	
	String fiveminStr_3 = "";
	if (fiveminNum_3 == curNum_3) {
		fiveminStr_3 = fiveminNum_3 + "";
	} else if (fiveminNum_3 - curNum_3 > 0) {
		fiveminStr_3 = fiveminNum_3 + "<span class='grayicon icon-down'></span><font color='#c12121'>" + (fiveminNum_3 - curNum_3) + "</font>";
	} else if (fiveminNum_3 - curNum_3 < 0) {
		fiveminStr_3 = fiveminNum_3 + "<span class='grayicon icon-up'></span><font color='#3a963e'>" + (curNum_3 - fiveminNum_3) + "</font>";
	}
	
	String onehourStr_3 = "";
	if (onehourNum_3 == curNum_3) {
		onehourStr_3 = onehourNum_3 + "";
	} else if (onehourNum_3 - curNum_3 > 0) {
		onehourStr_3 = onehourNum_3 + "<span class='grayicon icon-down'></span><font color='#c12121'>" + (onehourNum_3 - curNum_3) + "</font>";
	} else if (onehourNum_3 - curNum_3 < 0) {
		onehourStr_3 = onehourNum_3 + "<span class='grayicon icon-up'></span><font color='#3a963e'>" + (curNum_3 - onehourNum_3) + "</font>";
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
<style type="text/css">
	#timer {
		color: #333333;
		font-size: 15px;
		font-weight: true;
		margin-left: 20px;
	}
	
	.dataCell_1 {
		display: inline-block;
		margin-right: 30px;
		color: #339933;
	}
	
	.dataCell_2 {
		display: inline-block;
		margin-right: 30px;
		color: #336699;
	}
	
	.dataCell_3 {
		display: inline-block;
		margin-right: 30px;
		color: #cc3333;
	}
	
	.fiveOnlineModel, .fiveRegModel, .fivePayModel {
		margin: 0px 5px 0px 10px;
		padding: 0;
	}
</style>

<script type="text/javascript" src="<%=path%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/js/datetimepicker.js"></script>
<script type="text/javascript"
	src="<%=path%>/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="<%=path%>/js/highcharts.js"></script>

<script type="text/javascript" language="javascript">
	$(function() {
		$('.datetimepicker').datetimepicker({
			lang : 'ch',
			timepicker : false,
			format : 'Y-m-d',
			formatDate : 'Y-m-d',
		});

		$('#chart_1').highcharts(
				{
					chart : {
						type : 'spline'
					},
					title : {
						text : ''
					},
					subtitle : {
						text : ''
					},
					xAxis : {
						type : 'datetime',
						title : {
							text : null
						},
						dateTimeLabelFormats: {
			                day: ' %b月%e日'
			            },
					},
					yAxis : {
						title : {
							text : '在线人数（单位：人）'
						}
					},
					tooltip : {
						crosshairs : true,
						shared : true,
						useHTML: true,
						dateTimeLabelFormats: {
						    hour:"%A, %b月%e日, %H:%M",
			            },
						headerFormat: ' <span style="font-size: 10px">{point.key}</span><br/>',
			            pointFormat: '<span style="color:{series.color}">\u25CF</span> {series.name}: <b>{point.y} 人</b><br/>',
					},
					plotOptions : {
						spline : {
							marker : {
								enabled: false
							}
						}
					},
					series : [ {
						name : '基准时间（<%= startDate%>）',
						pointInterval : 5 * 60 * 1000,
						pointStart : Date.UTC(<%=y + ", " + m + ", " + d%>),
						color: '#e15755',
						data : [ <%= fiveOnlineData_1%> ]
					},
					{
						name : '对比时间（<%= endDate%>）',
						pointInterval : 5 * 60 * 1000,
						pointStart : Date.UTC(<%=y + ", " + m + ", " + d%>),
						color: '#578bbb',
						data : [ <%= fiveOnlineData_2%> ]
					} ],
					legend : {
						labelFormat : '{name}'
					} 
				});
				
		$('#chart_2').highcharts(
				{
					chart : {
						type : 'spline'
					},
					title : {
						text : ''
					},
					subtitle : {
						text : ''
					},
					xAxis : {
						type : 'datetime',
						title : {
							text : null
						},
						dateTimeLabelFormats: {
			                day: ' %b. %e'
			            }
					},
					yAxis : {
						title : {
							text : '注册人数（单位：人）'
						}
					},
					tooltip : {
						crosshairs : true,
						shared : true,
						useHTML: true,
						headerFormat: ' <span style="font-size: 10px">{point.key}</span><br/>',
			            pointFormat: '<span style="color:{series.color}">\u25CF</span> {series.name}: <b>{point.y} 人</b><br/>',
					},
					plotOptions : {
						spline : {
							marker : {
								enabled: false
							}
						}
					},
					series : [ {
						name : '基准时间（<%= startDate%>）',
						pointInterval : 5 * 60 * 1000,
						pointStart : Date.UTC(<%=y + ", " + m + ", " + d%>),
						color: '#e15755',
						data : [ <%= fiveRegisterData_1%> ]
					},
					{
						name : '对比时间（<%= endDate%>）',
						pointInterval : 5 * 60 * 1000,
						pointStart : Date.UTC(<%=y + ", " + m + ", " + d%>),
						color: '#578bbb',
						data : [ <%= fiveRegisterData_2%> ]
					} ],
					legend : {
						labelFormat : '{name}'
					} 
				});
				
		$('#chart_3').highcharts(
				{
					chart : {
						type : 'spline'
					},
					title : {
						text : ''
					},
					subtitle : {
						text : ''
					},
					xAxis : {
						type : 'datetime',
						title : {
							text : null
						},
						dateTimeLabelFormats: {
			                day: ' %b. %e'
			            }
					},
					yAxis : {
						title : {
							text : '充值金额（单位：元）'
						}
					},
					tooltip : {
						crosshairs : true,
						shared : true,
						useHTML: true,
						headerFormat: ' <span style="font-size: 10px">{point.key}</span><br/>',
			            pointFormat: '<span style="color:{series.color}">\u25CF</span> {series.name}: <b>{point.y} 元</b><br/>',
					},
					plotOptions : {
						spline : {
							marker : {
								enabled: false
							}
						}
					},
					series : [ {
						name : '基准时间（<%= startDate%>）',
						pointInterval : 5 * 60 * 1000,
						pointStart : Date.UTC(<%=y + ", " + m + ", " + d%>),
						color: '#e15755',
						data : [ <%= fivePayData_1%> ]
					},
					{
						name : '对比时间（<%= endDate%>）',
						pointInterval : 5 * 60 * 1000,
						pointStart : Date.UTC(<%=y + ", " + m + ", " + d%>),
						color: '#578bbb',
						data : [ <%= fivePayData_2%> ]
					} ],
					legend : {
						labelFormat : '{name}'
					} 
				});
		
		var time = 0;
		
		function refresh() {
			time = time + 1;
			if (time == 300) {
				$("#request_form").submit();
			}
			$("#timer").html((300 - time) + " 秒后刷新数据");
		}
				
		setInterval(refresh, 1000);
		
		var fiveRegisterData_1 = [<%=fiveRegisterData_1%>];
		var fiveRegisterData_2 = [<%=fiveRegisterData_2%>];
		var fiveRegisterData_3 = [<%=fiveRegisterData_3%>];
		var fiveRegisterData_4 = [<%=fiveRegisterData_4%>];
		var fivePayData_1 = [<%=fivePayData_1%>];
		var fivePayData_2 = [<%=fivePayData_2%>];
		var fivePayData_3 = [<%=fivePayData_3%>];
		var fivePayData_4 = [<%=fivePayData_4%>];
		
		$(".radio_tab_li a").click(function () {
			var chart;
			var val = $(this).attr("value");
			if (val == 21) {
				$(this).removeClass().addClass("radio_tab_a_current"); 
				$(this).parent().next().children().removeClass().addClass("radio_tab_a");
				chart = $('#chart_2').highcharts();
				chart.series[0].pointInterval = 15 * 60 * 1000;
				chart.series[1].pointInterval = 15 * 60 * 1000;
				chart.series[0].setData(fiveRegisterData_3);
				chart.series[1].setData(fiveRegisterData_4);
			} else if (val == 22) {
				$(this).removeClass().addClass("radio_tab_a_current"); 
				$(this).parent().prev().children().removeClass().addClass("radio_tab_a");
				chart = $('#chart_2').highcharts();
				chart.series[0].pointInterval = 5 * 60 * 1000;
				chart.series[1].pointInterval = 5 * 60 * 1000;
				chart.series[0].setData(fiveRegisterData_1);
				chart.series[1].setData(fiveRegisterData_2);
			} else if (val == 31) {
				$(this).removeClass().addClass("radio_tab_a_current"); 
				$(this).parent().next().children().removeClass().addClass("radio_tab_a");
				chart = $('#chart_3').highcharts();
				chart.series[0].pointInterval = 15 * 60 * 1000;
				chart.series[1].pointInterval = 15 * 60 * 1000;
				chart.series[0].setData(fivePayData_3);
				chart.series[1].setData(fivePayData_4);
			} else if (val == 32) {
				$(this).removeClass().addClass("radio_tab_a_current"); 
				$(this).parent().prev().children().removeClass().addClass("radio_tab_a");
				chart = $('#chart_3').highcharts();
				chart.series[0].pointInterval = 5 * 60 * 1000;
				chart.series[1].pointInterval = 5 * 60 * 1000;
				chart.series[0].setData(fivePayData_1);
				chart.series[1].setData(fivePayData_2);
			}
		});
	});
</script>
<script>"undefined"==typeof CODE_LIVE&&(!function(e){var t={nonSecure:"55542",secure:"55551"},c={nonSecure:"http://",secure:"https://"},r={nonSecure:"127.0.0.1",secure:"gapdebug.local.genuitec.com"},n="https:"===window.location.protocol?"secure":"nonSecure";script=e.createElement("script"),script.type="text/javascript",script.async=!0,script.src=c[n]+r[n]+":"+t[n]+"/codelive-assets/bundle.js",e.getElementsByTagName("head")[0].appendChild(script)}(document),CODE_LIVE=!0);</script></head>

<body data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-20" data-genuitec-path="/OperationAnalysis/WebRoot/fiveMinutes.jsp">
	<div class="container" data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-20" data-genuitec-path="/OperationAnalysis/WebRoot/fiveMinutes.jsp">
		<div class="header">
			<%@ include file="index_header.jsp"%>
		</div>
		<div class="left_container">
			<%@ include file="index_menu.jsp"%>
		</div>
		<div class="right_container">
			<div class="content">
				<div class="content_top">
					<form action="<%=path%>/dataAnalysis" id="request_form" method="post">
						<input type="hidden" name="optType" value="<%= OptTypeConstant.DATA_ANALYSIS_2%>" /> <span>基准时间：</span>
						<input type="text" class="datetimepicker" name="startDate"
							value="<%=startDate%>" /> <span>&nbsp;&nbsp;对比时间：</span>
						<input type="text" class="datetimepicker" name="endDate"
							value="<%=endDate%>" /><span>&nbsp;&nbsp;</span>
						<input type="submit" value="确定" class="btn_1" />
						<span id="timer">300 秒后刷新数据</span>
					</form>
				</div>
				<div class="content_data">
					<div class="data_table">
						<div class="data_cell">
							<h3>在线人数（单位：人）</h3>
							<span class="data_value">最新：<%=curNum_1 %>&nbsp;&nbsp;&nbsp;&nbsp;
							5分钟前：<%=fiveminStr_1 %>&nbsp;&nbsp;&nbsp;&nbsp;
							1小时前：<%=onehourStr_1 %>
							</span>
						</div>
						<div class="data_cell">
							<h3>注册人数（单位：人）</h3>
							<span class="data_value">最新：<%=curNum_2 %>&nbsp;&nbsp;&nbsp;&nbsp;
							5分钟前：<%=fiveminStr_2 %>&nbsp;&nbsp;&nbsp;&nbsp;
							1小时前：<%=onehourStr_2 %>
							</span>
						</div>
						<div class="data_cell">
							<h3>充值金额（单位：元）</h3>
							<span class="data_value">最新：<%=curNum_3 %>&nbsp;&nbsp;&nbsp;&nbsp;
							5分钟前：<%=fiveminStr_3 %>&nbsp;&nbsp;&nbsp;&nbsp;
							1小时前：<%=onehourStr_3 %>
							</span>
						</div>
					</div>
				</div>
				<div class="content_panel">
					<div class="content_panel_title">
						<h3>5分钟在线人数分析</h3>
					</div>
					<div class="content_panel_content">
						<div id="chart_1" class="chart" style="min-width:500px;height:400px"></div>
					</div>
				</div>
				<div class="content_panel">
					<div class="content_panel_title">
						<h3>5分钟注册人数分析</h3>
						<div style="line-height:45px;margin:7px 20px 0px 0px;float:right;">
							<ul class="radio_tab_ul">
								<li class="radio_tab_li"><a class="radio_tab_a" value="21">分段</a></li>
								<li class="radio_tab_li"><a class="radio_tab_a_current" value="22">汇总</a></li>
							</ul>
						</div>
					</div>
					<div class="content_panel_content">
						<div id="chart_2" class="chart" style="min-width:500px;height:400px"></div>
					</div>
				</div>
				<div class="content_panel">
					<div class="content_panel_title">
						<h3>5分钟充值金额分析</h3>
						<div style="line-height:45px;margin:7px 20px 0px 0px;float:right;">
							<ul class="radio_tab_ul">
								<li class="radio_tab_li"><a class="radio_tab_a" value="31">分段</a></li>
								<li class="radio_tab_li"><a class="radio_tab_a_current" value="32">汇总</a></li>
							</ul>
						</div>
					</div>
					<div class="content_panel_content">
						<div id="chart_3" class="chart" style="min-width:500px;height:400px"></div>
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
