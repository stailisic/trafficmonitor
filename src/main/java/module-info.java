module fhtw.trafficmonitor {
    requires javafx.controls;
    requires javafx.fxml;


    opens fhtw.trafficmonitor to javafx.fxml;
    exports fhtw.trafficmonitor;
}