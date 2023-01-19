package fhtw.trafficmonitor;

public class LineRecord {
    private String lineTransportType;   // e.g. ptMetro
    private String lineName;            // e.g. U1, U2, U3, U4, U6
    private String lineStationName;     // e.g. Schwedenplatz, Vorgartenstraße (RadioButtons)
    private String lineTowards;         // Endstation/s e.g. in regard of U4 Heiligenstadt and Hütteldorf
    private String lineDepartureTimePlanned1; // departure countdown in minutes
    private String lineDepartureTimePlanned2; // departure time in some sort of date/time format
    private String diva;                // ID of respective station name

    public LineRecord(String lineTransportType, String diva,String lineName, String lineStationName, String lineTowards, String lineDepartureTimePlanned1, String lineDepartureTimePlanned2) {
        this.lineTransportType = lineTransportType;
        this.diva = diva;
        this.lineName = lineName;
        this.lineStationName = lineStationName;
        this.lineTowards = lineTowards;
        this.lineDepartureTimePlanned1 = lineDepartureTimePlanned1;
        this.lineDepartureTimePlanned2 = lineDepartureTimePlanned2;
    }

    public String getLineTransportType() {
        return lineTransportType;
    }

    public String getLineName() {
        return lineName;
    }

    public String getLineStationName() {
        return lineStationName;
    }

    public String getLineTowards() {
        return lineTowards;
    }

    public String getLineDepartureTimePlanned1() {
        return lineDepartureTimePlanned1;
    }

    public String getLineDepartureTimePlanned2() {
        return lineDepartureTimePlanned2;
    }


    public String getDiva() {
        return diva;
    }
}
