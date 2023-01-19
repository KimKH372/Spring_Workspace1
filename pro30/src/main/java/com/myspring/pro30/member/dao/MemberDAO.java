package com.myspring.pro30.member.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.myspring.pro30.member.vo.MemberVO;


public interface MemberDAO {
	 // 업데이트 멤버 기능 추가. 
	 public int updateMember(MemberVO memberVO) throws DataAccessException ;
	 
	 //getMember 라는 메서드 만들기. 
	 public MemberVO getMember(String id) throws DataAccessException ;
	
	 public List selectAllMemberList() throws DataAccessException;
	 public int insertMember(MemberVO memberVO) throws DataAccessException ;
	 public int deleteMember(String id) throws DataAccessException;
	 
	//27장꺼.
	 public MemberVO loginById(MemberVO memberVO) throws DataAccessException;

}
