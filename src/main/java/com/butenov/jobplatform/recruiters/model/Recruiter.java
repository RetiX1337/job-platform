package com.butenov.jobplatform.recruiters.model;

import com.butenov.jobplatform.companies.model.Company;
import com.butenov.jobplatform.users.model.User;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
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
public class Recruiter extends User
{

	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;
}

