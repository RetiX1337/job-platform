package com.butenov.jobplatform.jobs.dto;

import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.matching.model.JobCandidateMatch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobIntellectualSearchResult
{
	private Job job;
	private JobCandidateMatch jobCandidateMatch;

	public JobIntellectualSearchResult(final JobCandidateMatch jobCandidateMatch)
	{
		this.job = jobCandidateMatch.getJob();
		this.jobCandidateMatch = jobCandidateMatch;
	}
}
