package view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

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

    DummyResponse dmr;
    PopupCreator popupCreator;
    boolean connected;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dmr = new DummyResponse();
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
                try {
                    String serverIP = serverIPTF.getText();
                    int serverPort = Integer.parseInt(serverPortTF.getText());
                    String nodeIP = nodeIPTF.getText();
                    int nodePort = Integer.parseInt(nodePortTF.getText());
                    String status = dmr.connect(serverIP, serverPort, nodeIP, nodePort);
                    if (status.equals("OK")) {
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
                        connected = true;
                    }else{
                        popupCreator.showErrorDialog("Connection Failed", status);
                        connectBtn.setText("Connect");
                    }
                } catch (NumberFormatException e) {
                    popupCreator.showErrorDialog("Input Error", "Some input parameters are Invalid");
                    connectBtn.setText("Connect");
                }
            } else {
                connectBtn.setText("Disconnecting...");

                String status = dmr.disconnect();
                if (status.equals("OK")) {
                    serverIPTF.setDisable(false);
                    serverPortTF.setDisable(false);
                    nodeIPTF.setDisable(false);
                    nodePortTF.setDisable(false);

                    searchPane.setDisable(true);
                    filePane.setDisable(true);
                    routingPane.setDisable(true);

                    connectBtn.getStyleClass().remove(connectBtn.getStyleClass().size() - 1);
                    connectBtn.getStyleClass().add("green-button");
                    connectBtn.setText("Connect");
                    connected = false;
                }else{
                    popupCreator.showErrorDialog("Disconnection Failed", status);
                    connectBtn.setText("Disconnect");
                }
            }
            connectBtn.setDisable(false);

        });

        searchBtn.setOnAction((event)->{
            searchBtn.setText("Searching...");
            searchBtn.setDisable(true);
            String fileName = searchTF.getText();
            dmr.search(fileName);

            //popup to show file list

            searchBtn.setText("Search");
            searchBtn.setDisable(false);
        });
    }
}
