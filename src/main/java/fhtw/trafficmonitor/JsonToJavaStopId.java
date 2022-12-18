package fhtw.trafficmonitor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class JsonToJavaStopId {

    public static void main (String[] args) throws IOException {

        //Reading JSON from file system
        BufferedReader br=new BufferedReader(new FileReader("src/haltepunkt_id_147.json"));
        String line;
        StringBuilder sbuilderObj = new StringBuilder();
        while((line=br.readLine()) !=null){
            sbuilderObj.append(line);
        }

        System.out.println("Original Json :: "+sbuilderObj.toString());
        //Using JSONObject
        JSONObject jsonObj = new JSONObject(sbuilderObj.toString());

        JSONObject data = jsonObj.getJSONObject("data");
        System.out.println(data.toString());
        JSONArray monitors = data.getJSONArray("monitors");
        System.out.println(monitors.toString());
        System.out.println("----------------------------------------");


        System.out.println("foreach list of monitors array: ");
        monitors.forEach((n) -> System.out.println(n));
        System.out.println("----------------------------------------");

        System.out.println(" monitor array len: " + monitors.length());
        System.out.println(monitors.getJSONObject(0).toString());

        System.out.println("----------------------------------------");

        String title = monitors.getJSONObject(0).getJSONObject("locationStop").getJSONObject("properties").getString("title");
        String towards = monitors.getJSONObject(0).getJSONArray("lines"). getJSONObject(0).getString("towards");

        JSONArray departure  = monitors.getJSONObject(0).getJSONArray("lines").getJSONObject(0).getJSONObject("departures").getJSONArray("departure");

        departure.forEach((n) -> System.out.println(n));

        for (int i = 0; i < departure.length(); i++) {
            String departureTime = departure.getJSONObject(i).getJSONObject("departureTime").getString("timePlanned");
            System.out.println("Stop: " + title + " towards " + towards + " planned departure " + departureTime);
        }













    }
}
