package converter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.Queue;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import config.Config;
import manager.ExcelManager;
import worker.FileScanner;
import worker.NameMaker;

public class CsvToExcelConverter implements Converter{
	
	static String RESULT_EXTENSION = "xlsx";
	
	//필요할 경우 true로 바꾸면 autoSizeColumn 적용됨
	static Boolean USE_AUTO_SIZE_COLUMN = false;
	
	NameMaker nm;
	FileScanner fs;
	ExcelManager em;
	
	//대상 csv파일 읽어서 저장하는 list
	Queue<String[]> lineList = new LinkedList<>();
	
	public CsvToExcelConverter(FileScanner fs, NameMaker nm, ExcelManager em) {
		this.fs=fs;
		this.nm=nm;
		this.em=em;
	}
	public void convert(String filename, String delimiter) throws IOException{
		String CREATE_PATH = nm.createResultPath(filename, RESULT_EXTENSION);
		String TARGET_PATH = nm.createReadPath(filename);
		fs.makeList(TARGET_PATH, lineList, delimiter);
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		SXSSFSheet sheet= workbook.createSheet("sheet");
		sheet.setDefaultRowHeightInPoints(Config.ROW_HEIGHT);
		em.InitialStyle(workbook);
		CellStyle title = em.getCellStyle("title");
		CellStyle value = em.getCellStyle("value");
		Row row;
		Cell cell;
		int rowIndex = 0;
		int columnCount = 0;
		//반복문 전체 돌기 전엔 엑셀 column수 파악 안됨. 임시 배열
		int[] maxColumnSize = new int[1000];
		while(!lineList.isEmpty()) {
			String[] targetString = lineList.poll();
			row = sheet.createRow(rowIndex++);
			for(int i=0; i<targetString.length; i++) {
				if(targetString[i].endsWith("\"")) {
					targetString[i]= targetString[i].substring(1, targetString[i].length()-1);
					targetString[i]= targetString[i].replaceAll("\"\"", "\"");
				}
				cell = row.createCell(i);
				cell.setCellValue(targetString[i]);
				cell.setCellStyle(value);
				maxColumnSize[i] = Math.max(maxColumnSize[i], getWeightedLength(targetString[i]));
				if(rowIndex==1) {
					cell.setCellStyle(title);
					columnCount++;
				}
			}
		}
		em.setColumnWidth(sheet, columnCount, maxColumnSize);

		FileOutputStream os = new FileOutputStream(CREATE_PATH);
		try {
			workbook.write(os);
			workbook.close();
			os.close();

		} catch (OutOfMemoryError e) {
			System.out.println("메모리 오류로 프로그램을 종료합니다.");
			workbook.close();
			os.close();
			System.exit(0);
		}
	}
	private int getWeightedLength(String value) {
	    if (value == null || value.trim().isEmpty()) {
	        return 0;
	    }

	    float len = 0;
	    for (int i = 0; i < value.length(); i++) {
	        char ch = value.charAt(i);

	        // 완성형 한글 (가 ~ 힣)
	        if (ch >= 0xAC00 && ch <= 0xD7A3) {
	            len += 1.6;
	        }
	        // 한글 자모 (ㄱ ~ ㅎ, ㅏ ~ ㅣ)
	        else if (ch >= 0x3131 && ch <= 0x318E) {
	            len += 1.6;
	        }
	        // 그 외 (영문, 숫자, 특수문자 등)
	        else {
	            len += 0.9;
	        }
	    }

	    return Math.round(len);
	}
}
