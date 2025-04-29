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
		Label welcomeLabel = new Label("Welcome " + "RU24-4" + " !"),
				errLabel = new Label("Wrong Password");
		PasswordField pField = new PasswordField();
		Button btnLogin  = new Button("Login");
		
		pp.setImage(new Image("style/resources/default-profile-pic.png", 200, 200, false, false));
		
		pField.setId("password");
		btnLogin.setId("btnLogin");
		vb.setId("main");
		welcomeLabel.setId("welcome");
		hb.getStyleClass().add("hbox");
		errLabel.setVisible(false);
		
		hb.getChildren().addAll(pField, btnLogin);
		vb.getChildren().addAll(pp, welcomeLabel, hb, errLabel);
		root.setCenter(vb);
		root.getStylesheets().add("style/login.css");
		
//		validate
		btnLogin.setOnMouseClicked(e -> {
			if (pField.getText().trim() != "owo") {
				errLabel.setVisible(true);
				return;
			}
			
			Stage currStage = (Stage) root.getScene().getWindow();
			Stage stage = new Stage();
			stage.initModality(currStage.getModality());
			stage.initOwner(currStage);
		});
		
		sc = new Scene(root, 1920, 1080);
		return sc;
	}
	
	public void homePage() {
		
	}
}
