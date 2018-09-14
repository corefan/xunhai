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
	JSONArray diamondData = new JSONArray();
	String xAxisStr = "";
	String dataStr = "";
	if (data != null) {
		diamondData = (JSONArray) data.getJSONArray("stockDiamondData");
		for (int i = 0; i < diamondData.length(); i++) {
			JSONObject obj = diamondData.getJSONObject(i);
			String date = obj.getString("diamondStockCreateTime");
			xAxisStr += "'" + (date.substring(5, date.length())).replace("-", "/")
					+ "',";
			dataStr += obj.getString("diamondStockTotalNum") + ",";
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

		$('#chart_1').highcharts({
			chart : {
				type : 'spline'
			},
			title : {
				text : ''
			},
			subtitle : {
				text : ''
			},
			legend : {
				enabled : false
			},
			xAxis : {
				categories : [<%=xAxisStr%>]
	},
			yAxis : {
				title : {
					text : '钻石数量（单位：钻石）'		
				},
				labels : {
					formatter : function() {
						return this.value
					}
				}
			},
			tooltip : {
				crosshairs : true,
				shared : true,
			},
			plotOptions : {
				spline : {
					marker : {
						radius : 4,
						lineColor : '#666666',
						lineWidth : 1
					}
				}
			},
			series : [ {
				name : '钻石库存',
				data : [<%=dataStr%>]
			} ]
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
						<input type="hidden" name="optType" value="<%= OptTypeConstant.DATA_ANALYSIS_16%>" />
						<input type="text" class="datetimepicker" name="startDate" value="<%= startDate %>" /> <span> - </span>
						<input type="text" class="datetimepicker" name="endDate" value="<%= endDate %>" /><span>&nbsp;&nbsp;</span>
						<input type="submit" value="确定" class="btn_1" />
					</form>
				</div>
				<div class="content_help">
					<ul>
						<li><b>活跃人数：  </b>近30天玩家有登录的天数（包涵取数日）≥3天的玩家数。</li>
						<li><b>存： </b>活跃玩家取数日剩余钻石数。</li>
						<li><b>进： </b>活跃玩家取数日新增的钻石数。</li>
						<li><b>销： </b>活跃玩家取数日消费的钻石数。</li>
						<li><b>人均库存： </b>存/活跃人数。</li>
						<li><b>差额： </b>进-销。</li>
					</ul>
				</div>
				<div class="content_panel">
					<div class="content_panel_title">
						<h3>钻石库存分析</h3>
						<a class="link" href="<%=path %>/dataAnalysis?optType=216&export=excel&startDate=<%=startDate%>&endDate=<%=endDate%>">导出Excel</a>
					</div>
					<div class="content_panel_content">
						<div id="chart_1" class="chart" style="min-width:500px;height:400px"></div>
						<table id="table_1" class="display" cellspacing="0" width="100%">
							<thead>
								<tr>
									<th>日期</th>
									<th>活跃人数</th>
									<th>存</th>
									<th>进</th>
									<th>销</th>
									<th>人均库存</th>
									<th>差额</th>
								</tr>
							</thead>
	
							<tbody>
								<%
									for (int i = 0; i < diamondData.length(); i++) {
										JSONObject obj = diamondData.getJSONObject(i);
										String diamondStockBuyNum = obj.getString("diamondStockBuyNum");
										String diamondStockUseNum = obj.getString("diamondStockUseNum");
										int activePlayer = Integer.parseInt(obj.getString("activePlayer"));
										int diamondStockTotalNum = Integer.parseInt(obj.getString("diamondStockTotalNum"));
										int diamondBalance = Integer.parseInt(diamondStockBuyNum) - Integer.parseInt(diamondStockUseNum);
										int aveDiamondStock = 0;
										if (activePlayer != 0)
											aveDiamondStock = diamondStockTotalNum / activePlayer;
								%>
								<tr>
									<td><%=obj.getString("diamondStockCreateTime")%></td>
									<td><%=activePlayer %></td>
									<td><%=diamondStockTotalNum%></td>
									<td><%=diamondStockBuyNum%></td>
									<td><%=diamondStockUseNum%></td>
									<td><%=aveDiamondStock%></td>
									<td><%=diamondBalance%></td>
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
