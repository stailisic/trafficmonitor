package fhtw.trafficmonitor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.JSONObject;

public class HelloApplication extends Application {

    /*
    adding a comment
    I am on dev branch right now
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("demo.fxml"));
        stage.setTitle("Demonstration TableView with defined Columns");
        stage.setScene(new Scene(root));
        stage.show();

    }

    /*
    public void start(Stage stage) throws IOException {
       FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }*/




    public static void main(String[] args) {
        // modify value to enable/disable debugMode
        int debugMode = 0; // 0=off, 1=on

        if (debugMode == 1) {
            /**
             * demo purpose regarding:
             * 1. send URL request on basis of known 'diva'
             * 2. receive json data
             * 3. parse received json data
             *
             * Note: in class JsonParse you can set debugMode to ON/OFF either to display more information
             */

            JsonParse jsonSource1 = new JsonParse("60201198", "Schwedenplatz", "U1", "ptMetro");
            System.out.println(jsonSource1.getUrl_source());
            System.out.println(jsonSource1.getJsonInput());
            jsonSource1.getKeyStage1(new JSONObject(jsonSource1.getJsonInput()), "U1", "ptMetro");

            //System.out.println("Retrieve only selected transportType");
            //jsonSource1.getListLines().forEach(System.out::println);
            //System.out.println("Retrieve records: ");
            //jsonSource1.getKeyStage2();
            //System.out.println(jsonSource1.getListLinesLineRecords().get(0).getLineName());
            //jsonSource1.createController(jsonSource1.getListLinesLineRecords());
        } else {
            // launch the GUI
            launch(args);
        }
    }
}