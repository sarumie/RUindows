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
    private File imageFile;
    private ImageView imageView;
    private double rotation = 0;
    private double zoomFactor = 0.1;
    private Image originalImage;
    private double dragStartX, dragStartY;
    private double translateX, translateY;


    public ImageViewer() {
        this.stage = new Stage();
    }

    public void show(File file) {
        if (file == null) {
            return;
        }

        this.imageFile = file;

        stage.setTitle(file.getName());
        stage.setScene(createScene());
        stage.setWidth(800);
        stage.setHeight(600);
        stage.show();
    }

    private Scene createScene() {
        BorderPane root = new BorderPane();

        HBox menuBarContainer = new HBox();

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

        Button rotateButton = new Button("Rotate");
        rotateButton.setOnAction(e -> rotate());
        rotateButton.setStyle("-fx-background-color: transparent; -fx-padding: 5 10; -fx-font-size: 12;");
        rotateButton.setFocusTraversable(false);
        
        rotateButton.setOnMouseEntered(e -> rotateButton.setStyle("-fx-background-color: #0096C9; -fx-padding: 5 10; -fx-font-size: 12; -fx-text-fill: white;"));
        rotateButton.setOnMouseExited(e -> rotateButton.setStyle("-fx-background-color: transparent; -fx-padding: 5 10; -fx-font-size: 12;"));

        menuBarContainer.getChildren().addAll(menuBar, rotateButton);
        menuBarContainer.setAlignment(Pos.CENTER_LEFT);

        root.setTop(menuBarContainer);

        originalImage = new Image(imageFile.toURI().toString());
        imageView = new ImageView(originalImage);
        imageView.setPreserveRatio(true);

        StackPane imagePane = new StackPane(imageView);
        imagePane.setAlignment(Pos.CENTER);

        setupImageDragging(imagePane);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(imagePane);
        scrollPane.setPannable(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        root.setCenter(scrollPane);
        
        updateImageView();

        return new Scene(root);
    }

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

    private void rotate() {
        rotation = (rotation + 90) % 360;
        updateImageView();
    }

    private void updateImageView() {
        imageView.setRotate(rotation);

        double newWidth = originalImage.getWidth() * zoomFactor;
        imageView.setFitWidth(newWidth);

        imageView.setPreserveRatio(true);
    }

    public Stage getStage() {
        return stage;
    }
}