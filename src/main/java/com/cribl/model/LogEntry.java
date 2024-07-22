package com.cribl.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.cribl.util.Constants.LOG_LINE_FORMAT;

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
            return new LogEntry(LocalDateTime.parse(timestamp, formatter),
                    logLevel, requestId, logMessage);
        } else {
            throw new IllegalArgumentException("Couldn't parse log line:  " + logLine);
        }
    }

    /*
    Keywords are being tokenized based on anything apart from word characters.
    \\W matches any character that is not a word character.
    Word characters are typically defined as [a-zA-Z0-9_] (letters, digits, and underscores).
     */
    private static List<String> tokenizeLogMessage(String logMessage) {
        String[] words = logMessage.split("\\W+");
        return Arrays.asList(words);
    }
    @Override
    public String toString() {
        return MessageFormat.format(LOG_LINE_FORMAT, timestamp, logLevel, requestUuid, message);
    }

    public String generateHashForLogEntryKeywordIndex() {
        return Objects.hash(timestamp, logLevel, requestUuid, message, tokens)
                + UUID.randomUUID().toString();
    }
}
