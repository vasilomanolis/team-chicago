package CurrentClient;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import Protocol.SimpleProtocol;

public class Client {

	private static Socket clientSocket;								// socket connecting to server
	private static DataOutputStream outToServer;					// output stream to server
	private BufferedReader inFromServer;							// input stream from server
	private static SimpleProtocol protocol = new SimpleProtocol();	// pack and unpack messages
	public Integer offset = -1;										// offset of messages, at the beginning it is -1. Update it to the offset of the latest message
	private String host = "";										// IP address of server
	private Integer port = 0;										// Port number of server
	private String thisUser;

	private static ArrayList<String> onlineUsers = new ArrayList<String>();

	/*
	 * 		Read a line from server and unpack it using SimpleProtocol
	 */
	public String[] getResponse(){
		
		try {
			return protocol.decodeMessage(inFromServer.readLine());
		}catch (java.net.SocketException f) {
			String[] serverGone = new String[] {"not-responding"};
			return serverGone;
		} catch (IOException e) {	
			e.printStackTrace();
		}		
		return null;
	}

	/*
	 * 		Send sign-up request to server, return the response to GuiSignUp
	 */
	public String[] signup(String user, String pass){
		String string = protocol.createMessage("sign-up", user, pass);
		try {
			outToServer.writeBytes(string + "\n");
			String[] response = this.getResponse();
			return response;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * 		Send sign-in request to server, return the response to GuiSignIn
	 */
	public String[] signin(String user, String pass, String annon){
		thisUser = user;
		String string = protocol.createMessage("sign-in", user, pass, annon);
		try {
			outToServer.writeBytes(string + "\n");
			String[] response = this.getResponse();
			if(response[1].equals("true")){
				System.out.println("Sign-in successful.");
			}else{
				System.out.println(response[2]);
			}
			return response;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	public String[] changePassword(String user, String pass){
		String string = protocol.createMessage("change-password", user, pass);
		try {
			outToServer.writeBytes(string + "\n");
			String[] response = this.getResponse();
			if(response[1].equals("true")){
				System.out.println(" Change password successful.");
			}else{
				System.out.println(response[2]);
			}
			return response;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}


	/*
	 * 		Send get-message request to server
	 */
	public void get_message(){
		String string = protocol.createMessage("get-message", this.offset.toString());
		try {
			outToServer.writeBytes(string + "\n");
		} catch (IOException e) {
			System.out.println("Unable to get message");
			e.printStackTrace();
		}
	}

	/*
	 * 		Send a message to server.
	 */
	public void send_message(String msg){
		String string = protocol.createMessage("send-message", msg);
		try {
			outToServer.writeBytes(string + "\n");
		} catch (IOException e) {
			System.out.println("Unable to send message.");
			e.printStackTrace();
		}
	}

	/**
	 * Method for starting a new private group chat, sends a message to the server width all the required content
	 * @param username, the username of the user who started the group chat
	 * @param otherUsers, an array of the other users to be included 
	 * @param chatName, the name given to the chat by the first user 
	 */
	public static void startGroup(String username, String[] otherUsers, String chatName) {

		String[] create = new String[otherUsers.length + 3];//string array that will be the message sent to the server, the frst 3 positions are the command, 
															//the name of the chat, and the username of the user who created the chat, with the oter positions 
															//given to the other users to be included 
		create[0] = "create-group"; create[1] = chatName; create[2] = username;
		for (int i = 0; i < otherUsers.length; i++){
			create[i + 3] = otherUsers[i];
		}

		String group = protocol.createMessage(create);

		try {
			outToServer.writeBytes(group + "\n");
		} catch (IOException e) {
			System.out.println("Unable to create group.");
			e.printStackTrace();
		}


	}

	/**
	 * the send_Private_message method is what allows users to communicate in private chats, 
	 * it sends to the server,  the content of the message and the id of the chat it should be sent to
	 * @param id, the unique id of the chat concerned, as an integer  
	 * @param msg, the message to be sent to the chat, as a string 
	 */
	public void send_Private_message(int id, String msg) {
		String string = protocol.createMessage("send-private-message", String.valueOf(id), msg);
		try {
			outToServer.writeBytes(string + "\n");
		} catch (IOException e) {
			System.out.println("Unable to send private message.");
			e.printStackTrace();
		}

	}
	/**
	 * the add chat user method is called when a user wishes to add one or more other users into an existing private chat, so it sends  a message to the server 
	 * with the add-to-chat along with the users to be added and id of the chat 
	 * @param addUsers, the array of users to be added to the the chat, as a string, as well as the id of the chat, which is the first element in the array 
	 */
	public void addChatUser(String[] addUsers) {

		String[] create = new String[addUsers.length + 1];
		create[0] = "add-to-chat";
		for (int i = 0; i < addUsers.length; i++){
			create[i + 1] = addUsers[i];
		}
		String addToGroup = protocol.createMessage(create);
		try {
			outToServer.writeBytes(addToGroup + "\n");
		} catch (IOException e) {
			System.out.println("Unable to send private message.");
			e.printStackTrace();
		}

	}
	
	/**
	 * Method for leaving a private chat, sends to the server; the command to leave, id of the chat to be left, 
	 * and the username of the current user, who is leaving 
	 * @param id, the id of the private chat this user wishes to leave 
	 */
	public static void leavePrivateChat(int id) {
		String leave = protocol.createMessage("leave-chat", String.valueOf(id), ChatGui.Username );
		try {
			outToServer.writeBytes(leave + "\n");
		} catch (IOException e) {
			System.out.println("Unable to send private message.");
			e.printStackTrace();
		}
	}
	
	/**
	 * request to be sent public chat history
	 */
	public void historyRequest() {
		String string = protocol.createMessage("history-request");
		try {
			outToServer.writeBytes(string + "\n");
		} catch (IOException e) {
			System.out.println("Unable contact server.");
			e.printStackTrace();
		}
		
	}
	
	/**
	 * method for disconnecting this user from the server, it send a disconnect command to the server then closes down the open assets of the client 
	 * @throws IOException
	 */
	public static void disconnectRequest() throws IOException{

		String disconnect = protocol.createMessage("disconnect", ChatGui.Username );
		try {
			outToServer.writeBytes(disconnect + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	/**
	 * action to close the client after confirmation is received from the server that disconnection was successful on that end 
	 */
	public void fullDisconnect() {
		try {
			outToServer.flush();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "You have disconnected !");
		System.exit(0);


	}
	
	/**
	 * setter for the ip address of the server
	 * @param host, the host's ip address
	 */
	public void setHost(String host) {  
		this.host = host;
	}

	/**
	 * setter for the port the server is using
	 * @param port, the servers port
	 */
	public void setPort(Integer port) { 
		this.port = port;
	}

	/**
	 * getter for list of curently online users 
	 * @return, arrraylist of currently online users 
	 */
	public static ArrayList<String> getOnlineUsers(){
		return onlineUsers;

	}

	/**
	 * getter for the username of this clients user 
	 * @return, the username 
	 */
	public String getUsername() {
		return thisUser;
	}


	/*
	 * 		Initialize socket and input/output streams
	 */
	public int start(){
		try {
			clientSocket = new Socket(this.host, this.port);
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			System.out.println(this.getResponse()[1]);
			return 0;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return 1;
		} catch (java.net.ConnectException e) {
			e.printStackTrace();
			return 2;
		} catch (IOException e) {
			e.printStackTrace();
			return 3;

		}
	}
	/*
	 * 		Close socket
	 */
	public void stop(){
		try {
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	



}
