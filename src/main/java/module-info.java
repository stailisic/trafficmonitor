module fhtw.trafficmonitor {
    requires javafx.controls;
    requires javafx.fxml;
    requires json;


    opens fhtw.trafficmonitor to javafx.fxml;
    exports fhtw.trafficmonitor;
}