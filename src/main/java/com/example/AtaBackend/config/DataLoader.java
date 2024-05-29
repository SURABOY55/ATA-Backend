package com.example.AtaBackend.config;

import com.example.AtaBackend.entity.JobRecord;
import com.example.AtaBackend.repository.JobRecordRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class DataLoader implements CommandLineRunner {

    @Autowired
    private JobRecordRepository jobRecordRepository;

    @Value("classpath:salary_survey-3.json")
    private Resource resource;

    @Override
    public void run(String... args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<JobRecord>> typeReference = new TypeReference<>() {
        };
        List<JobRecord> jobRecords = mapper.readValue(resource.getInputStream(), typeReference);
        jobRecordRepository.saveAll(jobRecords);
        log.info("Import data to embedded database success!");
    }
}