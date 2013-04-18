package guardian;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.techventus.server.voice.Voice;

public class GoogleVoice {

	String userName;
	String pass;
	Voice voice;

	GoogleVoice()
	{
		userName = "buzzguardian@gmail.com";
		pass = "gtbuzz00";

		try {
			voice = new Voice(userName, pass);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// Returns a list of strings. 3 consecutive strings represent a SMS
	// First string is sender number
	// Second string is sms text
	// Third string is time
	public ArrayList<String> getUnreadSMS() throws DocumentException {

		ArrayList<String> aList = new ArrayList<String>();

		try {
			String recentMsg = voice.getUnreadSMS();
			// System.out.println( recentMsg );
			recentMsg = recentMsg.replace("<![CDATA[", "");
			recentMsg = recentMsg.replace("]]>", "");
			recentMsg = recentMsg.replaceAll("&", "");

			Document document = DocumentHelper.parseText(recentMsg);
			List list = document.selectNodes("//div[@class='gc-message-sms-row']");
			System.out.println(list.size());
			int readCount = 0;

			File fname = new File("readCount.txt");
			if (!fname.exists()) {
				PrintWriter writer = new PrintWriter("readCount.txt");
				writer.println(readCount);
				writer.close();
			}
			else {
				Scanner sc = new Scanner( new File("readCount.txt") );
				readCount = list.size() - sc.nextInt();
				sc.close();
				PrintWriter writer = new PrintWriter("readCount.txt");
				writer.println(list.size());
				writer.close();
			}

			Iterator msgIterator = list.iterator();
			for (int i = 0; i < readCount && msgIterator.hasNext(); ++i) {
				Element msgElement = (Element) msgIterator.next();
				for (Iterator msgAttributeIterator = msgElement.elementIterator(); msgAttributeIterator.hasNext();) {
					Element msgAttributeElement = (Element) msgAttributeIterator.next();
					System.out.println(msgAttributeElement.attributeValue("class") + "\t" + msgAttributeElement.getStringValue().trim());
					aList.add(msgAttributeElement.getStringValue().trim());
				}
			}
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}

		return aList;
	}

	public static void main(String[] args) {
		ArrayList<String> SMSList = new ArrayList<String>();
		
		try {
			GoogleVoice googleVoice = new GoogleVoice();
			for (int i = 0; i < 1; ++i) {
				SMSList = googleVoice.getUnreadSMS();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		System.out.println("Size of SMSList = "+SMSList.size());
	}
}