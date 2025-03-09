package com.butenov.jobplatform.jobs.service.impl;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

import java.util.List;

import org.springframework.stereotype.Service;

import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.jobs.repository.JobRepository;
import com.butenov.jobplatform.jobs.service.JobService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class DefaultJobService implements JobService
{
	public static final String ENTITY_NOT_FOUND_EXCEPTION = "Job with id %d not found";
	private final JobRepository jobRepository;

	public DefaultJobService(final JobRepository jobRepository)
	{
		this.jobRepository = jobRepository;
	}

	@Transactional
	@Override
	public Job save(final Job job)
	{
		return jobRepository.save(job);
	}

	@Override
	public Job findById(final Long id)
	{
		return jobRepository.findById(id)
		                    .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND_EXCEPTION.formatted(id)));
	}

	@Override
	public List<Job> findAll()
	{
		return jobRepository.findAll();
	}

	@Transactional
	@Override
	public void deleteById(final Long id)
	{
		jobRepository.delete(findById(id));
	}

	@Override
	public Job update(final Job job)
	{
		final long id = job.getId();
		if (jobRepository.existsById(id))
		{
			return jobRepository.save(job);
		}
		throw new EntityNotFoundException(ENTITY_NOT_FOUND_EXCEPTION.formatted(id));
	}
}
