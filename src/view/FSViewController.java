package view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by udith on 3/4/15.
 */
public class FSViewController implements Initializable {
    @FXML
    AnchorPane searchPane;
    @FXML
    AnchorPane filePane;
    @FXML
    AnchorPane routingPane;
    @FXML
    Button connectBtn;
    @FXML
    TextField serverIPTF;
    @FXML
    TextField serverPortTF;
    @FXML
    TextField nodeIPTF;
    @FXML
    TextField nodePortTF;
    @FXML
    TextField searchTF;
    @FXML
    Button searchBtn;
    @FXML
    VBox fileList;

    DummyResponse dmr;
    PopupCreator popupCreator;
    boolean connected;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dmr = new DummyResponse(this);
        popupCreator = new PopupCreator();

        serverIPTF.setText("127.0.0.1");
        serverPortTF.setText("5001");
        nodeIPTF.setText("127.0.0.1");
        nodePortTF.setText("5100");
        connected = false;

        searchPane.setDisable(true);
        filePane.setDisable(true);
        routingPane.setDisable(true);

        setHandlers();
    }

    private void setHandlers() {
        connectBtn.setOnAction((event) -> {
            connectBtn.setDisable(true);

            if (!connected) {
                connectBtn.setText("Connecting...");
                boolean conStatus = connectToNetwork();

                if (conStatus) {
                    serverIPTF.setDisable(true);
                    serverPortTF.setDisable(true);
                    nodeIPTF.setDisable(true);
                    nodePortTF.setDisable(true);

                    searchPane.setDisable(false);
                    filePane.setDisable(false);
                    routingPane.setDisable(false);

                    connectBtn.getStyleClass().remove(connectBtn.getStyleClass().size() - 1);
                    connectBtn.getStyleClass().add("red-button");
                    connectBtn.setText("Disconnect");
                } else {
                    connectBtn.setText("Connect");
                }
            } else {
                connectBtn.setText("Disconnecting...");

                boolean conStatus = disconnectFromNetwork();
                if (conStatus) {
                    serverIPTF.setDisable(false);
                    serverPortTF.setDisable(false);
                    nodeIPTF.setDisable(false);
                    nodePortTF.setDisable(false);

                    searchPane.setDisable(true);
                    filePane.setDisable(true);
                    routingPane.setDisable(true);

                    clearInputs();

                    connectBtn.getStyleClass().remove(connectBtn.getStyleClass().size() - 1);
                    connectBtn.getStyleClass().add("green-button");
                    connectBtn.setText("Connect");
                } else {
                    connectBtn.setText("Disconnect");
                }
            }
            connectBtn.setDisable(false);
        });

        searchBtn.setOnAction((event) -> {
            searchBtn.setText("Searching...");
            searchBtn.setDisable(true);
            String fileName = searchTF.getText();
            dmr.search(fileName);

            //popup to show file list
            popupCreator.showSearchResults(fileName, new String[0]);

            searchBtn.setText("Search File");
            searchBtn.setDisable(false);
        });
    }

    private boolean connectToNetwork() {
        try {
            String serverIP = serverIPTF.getText();
            int serverPort = Integer.parseInt(serverPortTF.getText());
            String nodeIP = nodeIPTF.getText();
            int nodePort = Integer.parseInt(nodePortTF.getText());

            if ((serverIP.isEmpty()) || (nodeIP.isEmpty())) {
                throw new Exception("Empty String");
            }

            String status = dmr.connect(serverIP, serverPort, nodeIP, nodePort);
            if (status.equals("OK")) {
                connected = true;
                return true;
            } else {
                popupCreator.showErrorDialog("Connection Failed", status);
            }
        } catch (NumberFormatException e) {
            popupCreator.showErrorDialog("Input Error", "Some port numbers are invalid");
        } catch (Exception e) {
            popupCreator.showErrorDialog("Input Error", "Some IP addresses are empty");
        }
        return false;
    }

    private boolean disconnectFromNetwork(){
        String status = dmr.disconnect();
        if (status.equals("OK")) {
            connected = false;
            return true;
        } else {
            popupCreator.showErrorDialog("Disconnection Failed", status);
            return false;
        }
    }

    public void populateFileList(String[] fileNames) {
        for (String f : fileNames) {
            Label l = new Label(f);
            l.setTooltip(new Tooltip(f));
            fileList.getChildren().add(l);
        }
    }

    private void clearInputs() {
        searchTF.clear();
        fileList.getChildren().clear();
    }
}
