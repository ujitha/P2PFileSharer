package view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by udith on 3/6/15.
 */
public class LogWindowController implements Initializable {

    @FXML
    TextArea logTextArea;
    @FXML
    Button clearBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logTextArea.setText("");

        clearBtn.setOnAction((event) -> {
            logTextArea.setText("");
        });

        //scrolls log to the bottom
        logTextArea.textProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue,
                                Object newValue) {
                logTextArea.setScrollTop(Double.MAX_VALUE);
            }
        });
    }

    public void appendLog(String logStr) {
        logTextArea.appendText(logStr + "\n");

    }

    public void writeToFile(String fileName) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(fileName, "UTF-8");
            writer.println("------------------ " + fileName + " --------------------");
            String text = logTextArea.getText();
            writer.append(text);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
