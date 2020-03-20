<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.sql.Timestamp,lys.board.*" %>
 <%
    //한글처리
    request.setCharacterEncoding("utf-8");
    //BoardDTO->Setter Method(5)+hidden(4)+2개(작성)=>setWriter,setEmail,,,
    //BoardDTO article=new BoardDTO();
    //BoardDAO객체 필요
 %>
 <jsp:useBean id="article"  class="lys.board.BoardDTO" />
 <jsp:setProperty name="article" property="*" />
 <%
    //작성날짜,원격ip주소 구해서 저장=>readcount->0으로 디폴트 설정
    Timestamp temp=new Timestamp(System.currentTimeMillis());//컴퓨터의 시간
    article.setReg_date(temp);//작성날짜,시간
    article.setIp(request.getRemoteAddr());//원격ip주소 저장
    BoardDAO dbPro=new BoardDAO();
    dbPro.insertArticle(article);
    //데이터공유하면서 이동X->그냥 이동
    response.sendRedirect("list.jsp");
 %>
 
 
 
 
 
 
 
 
 
 
 
