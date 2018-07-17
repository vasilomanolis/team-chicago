package CurrentServer;

import java.net.Socket;
import java.util.ArrayList;
/**
 * the User class is for the creation of user objects, created whenever someone signs into the server, it has an id given to them for this session, 
 * Stores their username/password, the socket uses to communicate with their client and a list of chats they are in signified by that chats id. 
 * @author Mcnally
 *
 */
public class User {

	private int sessionID; 				// the users id for this session
	private String username;			// the username of the user 
	private String password;			// the passwod of the user 
	private Socket socket;				// the socket used to communicate to the users client 
	private ArrayList<Integer> chats;	//a list of chats the user is in 

	//constructor
	public User(int sessionID, String username, String password, Socket socket, ArrayList<Integer> chats) {
		this.sessionID = sessionID;
		this.username = username;
		this.password = password;
		this.socket = socket;
		this.chats = chats;
	}


	public User(String username, Socket socket) {
		this.username = username;
		this.socket = socket;
	}


	//getter for the id of the user 
	public int getsessionID() {
		return sessionID;
	}

	//getter for the socket of the user 
	public Socket getSocket() {
		return socket;
	}

	//getter for the username of the user 
	public String getUsername() {
		return username;
	}


	// getter for the lsit of chats the user is in
	public ArrayList<Integer> getChats() {
		return chats;
	}




}
