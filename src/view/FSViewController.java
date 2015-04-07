package view;

import app.DSManager;
import app.Node;
import app.UDPDSManager;
import app.WebServiceDSManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.*;

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
    Button connectBtn, executeBtn, printBtn;
    @FXML
    TextField serverIPTF;
    @FXML
    TextField serverPortTF;
    @FXML
    ComboBox<String> nodeIPCB;
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
    ToggleGroup comRBGroup;

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
    private DSManager dsManager;
    private String lastQuery = "";
    private int queryCounter = 0;
    private boolean executing = false;
    private String csvString;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        popupCreator = new PopupCreator();
        neighbours = FXCollections.observableArrayList();

        try {
            Enumeration e1 = NetworkInterface.getNetworkInterfaces();
            ObservableList<String> ips = FXCollections.observableArrayList();
            while (e1.hasMoreElements()) {
                NetworkInterface n = (NetworkInterface) e1.nextElement();
                Enumeration e2 = n.getInetAddresses();
                while (e2.hasMoreElements()) {
                    InetAddress iNetAdd = (InetAddress) e2.nextElement();
                    if (iNetAdd instanceof Inet4Address) {
                        ips.add(iNetAdd.getHostAddress());
                    }
                }
            }
            nodeIPCB.setItems(ips);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        nodeIPCB.valueProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                serverIPTF.setText(newValue);

            }
        });
//        serverIPTF.setText("10.8.98.24");
        nodeIPCB.getSelectionModel().selectFirst();
        serverPortTF.setText("5001");
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

        executeBtn.setOnAction((event) -> {
                    queryCounter = 0;
                    executing = true;
                    csvString = "query,time to first,time to total,hops\n";
                    executeSearchQueries();

                }

        );

        printBtn.setOnAction((event) -> {

                    dsManager.printQueryValues();
                }

        );

        connectBtn.setOnAction((event) -> {
            connectBtn.setDisable(true);

            if (!connected) {
                connectBtn.setText("Connecting...");
                boolean conStatus = connectToNetwork();

                if (conStatus) {
                    serverIPTF.setDisable(true);
                    serverPortTF.setDisable(true);
                    nodeIPCB.setDisable(true);
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
                    nodeIPCB.setDisable(false);
                    nodePortTF.setDisable(false);

                    searchPane.setDisable(true);
                    filePane.setDisable(true);
                    routingPane.setDisable(true);

                    searchTF.clear();
                    fileList.getChildren().clear();
                    neighbours.clear();

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

    public void appendCsvStr(String str){
        csvString = csvString.concat(str);
    }

    public void writeToCsvFile(String fileName) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(fileName+".csv", "UTF-8");
            writer.println(csvString);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void executeSearchQueries() {

        String[] queries = new String[]{"Twilight", "Jack", "American Idol", "Happy Feet", "Twilight saga", "Happy Feet", "Happy Feet", "Feet", "Happy Feet", "Twilight", "Windows", "Happy Feet", "Mission Impossible", "Twilight", "Windows 8", "The", "Happy", "Windows 8", "Happy Feet", "Super Mario", "Jack and Jill", "Happy Feet", "Impossible", "Happy Feet", "Turn Up The Music", "Adventures of Tintin", "Twilight saga", "Happy Feet", "Super Mario", "American Pickers", "Microsoft Office 2010", "Twilight", "Modern Family", "Jack and Jill", "Jill", "Glee", "The Vampire Diarie", "King Arthur", "Jack and Jill", "King Arthur", "Windows XP", "Harry Potter", "Feet", "Kung Fu Panda", "Lady Gaga", "Gaga", "Happy Feet", "Twilight", "Hacking", "King"};

        int qLimit = queries.length;

        if (qLimit == queryCounter) {
            executing = false;
        }

        if (qLimit > queryCounter) {
            System.out.println("Sent - (" + (queryCounter + 1) + "/" + qLimit + ") " + queries[queryCounter]);
            lastQuery = queries[queryCounter++];
            dsManager.getQueryResults(lastQuery);

        }
    }

    private boolean connectToNetwork() {
        try {
            String serverIP = serverIPTF.getText();
            int serverPort = Integer.parseInt(serverPortTF.getText());
            String nodeIP = nodeIPCB.getValue();
            int nodePort = Integer.parseInt(nodePortTF.getText());

            RadioButton selectedRB = (RadioButton) comRBGroup.getSelectedToggle();

            if (selectedRB.getId().equals("udpRB")) {
                //if udp
                dsManager = new UDPDSManager(serverIP, serverPort, nodeIP, nodePort, this);
            } else {
                //if web service
                dsManager = new WebServiceDSManager(serverIP, serverPort, nodeIP, nodePort, this);
            }

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
        if (connected) {
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

    public void writeToLog(String str) {
        this.logCtrl.appendLog(str);    }

    /*
        status = 1 -> initial result
        status = 2 -> any other result
        status = 3 -> end of search
     */
    public void showSearchResults(HashMap<String, String[]> results, int status) {


        if (!executing) {
            popupCreator.showSearchResults(lastQuery, results, status);
        }


        if (status == 3) {
            searchBtn.setText("Search File");
            searchTF.setDisable(false);
            searchBtn.setDisable(false);


            if (executing) {
                executeSearchQueries();
            }
        }
//        if(results.isEmpty()){
//            popupCreator.showInfoDialog("No files found", "No files matching \'"+lastQuery+"\' found!");
//        }
//        else {
//            popupCreator.showSearchResults(lastQuery, results);
//        }

    }

    public void writeToFile(String nodeName) {
        logCtrl.writeToFile(nodeName);
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
