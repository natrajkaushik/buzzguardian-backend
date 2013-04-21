package guardian;

/**
 * Wrapper object which contains a Runnable task and id which corresponds to the SMS which the task is processing
 */
// Shouldn't WaitingThread extend Thread?
public class WaitingThread {

	String id;
	Thread task;
	
	public WaitingThread(String id, Thread task) {
		super();
		this.id = id;
		this.task = task;
	}
	
	/**
	 * starts the task
	 */
	public void start(){
		task.start();
	}
	
	public String getId() {
		return id;
	}
	
	public Thread getTask() {
		return task;
	}

}
