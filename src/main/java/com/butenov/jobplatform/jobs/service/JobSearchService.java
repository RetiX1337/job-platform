package com.butenov.jobplatform.jobs.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.jobs.dto.JobIntellectualSearchResult;
import com.butenov.jobplatform.jobs.dto.JobSearchCriteria;
import com.butenov.jobplatform.jobs.model.Job;

public interface JobSearchService
{
	Page<Job> searchJobs(JobSearchCriteria criteria, Pageable pageable);

	Page<JobIntellectualSearchResult> findMostFittingJobs(JobSearchCriteria criteria, Candidate candidate, Pageable pageable);
}
