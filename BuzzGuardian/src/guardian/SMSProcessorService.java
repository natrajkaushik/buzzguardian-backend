package guardian;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SMSProcessorService implements Runnable{

	private ArrayList<String> list;
	private ProcessingQueue pendingQueue;
	private ProcessingQueue trackingQueue;
	
	
	public SMSProcessorService(ArrayList<String> list) {
		super();
		this.list = list;
		this.pendingQueue = new ProcessingQueue(QueueType.PENDING);
		this.trackingQueue = new ProcessingQueue(QueueType.TRACKING);
	}

	@Override
	public void run() {
		// Read unread SMS using GoogleVoice into smsList.
		try {
			long startTime = System.currentTimeMillis();;			
			List<SMSData> smsList = GoogleVoice.getUnreadSMS();
			long midTime = (System.currentTimeMillis() - startTime);
			System.out.println("Number of unread messages: " + smsList.size());
			System.out.println("Time taken to read the SMS: " + midTime);
			
			Collections.sort(smsList, new Comparator<SMSData>() {

				@Override
				public int compare(SMSData o1, SMSData o2) {
					return (int)(o1.getTimestamp().getTime() - o2.getTimestamp().getTime());
				}
			});
			
			for(SMSData sms : smsList){
				System.out.println(sms.getFromNumber() + "\n" + sms.getMessage() + "\n" + sms.getTimestamp());
				
				parseSMS(sms);	
				
				System.out.println("Time to Parse each unread SMS = " + (System.currentTimeMillis() - startTime));
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
    	list.add(String.valueOf(Math.random()));
    	System.out.println("Iteration : " +  list.size());
	}
	
	private void parseSMS (SMSData sms) {
		String eachSMS[] = sms.getMessage().split(" ");
		String request = eachSMS[3];
		double latitude = Double.parseDouble(eachSMS[5]);
		double longitude = Double.parseDouble(eachSMS[7]);
		switch (request) {
			case "EMERGENCY": sendAlert(sms, latitude, longitude);
						 break;
			case "CANCEL": cancelAlert(sms, latitude, longitude);
						   break;
			case "TRACKING": trackPhone(sms, latitude, longitude);
						  break;
			default: break;
		}
	}
	
	private void sendAlert (SMSData sms, double latitude, double longitude) {
		SMSData newSMS = new SMSData(sms.getFromNumber(), sms.getMessage(), sms.getTimestamp(), RequestType.SEND_ALERT, latitude, longitude);
		
		// log SMS in the Database - SMSLog Table
		LogToDB log = new LogToDB();
		log.logToSMSLog (newSMS);
		
		// Add to Pending Queue to begin a new thread that waits for Cancel Request.
		if (pendingQueue.addSMS(newSMS)) {
			// Add to Tracing Queue to begin a new thread that waits for 30mins while the phone is being tracked.
			trackingQueue.addSMS(newSMS);
		}	
		
	}
	
	private void cancelAlert(SMSData sms, double latitude, double longitude) {
		SMSData newSMS = new SMSData (sms.getFromNumber(), sms.getMessage(), sms.getTimestamp(), RequestType.CANCEL_ALERT, State.PROCESSED, latitude, longitude);
				
		// Remove from Pending Queue to send interrupt to the pending thread and cancel the request.
		pendingQueue.removeSMS(newSMS.getFromNumber());
		
		// Remove from Tracking Queue to send interrupt to the pending thread and cancel the request.
		trackingQueue.removeSMS(newSMS.getFromNumber());
				
		
		// log SMS in the Database - SMSLog Table
		LogToDB log = new LogToDB();
		log.logToSMSLog (newSMS);
	}

	private void trackPhone(SMSData sms, double latitude, double longitude) {
		SMSData newSMS = new SMSData (sms.getFromNumber(), sms.getMessage(), sms.getTimestamp(), RequestType.TRACK, State.TRACKING, latitude, longitude);
				
		// Add to Tracking Queue to keep track of the phone and log SMS in the Database - TrackingSMS Table
		if (trackingQueue.updateSMS(newSMS)) {
			LogToDB log = new LogToDB();
			log.logToTrackingSMS (newSMS);
		}
	}
}
