package guardian;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
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
	public static List<SMSObject> getUnreadSMS() throws DocumentException{

		List<SMSObject> smsList = new ArrayList<SMSObject>();

		Collection<SMSThread> smsthreads = null;
		
		try {
			smsthreads = voice.getSMSThreads();
		} catch (IOException e) {
			System.err.println("ERROR GETTING SMS THREADS");
			e.printStackTrace();
		}

		int size = 0;
		for (SMSThread t : smsthreads) {
			Collection<SMS> sms = t.getAllSMS();
			size += sms.size();
			// System.out.println( "Thread Messages: " + sms.size() );
		}

		System.out.println("Current Messages: " + size);

		int readCount;
		File fname = new File("readCount.txt");
		if (!fname.exists()) {
			System.out.println("Initializing !!!");
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

		int kt = 0;
		for (SMSThread t : smsthreads) {
			if (kt++ >= readCount) {
				break;
			}
			Collection<SMS> sms = t.getAllSMS();
			for (SMS s : sms) {
				System.out.println("Number: " + s.getFrom().getNumber());
				System.out.println("Text: " + s.getContent());
				System.out.println("Time: " + s.getDateTime().toString());

				smsList.add(new SMSObject(s.getFrom().getNumber(), s
						.getContent(), s.getDateTime().toString()));
				break;
			}
		}

		return smsList;
	}

	public static void main(String[] args) {
		try {
			List<SMSObject> smsList = GoogleVoice.getUnreadSMS();
			System.out.println("Number of unread messages: " + smsList.size());
			for(SMSObject sms : smsList){
				System.out.println(sms.getFromNumber() + "\n" + sms.getMessage() + "\n" + sms.getTimestamp());
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}