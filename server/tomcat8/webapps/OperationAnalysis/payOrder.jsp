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
			
	JSONObject data = (JSONObject) request.getAttribute("data");
	JSONArray payOrderData = new JSONArray();
	if (data != null) {
		payOrderData = (JSONArray) data.getJSONArray("dataList");
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
					<form action="<%=path %>/accountAnalysis" method="post">
						<input type="hidden" name="optType" value="<%= OptTypeConstant.ACCOUNT_ANALYSIS_2%>" />
						<input type="text" class="datetimepicker" name="startDate" value="<%= startDate %>" /> <span> - </span>
						<input type="text" class="datetimepicker" name="endDate" value="<%= endDate %>" /><span>&nbsp;&nbsp;</span>
						<input type="submit" value="确定" class="btn_1" />
					</form>
				</div>
				<div class="content_panel">
					<div class="content_panel_title">
						<h3>充值订单</h3>
						<a class="link" href="<%=path %>/accountAnalysis?optType=<%= OptTypeConstant.ACCOUNT_ANALYSIS_2%>&export=excel&startDate=<%=startDate%>">导出本月Excel报表（以开始日期为准）</a>
					</div>
					<div class="content_panel_content">
						<table id="table_1" class="display" cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>支付记录ID</th>
									<th>用户编号</th>
									<th>玩家编号</th>
									<th>支付区服</th>
									<th>商户订单号</th>
									<th>平台订单号</th>
									<th>金额</th>
									<th>支付类型</th>
									<th>商品编号</th>
									<th>链接参数</th>
									<th>订单状态</th>
									<th>支付时间</th>
								</tr>
							</thead>
	
							<tbody>
								<%
									for (int i = 0; i < payOrderData.length(); i++) {
										JSONObject obj = payOrderData.getJSONObject(i);
								%>
								<tr>
									<td><%=obj.getString("logId")%></td>
									<td><%=obj.getString("userId")%></td>
									<td><%=obj.getString("playerId")%></td>
									<td><%=obj.getString("paySite") %></td>
									<td><%=obj.getString("outOrderNo")%></td>
									<td><%=obj.getString("orderNo")%></td>
									<td><%=obj.getString("money")%></td>
									<td><%=obj.getString("payType")%></td>
									<td><%=obj.getString("payItemId")%></td>
									<td><%=obj.getString("payUrl")%></td>
									<td><%=obj.getString("state")%></td>
									<td><%=obj.getString("createTime")%></td>
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
