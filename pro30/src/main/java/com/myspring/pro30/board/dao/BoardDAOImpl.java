package com.myspring.pro30.board.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.board.vo.ImageVO;


@Repository("boardDAO")
public class BoardDAOImpl implements BoardDAO {
	@Autowired
	private SqlSession sqlSession;

	@Override
	public List selectAllArticlesList() throws DataAccessException {
		List<ArticleVO> articlesList = articlesList = sqlSession.selectList("mapper.board.selectAllArticlesList");
		return articlesList;
	}

	// 입력된 문자열 부분만 처리해서 등록하는 부분. 일반글 작성
		@Override
		public int insertNewArticle(Map articleMap) throws DataAccessException {
			// selectNewArticleNO 메서드 살펴보기 
			// 해당 게시글의 글 번호에 대해서 조회 후, 1 증가 시킨 번호이네. 
			// 예) 기존에 글이 2개있다. 조회의 결과, 3이 나옴.

			System.out.println("selectNewArticleNO 호출 전===============");
			// 최초에 selectNewArticleNO() 호출 하면 오류가 발생. 
			// 디벨로퍼에서는 동작을 하지만, 자바에서는 실행 하면 오류가 발생. 
			// 널 체크를 디비에서 nvl 함수로 대체 하겠음.
			
			int articleNO = selectNewArticleNO();
			System.out.println("selectNewArticleNO 호출 후===============");
			
			articleMap.put("articleNO", ++articleNO);
			sqlSession.insert("mapper.board.insertNewArticle",articleMap);
		
			return articleNO;
		}
    
		//다중 파일 업로드
		// 만약, 이미지 파일 부분이 있다면, 이 메서드를 수행해서 , 파일 처리 동작을 함. 
		//
		//articleMap 에 담겨 있는 내용이 뭐냐?
		//1 articleMap.put(name,value); 일반글
		//2 articleMap.put("id",id); 작성자의 아이디 
		//3 articleMap.put("imageFileList", imageFileList); 업로드할 파일 이미지들
	
	@Override
	public void insertNewImage(Map articleMap) throws DataAccessException {
		//articleMap에 담기 이미지 파일 리스트를 꺼내어서 다시 , 재할당.
		List<ImageVO> imageFileList = (ArrayList)articleMap.get("imageFileList");
		// 게시글의 글 번호를 가져오는 작업
		// 왜? 파일 이미지 테이블은 부모 테이블 게시글 테이블 제약 관계가 외래키 관계이므로 
		// 반드시 필요함. 
		int articleNO = (Integer)articleMap.get("articleNO");
		//selectNewImageFileNO 이 메서드는 뭘까?
		// 파일 이미지 테이블의 글 번호 
		// 널 처리가 되어서, 
		// 최초에 해당 이미지를 등록시 0으로 할당.
		// 디비에서 nvl 함수로 널처리를 했음. 
		int imageFileNO = selectNewImageFileNO();
		for(ImageVO imageVO : imageFileList){
			// 최초 파일 이미지 등록시 : imageFileNO : 1로 증가함. 
			imageVO.setImageFileNO(++imageFileNO);
			// 부모 글의 번호를 같이 추가합니다. 
			imageVO.setArticleNO(articleNO);
		}
		// 이미지 파일들을 담은 imageFileList 가지고 , 마이바티스로 감. 
		// 아이디 : insertNewImage
		sqlSession.insert("mapper.board.insertNewImage",imageFileList);
	}
	
	// 아이디 : selectArticle
	// 결과는 해당 게시글 번호에 대한 정보를 조회해서 리턴함.
	@Override
	public ArticleVO selectArticle(int articleNO) throws DataAccessException {
		return sqlSession.selectOne("mapper.board.selectArticle", articleNO);
	}

	@Override
	public void updateArticle(Map articleMap) throws DataAccessException {
		sqlSession.update("mapper.board.updateArticle", articleMap);
	}

	@Override
	public void deleteArticle(int articleNO) throws DataAccessException {
		sqlSession.delete("mapper.board.deleteArticle", articleNO);
		
	}
	
	//해당 게시글 번호로 파일 테이블에서 해당 게시글 번호에 관련된 파일 이미지 모두를 리턴. 
	// 리스트에 담기. 
	// 아이디 : selectImageFileList
	@Override
	public List selectImageFileList(int articleNO) throws DataAccessException {
		List<ImageVO> imageFileList = null;
		imageFileList = sqlSession.selectList("mapper.board.selectImageFileList",articleNO);
		return imageFileList;
	}
	
	// 아이디 : selectNewArticleNO
		// 게시글의 글 번호를 조회하는 역할. 
		// SELECT  max(articleNO)+1 from t_board	
		// 아예 게시글이 없다면 처리가 안되는 상황.
		// null 처리가 안 된 상태. 
	
	private int selectNewArticleNO() throws DataAccessException {
		return sqlSession.selectOne("mapper.board.selectNewArticleNO");
	}
	// 아이디 : selectNewImageFileNO
			// 게시글의 글 번호를 조회하는 역할. 
		// SELECT  nvl(max(imageFileNO),0) from t_imageFile	
		// 해당 이미지 파일 테이블에 파일이 하나도 없으면, 기본값으로 0으로 할당. 
		// null 처리가 된 상태. 
	
	private int selectNewImageFileNO() throws DataAccessException {
		return sqlSession.selectOne("mapper.board.selectNewImageFileNO");
	}

}
