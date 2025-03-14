package com.butenov.jobplatform.candidates.service;

import com.butenov.jobplatform.candidates.model.Candidate;

public interface CandidateService
{
	Candidate findById(Long id);

	void save(Candidate candidate);
}
