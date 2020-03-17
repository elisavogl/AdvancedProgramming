import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
/**
 * The ReaderWriterFactory is for creating the Factory Pattern 
 * and using it for other classes. 
 * @author evogl
 *
 */
public class ReaderWriterFactory {
private static final Logger LOGGER = Logger.getLogger(ReaderWriterFactory.class.getName());	

public ReaderWriterFactory(){
	try {
		Handler handler = new FileHandler(
		"LoggingFiles" + System.getProperty("file.separator") + "DeletePanel_%g_10.txt", 10000, 10);
		SimpleFormatter formatter = new SimpleFormatter();
		LOGGER.setLevel(Level.SEVERE);
		handler.setFormatter(formatter);
		LOGGER.addHandler(handler);
	} catch (SecurityException e1) {
		LOGGER.log(Level.SEVERE, e1.toString(), e1);
	} catch (IOException e1) {
		LOGGER.log(Level.SEVERE, e1.toString(), e1);
	}	
}
/**
 * The getReaderWriterInterface defines which class
 * has to be taken for the created interface ReaderWriterInterface
 * in other classes
 * @param type gets the type and which one has to be used
 * for the other classes
 * @return null if the type has not been found, returns the UserReader
 * if the type is USERREADER or returns the MovieReader if the type is 
 * MOVIEREADER
 */
public ReaderWriterInterface getReaderWriterInterface(String type) {
	if (type==null){
		return null;
	}
	else if (type.equals("USERREADER")){
		try {
			return new UserReader();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.toString(),e);
		}
	}
	else if (type.equals("MOVIEREADER")){
		try {
			return new MovieReader();
		} catch (NumberFormatException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		} catch (IOException e) {
		LOGGER.log(Level.SEVERE, e.toString(),e);
		}
	}
	return null;
}}
