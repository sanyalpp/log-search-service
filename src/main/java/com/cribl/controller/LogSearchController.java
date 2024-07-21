package com.cribl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.cribl.entities.Log;

//creating RestController
@RestController
public class LogSearchController {
    //autowired the LogSearchService class
    //@Autowired
    //private LogSearchService logSearchService;

    //creating a get mapping that retrieves all the logs detail from the database
    @GetMapping("/logs")
    private List<Log> getAllLogs() {

        return null;//logSearchService.getAllLogs();
    }

    //creating a get mapping that retrieves the detail of a specific log
    @GetMapping("/logs/{id}")
    private Log getStudent(@PathVariable("id") int id) {

        return null;//logSearchService.getStudentById(id);
    }

}
