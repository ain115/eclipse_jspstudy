<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSP 구성요소</title>
</head>
<body>
<% 
    //자바코드를 사용할 수있도록 해주는 영역(=스크립트릿)(변수선언, 간단한 제어문 정도)
    String str="이연수";
    System.out.println("str="+str);//콘솔에 출력(=>디버깅할때 필요)
    out.println("<h1>"+str+"</h1>");//웹에 출력<->document.write("str=>"+str);
    //<h1>test</h1> 내부에 태그를 사용하지 못한다.
 %>
</body>
</html>


