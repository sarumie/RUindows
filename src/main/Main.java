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
//		new BrowserView().show();
		ps.setScene(new LoginView().getScene());
//		ps.setScene(new HomeView().getScene());
		ps.setTitle("Home");
		ps.show();
	}
}
