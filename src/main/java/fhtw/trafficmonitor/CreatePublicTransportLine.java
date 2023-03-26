package fhtw.trafficmonitor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



/**
 * Class to create the customized 'TrafficWindow UX' window
 */
public class CreatePublicTransportLine {
    private String transportLine; // e.g.: U1, U2, U3, U4, U6

    /**
     * Layout values for the RadioButtons
     */
    private static int haltestelle_layout_y_pos_start = 100;
    private static int haltestelle_layout_x_pos_start = 20;
    private static int haltestelle_layout_x_abstand = 30;

    /**
     * ToolTip translations for the Buttons
     */
    private static String btn_refresh_TooltipText_DE = "Echtzeitdaten via GET-Request holen. Json zerlegen und relevanten Key/Values für die Anzeige abspeichern.";
    private static String btn_display_TooltipText_DE = "Resultierende Echtzeitdaten anzeigen lassen.";
    private static String btn_deleteDisplay_TooltipText_DE = "Gespeicherte Echtzeitdaten und Anzeige löschen";

    /**
     * list of station Names of respective transportLine represented as RadioButtons
     */
    private ArrayList<RadioButton> transportLineButtonsList = new ArrayList<>();

    /**
     * list of lineRecords of respective transportLine, used for tableView
     */
    private ObservableList<LineRecord> list = FXCollections.observableArrayList();

    private JsonParse jsonParseMain = new JsonParse();

    /**
     * Constructor used for instance creation in regard of provided transportLine (e.g. U1)
     *
     * @param transportLineName (e.g.: u1, u2, u3, u4, u6 - provided as small letters)
     */
    public CreatePublicTransportLine(String transportLineName) {
        this.transportLine = transportLineName.toUpperCase(); // !!! for further processing, upperCase is required

        add_transportLineButtons(this.transportLine, this.transportLineButtonsList);

        System.out.println("Size of RadioButton list " + this.transportLineButtonsList.size());

        TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor("Haltestellen aus csv in ArrayList<RadioButton> 'transportLineButtonsList' hinzugefügt");
    }

    /**
     * Method to create final TrafficMonitor UX window including functionality of respective buttons
     *
     * @return custom window representing the respective transportLine UX
     */
    public Parent buildView() {
        TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor("Erstellung des Fenster TrafficMonitor " + transportLine.toUpperCase());
        BorderPane borderPane = createBorderPane();
        AnchorPane anchorPane = createAnchorPane();
        TableView tableView = createTableView();
        Label infoLabel = createLabel();
        ButtonBar buttonBar = createButtonBar();


        Button btn_refresh = createButton();
        btn_refresh.setText("GET INFO");
        btn_refresh.setTooltip(new Tooltip(btn_refresh_TooltipText_DE));
        btn_refresh.setStyle("-fx-background-color: blue; -fx-text-fill: white;-fx-font-weight: bold;");
        ButtonBar.ButtonData buttonData = ButtonBar.ButtonData.LEFT;
        ButtonBar.setButtonData(btn_refresh, buttonData);


        Button btn_display = createButton();
        btn_display.setText("Anzeigen");
        btn_display.setTooltip(new Tooltip(btn_display_TooltipText_DE));

        Button btn_deleteDisplay = createButton();
        btn_deleteDisplay.setText("Anzeige löschen");
        btn_deleteDisplay.setPrefWidth(200);
        btn_deleteDisplay.setTooltip(new Tooltip(btn_deleteDisplay_TooltipText_DE));

        buttonBar.getButtons().add(btn_refresh);
        //buttonBar.getButtons().add(btn_display);
       //buttonBar.getButtons().add(btn_deleteDisplay);

        borderPane.setTop(anchorPane);
        borderPane.setCenter(tableView);
        borderPane.setBottom(buttonBar);
        anchorPane.getChildren().add(infoLabel);

        /*
         * Starting state of the executable bottom Buttons
         * setDisable(false): Button clickable
         * setDisable(true): Button greyed out (not clickable)
         */
        //btn_refresh.setDisable(false);
        //btn_display.setDisable(true);
        //btn_deleteDisplay.setDisable(true);

        /*
         * Button 'Aktualisieren'
         */


        btn_refresh.setOnAction(e -> {
            //System.out.println("Aktualisieren geklickt");

            TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor("Aktualisieren geklickt");

            resetInfoLabel(infoLabel);

            //jsonParseMain.removeLineRecordsFromList();


            ArrayList<RadioButton> selectedButtons = new ArrayList<>();


            for (int i = 0; i < transportLineButtonsList.size(); i++) {
                if (transportLineButtonsList.get(i).isSelected()) {
                    selectedButtons.add(transportLineButtonsList.get(i));
                }
            }
            TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor("Selektierte RadioButtons (Haltestellen) in neue ArrayList gespeichert.");

            if (selectedButtons.size() > 0) {
                TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor("Workflow beginnt mit Multithreading.");
                //btn_refresh.setDisable(true);
                btn_refresh.setText("REFRESH");
                //btn_display.setDisable(false);
                //btn_display.setText("> Anzeigen");

                ScheduledExecutorService executor = Executors.newScheduledThreadPool(selectedButtons.size());

                Runnable task = () -> {


                    if(jsonParseMain.getListLinesLineRecords().size()>0){

                        for (int k = 0; k < jsonParseMain.getListLinesLineRecords().size(); k++) {
                            if (jsonParseMain.getListLinesLineRecords().get(k).getLineName().equals(this.transportLine)) {
                                jsonParseMain.getListLinesLineRecords().remove(k);
                                k = -1;
                            }
                        }
                    }

                    list.clear();

                    for (int i = 0; i < selectedButtons.size(); i++) {
                        CsvReader csvReader = new CsvReader(selectedButtons.get(i).getText());
                        csvReader.retrieveDiva();

                        JsonParse jsonParse = new JsonParse(csvReader.getDiva(), selectedButtons.get(i).getText(), this.transportLine, "ptMetro", i);
                        Thread thread = new Thread(jsonParse);
                        thread.setName("Thread_" + jsonParse.getLineName() + "_" + jsonParse.getDiva());
                        thread.start();

                        System.out.println("thread state: " + thread.getState() + " " + thread.isAlive());
                        TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor("thread.current: "
                                + Thread.currentThread().getId() + ", "
                                + Thread.currentThread().getName()
                                + "thread state: " + thread.getState() + " " + thread.isAlive());

                    }

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }


                    tableView.getItems().removeAll();
                    list.clear();

                    System.out.println("---------------------------------------------------------");
                    System.out.println("(1) Entries of list: ");
                    list.forEach(System.out::println);
                    System.out.println("---------------------------------------------------------");

                    for (int k = 0; k < jsonParseMain.getListLinesLineRecords().size(); k++) {
                        if (jsonParseMain.getListLinesLineRecords().get(k).getLineName().equals(this.transportLine)) {
                            list.add(jsonParseMain.getListLinesLineRecords().get(k));
                        }
                    }

                    System.out.println("---------------------------------------------------------");
                    System.out.println("(2) Entries of list: ");
                    list.forEach(System.out::println);
                    System.out.println("---------------------------------------------------------");


                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    tableView.refresh();



                    System.out.println("Executing the task every 2 minutes with thread " + Thread.currentThread().getName());
                };

                executor.scheduleAtFixedRate(task, 0, 2, TimeUnit.MINUTES);


            } else {
                infoLabel.setText("Keine Haltestelle ausgewählt.");
                infoLabel.setStyle("-fx-background-color: #EE1D23; -fx-text-fill: #FFFFFF");
                TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor("Keine Haltestelle ausgewählt.");
            }


        });

        /*
         * - retrieve lineRecords from static list
         * - display only of respective transportLine 'UX'
         */
        btn_display.setOnAction(e -> {
            TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor("Anzeigen der resultierenden Ergebnisse.");
            btn_refresh.setDisable(false);
            btn_refresh.setText("> Aktualisieren");
            btn_display.setDisable(true);
            btn_display.setText("< Anzeigen >");
            btn_deleteDisplay.setDisable(false);
            btn_deleteDisplay.setText("> Anzeige löschen");

            tableView.getItems().removeAll();
            list.clear();

            System.out.println("---------------------------------------------------------");
            System.out.println("(1) Entries of list: ");
            list.forEach(System.out::println);
            System.out.println("---------------------------------------------------------");

            for (int k = 0; k < jsonParseMain.getListLinesLineRecords().size(); k++) {
                if (jsonParseMain.getListLinesLineRecords().get(k).getLineName().equals(this.transportLine)) {
                    list.add(jsonParseMain.getListLinesLineRecords().get(k));
                }
            }

            System.out.println("---------------------------------------------------------");
            System.out.println("(2) Entries of list: ");
            list.forEach(System.out::println);
            System.out.println("---------------------------------------------------------");

            tableView.refresh();
        });

        btn_deleteDisplay.setOnAction(e -> {
            TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor("Löschen der Anzeige und der betroffenen Einträge in der Datenbank.");
            btn_refresh.setDisable(false);
            btn_display.setDisable(true);

            resetInfoLabel(infoLabel);

            tableView.getItems().removeAll();
            list.clear();

            System.out.println("---------------------------------------------------------");
            System.out.println("1 Entries of list: ");
            list.forEach(System.out::println);
            System.out.println("---------------------------------------------------------");

            for (int k = 0; k < jsonParseMain.getListLinesLineRecords().size(); k++) {
                if (jsonParseMain.getListLinesLineRecords().get(k).getLineName().equals(this.transportLine)) {
                    jsonParseMain.getListLinesLineRecords().remove(k);
                    k = -1;
                }
            }

            System.out.println("---------------------------------------------------------");
            System.out.println("2 Entries of jsonParseMain.listLinesLineRecords: ");
            jsonParseMain.getListLinesLineRecords().forEach(System.out::println);
            System.out.println("---------------------------------------------------------");

            for (int i = 0; i < transportLineButtonsList.size(); i++) {
                transportLineButtonsList.get(i).setSelected(false); // Reset state of RadioButton: 'not selected'
            }

            tableView.refresh();

        });

        return borderPane;
    }



    /**
     * Method to create a ButtonBar with specific padding settings.
     *
     * @return ButtonBar with specific padding settings
     */
    private ButtonBar createButtonBar() {
        ButtonBar buttonBar = new ButtonBar();
        buttonBar.setPadding(new Insets(10, 10, 10, 0));
        return buttonBar;
    }

    /**
     * Method to create Button without any specific settings
     *
     * @return plain Button
     */
    private Button createButton() {
        return new Button();
    }

    /**
     * Method to create the general structure of the TableView, which is
     * used for displaying the lineRecords.
     *
     * @return custom TableView
     */
    private TableView createTableView() {
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

        departureTimePlanned1.setCellFactory(column -> new TableCell<LineRecord, String>() {


            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);

                setTextFill(item != null && item.contains("0") ? Color.RED : Color.BLACK);



            }
        });


        /*
        if (departureTimePlanned1.equals(1)) {
            departureTimePlanned1.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        } else
            departureTimePlanned1.setStyle("-fx-background-color: red; -fx-text-fill: white;");

         */

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

    /**
     * Method to create an area of type BorderPane with specific settings
     *
     * @return BorderPane with specific size setting
     */
    private BorderPane createBorderPane() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefWidth(1024);
        borderPane.setPrefHeight(768);
        return borderPane;
    }

    /**
     * Method to create an area of type AnchorPane with specific settings,
     * in which the station names (each of type RadioButton) are included.
     *
     * @return AnchorPane with specific setting including the station names represented as RadioButtons
     */
    private AnchorPane createAnchorPane() {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefWidth(200);
        anchorPane.setPrefHeight(200);

        /*
         * add station names, represented each as RadioButton, to the AnchorPane area
         */
        for (int i = 0; i < this.transportLineButtonsList.size(); i++) {
            anchorPane.getChildren().add(this.transportLineButtonsList.get(i));
        }
        return anchorPane;
    }

    /**
     * Method to create a Label with specific settings
     * - used for the infoLabel as message indicator in general
     *
     * @return Label with specific setting
     */
    private Label createLabel() {
        Label label = new Label();
        label.setText("");
        label.setLayoutX(30);
        label.setLayoutX(50);
        label.setPadding(new Insets(10, 0, 10, 0));
        return label;
    }

    /**
     * Method to create a RadioButton with specific settings
     * - used when processing the list for creating the station names (Haltestellennamen) as type of RadioButton
     * - includes design pattern: size, rotated, custom (x,y)-location of each newly created RadioButton
     *
     * @param buttonName equals to station name (Haltestellenname)
     * @param y          is the y-value for positioning the RadioButton with (x,y)-coordinates
     * @param x          is the x-value for positioning the RadioButton with (x,y)-coordinates
     * @return RadioButton with specific setting
     */
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

    /**
     * Method to create
     * - on the basis of provided transportLine, e.g.: u1, u2, u3, u4, u6 (note: transportLine is provided in small case)
     * - the list of station names will be created by retrieving from the given *.csv and
     * - stored in the ArrayList of type RadioButton
     *
     * @param transportLine            e.g.: u1, u2, u3, u4, u6 (note: transportLine is provided in small case)
     * @param transportLineButtonsList ArrayList that contains the list of station names (as RadioButtons) of the respective transportLine
     */
    public void add_transportLineButtons(String transportLine, ArrayList<RadioButton> transportLineButtonsList) {
        int y = haltestelle_layout_y_pos_start;
        int x = haltestelle_layout_x_pos_start;
        int x_abstand = haltestelle_layout_x_abstand;

        try {
            String filepath = "";
            switch (transportLine) {
                case "U1" -> filepath = "src/main/resources/fhtw/trafficmonitor/haltestellennamen_u1_linie.csv";
                case "U2" -> filepath = "src/main/resources/fhtw/trafficmonitor/haltestellennamen_u2_linie.csv";
                case "U3" -> filepath = "src/main/resources/fhtw/trafficmonitor/haltestellennamen_u3_linie.csv";
                case "U4" -> filepath = "src/main/resources/fhtw/trafficmonitor/haltestellennamen_u4_linie.csv";
                case "U6" -> filepath = "src/main/resources/fhtw/trafficmonitor/haltestellennamen_u6_linie.csv";
                default -> System.out.println("Sorry no file found");
            }

            String line = "";

            int cntLine = 0;

            BufferedReader br = new BufferedReader(new FileReader(filepath));
            while ((line = br.readLine()) != null) {
                if (cntLine == 0) {
                    cntLine++;
                    //continue;
                } else {
                    String[] data = line.split(";");
                    transportLineButtonsList.add(createButton(data[1], y, x));
                    System.out.println("col[0]: " + data[0] + " & col[1]: " + data[1]
                            + " x: " + x
                            + " y: " + y);
                    x = x + x_abstand;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Method to remove existing lineRecords of respective transportLine.
     * - used, whenever the Button btn_refresh is executed, so only
     *   newly retrieved and lately added records from the ArrayList will be displayed
     * @param diva

    public void checkListForTableViewRefresh(String diva){
        for (int i = 0; i < list.size();i++) {
            if (list.get(i).getDiva().equals(diva)) {
                list.remove(i);
            }
        }
    }

     */

    /**
     * Method to return
     *
     * @return the ObservableList that contains the lineRecords, required for TableView display
     */
    public ObservableList<LineRecord> getList() {
        return list;
    }
/*
    public void setList(ObservableList<LineRecord> list) {
        list = list;
    }

 */

    /**
     * Simple Method to reset the InfoLabel
     *
     * @param infoLabel passed Label
     */
    public void resetInfoLabel(Label infoLabel) {
        infoLabel.setText(" ");
        infoLabel.setStyle("-fx-background-color: null; -fx-text-fill: #FFFFFF");
        TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor("InfoLabel zurückgesetzt.");
    }

}
