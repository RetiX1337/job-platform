package com.butenov.jobplatform.jobs.model;

import java.util.HashSet;
import java.util.Set;

import com.butenov.jobplatform.commons.model.BaseEntity;
import com.butenov.jobplatform.companies.model.Company;
import com.butenov.jobplatform.users.model.Recruiter;
import com.butenov.jobplatform.skills.model.Skill;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(JobEntityListener.class)
public class Job extends BaseEntity
{
	private String title;
	private String description;
	private String location;
	private String requirements;
	private Double salary;

	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;

	@ManyToOne
	@JoinColumn(name = "recruiter_id")
	private Recruiter recruiter;

	@ManyToMany
	@JoinTable(
			name = "job_skills",
			joinColumns = @JoinColumn(name = "job_id"),
			inverseJoinColumns = @JoinColumn(name = "skill_id")
	)
	private Set<Skill> requiredSkills = new HashSet<>();



}
