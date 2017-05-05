<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>加班报名</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
    <link rel="icon" href="/hello.ico" mce_href="/hello.ico" type="image/x-icon">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript" src="/js/jquery-1.9.1.min.js"></script>
	<script type="text/javascript">
	$(function(){
		$("#submit_id").click(function(){
			var userName = $("#userName").val();
			window.location = 'menu.html';
			<%--$.post("<%=path %>/overtime", {userName:userName},function(data){--%>
				<%--if (1 == data) {--%>
					<%--alert("报名成功！");--%>
					<%--location.reload();--%>
				<%--} else {--%>
					<%--alert("报名失败！");--%>
					<%--location.reload();--%>
				<%--}--%>
			<%--});--%>
		});
	})
		
	</script>
	
  </head>
  
  <body style="width:80%; margin-top:3%;">
    <form id="myform" enctype="application/x-www-form-urlencoded" method="post">
    	<div><b>人名: </b><input type="text" id="userName" name="userName" />
    	<input type="button" value=" 报名 " id="submit_id" style="width:48px;height:24px;" /></div>
    </form>
  </body>
</html>
