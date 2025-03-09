package com.butenov.jobplatform.jobapplications.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.butenov.jobplatform.jobapplications.model.JobApplication;
import com.butenov.jobplatform.jobapplications.repository.JobApplicationRepository;
import com.butenov.jobplatform.jobapplications.service.JobApplicationService;

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
}
