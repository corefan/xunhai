<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<%@page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
			
	JSONObject data = (JSONObject) request.getAttribute("data");
	JSONArray firstPayData = new JSONArray();
	if (data != null) {
		firstPayData = (JSONArray) data.getJSONArray("dataList");
	}
	
	String dataHTMLStr = "";
	int diamondSum = 0;
	for (int i = 0; i < firstPayData.length(); i++) {
		JSONObject obj = firstPayData.getJSONObject(i);
		if (!obj.isNull("num1")) {
			dataHTMLStr += "<tr>";
			dataHTMLStr += "<td>" + obj.getString("date") + "</td>";
			dataHTMLStr += "<td>" + obj.getString("rnum") + "</td>";
			dataHTMLStr += "<td>" + obj.getString("num1") + "</td>";
			dataHTMLStr += "<td>" + obj.getString("num2") + "</td>";
			dataHTMLStr += "<td>" + obj.getString("num3") + "</td>";
			dataHTMLStr += "<td>" + obj.getString("num4") + "</td>";
			dataHTMLStr += "<td>" + obj.getString("num5") + "</td>";
			dataHTMLStr += "<td>" + obj.getString("num6") + "</td>";
			dataHTMLStr += "<td>" + obj.getString("num7") + "</td>";
			dataHTMLStr += "</tr>";
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
					<form action="<%=path %>/payAnalysis" method="post">
						<input type="hidden" name="optType" value="<%=OptTypeConstant.PAY_ANALYSIS_41%>" />
						<input type="text" class="datetimepicker" name="startDate" value="<%= request.getAttribute("startDate") %>" /> <span> - </span>
						<input type="text" class="datetimepicker" name="endDate" value="<%= request.getAttribute("endDate") %>" /><span>&nbsp;&nbsp;</span>
						<input type="submit" value="刷新数据" class="btn_1" />
					</form>
				</div>
				<div class="content_help">
					<ul>
						<li><b>注册人数：  </b>新进入区服的人数</li>
						<li><b>当日付费人数：  </b>取数日注册且取数日首次付费。</li>
						<li><b>次日付费人数： </b>取数日注册且注册第二天首次付费。</li>
						<li><b>3日付费人数： </b>取数日注册且注册第3天首次付费。</li>
						<li><b>N日付费人数： </b>取数日注册且注册第N天首次付费。</li>
					</ul>
				</div>
				<div class="content_panel">
					<div class="content_panel_title">
						<h3>首次付费分析</h3>
					</div>
					<div class="content_panel_content">
						<table id="table_1" class="display" cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>日期</th>
									<th>注册人数</th>
									<th>当日付费人数</th>
									<th>次日付费人数</th>
									<th>3日付费人数</th>
									<th>4日付费人数</th>
									<th>5日付费人数</th>
									<th>6日付费人数</th>
									<th>7日付费人数</th>
								</tr>
							</thead>
	
							<tbody>
								<%= dataHTMLStr %>
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
