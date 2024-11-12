package worker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import config.Config;

public class FileScanner {
	
	private Logger logger;
	
	public FileScanner(Logger logger) {
		this.logger = logger;
	}
	public void setInitFolder() throws Exception {
		File file = new File(Config.TARGET_PATH+"\\");
		if(!file.exists()) {
			logger.writeLog("작업 대상 폴더를 찾을 수 없어, 폴더를 새로 생성합니다. 대상 폴더에 변환을 원하는 파일을 첨부한 후 작업 번호를 입력해주세요.", false);
			logger.writeLog("생성된 작업 대상 폴더 경로 :  " + Config.TARGET_PATH, true);
			logger.writeLog("-----------------------------------------------", false);
			file.mkdir();
		}
	}
	
	public String setDelimiter() throws Exception {
		File file = new File(Config.DELIMITER_CONFIG_PATH);
		
		if(!file.exists()) {
			logger.writeLog("config 파일을 찾을 수 없어 Delimiter값을 ','로 초기화합니다.", true);
			return ",";
		}
		BufferedReader br = new BufferedReader(new FileReader(file));
		String delimiter = br.readLine();
		if(delimiter == null) {
			logger.writeLog("config 정보를 읽을 수 없어 Delimiter값을 ','로 초기화합니다.", true);
			return ",";
		}
		logger.writeLog("delimiter.conf 에서 읽은 delimiter : "+delimiter, true);
		return delimiter;
		
	}
	public String[] readFiles() throws Exception{
		File file = new File(Config.TARGET_PATH+"\\");
		if(!file.exists()) {
			logger.writeLog("작업 대상 폴더를 찾을 수 없어, 폴더를 새로 생성합니다. 대상 폴더에 변환을 원하는 파일을 첨부 후 다시 시도해주세요.", false);
			logger.writeLog("생성된 작업 대상 폴더 경로 :  " + Config.TARGET_PATH, true);
			logger.writeLog("-----------------------------------------------", false);
			file.mkdir();
			return null;
		}
		return file.list();
	}
	public List<Integer> readWidthConfig() throws Exception{
		File file = new File(Config.WIDTH_CONFIG_PATH);
		if(!file.exists()) {
			logger.writeLog("컬럼 정보 파일을 찾을 수 없어, 기본 설정으로 진행합니다.", true);
			return null;
		}
		BufferedReader br = new BufferedReader(new FileReader(file));
		List<Integer> widthList = new LinkedList<>();
		String title = br.readLine();
		while(true) {
			try {
				String line = br.readLine();
				if(line==null) {
					break;
				}
				int width = Integer.parseInt(line);
				widthList.add(width);
			} catch (Exception e) {
				logger.writeLog("컬럼 너비 정보 조회에 실패하여, 기본 설정으로 진행합니다.", true);
				return null;
			}
		}
		if(widthList.isEmpty()) {
			logger.writeLog("컬럼 너비 정보가 존재하지 않아, 기본 설정으로 진행합니다.", true);
			return null;
		}
		logger.writeLog("columnWidth 구성 : "+title, true);
		return widthList;
	}
	public void makeList(String targetPath, Queue<String[]> lineList, String delimiter) throws IOException {
		FileInputStream target= new FileInputStream(targetPath);
		String del = delimiter;
		if(del.equals("|")) {
			del = "\\|";
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(target, Config.ENCODING_NAME));
        String line;
        while((line = br.readLine())!=null) {
            String[] lineContents = line.split(del+"(?=([^\"]*\"[^\"]*\")*[^\"]*$)",-1);

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
