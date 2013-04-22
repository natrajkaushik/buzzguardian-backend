package guardian;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetAddress {

  public static final void main (String[] argv) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {

    String address;
    URL url = null;

    // prepare a URL to the geocoder
    try {
    	url = new URL("http://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&sensor=false");
    } catch (MalformedURLException e)  {
    	e.printStackTrace();
    }

    // prepare an HTTP connection to the geocoder
    HttpURLConnection conn = null;
    try {
    	conn = (HttpURLConnection) url.openConnection();
    } catch (IOException e) {
    	e.printStackTrace();
    }

    BufferedReader br;    
    try {
    	br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    	String line;
        StringBuilder sb = new StringBuilder();
        while((line = br.readLine()) != null) {
        	sb.append(line + "\n");
        }
        br.close();
        //System.out.println(sb);
        
        try {
        	JSONObject object = new JSONObject(sb.toString());
			JSONArray results = object.getJSONArray("results");
			System.out.println("Size of Results = " + results.length());
			
			String formattedAddress = results.getJSONObject(0).getJSONArray("address_components").getJSONObject(0).getString("short_name");
			formattedAddress = formattedAddress+ " " + results.getJSONObject(0).getJSONArray("address_components").getJSONObject(1).getString("short_name");
			System.out.println(formattedAddress);			
        } catch (Exception e) {
        	e.printStackTrace();
        }
    } catch (Exception e) {
    	e.printStackTrace();
    }
}
}
