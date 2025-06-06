package main.view;

import java.io.File;
import java.util.Arrays;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import main.Utils;

public class HomeView {
	private Stage stage = new Stage();
	private FlowPane shortcuts = new FlowPane(Orientation.VERTICAL);
	private FlowPane taskbarPane = new FlowPane();
	private ImageView windowsIcon = new ImageView(new Image("/style/resources/icons/window-icon.png"));
	private ImageView notepadIcon = new ImageView(new Image("/style/resources/icons/notepad-icon.png"));
	
	private final int ICON_SIZE = 104;
	private final int SHORTCUT_SIZE = 40 + ICON_SIZE;
	
	private final String[][] DEFAULT_APPS = {
		{"Trash", "/style/resources/icons/trash-icon.png"},
		{"Notepad", "/style/resources/icons/notepad-icon.png"},
		{"Browser", "/style/resources/icons/chrome.png"}
	};

	public Scene getScene() {
		BorderPane root = new BorderPane();
		
		shortcuts.setPrefHeight(Double.MAX_VALUE);
		shortcuts.setVgap(10); 
		
		shortcuts.setPrefWrapLength(SHORTCUT_SIZE + 20); 
		shortcuts.setId("shortcuts");
		
		loadDefaultApps();
		loadFilesAsShortcuts();
		root.setLeft(shortcuts);
		setupTaskbar();

		root.setBottom(taskbarPane);
		taskbarPane.setPrefHeight(72);
		taskbarPane.setMinHeight(72);
		
		root.getStylesheets().add("style/home.css");
		
		return new Scene(root, 1920, 1080);
	}
	
	private void setupTaskbar() {
		
		taskbarPane.setId("taskbar");
		taskbarPane.setAlignment(Pos.CENTER_LEFT);
		taskbarPane.setHgap(10);
		taskbarPane.setPadding(new javafx.geometry.Insets(10, 20, 10, 20));

		int iconSize = 48;
		windowsIcon.setFitWidth(iconSize);
		windowsIcon.setPreserveRatio(true);
		
		javafx.scene.control.Button windowsButton = new javafx.scene.control.Button();
		windowsButton.setGraphic(windowsIcon);
		windowsButton.setStyle("-fx-background-color: transparent; -fx-padding: 5;");
		
		windowsButton.setOnMouseEntered(e -> windowsButton.setStyle("-fx-background-color: #0096C9; -fx-padding: 5;"));
		windowsButton.setOnMouseExited(e -> windowsButton.setStyle("-fx-background-color: transparent; -fx-padding: 5;"));
		
		notepadIcon.setFitWidth(iconSize);
		notepadIcon.setPreserveRatio(true);
		
		javafx.scene.control.Button notepadButton = new javafx.scene.control.Button();
		notepadButton.setGraphic(notepadIcon);
		notepadButton.setStyle("-fx-background-color: transparent; -fx-padding: 5;");
		
		notepadButton.setOnMouseEntered(e -> notepadButton.setStyle("-fx-background-color: #0096C9; -fx-padding: 5;"));

		VBox windowsMenu = new VBox();
		windowsMenu.getStyleClass().add("taskbar-popup-menu");
		windowsMenu.setStyle("-fx-background-color: black;");
		
		javafx.scene.control.Button logoutItem = new javafx.scene.control.Button("Logout");
		logoutItem.setTextFill(javafx.scene.paint.Color.WHITE);
		ImageView logoutIcon = new ImageView(new Image("/style/resources/icons/logout3-icon.png"));
		logoutIcon.setFitHeight(24);
		logoutIcon.setFitWidth(24);
		logoutItem.setGraphic(logoutIcon);
		logoutItem.setPadding(new javafx.geometry.Insets(10));
		logoutItem.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
		
		logoutItem.setOnMouseEntered(e -> logoutItem.setStyle("-fx-background-color: #0096C9; -fx-text-fill: white;"));
		logoutItem.setOnMouseExited(e -> logoutItem.setStyle("-fx-background-color: transparent; -fx-text-fill: white;"));
		
		javafx.scene.control.Button shutdownItem = new javafx.scene.control.Button("Shutdown");
		shutdownItem.setTextFill(javafx.scene.paint.Color.WHITE);
		ImageView shutdownIcon = new ImageView(new Image("/style/resources/icons/shutdown4-icon.png"));
		shutdownIcon.setFitHeight(24);
		shutdownIcon.setFitWidth(24);
		shutdownItem.setGraphic(shutdownIcon);
		shutdownItem.setPadding(new javafx.geometry.Insets(10));
		shutdownItem.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
		
		shutdownItem.setOnMouseEntered(e -> shutdownItem.setStyle("-fx-background-color: #0096C9; -fx-text-fill: white;"));
		shutdownItem.setOnMouseExited(e -> shutdownItem.setStyle("-fx-background-color: transparent; -fx-text-fill: white;"));
		
		windowsMenu.getChildren().addAll(logoutItem, shutdownItem);
		
		Popup popup = new javafx.stage.Popup();
		popup.getContent().add(windowsMenu);
		 
		boolean[] isPopupShowing = {false};

		windowsButton.setOnAction(e -> {
			if (isPopupShowing[0]) {
				popup.hide();
				isPopupShowing[0] = false;
			} else {
				popup.show(windowsButton, 
				windowsButton.localToScreen(0, 0).getX(), 
				windowsButton.localToScreen(0, 0).getY() - windowsMenu.prefHeight(-1));
				isPopupShowing[0] = true;
			}
		});
		
		stage.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
			if (isPopupShowing[0] && !windowsButton.getBoundsInParent().contains(windowsButton.sceneToLocal(e.getSceneX(), e.getSceneY()))
					&& !windowsMenu.getBoundsInParent().contains(windowsMenu.sceneToLocal(e.getSceneX(), e.getSceneY()))) {
				popup.hide();
				isPopupShowing[0] = false;
			}
		});
		
		logoutItem.setOnAction(e -> {
			popup.hide();
			isPopupShowing[0] = false;
			Stage currStage = (Stage) taskbarPane.getScene().getWindow();
			currStage.setScene(new LoginView().getScene());
		});
		
		shutdownItem.setOnAction(e -> System.exit(0));
		notepadButton.setOnAction(e -> new NotepadView(this).show());
		
		taskbarPane.getChildren().addAll(windowsButton, notepadButton);
	}
	
	private void loadDefaultApps() {
		for (String[] app : DEFAULT_APPS) {
			VBox shortcut = createAppShortcut(app[0], app[1]);
			shortcuts.getChildren().add(shortcut);
		}
	}


	private void loadFilesAsShortcuts() {
		File[] files = new File(getClass().getResource("/files").getFile()).listFiles();
		
		if (files != null) {
			Arrays.sort(files, (f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()));
			
			for (File file : files) {
				VBox shortcut = createFileShortcut(file);
				if (shortcut != null) {
					shortcuts.getChildren().add(shortcut);
				}
			}
		}
	}
	
	private VBox createAppShortcut(String appName, String iconPath) {
		VBox shortcut = new VBox();
		ImageView shortcutImgView = new ImageView(new Image(iconPath));
		
		shortcutImgView.setFitWidth(ICON_SIZE);
		shortcutImgView.setFitHeight(ICON_SIZE);
		shortcutImgView.setPreserveRatio(true);
		shortcut.getStyleClass().add("shortcut");
		
		shortcut.setPrefWidth(SHORTCUT_SIZE);
		shortcut.setPrefHeight(SHORTCUT_SIZE);
		shortcut.setMinWidth(SHORTCUT_SIZE);
		shortcut.setMinHeight(SHORTCUT_SIZE);
		shortcut.setMaxWidth(SHORTCUT_SIZE);
		shortcut.setMaxHeight(SHORTCUT_SIZE);
		shortcut.setAlignment(Pos.CENTER);
		
		Label nameLabel = new Label(appName);
		nameLabel.setWrapText(true);
		nameLabel.setAlignment(Pos.CENTER);
		
		shortcut.getChildren().addAll(shortcutImgView, nameLabel);
		
		shortcut.setOnMouseClicked(e -> {
			if (e.getClickCount() == 2) {
				launchApplication(appName);
			}
		});
		
		return shortcut;
	}
	
	private void launchApplication(String appName) {
		switch (appName) {
			case "Notepad":
				new NotepadView(this).show();
				break;
			case "Browser":
				new BrowserView(this).show();
				break;
		}
	}

	private VBox createFileShortcut(File file) {
		Image img;
		
		if (Utils.isImageFile(file)) {
			img = new Image(file.toURI().toString());
		} else if (Utils.isTextFile(file)) {
			img = new Image("/style/resources/icons/notepad-icon.png");
		} else {
			return null;
		}
		
		VBox shortcut = new VBox();
		ImageView shortcutImgView = new ImageView(img);
		
		shortcutImgView.setFitWidth(ICON_SIZE);
		shortcutImgView.setFitHeight(ICON_SIZE);
		shortcutImgView.setPreserveRatio(true);
		shortcut.getStyleClass().add("shortcut");
		
		shortcut.setPrefWidth(SHORTCUT_SIZE);
		shortcut.setPrefHeight(SHORTCUT_SIZE);
		shortcut.setMinWidth(SHORTCUT_SIZE);
		shortcut.setMinHeight(SHORTCUT_SIZE);
		shortcut.setMaxWidth(SHORTCUT_SIZE);
		shortcut.setMaxHeight(SHORTCUT_SIZE);
		shortcut.setAlignment(Pos.CENTER);
		
		Label nameLabel = new Label(file.getName());
		nameLabel.setWrapText(true);
		nameLabel.setAlignment(Pos.CENTER);
		
		shortcut.getChildren().addAll(shortcutImgView, nameLabel);
		
		shortcut.setOnMouseClicked(e -> {
			if (e.getClickCount() == 2) {
				if (Utils.isTextFile(file)) {
					new NotepadView(this, file).show();
				} else if (Utils.isImageFile(file)) {
					new ImageViewer().show(file);
				}
			}
		});
		
		return shortcut;
	}
	
	public void addAppShortcut(String appName, String iconPath) {
		VBox shortcut = createAppShortcut(appName, iconPath);
		shortcuts.getChildren().add(shortcut);
	}

	public void addFileShortcut(File file) {
		VBox shortcut = createFileShortcut(file);
		if (shortcut != null) {
			shortcuts.getChildren().add(shortcut);
		}
	}
	
	public void refreshShortcuts() {
		shortcuts.getChildren().clear();
		loadDefaultApps();
		loadFilesAsShortcuts();
	}
	
	public FlowPane getShortcutsPane() {
		return shortcuts;
	}
}
