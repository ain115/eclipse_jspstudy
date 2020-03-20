<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="lys.board.*" %>
 
 <%
    //정상적으로 수정됐을때 수정이 된 레코드로 이동->게시물이 수정된 페이지로 이동하라
    String pageNum=request.getParameter("pageNum");//hidden
    //--------------------------------------------------------------------------
    int num=Integer.parseInt(request.getParameter("num"));//hidden
    String passwd=request.getParameter("passwd");//직접입력
    //--------------------------------------------------------------------------
    BoardDAO dbPro=new BoardDAO();//deleteArticle메서드 호출하기위해서 
    int check=dbPro.deleteArticle(num,passwd);
    
    //삭제가 성공했다면
    if(check==1){ 
 %>
 <meta http-equiv="Refresh" content="0;url=list.jsp?pageNum=<%=pageNum%>">
 <%}else { %>
   <script>
         alert("비밀번호가 맞지않습니다.\n 다시 비밀번호를 확인요망!")
         history.go(-1)
   </script>
 <%} %>
 
 
 
 
 
 
 
