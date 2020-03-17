import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * 
 * @author evogl In the LogInPanel the user is able to login to the account with
 *         the correct username and password.
 */

public class LogInPanel extends JPanel {

	JTextField usernametext;
	private JTextField passwordtext;
	private JLabel usernameLabel, passwordLabel, moviepicture, enterLabel;
	private JButton login,deleteaccount;
	private String username;
	private MainFrame mainframe;
	private static UserReader userreader;
	private static final Logger LOGGER = Logger.getLogger(LogInPanel.class.getName());
	private ArrayList<User> userlist;
	ReaderWriterFactory rwfactory = new ReaderWriterFactory();
	ReaderWriterInterface rwinterface = rwfactory.getReaderWriterInterface("USERREADER");

	public LogInPanel(ProfilePanel profilepanel) throws IOException {
		// creating file for the logging messages
		try {
			Handler handler = new FileHandler(
					"LoggingFiles" + System.getProperty("file.separator") + "LogInPanel_%g_10.txt", 10000, 10);
			SimpleFormatter formatter = new SimpleFormatter();
			LOGGER.setLevel(Level.SEVERE);
			handler.setFormatter(formatter);
			LOGGER.addHandler(handler);
		} catch (SecurityException e1) {
			LOGGER.log(Level.SEVERE,e1.toString(),e1);
		} catch (IOException e1) {
			LOGGER.log(Level.SEVERE,e1.toString(),e1);
		}

		mainframe = MainFrame.getObject();
		userreader = new UserReader();
		userlist=rwinterface.getList();
		setBackground(Color.orange);

		JPanel welcomepanel = new JPanel();
		welcomepanel.setPreferredSize(new Dimension(600, 399));
		welcomepanel.setBackground(Color.orange);

		moviepicture = new JLabel(" ");
		moviepicture.setIcon(new ImageIcon("Resources" + System.getProperty("file.separator") + "bild welcome.jpg"));
		welcomepanel.add(moviepicture);
		add(welcomepanel);

		JPanel loginpanel = new JPanel();
		loginpanel.setPreferredSize(new Dimension(550, 400));
		loginpanel.setBackground(Color.orange);

		enterLabel = new JLabel("Please enter your username and password: ");
		enterLabel.setFont(new Font("Arial Black", Font.BOLD, 20));
		enterLabel.setForeground(Color.DARK_GRAY);
		loginpanel.add(enterLabel);

		usernameLabel = new JLabel("");
		usernameLabel.setIcon(new ImageIcon("Resources" + System.getProperty("file.separator") + "logousername.png"));
		usernameLabel.setFont(new Font("Arial Black", Font.BOLD, 15));
		loginpanel.add(usernameLabel);

		usernametext = new JTextField();
		usernametext.setPreferredSize(new Dimension(150, 30));
		loginpanel.add(usernametext);

		passwordLabel = new JLabel("");
		passwordLabel.setIcon(new ImageIcon("Resources" + System.getProperty("file.separator") + "logopassword.png"));
		passwordLabel.setFont(new Font("Arial Black", Font.BOLD, 15));
		loginpanel.add(passwordLabel);

		passwordtext = new JPasswordField();
		passwordtext.setPreferredSize(new Dimension(170, 30));
		loginpanel.add(passwordtext);

		login = new JButton("Login");
		login.setPreferredSize(new Dimension(150, 50));
		loginpanel.add(login);
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (passwordtext.getText().isEmpty() || usernametext.getText().isEmpty()) {
					try {
						throw new EmptyFieldException("Field empty");
					} catch (EmptyFieldException e1) {
						JOptionPane.showMessageDialog(null, "Please enter the necessary fiels username and password",
								"Error", JOptionPane.ERROR_MESSAGE);
						LOGGER.log(Level.WARNING, "Field is empty", e1);
					}
				} else if (userreader.findUsernamePassword(usernametext.getText(),
						passwordtext.getText()) == true) {
					username = usernametext.getText();
					JOptionPane.showMessageDialog(null, "Login succesfully");
					mainframe.changeToProfile();
					profilepanel.changeUserName(username);
					profilepanel.showpicture.setIcon(profilepanel.uploadImage());

				} else {
					JOptionPane.showMessageDialog(null, "<html>Sorry, please check again your login details<html>");
				}

			}

		}

		);
		
		deleteaccount=new JButton("Delete your account");
		loginpanel.add(deleteaccount);
		deleteaccount.setPreferredSize(new Dimension(150, 50));
		deleteaccount.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if (passwordtext.getText().isEmpty() || usernametext.getText().isEmpty()) {
					try {
						throw new EmptyFieldException("Field is empty");
					} catch (EmptyFieldException e1) {
						LOGGER.log(Level.INFO, "Textfields are empty", e1);
						JOptionPane.showMessageDialog(null, "Please put in all necessary details", "Error",
								JOptionPane.ERROR_MESSAGE);
					}

				} else if (userreader.findUsernamePassword(usernametext.getText(),passwordtext.getText()) == true) {
					removeUser(usernametext.getText());
					rwinterface.write();
					
					File userpicture = new File(usernametext.getText() + ".jpg");
					userpicture.delete();

					JOptionPane.showMessageDialog(null, "We have successfully deleted your account");
					usernametext.setText("");
					passwordtext.setText("");
					
				}

				else {
					JOptionPane.showMessageDialog(null, "<html>The entered logindetails could not be found!<br> "
							+ "Sorry! Please try again.<html>");
				}

			}
		}

		);

		add(loginpanel);
	}
	/**
	 * The method removeUser removes the whole user from the ArrayList
	 * @param username which name should be removed from the ArrayList
	 */
	public void removeUser(String username) {
		   Iterator<User> it = userlist.iterator();
		   while(it.hasNext()) {
		   if(it.next().getUsername().equals(username)) { it.remove(); break; }
		   }
		}
}
