<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ page import="java.sql.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BuzzGuardian</title>
</head>
<body>
<jsp:declaration>

Statement stmt;
Connection con;
String url = "jdbc:mysql://localhost:3306/buzz";

</jsp:declaration>

<jsp:scriptlet><![CDATA[

Class.forName("com.mysql.jdbc.Driver");
con = DriverManager.getConnection(url, "buzz", "guardian"); 

stmt = con.createStatement();
ResultSet rs = stmt.executeQuery("SELECT * FROM test");

while (rs.next()) {
	System.out.println(rs.getString(1) + "\t " + rs.getString(2));
}

con.close();
]]></jsp:scriptlet>

</body>
</html>