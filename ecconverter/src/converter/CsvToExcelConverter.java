package converter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
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
	public void convert(String filename) throws IOException{
		String CREATE_PATH = nm.createResultPath(filename, RESULT_EXTENSION);
		String TARGET_PATH = nm.createReadPath(filename);
		fs.makeList(TARGET_PATH, lineList);
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
		em.setColumnWidth(sheet, columnCount, USE_AUTO_SIZE_COLUMN);

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
}
