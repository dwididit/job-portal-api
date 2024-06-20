package dev.dwidi.jobportal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Employer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employerId;

    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    private String password;

    private String companyName;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    private String userRole;

    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Job> jobs;

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
