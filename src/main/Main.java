package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.view.BrowserView;
import main.view.HomeView;
import main.view.LoginView;

public class Main extends Application {	

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage ps) throws Exception {
//		new BrowserView().show();
//		ps.setScene(new LoginView().getScene());
		ps.setScene(new HomeView().getScene());
		ps.setTitle("Home");
		ps.show();
	}
}
