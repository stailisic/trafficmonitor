package fhtw.trafficmonitor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Class to retrieve real time data in json format, parse them and extract the essential data for display
 */
public class JsonParse implements Runnable{
    enum DebugState {
        OFF,
        ON
    }

    //private static DebugState debugMode = DebugState.OFF;   // 0=off, 1=on
    private static final DebugState debugMode = DebugState.OFF;

    private String diva;
    private String stationName;
    private String lineName; // e.g. U1, U2, U3, U4, U6
    private String transportType;
    private String url_source = "https://www.wienerlinien.at/ogd_realtime/monitor?stopid=0&diva=";
    private String jsonInput = "";
    private int threadNumber;

    // pro Haltestelle
    private List<String> listLines = new ArrayList<>();

    /**
     * Approach:
     * - collect all created LineRecords into one static list
     *   - e.g. if U1- and U4-TransportMonitor window is open and
     *     both have several stations selected, then all records
     *     are gathered in one list
     * - for displaying the respective lineRecords for each 'TransportMonitor window UX'
     *   will be done by filtering the transportLine 'UX'
     */
    private static ObservableList<LineRecord> listLinesLineRecords = FXCollections.observableArrayList();

    /**
     * key "lines" is of type JSONArray, but provides in general only one index [0] JSONObject,
     *                  * e.g. [{"name":"U1",...,"direction":"H"}] -- including the square brackets
     *                  * substring -> remove square brackets (at the beginning and end) and therefore
     *                  * add the modified { content } into the ArrayList.
     * @param json ... pass JSONObject to be added to the list
     * @param key ... key in question
     */
    public void parseObjectLines(JSONObject json, String key) {
        addListLines(json.get(key).toString().substring(1,json.get(key).toString().length() -1));
    }

    /**
     * add value to listLines
     * @param string ... String value provided by parseObjectLines
     */
    public void addListLines(String string) {
        this.listLines.add(string);
    }

    /**
     * parsing jsonInput {data: ...} and retrieve ~value~ of requested 'key'
     *  e.g.: key = lines -> value = [{name: ...}]
     * @param json provided as JSONObject and is the original GET-Request
     * @param key for the first stage, we look for key 'lines'
     *
     * Source:
     * - How to parse dynamic and nested JSON in java? - Rest assured API automation framework
     *   - https://www.youtube.com/watch?v=ZjZqLUGCWxo
     */
    public void getKey(JSONObject json, String key /* , List<String> listLines*/) {
        //Check if particular key exists -> store as boolean value
        boolean exists = json.has(key);
        Iterator<?> keys;  // Iterator use generic concept
        String nextKeys;

        if (!exists) { // not exists part
            keys = json.keys();     // store retrieved current key into iterator
            while (keys.hasNext()) {
                nextKeys = (String) keys.next();
                try {
                    //Check if the next key is either an Object or Array
                    if (json.get(nextKeys) instanceof JSONObject) {              // if nextKey is an JsonObject

                        if (exists == false) {
                            getKey(json.getJSONObject(nextKeys), key /*, listLines*/);
                        }

                    } else if (json.get(nextKeys) instanceof JSONArray) {       // if nextKey is an JsonArray
                        JSONArray jsonArray = json.getJSONArray(nextKeys);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String jsonArrayString = jsonArray.get(i).toString();
                            JSONObject innerJson = new JSONObject(jsonArrayString);

                            if (exists == false) {
                                getKey(innerJson, key /*, listLines*/);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Validation next key - ERROR.");
                }
            }
        } else { // exists part
                parseObjectLines(json, key);
        }
    }

    /**
     * Method to retrieve value on the basis of given JSONObject and key
     * @param json JSONObject passed to retrieve the necessary key
     * @param key either 'type' or 'name'
     * @return the value to of the related key as  String
     */

    public String getKeyString(JSONObject json, String key /*, List<String> listLines*/) {
        /*
          Check if particular key exists -> store as boolean value
         */
        boolean exists = json.has(key);
        Iterator<?> keys;  // Iterator use generic concept
        String nextKeys;

        if (!exists) { // not exists part
            keys = json.keys();     // store retrieved current key into iterator
            while (keys.hasNext()) {
                nextKeys = (String) keys.next();
                try {
                    /*
                      Check if the next key is either an Object or Array
                     */
                    if (json.get(nextKeys) instanceof JSONObject) {              // if nextKey is an JsonObject
                        if (exists == false) {
                            getKeyString(json.getJSONObject(nextKeys), key /*, listLines*/);
                        }

                    } else if (json.get(nextKeys) instanceof JSONArray) {       // if nextKey is an JsonArray
                        JSONArray jsonArray = json.getJSONArray(nextKeys);
                        for (int i = 0; i < jsonArray.length(); i++) {

                            String jsonArrayString = jsonArray.get(i).toString();
                            JSONObject innerJson = new JSONObject(jsonArrayString);

                            if (exists == false) {
                                getKeyString(innerJson, key /*, listLines*/);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Validation next key - ERROR.");
                }
            }
        } else { // exists part
            return json.get(key).toString();
        }
        return json.toString();
    }

    /**
     * Method to retrieve either departure countdown or departure time
     * @param json JSONObject passed to retrieve the necessary key
     * @param key either 'countdown' or 'timePlanned'
     * @return the value to of the related key as String
     */
    public String getKeyStringDepartures(JSONObject json, String key) {
        String result = "";
        JSONObject jsonObjectDepartures = new JSONObject(getKeyString(json, "departures"/*, this.listLines*/));

        if (debugMode.equals(DebugState.ON)) {
            System.out.println("DEBUG " + jsonObjectDepartures);
            System.out.println("DEBUG " + jsonObjectDepartures.get("departure").toString());
        }

        JSONArray jsonArrayDeparture = new JSONArray(jsonObjectDepartures.get("departure").toString());
        for (int i = 0; i < jsonArrayDeparture.length(); i++) {
            String jsonArrayString = jsonArrayDeparture.get(i).toString();
            JSONObject innerJson = new JSONObject(jsonArrayString);

            JSONObject innerJsonTimePlanned = new JSONObject(innerJson.get("departureTime").toString());

            if (debugMode.equals(DebugState.ON)) {
                System.out.println("DEBUG " + innerJsonTimePlanned.get(key).toString());
            }

            result += innerJsonTimePlanned.get(key).toString() + "\n";
        }
        return result;
    }

    /**
     * Constructor used for the only purpose to access static ArrayList 'listLinesLineRecords'
     */
    public JsonParse() {
    }

    /**
     * Constructor
     * @param diva ID of respective station name
     * @param stationName e.g. Schwedenplatz, Vorgartenstraße (RadioButtons)
     * @param lineName e.g. U1, U2, U3, U4, U6
     * @param transportType e.g. ptMetro
     * @param threadNumber index number passed on from the for loop
     */
    public JsonParse(String diva, String stationName, String lineName, String transportType, int threadNumber) {
        this.diva = diva;
        this.stationName = stationName;
        this.lineName = lineName;
        this.transportType = transportType;
        this.url_source = url_source + diva;
        this.threadNumber = threadNumber;
    }

    /**
     * Threading procedure
     * 1. getKeyStage0: GET-Request > save json data into String 'jsonInput'
     * 2. getKeyStage1: parse json data and retrieve necessary key/values -> save in ArrayList 'listLines'
     * 3. getKeyStage2: pass each entry of ArrayList 'listLines' as new LineRecord into static ArrayList 'listLinesLineRecords'
     */
    @Override
    public void run () {
        TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor("Workflow 'run'");
        /*
          Remove any existing LineRecords from ArrayList 'listLinesLineRecords'
          in regard of 'lineName' (=transportLine, e.g. U1,U2,U3,U4,U6)
         */
        removeLineRecordsFromList();

        getKeyStage0();
        getKeyStage1(new JSONObject(this.jsonInput), this.lineName, this.transportType);
        getKeyStage2();

        System.out.println(" *******************************************************************************");
        System.out.println(" ** Thread.current: "
                + Thread.currentThread().getId()
                + " index [" + threadNumber + "] for " + diva
                + ", " + stationName
                + ", " +  lineName
                + ", " +  transportType );
        System.out.println(" *******************************************************************************");


        TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor(" *******************************************************************************\n");
        TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor(" ** Thread.current: "
                        + Thread.currentThread().getId()
                        + " index " + threadNumber + "] for " + diva
                        + ", " + stationName
                        + ", " + lineName
                        + ", " +  transportType);
        TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor(" *******************************************************************************");
    }




    /**
     * Method used for instance:
     * - to retrieve jsonData and store it in String 'jsonInput'
     */
    public void getKeyStage0() {
        /*
          Source: https://medium.com/swlh/getting-json-data-from-a-restful-api-using-java-b327aafb3751
           - part regarding HttpURLConnection used from source above and modified for this purpose
         */
        try {
            URL url = new URL(url_source);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            // Receiving the response code
            int responseCode = conn.getResponseCode();

            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                Scanner scanner = new Scanner(url.openStream());

                // store retrieved JSON data into string using a scanner line by line
                while (scanner.hasNext()) {
                    this.jsonInput += scanner.nextLine();
                }
                //Close the scanner
                scanner.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        this.listLines = new ArrayList<>();

        System.out.println(" *******************************************************************************");
        System.out.println(" ** getKeyStage0 >> Thread.current: "
                + Thread.currentThread().getId()
                + " index [" + threadNumber + "] for " + diva
                + ", " + stationName
                + ", " +  lineName
                + ", " +  transportType );
        System.out.println(" *******************************************************************************");

        TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor(" *******************************************************************************\n");
        TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor(" ** getKeyStage0 >> Thread.current: "
                + Thread.currentThread().getId()
                + " index " + threadNumber + "] for " + diva
                + ", " + stationName
                + ", " + lineName
                + ", " +  transportType);
        TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor(" *******************************************************************************");

    }

    /**
     * Parse jsonInput of the related Haltepunkt (diva) to stage 1:
     *  1. retrieve from all 'monitors index' (A)
     *  2. the inner lines JsonArray (B)
     *  3. convert it into an JsonObject and save them into the ArrayList.
     *
     * The structure of the jsonInput is constructed as follows:
     * - data (jsonObject)
     *   - monitors (jsonArray)
     *(A)  - [0] (jsonObject)
     *       - locationStop (jsonObject)
     *   |->  - lines (jsonArray) Note: checked ona sample basis, there seems to be always only index [0]
     *   |     - [0] (jsonObject): { name, towards
     *   |       - departures (jsonObject)
     *(B)|         - departure (jsonArray)
     *   |           - [0] (jsonObject): timePlanned, countdown
     *   |           - [x] (jsonObject)
     *   |->       - type, lineId }
     *       - attributes (jsonObject)
     *(A)  - [1] (jsonObject)
     *(A)  - [2] (jsonObject)
     *(A)  - [x] (jsonObject)
     * - message (jsonObject)
     *
     * The ArrayList of type String results in having multiple records:
     *  [0]: {lines ....}
     *  [1]: {lines ....}
     *  [x]: {lines ....}
     * @param inputJsonObject ... (a) new Object in regard of Haltestelle is selected in UI
     * @param lineName        ... attribute provided by (a)
     * @param transportType   ... attribute provided by(a)
     */
    public void getKeyStage1(JSONObject inputJsonObject, String lineName, String transportType) {
        getKey(inputJsonObject, "lines"/*, this.listLines*/);

        // debug information
        if (debugMode.equals(DebugState.ON)) {
            System.out.println("----- getKeyStage1 | INNER START: display all entries of listLines ");
            this.listLines.forEach(System.out::println); // debug
            System.out.println("----- getKeyStage1 | INNER END "); // debug
        }

        /*
           Remove any entries that do not match ptMetro
         */
        for (int i=0; i < this.listLines.size(); i++) {
            if (this.listLines.get(i).contains("\"name\":\"" + lineName + "\"")
                    && this.listLines.get(i).contains("\"type\":\"" + transportType + "\"")){

                // debug information: display all entries that will remain in listLines
                if (debugMode.equals(DebugState.ON)) {
                    System.out.println("\tRemain listLines[" + i + "]"
                            + "\"name\":\"" + lineName + "\"" + "\"type\":\"" + transportType + "\"" + this.listLines.get(i)); // debug
                }

                continue;
            } else {
                // debug information: display all entries that will be removed from listLines
                if (debugMode.equals(DebugState.ON)) {
                    System.out.println("\tRemove listLines[" + i + "]"
                            + "\"name\":\"" + lineName + "\"" + "\"type\":\"" + transportType + "\"" + this.listLines.get(i)); // debug
                }

                this.listLines.remove(i);
                i = -1;
            }
        }

        System.out.println(" *******************************************************************************");
        System.out.println(" ** getKeyStage1 >> Thread.current: " + Thread.currentThread().getId() + " index [" + threadNumber + "] for " + diva
                + ", " + stationName
                + ", " +  lineName
                + ", " +  transportType );
        System.out.println(" *******************************************************************************");

        TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor(" *******************************************************************************\n");
        TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor(" ** getKeyStage1 >> Thread.current: "
                + Thread.currentThread().getId()
                + " index " + threadNumber + "] for " + diva
                + ", " + stationName
                + ", " + lineName
                + ", " +  transportType);
        TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor(" *******************************************************************************");
    }

    /**
     * From ArrayList retrieve necessary information on basis of provided attributes
     * and save them in our general static observableArrayList which is required for GUI
     */
    public void getKeyStage2() {
        for (int i=0; i < this.listLines.size(); i++) {

            JSONObject inputJsonObjectListLines = new JSONObject(this.listLines.get(i));

            listLinesLineRecords.add(
                    new LineRecord(
                            getKeyString(inputJsonObjectListLines, "type" /*, this.listLines*/),
                            this.diva,
                            getKeyString(inputJsonObjectListLines, "name" /*, this.listLines*/),
                            this.stationName,
                            getKeyString(inputJsonObjectListLines, "towards" /*, this.listLines*/),
                            getKeyStringDepartures(inputJsonObjectListLines, "countdown"),
                            getKeyStringDepartures(inputJsonObjectListLines, "timePlanned")
                    )
            );
        }

        System.out.println(" *******************************************************************************");
        System.out.println(" ** getKeyStage2 >> Thread.current: " + Thread.currentThread().getId() + " index [" + threadNumber + "] for " + diva
                + ", " + stationName
                + ", " +  lineName
                + ", " +  transportType );
        listLinesLineRecords.forEach(System.out::println);
        System.out.println(" *******************************************************************************");

        TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor(" *******************************************************************************\n");
        TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor(" ** getKeyStage2 >> Thread.current: "
                + Thread.currentThread().getId()
                + " index " + threadNumber + "] for " + diva
                + ", " + stationName
                + ", " + lineName
                + ", " +  transportType);
        TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor(" *******************************************************************************");
    }

    /**
     * Method to return String of
     * @return the diva number, which is the ID of the respective station in question
     */
    public String getDiva() {
        return diva;
    }

    /**
     * Method to set
     * @param diva the value for the respective diva attribute
     */
    public void setDiva(String diva) {
        this.diva = diva;
    }

    /**
     * Method to return String of
     * @return the respective name of transportation, e.g. U4
     */
    public String getLineName() {
        return lineName;
    }

    /**
     * Method to return
     * @return the ObservableList that contains the lineRecords
     */
    public ObservableList<LineRecord> getListLinesLineRecords() {
        return listLinesLineRecords;
    }

    /**
     * remove any LineRecords in static ArrayList listLinesLineRecords
     * in regard of given 'lineName'
     */
    public void removeLineRecordsFromList(){
        TrafficMonitorApplication.trafficMonitorLog.logTrafficMonitor(" ** Remove LineRecords that will not be used for display. LineRecords of lineName " + this.lineName + " remain.");

        for (int k = 0; k < listLinesLineRecords.size(); k++) {
            if (listLinesLineRecords.get(k).getLineName().equals(this.lineName)) {
                listLinesLineRecords.remove(k);
                k=-1;
            }
        }
    }

}
