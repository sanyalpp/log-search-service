package com.cribl.rest.response;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@EqualsAndHashCode
public class LogSearchResponse implements Serializable {
    private String logFileName;
    private Set<String> logLines;
    /*
    Note:
        HATEOAS can be implemented so that the links contain the prev,next, current page etc, instead
        of returning the page and offset in the response.
     */
    private int pageSize;
    private int nextOffset;

}
