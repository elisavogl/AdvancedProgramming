import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * 
 * @author evogl In the class RegisterPanel the user for the Movie Collection is
 *         able to register at it and there are added the needed components for
 *         doing the registration
 */
public class RegisterPanel extends JPanel {

	private JLabel informationregistration, conditions, registerusernameLabel, registerpasswordLabel;
	private JButton uploadimage, goback;
	private BufferedImage image;
	private JTextField registerusername;
	private JPasswordField registerpassword;
	private JButton register;
	private MainFrame mainframe;
	private UserReader userreader;
	private ArrayList<User> userlist;
	private static final Logger LOGGER = Logger.getLogger(RegisterPanel.class.getName());
	ReaderWriterFactory rwfactory = new ReaderWriterFactory();
	ReaderWriterInterface rwinterface = rwfactory.getReaderWriterInterface("USERREADER");
	private PrintWriter writer;


	public RegisterPanel() throws IOException {
		try {
			Handler handler = new FileHandler(
					"LoggingFiles" + System.getProperty("file.separator") + "RegisterPanel_%g_10.txt", 10000, 10);
			SimpleFormatter formatter = new SimpleFormatter();
			LOGGER.setLevel(Level.SEVERE);
			handler.setFormatter(formatter);
			LOGGER.addHandler(handler);
		} catch (SecurityException e1) {
			LOGGER.log(Level.SEVERE, e1.toString(), e1);
		} catch (IOException e1) {
			LOGGER.log(Level.SEVERE, e1.toString(), e1);
		}

		mainframe = MainFrame.getObject();
		userreader = new UserReader();
		userlist = rwinterface.getList();

		JPanel instructions = new JPanel(new BorderLayout());
		instructions.setPreferredSize(new Dimension(800, 200));
		instructions.setBackground(Color.orange);
		conditions = new JLabel("<html>Please note <br> the following conditions <br> about username and password: ");
		instructions.add(conditions, BorderLayout.CENTER);
		conditions.setFont(new Font("Arial Black", Font.BOLD, 15));

		informationregistration = new JLabel(
				"<html>1. Username can contain only 1 dash, 1 underscore or 1 period.<br>Any other combinations of"
						+ "letters and numbers are allowed.<br>The minimum length of characters is 5."
						+ "<br>2. Password can have any combination of Ascii characters.<br>The minimum are 5 characters.<html>");
		instructions.add(informationregistration, BorderLayout.EAST);
		add(instructions);

		JPanel buttonpanel = new JPanel(new BorderLayout());
		buttonpanel.setBackground(Color.gray);
		uploadimage = new JButton("Please upload \n your picture here");
		uploadimage.setForeground(Color.blue);

		uploadimage.setPreferredSize(new Dimension(400, 300));
		buttonpanel.add(uploadimage);
		uploadimage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG, GIF, and PNG Images", "jpg", "gif",
						"png");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(RegisterPanel.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File uploadfile = chooser.getSelectedFile();
					JOptionPane.showMessageDialog(null,
							"You choose to open the following file: " + uploadfile.getName());
					try {
						image = ImageIO.read(uploadfile);
						JOptionPane.showMessageDialog(null,
								"<html>Please note that we have uploaded your userimage.<br>Now you are able to choose your username and password<br>"
										+ "Note that you are able to see your image after the first really login<html>");
					} catch (IOException ex) {
						LOGGER.log(Level.SEVERE, "Not able to upload the image!", ex);
					}

				}
			}
		}

		);
		add(buttonpanel);

		JPanel registerpanel = new JPanel();
		registerusernameLabel = new JLabel("Username: ");
		registerusernameLabel.setFont(new Font("Arial Black", Font.BOLD, 14));
		registerpanel.add(registerusernameLabel);

		registerusername = new JTextField();
		registerusername.setPreferredSize(new Dimension(100, 20));
		registerpanel.add(registerusername);

		registerpasswordLabel = new JLabel("Password: ");
		registerpasswordLabel.setFont(new Font("Arial Black", Font.BOLD, 14));
		registerpanel.add(registerpasswordLabel);

		registerpassword = new JPasswordField();
		registerpassword.setPreferredSize(new Dimension(100, 20));
		registerpanel.add(registerpassword);

		JPanel buttonpanel2 = new JPanel(new BorderLayout());
		register = new JButton("Register a new account");
		buttonpanel2.add(register, BorderLayout.WEST);
		register.setBackground(Color.yellow);
		register.setPreferredSize(new Dimension(300, 50));
		register.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				
				try {

					if (registerusername.getText().isEmpty() || registerpassword.getText().isEmpty()) {
						throw new EmptyFieldException("Field is empty");

					} else if (rwinterface.findinFile(registerusername.getText()) == true) {
						JOptionPane.showMessageDialog(null,
								"Sorry the username already exists.Please try another one.");
					} else {
						if (userreader.checkPassword(registerpassword.getText()) == false
								|| userreader.checkUsername(registerusername.getText()) == false) {
							JOptionPane.showMessageDialog(null,
									"Please check the username and password conditions mentioned above");
						} else {
							try {
								String name = registerusername.getText();
								if (image == null) {
									try {
										throw (new NoImageException("No image uploaded"));
									} catch (NoImageException e1) {
										LOGGER.log(Level.WARNING, "No image uploaded!", e1);
										JOptionPane.showMessageDialog(null,
												"Please upload a picture before registering!", "Error",
												JOptionPane.ERROR_MESSAGE);
									}

								} else {
					
									ImageIO.write(image, "png", new File(name + ".jpg"));
									addnewUser(userlist, registerusername.getText(),
											userreader.encrypt(registerpassword.getText()));
									rwinterface.add(writer);

									JOptionPane.showMessageDialog(null, "You have registered succesfully your account");
									registerusername.setText("");
									registerpassword.setText("");
									
								}
							} catch (IOException e1) {
								LOGGER.log(Level.SEVERE, e1.toString(), e1);
							}

						}
					}
				} catch (HeadlessException e1) {
					LOGGER.log(Level.SEVERE, e1.toString(), e1);

				} catch (EmptyFieldException e1) {
					LOGGER.log(Level.WARNING, "Field is empty", e1);
					JOptionPane.showMessageDialog(null,
							"Please enter all the necessary information for password and username", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			
			}
		}

		);

		goback = new JButton("Go back to profile");
		goback.setBackground(Color.orange);
		goback.setPreferredSize(new Dimension(300, 50));
		buttonpanel2.add(goback, BorderLayout.EAST);
		goback.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainframe.changeToProfile();

			}

		});
		add(registerpanel);
		add(buttonpanel2);
	}
/**
 * The method addnewUser adds a new user to the ArrayList 
 * @param userlist to which ArrayList of Users it has to be added
 * @param username which has to be added
 * @param password which has to be added
 */
	public void addnewUser(ArrayList<User> userlist, String username, String password) {
		User newuser = new User(username, password);
		userlist=rwinterface.getList();
		userlist.add(newuser);
	}
}