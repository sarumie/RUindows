package main.view;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.Utils;

public class NotepadView {
	HomeView homeView;
	Stage notepadStage = new Stage();
	BorderPane root = new BorderPane();
	Menu fileMenu = new Menu("File");
	MenuItem saveFileMenu = new MenuItem("Save");
	MenuBar navbar = new MenuBar(fileMenu);
	TextArea txtArea = new TextArea();

	public NotepadView(HomeView homeView) {
		this.homeView = homeView;
		
	}
	
	public void show() {
		fileMenu.getItems().add(saveFileMenu);
		
		saveFileMenu.setOnAction(e -> saveFile());
		
		root.setTop(navbar);
		root.setCenter(txtArea);
		root.getStylesheets().add("style/notepad.css");
		notepadStage.setScene(new Scene(root, 1280, 720));
		notepadStage.setTitle("Notepad");
		notepadStage.getIcons().add(new Image("/style/resources/icons/notepad-icon.png"));
		notepadStage.show();
	}
	
	public void show(File file) {
		try {
			java.util.Scanner scanner = new java.util.Scanner(file);
			StringBuilder content = new StringBuilder();
			while (scanner.hasNextLine()) {
				content.append(scanner.nextLine()).append("\n");
			}
			scanner.close();
			txtArea.setText(content.toString());
			notepadStage.setTitle("Notepad - " + file.getName());
			show();
		} catch (IOException e) {
			Utils.showAlert("Error opening file", "Could not open the file: " + e.getMessage());
			// show();
		}
	}
	
	public void saveFile() {
		String saveDirectory = getClass().getResource("/files").getPath();
		
		File directory = new File(saveDirectory);
		File[] textFiles = directory.listFiles((dir, name) -> name.matches("text\\d+\\.txt") || name.matches("text.txt"));
		int nextNumber = (textFiles != null) ? textFiles.length : 0;
		TextInputDialog saveFileDialog = new TextInputDialog("text" + (nextNumber == 0 ? "" : nextNumber) + ".txt");
		saveFileDialog.setTitle("Save file");
		saveFileDialog.setHeaderText("Save file");
		saveFileDialog.setContentText("Name file:");
		Optional<String> result = saveFileDialog.showAndWait();
		result.ifPresent(fileName -> {
			// Check if filename is alphanumeric and has .txt extension
			if (!fileName.matches("^[a-zA-Z0-9]+\\.txt$")) {
				Utils.showAlert("Invalid file name", "File name is not alphanumeric");
			}

			// Check if file already exists
			File file = new File(saveDirectory +  fileName);
			if (file.exists()) {
				Utils.showAlert("File Exists", "A file with this name already exists. Please choose another name.");
				saveFile();
				return;
			}

			try (PrintWriter writer = new PrintWriter(new FileWriter(file), false)) {
				writer.print(txtArea.getText());
				homeView.addFileShortcut(file);
			} catch (IOException e) {
				Utils.showAlert("Error saving file", e.getMessage());
			}
		});
	}
}
