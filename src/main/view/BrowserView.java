package main.view;

import java.io.File;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Utils;

public class BrowserView {

	HomeView homeView;

	Stage stage = new Stage();
	BorderPane root = new BorderPane();
	BorderPane main = new BorderPane();
	FlowPane header = new FlowPane();
	FlowPane containerTxt = new FlowPane();
	TextField searchField = new TextField();
	Button searchBtn = new Button("Search");
	HBox btnContainer = new HBox();
	Text headerTxt = new Text();
	Text descTxt = new Text();
	Text siteNameTxt = new Text();

	BrowserView(HomeView homeView) {
		this.homeView = homeView;
	}
	
	public void show() {
		header.setId("header");
		searchField.setId("searchField");
		headerTxt.setId("headerTxt");
		descTxt.setId("descTxt");
		containerTxt.setId("containerTxt");
		siteNameTxt.setId("siteNameTxt");
		btnContainer.setId("btnContainer");
		
		searchBtn.setOnAction(e -> showContent(searchField.getText()));
		searchField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
            	searchBtn.fire();
            }
		});
		
		header.getChildren().addAll(searchField, searchBtn);
		root.setTop(header);
		root.setCenter(main);
		root.getStylesheets().add("style/browser.css");
		stage.setScene(new Scene(root, 1280, 720));
		stage.setTitle("ChRUme");
		stage.getIcons().add(new Image("/style/resources/icons/chrome.png"));
		stage.show();
	}
	
	public void showContent(String url) {
		Utils.deepClearChildren(main);
		main.setId("");
		if (url.equals("RUtube.net")) {
		// if (url.equals("r")) {
			main.setId("mainRUtube");
			
			FlowPane navbar = new FlowPane();
			FlowPane videoPlayerContainer = new FlowPane(Orientation.VERTICAL); 
			ImageView icon = new ImageView(new Image("style/resources/icons/youtube-logo.png"));
			MediaPlayer videoPlayer = new MediaPlayer(new Media(new File(getClass().getResource("/style/resources/videos/DiamondJack.mp4").getFile()).toURI().toString()));
	        MediaView videoViewer = new MediaView(videoPlayer);
	        Button playBtn = new Button("Play");
	        Button pauseBtn = new Button("Pause");
	        
	        siteNameTxt.setText("RUtube");
	        icon.setFitWidth(32);
	        icon.setPreserveRatio(true);
	        navbar.setHgap(16);
	        videoViewer.setFitWidth(1000);
	        videoPlayerContainer.setAlignment(Pos.CENTER);
	        btnContainer.setAlignment(Pos.TOP_CENTER);
	        
	        playBtn.setOnAction(e -> videoPlayer.play());
	        pauseBtn.setOnAction(e -> videoPlayer.pause());
	        
	        navbar.getChildren().addAll(icon, siteNameTxt);
	        btnContainer.getChildren().addAll(playBtn, pauseBtn);
	        videoPlayerContainer.getChildren().addAll(videoViewer, btnContainer);
	        main.setTop(navbar);
	        main.setCenter(videoPlayerContainer);
	        return;
		}
		if (url.equals("RUtify.net")) {
			main.setId("mainRUtify");
			
			FlowPane navbar = new FlowPane();
			MediaPlayer audioPlayer = new MediaPlayer(new Media(new File(getClass().getResource("/style/resources/audios/PromQueen.mp3").getFile()).toURI().toString()));
			FlowPane audioPlayerContainer = new FlowPane(Orientation.VERTICAL);
			ImageView icon = new ImageView(new Image("style/resources/icons/spotify-logo.png"));
			Slider playerSlider = new Slider(0, 100, 0);
			Button playBtn = new Button("Play");
			Button pauseBtn = new Button("Pause");
						
			siteNameTxt.setText("RUtify");
			icon.setFitWidth(32);
			icon.setPreserveRatio(true);
			navbar.setHgap(16);
			navbar.paddingProperty().setValue(new javafx.geometry.Insets(16));
			playerSlider.setPrefWidth(400);
			audioPlayerContainer.setAlignment(Pos.CENTER);
			btnContainer.setAlignment(Pos.TOP_CENTER);
			
			playBtn.setOnAction(e -> audioPlayer.play());
			pauseBtn.setOnAction(e -> audioPlayer.pause());
			audioPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
                double total = audioPlayer.getTotalDuration().toSeconds();
                double current = newTime.toSeconds();
                playerSlider.setValue((current / total) * 100);
	        });
			
			navbar.getChildren().addAll(icon, siteNameTxt);
			audioPlayerContainer.getChildren().addAll(playerSlider, btnContainer);
			btnContainer.getChildren().addAll(playBtn, pauseBtn);
			main.setTop(navbar);
			main.setCenter(audioPlayerContainer);
			return;
		}
		if (url.equals("stockimages.net")) {
			main.setId("mainSI");
			
			ScrollPane scrollContainer = new ScrollPane();
			FlowPane container = new FlowPane();
			String[][] images = {
				{"orangecat.jpg", "cat-image1.jpg"},
				{"graycat.jpg", "cat-image2.jpg"},
				{"blackcat.jpg", "cat-image3.jpg"},
				{"graycat.jpg", "cat-image4.jpg"},
			};
			
			for (String imageData[] : images) {
				HBox imageWrapper = new HBox();
				Button downloadBtn = new Button("download");
				Image image = new Image("style/resources/pictures/" + imageData[1]);
				ImageView imageView = new ImageView(image);
				
				imageWrapper.setAlignment(Pos.CENTER_LEFT);
				imageWrapper.setSpacing(32);
				imageView.setFitWidth(600);
				imageView.setPreserveRatio(true);
				
				downloadBtn.setOnAction(e -> saveFile(imageData, image));
				
				imageWrapper.getChildren().addAll(imageView, downloadBtn);
				container.getChildren().add(imageWrapper);
			}
			
			container.setId("containerSI");
			scrollContainer.setId("scrollContainerSI");
			
			scrollContainer.setContent(container);
			main.setCenter(scrollContainer);
			return;
		}
		
		headerTxt.setText("This site can't be reached");
		descTxt.setText(url + " does not exist, try checking your spelling");
		descTxt.setWrappingWidth(800);
		containerTxt.getChildren().addAll(headerTxt, descTxt);
		main.setCenter(containerTxt);
	}
	
	public void saveFile(String[] imageData, Image image) {
		try {
			TextInputDialog dialog = new TextInputDialog(imageData[0]);
			dialog.setTitle("Save Image");
			dialog.setHeaderText("Save Image");
			dialog.setContentText("Rename file:");
			
			dialog.showAndWait().ifPresent(fileName -> {
				File outputFile = new File(getClass().getResource("/files").getPath() + fileName);
				if (!fileName.matches("[a-zA-Z0-9]+\\.jpg$")) {
					Utils.showAlert("File name is invalid", "File name is not alphanumeric!");
					saveFile(imageData, image);
					return;
				}
				
				if (outputFile.exists()) {
					Utils.showAlert("File with that name is aready exists!", "A file with that name has already been made");
					saveFile(imageData, image);
					return;
				}

				try {
					ImageIO.write(SwingFXUtils.fromFXImage(image, null), "jpg", outputFile);
					
					System.out.println("Downloaded: " + fileName);
					homeView.addFileShortcut(outputFile);
				} catch (Exception ex) {
					System.err.println("Error saving image: " + ex.getMessage());
					new Alert(Alert.AlertType.ERROR, "Error saving image: " + ex.getMessage()).showAndWait();
				}
			});
		} catch (Exception ex) {
			System.err.println("Error handling image: " + ex.getMessage());
			new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).showAndWait();
		}
	
	}
}
