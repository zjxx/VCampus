package app.vcampus.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger is a utility class for logging messages to both a file and the console.
 * It provides methods to log messages with timestamps and IP addresses.
 */
public class Logger {
    private static PrintWriter writer;

    // Static block to initialize the PrintWriter for logging to a file
    static {
        try {
            writer = new PrintWriter(new FileWriter("logs.txt", true), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs a message to a file with a timestamp and IP address.
     *
     * @param message the message to log
     * @param ipAddress the IP address of the client
     */
    public static void logToFile(String message, String ipAddress) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        writer.println(timestamp + " - " + ipAddress + " - " + message);
    }

    /**
     * Logs a message to the console with an IP address.
     *
     * @param message the message to log
     * @param ipAddress the IP address of the client
     */
    public static void logToConsole(String message, String ipAddress) {
        System.out.println(ipAddress + " - " + message);
    }

    /**
     * Logs a message to both a file and the console with a timestamp and IP address.
     *
     * @param message the message to log
     * @param ipAddress the IP address of the client
     */
    public static void log(String message, String ipAddress) {
        logToFile(message, ipAddress);
        logToConsole(message, ipAddress);
    }

    /**
     * Closes the PrintWriter used for logging to a file.
     */
    public static void close() {
        if (writer != null) {
            writer.close();
        }
    }
}