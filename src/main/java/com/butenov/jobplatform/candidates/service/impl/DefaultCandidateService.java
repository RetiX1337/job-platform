package com.butenov.jobplatform.candidates.service.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.butenov.jobplatform.candidates.dto.CandidateProfileEditingDto;
import com.butenov.jobplatform.candidates.dto.LlmCvProcessingDto;
import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.candidates.model.CandidateProfile;
import com.butenov.jobplatform.candidates.repository.CandidateRepository;
import com.butenov.jobplatform.candidates.service.CandidateService;
import com.butenov.jobplatform.candidates.service.LlmCvProcessingService;
import com.butenov.jobplatform.commons.files.service.FileService;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.matching.service.JobCandidateMatchService;
import com.butenov.jobplatform.skills.model.Skill;
import com.butenov.jobplatform.skills.repository.SkillRepository;
import com.butenov.jobplatform.skills.service.SkillService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class DefaultCandidateService implements CandidateService
{
	public static final String ENTITY_NOT_FOUND_EXCEPTION = "Candidate with id %d not found";
	private final FileService fileService;
	private final CandidateRepository candidateRepository;
	private final SkillRepository skillRepository;
	private final SkillService skillService;
	private final LlmCvProcessingService llmCvProcessingService;
	private final JobCandidateMatchService jobCandidateMatchService;

	@Override
	public Candidate findById(final Long id)
	{
		return candidateRepository.findById(id)
		                          .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND_EXCEPTION.formatted(id)));
	}

	@Override
	public void save(final Candidate candidate)
	{
		candidateRepository.save(candidate);
	}

	@Override
	public void uploadCV(final Candidate candidate, final MultipartFile cvFile)
	{
		try
		{
			final String cvLink = fileService.storeFile(cvFile);
			candidate.setCvLink(cvLink);
			save(candidate);
		}
		catch (final IOException e)
		{
			throw new RuntimeException("Failed to upload CV", e);
		}
	}

	@Transactional
	@Override
	public Candidate updateCandidateFromCV(final Candidate candidate)
	{
		final LlmCvProcessingDto parsedData = llmCvProcessingService.processCv(candidate.getCvLink());

		if (parsedData == null)
		{
			throw new RuntimeException("Failed to retrieve structured data from LLM");
		}

		if (parsedData.getJobExperiences() != null)
		{
			candidate.getCandidateProfile().getJobExperiences().clear();
			parsedData.getJobExperiences()
			          .forEach(jobExperience -> jobExperience.setCandidateProfile(candidate.getCandidateProfile()));
			candidate.getCandidateProfile().getJobExperiences().addAll(parsedData.getJobExperiences());
		}

		if (parsedData.getEducations() != null)
		{
			candidate.getCandidateProfile().getEducations().clear();
			parsedData.getEducations()
			          .forEach(education -> education.setCandidateProfile(candidate.getCandidateProfile()));
			candidate.getCandidateProfile().getEducations().addAll(parsedData.getEducations());
		}

		final List<Skill> matchedSkills = parsedData.getSkills().stream().map(skillRepository::findBySimilarName)
		                                            .filter(Optional::isPresent)
		                                            .map(Optional::get)
		                                            .toList();
		candidate.getCandidateProfile().setSkills(new HashSet<>(matchedSkills));

		return update(candidate);
	}

	@Transactional
	@Override
	public Candidate update(final Candidate candidate)
	{
		final long id = candidate.getId();
		if (candidateRepository.existsById(id))
		{
			return candidateRepository.save(candidate);
		}
		throw new EntityNotFoundException(ENTITY_NOT_FOUND_EXCEPTION.formatted(id));
	}

	@Transactional
	@Override
	public void toggleJobBookmark(final Candidate candidate, final Job job)
	{
		final Set<Job> savedJobs = candidate.getBookmarkedJobs();
		if (savedJobs.contains(job))
		{
			savedJobs.remove(job);
		}
		else
		{
			savedJobs.add(job);
		}
		update(candidate);
	}

	@Transactional
	@Override
	public void updateCandidateProfile(final Candidate candidate, final CandidateProfileEditingDto candidateProfileEditingDto)
	{
		final CandidateProfile currentCandidateProfile = candidate.getCandidateProfile();
		currentCandidateProfile.getJobExperiences().clear();
		if (candidateProfileEditingDto.getJobExperienceList() != null)
		{
			candidateProfileEditingDto.getJobExperienceList()
			                          .forEach(jobExperience -> jobExperience.setCandidateProfile(currentCandidateProfile));
			currentCandidateProfile.getJobExperiences().addAll(candidateProfileEditingDto.getJobExperienceList());
		}

		currentCandidateProfile.getEducations().clear();
		if (candidateProfileEditingDto.getEducationList() != null)
		{
			candidateProfileEditingDto.getEducationList()
			                          .forEach(education -> education.setCandidateProfile(currentCandidateProfile));
			currentCandidateProfile.getEducations().addAll(candidateProfileEditingDto.getEducationList());
		}

		if (candidateProfileEditingDto.getSkillIds() != null)
		{
			currentCandidateProfile.getSkills().clear();
			candidateProfileEditingDto.getSkillIds()
			                          .forEach(skillId -> currentCandidateProfile.getSkills().add(skillService.findById(skillId)));
		}
		jobCandidateMatchService.delete(candidate);
		update(candidate);
	}
}
