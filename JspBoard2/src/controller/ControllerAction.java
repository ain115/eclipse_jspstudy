package controller;

import java.io.*;//FileInputStream
import java.util.*;//Map,Properties(DB연동에 관련된 환경설정파일)
import javax.servlet.*;
import javax.servlet.http.*;
//추가->다른 패키지의 클래스나 인터페이스를 참조
import action.CommandAction;//요청을 받아서 처리해주는 클래스를 사용(객체형변환)

public class ControllerAction extends HttpServlet {
	
    //명령어와 명령어 처리클래스를 쌍으로 저장
    private Map commandMap = new HashMap();
    
	//서블릿을 실행시 서블릿의 초기화 작업->생성자
    public void init(ServletConfig config) throws ServletException {
    	
  //경로에 맞는 CommandPro.properties파일을 불러옴
    String props = config.getInitParameter("propertyConfig");//web.xml에서 전달받음
    System.out.println("불러온경로="+props);
    
  //명령어와 처리클래스의 매핑정보를 저장할
  //Properties객체 생성
    Properties pr = new Properties();
    FileInputStream f = null;//파일불러올때 
    
        try {
           //CommandPro.properties파일의 내용을 읽어옴
        	f=new FileInputStream(props);
        	//파일의 정보를 Properties에 저장
        	pr.load(f);
        	
        }catch(IOException e){
          throw new ServletException(e);
        }finally{
        if(f!=null) try{f.close();}catch(IOException ex){}	
        }
        	
     //객체를 하나씩 꺼내서 그 객체명으로 Properties
     //객체에 저장된 객체를 접근
     Iterator keyiter = pr.keySet().iterator();
     
     //요청명령어가 존재한다면
     while(keyiter.hasNext()){
       //요청한 명령어를 구하기위해
       String command = (String)keyiter.next();
       System.out.println("요청 command="+command);
       //요청한 명령어(키)에 해당하는 클래스명을 구함
       String className=pr.getProperty(command);
       System.out.println("요청 className="+className);//요청받아서 처리해주는 클래스명
       
       try{
       //그 클래스의 객체를 얻어오기위해 메모리에 로드
       Class commandClass = Class.forName(className);
       System.out.println("commandClass="+commandClass);//요청클래스 얻어옴
       //요청클래스명.newInstance()=>요청클래스의 객체를 얻어올 수있다.
       Object commandInstance = commandClass.newInstance();
       System.out.println("commandInstance="+commandInstance);
      
       //Map객체 commandMap에 저장=>요청을 받아서 처리할때 필요로하는 객체를 바로바로 
       //                                             사용하기 쉽게 미리 만들어서 넣어주는 역할
       commandMap.put(command, commandInstance);
       System.out.println("commandMap="+commandMap);
       
            } catch (ClassNotFoundException e) {
                throw new ServletException(e);
            } catch (InstantiationException e) {
                throw new ServletException(e);
            } catch (IllegalAccessException e) {
                throw new ServletException(e);
            }
        }//while
    }

    public void doGet(//get방식의 서비스 메소드
                     HttpServletRequest request, 
                     HttpServletResponse response)
    throws ServletException, IOException {
    	    requestPro(request,response);
    }

    protected void doPost(//post방식의 서비스 메소드
                     HttpServletRequest request, 
                     HttpServletResponse response)
    throws ServletException, IOException {
    	    requestPro(request,response);
    }

    //*** 시용자의 요청을 분석해서 해당 작업을 처리  ******
    private void requestPro(HttpServletRequest request,
    		                             HttpServletResponse response) 
                                          throws ServletException, IOException {
       String view=null;//요청명령어에 따라서 이동할 페이지의 이름을 저장->ex) list.jsp
       // /list.do=action.ListAction=>객체가 필요
       /*
        * ListAction com=null; ListAction com=new ListAction(),,,,
        *  CommandAction com=new ListAction();//글목록보기의 경우
        *  CommandAction com=new WriteFormAction();//글쓰기폼
        *  ,,,
        */
       CommandAction com=null;//어떠한 자식클래스의 객체라도 부모형으로 형변환
   	   try {
   		   //요청명령어를 분리해주는 코드->ex) list.jsp
   		   String command=request.getRequestURI(); //  /Jspboard2/list.do
   		   //  /프로젝트명/요청명령어
   		   System.out.println("request.getRequestURI()=>"+request.getRequestURI());
   		   //프로젝트명을 얻어온다.
   		   System.out.println("request.getContextPath()=>"+request.getContextPath());
   	       //  /Jspboard2/list.do
   		   // /Jspboard2
   		   if(command.indexOf(request.getContextPath())==0) {
   			   command=command.substring(request.getContextPath().length());
   			   System.out.println("실질적인 요청명령어 command=>"+command); // /list.do
   		   }
   		   // /요청명령어 -> /list.do=action.ListAction객체
   		   com=(CommandAction)commandMap.get(command);//~get(/list.do)
   		   System.out.println("com=>"+com);//action.ListAction@주소값(처리객체명)
   		   //이동할 페이지명 얻어오기
   		   view=com.requestPro(request, response);
   		   System.out.println("이동할 페이지명(view)=>"+view); // /list.jsp
   	   }catch(Throwable e) {
   		   throw new ServletException(e);//서블릿 예외처리클래스
   	   }
   	   //위에서 요청명령어에 해당하는 view로 데이터를 공유시키면서 이동
   	   RequestDispatcher dispatcher=request.getRequestDispatcher(view);// /list.jsp
   	   dispatcher.forward(request, response);//forward메서드를 이용해서 데이터를 공유->이동
    }
}





