package guardian;

import java.sql.Timestamp;

/**
 * Object representation of SMS
 */
public class SMSData {
	
	private String fromNumber; //number of user who sent the panic request
	private String message;
	private Timestamp timestamp;	
	
	private RequestType requestType; //SEND_ALERT, CANCEL_ALERT or TRACK
	private State state; // state of the object - PENDING, PROCESSING or TRACKING
	
	private Double latitude;
	private Double longitude;
	
	public SMSData(String fromNumber, String message, Timestamp timestamp) {
		super();
		this.fromNumber = fromNumber;
		this.message = message;
		this.timestamp = timestamp;
		this.requestType = RequestType.SEND_ALERT;
		this.state = State.PENDING;
		this.latitude = 0.0;
		this.longitude = 0.0;
	}
	
	public SMSData(String fromNumber, String message, Timestamp timestamp,
			RequestType requestType) {
		super();
		this.fromNumber = fromNumber;
		this.message = message;
		this.timestamp = timestamp;
		this.requestType = requestType;
		this.state = State.PENDING;
		this.latitude = 0.0;
		this.longitude = 0.0;
	}
	
	public SMSData(String fromNumber, String message, Timestamp timestamp,
			RequestType requestType, State state) {
		super();
		this.fromNumber = fromNumber;
		this.message = message;
		this.timestamp = timestamp;
		this.requestType = requestType;
		this.state = state;
		this.latitude = 0.0;
		this.longitude = 0.0;
	}
	
	public SMSData(String fromNumber, String message, Timestamp timestamp,
			RequestType requestType, State state, Double latitude, Double longitude) {
		super();
		this.fromNumber = fromNumber;
		this.message = message;
		this.timestamp = timestamp;
		this.requestType = requestType;
		this.state = state;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public SMSData(String fromNumber, String message, Timestamp timestamp,
			RequestType requestType, Double latitude, Double longitude) {
		super();
		this.fromNumber = fromNumber;
		this.message = message;
		this.timestamp = timestamp;
		this.requestType = requestType;
		this.state = State.PENDING;
		this.latitude = latitude;
		this.longitude = longitude;
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
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	public RequestType getRequestType() {
		return requestType;
	}
	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public Double getLatitude() {
		return latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
}
