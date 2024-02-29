package manager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.io.FilenameUtils;

public class ListManager {
	//작업대상 file목록
	Queue<String>targetList;
	Map<String, String> extensionList;

	//작업목록 ex)csv->excel
	Map<Integer, String> workList;

	public void initialFileList() {
		targetList=new LinkedList<>();
		extensionList=new HashMap<>();
	}
	public void initialWorkList() {
		workList=new HashMap<>();
	}
	public Queue<String> getTargetList(){
		return targetList;
	}
	public Map<Integer, String> getWorkList(){
		return workList;
	}
	public Map<String, String> getExtensionList(){
		return extensionList;
	}

	public void makeWorkLists(String[] fileList, String target) {
		for(String name: fileList) {
			if(FilenameUtils.getExtension(name).contains(target)) {
				targetList.add(FilenameUtils.getBaseName(name));
				extensionList.put(FilenameUtils.getBaseName(name), FilenameUtils.getExtension(name));
			}
		}
	}
	public String getExtension(String filename) {
		return extensionList.get(filename);
	}
	public String nextTarget() {
		return targetList.poll();
	}
}
