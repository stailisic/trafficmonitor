package fhtw.trafficmonitor;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
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

        this.btn_u1.setStyle("-fx-background-color: #EE1D23; -fx-text-fill: #FFFFFF; -fx-font-weight: BOLD");
        this.btn_u1.setEffect(dropShadow);
        this.btn_u2.setStyle("-fx-background-color: #A065AA; -fx-text-fill: #FFFFFF; -fx-font-weight: BOLD");
        this.btn_u2.setEffect(dropShadow);
        this.btn_u3.setStyle("-fx-background-color: #F58220; -fx-text-fill: #FFFFFF; -fx-font-weight: BOLD");
        this.btn_u3.setEffect(dropShadow);
        this.btn_u4.setStyle("-fx-background-color: #00A54F; -fx-text-fill: #FFFFFF; -fx-font-weight: BOLD");
        this.btn_u4.setEffect(dropShadow);
        this.btn_u6.setStyle("-fx-background-color: #986A39; -fx-text-fill: #FFFFFF; -fx-font-weight: BOLD");
        this.btn_u6.setEffect(dropShadow);


        // Prototype
        this.btn_u4.setOnAction(e->{

            // grey out button, so user cannot press it again !!!
            // TODO: if new window is closed, button should be available again
            btn_u4.setDisable(true);

            CreatePublicTransportLine u4 = new CreatePublicTransportLine("u4");

            Stage stage = new Stage();
            Scene scene = new Scene(u4.buildView(), 1024, 800);
            stage.setTitle("TrafficMonitor U4");
            stage.setScene(scene);
            stage.show();

        });

    }
}
