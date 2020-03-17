import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserReaderTest {

	UserReader userreader;
	BufferedReader reader;

	@Before
	public void initData() throws Exception {
		userreader = new UserReader();

	}

	@After
	public void tearDown() throws Exception {
		userreader = null;
	} 

	// checks if user is read succesffully in an ArrayList
	@Test
	public void ArrayListreadFile() throws IOException {
		UserReader userreader = new UserReader() {
			protected Reader openUsers() {
				return new CharArrayReader(
						"admin;21232f297a57a5a743894a0e4a801fc3;\nlightbulb;4902f1f0edd536d1698288ed23f85bcb;"
								.toCharArray());
			};
		};
		ArrayList<User> correct = userreader.readFile();

		assertEquals(2, correct.size());
	}

	// checks if a user has been created successfully in the file
	@Test
	public void add() throws IOException {
		ArrayList<User> userlist = new ArrayList<User>();
		
		User user1=new User("elisavogl", "elisa_vogl");
		User user2=new User ("admin", "admin");
		CharArrayWriter writer = new CharArrayWriter();
		userlist.add(user1);
		userlist.add(user2);
		userreader.add(writer);

		assertEquals("admin", userlist.get(1).getUsername());
		assertEquals(2,userlist.size());
	}

	// checks if a string is found correctly in the file
	@Test
	public void findInFile() throws IOException {
		UserReader userreader = new UserReader() {
			protected Reader openUsers() {
				return new CharArrayReader(
						"admin;21232f297a57a5a743894a0e4a801fc3;\nlightbulb;4902f1f0edd536d1698288ed23f85bcb;"
								.toCharArray());
			};
		};
		String username = "lightbulb";
		String username1 = "elisavogl";
		boolean correct = userreader.findinFile(username);
		boolean incorrect = userreader.findinFile(username1);
		assertEquals(true, correct);
		assertEquals(false, incorrect);

	}

	// checks if username and password are found correctly in the file
	@Test
	public void findUsernamePassword() throws IOException {
		UserReader userreader = new UserReader() {
			protected Reader openUsers() {
				return new StringReader("admin;admin;\nlightbulb;lightbulb;");
			};
		};
		String username = "admin";
		String password = "admin";

		String username1 = "elisavogl";
		String username2 = "London1+";

		boolean correct = userreader.findUsernamePassword(username, password);
		boolean incorrect = userreader.findUsernamePassword(username1, username2);
		assertEquals(true, correct);
		assertEquals(false, incorrect);

	}

	// checks if the username has the correct credentials
	@Test
	public void ValidUserNameTest() {
		String[] valid = new String[] { "elisa_vogl", "eli91_vogl" };
		String[] invalid = new String[] { "eli_91_100", "elisa_vogl_91", "elisa.vogl.", "elisa-vogl-", "eli",
				"eli-vogl." };
		for (String temp : valid) {
			boolean checkvalid = userreader.checkUsername(temp);
			assertEquals(true, checkvalid);

			for (String temp1 : invalid) {
				checkvalid = userreader.checkUsername(temp1);
				assertEquals(false, checkvalid);
			}
		}
	}

	// checks if the password has the correct credentials
	@Test
	public void ValidPasswordTest() {

		String[] valid = new String[] { "hello", "elisavogl", "number1991", "elisa_vogl", "elisa!vogl" };
		String[] invalid = new String[] { "eli", "num", "num_" };
		for (String temp : valid) {
			boolean checkvalid = userreader.checkPassword(temp);
			assertEquals(true, checkvalid);
			for (String temp1 : invalid) {
				checkvalid = userreader.checkPassword(temp1);
				assertEquals(false, checkvalid);
			}
		}
	}

	// checks if the password is encrypted successfully
	@Test
	public void checkEncryption() {
		String normalText = "elisa_vogl";
		String encrypted = userreader.encrypt(normalText);
		assertTrue(encrypted != normalText);
	}

}