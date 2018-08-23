<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="req" value="${pageContext.request }"/>
<c:set var="base" value="${req.scheme}://${req.serverName}:${req.serverPort}${req.contextPath }/"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="${base }">
    
    <title>My JSP 'bookdesc.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<style type="text/css">
	body {
		font-size: 10pt;
	}
	div {
		margin:20px;
		border: solid 2px gray;
		width: 150px;
		height: 150px;
		text-align: center;
	}
	li {
		margin: 10px;
	}
	a {
		background: url(${base}images/all.png) no-repeat;
		display: inline-block;
		
		background-position: 0 -70px;
		margin-left: 30px;
		height: 36px;
		width: 146px;
	}
	a:HOVER {
		background: url(${base}images/all.png) no-repeat;
		display: inline-block;
		
		background-position: 0 -106px;
		margin-left: 30px;
		height: 36px;
		width: 146px;
	}
</style>
  </head>
  
  <body>
  <div>
    <img src="${book.image }"/>
  </div>
  <ul>
    <li>书名：${book.bname}</li>
    <li>作者：${book.author }</li>
    <li>单价：${book.price }</li>
  </ul>
  <c:choose>
  	<c:when test="${empty sessionScope.user }">
  		<c:set var="target" value="_parent"/>
  	</c:when>
  	<c:otherwise>
  		<c:set var="target" value=""/>
  	</c:otherwise>
  </c:choose>
  <form id="form" action="cart/CartServlet" method="post" target="${target }">
  	<input type="hidden" name="method" value="add"/>
  	<input type="hidden" name="bid" value="${book.bid }"/>
  	<input type="text" size="3" name="count" value="1"/>
  </form>
  <a href="javascript:_go()"></a>
  <script type="text/javascript">
  	function _go() {
  		var _form = document.getElementById("form");
  		_form.submit();
  	}
  </script>
  </body>
</html>
