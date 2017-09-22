<%@page import="cn.e3mall.pojo.TbItem"%>
<%@page import="java.util.Date"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		TbItem item = new TbItem();
		item.setUpdated(new Date());
		request.setAttribute("item", item);	
	%>
	
	<fn:formatDate value="${item.updated }" pattern="MM.dd.yyyy"/>
	
</body>x
</html>