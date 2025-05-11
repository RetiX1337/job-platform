package com.butenov.jobplatform.matching.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.matching.model.JobCandidateMatch;

import jakarta.transaction.Transactional;

public interface JobCandidateMatchService
{
	JobCandidateMatch getJobMatchScore(Job job, Candidate candidate);

	@Transactional
	List<JobCandidateMatch> getJobMatchScores(List<Job> jobs, Candidate candidate);

	void delete(Job job);

	void delete(Candidate candidate);
}
