package CurrentClient;

import javax.swing.*;


//import Database.DBHelper;
import Protocol.SimpleProtocol;
//import entity.User;
//import socket.ChatClient;
//import socket.Client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class login_GUI extends JFrame{
	private JTextField username;
	private JPasswordField password;
	private JLabel jlAll;
	private JLabel jlUsername;
	private JLabel jlPassword;
	private JButton login;
	private JButton register;
	private JButton anonSignIn;
	private JButton jbConnect;
	private JLabel jlPort;
	private JLabel jlIP;
	private JLabel jlCN;
	private JTextField jtPort;
	private JTextField jtIP;

	//constructor
	public login_GUI() {
		this.setTitle("Client");
		initial();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

		jlUsername = new JLabel("Username: ");
		jlUsername.setBounds(30, 100, 70, 20);
		jlPassword = new JLabel("Password: ");
		jlPassword.setBounds(30, 130, 70, 20);
		username = new JTextField();
		username.setBounds(110, 100, 150, 20);
		password = new JPasswordField();
		password.setBounds(110, 130, 150, 20);
		username.setFont(new Font("Calibri",Font.TRUETYPE_FONT|Font.ITALIC,10));
		password.setFont(new Font("Calibri",Font.BOLD,14));
		//set hint
		username.setText("please input your account.");
		username.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				username.setFont(new Font("Calibri",Font.BOLD,14));
				username.setText("");
				username.removeMouseListener(this);
			}
		});

		jlPort = new JLabel("Port: ");
		jlPort.setBounds(10, 20, 40, 30);
		jtPort = new JTextField("8787");
		jtPort.setBounds(60, 20, 70, 30);
		jtPort.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				jtPort.setText("");
				jtPort.removeMouseListener(this);
			}
		});

		jlIP = new JLabel("IP: ");
		jlIP.setBounds(150, 20, 30, 30);
		jtIP = new JTextField("LocalHost");
		jtIP.setBounds(190, 20, 70, 30);
		jtIP.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				jtIP.setText("");
				jtIP.removeMouseListener(this);
			}
		});

		jlCN = new JLabel("Not Connected");
		
		jlCN.setBounds(150, 50, 150, 30);
		jlCN.setForeground(Color.RED);


		jbConnect = new JButton("Connect");
		jbConnect.setFont(new Font("Arial", Font.PLAIN, 12));
		jbConnect.setBounds(280, 20, 90, 30);
		jbConnect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				ChatGui.client.setHost(jtIP.getText());
				ChatGui.client.setPort(Integer.parseInt(jtPort.getText()));

				int connected = ChatGui.client.start();

				if (connected == 0) { // if connection was successful 
					jlCN.setText("Connected!");
					jlCN.setForeground(Color.green);
				}else if(connected == 1) { // if connection failed because host was unknown
					JOptionPane.showMessageDialog(jlAll, "Unknown Host","Warning" ,JOptionPane.WARNING_MESSAGE);
				}else if(connected == 2) {// if connection failed because server could not be found 
					JOptionPane.showMessageDialog(jlAll, "Can't connect to server, is it running?","Warning",JOptionPane.WARNING_MESSAGE);
				}else{ // if connection failed because of unspecified IOexception 
					JOptionPane.showMessageDialog(jlAll, "Unaccounted for Error, oh dear...","Warning",JOptionPane.WARNING_MESSAGE);
				}


			}
		});

		login = new JButton("login");
		login.setFont(new Font("Arial", Font.PLAIN, 12));
		login.setBounds(280,100,65,20);
		login.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(jlCN.getText().equals("Connected!")) {

					String user = username.getText();
					String pass = String.valueOf(password.getPassword());
					if(user.equals("please input your account.") || pass.equals("") || user.equals("")){
						JOptionPane.showMessageDialog(jlAll, "Both username and password can not be empty!","Warning",JOptionPane.WARNING_MESSAGE);
					}else {
						String[] response = ChatGui.client.signin(user, pass, "false");
						if(response[1].equals("true")){
							ChatGui.BuildMainWindow();
							ChatGui.loggedIn(user, response[3]);

							dispose();
						}else{
							JOptionPane.showMessageDialog(jlAll, "sign-in Error, username/password combo is incorrect","Warning",JOptionPane.WARNING_MESSAGE);
						}
					}
				}else {
					JOptionPane.showMessageDialog(jlAll, "you are not connected!","Warning",JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		register = new JButton("create an account");
		register.setFont(new Font("Arial", Font.PLAIN, 10));
		register.setBounds(30, 210, 160, 20);
		register.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(jlCN.getText().equals("Connected!")) {
					register_GUI regis = new register_GUI();
					regis.setVisible(true);
				}else {
					JOptionPane.showMessageDialog(jlAll, "you are not connected!","Warning",JOptionPane.WARNING_MESSAGE);
				}
			}

		});

		anonSignIn = new JButton("Anonymous sign in");
		anonSignIn.setFont(new Font("Arial", Font.PLAIN, 10));
		anonSignIn.setBounds(200, 210, 150, 20);
		anonSignIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(jlCN.getText().equals("Connected!")) {

					String[] response = ChatGui.client.signin("anon", "anon", "true");
					if(response[1].equals("true")){
						ChatGui.BuildMainWindow();
						ChatGui.loggedIn(response[2], response[3]);


						dispose();
					}else{
						JOptionPane.showMessageDialog(jlAll, "sign-in Error, username/password combo is incorrect","Warning",JOptionPane.WARNING_MESSAGE);
					}
				}else {
					JOptionPane.showMessageDialog(jlAll, "you are not connected!","Warning",JOptionPane.WARNING_MESSAGE);
				}
			}


		});

		jlAll.add(jlUsername);
		jlAll.add(jlPassword);
		jlAll.add(login);
		jlAll.add(register);
		jlAll.add(anonSignIn);
		jlAll.add(jlIP);
		jlAll.add(jlPort);
		jlAll.add(jtPort);
		jlAll.add(jtIP);
		jlAll.add(jbConnect);
		jlAll.add(jlCN);
		con.add(jlAll);
		con.add(username);
		con.add(password);
	}

	public static void main(String[] args) {
		login_GUI login = new login_GUI();
	}

}
