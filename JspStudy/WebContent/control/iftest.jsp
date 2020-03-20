<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>요청을 받아서 처리해주는 페이지 작성</title>
<%!
      String msg;//전달받은 색깔(영어)->한글로 바꿔서 출력(색깔)
%>
<%
      request.setCharacterEncoding("utf-8");
%>
<%
   //외부에서 전달해주는 변수를 받아서 처리해주는 내장객체가 존재->request(요청객체)
   //요구사항->분석->처리결과를 html와 결합->재전송해주는 객체->response(응답객체)
   //형식) String 반환값=request.getParameter("전달받은 매개변수명")
   //request.setCharacterEncoding("utf-8");//euc-kr
   
   String name=request.getParameter("name");
   String color=request.getParameter("color");
   System.out.println("name=>"+name+",color="+color);//영어로 전달
   //문자열 비교=>equals() or conentEquals()를 사용
   if(color.equals("blue")){
	   msg="파란색";
   }else if(color.equals("red")){
	   msg="붉은색";
   }else if(color.equals("orange")){
	   msg="오렌지색";
   }else {
	   color="white";//디폴트
	   msg="기타색(흰색)";
   }
%>
</head>
<body bgcolor="<%=color%>">
<%=name %>님이 좋아하는 색깔은 ? <%=msg %>입니다.
</body>
</html>






