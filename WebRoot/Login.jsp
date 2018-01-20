<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'Login.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<!-- 新 Bootstrap 核心 CSS 文件 -->
<link
	href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css"
	rel="stylesheet">

<!-- 可选的Bootstrap主题文件（一般不使用） -->
<script
	src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap-theme.min.css"></script>

<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script
	src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script
	src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	
	
	<style type="text/css">
<!--
#Layer1 {
	position:absolute;
	left:151px;
	top:12px;
	width:1311px;
	height:124px;
	z-index:1;
}
#Layer2 {
	position:absolute;
	left:152px;
	top:151px;
	width:1308px;
	height:565px;
	z-index:2;
}
-->
</style>
</head>
<body>
<div id="Layer1">
<img alt="logo" src="">

</div>

	<form action="Report_Servlet?action=login" method="POST">
		<div class="form-group">
			<input type="text" class="form-control" id="name" name="name"
				placeholder="工号">
		</div>
		<div class="form-group">
			<input type="password" class="form-control" id="name" name="pass"
				placeholder="请输入密码">
		</div>
		<button type="submit" class="btn btn-default">登录</button>

	</form>
<div id="Layer2">

</div>
</body>
</html>
