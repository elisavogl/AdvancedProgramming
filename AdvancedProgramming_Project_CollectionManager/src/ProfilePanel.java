import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * 
 * @author evogl
 * The class ProfilePanel provides a description what can be done in the Movie collection and
 * shows your picture what you have uploaded during the registration. Additionally,
 * it is possible to delete your user account or register a new user
 *
 */
public class ProfilePanel extends JPanel{

	JLabel showpicture,welcomedescription,descriptionLabel,welcomeimage,welcome;
	JButton goMenu,registernew;
	JTextField jt;
	LogInPanel loginpanel;
	String username;
	private static final Logger LOGGER = Logger.getLogger(ProfilePanel.class.getName());
	
public ProfilePanel () throws IOException{

	try {
		Handler handler = new FileHandler("LoggingFiles" + System.getProperty("file.separator") + "ProfilePanel_%g_10.txt",10000,10);
		SimpleFormatter formatter=new SimpleFormatter();
		LOGGER.setLevel(Level.SEVERE);
		handler.setFormatter(formatter);
		LOGGER.addHandler(handler);
	} catch (SecurityException e1) {
		LOGGER.log(Level.SEVERE,e1.toString(),e1);
	} catch (IOException e1) {
	LOGGER.log(Level.SEVERE,e1.toString(),e1);
		
	}

MainFrame mainframe=MainFrame.getObject();
loginpanel=new LogInPanel (this);
username=loginpanel.usernametext.getText();
	
JPanel picturePanel = new JPanel(new BorderLayout ());
picturePanel.setBackground(Color.orange);
picturePanel.setPreferredSize(new Dimension(800,200));
showpicture=new JLabel ("");
picturePanel.add(showpicture,BorderLayout.CENTER);


welcome=new JLabel ("");
welcome.setIcon(new ImageIcon("Resources" + System.getProperty("file.separator") + "welcome.png"));
picturePanel.add(welcome,BorderLayout.WEST);
add(picturePanel);

JPanel welcomePanel=new JPanel (new BorderLayout());
welcomePanel.setPreferredSize(new Dimension(600,300));
	descriptionLabel = new JLabel ("<html>You can do the following things:<br>"
			+ "1. Add different movies to your account."
			+ "<br>2.Delete movies from your account."
			+ "<br>3. Import Data from your computer."
			+ "<br>4. Search a movie record.  ");
	descriptionLabel.setFont(new Font("Arial black",Font.ITALIC,15));
	welcomePanel.add(descriptionLabel,BorderLayout.WEST);
	
	add(welcomePanel);

	JPanel buttonpanel=new JPanel (new BorderLayout());

	goMenu=new JButton ("Go to your movie collection");
	goMenu.setBackground(Color.YELLOW);
	goMenu.setPreferredSize(new Dimension (200,50));
	buttonpanel.add(goMenu,BorderLayout.WEST);
	goMenu.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			mainframe.changetoMenu();
		}
	});
	
	

	registernew = new JButton("Make a new Registration for an user");
	registernew.setBackground(Color.orange);
	registernew.setPreferredSize(new Dimension (300,50));
	buttonpanel.add(registernew,BorderLayout.CENTER);
	registernew.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			mainframe.changeToRegister();
		}
	});


	add (buttonpanel);
	

		
	}
	
/**
 * uploads the image from a file and resizes it
 * @return the image/icon which has to be uploaded
 */
	public ImageIcon uploadImage (){
		
		ImageIcon icon=null;
		File image = new File (username+".jpg");
		try {
			BufferedImage img=ImageIO.read(image);
			icon=new ImageIcon(resize(img,150,200));
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error in uploading the image", e);
		}
		return icon;
	}
	/**
	 * Changes the username to the correct one which is entered in the textfield for uploading
	 * the picture
	 * @param name which has to be changed for uploading the correct picture
	 */
	public void changeUserName (String name){
		username=name;
	}
	
	/**
	 * The method BufferedImage resize(BufferedImage img, int newW, int newH) resizes all the images
	 * which has to be uploaded for the users in an uniform size
	 * @param img the image which has to be resized
	 * @param newW the width of the image
	 * @param newH the height of the image
	 * @return image in changed size
	 */
	public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}  
	
}
