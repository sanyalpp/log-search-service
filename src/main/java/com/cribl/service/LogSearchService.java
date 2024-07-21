package com.cribl.service;

import com.cribl.repository.LogKeyWordIndexRepository;
import com.cribl.repository.LogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LogSearchService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private LogKeyWordIndexRepository logKeyWordIndexRepository;

    p
}
