package com.cribl.rest.controller;

import java.util.List;
import java.util.Set;

import com.cribl.rest.response.LogSearchResponse;
import com.cribl.service.LogSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1", produces = {MediaType.APPLICATION_JSON_VALUE})
@Slf4j
public class LogSearchController {
    @Autowired
    private LogSearchService logSearchService;

    //Creating a get mapping that retrieves all the logs detail from the database
    @GetMapping("/logs")
    private LogSearchResponse getAllLogs(@RequestParam(value = "logFileName") String logFileName,
                                         @RequestParam(value = "keyword", required = false) String keyword,
                                         @RequestParam(value = "pageSize",defaultValue = "50") int pageSize,
                                         @RequestParam(value = "offset",defaultValue = "1") int offset) {
        validate(keyword, pageSize, offset);
        Set<String> logs = logSearchService.getAllLogs(logFileName, keyword, pageSize, offset);
        return LogSearchResponse.builder()
                .logFileName(logFileName)
                .pageSize(pageSize)
                .nextOffset(offset + 1)
                .logLines(logs)
                .build();
    }

    // Validations
    private void validate(String keyword, int pageSize, int offset) {
        if (pageSize < 1 || offset < 0 ) {
            throw new IllegalArgumentException("pageSize or offset values are incorrect");
        }

        if (keyword != null && keyword.isEmpty()) {
           throw new IllegalArgumentException("provided keyword is empty");
        }
    }
}
