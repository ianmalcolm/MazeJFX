package maze;

public class InvalidMazeException extends Exception {

	/**
	 * Family of Expcetions for various incompatible map format The naming
	 * convention of child classes are self-explanatory
	 * 
	 * @param string, raise one of this exception family to indicate a corresponding problem of the maze file
	 */
	public InvalidMazeException(String string) {
		// TODO Auto-generated constructor stub
		super(string);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4202895441597220295L;

}
