package com.butenov.jobplatform.candidates.service.impl;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.candidates.repository.CandidateRepository;
import com.butenov.jobplatform.candidates.service.CandidateService;
import com.butenov.jobplatform.commons.files.service.FileService;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class DefaultCandidateService implements CandidateService
{
	private final FileService fileService;
	private final CandidateRepository candidateRepository;

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
}
