package com.butenov.jobplatform.candidates.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.butenov.jobplatform.commons.model.BaseEntity;
import com.butenov.jobplatform.skills.model.Skill;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
public class CandidateProfile extends BaseEntity
{
	@OneToOne
	@JoinColumn(name = "candidate_id")
	private Candidate candidate;

	@ManyToMany
	@JoinTable(
			name = "candidate_profile_skills",
			joinColumns = @JoinColumn(name = "candidate_profile_id"),
			inverseJoinColumns = @JoinColumn(name = "skill_id")
	)
	private Set<Skill> skills = new HashSet<>();

	@OneToMany(mappedBy = "candidateProfile", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<JobExperience> jobExperiences = new ArrayList<>();

	@OneToMany(mappedBy = "candidateProfile", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Education> educations = new ArrayList<>();
}