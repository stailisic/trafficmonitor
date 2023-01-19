package fhtw.trafficmonitor;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
            String transportLine = "u1";
            //label_u1.setText("Button " + transportLine.toUpperCase() + " has been pressed.");
            openTrafficMonitorWindow(transportLine, btn_u1, label_u1);
        });

        btn_u2.setOnAction(e->{
            String transportLineName = "u2";
            openTrafficMonitorWindow(transportLineName, btn_u2, label_u2);
        });

        btn_u3.setOnAction(e->{
            String transportLineName = "u3";
            openTrafficMonitorWindow(transportLineName, btn_u3, label_u3);

        });

        btn_u4.setOnAction(e->{
            String transportLineName = "u4";
            openTrafficMonitorWindow(transportLineName, btn_u4, label_u4);
        });

        btn_u6.setOnAction(e->{
            String transportLineName = "u6";
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
            stage.setScene(scene);

            btn_uX.setDisable(true);

            label_uX.setText("TrafficMonitor window " + transportLineName.toUpperCase() + " is now open!");
            stage.showAndWait();

            if (!stage.isShowing()) {
                btn_uX.setDisable(false);
                label_uX.setText("");
            }
        }
        catch (Exception exception) {
            System.out.println("TrafficMonitor window " + transportLineName.toUpperCase() + " is already open!");
        }
    }
}
