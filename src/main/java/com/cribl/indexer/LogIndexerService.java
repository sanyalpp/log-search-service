package com.cribl.indexer;

import com.cribl.entities.Log;
import com.cribl.entities.LogKeyWordIndex;
import com.cribl.model.LogEntry;
import com.cribl.repository.LogKeyWordIndexRepository;
import com.cribl.repository.LogRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZoneId;
import java.util.List;
import java.util.Date;

import static com.cribl.util.Constants.LOG_INDEX_FILE_EXTENSION;
import static com.cribl.util.Constants.SERVER_LOG_INDEXED_FILE_LOCATION;
import static com.cribl.util.Constants.SERVER_TEMP_LOG_INDEX_PROCESSING_FILE_LOCATION;
import static com.cribl.util.FileUtil.copyFileToLocation;
import static com.cribl.util.FileUtil.readFile;
import static com.cribl.util.FileUtil.writeContentToFile;

@Service
@Slf4j
public class LogIndexerService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private LogKeyWordIndexRepository logKeyWordIndexRepository;

    public void processLogFile(final MultipartFile file) {

        String filePath = copyFileToLocation(file, SERVER_TEMP_LOG_INDEX_PROCESSING_FILE_LOCATION);
        Log logFile = createLogFileEntry(file.getOriginalFilename());
        indexLogFile(filePath, logFile.getId());
    }

    private Log createLogFileEntry(String serverFilePath) {
        Log log = new Log();
        log.setLogFileName(serverFilePath);
        return logRepository.save(log);
    }

    private void indexLogFile(String filePath, String fileId) {
        List<String> logLines = readFile(filePath);

        for (String logLine : logLines) {
            log.info("Parsing log line: {}", logLine);
            LogEntry logEntry = LogEntry.parseLogLine(logLine);
            String indexedFilePath = getIndexedFileName(logEntry);
            writeContentToFile(indexedFilePath, logEntry.getMessage());
            for (String logKeyWord : logEntry.getTokens()) {
                LogKeyWordIndex logKeyWordIndex = new LogKeyWordIndex();
                logKeyWordIndex.setLogFileId(fileId);
                logKeyWordIndex.setLogTimeStamp(Date.from(logEntry.getTimestamp().atZone(
                        ZoneId.systemDefault()).toInstant()));
                logKeyWordIndex.setLogRequestId(logEntry.getRequestUuid());
                logKeyWordIndex.setLogKeyWord(logKeyWord);
                logKeyWordIndex.setLogLevel(logEntry.getLogLevel());
                logKeyWordIndex.setIndexedFileName(indexedFilePath);
                logKeyWordIndexRepository.save(logKeyWordIndex);
            }
        }
    }

    private static String getIndexedFileName(LogEntry logEntry) {
        return SERVER_LOG_INDEXED_FILE_LOCATION
                + logEntry.generateHashForLogEntry()
                + LOG_INDEX_FILE_EXTENSION;
    }
}

