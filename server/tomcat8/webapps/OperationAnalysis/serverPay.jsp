<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Calendar" %>
<%@page import="java.util.Date" %>
<%@page import="java.text.DateFormat" %>
<%@page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
			
	String startDate = request.getAttribute("startDate").toString();
	String endDate = request.getAttribute("endDate").toString();
		
	JSONObject data = (JSONObject) request.getAttribute("data");
	JSONArray serverPayData = new JSONArray();
	if (data != null) {
		serverPayData = (JSONArray) data.getJSONArray("dataList");
	}
	
	String dataHTMLStr = "";
	
	int length = serverPayData.length();
	
	String[] rows = new String[length + 1];
	for (int i = 0; i < length + 1; i++) {
		rows[i] = "";
	}
	
	List<String> dates = new ArrayList<String>();
	
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	Calendar startTime = Calendar.getInstance();
	Calendar endTime = Calendar.getInstance();
	startTime.setTime(df.parse(startDate));
	endTime.setTime(df.parse(endDate));
	 
	int intervalDay = (int)((endTime.getTimeInMillis() - startTime.getTimeInMillis()) / (24 * 60 * 60 * 1000));

	rows[0] = "<th>区服</th>";
	for (int i = 0; i < intervalDay + 1; i++) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startTime.getTime());
		calendar.set(Calendar.DAY_OF_YEAR, (startTime.get(Calendar.DAY_OF_YEAR) + i));
		String date = df.format(calendar.getTime());
		dates.add(date);
		rows[0] += "<th>" + date + "</th>";
	}
	
	for (int i = 0; i < length; i++) {
		JSONObject obj = serverPayData.getJSONObject(i);
		
		rows[i + 1] += "<tr><th>" + obj.getString("site_name") + "</th>";
		
		for (int j = 0; j < dates.size(); j++) {
			rows[i + 1] += "<td>" + obj.getString(dates.get(j)) + "</td>";
		}
		
		rows[i + 1] += "</tr>";
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

<body data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-43" data-genuitec-path="/OperationAnalysis/WebRoot/serverPay.jsp">
	<div class="container" data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-43" data-genuitec-path="/OperationAnalysis/WebRoot/serverPay.jsp">
		<div class="header">
			<%@ include file="index_header.jsp"%>
		</div>
		<div class="left_container">
			<%@ include file="index_menu.jsp"%>
		</div>
		<div class="right_container">
			<div class="content">
				<div class="content_top">
					<form action="<%=path%>/accountAnalysis" method="post">
						<input type="hidden" name="optType" value="<%=OptTypeConstant.ACCOUNT_ANALYSIS_4%>" /> <input
							type="text" class="datetimepicker" name="startDate"
							value="<%=startDate%>" /> <span>
							- </span> <input type="text" class="datetimepicker" name="endDate"
							value="<%=endDate%>" /><span>&nbsp;&nbsp;</span>
						<input type="submit" value="确定" class="btn_1" />
					</form>
				</div>
				<div class="content_panel">
					<div class="content_panel_title">
						<h3>区服充值数据</h3>
					</div>
					<div class="content_panel_content">
						<table id="table_1" class="display" cellspacing="0" width="100%">
							<thead>
								<tr>
									<%=rows[0]%>
								</tr>
							</thead>
	
							<tbody>
								<%
									for (int i = 1; i < rows.length; i++) {
								%>
								 		<%=rows[i]%>
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
