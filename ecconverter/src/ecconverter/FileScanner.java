package ecconverter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Queue;

public class FileScanner {
	
	public String[] readFiles() {
		File file = new File(Config.BASIC_PATH+"\\");
		if(!file.exists()) {
			System.out.println("작업 대상 폴더를 찾을 수 없어, 폴더를 새로 생성합니다. 대상 폴더에 변환을 원하는 파일을 첨부 후 다시 시도해주세요.");
			System.out.println("생성된 작업 대상 폴더 경로 :  " + Config.BASIC_PATH);
			file.mkdir();
			System.exit(0);
		}
		return file.list();
	}
	public void makeList(String targetPath, Queue<String[]> lineList) throws IOException {
		FileInputStream target= new FileInputStream(targetPath);
		BufferedReader br = new BufferedReader(new InputStreamReader(target, Config.ENCODING_NAME));
        String line;
        while((line = br.readLine())!=null) {
            String[] lineContents = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",-1);

            lineList.add(lineContents);
        }	
		target.close();
		br.close();
	}
	public void findResultPath() {
		File file = new File(Config.RESULT_PATH);
		if(!file.exists()) {
			System.out.println("결과 저장 폴더를 찾을 수 없어, 폴더를 새로 생성합니다. 해당 폴더에 변환된 파일이 저장됩니다.");
			System.out.println("생성된 작업 대상 폴더 경로 :  " + Config.RESULT_PATH);
			file.mkdir();
		}
	}	
}
