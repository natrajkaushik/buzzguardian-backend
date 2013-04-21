package guardian;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the Pending Queue which contain the panic messages during their timeout 
 */
public class ProcessingQueue implements SMSTransactionQueue{
	public static final int PENDING_TIMEOUT = 35000;
	public static final int TRACKING_TIMEOUT = 1800000;
	public static final ThreadMap THREAD_MAP = new ThreadMap();
	
	private Map<String, SMSData> queue;
	private QueueType queueType;
	private int timeout;
	
	public ProcessingQueue(QueueType queueType) {
		this.queueType = queueType;
		queue = new HashMap<String, SMSData>();
		this.timeout = queueType == QueueType.PENDING ? PENDING_TIMEOUT : TRACKING_TIMEOUT;
	}
	
	/**
	 * @param fromNumber
	 * @return true if SMSData present in queue
	 */
	public synchronized boolean isPresent(String fromNumber){
		return queue.containsKey(fromNumber);
	}
	
	/**
	 * Add an SMSData to synchronized queue
	 * @param data: SMSData object to add
	 */
	public synchronized void addSMS(final SMSData smsData){
		// if the queue does not already contain the number from which the SMS is originating
		if(!isPresent(smsData.getFromNumber())){
			queue.put(smsData.getFromNumber(), smsData);
			
			// start a waiting thread to wait for timeout
			WaitingThread task = new WaitingThread(smsData.getFromNumber(), new Thread() {
				
				@Override
				public void run() {
					try {
						Thread.sleep(timeout);
					} catch (InterruptedException e) {
						THREAD_MAP.removeWaitingThread(smsData.getFromNumber());
						return;
					}
					/* Sandeep - If the waiting thread has timed out then move SMSData from PENDING to PROCESSED */
					LogToDB log = new LogToDB();
					smsData.setState(guardian.State.PROCESSED);
					log.logToSMSLog (smsData);
					
					/* Send SMS to the Police with the userInfo(MobileNo, FirstName, LastName, EmailID) and Location derived from SMSData */
					UserInfo userInfo = new UserInfo();
					userInfo = log.queryUserInfo(smsData);
					System.out.println("FirstName: " + userInfo.firstName);
				}
			});
			task.start();
			
			//track the waiting thread through the THREAD_MAP
			THREAD_MAP.addWaitingThread(task);
		}
	}

	/**
	 * @param fromNumber
	 * @return remove and return SMSData from queue
	 */
	public synchronized SMSData removeSMS(String fromNumber) {
		if(isPresent(fromNumber)){
			// interrupt the waiting thread
			WaitingThread task = THREAD_MAP.getWaitingThread(fromNumber);
			task.getTask().interrupt();
			
			return queue.remove(fromNumber);
		}
		else{
			return null;
		}
	}

	/**
	 * update an SMSData object already in queue
	 * @param data: new SMSData object
	 */
	public synchronized void updateSMS(SMSData data) {
		if(isPresent(data.getFromNumber())){
			queue.put(data.getFromNumber(), data);
		}
	}
	
	
	/* --------------------------------------- TESTING --------------------------------------- */
	
	public static void main(String[] args) {
		ProcessingQueue processingQueue = new ProcessingQueue(QueueType.PENDING);
		processingQueue.addSMS(null);
	}

}

