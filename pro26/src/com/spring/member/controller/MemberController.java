package com.spring.member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.spring.member.vo.MemberVO;
//현 위치 1번
// 26장에서 데이터 바인딩 기법인 에너테이션을 이용해서, 바인딩 하는 형식이 변경됨.
public interface MemberController {
	public ModelAndView listMembers(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	//회원 수정 창에서 입력된 내용을 처리하는 업데이트 기능을 추가합니다. 참고는 회원가입 기능을 참고.
	public ModelAndView updateMember(@ModelAttribute("info") MemberVO memberVO,HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	// @ModelAttribute : MemberVO의 형으로 데이터를 받겠다.
	// info : 뷰에서 사용할 변수명
	public ModelAndView addMember(@ModelAttribute("info") MemberVO memberVO,HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	// @RequestParam -> request.getParameter("id); 대체용
	public ModelAndView removeMember(@RequestParam("id") String id, HttpServletRequest request, HttpServletResponse response) throws Exception;

}
