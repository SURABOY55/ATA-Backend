package com.example.AtaBackend.service.impl;

import com.example.AtaBackend.entity.JobRecord;
import com.example.AtaBackend.repository.JobRecordRepository;
import com.example.AtaBackend.service.JobService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRecordRepository jobRecordRepository;

    @Autowired
    private EntityManager entityManager;

    public List<Map<String, Object>> findJobRecordWithCondition(String jobTitle, String gender, String salary, String[] sort, String sortType, String[] fields) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<JobRecord> query = cb.createQuery(JobRecord.class);
        Root<JobRecord> root = query.from(JobRecord.class);

        List<Predicate> predicates = new ArrayList<>();

        //CASE 1 Filter by one or more
        if (jobTitle != null) {
            predicates.add(cb.equal(root.get("jobTitle"), jobTitle));
        }
        if (gender != null) {
            predicates.add(cb.equal(root.get("gender"), gender));
        }
        if (salary != null) {
            predicates.add(cb.equal(root.get("salary"), salary));
        }

        query.where(predicates.toArray(new Predicate[0]));

        // CASE 3 Sort by one or more
        if (sort != null && sort.length > 0) {
            List<Order> orders = new ArrayList<>();
            for (String s : sort) {
                if ("DESC".equalsIgnoreCase(sortType)) {
                    orders.add(cb.desc(root.get(s)));
                } else {
                    orders.add(cb.asc(root.get(s)));
                }
            }
            query.orderBy(orders);
        }
        List<JobRecord> jobRecords = entityManager.createQuery(query).getResultList();
        return jobRecords.stream()
            .map(record -> filterFields(record, fields))
            .collect(Collectors.toList());
    }

    //CASE 2 Filter by a sparse fields
    private Map<String, Object> filterFields(JobRecord jobRecord, String[] fields) {
        Map<String, Object> jobRecordMap = new HashMap<>();
        jobRecordMap.put("timestamp", jobRecord.getTimestamp());
        jobRecordMap.put("employer", jobRecord.getEmployer());
        jobRecordMap.put("location", jobRecord.getLocation());
        jobRecordMap.put("job_title", jobRecord.getJobTitle());
        jobRecordMap.put("years_at_employer", jobRecord.getYearsAtEmployer());
        jobRecordMap.put("years_of_experience", jobRecord.getYearsOfExperience());
        jobRecordMap.put("salary", jobRecord.getSalary());
        jobRecordMap.put("signing_bonus", jobRecord.getSigningBonus());
        jobRecordMap.put("annual_bonus", jobRecord.getAnnualBonus());
        jobRecordMap.put("annual_stock_value/bonus", jobRecord.getAnnualStockValueBonus());
        jobRecordMap.put("gender", jobRecord.getGender());
        jobRecordMap.put("additional_comments", jobRecord.getAdditionalComments());

        if (fields == null || fields.length == 0) {
            return jobRecordMap;
        }
        return jobRecordMap.entrySet().stream()
            .filter(entry -> List.of(fields).contains(entry.getKey()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
