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
<title>My JSP 'Test.jsp' starting page</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
 <link href="My97DatePicker/skin/WdatePicker.css" rel="stylesheet" type="text/css">
 <script language="javascript" type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
</head>

<body>
<label>开始时间:<input class="Wdate" type="text" id="Date_time1" onclick="WdatePicker()"></label>
              <label>结束时间:<input class="Wdate" type="text" id="Date_time2" onclick="WdatePicker()"></label>
              <input type="button" value="查询" class="button" onclick="chaxun(this);">
</body>
</html>
