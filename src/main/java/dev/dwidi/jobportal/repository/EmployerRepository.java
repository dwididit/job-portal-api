package dev.dwidi.jobportal.repository;

import dev.dwidi.jobportal.entity.Employer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, Long> {
    Optional<Employer> findByEmployerId (Long id);
    Optional<Employer> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
