package ecconverter;

import java.io.IOException;
import java.util.Queue;


public class WorkManager {
	
	//TODO 주입관리자 분리?
	ListManager lm = new ListManager();
	NameMaker nm = new NameMaker();
	FileScanner fs = new FileScanner();
	ConverterManager cm = new ConverterManager(lm, fs, nm);
	TaskSelector ts = new TaskSelector(lm);
	
	public void run() {
		ts.setTask();
		work(ts.getTaskNum());
	}
	public void work(int num){
		try {
			switch(num) {
			case 1 : csvToExcel(); break;
			case 2 : excelToCsv(); break;
			
			//TODO work Listup할 때 ts에서의 List랑 switch문 연동되도록
			//TODO TaskSelector에서 escape하는데 2중으로 validation을 해야 할지 결정
			default : System.out.println("작업 값이 손상되었습니다. 다시 시도해주세요.");
			}
		} catch (IOException e) {
			System.out.println("입출력 오류로 프로그램을 종료합니다.");
			System.exit(0);
		}

	}
	public void excelToCsv() throws IOException {
		//initial for ExcelToCsv
		lm.initialExcelList();
		Queue<String> xlsList = lm.getXlsList();
		Queue<String> xlsxList = lm.getXlsxList();
		
		//readfile
		String[] fileList = fs.readFiles();
		lm.makeExcelWorkList(fileList);
		int count = lm.getExcelListSize();
		System.out.println("총 작업 개수 [ "+count+" ] 개. 작업 시작");
		
		Converter cv = cm.createConverter("excel");		
		while(!xlsList.isEmpty()) {
			String targetName= xlsList.poll();
			cv.convert("xls", targetName);
			System.out.println(targetName+" 작업 완료. [ " + (count-xlsList.size())+" / "+count+" ]");
		}
		while(!xlsxList.isEmpty()) {
			String targetName = xlsxList.poll();
			cv.convert("xlsx", targetName);
			System.out.println(targetName+" 작업 완료. [ " + (count-xlsxList.size())+" / "+count+" ]");
		}
		System.out.println("---전체 작업 완료.---");
	}
	
	public void csvToExcel() throws IOException {
		//initial for CsvToExcel
		lm.initialCsvList();
		Queue<String>csvList = lm.getCsvList();
		
		//readfile
		String[] fileList = fs.readFiles();
		lm.makeCsvWorkList(fileList);
		int count=csvList.size();
		System.out.println("총 작업 개수 [ "+count+" ] 개. 작업 시작");
		
		Converter cv = cm.createConverter("csv");
		while(!lm.csvList.isEmpty()) {
			String targetName = csvList.poll();
			System.out.println(targetName+" 시작... [ " + (count-csvList.size())+" / "+count+" ]");
			cv.convert("csv", targetName);
			System.out.println(targetName+" 작업 완료.");
		}
		System.out.println("---전체 작업 완료.---");
	}
}
