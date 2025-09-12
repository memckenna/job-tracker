package com.jobtracker.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.jobtracker.backend.model.JobApplication;

import java.util.List;

public interface JobApplicationRepository extends MongoRepository<JobApplication, String> {
    List<JobApplication> findByUserId(String userId);
}
