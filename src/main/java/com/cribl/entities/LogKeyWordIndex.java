package com.cribl.entities;

import lombok.Data;
import lombok.Generated;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@ToString
@Table(name = "log_keywords_index", schema = "INFORMATION_SCHEMA")
public class LogKeyWordIndex {

    @Id
    @Column (name = "log_keyword_index_id")
    @Generated
    private String id;

    @Column(name =  "log_keyword")
    private String logKeyWord;

    //@OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    //@JoinColumn(name = "log_file", referencedColumnName = "log_file_id")
    @Column(name = "log_file_id")
    private String logFileId;

    @Column(name = "indexed_file_name")
    private String indexedFileName;

    @Column(name = "log_time_stamp")
    private Date logTimeStamp;

    @Column(name = "log_request_id")
    private String logRequestId;

    @Column(name = "log_level")
    private String logLevel;

    @PrePersist
    public void onCreate() {
        this.setId(UUID.randomUUID().toString());
    }
}
