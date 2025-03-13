package com.butenov.jobplatform.jobapplications.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.butenov.jobplatform.jobapplications.model.JobApplication;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.users.model.Candidate;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long>
{
	boolean existsByJobAndCandidate(Job job, Candidate candidate);
}
