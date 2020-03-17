
/**
 * 
 * @author evogl
 *In the class User there are all the methods for reading the file User.dat and also the creation of the 
 *ArrayList for the users 
 */
public class User {
	private  String username;
	private String password;


	
	public User (String NewUsername,String NewPassword){
		this.username=NewUsername;
		this.password=NewPassword;
		 
	}
/**
 * The method getUsername takes the username from the ArrayList
 * for doing some function or changing 
 * @return the username which is used for the function
 */
	public String getUsername(){
		return username;
	}
	/**
	 * The method getPassword takes the password from the ArrayList
	 * for doing some function or changing 
	 * @return the password which is used in other classes
	 * for getting the password from the ArrayList
	 */

	public String getPassword(){
		return password;
	}
	/**
	 * The method String toString is used for how to write
	 * the ArrayList for example to the file. 
	 */
	public String toString() {
        return username+";"+password+";\n";
    }

}
