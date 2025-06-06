package main.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginView {
	BorderPane root = new BorderPane();
	VBox vb = new VBox();
	HBox hb = new HBox();
	ImageView pp = new ImageView();
	Label welcomeLabel = new Label("Welcome " + "RU24-4" + " !"),
			errLabel = new Label("Wrong password!");
	PasswordField pField = new PasswordField();
	Button btnLogin  = new Button("Login");
	
	String password = "user123";
	
	public Scene getScene() {
		pp.setImage(new Image("/style/resources/icons/default_profile_pic.png", 200, 200, true, false));
		
		pField.setId("password");
		btnLogin.setId("btnLogin");
		vb.setId("main");
		welcomeLabel.setId("welcome");
		errLabel.setId("err");
		hb.getStyleClass().add("hbox");
		errLabel.setVisible(false);
		
		hb.getChildren().addAll(pField, btnLogin);
		vb.getChildren().addAll(pp, welcomeLabel, hb, errLabel);
		root.setCenter(vb);
		root.getStylesheets().add("style/login.css");
		
		// Validate
		btnLogin.setOnMouseClicked(e -> {
			if (!pField.getText().trim().equals(password)) {
				errLabel.setVisible(true);
				return;
			}
			
			Stage currStage = (Stage) root.getScene().getWindow();
			currStage.setScene(new HomeView().getScene());
			currStage.show();
		});

		return new Scene(root, 1920, 1080);
	}
}
