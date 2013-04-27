package guardian;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class EmergencyMap
 */
@WebServlet(description = "Returns Latest Coordinates of the user", urlPatterns = { "/EmergencyMap" })
public class EmergencyMap extends HttpServlet {
	
	private static Timestamp latestTimestamp = null;
	
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub		
	}
	
    /**
     * 
     * @see HttpServlet#HttpServlet()
     */
    public EmergencyMap() {
        super();
        // TODO Auto-generated constructor stub
    }
	

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String connectionURL = "jdbc:mysql://localhost:3306/buzz";
		Connection connection = null;
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		String jsonObject = "{";
		try {
	    	Class.forName("com.mysql.jdbc.Driver");
	        connection = DriverManager.getConnection(connectionURL, "buzz",
	                "guardian");
	        Statement stmt = connection.createStatement();			
	        String sql = "select TimeStamp from trackingsms order by ID desc LIMIT 1" ;
	        ResultSet rs = stmt.executeQuery(sql);
	        try {
		        while (rs.next()) {
		        	latestTimestamp = rs.getTimestamp(1);
		        }
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	        
	        // out.println(latestTimestamp.toString());
	        jsonObject = jsonObject + "\"timestamp\": " + "\"" + latestTimestamp + "\",";
	        
	        sql = "select Latitude, Longitude from trackingsms where Timestamp >= '"+ latestTimestamp + "' order by ID asc" ;
	        rs = stmt.executeQuery(sql);
	        try {
	        	jsonObject = jsonObject + "\"locations\": [";
		        while (rs.next()) {
		        	// out.println(rs.getDouble(1) + " " + rs.getDouble(2));
		        	jsonObject = jsonObject + "[" + rs.getDouble(1) + "," +rs.getDouble(2) + "],";
		        }
		        jsonObject = jsonObject.substring(0, jsonObject.length()-1);
		        jsonObject = jsonObject + "]}";
		        out.println(jsonObject);
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	        connection.close();
	    } catch (Exception e) {
	    	System.out.println(e);
	    }		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
