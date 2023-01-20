module fhtw.trafficmonitor {
    requires javafx.controls;
    requires javafx.fxml;
    requires json;
    requires java.logging;


    opens fhtw.trafficmonitor to javafx.fxml;
    exports fhtw.trafficmonitor;
}