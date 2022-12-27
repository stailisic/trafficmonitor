package fhtw.trafficmonitor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Line;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

        @FXML
        private TableView<LineRecord> tableViewDemo;

        @FXML
        private TableColumn<LineRecord, String> transportType;

        @FXML
        private TableColumn<LineRecord, String> line;

        @FXML
        private TableColumn<LineRecord, String> stationName;

        @FXML
        private TableColumn<LineRecord, String> towards;

        @FXML
        private TableColumn<LineRecord, String> departureTimePlanned1;

        @FXML
        private TableColumn<LineRecord, String> departureTimePlanned2;

        ObservableList<LineRecord> list = FXCollections.observableArrayList(
                new LineRecord("Tram","1","Schwedenplatz U", "Stefan-Fadinger-Platz", "2022-12-19T23:53:30.000+0100","2022-12-20T00:05:30.000+0100"),
                new LineRecord("Tram","1","Schwedenplatz U","Prater Hauptallee","2022-12-20T00:02:00.000+0100",""),
                new LineRecord("Tram","2","Schwedenplatz U","Wexstraße, Betriebsbhf. Brigittenau","2022-12-20T00:15:00.000+0100","2022-12-20T00:30:00.000+0100"),
                new LineRecord("Tram","2","Schwedenplatz U","Friedrich-Engels-Platz","2022-12-19T23:39:00.000+0100","2022-12-19T23:54:00.000+0100"),
                new LineRecord("Tram","2","Schwedenplatz U","Dornbach", "2022-12-20T00:00:07.000+0100", "2022-12-20T00:24:12.000+0100")
        );



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        transportType.setCellValueFactory(new PropertyValueFactory<LineRecord, String>("lineTransportType"));
        line.setCellValueFactory(new PropertyValueFactory<LineRecord, String>("lineName"));
        stationName.setCellValueFactory(new PropertyValueFactory<LineRecord, String>("lineStationName"));
        towards.setCellValueFactory(new PropertyValueFactory<LineRecord, String>("lineTowards"));
        departureTimePlanned1.setCellValueFactory(new PropertyValueFactory<LineRecord, String>("lineDepartureTimePlanned1"));
        departureTimePlanned2.setCellValueFactory(new PropertyValueFactory<LineRecord, String>("lineDepartureTimePlanned2"));

        tableViewDemo.setItems(list);


    }
}
