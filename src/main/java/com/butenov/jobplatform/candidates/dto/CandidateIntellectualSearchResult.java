package com.butenov.jobplatform.candidates.dto;

import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.matching.model.JobCandidateMatch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CandidateIntellectualSearchResult
{
	private Candidate candidate;
	private JobCandidateMatch jobCandidateMatch;
}
