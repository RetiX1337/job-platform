package com.butenov.jobplatform.jobapplications.service;

import java.util.List;

import com.butenov.jobplatform.jobapplications.model.JobApplication;

public interface JobApplicationService
{
	void save(final JobApplication jobApplication);

	JobApplication findById(final Long id);

	List<JobApplication> findAll();

	void deleteById(final Long id);
}
