package manager;

import converter.Converter;
import converter.CsvToExcelConverter;
import converter.ExcelToCsvConverter;
import worker.FileScanner;
import worker.NameMaker;

public class ConverterManager {
	
	NameMaker nm;
	FileScanner fs;
	Converter converter;
	ExcelManager em;
	
	
	public ConverterManager(FileScanner fs, NameMaker nm, ExcelManager em) {
		this.fs=fs;
		this.nm=nm;
		this.em=em;
	}
	
	
	public Converter createConverter(String convertTarget) {
		switch(convertTarget) {
		case "csv" : return new CsvToExcelConverter(fs, nm, em);
		case "xls" : return new ExcelToCsvConverter(nm, em);
		default: return null;
		}
	}
}
