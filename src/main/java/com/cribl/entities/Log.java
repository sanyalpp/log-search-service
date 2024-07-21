package com.cribl.entities;

import lombok.Data;
import lombok.Generated;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.util.UUID;

//mark class as an Entity
@Entity
@ToString
@Table(name = "log_file", schema = "INFORMATION_SCHEMA")
@Data
public class Log {

    // Id - Primary Key
    @Id
    @Column(name = "log_file_id")
    @Generated
    private String id;
    //defining name as column name
    @Column(name = "log_file_name")
    private String logFileName;

    @PrePersist
    public void onCreate() {
        this.setId(UUID.randomUUID().toString());
    }
}