<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%!
    //변수와 메서드를 jsp페이지에서 자유롭게 불러다 사용하고 싶다.=>선언문
    String name="홍길동";//static String name;
    //메서드는 따로 뽑아내서 클래스를 작성->메서드를 불러와서 작업=>빈즈 클래스
    public String getName(){  //내부 public static String getName(){ return name;}
    	return name;
    }
%>
<html>
<head>
<meta charset="UTF-8">
<title>JSP 3번째 예제(표현식과 선언문의 활용)</title>
</head>
<body>

<%
     float f=2.3f; //소수점은 double->float->강제형변환,변수명 뒤에 f를 줘야 된다.
     int i=Math.round(f);//클래스명.정적메서드명(~)=>2.3=>2
     java.util.Date d=new java.util.Date();//패키지명.하위패키지명...불러올 클래스명~
     out.println("d의 값은?"+d);//d.toString() ->날짜데이터를 문자로 변환해서 출력하라
%>
<%
	String str="Git 연습";
	System.out.println("str=>"+str);
	out.println("<h1>"+"str=>"+"str"+"</h1>");
%>
<hr>
str의 값 출력=<%=str%>
<hr><p>
정수 f의 반올림은? <%=i%><p>
현재의 날짜와 시간은?<%=d.toString() %>
name의 값은? <%=name %><p>
getName()을 호출? <%=getName()%>
</body>
</html>







