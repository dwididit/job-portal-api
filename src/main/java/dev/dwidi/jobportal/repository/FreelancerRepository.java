package dev.dwidi.jobportal.repository;

import dev.dwidi.jobportal.entity.Freelancer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FreelancerRepository extends JpaRepository<Freelancer, Long> {
    Optional<Freelancer> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
