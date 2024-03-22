package converter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import config.Config;
import manager.ExcelManager;
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
	
	public void convert(String filename) throws Exception {
		if(FilenameUtils.getExtension(filename).equals("xls")) {
			xls(filename);
			return;
		}
		IOUtils.setByteArrayMaxOverride(Integer.MAX_VALUE);
		SheetHandler excelHandler = new SheetHandler();
		String targetPath = nm.createReadPath(filename);
		File file = new File(targetPath);
		FileInputStream input = new FileInputStream(file); 
		OPCPackage opc = OPCPackage.open(input);
		XSSFReader xssfReader = new XSSFReader(opc);
        StylesTable styles = xssfReader.getStylesTable();
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(opc);
        InputStream sheetStream = xssfReader.getSheetsData().next();
		InputSource inputSource = new InputSource(sheetStream);
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		saxParserFactory.setNamespaceAware(true);
		SAXParser parser = saxParserFactory.newSAXParser();
		XMLReader xmlReader = parser.getXMLReader();
        DataFormatter dataFormatter = new DataFormatter();
        dataFormatter.addFormat("General", new java.text.DecimalFormat("#.###############"));
        ContentHandler handle = new XSSFSheetXMLHandler(styles, strings, excelHandler, false);
		xmlReader.setContentHandler(handle);
		xmlReader.parse(inputSource);
		sheetStream.close();
		BufferedWriter bw= new BufferedWriter(new FileWriterWithEncoding(nm.createResultPath(filename, RESULT_EXTENSION), Config.ENCODING_NAME));
		opc.close();
		Boolean escape;
		List<String> header = excelHandler.getHeader();
		for (int i = 0; i < header.size(); i++) {
			if(i!=0) {
				bw.write(",");
			}
			escape=false;
			if(header.get(i)==null) {
				continue;
			}
			String cellValue=header.get(i);
			if(cellValue.contains(",") || cellValue.contains("\"")){
				cellValue = new String(cellValue.replaceAll("\"", "\"\""));
				bw.write("\"");
				escape=true;
			}
			bw.write(cellValue);
			if(escape) {
				bw.write("\"");
				escape=false;
			}
		}
		bw.write("\r\n");
		for (List<String> row : excelHandler.getRows()) {
			
			for (int i = 0; i < row.size(); i++) {
				if(i!=0) {
					bw.write(",");
				}
				escape=false;
				if(row.get(i)==null) {
					continue;
				}
				String cellValue=row.get(i);
				if(cellValue.contains(",") || cellValue.contains("\"")){
					cellValue = new String(cellValue.replaceAll("\"", "\"\""));
					bw.write("\"");
					escape=true;
				}
				bw.write(cellValue);
				if(escape) {
					bw.write("\"");
					escape=false;
				}
			}
			bw.write("\r\n");
		}
		bw.flush();
		bw.close();
	}
	public void xls(String filename) throws IOException{

		String TARGET_PATH = nm.createReadPath(filename);
		String RESULT_PATH = nm.createResultPath(filename, RESULT_EXTENSION);
		FileInputStream target= new FileInputStream(TARGET_PATH);
		IOUtils.setByteArrayMaxOverride(Integer.MAX_VALUE);
		Workbook workbook = em.createWorkbook(target, FilenameUtils.getExtension(filename));
		Sheet sheet = workbook.getSheetAt(0);
		File file = new File(RESULT_PATH);
		if(file.exists()) {
			file.delete();
		}

		Boolean escape;
		int rowNum=sheet.getFirstRowNum();
		boolean cont=true;
		BufferedWriter bw= new BufferedWriter(new FileWriterWithEncoding(RESULT_PATH, Config.ENCODING_NAME));
		
		Row row;
		Cell cell;
		for(int i=sheet.getFirstRowNum(); i<=sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i);
			for(int j=row.getFirstCellNum(); j<row.getLastCellNum(); j++) {
				if(j!=row.getFirstCellNum()) {
					bw.write(",");
				}
				cell = row.getCell(j);
				String cellValue=new String(cellReader.read(cell));
				escape=false;
				if(cellValue.contains(",") || cellValue.contains("\"")){
					cellValue = new String(cellValue.replaceAll("\"", "\"\""));
					bw.write("\"");
					escape=true;
				}
				bw.write(cellValue);
				if(escape) {
					bw.write("\"");
					escape=false;
				}
			}
			bw.write("\r\n");
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
		public Object reads(Cell cell, CellType type) {
			if(type != null) {
				switch(type) {
				case FORMULA:
					return cell.getCellFormula();
				case NUMERIC:
					return cell.getNumericCellValue();
				case STRING:
				    return cell.getStringCellValue();
				case BOOLEAN:
				    return cell.getBooleanCellValue();
				case ERROR:
				    return cell.getErrorCellValue();
				default:
					return null;
				}
			}
			return null;
		}
	}
}
