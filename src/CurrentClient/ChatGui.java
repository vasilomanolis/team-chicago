package CurrentClient;

import javax.swing.*;

import Translate.Translator;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ChatGui {

	public static String Username = "";									// username of user 
	public static Client client = new Client();							//client used to communicate with server 
	public static JFrame MainWindow = new JFrame();						//JFrame for displaying GUI
	
	private static JButton B_GROUP = new JButton(); 					// Initialisation of various buttons 
	private static JButton B_DISCONNECT = new JButton();
	private static JButton B_TRANSLATE = new JButton();
	private static JButton B_HISTORY = new JButton();
	private static JButton B_SEND = new JButton();
	private static JButton B_COLOR = new JButton();
	
	private static JLabel L_MESSAGE = new JLabel("Message: ");       	// label and text area for sending messages 
	public static JTextField TF_Message = new JTextField(20);
	
	private static JLabel L_Conversation = new JLabel();				// label for displaying public messages 
	
	
	
	private static JLabel L_ONLINE = new JLabel();						// label, text area, and scrollpane for displaying online users									
	public static JList<String> JL_ONLINE;
	private static JScrollPane SP_ONLINE = new JScrollPane();
	
	private static JLabel L_LoggedInAs = new JLabel();					// labels + text field for displaying username
	private static JLabel L_LoggedInAsBox = new JLabel();
	public static JTextField TF_UserNameBox = new JTextField(20);
	
	private static JComboBox CB_TRANSLATE_FROM = new JComboBox();		// combo boxes for translate language selection 
	private static JComboBox CB_TRANSLATE_TO = new JComboBox();

	private static JLabel L_Translate_From = new JLabel();				// labels for translate cmbo boxes 
	private static JLabel L_Translate_To = new JLabel();



	private static Thread thread;																// thread for waiting for server communication 
	private static Timer timer;																	// timer for periodically pinging server
	private static JList<String> listChat;														// Jlist for displaying public chat 
	private static JScrollPane scrollPane;														// scrollpane for displaying public chat JList 
	private static boolean shutdown;
	public static Map<Integer, PrivateChatGui> chats = new HashMap<Integer, PrivateChatGui>(); 	// map for keeping track of private chats 


	
	public static void main(String[] args) {
		ChatGui a = new ChatGui();
		a.BuildMainWindow();
	}


	/**
	 * metohd for constructing the main window of the GUI, sets basic size and configuration
	 */
	public static void BuildMainWindow() { 

		MainWindow.setTitle( "Chat Box");
		MainWindow.setSize(800, 600);
		MainWindow.setLocation(220, 180);
		MainWindow.setResizable(false);
		ConfigureMainWindow();	// adds all the elements to the main window 
		MainWindow_Action();	// adds all action listners for buttons 
		MainWindow.setVisible(true);
		
		
		MainWindow.addWindowListener(new java.awt.event.WindowAdapter(){ // Listener that executes the disconnect method if the user simply closes the window 
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				try {
					client.disconnectRequest();
				} catch (Exception Y) {
					Y.printStackTrace();
				}
			}
			
		});
	}

	public static void ConfigureMainWindow() {

		MainWindow.setSize(800, 600);
		MainWindow.getContentPane().setLayout(null);
		
		String[] launguages = new String[] {"Afrikaans","Arabic", "Azerbaijani", "Belarusian", "Bulgarian", "Bengali", "Bosnian",  //String array for translate combo boxes 
				 "Catalan", "Cebuano", "Czech", "Welsh", "Danish", "German", "Greek", "English", "Esperanto", 
				 "Spanish", "Estonian", "Basque", "Persian", "Finnish", "French", "Irish", "Galician", 
				  "Gujarati", "Hausa", "Hindi", "Hmong", "Croatian", "Haitian", "Creole", "Hungarian", 
				 "Armenian", "Indonesian", "Igbo", "Icelandic", "Italian", "Hebrew", "Japanese", "Javanese", 
				 "Georgian", "Kazakh", "Khmer", "Kannada", "Korean", "Latin", "Lao", "Lithuanian", 
				 "Latvian", "Punjabi", "Malagasy", "Maori", "Macedonian", "Malayalam", "Mongolian", 
				 "Marathi", "Malay", "Maltese", "Myanmar (Burmese)", "Nepali", "Dutch", "Norwegian", 
				"Chichewa", "Polish", "Portuguese", "Romanian", "Russian", "Sinhala", "Slovak", 
				 "Slovenian", "Somali", "Albanian", "Serbian", "Sesotho", "Sudanese", "Swedish", "Swahili", 
				 "Tamil", "Telugu", "Tajik", "Thai", "Filipino", "Turkish", "Ukrainian", "Urdu", "Uzbek", "Vietnamese", 
				"Yiddish","Yoruba", "Chinese Simplified", "Chinese Traditional", "Zulu"};
				
		CB_TRANSLATE_FROM = new JComboBox(launguages);	//configuration of translate from combo box 
		CB_TRANSLATE_FROM.setBounds(155, 520, 110, 30);
		CB_TRANSLATE_FROM.setSelectedIndex(14);
		MainWindow.getContentPane().add(CB_TRANSLATE_FROM);
		
		CB_TRANSLATE_TO = new JComboBox(launguages);	//configuration of translate to combo box 
		CB_TRANSLATE_TO.setBounds(385, 520, 110, 30);
		MainWindow.getContentPane().add(CB_TRANSLATE_TO);
		
		L_Translate_From.setBounds(55, 520, 100, 30); 	//configuration of translate from label 
		L_Translate_From.setText("Translate from: ");
		L_Translate_From.setFont(new Font("Arial", Font.BOLD, 10));
		MainWindow.getContentPane().add(L_Translate_From);
		
		L_Translate_To.setBounds(290, 520, 90, 30);		//configuration of translate to label
		L_Translate_To.setFont(new Font("Arial", Font.BOLD, 10));
		L_Translate_To.setText("Translate To: ");


		B_SEND.setForeground(Color.DARK_GRAY); 	//configuration of send button 
		B_SEND.setText("SEND");
		B_SEND.setFont(new Font("Arial", Font.BOLD, 10));
		MainWindow.getContentPane().add(B_SEND);
		B_SEND.setBounds(550, 430, 81, 25);
		
		B_HISTORY.setForeground(Color.DARK_GRAY);	//configuration of history button
		B_HISTORY.setText("VIEW HISTORY");
		B_HISTORY.setFont(new Font("Arial", Font.BOLD, 10));
		MainWindow.getContentPane().add(B_HISTORY);
		B_HISTORY.setBounds(200, 60, 150, 25);

		B_DISCONNECT.setForeground(Color.WHITE); //configuration of disconnect button
		B_DISCONNECT.setBackground(Color.RED);
		B_DISCONNECT.setFont(new Font("Calibri",Font.BOLD,12));
		B_DISCONNECT.setText("DISCONNECT");
		MainWindow.getContentPane().add(B_DISCONNECT);
		B_DISCONNECT.setBounds(640, 10, 130, 25);

		B_COLOR.setText("choose color");	 //configuration of color button
		B_COLOR.setFont(new Font("Arial", Font.PLAIN, 12));
		B_COLOR.setBounds(450, 10, 120, 25);
		MainWindow.getContentPane().add(B_COLOR);

		B_TRANSLATE.setForeground(Color.DARK_GRAY); //configuration of translate button
		B_TRANSLATE.setText("TRANSLATE");
		B_TRANSLATE.setFont(new Font("Arial", Font.BOLD, 10));
		MainWindow.getContentPane().add(B_TRANSLATE);
		B_TRANSLATE.setBounds(550, 470, 120, 25);

		B_GROUP.setForeground(Color.DARK_GRAY); //configuration of group button
		B_GROUP.setText("GROUP");
		B_GROUP.setFont(new Font("Arial", Font.BOLD, 10));
		MainWindow.getContentPane().add(B_GROUP);
		B_GROUP.setBounds(650, 430, 75, 25);

		L_MESSAGE.setText("Message:");		// configuration of message label 
		L_MESSAGE.setFont(new Font("Arial", Font.BOLD, 10));
		MainWindow.getContentPane().add(L_MESSAGE);
		L_MESSAGE.setBounds(30, 450, 60, 20);

		TF_Message.setForeground(new java.awt.Color(0, 0, 255)); // Configuration of message text box 
		TF_Message.requestFocus();
		MainWindow.getContentPane().add(TF_Message);
		TF_Message.setBounds(100, 450, 400, 60);

		L_Conversation.setHorizontalAlignment(SwingConstants.CENTER); // Configuration of conversation (public chat) label 
		L_Conversation.setText("Conversation");
		MainWindow.getContentPane().add(L_Conversation);
		L_Conversation.setBounds(60, 70, 140, 16);


		listChat = new JList<String>(); // configuration of public chat display, creates a JList which is put inside of a scrollpane 
		listChat.setValueIsAdjusting(true);
		listChat.setModel(new DefaultListModel<String>());
		scrollPane = new JScrollPane(listChat);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(100, 90, 400, 330);
		scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
			private int prevMax = 0;
			public void adjustmentValueChanged(AdjustmentEvent e) {  
				if(e.getAdjustable().getMaximum() != prevMax){
					prevMax = e.getAdjustable().getMaximum();
					e.getAdjustable().setValue(e.getAdjustable().getMaximum());
				}

			}
		});

		MainWindow.getContentPane().add(scrollPane);


		L_ONLINE.setHorizontalAlignment(SwingConstants.CENTER); //configuration of online users label 
		L_ONLINE.setText("Currently Online");
		L_ONLINE.setToolTipText("");
		MainWindow.getContentPane().add(L_ONLINE);
		L_ONLINE.setBounds(550, 70, 150, 16);

		JL_ONLINE = new JList<String>();	// configuration of online users display, creates a JList which is put inside of a scrollpane 
		JL_ONLINE.setValueIsAdjusting(true);
		JL_ONLINE.setModel(new DefaultListModel<String>());
		JL_ONLINE.setForeground(new java.awt.Color(0, 0, 255));
		SP_ONLINE = new JScrollPane(JL_ONLINE);
		SP_ONLINE.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		SP_ONLINE.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
			private int prevMaxOnline = 0;
			public void adjustmentValueChanged(AdjustmentEvent e) {  
				if(e.getAdjustable().getMaximum() != prevMaxOnline){
					prevMaxOnline = e.getAdjustable().getMaximum();
					e.getAdjustable().setValue(e.getAdjustable().getMaximum());
				}

			}
		});

		MainWindow.getContentPane().add(SP_ONLINE);
		SP_ONLINE.setBounds(550, 90, 200, 330);

		L_LoggedInAs.setFont(new java.awt.Font("Tahoma", 0, 12)); // configuration of logged in as label 
		L_LoggedInAs.setText("Currently Logged In As");
		MainWindow.getContentPane().add(L_LoggedInAs);
		L_LoggedInAs.setBounds(20, 10, 140, 15);

		L_LoggedInAsBox.setHorizontalAlignment(SwingConstants.CENTER); // configuration of logged in as box
		L_LoggedInAsBox.setFont(new java.awt.Font("Tahoma", 0, 12));
		L_LoggedInAsBox.setForeground(new java.awt.Color(255, 0, 0));
		L_LoggedInAsBox.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		MainWindow.getContentPane().add(L_LoggedInAsBox);
		L_LoggedInAsBox.setBounds(170, 10, 160, 20);
	}

	/**
	 * method for checking messages, creates a new thread which constantly checks for response from the server, 
	 * then using 'if' statements to check the first element of the 'response' array, it takes the appropriate action 
	 */
	public static void StartCheckingMessages(){

		thread = new Thread(new Runnable() {

		

			@Override
			public void run() {
				
				while(!shutdown){ // boolean value indicating weather the server is shutting down, and so there is no need to check for more messages 
					
					String[] response = client.getResponse();
					
					
					if(response[0].equals("get-message")){// a message is revived to check that the list of public messages is up to date 
						DefaultListModel<String> model = (DefaultListModel<String>) listChat.getModel();
						for(int i=1; i<response.length; i=i+4){
							if(client.offset < Integer.parseInt(response[i])){
								client.offset = Integer.parseInt(response[i]);
								model.addElement(String.format("%s @ (%s): %s", response[i+1], response[i+2], response[i+3]));
								try {
									Thread.sleep(10);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}

					}
					if(response[0].equals("send-message")){ // a public message has been sent 
						if(response[1].equals("true")){
							System.out.println("Message sent.");
						}else{
							JOptionPane.showMessageDialog(ChatGui.MainWindow,
									"Cannot send message!",
									"Error",
									JOptionPane.WARNING_MESSAGE);
						}
					}

					if(response[0].equals("update-list")){ // message recived that the list of online users must be updated 
						DefaultListModel<String> model = (DefaultListModel<String>) JL_ONLINE.getModel();

						if(response[1].equals("add-user")){ // a new user has come online
							model.addElement(String.format("%s" ,response[2]));
							JL_ONLINE = new JList<String>(model);

							System.out.println(response[2]);

							if(!response[2].equals(Username)){ // if username sent is not the username of this user 
								Client.getOnlineUsers().add(response[2]);
								
							}
						}
						if(response[1].equals("remove-user")){ // a user has gone offline 
							
							if(response[2].equals(Username)){	// if the user going offline is this client, shutdown 
								System.out.println("shutdown party");
								client.fullDisconnect();
								shutdown = true;
								
							} else {	// else the clients list of users and the GUI list is updated 
							model.removeElement(String.format("%s" ,response[2]));
							JL_ONLINE = new JList<String>(model);
							Client.getOnlineUsers().remove(response[2]);
						}
						}



					}
					if(response[0].equals("create-group")){ // message received that a new private chat has been created 
						PrivateChatGui chat = new PrivateChatGui(client, Integer.valueOf(response[1]), response[2]);
						chats.put(Integer.valueOf(response[1]), chat);

						for (int i = 3; i < response.length; i++) {
							chats.get(Integer.valueOf(response[1])).addUser(response[i]);
						}



					}
					if(response[0].equals("send-private-message")){ 	// message that a message has been received for a private chat 
						chats.get(Integer.valueOf(response[1])).updateMessages(response[2], response[3], response[4]);


					}
					if(response[0].equals("add-to-chat")){	//message received that a user has joined a private chat
						for (int i = 2; i < response.length; i++) {
							chats.get(Integer.valueOf(response[1])).addUser(response[i]);
						}


					}
					if(response[0].equals("history-request")){ // if message is recived that the server has been shutdown 
						String[] messages = response[1].split("%##%");
						new ChatHistory_GUI(client, messages);


					}
					if(response[0].equals("remove-from-chat")) { //message received that a user has  left a private chat
						for (int i = 2; i < response.length; i++) {
							chats.get(Integer.valueOf(response[1])).removeUser(response[i]);
						}


					}
					if(response[0].equals("server-shutdown")){ // if message is received that the server has been shutdown 
						JOptionPane.showMessageDialog(MainWindow, "Server manually shutdown, goodbye!","Warning",JOptionPane.WARNING_MESSAGE);
						client.fullDisconnect();


					}
					if(response[0].equals("not-responding")){ // if message is received that the server is not responding 
						JOptionPane.showMessageDialog(MainWindow, "Server is not responding, PANIC!!","Warning",JOptionPane.WARNING_MESSAGE);
						client.fullDisconnect();


					}
				}
			}
		});

		thread.start(); //timer so that client periodically pings server for an updated list of public messages, also used to check server is responding 
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				client.get_message();
			}
		}, 1000, 2000);

	}
	public static void StopCheckingMessages(){ // method to stop checking for public messages 
		timer.cancel();
	}


	/**
	 * this method adds action listeners to all of the buttons on the main window
	 */
	public static void MainWindow_Action() {

		B_COLOR.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Color color = Color.WHITE;
				color = JColorChooser.showDialog(null, "pick your color", color);
				if(color==null) {
					color = (Color.WHITE);
				}
				MainWindow.getContentPane().setBackground(color);

			}
		});

		B_SEND.addActionListener(new java.awt.event.ActionListener() { //action listener for the send button, calls the send method 
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ACTION_B_SEND();
			}
		}

				);

		B_DISCONNECT.addActionListener( // action listener for the disconnect button, calls the disconnect method 

				new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						ACTION_B_DISCONNECT();


					}
				}

				);
		
		B_HISTORY.addActionListener(	// action listener for the history button, calls the history request  method from the client 

				new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						client.historyRequest();


					}
				}

				);
	
	

		B_TRANSLATE.addActionListener(new java.awt.event.ActionListener() { //action listener for the translate button, call the translate functionality from the translate package 

			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					String translate = TF_Message.getText();
					String translated = Translator.translate(String.valueOf(CB_TRANSLATE_FROM.getSelectedItem()), String.valueOf(CB_TRANSLATE_TO.getSelectedItem()), translate );
					if(translated.equals("browser (or proxy) sent a request that this server could not understand.</p>")) {
						JOptionPane.showMessageDialog(MainWindow, "could not translate","Warning",JOptionPane.WARNING_MESSAGE);
					}else {
					TF_Message.setText(translated);
					}				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

				);

		B_GROUP.addActionListener(	//action listener for the group button, opens a new MakeGroup_GUI

				new java.awt.event.ActionListener() {

					public void actionPerformed(java.awt.event.ActionEvent evt) {
						new MakeGroup_GUI(client, Client.getOnlineUsers(), Username);

					}
				}

				);
	}

	/**
	 * method for sending a public messages, calls send message from the client to send the message to the the server, 
	 * and resets the message box content to an empty string 
	 */
	public static void ACTION_B_SEND() {
		String msg = TF_Message.getText();
		if(msg.equals("")){ // if message box is empty, do nothing 
	
		}else {
		TF_Message.setText("");
		client.send_message(msg);
		TF_Message.grabFocus();
		}
	}




	/**
	 * the disconnect method calls the client to make a disconnect request of the server
	 */
	public static void ACTION_B_DISCONNECT() {
		try {
			StopCheckingMessages();
			client.disconnectRequest();
		} catch (Exception Y) {
			Y.printStackTrace();
		}
	}


	/**
	 * The method called when a user sucesfully logs in, sets the username to the one given, 
	 * displays the chat history and starts pinging the server asking for public messages.
	 * @param user, the username of the user 
	 * @param chatLog, the history of the public chat 
	 */
	public static void loggedIn(String user, String chatLog) {
		B_SEND.setEnabled(true);
		B_DISCONNECT.setEnabled(true);
		L_LoggedInAsBox.setText(user);
		Username = user;
		
		String[] chatlogDisplay = chatLog.split("%##%");
		
		DefaultListModel jListModel = new DefaultListModel();
		listChat.setModel(jListModel);
		for(String str:  chatlogDisplay){
			jListModel.addElement(str +'\n');
			
			
		}

		jListModel.addElement("\n"+"----------------------HISTORY-------------------------"+"\n");
		
		StartCheckingMessages();
	}




}
