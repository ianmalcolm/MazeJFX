package maze.visualisation;

import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import maze.Tile;
import maze.routing.RouteFinder;

public class MapGUIDisplay extends VBox {


	/**
	 * Display maze exploring context given a routefinder
	 * @param _rf, a routefinder object
	 */
	public void displayFromString(RouteFinder _rf) {

		this.getChildren().clear();

		for (List<Tile> rowTiles : _rf.getMaze().getTiles()) {
			
			HBox hbox = new HBox(); 
			for (Tile t : rowTiles) {
				Label label = new Label();
				label.setAlignment(Pos.CENTER);
				label.setMinSize(50, 50);

				if (!t.isNavigable()) {
					// Black for walls
					label.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
				} else if (_rf.getMaze().getExit() == t) {
					// Green for Exit
					label.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
					label.setText("X");
				} else if (_rf.getMaze().getEntrance() == t) {
					// Red for Entrance
					label.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
					label.setText("E");
				} else if (_rf.isRoute(t)) {
					// Blue for Route
					label.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
				} else {
					// White for the rest of tiles
					label.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
				}

				hbox.getChildren().add(label);
			}
			hbox.setAlignment(Pos.CENTER);
			this.getChildren().add(hbox);
		}
		
		this.setAlignment(Pos.CENTER);
	}
}
