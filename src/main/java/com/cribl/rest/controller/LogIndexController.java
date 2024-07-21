package com.cribl.rest.controller;

import com.cribl.indexer.LogIndexerService;
import com.cribl.log.generator.LogFileGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.cribl.util.Constants.LOG_SAMPLE_FILE_NAME;
import static org.springframework.http.HttpStatus.OK;

@RestController
// Versioning with v1 so that other versions can be supported in future.
@RequestMapping(value = "/v1/logs", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE})
public class LogIndexController {

    @Autowired
    private LogIndexerService logIndexerService;

    @PostMapping(value = "/index")
    @ResponseStatus(OK)
    @ResponseBody
    public ResponseEntity<String> indexLogFile(@RequestParam("file") MultipartFile file) {
        logIndexerService.processLogFile(file);
        return ResponseEntity.ok("Logfile file {"+ file.getOriginalFilename() + "} successfully indexed.");
    }

    /*
    This method is to create a sample log file of the following format.
    <timestamp> [Log Level] [RequestId] : Log line
     */
    @PostMapping(value = "/create")
    @ResponseStatus(OK)
    @ResponseBody
    public ResponseEntity<String> createSampleLogFile(@RequestParam("file") MultipartFile file) {
        LogFileGenerator.createSampleLogFile(file);
        return ResponseEntity.ok("Successfully generated Logfile " + LOG_SAMPLE_FILE_NAME
                + " from file {"+ file.getOriginalFilename() + "} and stored at /var/logs");
    }

    /*

    This method can be used to post a single log event, index and save it
    public HttpStatus indexLogFile(LogEvent event) {
        logIndexerService.processLogEvent(events)
        return HttpStatus.OK;
    }
     */
}
