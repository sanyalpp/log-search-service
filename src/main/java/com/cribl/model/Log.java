package com.cribl.model;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//mark class as an Entity
@Entity
//defining class name as Table name
@Table
@Getter
public class Log {
    //mark id as primary key
    @Id
    //defining id as column name
    @Column
    private int id;
    //defining name as column name
    @Column
    private String name;
}