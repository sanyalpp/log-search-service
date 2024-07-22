package com.cribl.rest.controller;

import com.cribl.service.LogSearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LogSearchControllerTest {

    @Mock
    private LogSearchService logSearchService;

    @InjectMocks
    private LogSearchController logSearchController;

    @Test(expected = IllegalArgumentException.class)
    public void testGetAllLogs_LogFileNameIsNull() {
        logSearchController.getAllLogs(null, "", 10, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAllLogs_pageSizeIsNegative() {
        logSearchController.getAllLogs("some.log", "", -10, 1);
    }

    /*
    More test cases ...
     */
}
