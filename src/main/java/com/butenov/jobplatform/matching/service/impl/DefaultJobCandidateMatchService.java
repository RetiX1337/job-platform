package com.butenov.jobplatform.matching.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.matching.dto.CandidateMatchingDto;
import com.butenov.jobplatform.matching.dto.JobMatchingDto;
import com.butenov.jobplatform.matching.model.JobCandidateMatch;
import com.butenov.jobplatform.matching.repository.JobCandidateMatchRepository;
import com.butenov.jobplatform.matching.service.JobCandidateMatchService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultJobCandidateMatchService implements JobCandidateMatchService
{
	private final JobCandidateMatchRepository jobCandidateMatchRepository;
	private final MatchScoreCalculationAsyncExecutor matchScoreCalculationAsyncExecutor;
	private final ObjectMapper objectMapper;
	private final Map<String, CompletableFuture<JobCandidateMatch>> inFlight = new ConcurrentHashMap<>();

	@Override
	@Transactional
	public JobCandidateMatch getJobMatchScore(final Job job, final Candidate candidate)
	{
		try
		{
			return getJobMatchScoreAsync(job, candidate).get();
		}
		catch (final InterruptedException | ExecutionException e)
		{
			Thread.currentThread().interrupt();
			throw new IllegalStateException("Failed to calculate match score", e);
		}
	}

	@Override
	public List<JobCandidateMatch> getJobMatchScores(final List<Job> jobs, final Candidate candidate)
	{
		return resolveFutures(
				jobs.stream()
				    .map(job -> getJobMatchScoreAsync(job, candidate))
				    .toList()
		);
	}

	@Override
	public List<JobCandidateMatch> getJobMatchScores(final Job job, final List<Candidate> candidates)
	{
		return resolveFutures(
				candidates.stream()
				          .map(candidate -> getJobMatchScoreAsync(job, candidate))
				          .toList()
		);
	}

	@Transactional
	@Override
	public void delete(final Job job)
	{
		final String prefix = job.getId() + "-";
		inFlight.keySet().removeIf(k -> k.startsWith(prefix));
		jobCandidateMatchRepository.deleteByJob(job);
	}

	@Transactional
	@Override
	public void delete(final Candidate candidate)
	{
		final String suffix = "-" + candidate.getId();
		inFlight.keySet().removeIf(k -> k.endsWith(suffix));
		jobCandidateMatchRepository.deleteByCandidate(candidate);
	}

	private CompletableFuture<JobCandidateMatch> getJobMatchScoreAsync(final Job job, final Candidate candidate)
	{
		try
		{
			final String key = job.getId() + "-" + candidate.getId();
			final JobMatchingDto rawJobDto = JobMatchingDto.builder()
			                                               .id(job.getId())
			                                               .title(job.getTitle())
			                                               .description(job.getDescription())
			                                               .requirements(job.getRequirements())
			                                               .requiredSkills(new ArrayList<>(job.getRequiredSkills()))
			                                               .build();
			final CandidateMatchingDto rawCandidateDto = new CandidateMatchingDto(
					candidate.getId(), candidate.getCandidateProfile().getJobExperiences(),
					new ArrayList<>(candidate.getCandidateProfile().getSkills()),
					candidate.getCandidateProfile().getEducations());
			final JobMatchingDto jobMatchingDto = objectMapper.readValue(objectMapper.writeValueAsString(rawJobDto),
					JobMatchingDto.class);
			jobMatchingDto.setId(job.getId());
			final CandidateMatchingDto candidateMatchingDto = objectMapper.readValue(
					objectMapper.writeValueAsString(rawCandidateDto), CandidateMatchingDto.class);
			candidateMatchingDto.setId(candidate.getId());

			final Optional<JobCandidateMatch> stored = jobCandidateMatchRepository.findByJobAndCandidate(job, candidate);
			return stored.map(CompletableFuture::completedFuture).orElseGet(() -> inFlight.computeIfAbsent(key, k ->
					matchScoreCalculationAsyncExecutor.calculateAndStoreMatchScore(jobMatchingDto, candidateMatchingDto)
					                                  .whenComplete((res, ex) -> inFlight.remove(k))
			));
		}
		catch (final Exception e)
		{
			throw new RuntimeException("Failed to convert job or candidate to JSON", e);
		}
	}

	private List<JobCandidateMatch> resolveFutures(final List<CompletableFuture<JobCandidateMatch>> futures)
	{
		CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

		return futures.stream()
		              .map(future -> {
			              try
			              {
				              return future.get();
			              }
			              catch (final Exception e)
			              {
				              Thread.currentThread().interrupt();
				              throw new IllegalStateException("Failed to calculate match score", e);
			              }
		              })
		              .filter(Objects::nonNull)
		              .toList();
	}


}
