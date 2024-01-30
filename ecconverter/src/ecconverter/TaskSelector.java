package ecconverter;

import java.util.*;

public class TaskSelector {
	Scanner scanner = new Scanner(System.in);
	
	ListManager lm;
	Map<Integer, String> workList;
	int taskNum=-1;
	
	public TaskSelector(ListManager lm) {
		lm.initialWorkList();
		this.workList = lm.getWorkList();
		setInitialTaskList();
	}
	//TODO 나중에 분리해서 동적 생성
	public void setInitialTaskList() {
		workList.put(1, "csv->excel");
		workList.put(2, "excel->csv");
	}
	
	public int getTaskNum() {
		return taskNum;
	}
	public void setTask() {
		System.out.println("필요한 작업 번호를 입력해주세요 ex) 1");
		for(Map.Entry<Integer, String> entry : workList.entrySet()) {
			System.out.println(entry.getKey()+". "+entry.getValue());
		}
		//올바르지 않은 입력에 대해 escape후 반복문을 통해 재입력 요청
		while(true) {
			try {
				taskNum=scanner.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("입력값이 숫자가 아닙니다. 다시 확인해주세요.");
				scanner.close();
				scanner=new Scanner(System.in);
				continue;
			}
			//작업 번호일 시 해당 작업번호 key 반환
			if(workList.get(taskNum)!=null) {
				break;
			}
			System.out.println("유효한 작업 번호가 아닙니다. 다시 확인해주세요.");
		}
	}
}
