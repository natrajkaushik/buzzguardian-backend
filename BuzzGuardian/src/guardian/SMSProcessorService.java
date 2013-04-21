package guardian;

import java.util.ArrayList;
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
			List<SMSData> smsList = GoogleVoice.getUnreadSMS();
			System.out.println("Number of unread messages: " + smsList.size());
			
			for(SMSData sms : smsList){
				System.out.println(sms.getFromNumber() + "\n" + sms.getMessage() + "\n" + sms.getTimestamp());
				
				parseSMS(sms);				
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
		double latitude = Double.parseDouble(eachSMS[6]);
		double longitude = Double.parseDouble(eachSMS[8]);
		switch (request) {
			case "HELP": sendAlert(sms, latitude, longitude);
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
		
		// Add to Pending Queue to begin a new thread that waits for Cancel Request
		pendingQueue.addSMS(newSMS);
	}
	
	private void cancelAlert(SMSData sms, double latitude, double longitude) {
		SMSData newSMS = new SMSData (sms.getFromNumber(), sms.getMessage(), sms.getTimestamp(), RequestType.CANCEL_ALERT, State.TRACKING, latitude, longitude);
		// log SMS in the Database - SMSLog Table
		LogToDB log = new LogToDB();
		log.logToSMSLog (newSMS);
		
		// Remove from Pending Queue to send interrupt to the pending thread and cancel the request.
		pendingQueue.removeSMS(newSMS.getFromNumber());
	}

	private void trackPhone(SMSData sms, double latitude, double longitude) {
		SMSData newSMS = new SMSData (sms.getFromNumber(), sms.getMessage(), sms.getTimestamp(), RequestType.TRACK, State.TRACKING, latitude, longitude);
		// log SMS in the Database - TrackingSMS Table
		LogToDB log = new LogToDB();
		log.logToTrackingSMS (newSMS);
		
		// Add to Tracking Queue to keep track of the phone.
		trackingQueue.addSMS(newSMS);
	}
}
