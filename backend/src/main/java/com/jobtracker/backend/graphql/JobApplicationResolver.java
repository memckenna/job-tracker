package com.jobtracker.backend.graphql;

import com.jobtracker.backend.model.JobApplication;
import com.jobtracker.backend.repository.JobApplicationRepository;
import com.jobtracker.backend.security.CurrentUserProvider;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class JobApplicationResolver {

    private final JobApplicationRepository appRepo;
    private final CurrentUserProvider currentUser;

    @PreAuthorize("isAutheticated()")
    //For this to work, I need to enable method
    // security in project MethodSecurityConfig.java
    @QueryMapping
    public List<JobApplication> getApplications() {
        String userId = currentUser.getCurrentUserId();
        return appRepo.findByUserId(userId);
    }

    @PreAuthorize("isAuthenticated()")
    @MutationMapping
    public JobApplication createApplication(@Argument ApplicationInput input) {
        String userId = currentUser.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("Unauthorized");
        }

        JobApplication app = JobApplication.builder()
                .company(input.getCompany())
                .position(input.getPosition())
                .status(input.getStatus())
                .notes(input.getNotes())
                .deadline(input.getDeadline())
                .userId(userId)
                .build();

        return appRepo.save(app);
    }

    // GraphQL Input DTO
    @Data
    public static class ApplicationInput {
        private String position;
        private String company;
        private String status;
        private String notes;
        private String deadline;
    }
}
