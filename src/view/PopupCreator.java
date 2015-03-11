package view;

//import javafx.scene.control.Alert;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by udith on 3/5/15.
 */
public class PopupCreator {

    SearchResultsController srController = null;

    public void showErrorDialog(String title, String message){
        JOptionPane.showMessageDialog(null,message,title,JOptionPane.ERROR_MESSAGE);

        /*Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();*/
    }

    public void showWarningDialog(String title, String message){
        JOptionPane.showMessageDialog(null,message,title,JOptionPane.WARNING_MESSAGE);

        /*Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();*/
    }

    public void showInfoDialog(String title, String message){
        JOptionPane.showMessageDialog(null,message,title,JOptionPane.INFORMATION_MESSAGE);

        /*Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();*/
    }

    public void showSearchResults(String query, HashMap<String, String[]> results, int status){
        switch (status){
            case 1:
                AnchorPane srWindow = null;

                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(FSViewController.class.getResource("SearchResults.fxml"));
                    srWindow = fxmlLoader.load();
                    srController = fxmlLoader.getController();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                srController.addResults(query, results);

                Stage srStage = new Stage(StageStyle.UTILITY);
                Scene srScene = new Scene(srWindow);
                srStage.setTitle("Search Results");
                srStage.setScene(srScene);
                srStage.show();
                break;
            case 2:
                srController.addResults(query, results);
                break;
            case 3:
                srController.endOfSearch();
                break;
        }

    }
}
