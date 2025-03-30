package com.butenov.jobplatform.candidates.model;

import java.time.LocalDate;

import com.butenov.jobplatform.commons.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class JobExperience extends BaseEntity
{
	private String jobTitle;
	private String companyName;
	private String location;
	private LocalDate startDate;
	private LocalDate endDate;

	@Column(columnDefinition = "TEXT")
	private String description;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "candidate_profile_id")
	private CandidateProfile candidateProfile;
}
