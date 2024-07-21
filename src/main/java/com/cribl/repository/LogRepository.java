package com.cribl.repository;

import com.cribl.entities.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log, String> {
    Log findByLogFileName(@Param("logFileName") String logFileName);
}
