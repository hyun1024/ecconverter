package ecconverter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class ListManager {
	//excel->csv 작업 시 필요한 리스트
	Queue<String>xlsList;
	Queue<String>xlsxList;
	
	
	//csv->excel 작업 시 필요한 리스트
	Queue<String>csvList;
	Map<Integer, String> workList;

	public void initialExcelList() {
		xlsList = new LinkedList<>();
		xlsxList = new LinkedList<>();
	}
	public void initialCsvList() {
		csvList = new LinkedList<>();
	}
	public void initialWorkList() {
		workList = new HashMap<>();
	}
	public Queue<String> getXlsList() {
		return xlsList;
	}
	public Queue<String> getXlsxList(){
		return xlsxList;
	}
	public Queue<String> getCsvList(){
		return csvList;
	}
	public Map<Integer, String> getWorkList(){
		return workList;
	}
	public int getExcelListSize() {
		return xlsList.size() + xlsxList.size();
	}
	
	public void makeExcelWorkList(String[] fileList) {
		for(String name: fileList) {
			if(name.contains(".xls") && !name.contains(".xlsx")) {
				name = name.replaceAll(".xls", "");
				xlsList.add(name);
			}
			if(name.contains(".xlsx")) {
				name = name.replaceAll(".xlsx", "");
				xlsxList.add(name);
			}
		}
	}
	public void makeCsvWorkList(String[] fileList) {
		for(String name: fileList) {
			if(name.contains(".csv")) {
				name=name.replaceAll(".csv", "");
				csvList.add(name);
			}
		}
	}
}
