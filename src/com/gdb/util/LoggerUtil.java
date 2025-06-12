package com.gdb.util;

import java.io.*;
import java.time.LocalDateTime;

public class LoggerUtil {

    private static final String LOG_FILE = "logs/error_log.txt";

    public static void logError(Exception e) {
        try {
            // Ensure the log file exists before writing
            File logFile = new File(LOG_FILE);
            logFile.getParentFile().mkdirs();
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            try (FileWriter fw = new FileWriter(logFile, true); // Append mode
                 PrintWriter pw = new PrintWriter(fw)) {

                pw.println("[" + LocalDateTime.now() + "] ERROR: " + e.getMessage());
                for (StackTraceElement element : e.getStackTrace()) {
                    pw.println("\t at " + element);
                }
                pw.println("-------------------------------------------------");

            }
        } catch (IOException ioEx) {
            System.err.println("Error writing to log file: " + ioEx.getMessage());
        }
    }
}