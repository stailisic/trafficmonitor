package fhtw.trafficmonitor;

public class LineRecord {
    private String lineTransportType;
    private String lineName;
    private String lineStationName;
    private String lineTowards;
    private String lineDepartureTimePlanned1;
    private String lineDepartureTimePlanned2;
    private String lineType;
    private String lineId;

    public LineRecord(String lineTransportType, String lineName, String lineStationName, String lineTowards, String lineDepartureTimePlanned1, String lineDepartureTimePlanned2) {
        this.lineTransportType = lineTransportType;
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
}
