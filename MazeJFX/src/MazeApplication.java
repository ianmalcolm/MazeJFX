
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import maze.visualisation.MapGUIDisplay;
import maze.visualisation.MazeGUILayout;

public class MazeApplication extends Application {

	MapGUIDisplay mapPane = null;

	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		stage.setTitle("Maze Go");

		MazeGUILayout root = new MazeGUILayout(stage);
		Scene scene = new Scene(root, 800,600);
		stage.setScene(scene);
	    stage.setResizable(false);

		stage.show();
	}

	public static void main(String[] args) {

		launch(args);
	}

}
