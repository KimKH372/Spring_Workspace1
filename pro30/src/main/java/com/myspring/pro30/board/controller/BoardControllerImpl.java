 package com.myspring.pro30.board.controller;

import java.io.File;
import java.util.ArrayList;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.myspring.pro30.board.service.BoardService;
import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.board.vo.ImageVO;
import com.myspring.pro30.member.vo.MemberVO;


@Controller("boardController")
public class BoardControllerImpl  implements BoardController{
	private static final String ARTICLE_IMAGE_REPO = "C:\\board\\article_image";
	@Autowired
	BoardService boardService;
	@Autowired
	ArticleVO articleVO;
	
	@Override
	@RequestMapping(value= "/board/listArticles.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView listArticles(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String)request.getAttribute("viewName");
		List articlesList = boardService.listArticles();
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("articlesList", articlesList);
		return mav;
		
	}
	/*
	 //한 개 이미지 글쓰기
	@Override
	@RequestMapping(value="/board/addNewArticle.do" ,method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity addNewArticle(MultipartHttpServletRequest multipartRequest, 
	HttpServletResponse response) throws Exception {
		multipartRequest.setCharacterEncoding("utf-8");
		Map<String,Object> articleMap = new HashMap<String, Object>();
		Enumeration enu=multipartRequest.getParameterNames();
		while(enu.hasMoreElements()){
			String name=(String)enu.nextElement();
			String value=multipartRequest.getParameter(name);
			articleMap.put(name,value);
		}
		
		String imageFileName= upload(multipartRequest);
		HttpSession session = multipartRequest.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("member");
		String id = memberVO.getId();
		articleMap.put("parentNO", 0);
		articleMap.put("id", id);
		articleMap.put("imageFileName", imageFileName);
		
		String message;
		ResponseEntity resEnt=null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		try {
			int articleNO = boardService.addNewArticle(articleMap);
			if(imageFileName!=null && imageFileName.length()!=0) {
				File srcFile = new 
				File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
				File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
				FileUtils.moveFileToDirectory(srcFile, destDir,true);
			}
	
			message = "<script>";
			message += " alert('새글을 추가했습니다.');";
			message += " location.href='"+multipartRequest.getContextPath()+"/board/listArticles.do'; ";
			message +=" </script>";
		    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		}catch(Exception e) {
			File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
			srcFile.delete();
			
			message = " <script>";
			message +=" alert('오류가 발생했습니다. 다시 시도해 주세요');');";
			message +=" location.href='"+multipartRequest.getContextPath()+"/board/articleForm.do'; ";
			message +=" </script>";
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			e.printStackTrace();
		}
		return resEnt;
	}
	
	
	//한개의 이미지 보여주기
	@RequestMapping(value="/board/viewArticle.do" ,method = RequestMethod.GET)
	public ModelAndView viewArticle(@RequestParam("articleNO") int articleNO,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception{
		String viewName = (String)request.getAttribute("viewName");
		articleVO=boardService.viewArticle(articleNO);
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		mav.addObject("article", articleVO);
		return mav;
	}
	  */

	//다중 이미지 보여주기
	//해당 URL주소 뒤에 정보 부분 참고 사항.
		// viewArticle.do?articleNO=1
	@RequestMapping(value="/board/viewArticle.do" ,method = RequestMethod.GET)
	public ModelAndView viewArticle(@RequestParam("articleNO") int articleNO,
			  HttpServletRequest request, HttpServletResponse response) throws Exception{
		// 해당 뷰는 잘 가지고 와서 괜찮음.
		String viewName = (String)request.getAttribute("viewName");
		//viewArticle 뭘까? 현재 위치 1번 -> 2번 갈 예정. 
		
		//articleMap 에 뭐가 담겨 있나?
		// 해당 게시글 번호 articleNO = 1 에 대한
		// 일반 글과 파일 이미지들이 다 조회 후 리턴 되었음. 
		Map articleMap=boardService.viewArticle(articleNO);
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		
		//일반글의 내용을 조회한 결과
		//articleMap.put("article", articleVO);
		// 파일 이미지들의 내용을 조회한 결과
		//articleMap.put("imageFileList", imageFileList);
		// 키 형식의 이름이 알아야 사용할수 있다. 
		// 이제 뷰로 가자. viewArticle.jsp
		mav.addObject("articleMap", articleMap);
		return mav;
	}
 
	

	/*
  //한 개 이미지 수정 기능
  @RequestMapping(value="/board/modArticle.do" ,method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity modArticle(MultipartHttpServletRequest multipartRequest,  
    HttpServletResponse response) throws Exception{
    multipartRequest.setCharacterEncoding("utf-8");
	Map<String,Object> articleMap = new HashMap<String, Object>();
	Enumeration enu=multipartRequest.getParameterNames();
	while(enu.hasMoreElements()){
		String name=(String)enu.nextElement();
		String value=multipartRequest.getParameter(name);
		articleMap.put(name,value);
	}
	
	String imageFileName= upload(multipartRequest);
	HttpSession session = multipartRequest.getSession();
	MemberVO memberVO = (MemberVO) session.getAttribute("member");
	String id = memberVO.getId();
	articleMap.put("id", id);
	articleMap.put("imageFileName", imageFileName);
	
	String articleNO=(String)articleMap.get("articleNO");
	String message;
	ResponseEntity resEnt=null;
	HttpHeaders responseHeaders = new HttpHeaders();
	responseHeaders.add("Content-Type", "text/html; charset=utf-8");
    try {
       boardService.modArticle(articleMap);
       if(imageFileName!=null && imageFileName.length()!=0) {
         File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
         File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
         FileUtils.moveFileToDirectory(srcFile, destDir, true);
         
         String originalFileName = (String)articleMap.get("originalFileName");
         File oldFile = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO+"\\"+originalFileName);
         oldFile.delete();
       }	
       message = "<script>";
	   message += " alert('글을 수정했습니다.');";
	   message += " location.href='"+multipartRequest.getContextPath()+"/board/viewArticle.do?articleNO="+articleNO+"';";
	   message +=" </script>";
       resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
    }catch(Exception e) {
      File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
      srcFile.delete();
      message = "<script>";
	  message += " alert('오류가 발생했습니다.다시 수정해주세요');";
	  message += " location.href='"+multipartRequest.getContextPath()+"/board/viewArticle.do?articleNO="+articleNO+"';";
	  message +=" </script>";
      resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
    }
    return resEnt;
  }*/
  
  @Override
  @RequestMapping(value="/board/removeArticle.do" ,method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity  removeArticle(@RequestParam("articleNO") int articleNO,
                              HttpServletRequest request, HttpServletResponse response) throws Exception{
	response.setContentType("text/html; charset=UTF-8");
	String message;
	ResponseEntity resEnt=null;
	HttpHeaders responseHeaders = new HttpHeaders();
	responseHeaders.add("Content-Type", "text/html; charset=utf-8");
	try {
		boardService.removeArticle(articleNO);
		File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
		FileUtils.deleteDirectory(destDir);
		
		message = "<script>";
		message += " alert('글을 삭제했습니다.');";
		message += " location.href='"+request.getContextPath()+"/board/listArticles.do';";
		message +=" </script>";
	    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
	       
	}catch(Exception e) {
		message = "<script>";
		message += " alert('작업중 오류가 발생했습니다.다시 시도해 주세요.');";
		message += " location.href='"+request.getContextPath()+"/board/listArticles.do';";
		message +=" </script>";
	    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
	    e.printStackTrace();
	}
	return resEnt;
  }  
  

  //다중 이미지 글 추가하기
  @Override
  @RequestMapping(value="/board/addNewArticle.do" ,method = RequestMethod.POST)
//응답을 잭슨에 의해서 JSON 타입으로 반환을 합니다. 
 // ResponseEntity 전달시 메세지, 해당 HTTP 상태 코드 같이 전달.
  
  @ResponseBody
  public ResponseEntity  addNewArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response) throws Exception {
	multipartRequest.setCharacterEncoding("utf-8");
	String imageFileName=null;
	
	// articleMap 에 1차로 일반 게시글 정보 담기. 
	Map articleMap = new HashMap();
	// 일반 데이터를 처리하는 부분, 글의 제목, 본문의 내용부분
	Enumeration enu=multipartRequest.getParameterNames();
	while(enu.hasMoreElements()){
		String name=(String)enu.nextElement();
		String value=multipartRequest.getParameter(name);
		articleMap.put(name,value);
	}
	
	//로그인 시 세션에 저장된 회원 정보에서 글쓴이 아이디를 얻어와서 Map에 저장합니다.
	HttpSession session = multipartRequest.getSession();
	// 로그인 되었을 때, 해당 세션에 해당 멤버의 정보를 담은 객체를 저장. 
		// 로그인 후 세션에 저장된, 회원 정보를 가져오는 작업.  
	
	MemberVO memberVO = (MemberVO) session.getAttribute("member");
	// 로그인 한 아이디를 조회하기.
	String id = memberVO.getId();
	// 해당 아이디도 articleMap 컬렉션에 담기.
	articleMap.put("id",id);
	
	// upload 메서드 기능 살펴보기. 
		// 메모리 상에 파일 객체를 , 실제 물리 파일로 생성하고, 
		// 해당 파일 이미지들의 원본 파일 이름을 포함한 리스트를 반환함. 
		// 파일 이미지 예) bread.jpg 확장자 포함해서. 
	List<String> fileList;
	//imageFileList : 사용자가 업로드한 파일 이미지들을 임시로 담을 리스트.
	
	List<ImageVO> imageFileList = new ArrayList<ImageVO>();
	System.out.println("multipartRequest.getFileNames() 의 뭐가있나? " + multipartRequest.getFileNames());

	
	//upload 메서드 안에서 어차피 파일 관련 속성이 없어서 리턴을 null 한다. 
		// 하지만, upload 가 호출 되기전에 분기문으로 유효성 체크를 하고 싶은게 나의 의도.
		// 일단 시간상 임시 방편으로 upload 메서드 일단 찍고 진행 하는 걸로 합니다. 
		// 죄송합니다. 
		fileList =upload(multipartRequest);
		// temp 폴더 아래에 임시로 파일을 저장한 상태
		// 여기까지는 파일이름이 한글이 포함한 부분에서 깨지지 않음.
		
		
		//imageFileList : 사용자가 업로드한 파일 이미지들을 임시로 담을 리스트.
//			 imageFileList = new ArrayList<ImageVO>();
			
			// 파일의 이미지가 존재한다면, 아래의 알고리즘을 진행함. 
			if(fileList!= null && fileList.size()!=0) {
				
				for(String fileName : fileList) {
					
					//ImageVO 살펴보기. 사실은 하나의 이미지의 정보를 담을 임시 객체. DTO
					// 파일 이미지 테이블의 컬럼과 필드과 동일 할 것임. 
					ImageVO imageVO = new ImageVO();
					// 임시 객체에 fileList 에서 꺼내온 하나의 파일의 이름을 임시 객체에 저장하기.
					
					

					System.out.println("setImageFileName 하기 전의  fileName 의 값" + fileName);
					
					// 한글 깨지는 부분이
					// this.imageFileName = URLEncoder.encode(imageFileName,"UTF-8");
					imageVO.setImageFileName(fileName);
					System.out.println("업로드 후의 반복문 안의 fileName 의 값" + fileName);
					
					System.out.println("업로드 후의 반복문 안의 fileName 의 imageVO의 get이용해서 이름 값" + imageVO.getImageFileName());
					// 하나의 이미지 객체를 또 담을 리스트에 추가합니다. 
					imageFileList.add(imageVO);
				}
				articleMap.put("imageFileList", imageFileList);
			}
		// 여기까지도 한글 이름이 포함된 파일 이미지가 깨지지 않음. 
		
//			List<ImageVO> imageFileList3 = (ArrayList)articleMap.get("imageFileList");
//			for(ImageVO imageVO : imageFileList3){
//				System.out.println("BoardController 에서 해당 파일명 깨지는지 확인 중 try 들어가기 전 : "+ imageVO.getImageFileName());
//			}
			
			
			String message;
			ResponseEntity resEnt=null;
			HttpHeaders responseHeaders = new HttpHeaders();
		    responseHeaders.add("Content-Type", "text/html; charset=utf-8");
			try {
				//articleMap 에 담겨 있는 내용이 뭐냐?
				//1 articleMap.put(name,value); 일반글
				//2 articleMap.put("id",id); 작성자의 아이디 
				//3 articleMap.put("imageFileList", imageFileList); 업로드할 파일 이미지들
				// 현재 위치 1번 -> 2번에게 요청. boardService
				System.out.println("boardService.addNewArticle(articleMap); 호출 전");
//				List<ImageVO> imageFileList2 = (ArrayList)articleMap.get("imageFileList");
//				for(ImageVO imageVO : imageFileList2){
//					System.out.println("BoardController 에서 해당 파일명 깨지는지 확인 중 반복문 안 : "+ imageVO.getImageFileName());
//				}
				int articleNO;
				if(fileList!= null && fileList.size()!=0) {
				articleNO = boardService.addNewArticleWithImage(articleMap);
				} else {
				articleNO = boardService.addNewArticleWithoutImage(articleMap);
				}
				System.out.println("boardService.addNewArticle(articleMap); 호출 후");
//				System.out.println("imageFileList의 값: ?"+imageFileList);
//				System.out.println("imageFileList의 get(0) 값: ?"+imageFileList.get(0).toString());
//				System.out.println("imageFileList의 isEmpty: ?"+imageFileList.isEmpty());
				
				//글쓰기 시, 이미지가 없을 경우. 
				// 일반 글만 작성하는데, multipartRequest 에 타입 형식이 파일인 경우가 포함이 안되는 데.
				// imageFileList 안에 imageVO (null) 값이 들어가 있음. 
				// 값이 있는 것처럼 보임. !imageFileList.isEmpty() && imageFileList!=null && imageFileList.size()!=0
				
				if(!imageFileList.isEmpty() && imageFileList!=null && imageFileList.size()!=0) {
					System.out.println("if(!imageFileList.isEmpty() && imageFileList!=null && imageFileList.size()!=0) {");
					for(ImageVO  imageVO:imageFileList) {
						imageFileName = imageVO.getImageFileName();
//						if(imageFileName != null) {
						System.out.println("imageFileName의 :"+imageFileName);
						File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
						System.out.println("srcFile 있나? "+srcFile);
						File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
						System.out.println("destDir 있나? "+destDir);
						//destDir.mkdirs();
						
						FileUtils.moveFileToDirectory(srcFile, destDir,true);
//						}
						
					}
				}
				    
				message = "<script>";
				message += " alert('새글을 추가했습니다.');";
				message += " location.href='"+multipartRequest.getContextPath()+"/board/listArticles.do'; ";
				message +=" </script>";
			    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			    
				 
			}catch(Exception e) {
				if(imageFileList!=null && imageFileList.size()!=0) {
				  for(ImageVO  imageVO:imageFileList) {
				  	imageFileName = imageVO.getImageFileName();
					File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
				 	srcFile.delete();
				  }
				}

				
				message = " <script>";
				message +=" alert('오류가 발생했습니다. 다시 시도해 주세요');');";
				message +=" location.href='"+multipartRequest.getContextPath()+"/board/articleForm.do'; ";
				message +=" </script>";
				resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
				e.printStackTrace();
			}
			return resEnt;
		  }
			


			

			@RequestMapping(value = "/board/*Form.do", method =  RequestMethod.GET)
			private ModelAndView form(HttpServletRequest request, HttpServletResponse response) throws Exception {
				String viewName = (String)request.getAttribute("viewName");
				ModelAndView mav = new ModelAndView();
				mav.setViewName(viewName);
				return mav;
			}

			/*
			//한개 이미지 업로드하기
			private String upload(MultipartHttpServletRequest multipartRequest) throws Exception{
				String imageFileName= null;
				Iterator<String> fileNames = multipartRequest.getFileNames();
				
				while(fileNames.hasNext()){
					String fileName = fileNames.next();
					MultipartFile mFile = multipartRequest.getFile(fileName);
					imageFileName=mFile.getOriginalFilename();
					File file = new File(ARTICLE_IMAGE_REPO +"\\"+ fileName);
					if(mFile.getSize()!=0){ //File Null Check
						if(! file.exists()){ //경로상에 파일이 존재하지 않을 경우
							if(file.getParentFile().mkdirs()){ //경로에 해당하는 디렉토리들을 생성
									file.createNewFile(); //이후 파일 생성
							}
						}
						mFile.transferTo(new File(ARTICLE_IMAGE_REPO +"\\"+"temp"+ "\\"+imageFileName)); //임시로 저장된 multipartFile을 실제 파일로 전송
					}
				}
				return imageFileName;
			}*/
			
			
			//다중 이미지 업로드하기
			private List<String> upload(MultipartHttpServletRequest multipartRequest) throws Exception{
				// 이미지 파일들을 담을 컬렉션 생성. 
				List<String> fileList= new ArrayList<String>();
				// 해당 file1, file2 해당 파일 속성의 이름을 조회하기. 
				Iterator<String> fileNames = multipartRequest.getFileNames();
				
				while(fileNames.hasNext()){
					// fileName -> name = file1, file2 ....
					String fileName = fileNames.next();
					System.out.println("upload 의 fileName = " + fileName);
					//name = file1 의 속성이라고 하면 해당 정보를 담은 객체 : mFile에 할당. 
					MultipartFile mFile = multipartRequest.getFile(fileName);
					// mFile에서 해당 원본의 파일의 이름을 가져오기. 
					String originalFileName=mFile.getOriginalFilename();
					System.out.println("upload 의originalFileName = " + originalFileName);
					// originalFileName : ex) bread.jpg
					// 이미지 파일들을 해당 리스트에 담기. 
					fileList.add(originalFileName);
					// "C:\\board\\article_image" 아래 경로에 파일 객체를 메모리 상에 만들기. 
					File file = new File(ARTICLE_IMAGE_REPO +"\\"+ fileName);
					
					// mFile 의미는 이미지가 있다면. 
					if(mFile != null && mFile.getSize()!=0){ //File Null Check
						
						if(! file.exists()){ //경로상에 파일이 존재하지 않을 경우
							// 해당 부모 경로가 없을 때 최초에 한번 정도 실행이 되는 부분이라서 참고만 하기. 
							if(file.getParentFile().mkdirs()){ //경로에 해당하는 디렉토리들을 생성
									file.createNewFile(); //이후 파일 생성
									// 임시로 최초 경로를 만들 때 빈 파일을 예를 들어 생성.
							}
						}
						// 메모리에 생성 된 파일 객체들을 실제 물리 파일 위치로 이동 해야 함. 
						// 문제가 뭐냐면, 상위 부모 폴더를 생성을 어디까지 해주냐? 
						// "C:\\board\\article_image" 
						// 하위에 temp 라는 임시 폴더를 만들었음. 
						// 참고 
						// 지금은 로컬에 임시 파일 서버를 같이 이용하지만, 나중에 다 따로 분리 작업을 함. 
						// 이미지 서버를 만듦 원격지 ( 예) aws , 해당 서버를 만들어서, 파일 서버로 이용함. 
						mFile.transferTo(new File(ARTICLE_IMAGE_REPO +"\\"+"temp"+ "\\"+originalFileName)); //임시로 저장된 multipartFile을 실제 파일로 전송
					}
				}
				return fileList;
			}
			
		}