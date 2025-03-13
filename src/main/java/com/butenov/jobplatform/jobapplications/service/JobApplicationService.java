package com.butenov.jobplatform.jobapplications.service;

import java.util.List;

import com.butenov.jobplatform.jobapplications.model.JobApplication;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.users.model.Candidate;

public interface JobApplicationService
{
	void save(final JobApplication jobApplication);

	void applyForJob(final Job job, final Candidate candidate);

	JobApplication findById(final Long id);

	List<JobApplication> findAll();

	void deleteById(final Long id);

	boolean candidateHasAppliedForJob(final Job job, final Candidate candidate);
}
