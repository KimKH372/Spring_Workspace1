package com.myspring.pro28.ex01;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

//다운로드 부분이, 업로드가 된 파일들을 스트림으로 읽어서 버퍼에 저장후
//이 내용에 웹브라우저에 뿌리는 작업.

//@Controller 부분이 주석이 된 상태에서 업로드를 실해하고,
//해당 이미지를 출력 하는 result 부분에서 , 다운로드 위치 부분이 중첩 
//@Controller
public class FileDownloadController {
	private static String CURR_IMAGE_REPO_PATH = "c:\\spring\\image_repo";

	@RequestMapping("/download")
	//imageFileName 해당 이미지의 a 링크 주소에 
	// src="${pageContext.request.contextPath }/download?imageFileName=${imageFileName }">
	protected void download(@RequestParam("imageFileName") String imageFileName,
			                 HttpServletResponse response)throws Exception {
		// imageFileName 에는 실제 파일 이미지의 이름이 있음. 
				// 바이트 단위로 전달 하기 위해서 , 서버에서 응답 객체를 생성함. 
		OutputStream out = response.getOutputStream();
		
		//downFile 해당경로를 포함한 이미지 파일.
		String downFile = CURR_IMAGE_REPO_PATH + "\\" + imageFileName;
		
		//downFile 경로를 매개변수로 해서 file 객체 생성.
		File file = new File(downFile);

		// http 통신의 규격에 맞게끔 전달하는 과정. 
		response.setHeader("Cache-Control", "no-cache");
		response.addHeader("Content-disposition", "attachment; fileName=" + imageFileName);
		
		// 입력 하기 위한 스트림 객체 생성. file 경로에 해당하는 객체.
		FileInputStream in = new FileInputStream(file);
		// 입력된 내용을 버퍼에 담을 예정. 8KB 단위로.
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

}
