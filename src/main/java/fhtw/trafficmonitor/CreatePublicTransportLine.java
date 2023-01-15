package fhtw.trafficmonitor;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CreatePublicTransportLine {

    private String transportLine = "u4";
    private static int haltestelle_layout_y_pos_start = 100;
    private static int haltestelle_layout_x_pos_start = 20;
    private static int haltestelle_layout_x_abstand = 30;
    private ArrayList <RadioButton> transportLineButtonsList = new ArrayList<>();


    public CreatePublicTransportLine(String transportLine) {
        this.transportLine = transportLine;

        add_transportLineButtons(transportLine, this.transportLineButtonsList);

        System.out.println("Size of button list " + this.transportLineButtonsList.size());
    }


    public Parent buildView () {
     //   this.transportLine = transportLine;

        BorderPane borderPane = createBorderPane();
        AnchorPane anchorPane = createAnchorPane();
        TableView tableView = createTableView();
        ButtonBar buttonBar = createButtonBar();
        Button btn_refresh = createButton();
        btn_refresh.setText("Aktualisieren");

        buttonBar.getButtons().add(btn_refresh);


        borderPane.setTop(anchorPane);
        borderPane.setCenter(tableView);
        borderPane.setBottom(buttonBar);

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

        TableColumn type = new TableColumn<>("Typ");
        type.setPrefWidth(75);
        TableColumn line = new TableColumn<>("Linie");
        line.setPrefWidth(75);
        TableColumn stationName = new TableColumn<>("Stationsname");
        stationName.setPrefWidth(200);
        TableColumn towards = new TableColumn("Richtung");
        towards.setPrefWidth(200);
        TableColumn departCountdown = new TableColumn<>("Abfahrt Countdown in Minuten");
        departCountdown.setPrefWidth(200);
        TableColumn departTime = new TableColumn<>("Abfahrtszeiten");
        departTime.setPrefWidth(200);

        tableView.getColumns().add(type);
        tableView.getColumns().add(line);
        tableView.getColumns().add(stationName);
        tableView.getColumns().add(towards);
        tableView.getColumns().add(departCountdown);
        tableView.getColumns().add(departTime);

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
                    filepath = "src/main/resources/fhtw/trafficmonitor/haltestellennamen_u1_linie.csv"; // TODO: create, retrieve names from wienerlinien-ogd-haltestellen.csv
                    break;
                case "u2":
                    filepath = "src/main/resources/fhtw/trafficmonitor/haltestellennamen_u2_linie.csv"; // TODO: create, retrieve names from wienerlinien-ogd-haltestellen.csv
                    break;
                case "u3":
                    filepath = "src/main/resources/fhtw/trafficmonitor/haltestellennamen_u3_linie.csv"; // TODO: create, retrieve names from wienerlinien-ogd-haltestellen.csv
                    break;
                case "u4":
                    filepath = "src/main/resources/fhtw/trafficmonitor/haltestellennamen_u4_linie.csv"; // TODO: Check names with wienerlinien-ogd-haltestellen.csv
                    break;
                case "u6":
                    filepath = "src/main/resources/fhtw/trafficmonitor/haltestellennamen_u6_linie.csv"; // TODO: create, retrieve names from wienerlinien-ogd-haltestellen.csv
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




}
