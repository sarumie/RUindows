package main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;

public class Utils {
    /**
     * Checks if a file is a text file based on its extension
     * @param file The file to check
     * @return true if the file is a text file, false otherwise
     */
    public static boolean isTextFile(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".txt");
    }
    
    /**
     * Checks if a file is an image file based on its extension
     * @param file The file to check
     * @return true if the file is an image, false otherwise
     */
    public static boolean isImageFile(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".jpg") || name.endsWith(".jpeg") 
                || name.endsWith(".png") || name.endsWith(".gif")
                || name.endsWith(".bmp") || name.endsWith(".webp");
    }
    
    /**
     * Recursively clears all children from a parent node
     * @param parent The parent node to clear
     */
    public static void deepClearChildren(Parent parent) {
        if (parent == null) return;
        
        // Get a copy of the children list to avoid ConcurrentModificationException
        List<Node> childrenCopy = new ArrayList<>(parent.getChildrenUnmodifiable());
        
        for (Node child : childrenCopy) {
            // If this child is also a parent, clear its children first
            if (child instanceof Parent) {
                deepClearChildren((Parent) child);
            }
            
            // Remove the child from its parent
            if (parent instanceof Pane) {
                ((Pane) parent).getChildren().remove(child);
            } else if (parent instanceof Group) {
                ((Group) parent).getChildren().remove(child);
            }
        }
    }
    
    /**
     * Displays an alert dialog with a given title and message
     * @param title The title of the alert
     * @param message The message to display
     */
    public static void showAlert(String title, String message) {
	    Alert alert = new Alert(Alert.AlertType.ERROR);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.showAndWait();
	}
}
