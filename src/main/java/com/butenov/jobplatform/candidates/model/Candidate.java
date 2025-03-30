package com.butenov.jobplatform.candidates.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.skills.model.Skill;
import com.butenov.jobplatform.users.model.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class Candidate extends User
{
	@OneToOne(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
	private CandidateProfile candidateProfile;

	@ManyToMany
	@JoinTable(
			name = "bookmarked_jobs",
			joinColumns = @JoinColumn(name = "candidate_id"),
			inverseJoinColumns = @JoinColumn(name = "job_id")
	)
	private Set<Job> bookmarkedJobs = new HashSet<>();

	private String cvLink;
}

