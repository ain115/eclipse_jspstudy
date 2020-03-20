package action;
/*
 * 기능은 다르지만 요청을 받아서 처리해주는 메서드가 필요->공통의 메서드로 작성하기위해서
 *                                                                      인터페이스를 만들어야 된다.
 */
import javax.servlet.http.*;//request,response필요

public interface CommandAction {
  //이동할 페이지의 경로와 페이지명이 필요(요청에 따른)->반환값이 String->ModelAndView(스프링)
 public String requestPro(HttpServletRequest request,HttpServletResponse response)
                                 throws Throwable;
}
