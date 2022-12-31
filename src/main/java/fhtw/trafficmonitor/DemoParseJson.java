package fhtw.trafficmonitor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Demo Playground
 */
public class DemoParseJson {

    enum Stage {
        STAGE_LINES,
        STAGE_LINES_INNER
    }

    private static List<String> listLines = new ArrayList<>();
    private static ObservableList<LineRecord> listLinesLineRecords = FXCollections.observableArrayList();

    /**
     * Print key value of JsonObject
     * @param json
     * @param key
     */
    public static void parseObject(JSONObject json, String key) {
        // System.out.println(json.has(key));
        System.out.println(json.get(key));
        //return json.get(key).toString();
    }

    public static void parseObjectLines(JSONObject json, String key) {
        addListLines(json.get(key).toString().substring(1,json.get(key).toString().length() -1));
    }

    /**
     * get Key value
     */
    public static void getKey(JSONObject json, String key, Enum stage) {

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
                            getKey(json.getJSONObject(nextKeys), key, stage);
                        }

                    } else if (json.get(nextKeys) instanceof JSONArray) {       // if nextKey is an JsonArray
                        JSONArray jsonArray = json.getJSONArray(nextKeys);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String jsonArrayString = jsonArray.get(i).toString();
                            JSONObject innerJson = new JSONObject(jsonArrayString);

                            if (exists == false) {
                                getKey(innerJson, key, stage);
                            }
                        }

                    }
                } catch (Exception e) {
                    // Todo: handle exception
                }
            }

        } else { // exists part
            if (Stage.STAGE_LINES.equals(stage)) {
                parseObjectLines(json, key);
            }

            else if (stage.equals(Stage.STAGE_LINES_INNER)) {
              //  return "DepartureTime";
                //return json.get(key).toString();
                json.get(key).toString();
            }
            //return parseObject(json, key);
            parseObject(json, key);


        }

        //return json.toString();
       // return "here";
    }

    public static void main (String[] args) {
        String inputJson="{\n" +
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

        /**
         * Convert String to JSONObject
         */
        JSONObject inputJsonObject = new JSONObject(inputJson);

        getKey(inputJsonObject, "lines", Stage.STAGE_LINES);
        System.out.println("-------");

        System.out.println("Size of ListKeys: " + getListLines().size());
        getListLines().forEach(System.out::println);

        System.out.println("--------------------------------------------------");

        for (int i=0; i < getListLines().size(); i++) {
            //System.out.println("Index [" + i + "]: " + getListLines().get(i));


            try {
                JSONObject inputJsonObjectListLines = new JSONObject(getListLines().get(i));

                /*

                addListLinesLineRecords(listLinesLineRecords
                        ,getKey(inputJsonObjectListLines, "type", Stage.STAGE_LINES_INNER)
                        ,getKey(inputJsonObjectListLines, "name", Stage.STAGE_LINES_INNER)
                        , ""
                        ,getKey(inputJsonObjectListLines, "towards", Stage.STAGE_LINES_INNER)
                        ,getKey(inputJsonObjectListLines, "timePlanned", Stage.STAGE_LINES_INNER)
                        ,""
                );

                 */


/*
                System.out.println(getKey(inputJsonObjectListLines, "type", Stage.STAGE_LINES_INNER));
                System.out.println(getKey(inputJsonObjectListLines, "name", Stage.STAGE_LINES_INNER));
                System.out.println(getKey(inputJsonObjectListLines, "towards", Stage.STAGE_LINES_INNER));
                System.out.println(getKey(inputJsonObjectListLines, "timePlanned", Stage.STAGE_LINES_INNER));

 */



                getKey(inputJsonObjectListLines, "type", Stage.STAGE_LINES_INNER);
                getKey(inputJsonObjectListLines, "name", Stage.STAGE_LINES_INNER);
                getKey(inputJsonObjectListLines, "towards", Stage.STAGE_LINES_INNER);
                getKey(inputJsonObjectListLines, "timePlanned", Stage.STAGE_LINES_INNER);


            } catch (Exception e) {
                System.out.println("Index [" + i + "] error. ");
            }



        }



        System.out.println("--------------------------------------------------");


        //listLinesLineRecords.forEach(System.out::println);

/*
        // remove first and last char of string: https://reactgo.com/java-remove-first-last-character/#:~:text=To%20remove%20the%20first%20and%20last%20character%20of,and%20last%20character%20o%20from%20the%20following%20string%3A
        for (int i=0; i < getListLines().size(); i++) {
            System.out.println("Index [" + i + "]: " + getListLines().get(i).substring(1,getListLines().get(i).length() - 1));

        }

 */






    }

    public static List<String> getListLines() {
        return listLines;
    }

    public static void setListLines(List<String> listLines) {
        DemoParseJson.listLines = listLines;
    }

    public static void addListLines(String string) {
        DemoParseJson.listLines.add(string);
    }

    public static ObservableList<LineRecord> getListLinesLineRecords() {
        return listLinesLineRecords;
    }

    public static void setListLinesLineRecords(ObservableList<LineRecord> listLinesLineRecords) {
        DemoParseJson.listLinesLineRecords = listLinesLineRecords;
    }

    public static void addListLinesLineRecords(ObservableList<LineRecord> listLinesLineRecords, String lineTransportType, String lineName, String lineStationName, String lineTowards, String lineDepartureTimePlanned1, String lineDepartureTimePlanned2) {
        DemoParseJson.listLinesLineRecords.add(new LineRecord(lineTransportType, lineName, lineStationName, lineTowards, lineDepartureTimePlanned1,lineDepartureTimePlanned2));
    }
}
