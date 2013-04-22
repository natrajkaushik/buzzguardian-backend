package guardian;

/**
 * Synchronized queue interface for SMS processing
 */
public interface SMSTransactionQueue {
	
	/**
	 * @param fromNumber
	 * @return true if SMSData present in queue
	 */
	public boolean isPresent(String fromNumber);
	
	/**
	 * Add an SMSData to synchronized queue
	 * @param data: SMSData object to add
	 */
	public boolean addSMS(SMSData data); 
	
	/**
	 * @param fromNumber
	 * @return remove and return SMSData from queue
	 */
	public SMSData removeSMS(String fromNumber);
	
	/**
	 * update an SMSData object already in queue
	 * @param data: new SMSData object
	 */
	public boolean updateSMS(SMSData data);	
	
}
