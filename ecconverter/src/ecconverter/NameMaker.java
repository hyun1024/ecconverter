package ecconverter;


public class NameMaker {

	public String getTargetDirPath() {
		return Config.TARGET_PATH+"\\";
	}
	public String createXlsName(String filename) {
		return Config.RESULT_PATH+"\\"+filename+".xls";
	}
	public String createXlsxName(String filename) {
		return Config.RESULT_PATH+"\\"+filename+".xlsx";
	}
	public String createCsvName(String filename) {
		return Config.RESULT_PATH+"\\"+filename+".csv";
	}
	public String readCsvName(String filename) {
		return Config.TARGET_PATH+"\\"+filename+".csv";
	}
	public String readXlsName(String filename) {
		return Config.TARGET_PATH+"\\"+filename+".xls";
	}
	public String readXlsxName(String filename) {
		return Config.TARGET_PATH+"\\"+filename+".xlsx";
	}
	public String createNumberingXlsxName(String filename, int count) {
		return Config.RESULT_PATH+"\\"+filename+"_"+count+".xlsx";
	}
}
