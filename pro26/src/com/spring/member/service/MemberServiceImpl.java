package com.spring.member.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.spring.member.dao.MemberDAO;
import com.spring.member.vo.MemberVO;
//현 위치 2-2

//@Service  : 컴포넌트 스캔에서 해당 패키지명을 등록해서, 이 애너테이션을보면.
//컴파일러가 이 클래스는 기능이 Service 역할을 하는구나. 
@Service("memberService")

//단위기능 1, 단위기능 2, 하나의 논리적인 구조 묶어서
//all, nothing 
//2개의 단위기능이 다 정상 동작 해야지 마지막에 커밋함. 
@Transactional(propagation = Propagation.REQUIRED)
public class MemberServiceImpl implements MemberService {
//  @Autowired -> DI, xml 파일에서 해당 빈 객체에 ref 속성으로 주입했음. 
	// 방식은 , 생성자 또는 세터 형식으로 주입했음. 
	// 이제는 	@Autowired 한줄로 대체가능. 
	@Autowired
	private MemberDAO memberDAO;
	
	//회원 가입 하는 부분에서 샘플 코드 이용.
	   // return memberDAO.insertMember(memberVO);
	   // 현재 위치는 2번. 디비에 직접 접근하는 기능이 있는 3번에게 도움을 요청. 
	@Override
	public MemberVO getMember(String id) throws DataAccessException {

		return memberDAO.getMember(id);
	}

	// 현재 위치 2번.
	// 작업을 memberDAO 3번에게 요청함. 
	// addMember 코드를 참고해서 작성함. 
	@Override
	public int updateMember(MemberVO membeVO) throws DataAccessException {
		return memberDAO.updateMember(membeVO);
	}

	@Override
	public List listMembers() throws DataAccessException {
		List membersList = null;
		membersList = memberDAO.selectAllMemberList();
		return membersList;
	}

	@Override
	public int addMember(MemberVO member) throws DataAccessException {
		return memberDAO.insertMember(member);
	}

	@Override
	public int removeMember(String id) throws DataAccessException {
		return memberDAO.deleteMember(id);
	}
}
