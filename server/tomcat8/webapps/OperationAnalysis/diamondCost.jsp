<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<%@page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
			
	JSONObject data = (JSONObject) request.getAttribute("data");
	JSONArray diamondCostData = new JSONArray();
	if (data != null) {
		diamondCostData = (JSONArray) data.getJSONArray("dataList");
	}
	
	String dataHTMLStr = "";
	int diamondSum = 0;
	for (int i = 0; i < diamondCostData.length(); i++) {
		JSONObject obj = diamondCostData.getJSONObject(i);
		int diamondNum = Integer.parseInt(obj.getString("num"));
		dataHTMLStr += "<tr>";
		dataHTMLStr += "<td>" + obj.getString("name") + "</td>";
		dataHTMLStr += "<td>" + diamondNum + "</td>";
		dataHTMLStr += "<td>" + obj.getString("rate") + "</td>";
		dataHTMLStr += "</tr>";
		diamondSum += diamondNum;
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
<script>"undefined"==typeof CODE_LIVE&&(!function(e){var t={nonSecure:"55542",secure:"55551"},c={nonSecure:"http://",secure:"https://"},r={nonSecure:"127.0.0.1",secure:"gapdebug.local.genuitec.com"},n="https:"===window.location.protocol?"secure":"nonSecure";script=e.createElement("script"),script.type="text/javascript",script.async=!0,script.src=c[n]+r[n]+":"+t[n]+"/codelive-assets/bundle.js",e.getElementsByTagName("head")[0].appendChild(script)}(document),CODE_LIVE=!0);</script></head>

<body data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-15" data-genuitec-path="/OperationAnalysis/WebRoot/diamondCost.jsp">
	<div class="container" data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-15" data-genuitec-path="/OperationAnalysis/WebRoot/diamondCost.jsp">
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
						<input type="hidden" name="optType" value="<%=OptTypeConstant.PAY_ANALYSIS_32%>" />
						<input type="text" class="datetimepicker" name="startDate" value="<%= request.getAttribute("startDate") %>" /> <span> - </span>
						<input type="text" class="datetimepicker" name="endDate" value="<%= request.getAttribute("endDate") %>" /><span>&nbsp;&nbsp;</span>
						<input type="submit" value="确定" class="btn_1" />
					</form>
				</div>
				<div class="content_panel">
					<div class="content_panel_title">
						<h3>元宝消耗分布</h3>
					</div>
					<div class="content_panel_content">
						<b style="font-size:15px">总花费：<span style="color:#e25856"><%= diamondSum%></span> 元宝</b>
						<table id="table_1" class="display" cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>花费类型</th>
									<th>花费元宝</th>
									<th>总花费比例</th>
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
