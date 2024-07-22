package com.cribl.service;

import com.cribl.entities.Log;
import com.cribl.entities.LogKeyWordIndex;
import com.cribl.repository.LogKeyWordIndexRepository;
import com.cribl.repository.LogRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cribl.util.Constants.LOG_LINE_FORMAT;
import static com.cribl.util.FileUtil.readFileAsString;

@Service
@Slf4j
public class LogSearchService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private LogKeyWordIndexRepository logKeyWordIndexRepository;

    /*
    Method that fetches all logs based on the file name, keyword, pageSize and offset.
     */
    @SneakyThrows
    public List<String> getAllLogs(final String logFileName, final String keyword,
                                   final int pageSize, final int offset) {

        Log log = logRepository.findByLogFileName(logFileName);
        if (log == null) {
            return new ArrayList<>();
        }

        List<LogKeyWordIndex> logKeyWordIndices = null;

        if (keyword == null) {
            logKeyWordIndices = logKeyWordIndexRepository.findAllByLogFileId(log.getId(), pageSize, offset);
        } else {
            logKeyWordIndices = logKeyWordIndexRepository
                    .findByLogFileIdAndLogKeyWordLike(log.getId(), keyword, pageSize, offset);
        }
        return getLogLines(logKeyWordIndices);
    }

    @SneakyThrows
    private List<String> getLogLines(final List<LogKeyWordIndex> logKeyWordIndices) {
        Map<String, LogKeyWordIndex> logFileIndicesMap = new HashMap<>();
        List<String> logs = new ArrayList<>();
        for (LogKeyWordIndex logKeyWordIndex : logKeyWordIndices) {
            /*
             Multiple keywords could point to the same indexed file, we dont need to read the same file more than once
             */
            logFileIndicesMap.put(logKeyWordIndex.getIndexedFileName(), logKeyWordIndex);
        }

        for (Map.Entry<String, LogKeyWordIndex> indexedFileEntry : logFileIndicesMap.entrySet()) {
            String logMessage = readFileAsString(indexedFileEntry.getKey());
            String logLine = MessageFormat.format(LOG_LINE_FORMAT,
                    indexedFileEntry.getValue().getLogTimeStamp(),
                    indexedFileEntry.getValue().getLogLevel(),
                    indexedFileEntry.getValue().getLogRequestId(), logMessage);
            logs.add(logLine);
        }
        return logs;
    }
}
