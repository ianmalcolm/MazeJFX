package maze;

import java.io.Serializable;

public class Tile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6800628396309368334L;
	private Type type;

	private Tile(Type ta) {
		type = ta;
	}

	public Type getType() {
		return type;
	}

	public boolean isNavigable() {
		return this.type != Type.WALL;
	}

	public boolean isEntrance() {
		return this.getType() == Type.ENTRANCE;
	}

	public boolean isExit() {
		return this.getType() == Type.EXIT;
	}

	public String toString() {
		// return type.toString();]
		if (Type.CORRIDOR == type)
			return ".";
		if (Type.WALL == type)
			return "#";
		if (Type.ENTRANCE == type)
			return "e";
		if (Type.EXIT == type)
			return "x";
		else
			return null;
	}

	protected static Tile fromChar(char c) throws InvalidMazeException {
		if (c == 'e')
			return new Tile(Type.ENTRANCE);
		if (c == 'x')
			return new Tile(Type.EXIT);
		if (c == '#')
			return new Tile(Type.WALL);
		if (c == '.')
			return new Tile(Type.CORRIDOR);
		else
			throw new InvalidMazeException("Invalid maze representation!");

	}// take a char from data(string) and give a tile from char and put it into tiles

	enum Type {
		CORRIDOR, ENTRANCE, EXIT, WALL
	}
}
