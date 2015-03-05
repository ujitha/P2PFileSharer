package view;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * Created by udith on 3/5/15.
 */
public class PopupCreator {

    public void showErrorDialog(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showWarningDialog(String title, String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showInfoDialog(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showSearchResults(String query, String[] results){
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Search Results");
        dialog.setHeaderText("Search Results for : "+query);
        dialog.setGraphic(new ImageView(new Image(FileSharer.class.getResourceAsStream("images/search.png"))));

        if(results.length==0){
            Label noFiles = new Label("No matching files found!");
            noFiles.setTextFill(Color.RED);
            dialog.getDialogPane().setContent(noFiles);
        }

        dialog.showAndWait();
    }
}
