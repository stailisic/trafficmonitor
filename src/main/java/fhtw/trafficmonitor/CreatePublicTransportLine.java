package fhtw.trafficmonitor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CreatePublicTransportLine {

    private String transportLine;
    private static int haltestelle_layout_y_pos_start = 100;
    private static int haltestelle_layout_x_pos_start = 20;
    private static int haltestelle_layout_x_abstand = 30;
    private ArrayList <RadioButton> transportLineButtonsList = new ArrayList<>();

    ObservableList<LineRecord> list = FXCollections.observableArrayList();


    public CreatePublicTransportLine(String transportLine) {
        this.transportLine = transportLine.toUpperCase();

        add_transportLineButtons(transportLine, this.transportLineButtonsList);

        System.out.println("Size of button list " + this.transportLineButtonsList.size());
    }


    public Parent buildView () {
     //   this.transportLine = transportLine;
        BorderPane borderPane = createBorderPane();
        AnchorPane anchorPane = createAnchorPane();
        TableView tableView = createTableView();
        Label infoLabel = createLabel();
        ButtonBar buttonBar = createButtonBar();
        Button btn_refresh = createButton();
        btn_refresh.setText("Aktualisieren");

        buttonBar.getButtons().add(btn_refresh);

        borderPane.setTop(anchorPane);
        borderPane.setCenter(tableView);
        borderPane.setBottom(buttonBar);
        anchorPane.getChildren().add(infoLabel);

        btn_refresh.setOnAction(e->{
            System.out.println("Aktualisieren geklickt");

            for(int i=0;i<transportLineButtonsList.size();i++) {
                if(transportLineButtonsList.get(i).isSelected()) {
                    CsvReader csvReader = new CsvReader(transportLineButtonsList.get(i).getText());
                    csvReader.retrieveDiva();

                    checkListForTableViewRefresh(csvReader.getDiva());

                    JsonParse jsonParse = new JsonParse(csvReader.getDiva(), transportLineButtonsList.get(i).getText(), this.transportLine , "ptMetro");
                    jsonParse.getKeyStage1(new JSONObject(jsonParse.getJsonInput()), this.transportLine, "ptMetro");
                    jsonParse.getKeyStage2();

                    for (int k = 0; k < jsonParse.getListLinesLineRecords().size(); k++) {
                        list.add(jsonParse.getListLinesLineRecords().get(k));
                    }

                } else {
                    System.out.println("Linie " + this.transportLine.toUpperCase() + " nicht im Betrieb.");
                    infoLabel.setText("Keine Auswahl getroffen bzw. Linie " + this.transportLine.toUpperCase() + " nicht im Betrieb.");
                    //System.out.println("Nothing selected.");
                }
            }

            tableView.refresh();

        });

        return  borderPane;

    }

    private ButtonBar createButtonBar() {
        ButtonBar buttonBar = new ButtonBar();
        buttonBar.setPadding(new Insets(10,10,10,0));
        return buttonBar;
    }

    private Button createButton(){
        Button button = new Button();
        return button;
    }

    private TableView createTableView(){
        TableView tableView = new TableView();

        TableColumn transportType = new TableColumn<>("Typ");
        transportType.setPrefWidth(75);
        transportType.setCellValueFactory(new PropertyValueFactory<LineRecord, String>("lineTransportType"));
        TableColumn line = new TableColumn<>("Linie");
        line.setPrefWidth(75);
        line.setCellValueFactory(new PropertyValueFactory<LineRecord, String>("lineName"));
        TableColumn stationName = new TableColumn<>("Stationsname");
        stationName.setPrefWidth(200);
        stationName.setCellValueFactory(new PropertyValueFactory<LineRecord, String>("lineStationName"));
        TableColumn towards = new TableColumn("Richtung");
        towards.setPrefWidth(200);
        towards.setCellValueFactory(new PropertyValueFactory<LineRecord, String>("lineTowards"));
        TableColumn departureTimePlanned1 = new TableColumn<>("Abfahrt Countdown in Minuten");
        departureTimePlanned1.setPrefWidth(200);
        departureTimePlanned1.setCellValueFactory(new PropertyValueFactory<LineRecord, String>("lineDepartureTimePlanned1"));
        TableColumn departureTimePlanned2 = new TableColumn<>("Abfahrtszeiten");
        departureTimePlanned2.setPrefWidth(200);
        departureTimePlanned2.setCellValueFactory(new PropertyValueFactory<LineRecord, String>("lineDepartureTimePlanned2"));

        tableView.getColumns().add(transportType);
        tableView.getColumns().add(line);
        tableView.getColumns().add(stationName);
        tableView.getColumns().add(towards);
        tableView.getColumns().add(departureTimePlanned1);
        tableView.getColumns().add(departureTimePlanned2);

        tableView.setItems(list);

        return tableView;
    }

    private BorderPane createBorderPane() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefWidth(1024);
        borderPane.setPrefHeight(768);
        return borderPane;
    }

    private AnchorPane createAnchorPane() {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefWidth(200);
        anchorPane.setPrefHeight(200);

        for (int i=0; i < this.transportLineButtonsList.size();i++) {
            anchorPane.getChildren().add(this.transportLineButtonsList.get(i));
        }
        return anchorPane;
    }

    private Label createLabel() {
        Label label = new Label();
        label.setText("");
        label.setLayoutX(30);
        label.setLayoutX(50);
        label.setPadding(new Insets(10,0,10,0));
        return label;
    }

    public RadioButton createButton(String buttonName, int y, int x) {
        RadioButton button = new RadioButton();
        button.setText(buttonName);
        button.setPrefWidth(200);
        button.setPrefHeight(20);
        button.setRotate(-45);
        button.setLayoutY(y);
        button.setLayoutX(x);
        return button;
    }

    public void add_transportLineButtons(String transportLine, ArrayList<RadioButton> transportLineButtonsList) {
        int y = haltestelle_layout_y_pos_start;
        int x = haltestelle_layout_x_pos_start;
        int x_abstand = haltestelle_layout_x_abstand;

        try {
            String filepath="";
            switch(transportLine) {
                case "u1":
                    filepath = "src/main/resources/fhtw/trafficmonitor/haltestellennamen_u1_linie.csv";
                    break;
                case "u2":
                    filepath = "src/main/resources/fhtw/trafficmonitor/haltestellennamen_u2_linie.csv";
                    break;
                case "u3":
                    filepath = "src/main/resources/fhtw/trafficmonitor/haltestellennamen_u3_linie.csv";
                    break;
                case "u4":
                    filepath = "src/main/resources/fhtw/trafficmonitor/haltestellennamen_u4_linie.csv";
                    break;
                case "u6":
                    filepath = "src/main/resources/fhtw/trafficmonitor/haltestellennamen_u6_linie.csv";
                    break;
                default:
                    System.out.println("Sorry no file found");
                    break;
            }

            String line = "";

            int cntLine=0;

            BufferedReader br = new BufferedReader(new FileReader(filepath));
            while ((line = br.readLine()) != null) {
                if (cntLine == 0) {
                    cntLine++;
                    continue;
                }
                else {
                    String[] data = line.split(";");
                    transportLineButtonsList.add(createButton(data[1], y, x));
                    System.out.println("col[0]: " + data[0] + " & col[1]: " + data[1]
                            + " x: " + x
                            + " y: " + y
                            + "\n");
                    x = x + x_abstand;
                }

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void checkListForTableViewRefresh(String diva){
        for (int i = 0; i < list.size();i++) {
            if (list.get(i).getDiva().equals(diva)) {
                list.remove(i);
            }
        }

    }


}
