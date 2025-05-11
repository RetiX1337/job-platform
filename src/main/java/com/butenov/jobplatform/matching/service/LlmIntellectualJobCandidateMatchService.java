package com.butenov.jobplatform.matching.service;

import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.matching.dto.CandidateMatchingDto;
import com.butenov.jobplatform.matching.dto.JobCandidateMatchDto;
import com.butenov.jobplatform.matching.dto.JobMatchingDto;

public interface LlmIntellectualJobCandidateMatchService
{
	JobCandidateMatchDto calculateLlmIntellectualMatch(String jobJson,
	                                                   String candidateJson);
}
