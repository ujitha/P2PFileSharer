package view;

import app.Node;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Created by udith on 3/6/15.
 */
public class SearchResultsController implements Initializable {

    @FXML
    Label queryLabel;
    @FXML
    TableView<SearchResult> searchTable;
    @FXML
    TableColumn<SearchResult, String> fileColumn;
    @FXML
    TableColumn<SearchResult, String> ipColumn;

    ObservableList<SearchResult> results;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        results = FXCollections.observableArrayList();

        fileColumn.setCellValueFactory(cellData -> cellData.getValue().fileNameProperty());
        ipColumn.setCellValueFactory(cellData -> cellData.getValue().ipProperty());
        searchTable.setItems(results);
    }

    public void addResults(String q, HashMap<String,String[]> results){

        queryLabel.setText(q);
        Set<String> ips = results.keySet();

        for (String k : ips) {
            String[] res = results.get(k);
            for (String s : res) {
                SearchResult sr = new SearchResult(k,s);
                this.results.add(sr);
            }
        }
    }
}
