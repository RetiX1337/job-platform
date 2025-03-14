package com.butenov.jobplatform.candidates.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.butenov.jobplatform.candidates.model.Candidate;

public interface CandidateRepository extends JpaRepository<Candidate, Long>
{
}
