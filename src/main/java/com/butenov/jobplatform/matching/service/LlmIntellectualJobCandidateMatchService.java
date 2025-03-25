package com.butenov.jobplatform.matching.service;

import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.matching.dto.JobCandidateMatchDto;

public interface LlmIntellectualJobCandidateMatchService
{
	JobCandidateMatchDto calculateLlmIntellectualMatch(Job job, Candidate candidate);
}
