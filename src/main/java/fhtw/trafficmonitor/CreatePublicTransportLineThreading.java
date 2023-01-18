package fhtw.trafficmonitor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import org.json.JSONObject;

import java.util.ArrayList;

public class CreatePublicTransportLineThreading  {
/*
    private ArrayList <RadioButton> transportLineButtonsList = new ArrayList<>();
    private ObservableList<LineRecord> list = FXCollections.observableArrayList();
    private String transportLine;
    private Label infoLabel;
*/
    /*


    @Override
    public void run(){
        for(int i=0;i<transportLineButtonsList.size();i++) {
            if(transportLineButtonsList.get(i).isSelected()) {

                CsvReader csvReader = new CsvReader(transportLineButtonsList.get(i).getText());
                csvReader.retrieveDiva();

                checkListForTableViewRefresh(csvReader.getDiva());

                JsonParse jsonParse = new JsonParse(csvReader.getDiva(), transportLineButtonsList.get(i).getText(), this.transportLine , "ptMetro");
                jsonParse.getKeyStage1(new JSONObject(jsonParse.getJsonInput()), this.transportLine, "ptMetro");
                jsonParse.getKeyStage2();

                for (int k = 0; k < jsonParse.getListLinesLineRecords().size(); k++) {
                    list.add(jsonParse.getListLinesLineRecords().get(k));
                }

            } else {
                System.out.println("Linie " + this.transportLine.toUpperCase() + " nicht im Betrieb.");
                infoLabel.setText("Keine Auswahl getroffen bzw. Linie " + this.transportLine.toUpperCase() + " nicht im Betrieb.");
                //System.out.println("Nothing selected.");
                //infoLabel.setText(returnInfoLabel());
            }
        }


    }

    public void checkListForTableViewRefresh(String diva){
        for (int i = 0; i < list.size();i++) {
            if (list.get(i).getDiva().equals(diva)) {
                list.remove(i);
            }
        }
    }

    public String returnInfoLabel () {
        return "Keine Auswahl getroffen bzw. Linie " + this.transportLine.toUpperCase() + " nicht im Betrieb.";
    }

     */
}
