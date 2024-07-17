package com.cribl.service;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cribl.model.Log;
import com.cribl.repository.LogRepository;
//defining the business logic
@Service
public class LogSearchService
{
@Autowired
LogRepository logRepository;
//getting all student records
public List<Log> getAllLogs()
{
List<Log> students = new ArrayList<Log>();
logRepository.findAll().forEach(log -> students.add(log));
return students;
}
//getting a specific record
public Log getStudentById(int id)
{
return logRepository.findById(id).get();
}
public void saveOrUpdate(Log log)
{
logRepository.save(log);
}
//deleting a specific record
public void delete(int id) 
{
logRepository.deleteById(id);
}
}