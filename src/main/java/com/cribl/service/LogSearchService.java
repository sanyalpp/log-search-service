package com.cribl.service;

import com.cribl.entities.Log;
import com.cribl.entities.LogKeyWordIndex;
import com.cribl.repository.LogKeyWordIndexRepository;
import com.cribl.repository.LogRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.cribl.util.FileUtil.readFileAsString;

@Service
@Slf4j
public class LogSearchService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private LogKeyWordIndexRepository logKeyWordIndexRepository;

    @SneakyThrows
    public List<String> getAllLogs(String logFileName, String keyword, int pageSize, int offset) {

        Log log = logRepository.findByLogFileName(logFileName);
        List<LogKeyWordIndex> logKeyWordIndices
                = logKeyWordIndexRepository.findByLogKeyWordLike(keyword, pageSize, offset);
        List<String> logs = new ArrayList<>();
        for (LogKeyWordIndex logKeyWordIndex : logKeyWordIndices) {
            String logLine = readFileAsString(logKeyWordIndex.getIndexedFileName());
            logs.add(logLine);
        }
        return logs;
    }
}
