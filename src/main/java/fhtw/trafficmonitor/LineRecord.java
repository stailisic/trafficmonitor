package fhtw.trafficmonitor;

public class LineRecord {
    private String lineTransportType;
    private String lineName;
    private String lineStationName;
    private String lineTowards;
    private String lineDepartureTimePlanned1; // Abfahrt CountDown in Minuten
    private String lineDepartureTimePlanned2; // Abfahrtszeit im Zeitformat hh:mm
    private String lineType;
    private String lineId;
    private String diva;

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

    public String getLineType() {
        return lineType;
    }

    public String getLineId() {
        return lineId;
    }

    public String getDiva() {
        return diva;
    }
}
