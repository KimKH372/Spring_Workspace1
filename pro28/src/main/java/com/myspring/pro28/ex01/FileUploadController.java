package com.myspring.pro28.ex01;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class FileUploadController  {
	// 임시 저장할 업로드 폴더 경로
	private static final String CURR_IMAGE_REPO_PATH = "c:\\spring\\image_repo";
	// 여기서 업로드 폼 페이지 부분을 받는 역할
	@RequestMapping(value="/form")
	public String form() {
		// WEB-INF/views/uploadForm.jsp
		// 해당 뷰 파일로 이동함.
	    return "uploadForm";
	  }
	
	// 업로드 된 이미지 파일을 처리하는 역할
	@RequestMapping(value="/upload",method = RequestMethod.POST)
	public ModelAndView upload(MultipartHttpServletRequest multipartRequest,HttpServletResponse response)
	  throws Exception{
		multipartRequest.setCharacterEncoding("utf-8");
		// 파일 이미지가 여러 개 생성되므로, 여러 객체를 담을 컬렉션을 생성
		// 맵으로 : 키값 형식
		Map map = new HashMap();
		// Enumeration 반복 작업을 하기 위한 그룹
		// 해당 내용은 파일 이미지가 아니고, 일반 데이터 형식
		Enumeration enu=multipartRequest.getParameterNames();
		// hasMoreElements() : 해당 enu 객체에 등록된 이미지들의 객체에 하나 씩 접근
		while(enu.hasMoreElements()){
			// nextElement() : name, id에 접근
			String name=(String)enu.nextElement();
			// name, id에 해당하는 실제 값을 가져 옵니다.
			String value=multipartRequest.getParameter(name);
			System.out.println(name+", "+value);
			// map 컬렉션에 객체들을 하나 씩 등록
			map.put(name,value);
		}
		// 일반 데이터 : 입력된 아이디 및 이름. 
		// 윗 부분 메모리상에 임시로 해당 이미지를 가지고 있다.
		//=============================================
		
		// 파일의 이미지들을 업로드 할 경로에 대해서 실제 물리파일을 작성하는 파일
		// 부모 폴더가 없다면 만들어주기
		// fileList에 실제 파일의 이름이 담겨 있음
		List fileList= fileProcess(multipartRequest);
		
		// 컬렉션 등록함. 키 이름 : fileList, 값 이름 : fileList
		map.put("fileList", fileList);
		ModelAndView mav = new ModelAndView();
		// 모델 앤 뷰에 데이터와, 뷰를 등록하고
		// 해당 뷰로 전달합니다.
		mav.addObject("map", map);
		mav.setViewName("result");
		return mav;
	}
	
	private List<String> fileProcess(MultipartHttpServletRequest multipartRequest) throws Exception{
		// multipartRequest에 담겨진 파일 이미지들이 있음
		// fileList : 파일 이미지들의 이름들을 담을 컬렉션
		List<String> fileList= new ArrayList<String>();
		// multipartRequest의 해당 이미지들의 키 값 들을
		// Iterator 반복 작업을 하기 위한 그룹
		Iterator<String> fileNames = multipartRequest.getFileNames();
		while(fileNames.hasNext()){
			String fileName = fileNames.next();
			MultipartFile mFile = multipartRequest.getFile(fileName);
			String originalFileName=mFile.getOriginalFilename();
			
			// 실제 파일 이름들을 해당 리스트에 하나 씩 등록
			fileList.add(originalFileName);
			
			// 실제 업로드 되는 저장 장소에 해당 이름으로 등록하는 객체
			File file = new File(CURR_IMAGE_REPO_PATH +"\\"+ fileName);
			if(mFile.getSize()!=0){ //File Null Check
				//해당 파일의 경로가 존재하지 않는다면
				//CURR_IMAGE_REPO_PATH = "c:\\spring\\image_repo";
				if(! file.exists()){ //��λ� ������ �������� ���� ���
					// 해당 부모 폴더를 만들었다면 
					//CURR_IMAGE_REPO_PATH = "c:\\spring\\image_repo";
					if(file.getParentFile().mkdirs()){ //��ο� �ش��ϴ� ���丮���� ����
						// 해당 파일 객체를 새로 생성하겠다
						file.createNewFile(); //���� ���� ����
					}
				}
				// 
				mFile.transferTo(new File(CURR_IMAGE_REPO_PATH +"\\"+ originalFileName)); //�ӽ÷� ����� multipartFile�� ���� ���Ϸ� ����
			}
		}
		return fileList;
	}
}
