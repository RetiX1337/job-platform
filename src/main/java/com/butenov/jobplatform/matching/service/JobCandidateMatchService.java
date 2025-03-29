package com.butenov.jobplatform.matching.service;

import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.matching.model.JobCandidateMatch;

public interface JobCandidateMatchService
{
	JobCandidateMatch calculateAndStoreMatchScore(Job job, Candidate candidate);

	JobCandidateMatch getJobMatchScore(Job job, Candidate candidate);
}
