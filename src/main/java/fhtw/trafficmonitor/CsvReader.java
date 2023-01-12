package fhtw.trafficmonitor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CsvReader {

    public void csvReader (){

        String filepath = "wienerlinien-ogd-haltestellen.csv";
        try {
            String line = "";
            BufferedReader br  =new BufferedReader(new FileReader(filepath));
            while ((line = br.readLine()) != null){
                String [] data = line.split(";");
                //System.out.println(data[0] + "" + data [1] + "");
                if (data [1].equals("Ottakring")){
                    System.out.println(data [0]);
                }

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
