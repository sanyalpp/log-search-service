package com.cribl.repository;
import org.springframework.data.repository.CrudRepository;
import com.cribl.model.Log;
public interface LogRepository extends CrudRepository<Log, Integer>
{
}
