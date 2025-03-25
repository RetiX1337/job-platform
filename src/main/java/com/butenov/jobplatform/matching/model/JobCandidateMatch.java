package com.butenov.jobplatform.matching.model;

import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.commons.model.BaseEntity;
import com.butenov.jobplatform.jobs.model.Job;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "job_candidate_match")
@AllArgsConstructor
@NoArgsConstructor
public class JobCandidateMatch extends BaseEntity
{
	@ManyToOne
	@JoinColumn(name = "job_id")
	private Job job;

	@ManyToOne
	@JoinColumn(name = "candidate_id")
	private Candidate candidate;

	private Double matchScore;

}
