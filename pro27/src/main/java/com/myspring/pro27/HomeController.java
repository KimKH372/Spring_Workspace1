package com.myspring.pro27;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
/*@Controller
public class HomeController {
  private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
  *//**
		 * Simply selects the home view to render by returning its name.
		 *//*
			 * // 해당 프로젝트를 서버에 등록 // home.jsp 파일에서 ctrl + f11 누르면, 해당 프로젝트 서버에 사용할래? //
			 * value = "/"의 의미임 // http://localhost:8090/pro27
			 * 
			 * @RequestMapping(value = "/", method = RequestMethod.GET) public String
			 * home(Locale locale, Model model) {
			 * logger.info("Welcome home! The client locale is {}.", locale);
			 * 
			 * // 날짜를 해당 포맷 형태로 출력하는 샘플 코드 Date date = new Date(); DateFormat dateFormat =
			 * DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
			 * String formattedDate = dateFormat.format(date);
			 * 
			 * // 데이터만 전달하겠다 model.addAttribute("serverTime", formattedDate ); //
			 * /WEB-INF/views/home.jsp 의미. // 이름만 사용함. : home return "home"; } }
			 */

//�ٱ��� ��� �ڵ�

@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = "/main.do", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		return "main";
	}
}
