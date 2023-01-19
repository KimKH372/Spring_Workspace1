package com.myspring.pro30.board.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.myspring.pro30.board.dao.BoardDAO;
import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.board.vo.ImageVO;


@Service("boardService")
@Transactional(propagation = Propagation.REQUIRED)
public class BoardServiceImpl  implements BoardService{
	@Autowired
	BoardDAO boardDAO;
	
	public List<ArticleVO> listArticles() throws Exception{
		List<ArticleVO> articlesList =  boardDAO.selectAllArticlesList();
        return articlesList;
	}

	
	//단일 이미지 추가하기
	/*
	 * @Override public int addNewArticle(Map articleMap) throws Exception{ return
	 * boardDAO.insertNewArticle(articleMap); }
	 */
	
	//다중 이미지 추가하기
		// 1번 위치에서 -> articleMap 객체가 넘어 왔음. 

		// 현재 위치 2번 -> 3번에게 요청(DAO)

	@Override
	public int addNewArticleWithImage(Map articleMap) throws Exception{
		//articleMap 에 담은 정보를 기반으로해서, 해당 글쓰기 작업으로 들어가기. 
		// 해당 객체에 접근 하기위한 메서드들이 있음. 
		// 해당 일반글 작성 후 리턴의 결과
		int articleNO = boardDAO.insertNewArticle(articleMap);
		// 해당 글을 작성 후, 해당 글의 게시글 번호를 리턴해서, 다시 맵에 재할당.
		articleMap.put("articleNO", articleNO);
		List<ImageVO> imageFileList = (ArrayList)articleMap.get("imageFileList");
		for(ImageVO imageVO : imageFileList){
			System.out.println("BoardService 에서 해당 파일명 깨지는지 확인 중 반복문 안 : "+ imageVO.getImageFileName());
		}
		// 이미지를 추가하는 기능. 
		boardDAO.insertNewImage(articleMap);
		return articleNO;
	}
	
	@Override
	public int addNewArticleWithoutImage(Map articleMap) throws Exception{
		//articleMap 에 담은 정보를 기반으로해서, 해당 글쓰기 작업으로 들어가기. 
		// 해당 객체에 접근 하기위한 메서드들이 있음. 
		// 해당 일반글 작성 후 리턴의 결과
		int articleNO = boardDAO.insertNewArticle(articleMap);
		// 해당 글을 작성 후, 해당 글의 게시글 번호를 리턴해서, 다시 맵에 재할당.
		articleMap.put("articleNO", articleNO);
		
		return articleNO;
	}
	
	//다중 파일 보이기
	@Override
	public Map viewArticle(int articleNO) throws Exception {
		// 1번에서 2번으로 해당 게시글의 번호가 넘어 왔음. 
		// 예) articleNO = 1
		
		// 조회된 정보를 담기 위한 임시 컬렉션
		Map articleMap = new HashMap();
		// 일반 글의 내용을 담기 위한 임시 객체
		//selectArticle  뭘까? 
		// 해당 게시글 번호에 대한 게시글의 정보를 리턴함. 
		ArticleVO articleVO = boardDAO.selectArticle(articleNO);
		
		//파일 이미지들의 내용을 담기 위한 임시 컬렉션
		//selectImageFileList 뭘까? 
		List<ImageVO> imageFileList = boardDAO.selectImageFileList(articleNO);
		// 일반글의 내용을 조회한 결과
		articleMap.put("article", articleVO);
		// 파일 이미지들의 내용을 조회한 결과
		articleMap.put("imageFileList", imageFileList);
		return articleMap;
	}
   
	
	
	//단일 파일 보이기
	/*
	 * @Override public ArticleVO viewArticle(int articleNO) throws Exception {
	 * ArticleVO articleVO = boardDAO.selectArticle(articleNO); 
	 * return articleVO;
	 *  }
	 */
	
	
	@Override
	public void modArticle(Map articleMap) throws Exception {
		boardDAO.updateArticle(articleMap);
	}
	
	@Override
	public void removeArticle(int articleNO) throws Exception {
		boardDAO.deleteArticle(articleNO);
	}
	

	
}
