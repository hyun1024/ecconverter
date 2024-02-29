package manager;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelManager {
	
	String extension;
	Workbook workbook;
	
	
	public void setExtenstion(String extension) {
		this.extension = extension;
	}
	public Workbook createWorkbook(FileInputStream fs) throws IOException {
		switch(extension) {
			case "xls" : return new HSSFWorkbook(fs);
			
			case "xlsx" : return new XSSFWorkbook(fs);
						  
			default: return null;
		}
	}
}
