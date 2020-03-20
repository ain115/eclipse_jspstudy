package lys.board;

//寃뚯떆�뙋�쓽 �옉�꽦�궇吏�,�떆媛�->DB�뿉 愿��젴�맂 �겢�옒�뒪 �뵲濡� 議댁옱
import java.sql.Timestamp;  //import java.util.Date(�씪諛섏쟻�씤 �궇吏�)

//�쎒�긽�뿉�꽌 �뀒�씠釉붿쓽 �븘�뱶蹂꾨줈 ���옣,爰쇰궡�삱 紐⑹쟻�쑝濡� �궗�슜�븯�뒗 �겢�옒�뒪(硫붿꽌�뱶�쓽 留ㅺ컻蹂��닔,諛섑솚�삎�쓣 �씠�슜)
public class BoardDTO {

	private int num;//寃뚯떆臾쇰쾲�샇
	//1.�늿�뿉 蹂댁씠�뒗 �긽�깭(�엯�젰)
	private String writer;//�옉�꽦�옄
	private String subject;//湲��젣紐�
	private String email;//�씠硫붿씪
	private String content;//湲��궡�슜
	private String passwd;//�븫�샇(湲��벐湲�,�닔�젙�븷�븣->蹂몄씤�엫�쓣 �씤利앸븣臾몄뿉)
	//2.�늿�뿉 �븞蹂댁씠�뒗 寃쎌슦->吏곸젒 X
	private Timestamp reg_date;//�옉�꽦�궇吏�->sysdate<->now()(MySQL�뿉�꽌 �궗�슜�븯�뒗 �븿�닔)
	private int readcount;//議고쉶�닔->default->0�쓣 �엯�젰
	private String ip;//�옉�꽦�옄 ip二쇱냼瑜� 異쒕젰
	//怨듭��궗�빆,�옄�쑀寃뚯떆�뙋+3媛쒖쓽 �븘�뱶異붽�(�떟蹂��삎)
	private int ref;//湲� 洹몃９踰덊샇(=�떒�룆�쑝濡� �궗�슜�릺硫� 寃뚯떆臾� 踰덊샇�� �뿭�븷�씠 媛숇떎)
	private int re_step;//�떟蹂�湲��쓽 �닚�꽌瑜� 吏��젙(=媛숈� 洹몃９�씪�븣�쓽 �떟蹂�湲� �닚�꽌)
	private int re_level;//�떟蹂�湲��쓽 �떟蹂��뿉 ���븳 源딆씠(depth)
	//�옄猷뚯떎 異붽�
	//private String file;//�뾽濡쒕뱶�릺�뒗 �뙆�씪�쓽 �젙蹂댁텛媛�(�옄猷뚯떎)
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public Timestamp getReg_date() {
		return reg_date;
	}
	public void setReg_date(Timestamp reg_date) {
		this.reg_date = reg_date;
	}
	public int getReadcount() {
		return readcount;
	}
	public void setReadcount(int readcount) {
		this.readcount = readcount;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getRef() {
		return ref;
	}
	public void setRef(int ref) {
		this.ref = ref;
	}
	public int getRe_step() {
		return re_step;
	}
	public void setRe_step(int re_step) {
		this.re_step = re_step;
	}
	public int getRe_level() {
		return re_level;
	}
	public void setRe_level(int re_level) {
		this.re_level = re_level;
	}
}






