package guardian;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

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
	private static final long serialVersionUID = 1L;
       
    /**
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
		Timestamp ts = null;
		String connectionURL = "jdbc:mysql://localhost:3306/buzz";
		Connection connection = null;
		try {
	    	Class.forName("com.mysql.jdbc.Driver");
	        connection = DriverManager.getConnection(connectionURL, "buzz",
	                "guardian");
	        Statement stmt = connection.createStatement();			
	        String sql = "select TimeStamp from trackingsms order by ID desc LIMIT 1" ;
	        ResultSet rs = stmt.executeQuery(sql);
	        while (rs.next()) {
		           ts = rs.getTimestamp(1);
	        }
	        connection.close();
	    } catch (Exception e) {
	    	System.out.println(e);
	    }
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		out.println("Latest TimeStamp = " + ts.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
