<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>다른경로에서 요청페이지를 어떻게 처리해서 보여줄것.</title>
</head>
<body>
<%
      request.setCharacterEncoding("utf-8");//한글데이터인 경우
      String msg=request.getParameter("msg");//안녕하세요?
       /*
      String number=request.getParameter("number");//"5"->5
      int num=Integer.parseInt(number);*/
      int number=Integer.parseInt(request.getParameter("number"));
      System.out.println("msg=>"+msg+",number="+number);
      //for,while
      /* (1)
      int count=0;//반복할 횟수 0
      while(number>count){    //while(5>0) 5>1,5>2,5>3,5>4,5>5
    	  out.println(msg+"<br>");
          count++;
      } */
      int count=0;//반복할 횟수 0
      while(number>count){  %> 
    	  <b><%=msg%></b><br>
 <%
          count++;
      }
%>
</body>
</html>







