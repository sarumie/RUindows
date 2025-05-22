package main.view;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import javafx.scene.Scene;
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
	File txtFile;
	Stage notepadStage = new Stage();
	BorderPane root = new BorderPane();
	Menu fileMenu = new Menu("File");
	MenuItem saveFileMenu = new MenuItem("Save");
	MenuBar navbar = new MenuBar(fileMenu);
	TextArea txtArea = new TextArea();

	public NotepadView(HomeView homeView, File txtFile) {
		this.homeView = homeView;
		this.txtFile = txtFile;
	}

	public NotepadView(HomeView homeView) {
		this(homeView, null);
	}

	public void show() {
		fileMenu.getItems().add(saveFileMenu);
		saveFileMenu.setOnAction(e -> saveFile());
		
		root.setTop(navbar);
		root.setCenter(txtArea);
		root.getStylesheets().add("style/notepad.css");
		
		if (txtFile != null) {
			try {
				java.util.Scanner scanner = new java.util.Scanner(txtFile);
				StringBuilder content = new StringBuilder();
				while (scanner.hasNextLine()) {
					content.append(scanner.nextLine()).append("\n");
				}
				scanner.close();
				txtArea.setText(content.toString());
				notepadStage.setTitle("Notepad - " + txtFile.getName());
			} catch (IOException e) {
				Utils.showAlert("Error opening file", "Could not open the file: " + e.getMessage());
			}
		} else {
			notepadStage.setTitle("Notepad");
		}
		
		notepadStage.setScene(new Scene(root, 1280, 720));
		notepadStage.getIcons().add(new Image("/style/resources/icons/notepad-icon.png"));
		notepadStage.show();
	}
	
	public void saveFile() {
		if (txtFile != null) {
			try (PrintWriter writer = new PrintWriter(new FileWriter(txtFile), false)) {
				writer.print(txtArea.getText());
			} catch (IOException e) {
				Utils.showAlert("Error saving file", e.getMessage());
			}
			return;
		}
		
		String saveDirectory = getClass().getResource("/files").getPath();
		
		File directory = new File(saveDirectory);
		File[] textFiles = directory.listFiles((dir, name) -> name.matches("text\\d+\\.txt") || name.matches("text.txt"));
		int nextNumber = (textFiles != null) ? textFiles.length : 0;
		TextInputDialog saveFileDialog = new TextInputDialog("text" + (nextNumber == 0 ? "" : nextNumber) + ".txt");
		saveFileDialog.setTitle("Save file");
		saveFileDialog.setHeaderText("Save file");
		saveFileDialog.setContentText("Rename file:");
		Optional<String> result = saveFileDialog.showAndWait();
		result.ifPresent(fileName -> {
			if (!fileName.matches("^[a-zA-Z0-9]+\\.txt$")) {
				Utils.showAlert("File name is invalid", "File name is not alphanumeric!");
				saveFile();
				return;
			}

			File file = new File(saveDirectory +  fileName);
			if (file.exists()) {
				Utils.showAlert("File with that name is aready exists!", "A file with that name has already been made");
				saveFile();
				return;
			}

			try (PrintWriter writer = new PrintWriter(new FileWriter(file), false)) {
				writer.print(txtArea.getText());
				homeView.addFileShortcut(file);
			} catch (IOException e) {
				Utils.showAlert("Error saving file", e.getMessage());
				saveFile();
				return;
			}
		});
	}
}
