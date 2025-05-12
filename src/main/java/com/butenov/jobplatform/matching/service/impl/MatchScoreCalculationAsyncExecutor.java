package com.butenov.jobplatform.matching.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.candidates.repository.CandidateRepository;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.jobs.repository.JobRepository;
import com.butenov.jobplatform.matching.dto.CandidateMatchingDto;
import com.butenov.jobplatform.matching.dto.JobCandidateMatchDto;
import com.butenov.jobplatform.matching.dto.JobMatchingDto;
import com.butenov.jobplatform.matching.model.JobCandidateMatch;
import com.butenov.jobplatform.matching.repository.JobCandidateMatchRepository;
import com.butenov.jobplatform.matching.service.LlmIntellectualJobCandidateMatchService;
import com.butenov.jobplatform.skills.model.Skill;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class MatchScoreCalculationAsyncExecutor
{
	private final JobRepository jobRepository;
	private final CandidateRepository candidateRepository;
	private final JobCandidateMatchRepository jobCandidateMatchRepository;
	private final LlmIntellectualJobCandidateMatchService llmIntellectualJobCandidateMatchService;
	private final ObjectMapper objectMapper;

	@Async("llmExecutor")
	@Transactional
	public CompletableFuture<JobCandidateMatch> calculateAndStoreMatchScore(final JobMatchingDto jobMatchingDto,
	                                                                        final CandidateMatchingDto candidateMatchingDto)
	{
		try
		{
			final Job job = jobRepository.findById(jobMatchingDto.getId())
			                             .orElseThrow(() -> new IllegalArgumentException("Job not found"));
			final Candidate candidate = candidateRepository.findById(candidateMatchingDto.getId())
			                                               .orElseThrow(
					                                               () -> new IllegalArgumentException("Candidate not found"));
			final String jobJson = objectMapper.writeValueAsString(jobMatchingDto);
			final String candidateJson = objectMapper.writeValueAsString(candidateMatchingDto);

			// (re-check cache inside transaction to avoid races)
			return CompletableFuture.supplyAsync(() -> jobCandidateMatchRepository.findByJobAndCandidate(job, candidate)
			                                                                      .orElseGet(() -> {
				                                                                      final double skillMatch = calculateSkillMatch(
						                                                                      jobMatchingDto, candidateMatchingDto);
				                                                                      final JobCandidateMatchDto intellectual = llmIntellectualJobCandidateMatchService.calculateLlmIntellectualMatch(
						                                                                      jobJson, candidateJson);

				                                                                      final double score = skillMatch * 0.4
						                                                                      + intellectual.getExperienceMatch() * 0.6;

				                                                                      final JobCandidateMatch entity = new JobCandidateMatch(
						                                                                      job, candidate, score,
						                                                                      intellectual.getJustification());
				                                                                      return jobCandidateMatchRepository.save(
						                                                                      entity);
			                                                                      }));
		}
		catch (final IOException ioException)
		{
			throw new RuntimeException("Failed to convert job or candidate to JSON", ioException);
		}
	}

	private double calculateSkillMatch(final JobMatchingDto jobMatchingDto, final CandidateMatchingDto candidateMatchingDto)
	{
		final List<Skill> jobSkills = jobMatchingDto.getRequiredSkills();
		final List<Skill> candidateSkills = candidateMatchingDto.getSkills();

		if (jobSkills == null || jobSkills.isEmpty())
		{
			return 0.0;
		}

		if (candidateSkills == null || candidateSkills.isEmpty())
		{
			return 0.0;
		}

		final long matchingSkills = candidateSkills.stream()
		                                           .filter(jobSkills::contains)
		                                           .count();

		return (double) matchingSkills / jobSkills.size();
	}
}
