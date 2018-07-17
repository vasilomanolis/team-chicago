package CurrentServer;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JTextArea;

import Database.Database;
import Protocol.SimpleProtocol;

/**
 * 
 * @author Chicago the ServerThreadClass is a thread created for a client when
 *         they connect, it handles all the requests the client sends such as
 *         registering, logging in, sending/receiving messages
 *
 */
public class ServerThread extends Thread {
	
	private JTextArea ChatLog = new JTextArea(); // log for displaying info on server_GUI

	private Socket clientSock; // the socket that connects the serverThread to the client 

	private DataOutputStream outToClient; // an output stream for sending to the client

	private BufferedReader inFromClient; // a bufferedReader for receiving from the client

	private SimpleProtocol protocol = new SimpleProtocol(); // a protocol for encoding/decoding communication to/from the client 


	String username;

	private ArrayList<String> privateStorage = new ArrayList<>();

	private DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private Database database = new Database();

	private boolean first = true;

	// Initialises the socket
	public ServerThread(Socket clientSock, JTextArea ChatLog) {
		this.clientSock = clientSock;
		this.ChatLog = ChatLog;
	}

	// the run method for ServerThread
	public void run() {

		/**
		 * initialises the outputstream and BuffferedReader, then sends a
		 * message to the client to confirm it is connected
		 */

		String str = "coneccted!!";
		System.out.println(str);
		ChatLog.append(str + "\n");
		try {
			outToClient = new DataOutputStream(clientSock.getOutputStream());
			inFromClient = new BufferedReader(new InputStreamReader(new DataInputStream(clientSock.getInputStream())));
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		String welcome = protocol.createMessage("send-message", "Welcome to the server!");

		try {
			outToClient.writeBytes(welcome + "\n");
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// infinite loop that waits for contact from the client
		try {
			while (true) {

				try {
					if (inFromClient.ready()) { // if there is something in the
						// bufferedReader, decode it
						String[] in = protocol.decodeMessage(inFromClient.readLine());

						// sign up process
						if (in[0].equals("sign-up")) {

							String username = in[1];
							String password = in[2];

							boolean validUsername = true; // boolean for being in correct format

							boolean validPassword = true; // boolean for password being in the correct format 

							boolean unique = true; // boolean for username not being taken 


							if (username.length() < 5 || username.length() > 20) { // check for correct username length 

								validUsername = false;
								String response = protocol.createMessage("sign-up", "false",
										"Usernames must be between 5 and 20 characters");
								ChatLog.append("sign-up failed" + "\n");		

								try {
									outToClient.writeBytes(response + "\n");
								} catch (IOException e1) {
									e1.printStackTrace();
								}

							}

							if (password.length() < 8 || password.length() > 32) { // check for correct password length 

								validPassword = false;
								String response = protocol.createMessage("sign-up", "false",
										"Passwords must be between 8 and 32 characters");
								ChatLog.append("sign-up failed" + "\n");

								try {
									outToClient.writeBytes(response + "\n");
								} catch (IOException e1) {
									e1.printStackTrace();
								}

							}

							if (validUsername && validPassword && !database.isExistUser(username)) { // check to see if username already exists 

								unique = false;

								String response = protocol.createMessage("sign-up", "false", "Username already exists");
								ChatLog.append("sign-up failed" + "\n");

								try {
									outToClient.writeBytes(response + "\n");
								} catch (IOException e1) {
									e1.printStackTrace();
								}

							}

							if (validUsername && validPassword && unique) { // password and username are valid and username is not taken 

								String response = protocol.createMessage("sign-up", "true", "sign up succesfull");
								ChatLog.append("sign-up succesfull" + "\n");

								database.createUser(username, password);
								try {
									outToClient.writeBytes(response + "\n");
								} catch (IOException e1) {
									e1.printStackTrace();
								}

							}

						}



						// sign in method
						if (in[0].equals("sign-in")) {
					
							ChatLog.append("trying to sign in normally" + "\n");


							username = in[1];
							String password = in[2];

							if (in[3].equals("true")){ //process if sign in is anonymous 
								try {
									
									username = Server.newAnnon(); 	// creation of new anon username 



									String response = protocol.createMessage("sign-in", "true", username,
											database.getHistory(false));
									ChatLog.append("sign-in succesfull" + "\n");
							
									try {
										outToClient.writeBytes(response + "\n");
									} catch (IOException e1) {
										e1.printStackTrace();
									}

									Server.addUser(username, "dummypass", clientSock);

								} catch (Exception e1) {
									e1.printStackTrace();
								}


							}

							if (!Server.userIdMap.containsKey(username)|| in[3].equals("true")) { //check if user is already signed in or sign in is anonymous 
								if (database.isValidUser(username, password) || in[3].equals("true")) { //check if pass is correct and if user is already signed in or sign in is anonymous 

									if( in[3].equals("false")) { // process if sign in is not anonymous 
										String response = protocol.createMessage("sign-in", "true", "sign in succesfull",
												database.getHistory(false));
										ChatLog.append("sign-in succesfull" + "\n");

										try {
											outToClient.writeBytes(response + "\n");
										} catch (IOException e1) {
											e1.printStackTrace();
										}

										try {
											Server.addUser(username, password, clientSock);
										} catch (Exception e1) {
											e1.printStackTrace();
										}
									}

									// user has succesfully signed in,
									while (true) {
										if(first) {
											System.out.println("in the loop!");
											first  = false;
										}
										
										String[] newIn = protocol.decodeMessage(inFromClient.readLine());

										if (newIn[0].equals("get-message")) {
											if (Integer.parseInt(newIn[1]) == -1) { // show all messages if offset is -1

												if (Server.messagesO.isEmpty()) { // do nothing
													

												} else { // show all messages
													for (int i = 0; i < Server.messagesO.size(); i++) {
														String updatedMessageO = protocol.createMessage("get-message",
																Integer.toString(i), Server.messagesO.get(i).getSender(),
																Server.messagesO.get(i).getTimeStamp(),
																Server.messagesO.get(i).getContent());
														outToClient.writeBytes(updatedMessageO + "\n");

													}
												}

											} else { // Receive current message
												for (int i = Integer.parseInt(newIn[1]); i < Server.messagesO.size(); i++) {
													String updatedMessageO = protocol.createMessage("get-message",
															Integer.toString(i), Server.messagesO.get(i).getSender(),
															Server.messagesO.get(i).getTimeStamp(),
															Server.messagesO.get(i).getContent());
													outToClient.writeBytes(updatedMessageO + "\n");

												}
											}

										}

										if (newIn[0].equals("send-message")) { // add add sent message to arrayList 

											String sendMessage = protocol.createMessage("send-message", "true",
													Integer.toString(Server.messagesO.size()));
											Message messageObj = new Message(username, dateFormat.format(new Date()),
													newIn[1]);
											Server.messagesO.add(messageObj);
											outToClient.writeBytes(sendMessage + "\n");
											ChatLog.append("send message " + newIn[1] + "\n");

											String time = dateFormat.format(new Date());
											database.saveLog(username, newIn[1], time);


										}
										if (newIn[0].equals("create-group")) { //request to start new private chat with specified users 

											ArrayList<String> chatUsers = new ArrayList<String>();
											String chatName = newIn[1];

											for (int i = 2; i < newIn.length; i++) {
												chatUsers.add(newIn[i]);

											}

											this.privateStorage = chatUsers;

											Server.makeChat(chatName, chatUsers);
											ChatLog.append("create a new group" + "\n");

										}



										if (newIn[0].equals("send-private-message")) { // Request to send message to private chat 

											
											Server.updatePrivateMessage(Integer.valueOf(newIn[1]), username,
													dateFormat.format(new Date()), newIn[2]); // the updatePrivateMessage method is called
											database.groupLog(privateStorage, newIn[2]);
											ChatLog.append("send private message " + newIn[2] + "\n");

										}
										if (newIn[0].equals("add-to-chat")) { // request toa dd user(s) to a private chat

											ArrayList<String> chatUsers = new ArrayList<String>();

											for (int i = 2; i < newIn.length; i++) { 	// forms an an arraylist of the users to be added, skipping the first two positions 
																						// as these are the command and chat id

												chatUsers.add(newIn[i]);
											}

											Server.addToChat(Integer.valueOf(newIn[1]), chatUsers); // calls the add to chat method with the chat id, and list of users 

										}
										if (newIn[0].equals("history-request")) { // request to be sent the public chat history for searching 
											
											String response = protocol.createMessage("history-request", database.getHistory(true));
											
									
											try {
												outToClient.writeBytes(response + "\n");
											} catch (IOException e1) {
												e1.printStackTrace();
											}
											ChatLog.append("history request sent" + "\n");
										}

										if (newIn[0].equals("disconnect")) { // request to disconnect client completely 
											Server.disconnect(newIn[1]);

										}
										if (newIn[0].equals("leave-chat")) { // request to remove this user from a specific private chat 

											Server.disconnectFromChat(Integer.valueOf(newIn[1]), newIn[2]);

										}

									}


								} else { // response if password is incorrect
									String response = protocol.createMessage("sign-in", "false", "password/username combo is incorrect");
									ChatLog.append("sign-in failed" + "\n");
									try {
										outToClient.writeBytes(response + "\n");
									} catch (IOException e1) {
										e1.printStackTrace();
									}

								}
							}else { // response if user is already signed in 
								String response = protocol.createMessage("sign-in", "false", "password/username combo is incorrect");
								ChatLog.append("sign-in failed" + "\n");
								try {
									outToClient.writeBytes(response + "\n");
								} catch (IOException e1) {
									e1.printStackTrace();
								}

							}
						} 

					}
				} catch (IOException | SQLException e) {
					e.printStackTrace();
				}

			}

		} finally {
			try {
				clientSock.close();
				outToClient.close();
				inFromClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
