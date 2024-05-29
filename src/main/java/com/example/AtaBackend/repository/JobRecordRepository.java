package com.example.AtaBackend.repository;

import com.example.AtaBackend.entity.JobRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRecordRepository extends JpaRepository<JobRecord, Long> {
}
