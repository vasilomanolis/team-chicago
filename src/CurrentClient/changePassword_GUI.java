package CurrentClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class changePassword_GUI extends JFrame{
	private JTextField username;
	private JPasswordField password;
	private JPasswordField password2;
	private JLabel jlAll;
	private JLabel jlUsername;
	private JLabel jlPassword;
	private JLabel jlPassword2;
	private JButton register;
	private JButton cancel;
	
	public changePassword_GUI() {
		this.setTitle("Forget password");
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
		
		jlUsername = new JLabel("username: ");
		jlUsername.setBounds(30, 30, 100, 20);
		jlPassword = new JLabel("new password: ");
		jlPassword.setBounds(30, 90, 100, 20);
		jlPassword2 = new JLabel("password comfirm: ");
		jlPassword2.setBounds(30, 150, 120, 20);
		
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
					if(password.getPassword().length==0||username.getText().length()==0) {
						JOptionPane.showMessageDialog(null, 
								"Username and password can not be empty."
								, "Error", JOptionPane.ERROR_MESSAGE);
					}else if (!(String.valueOf(password.getPassword()).equals(String.valueOf(password2.getPassword())))) {
						JOptionPane.showMessageDialog(null, 
								"Both input password are different."
								, "Error", JOptionPane.ERROR_MESSAGE);
						password.setText("");
						password2.setText("");
					}else {
						
						JOptionPane.showMessageDialog(null, "Success",
								"changePassword", JOptionPane.PLAIN_MESSAGE);
						if(!checkUsername()) {
						changePassword();
						}else {
							JOptionPane.showMessageDialog(null, "not exist username",
									"changePassword", JOptionPane.PLAIN_MESSAGE);
						}
					}
				}
			});
		
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
	
	public boolean checkUsername() {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/chatroom";
		String DBusername = "root";
		String DBpassword = "mysql123456";
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url,DBusername,DBpassword);
			
			conn.setAutoCommit(false);

			String sql = "select username "
					   + "from accountprofile "
					   + "where username = ?";

			ps = conn.prepareStatement(sql);
			ps.setString(1, username.getText());
			
			rs = ps.executeQuery();

			while(rs.next()) {
				String un = rs.getString("username");
				
				if(!(un.isEmpty())) {

					return false;
				}else {
					return true;
				}
			}

			conn.commit();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			try {conn.rollback();} catch (SQLException e1) {e1.printStackTrace();}
			e.printStackTrace();
		} finally {
			try {if(rs!=null) {	rs.close(); }} catch (SQLException e) {	e.printStackTrace();}
			try {if(ps!=null) { ps.close(); }} catch (SQLException e) { e.printStackTrace();}
			if(conn!=null) {try {conn.close();} catch (SQLException e) {e.printStackTrace();}}
		}
		return true;
	}
	
	
	public void changePassword() {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/chatroom";
		String DBusername = "root";
		String DBpassword = "mysql123456";
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url,DBusername,DBpassword);
			
			conn.setAutoCommit(false);

			String sql = "update accountprofile set password = ? where username = ?";

			ps = conn.prepareStatement(sql);
			ps.setString(2, username.getText());
			ps.setString(1, String.valueOf(password.getPassword()));
			
			ps.executeUpdate();

			conn.commit();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			try {conn.rollback();} catch (SQLException e1) {e1.printStackTrace();}
			e.printStackTrace();
		} finally {
			try {if(ps!=null) { ps.close(); }} catch (SQLException e) { e.printStackTrace();}
			if(conn!=null) {try {conn.close();} catch (SQLException e) {e.printStackTrace();}}
		}
	}
	
	
	public static void main(String[] args) {
		changePassword_GUI cp = new changePassword_GUI();
	}

}
