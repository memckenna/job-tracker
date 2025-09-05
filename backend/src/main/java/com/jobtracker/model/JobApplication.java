package com.jobtracker.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "applications")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobApplication {
    @Id
    private String id;

    private String position;
    private String company;
    private String status;  // saved, applied, interviewing, rejected
    private String notes;
    private String deadline;

    private String userId; // Link to User
}
