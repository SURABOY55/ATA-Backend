package com.example.AtaBackend.controller;

import com.example.AtaBackend.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping("/job_data")
    public ResponseEntity<List<Map<String, Object>>> getJobData(
        @RequestParam(required = false, name = "job_title") String jobTitle,
        @RequestParam(required = false) String gender,
        @RequestParam(required = false) String salary,
        @RequestParam(required = false) String[] fields,
        @RequestParam(required = false) String[] sort,
        @RequestParam(required = false, name = "sort_type") String sortType) {

        List<Map<String, Object>> filteredRecords = jobService.findJobRecordWithCondition(jobTitle, gender, salary, sort, sortType, fields);

        return ResponseEntity.ok(filteredRecords);
    }
}