<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<%@page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
			
	JSONObject data = (JSONObject) request.getAttribute("data");
	JSONArray gameStepData = new JSONArray();
	if (data != null) {
		gameStepData = (JSONArray) data.getJSONArray("dataList");
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
	});
</script>
<script>"undefined"==typeof CODE_LIVE&&(!function(e){var t={nonSecure:"55542",secure:"55551"},c={nonSecure:"http://",secure:"https://"},r={nonSecure:"127.0.0.1",secure:"gapdebug.local.genuitec.com"},n="https:"===window.location.protocol?"secure":"nonSecure";script=e.createElement("script"),script.type="text/javascript",script.async=!0,script.src=c[n]+r[n]+":"+t[n]+"/codelive-assets/bundle.js",e.getElementsByTagName("head")[0].appendChild(script)}(document),CODE_LIVE=!0);</script></head>

<body data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-21" data-genuitec-path="/OperationAnalysis/WebRoot/gameStep.jsp">
	<div class="container" data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-21" data-genuitec-path="/OperationAnalysis/WebRoot/gameStep.jsp">
		<div class="header">
			<%@ include file="index_header.jsp"%>
		</div>
		<div class="left_container">
			<%@ include file="index_menu.jsp"%>
		</div>
		<div class="right_container">
			<div class="content">
				<div class="content_top">
					<form action="<%=path %>/behaviorAnalysis" method="post" style="display:inline;">
						<input type="hidden" name="optType" value="<%= OptTypeConstant.BEHAVIOR_ANALYSIS_11%>" />
						<input type="submit" value="刷新数据" class="btn_1" />
					</form>
				</div>
				<div class="content_help">
					<ul>
						<li><b>绝对流失率： </b>(上一节点人数 - 节点人数) / 上一节点人数</li>
						<li><b>完成率： </b>节点完成人数  / 创角色人数</li>
					</ul>
				</div>
				<div class="content_panel">
					<div class="content_panel_title">
						<h3>节点数据分析</h3>
					</div>
					<div class="content_panel_content">
						<table id="table_1" class="display" cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>节点编号</th>
									<th>节点名称</th>
									<th>玩家数量</th>
									<th>绝对流失率</th>
									<th>完成率</th>
								</tr>
							</thead>
	
							<tbody>
								<%
									for (int i = 0; i < gameStepData.length(); i++) {
										JSONObject obj = gameStepData.getJSONObject(i);
								%>
								<tr>
									<td><%=obj.getString("gameStep")%></td>
									<td><%=obj.getString("stepName")%></td>
									<td><%=obj.getString("num")%></td>
									<td><%=obj.getString("rate1")%></td>
									<td><%=obj.getString("rate2")%></td>
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
