import java.io.Writer;
import java.util.ArrayList;

public interface ReaderWriterInterface {

	public boolean findinFile(String linetoremove);
	public void add (Writer writer);
	public <E> ArrayList<E> getList();
	public void write();
	
}
