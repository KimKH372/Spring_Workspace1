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
	 //??? ??? ????????? ?????????
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
			message += " alert('????????? ??????????????????.');";
			message += " location.href='"+multipartRequest.getContextPath()+"/board/listArticles.do'; ";
			message +=" </script>";
		    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		}catch(Exception e) {
			File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
			srcFile.delete();
			
			message = " <script>";
			message +=" alert('????????? ??????????????????. ?????? ????????? ?????????');');";
			message +=" location.href='"+multipartRequest.getContextPath()+"/board/articleForm.do'; ";
			message +=" </script>";
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			e.printStackTrace();
		}
		return resEnt;
	}
	
	
	//????????? ????????? ????????????
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

	//?????? ????????? ????????????
	//?????? URL?????? ?????? ?????? ?????? ?????? ??????.
		// viewArticle.do?articleNO=1
	@RequestMapping(value="/board/viewArticle.do" ,method = RequestMethod.GET)
	public ModelAndView viewArticle(@RequestParam("articleNO") int articleNO,
			  HttpServletRequest request, HttpServletResponse response) throws Exception{
		// ?????? ?????? ??? ????????? ?????? ?????????.
		String viewName = (String)request.getAttribute("viewName");
		//viewArticle ??????? ?????? ?????? 1??? -> 2??? ??? ??????. 
		
		//articleMap ??? ?????? ?????? ???????
		// ?????? ????????? ?????? articleNO = 1 ??? ??????
		// ?????? ?????? ?????? ??????????????? ??? ?????? ??? ?????? ?????????. 
		Map articleMap=boardService.viewArticle(articleNO);
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		
		//???????????? ????????? ????????? ??????
		//articleMap.put("article", articleVO);
		// ?????? ??????????????? ????????? ????????? ??????
		//articleMap.put("imageFileList", imageFileList);
		// ??? ????????? ????????? ????????? ???????????? ??????. 
		// ?????? ?????? ??????. viewArticle.jsp
		mav.addObject("articleMap", articleMap);
		return mav;
	}
 
	

	/*
  //??? ??? ????????? ?????? ??????
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
	   message += " alert('?????? ??????????????????.');";
	   message += " location.href='"+multipartRequest.getContextPath()+"/board/viewArticle.do?articleNO="+articleNO+"';";
	   message +=" </script>";
       resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
    }catch(Exception e) {
      File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
      srcFile.delete();
      message = "<script>";
	  message += " alert('????????? ??????????????????.?????? ??????????????????');";
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
		message += " alert('?????? ??????????????????.');";
		message += " location.href='"+request.getContextPath()+"/board/listArticles.do';";
		message +=" </script>";
	    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
	       
	}catch(Exception e) {
		message = "<script>";
		message += " alert('????????? ????????? ??????????????????.?????? ????????? ?????????.');";
		message += " location.href='"+request.getContextPath()+"/board/listArticles.do';";
		message +=" </script>";
	    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
	    e.printStackTrace();
	}
	return resEnt;
  }  
  

  //?????? ????????? ??? ????????????
  @Override
  @RequestMapping(value="/board/addNewArticle.do" ,method = RequestMethod.POST)
//????????? ????????? ????????? JSON ???????????? ????????? ?????????. 
 // ResponseEntity ????????? ?????????, ?????? HTTP ?????? ?????? ?????? ??????.
  
  @ResponseBody
  public ResponseEntity  addNewArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response) throws Exception {
	multipartRequest.setCharacterEncoding("utf-8");
	String imageFileName=null;
	
	// articleMap ??? 1?????? ?????? ????????? ?????? ??????. 
	Map articleMap = new HashMap();
	// ?????? ???????????? ???????????? ??????, ?????? ??????, ????????? ????????????
	Enumeration enu=multipartRequest.getParameterNames();
	while(enu.hasMoreElements()){
		String name=(String)enu.nextElement();
		String value=multipartRequest.getParameter(name);
		articleMap.put(name,value);
	}
	
	//????????? ??? ????????? ????????? ?????? ???????????? ????????? ???????????? ???????????? Map??? ???????????????.
	HttpSession session = multipartRequest.getSession();
	// ????????? ????????? ???, ?????? ????????? ?????? ????????? ????????? ?????? ????????? ??????. 
		// ????????? ??? ????????? ?????????, ?????? ????????? ???????????? ??????.  
	
	MemberVO memberVO = (MemberVO) session.getAttribute("member");
	// ????????? ??? ???????????? ????????????.
	String id = memberVO.getId();
	// ?????? ???????????? articleMap ???????????? ??????.
	articleMap.put("id",id);
	
	// upload ????????? ?????? ????????????. 
		// ????????? ?????? ?????? ????????? , ?????? ?????? ????????? ????????????, 
		// ?????? ?????? ??????????????? ?????? ?????? ????????? ????????? ???????????? ?????????. 
		// ?????? ????????? ???) bread.jpg ????????? ????????????. 
	List<String> fileList;
	//imageFileList : ???????????? ???????????? ?????? ??????????????? ????????? ?????? ?????????.
	
	List<ImageVO> imageFileList = new ArrayList<ImageVO>();
	System.out.println("multipartRequest.getFileNames() ??? ????????????? " + multipartRequest.getFileNames());

	
	//upload ????????? ????????? ????????? ?????? ?????? ????????? ????????? ????????? null ??????. 
		// ?????????, upload ??? ?????? ???????????? ??????????????? ????????? ????????? ?????? ????????? ?????? ??????.
		// ?????? ????????? ?????? ???????????? upload ????????? ?????? ?????? ?????? ?????? ?????? ?????????. 
		// ???????????????. 
		fileList =upload(multipartRequest);
		// temp ?????? ????????? ????????? ????????? ????????? ??????
		// ??????????????? ??????????????? ????????? ????????? ???????????? ????????? ??????.
		
		
		//imageFileList : ???????????? ???????????? ?????? ??????????????? ????????? ?????? ?????????.
//			 imageFileList = new ArrayList<ImageVO>();
			
			// ????????? ???????????? ???????????????, ????????? ??????????????? ?????????. 
			if(fileList!= null && fileList.size()!=0) {
				
				for(String fileName : fileList) {
					
					//ImageVO ????????????. ????????? ????????? ???????????? ????????? ?????? ?????? ??????. DTO
					// ?????? ????????? ???????????? ????????? ????????? ?????? ??? ??????. 
					ImageVO imageVO = new ImageVO();
					// ?????? ????????? fileList ?????? ????????? ????????? ????????? ????????? ?????? ????????? ????????????.
					
					

					System.out.println("setImageFileName ?????? ??????  fileName ??? ???" + fileName);
					
					// ?????? ????????? ?????????
					// this.imageFileName = URLEncoder.encode(imageFileName,"UTF-8");
					imageVO.setImageFileName(fileName);
					System.out.println("????????? ?????? ????????? ?????? fileName ??? ???" + fileName);
					
					System.out.println("????????? ?????? ????????? ?????? fileName ??? imageVO??? get???????????? ?????? ???" + imageVO.getImageFileName());
					// ????????? ????????? ????????? ??? ?????? ???????????? ???????????????. 
					imageFileList.add(imageVO);
				}
				articleMap.put("imageFileList", imageFileList);
			}
		// ??????????????? ?????? ????????? ????????? ?????? ???????????? ????????? ??????. 
		
//			List<ImageVO> imageFileList3 = (ArrayList)articleMap.get("imageFileList");
//			for(ImageVO imageVO : imageFileList3){
//				System.out.println("BoardController ?????? ?????? ????????? ???????????? ?????? ??? try ???????????? ??? : "+ imageVO.getImageFileName());
//			}
			
			
			String message;
			ResponseEntity resEnt=null;
			HttpHeaders responseHeaders = new HttpHeaders();
		    responseHeaders.add("Content-Type", "text/html; charset=utf-8");
			try {
				//articleMap ??? ?????? ?????? ????????? ???????
				//1 articleMap.put(name,value); ?????????
				//2 articleMap.put("id",id); ???????????? ????????? 
				//3 articleMap.put("imageFileList", imageFileList); ???????????? ?????? ????????????
				// ?????? ?????? 1??? -> 2????????? ??????. boardService
				System.out.println("boardService.addNewArticle(articleMap); ?????? ???");
//				List<ImageVO> imageFileList2 = (ArrayList)articleMap.get("imageFileList");
//				for(ImageVO imageVO : imageFileList2){
//					System.out.println("BoardController ?????? ?????? ????????? ???????????? ?????? ??? ????????? ??? : "+ imageVO.getImageFileName());
//				}
				int articleNO;
				if(fileList!= null && fileList.size()!=0) {
				articleNO = boardService.addNewArticleWithImage(articleMap);
				} else {
				articleNO = boardService.addNewArticleWithoutImage(articleMap);
				}
				System.out.println("boardService.addNewArticle(articleMap); ?????? ???");
//				System.out.println("imageFileList??? ???: ?"+imageFileList);
//				System.out.println("imageFileList??? get(0) ???: ?"+imageFileList.get(0).toString());
//				System.out.println("imageFileList??? isEmpty: ?"+imageFileList.isEmpty());
				
				//????????? ???, ???????????? ?????? ??????. 
				// ?????? ?????? ???????????????, multipartRequest ??? ?????? ????????? ????????? ????????? ????????? ????????? ???.
				// imageFileList ?????? imageVO (null) ?????? ????????? ??????. 
				// ?????? ?????? ????????? ??????. !imageFileList.isEmpty() && imageFileList!=null && imageFileList.size()!=0
				
				if(!imageFileList.isEmpty() && imageFileList!=null && imageFileList.size()!=0) {
					System.out.println("if(!imageFileList.isEmpty() && imageFileList!=null && imageFileList.size()!=0) {");
					for(ImageVO  imageVO:imageFileList) {
						imageFileName = imageVO.getImageFileName();
//						if(imageFileName != null) {
						System.out.println("imageFileName??? :"+imageFileName);
						File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
						System.out.println("srcFile ??????? "+srcFile);
						File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
						System.out.println("destDir ??????? "+destDir);
						//destDir.mkdirs();
						
						FileUtils.moveFileToDirectory(srcFile, destDir,true);
//						}
						
					}
				}
				    
				message = "<script>";
				message += " alert('????????? ??????????????????.');";
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
				message +=" alert('????????? ??????????????????. ?????? ????????? ?????????');');";
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
			//?????? ????????? ???????????????
			private String upload(MultipartHttpServletRequest multipartRequest) throws Exception{
				String imageFileName= null;
				Iterator<String> fileNames = multipartRequest.getFileNames();
				
				while(fileNames.hasNext()){
					String fileName = fileNames.next();
					MultipartFile mFile = multipartRequest.getFile(fileName);
					imageFileName=mFile.getOriginalFilename();
					File file = new File(ARTICLE_IMAGE_REPO +"\\"+ fileName);
					if(mFile.getSize()!=0){ //File Null Check
						if(! file.exists()){ //???????????? ????????? ???????????? ?????? ??????
							if(file.getParentFile().mkdirs()){ //????????? ???????????? ?????????????????? ??????
									file.createNewFile(); //?????? ?????? ??????
							}
						}
						mFile.transferTo(new File(ARTICLE_IMAGE_REPO +"\\"+"temp"+ "\\"+imageFileName)); //????????? ????????? multipartFile??? ?????? ????????? ??????
					}
				}
				return imageFileName;
			}*/
			
			
			//?????? ????????? ???????????????
			private List<String> upload(MultipartHttpServletRequest multipartRequest) throws Exception{
				// ????????? ???????????? ?????? ????????? ??????. 
				List<String> fileList= new ArrayList<String>();
				// ?????? file1, file2 ?????? ?????? ????????? ????????? ????????????. 
				Iterator<String> fileNames = multipartRequest.getFileNames();
				
				while(fileNames.hasNext()){
					// fileName -> name = file1, file2 ....
					String fileName = fileNames.next();
					System.out.println("upload ??? fileName = " + fileName);
					//name = file1 ??? ??????????????? ?????? ?????? ????????? ?????? ?????? : mFile??? ??????. 
					MultipartFile mFile = multipartRequest.getFile(fileName);
					// mFile?????? ?????? ????????? ????????? ????????? ????????????. 
					String originalFileName=mFile.getOriginalFilename();
					System.out.println("upload ???originalFileName = " + originalFileName);
					// originalFileName : ex) bread.jpg
					// ????????? ???????????? ?????? ???????????? ??????. 
					fileList.add(originalFileName);
					// "C:\\board\\article_image" ?????? ????????? ?????? ????????? ????????? ?????? ?????????. 
					File file = new File(ARTICLE_IMAGE_REPO +"\\"+ fileName);
					
					// mFile ????????? ???????????? ?????????. 
					if(mFile != null && mFile.getSize()!=0){ //File Null Check
						
						if(! file.exists()){ //???????????? ????????? ???????????? ?????? ??????
							// ?????? ?????? ????????? ?????? ??? ????????? ?????? ?????? ????????? ?????? ??????????????? ????????? ??????. 
							if(file.getParentFile().mkdirs()){ //????????? ???????????? ?????????????????? ??????
									file.createNewFile(); //?????? ?????? ??????
									// ????????? ?????? ????????? ?????? ??? ??? ????????? ?????? ?????? ??????.
							}
						}
						// ???????????? ?????? ??? ?????? ???????????? ?????? ?????? ?????? ????????? ?????? ?????? ???. 
						// ????????? ?????????, ?????? ?????? ????????? ????????? ???????????? ?????????? 
						// "C:\\board\\article_image" 
						// ????????? temp ?????? ?????? ????????? ????????????. 
						// ?????? 
						// ????????? ????????? ?????? ?????? ????????? ?????? ???????????????, ????????? ??? ?????? ?????? ????????? ???. 
						// ????????? ????????? ?????? ????????? ( ???) aws , ?????? ????????? ????????????, ?????? ????????? ?????????. 
						mFile.transferTo(new File(ARTICLE_IMAGE_REPO +"\\"+"temp"+ "\\"+originalFileName)); //????????? ????????? multipartFile??? ?????? ????????? ??????
					}
				}
				return fileList;
			}
			
		}