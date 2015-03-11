package view;

import app.DSManagerInterface;
import app.UDPDSManager;
import app.Node;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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
    @FXML
    Button logBtn;

    @FXML
    TableView<Node> routingTable;
    @FXML
    TableColumn<Node, String> nameColumn;
    @FXML
    TableColumn<Node, String> ipColumn;
    @FXML
    TableColumn<Node, String> portColumn;

    private PopupCreator popupCreator;
    private boolean connected;
    private Stage logStage;
    private LogWindowController logCtrl;
    private ObservableList<Node> neighbours;
    private DSManagerInterface dsManager;
    private String lastQuery = "";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        popupCreator = new PopupCreator();
        neighbours = FXCollections.observableArrayList();

        serverIPTF.setText("127.0.0.1");
        serverPortTF.setText("5001");
        nodeIPTF.setText("127.0.0.1");
        nodePortTF.setText("5100");
        connected = false;

        searchPane.setDisable(true);
        filePane.setDisable(true);
        routingPane.setDisable(true);

        nameColumn.setCellValueFactory(cellData -> cellData.getValue().userNameProperty());
        ipColumn.setCellValueFactory(cellData -> cellData.getValue().ipProperty());
        portColumn.setCellValueFactory(cellData -> cellData.getValue().portProperty());
        routingTable.setItems(neighbours);

        initLogWindow();
        setHandlers();
    }

    public void addNeighbour(Node node) {
        this.neighbours.add(node);
    }

    public void removeNeighbour(Node node) {
        this.neighbours.remove(node);
    }

    private void initLogWindow() {
        AnchorPane logWindow = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(FSViewController.class.getResource("LogWindow.fxml"));
            logWindow = fxmlLoader.load();
            logCtrl = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logStage = new Stage(StageStyle.UTILITY);
        Scene logScene = new Scene(logWindow);
        logStage.setTitle("Log Window");
        logStage.setScene(logScene);
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

                    populateFileList(dsManager.getNodeFileList());

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
            searchTF.setDisable(true);
            String fileName = searchTF.getText();
            lastQuery = fileName;
            dsManager.getQueryResults(fileName);
        });

        logBtn.setOnAction((event) -> {
            logStage.show();
        });
    }

    private boolean connectToNetwork() {
        try {
            String serverIP = serverIPTF.getText();
            int serverPort = Integer.parseInt(serverPortTF.getText());
            String nodeIP = nodeIPTF.getText();
            int nodePort = Integer.parseInt(nodePortTF.getText());

//            if ((serverIP.isEmpty()) || (nodeIP.isEmpty())) {
//                throw new Exception("Empty String");
//            }

            dsManager = new UDPDSManager(serverIP, serverPort, nodeIP, nodePort, this);

            String status = dsManager.start();
            if (status.equals("Successful")) {
                connected = true;
                return true;
            } else {
                popupCreator.showErrorDialog("Connection Failed", status);
            }
        } catch (NumberFormatException e) {
            popupCreator.showErrorDialog("Input Error", "Some port numbers are invalid");
        }
        return false;
    }

    public boolean disconnectFromNetwork() {
        if(connected) {
            dsManager.sendLeaveMessages();
            connected = false;
        }
        return true;
    }

    public void populateFileList(ArrayList<String> fileNames) {
        for (String f : fileNames) {
            Label l = new Label(f);
            l.setTooltip(new Tooltip(f));
            fileList.getChildren().add(l);
        }
    }

    public void writeToLog(String str){
        this.logCtrl.appendLog(str);
    }

    public void showSearchResults(HashMap<String, String[]> results) {
        if(results.isEmpty()){
            popupCreator.showInfoDialog("No files found", "No files matching \'"+lastQuery+"\' found!");
        }
        else {
            popupCreator.showSearchResults(lastQuery, results);
        }
        searchBtn.setText("Search File");
        searchTF.setDisable(false);
        searchBtn.setDisable(false);
    }

    private void clearInputs() {
        searchTF.clear();
        fileList.getChildren().clear();
    }
}

class SearchResult {
    private StringProperty ip;
    private StringProperty fileName;

    public SearchResult(String ip, String fileName) {
        this.ip = new SimpleStringProperty(ip);
        this.fileName = new SimpleStringProperty(fileName);
    }

    public String getIp() {
        return ip.get();
    }

    public StringProperty ipProperty() {
        return ip;
    }

    public String getFileName() {
        return fileName.get();
    }

    public StringProperty fileNameProperty() {
        return fileName;
    }
}
