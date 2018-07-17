package CurrentServer;


/**
 * @author rxm448
 * the message class is for the creation of message objects, 
 * which will be used to store the communication on the public lobby and the messages sent over private chats  
 */

public class Message {
	private String sender; // the user who sent the message 
	private String timeStamp; // the time the message was sent 
	private String content; // the content of the message 

	/**
	 * the constructor initialises each of the fields with the values provided 
	 * @param sender, the username of the sender, as a string 
	 * @param timeStamp, the time the message was sent, as a String  
	 * @param content, the content of the message as a string 
	 */
	public Message(String sender, String timeStamp, String content) {
		this.sender = sender;
		this.timeStamp = timeStamp;
		this.content = content;
		
	}

	//getter for the sender 
	public String getSender() {
		return sender;
	}

	// getter for the timeStamp
	public String getTimeStamp() {
		return timeStamp;
	}

	// getter for the content 
	public String getContent() {
		return content;
	}
}
