package fhtw.trafficmonitor;

/**
 * Class used to create lineRecords when parsing the json block.
 * The resulted ArrayList containing lineRecords will be used to display the data in the TableView.
 */
public class LineRecord {
    private String lineTransportType;   // e.g. ptMetro
    private String lineName;            // e.g. U1, U2, U3, U4, U6
    private String lineStationName;     // e.g. Schwedenplatz, Vorgartenstraße (RadioButtons)
    private String lineTowards;         // Endstation/s e.g. in regard of U4 Heiligenstadt and Hütteldorf
    private String lineDepartureTimePlanned1; // departure countdown in minutes
    private String lineDepartureTimePlanned2; // departure time in some sort of date/time format
    private String diva;                // ID of respective station name

    /**
     * Contructor
     * @param lineTransportType e.g. ptMetro
     * @param diva ID of respective station name
     * @param lineName e.g. U1, U2, U3, U4, U6
     * @param lineStationName e.g. Schwedenplatz, Vorgartenstraße (RadioButtons)
     * @param lineTowards Endstation/s e.g. in regard of U4 Heiligenstadt and Hütteldorf
     * @param lineDepartureTimePlanned1 departure countdown in minutes
     * @param lineDepartureTimePlanned2 departure time in some sort of date/time format
     */
    public LineRecord(String lineTransportType, String diva,String lineName, String lineStationName, String lineTowards, String lineDepartureTimePlanned1, String lineDepartureTimePlanned2) {
        this.lineTransportType = lineTransportType;
        this.diva = diva;
        this.lineName = lineName;
        this.lineStationName = lineStationName;
        this.lineTowards = lineTowards;
        this.lineDepartureTimePlanned1 = lineDepartureTimePlanned1;
        this.lineDepartureTimePlanned2 = lineDepartureTimePlanned2;
    }


    /**
     * Method to return String of
     * @return the respective transport type, e.g. for U1 it's ptMetro
     */
    public String getLineTransportType() {
        return lineTransportType;
    }

    /**
     * Method to return String of
     * @return the respective name of transportation, e.g. U4
     */
    public String getLineName() {
        return lineName;
    }

    /**
     * Method to return String of
     * @return the respective name of the station in question, e.g. Schwedenplatz
     */
    public String getLineStationName() {
        return lineStationName;
    }

    /**
     * Method to return String of
     * @return the name of the end station in regard of the respective station in question
     */
    public String getLineTowards() {
        return lineTowards;
    }

    /**
     * Method to return String of
     * @return the departure countdown in minutes
     */
    public String getLineDepartureTimePlanned1() {
        return lineDepartureTimePlanned1;
    }

    /**
     * Method to return String of
     * @return the departure time in date/time format used/provided by the json data
     */
    public String getLineDepartureTimePlanned2() {
        return lineDepartureTimePlanned2;
    }

    /**
     * Method to return String of
     * @return the diva number, which is the ID of the respective station in question
     */
    public String getDiva() {
        return diva;
    }
}
