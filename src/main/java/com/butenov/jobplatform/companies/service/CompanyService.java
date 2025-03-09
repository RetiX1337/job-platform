package com.butenov.jobplatform.companies.service;

import java.util.Set;

import com.butenov.jobplatform.companies.model.Company;

public interface CompanyService
{
	Company findById(final Long id);

	Set<Company> findAll();
}
