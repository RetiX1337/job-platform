package com.butenov.jobplatform.jobs.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.butenov.jobplatform.skills.model.Skill;

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
		final Set<Long> skillIds = candidate.getCandidateProfile().getSkills().stream()
		                                    .map(Skill::getId).collect(Collectors.toSet());

		final Page<Job> page = jobRepository.findBySkillsAndFiltersOrdered(
				skillIds,
				criteria.getTitle(),
				criteria.getLocation(),
				criteria.getMinSalary(),
				criteria.getMaxSalary(),
				criteria.getCompanyIds(),
				pageable
		);

		final List<Job> topJobs = preRankJobs(page.getContent(), candidate);

		final List<JobIntellectualSearchResult> topJobsWithMatch = jobCandidateMatchService.getJobMatchScores(topJobs, candidate).stream()
		                                                                                .map(JobIntellectualSearchResult::new)
		                                                                                .sorted(Comparator.<JobIntellectualSearchResult>comparingDouble(
				                                                                                job -> job.getJobCandidateMatch()
				                                                                                          .getMatchScore()).reversed())
		                                                                                .toList();

		return new PageImpl<>(topJobsWithMatch, pageable, page.getTotalElements());
	}

	private List<Job> preRankJobs(final List<Job> jobs, final Candidate candidate)
	{
		return jobs.stream().sorted(Comparator.<Job>comparingDouble(job -> preliminaryScore(job, candidate)).reversed()).toList();
	}

	private double preliminaryScore(final Job job, final Candidate candidate)
	{
		final long matchingSkills = job.getRequiredSkills().stream().filter(candidate.getCandidateProfile().getSkills()::contains).count();

		return job.getRequiredSkills().isEmpty() ? 0 : (double) matchingSkills / job.getRequiredSkills().size();
	}
}
