<%@page import="com.constant.OptTypeConstant"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	int pageIndex = 0;
	if (request.getAttribute("pageIndex") != null) {
		pageIndex = Integer.parseInt(request.getAttribute("pageIndex").toString());
	}
%>
<link type="text/css" href="<%= request.getContextPath()%>/css/index_menu.css" rel="stylesheet"  data-genuitec-lp-enabled="false" data-genuitec-file-id="wc1-28" data-genuitec-path="/OperationAnalysis/WebRoot/index_menu.jsp"/>
<script type="text/javascript" src="<%= request.getContextPath()%>/js/index_menu.js"> </script>

<div id="menu">
	<div id="menucontent">
		<ul>
			<li class="menu">
				<a href="" onclick="return false" class="menuheader">运营分析</a>
				<ul>
					<li>
						<a class="<%= pageIndex == 101 ? "menuitem_current" : "menuitem_none"%>" href="<%= request.getContextPath()%>/dataAnalysis?optType=<%= OptTypeConstant.DATA_ANALYSIS_2%>">实时监控</a>
					</li>
					<li>
						<a class="<%= pageIndex == 102 ? "menuitem_current" : "menuitem_none"%>" href="<%= request.getContextPath()%>/dataAnalysis?optType=<%= OptTypeConstant.DATA_ANALYSIS_4%>">留存分析</a>
					</li>
<%-- 					<li>
						<a class="<%= pageIndex == 104 ? "menuitem_current" : "menuitem_none"%>" href="<%= request.getContextPath()%>/dataAnalysis?optType=<%= OptTypeConstant.DATA_ANALYSIS_16%>">钻石库存</a>
					</li> --%>
					<li>
						<a class="<%= pageIndex == 105 ? "menuitem_current" : "menuitem_none"%>" href="<%= request.getContextPath()%>/dataAnalysis?optType=<%= OptTypeConstant.DATA_ANALYSIS_18%>">游戏区看盘</a>
					</li>
					<li>
						<a class="<%= pageIndex == 106 ? "menuitem_current" : "menuitem_none"%>" href="<%= request.getContextPath()%>/dataAnalysis?optType=<%= OptTypeConstant.DATA_ANALYSIS_19%>">指标趋势看盘</a>
					</li>
<%-- 					<li>
						<a class="<%= pageIndex == 107 ? "menuitem_current" : "menuitem_none"%>" href="<%= request.getContextPath()%>/dataAnalysis?optType=<%= OptTypeConstant.DATA_ANALYSIS_20%>">活跃度监控</a>
					</li> --%>
					<li>
						<a class="<%= pageIndex == 108 ? "menuitem_current" : "menuitem_none"%>" href="<%= request.getContextPath()%>/dataAnalysis?optType=<%= OptTypeConstant.DATA_ANALYSIS_21%>">在线时长分析</a>
					</li>
					<li>
						<a class="<%= pageIndex == 109 ? "menuitem_current" : "menuitem_none"%>" href="<%= request.getContextPath()%>/dataAnalysis?optType=<%= OptTypeConstant.DATA_ANALYSIS_22%>">登录用户构成</a>
					</li>
					<li>
						<a class="<%= pageIndex == 311 ? "menuitem_current" : "menuitem_none"%>" href="<%= request.getContextPath()%>/behaviorAnalysis?optType=<%= OptTypeConstant.BEHAVIOR_ANALYSIS_11%>">新手节点流失分析</a>
					</li>
				</ul>
			</li>
			<li class="menu">
				<a href="" onclick="return false" class="menuheader">客户管理</a>
				<ul>
					<li>
						<a class="<%= pageIndex == 401 ? "menuitem_current" : "menuitem_none"%>" href="<%= request.getContextPath()%>/accountAnalysis?optType=<%=OptTypeConstant.ACCOUNT_ANALYSIS_1%>">大客户管理</a>
					</li>
					<li>
						<a class="<%= pageIndex == 402 ? "menuitem_current" : "menuitem_none"%>" href="<%= request.getContextPath()%>/accountAnalysis?optType=<%=OptTypeConstant.ACCOUNT_ANALYSIS_2%>">充值订单</a>
					</li>
					<li>
						<a class="<%= pageIndex == 403 ? "menuitem_current" : "menuitem_none"%>" href="<%= request.getContextPath()%>/accountAnalysis?optType=<%=OptTypeConstant.ACCOUNT_ANALYSIS_4%>">区服充值数据</a>
					</li>
				</ul>
			</li>
			
			<%
				if (request.getSession().getAttribute("canUsePay") != null && Boolean.parseBoolean(request.getSession().getAttribute("canUsePay").toString()) == true) {
			 %>
			
			<li class="menu">
				<a href="" onclick="return false" class="menuheader">付费分析</a>
				<ul>
					<li>
						<a class="<%= pageIndex == 201 ? "menuitem_current" : "menuitem_none"%>" href="<%= request.getContextPath()%>/payAnalysis?optType=<%=OptTypeConstant.PAY_ANALYSIS_31%>">商城销量分析</a>
					</li>
					<li>
						<a class="<%= pageIndex == 202 ? "menuitem_current" : "menuitem_none"%>" href="<%= request.getContextPath()%>/payAnalysis?optType=<%=OptTypeConstant.PAY_ANALYSIS_32%>">元宝消耗分布</a>
					</li>
					<li>
						<a class="<%= pageIndex == 203 ? "menuitem_current" : "menuitem_none"%>" href="<%= request.getContextPath()%>/payAnalysis?optType=<%=OptTypeConstant.PAY_ANALYSIS_41%>">首次付费分析</a>
					</li>
					<li>
						<a class="<%= pageIndex == 204 ? "menuitem_current" : "menuitem_none"%>" href="<%= request.getContextPath()%>/payAnalysis?optType=<%=OptTypeConstant.PAY_ANALYSIS_42%>">首次付费等级分布</a>
					</li>
				</ul>
			</li>
			
			<li class="menu">
				<a href="" onclick="return false" class="menuheader">中控系统</a>
				<ul>
					<li>
						<a href="<%= request.getContextPath()%>/gccServer.jsp">中控系统</a>
					</li>
				</ul>
			</li>
			
			<%
				}
				
				if (request.getSession().getAttribute("canUseBeaviour") != null && Boolean.parseBoolean(request.getSession().getAttribute("canUseBeaviour").toString()) == true) {
			 %>
			
<%-- 			<li class="menu">
				<a href="" onclick="return false" class="menuheader">行为监控</a>
				<ul>
					<li>
						<a class="<%= pageIndex == 301 ? "menuitem_current" : "menuitem_none"%>" href="<%= request.getContextPath()%>/behaviorAnalysis?optType=<%= OptTypeConstant.BEHAVIOR_ANALYSIS_1%>">精力使用情况</a>
					</li>
					<li>
						<a class="<%= pageIndex == 302 ? "menuitem_current" : "menuitem_none"%>" href="<%= request.getContextPath()%>/behaviorAnalysis?optType=<%= OptTypeConstant.BEHAVIOR_ANALYSIS_2%>">每日副本情况</a>
					</li>
					<li>
						<a class="<%= pageIndex == 303 ? "menuitem_current" : "menuitem_none"%>" href="<%= request.getContextPath()%>/behaviorAnalysis?optType=<%= OptTypeConstant.BEHAVIOR_ANALYSIS_3%>">多人副本情况</a>
					</li>
					<li>
						<a class="<%= pageIndex == 304 ? "menuitem_current" : "menuitem_none"%>" href="<%= request.getContextPath()%>/behaviorAnalysis?optType=<%= OptTypeConstant.BEHAVIOR_ANALYSIS_4%>">恶魔城情况</a>
					</li>
					<li>
						<a class="<%= pageIndex == 305 ? "menuitem_current" : "menuitem_none"%>" href="<%= request.getContextPath()%>/behaviorAnalysis?optType=<%= OptTypeConstant.BEHAVIOR_ANALYSIS_5%>">王者争霸情况</a>
					</li>
					<li>
						<a class="<%= pageIndex == 306 ? "menuitem_current" : "menuitem_none"%>" href="<%= request.getContextPath()%>/behaviorAnalysis?optType=<%= OptTypeConstant.BEHAVIOR_ANALYSIS_6%>">竞技场情况</a>
					</li>
					<li>
						<a class="<%= pageIndex == 307 ? "menuitem_current" : "menuitem_none"%>" href="<%= request.getContextPath()%>/behaviorAnalysis?optType=<%= OptTypeConstant.BEHAVIOR_ANALYSIS_7%>">炼金情况</a>
					</li>
					<li>
						<a class="<%= pageIndex == 308 ? "menuitem_current" : "menuitem_none"%>" href="<%= request.getContextPath()%>/behaviorAnalysis?optType=<%= OptTypeConstant.BEHAVIOR_ANALYSIS_8%>">日常任务环数</a>
					</li>
					<li>
						<a class="<%= pageIndex == 309 ? "menuitem_current" : "menuitem_none"%>" href="<%= request.getContextPath()%>/behaviorAnalysis?optType=<%= OptTypeConstant.BEHAVIOR_ANALYSIS_9%>">公会任务环数</a>
					</li>
					<li>
						<a class="<%= pageIndex == 310 ? "menuitem_current" : "menuitem_none"%>" href="<%= request.getContextPath()%>/behaviorAnalysis?optType=<%= OptTypeConstant.BEHAVIOR_ANALYSIS_10%>">诅咒殿堂时长分布</a>
					</li>

				</ul>
			</li> --%>
			<%
				}
			 %>
		</ul>
	</div>
</div>