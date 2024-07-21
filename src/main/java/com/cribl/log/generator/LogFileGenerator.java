package com.cribl.log.generator;

import com.cribl.model.LogEntry;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/*
This program generates a sample log file.
 */
public class LogFileGenerator {
    private static final Random random = new Random();

    private static List<LogEntry> generateLogEntries(String fileReadPath) {
        List<LogEntry> logEntries = new ArrayList<>();
        String[] logLevels = {"[INFO]", "[DEBUG]", "[ERROR]"};

        List<String> logs = readLogsFromFile(fileReadPath);

        for (String message : logs) {
            LocalDateTime timestamp = LocalDateTime.now();
            String logLevel = logLevels[random.nextInt(100) % logLevels.length];
            UUID requestUuid = UUID.randomUUID(); // Generate a random UUID for each request
            logEntries.add(new LogEntry(timestamp, logLevel, requestUuid.toString(), message));
            LogEntry errorLogEntry = generateRandomExceptionLog();
            if (errorLogEntry != null) {
                logEntries.add(errorLogEntry);
            }
        }
        return logEntries;
    }

    private static LogEntry generateRandomExceptionLog() {
        if (random.nextInt(1000) % 10 == 0) {
            try {
                // Generate a random exception
                throw generateRandomException();
            } catch (Exception e) {
                return new LogEntry(LocalDateTime.now(),
                        "[ERROR]", UUID.randomUUID().toString(), getStackTraceAsString(e));
            }
        }
        return null;
    }

    public static void writeLogsToFile(List<LogEntry> logs, String fileWritePath) {

        // Construct the full path to /var/log within the current directory
        File logDir = Paths.get(fileWritePath, "var", "logs").toFile();

        // Ensure the directory exists
        if (!logDir.exists()) {
            if (logDir.mkdirs()) {
                System.out.println("Created directories: " + logDir.getPath());
            } else {
                System.err.println("Failed to create directories: " + logDir.getPath());
                return;
            }
        }

        // Create the log file within the directory
        File logFile = new File(logDir, "serviceLogs.log");

        // Write the list of strings to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile))) {
            for (LogEntry log : logs) {
                System.out.println("Writing : " + log);
                writer.write(log.toString());
                writer.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Logs written to file: " + logFile.getPath());
    }

    private static List<String> readLogsFromFile(String filePath) {
        List<String> logs = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logs.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return logs;
    }

    // Method to generate a random exception
    private static Exception generateRandomException() {
        Random random = new Random();
        int exceptionType = random.nextInt(3); // Generate a random number between 0 and 2

        switch (exceptionType) {
            case 0:
                return new NullPointerException("Generated NullPointerException");
            case 1:
                return new ArrayIndexOutOfBoundsException("Generated ArrayIndexOutOfBoundsException");
            case 2:
                return new IllegalArgumentException("Generated IllegalArgumentException");
            default:
                return new Exception("Generated General Exception");
        }
    }
    // Method to get the stack trace of an exception as a string
    private static String getStackTraceAsString(Exception e) {
        StackTraceElement [] stackTrace = e.getStackTrace();
        StringBuilder str = new StringBuilder();
        for (StackTraceElement stackTraceElement : stackTrace) {
            str.append(stackTraceElement.toString()).append(" ");
        }
        System.out.println(str);
        return str.toString();
    }

    public static void main(String[] args) {
        // Please modify based on your destination directory
        String fileReadPath = System.getProperty("user.dir")
                + FileSystems.getDefault().getSeparator()
                + "LogsSample/sample-cribl-for-log.txt";
        System.out.println("Read from " + fileReadPath);
        String fileWritePath = System.getProperty("user.dir")
                +  FileSystems.getDefault().getSeparator();
        System.out.println("Write from: " + fileWritePath);
        writeLogsToFile(generateLogEntries(fileReadPath), fileWritePath);
    }
}
