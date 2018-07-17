package CurrentClient;



import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * @author rxm448
 * The AddUserView class is for adding users to an existing group chat, it is very similar to CreateGroupView
 */

public class AddToGroup_GUI {

	private ArrayList<String> onlineUsers;
	private Client client;
	private String username;
	private int id;
	

	//Constructor 
	public AddToGroup_GUI(Client client, ArrayList<String> onlineUsers, int id){
		this.onlineUsers = onlineUsers;
		this.client = client;
		this.id = id;

		JFrame gui = new JFrame("Make private Chatroom");
		JPanel panel = new JPanel();
		
		
		
		gui.setSize(330, 350);

		//creation of labels 
		
		JLabel usersLabel = new JLabel("Users online:");
		usersLabel.setBounds(40,5,200,50);
		
		
		
		//creation of list of online users 
		DefaultListModel<String> model = new DefaultListModel<String>();
		for (int i = 0; i < onlineUsers.size(); i++) {
			model.addElement(onlineUsers.get(i));
		}
		JList usersList = new JList(model);
		usersList.setLayoutOrientation(JList.VERTICAL_WRAP);
		usersList.setVisibleRowCount(-1);
		JScrollPane usersScroller = new JScrollPane(usersList);
		usersScroller.setBounds(10, 41, 140, 250);

		//creation of buttons 
		JButton add = new JButton("Add selected");
		add.setBounds(195, 120, 100, 50);

		JButton cancel = new JButton("Cancel");
		cancel.setBounds(195, 180, 100, 50);

		//adding components to panel
		panel.add(usersLabel);
		panel.add(usersScroller); panel.add(add); panel.add(cancel);

		//ActionListner for buttons 
		ActionListener button = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == cancel) {
					gui.dispose();

				} else if (e.getSource() == add) {

					int [] selected = usersList.getSelectedIndices();
					String[] users = new String[selected.length +1];
					users[0] = String.valueOf(id);
					for (int i = 0; i < selected.length; i++) {
						users[i+1] = (String) usersList.getModel().getElementAt(selected[i]);


					}
					client.addChatUser(users); 
					gui.dispose();
				}
			}




		};

		//linking ActionListner to buttons 
		cancel.addActionListener(button);
		add.addActionListener(button);



		//formatting 
		panel.setLayout(null);
		gui.setResizable(false);
		gui.add(panel, BorderLayout.CENTER);
		gui.setLocationRelativeTo(null);
		gui.setVisible(true);

		gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}

	//main method 
	public static void main(String[] args) throws Exception {
		javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());

		Client client = new Client();
		ArrayList<String> list = new ArrayList<String>();
		new AddToGroup_GUI(client, list, 0);

	}
}
