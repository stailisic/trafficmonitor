package fhtw.trafficmonitor;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
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

        borderPane.setTop(anchorPane);
        return  borderPane;

    }

    private BorderPane createBorderPane() {
        BorderPane borderPane = new BorderPane();
        return borderPane;
    }

    private AnchorPane createAnchorPane() {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefWidth(1024);
        anchorPane.setPrefHeight(400);

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
                    filepath = "src/main/resources/fhtw/trafficmonitor/haltestellennamen_u1_linie.csv";
                    break;
                case "u4":
                    filepath = "src/main/resources/fhtw/trafficmonitor/haltestellennamen_u4_linie.csv";
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
