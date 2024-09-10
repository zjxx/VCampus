package app.vcampus.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static PrintWriter writer;

    static {
        try {
            writer = new PrintWriter(new FileWriter("logs.txt", true), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logToFile(String message, String ipAddress) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        writer.println(timestamp + " - " + ipAddress + " - " + message);
    }

    public static void logToConsole(String message, String ipAddress) {
        System.out.println(ipAddress + " - " + message);
    }

    public static void log(String message, String ipAddress) {
        logToFile(message, ipAddress);
        logToConsole(message, ipAddress);
    }

    public static void close() {
        if (writer != null) {
            writer.close();
        }
    }
}