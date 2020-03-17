import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * 
 * @author evogl In the MainFrame there is the public static void main to start
 *         the application and there is implemented also the CardLayout for
 *         jumping from one Panel to another Panel
 *
 */
public class MainFrame {

	private static JPanel contentpane;
	private static CardLayout cardlayout;
	private static LogInPanel loginpanel;
	private static MenuPanel menupanel;
	private static RegisterPanel registerpanel;
	private static ProfilePanel profilepanel;
	private static MainFrame object = new MainFrame();

	private MainFrame() {
	}

	// instantiating for Singleton Pattern
	public static MainFrame getObject() {
		return object;
	}

	public static void main(String[] args) throws IOException {

		JFrame frame = new JFrame("Movie collection");
		cardlayout = new CardLayout();
		contentpane = new JPanel(cardlayout);
		contentpane.setPreferredSize(new Dimension(800, 650));
		registerpanel = new RegisterPanel();
		profilepanel = new ProfilePanel();
		loginpanel = new LogInPanel(profilepanel);
		menupanel = new MenuPanel();

		contentpane.add(loginpanel, "loginpanel");
		contentpane.add(registerpanel, "registerpanel");
		contentpane.add(profilepanel, "profilepanel");
		contentpane.add(menupanel, "menupanel");

		frame.add(contentpane);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createMenu(frame);

		frame.pack();
		frame.setVisible(true);

	}

	/**
	 * In the function changeToRegister the current Panel switches to the
	 * RegisterPanel
	 */
	public void changeToRegister() {
		cardlayout.show(contentpane, "registerpanel");
	}

	/**
	 * In the function changeToProfile the current Panel switches to the
	 * ProfilePanel
	 */
	public void changeToProfile() {
		cardlayout.show(contentpane, "profilepanel");
	}

	/**
	 * In the function changeToLogin the current Panel switches to the
	 * LogInPanel
	 */

	public void changeToLogin() {
		cardlayout.show(contentpane, "loginpanel");
	}

	/**
	 * In the function changetoMenu the current Panel switches to the MenuPanel
	 */
	public void changetoMenu() {
		cardlayout.show(contentpane, "menupanel");
	}

	/**
	 * In the function createMenu there is created the Exit and About for
	 * closing the whole frame or have information of About.
	 * 
	 * @param frame
	 *            puts it into the correct frame
	 */

	private static void createMenu(JFrame frame) {
		JMenu menu = new JMenu("Menu");
		MenuActionListener menuAcListener = new MenuActionListener();

		JMenuItem exitMenu = new JMenuItem("Exit");
		JMenuItem aboutMenu = new JMenuItem("About");

		exitMenu.addActionListener(menuAcListener);
		menu.add(exitMenu);

		aboutMenu.addActionListener(menuAcListener);
		menu.add(aboutMenu);

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(menu);
		frame.setJMenuBar(menuBar);
	}

	private static class MenuActionListener implements ActionListener {

		public void actionPerformed(ActionEvent qst) {
			JMenuItem source = (JMenuItem) qst.getSource();
			String text = source.getText();
			if (text.equals("Exit")) {

				System.exit(0);
			}

			if (text.equals("About")) {
				String info = "This application written by Elisa Vogl is a Movie Collection in "
						+ "which you are able to make different functions for movies\n like search,"
						+ "update, delete, import data. You are only able to enter in this collection"
						+ "if you have an account registered. \n"
						+ "The application is created by 18 classes."
						;

				JPanel creditsPanel = new JPanel();

				JTextArea textArea = new JTextArea(info);
				textArea.setName("About");

				creditsPanel.add(textArea);

				JOptionPane.showMessageDialog(null, creditsPanel, "Info", 1);

			}
		}
	}
}
