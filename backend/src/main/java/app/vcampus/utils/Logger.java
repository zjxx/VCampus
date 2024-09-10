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

    public static void logToFile(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        writer.println(timestamp + " - " + message);
    }

    public static void logToConsole(String message) {
        System.out.println(message);
    }

    public static void log(String message) {
        logToFile(message);
        logToConsole(message);
    }

    public static void close() {
        if (writer != null) {
            writer.close();
        }
    }
}