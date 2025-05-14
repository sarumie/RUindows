package main.view;

import java.io.File;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ImageViewer {
    private Stage stage;
    private HomeView homeView;
    private File imageFile;
    private ImageView imageView;
    private double rotation = 0;
    private double zoomFactor = 0.1;
    private Image originalImage;
    private double dragStartX, dragStartY;
    private double translateX, translateY;

    /**
     * Constructor for the ImageViewer
     * @param homeView Reference to the HomeView
     */
    public ImageViewer(HomeView homeView) {
        this.homeView = homeView;
        this.stage = new Stage();
    }

    /**
     * Shows the ImageViewer with the specified image file
     * @param file The image file to display
     */
    public void show(File file) {
        if (file == null) {
            return;
        }

        this.imageFile = file;

        // Set up the stage
        stage.setTitle(file.getName());
        stage.setScene(createScene());
        stage.setWidth(800);
        stage.setHeight(600);
        stage.show();
    }

    /**
     * Creates and returns the scene for the ImageViewer
     * @return The scene for the ImageViewer
     */
    private Scene createScene() {
        BorderPane root = new BorderPane();

        // Create the menu bar and button
        HBox menuBarContainer = new HBox();

        // Create Zoom menu with slider
        MenuBar menuBar = new MenuBar();
        Menu zoomMenu = new Menu("Zoom");

        Slider zoomSlider = new Slider(0.1, 5.0, 0.0);
        zoomSlider.setPrefWidth(200);

        zoomSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            zoomFactor = newVal.doubleValue();
            updateImageView();
        });

        zoomMenu.setGraphic(zoomSlider);
        menuBar.getMenus().add(zoomMenu);

        // Create rotate button that looks like menu
        Button rotateButton = new Button("Rotate");
        rotateButton.setOnAction(e -> rotate());
        rotateButton.setStyle("-fx-background-color: transparent; -fx-padding: 5 10; -fx-font-size: 12;");
        rotateButton.setFocusTraversable(false);
        
        // Add hover effect with specified color
        rotateButton.setOnMouseEntered(e -> rotateButton.setStyle("-fx-background-color: #0096C9; -fx-padding: 5 10; -fx-font-size: 12; -fx-text-fill: white;"));
        rotateButton.setOnMouseExited(e -> rotateButton.setStyle("-fx-background-color: transparent; -fx-padding: 5 10; -fx-font-size: 12;"));

        // Add both to container
        menuBarContainer.getChildren().addAll(menuBar, rotateButton);
        menuBarContainer.setAlignment(Pos.CENTER_LEFT);

        root.setTop(menuBarContainer);

        // Load the image
        originalImage = new Image(imageFile.toURI().toString());
        imageView = new ImageView(originalImage);
        imageView.setPreserveRatio(true);

        // Create a stack pane to center the image
        StackPane imagePane = new StackPane(imageView);
        imagePane.setAlignment(Pos.CENTER);

        // Set up image dragging
        setupImageDragging(imagePane);

        // Create a scroll pane to hold the image view
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(imagePane);
        scrollPane.setPannable(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        root.setCenter(scrollPane);
        
        updateImageView();

        return new Scene(root);
    }

    /**
     * Sets up image dragging capability
     * @param imagePane The pane containing the image
     */
    private void setupImageDragging(StackPane imagePane) {
        imagePane.setOnMousePressed(event -> {
            dragStartX = event.getSceneX();
            dragStartY = event.getSceneY();
            translateX = imageView.getTranslateX();
            translateY = imageView.getTranslateY();
        });

        imagePane.setOnMouseDragged(event -> {
            double offsetX = event.getSceneX() - dragStartX;
            double offsetY = event.getSceneY() - dragStartY;
            imageView.setTranslateX(translateX + offsetX);
            imageView.setTranslateY(translateY + offsetY);
        });
    }

    /**
     * Rotates the image by 90 degrees
     */
    private void rotate() {
        rotation = (rotation + 90) % 360;
        updateImageView();
    }

    /**
     * Updates the image view with the current rotation and zoom values
     */
    private void updateImageView() {
        // Apply rotation
        imageView.setRotate(rotation);

        // Apply zoom
        double newWidth = originalImage.getWidth() * zoomFactor;
        imageView.setFitWidth(newWidth);

        // Preserve aspect ratio
        imageView.setPreserveRatio(true);
    }

    /**
     * Returns the stage of this ImageViewer
     * @return The stage
     */
    public Stage getStage() {
        return stage;
    }
}