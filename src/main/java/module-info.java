/**
 * That's the module descriptor and contains
 * any data needed to build and use our new module.
 */


module fhtw.trafficmonitor {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    //requires json;
    requires org.json;
    requires java.logging;


    opens fhtw.trafficmonitor to javafx.fxml;
    exports fhtw.trafficmonitor;
}