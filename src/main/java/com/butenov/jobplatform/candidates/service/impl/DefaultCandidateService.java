package com.butenov.jobplatform.candidates.service.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.butenov.jobplatform.candidates.dto.LlmCvProcessingDto;
import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.candidates.repository.CandidateRepository;
import com.butenov.jobplatform.candidates.service.CandidateService;
import com.butenov.jobplatform.candidates.service.LlmCvProcessingService;
import com.butenov.jobplatform.commons.files.service.FileService;
import com.butenov.jobplatform.matching.service.JobCandidateMatchService;
import com.butenov.jobplatform.skills.model.Skill;
import com.butenov.jobplatform.skills.repository.SkillRepository;

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
	private final LlmCvProcessingService llmCvProcessingService;
	private final JobCandidateMatchService jobCandidateMatchService;

	@Override
	public Candidate findById(final Long id)
	{
		return candidateRepository.findById(id)
		                          .orElseThrow(() -> new EntityNotFoundException("Candidate with id %d not found".formatted(id)));
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
			candidate.getJobExperiences().clear();
			parsedData.getJobExperiences()
			          .forEach(jobExperience -> jobExperience.setCandidate(candidate));
			candidate.getJobExperiences().addAll(parsedData.getJobExperiences());
		}

		if (parsedData.getEducations() != null)
		{
			candidate.getEducations().clear();
			parsedData.getEducations()
			          .forEach(education -> education.setCandidate(candidate));
			candidate.getEducations().addAll(parsedData.getEducations());
		}

		final List<Skill> matchedSkills = parsedData.getSkills().stream().map(skillRepository::findBySimilarName)
		                                            .filter(Optional::isPresent)
		                                            .map(Optional::get)
		                                            .toList();
		candidate.setSkills(new HashSet<>(matchedSkills));

		return update(candidate);
	}

	@Transactional
	@Override
	public Candidate update(final Candidate candidate)
	{
		final long id = candidate.getId();
		if (candidateRepository.existsById(id))
		{
			jobCandidateMatchService.delete(candidate);
			return candidateRepository.save(candidate);
		}
		throw new EntityNotFoundException(ENTITY_NOT_FOUND_EXCEPTION.formatted(id));
	}
}
