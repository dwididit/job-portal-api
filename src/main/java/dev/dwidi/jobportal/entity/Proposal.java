package dev.dwidi.jobportal.entity;

import dev.dwidi.jobportal.entity.enums.ProposalStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long proposalId;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private ProposalStatus status;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "freelancer_id", nullable = false)
    private Freelancer freelancer;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @PrePersist
    protected void onCreate() {
        createdAt = ZonedDateTime.now(ZoneId.of("UTC"));
        updatedAt = ZonedDateTime.now(ZoneId.of("UTC"));
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = ZonedDateTime.now(ZoneId.of("UTC"));
    }
}
