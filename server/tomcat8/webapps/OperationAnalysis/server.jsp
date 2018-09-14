<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<%@page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
			
	JSONObject data = (JSONObject) request.getAttribute("data");
	JSONArray serverData = new JSONArray();
	if (data != null) {
		serverData = (JSONArray) data.getJSONArray("gameData");
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
/* 	$(function() {
		$('#table_1').dataTable({
		});
		
		$('.datetimepicker').datetimepicker({
			lang : 'ch',
			timepicker : false,
			format : 'Y-m-d',
			formatDate : 'Y-m-d',
		});
	}); */
	
	$(function() {
		$('#table_1').dataTable({
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
					<form action="<%=path %>/dataAnalysis" method="post">
						<input type="hidden" name="optType" value="<%= OptTypeConstant.DATA_ANALYSIS_18%>" />
						<input type="submit" value="刷新数据" class="btn_1" />
					</form>
				</div>
				<div class="content_help">
					<ul>
						<li><b>创号率：  </b>角色数/注册人数。</li>
						<li><b>付费率：  </b>付费人数/注册人数。</li>						
						<li><b>人均付费ARPU： </b>付费金额/付费人数。</li>
						<li><b>人均注册ARPU： </b>付费金额/注册人数。</li>
						<li><b>留存率： </b>开服当日注册人数中N天后登录人数/开服当日注册人数。</li>
					</ul>
				</div>
				<div class="content_panel">
					<div class="content_panel_title">
						<h3>游戏区看盘</h3>
					</div>
					<div class="content_panel_content">
						<table id="table_1" class="display" cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>序号</th>
									<th>区服编号</th>
									<th>区服名称</th>
									<th>开区时间</th>
									<th>注册用户数</th>
									<th>创角用户数</th>
									<th>创号率</th>
									<th>游戏角色数</th>									
									<th>付费人数</th>
									<th>付费率</th>
									<th>付费金额</th>
									<th>人均付费ARPU</th>
									<th>人均注册ARPU</th>
									<th>次日留存率</th>
									<th>7日留存率</th>
									<th>双周留存率</th>
									<th>月留存率</th> 
								</tr>
							</thead>
	
							<tbody>
								<%
									int dataSize = serverData.length();
									for (int i = 0; i < dataSize; i++) {
										JSONObject obj = serverData.getJSONObject(i);
								%>
								<tr>
									<td><%=i + 1%></td>
									<td><%=obj.getString("gameSite")%></td>
									<td><%=obj.getString("serverName")%></td>
									<td><%=obj.getString("openServerDate")%></td>
									<td><%=obj.getString("regNum")%></td>
									<td><%=obj.getString("createNum")%></td>
									<td><%=obj.getString("playRate")%></td>
									<td><%=obj.getString("roleNum")%></td>									
									<td><%=obj.getString("payNum")%></td>
									<td><%=obj.getString("payRate")%></td>
									<td><%=obj.getString("payMoney")%></td>
									<td><%=obj.getString("pArpu")%></td>
									<td><%=obj.getString("nArpu")%></td>
									<td><%=obj.getString("type1")%></td>
									<td><%=obj.getString("type7")%></td>
									<td><%=obj.getString("type14")%></td>
									<td><%=obj.getString("type30")%></td> 
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
