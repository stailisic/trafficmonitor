package fhtw.trafficmonitor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CsvReader {

    private String haltestellenName;
    private String diva;

    public CsvReader(String haltestellenName) {
        this.haltestellenName = haltestellenName;
    }

    /**
     * CSV source: https://www.wienerlinien.at/ogd_realtime/doku/ogd/wienerlinien-ogd-haltestellen.csv
     */
    public void retrieveDiva () {
        String filepath = "src/main/resources/fhtw/trafficmonitor/wienerlinien-ogd-haltestellen.csv";
        try {
            String line = "";
            BufferedReader br  =new BufferedReader(new FileReader(filepath));
            while ((line = br.readLine()) != null) {
                String [] data = line.split(";");
                //System.out.println(data[0] + "" + data [1] + "");
                if (data[1].equals(this.haltestellenName)) {
                    System.out.println(data[0]);
                    this.diva = data[0];
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getHaltestellenName() {
        return haltestellenName;
    }

    public String getDiva() {
        return diva;
    }
}
