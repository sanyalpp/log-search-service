package com.cribl.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Setter
@Getter
@Slf4j
public class LogEntry {
    private final static String LOG_LINE_REGEX =
            "(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{6}) \\[(\\w+)] \\[([a-f0-9-]{36})] : (.+)";
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
    private LocalDateTime timestamp;
    private String logLevel;
    private String requestUuid;
    private String message;
    private List<String> tokens;

    public LogEntry(LocalDateTime timestamp, String logLevel, String requestUuid, String message) {
        this.timestamp = timestamp;
        this.logLevel = logLevel;
        this.message = message;
        this.requestUuid = requestUuid;
        this.tokens = tokenizeLogMessage(message);
    }

    public static LogEntry parseLogLine(String logLine) {
        Pattern pattern = Pattern.compile(LOG_LINE_REGEX);
        Matcher matcher = pattern.matcher(logLine);

        if (matcher.matches()) {
            String timestamp = matcher.group(1);
            String logLevel = matcher.group(2);
            String requestId = matcher.group(3);
            String logMessage = matcher.group(4);
            return new LogEntry(LocalDateTime.parse(timestamp, formatter), logLevel, requestId, logMessage);
        } else {
            throw new IllegalArgumentException("Couldn't parse log line:  " + logLine);
        }
    }

    private static List<String> tokenizeLogMessage(String logMessage) {
        String[] words = logMessage.split("\\W+");
        return Arrays.asList(words);
    }
    @Override
    public String toString() {
        return timestamp + " " + logLevel + " [" + requestUuid + "] : " + message ;
    }

    public String generateHashForLogEntry() {
        return Objects.hash(timestamp, logLevel, requestUuid, message, tokens) + UUID.randomUUID().toString();
    }

    public static void main(String[] args) {
        String logLine = "2024-07-20T10:32:23.757473 [ERROR] [cb8d87e9-80ae-462b-8fd1-abb9c5b293dd] : Cribl is the Data Engine for IT and Security. Our mission is to empower enterprises to unlock the value of all their data.";
        String regex = "(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{6}) \\[(\\w+)] \\[([a-f0-9-]{36})] : (.+)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(logLine);

        if (matcher.matches()) {
            String timestamp = matcher.group(1);
            String logLevel = matcher.group(2);
            String requestId = matcher.group(3);
            String message = matcher.group(4);

            log.debug("Timestamp: " + timestamp);
            log.debug("Log Level: " + logLevel);
            log.debug("Request ID: " + requestId);
            log.debug("Message: " + message);
        } else {
            System.out.println("Log line format is incorrect.");
        }
    }
}
