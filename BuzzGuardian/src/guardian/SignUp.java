package guardian;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;

/**
 * Servlet implementation class SignUp
 */
@WebServlet("/SignUp")
public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUp() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub	    
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String connectionURL = "jdbc:mysql://localhost:3306/buzz";
		Statement stmt;
	    Connection connection = null;
	    ResultSet rs;
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    String firstName = request.getParameter("firstName");
	    String lastName = request.getParameter("lastName");
	    String emailID = request.getParameter("emailID");
	    String mobileNo = request.getParameter("mobileNo");
	    String passwd = request.getParameter("passwd");
	    
	    try {
	        Class.forName("com.mysql.jdbc.Driver");
	        connection = DriverManager.getConnection(connectionURL, "buzz",
	                "guardian");
	        String sql = "insert into userinfo (MobileNo, FirstName, LastName, EmailID, Passwd) values (?,?,?,?,?)";
	        PreparedStatement pst = connection.prepareStatement(sql);
	        pst.setString(1, mobileNo);
	        pst.setString(2, firstName);
	        pst.setString(3, lastName);
	        pst.setString(4, emailID);
	        pst.setString(5, passwd);
	        int numRowsChanged = pst.executeUpdate();
	        System.out.println("Number of Rows updated = " + numRowsChanged);
	        pst.close();
	        connection.close();
	        out.println("<html>");
	        out.println("<head>");
	        out.println("<title>Buzz Guardian</title>");
	        out.println("</head>");
	        out.println("<body>");
	        out.println("<h3>Registration Successful!</h3>");
	        out.println("<h4>Welcome "+ firstName + " " + lastName + " </h4>");
	        out.println("<a href=http://localhost:8080/BuzzGuardian/welcome.html > Go to Main Page</a>");
	        out.println("</body>");

		} catch (Exception e) {
	        out.println(e);
	    }
	}
}
