package com.butenov.jobplatform.jobapplications.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.butenov.jobplatform.jobapplications.model.JobApplication;
import com.butenov.jobplatform.jobapplications.repository.JobApplicationRepository;
import com.butenov.jobplatform.jobapplications.service.JobApplicationService;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.recruiters.model.Recruiter;

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
		application.setStatus(JobApplication.Status.SUBMITTED);

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
	public List<JobApplication> findByJobId(final Long jobId)
	{
		return jobApplicationRepository.findByJobId(jobId);
	}

	@Override
	public List<JobApplication> findByCandidateId(final Long candidateId)
	{
		return jobApplicationRepository.findByCandidateId(candidateId);
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

	@Override
	public Page<JobApplication> findLatestApplicationsForRecruiter(final Recruiter recruiter, final Pageable pageable)
	{
		return jobApplicationRepository.findByCompanyOrderByCreatedAtDesc(recruiter.getCompany(), pageable);
	}

}
