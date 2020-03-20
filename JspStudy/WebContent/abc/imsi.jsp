<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSP 2번째 예제</title>
</head>


<body>
스크립트위에 출력하는 count:<%=count%> 
<%
    // int count=3;
 for(int i=0;i<count;i++){
	 out.println("<h1>JSP 테스트"+i+"!<br>");//docuement.write("<h1>JSP 테스트"+i+"!<br>");
 }
 out.println("count=>"+count);//3
 
%>
스크립트아래에 출력하는 count:<%=count%> 
<%!
   //선언된 위치에 상관없이 페이지에 선언만 해놓으면 언제든지 그 페이지에서 불러다 사용이 가능한
   //변수 또는 메서드를 선언한 영역
       int count=3;
%>
</body>
</html>






