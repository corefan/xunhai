<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
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
	JSONObject accountInfo = new JSONObject();
	String tel = "";
	String name = "";
	if (accountData.length() > 0) {
		accountInfo = accountData.getJSONObject(0);
		tel = accountInfo.getString("tel");
		name = accountInfo.getString("name");
		if (tel.equals("tel")) {
			tel = "无";
		}
	}	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>

<head>
	<link type="text/css" href="<%=path%>/css/jquery.dataTables.min.css" rel="stylesheet" />
	<style type="text/css">
		div {
			font-size: 14px;
			font-family: Arial, "微软雅黑", "宋体";
			padding: 5px;
		}
	</style>
	<base href="<%=basePath%>">
	<title><%= name %> - 客户资料信息</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="icon" href="<%=path %>/images/favicon.ico" type="image/x-icon"/>
	<script type="text/javascript" src="<%=path%>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=path%>/js/jquery.dataTables.min.js"></script>
	<script type="text/javascript" language="javascript">
		$(function() {
			$('#table_1').dataTable({
				"bLengthChange": false,
				"bInfo": false,
				"bPaginate": false,
				"bFilter":false,
				"bSort":false,
			});
		});
	</script>
<script>"undefined"==typeof CODE_LIVE&&(!function(e){var t={nonSecure:"55542",secure:"55551"},c={nonSecure:"http://",secure:"https://"},r={nonSecure:"127.0.0.1",secure:"gapdebug.local.genuitec.com"},n="https:"===window.location.protocol?"secure":"nonSecure";script=e.createElement("script"),script.type="text/javascript",script.async=!0,script.src=c[n]+r[n]+":"+t[n]+"/codelive-assets/bundle.js",e.getElementsByTagName("head")[0].appendChild(script)}(document),CODE_LIVE=!0);</script></head>

<body data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-37" data-genuitec-path="/OperationAnalysis/WebRoot/playerInfo.jsp">
	<div id="container" data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-37" data-genuitec-path="/OperationAnalysis/WebRoot/playerInfo.jsp">
		<div calss="table_container">
			<table id="table_1" class="display">
				<thead>
					<tr>
						<th>资料</th>
						<th>信息</th>
					</tr>
				</thead>
				<tbody>
				<%
					if (!accountInfo.isNull("name")) {
				%>
					<tr>
						<th>用户昵称</th>
						<td><%=accountInfo.get("name") %></td>
					</tr>
					<tr>
						<th>职业</th>
						<td><%=accountInfo.get("career") %></td>
					</tr>
					<tr>
						<th>等级</th>
						<td><%=accountInfo.get("level") %></td>
					</tr>
					<tr>
						<th>战斗力</th>
						<td><%=accountInfo.get("fightVal") %></td>
					</tr>
					<tr>
						<th>VIP等级</th>
						<td><%=accountInfo.get("vip") %></td>
					</tr>
					<tr>
						<th>公会排名</th>
						<td><%=accountInfo.get("rank") %></td>
					</tr>
					<tr>
						<th>公会职务</th>
						<td><%=accountInfo.get("position") %></td>
					</tr>
					<tr>
						<th>剩余钻石</th>
						<td><%=accountInfo.get("diamond") %></td>
					</tr>
					<tr>
						<th>剩余绑钻</th>
						<td><%=accountInfo.get("bind_diamond") %></td>
					</tr>
					<tr>
						<th>注册天数</th>
						<td><%=(int)Double.parseDouble(accountInfo.getString("regdate")) %></td>
					</tr>
					<tr>
						<th>最近七天登录次数</th>
						<td><%=accountInfo.get("lognum") %></td>
					</tr>
					<tr>
						<th>最近七天日均时长</th>
						<td><%=(int)Double.parseDouble(accountInfo.getString("logtime")) + "分钟" %></td>
					</tr>
					<tr>
						<th>电话号码</th>
						<td><%=tel %></td>
					</tr>
				<%
					}
				 %>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>
