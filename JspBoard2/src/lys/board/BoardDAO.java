package lys.board;

//has a 관계->서로 관련이 있는 클래스들의 관계설정
import java.sql.*;//DB사용
import java.util.*;//ArrayList,List을 사용하기위해서 

public class BoardDAO { //MemberDAO

	private DBConnectionMgr pool=null;//1.얻어올 객체 선언
	//웹상에서 공통으로 사용할 멤버변수
	private Connection con=null;//DB접속할때 필요
	private PreparedStatement pstmt=null;//훨씬 속도가 더 빠르다 > stmt객체 보다
	private ResultSet rs=null;//select구문->표형태로 얻어오기
	private String sql="";//실행시킬 SQL구문 저장
	
	//2.생성자를 통해서 상대방의 객체를 생성해서 연결하라
	public BoardDAO() {
		try {
			pool=DBConnectionMgr.getInstance();
			System.out.println("pool=>"+pool);
		}catch(Exception e) {
			System.out.println("pool=>"+pool);
		}
	}
	
	//1.페이징처리를 위해서 전체 레코드수를 구해와야 된다.=>메서드
	//select count(*) from board //->select count(*) from member
	public int getArticleCount() {  //public int getMemberCount(){}
		int x=0;//레코드갯수
		
		try {
			con=pool.getConnection();
			System.out.println("con=>"+con);
			sql="select count(*) from board";//sql="select count(*) from member";
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			//검색된 레코드갯수가 있다면
			if(rs.next()) {
				x=rs.getInt(1);    //변수명=rs.get자료형(필드명 또는 인덱스번호)
			}        //불러올 데이터가 필드명이 아니기에 select~from사이의 나오는 인덱스번호로 불러옴
		}catch(Exception e) {
			System.out.println("getArticleCount() 메서드 오류=>"+e);
		}finally {
			pool.freeConnection(con,pstmt,rs);
		}
		return x;
	}
	//2.글목록보기에 대한 메서드 구현->레코드가 한개이상->한 페이지당 10개씩 끊어서 보여주는 기술
	//1.레코드의 시작번호         2.불러올 레코드의 갯수(10,15,30~)
	//public Vector<ZipcodeBean>
	public List<BoardDTO> getArticles(int start,int end){
		
		List<BoardDTO> articleList=null;//ArrayList<BoardDTO> articleList=null;
		
		try {
			con=pool.getConnection();
			sql="select * from board order by ref desc,re_step asc limit ?,?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, start-1);//mysql은 레코드순번이 내부적으로 0부터 시작
			pstmt.setInt(2, end);//몇개씩 끊어서 보여줄것인지를 지정
			rs=pstmt.executeQuery();
			//페이징 처리에서는 기본적으로 누적의 개념을 도입
			//기존의 레코드외에 추가된 레코드를 첨부해서 다같이 보여줄려면 누적(do~while)
			if(rs.next()) {//레코드가 존재한다면(최소 만족 1개)
				//articleList=new List()(X)
				//형식) articleList = new 자식클래스명();
				articleList=new ArrayList<BoardDTO>(end);//10->10개만 저장가능한 데이터 저장공간이 만들어진다
			    do {
			    	BoardDTO article=makeArticleFromResult();
			    	/*
			    	BoardDTO article=new BoardDTO();//MemberDTO mem=new MemberDTO()
			    	article.setNum(rs.getInt("num"));
			    	article.setWriter(rs.getString("writer"));
			    	article.setEmail(rs.getString("email"));
			    	article.setSubject(rs.getString("subject"));
			    	article.setPasswd(rs.getString("passwd"));
			    	article.setReg_date(rs.getTimestamp("reg_date"));//오늘날짜
			    	//정수값(조회수,답변에 대한 필드)
			    	article.setReadcount(rs.getInt("readcount"));//default->0
			    	article.setRef(rs.getInt("ref"));//그룹번호
			    	article.setRe_step(rs.getInt("re_step"));//답변글의 순서
			    	article.setRe_level(rs.getInt("re_level"));//들여쓰기
			    	article.setContent(rs.getString("content"));//글내용
			    	article.setIp(rs.getString("ip"));//글쓴이의 ip주소
			    	*/
			    	//레코드를 찾을때마다.
			    	articleList.add(article);//vecList.add(zipbean);
			    	//--------------------------
			    }while(rs.next());
			}
			
		}catch(Exception e) {
			System.out.println("getArticles() 메서드 오류발생=>"+e);
		}finally {
			pool.freeConnection(con,pstmt,rs);
		}
		return articleList;
	}
	//3.글쓰기 및 답변글까지 구현
	//insert into board values(?,?,,,,,)=>반환값 X, 매개변수 O
	public void insertArticle(BoardDTO article) {//~(MemberDTO member)
		//1.article->신규글인지 답변글인지 구분
		int num=article.getNum();//0(신규글) 0이아닌경우(1,2,3,,,,>)답변글인지 구분목적
		int ref=article.getRef();
		int re_step=article.getRe_step();
		int re_level=article.getRe_level();
		//테이블에 입력할 게시물 번호를 저장할 변수
		int number=0;//데이터를 넣어줄때 필요로하는 게시물 번호(신규글 목적)
		
		System.out.println("insertArticle 메서드 내부의 num=>"+num);
		System.out.println("ref="+ref+",re_step=>"+re_step+",re_level=>"+re_level);
	  
		try {
			con=pool.getConnection();
			sql="select max(num) from board";//최대값+1=>실제 저장할 게시물번호를 생성
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next()) {//현재 테이블에서 데이터가 존재한다면
				number=rs.getInt(1)+1;
			}else {//맨처음에 레코드가 한개라도 없다면 무조건 number=1
				number=1;
			}
			//만약에 답변글이라면
			if(num!=0) {
				//답변글에 해당되는 SQL구문
				//같은 그룹번호를 가지고 있으면서 나(새로 추가된 답변글)보다 step값이 큰답변글을 찾아서
				//re_step값을 하나증가시킨다.
				sql="update board set re_step=re_step+1 where ref=? and re_step > ?";
				pstmt=con.prepareStatement(sql);
				pstmt.setInt(1, ref);//같은 그룹번호
				pstmt.setInt(2, re_step);
				int update=pstmt.executeUpdate();
				System.out.println("댓글수정유무(update)=>"+update);//1 or 0
				//답변글->하나씩 값을 증가시켜야 된다.
				re_step=re_step+1;
				re_level=re_level+1;
			}else {//신규글이라는 얘기
				ref=number;//그룹번호가 단독으로 사용할때에는 num와 같이 구분자로 사용하기때문에
				re_step=0;
				re_level=0;
			}
			//12개의 필드를 넣어준다.(num->자동으로 넣어들어간다. readcount=>0(디폴트로설정)
			sql="insert into board(writer,email,subject,passwd,reg_date,";
			sql+=" ref,re_step,re_level,content,ip)values(?,?,?,?,?,?,?,?,?,?)";
			//sql+=" ref,re_step,re_level,content,ip)values(?,?,?,?,now(),?,?,?,?,?)";
			pstmt=con.prepareStatement(sql);//                           sysdate
			pstmt.setString(1, article.getWriter());//웹에 입력저장->Setter Method호출
			pstmt.setString(2, article.getEmail());
			pstmt.setString(3, article.getSubject());
			pstmt.setString(4, article.getPasswd());
			//writePro.jsp(날짜를 어떻게 저장?)
			pstmt.setTimestamp(5, article.getReg_date());
			//-------ref,re_step,re_level
			pstmt.setInt(6, ref);//pstmt.setInt(6,article.getRef()(X))
			pstmt.setInt(7, re_step);//0 or 1~
			pstmt.setInt(8, re_level);//0
			//-------------------------------------------------------------
			pstmt.setString(9, article.getContent());//글내용
			pstmt.setString(10, article.getIp());//request.getRemoteAddr();
			int insert=pstmt.executeUpdate();
			System.out.println("게시판의 글쓰기 성공유무(insert)=>"+insert);//1성공, 0실패
		}catch(Exception e) {
			System.out.println("insertArticle 메서드오류발생=>"+e);
		}finally {
			pool.freeConnection(con, pstmt, rs);
		}
	}
	
	//4.글상세보기 위한 메서드호출
	//형식) select * from board where num=?->반환형 O  매개변수 O
	//형식2) update board set readcount=readcount+1 where num=?
	public BoardDTO getArticle(int num) {
		
       BoardDTO article=null;//한개의 레코드를 담을 객체선언
		
		try {
			con=pool.getConnection();
			sql="update board set readcount=readcount+1 where num=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, num);
			int update=pstmt.executeUpdate();
			System.out.println("조회수 증가유무(update)=>"+update);
			
			//2.조회수를 증가시킨 게시물을 찾아서 담아서 출력
			sql="select * from board where num=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {//레코드가 존재한다면(최소 만족 1개)
				    article=makeArticleFromResult();
				   /*
				    article=new BoardDTO();
			    	article.setNum(rs.getInt("num"));
			    	article.setWriter(rs.getString("writer"));
			    	article.setEmail(rs.getString("email"));
			    	article.setSubject(rs.getString("subject"));
			    	article.setPasswd(rs.getString("passwd"));
			    	article.setReg_date(rs.getTimestamp("reg_date"));//오늘날짜
			    	//정수값(조회수,답변에 대한 필드)
			    	article.setReadcount(rs.getInt("readcount"));//default->0
			    	article.setRef(rs.getInt("ref"));//그룹번호
			    	article.setRe_step(rs.getInt("re_step"));//답변글의 순서
			    	article.setRe_level(rs.getInt("re_level"));//들여쓰기
			    	article.setContent(rs.getString("content"));//글내용
			    	article.setIp(rs.getString("ip"));//글쓴이의 ip주소
			    	*/
			}
		}catch(Exception e) {
			System.out.println("getArticle() 메서드 오류발생=>"+e);
		}finally {
			pool.freeConnection(con,pstmt,rs);
		}
		return article;
	}
	//--중복된 레코드 한개를 담을 수 있는 메서드를 따로 만들어서 호출하자.->공개X
	//DB연동에 관련된 것은 반드시 예외처리해야 된다.
	private BoardDTO makeArticleFromResult() throws Exception {
		BoardDTO article=new BoardDTO();
    	article.setNum(rs.getInt("num"));
    	article.setWriter(rs.getString("writer"));
    	article.setEmail(rs.getString("email"));
    	article.setSubject(rs.getString("subject"));
    	article.setPasswd(rs.getString("passwd"));
    	article.setReg_date(rs.getTimestamp("reg_date"));//오늘날짜
    	//정수값(조회수,답변에 대한 필드)
    	article.setReadcount(rs.getInt("readcount"));//default->0
    	article.setRef(rs.getInt("ref"));//그룹번호
    	article.setRe_step(rs.getInt("re_step"));//답변글의 순서
    	article.setRe_level(rs.getInt("re_level"));//들여쓰기
    	article.setContent(rs.getString("content"));//글내용
    	article.setIp(rs.getString("ip"));//글쓴이의 ip주소
    	return article;
	}
	
	//5.글수정하기위한 메서드호출
	//select * from board where num=?->조회수를 증가X
	public BoardDTO updateGetArticle(int num) { //updateForm.jsp에서 호출
		BoardDTO article=null;
		
		try {
			con=pool.getConnection();
			sql="select * from board where num=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs=pstmt.executeQuery();
			//찾은 데이터가 있으면 담기
			if(rs.next()) {
				article=makeArticleFromResult();//중복코딩을 배제
			}
		}catch(Exception e) {
			System.out.println("updateGetArticle()메서드 오류=>"+e);
		}finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return article;
	}
	
	//6.글수정하는 메서드호출->insertArticle와 거의 동일->1)SQL 2)암호를 인증
	public int updateArticle(BoardDTO article) {  //updatePro.jsp에서 호출(암호입력)
		String dbpasswd = null;// db에서 찾은 암호를 저장
		int x = -1;// 게시물의 수정성공유무

		// 1.암호먼저 찾아서 비교->승낙->2)update 수행이 가능
		try {
			con = pool.getConnection();
			sql = "select passwd from board where num=?";// 최대값+1=>실제 저장할 게시물번호를 생성
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, article.getNum());
			rs = pstmt.executeQuery();
			if (rs.next()) {// 현재 테이블에서 데이터가 존재한다면
				dbpasswd = rs.getString("passwd");
				System.out.println("dbpasswd=>" + dbpasswd);// 암호확인용

				// db상의 암호==웹상에 입력한 암호가 맞는지 확인
				if (dbpasswd.contentEquals(article.getPasswd())) {

					sql = "update board set writer=?,email=?,subject=?,passwd=?,";
					sql += " content=?  where num=?";
					// sql+=" ref,re_step,re_level,content,ip)values(?,?,?,?,now(),?,?,?,?,?)";
					pstmt = con.prepareStatement(sql);// sysdate
					pstmt.setString(1, article.getWriter());// 웹에 입력저장->Setter Method호출
					pstmt.setString(2, article.getEmail());
					pstmt.setString(3, article.getSubject());
					pstmt.setString(4, article.getPasswd());
					pstmt.setString(5, article.getContent());// 글내용
					pstmt.setInt(6, article.getNum());

					int update = pstmt.executeUpdate();
					System.out.println("게시판의 글수정 성공유무(update)=>" + update);// 1성공, 0실패
					x = 1;
				} else {
					x = 0;// 암호가 틀린경우(수정 실패)
				}
			}//if(rs.next()) 찾는 데이터가 있다면
		} catch (Exception e) {
			System.out.println("updateArticle 메서드오류발생=>" + e);
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return x;//1 or 0 or -1
	}
	
	//7.글삭제하는 메서드 호출
	//select passwd from board where num=?
	//delete from board where num=?
	public int deleteArticle(int num,String passwd) { //게시물번호 hidden객체로 전달받는다
		
		String dbpasswd = null;// db에서 찾은 암호를 저장
		int x = -1;// 게시물의 삭제성공유무

		// 1.암호먼저 찾아서 비교->승낙->2)update 수행이 가능
		try {
			con = pool.getConnection();
			sql = "select passwd from board where num=?";// 최대값+1=>실제 저장할 게시물번호를 생성
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			if (rs.next()) {// 현재 테이블에서 데이터가 존재한다면
				dbpasswd = rs.getString("passwd");
				System.out.println("dbpasswd=>" + dbpasswd);// 암호확인용

				// db상의 암호==웹상에 입력한 암호가 맞는지 확인
				if (dbpasswd.contentEquals(passwd)) {

					sql = "delete from board  where num=?";
					pstmt = con.prepareStatement(sql);// sysdate
					pstmt.setInt(1,num);
					int delete = pstmt.executeUpdate();
					System.out.println("게시판의 글삭제 성공유무(delete)=>" + delete);// 1성공, 0실패
					x = 1;//글삭제 성공
				} else {
					x = 0;// 암호가 틀린경우(삭제 실패)
				}
			}//if(rs.next()) 찾는 데이터가 있다면
		} catch (Exception e) {
			System.out.println("deleteArticle 메서드 오류발생=>" + e);
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return x;
	}
}
