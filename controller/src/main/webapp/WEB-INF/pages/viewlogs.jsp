<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	request.setAttribute("contextPath", request.getContextPath()) ;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>View Logs :: Athena Meerkat</title>
<style type="text/css">
.osc-logs {
	color: #c1c1c1;
}
</style>
<script type="text/javascript" src="${contextPath}/resources/js/jquery.min.js"></script>
<script type="text/javascript">


	function loadLog(){
		
		$.getJSON( "${contextPath}/task/getLogs/${taskDetailId}", function( logs ) {
			$.each( logs, function( index, log ) {
				if(log == 'end') {
					
					clearInterval(intervalId);
	    			console.log('clear interval.');
					
					return false;
				}
			  	$( "<div class='osc-logs'>" + log + "</div>" ).appendTo( "body" );
			});
		});
	}
   	
    $( document ).ready(function() {
    	
    	loadLog()
    	
    	intervalId = setInterval(function(){
    		loadLog();
		}, 3000);
    	
    });
</script>

</head>
<body style="background-color: black;">

</body>
</html>