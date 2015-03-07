package view;
/**
 * Created by udith on 3/4/15.
 */

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.io.IOException;

public class FileSharer extends Application {

    public static String JAVA_VERSION;

    public static void main(String[] args) {
        FileSharer.JAVA_VERSION = System.getProperty("java.version");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Parent root = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("FSView.fxml"));
            root = fxmlLoader.load();
            final FSViewController fsCtrl = fxmlLoader.getController();
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    boolean b = fsCtrl.disconnectFromNetwork();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        primaryStage.setTitle("P2P File Sharer");
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.getIcons().add(new Image(FileSharer.class.getResourceAsStream("images/icon.png")));
        primaryStage.setScene(scene);



        primaryStage.show();
        }
    }
