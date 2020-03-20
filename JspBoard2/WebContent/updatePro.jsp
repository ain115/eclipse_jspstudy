<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="lys.board.*" %>
 <%
    //한글처리=>수정한 내용은 저장,수정X->그대로 덮어쓰기
    request.setCharacterEncoding("utf-8");
 %>
 <jsp:useBean id="article"  class="lys.board.BoardDTO" />
 <jsp:setProperty name="article" property="*" />
 <%
    //정상적으로 수정됐을때 수정이 된 레코드로 이동->게시물이 수정된 페이지로 이동하라
    String pageNum=request.getParameter("pageNum");
    BoardDAO dbPro=new BoardDAO();//updateArticle메서드 호출하기위해서 
    int check=dbPro.updateArticle(article);
    
    //수정이 성공했다면
    if(check==1){ 
    //response.sendRedirect("list.jsp");
    //http-equiv="Refresh"=>새로고침 옵션 content="초단위(몇초동안 멈춘후);url="이동페이지명"
 %>
 <meta http-equiv="Refresh" content="0;url=list.jsp?pageNum=<%=pageNum%>">
 <%}else { %>
   <script>
         alert("비밀번호가 맞지않습니다.\n 다시 비밀번호를 확인요망!")
         history.go(-1)
   </script>
 <%} %>
 
 
 
 
 
 
 
