package main.view;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
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
	private Menu windows = new Menu();
	private Menu notepad = new Menu();
	
	// Define uniform shortcut size
	private final int SHORTCUT_SIZE = 120;
	private final int ICON_SIZE = 80;
	
	// Default system applications
	private final String[][] DEFAULT_APPS = {
		{"Trash", "/style/resources/icons/trash-icon.png"},
		{"Notepad", "/style/resources/icons/notepad-icon.png"},
		{"Browser", "/style/resources/icons/chrome.png"}
	};
	
	/**
	 * Creates and returns the main scene
	 * @return The main application scene
	 */
	public Scene getScene() {
		BorderPane root = new BorderPane();
		
		// Make shortcuts container fill the available height
		shortcuts.setPrefHeight(Double.MAX_VALUE);
		shortcuts.setVgap(10); // Add some vertical gap between shortcuts
		
		// Calculate width to fit 5 vertical shortcuts
		shortcuts.setPrefWrapLength(SHORTCUT_SIZE + 20); // Width plus some margin
		shortcuts.setId("shortcuts");
		
		// Load default applications
		loadDefaultApps();
		
		// Load files from the files directory
		loadFilesAsShortcuts();
		
		// Change to LEFT instead of TOP to allow the taskbar to be visible at the bottom
		root.setLeft(shortcuts);
		
		// Setup the taskbar
		setupTaskbar();
		root.setBottom(taskbarPane);
		
		// Give the taskbar adequate size to be visible
		taskbarPane.setPrefHeight(72);
		taskbarPane.setMinHeight(72);
		
		root.getStylesheets().add("style/home.css");
		
		return new Scene(root, 1920, 1080);
	}
	
	/**
	 * Sets up the taskbar with icons and menus
	 */
	private void setupTaskbar() {
		// Create a FlowPane as the taskbar container
		taskbarPane.setId("taskbar");
		taskbarPane.setAlignment(Pos.CENTER_LEFT);
		taskbarPane.setHgap(10);
		taskbarPane.setPadding(new javafx.geometry.Insets(10, 20, 10, 20));

		// Set up the Windows button
		int iconSize = 48;
		windowsIcon.setFitWidth(iconSize);
		windowsIcon.setPreserveRatio(true);
		
		javafx.scene.control.Button windowsButton = new javafx.scene.control.Button();
		windowsButton.setGraphic(windowsIcon);
		windowsButton.setStyle("-fx-background-color: transparent; -fx-padding: 5;");
		
		// Apply hover effect
		windowsButton.setOnMouseEntered(e -> windowsButton.setStyle("-fx-background-color: #0096C9; -fx-padding: 5;"));
		windowsButton.setOnMouseExited(e -> windowsButton.setStyle("-fx-background-color: transparent; -fx-padding: 5;"));
		
		// Set up the Notepad button
		notepadIcon.setFitWidth(iconSize);
		notepadIcon.setPreserveRatio(true);
		
		javafx.scene.control.Button notepadButton = new javafx.scene.control.Button();
		notepadButton.setGraphic(notepadIcon);
		notepadButton.setStyle("-fx-background-color: transparent; -fx-padding: 5;");
		
		// Apply hover effect
		notepadButton.setOnMouseEntered(e -> notepadButton.setStyle("-fx-background-color: #0096C9; -fx-padding: 5;"));
		notepadButton.setOnMouseExited(e -> notepadButton.setStyle("-fx-background-color: transparent; -fx-padding: 5;"));
		
		// Create popup menu for Windows button
		VBox windowsMenu = new VBox();
		windowsMenu.getStyleClass().add("taskbar-popup-menu");
		windowsMenu.setStyle("-fx-background-color: black;");
		
		// Logout option - convert to button with hover effect
		javafx.scene.control.Button logoutItem = new javafx.scene.control.Button("Logout");
		logoutItem.setTextFill(javafx.scene.paint.Color.WHITE);
		ImageView logoutIcon = new ImageView(new Image("/style/resources/icons/logout3-icon.png"));
		logoutIcon.setFitHeight(24);
		logoutIcon.setFitWidth(24);
		logoutItem.setGraphic(logoutIcon);
		logoutItem.setPadding(new javafx.geometry.Insets(10));
		logoutItem.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
		
		// Apply hover effect
		logoutItem.setOnMouseEntered(e -> logoutItem.setStyle("-fx-background-color: #0096C9; -fx-text-fill: white;"));
		logoutItem.setOnMouseExited(e -> logoutItem.setStyle("-fx-background-color: transparent; -fx-text-fill: white;"));
		
		// Shutdown option - convert to button with hover effect
		javafx.scene.control.Button shutdownItem = new javafx.scene.control.Button("Shutdown");
		shutdownItem.setTextFill(javafx.scene.paint.Color.WHITE);
		ImageView shutdownIcon = new ImageView(new Image("/style/resources/icons/shutdown4-icon.png"));
		shutdownIcon.setFitHeight(24);
		shutdownIcon.setFitWidth(24);
		shutdownItem.setGraphic(shutdownIcon);
		shutdownItem.setPadding(new javafx.geometry.Insets(10));
		shutdownItem.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
		
		// Apply hover effect
		shutdownItem.setOnMouseEntered(e -> shutdownItem.setStyle("-fx-background-color: #0096C9; -fx-text-fill: white;"));
		shutdownItem.setOnMouseExited(e -> shutdownItem.setStyle("-fx-background-color: transparent; -fx-text-fill: white;"));
		
		windowsMenu.getChildren().addAll(logoutItem, shutdownItem);
		
		// Set up popup behavior
		Popup popup = new javafx.stage.Popup();
		popup.getContent().add(windowsMenu);
		
		 // Track popup state
		boolean[] isPopupShowing = {false};
		
		// Add event handlers for toggling the popup
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
		
		// Add global event filter to close popup when clicking outside
		stage.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
			if (isPopupShowing[0] && !windowsButton.getBoundsInParent().contains(windowsButton.sceneToLocal(e.getSceneX(), e.getSceneY()))
					&& !windowsMenu.getBoundsInParent().contains(windowsMenu.sceneToLocal(e.getSceneX(), e.getSceneY()))) {
				popup.hide();
				isPopupShowing[0] = false;
			}
		});
		
		// Update logoutItem and shutdownItem actions to update popup state
		logoutItem.setOnAction(e -> {
			popup.hide();
			isPopupShowing[0] = false;
			Stage currStage = (Stage) taskbarPane.getScene().getWindow();
			currStage.setScene(new LoginView().getScene());
		});
		
		shutdownItem.setOnAction(e -> System.exit(0));
		
		notepadButton.setOnAction(e -> new NotepadView(this).show());
		
		// Add buttons to taskbar
		taskbarPane.getChildren().addAll(windowsButton, notepadButton);
	}
	
	/**
	 * Loads default system applications as shortcuts
	 */
	private void loadDefaultApps() {
		for (String[] app : DEFAULT_APPS) {
			VBox shortcut = createAppShortcut(app[0], app[1]);
			shortcuts.getChildren().add(shortcut);
		}
	}
	
	/**
	 * Loads files from the /files directory and adds them as shortcuts
	 */
	private void loadFilesAsShortcuts() {
		File[] files = new File(getClass().getResource("/files").getFile()).listFiles();
		
		if (files != null) {
			for (File file : files) {
				VBox shortcut = createFileShortcut(file);
				if (shortcut != null) {
					shortcuts.getChildren().add(shortcut);
				}
			}
		}
	}
	
	/**
	 * Creates a shortcut for system applications
	 * @param appName Name of the application
	 * @param iconPath Path to the application icon
	 * @return VBox containing the shortcut
	 */
	private VBox createAppShortcut(String appName, String iconPath) {
		VBox shortcut = new VBox();
		ImageView shortcutImgView = new ImageView(new Image(iconPath));
		
		shortcutImgView.setFitWidth(ICON_SIZE);
		shortcutImgView.setFitHeight(ICON_SIZE);
		shortcutImgView.setPreserveRatio(true);
		shortcut.getStyleClass().add("shortcut");
		
		// Make shortcut square with fixed dimensions
		shortcut.setPrefWidth(SHORTCUT_SIZE);
		shortcut.setPrefHeight(SHORTCUT_SIZE);
		shortcut.setMinWidth(SHORTCUT_SIZE);
		shortcut.setMinHeight(SHORTCUT_SIZE);
		shortcut.setMaxWidth(SHORTCUT_SIZE);
		shortcut.setMaxHeight(SHORTCUT_SIZE);
		
		// Center content
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
	
	/**
	 * Launches the appropriate application based on the application name
	 * @param appName Name of the application to launch
	 */
	private void launchApplication(String appName) {
		switch (appName) {
			case "Notepad":
				new NotepadView(this).show();
				break;
			case "Browser":
				new BrowserView(this).show();
				break;
			case "Trash":
				// Trash functionality to be implemented
				break;
		}
	}
	
	/**
	 * Creates a shortcut for a file
	 * @param file The file to create a shortcut for
	 * @return VBox containing the shortcut or null if file type is not supported
	 */
	private VBox createFileShortcut(File file) {
		Image img;
		
		if (Utils.isImageFile(file)) {
			img = new Image(file.toURI().toString());
		} else if (Utils.isTextFile(file)) {
			img = new Image("/style/resources/icons/notepad-icon.png");
		} else {
			return null; // Skip unsupported file types
		}
		
		VBox shortcut = new VBox();
		ImageView shortcutImgView = new ImageView(img);
		
		shortcutImgView.setFitWidth(ICON_SIZE);
		shortcutImgView.setFitHeight(ICON_SIZE);
		shortcutImgView.setPreserveRatio(true);
		shortcut.getStyleClass().add("shortcut");
		
		// Make shortcut square with fixed dimensions
		shortcut.setPrefWidth(SHORTCUT_SIZE);
		shortcut.setPrefHeight(SHORTCUT_SIZE);
		shortcut.setMinWidth(SHORTCUT_SIZE);
		shortcut.setMinHeight(SHORTCUT_SIZE);
		shortcut.setMaxWidth(SHORTCUT_SIZE);
		shortcut.setMaxHeight(SHORTCUT_SIZE);
		
		// Center content
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
					new ImageViewer(this).show(file);
				}
			}
		});
		
		return shortcut;
	}
	
	/**
	 * Adds a new application shortcut to the desktop
	 * @param appName Name of the application
	 * @param iconPath Path to the application icon
	 */
	public void addAppShortcut(String appName, String iconPath) {
		VBox shortcut = createAppShortcut(appName, iconPath);
		shortcuts.getChildren().add(shortcut);
	}
	
	/**
	 * Adds a new file shortcut to the desktop
	 * @param file File to add as shortcut
	 */
	public void addFileShortcut(File file) {
		VBox shortcut = createFileShortcut(file);
		if (shortcut != null) {
			shortcuts.getChildren().add(shortcut);
		}
	}
	
	/**
	 * Removes all shortcuts and reloads them
	 */
	public void refreshShortcuts() {
		shortcuts.getChildren().clear();
		loadDefaultApps();
		loadFilesAsShortcuts();
	}
	
	/**
	 * Get access to the shortcuts FlowPane
	 * @return The shortcuts FlowPane
	 */
	public FlowPane getShortcutsPane() {
		return shortcuts;
	}
}
