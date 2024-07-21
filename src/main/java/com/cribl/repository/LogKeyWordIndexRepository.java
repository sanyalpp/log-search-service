package com.cribl.repository;

import com.cribl.entities.LogKeyWordIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogKeyWordIndexRepository extends JpaRepository<LogKeyWordIndex, String> {

    @Query(value = "SELECT * FROM INFORMATION_SCHEMA.log_keywords_index lki " +
            "where lki.log_file_id = ?1 AND lki.log_keyword like ?2 " +
            "limit ?3 offset ?4", nativeQuery = true)
    List<LogKeyWordIndex> findByLogFileIdAndLogKeyWordLike(@Param("logFileId") String logFileId,
                                                           @Param("logKeyWord") String logKeyWord,
                                                           int limit,
                                                           int offset);
    @Query(value = "SELECT * FROM INFORMATION_SCHEMA.log_keywords_index lki " +
            "where lki.log_file_id = ?1 " +
            "limit ?2 offset ?3", nativeQuery = true)
    List<LogKeyWordIndex> findAllByLogFileId(@Param("logFileId") String logFileId,
                                          int limit,
                                          int offset);

    /*
     Other granular queries can be provided by logLevel and requestId as well.
     The following two methods are examples, they aren't implemented.
     */
    List<LogKeyWordIndex> findByLogFileIdAndLogLevel(@Param("logFileId") String logFileId,
                                                     @Param("logLevel") String logLevel);

    List<LogKeyWordIndex> findByLogRequestId(@Param("logRequestId") String logRequestId);
}
