package dev.dwidi.jobportal.repository;

import dev.dwidi.jobportal.entity.Job;
import dev.dwidi.jobportal.entity.enums.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByStatus(JobStatus status);
}
