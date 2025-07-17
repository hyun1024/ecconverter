package worker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import config.Config;

public class Logger {
	
	public File logFile;
	
	SimpleDateFormat filenameFormat = new SimpleDateFormat("yyyy-MM-dd hhmmss");
	SimpleDateFormat logdateFormat = new SimpleDateFormat("[yyyy-MM-dd hh:mm:ss]");
	
	public void setLogFile() {
		String dir = Config.BASIC_PATH+"\\"+"Log";
		File dirFile = new File(dir);
		if(!dirFile.exists()) {
			dirFile.mkdir();
		}
		
		logFile = new File(dir+"\\"+filenameFormat.format(new Date())+".log");
		System.out.println("로그파일 생성 완료");
	}
	
	public void writeLog(String info, Boolean isPrint) throws Exception {
		BufferedWriter bw = new BufferedWriter(new FileWriter(logFile, true));
		String logdate = logdateFormat.format(new Date())+" ";
		bw.write(logdate);
		bw.write(info);
		bw.write("\n");
		if(isPrint) {
			System.out.println(logdate+info);
		}
		bw.close();
	}
}
