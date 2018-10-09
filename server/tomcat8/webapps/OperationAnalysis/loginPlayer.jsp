<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<%@page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	JSONObject data = (JSONObject) request.getAttribute("data");
	JSONArray loginPlayerData = new JSONArray();
	if (data != null) {
		loginPlayerData = (JSONArray) data.getJSONArray("loginUserContent");
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
	});
</script>
<script>"undefined"==typeof CODE_LIVE&&(!function(e){var t={nonSecure:"55542",secure:"55551"},c={nonSecure:"http://",secure:"https://"},r={nonSecure:"127.0.0.1",secure:"gapdebug.local.genuitec.com"},n="https:"===window.location.protocol?"secure":"nonSecure";script=e.createElement("script"),script.type="text/javascript",script.async=!0,script.src=c[n]+r[n]+":"+t[n]+"/codelive-assets/bundle.js",e.getElementsByTagName("head")[0].appendChild(script)}(document),CODE_LIVE=!0);</script></head>

<body data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-33" data-genuitec-path="/OperationAnalysis/WebRoot/loginPlayer.jsp">
	<div class="container" data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-33" data-genuitec-path="/OperationAnalysis/WebRoot/loginPlayer.jsp">
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
						<input type="hidden" name="optType" value="<%= OptTypeConstant.DATA_ANALYSIS_22%>" /> <input
							type="text" class="datetimepicker" name="startDate"
							value="<%=request.getAttribute("startDate")%>" /> <span>
							- </span> <input type="text" class="datetimepicker" name="endDate"
							value="<%=request.getAttribute("endDate")%>" /><span>&nbsp;&nbsp;</span>
						<input type="submit" value="确定" class="btn_1" />
					</form>
				</div>
				<div class="content_help">
					<ul>
						<li><b>登录用户数： </b>新注册用户+老用户数</li>
					</ul>
				</div>
				<div class="content_panel">
					<div class="content_panel_title">
						<h3>登录用户构成</h3>
					</div>
					<div class="content_panel_content">
						<%
							int dataSize = loginPlayerData.length();
							int startIndex = 0;
							if (dataSize > 7) {
								startIndex = dataSize - 7;
							}
							String[] rows = { "", "", "", "", "", "", "", "", "", "", "", "" };
							int validDataSize = dataSize - startIndex;
							for (int i = 0; i < validDataSize; i++) {
								JSONObject obj = loginPlayerData.getJSONObject(startIndex);
								rows[0] += "<th>" + obj.getString("CREATE_TIME") + "</th>";
								rows[1] += "<td>" + obj.getString("loginNum") + "</td>";
								rows[2] += "<td>" + obj.getString("registerNum") + "</td>";
								rows[3] += "<td>" + obj.getString("oldUser") + "</td>";
/* 								rows[4] += "<td>" + obj.getString("regOneWeek") + "</td>";
								rows[5] += "<td>" + obj.getString("regTwoWeek") + "</td>";
								rows[6] += "<td>" + obj.getString("regOneMon") + "</td>";
								rows[7] += "<td>" + obj.getString("regTwoMon") + "</td>";
								rows[8] += "<td>" + obj.getString("regThrMon") + "</td>";
								rows[9] += "<td>" + obj.getString("regFourMon") + "</td>";
								rows[10] += "<td>" + obj.getString("regFiveMon") + "</td>";
								rows[11] += "<td>" + obj.getString("regSixMon") + "</td>"; */
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
									<th>登录用户数</th>
									<%=rows[1]%>
								</tr>
								<tr>
									<th>新注册用户</th>
									<%=rows[2]%>
								</tr>
								<tr>
									<th>老用户数</th>
									<%=rows[3]%>
								</tr>
<%-- 								<tr>
									<th>一周内</th>
									<%=rows[4]%>
								</tr>
								<tr>
									<th>两周内</th>
									<%=rows[5]%>
								</tr>
								<tr>
									<th>一个月内</th>
									<%=rows[6]%>
								</tr>
								<tr>
									<th>两个月内</th>
									<%=rows[7]%>
								</tr>
								<tr>
									<th>三个月内</th>
									<%=rows[8]%>
								</tr>
								<tr>
									<th>四个月内</th>
									<%=rows[9]%>
								</tr>
								<tr>
									<th>五个月内</th>
									<%=rows[10]%>
								</tr>
								<tr>
									<th>六个月内</th>
									<%=rows[11]%>
								</tr> --%>
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
