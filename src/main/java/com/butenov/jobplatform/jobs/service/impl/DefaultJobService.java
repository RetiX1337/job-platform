package com.butenov.jobplatform.jobs.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.butenov.jobplatform.companies.model.Company;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.jobs.repository.JobRepository;
import com.butenov.jobplatform.jobs.service.JobService;
import com.butenov.jobplatform.matching.service.JobCandidateMatchService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DefaultJobService implements JobService
{
	public static final String ENTITY_NOT_FOUND_EXCEPTION = "Job with id %d not found";
	private final JobRepository jobRepository;
	private final JobCandidateMatchService jobCandidateMatchService;

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
	public List<Job> findAllByCompany(final Company company)
	{
		return jobRepository.findAllByCompany(company);
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

	@Transactional
	@Override
	public Job update(final Job job)
	{
		final long id = job.getId();
		if (jobRepository.existsById(id))
		{
			jobCandidateMatchService.delete(job);
			return jobRepository.save(job);
		}
		throw new EntityNotFoundException(ENTITY_NOT_FOUND_EXCEPTION.formatted(id));
	}

	@Override
	public Page<Job> findLatestJobsForCompany(final Company company, final Pageable pageable)
	{
		return jobRepository.findByCompanyOrderByCreatedAtDesc(company, pageable);
	}
}
