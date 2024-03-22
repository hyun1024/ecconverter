package manager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import config.Config;

public class ExcelManager {
	static int DEFAULT_WIDTH = 67;
	String extension;
	Workbook workbook;
	Map<String, CellStyle> styleList=new HashMap<>();
	List<Integer> sheetWidth = new LinkedList<>();
	public void setExtenstion(String extension) {
		this.extension = extension;
	}
	//autosize 대체로 본인 양식에 맞게 변경
	public void setSheetWidth() {
		sheetWidth.add(58);
		sheetWidth.add(169);
		sheetWidth.add(110);
		sheetWidth.add(38);
		sheetWidth.add(73);
		sheetWidth.add(84);
		sheetWidth.add(75);
		sheetWidth.add(100);
		sheetWidth.add(70);
		sheetWidth.add(85);
		sheetWidth.add(38);
		sheetWidth.add(155);
		sheetWidth.add(69);
		sheetWidth.add(69);
		sheetWidth.add(40);
		sheetWidth.add(69);
	}
	public Integer getSheetWidth(int column) {
		if(sheetWidth.get(column)==null) {
			return DEFAULT_WIDTH;
		}
		return sheetWidth.get(column);
	}
	
	public Workbook createWorkbook(FileInputStream fs, String extensions) throws IOException {
		switch(extensions) {
			case "xls" : return new HSSFWorkbook(fs);
			
			case "xlsx" : return new XSSFWorkbook(fs);
						  
			default : return new XSSFWorkbook(fs);
		}
	}
    public void InitialStyle(Workbook workbook) {
    	Font font = workbook.createFont();
    	font.setFontName(Config.FONT_NAME);
    	font.setFontHeightInPoints(Config.FONT_SIZE);
    	CellStyle value = workbook.createCellStyle();
    	value.setFont(font);
    	value.setVerticalAlignment(VerticalAlignment.CENTER);
    	styleList.put("value", value);
    	CellStyle title = workbook.createCellStyle();
    	title.setFont(font);
    	title.setVerticalAlignment(VerticalAlignment.CENTER);
    	title.setAlignment(HorizontalAlignment.CENTER);
    	title.setBorderBottom(BorderStyle.THIN);
    	styleList.put("title", title);
    }
    public CellStyle getCellStyle(String styleName) {
    	return styleList.get(styleName);
    }
	public void setColumnWidth(Sheet sheet, int columnCount, Boolean isUsingAutoSizeColumn) {
		//autoSizeColumn 속도로 인해 적절한 columnWidth 직접 리스트화해서 사용.

		for(int j=0; j<columnCount; j++) {
			if(isUsingAutoSizeColumn) {
				sheet.autoSizeColumn(j);
			} else {
				sheet.setColumnWidth(j, getSheetWidth(j)*37);				
			}
		}
		
		
	}
}
