package com.butenov.jobplatform.jobs.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.butenov.jobplatform.jobs.dto.JobSearchCriteria;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.jobs.repository.JobRepository;
import com.butenov.jobplatform.jobs.repository.JobSpecifications;
import com.butenov.jobplatform.jobs.service.JobSearchService;

@Service
public class DefaultJobSearchService implements JobSearchService
{
	private final JobRepository jobRepository;

	public DefaultJobSearchService(final JobRepository jobRepository)
	{
		this.jobRepository = jobRepository;
	}

	@Override
	public Page<Job> searchJobs(final JobSearchCriteria criteria, final Pageable pageable)
	{
		final Specification<Job> spec = JobSpecifications.withFilters(
				criteria.getTitle(),
				criteria.getLocation(),
				criteria.getMinSalary(),
				criteria.getMaxSalary(),
				criteria.getRequiredSkillIds(),
				criteria.getCompanyIds()
		);
		return jobRepository.findAll(spec, pageable);
	}
}
