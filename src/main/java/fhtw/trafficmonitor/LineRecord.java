package fhtw.trafficmonitor;

public class LineRecord {
    private String lineName;
    private String lineTowards;
    private String lineDepartureTimePlanned;
    private String lineType;
    private String lineId;


    public LineRecord(String lineName, String lineTowards, String lineDepartureTimePlanned) {
        this.lineName = lineName;
        this.lineTowards = lineTowards;
        this.lineDepartureTimePlanned = lineDepartureTimePlanned;
    }

    public LineRecord(String lineName, String lineTowards, String lineDepartureTimePlanned, String lineType, String lineId) {
        this.lineName = lineName;
        this.lineTowards = lineTowards;
        this.lineDepartureTimePlanned = lineDepartureTimePlanned;
        this.lineType = lineType;
        this.lineId = lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public String getLineTowards() {
        return lineTowards;
    }

    public String getLineDepartureTimePlanned() {
        return lineDepartureTimePlanned;
    }


    public String getLineType() {
        return lineType;
    }

    public String getLineId() {
        return lineId;
    }
}
