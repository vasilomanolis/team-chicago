package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Helper method to connect to the database class.
 * @author Guan
 *
 */
public class DBHelper {
	
	//Connection details.
	private static final String driver="org.postgresql.Driver";
	private static final String url="jdbc:postgresql://link";
	private static final String DBusername="username";
	private static final String DBpassword="password";

	private static  Connection con=null;
	static
	{
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static Connection getConnection(){

		if(con==null){
			try {
				//attempt connection the given database url.
				con=DriverManager.getConnection(url, DBusername, DBpassword);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return con;
	}
}	

