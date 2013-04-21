package guardian;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import org.dom4j.DocumentException;

import com.techventus.server.voice.Voice;
import com.techventus.server.voice.datatypes.records.SMS;
import com.techventus.server.voice.datatypes.records.SMSThread;

/**
 * Google Voice API Services
 */
public class GoogleVoice {

	private static final String BUZZ_GUARDIAN_ADDRESS = "buzzguardian@gmail.com";
	private static final String BUZZ_GUARDIAN_PASSWORD = "gtbuzz00";

	private static Voice voice;

	static {
		try {
			voice = new Voice(BUZZ_GUARDIAN_ADDRESS, BUZZ_GUARDIAN_PASSWORD);
		} catch (IOException e) {
			System.err.println("ERROR NOT ABLE TO CONNECT TO GOOGLE VOICE");
			e.printStackTrace();
		}
	}
	
	/**
	 * @return Unread SMS messages in the form of a List of SMSObjects 
	 * @throws DocumentException
	 */
	public static List<SMSData> getUnreadSMS() throws DocumentException{

		List<SMSData> smsList = new ArrayList<SMSData>();

		Collection<SMSThread> smsThreads = null;
		
		try {
			smsThreads = voice.getSMSThreads();
		} catch (IOException e) {
			System.err.println("ERROR GETTING SMS THREADS");
			e.printStackTrace();
		}

		int size = 0;
		for (SMSThread t : smsThreads) {
			Collection<SMS> sms = t.getAllSMS();
			size += sms.size();
			//System.out.println( "Thread Messages: " + sms.size() );
		}

		System.out.println("Current Messages: " + size);

		int readCount;
		File fname = new File("readCount.txt");
		if (!fname.exists()) {
			// System.out.println("Initializing !!!");
			readCount = 0;
		} else {
			Scanner sc = null;
			try {
				sc = new Scanner(new File("readCount.txt"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			readCount = size - sc.nextInt();
			System.out.println("Read Count  = " +readCount);
			sc.close();
		}

		PrintWriter writer = null;
		try {
			writer = new PrintWriter("readCount.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.println(size);
		writer.close();

		return _getUnreadSMS(readCount, smsThreads);
	}
	
	private static List<SMSData> _getUnreadSMS(int readCount, Collection<SMSThread> smsThreads){
		List<SMSData> smsList = new ArrayList<SMSData>();
		int kt = 0;
		for (SMSThread t : smsThreads) {
			Collection<SMS> sms = t.getAllSMS();
			for (SMS s : sms) {
				if(kt >= readCount){
					return smsList;
				}
				Timestamp ts = new Timestamp(s.getDateTime().getTime());
				if (s.getFrom().getNumber().equals(Constants.BUZZ_GUARDIAN_NUMBER)) { 
					readCount--;
					continue;				
				} else {
					smsList.add(new SMSData(s.getFrom().getNumber(), s.getContent(), ts));
				}
				
				kt++;
				if(kt >= readCount){
					return smsList;
				}
			}
		}
		return smsList;
	}
	
	/**
	 * sends an SMS containing a specified text to a number
	 * @param number
	 * @param text
	 */
	public static void sendSMS(String number, String text){
			try {
				voice.sendSMS(number, text);
				System.out.println("Sent SMS to Police!");
			} catch (IOException e) {
				System.err.println("Unable to send SMS to [" + number + "]");
				System.err.println(e.getMessage());
			}	
	}

	/*
	public static void main(String[] args) {
		try {
			List<SMSData> smsList = GoogleVoice.getUnreadSMS();
			System.out.println("Number of unread messages: " + smsList.size());
			for(SMSData sms : smsList){
				System.out.println(sms.getFromNumber() + "\n" + sms.getMessage() + "\n" + sms.getTimestamp());
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	*/
}