package CurrentClient;

import java.awt.Color;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

/**
 * The PrivateChatGui class is used for private chat rooms between users, 
 * it can be created with any number of initial users present and any number can be added 
 * @author Chicago
 *
 */
public class PrivateChatGui extends JFrame {
	private Client client;							// the client associated with this private chat 
	private int id;									// the id of this chat
	private String chatName;						// name of this chat
	
	private JList<String> listChat;					// JList and scrollPane for displaying messages
	private JScrollPane scrollPane; 
	
	private JLabel L_ONLINE;						// label, JList, model and scrollPane for displaying users present
	private JList<String> JL_ONLINE;  				 
	private JScrollPane SP_ONLINE;
	private DefaultListModel JL_ONLINE_MODEL;
	
	private JTextField TF_Message;					// text field for inputing messages to send 
	
	private JButton B_ADD;							// Deceleration of buttons 
	private JButton B_LEAVE;
	private static JButton B_SEND;
	
	
	private ArrayList<String> usersPresent = new ArrayList<String>();	// arraylist of users present in this chat 


	/**
	 * Constructor for a PrivateChatGui
	 * @param client, sets the client so the private chat may call its methods 
	 * @param id, sets the id of this private chat 
	 * @param chatName, the name of the chat 
	 */
	public PrivateChatGui(CurrentClient.Client client, int id, String chatName) {
		this.client = client;
		this.id = id;
		this.chatName = chatName;
		buildView();
	}

	public PrivateChatGui() {
		buildView();
	}


	/**
	 * the build view method constructs the GUi, configuring the frame and calling methods to add the other elements and add action listeners to the buttons 
	 */
	private void buildView() {
		this.setTitle(chatName);
		this.setSize(500, 320);
		this.setLocation(220, 180);
		this.setResizable(false);
		this.getContentPane().setLayout(null);
		configure();
		addActions();
		this.setVisible(true);
		
		this.addWindowListener(new java.awt.event.WindowAdapter(){	// Listener that executes the LeavePrivatChat method if the user simply closes the window 
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				Client.leavePrivateChat(id);
				dispose();
			}
			
		});

	}


	private void configure() {


		// creation of JList for displaying the chat messages 
		listChat = new JList<String>();
		listChat.setValueIsAdjusting(true);
		listChat.setModel(new DefaultListModel<String>());
		scrollPane = new JScrollPane(listChat);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(10, 90, 330, 180);
		scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
			private int prevMax = 0;
			public void adjustmentValueChanged(AdjustmentEvent e) {  
				if(e.getAdjustable().getMaximum() != prevMax){
					prevMax = e.getAdjustable().getMaximum();
					e.getAdjustable().setValue(e.getAdjustable().getMaximum());
				}

			}
		});

		this.getContentPane().add(scrollPane);

		// creation of JLabel for displaying users present in the chat 
		L_ONLINE = new JLabel();
		L_ONLINE.setHorizontalAlignment(SwingConstants.CENTER);
		L_ONLINE.setText("Users present:");
		L_ONLINE.setToolTipText("");
		this.getContentPane().add(L_ONLINE);
		L_ONLINE.setBounds(350, 70, 130, 16);

		// Creation of JList for displaying users whom are in the chat 
		JL_ONLINE = new JList<String>();
		JL_ONLINE.setValueIsAdjusting(true);
		JL_ONLINE.setModel(new DefaultListModel<String>());
		JL_ONLINE.setForeground(new java.awt.Color(0, 0, 255));
		SP_ONLINE = new JScrollPane(JL_ONLINE);
		SP_ONLINE.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		SP_ONLINE.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		SP_ONLINE.setBounds(350, 90, 130, 180);
		scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
			private int prevMaxOnline = 0;
			public void adjustmentValueChanged(AdjustmentEvent e) {  
				if(e.getAdjustable().getMaximum() != prevMaxOnline){
					prevMaxOnline = e.getAdjustable().getMaximum();
					e.getAdjustable().setValue(e.getAdjustable().getMaximum());
				}

			}
		});

		this.getContentPane().add(SP_ONLINE);

		// creation of JTextField for inputing messages to send to other users in the chat 
		TF_Message = new JTextField();
		TF_Message.setForeground(new java.awt.Color(0, 0, 255));
		TF_Message.requestFocus();
		this.getContentPane().add(TF_Message);
		TF_Message.setBounds(70, 4, 260, 30);

		// creation of buttin for sending messages 
		B_SEND = new JButton();
		B_SEND.setForeground(Color.DARK_GRAY);
		B_SEND.setText("SEND");
		this.getContentPane().add(B_SEND);
		B_SEND.setBounds(250, 40, 81, 25);
		
		// creration of button for leaving the chat 
		B_LEAVE = new JButton();
		B_LEAVE.setForeground(Color.DARK_GRAY);
		B_LEAVE.setText("LEAVE");
		this.getContentPane().add(B_LEAVE);
		B_LEAVE.setBounds(50, 40, 81, 25);

		// creation of button for adding more users to the private chat 
		B_ADD = new JButton();
		B_ADD.setForeground(Color.DARK_GRAY);
		B_ADD.setText("ADD");
		this.getContentPane().add(B_ADD);
		B_ADD.setBounds(380, 40, 81, 25);


	}

	/**
	 * adds action listeners to all of the buttons 
	 */
	private void addActions() {
		
		
		B_SEND.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				String msg = TF_Message.getText(); 
				if(msg.equals("")){ // if there is no text, do nothing 
				}
				TF_Message.setText("");
				client.send_Private_message(id, msg);	//calls the client to send the message to the chat, identified by it's id 
				TF_Message.grabFocus();

			}
		}

				);

		B_ADD.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ArrayList<String> notInChat = Client.getOnlineUsers();
				notInChat.removeAll(usersPresent);
				new AddToGroup_GUI(client, notInChat, id); // opens a new addToGroup_GUI using the clients list of currently online users with the users currently in this caht removed 


			}
		}

				);
		B_LEAVE.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
			Client.leavePrivateChat(id);	//calls the client to message the server for this client to be removed from the chat 
			dispose();
			}
		}

				);





	}
	/**
	 * the updateMessages method is called when the client revives a message from the server, telling it that a message has been sent to this private chat,
	 * it then adds the message to the model of the listChat JList, which displays the messages in this chat gui   
	 * @param sender, the user who sent the message 
	 * @param time, the time the message was sent 
	 * @param message, the content of the message 
	 */
	public void updateMessages(String sender, String time, String message) {
		DefaultListModel<String> model = (DefaultListModel<String>) listChat.getModel();
		model.addElement(String.format("%s @ (%s): %s", sender, time, message));
		if (sender.equals(client.getUsername())) {
			
			
		}

	}

	/**
	 * the addUser method is called when a new user needs to be added to the list of users in this chat, 
	 * it takes the username given and adds it to the list of current users in the chat 
	 * @param username, the username to be added to the list,as a string 
	 */
	public void addUser(String username) {
		usersPresent.add(username);
		DefaultListModel<String> model = (DefaultListModel<String>) JL_ONLINE.getModel();
		model.addElement(username);
		JL_ONLINE = new JList<String>(model);


	}
	
	

	public static void main(String[] args) {
		new PrivateChatGui();
	}

	/**
	 * method from removing a user from the list of users present in the chat, 
	 * used when the client receives a message that a user has left the chat 
	 * @param username, the username of the user who has left the chat 
	 */
	public void removeUser(String username) {
		usersPresent.remove(username);
		DefaultListModel<String> model = (DefaultListModel<String>) JL_ONLINE.getModel();
		model.removeElement(username);
		JL_ONLINE = new JList<String>(model);


	}
	
	



}
