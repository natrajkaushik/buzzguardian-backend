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
			        /*
	        PreparedStatement pst = connection.prepareStatement(sql);
	        pst.setString(1, user_name);
	        pst.setString(2, email_Id);
	        pst.setString(3, gender);
	        pst.setString(4, age);
	        pst.setString(5, mobile_no);
	        pst.setString(6, password);
	        pst.setString(7, new Date().toString());

	        int numRowsChanged = pst.executeUpdate();
	        if (numRowsChanged < 7) {
	            out.println("error createing : " + numRowsChanged);
	            return;
	        }
	        out.println("Account created succesfully with the user name "
	                + user_name);
	        pst.close();
	        */
	    
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("in doPost!");
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
	    

	    int mobNo = 0;
	    try {
	    	if (mobileNo != null && !mobileNo.isEmpty()){
	    		mobNo = Integer.parseInt(mobileNo);
	    	}
	    } catch (NumberFormatException e) {
	    	System.out.println(e);
	    }
	    try {
	    	
	    	System.out.println("Iam here!");
	        Class.forName("com.mysql.jdbc.Driver");
	        connection = DriverManager.getConnection(connectionURL, "buzz",
	                "guardian");
	        String sql = "insert into test values (?,?)";
	        PreparedStatement pst = connection.prepareStatement(sql);
	        pst.setString(1, firstName);
	        pst.setInt(2, mobNo);
	        int numRowsChanged = pst.executeUpdate();
	        System.out.println("Number of Rows updated = " + numRowsChanged);
	        pst.close();
	        /*
	        while (rs.next()) {
	        	System.out.println(rs.getString(1) + "\t " + rs.getString(2));
	        }
			*/
	        connection.close();

		} catch (Exception e) {
	        out.println(e);
	    }
	}
}
