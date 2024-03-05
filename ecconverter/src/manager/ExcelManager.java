package manager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import config.Config;

public class ExcelManager {
	
	String extension;
	Workbook workbook;
	Map<String, CellStyle> styleList=new HashMap<>();
	
	public void setExtenstion(String extension) {
		this.extension = extension;
	}
	
	public Workbook createWorkbook(FileInputStream fs) throws IOException {
		switch(extension) {
			case "xls" : return new HSSFWorkbook(fs);
			
			case "xlsx" : return new XSSFWorkbook(fs);
						  
			default: System.out.println("dd"); return new XSSFWorkbook(fs);
		}
	}
	
	public Workbook createWorkbook(FileInputStream fs, String extensions) throws IOException {
		switch(extensions) {
			case "xls" : return new HSSFWorkbook(fs);
			
			case "xlsx" : return new XSSFWorkbook(fs);
						  
			default: System.out.println("dd"); return new XSSFWorkbook(fs);
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
}
