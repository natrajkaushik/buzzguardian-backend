package guardian;

public class UserInfo {
	public String mobileNo;
	public String firstName;
	public String lastName;
	public String emailID;
	
	public UserInfo () {
		
	}
	public UserInfo (String mobileNo, String firstName, String lastName, String emailID) {
		this.mobileNo = mobileNo;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailID = emailID;		
	}
}
