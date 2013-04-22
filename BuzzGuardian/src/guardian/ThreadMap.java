package guardian;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains a mapping between Waiting Threads and SMSData objects
 */
public class ThreadMap {

	private Map<String, WaitingThread> threadMap;
	
	public ThreadMap() {
		threadMap = new HashMap<String, WaitingThread>();
	}
	
	/**
	 * @param id: fromNumber+requestType.toString()
	 * @return true is ThreadMap contains a WaitingThread for the given SMSData object
	 */
	public synchronized boolean contains(String id){
		return threadMap.containsKey(id);
	}
	
	/**
	 * @param id: fromNumber of SMSData object 
	 * @return WorkerThread that is processing the given SMSData object
	 */
	public synchronized WaitingThread getWaitingThread(String id){
		if(contains(id)){
			return threadMap.get(id);
		}
		else{
			return null;
		}
	}
	
	/**
	 * adds a WaitingThread to the Map
	 * @param task
	 */
	public synchronized void addWaitingThread(WaitingThread task){
		if(!contains(task.getId())){
			threadMap.put(task.getId(), task);
		}
	}
	
	/**
	 * removes a WaitingThread from the Map
	 * @param id
	 * @return
	 */
	public synchronized WaitingThread removeWaitingThread(String id){
		if(contains(id)){
			return threadMap.remove(id);
		}
		return null;
	}

}
