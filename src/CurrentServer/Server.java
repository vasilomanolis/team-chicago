package CurrentServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.html.HTMLDocument.Iterator;

import Protocol.SimpleProtocol;
/**
 * 
 * @author rxm448
 * the server class, it accepts connections from the client, and creates a new serverThread for the that client 
 *
 */
public class Server extends Thread{

	private int port;
	private static int userSessionID = 0;
	private static int chatSessionID = 0;
	private static int anonUsers = 0;

	private static SimpleProtocol protocol = new SimpleProtocol(); //protocol for rndocing/decoding communication
	
	private static JTextArea ChatLog = new JTextArea(); // Log for displaying infor on server_GUI

	public static Map<Integer, Chat> chatMap = new HashMap<Integer, Chat>(); // map of chat session ids to chat objects

	public static Map<Integer, User> userMap = new HashMap<Integer, User>();	//map of user session ids to user objects
	
	public static Map<String, Integer> userIdMap = new HashMap<String, Integer>(); // map of usernames to user session ids

	public static ArrayList<Message> messagesO = new ArrayList<Message>(); // an ArrayList of message objects which stores all the messages sent since this instance of the server was started 

	/**
	 * constructor for the server 
	 * @param port, sets the given port to this servers port 
	 * @param ChatLog, creates a chat log for displaying server activity
	 */
	public Server(int port, JTextArea ChatLog) { 
		this.port = port;
		this.ChatLog = ChatLog;
	}

	/**
	 * run method for the server which waits for clients to connect then creates a thread for that client 
	 */
	public void run(){ 

		ArrayList<ServerThread> threads = new ArrayList<ServerThread>();


		ServerSocket serverSock;
		try {
			serverSock = new ServerSocket(port);

			String str = "Waiting for clients...";
			System.out.println(str);
			ChatLog.append(str+"\n");

			try {
				while(true){ // infinite loop that accepts new connections, creating and starting anew serverThread for each 


					Socket clientSock = serverSock.accept();
					String str1 = "Client connected from: " + clientSock.getLocalAddress().getHostName();
					System.out.println("Client connected from: " + clientSock.getLocalAddress().getHostName()); 
					ChatLog.append(str1 + "\n");
					ServerThread thread = new ServerThread(clientSock, ChatLog);
					threads.add(thread);
					thread.start();
				}
				
			}finally { // closes the socket
				
				for (Map.Entry<Integer, User> entry : userMap.entrySet()) {
					Socket tempSock = entry.getValue().getSocket();
					try {
						DataOutputStream tempOut = new DataOutputStream(tempSock.getOutputStream());
						String shutdown = protocol.createMessage("server-off");
						tempOut.writeBytes(shutdown + "\n");

					} catch (IOException e) {
						e.printStackTrace();

					}
				}

				serverSock.close();
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**
	 * the add user method, when a user signs in this method is called to add the user to the list of currently online users, 
	 * and to update the lists which each client holds
	 * @param id,id number given to this user, this id unique both to this user and this serssion of the server 
	 * @param username, the clients username 
	 * @param password, the clients password 
	 * @param clientSock, the socket the client is using 
	 * @param arrayList, an arraylist which will hold the different chats that user is engaged in 
	 */
	public static void addUser(String username, String password, Socket clientSock) {

		int id = assignUserId(); //assigns this user a unique user id for theis session 
		
		/**
		 * this loop cycles through the list of users and sends the username of the user currently being added to each of them
		 */

		for (Map.Entry<Integer, User> entry : userMap.entrySet()) {
			Socket tempSock = entry.getValue().getSocket();
			try {
				DataOutputStream tempOut = new DataOutputStream(tempSock.getOutputStream());
				String addition = protocol.createMessage("update-list", "add-user", username);
				tempOut.writeBytes(addition + "\n");

			} catch (IOException e) {
				e.printStackTrace();

			}
		}

		/**
		 * the current user is then added to the list, and another for loop cycles through list of online users and sends their usernames to this current user
		 * this includes the username of themselves, as they have just been added 
		 */
		
		
		
		userMap.put(id, new User(id, username, password, clientSock, new ArrayList<Integer>()));
		userIdMap.put(username, id);

		for (Map.Entry<Integer, User> entry : userMap.entrySet()) {
			try {
				DataOutputStream tempOut = new DataOutputStream(clientSock.getOutputStream());
				String name = entry.getValue().getUsername();
				String addition = protocol.createMessage("update-list", "add-user", name);
				tempOut.writeBytes(addition + "\n");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * method for creating a group chat, creates a new chat object with a unique id for this session, 
	 * then sends a message to all the clients the group was created for
	 * @param users, the users that should be added to the chat on initial creation 
	 */
	public static synchronized void makeChat(String chatName, ArrayList<String> users) {
		String str = "makeChat got called!";
		System.out.println(str);
		ChatLog.append(str + "\n");
		int id = assignChatId(); //chat is given an id unique to this session 

		ArrayList<Message> chatMessages = new ArrayList<Message>();
		Chat chat = new Chat(id, chatName, users, chatMessages); // chat is created and added to the HashMap of active chats 

		chatMap.put(id, chat);

		String[] create = new String[users.size() + 3];
		create[0] = "create-group"; create[1] = String.valueOf(id); create[2] = chatName;

		for (int i = 0; i < users.size(); i++) { // loop that creates the message to be sent to the clients 
			create[i + 3] = users.get(i);
		}

		String group = protocol.createMessage(create);

		for (int j = 0; j < users.size(); j++) { // loop that goes through the list of users in the chat, gets their socket and sends the "group" message
			User currentUser = userMap.get(userIdMap.get(users.get(j)));
			currentUser.getChats().add(id);
			Socket tempSock = currentUser.getSocket();
			try {
				DataOutputStream tempOut = new DataOutputStream(tempSock.getOutputStream());
				tempOut.writeBytes(group + "\n");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * The updatePrivateMessage is called when the server receives a private message from a client, it finds the id of the chat the message was sent in, 
	 * and sends the message to all the participants of that chat 
	 * @param id, the id of the chat the message was sent in, as an integer
	 * @param sender, the username of the sender of the message, as a string  
	 * @param time, the time the message was received by the server, as a string  
	 * @param message, the content of the message, as a string 
	 */
	public static synchronized void updatePrivateMessage(Integer id, String sender, String time, String content) {
		
		Message message = new Message(sender, time, content);
		chatMap.get(id).getMessages().add(message);
		
		String pMessage = protocol.createMessage("send-private-message", String.valueOf(id), sender, time, content); //forms a message to send to the client, 
																													 //specifying the chat (via it's id), the sender, 
																													 //time and content of the message  

		for (int i = 0; i < chatMap.get(id).getUsers().size(); i++) { // loops through all the users in the specified chat 
			
			Socket tempSock = userMap.get(userIdMap.get(chatMap.get(id).getUsers().get(i))).getSocket(); // gets the socket of the current user as define by the for loop 
			try {
				DataOutputStream tempOut = new DataOutputStream(tempSock.getOutputStream());
				tempOut.writeBytes( pMessage + "\n"); // sends the private message to the current user 

			} catch (IOException e) {
				e.printStackTrace();
			}

		}


	}

	/**
	 * the addToChat message is called when one or more users is added to an existing private chat, it loops through the list of those to be added,
	 * each time sending a message to that client to start a group chat, it then sends a message to all existing members of the chat, 
	 * to update their lists of who is currently in the chat
	 * @param id, the id of the private chat, as an Integer
	 * @param usernames, a list of usernames of those to be added to the chat, as an ArrayList
	 */
	public static synchronized void addToChat(Integer id, ArrayList<String> usernames){
		ArrayList<String> newList = chatMap.get(id).getUsers();


		for (int k = 0; k < usernames.size(); k++){ //cycles through the list of all the users to be added to the chat

			if(!newList.contains(usernames.get(k))) { // if user is not already in the arayList of users for that chat 

				String[] create = new String[newList.size() + 4]; //message is formed to be sent to the user being added, which will open a chat window on their end  
				create[0] = "create-group"; create[1] = String.valueOf(id);  create[2] = chatMap.get(id).getChatName(); create[3] = usernames.get(k);

				for (int i = 0; i < newList.size(); i++) { // this loop adds to the array all of the other users currently in the chat 
					create[i + 4] = newList.get(i);
				}

				String addUser = protocol.createMessage(create);
				Socket tempSock1 = userMap.get(userIdMap.get(usernames.get(k))).getSocket();

				try {
					DataOutputStream tempOut1 = new DataOutputStream(tempSock1.getOutputStream());
					tempOut1.writeBytes( addUser + "\n");

				} catch (IOException e) {
					e.printStackTrace();

				}
				
				ArrayList<Message> messages = chatMap.get(id).getMessages();	// the list of messages that have already been sent in this chat 
				
				for (int l = 0; l < messages.size(); l++) {	// this loop sends the user currently being added the chat history
					String pastMessage = protocol.createMessage("send-private-message", String.valueOf(id), messages.get(l).getSender(), messages.get(l).getTimeStamp(), messages.get(l).getContent());
					try {
						DataOutputStream tempOut1 = new DataOutputStream(tempSock1.getOutputStream());
						tempOut1.writeBytes( pastMessage + "\n");

					} catch (IOException e) {
						e.printStackTrace();

					}
				}


				String add = protocol.createMessage("add-to-chat", String.valueOf(id), usernames.get(k));

				for (int j = 0; j < newList.size(); j++) { //loop that  messages all users currently in the chat with the username of the one just added 
					Socket tempSock = userMap.get(userIdMap.get(newList.get(j))).getSocket();
					try {
						DataOutputStream tempOut = new DataOutputStream(tempSock.getOutputStream());
						tempOut.writeBytes( add + "\n");

					} catch (IOException e) {
						e.printStackTrace();


					}

				}
				chatMap.get(id).getUsers().add(usernames.get(k)); // finally the current user is added to the list of users in the chat
				
				User currentUser = userMap.get(userIdMap.get(usernames.get(k)));
				currentUser.getChats().add(id);


			}


		}

	}

	/**
	 * The disconnect method is called when a client disconnects, it first removes the user form all the private chats they are in by calling disconnectFromChat,
	 * then a message is sent to each user online to remove this user from their local lists of online users 
	 * @param username, the username of the user being disconnected, as a string 
	 */
	public static synchronized void disconnect(String username) {
		ArrayList<Integer> chats = userMap.get(userIdMap.get(username)).getChats();
		
		for (int i = 0; i < chats.size(); i++) {
			disconnectFromChat( chats.get(i), username);
		}
		
		String disconnect = protocol.createMessage("update-list", "remove-user", username);
		
		for (Map.Entry<Integer, User> entry : userMap.entrySet()) {
			Socket tempSock = entry.getValue().getSocket();
			try {
				DataOutputStream tempOut = new DataOutputStream(tempSock.getOutputStream());
				tempOut.writeBytes( disconnect + "\n");

			} catch (IOException e) {
				e.printStackTrace();


			}
		}
		
		userMap.remove(userIdMap.get(username));
		userIdMap.remove(username);
	}
	
	/**
	 * this method is called either when a user specifically leaves a private chat or when they disconnect altogether,
	 * first it removes the user from that chat objects list of users, then it loops through the remaining users and sends them a message to 
	 * remove  this user from their list of users in the private chat 
	 * @param chatId, the id of the chat this user must be removed from, as an integer 
	 * @param username, the username of the user to be removed from the chat 
	 */
	public static synchronized void disconnectFromChat(Integer chatId, String username) {

		ArrayList<String> chatUsers = chatMap.get(chatId).getUsers();
		chatUsers.remove(username);
		
		for (int i = 0; i < chatUsers.size(); i++) { //loops through the other users in the chat and messages them, so they will remove this user from their chats
			String remove = protocol.createMessage("remove-from-chat", String.valueOf(chatId), username);
			Socket tempSock = userMap.get(userIdMap.get(chatUsers.get(i))).getSocket();
			try {
				DataOutputStream tempOut = new DataOutputStream(tempSock.getOutputStream());
				tempOut.writeBytes( remove + "\n");

			} catch (IOException e) {
				e.printStackTrace();


			}
		}

	}

	/**
	 * method to increment userSessionID by one, called when a new user signs in 
	 * and is used to give that client a unique id number for this session  
	 * @return userSessionID
	 */
	public static synchronized int assignUserId() {
		userSessionID++;
		return userSessionID;


	}

	/**
	 * method to increment chatSessionID by one, called when a new chat is made
	 * and is used to give that chat a unique id number for this session  
	 * @return chatSessionID
	 */
	public static synchronized int assignChatId() {
		chatSessionID++;
		return chatSessionID;
	}

	/**
	 * Method for the creation of a new anonymous username, simply combines the string "anon" with the string value of an integer,
	 * which ticks up each time an anonymous user connects.
	 * @return, the new usrname 
	 */
	public static synchronized String newAnnon() {
		String username = "anon" + String.valueOf(anonUsers);
		anonUsers++;
		return username;

	}

	/**
	 * method for the server being purposefully shutdown, messages each connected user to inform their clients to close 
	 */
	public void shutdown() {
		ChatLog.append("server shutting down!" + "\n");
		for (Map.Entry<Integer, User> entry : userMap.entrySet()) { //cyces through connected users 
			Socket tempSock = entry.getValue().getSocket();		// sets a temporary socket to that users socket 
			try {
				DataOutputStream tempOut = new DataOutputStream(tempSock.getOutputStream());
				String addition = protocol.createMessage("server-shutdown");
				tempOut.writeBytes(addition + "\n"); // sends the shucdown message sto the users client

			} catch (IOException e) {
				e.printStackTrace();

			}
		}
		
		
		
	}
	
}

