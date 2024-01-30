package ecconverter;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelToCsvConverter implements Converter {

	ListManager lm;
	NameMaker nm;
	CellReader cellReader = new CellReader();
	
	public ExcelToCsvConverter(ListManager lm, NameMaker nm) {
		this.lm=lm;
		this.nm=nm;
	}
	public void convert(String type, String filename) throws IOException {
		if(type.equals("xls")) {
			xlsToCsv(filename);
		} else {
			xlsxToCsv(filename);
		}
	}
	public void xlsToCsv(String filename) throws IOException {
		String TARGET_PATH = nm.createXlsName(filename);
		String CREATE_PATH = nm.createCsvName(filename);

		FileInputStream target= new FileInputStream(TARGET_PATH);
		HSSFWorkbook workbook = new HSSFWorkbook(target);
		HSSFSheet sheet= workbook.getSheetAt(0);
		BufferedWriter bw= new BufferedWriter(new FileWriterWithEncoding(CREATE_PATH, Config.ENCODING_NAME));
		for(int i=sheet.getFirstRowNum(); i<=sheet.getLastRowNum(); i++) {
			HSSFRow row = sheet.getRow(i);
			for(int j=row.getFirstCellNum(); j<row.getLastCellNum(); j++) {
				if(j!=row.getFirstCellNum()) {
					bw.write(",");
				}
				HSSFCell cell = row.getCell(j);
				String cellValue=cellReader.read(cell);
				Boolean escape=false;
				if(cellValue.contains(",") || cellValue.contains("\"")) {
					cellValue = cellValue.replaceAll("\"", "\"\"");
					bw.write("\"");
					escape=true;
				}
				bw.write(cellValue);
				if(escape) {
					bw.write("\"");
					escape=false;
				}
			}	
			bw.newLine();
		}
		 bw.flush();
		 bw.close();
		 workbook.close();
		 target.close();
	}
	public void xlsxToCsv(String filename) throws IOException {
		String TARGET_PATH = nm.createXlsxName(filename);
		String CREATE_PATH = nm.createCsvName(filename);
		FileInputStream target= new FileInputStream(TARGET_PATH);
		XSSFWorkbook workbook = new XSSFWorkbook(target);
		XSSFSheet sheet= workbook.getSheetAt(0);
		BufferedWriter bw= new BufferedWriter(new FileWriterWithEncoding(CREATE_PATH, Config.ENCODING_NAME));
		for(int i=sheet.getFirstRowNum(); i<=sheet.getLastRowNum(); i++) {
			XSSFRow row = sheet.getRow(i);
			for(int j=row.getFirstCellNum(); j<row.getLastCellNum(); j++) {
				if(j!=row.getFirstCellNum()) {
					bw.write(",");
				}
				XSSFCell cell = row.getCell(j);
				String cellValue=cellReader.read(cell);
				Boolean escape=false;
				if(cellValue.contains(",") || cellValue.contains("\"")) {
					cellValue = cellValue.replaceAll("\"", "\"\"");
					bw.write("\"");
					escape=true;
				}
				bw.write(cellValue);
				if(escape) {
					bw.write("\"");
					escape=false;
				}
			}	
			bw.newLine();
		}
		 bw.flush();
		 bw.close();
		 workbook.close();
		 target.close();
	}
	
	
	public class CellReader{
		public String read(HSSFCell cell) {
			String value = "";
			CellType ct = cell.getCellType();
			if(ct != null) {
				switch(cell.getCellType()) {
				case FORMULA:
					value = cell.getCellFormula();
					break;
				case NUMERIC:
					if(Math.rint(cell.getNumericCellValue()) == cell.getNumericCellValue()){
						value=(int)cell.getNumericCellValue()+"";
					} else {
						value=cell.getNumericCellValue()+"";
					}
				    break;
				case STRING:
				    value=cell.getStringCellValue()+"";
				    break;
				case BOOLEAN:
				    value=cell.getBooleanCellValue()+"";
				    break;
				case ERROR:
				    value=cell.getErrorCellValue()+"";
				    break;
				default:
					break;
				}
			}
			return value; 
		}
		public String read(XSSFCell cell) {
			String value = "";
			CellType ct = cell.getCellType();
			if(ct != null) {
				switch(cell.getCellType()) {
				case FORMULA:
					value = cell.getCellFormula();
					break;
				case NUMERIC:
					if(Math.rint(cell.getNumericCellValue()) == cell.getNumericCellValue()){
						value=(int)cell.getNumericCellValue()+"";
					} else {
						value=cell.getNumericCellValue()+"";
					}
				    break;
				case STRING:
				    value=cell.getStringCellValue()+"";
				    break;
				case BOOLEAN:
				    value=cell.getBooleanCellValue()+"";
				    break;
				case ERROR:
				    value=cell.getErrorCellValue()+"";
				    break;
				default:
					break;
				}
			}
			return value; 
		}
	}
}
