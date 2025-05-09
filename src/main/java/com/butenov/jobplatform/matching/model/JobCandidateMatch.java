package com.butenov.jobplatform.matching.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.commons.model.BaseEntity;
import com.butenov.jobplatform.jobs.model.Job;

import jakarta.persistence.Column;
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
	@JoinColumn(name = "job_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Job job;

	@ManyToOne
	@JoinColumn(name = "candidate_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Candidate candidate;

	private Double matchScore;

	@Column(columnDefinition = "TEXT")
	private String intellectualAnalysisJustification;

}
