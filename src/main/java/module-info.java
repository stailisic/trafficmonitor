module fhtw.trafficmonitor {
    requires javafx.controls;
    requires javafx.fxml;
    //requires json;
    requires org.json;


    opens fhtw.trafficmonitor to javafx.fxml;
    exports fhtw.trafficmonitor;
}