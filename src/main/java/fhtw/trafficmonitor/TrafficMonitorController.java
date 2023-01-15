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
        //setting the type of blur for the shadow
        dropShadow.setBlurType(BlurType.GAUSSIAN);

        //Setting color for the shadow
        dropShadow.setColor(Color.DARKGREY);

        //Setting the height of the shadow
        dropShadow.setHeight(2);

        //Setting the width of the shadow
        dropShadow.setWidth(2);

        //Setting the radius of the shadow
        dropShadow.setRadius(2);

        //Setting the spread of the shadow
        dropShadow.setSpread(12);

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


        btn_u1.setOnAction(e->{
            String trafficLineName = "u1";
            label_u1.setText("Button " + trafficLineName.toUpperCase() + " has been pressed.");
        });

        btn_u2.setOnAction(e->{
            String trafficLineName = "u2";
            label_u2.setText("Button " + trafficLineName.toUpperCase() + " has been pressed.");
        });

        btn_u3.setOnAction(e->{
            String trafficLineName = "u3";
            label_u3.setText("Button " + trafficLineName.toUpperCase() + " has been pressed.");
        });


        // Prototype
        btn_u4.setOnAction(e->{
            try {
                Stage stage_u4 = new Stage();
                CreatePublicTransportLine u4 = new CreatePublicTransportLine("u4");
                Scene scene = new Scene(u4.buildView(), 1024, 800);
                stage_u4.setTitle("TrafficMonitor U4");
                stage_u4.setScene(scene);

                btn_u4.setDisable(true);
                String trafficLineName = "u4";

                label_u4.setText("TrafficMonitor window " + trafficLineName.toUpperCase() + " is now open!");
                stage_u4.showAndWait();

                if (!stage_u4.isShowing()) {
                    btn_u4.setDisable(false);
                    label_u4.setText("");
                }
            }
            catch (Exception exception) {
                String trafficLineName = "u4";
                System.out.println("TrafficMonitor window " + trafficLineName.toUpperCase() + " is already open!");
            }
        });


        btn_u6.setOnAction(e->{
            String trafficLineName = "u6";
            label_u6.setText("Button " + trafficLineName.toUpperCase() + " has been pressed.");
        });


    }
}
