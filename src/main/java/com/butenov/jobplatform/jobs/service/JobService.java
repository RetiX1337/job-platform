package com.butenov.jobplatform.jobs.service;

import java.util.List;

import com.butenov.jobplatform.jobs.model.Job;

public interface JobService
{
	Job save(final Job job);

	Job findById(final Long id);

	List<Job> findAll();

	void deleteById(final Long id);

	Job update(final Job job);
}

