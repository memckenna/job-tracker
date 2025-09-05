package com.jobtracker.graphql;

import com.jobtracker.model.JobApplication;
import com.jobtracker.repository.JobApplicationRepository;
import com.jobtracker.security.CurrentUserProvider;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class JobApplicationResolver {

    private final JobApplicationRepository appRepo;
    private final CurrentUserProvider currentUser;

    @QueryMapping
    public List<JobApplication> getApplications() {
        String userId = currentUser.getCurrentUserId();
        return appRepo.findByUserId(userId);
    }

    @MutationMapping
    public JobApplication createApplication(@Argument ApplicationInput input) {
        String userId = currentUser.getCurrentUserId();

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
