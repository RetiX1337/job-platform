package com.butenov.jobplatform.candidates.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.butenov.jobplatform.candidates.dto.CandidateIntellectualSearchResult;
import com.butenov.jobplatform.candidates.dto.CandidateSearchCriteria;
import com.butenov.jobplatform.jobs.model.Job;

public interface CandidateSearchService
{
	Page<CandidateIntellectualSearchResult> findMostFittingCandidates(CandidateSearchCriteria criteria, Job job, Pageable pageable);
}
