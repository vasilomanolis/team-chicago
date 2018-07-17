package CurrentServer;

import java.util.ArrayList;
/**
 * The Chat class is for managing private chats between several users, it is assigned a unique id upon creation, 
 * given a name by the creator and stores lists of the users currently in the chat and the messages which have been sent 
 * @author Chicago
 *
 */
public class Chat {
	
	private int chatID; 					// the id of this chat
	private String chatName; 				// the name given to this chat by the user who created it 
	private ArrayList<String> users; 		// the list of users in the chat 
	private ArrayList<Message> messages; 	// the list of messages sent in this chat
	
	//Constructor 
	public Chat(int chatID, String chatName, ArrayList<String> users, ArrayList<Message> messages) {
		this.chatID = chatID;
		this.chatName = chatName;
		this.users = users;
		this.messages = messages;
		
	}
	
	
	public int getChatID() { 					// getter for chat ID
		return chatID;
	}

	public ArrayList<String> getUsers() { 		// getter for list of users 
		return users;
	}

	public ArrayList<Message> getMessages() { 	// getter for list of messages 
		return messages;
	}
	
	public String getChatName() { 				// getter for name of chat 
		return chatName;
	}

	

}
