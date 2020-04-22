

import maze.InvalidMazeException;
import maze.Maze;
import maze.routing.NoRouteFoundException;
import maze.routing.RouteFinder;

public class MazeDriver {
	public static void main(String[] args) {
		Maze maze = null;

		try {
			maze = Maze.fromTxt("maps/Maze1.txt");
		} catch (InvalidMazeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		System.out.println(maze.toString());

		System.out.println();

		RouteFinder rf = new RouteFinder(maze);

		try {
			while (!rf.step()) {
				System.out.println(rf.toString());

				System.out.println();

				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
			System.out.println("done!");
		} catch (NoRouteFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}