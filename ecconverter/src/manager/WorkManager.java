package manager;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import converter.Converter;
import worker.FileScanner;
import worker.Logger;
import worker.NameMaker;
import worker.TaskSelector;


public class WorkManager {
	
	//TODO 주입관리자 분리?
	Logger logger = new Logger();
	ListManager lm = new ListManager();
	NameMaker nm = new NameMaker();
	FileScanner fs = new FileScanner(logger);
	ExcelManager em = new ExcelManager();
	ConverterManager cm = new ConverterManager(fs, nm, em);
	TaskSelector ts = new TaskSelector(lm, logger);
	
	public String delimiter = "";
	
	public void run() throws Exception {
		fs.setInitFolder();
		logger.setLogFile();
		delimiter = fs.setDelimiter();
		em.addWidthConfig(fs.readWidthConfig());
		while(true) {
			try {
				ts.setTask();
				work(ts.getTargetExtension(), delimiter);
			} catch (NullPointerException e) {
				ts.resetTaskNum();
				continue;
			}

			break;
		}

	}
	public void work(String extension, String delimiter) throws Exception{
		lm.initialFileList();
		lm.makeWorkList(fs.readFiles(), extension);
		int count = lm.getTargetList().size();
		if(count==0) {
			logger.writeLog("작업 대상 파일이 없어 프로그램을 종료합니다.", true);
			System.exit(0);
		}
		logger.writeLog("총 작업 개수 [ "+count+" ] 개. 작업 시작", true);
		fs.findResultPath();
		em.setExtenstion(extension);
		Converter cv = cm.createConverter(extension);
		String target;
			while(!lm.getTargetList().isEmpty()) {
				target = lm.nextTarget();
				logger.writeLog(FilenameUtils.getBaseName(target)+" 시작... ", true);
				try {
					cv.convert(target, delimiter);
				} catch (Exception e) {
					logger.writeLog(FilenameUtils.getBaseName(target)+" 작업 실패. 원인 : "+ e.getMessage(), true);
					e.printStackTrace();
					continue;
				}
				logger.writeLog(FilenameUtils.getBaseName(target)+" 작업 완료. [ " + (count-lm.getTargetList().size())+" / "+count+" ]", true);
			}
			logger.writeLog("---전체 작업 완료.---", true);
	}
	
	public void exit() throws Exception {
		logger.writeLog("프로그램을 종료합니다.", false);
		System.exit(0);
	}
}
