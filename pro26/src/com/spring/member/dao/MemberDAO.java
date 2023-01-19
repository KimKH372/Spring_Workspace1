package com.spring.member.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.spring.member.vo.MemberVO;

//현 위치 3-1
public interface MemberDAO {
	 // 업데이트 멤버 기능 추가. 
	 public int updateMember(MemberVO memberVO) throws DataAccessException ;

	 //getMember 라는 메서드 만들기. 
	 public MemberVO getMember(String id) throws DataAccessException;
	
	 public List selectAllMemberList() throws DataAccessException;
	 public int insertMember(MemberVO memberVO) throws DataAccessException ;
	 public int deleteMember(String id) throws DataAccessException;
	 

}
