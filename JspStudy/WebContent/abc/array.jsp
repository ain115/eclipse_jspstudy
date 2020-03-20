<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>중간점검(배열의 값을 출력)</title>
<%!
    String keyword[]={"Scriptlet","Expression","Declaration"};
%>
</head>
<body>
<h1>요청을 하고 요청에 따라서 응답을 해주는 페이지가 한개로 구성</h1>
<table border="1" width="200">
  <%
     for(int i=0;i<keyword.length;i++){ %>
    	 <tr><td><%=i%></td>
    	        <td><%=keyword[i]%>
    	 </tr>
  <%  }  %>
</table>
</body>
</html>



