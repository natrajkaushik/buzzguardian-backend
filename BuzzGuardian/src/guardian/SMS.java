package guardian;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.TimerTask;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.techventus.server.voice.Voice;


/**
 * Servlet implementation class SMS
 */
@WebServlet(description = "Retrieves User Info and Sends to Police via SMS", urlPatterns = { "/SMS" })
public class SMS extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SMS() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#getServletConfig()
	 */
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	/*
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		ArrayList<String> SMSList = new ArrayList<String>();
		
		try {
			GoogleVoice googleVoice = new GoogleVoice();
			for (int i = 0; i < 10; ++i) {
				SMSList = googleVoice.getUnreadSMS();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		
		response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.println("Size of ArrayList: " + SMSList.size());
        out.println("Latest SMS received:");
        
        out.println("From: " + SMSList.get(SMSList.size()-3));
        out.println("Text: " + SMSList.get(SMSList.size()-2));
        out.println("At: " + SMSList.get(SMSList.size()-1));
        
	}
*/
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}


//package com.security.buzz;

/*
class GoogleVoice {

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
			System.out.println("Size of List: "+list.size());
			int readCount = 10;
			
			File fname = new File("readCount.txt");
			if (!fname.exists()) {
				PrintWriter writer = new PrintWriter("readCount.txt");
				writer.println(readCount);
				writer.close();
			}
			else {
				Scanner sc = new Scanner(new File("readCount.txt") );
				readCount = list.size() - sc.nextInt();
				sc.close();
				PrintWriter writer = new PrintWriter("readCount.txt");
				writer.println(list.size());
				writer.close();				
			}
System.out.println("Read Count COunt: "+readCount);
			Iterator msgIterator = list.iterator();
			for (int i = 0; i < readCount && msgIterator.hasNext(); ++i) {
				Element msgElement = (Element) msgIterator.next();
				for (Iterator msgAttributeIterator = msgElement.elementIterator(); msgAttributeIterator.hasNext();) {
					Element msgAttributeElement = (Element) msgAttributeIterator.next();
					System.out.println(msgAttributeElement.attributeValue("class") + "\t" + msgAttributeElement.getStringValue().trim());
					aList.add(msgAttributeElement.getStringValue().trim());
					System.out.println("i = " + i);
				}
			}
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}

		return aList;
	}

}
*/