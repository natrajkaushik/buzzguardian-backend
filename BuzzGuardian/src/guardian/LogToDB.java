package guardian;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

public class LogToDB {
	private String connectionURL = "jdbc:mysql://localhost:3306/buzz";
	
	public void logToSMSLog(SMSData sms) {		
	    Connection connection = null;	    
	    try {
	    	Class.forName("com.mysql.jdbc.Driver");
	        connection = DriverManager.getConnection(connectionURL, "buzz",
	                "guardian");
	        String sql = "insert into smslog (MobileNo, TimeStamp, Request, State, Latitude, Longitude) values (?,?,?,?,?,?)";
	        PreparedStatement pst = connection.prepareStatement(sql);
	        pst.setString(1, sms.getFromNumber());
	        pst.setTimestamp(2, sms.getTimestamp());
	        pst.setString(3, sms.getRequestType().toString());
	        pst.setString(4, sms.getState().toString());
	        pst.setDouble(5, sms.getLatitude());
	        pst.setDouble(6, sms.getLongitude());
	        int numRowsChanged = pst.executeUpdate();
	        pst.close();
	        connection.close();
	    } catch (Exception e) {
	    	System.out.println(e);
	    }
	}
	
	public void logToTrackingSMS (SMSData sms) {		
	    Connection connection = null;
	    
	    try {
	    	Class.forName("com.mysql.jdbc.Driver");
	        connection = DriverManager.getConnection(connectionURL, "buzz",
	                "guardian");
	        String sql = "insert into trackingsms (MobileNo, TimeStamp, State, Latitude, Longitude) values (?,?,?,?,?)";
	        PreparedStatement pst = connection.prepareStatement(sql);
	        pst.setString(1, sms.getFromNumber());
	        pst.setTimestamp(2, sms.getTimestamp());
	        pst.setString(3, sms.getState().toString());
	        pst.setDouble(4, sms.getLatitude());
	        pst.setDouble(5, sms.getLongitude());
	        int numRowsChanged = pst.executeUpdate();
	        connection.close();
	    } catch (Exception e) {
	    	System.out.println(e);
	    }
	}
	
	public UserInfo queryUserInfo (SMSData sms) {
		Connection connection = null;
	    UserInfo info = new UserInfo();
	    try {
	    	Class.forName("com.mysql.jdbc.Driver");
	        connection = DriverManager.getConnection(connectionURL, "buzz",
	                "guardian");
			Statement stmt = connection.createStatement();			
	        String sql = "select MobileNo, FirstName, LastName, EmailID from userinfo where MobileNo = '" + sms.getFromNumber() + "'" ;
	        ResultSet rs = stmt.executeQuery(sql);
	        while (rs.next()) {
		        info.mobileNo = rs.getString(1);
		        info.firstName = rs.getString(2);
		        info.lastName = rs.getString(3);
		        info.emailID = rs.getString(4);
	        }
	        connection.close();
	        return info;
	    } catch (Exception e) {
	    	System.out.println(e);
	    }
	    return info;
	}
	
	public void logToStats (long smsTime, long policeTime, String requestType) {		
	    Connection connection = null;
	    Timestamp smsTs = new Timestamp(smsTime);
        Timestamp policeTs = new Timestamp(policeTime);
	    
	    try {
	    	Class.forName("com.mysql.jdbc.Driver");
	        connection = DriverManager.getConnection(connectionURL, "buzz",
	                "guardian");
	        String sql = "insert into stats (InTimestamp, OutTimestamp, RequestType, TimeTaken) values (?,?,?,?)";
	        PreparedStatement pst = connection.prepareStatement(sql);	        
	        pst.setTimestamp(1, smsTs);
	        pst.setTimestamp(2, policeTs);
	        pst.setString(3, requestType);
	        pst.setInt(4, (int)(policeTime-smsTime)/(1000));
	        int numRowsChanged = pst.executeUpdate();
	        connection.close();
	    } catch (Exception e) {
	    	System.out.println(e);
	    }
	}
}
