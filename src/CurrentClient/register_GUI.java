package CurrentClient;

import javax.swing.*;

//import socket.Client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class register_GUI extends JFrame{
	private JTextField username;
	private JPasswordField password;
	private JPasswordField password2;
	private JLabel jlAll;
	private JLabel jlUsername;
	private JLabel jlPassword;
	private JLabel jlPassword2;
	private JButton register;
	private JButton cancel;
	
	public register_GUI() {
		this.setTitle("create a new account");
		initial();
		this.setLayout(null);
		this.setBounds(0,0,400,300);
		Image image = new ImageIcon("./Image/timg[1].jpg").getImage();
		this.setIconImage(image);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public void initial() {
		Container con = this.getContentPane();
		jlAll = new JLabel();
		jlAll.setBounds(0, 0, 400, 300);
		
		jlUsername = new JLabel("new username: ");
		jlUsername.setBounds(30, 30, 120, 20);
		jlPassword = new JLabel("new password: ");
		jlPassword.setBounds(30, 90, 120, 20);
		jlPassword2 = new JLabel("password comfirm: ");
		jlPassword2.setBounds(20, 150, 140, 20);
		
		username = new JTextField();
		username.setBounds(160, 30, 160, 30);
		username.setFont(new Font("Calibri",Font.BOLD,12));

		password = new JPasswordField();
		password.setBounds(160, 90, 160, 30);
		password.setFont(new Font("Calibri",Font.BOLD,12));
		
		password2 = new JPasswordField();
		password2.setBounds(160, 150, 160, 30);
		password2.setFont(new Font("Calibri",Font.BOLD,12));
		
		register = new JButton("OK");
		register.setBounds(60, 210, 100, 25);
			register.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ChatGui.client.start();
					String user = username.getText();
					String pass = String.valueOf(password.getPassword());
					if(username.equals("") || password.equals("")){
//						JOptionPane.showMessageDialog(ChatClientApp.frame,
//							    "Both username and password can not be empty!",
//							    "Warning",
//							    JOptionPane.WARNING_MESSAGE);
						return;
						
					}
					String[] response = ChatGui.client.signup(user, pass);
				
//					JOptionPane.showMessageDialog(ChatClientApp.frame,
//						    response[2],
//						    "Message from server",
//						    JOptionPane.INFORMATION_MESSAGE);
					if(response[1].equals("true")){
						JOptionPane.showMessageDialog(null, "Success",
								"Register", JOptionPane.PLAIN_MESSAGE);
						setVisible(false);
						dispose();
					}
				}
			});
			
//					String[] response = client.signup(username.getText(), String.valueOf(password.getPassword()));
//					if(response[1].equals("true")) {
//						JOptionPane.showMessageDialog(null, "Success",
//								"Register", JOptionPane.PLAIN_MESSAGE);
//						setVisible(false);
//						dispose();
//					}else if(response[1].equals("false1")) {
//						JOptionPane.showMessageDialog(null, 
//								"Username should be 5 to 20 characters long."
//								, "Error", JOptionPane.ERROR_MESSAGE);
//					}else if(response[1].equals("false2")) {
//						JOptionPane.showMessageDialog(null, 
//								"Password should be 5 to 32 characters long."
//								, "Error", JOptionPane.ERROR_MESSAGE);
//					}else if(response[1].equals("false3")) {
//						JOptionPane.showMessageDialog(null, 
//								"Pick another username, because another user has this one."
//								, "Error", JOptionPane.ERROR_MESSAGE);
//					}
//				}
//			});
		
		cancel = new JButton("cancel");
		cancel.setBounds(210, 210, 100, 25);
		
		jlAll.add(jlUsername);
		jlAll.add(jlPassword);
		jlAll.add(jlPassword2);
		jlAll.add(username);
		jlAll.add(password);
		jlAll.add(password2);
		jlAll.add(register);
		jlAll.add(cancel);

		con.add(jlAll);
	}
	
	public static void main(String[] args) {
		register_GUI register = new register_GUI();
	}

}
