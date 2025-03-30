package com.butenov.jobplatform.candidates.service;

import org.springframework.web.multipart.MultipartFile;

import com.butenov.jobplatform.candidates.model.Candidate;

import jakarta.transaction.Transactional;

public interface CandidateService
{
	Candidate findById(Long id);

	void save(Candidate candidate);

	void uploadCV(Candidate candidate, MultipartFile cvFile);

	@Transactional
	Candidate updateCandidateFromCV(Candidate candidate);

	Candidate update(Candidate candidate);
}
