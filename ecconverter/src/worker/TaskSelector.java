package worker;

import java.util.*;

import manager.ListManager;

public class TaskSelector {
	Scanner scanner = new Scanner(System.in);
	
	ListManager lm;
	Map<Integer, String> workList;
	int taskNum=-1;
	String target;
	public TaskSelector(ListManager lm) {
		lm.initialWorkList();
		this.workList = lm.getWorkList();
		setInitialTaskList();
	}
	//TODO 나중에 분리해서 동적 생성
	public void setInitialTaskList() {
		workList.put(1, "csv->excel");
		workList.put(2, "excel->csv");
		workList.put(0, "exit");
	}
	public String setTask() {
		StringBuilder sb = new StringBuilder();
		System.out.println("필요한 작업 번호를 입력해주세요 ex) 1");
		for(Map.Entry<Integer, String> entry : workList.entrySet()) {
			sb.append(entry.getKey()+". "+entry.getValue()).append("\n");
		}
		System.out.println(sb.toString());
		//올바르지 않은 입력에 대해 escape후 반복문을 통해 재입력 요청
		while(true) {
			try {
				taskNum=scanner.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("입력값이 숫자가 아닙니다. 다시 확인해주세요.");
				scanner.nextLine();
				continue;
			}
			//작업 번호일 시 해당 작업번호 value 반환
			if(workList.get(taskNum)!=null) {
				switch(taskNum) {
				case 1: return "csv";
				case 2: return "xls";
				}
			}
			System.out.println("유효한 작업 번호가 아닙니다. 다시 확인해주세요.");
		}
	}
	public void resetTaskNum() {
		taskNum=-1;
	}
}
