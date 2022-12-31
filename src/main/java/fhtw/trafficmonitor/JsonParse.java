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

public class JsonParse {

    enum Stage {
        STAGE_LINES,
        STAGE_LINES_INNER
    }

    private String diva;
    private String url_source = "https://www.wienerlinien.at/ogd_realtime/monitor?stopid=0&diva=";
    private String jsonInput;

    private List<String> listLines = new ArrayList<>();
    private ObservableList<LineRecord> listLinesLineRecords = FXCollections.observableArrayList();

    public void parseObject(JSONObject json, String key) {
        // System.out.println(json.has(key));
        System.out.println(json.get(key));
        //return json.get(key).toString();
    }


    // ToDo: Delete or leave it (if so, implement it in getKey in exist-part)
    public  void parseObjectLines(JSONObject json, String key) {
        addListLines(json.get(key).toString().substring(1,json.get(key).toString().length() -1));
    }

    // ToDo: depending on method parseObjectLines
    public void addListLines(String string) {
        this.listLines.add(string);
    }


    public void getKey(JSONObject json, String key, Enum stage, List<String> listLines) {

        /**
         * Check if particular key exists -> store as boolean value
         */
        boolean exists = json.has(key);
        Iterator<?> keys;  // Iterator use generic concept
        String nextKeys;

        if (!exists) { // not exists part
            keys = json.keys();     // store retrieved current key into iterator
            while (keys.hasNext()) {
                nextKeys = (String) keys.next();
                try {
                    /**
                     * Check if the next key is either an Object or Array
                     */
                    if (json.get(nextKeys) instanceof JSONObject) {              // if nextKey is an JsonObject

                        if (exists == false) {
                            getKey(json.getJSONObject(nextKeys), key, stage, listLines);
                        }

                    } else if (json.get(nextKeys) instanceof JSONArray) {       // if nextKey is an JsonArray
                        JSONArray jsonArray = json.getJSONArray(nextKeys);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String jsonArrayString = jsonArray.get(i).toString();
                            JSONObject innerJson = new JSONObject(jsonArrayString);

                            if (exists == false) {
                                getKey(innerJson, key, stage, listLines);
                            }
                        }

                    }
                } catch (Exception e) {
                    // Todo: handle exception
                }
            }

        } else { // exists part
            if (JsonParse.Stage.STAGE_LINES.equals(stage)) {
                /**
                 * key "lines" is of type JSONArray, but provides in general only one index [0] JSONObject,
                 * e.g. [{"name":"U1",...,"direction":"H"}] <-- including the square brackets
                 * substring -> remove square brackets and add { content } into the ArrayList.
                 *
                 * ToDo: Decide if you want to use method parseObjectLines or the nested command.
                 */
                //parseObjectLines(json, key);
                listLines.add(json.get(key).toString().substring(1,json.get(key).toString().length() -1));
            }
            else if (stage.equals(JsonParse.Stage.STAGE_LINES_INNER)) {
                //json.get(key).toString();
                parseObject(json, key);
            }
            parseObject(json, key);
        }

    }


    public String getKeyString(JSONObject json, String key, List<String> listLines) {

        /**
         * Check if particular key exists -> store as boolean value
         */
        boolean exists = json.has(key);
        Iterator<?> keys;  // Iterator use generic concept
        String nextKeys;

        if (!exists) { // not exists part
            keys = json.keys();     // store retrieved current key into iterator
            while (keys.hasNext()) {
                nextKeys = (String) keys.next();
                try {
                    /**
                     * Check if the next key is either an Object or Array
                     */
                    if (json.get(nextKeys) instanceof JSONObject) {              // if nextKey is an JsonObject
                        if (exists == false) {
                            getKeyString(json.getJSONObject(nextKeys), key, listLines);
                        }

                    } else if (json.get(nextKeys) instanceof JSONArray) {       // if nextKey is an JsonArray
                        JSONArray jsonArray = json.getJSONArray(nextKeys);
                        for (int i = 0; i < jsonArray.length(); i++) {

                            String jsonArrayString = jsonArray.get(i).toString();
                            JSONObject innerJson = new JSONObject(jsonArrayString);

                            if (exists == false) {
                                getKeyString(innerJson, key, listLines);
                            }
                        }

                    }
                } catch (Exception e) {
                    // Todo: handle exception
                }
            }

        } else { // exists part

            return json.get(key).toString();

        }

        return json.toString();
    }

    public String getKeyStringDepartures(JSONObject json, String key) {
        String result = "";
        JSONObject jsonObjectDepartures = new JSONObject(getKeyString(json, key, this.listLines));
        //System.out.println("DEBUG " + jsonObjectDepartures.toString());

        //System.out.println("DEBUG " + jsonObjectDepartures.get("departure").toString());
        JSONArray jsonArrayDeparture = new JSONArray(jsonObjectDepartures.get("departure").toString());
        for (int i = 0; i < jsonArrayDeparture.length(); i++) {

            String jsonArrayString = jsonArrayDeparture.get(i).toString();
            JSONObject innerJson = new JSONObject(jsonArrayString);

            JSONObject innerJsonTimePlanned = new JSONObject(innerJson.get("departureTime").toString());

            //System.out.println("DEBUG " + innerJsonTimePlanned.get("timePlanned").toString());

            result += innerJsonTimePlanned.get("timePlanned").toString() + "\n";
        }

        return result;
    }

    public JsonParse(String diva) {
        this.diva = diva;
        this.url_source = url_source + diva;

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

                //Write all the JSON data into a string using a scanner
                while (scanner.hasNext()) {
                    this.jsonInput += scanner.nextLine();
                }

                //Close the scanner
                scanner.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This constructor uses static JSON source for testing/development reasons.
     * Remark: Any updates should be applied to respective constructor above that retrieves JSON source on demand
     * @param diva
     * @param lineName
     * @param transportType
     */
    public JsonParse(String diva, String lineName, String transportType) {
        this.diva = diva;
        System.out.println("Demo: " + getDiva() + " " + lineName + " " + transportType);
        this.jsonInput ="{\n" +
                "\t\"data\": {\n" +
                "\t\t\"monitors\": [\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"locationStop\": {\n" +
                "\t\t\t\t\t\"type\": \"Feature\",\n" +
                "\t\t\t\t\t\"geometry\": {\n" +
                "\t\t\t\t\t\t\"type\": \"Point\",\n" +
                "\t\t\t\t\t\t\"coordinates\": [\n" +
                "\t\t\t\t\t\t\t16.3775813,\n" +
                "\t\t\t\t\t\t\t48.2117466\n" +
                "\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"properties\": {\n" +
                "\t\t\t\t\t\t\"name\": \"60201198\",\n" +
                "\t\t\t\t\t\t\"title\": \"Schwedenplatz U\",\n" +
                "\t\t\t\t\t\t\"municipality\": \"Wien\",\n" +
                "\t\t\t\t\t\t\"municipalityId\": 90001,\n" +
                "\t\t\t\t\t\t\"type\": \"stop\",\n" +
                "\t\t\t\t\t\t\"coordName\": \"WGS84\",\n" +
                "\t\t\t\t\t\t\"gate\": \"B\",\n" +
                "\t\t\t\t\t\t\"attributes\": {\n" +
                "\t\t\t\t\t\t\t\"rbl\": 42\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"lines\": [\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"name\": \"1\",\n" +
                "\t\t\t\t\t\t\"towards\": \"Stefan-Fadinger-Platz\",\n" +
                "\t\t\t\t\t\t\"direction\": \"H\",\n" +
                "\t\t\t\t\t\t\"platform\": \"1\",\n" +
                "\t\t\t\t\t\t\"richtungsId\": \"1\",\n" +
                "\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\"departures\": {\n" +
                "\t\t\t\t\t\t\t\"departure\": [\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-19T23:53:30.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timeReal\": \"2022-12-19T23:53:22.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 1\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:05:30.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timeReal\": \"2022-12-20T00:05:30.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 13\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:17:30.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timeReal\": \"2022-12-20T00:17:30.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 25\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:27:30.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timeReal\": \"2022-12-20T00:27:30.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 35\n" +
                "\t\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t\t\"vehicle\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"name\": \"1\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"towards\": \"Siccardsburggasse, Betriebsbahnhof Favoriten\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"direction\": \"H\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"richtungsId\": \"1\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"type\": \"ptTram\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"attributes\": {},\n" +
                "\t\t\t\t\t\t\t\t\t\t\"linienId\": 101\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\"type\": \"ptTram\",\n" +
                "\t\t\t\t\t\t\"lineId\": 101\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"attributes\": {}\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"locationStop\": {\n" +
                "\t\t\t\t\t\"type\": \"Feature\",\n" +
                "\t\t\t\t\t\"geometry\": {\n" +
                "\t\t\t\t\t\t\"type\": \"Point\",\n" +
                "\t\t\t\t\t\t\"coordinates\": [\n" +
                "\t\t\t\t\t\t\t16.3780233,\n" +
                "\t\t\t\t\t\t\t48.2115755\n" +
                "\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"properties\": {\n" +
                "\t\t\t\t\t\t\"name\": \"60201198\",\n" +
                "\t\t\t\t\t\t\"title\": \"Schwedenplatz U\",\n" +
                "\t\t\t\t\t\t\"municipality\": \"Wien\",\n" +
                "\t\t\t\t\t\t\"municipalityId\": 90001,\n" +
                "\t\t\t\t\t\t\"type\": \"stop\",\n" +
                "\t\t\t\t\t\t\"coordName\": \"WGS84\",\n" +
                "\t\t\t\t\t\t\"gate\": \"D\",\n" +
                "\t\t\t\t\t\t\"attributes\": {\n" +
                "\t\t\t\t\t\t\t\"rbl\": 22\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"lines\": [\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"name\": \"1\",\n" +
                "\t\t\t\t\t\t\"towards\": \"Prater Hauptallee\",\n" +
                "\t\t\t\t\t\t\"direction\": \"R\",\n" +
                "\t\t\t\t\t\t\"platform\": \"2\",\n" +
                "\t\t\t\t\t\t\"richtungsId\": \"2\",\n" +
                "\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\"departures\": {\n" +
                "\t\t\t\t\t\t\t\"departure\": [\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:02:00.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timeReal\": \"2022-12-20T00:00:22.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 8\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\"type\": \"ptTram\",\n" +
                "\t\t\t\t\t\t\"lineId\": 101\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"attributes\": {}\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"locationStop\": {\n" +
                "\t\t\t\t\t\"type\": \"Feature\",\n" +
                "\t\t\t\t\t\"geometry\": {\n" +
                "\t\t\t\t\t\t\"type\": \"Point\",\n" +
                "\t\t\t\t\t\t\"coordinates\": [\n" +
                "\t\t\t\t\t\t\t16.3780472,\n" +
                "\t\t\t\t\t\t\t48.2116211\n" +
                "\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"properties\": {\n" +
                "\t\t\t\t\t\t\"name\": \"60201198\",\n" +
                "\t\t\t\t\t\t\"title\": \"Schwedenplatz U\",\n" +
                "\t\t\t\t\t\t\"municipality\": \"Wien\",\n" +
                "\t\t\t\t\t\t\"municipalityId\": 90001,\n" +
                "\t\t\t\t\t\t\"type\": \"stop\",\n" +
                "\t\t\t\t\t\t\"coordName\": \"WGS84\",\n" +
                "\t\t\t\t\t\t\"gate\": \"C\",\n" +
                "\t\t\t\t\t\t\"attributes\": {\n" +
                "\t\t\t\t\t\t\t\"rbl\": 1000\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"lines\": [\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"name\": \"1\",\n" +
                "\t\t\t\t\t\t\"towards\": \"Wexstraße, Betriebsbhf. Brigittenau\",\n" +
                "\t\t\t\t\t\t\"direction\": \"R\",\n" +
                "\t\t\t\t\t\t\"platform\": \"2\",\n" +
                "\t\t\t\t\t\t\"richtungsId\": \"2\",\n" +
                "\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\"departures\": {\n" +
                "\t\t\t\t\t\t\t\"departure\": [\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:15:00.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timeReal\": \"2022-12-20T00:14:55.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 23\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:30:00.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timeReal\": \"2022-12-20T00:30:00.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 38\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\"type\": \"ptTram\",\n" +
                "\t\t\t\t\t\t\"lineId\": 101\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"attributes\": {}\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"locationStop\": {\n" +
                "\t\t\t\t\t\"type\": \"Feature\",\n" +
                "\t\t\t\t\t\"geometry\": {\n" +
                "\t\t\t\t\t\t\"type\": \"Point\",\n" +
                "\t\t\t\t\t\t\"coordinates\": [\n" +
                "\t\t\t\t\t\t\t16.3776072,\n" +
                "\t\t\t\t\t\t\t48.2117930\n" +
                "\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"properties\": {\n" +
                "\t\t\t\t\t\t\"name\": \"60201198\",\n" +
                "\t\t\t\t\t\t\"title\": \"Schwedenplatz U\",\n" +
                "\t\t\t\t\t\t\"municipality\": \"Wien\",\n" +
                "\t\t\t\t\t\t\"municipalityId\": 90001,\n" +
                "\t\t\t\t\t\t\"type\": \"stop\",\n" +
                "\t\t\t\t\t\t\"coordName\": \"WGS84\",\n" +
                "\t\t\t\t\t\t\"gate\": \"A\",\n" +
                "\t\t\t\t\t\t\"attributes\": {\n" +
                "\t\t\t\t\t\t\t\"rbl\": 134\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"lines\": [\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"name\": \"2\",\n" +
                "\t\t\t\t\t\t\"towards\": \"Friedrich-Engels-Platz\",\n" +
                "\t\t\t\t\t\t\"direction\": \"H\",\n" +
                "\t\t\t\t\t\t\"platform\": \"1\",\n" +
                "\t\t\t\t\t\t\"richtungsId\": \"1\",\n" +
                "\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\"departures\": {\n" +
                "\t\t\t\t\t\t\t\"departure\": [\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-19T23:39:00.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timeReal\": \"2022-12-19T23:53:42.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 2\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-19T23:54:00.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timeReal\": \"2022-12-19T23:56:21.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 4\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:09:00.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timeReal\": \"2022-12-20T00:08:21.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 16\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:28:00.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timeReal\": \"2022-12-20T00:28:00.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 36\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\"type\": \"ptTram\",\n" +
                "\t\t\t\t\t\t\"lineId\": 102\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"attributes\": {}\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"locationStop\": {\n" +
                "\t\t\t\t\t\"type\": \"Feature\",\n" +
                "\t\t\t\t\t\"geometry\": {\n" +
                "\t\t\t\t\t\t\"type\": \"Point\",\n" +
                "\t\t\t\t\t\t\"coordinates\": [\n" +
                "\t\t\t\t\t\t\t16.3780233,\n" +
                "\t\t\t\t\t\t\t48.2115755\n" +
                "\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"properties\": {\n" +
                "\t\t\t\t\t\t\"name\": \"60201198\",\n" +
                "\t\t\t\t\t\t\"title\": \"Schwedenplatz U\",\n" +
                "\t\t\t\t\t\t\"municipality\": \"Wien\",\n" +
                "\t\t\t\t\t\t\"municipalityId\": 90001,\n" +
                "\t\t\t\t\t\t\"type\": \"stop\",\n" +
                "\t\t\t\t\t\t\"coordName\": \"WGS84\",\n" +
                "\t\t\t\t\t\t\"gate\": \"D\",\n" +
                "\t\t\t\t\t\t\"attributes\": {\n" +
                "\t\t\t\t\t\t\t\"rbl\": 22\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"lines\": [\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"name\": \"2\",\n" +
                "\t\t\t\t\t\t\"towards\": \"Dornbach\",\n" +
                "\t\t\t\t\t\t\"direction\": \"R\",\n" +
                "\t\t\t\t\t\t\"platform\": \"2\",\n" +
                "\t\t\t\t\t\t\"richtungsId\": \"2\",\n" +
                "\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\"departures\": {\n" +
                "\t\t\t\t\t\t\t\"departure\": [\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:00:00.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timeReal\": \"2022-12-20T00:00:07.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 8\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:13:00.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timeReal\": \"2022-12-20T00:24:12.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 32\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\"type\": \"ptTram\",\n" +
                "\t\t\t\t\t\t\"lineId\": 102\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"attributes\": {}\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"locationStop\": {\n" +
                "\t\t\t\t\t\"type\": \"Feature\",\n" +
                "\t\t\t\t\t\"geometry\": {\n" +
                "\t\t\t\t\t\t\"type\": \"Point\",\n" +
                "\t\t\t\t\t\t\"coordinates\": [\n" +
                "\t\t\t\t\t\t\t16.3783375,\n" +
                "\t\t\t\t\t\t\t48.2116644\n" +
                "\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"properties\": {\n" +
                "\t\t\t\t\t\t\"name\": \"60201198\",\n" +
                "\t\t\t\t\t\t\"title\": \"Schwedenplatz U\",\n" +
                "\t\t\t\t\t\t\"municipality\": \"Wien\",\n" +
                "\t\t\t\t\t\t\"municipalityId\": 90001,\n" +
                "\t\t\t\t\t\t\"type\": \"stop\",\n" +
                "\t\t\t\t\t\t\"coordName\": \"WGS84\",\n" +
                "\t\t\t\t\t\t\"gate\": \"F\",\n" +
                "\t\t\t\t\t\t\"attributes\": {\n" +
                "\t\t\t\t\t\t\t\"rbl\": 5501\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"lines\": [\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"name\": \"N25\",\n" +
                "\t\t\t\t\t\t\"towards\": \"Kai, Ring\",\n" +
                "\t\t\t\t\t\t\"direction\": \"R\",\n" +
                "\t\t\t\t\t\t\"platform\": \"2\",\n" +
                "\t\t\t\t\t\t\"richtungsId\": \"2\",\n" +
                "\t\t\t\t\t\t\"barrierFree\": false,\n" +
                "\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\"departures\": {\n" +
                "\t\t\t\t\t\t\t\"departure\": [\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:54:00.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 62\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\"type\": \"ptBusNight\",\n" +
                "\t\t\t\t\t\t\"lineId\": 525\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"attributes\": {}\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"locationStop\": {\n" +
                "\t\t\t\t\t\"type\": \"Feature\",\n" +
                "\t\t\t\t\t\"geometry\": {\n" +
                "\t\t\t\t\t\t\"type\": \"Point\",\n" +
                "\t\t\t\t\t\t\"coordinates\": [\n" +
                "\t\t\t\t\t\t\t16.3775813,\n" +
                "\t\t\t\t\t\t\t48.2117466\n" +
                "\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"properties\": {\n" +
                "\t\t\t\t\t\t\"name\": \"60201198\",\n" +
                "\t\t\t\t\t\t\"title\": \"Schwedenplatz U\",\n" +
                "\t\t\t\t\t\t\"municipality\": \"Wien\",\n" +
                "\t\t\t\t\t\t\"municipalityId\": 90001,\n" +
                "\t\t\t\t\t\t\"type\": \"stop\",\n" +
                "\t\t\t\t\t\t\"coordName\": \"WGS84\",\n" +
                "\t\t\t\t\t\t\"gate\": \"B\",\n" +
                "\t\t\t\t\t\t\"attributes\": {\n" +
                "\t\t\t\t\t\t\t\"rbl\": 42\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"lines\": [\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"name\": \"N29\",\n" +
                "\t\t\t\t\t\t\"towards\": \"Floridsdorf U\",\n" +
                "\t\t\t\t\t\t\"direction\": \"H\",\n" +
                "\t\t\t\t\t\t\"platform\": \"1\",\n" +
                "\t\t\t\t\t\t\"richtungsId\": \"1\",\n" +
                "\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\"departures\": {\n" +
                "\t\t\t\t\t\t\t\"departure\": [\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:42:00.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timeReal\": \"2022-12-20T00:42:00.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 50\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\"type\": \"ptBusNight\",\n" +
                "\t\t\t\t\t\t\"lineId\": 529\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"attributes\": {}\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"locationStop\": {\n" +
                "\t\t\t\t\t\"type\": \"Feature\",\n" +
                "\t\t\t\t\t\"geometry\": {\n" +
                "\t\t\t\t\t\t\"type\": \"Point\",\n" +
                "\t\t\t\t\t\t\"coordinates\": [\n" +
                "\t\t\t\t\t\t\t16.3771338,\n" +
                "\t\t\t\t\t\t\t48.2119633\n" +
                "\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"properties\": {\n" +
                "\t\t\t\t\t\t\"name\": \"60201198\",\n" +
                "\t\t\t\t\t\t\"title\": \"Schwedenplatz U\",\n" +
                "\t\t\t\t\t\t\"municipality\": \"Wien\",\n" +
                "\t\t\t\t\t\t\"municipalityId\": 90001,\n" +
                "\t\t\t\t\t\t\"type\": \"stop\",\n" +
                "\t\t\t\t\t\t\"coordName\": \"WGS84\",\n" +
                "\t\t\t\t\t\t\"gate\": \"F\",\n" +
                "\t\t\t\t\t\t\"attributes\": {\n" +
                "\t\t\t\t\t\t\t\"rbl\": 5507\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"lines\": [\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"name\": \"N29\",\n" +
                "\t\t\t\t\t\t\"towards\": \"Wittelsbachstraße\",\n" +
                "\t\t\t\t\t\t\"direction\": \"R\",\n" +
                "\t\t\t\t\t\t\"platform\": \"2\",\n" +
                "\t\t\t\t\t\t\"richtungsId\": \"2\",\n" +
                "\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\"departures\": {\n" +
                "\t\t\t\t\t\t\t\"departure\": [\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:25:00.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timeReal\": \"2022-12-20T00:25:00.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 33\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:55:00.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timeReal\": \"2022-12-20T00:55:00.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 63\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\"type\": \"ptBusNight\",\n" +
                "\t\t\t\t\t\t\"lineId\": 529\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"attributes\": {}\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"locationStop\": {\n" +
                "\t\t\t\t\t\"type\": \"Feature\",\n" +
                "\t\t\t\t\t\"geometry\": {\n" +
                "\t\t\t\t\t\t\"type\": \"Point\",\n" +
                "\t\t\t\t\t\t\"coordinates\": [\n" +
                "\t\t\t\t\t\t\t16.3779333,\n" +
                "\t\t\t\t\t\t\t48.2117591\n" +
                "\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"properties\": {\n" +
                "\t\t\t\t\t\t\"name\": \"60201198\",\n" +
                "\t\t\t\t\t\t\"title\": \"Schwedenplatz\",\n" +
                "\t\t\t\t\t\t\"municipality\": \"Wien\",\n" +
                "\t\t\t\t\t\t\"municipalityId\": 90001,\n" +
                "\t\t\t\t\t\t\"type\": \"stop\",\n" +
                "\t\t\t\t\t\t\"coordName\": \"WGS84\",\n" +
                "\t\t\t\t\t\t\"gate\": \"F\",\n" +
                "\t\t\t\t\t\t\"attributes\": {\n" +
                "\t\t\t\t\t\t\t\"rbl\": 5503\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"lines\": [\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"name\": \"N66\",\n" +
                "\t\t\t\t\t\t\"towards\": \"Liesing S\",\n" +
                "\t\t\t\t\t\t\"direction\": \"H\",\n" +
                "\t\t\t\t\t\t\"platform\": \"1\",\n" +
                "\t\t\t\t\t\t\"richtungsId\": \"1\",\n" +
                "\t\t\t\t\t\t\"barrierFree\": false,\n" +
                "\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\"departures\": {\n" +
                "\t\t\t\t\t\t\t\"departure\": [\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:54:00.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 62\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\"type\": \"ptBusNight\",\n" +
                "\t\t\t\t\t\t\"lineId\": 566\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"attributes\": {}\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"locationStop\": {\n" +
                "\t\t\t\t\t\"type\": \"Feature\",\n" +
                "\t\t\t\t\t\"geometry\": {\n" +
                "\t\t\t\t\t\t\"type\": \"Point\",\n" +
                "\t\t\t\t\t\t\"coordinates\": [\n" +
                "\t\t\t\t\t\t\t16.3780930,\n" +
                "\t\t\t\t\t\t\t48.2118811\n" +
                "\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"properties\": {\n" +
                "\t\t\t\t\t\t\"name\": \"60201198\",\n" +
                "\t\t\t\t\t\t\"title\": \"Schwedenplatz\",\n" +
                "\t\t\t\t\t\t\"municipality\": \"Wien\",\n" +
                "\t\t\t\t\t\t\"municipalityId\": 90001,\n" +
                "\t\t\t\t\t\t\"type\": \"stop\",\n" +
                "\t\t\t\t\t\t\"coordName\": \"WGS84\",\n" +
                "\t\t\t\t\t\t\"gate\": \"1\",\n" +
                "\t\t\t\t\t\t\"attributes\": {\n" +
                "\t\t\t\t\t\t\t\"rbl\": 4113\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"lines\": [\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"name\": \"U1\",\n" +
                "\t\t\t\t\t\t\"towards\": \"LEOPOLDAU    \",\n" +
                "\t\t\t\t\t\t\"direction\": \"H\",\n" +
                "\t\t\t\t\t\t\"platform\": \"1\",\n" +
                "\t\t\t\t\t\t\"richtungsId\": \"1\",\n" +
                "\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\"departures\": {\n" +
                "\t\t\t\t\t\t\t\"departure\": [\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-19T23:51:21.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timeReal\": \"2022-12-19T23:51:21.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 0\n" +
                "\t\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t\t\"vehicle\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"name\": \"U1\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"towards\": \"LEOPOLDAU    \",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"direction\": \"H\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"richtungsId\": \"1\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"foldingRamp\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"foldingRampType\": \"part\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"type\": \"ptMetro\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"attributes\": {},\n" +
                "\t\t\t\t\t\t\t\t\t\t\"linienId\": 301\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-19T23:59:21.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timeReal\": \"2022-12-19T23:59:21.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 8\n" +
                "\t\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t\t\"vehicle\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"name\": \"U1\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"towards\": \"LEOPOLDAU    \",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"direction\": \"H\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"richtungsId\": \"1\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"foldingRamp\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"foldingRampType\": \"part\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"type\": \"ptMetro\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"attributes\": {},\n" +
                "\t\t\t\t\t\t\t\t\t\t\"linienId\": 301\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:06:30.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 14\n" +
                "\t\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t\t\"vehicle\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"name\": \"U1\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"towards\": \"Leopoldau\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"direction\": \"H\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"richtungsId\": \"1\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"type\": \"ptMetro\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"attributes\": {},\n" +
                "\t\t\t\t\t\t\t\t\t\t\"linienId\": 301\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:14:00.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 22\n" +
                "\t\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t\t\"vehicle\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"name\": \"U1\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"towards\": \"Leopoldau\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"direction\": \"H\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"richtungsId\": \"1\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"type\": \"ptMetro\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"attributes\": {},\n" +
                "\t\t\t\t\t\t\t\t\t\t\"linienId\": 301\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:21:30.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 29\n" +
                "\t\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t\t\"vehicle\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"name\": \"U1\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"towards\": \"Leopoldau\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"direction\": \"H\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"richtungsId\": \"1\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"type\": \"ptMetro\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"attributes\": {},\n" +
                "\t\t\t\t\t\t\t\t\t\t\"linienId\": 301\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:33:00.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 41\n" +
                "\t\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t\t\"vehicle\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"name\": \"U1\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"towards\": \"Leopoldau\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"direction\": \"H\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"richtungsId\": \"1\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"type\": \"ptMetro\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"attributes\": {},\n" +
                "\t\t\t\t\t\t\t\t\t\t\"linienId\": 301\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\"type\": \"ptMetro\",\n" +
                "\t\t\t\t\t\t\"lineId\": 301\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"attributes\": {}\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"locationStop\": {\n" +
                "\t\t\t\t\t\"type\": \"Feature\",\n" +
                "\t\t\t\t\t\"geometry\": {\n" +
                "\t\t\t\t\t\t\"type\": \"Point\",\n" +
                "\t\t\t\t\t\t\"coordinates\": [\n" +
                "\t\t\t\t\t\t\t16.3780750,\n" +
                "\t\t\t\t\t\t\t48.2119288\n" +
                "\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"properties\": {\n" +
                "\t\t\t\t\t\t\"name\": \"60201198\",\n" +
                "\t\t\t\t\t\t\"title\": \"Schwedenplatz\",\n" +
                "\t\t\t\t\t\t\"municipality\": \"Wien\",\n" +
                "\t\t\t\t\t\t\"municipalityId\": 90001,\n" +
                "\t\t\t\t\t\t\"type\": \"stop\",\n" +
                "\t\t\t\t\t\t\"coordName\": \"WGS84\",\n" +
                "\t\t\t\t\t\t\"gate\": \"2\",\n" +
                "\t\t\t\t\t\t\"attributes\": {\n" +
                "\t\t\t\t\t\t\t\"rbl\": 4116\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"lines\": [\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"name\": \"U1\",\n" +
                "\t\t\t\t\t\t\"towards\": \"OBERLAA      \",\n" +
                "\t\t\t\t\t\t\"direction\": \"R\",\n" +
                "\t\t\t\t\t\t\"platform\": \"2\",\n" +
                "\t\t\t\t\t\t\"richtungsId\": \"2\",\n" +
                "\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\"departures\": {\n" +
                "\t\t\t\t\t\t\t\"departure\": [\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-19T23:52:21.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timeReal\": \"2022-12-19T23:52:21.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 1\n" +
                "\t\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t\t\"vehicle\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"name\": \"U1\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"towards\": \"OBERLAA      \",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"direction\": \"R\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"richtungsId\": \"2\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"foldingRamp\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"foldingRampType\": \"part\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"type\": \"ptMetro\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"attributes\": {},\n" +
                "\t\t\t\t\t\t\t\t\t\t\"linienId\": 301\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:00:21.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timeReal\": \"2022-12-20T00:00:21.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 9\n" +
                "\t\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t\t\"vehicle\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"name\": \"U1\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"towards\": \"OBERLAA      \",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"direction\": \"R\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"richtungsId\": \"2\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"foldingRamp\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"foldingRampType\": \"part\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"type\": \"ptMetro\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"attributes\": {},\n" +
                "\t\t\t\t\t\t\t\t\t\t\"linienId\": 301\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:07:00.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 15\n" +
                "\t\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t\t\"vehicle\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"name\": \"U1\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"towards\": \"Oberlaa\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"direction\": \"R\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"richtungsId\": \"2\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"type\": \"ptMetro\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"attributes\": {},\n" +
                "\t\t\t\t\t\t\t\t\t\t\"linienId\": 301\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:14:30.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 22\n" +
                "\t\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t\t\"vehicle\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"name\": \"U1\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"towards\": \"Oberlaa\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"direction\": \"R\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"richtungsId\": \"2\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"type\": \"ptMetro\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"attributes\": {},\n" +
                "\t\t\t\t\t\t\t\t\t\t\"linienId\": 301\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:23:00.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 31\n" +
                "\t\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t\t\"vehicle\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"name\": \"U1\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"towards\": \"Karlsplatz\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"direction\": \"R\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"richtungsId\": \"2\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"type\": \"ptMetro\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"attributes\": {},\n" +
                "\t\t\t\t\t\t\t\t\t\t\"linienId\": 301\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\"type\": \"ptMetro\",\n" +
                "\t\t\t\t\t\t\"lineId\": 301\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"attributes\": {}\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"locationStop\": {\n" +
                "\t\t\t\t\t\"type\": \"Feature\",\n" +
                "\t\t\t\t\t\"geometry\": {\n" +
                "\t\t\t\t\t\t\"type\": \"Point\",\n" +
                "\t\t\t\t\t\t\"coordinates\": [\n" +
                "\t\t\t\t\t\t\t16.3781380,\n" +
                "\t\t\t\t\t\t\t48.2118750\n" +
                "\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"properties\": {\n" +
                "\t\t\t\t\t\t\"name\": \"60201198\",\n" +
                "\t\t\t\t\t\t\"title\": \"Schwedenplatz\",\n" +
                "\t\t\t\t\t\t\"municipality\": \"Wien\",\n" +
                "\t\t\t\t\t\t\"municipalityId\": 90001,\n" +
                "\t\t\t\t\t\t\"type\": \"stop\",\n" +
                "\t\t\t\t\t\t\"coordName\": \"WGS84\",\n" +
                "\t\t\t\t\t\t\"gate\": \"1\",\n" +
                "\t\t\t\t\t\t\"attributes\": {\n" +
                "\t\t\t\t\t\t\t\"rbl\": 4427\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"lines\": [\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"name\": \"U4\",\n" +
                "\t\t\t\t\t\t\"towards\": \"HEILIGENSTADT\",\n" +
                "\t\t\t\t\t\t\"direction\": \"H\",\n" +
                "\t\t\t\t\t\t\"platform\": \"1\",\n" +
                "\t\t\t\t\t\t\"richtungsId\": \"1\",\n" +
                "\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\"departures\": {\n" +
                "\t\t\t\t\t\t\t\"departure\": [\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-19T23:51:21.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timeReal\": \"2022-12-19T23:51:21.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 0\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-19T23:55:21.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timeReal\": \"2022-12-19T23:55:21.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 4\n" +
                "\t\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t\t\"vehicle\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"name\": \"U4\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"towards\": \"HEILIGENSTADT\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"direction\": \"H\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"richtungsId\": \"1\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"foldingRamp\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"foldingRampType\": \"part\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"type\": \"ptMetro\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"attributes\": {},\n" +
                "\t\t\t\t\t\t\t\t\t\t\"linienId\": 304\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:03:30.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 11\n" +
                "\t\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t\t\"vehicle\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"name\": \"U4\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"towards\": \"Heiligenstadt\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"direction\": \"H\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"richtungsId\": \"1\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"type\": \"ptMetro\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"attributes\": {},\n" +
                "\t\t\t\t\t\t\t\t\t\t\"linienId\": 304\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:11:00.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 19\n" +
                "\t\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t\t\"vehicle\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"name\": \"U4\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"towards\": \"HS Gl.5   - Heiligenstadt\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"direction\": \"H\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"richtungsId\": \"1\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"type\": \"ptMetro\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"attributes\": {},\n" +
                "\t\t\t\t\t\t\t\t\t\t\"linienId\": 304\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:18:30.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 26\n" +
                "\t\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t\t\"vehicle\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"name\": \"U4\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"towards\": \"HS Gl.5   - Heiligenstadt\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"direction\": \"H\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"richtungsId\": \"1\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"type\": \"ptMetro\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"attributes\": {},\n" +
                "\t\t\t\t\t\t\t\t\t\t\"linienId\": 304\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:25:30.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 33\n" +
                "\t\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t\t\"vehicle\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"name\": \"U4\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"towards\": \"HS Gl.5   - Heiligenstadt\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"direction\": \"H\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"richtungsId\": \"1\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"type\": \"ptMetro\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"attributes\": {},\n" +
                "\t\t\t\t\t\t\t\t\t\t\"linienId\": 304\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:34:30.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 42\n" +
                "\t\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t\t\"vehicle\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"name\": \"U4\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"towards\": \"HS Gl.5   - Heiligenstadt\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"direction\": \"H\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"richtungsId\": \"1\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"type\": \"ptMetro\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"attributes\": {},\n" +
                "\t\t\t\t\t\t\t\t\t\t\"linienId\": 304\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\"type\": \"ptMetro\",\n" +
                "\t\t\t\t\t\t\"lineId\": 304\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"attributes\": {}\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"locationStop\": {\n" +
                "\t\t\t\t\t\"type\": \"Feature\",\n" +
                "\t\t\t\t\t\"geometry\": {\n" +
                "\t\t\t\t\t\t\"type\": \"Point\",\n" +
                "\t\t\t\t\t\t\"coordinates\": [\n" +
                "\t\t\t\t\t\t\t16.3781111,\n" +
                "\t\t\t\t\t\t\t48.2118269\n" +
                "\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"properties\": {\n" +
                "\t\t\t\t\t\t\"name\": \"60201198\",\n" +
                "\t\t\t\t\t\t\"title\": \"Schwedenplatz\",\n" +
                "\t\t\t\t\t\t\"municipality\": \"Wien\",\n" +
                "\t\t\t\t\t\t\"municipalityId\": 90001,\n" +
                "\t\t\t\t\t\t\"type\": \"stop\",\n" +
                "\t\t\t\t\t\t\"coordName\": \"WGS84\",\n" +
                "\t\t\t\t\t\t\"gate\": \"2\",\n" +
                "\t\t\t\t\t\t\"attributes\": {\n" +
                "\t\t\t\t\t\t\t\"rbl\": 4410\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"lines\": [\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"name\": \"U4\",\n" +
                "\t\t\t\t\t\t\"towards\": \"HÜTTELDORF   \",\n" +
                "\t\t\t\t\t\t\"direction\": \"R\",\n" +
                "\t\t\t\t\t\t\"platform\": \"2\",\n" +
                "\t\t\t\t\t\t\"richtungsId\": \"2\",\n" +
                "\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\"departures\": {\n" +
                "\t\t\t\t\t\t\t\"departure\": [\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-19T23:51:21.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timeReal\": \"2022-12-19T23:51:21.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 0\n" +
                "\t\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t\t\"vehicle\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"name\": \"U4\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"towards\": \"HÜTTELDORF   \",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"direction\": \"R\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"richtungsId\": \"2\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"foldingRamp\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"foldingRampType\": \"part\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"type\": \"ptMetro\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"attributes\": {},\n" +
                "\t\t\t\t\t\t\t\t\t\t\"linienId\": 304\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:02:21.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timeReal\": \"2022-12-20T00:02:21.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 11\n" +
                "\t\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t\t\"vehicle\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"name\": \"U4\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"towards\": \"HÜTTELDORF   \",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"direction\": \"R\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"richtungsId\": \"2\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"foldingRamp\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"foldingRampType\": \"part\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"type\": \"ptMetro\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"attributes\": {},\n" +
                "\t\t\t\t\t\t\t\t\t\t\"linienId\": 304\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:08:00.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 16\n" +
                "\t\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t\t\"vehicle\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"name\": \"U4\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"towards\": \"Hütteldorf S U\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"direction\": \"R\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"richtungsId\": \"2\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"type\": \"ptMetro\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"attributes\": {},\n" +
                "\t\t\t\t\t\t\t\t\t\t\"linienId\": 304\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:15:30.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 23\n" +
                "\t\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t\t\"vehicle\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"name\": \"U4\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"towards\": \"Hütteldorf S U\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"direction\": \"R\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"richtungsId\": \"2\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"type\": \"ptMetro\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"attributes\": {},\n" +
                "\t\t\t\t\t\t\t\t\t\t\"linienId\": 304\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\t\t\"departureTime\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"timePlanned\": \"2022-12-20T00:22:30.000+0100\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"countdown\": 30\n" +
                "\t\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t\t\"vehicle\": {\n" +
                "\t\t\t\t\t\t\t\t\t\t\"name\": \"U4\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"towards\": \"Karlsplatz\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"direction\": \"R\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"richtungsId\": \"2\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"barrierFree\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"realtimeSupported\": true,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"trafficjam\": false,\n" +
                "\t\t\t\t\t\t\t\t\t\t\"type\": \"ptMetro\",\n" +
                "\t\t\t\t\t\t\t\t\t\t\"attributes\": {},\n" +
                "\t\t\t\t\t\t\t\t\t\t\"linienId\": 304\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\"type\": \"ptMetro\",\n" +
                "\t\t\t\t\t\t\"lineId\": 304\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"attributes\": {}\n" +
                "\t\t\t}\n" +
                "\t\t]\n" +
                "\t},\n" +
                "\t\"message\": {\n" +
                "\t\t\"value\": \"OK\",\n" +
                "\t\t\"messageCode\": 1,\n" +
                "\t\t\"serverTime\": \"2022-12-19T23:51:32.000+0100\"\n" +
                "\t}\n" +
                "}";

                //JSONObject inputJsonObject = new JSONObject(this.jsonInput);
                this.listLines = new ArrayList<>();
                this.listLinesLineRecords = FXCollections.observableArrayList();
    }

    /**
     * retrieve all lines Object (related Haltepunkte) and save them into the ArrayList.
     */
    public void getKeyStage1(JSONObject inputJsonObject, String lineName, String transportType) {
        getKey(inputJsonObject, "lines", JsonParse.Stage.STAGE_LINES, this.listLines);

        //System.out.println("----- INNER START ");   // debug
        //this.listLines.forEach(System.out::println); // debug
        //System.out.println("----- INNER END "); // debug

        for (int i=0; i < this.listLines.size(); i++) {
            if (this.listLines.get(i).contains("\"name\":\"" + lineName + "\"") && this.listLines.get(i).contains("\"type\":\"" + transportType + "\"")){
                //System.out.println("Remain listLines[" + i + "]" + "\"name\":\"" + lineName + "\"" + "\"type\":\"" + transportType + "\"" + this.listLines.get(i)); // debug
                continue;
            } else {
                //System.out.println("Remove listLines[" + i + "]" + "\"name\":\"" + lineName + "\"" + "\"type\":\"" + transportType + "\"" + this.listLines.get(i)); // debug
                this.listLines.remove(i);
                i = -1;
            }
        }

    }

    /**
     * From ArrayList retrieve necessary information on basis of provided attributes
     * and save them in our observableArrayList which is required for GUI
     */
    public void getKeyStage2() {
        for (int i=0; i < this.listLines.size(); i++) {

            JSONObject inputJsonObjectListLines = new JSONObject(this.listLines.get(i));

            this.listLinesLineRecords.add(
                    new LineRecord(
                            getKeyString(inputJsonObjectListLines, "type", this.listLines),
                            getKeyString(inputJsonObjectListLines, "name",  this.listLines),
                            "Demo",
                            getKeyString(inputJsonObjectListLines, "towards",  this.listLines),
                            getKeyStringDepartures(inputJsonObjectListLines, "departures"),
                            "XYZ"
                    )
            );





/*
            System.out.println(getKeyString(inputJsonObjectListLines, "type", this.listLines));
            System.out.println(getKeyString(inputJsonObjectListLines, "name", this.listLines));
            System.out.println(getKeyString(inputJsonObjectListLines, "towards", this.listLines));
            System.out.println(getKeyStringDepartures(inputJsonObjectListLines, "departures"));

 */

            /*
            JSONObject test = new JSONObject(getKeyStringDepartures(inputJsonObjectListLines, "departures", this.listLines));
            System.out.println(test.toString());

            System.out.println(test.get("departure").toString());
            JSONArray test1 = new JSONArray(test.get("departure").toString());
            for (int j = 0; j < test1.length(); j++) {

                String jsonArrayString = test1.get(j).toString();
                JSONObject innerJson = new JSONObject(jsonArrayString);

                JSONObject innerJsonTimePlanned = new JSONObject(innerJson.get("departureTime").toString());

                System.out.println(innerJsonTimePlanned.get("timePlanned").toString());


            }

             */








            //System.out.println(getKeyString(inputJsonObjectListLines, "departureTime", this.listLines));
            //System.out.println(getKeyString(inputJsonObjectListLines, "timePlanned", this.listLines));
            //getKey(inputJsonObjectListLines, "timePlanned", Stage.STAGE_LINES, this.listTimePlanned);

/*
            System.out.println(
            getKeyString(
                    new JSONObject(getKeyString(inputJsonObjectListLines, "departures", this.listLines)),
                            "departureTime",
                            this.listLines)
            );

 */








        }

    }




// I think I will not use this anymore, because I implemented this in the constructor
    public void retrieveJsonSource(String url_source) {
        try {

            URL url = new URL(url_source);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Getting the response code
            int responseCode = conn.getResponseCode();

            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {

                String jsonInput = "";
                Scanner scanner = new Scanner(url.openStream());

                //Write all the JSON data into a string using a scanner
                while (scanner.hasNext()) {
                    jsonInput += scanner.nextLine();
                }

                //Close the scanner
                scanner.close();


                /*
                //Using the JSON simple library parse the string into a json object
                JSONParser parse = new JSONParser();
                JSONObject data_obj = (JSONObject) parse.parse(inline);

                //Get the required object from the above created object
                JSONObject obj = (JSONObject) data_obj.get("Global");

                //Get the required data using its key
                System.out.println(obj.get("TotalRecovered"));

                JSONArray arr = (JSONArray) data_obj.get("Countries");

                for (int i = 0; i < arr.size(); i++) {

                    JSONObject new_obj = (JSONObject) arr.get(i);

                    if (new_obj.get("Slug").equals("spain")) {
                        System.out.println("Total Confirmed: " + new_obj.get("TotalConfirmed"));
                        break;
                    }
                }
                 */
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDiva() {
        return diva;
    }

    public void setDiva(String diva) {
        this.diva = diva;
    }

    public String getUrl_source() {
        return url_source;
    }

    public void setUrl_source(String url_source) {
        this.url_source = url_source;
    }

    public String getJsonInput() {
        return jsonInput;
    }

    public void setJsonInput(String jsonInput) {
        this.jsonInput = jsonInput;
    }

    public List<String> getListLines() {
        return listLines;
    }

    public void setListLines(List<String> listLines) {
        this.listLines = listLines;
    }

    public ObservableList<LineRecord> getListLinesLineRecords() {
        return listLinesLineRecords;
    }

    public void setListLinesLineRecords(ObservableList<LineRecord> listLinesLineRecords) {
        this.listLinesLineRecords = listLinesLineRecords;
    }

}
