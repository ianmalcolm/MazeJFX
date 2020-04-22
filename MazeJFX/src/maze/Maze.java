package maze;

import java.io.*;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ian
 *
 */
public class Maze implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5739374811126527916L;
	private Tile entrance = null;
	private Tile exit = null;
	private List<List<Tile>> tiles = null;

	/**
	 * A private constructor according to the UML Do not understand the purpose of
	 * this
	 */
	private Maze() {

	}

	/**
	 * 
	 * Maze constructor for Maze.fromTxt
	 * 
	 * @param tiles a 2d list of tiles in the format of List<List<Tile>>
	 * @throws InvalidMazeException
	 */
	private Maze(List<List<Tile>> tiles) throws InvalidMazeException {
		this.tiles = tiles;

		int width = this.tiles.get(0).size();

		int cntEntrance = 0, cntExit = 0;
		for (List<Tile> row : this.tiles) {

			if (row.size() != width) {
				throw new RaggedMazeException("Ragged Maze!");
			}

			for (Tile t : row) {
				if (t.isEntrance()) {
					this.setEntrance(t);
					cntEntrance++;
				}
				if (t.isExit()) {
					this.setExit(t);
					cntExit++;
				}
			}
		}

		if (cntEntrance == 0) {
			throw new NoEntranceException("No Entrance!");
		}
		if (cntExit == 0) {
			throw new NoExitException("No Exit!");
		}
		if (cntEntrance > 1) {
			throw new MultipleEntranceException("Multiple Entrance!");
		}
		if (cntExit > 1) {
			throw new MultipleExitException("Multiple Exit!");
		}

	}

	/**
	 * Factory for constructing Maze from text file.
	 * 
	 * @param filename The path of the txt file
	 * @return Maze a maze object
	 * @throws InvalidMazeException will raise an exception if the input file has problem
	 */
	public static Maze fromTxt(String filename) throws InvalidMazeException {

		List<List<Tile>> tiles = new ArrayList<List<Tile>>();

		System.out.println("Load map from " + filename);

		try (Stream<String> lines = Files.lines(Paths.get(filename))) {
			List<String> linelists = lines.collect(Collectors.toList());

			for (String row : linelists) {
				List<Tile> rowTiles = new ArrayList<Tile>();
				for (char c : row.chars().mapToObj(e -> (char) e).collect(Collectors.toList())) {
					rowTiles.add(Tile.fromChar(c));
				}
				tiles.add(rowTiles);
			}

		} catch (MalformedInputException e) {
			throw new InvalidMazeException("Invalid maze representation!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return new Maze(tiles);

	}

	/**
	 * Given a tile t and direction d, return the neighbouring tile
	 * 
	 * @param t tile
	 * @param d direction
	 * @return the neighbouring tile or null if out of the boundary of the map
	 */
	public Tile getAdjacentTile(Tile t, Direction d) {
		Coordinate cor = this.getTileLocation(t);
		if (cor != null) {
			int x = cor.getX(), y = cor.getY();

			if (d == Direction.NORTH && y + 1 < this.getHeight()) {
				return tiles.get(y + 1).get(x);
			} else if (d == Direction.SOUTH && y - 1 >= 0) {
				return tiles.get(y - 1).get(x);
			} else if (d == Direction.EAST && x + 1 < this.getWidth()) {
				return tiles.get(y).get(x + 1);
			} else if (d == Direction.WEST && x - 1 >= 0) {
				return tiles.get(y).get(x - 1);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * @return tile of the entrance
	 */
	public Tile getEntrance() {
		return entrance;
	}

	/**
	 * @return tile of the exit
	 */
	public Tile getExit() {
		return exit;
	}

	/**
	 * @param cor the coordinate of a tile
	 * @return the tile of the coordinate
	 */
	public Tile getTileAtLocation(Coordinate cor) {
		int x = cor.getX();
		int y = this.getHeight() - 1 - cor.getY();
		return tiles.get(y).get(x);
	}

	/**
	 * @param t the tile to be looking for
	 * @return the coordinate of the tile
	 */
	public Coordinate getTileLocation(Tile t) {
		for (int y = 0; y < tiles.size(); y++) {
			int y_inv = this.getHeight() - 1 - y;
			for (int x = 0; x < tiles.get(y_inv).size(); x++) {
				if (t == tiles.get(y_inv).get(x)) {
					return new Coordinate(x, y_inv);
				}
			}
		}
		return null;
	}

	/**
	 * @return 2d list of tiles
	 */
	public List<List<Tile>> getTiles() {
		return tiles;
	}

	/**
	 * @param entrance
	 */
	private void setEntrance(Tile entrance) {
		this.entrance = entrance;
	}

	/**
	 * @param exit
	 */
	private void setExit(Tile exit) {
		this.exit = exit;
	}

	/**
	 * @return the width of the map
	 */
	public int getWidth() {
		return this.getTiles().get(0).size();
	}

	/**
	 * @return the height of the map
	 */
	public int getHeight() {
		return this.getTiles().size();
	}

	/**
	 * Convert the map to printable string
	 */
	public String toString() {

		String ret = "";

		for (int y = this.getHeight() - 1; y >= 0; y--) {
			ret += String.valueOf(y) + "\t";
			for (int x = 0; x < this.getWidth(); x++) {
				Coordinate coor = new Coordinate(x, y);
				Tile t = this.getTileAtLocation(coor);
				ret += t.toString() + "\t";
			}
			ret += "\n";
		}

		ret += "\n";
		ret += "\t";
		for (int x = 0; x < this.getWidth(); x++) {
			ret += String.valueOf(x) + "\t";
		}

		return ret;

	}

	public class Coordinate {
		private int x;
		private int y;

		public String toString() {
			return "(" + x + ", " + y + ")";
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public Coordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}

	}

	public enum Direction {
		NORTH, SOUTH, EAST, WEST;

	}
}
