package guardian;

/**
 * POJO for representing an SMS
 */
public class SMSObject {
	
	private String fromNumber;
	private String message;
	private String timestamp;
	
	public SMSObject(String fromNumber, String message, String timestamp) {
		super();
		this.fromNumber = fromNumber;
		this.message = message;
		this.timestamp = timestamp;
	}
	public String getFromNumber() {
		return fromNumber;
	}
	public void setFromNumber(String fromNumber) {
		this.fromNumber = fromNumber;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	

}
