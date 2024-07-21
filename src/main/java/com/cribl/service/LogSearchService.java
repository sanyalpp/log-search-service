package com.cribl.service;

import com.cribl.entities.Log;
import com.cribl.entities.LogKeyWordIndex;
import com.cribl.repository.LogKeyWordIndexRepository;
import com.cribl.repository.LogRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.cribl.util.FileUtil.readFileAsString;

@Service
@Slf4j
public class LogSearchService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private LogKeyWordIndexRepository logKeyWordIndexRepository;

    @SneakyThrows
    public Set<String> getAllLogs(String logFileName, String keyword, int pageSize, int offset) {

        Log log = logRepository.findByLogFileName(logFileName);
        if (log == null) {
            return new HashSet<>();
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
    private Set<String> getLogLines(List<LogKeyWordIndex> logKeyWordIndices) {
        Set<String> logs = new HashSet<>();
        for (LogKeyWordIndex logKeyWordIndex : logKeyWordIndices) {
            String logMessage = readFileAsString(logKeyWordIndex.getIndexedFileName());
            String logLine = logKeyWordIndex.getLogTimeStamp()
                    + " [" + logKeyWordIndex.getLogLevel()
                    +"] ["
                    + logKeyWordIndex.getLogRequestId() + "] : " + logMessage;
            logs.add(logLine);
        }

        return logs;
    }
}
