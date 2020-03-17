
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * In the MenuPanel there can be found the whole movie collection in a JTable
 * and options with JButtons to delete,add,import data or search a movie
 * 
 * @author evogl
 *
 */
public class MenuPanel extends JPanel {

	private JButton addrecord, goback, importdataButton, deleteButton, updateButton, instructions, summarydata;
	private JTextField search, titleText, genreText, yearText, directorText, oscarText;
	private JLabel searchLabel, titleLabel, genreLabel, yearLabel, directorLabel, oscarLabel;
	private DefaultTableModel model;
	private JTable table;
	private JComboBox filterby;
	ArrayList<Movies> movielist;
	private TableRowSorter<TableModel> sorter;
	private String Movie_File = "Resources" + System.getProperty("file.separator") + "Data.dat";
	private String columnNames[];
	private JFileChooser chooseFile;
	private static MovieReader moviereader;
	int addedrecords = 0;
	int deletedrecords = 0;
	int result;
	private static final Logger LOGGER = Logger.getLogger(MenuPanel.class.getName());
	private PrintWriter writer;
	private BufferedReader reader;
	ReaderWriterFactory rwfactory = new ReaderWriterFactory();
	ReaderWriterInterface rwinterface = rwfactory.getReaderWriterInterface("MOVIEREADER");

	public MenuPanel() throws NumberFormatException, IOException {

		try {
			Handler handler = new FileHandler(
					"LoggingFiles" + System.getProperty("file.separator") + "MenuPanel_%g_10.txt", 10000, 10);
			SimpleFormatter formatter = new SimpleFormatter();
			LOGGER.setLevel(Level.SEVERE);
			handler.setFormatter(formatter);
			LOGGER.addHandler(handler);
		} catch (SecurityException e1) {
			LOGGER.log(Level.SEVERE,e1.toString(),e1);
			
		} catch (IOException e1) {
			LOGGER.log(Level.SEVERE,e1.toString(),e1);
		}

		MainFrame mainframe = MainFrame.getObject();
		moviereader = new MovieReader();
		movielist = rwinterface.getList();

		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.setPreferredSize(new Dimension(800, 300));
		columnNames = new String[] { "Title", "Genre", "Year", "Director", "Oscar" };
		table = new JTable(new DefaultTableModel(columnNames, 0) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int selectedrow = table.getSelectedRow();
				int modelIndex = table.convertRowIndexToModel(selectedrow);

				titleText.setText(model.getValueAt(modelIndex, 0).toString());
				genreText.setText(model.getValueAt(modelIndex, 1).toString());
				yearText.setText(model.getValueAt(modelIndex, 2).toString());
				directorText.setText(model.getValueAt(modelIndex, 3).toString());
				oscarText.setText(model.getValueAt(modelIndex, 4).toString());

			}
		});
		table.setBackground(Color.darkGray);
		table.setForeground(Color.white);

		model = (DefaultTableModel) table.getModel();
		Object rowData[] = new Object[5];
		for (int i = 0; i < movielist.size(); i++) {

			rowData[0] = movielist.get(i).getTitle();
			rowData[1] = movielist.get(i).getGenre();
			rowData[2] = movielist.get(i).getYear();
			rowData[3] = movielist.get(i).getDirector();
			rowData[4] = movielist.get(i).getOscar();
		
			model.addRow(rowData);

		}
		sorter = new TableRowSorter<TableModel>(model);
		table.setRowSorter(sorter);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(500, 300));
		tablePanel.add(scrollPane);

		add(tablePanel);

		JPanel searchPanel = new JPanel(new BorderLayout());

		String[] attributes = new String[] { "Searching without attribut", "Title", "Genre", "Year", "Director",
				"Oscar" };
		filterby = new JComboBox(attributes);
		searchPanel.add(filterby, BorderLayout.EAST);
		filterby.setSelectedIndex(0);
		filterby.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				search();

			}

		});

		searchLabel = new JLabel("");
		searchLabel.setIcon(new ImageIcon("Resources" + System.getProperty("file.separator") + "logosearch.png"));
		searchPanel.add(searchLabel, BorderLayout.WEST);
		search = new JTextField();
		search.setPreferredSize(new Dimension(100, 30));
		search.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				search();
			}

			public void removeUpdate(DocumentEvent e) {
				search();
			}

			public void insertUpdate(DocumentEvent e) {
				search();
			}
		});
		searchPanel.add(search, BorderLayout.CENTER);

		add(searchPanel);

		JPanel addPanel = new JPanel();
		addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));

		titleLabel = new JLabel("Movie title: ");
		titleLabel.setFont(new Font("Arial Black", Font.BOLD, 14));
		titleText = new JTextField();
		titleText.setPreferredSize(new Dimension(100, 30));
		addPanel.add(titleLabel);
		addPanel.add(titleText);

		genreLabel = new JLabel("Genre: ");
		genreLabel.setFont(new Font("Arial Black", Font.BOLD, 14));
		genreText = new JTextField();
		genreText.setPreferredSize(new Dimension(100, 30));
		addPanel.add(genreLabel);
		addPanel.add(genreText);

		yearLabel = new JLabel("Year of Publication: ");
		yearLabel.setFont(new Font("Arial Black", Font.BOLD, 14));
		yearText = new JTextField();
		yearText.setPreferredSize(new Dimension(100, 30));
		addPanel.add(yearLabel);
		addPanel.add(yearText);

		directorLabel = new JLabel("Director: ");
		directorLabel.setFont(new Font("Arial Black", Font.BOLD, 14));
		directorText = new JTextField();
		directorText.setPreferredSize(new Dimension(100, 30));
		addPanel.add(directorLabel);
		addPanel.add(directorText);

		oscarLabel = new JLabel("Oscars received: ");
		oscarLabel.setFont(new Font("Arial Black", Font.BOLD, 14));
		oscarText = new JTextField();
		oscarText.setPreferredSize(new Dimension(100, 30));
		addPanel.add(oscarLabel);
		addPanel.add(oscarText);

		add(addPanel);

		JPanel buttonpanel = new JPanel();
		buttonpanel.setLayout(new BoxLayout(buttonpanel, BoxLayout.Y_AXIS));

		updateButton = new JButton("Update");
		updateButton.setBackground(Color.yellow);
		buttonpanel.add(updateButton);
		updateButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (!oscarText.getText().matches("-?\\d+") || !yearText.getText().matches("-?\\d+")) {try {
					throw new RegexNumberException("Textfield has to contain a number");
				} catch (RegexNumberException e) {
					JOptionPane.showMessageDialog(null, "Oscar and year textfield has to have a number");
					LOGGER.log(Level.INFO, "The textfield does not contain a number");
				}
				} else if (oscarText.getText().isEmpty() || titleText.getText().isEmpty()
						|| genreText.getText().isEmpty() || yearText.getText().isEmpty()
						|| directorText.getText().isEmpty()) {
					try {
						throw new EmptyFieldException("Field is empty!");
					} catch (EmptyFieldException e) {
						JOptionPane.showMessageDialog(null,
								"Please note that we can only add a movie if you have entered all the fields.");
						LOGGER.log(Level.INFO, "Empty Field", e);
					}

				} else {
					int selectedrow = table.getSelectedRow();
					if (selectedrow == -1) {
						if (table.getRowCount() == 0) {
							JOptionPane.showMessageDialog(null, "Sorry the table is empty");

						} else {
							JOptionPane.showMessageDialog(null, "Please select the movie to update");
						}

					} else {
						int modelIndex = table.convertRowIndexToModel(table.getSelectedRow());

						for (int i = 0; i < movielist.size(); i++) {
							if (movielist.get(i).getTitle().equals(model.getValueAt(selectedrow, 0))) {
								model.setValueAt(titleText.getText(), modelIndex, 0);
								model.setValueAt(genreText.getText(), modelIndex, 1);
								model.setValueAt(yearText.getText(), modelIndex, 2);
								model.setValueAt(directorText.getText(), modelIndex, 3);
								model.setValueAt(oscarText.getText(), modelIndex, 4);

								final Movies newmovie = new Movies.Builder(titleText.getText(), genreText.getText(),
										Integer.parseInt(yearText.getText()), directorText.getText(),
										Integer.parseInt(oscarText.getText())).createMovie();

								movielist.set(i, newmovie);
								rwinterface.write();
								JOptionPane.showMessageDialog(null, "We have successfully updated the movie");
								titleText.setText("");
								genreText.setText("");
								yearText.setText("");
								directorText.setText("");
								oscarText.setText("");

							}
						}

					}
				}

			}
		});

		deleteButton = new JButton("Delete");
		deleteButton.setBackground(Color.orange);
		buttonpanel.add(deleteButton);
		deleteButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(null, "Please choose a movie in the table.");
				}
				if (table.getSelectedRow() != -1) {
					result = JOptionPane.showConfirmDialog(null,
							"Are you sure to delete the movie" + titleText.getText() + "?");
					if (result == JOptionPane.OK_OPTION) {
						int modelIndex = table.convertRowIndexToModel(table.getSelectedRow());
						model = (DefaultTableModel) table.getModel();
						model.removeRow(modelIndex);
						movielist.remove(modelIndex);
						rwinterface.write();
						deletedrecords++;
						JOptionPane.showMessageDialog(null, "We have successfully deleted the movie");
						titleText.setText("");
						genreText.setText("");
						yearText.setText("");
						directorText.setText("");
						oscarText.setText("");
					}
				}

			}

		});

		addrecord = new JButton("Add record");
		addrecord.setBackground(Color.yellow);
		buttonpanel.add(addrecord);
		addrecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				model = (DefaultTableModel) table.getModel();
				if (!oscarText.getText().matches("-?\\d+") || !yearText.getText().matches("-?\\d+")) {
					try {
						throw new RegexNumberException("Textfield has to contain a number");
					} catch (RegexNumberException e) {
						JOptionPane.showMessageDialog(null, "Oscar and year textfield has to have a number");
						LOGGER.log(Level.INFO, "The textfield does not contain a number");
					}
				
				} else if (oscarText.getText().isEmpty() || titleText.getText().isEmpty()
						|| genreText.getText().isEmpty() || yearText.getText().isEmpty()
						|| directorText.getText().isEmpty()) {
					try {
						throw new EmptyFieldException("Field is empty");
					} catch (EmptyFieldException e) {
						LOGGER.log(Level.WARNING, "One of the textfields is empty", e);
						JOptionPane.showMessageDialog(null,
								"Please note that we can only add a movie if you have entered all the fields.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}

				} else {
					if (rwinterface.findinFile(titleText.getText()) == true) {
						JOptionPane.showMessageDialog(null, "The movie already exists.");
					} else {
						addedrecords++;
						addRowforMovie();

						JOptionPane.showMessageDialog(null,
								"We have added the movie " + titleText.getText() + " successfully to your collection.");
						titleText.setText("");
						genreText.setText("");
						yearText.setText("");
						directorText.setText("");
						oscarText.setText("");
					}
					
				}
			}

		});

		importdataButton = new JButton("Import data from File");
		importdataButton.setBackground(Color.orange);
		buttonpanel.add(importdataButton);
		importdataButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				result = JOptionPane.showConfirmDialog(null,
						"<html>Please use as separator ; <br>Please note to upload only dat txt or a csv File"
								+ "<br>Please click yes if you have considered this information.");
				if (result == JOptionPane.OK_OPTION) {
					chooseFile = new JFileChooser();
					int returnVal = chooseFile.showOpenDialog(MenuPanel.this);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File uploadfile = chooseFile.getSelectedFile();
						JOptionPane.showMessageDialog(null,
								"You choose to open the following file: " + uploadfile.getName());
						String datafile = uploadfile.getAbsolutePath();
						InsertFileDataToJTable(datafile);

						rwinterface.write();
						
					}
					
				}
			}
			

		});
		add(buttonpanel);

		JPanel menuinstructionpanel = new JPanel(new FlowLayout());
		goback = new JButton("Go back to the description");
		goback.setBackground(Color.gray);
		goback.setFont(new Font("Arial Black", Font.BOLD, 12));
		goback.setForeground(Color.white);
		menuinstructionpanel.add(goback, BorderLayout.WEST);
		goback.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainframe.changeToProfile();

			}

		});

		instructions = new JButton("How to use the movie collection?");
		instructions.setBackground(Color.gray);
		instructions.setFont(new Font("Arial Black", Font.BOLD, 12));
		instructions.setForeground(Color.white);
		menuinstructionpanel.add(instructions, BorderLayout.EAST);
		instructions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String info = "<html><li>How to update data:<br>"
						+ "1. Please choose in the table what movie you would like to update in the table.<br>"
						+ "2. Write in the textfield below what column you want to change.<br>"
						+ "3. Please click on update and you will receive a confirmation.<br>"
						+ "<li>How to search data: <br>"
						+ "1. In the textfield of search you can find all the information in the table.<br>"
						+ "If you would like to search by column you have to choose the filterfunction.<br>"
						+ "<li>How to add a movie:<br>"
						+ "To a new movie you have to fill out all the information and then click on add<br>"
						+ "for saving. " + "<li>How to delete a data record:<br>"
						+ "1. Please choose in the table what movie you would like to delete.<br>"
						+ "2. Please click then on delete and the movie will be deleted.<br>"
						+ "<li>How to import data: <br>"
						+ "Please be informed that your file has to have a separator ; after all categories<br>"
						+ "title, genre, year, oscar. <br> If not the data cannot be read.<html>";

				JLabel label = new JLabel(info);

				JOptionPane.showMessageDialog(null, label);

			}

		});
		summarydata = new JButton("Summary of data");
		summarydata.setBackground(Color.gray);
		summarydata.setFont(new Font("Arial Black", Font.BOLD, 12));
		summarydata.setForeground(Color.white);
		menuinstructionpanel.add(summarydata);
		summarydata.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				File movieFile = new File(Movie_File);

				String info = "File Path to the data file:\n" + movieFile.getAbsolutePath() + " \n"
						+ "Total number of data records: " + movielist.size() + "\n" + "Total number of added records: "
						+ addedrecords + "\n" + "Total number of deleted records:  " + deletedrecords + "\n";

				JOptionPane.showMessageDialog(null, info);

			}

		});
		add(menuinstructionpanel);
	}

	/**
	 * The method search() allows the user to search in the Movie Collection
	 * filtered by Attributes or can also be searched without filtering
	 */
	public void search() {
		Object selected = filterby.getSelectedItem();
		String searchAttribut = "(?i)" + search.getText();
		if (searchAttribut.length() == 0) {
			sorter.setRowFilter(null);
		} else if ("Searching without attribut".equals(selected)) {
			sorter.setRowFilter(RowFilter.regexFilter(searchAttribut));
		} else {
			int col = -1;
			if ("Title".equals(selected)) {
				col = 0;
			} else if ("Genre".equals(selected)) {
				col = 1;
			} else if ("Year".equals(selected)) {
				col = 2;
			} else if ("Director".equals(selected)) {
				col = 3;
			} else if ("Oscar".equals(selected)) {
				col = 4;
			}
			sorter.setRowFilter(RowFilter.regexFilter(searchAttribut, col));
		}
	}

	/**
	 * In the method InsertFileDataToJTable(String filename,String separator) we
	 * are importing a file to the JTable and adding it also directly to the
	 * arraylist of Movies
	 * 
	 * @param filename
	 *            the name of the file where you have to read
	 */
	public void InsertFileDataToJTable(String filename) {
		
		Object rowData[] = new Object[5];
		String separator = ";";
		String line = "";
		String[] allMovies;

		try {
			reader = new BufferedReader(new FileReader(filename));
			while ((line = reader.readLine()) != null) {
				allMovies = line.split(separator);
				String title = allMovies[0];
				String genre = allMovies[1];
				int year = Integer.parseInt(allMovies[2]);
				String director = allMovies[3];
				int oscar = Integer.parseInt(allMovies[4]);

				if (rwinterface.findinFile(title) == true) {
					JOptionPane.showMessageDialog(null, "Please note that movie " + title
							+ " already exists. We will therefore update " + "only the movies which do not exist.");
				} else {
					model = (DefaultTableModel) table.getModel();
					rowData[0] = title;
					rowData[1] = genre;
					rowData[2] = year;
					rowData[3] = director;
					rowData[4] = oscar;
					model.addRow(rowData);
					addedrecords++;
					final Movies newmovie = new Movies.Builder(title, genre, year, director, oscar).createMovie();
					movielist.add(newmovie);
					JOptionPane.showMessageDialog(null, "We have successfully updated the collection");
				}
			}

		} catch (FileNotFoundException e) {

			LOGGER.log(Level.WARNING, "File " + Movie_File + "could not be found", e);
		} catch (NumberFormatException e) {

			LOGGER.log(Level.WARNING, "Year and oscar token does not have a number");
			JOptionPane.showMessageDialog(null,
					"Please note that the oscar and year has to have a number "
							+ "for importing successfully. We were not able to upload.",
					"Error", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error in reading the file.Please check if the file is of csv,txt or dat", "Error", JOptionPane.ERROR_MESSAGE);
			LOGGER.log(Level.WARNING, e.toString(), e);

		} catch (ArrayIndexOutOfBoundsException e) {
			JOptionPane
					.showMessageDialog(null,
							"File could not be uploaded successfully. Please check your separator "
									+ "and that all fiels have been imported successfully",
							"Error", JOptionPane.ERROR_MESSAGE);
			LOGGER.log(Level.WARNING, e.toString(), e);
		}
	

	}

	/**
	 * The method addRowforMovie() adds the new movie to the row and
	 * additionally writes it to the file and adds the new user to the ArrayList
	 */
	public void addRowforMovie() {
		Object[] row = new Object[5];

		String title = titleText.getText();
		String genre = genreText.getText();
		int year = Integer.parseInt(yearText.getText());
		String director = directorText.getText();
		int oscar = Integer.parseInt(oscarText.getText());
		row[0] = title;
		row[1] = genre;
		row[2] = year;
		row[3] = director;
		row[4] = oscar;
		model.addRow(row);
		Movies newmovie = new Movies.Builder(title, genre, year, director, oscar).createMovie();
		movielist.add(newmovie);
		rwinterface.add(writer);

	}
}
	