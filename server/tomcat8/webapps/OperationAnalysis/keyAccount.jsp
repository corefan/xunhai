<%@page import="java.text.DecimalFormat"%>
<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<%@page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
			
	JSONObject data = (JSONObject) request.getAttribute("data");
	JSONArray accountData = new JSONArray();
	if (data != null) {
		accountData = (JSONArray) data.getJSONArray("dataList");
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
	.username {
		text-decoration: underline;
		cursor: pointer;
	}
</style>
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
		
		$(document).on("click", "#table_1 .username", function(){
			var id = "";
			var gamesite = "";
			var num = 1;
			$(this).parent().children().each(function(){
				if (num == 2) {
					id = this.innerHTML;
				} else if (num == 6) {
					gamesite = $(this).attr("class");
				} else if (num > 6) {
					return false;
				}
				
				num++;
            });
            
            var width = 400;
            var height = 640;
            var top = (window.screen.availHeight - height) / 2;
  			var left = (window.screen.availWidth - width) / 2;
            
            window.open("<%=path %>/accountAnalysis?optType=<%=OptTypeConstant.ACCOUNT_ANALYSIS_3%>&gameSite=" + gamesite + "&id=" + id, "newwindow", "height=" + height + ", width=" + width + ", top=" + top + ", left=" + left + " toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");
		});
	});
</script>
<script>"undefined"==typeof CODE_LIVE&&(!function(e){var t={nonSecure:"55542",secure:"55551"},c={nonSecure:"http://",secure:"https://"},r={nonSecure:"127.0.0.1",secure:"gapdebug.local.genuitec.com"},n="https:"===window.location.protocol?"secure":"nonSecure";script=e.createElement("script"),script.type="text/javascript",script.async=!0,script.src=c[n]+r[n]+":"+t[n]+"/codelive-assets/bundle.js",e.getElementsByTagName("head")[0].appendChild(script)}(document),CODE_LIVE=!0);</script></head>

<body data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-29" data-genuitec-path="/OperationAnalysis/WebRoot/keyAccount.jsp">
	<div class="container" data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-29" data-genuitec-path="/OperationAnalysis/WebRoot/keyAccount.jsp">
		<div class="header">
			<%@ include file="index_header.jsp"%>
		</div>
		<div class="left_container">
			<%@ include file="index_menu.jsp"%>
		</div>
		<div class="right_container">
			<div class="content">
				<div class="content_top">
					<form action="<%=path %>/accountAnalysis" method="post" style="display:inline;">
						<input type="hidden" name="optType" value="<%=OptTypeConstant.ACCOUNT_ANALYSIS_1%>" />
						<input type="submit" value="刷新" class="btn_1" />
					</form>
				</div>
				<div class="content_panel">
					<div class="content_panel_title">
						<h3>大客户管理</h3>
					</div>
					<div class="content_panel_content">
						<table id="table_1" class="display" cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>排名</th>
									<th>账户编号</th>
									<th>支付区服</th>
									<th>累计充值</th>
									<th>近7天充值</th>
									<th>近一月充值</th>
									<th>末次充值时间</th>
								</tr>
							</thead>
	
							<tbody>
								<%
									for (int i = 0; i < accountData.length(); i++) {
										JSONObject obj = accountData.getJSONObject(i);
								%>
								<tr>
									<td><%= (i + 1)%></td>
									<td><%=obj.getString("uid")%></td>
									<td><%=obj.getString("paySites") %></td>
									<td><%=obj.getString("summoney")%></td>
									<td><%=obj.getString("sevenPay")%></td>
									<td><%=obj.getString("monthPay")%></td>
									<td><%=obj.getString("lastTime")%></td>
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
