package com.cribl.repository;

import com.cribl.entities.LogKeyWordIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogKeyWordIndexRepository extends JpaRepository<LogKeyWordIndex, String> {

    List<LogKeyWordIndex> findByLogKeyWord(@Param("logKeyWord") String logKeyWord);

    List<LogKeyWordIndex> findByLogFileIdAndLogKeyWord(@Param("logFileId") String logFileId,
                                                       @Param("logKeyWord") String logKeyWord);
    List<LogKeyWordIndex> findByLogFileIdAndLogLevel(@Param("logFileId") String logFileId,
                                                     @Param("logLevel") String logLevel);

    List<LogKeyWordIndex> findByLogRequestId(@Param("logRequestId") String logRequestId);
}
