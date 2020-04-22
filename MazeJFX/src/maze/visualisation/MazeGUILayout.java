package maze.visualisation;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import maze.Maze;
import maze.routing.NoRouteFoundException;
import maze.routing.RouteFinder;

public class MazeGUILayout extends BorderPane {

	MapGUIDisplay mapPane = null;
	RouteFinder rf = null;

	/**
	 * Construct GUI layout for display
	 * 
	 * @param stage, the stage of the window
	 */
	public MazeGUILayout(Stage stage) {

		Menu fileMenu = new Menu("File");
		MenuItem loadMapItem = new MenuItem("Load Map");
		MenuItem loadRouteItem = new MenuItem("Load Route");
		MenuItem saveRouteItem = new MenuItem("Save Route");
		SeparatorMenuItem separator = new SeparatorMenuItem();

		saveRouteItem.setDisable(true);

		fileMenu.getItems().addAll(loadMapItem, separator, loadRouteItem, saveRouteItem);
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().add(fileMenu);

		Button stepBtn = new Button();

		stepBtn.setText("Step");
		stepBtn.setDisable(true);
		stepBtn.setMinWidth(100);

		loadMapItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Map File");
				File file = fileChooser.showOpenDialog(stage);
				Maze maze = null;

				if (file != null) {
					try {
						maze = Maze.fromTxt(file.getPath());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("LoadMap Error");
						alert.setHeaderText(null);
						alert.setContentText(e.getLocalizedMessage());
						alert.showAndWait();
						return;
					}
				}
				if (maze != null) {
					rf = new RouteFinder(maze);
					saveRouteItem.setDisable(false);
					stepBtn.setDisable(false);

					mapPane.displayFromString(rf);
				}
			}
		});

		loadRouteItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open RouteFinder");
				File file = fileChooser.showOpenDialog(stage);
				if (file != null) {
					rf = RouteFinder.load(file.getPath());

					saveRouteItem.setDisable(false);
					stepBtn.setDisable(false);

					mapPane.displayFromString(rf);
				}
			}
		});

		saveRouteItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Save RouteFinder");
				File file = fileChooser.showSaveDialog(stage);
				if (file != null) {
					rf.save(file.getPath());
				}
			}
		});

		stepBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				try {
					rf.step();
					mapPane.displayFromString(rf);
				} catch (NoRouteFoundException e) {
					// TODO Auto-generated catch block
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("RouteFinder Error");
					alert.setHeaderText(null);
					alert.setContentText("The exit is not reachable from the entrance!");
					alert.showAndWait();
				}

			}
		});

		this.mapPane = new MapGUIDisplay();
		this.setTop(menuBar);
		this.setCenter(this.mapPane);
		this.setBottom(stepBtn);
		BorderPane.setAlignment(stepBtn, Pos.CENTER);
		BorderPane.setAlignment(this.mapPane, Pos.CENTER);
		BorderPane.setMargin(stepBtn, new Insets(10, 10, 10, 10));
		BorderPane.setMargin(this.mapPane, new Insets(10, 10, 10, 10));
	}
}
