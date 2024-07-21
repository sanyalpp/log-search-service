package com.cribl.repository;

import com.cribl.entities.LogKeyWordIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogKeyWordIndexRepository extends JpaRepository<LogKeyWordIndex, String> {

    //@Query("SELECT l FROM LogKeyWordIndex l WHERE l.logKeyWord LIKE :logKeyWord")
    @Query(value = "SELECT * FROM INFORMATION_SCHEMA.log_keywords_index lki where lki.log_keyword like %?1% limit ?2 offset ?3", nativeQuery = true)
    List<LogKeyWordIndex> findByLogKeyWordLike(@Param("logKeyWord") String logKeyWord, int limit, int offset);

    List<LogKeyWordIndex> findByLogFileIdAndLogKeyWord(@Param("logFileId") String logFileId,
                                                       @Param("logKeyWord") String logKeyWord);
    List<LogKeyWordIndex> findByLogFileIdAndLogLevel(@Param("logFileId") String logFileId,
                                                     @Param("logLevel") String logLevel);

    List<LogKeyWordIndex> findByLogRequestId(@Param("logRequestId") String logRequestId);
}
