package ecconverter;

import manager.WorkManager;
public class Main {
	
	static WorkManager wm = new WorkManager();
	
	public static void main(String[] args) {
		try{
			wm.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}		
}