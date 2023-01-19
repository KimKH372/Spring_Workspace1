package com.myspring.pro28.ex02;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.coobird.thumbnailator.Thumbnails;

@Controller
public class FileDownloadController {
	private static String CURR_IMAGE_REPO_PATH = "c:\\spring\\image_repo";
	
	@RequestMapping("/download")
	
	protected void download(@RequestParam("imageFileName") String imageFileName,
			                 HttpServletResponse response) throws Exception {
		OutputStream out = response.getOutputStream();
		
		String filePath = CURR_IMAGE_REPO_PATH + "\\" + imageFileName;
		File image = new File(filePath);
		
		// imageFileName : 실제 파일명.
		int lastIndex = imageFileName.lastIndexOf(".");
		System.out.println("lastIndex의 값 : " + lastIndex);
		// 예) lastIndex 6
		// 예) bread1.jpg : 총 크기 : 10, 인덱스 : 0~9
		// 예) fileName : bread1
		String fileName = imageFileName.substring(0,lastIndex);
		System.out.println("fileName의 값 : " + fileName);
		// 경로에 하위 폴더가 추가. thumbnail 폴더 추가
		// 폴더 안에 예) bread1.png 파일명으로 변환 될 예정
		File thumbnail = new File(CURR_IMAGE_REPO_PATH+"\\"+"thumbnail"+"\\"+fileName+".png");
		if (image.exists()) { 
			// image : File image = new File(filePath); CURR_IMAGE_REPO_PATH
			// 예) image  C:\spring\image_repo
			thumbnail.getParentFile().mkdirs();
			// 실제 썸네일 크기로 전환 되는 작업
			// 파일이 저장되는 코드
			// thumbnail : c:\\spring\\image_repo\thumbnail
		    Thumbnails.of(image).size(50,50).outputFormat("png").toFile(thumbnail);
		    // 실제 업로드 경로에 파일 추가 안하는 명령어.
		    //Thumbnails.of(image).size(50,50).outputFormat("png").toOutputStream(out)
		}

		// 브라우저에 쓰기 작업
		FileInputStream in = new FileInputStream(thumbnail);
		byte[] buffer = new byte[1024 * 8];
		while (true) {
			int count = in.read(buffer); // 버퍼에 읽어들인 문자개수
			if (count == -1) // 버퍼의 마지막에 도달했는지 체크
				break;
			out.write(buffer, 0, count);
		}
		in.close();
		out.close();
	}
	
/*	
	@RequestMapping("/download")
	protected void download(@RequestParam("imageFileName") String imageFileName,
			                 HttpServletResponse response) throws Exception {
		OutputStream out = response.getOutputStream();
		String filePath = CURR_IMAGE_REPO_PATH + "\\" + imageFileName;
		File image = new File(filePath);
		int lastIndex = imageFileName.lastIndexOf(".");
		String fileName = imageFileName.substring(0,lastIndex);
		File thumbnail = new File(CURR_IMAGE_REPO_PATH+"\\"+"thumbnail"+"\\"+fileName+".png");
		if (image.exists()) { 
			Thumbnails.of(image).size(50,50).outputFormat("png").toOutputStream(out);
		}else {
			return;
		}
		byte[] buffer = new byte[1024 * 8];
		out.write(buffer);
		out.close();
	}
	*/
}
