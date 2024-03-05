package converter;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import config.Config;
import manager.ExcelManager;
import manager.ListManager;
import worker.NameMaker;

public class ExcelToCsvConverter implements Converter {

	NameMaker nm;
	ExcelManager em;
	CellReader cellReader = new CellReader();
	static String RESULT_EXTENSION = "csv";
	
	public ExcelToCsvConverter(NameMaker nm, ExcelManager em) {
		this.nm=nm;
		this.em=em;
	}
	
	public void convert(String filename) throws IOException{
		String TARGET_PATH = nm.createReadPath(filename);
		String RESULT_PATH = nm.createResultPath(filename, RESULT_EXTENSION);
		
		FileInputStream target= new FileInputStream(TARGET_PATH);
		Workbook workbook = em.createWorkbook(target, FilenameUtils.getExtension(filename));
		Sheet sheet = workbook.getSheetAt(0);
		BufferedWriter bw= new BufferedWriter(new FileWriterWithEncoding(RESULT_PATH, Config.ENCODING_NAME));
		Boolean escape;
		for(int i=sheet.getFirstRowNum(); i<=sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			for(int j=row.getFirstCellNum(); j<row.getLastCellNum(); j++) {
				if(j!=row.getFirstCellNum()) {
					bw.write(",");
				}
				Cell cell = row.getCell(j);
				String cellValue=cellReader.read(cell);
				escape=false;
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
		public String read(Cell cell) {
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
