package com.butenov.jobplatform.jobs.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.butenov.jobplatform.companies.model.Company;
import com.butenov.jobplatform.jobs.model.Job;

public interface JobService
{
	Job save(final Job job);

	Job findById(final Long id);

	List<Job> findAllByCompany(Company company);

	List<Job> findAll();

	void deleteById(final Long id);

	Job update(final Job job);

	Page<Job> findLatestJobsForCompany(Company company, Pageable pageable);
}

