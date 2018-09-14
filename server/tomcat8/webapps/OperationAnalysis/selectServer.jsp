<%@page import="org.json.JSONArray"%>
<%@page import="org.json.JSONObject"%>
<%@page import="com.domain.User"%>
<%@page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	JSONObject serverData = (JSONObject) request
			.getAttribute("serverData");
	JSONArray agentSites = new JSONArray();
	if (serverData != null) {
		agentSites = (JSONArray) serverData.getJSONArray("agentSite");
	}
	
	String selectModel = "0";
	if (request.getAttribute("selectModel") != null) {
		selectModel = request.getAttribute("selectModel").toString();
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>

<%@ include file="header.jsp"%>
<link type="text/css" href="<%=path%>/css/index.css" rel="stylesheet" />
<link type="text/css" href="<%=path%>/css/jquery-ui.min.css"
	rel="stylesheet" />
<link type="text/css" href="<%=path%>/css/selectServer.css"
	rel="stylesheet" />
<script type="text/javascript" src="<%=path%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=path%>/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=path%>/js/selectServer.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $("#tabs").tabs();
        
        var selectModel = <%= selectModel%>;
        
        if (selectModel == 2) {
        	$("#ui-id-2").click();
        }
    });
    
    function selectAll() {
    	var agents = document.getElementsByName("agent");
		if (document.getElementsByName("selectAllCB")[0].checked == true) {
			for (var i = 0; i < agents.length; i++) {
				agents[i].checked = true;
			}
		} else {
			for (var i = 0; i < agents.length; i++){
				agents[i].checked = false;
		 	}
		 }
    }
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
				<div class="content_panel" id="panel_1">
					<div id="tabs">
						<ul>
							<li><a href="#tabs-1" onclick="return false;">全服</a></li>
							<li><a href="#tabs-2" onclick="return false;">多服</a></li>
						</ul>
						<div id="tabs-1">
							<form action="<%=path%>/all" method="post">
								<input type="hidden" name="optType" value="117" /> <input
									type="hidden" name="selectType" value="1" />
								<%
									String[] agentArr = ((String)request.getSession().getAttribute("agent")).split(",");
									String[] gameSiteArr = ((String)request.getSession().getAttribute("gameSite")).split(",");
									for (int i = 0; i < agentSites.length(); i++) {
										JSONObject agentSite = agentSites.getJSONObject(i);
										JSONArray servers = agentSite.getJSONArray("gameServerList");
										if (agentArr.length > 0) {
											boolean isSelected = false;
											for (int j = 0; j < agentArr.length; j++) {
												if (agentSite.getString("agent").equals(agentArr[j])) {
													isSelected = true;
													break;
												}
											}
											if (isSelected) {
								%>
									<input type="checkbox" name="agent" checked="checked" id="<%=agentSite.getString("agent")%>"
										value="<%=agentSite.getString("agent")%>" /><label for="<%=agentSite.getString("agent")%>"><%=agentSite.getString("agent")%></label>
								<%
											} else {
								%>
									<input type="checkbox" name="agent" id="<%=agentSite.getString("agent")%>"
										value="<%=agentSite.getString("agent")%>" /><label for="<%=agentSite.getString("agent")%>"><%=agentSite.getString("agent")%></label>
								<%			
											}
										} else {
								%>
									<input type="checkbox" name="agent" id="<%=agentSite.getString("agent")%>"
										value="<%=agentSite.get("agent")%>" /><label for="<%=agentSite.getString("agent")%>"><%=agentSite.get("agent")%></label>
								<%
										}									
								%>
									&nbsp;&nbsp;&nbsp;
								<%
									}
								%>
								<br/><br/>
								<input type="checkbox" name="selectAllCB" onclick="selectAll()"/>全选<br/>
								<input class="btn_1" type="submit" value="确定" />
							</form>
						</div>
						<div id="tabs-2">
							<form action="<%=path%>/all" method="post">
								<input type="hidden" name="optType" value="117" /> <input
									type="hidden" name="selectType" value="2" />
								<%
									for (int i = 0; i < agentSites.length(); i++) {
										JSONObject agentSite = agentSites.getJSONObject(i);
										JSONArray servers = agentSite.getJSONArray("gameServerList");
								%>
								<label><%=agentSite.get("agent")%></label><br />
								<div style="overflow:auto;">
								<%
									for (int j = 0; j < servers.length(); j++) {
										JSONObject obj = servers.getJSONObject(j);
										if (gameSiteArr.length > 0) {
											boolean isSelected = false;
											String firstGameSite = obj.getString("gameSite").split(",")[0];
											for (int k = 0; k < gameSiteArr.length; k++) {
												if (firstGameSite.equals(gameSiteArr[k])) {
													isSelected = true;
													break;
												}
											}
											if (isSelected) {
								%>
								<span style="display:block; float:left;">	
								<input type="checkbox" name="gameSite" checked="checked" id="<%=obj.getString("gameSite") %>"
									value="<%=obj.getString("gameSite") + "@" + obj.getString("name")%>" /><label for="<%=obj.getString("gameSite") %>"><%=obj.getString("name") + "（" + obj.getString("openDate") + "）"%>&nbsp;&nbsp;&nbsp;
								</label>
								</span>
								<%
											} else {
								%>
								<span style="display:block; float:left;">	
								<input type="checkbox" name="gameSite" id="<%=obj.getString("gameSite") %>"
									value="<%=obj.getString("gameSite") + "@" + obj.getString("name")%>" /><label for="<%=obj.getString("gameSite") %>"><%=obj.getString("name") + "（" + obj.getString("openDate") + "）"%>&nbsp;&nbsp;&nbsp;
								</label>	
								</span>
								<%			
											}
										} else {
								%>
								<span style="display:block; float:left;">	
								<input type="checkbox" name="gameSite" id="<%=obj.getString("gameSite") %>"
									value="<%=obj.getString("gameSite") + "@" + obj.getString("name")%>" /><label for="<%=obj.getString("gameSite") %>"><%=obj.getString("name") + "（" + obj.getString("openDate") + "）"%>&nbsp;&nbsp;&nbsp;
								</label>
								</span>
								<%		
										}
									}
								%>
								</div>
								<br />
								<%
									}
								%>
								<input class="btn_1" type="submit" value="确定" />
							</form>
						</div>
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
