package com.butenov.jobplatform.matching.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.matching.model.JobCandidateMatch;
import com.butenov.jobplatform.matching.repository.JobCandidateMatchRepository;
import com.butenov.jobplatform.matching.service.JobCandidateMatchService;
import com.butenov.jobplatform.matching.service.LlmIntellectualJobCandidateMatchService;
import com.butenov.jobplatform.skills.model.Skill;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultJobCandidateMatchService implements JobCandidateMatchService
{
	private final JobCandidateMatchRepository jobCandidateMatchRepository;
	private final LlmIntellectualJobCandidateMatchService llmIntellectualJobCandidateMatchService;

	@Override
	public void calculateAndStoreMatchScore(final Job job, final Candidate candidate)
	{
		final double skillMatch = calculateSkillMatch(job, candidate);
		final double llmIntellectualMatch = calculateLlmIntellectualMatch(job, candidate);
		final double locationMatch = calculateLocationMatch(job, candidate);

		final double matchScore = (skillMatch * 0.4) + (llmIntellectualMatch * 0.4) + (locationMatch * 0.2);
		jobCandidateMatchRepository.findByJobAndCandidate(job, candidate)
		                           .ifPresentOrElse(
				                           jobCandidateMatch -> jobCandidateMatch.setMatchScore(matchScore),
				                           () -> jobCandidateMatchRepository.save(
						                           new JobCandidateMatch(job, candidate, matchScore)));
	}

	private double calculateSkillMatch(final Job job, final Candidate candidate)
	{
		final Set<Skill> jobSkills = job.getRequiredSkills();
		final Set<Skill> candidateSkills = candidate.getSkills();

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

	private double calculateLlmIntellectualMatch(final Job job, final Candidate candidate)
	{
		return 0;
	}

	// TODO
	private double calculateLocationMatch(final Job job, final Candidate candidate)
	{
		return 0;
	}
}
