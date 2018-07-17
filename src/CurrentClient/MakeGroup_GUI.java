package CurrentClient;



import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

public class MakeGroup_GUI {

	private ArrayList<String> onlineUsers;
	private Client client;
	private String username;
	

	//construstor 
	public MakeGroup_GUI(Client client, ArrayList<String> onlineUsers, String username){
		this.onlineUsers = onlineUsers;
		this.client = client;

		JFrame gui = new JFrame("Make private Chats");
		JPanel panel = new JPanel();
		
		
		
		gui.setSize(330, 350);

		//creation of labels 
		JLabel usersLabel = new JLabel("Users online:");
		usersLabel.setBounds(40,5,200,50);
		
		JLabel nameLabel = new JLabel("Chatroom name:");
		nameLabel.setBounds(200, 50 ,200, 50);
		
		//creation of field for group name input
		JTextField chatName = new JTextField();
		chatName.setBounds(180, 85, 130, 35);
		chatName.setFont(new Font("Arial", Font.PLAIN, 10));
		chatName.setText(username + "'s chat Room");
		chatName.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {	
				chatName.setText("");
				chatName.removeMouseListener(this);
			}
		});

		
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
		JButton add = new JButton("Make Chat");
		add.setBounds(195, 120, 100, 50);

		JButton cancel = new JButton("Cancel");
		cancel.setBounds(195, 180, 100, 50);

		//adding components to panel
		panel.add(usersLabel); panel.add(nameLabel); panel.add(chatName);
		panel.add(usersScroller); panel.add(add); panel.add(cancel);

		//ActionListner for buttons 
		ActionListener button = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == cancel) {
					gui.dispose();

				} else if (e.getSource() == add) {

					int [] selected = usersList.getSelectedIndices();
					String[] users = new String[selected.length];
					
					for (int i = 0; i < selected.length; i++) {
						users[i] = (String) usersList.getModel().getElementAt(selected[i]);


					}
					Client.startGroup(username, users, chatName.getText());
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
		new MakeGroup_GUI(client, list, "goodbye");

	}
}
