package manager;

import converter.Converter;
import converter.CsvToExcelConverter;
import converter.ExcelToCsvConverter;
import worker.FileScanner;
import worker.NameMaker;

public class ConverterManager {
	
	ListManager lm;
	NameMaker nm;
	FileScanner fs;
	ExcelToCsvConverter ec;
	CsvToExcelConverter cc;
	ExcelManager em;
	
	
	public ConverterManager(ListManager lm, FileScanner fs, NameMaker nm, ExcelManager em) {
		this.lm=lm;
		this.fs=fs;
		this.nm=nm;
		this.em=em;
	}
	
	
	public Converter createConverter(String convertTarget) {
		fs.findResultPath();
		switch(convertTarget) {
		case "csv" : return new CsvToExcelConverter(lm, fs, nm, em);
		case "xls" : return new ExcelToCsvConverter(lm, nm, em);
		default: return null;
		}
	}
}
