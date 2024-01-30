package ecconverter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Queue;

public class FileScanner {
	
	public String[] readFiles() {
		File file = new File(Config.BASIC_PATH+"\\");
		return file.list();
	}
	public void makeList(String targetPath, Queue<String[]> lineList) throws IOException {
		FileInputStream target= new FileInputStream(targetPath);
		BufferedReader br = new BufferedReader(new InputStreamReader(target, Config.ENCODING_NAME));
        String line;
        while((line = br.readLine())!=null) {
            String[] lineContents = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",-1);

            lineList.add(lineContents);
        }	
		target.close();
		br.close();
	}
}
