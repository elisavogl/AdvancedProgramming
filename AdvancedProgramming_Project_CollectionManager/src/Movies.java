/**
 * The class movies is used for creating a new Movie or also other methods
 * for example for writing to the file
 * @author evogl
 *
 */
public class Movies {

	private final int year, oscar;
	private final String title, genre, director;


	private Movies (final String newtitle,final String newgenre,final int newyear,final String newdirector,final int newoscar){
		this.title=newtitle;
		this.genre=newgenre;
		this.year=newyear;
		this.director=newdirector;
		this.oscar=newoscar;
	}
	/**
	 * The method getYear() gets the year of the ArrayList in 
	 * other classes and does some function
	 * @return year which has to be taken by this method
	 */
	public int getYear(){
		return this.year;
	}
	/**
	 * The method getOscar() gets the oscar of the ArrayList 
	 * in other classes and does some function 
	 * @return oscar for which some function has to be done
	 */
	public int getOscar(){
		return this.oscar;
	}
	/**
	 * The method getTitle() gets the title of the ArrayList which is 
	 * needed for other classes and does some function
	 * @return the title for which some function has to be done
	 */
	public String getTitle(){
		return this.title;
	}
	/**
	 * The method getGenre() gets the genre of the ArrayList which is
	 * needed for other classes and does some function
	 * @return the genre for which some function has to be done
	 */
	public String getGenre(){
		return this.genre;
	}
	/**
	 * The method getDirector() gets the director of the ArrayList which is
	 * needed for other classes and does some function 
	 * @return the director for which some function has to be done
	 */
	public String getDirector(){
		return this.director;
	}
	/**
	 * The method String toString () is especially
	 * for writing the movies correctly to the file
	 */
	public String toString() {
        return this.title+";"+this.genre+";"+this.year+";"+this.director+";"+this.oscar+";\n";
    }
	/**
	 * The public static class Builder creates a Builder pattern for creating always 
	 * a new Movie by this method in other classes
	 * @author evogl
	 *
	 */
	public static class Builder {
	private int nestedyear,nestedoscar;
	private String nestedtitle, nestedgenre, nesteddirector;
	
	public Builder(final String newtitle,final String newgenre,final int newyear,final String newdirector,final int newoscar){
	this.nestedtitle=newtitle;
	this.nestedgenre=newgenre;
	this.nestedyear=newyear;
	this.nesteddirector=newdirector;
	this.nestedoscar=newoscar;
	}
	public Builder title (final String newtitle){
		this.nestedtitle=newtitle;
		return this;
	}
	public Builder genre (final String newgenre){
		this.nestedgenre=newgenre; 
		return this;
	}
	public Builder year (final int newyear){
		this.nestedyear=newyear;
		return this;
	}
	public Builder director (final String newdirector){
		this.nesteddirector=newdirector;
		return this;
	}
	public Builder oscar (final int newoscar){
		this.nestedoscar=newoscar;
		return this;
	}
	/**
	 * The method createMovie() creates a new Movie when
	 * necessary for other classes
	 * @return a new Object of movies with all the necessary 
	 * parameters
	 */
	public Movies createMovie(){
		return new Movies (nestedtitle,nestedgenre,nestedyear,nesteddirector
				,nestedoscar);
	}
	}

	
}
