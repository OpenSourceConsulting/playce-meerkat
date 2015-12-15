<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.*" %>
<%@ page import="com.athena.dolly.enhancer.*" %>
<%@ page import="com.athena.dolly.stats.*" %>
<html>
<head>
<title>Dolly Session Statistics</title>
<meta http-equiv="Expires" content="0">
</head>
<style>
	body { font-family: "굴림", "arial"; font-size: 9pt; color: #555555; background-color: #FFFFFF; margin-top: 10px; margin-left: 15px; line-height: 13pt; }
	table { font-family: "굴림", "arial"; font-size: 9pt; color: #555555; background-color: #555555; line-height: 13pt; width: 900px; border-style: none }
	td { font-family: "굴림", "arial"; font-size: 9pt; color: #555555; background-color: #FFFFFF; line-height: 13pt; height: 18px; text-align: left }
	.title { font-family: "굴림", "arial"; font-size: 9pt; color: #FFFFFF; background-color: #6479A2; text-align: center; height: 20px; }
	.danger_row { font-family: "굴림", "arial"; font-size: 9pt; color: #415E8E; background-color: #CAD9F0;  height: 20px; }
	.td_left { text-align: left }
	.td_right { text-align: right }
	.td_center { text-align: center }
</style>
<body>
<%
	DollyStats stats = DollyManager.getInstance().getStats();
%>
<h2>Dolly Session Statistics</h2>
<ul>
	<li>
		<h4>Infinispan Properties</h4>
		<table border="1">
			<tr>
				<td class='title'>Protocol Version</td>
				<td class='title'>Version</td>
				<td class='title'>Cache Size</td>
				<td class='title'>Empty</td>
			</tr>
			<tr>
				<td class=td_center><%= stats.getProtocolVersion() %></td>
				<td class=td_center><%= stats.getVersion() %></td>
				<td class=td_center><%= stats.getSize() %></td>
				<td class=td_center><%= stats.getIsEmpty() %></td>
			</tr>
		</table>
	</li>
	<li>
		<h4>Infinispan Stats</h4>
		<table border="1">
			<tr>
				<td class='title'>Time Since Start (sec)</td>
				<td class='title'>Total Number of Entries</td>
				<td class='title'>Current Number of Entries</td>
				<td class='title'>Stores</td>
			</tr>
			<tr>
				<td class=td_center><%= stats.getTimeSinceStart() %></td>
				<td class=td_center><%= stats.getTotalNumberOfEntries() %></td>
				<td class=td_center><%= stats.getCurrentNumberOfEntries() %></td>
				<td class=td_center><%= stats.getStores() %></td>
			</tr>
		</table>
		<table border="1">
			<tr>
				<td class='title'>Retrievals</td>
				<td class='title'>Hits</td>
				<td class='title'>Misses</td>
				<td class='title'>Remove Hits</td>
				<td class='title'>Remove Misses</td>
			</tr>
			<tr>
				<td class=td_center><%= stats.getRetrievals() %></td>
				<td class=td_center><%= stats.getHits() %></td>
				<td class=td_center><%= stats.getMisses() %></td>
				<td class=td_center><%= stats.getRemoveHits() %></td>
				<td class=td_center><%= stats.getRemoveMisses() %></td>
			</tr>
		</table>
	</li>
	<li>
		<h4>Cache Data List</h4>
		<table border="1">
			<tr>
				<td class='title'>#</td>
				<td class='title'>Key</td>
				<td class='title'>Value</td>
			</tr>
			<%
				List<String> cacheKeys = stats.getCacheKeys();
				Object values = null;

				int i = 1;
				for (String key : cacheKeys) {
					values = DollyManager.getInstance().getValue(key);
			%>
			<tr>
				<td class=td_center><%= i++ %></td>
				<td class=td_center><%= key %></td>
				<td class=td_center><%= values %></td>
			</tr>
			<%
			     }
			%>
		</table>
	</li>
</ul>
</body>
</html>