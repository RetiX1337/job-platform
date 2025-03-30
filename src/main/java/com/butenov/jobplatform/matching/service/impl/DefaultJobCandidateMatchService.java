package com.butenov.jobplatform.matching.service.impl;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.matching.dto.JobCandidateMatchDto;
import com.butenov.jobplatform.matching.model.JobCandidateMatch;
import com.butenov.jobplatform.matching.repository.JobCandidateMatchRepository;
import com.butenov.jobplatform.matching.service.JobCandidateMatchService;
import com.butenov.jobplatform.matching.service.LlmIntellectualJobCandidateMatchService;
import com.butenov.jobplatform.skills.model.Skill;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultJobCandidateMatchService implements JobCandidateMatchService
{
	private final JobCandidateMatchRepository jobCandidateMatchRepository;
	private final LlmIntellectualJobCandidateMatchService llmIntellectualJobCandidateMatchService;

	@Transactional
	@Override
	public JobCandidateMatch calculateAndStoreMatchScore(final Job job, final Candidate candidate)
	{
		final Optional<JobCandidateMatch> jobCandidateMatchOptional = jobCandidateMatchRepository.findByJobAndCandidate(job,
				candidate);
		if (jobCandidateMatchOptional.isPresent())
		{
			return jobCandidateMatchOptional.get();
		}
		final double skillMatch = calculateSkillMatch(job, candidate);
		final JobCandidateMatchDto llmIntellectualMatch = llmIntellectualJobCandidateMatchService.calculateLlmIntellectualMatch(
				job, candidate);
		final double locationMatch = calculateLocationMatch(job, candidate);

		final double matchScore = (skillMatch * 0.4) + (llmIntellectualMatch.getExperienceMatch() * 0.4) + (locationMatch * 0.2);

		return jobCandidateMatchRepository.save(
				new JobCandidateMatch(job, candidate, matchScore, llmIntellectualMatch.getJustification()));

	}

	@Transactional
	@Override
	public JobCandidateMatch getJobMatchScore(final Job job, final Candidate candidate)
	{
		return jobCandidateMatchRepository.findByJobAndCandidate(job, candidate)
		                                  .orElse(calculateAndStoreMatchScore(job, candidate));
	}

	@Transactional
	@Override
	public void delete(final Job job)
	{
		jobCandidateMatchRepository.deleteByJob(job);
	}

	@Transactional
	@Override
	public void delete(final Candidate candidate)
	{
		jobCandidateMatchRepository.deleteByCandidate(candidate);
	}

	private double calculateSkillMatch(final Job job, final Candidate candidate)
	{
		final Set<Skill> jobSkills = job.getRequiredSkills();
		final Set<Skill> candidateSkills = candidate.getCandidateProfile().getSkills();

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

	// TODO
	private double calculateLocationMatch(final Job job, final Candidate candidate)
	{
		return 1.0;
	}
}
