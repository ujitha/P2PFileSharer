package view;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * Created by udith on 3/5/15.
 */
public class PopupCreator {

    private Alert searching;

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

    public void showSearchingDialog(String query){
        searching = new Alert(Alert.AlertType.NONE);
        searching.setTitle("Searching...");
        searching.setContentText("Searching for : "+query);
        searching.show();
    }

    public void closeSeachingDialog(){
        searching.hide();
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
        }else{
            TableColumn<SearchResult, String> fileColumn = new TableColumn<>("File Name");
            TableColumn<SearchResult, String> ipColumn = new TableColumn<>("IP Address");
            TableView<SearchResult> resultTable = new TableView<>();


        }

        dialog.showAndWait();
    }
}
