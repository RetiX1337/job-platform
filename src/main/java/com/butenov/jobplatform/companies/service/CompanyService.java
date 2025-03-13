package com.butenov.jobplatform.companies.service;

import java.util.List;

import com.butenov.jobplatform.companies.model.Company;

public interface CompanyService
{
	Company findById(final Long id);

	List<Company> findAll();
}
