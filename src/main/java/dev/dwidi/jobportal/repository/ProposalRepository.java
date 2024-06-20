package dev.dwidi.jobportal.repository;

import dev.dwidi.jobportal.entity.Freelancer;
import dev.dwidi.jobportal.entity.Job;
import dev.dwidi.jobportal.entity.Proposal;
import dev.dwidi.jobportal.entity.enums.ProposalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    boolean existsByFreelancerAndJob(Freelancer freelancer, Job job);

    List<Proposal> findByFreelancer(Freelancer freelancer);
    List<Proposal> findByStatus(ProposalStatus proposalStatus);
}
