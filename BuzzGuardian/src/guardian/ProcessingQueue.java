package guardian;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the Pending Queue which contain the panic messages during their timeout 
 */
public class ProcessingQueue implements SMSTransactionQueue{
	public static final int PENDING_TIMEOUT = 35000;
	public static final int TRACKING_TIMEOUT = 1800000;
	public static final int USER_TIMEOUT = 3600000;
	public static final String CLEANUP = "CLEANUP";
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
	public synchronized boolean addSMS(final SMSData smsData){
		// if the queue does not already contain the number from which the SMS is originating
		if(!isPresent(smsData.getFromNumber())){
			queue.put(smsData.getFromNumber(), smsData);
			
			// start a waiting thread to wait for timeout
			WaitingThread task1 = new WaitingThread(smsData.getFromNumber()+queueType.toString(), new Thread() {
				
				@Override
				public void run() {
					try {
						Thread.sleep(timeout);
					} catch (InterruptedException e) {
						THREAD_MAP.removeWaitingThread(smsData.getFromNumber()+queueType.toString());
						if (THREAD_MAP.contains(smsData.getFromNumber()+CLEANUP)) {
							WaitingThread task = THREAD_MAP.getWaitingThread(smsData.getFromNumber()+CLEANUP);
							task.getTask().interrupt();
						}
						Thread.currentThread().interrupt();
						return;
					}
					
					if (queueType.equals(QueueType.PENDING)) {
						/* Sandeep - If the waiting thread has timed out then move SMSData from PENDING to PROCESSED */
						LogToDB log = new LogToDB();
						smsData.setState(guardian.State.PROCESSED);
						log.logToSMSLog (smsData);
						
						/* Send SMS to the Police with the userInfo(MobileNo, FirstName, LastName, EmailID) and Location derived from SMSData */
						UserInfo userInfo = new UserInfo();
						userInfo = log.queryUserInfo(smsData);
						System.out.println("FirstName: " + userInfo.firstName);
						System.out.println("LastName: " + userInfo.lastName);
						System.out.println("MobileNo: " + userInfo.mobileNo);
						System.out.println("EmailID: " + userInfo.emailID);
						double lat = Math.round(smsData.getLatitude() * 10000.0 ) / 10000.0;
						double lon = Math.round(smsData.getLongitude() * 10000.0 ) / 10000.0;
						guardian.GetAddress ga = new GetAddress(smsData.getLatitude(),smsData.getLongitude());
						String address = null;
						try {
							address = ga.getAddressFromLatLang();
						} catch (IOException e) {
							e.printStackTrace();
						}
						// String textToPolice = userInfo.firstName.substring(0, 1).toUpperCase() + userInfo.firstName.substring(1) + " " + userInfo.lastName.substring(0, 1).toUpperCase() + userInfo.lastName.substring(1) + " needs HELP at Latitude: " + lat + " Longitude: " + lon + ". MobileNo: " + userInfo.mobileNo;
						String textToPolice = userInfo.firstName.substring(0, 1).toUpperCase() + userInfo.firstName.substring(1) + " " + userInfo.lastName.substring(0, 1).toUpperCase() + userInfo.lastName.substring(1) + " needs HELP at " + address + ". MobileNo: " + userInfo.mobileNo;
						String policeNumber = PoliceContactHelper.getPoliceNumber(smsData.getLatitude(), smsData.getLongitude());
						System.out.println("Police Number = " + policeNumber);
						GoogleVoice.sendSMS(policeNumber, textToPolice);
						long smsTime = smsData.getTimestamp().getTime();
						long policeTime = System.currentTimeMillis();
						System.out.println("SMS Time = " + smsTime);
						System.out.println("Current Time = " + policeTime);
						System.out.println("Time difference = " + (policeTime - smsTime)/(1000));
						log.logToStats(smsTime, policeTime, smsData.getRequestType().toString());						
						
						THREAD_MAP.removeWaitingThread(smsData.getFromNumber()+queueType.toString());
						
						// For Testing purposes: clear all queues and threads related to this SMS
						//SMSProcessorService.
						return;
					} else {
						queue.remove(smsData.getFromNumber());
						THREAD_MAP.removeWaitingThread(smsData.getFromNumber()+queueType.toString());
						return;
					}
					
				}
			});
			task1.start();
			
			//track the waiting thread through the THREAD_MAP
			THREAD_MAP.addWaitingThread(task1);
			
			if (queueType.equals(queueType.PENDING)) {
				WaitingThread task2 = new WaitingThread(smsData.getFromNumber()+CLEANUP, new Thread() {
					
					@Override
					public void run() {
						try {
								Thread.sleep(USER_TIMEOUT);
						} catch (InterruptedException e) {
							THREAD_MAP.removeWaitingThread(smsData.getFromNumber()+CLEANUP);
							Thread.currentThread().interrupt();
							return;
						}
						
						queue.remove(smsData.getFromNumber());
						THREAD_MAP.removeWaitingThread(smsData.getFromNumber()+CLEANUP);
						return;
					}
				});
				task2.start();
				
				//track the waiting thread through the THREAD_MAP
				THREAD_MAP.addWaitingThread(task2);
			}
			
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param fromNumber
	 * @return remove and return SMSData from queue
	 */
	public synchronized SMSData removeSMS(String fromNumber) {
		if(isPresent(fromNumber)){
			// interrupt the waiting thread
			if (THREAD_MAP.contains(fromNumber+queueType.toString())) {
				WaitingThread task = THREAD_MAP.getWaitingThread(fromNumber+queueType.toString());
				task.getTask().interrupt();
			}
			
			return queue.remove(fromNumber);
		}
		else{
			return null;
		}
	}

	/**
	 * update an SMSData object already in queue and return true
	 * @param data: new SMSData object
	 */
	public synchronized boolean updateSMS(SMSData data) {
		if(isPresent(data.getFromNumber())){
			queue.put(data.getFromNumber(), data);
			return true;
		} else {
			return false;
		}		
	}
	
	
	
	/* --------------------------------------- TESTING --------------------------------------- */
	
	public static void main(String[] args) {
		ProcessingQueue processingQueue = new ProcessingQueue(QueueType.PENDING);
		processingQueue.addSMS(null);
	}

}

