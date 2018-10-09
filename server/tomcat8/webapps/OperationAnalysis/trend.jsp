<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<%@page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	JSONObject data = (JSONObject) request.getAttribute("data");
	JSONArray trendData = new JSONArray();
	
	String xAxisStr = "";
	String dataStr_1 = ""; // 付费金额
	String dataStr_2 = ""; // 付费用户
	String dataStr_3 = ""; // 登录用户
	String dataStr_4 = ""; // 在线峰值
	String dataStr_5 = ""; // 注册用户
	if (data != null) {
		trendData = (JSONArray) data.getJSONArray("indexTrendPlate");
		for (int i = 0; i < trendData.length(); i++) {
			JSONObject obj = trendData.getJSONObject(i);
			String date = obj.getString("date");
			xAxisStr += "'" + (date.substring(5, date.length())).replace("-", "/")
					+ "',";
			dataStr_1 += obj.getString("mnum") + ",";
			dataStr_2 += obj.getString("pnum") + ",";
			dataStr_3 += obj.getString("loginNum") + ",";
 			dataStr_4 += obj.getString("onlinePeak") + ",";
			dataStr_5 += obj.getString("rnum") + ","; 
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
			"bLengthChange": false,
			"bInfo": false,
			"bPaginate": false,
			"bFilter":false,
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
				type : 'spline'
			},
			title : {
				text : ''
			},
			subtitle : {
				text : ''
			},
			legend : {
				enabled : true
			},
			xAxis : {
				categories : [<%=xAxisStr%>]
			},
			yAxis : {
				title : {
					text : ''		
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
			},
			plotOptions : {
				spline : {
				}
			},
			series : [ {
				name : '付费金额',
				data : [<%=dataStr_1%>]
			},
			{
				name : '付费用户',
				data : [<%=dataStr_2%>]
			},
			{
				name : '登录用户',
				data : [<%=dataStr_3%>]
			},
 			{
				name : '在线数峰值',
				data : [<%=dataStr_4%>]
			}, 
			{
				name : '新注册用户',
				data : [<%=dataStr_5%>]
			},
			]
		});
	});
</script>
<script>"undefined"==typeof CODE_LIVE&&(!function(e){var t={nonSecure:"55542",secure:"55551"},c={nonSecure:"http://",secure:"https://"},r={nonSecure:"127.0.0.1",secure:"gapdebug.local.genuitec.com"},n="https:"===window.location.protocol?"secure":"nonSecure";script=e.createElement("script"),script.type="text/javascript",script.async=!0,script.src=c[n]+r[n]+":"+t[n]+"/codelive-assets/bundle.js",e.getElementsByTagName("head")[0].appendChild(script)}(document),CODE_LIVE=!0);</script></head>

<body data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-46" data-genuitec-path="/OperationAnalysis/WebRoot/trend.jsp">
	<div class="container" data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-46" data-genuitec-path="/OperationAnalysis/WebRoot/trend.jsp">
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
						<input type="hidden" name="optType" value="<%= OptTypeConstant.DATA_ANALYSIS_19%>" /> <input
							type="text" class="datetimepicker" name="startDate"
							value="<%=request.getAttribute("startDate")%>" /> <span>
							- </span> <input type="text" class="datetimepicker" name="endDate"
							value="<%=request.getAttribute("endDate")%>" /><span>&nbsp;&nbsp;</span>
						<input type="submit" value="确定" class="btn_1" />
					</form>
				</div>
				<div class="content_help">
					<ul>
						<li><b>新用户： </b>取数日当天新注册的用户数。</li>
						<li><b>付费率：  </b>付费人数/登录人数。</li>						
						<li><b>人均付费ARPU： </b>付费金额/付费人数。</li>	
						<li><b>在线数峰值： </b>当天在线玩家数峰值。</li>	
					</ul>
				</div>
				<div class="content_panel">
					<div class="content_panel_title">
						<h3>指标趋势看盘</h3>
					</div>
					<div class="content_panel_content">
						<div id="chart_1" class="chart" style="min-width:500px;height:400px"></div>
						<%
							int dataSize = trendData.length();
							int startIndex = 0;
							if (dataSize > 7) {
								startIndex = dataSize - 7;
							}
							String[] rows = { "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
							int validDataSize = dataSize - startIndex;
							for (int i = 0; i < validDataSize; i++) {
								JSONObject obj = trendData.getJSONObject(startIndex);
								rows[0] += "<th>" + obj.getString("date") + "</th>";
								rows[1] += "<td style='background: none repeat scroll 0 0 #e2e2e2;'>" + obj.getString("loginNum") + "</td>";
								rows[2] += "<td>" + obj.getString("pnum") + "</td>";
								rows[3] += "<td>" + obj.getString("mnum") + "</td>";
								rows[4] += "<td>" + obj.getString("payRate") + "</td>";
								rows[5] += "<td>" + obj.getString("pArpu") + "</td>";
								rows[6] += "<td style='background: none repeat scroll 0 0 #e2e2e2;'>" + obj.getString("rnum") + "</font></td>";
								rows[7] += "<td>" + obj.getString("nPnum") + "</td>";
								rows[8] += "<td>" + obj.getString("nMnum") + "</td>";
								rows[9] += "<td>" + obj.getString("nPayRate") + "</td>";
								rows[10] += "<td>" + obj.getString("nParpu") + "</td>";
								rows[11] += "<td>" + obj.getString("onlinePeak") + "</td>";
								
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
									<th style="background: none repeat scroll 0 0 #e2e2e2;">登录用户数</th>
									<%=rows[1]%>
								</tr>
								<tr>
									<th>付费人数</th>
									<%=rows[2]%>
								</tr>
								<tr>
									<th>付费金额</th>
									<%=rows[3]%>
								</tr>
								<tr>
									<th>付费率</th>
									<%=rows[4]%>
								</tr>
								<tr>
									<th>人均付费ARPU</th>
									<%=rows[5]%>
								</tr>
								<tr>
									<th style="background: none repeat scroll 0 0 #e2e2e2;">新注册用户数</th>
									<%=rows[6]%>
								</tr>
								<tr>
									<th>新用户付费人数</th>
									<%=rows[7]%>
								</tr>
								<tr>
									<th>新用户付费金额</th>
									<%=rows[8]%>
								</tr>
								<tr>
									<th>新用户付费率</th>
									<%=rows[9]%>
								</tr>
								<tr>
									<th>新用户付费ARPU</th>
									<%=rows[10]%>
								</tr>
								<tr>
									<th>在线数峰值</th>
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
