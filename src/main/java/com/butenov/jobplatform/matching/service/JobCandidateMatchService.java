package com.butenov.jobplatform.matching.service;

import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.jobs.model.Job;

public interface JobCandidateMatchService
{
	void calculateAndStoreMatchScore(Job job, Candidate candidate);
}
