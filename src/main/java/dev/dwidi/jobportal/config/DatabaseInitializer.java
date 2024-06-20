package dev.dwidi.jobportal.config;

import dev.dwidi.jobportal.entity.Employer;
import dev.dwidi.jobportal.entity.Freelancer;
import dev.dwidi.jobportal.repository.EmployerRepository;
import dev.dwidi.jobportal.repository.FreelancerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private FreelancerRepository freelancerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (employerRepository.count() == 0) {
            // Insert Admin user
            Employer admin = new Employer();
            admin.setName("Admin User");
            admin.setEmail("admin@example.com");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("password123"));
            admin.setCompanyName("Admin Company");
            admin.setUserRole("ROLE_ADMIN");
            employerRepository.save(admin);

            // Insert Employer user
            Employer employer = new Employer();
            employer.setName("Employer User");
            employer.setEmail("employer@example.com");
            employer.setUsername("employer");
            employer.setPassword(passwordEncoder.encode("password123"));
            employer.setCompanyName("Employer Company");
            employer.setUserRole("ROLE_EMPLOYER");
            employerRepository.save(employer);
        }

        if (freelancerRepository.count() == 0) {
            // Insert Freelancer user
            Freelancer freelancer = new Freelancer();
            freelancer.setName("Freelancer User");
            freelancer.setEmail("freelancer@example.com");
            freelancer.setUsername("freelancer");
            freelancer.setPassword(passwordEncoder.encode("password123"));
            freelancer.setSkills("Java, Spring Boot");
            freelancer.setUserRole("ROLE_FREELANCER");
            freelancerRepository.save(freelancer);
        }
    }
}
