package com.example.AtaBackend.controller;

import com.example.AtaBackend.entity.JobRecord;
import com.example.AtaBackend.service.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(JobController.class)
public class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobService jobService;

    private JobRecord jobRecord;

    @BeforeEach
    void setUp() {
        jobRecord = new JobRecord();
        jobRecord.setId(1L);
        jobRecord.setJobTitle("Engineer");
        jobRecord.setSalary("120000");
        jobRecord.setGender("Male");
    }

    @Test
    void getJobData_when_valid_parameters_then_return_list_jobRecord() throws Exception {
        Map<String, Object> jobRecordMap = new HashMap<>();
        jobRecordMap.put("job_title", "Engineer");
        jobRecordMap.put("salary", "120000");
        jobRecordMap.put("gender", "Male");

        List<Map<String, Object>> jobRecords = Collections.singletonList(jobRecordMap);
        when(jobService.findJobRecordWithCondition(any(), any(), any(), any(), any(), any())).thenReturn(jobRecords);

        mockMvc.perform(get("/job_data")
                .param("job_title", "Engineer")
                .param("gender", "Male")
                .param("salary", "120000")
                .param("fields", "job_title,salary,gender")
                .param("sort", "jobTitle")
                .param("sort_type", "DESC"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].job_title", is("Engineer")))
            .andExpect(jsonPath("$[0].salary", is("120000")))
            .andExpect(jsonPath("$[0].gender", is("Male")));
    }

    @Test
    void getJobData_when_no_parameters_return_list_jobRecord() throws Exception {
        List<Map<String, Object>> jobRecords = new ArrayList<>();
        when(jobService.findJobRecordWithCondition(any(), any(), any(), any(), any(), any())).thenReturn(jobRecords);

        mockMvc.perform(get("/job_data"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }
}