<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Athena Meerkat</title>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/jsdifflib/diffview.css">

<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/jsdifflib/diffview.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/jsdifflib/difflib.js"></script>
<style type="text/css">
body {
	font-size: 12px;
	font-family: Sans-Serif;
}

h2 {
	margin: 0.5em 0 0.1em;
	text-align: center;
}

.top {
	text-align: center;
}

.textInput {
	display: none;
	width: 49%;
	float: left;
}

textarea {
	width: 100%;
	height: 300px;
}

label:hover {
	text-decoration: underline;
	cursor: pointer;
}

.spacer {
	margin-left: 10px;
}

.viewType {
	font-size: 16px;
	clear: both;
	text-align: center;
	padding: 1em;
	display: none;
}

#diffoutput {
	width: 100%;
}
</style>

<script type="text/javascript">
	function diffUsingJS(viewType) {
		"use strict";
		var byId = function(id) {
			return document.getElementById(id);
		}, base = difflib.stringAsLines(byId("baseText").value), newtxt = difflib
				.stringAsLines(byId("newText").value), sm = new difflib.SequenceMatcher(
				base, newtxt), opcodes = sm.get_opcodes(), diffoutputdiv = byId("diffoutput"), contextSize = byId("contextSize").value;
		diffoutputdiv.innerHTML = "";
		contextSize = contextSize || null;
		diffoutputdiv.appendChild(diffview.buildView({
			baseTextLines : base,
			newTextLines : newtxt,
			opcodes : opcodes,
			//baseTextName : "Origin Config",
			//newTextName : "Compare Config",
			baseTextName : "${firstConfigVersion}",
			newTextName : "${secondConfigVersion}",
			contextSize : contextSize,
			viewType : viewType
		}));
	}
</script>
</head>
<body onload="diffUsingJS(1);">
	<input type="hidden" id="contextSize" value="" />

	<div class="textInput">
		<h2>${firstConfigVerison}}</h2>
		<textarea id="baseText">${firstConfig}</textarea>
	</div>
	<div class="textInput spacer">
		<h2>${secondConfigVersion}</h2>
		<textarea id="newText">${secondConfig}</textarea>
	</div>
	<div id="diffoutput"></div>
</body>
</html>