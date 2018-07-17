package Protocol;

import java.io.IOException;
import java.sql.SQLException;

public class SimpleProtocol {

	// These lines are used to make sure an input of multilines (message including
	// ‘\n’) are translated into a single line. We replace ‘\n’ (one char) to ‘\\n’
	// (two chars) when packing a message and replace it back when unpacking. But
	// what if the user inputs ‘\\n’? To deal with the possible cases of ‘\\n’
	// input, we firstly replace ‘\\’ (1 char) to ‘\\\\’ (2 chars) then do the ‘\n’
	// translation;

	private static String escape(String str) {
		str = str.replace("\\", "\\\\");
		str = str.replace("_", "\\_");
		str = str.replace("\n", "\\n");
		return str;
	}

	private static String unescape(String str) {
		str = str.replace("\\\\", "\\");
		str = str.replace("\\_", "_");
		str = str.replace("\\n", "\n");
		return str;
	}

	public String createMessage(String... args) {
		String result = "";
		for (String str : args) {
			if (result != "")
				result = result + ":__:";
			result = result + escape(str);
		}
		System.out.println("send: " + result);
		return result;
	}

	public String[] decodeMessage(String str) {
		System.out.println("receive: " + str);
		if (str == null || str.equals("")) {
			return new String[0];
		}
		String[] messages = str.split(":__:");
		for (int i = 0; i < messages.length; i++) {
			messages[i] = unescape(messages[i]);
		}
		if (messages.length == 1 && messages[0].equals("")) {
			return new String[0];
		}
		return messages;
	}

	public static void main(String[] args) {
		SimpleProtocol protocol = new SimpleProtocol();
		String[] strings = protocol.decodeMessage(protocol.createMessage("_aaaa_", "bbbb", "cccc", "dddd"));
		for (String str : strings) {
			System.out.println(str);
		}
	}

	// PROTOCOL INITIAL TEST
	// public String[] encodeMessage(String str) {
	//
	// }
	//
	// public String[] decodeMessage(String request) {
	//
	// System.out.println(request);
	// String partA = request.substring(0, 0); // partA is the Category Number
	// String partB = request.substring(1, 3); // partB is the "req" or "res"
	// identification for request or response
	// // accordingly
	// String partC = request.substring(4); // partC is the rest of the message
	// String caseFinder = request.substring(0, 4);
	//
	// switch (caseFinder) {
	//
	// // Case 1: sendMessage(String recipient, String message)
	// // Case 2: sendMessageToAll(String message)
	// // Case 3: login(String username, String password)
	// // Case 4: logout()
	// // Case 5: verifyRegistration(String username)
	// // Case 6: changePassword()
	// // Case 7: viewPrivateHistory(String range, String otherUser)
	// // Case 8: viewPublicHistory()
	//
	// case "1" + "req": { // sendMessage
	// System.out.println(partC);
	//
	// }
	//
	// }
	//
	// }
	//
	// public String createMessage(String... args) {
	// String result = "";
	// for (String str : args) {
	// if (result != "")
	// result = result + "@:";
	// }
	// System.out.println("send: " + result);
	// return result;
	// }
	//
	// public static void main(String args) {
	// String str = "1requestSendthsimessage";
	// System.out.println(str.substring(4));
	// }

}
