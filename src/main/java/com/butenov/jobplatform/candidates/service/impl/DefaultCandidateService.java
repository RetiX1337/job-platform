package com.butenov.jobplatform.candidates.service.impl;

import org.springframework.stereotype.Service;

import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.candidates.repository.CandidateRepository;
import com.butenov.jobplatform.candidates.service.CandidateService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class DefaultCandidateService implements CandidateService
{
	private final CandidateRepository candidateRepository;

	public DefaultCandidateService(final CandidateRepository candidateRepository)
	{
		this.candidateRepository = candidateRepository;
	}

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
}
