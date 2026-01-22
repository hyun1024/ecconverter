package config;

public class Config {
	//System settings
	public static String BASIC_PATH = System.getProperty("user.dir");
	public static String TARGET_PATH = BASIC_PATH + "\\targetfiles";
	public static String RESULT_PATH = BASIC_PATH + "\\resultfiles";
	public static String CONFIG_PATH = BASIC_PATH + "\\config";
	public static String WIDTH_CONFIG_PATH = CONFIG_PATH + "\\width.conf";
	public static String DELIMITER_CONFIG_PATH = CONFIG_PATH + "\\delimiter.conf";
	
	
	public static String READ_ENCODING_NAME = "UTF-8";
	public static String WRITE_ENCODING_NAME = "UTF-8";
	
	
	
	//Values for excel(style,max row, etc..)
	public static String FONT_NAME = "맑은 고딕";
	public static int MAX_ROW_COUNT = 100000;
	public static short FONT_SIZE = 11;
	public static float ROW_HEIGHT = 16.5F;

	
}
