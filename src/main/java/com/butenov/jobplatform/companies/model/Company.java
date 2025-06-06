package com.butenov.jobplatform.companies.model;

import java.util.HashSet;
import java.util.Set;

import com.butenov.jobplatform.commons.model.BaseEntity;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.recruiters.model.Recruiter;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Company extends BaseEntity
{
	private String name;
	private String description;

	@OneToMany(mappedBy = "company")
	private Set<Job> jobs;

	@OneToMany(mappedBy = "company")
	private Set<Recruiter> recruiters = new HashSet<>();
}
