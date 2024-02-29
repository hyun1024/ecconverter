package manager;

import java.io.IOException;
import java.util.Queue;

import converter.Converter;
import worker.FileScanner;
import worker.NameMaker;
import worker.TaskSelector;


public class WorkManager {
	
	//TODO 주입관리자 분리?
	ListManager lm = new ListManager();
	NameMaker nm = new NameMaker();
	FileScanner fs = new FileScanner();
	ExcelManager em = new ExcelManager();
	ConverterManager cm = new ConverterManager(lm, fs, nm, em);
	TaskSelector ts = new TaskSelector(lm);
	public void run() {
		fs.setInitFolder();
		while(true) {
			try {
				work(ts.setTask());
			} catch (NullPointerException e) {
				ts.resetTaskNum();
				continue;
			}

			break;
		}

	}
	//새로운 버전
	public void work(String extension) {
		lm.initialFileList();
		lm.makeWorkLists(fs.readFiles(), extension);
		int count = lm.getTargetList().size();
		if(count==0) {
			System.out.println("작업 대상 파일이 없어 프로그램을 종료합니다.");
			System.exit(0);
		}
		System.out.println("총 작업 개수 [ "+count+" ] 개. 작업 시작");
		Converter cv = cm.createConverter(extension);	
		String target;
			while(!lm.getTargetList().isEmpty()) {
				target = lm.nextTarget();
				try {
					cv.convert(lm.getExtension(target), target);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(target+" 작업 완료. [ " + (count-lm.getTargetList().size())+" / "+count+" ]");
			}
		System.out.println("---전체 작업 완료.---");
	}
	
	public void exit() {
		System.out.println("프로그램을 종료합니다.");
		System.exit(0);
	}
}
