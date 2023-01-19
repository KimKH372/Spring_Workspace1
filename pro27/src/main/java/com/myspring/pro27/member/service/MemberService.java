package com.myspring.pro27.member.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.myspring.pro27.member.vo.MemberVO;

//현위치 2-1
public interface MemberService {
	 // 현재 위치 2번.
	 // updateMember 기능 추가. 
	 public int updateMember(MemberVO membeVO) throws DataAccessException;
	 
	 // addMember 이용해서, 파라미터에는 해당 회원의 아이디를 받아서.
	 // 리턴의 한명의 회원정보들을 담을 MemberVO 형으로 받도록 합니다. 
	 public MemberVO getMember(String id) throws DataAccessException;
	 
	 
	 public List listMembers() throws DataAccessException;
	 public int addMember(MemberVO memberVO) throws DataAccessException;
	 public int removeMember(String id) throws DataAccessException;
	 
	 
	 public MemberVO login(MemberVO memberVO) throws Exception;
}