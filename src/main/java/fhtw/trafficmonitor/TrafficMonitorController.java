package fhtw.trafficmonitor;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.layout.BorderWidths.*;

/**
 * Class used for creating the UI of the first window,
 * providing the subway lines: U1, U2, U3, U4, U6
 */
public class TrafficMonitorController implements Initializable {

    @FXML
    private Button btn_u1;

    @FXML
    private Button btn_u2;

    @FXML
    private Button btn_u3;

    @FXML
    private Button btn_u4;

    @FXML
    private Button btn_u6;

    @FXML
    private Label label_u1;

    @FXML
    private Label label_u2;

    @FXML
    private Label label_u3;

    @FXML
    private Label label_u4;

    @FXML
    private Label label_u6;

    @FXML
    private TextArea textArea_description;


    @FXML
    private TextField stationIn;


    @FXML
    void takeUserInput(MouseEvent event) {


        String userInput = stationIn.getText();
        System.out.println("--USER INPUT : "+userInput+"--");

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String textArea_description_DE = "Bedienungsanleitung:\n" +
                "Im Fenster \"TrafficMonitor Hauptmenü\" können Sie auf der rechten Seite " +
                "über die Button-Auswahl die gewünschte U-bahn-Linie auswählen. " +
                "Für jede Auswahl wird ein eigenes Fenster \"TrafficMonitor Ux\" geöffnet. " +
                "Mehrere Fenster zu einer U-bahn-Linie ist nicht gestattet. Dies wird einerseits durch " +
                "die Nichtverfügbarkeit des jeweiligen Buttons bzw. durch einen entsprechenden Hinweis neben den Button verdeutlicht." +
                "";

        textArea_description.setText(textArea_description_DE);
        textArea_description.setWrapText(true);
        textArea_description.setEditable(false);
        textArea_description.setDisable(false);

        // Dropshadow, see: https://www.tutorialspoint.com/javafx/drop_shadow_effect.htm
        DropShadow dropShadow = new DropShadow();
        dropShadow.setBlurType(BlurType.GAUSSIAN); //setting the type of blur for the shadow
        dropShadow.setColor(Color.DARKGREY); //Setting color for the shadow
        dropShadow.setHeight(2); //Setting the height of the shadow
        dropShadow.setWidth(2); //Setting the width of the shadow
        dropShadow.setRadius(2); //Setting the radius of the shadow
        dropShadow.setSpread(12); //Setting the spread of the shadow

        // apply style (button color, etc.) for each transportLine button
        btn_u1.setStyle("-fx-background-color: #EE1D23; -fx-text-fill: #FFFFFF; -fx-font-weight: BOLD");
        btn_u1.setEffect(dropShadow);
        btn_u2.setStyle("-fx-background-color: #A065AA; -fx-text-fill: #FFFFFF; -fx-font-weight: BOLD");
        btn_u2.setEffect(dropShadow);
        btn_u3.setStyle("-fx-background-color: #F58220; -fx-text-fill: #FFFFFF; -fx-font-weight: BOLD");
        btn_u3.setEffect(dropShadow);
        btn_u4.setStyle("-fx-background-color: #00A54F; -fx-text-fill: #FFFFFF; -fx-font-weight: BOLD");
        btn_u4.setEffect(dropShadow);
        btn_u6.setStyle("-fx-background-color: #986A39; -fx-text-fill: #FFFFFF; -fx-font-weight: BOLD");
        btn_u6.setEffect(dropShadow);

        /**
         * btn_uX.setOnAction:
         * When respective button is clicked
         *  -> run method to create new respective (2nd) window 'TransportMonitor UX'
         */
        btn_u1.setOnAction(e->{
            String transportLineName = "u1";
            TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor("Button " + transportLineName.toUpperCase() + " wurde angeklickt.");
            //label_u1.setText("Button " + transportLineName.toUpperCase() + " has been pressed.");
            openTrafficMonitorWindow(transportLineName, btn_u1, label_u1);
        });

        btn_u2.setOnAction(e->{
            String transportLineName = "u2";
            TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor("Button " + transportLineName.toUpperCase() + " wurde angeklickt.");
            openTrafficMonitorWindow(transportLineName, btn_u2, label_u2);

        });

        btn_u3.setOnAction(e->{
            String transportLineName = "u3";
            TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor("Button " + transportLineName.toUpperCase() + " wurde angeklickt.");
            openTrafficMonitorWindow(transportLineName, btn_u3, label_u3);
        });

        btn_u4.setOnAction(e->{
            String transportLineName = "u4";
            TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor("Button " + transportLineName.toUpperCase() + " wurde angeklickt.");
            openTrafficMonitorWindow(transportLineName, btn_u4, label_u4);
        });

        btn_u6.setOnAction(e->{
            String transportLineName = "u6";
            TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor("Button " + transportLineName.toUpperCase() + " wurde angeklickt.");
            openTrafficMonitorWindow(transportLineName, btn_u6, label_u6);
        });
    }

    /**
     * Method to create the new (2nd) window 'TransportMonitor UX' for the respective transportLine
     * @param transportLineName e.g. u1, u2, u3, u4, u6
     * @param btn_uX respective button labeled with transportLineName
     * @param label_uX provide message indicating that the button is pressed and the new (2nd) window for respective transportLine is available
     */
    private void openTrafficMonitorWindow(String transportLineName, Button btn_uX, Label label_uX)  {
        try {
            Stage stage = new Stage();
            CreatePublicTransportLine createPublicTransportLine = new CreatePublicTransportLine(transportLineName);
            Scene scene = new Scene(createPublicTransportLine.buildView(), 1024, 800);
            stage.setTitle("TrafficMonitor " + transportLineName.toUpperCase());
            Image iconmine = new Image("C:\\Users\\nagel\\Desktop\\ODE\\prj3\\src\\main\\resources\\fhtw\\trafficmonitor\\wlicon.png");

            stage.getIcons().add(iconmine);

            stage.setScene(scene);

            btn_uX.setDisable(true);
            //btn_uX.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

            label_uX.setText("TrafficMonitor window " + transportLineName.toUpperCase() + " is now open!");

            TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor("TrafficMonitor Hauptmenü " + transportLineName.toUpperCase() + " ist nun geöffnet!");
            stage.showAndWait();

            // After TrafficMonitor UX window has been closed:
            // - Button UX will be available again and
            // - the Label will be reset to empty
            if (!stage.isShowing()) {
                btn_uX.setDisable(false);
                label_uX.setText("");
                TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor("TrafficMonitor Hauptmenü " + transportLineName.toUpperCase() + " wurde geschlossen.");
            }
        }
        catch (Exception exception) {
            System.out.println("TrafficMonitor window " + transportLineName.toUpperCase() + " is already open!");
            TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor("TrafficMonitor Hauptmenü " + transportLineName.toUpperCase() + " ist bereits offen!");
        }
    }
}
