package com.spring.member.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.spring.member.service.MemberService;
import com.spring.member.vo.MemberVO;



//현 위치 1-1
// @Controller : 컴포넌트 스캔에서 해당 패키지명을 등록해서ㅡ 이 에너테이션을 보면,
// 컴파일러가 이 클래스는 기능이 Controller 역할을 하는구나

//action-servlet.xml 파일에서 
//해당 스캔 위치를 한번 더 확인함. 
//<context:component-scan base-package="com.spring.member"/>

@Controller("memberController")
public class MemberControllerImpl implements MemberController {
	// @Autowired -> DI, xml 파일에서 해당 빈 객체에 ref 속성으로 주입했음
	// 방식은, 생성자 또는 세터 형식으로 주입했음
	// 이제는 @Autowired 한 줄로 대체가능
	
	@Autowired
	private MemberService memberService;
	@Autowired
	MemberVO memberVO ;
	
	// 현 위치 1-1
	// @RequestMapping : value="/member/listMembers.do" 속성으로 오는 주소 부분에 대해서
	// 처리하는 역할. 지금은 메서드 부분에 표시 되어 있지만,
	// 클래스에 설정해서, 하위에 나눠서 작업도 가능함
	
	@Override
	@RequestMapping(value = "/member/listMembers.do" ,method = RequestMethod.GET)
	public ModelAndView listMembers(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = getViewName(request);
		List membersList = memberService.listMembers();
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("membersList", membersList);
		return mav;
	}

	//현 위치 1-1
	// @ModelAttribute("member") MemberVO member
	// member : 뷰에서 사용할 변수명.
	@Override
	@RequestMapping(value="/member/addMember.do" ,method = RequestMethod.POST)
	public ModelAndView addMember(@ModelAttribute("member") MemberVO member,
			                  HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		int result = 0;
		result = memberService.addMember(member);
		ModelAndView mav = new ModelAndView("redirect:/member/listMembers.do");
		return mav;
	}
	//현위치 1-1
		// @RequestParam -> request.getParameter("id"); 대체용.
	@Override
	@RequestMapping(value="/member/removeMember.do" ,method = RequestMethod.GET)
	public ModelAndView removeMember(@RequestParam("id") String id, 
		HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("utf-8");
		memberService.removeMember(id);
		ModelAndView mav = new ModelAndView("redirect:/member/listMembers.do");
		return mav;
	}
	
	// 현재, 회원가입 폼, 회원수정 폼 2개 있음
	// 회원가입은(새로 데이터를 받는 부분) : 데이터를 불러오는 게 사실상 말이 안되는 상황
	// 회원수정(기존의 회원 정보를 가져오는 부분)
	// 그래서, 매핑을 받는 부분에서, 하나의 폼으로 작업을 하면 불편함.
	// 분리작업함.
	/*@RequestMapping(value = { "/member/loginForm.do", "/member/memberForm.do" }, method =  RequestMethod.GET)*/
	@RequestMapping(value = "/member/memberForm.do", method =  RequestMethod.GET)
	public ModelAndView memberForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = getViewName(request);
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}
	
	// 업데이트 폼에서 할 작업 순서를 적어보기
	// 먼저, 한명의 회원을 식별하기 위한 아이디를 가져오는 작업 필요함.
	// @RequestParam("id") String id
	@RequestMapping(value = "/member/updateForm.do", method =  RequestMethod.GET)
	public ModelAndView updaeForm(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("id") String id) throws Exception {
		
		// 해당 아이디로 회원 정보를 담은 객체를 받을 변수 정하고,
		// 2번의 service에게 도움을 요청하기
		// 여기서 부터는 앞에 사용했던 코드를 재사용
		
		// 컨트롤러 1번 위치에서 -> 서비스로 요청(2번위치)
		// 예) id=1234 인 정보를 조회해서 가져와줘.
		// 한명의 회원의 정보이니까, 받을 때에도 , 한명의 타입으로 받으면 됩니다.
		// 목록 출력시 예제 코드 이용.
		// List membersList = memberService.listMembers();
		// 이용하는 방법은 memberService 객체에 들어 있는 메서드를 이용함.
		MemberVO memberVO = memberService.getMember(id);
		
		String viewName = getViewName(request);
		ModelAndView mav = new ModelAndView();
		
		//위에서 받아온 정보에 대해서 데이터를 설정하기. 
				mav.addObject(memberVO);
		mav.setViewName(viewName);
		return mav;
	}
	
	// addMember 의 기능을 복사해서 업데이트 기능으로 변경해서 사용 할 예정. 
			@Override
			@RequestMapping(value = "/member/updateMember.do", method = RequestMethod.POST)
			public ModelAndView updateMember(@ModelAttribute("info") MemberVO memberVO,HttpServletRequest request, HttpServletResponse response) throws Exception {
				request.setCharacterEncoding("utf-8");
				//매개 변수에서 MemberVO memberVO 해당 VO 클래스를 만들어서 필요 없음. 
//				MemberVO memberVO = new MemberVO();
				// 회원 수정 창에서 입력된 데이터가 전부 : MemberVO memberVO 다 들어 있음. 
				// 데이터 바인딩 기법. 

				// 회원 아이디는 읽기전용이라서 그대로 가져오고,
				// 나머지 정보는 변경된 내용을 가져오는 작업. 

				// 필요없는 부분 일단 학습 목적으로 주석 처리. 

//				String id=request.getParameter("id");
//				String pwd=request.getParameter("pwd");
//				String name=request.getParameter("name");
//				String email = request.getParameter("email");

				// 변경된 데이터를 옮기기 위해서, 임시 객체에 담는 작업. 
				// 필요없는 부분 일단 학습 목적으로 주석 처리. 
//				memberVO.setId(id);
//				memberVO.setPwd(pwd);
//				memberVO.setName(name);
//				memberVO.setEmail(email);

				// 실제 디비 작업 합니다. 
				// 1번 -> 2번 -> 3번 -> 4번 -> 5번
				// 한명 회원의 정보를 담은 객체를 전달하는 과정.
				// result 수정이 되었다면 1로 반환해서 확인하는 용도.
		// 현재 위치 1번
				int result = 0;
				// 업데이트 하는 기능은 memberService 2번에게 요청하기. 
				//회원 정보의 수정된 내용이 : memberVO 에 자동으로 할당이 된 상태. 
				result = memberService.updateMember(memberVO);
				if(result == 1) {
					String str = "회원 수정 성공 했음." ;
				} else {
					String str2 = "회원 수정 성공 안했음.";
				}
//				바인딩 작업은 뒤에서 설명 후 사용할 예정.
//				bind(request, memberVO);
				// 수정이 되었다면, 목록으로 가기. 
				ModelAndView mav = new ModelAndView("redirect:/member/listMembers.do");
				return mav;
			}	

	

	private String getViewName(HttpServletRequest request) throws Exception {
		String contextPath = request.getContextPath();
		String uri = (String) request.getAttribute("javax.servlet.include.request_uri");
		if (uri == null || uri.trim().equals("")) {
			uri = request.getRequestURI();
		}

		int begin = 0;
		if (!((contextPath == null) || ("".equals(contextPath)))) {
			begin = contextPath.length();
		}

		int end;
		if (uri.indexOf(";") != -1) {
			end = uri.indexOf(";");
		} else if (uri.indexOf("?") != -1) {
			end = uri.indexOf("?");
		} else {
			end = uri.length();
		}

		String viewName = uri.substring(begin, end);
		if (viewName.indexOf(".") != -1) {
			viewName = viewName.substring(0, viewName.lastIndexOf("."));
		}
		if (viewName.lastIndexOf("/") != -1) {
			viewName = viewName.substring(viewName.lastIndexOf("/"), viewName.length());
		}
		return viewName;
	}


}
