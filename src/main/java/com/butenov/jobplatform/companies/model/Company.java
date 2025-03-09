package com.butenov.jobplatform.companies.model;

import java.util.HashSet;
import java.util.Set;

import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.users.model.Recruiter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Company {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String description;

	@OneToMany(mappedBy = "company")
	private Set<Job> jobs;

	@OneToMany(mappedBy = "company")
	private Set<Recruiter> recruiters = new HashSet<>();
}
