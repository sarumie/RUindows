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

public class Main extends Application {	

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage ps) throws Exception {	
		ps.setScene(loginPage());
		ps.setTitle("Login");
		ps.show();
	}
	
	public Scene loginPage() {
		BorderPane root = new BorderPane();
		Scene sc;
		VBox vb = new VBox();
		HBox hb = new HBox();
		ImageView pp = new ImageView();
		Label welcomeLabel = new Label("Welcome " + System.getProperty("user.name") + " !"), messageLabel = new Label();
		PasswordField pField = new PasswordField();
		Button btnLogin  = new Button("Login");
		
		pp.setImage(new Image("style/resources/default-profile-pic.png", 200, 200, false, false));
		
		pField.setId("password");
		btnLogin.setId("btnLogin");
		vb.setId("main");
		hb.getStyleClass().add("wrap8");
		
		hb.getChildren().addAll(pField, btnLogin);
		vb.getChildren().addAll(pp, welcomeLabel, hb, messageLabel);
		root.setCenter(vb);
		root.getStylesheets().add("style/main.css");
		
		sc = new Scene(root, 1920, 1080);
		
		return sc;
	}
	
	public void homePage() {
		
	}
}
