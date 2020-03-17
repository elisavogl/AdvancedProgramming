import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * 
 * @author evogl In the class UserReader there can be found all methods refering
 *         to reading and writing in regards to the User
 */
public  class UserReader implements ReaderWriterInterface {

	private ArrayList<User> userlist;
	private static String USER_FILENAME = "Resources" + System.getProperty("file.separator") + "Users.dat";
	BufferedReader reader;
	User newuser;
	private static final Logger LOGGER = Logger.getLogger(UserReader.class.getName());

	public UserReader() throws IOException {
		try {
			Handler handler = new FileHandler(
					"LoggingFiles" + System.getProperty("file.separator") + "UserReader_%g_10.txt", 10000, 10);
			SimpleFormatter formatter = new SimpleFormatter();
			LOGGER.setLevel(Level.SEVERE);
			handler.setFormatter(formatter);
			LOGGER.addHandler(handler);
		} catch (SecurityException e1) {
			LOGGER.log(Level.SEVERE, e1.toString(), e1);
		} catch (IOException e1) {
			LOGGER.log(Level.SEVERE, e1.toString(), e1);
		}
		userlist = new ArrayList<User>();
		userlist = readFile();
	}

	/**
	 * The method readFile(String path) reads a file and returns it to an
	 * Arraylist for the Users 
	 * @return the ArrayList of User
	 * @throws IOException in case the file could not be read
	 */
	public ArrayList<User> readFile() throws IOException {

		String line = "";
		String separator = ";";
		String[] allUsers;
		ArrayList<User> list = new ArrayList<User>();
		Reader userreader = openUsers();
		BufferedReader reader = new BufferedReader(userreader);
		while ((line = reader.readLine()) != null) {
			allUsers = line.split(separator);

			String username = allUsers[0];
			String password = allUsers[1];

			User userclass = new User(username, password);
			list.add(userclass);
		}

		return list;

	}

	/**
	 * In the method getList() we can refer to the Arraylist created in
	 * other classes
	 * 
	 * @return the Arraylist of Users
	 */
	

	public ArrayList<User> getList() {
		return userlist;
	}


	/**
	 * In the method add(String path) we are adding a new user to the file by
	 * writing to it
	 * 
	 * @param print:
	 *            Writer needed for creating JUnit Test
	 */
	public void add(Writer print) {
		print = null;
		
		try {

			print = new PrintWriter(new BufferedWriter(new FileWriter(USER_FILENAME)));
			for (User getUser : userlist) {
				print.write(getUser.getUsername() + ";" + getUser.getPassword() + ";\n");
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.toString() + "Unable to write in file", e);
		} finally {
			if (print != null) {
				try {
					print.close();
				} catch (IOException e) {
					LOGGER.log(Level.SEVERE, "Unable to close the file");
				}
			} else {
				LOGGER.log(Level.INFO, "PrintWriter not open");
			}
		}
	}

	/**
	 * In the method findinFile(String path,String searchedString) we are
	 * checking if a specific STring exists in the file
	 * 
	 * @param searchedString
	 *            specifies what String is searched
	 * @return true if username has been found and otherwise false
	 */
	public boolean findinFile(String searchedString) {
		String line;
		
		try {
			Reader userreader = openUsers();
			BufferedReader reader = new BufferedReader(userreader);
			line = reader.readLine();
			while (line != null) {
				for (User getUser : userlist){
				if (getUser.getUsername().equals(searchedString)) {
					reader.close();
					return true;
				} else {
					line = reader.readLine();
				}}
			}

		} catch (FileNotFoundException e) {
			LOGGER.log(Level.SEVERE, "File could not be found" + e.toString(), e);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Unable to read the file", e);
		}

		return false;
	}


	/**
	 * In the method findUsernamePassword there is checked if username and
	 * password are the same as in the file
	 * 
	 * @param username
	 *            has to be entered for searching the username entered by the
	 *            user
	 * @param password
	 *            has to be entered for searching the password entered by the
	 *            user
	 * @return true if String of username and password has been found otherwise
	 *         return false
	 */
	public boolean findUsernamePassword(String username, String password) {
		String line;
		
		try {
			reader = new BufferedReader(new FileReader(USER_FILENAME));
			line = reader.readLine();
			while (line != null) {
				if (line.contentEquals(username + ";" + encrypt(password) + ";")) {
					reader.close();
					return true;
				} else {
					line = reader.readLine();
				}
			}
		} catch (FileNotFoundException e) {
			LOGGER.log(Level.SEVERE, "File could not be found", e);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		try {
			reader.close();
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Impossibile to close the file", e);
		}
		
		return false;
	}

	/**
	 * The method String encrypt (String unencryptedString) encrypts the
	 * password
	 * 
	 * @param unencryptedString
	 *            the normal password which has to be encrypted
	 * @return the encrypted password
	 */
	public String encrypt(String unencryptedString) {
		
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(unencryptedString.getBytes());
			byte[] bytes = md.digest();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();

		} catch (NoSuchAlgorithmException e) {
			LOGGER.log(Level.SEVERE, "Unable to encrypt", e);
		}
		
		return generatedPassword;

	}

	/**
	 * In the method checkUsername (CharSequence username) you can check if the
	 * username has been entered with the correct credentials for the
	 * registration
	 * 
	 * @param username which has to be checked for their credentials
	 * @return returns the correct matched username if it is true
	 */
	public boolean checkUsername(CharSequence username) {
		Pattern pattern = Pattern.compile("^(?=.{5})[a-z0-9]*([-_.][a-z0-9]*)?$");
		Matcher m = pattern.matcher(username);

		return m.matches();

	}

	/**
	 * In the method checkPassword (CharSequence password) you can check if the
	 * password has been entered with the correct credentials for the
	 * registration
	 * 
	 * @param password which has to be checked
	 * @return the correct matched password if it is true
	 */
	public boolean checkPassword(CharSequence password) {
		String regex = "^([ -~]{5,})$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(password);
		return m.matches();

	}

	/**
	 * Method openUsers for JUnit Tests to take the correct Reader for testing
	 * @return the file which has to be used for reading
	 * especially served for JUnit tests
	 * @throws FileNotFoundException
	 */
	protected Reader openUsers() throws FileNotFoundException {
		return new FileReader(USER_FILENAME);
	}

	/**
	 * The method write() writes the whole Arraylist to the file
	 */
	public void write() {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(USER_FILENAME)));
			for (User getMovie : userlist) {
				writer.write(getMovie+"");

			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Sorry there was an error to write to the file");
			LOGGER.log(Level.SEVERE, "Error in writing to file", e);
		}
		finally {
			if (writer!=null){
				writer.close();
			}else {
				LOGGER.log(Level.INFO,"PrintWriter not open");
			}}
		
	}

}
