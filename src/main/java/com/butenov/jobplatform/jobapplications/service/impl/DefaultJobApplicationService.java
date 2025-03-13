package com.butenov.jobplatform.jobapplications.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.butenov.jobplatform.jobapplications.model.JobApplication;
import com.butenov.jobplatform.jobapplications.repository.JobApplicationRepository;
import com.butenov.jobplatform.jobapplications.service.JobApplicationService;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.users.model.Candidate;

import jakarta.persistence.EntityNotFoundException;

@Service
public class DefaultJobApplicationService implements JobApplicationService
{
	private final JobApplicationRepository jobApplicationRepository;

	public DefaultJobApplicationService(final JobApplicationRepository jobApplicationRepository)
	{
		this.jobApplicationRepository = jobApplicationRepository;
	}

	@Override
	public void save(final JobApplication jobApplication)
	{
		jobApplicationRepository.save(jobApplication);
	}

	@Override
	public void applyForJob(final Job job, final Candidate candidate)
	{
		if (candidateHasAppliedForJob(job, candidate))
		{
			throw new IllegalArgumentException("Candidate has already applied for this job");
		}

		final JobApplication application = new JobApplication();
		application.setCandidate(candidate);
		application.setJob(job);
		application.setStatus(JobApplication.Status.PENDING);

		save(application);
	}

	@Override
	public JobApplication findById(final Long id)
	{
		return jobApplicationRepository.findById(id)
		                               .orElseThrow(() -> new EntityNotFoundException(
				                               "Job application for id %d not found".formatted(id)));
	}

	@Override
	public List<JobApplication> findAll()
	{
		return jobApplicationRepository.findAll();
	}

	@Override
	public void deleteById(final Long id)
	{
		jobApplicationRepository.delete(findById(id));
	}

	@Override
	public boolean candidateHasAppliedForJob(final Job job, final Candidate candidate)
	{
		return jobApplicationRepository.existsByJobAndCandidate(job, candidate);
	}
}
