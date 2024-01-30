package ecconverter;


public class NameMaker {

	public String getTargetDirPath() {
		return Config.BASIC_PATH+"\\";
	}
	public String createXlsName(String filename) {
		return Config.BASIC_PATH+"\\"+filename+".xls";
	}
	public String createXlsxName(String filename) {
		return Config.BASIC_PATH+"\\"+filename+".xlsx";
	}
	public String createCsvName(String filename) {
		return Config.BASIC_PATH+"\\"+filename+".csv";
	}
	public String createNumberingXlsxName(String filename, int count) {
		return Config.BASIC_PATH+"\\"+filename+"_"+count+".xlsx";
	}
}
