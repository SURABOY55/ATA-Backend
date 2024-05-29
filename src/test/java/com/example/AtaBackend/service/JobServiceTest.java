package com.example.AtaBackend.service;

import com.example.AtaBackend.entity.JobRecord;
import com.example.AtaBackend.repository.JobRecordRepository;
import com.example.AtaBackend.service.impl.JobServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JobServiceTest {

    @Mock
    private JobRecordRepository jobRecordRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private JobServiceImpl jobService;

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
    void findJobRecordWithCondition_when_valid_parameter_then_return_result() {
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery<JobRecord> cq = mock(CriteriaQuery.class);
        Root<JobRecord> root = mock(Root.class);
        TypedQuery<JobRecord> tq = mock(TypedQuery.class);

        when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(JobRecord.class)).thenReturn(cq);
        when(cq.from(JobRecord.class)).thenReturn(root);
        when(entityManager.createQuery(cq)).thenReturn(tq);
        when(tq.getResultList()).thenReturn(Collections.singletonList(jobRecord));

        List<Map<String, Object>> result = jobService.findJobRecordWithCondition(
            "Engineer", "Male", "120000", new String[]{"jobTitle"}, "DESC", new String[]{"job_title", "gender", "salary"});

        assertEquals(1, result.size());
        Map<String, Object> record = result.get(0);
        assertEquals("Engineer", record.get("job_title"));
        assertEquals("120000", record.get("salary"));
        assertEquals("Male", record.get("gender"));

        verify(entityManager, times(1)).getCriteriaBuilder();
        verify(cb, times(1)).createQuery(JobRecord.class);
        verify(cq, times(1)).from(JobRecord.class);
        verify(tq, times(1)).getResultList();
    }

    @Test
    void findJobRecordWithCondition_when_nullCriteriaBuilder_then_throws() {
        when(entityManager.getCriteriaBuilder()).thenReturn(null);

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            jobService.findJobRecordWithCondition(
                "Engineer", "Male", "120000", new String[]{"jobTitle"}, "DESC", new String[]{"job_title", "gender", "salary"});
        });

        assertNotNull(exception);

        verify(entityManager, times(1)).getCriteriaBuilder();
    }
}
