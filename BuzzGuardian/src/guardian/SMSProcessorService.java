package guardian;

import java.util.ArrayList;

public class SMSProcessorService implements Runnable{

	private ArrayList<String> list;
	
	
	public SMSProcessorService(ArrayList<String> list) {
		super();
		this.list = list;
	}

	@Override
	public void run() {
		 // Do your job here.
    	System.out.println("Hello!");
    	list.add(String.valueOf(Math.random()));
    	System.out.println("Size of list : " +  list.size());
	}

}
