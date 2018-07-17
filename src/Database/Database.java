package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;

/**
 * 
 * Class to connect to the database where user details, messages are stored.
 * 
 * @author Guan
 */
public class Database {

	// method to connect to the database to save the message in respect to
	// username, message and time.
	public void saveLog(String username, String msg, String time) throws SQLException {

		/**
		 * Connection details.
		 */
		String driver = "org.postgresql.Driver"; // postgres driver.
		String url = "jdbc:postgresql://mod-msc-sw1.cs.bham.ac.uk:5432/chicago"; // database
																					// link
		String DBusername = "username"; // database username
		String DBpassword = "password"; // database password
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			if (conn == null) {

				Class.forName(driver);
				conn = DriverManager.getConnection(url, DBusername, DBpassword);

			}
			conn.setAutoCommit(false);

			// String msg_id = username + "@" + dateFormat;
			// prepared statement to pass through username, msg, and time of the
			// message sent between client to server.
			String sql = "insert into logstore values(?,?,?,(select Max(msg_id)+1 from logstore))";

			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			// The first column represents the username.
			ps.setString(2, msg);
			// The second column represents the messages exchanged.
			ps.setString(3, time);
			// The third column represents the time the messages was sent.

			ps.executeUpdate();

			conn.commit();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * Method to store private or group messages.
	 * 
	 * @param groupnames
	 *            the name of the group chat.
	 * @param message
	 *            The messages exchanged between users within the same chat.
	 * @throws SQLException
	 */
	public void groupLog(ArrayList<String> groupnames, String message) throws SQLException {

		// The group name is store as an arrayList so any new addition of
		// usernames can be added to the List.

		String driver = "org.postgresql.Driver";
		String url = "jdbc:postgresql://mod-msc-sw1.cs.bham.ac.uk:5432/chicago";
		String DBusername = "chicago";
		String DBpassword = "v74x4mkdhl";
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			if (conn == null) {

				Class.forName(driver);
				conn = DriverManager.getConnection(url, DBusername, DBpassword);

			}
			conn.setAutoCommit(false);

			// String msg_id = username + "@" + dateFormat;
			String sql = "insert into groupchat values(?,?,(select Max(id_message)+1 from groupchat))";

			String groupname = "";
			for (String s : groupnames) {
				// enhanced for loop to print all the username involved with the
				// conversation.
				groupname = groupname + s + ":";
			}
			ps = conn.prepareStatement(sql);
			ps.setString(1, groupname);
			ps.setString(2, message);

			ps.executeUpdate();

			conn.commit();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * Method to get the history of the chats that were sent to the database and
	 * present it back to the client.
	 * 
	 * @param forSearch
	 *            The messages present in the database. When true then it is
	 *            printed out.
	 * @return ChatLog The chat histories of previous conversations that was
	 *         stored in the database.
	 */
	public String getHistory(boolean forSearch) {

		// Connection details.
		String driver = "org.postgresql.Driver";
		String url = "jdbc:postgresql://mod-msc-sw1.cs.bham.ac.uk:5432/chicago";
		String DBusername = "chicago";
		String DBpassword = "v74x4mkdhl";
		Connection conn = null;
		PreparedStatement ps = null;

		String ChatLog = "";
		try {
			if (conn == null) {

				Class.forName(driver);
				conn = DriverManager.getConnection(url, DBusername, DBpassword);

			}
			conn.setAutoCommit(false);

			Statement st = conn.createStatement();

			// String msg_id = username + "@" + dateFormat;
			String sql = "select msg,time, username from logstore order by msg_id";

			ps = conn.prepareStatement(sql);

			// ps.executeUpdate();

			ResultSet output = ps.executeQuery();

			ResultSetMetaData display = output.getMetaData();
			int columnsNumber = display.getColumnCount();

			System.out.println(columnsNumber + "\n");

			while (output.next()) {
				// Print one row

				String sender = output.getString("username");
				String message = output.getString("msg");
				String time = output.getString("time");

				// Ker @ (14:03:934): yeah we did it
				if (forSearch == false) {
					ChatLog = ChatLog + sender + " @ " + "(" + time + "): " + message + "%##%";
				} else {
					ChatLog = ChatLog + sender + "%##%" + " @ " + "(" + time + "): " + "%##%" + message + "%##%";
				}

				conn.commit();
			}
			System.out.println();

		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		// TODO Auto-generated method stub
		return ChatLog;
		// select msg,time, username from logstore order by msg_id;
	}

	/**
	 * Method to check if there is such user in the database.
	 * 
	 * @param username
	 *            The username of the client.
	 * @param password
	 *            The password of the client.
	 * @return false if the user does not exist in the database system.
	 * @throws SQLException
	 */
	public boolean isValidUser(String username, String password) throws SQLException {

		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {
			conn = DBHelper.getConnection();

			// close the auto commit
			conn.setAutoCommit(false);

			// prepared statement to check for client/user detail.
			String sql = "select username, password " + "from accountprofile " + "where username = ? and password = ?";

			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);

			rs = ps.executeQuery();

			while (rs.next()) {
				String un = rs.getString("username");
				String pw = rs.getString("password");

				if ((un.equals(username)) && (pw.equals(String.valueOf(password)))) {
					return true; // user exist in the database.
				} else {
					return false; // client does not exit in our database.
				}
			}

			conn.commit();

		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (conn != null) {
			}
		}
		return false;
	}

	/**
	 * Method to allow user to create a profile within our database.
	 * 
	 * @param username
	 * @param password
	 * @throws SQLException
	 */
	public void createUser(String username, String password) throws SQLException {

		// Connection details
		String driver = "org.postgresql.Driver";
		String url = "jdbc:postgresql://mod-msc-sw1.cs.bham.ac.uk:5432/chicago";
		String DBusername = "chicago";
		String DBpassword = "v74x4mkdhl";
		Connection conn = null;
		PreparedStatement ps = null;

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, DBusername, DBpassword);

			conn.setAutoCommit(false);

			// String ID = " select count(*)+1 from accountprofile";

			// prepared statement that increments the userID with each new user
			// registering with their username and password
			String sql = "insert into accountprofile values(?,?,(select Max(user_id)+1 from accountprofile))";

			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, String.valueOf(password));

			ps.executeUpdate();

			conn.commit();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean createPrivateChatHistory(String timestamp, String sender, String recipient, String message)
			throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean changePassword(String username, String password) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public String getPublicHistory(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPrivateHistory(String username1, String username2) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Method to check if the username already exist in the database.
	 * 
	 * @param username
	 *            The username of the client.
	 * @return boolean value to check if username is present or not.
	 * @throws SQLException
	 */
	public boolean isExistUser(String username) throws SQLException {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {
			conn = DBHelper.getConnection();

			conn.setAutoCommit(false);

			// prepare statement to select the username queried.
			String sql = "select username " + "from accountprofile " + "where username = ?";

			ps = conn.prepareStatement(sql);
			ps.setString(1, username);

			rs = ps.executeQuery();

			while (rs.next()) {
				String un = rs.getString("username");

				if (!(un.isEmpty())) {

					return false;
				} else {
					return true;
				}
			}

			conn.commit();

		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (conn != null) {
			}
		}
		return true;
	}

	public static void main(String[] args) {

		Database x = new Database();
		x.getHistory(false);

	}
}
