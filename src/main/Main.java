package main;

import javafx.application.Application;
import javafx.stage.Stage;
import main.view.LoginView;

public class Main extends Application {	

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage ps) throws Exception {
		ps.setScene(new LoginView().getScene());
		ps.setTitle("Login");
		ps.show();
	}
}
