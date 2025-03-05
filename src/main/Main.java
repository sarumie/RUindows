package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {
	Scene sc;
	GridPane gp;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage ps) throws Exception {
		gp = new GridPane();
		sc = new Scene(gp, 500, 500);
		
		ps.setScene(sc);
		ps.setTitle("Init");
		ps.show();
	}

}
