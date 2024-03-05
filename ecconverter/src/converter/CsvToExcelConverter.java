package converter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import config.Config;
import manager.ExcelManager;
import manager.ListManager;
import worker.FileScanner;
import worker.NameMaker;

public class CsvToExcelConverter implements Converter{
	
	static String RESULT_EXTENSION = "xlsx";
	NameMaker nm;
	FileScanner fs;
	ExcelManager em;
	Queue<String[]> lineList = new LinkedList<>();
	
	public CsvToExcelConverter(FileScanner fs, NameMaker nm, ExcelManager em) {
		this.fs=fs;
		this.nm=nm;
		this.em=em;
	}
	public void convert(String filename) throws IOException {
		String TARGET_PATH = nm.createReadPath(filename);
		fs.makeList(TARGET_PATH, lineList);
		int reference=Config.MAX_ROW_COUNT*2;
		if(lineList.size()>reference) {
			System.out.println("파일이 커서 분리 작업 방식으로 진행합니다.");
			bigCsvToXlsx(filename);

		} else {
			csvToXlsx(filename);
		}
	}
	public void csvToXlsx(String filename) throws IOException{
			String CREATE_PATH = nm.createResultPath(filename, RESULT_EXTENSION);
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet= workbook.createSheet("sheet");
			sheet.setDefaultRowHeightInPoints(Config.ROW_HEIGHT);
			em.InitialStyle(workbook);
			CellStyle title = em.getCellStyle("title");
			CellStyle value = em.getCellStyle("value");
			XSSFRow row;
			XSSFCell cell;
			int rowIndex=0;
			int columnCount = 0;
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
					if(rowIndex==1) {
						cell.setCellStyle(title);
						columnCount++;
					}
				}
			}
			for(int j=0; j<columnCount; j++) {
				sheet.autoSizeColumn(j);
			}
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
	public void bigCsvToXlsx(String filename) throws IOException{
		int filenum = 1;
		String[] titleString = null;
		while(!lineList.isEmpty()) {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet= workbook.createSheet("sheet");
			sheet.setDefaultRowHeightInPoints(16.5F);
			em.InitialStyle(workbook);
			CellStyle title = em.getCellStyle("title");
			CellStyle value = em.getCellStyle("value");
			XSSFRow row;
			XSSFCell cell;
			int rowIndex=0;
			int columnCount = 0;
			while(!lineList.isEmpty()) {
				String[] targetString = lineList.poll();
				row = sheet.createRow(rowIndex++);
				if(rowIndex==1 && filenum!=1) {
					for(int i=0; i<titleString.length-1; i++) {
						cell = row.createCell(i);
						cell.setCellValue(titleString[i]);
						cell.setCellStyle(title);
						columnCount++;
					}
					row = sheet.createRow(rowIndex++);
				}
				for(int i=0; i<targetString.length; i++) {
					//csv->excel parsing
					if(targetString[i].endsWith("\"")) {
						targetString[i]= targetString[i].substring(1, targetString[i].length()-1);
						targetString[i]= targetString[i].replaceAll("\"\"", "\"");
					}
					//title열 복제
					if( rowIndex==1 && filenum==1 && i==targetString.length-1) {
						titleString = targetString.clone();
					}
					cell = row.createCell(i);
					cell.setCellValue(targetString[i]);
					cell.setCellStyle(value);
					if(rowIndex==1) {
						cell.setCellStyle(title);
						columnCount++;
					}
				}
				if(rowIndex>Config.MAX_ROW_COUNT) {
					break;
				}
			}
			for(int i=0; i<columnCount; i++) {
					sheet.autoSizeColumn(i);

			}
			FileOutputStream os = new FileOutputStream(nm.createNumberingXlsxName(filename, filenum++));
			try {
				workbook.write(os);
				workbook.close();
				os.close();
				System.out.println((filenum-1)+"번 파일 완료");
			} catch (OutOfMemoryError oom) {
				System.out.println("메모리 오류로 프로그램을 종료합니다.");
				workbook.close();
				os.close();
				System.exit(0);
			}
		}

	}
}
