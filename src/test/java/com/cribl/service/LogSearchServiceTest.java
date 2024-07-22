package com.cribl.service;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.cribl.entities.Log;
import com.cribl.entities.LogKeyWordIndex;
import com.cribl.repository.LogKeyWordIndexRepository;
import com.cribl.repository.LogRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LogSearchServiceTest {

    @Mock
    private LogRepository logRepository;

    @Mock
    private LogKeyWordIndexRepository logKeyWordIndexRepository;

    @InjectMocks
    private LogSearchService logSearchService;

    @Test
    public void testGetAllLogs_logNotFound() {
        // Given
        String logFileName = "nonexistent.log";
        when(logRepository.findByLogFileName(logFileName)).thenReturn(null);

        // When
        List<String> result = logSearchService.getAllLogs(logFileName, null, 10, 0);

        // Then
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testGetAllLogs_keywordIsNull() {
        // Given
        String logFileName = "existing.log";
        Log log = new Log();
        log.setId(UUID.randomUUID().toString());
        //when(logRepository.findByLogFileName(logFileName)).thenReturn(log);

        LogKeyWordIndex index1 = new LogKeyWordIndex();
        index1.setIndexedFileName("index1.log");
        LogKeyWordIndex index2 = new LogKeyWordIndex();
        index2.setIndexedFileName("index2.log");

        //when(logKeyWordIndexRepository.findAllByLogFileId(anyString(), anyInt(), anyInt()))
                //.thenReturn(Arrays.asList(index1, index2));

        // When
        //List<String> result = logSearchService.getAllLogs(logFileName, null, 10, 0);

        // Then
        // Assertions
    }

    @Test
    public void testGetAllLogs_keywordIsNotNull() {
        // Given
        String logFileName = "existing.log";
        String keyword = "error";
        Log log = new Log();
        log.setId(UUID.randomUUID().toString());
        //when(logRepository.findByLogFileName(logFileName)).thenReturn(log);

        LogKeyWordIndex index1 = new LogKeyWordIndex();
        index1.setIndexedFileName("index1.log");
        LogKeyWordIndex index2 = new LogKeyWordIndex();
        index2.setIndexedFileName("index2.log");

        //when(logKeyWordIndexRepository.findByLogFileIdAndLogKeyWordLike(anyString(), anyString(), anyInt(), anyInt()))
              //  .thenReturn(Arrays.asList(index1, index2));

        // When
        //List<String> result = logSearchService.getAllLogs(logFileName, keyword, 10, 0);

        // Then
        // Assertions
    }
}
