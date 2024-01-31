package ecconverter;

public class ConverterManager {
	
	ListManager lm;
	NameMaker nm;
	FileScanner fs;
	ExcelToCsvConverter ec;
	CsvToExcelConverter cc;
	
	
	public ConverterManager(ListManager lm, FileScanner fs, NameMaker nm) {
		this.lm=lm;
		this.fs=fs;
		this.nm=nm;
	}
	
	
	public Converter createConverter(String convertTarget) {
		fs.findResultPath();
		switch(convertTarget) {
		case "csv" : return new CsvToExcelConverter(lm, fs, nm);
		case "excel" : return new ExcelToCsvConverter(lm, nm);
		default: return null;
		}
	}
}
