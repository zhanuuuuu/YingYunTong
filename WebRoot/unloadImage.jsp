<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'index.jsp' starting page</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta name="viewport" content="width=device-width, initial-scale=1" />
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<style type="text/css">
#preview,.img,img {
	width: 100%;
	height: 100%;
}

#preview {
	border: 1px solid #000;
}
</style>
</head>

<body>

		
	<form method="POST" ENCTYPE="multipart/form-data" name="form1" id="form1" >
		<p>编号：<input type="text" id="r1" name="cGoodsNo" /></p>
		<table>
		
			<tr>
				<td><input type="file" name="file1"  />
				</td>
			</tr>
			<tr>
				<td><input type="file" name="file2"  />
				</td>
			</tr>
			<tr>
				<td><input type="button" name="Submit" onclick="javascript:changeSubmitUrl();" value="上传"> </td>
			</tr>
		</table>
	</form>
	
</body>
<script language="javascript" type="text/javascript">
    
    function changeSubmitUrl()  
    {  
        var cGoodsNo=document.getElementById("r1");
        console.log(cGoodsNo.value);
        var url="ExcetionHannder?cGoodsNo="+cGoodsNo.value;
    
        //将表单的action的URL更改为我们希望提交的URL  
        document.form1.action=url;  
        //利用Javascript提交表单  
        document.getElementById("form1").submit(); 
    }  
   </script> 
</html>