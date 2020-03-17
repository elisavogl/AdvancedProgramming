import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MovieReaderTest {
	BufferedReader reader;
	Movies movie1, movie2;
	MovieReader moviereader;
	ReaderWriterFactory rwfactory = new ReaderWriterFactory();
	ReaderWriterInterface rwinterface = rwfactory.getReaderWriterInterface("MOVIEREADER");

	@Before
	public void setUp() throws Exception {
		moviereader = new MovieReader();
	}

	@After
	public void tearDown() throws Exception {
		moviereader = null;
	}

	// tests if the file is read successfully in an ArrayList
	@Test
	public void ArrayListreadFile() throws IOException {
		ArrayList<Movies> movielist = new ArrayList<Movies>();
		moviereader = new MovieReader() {
			protected Reader openMovies() {
				return new StringReader(
						"Baywatch;Action;2017;Seth Gordon;0;\nLogan;Action,Drama,Sci-Fi;2017;James Mangold;0;");
			};
		};
		movielist = moviereader.readFile();

		assertEquals(2, movielist.size());
		assertEquals("Action,Drama,Sci-Fi", movielist.get(1).getGenre());
		assertEquals(2017, movielist.get(0).getYear());
	}

	@Test
	public void findInFile() throws IOException {
		MovieReader moviereader = new MovieReader() {
			protected Reader openMovies() {
				return new StringReader(
						"Baywatch;Action;2017;Seth Gordon;0;\nLogan;Action,Drama,Sci-Fi;2017;James Mangold;0;\n"
								+ "Beauty and the Beast;Family, Fantasy, Musical;2017;Bill Condon;0;");
			};
		};
		String movie = "Beauty and the Beast";
		String movie1 = "Titanic";
		boolean correct = moviereader.findinFile(movie);
		boolean incorrect = moviereader.findinFile(movie1);
		assertEquals(true, correct);
		assertEquals(false, incorrect);
	}

	// testing if the user has been added successfully to the ArrayList and
	// written to the file with CharArrayWriter
	@Test
	public void add() throws NumberFormatException, IOException {
		MovieReader moviereader = new MovieReader();
		ArrayList<Movies> movielist = new ArrayList<>();
		Movies movie1 = new Movies.Builder("Logan", "Action,Drama,Sci-Fi", 2017, "James Mangold", 0).createMovie();
		Movies movie2 = new Movies.Builder("Beauty and the Beast", "Family, Fantasy, Musical", 2017, "Bill Condon", 0)
				.createMovie();

		CharArrayWriter writer = new CharArrayWriter();
		movielist.add(movie1);
		movielist.add(movie2);
		moviereader.add(writer);
		;

		assertEquals("James Mangold", movielist.get(0).getDirector());
		assertEquals(2017, movielist.get(0).getYear());

		assertEquals("Family, Fantasy, Musical", movielist.get(1).getGenre());
		assertEquals(0, movielist.get(1).getOscar());

	}

}
