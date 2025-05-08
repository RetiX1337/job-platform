package com.butenov.jobplatform.jobapplications.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.butenov.jobplatform.companies.model.Company;
import com.butenov.jobplatform.jobapplications.model.JobApplication;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.candidates.model.Candidate;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long>
{
	boolean existsByJobAndCandidate(Job job, Candidate candidate);

	List<JobApplication> findByJobId(Long jobId);

	List<JobApplication> findByCandidateId(Long candidateId);

	@Query("SELECT a FROM JobApplication a " +
			"WHERE a.job.company = :company " +
			"ORDER BY a.createdAt DESC")
	Page<JobApplication> findByCompanyOrderByCreatedAtDesc(Company company, Pageable pageable);
}
