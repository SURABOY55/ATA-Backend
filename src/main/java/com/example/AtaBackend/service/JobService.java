package com.example.AtaBackend.service;

import java.util.List;
import java.util.Map;

public interface JobService {
    List<Map<String, Object>> findJobRecordWithCondition(String jobTitle, String gender, String salary, String[] sort, String sortType, String[] fields);
}
