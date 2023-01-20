package fhtw.trafficmonitor;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

/**
 * Class used for creating a logger to track activities in a local session logfile
 */
public class Logging {
    private static Logger trafficMonitorLog = Logger.getLogger("TrafficMonitorLog");

    /**
     * Constructor to create logger file once application started
     */
    public Logging() {
        try {
            DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String logExtention = df.format(new Date());

            FileHandler fh = new FileHandler("src/main/resources/fhtw/trafficmonitor/" + logExtention + "_session_TrafficMonitorLog.log");
            trafficMonitorLog.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to provide logging.
     * Once the application has startet a logger will be created and all
     * activities by placing this method will be recorded and stored in a session file locally
     * in the path from source root started: src/main/resources/fhtw/trafficmonitor/*.log
     *     - naming of file: 'yyyyMMdd_HHmmss'_session_TrafficMonitorLog.log
     *       - e.g. 20230120_103501_session_TrafficMonitorLog.log
     * @param logMessage custom message to be logged
     */
    public void logTrafficMonitor(String logMessage) {
        try {
            trafficMonitorLog.log(Level.INFO, logMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
