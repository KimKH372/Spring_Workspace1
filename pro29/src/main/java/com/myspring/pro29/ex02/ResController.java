package com.myspring.pro29.ex02;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

// @Controller는 모델과 뷰를 전달한다
//@RestController 데이터만 전달한다.
// 이유가 -> 하위에 메서드들은 모두 @ResponseBody의 영향을 받기 때문에
// 그래서, 테스트를 @Controller에서 특정 메서드에만
// @ResponseBody를 달아서 리턴을 데이터(JSON)으로 받는 부분 확인중

@Controller
public class ResController {
	@RequestMapping(value = "/res1")
	@ResponseBody
	public Map<String, Object> res1() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", "hong");
		map.put("name", "홍길동");
		return map;
	}
	
	
	@RequestMapping(value = "/res2")
	public ModelAndView res2() {
		return new ModelAndView("home");
	}
	
}
