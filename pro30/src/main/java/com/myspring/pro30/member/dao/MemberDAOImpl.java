package com.myspring.pro30.member.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.myspring.pro30.member.dao.MemberDAO;
import com.myspring.pro30.member.vo.MemberVO;


//현위치 3-2
//@Repository : 컴포넌트 스캔에서 해당 패키지명을 등록해서, 이 애너테이션을보면.
//컴파일러가 이 클래스는 기능이 Repository(DAO) 역할을 하는구나. 
@Repository("memberDAO")
public class MemberDAOImpl implements MemberDAO {
	
	//  @Autowired -> DI, xml 파일에서 해당 빈 객체에 ref 속성으로 주입했음. 
	// 방식은 , 생성자 또는 세터 형식으로 주입했음. 
	// 이제는 	@Autowired 한줄로 대체가능. 
	@Autowired
	private SqlSession sqlSession;

	
	// 기능 구현을 목록 가져오기 부분을 참고.
		// 한명의 회원의 정보를 받은 타입을 정하고. :  MemberVO memberVO
		// 실제 작업은 sqlSession.selectOne(id1, id2)
		// id1 -> 4번 위치에 있는 마이바티스에 등록된 sql 태그
		// id1 -> 이용하는 형식. 예) "mapper.member.deleteMember"
		
		// id2 -> 넘어온 값 id =1234  의미. 
		@Override
		public MemberVO getMember(String id) throws DataAccessException {
			MemberVO memberVO = (MemberVO) sqlSession.selectOne("mapper.member.selectMemberById", id);
			return memberVO;
		}

		// 넘어온 한명의 회원의 정보를 가지고, update 하기. 
		// member.xml에서 업데이트하는 태그가 있다면 그대로 사용하고, 
		// 만약, 없다면 태그를 만들어야함. 
		// 찾아보니, 아이디가 : updateMember 만들어져 있어서, 사용함.
		@Override
		public int updateMember(MemberVO memberVO) throws DataAccessException {
			int result = sqlSession.update("mapper.member.updateMember", memberVO);
			return result;
		}
	
	@Override
	public List selectAllMemberList() throws DataAccessException {
		List<MemberVO> membersList = null;
		
		//실제 디비에 crud 작업 member.xml에서 진행을 합니다. -> 4번
		// sql 문장을 따로 분리해서 관리 하고 있고, 필요한 sql를 아이디 형식으로 불러와 사용중.
	
		//
		membersList = sqlSession.selectList("mapper.member.selectAllMemberList");
		return membersList;
	}

	// 만약, 매개변수가 2번째 부분있다면, 이 부분 data를 전달하기위한 인자값.: memberVO
	@Override
	public int insertMember(MemberVO memberVO) throws DataAccessException {
		int result = sqlSession.insert("mapper.member.insertMember", memberVO);
		return result;
	}

	@Override
	public int deleteMember(String id) throws DataAccessException {
		int result = sqlSession.delete("mapper.member.deleteMember", id);
		return result;
	}
	
	// 현재 위치 3번, 실제 디비 작업을 하는 sqlSession에게 도움을 요청.
	// 실제 작업은 입력된 아이디와 패스워드 2가지 정보로 디비에 해당 유저가 있는지 조회하는 과정.
	// 있다며느 해당 회원 정보를 memberVO에 담아서, 역순으로 
	// sqlSession -> dao -> service -> controller 역순으로 전달
	@Override
	public MemberVO loginById(MemberVO memberVO) throws DataAccessException{
		  MemberVO vo = sqlSession.selectOne("mapper.member.loginById",memberVO);
		  // 역순으로 해당 디비에서 검색된 회원의 정보를 memberVO 객체에 담아서 전달함.
		  // MemberVO vo 참조형 변수 이름.
		return vo;
	}


	@Override
	public MemberVO loginById(MemberVO memberVO) throws DataAccessException{
		  MemberVO vo = sqlSession.selectOne("mapper.member.loginById",memberVO);
		return vo;
	}

}
