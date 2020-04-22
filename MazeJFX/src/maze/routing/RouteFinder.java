package maze.routing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

import maze.Maze;
import maze.Tile;
import maze.Maze.Coordinate;

public class RouteFinder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8213820114545509581L;

	private Maze maze = null;
	private Stack<Tile> route = null;
	private boolean finished = false;

	private List<Tile> visited = null;

	public List<Maze.Direction> allDir = null;

	/**
	 * Construct a RouteFinder from a Maze object.
	 * @param m, a Maze object
	 */
	public RouteFinder(Maze m) {
		this.maze = m;
		this.route = new Stack<Tile>();
		this.finished = false;
		this.visited = new ArrayList<Tile>();

		this.route.push(this.maze.getEntrance());
		this.visited.add(this.maze.getEntrance());

		this.allDir = new ArrayList<Maze.Direction>();
		this.allDir.add(Maze.Direction.EAST);
		this.allDir.add(Maze.Direction.WEST);
		this.allDir.add(Maze.Direction.NORTH);
		this.allDir.add(Maze.Direction.SOUTH);

	}

	/**
	 * Get the maze to solve
	 * @return maze to solve
	 */
	public Maze getMaze() {
		return maze;
	}

	/**
	 * Get route explored so far
	 * @return the route explored so far
	 */
	public List<Tile> getRoute() {
		return this.route;
	}

	/**
	 * Check if the maze have been solved
	 * @return whether the maze have been solved
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * Load serialized RouteFinder
	 * @param filename the path of the file
	 * @return the RouteFinder object recovered from the input file
	 */
	public static RouteFinder load(String filename) {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(filename)))) {
			return (RouteFinder) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Serialize this RouteFinder object and save it as a filename 
	 * @param filename the path of the file
	 */
	public void save(String filename) {

		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(filename)))) {
			oos.writeObject(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Move a step in the maze.
	 * The strategy for solving the maze is Depth First Search (DFS)
	 * All reachable tiles is virtually transformed into a tree
	 * structure by "findUnvisited" and traversed with DFS strategy.    
	 * 
	 * @return whether the maze has been solved
	 * @throws NoRouteFoundException will raise an exception if the exit is not reachable from the entrance
	 */
	public boolean step() throws NoRouteFoundException {

		if (this.isFinished()) {
			return true;
		}

		if (this.route.isEmpty()) {
			// when this happens, the whole tree has been
			// traversed and no exit has been found, which
			// means the exit is not reachable from entrance
			throw new NoRouteFoundException();
		}

		Tile curTile = this.route.peek();
		List<Tile> unVisited = this.findUnvisited(curTile);

		if (unVisited.isEmpty()) {
			this.route.pop();
		} else {
			curTile = unVisited.get(0);
			this.visited.add(curTile);
			this.route.push(curTile);
		}

		if (curTile.isExit()) {
			this.finished = true;
		}

		return this.isFinished();
	}

	/**
	 * Find all unvisited and reachable neighbouring tiles
	 * @param t
	 * @return
	 */
	private List<Tile> findUnvisited(Tile t) {
		List<Tile> unVisited = new ArrayList<Tile>();
		for (Maze.Direction d : this.allDir) {
			Tile td = this.maze.getAdjacentTile(t, d);
			if (td != null && td.isNavigable() && !this.isVisited(td)) {
				unVisited.add(td);
			}
		}

		return unVisited;

	}

	/**
	 * Check if the given tile has been visited before
	 * @param t, the tile to check
	 * @return whether the tile has been visited
	 */
	public boolean isVisited(Tile t) {
		return this.visited.contains(t);
	}

	/**
	 * Check if the tile is a part of the route
	 * @param t, the tile to check
	 * @return whether the tile is a part of the route
	 */
	public boolean isRoute(Tile t) {
		return this.route.contains(t);
	}

	public String toString() {

		String ret = "";

		for (int y = this.getMaze().getHeight() - 1; y >= 0; y--) {
			ret += String.valueOf(y) + "\t";
			for (int x = 0; x < this.getMaze().getWidth(); x++) {
				Coordinate coor = this.getMaze().new Coordinate(x, y);
				Tile t = this.getMaze().getTileAtLocation(coor);
				if (this.isRoute(t)) {
					ret += "*\t";
				} else {
					ret += t.toString() + "\t";
				}

			}
			ret += "\n";
		}

		ret += "\n";
		ret += "\t";
		for (int x = 0; x < this.getMaze().getWidth(); x++) {
			ret += String.valueOf(x) + "\t";
		}

		return ret;
	}

}