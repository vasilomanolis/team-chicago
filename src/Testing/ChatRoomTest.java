package Testing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import Database.Database;

/**
 * JUnit
 * use to test sign up and sign in method for the chat room
 * @author Jiayu Liu
 *
 */
class ChatRoomTest {
	
	Database database = new Database();
	
	/**
	 * test username
	 * so if true, not exists
	 * if false, it exists
	 * @throws SQLException 
	 */
	@Test
	void test1() throws SQLException {
		String username_1 = "testtest1";
		boolean actual_1 = database.isExistUser(username_1);
		boolean expected_1 = false;
		assertEquals(expected_1,actual_1);
		
		String username_2 = "testtest4";
		boolean actual_2 = database.isExistUser(username_2);
		boolean expected_2 = true;
		assertEquals(expected_2,actual_2);
	}
	
	/**
	 * test password
	 * @throws SQLException 
	 */
	@Test
	void test2() throws SQLException {
		String username_1 = "testtest3";
		String password_1 = "testtest3";
		boolean actual_1 = database.isValidUser(username_1, password_1);
		boolean expected_1 = true;
		assertEquals(expected_1,actual_1);
		
		String username_2 = "testtest3";
		String password_2 = "testtest";
		boolean actual_2 = database.isValidUser(username_2, password_2);
		boolean expected_2 = false;
		assertEquals(expected_2,actual_2);
	}
	


}
