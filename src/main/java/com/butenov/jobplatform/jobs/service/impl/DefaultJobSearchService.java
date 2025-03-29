package com.butenov.jobplatform.jobs.service.impl;

import java.util.Comparator;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.jobs.dto.JobIntellectualSearchResult;
import com.butenov.jobplatform.jobs.dto.JobSearchCriteria;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.jobs.repository.JobRepository;
import com.butenov.jobplatform.jobs.repository.JobSpecifications;
import com.butenov.jobplatform.jobs.service.JobSearchService;
import com.butenov.jobplatform.matching.service.JobCandidateMatchService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class DefaultJobSearchService implements JobSearchService
{
	private final JobRepository jobRepository;
	private final JobCandidateMatchService jobCandidateMatchService;

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

	@Override
	public Page<JobIntellectualSearchResult> findMostFittingJobs(final JobSearchCriteria criteria, final Candidate candidate,
	                                                             final Pageable pageable)
	{
		final Specification<Job> spec = JobSpecifications.withFilters(
				criteria.getTitle(),
				criteria.getLocation(),
				criteria.getMinSalary(),
				criteria.getMaxSalary(),
				criteria.getRequiredSkillIds(),
				criteria.getCompanyIds()
		);

		final Page<Job> preFilteredJobs = jobRepository.findRelevantJobs(candidate.getSkills(), spec, pageable);

		final List<Job> topJobs = preRankJobs(preFilteredJobs.getContent(), candidate);

		topJobs.forEach(job -> jobCandidateMatchService.getJobMatchScore(job, candidate));

		final List<JobIntellectualSearchResult> topJobsWithMatch = topJobs.stream()
		                                                                  .map(job -> new JobIntellectualSearchResult(job,
				                                                                  jobCandidateMatchService.getJobMatchScore(job,
						                                                                  candidate)))
		                                                                  .sorted(Comparator.<JobIntellectualSearchResult>comparingDouble(
				                                                                  job -> job.getJobCandidateMatch()
				                                                                            .getMatchScore()).reversed())
		                                                                  .toList();

		return new PageImpl<>(topJobsWithMatch, pageable, preFilteredJobs.getTotalElements());
	}

	private List<Job> preRankJobs(final List<Job> jobs, final Candidate candidate)
	{
		return jobs.stream()
		           .sorted(Comparator.<Job>comparingDouble(job -> preliminaryScore(job, candidate)).reversed())
		           .toList();
	}

	private double preliminaryScore(final Job job, final Candidate candidate)
	{
		final long matchingSkills = job.getRequiredSkills().stream()
		                               .filter(candidate.getSkills()::contains)
		                               .count();

		return job.getRequiredSkills().isEmpty() ? 0 : (double) matchingSkills / job.getRequiredSkills().size();
	}
}
