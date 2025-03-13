package com.butenov.jobplatform.jobs.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.butenov.jobplatform.jobs.dto.JobSearchCriteria;
import com.butenov.jobplatform.jobs.model.Job;

public interface JobSearchService
{
	Page<Job> searchJobs(JobSearchCriteria criteria, Pageable pageable);
}
