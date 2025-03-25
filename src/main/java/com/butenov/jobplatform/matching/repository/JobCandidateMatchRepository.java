package com.butenov.jobplatform.matching.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.matching.model.JobCandidateMatch;

public interface JobCandidateMatchRepository extends JpaRepository<JobCandidateMatch, Long>
{
	Optional<JobCandidateMatch> findByJobAndCandidate(Job job, Candidate candidate);
}
