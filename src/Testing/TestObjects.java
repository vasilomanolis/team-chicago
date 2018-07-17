package Testing;

	import static org.junit.Assert.*;

	import java.net.Socket;
	import java.util.ArrayList;
	import java.util.List;

	import org.junit.Rule;
	import org.junit.Test;
	import org.junit.rules.ExpectedException;

import CurrentServer.Chat;
import CurrentServer.Message;
import CurrentServer.User;

	/**
	 * This class includes the testing for the methods of the classes Message, Chat
	 * and User.
	 * 
	 * @author chicago
	 *
	 */
	public class TestObjects {

		// Testing the Getters of the Message Class

		@Test
		public void MessageTestGetSender() { // testing the getSender() method of the Message Class

			Message testMessage = new Message("Sophia", "01:23", "This is a test string");

			String actualString = testMessage.getSender();
			String expectedString = "Sophia";

			assertEquals(expectedString, actualString);

		}

		@Test
		public void MessageTestGetContent() { // testing the getContent() method of the Message Class

			Message testMessage = new Message("Sophia", "01:23", "This is a test string");

			String actualString = testMessage.getContent();
			String expectedString = "This is a test string";

			assertEquals(expectedString, actualString);

		}

		@Test
		public void MessageTestGetTimeStamp() { // testing the getTimeStamp() method of the Message Class

			Message testMessage = new Message("Sophia", "01:23", "This is a test string");

			String actualString = testMessage.getTimeStamp();
			String expectedString = "01:23";

			assertEquals(expectedString, actualString);

		}

		// Testing the Getters of the Chat Class

		@Test
		public void ChatTestGetChatID() { // testing the getChatID() method of the Chat Class

			Message message1 = new Message("Sophia", "01:23", "This is a test string 1");
			Message message2 = new Message("Maria", "05:32", "This is a test string 2");
			ArrayList users = new ArrayList<String>();
			users.add("Sophia");
			users.add("Maria");
			ArrayList messages = new ArrayList<String>();
			messages.add(message1);
			messages.add(message2);

			Chat testChat = new Chat(1, "testChat", users, messages);

			int actualID = testChat.getChatID();
			int expectedID = 1;

			assertEquals(expectedID, actualID);

		}

		@Test
		public void ChatTestGetUsers() { // testing the getUsers() method of the Chat Class

			Message message1 = new Message("Sophia", "01:23", "This is a test string 1");
			Message message2 = new Message("Maria", "05:32", "This is a test string 2");
			ArrayList users = new ArrayList<String>();
			users.add("Sophia");
			users.add("Maria");
			ArrayList messages = new ArrayList<String>();
			messages.add(message1);
			messages.add(message2);

			Chat testChat = new Chat(1, "testChat", users, messages);

			ArrayList actualUsers = testChat.getUsers();
			ArrayList expectedUsers = users;

			assertEquals(expectedUsers, actualUsers);

		}

		@Test
		public void ChatTestGetMessages() { // testing the getMessages() method of the Chat Class

			Message message1 = new Message("Sophia", "01:23", "This is a test string 1");
			Message message2 = new Message("Maria", "05:32", "This is a test string 2");
			ArrayList users = new ArrayList<String>();
			users.add("Sophia");
			users.add("Maria");
			ArrayList messages = new ArrayList<String>();
			messages.add(message1);
			messages.add(message2);

			Chat testChat = new Chat(1, "testChat", users, messages);

			ArrayList actualMessages = testChat.getMessages();
			ArrayList expectedMessages = messages;

			assertEquals(expectedMessages, actualMessages);

		}

		@Test
		public void ChatTestGetChatName() { // testing the getSender() method of the Chat Class

			Message message1 = new Message("Sophia", "01:23", "This is a test string 1");
			Message message2 = new Message("Maria", "05:32", "This is a test string 2");
			ArrayList users = new ArrayList<String>();
			users.add("Sophia");
			users.add("Maria");
			ArrayList messages = new ArrayList<String>();
			messages.add(message1);
			messages.add(message2);

			Chat testChat = new Chat(1, "testChat", users, messages);

			String actualChatName = testChat.getChatName();
			String expectedChatName = "testChat";

			assertEquals(expectedChatName, actualChatName);

		}

		// Testing the Getters of the User Class
		@Test
		public void UserTestGetSessionID() { // testing the getSessionID() method of the User Class

			Socket socketTest = new Socket();

			ArrayList chatTest = new ArrayList<Integer>();
			chatTest.add(1);
			chatTest.add(2);
			chatTest.add(3);

			User sophia = new User(1, "sophia", "12345678", socketTest, chatTest);

			int actualSessionID = sophia.getsessionID();
			int expectedSessionID = 1;

			assertEquals(expectedSessionID, actualSessionID);

		}

		@Test
		public void UserTestGetSocket() { // testing the getSocket() method of the User Class

			Socket socketTest = new Socket();

			ArrayList chatTest = new ArrayList<Integer>();
			chatTest.add(1);
			chatTest.add(2);
			chatTest.add(3);

			User sophia = new User(1, "sophia", "12345678", socketTest, chatTest);

			Socket actualSocket = sophia.getSocket();
			Socket expectedSocket = socketTest;

			assertEquals(expectedSocket, actualSocket);

		}

		@Test
		public void UserTestGetUsername() { // testing the getUsername() method of the User Class

			Socket socketTest = new Socket();

			ArrayList chatTest = new ArrayList<Integer>();
			chatTest.add(1);
			chatTest.add(2);
			chatTest.add(3);

		User sophia = new User(1, "sophia", "12345678", socketTest, chatTest);

			String actualUsername = sophia.getUsername();
			String expectedUsername = "sophia";

			assertEquals(expectedUsername, actualUsername);

		}

		@Test
		public void UserTestGetChats() { // testing the getChats() method of the User Class

			Socket socketTest = new Socket();

			ArrayList chatTest = new ArrayList<Integer>();
			chatTest.add(1);
			chatTest.add(2);
			chatTest.add(3);

			User sophia = new User(1, "sophia", "12345678", socketTest, chatTest);

			ArrayList actualSessionID = sophia.getChats();
			ArrayList expectedSessionID = chatTest;

			assertEquals(expectedSessionID, actualSessionID);

		}

	}
