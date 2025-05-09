package com.butenov.jobplatform.jobapplications.model;

import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.commons.model.BaseEntity;
import com.butenov.jobplatform.jobs.model.Job;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "job_application")
public class JobApplication extends BaseEntity
{
	@Enumerated(EnumType.STRING)
	private Status status;

	@ManyToOne
	@JoinColumn(name = "candidate_id")
	private Candidate candidate;

	@ManyToOne
	@JoinColumn(name = "job_id")
	private Job job;

	@Column(columnDefinition = "TEXT")
	private String feedback;

	public enum Status
	{
		SUBMITTED,
		IN_REVIEW,
		INTERVIEW,
		OFFERED,
		REJECTED
	}
}
