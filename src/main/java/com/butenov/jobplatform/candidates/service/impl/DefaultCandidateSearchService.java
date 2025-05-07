package com.butenov.jobplatform.candidates.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.butenov.jobplatform.candidates.dto.CandidateIntellectualSearchResult;
import com.butenov.jobplatform.candidates.dto.CandidateSearchCriteria;
import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.candidates.repository.CandidateRepository;
import com.butenov.jobplatform.candidates.repository.CandidateSpecifications;
import com.butenov.jobplatform.candidates.service.CandidateSearchService;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.matching.service.JobCandidateMatchService;
import com.butenov.jobplatform.skills.model.Skill;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class DefaultCandidateSearchService implements CandidateSearchService
{
	private final CandidateRepository candidateRepository;
	private final JobCandidateMatchService jobCandidateMatchService;

	@Override
	public Page<CandidateIntellectualSearchResult> findMostFittingCandidates(final CandidateSearchCriteria criteria,
	                                                                         final Job job,
	                                                                         final Pageable pageable)
	{
		final Specification<Candidate> sortByMatchingSkills = CandidateSpecifications.sortByMatchingSkills(
				job.getRequiredSkills().stream()
				   .map(Skill::getId)
				   .collect(Collectors.toSet()));

		final Specification<Candidate> spec = CandidateSpecifications.withFilters(criteria).and(sortByMatchingSkills);

		final Page<Candidate> preFilteredCandidates = candidateRepository.findAll(spec, pageable);

		final List<Candidate> topCandidates = preRankCandidates(preFilteredCandidates.getContent(), job);

		topCandidates.forEach(candidate -> jobCandidateMatchService.getJobMatchScore(job, candidate));

		final List<CandidateIntellectualSearchResult> topCandidatesWithMatch = topCandidates.stream()
		                                                                              .map(candidate -> new CandidateIntellectualSearchResult(
				                                                                              candidate,
				                                                                              jobCandidateMatchService.getJobMatchScore(
						                                                                              job,
						                                                                              candidate)))
		                                                                              .sorted(Comparator.<CandidateIntellectualSearchResult>comparingDouble(
				                                                                                                candidate -> candidate.getJobCandidateMatch()
				                                                                                                                      .getMatchScore())
		                                                                                                .reversed())
		                                                                              .toList();

		return new PageImpl<>(topCandidatesWithMatch, pageable, preFilteredCandidates.getTotalElements());
	}

	private List<Candidate> preRankCandidates(final List<Candidate> candidates, final Job job)
	{
		return candidates.stream()
		                 .sorted(Comparator.<Candidate>comparingDouble(candidate -> preliminaryScore(candidate, job)).reversed())
		                 .toList();
	}

	private double preliminaryScore(final Candidate candidate, final Job job)
	{
		final long matchingSkills = candidate.getCandidateProfile().getSkills()
		                                     .stream()
		                                     .filter(candidate.getCandidateProfile().getSkills()::contains)
		                                     .count();

		return job.getRequiredSkills().isEmpty() ? 0 : (double) matchingSkills / job.getRequiredSkills().size();
	}
}
