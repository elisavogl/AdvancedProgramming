
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JOptionPane;

/**
 * 
 * @author evogl The MovieReader contains all methods regarding the Movie File
 *         for reading and writing
 */
public class MovieReader implements ReaderWriterInterface {

	
	
	private String MOVIE_FILENAME = "Resources" + System.getProperty("file.separator") + "Data.dat";
	private ArrayList<Movies> MovieList;
	private static final Logger LOGGER = Logger.getLogger(MovieReader.class.getName());
	private BufferedReader reader;

	public MovieReader() throws NumberFormatException, IOException {
		//creating logging file for the class MovieReader
		try {
			Handler handler = new FileHandler("LoggingFiles" + System.getProperty("file.separator") + "MovieReader%g_10.txt",10000,10);
			SimpleFormatter formatter=new SimpleFormatter();
			LOGGER.setLevel(Level.SEVERE);
			handler.setFormatter(formatter);
			LOGGER.addHandler(handler);
		} catch (SecurityException e1) {
			LOGGER.log(Level.SEVERE,e1.toString(),e1);
		} catch (IOException e1) {
			LOGGER.log(Level.SEVERE,e1.toString(),e1);
		}
		MovieList = new ArrayList<Movies>();
		MovieList = readFile();
	}
	
	/**
	 * gets the ArrayList from the movies when calling this method
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Movies> getList() {
		return MovieList;
	}

	/**
	 * The method readFile(String path) reads a file and returns it to an
	 * Arraylist for the Movies
	 * 
	 * @return arraylist from file which has to be created by splitting the
	 *         lines
	 * @throws IOException in case the file could not be read
	 * @throws NumberFormatException in case the file contains a String instead of number for oscar and year
	 */
	public ArrayList<Movies> readFile() throws NumberFormatException, IOException {

			Reader moviereader=openMovies();
			BufferedReader reader = new BufferedReader(moviereader);
			String line = "";
			String separator = ";";
			String[] allMovies;
			ArrayList<Movies> list = new ArrayList<Movies>();
			while ((line = reader.readLine()) != null) {
				allMovies = line.split(separator);

				String title = allMovies[0];
				String genre = allMovies[1];
				int year = Integer.parseInt(allMovies[2]);
				String director = allMovies[3];
				int oscar = Integer.parseInt(allMovies[4]);

				Movies movieclass=new Movies.Builder(title, genre, year, director, oscar).createMovie();	
				list.add(movieclass);

			}reader.close();
			return list;
		} 
		
		

	

	

	/**
	 * The method findinFile searches a String in the file and checks if it has
	 * been founded.
	 * 
	 * @param find entered String which has to be found in the file
	 * @return checks if the string has been found and returns true, otherwise
	 *         false
	 */

	public boolean findinFile(String find) {
		
		String line;
		try {
			
			Reader moviereader=openMovies();
			BufferedReader reader = new BufferedReader(moviereader);
			
			line = reader.readLine();
			while (line != null) {
				for (Movies getMovies : MovieList){
				if (getMovies.getTitle().equals(find)) {
					return true;
				} else {
					line = reader.readLine();
				}
			}}

		} catch (FileNotFoundException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);

		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);

		}
		finally {
			if (reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
				LOGGER.log(Level.WARNING, "Impossible to close the file");
				}
			}else {
				LOGGER.log(Level.INFO,"PrintWriter not open");
			}}
		
		return false;
	}

	/**
	 * The method write() writes the whole Arraylist to the file
	 */
	public void write() {
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(MOVIE_FILENAME)));
			for (Movies getMovie : MovieList) {
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

	/**
	 * The method add (String path) adds the new movie to the file and writes in
	 * it.
	 * @param writer: used for doing the JUnit Test
	 */
	public void add(Writer writer) {
	
		writer = null;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(MOVIE_FILENAME)));
			for (Movies getMovie : MovieList) {
				writer.write(getMovie.getTitle() + ";" + getMovie.getGenre() + ";" + getMovie.getYear() + ";"
						+ getMovie.getDirector() + ";" + getMovie.getOscar() + ";\n");
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}

		finally {
			if (writer!=null){
				try {
					writer.close();
				} catch (IOException e) {
				LOGGER.log(Level.WARNING, "Impossible to close the file");
				}
			}else {
				LOGGER.log(Level.INFO,"PrintWriter not open");
			}
		}
	}
	/**
	 * The method openMovies is used for the JUnitTests of the class 
	 * MovieReader
	 * @return in this class the Movie file, in JUnit Tests CharArrayReader
	 * or StringReader
	 * @throws FileNotFoundException
	 */
	protected Reader openMovies() throws FileNotFoundException{
		return new FileReader (MOVIE_FILENAME);
	}

}