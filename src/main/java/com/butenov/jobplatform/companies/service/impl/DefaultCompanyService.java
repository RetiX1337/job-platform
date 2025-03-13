package com.butenov.jobplatform.companies.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.butenov.jobplatform.companies.model.Company;
import com.butenov.jobplatform.companies.repository.CompanyRepository;
import com.butenov.jobplatform.companies.service.CompanyService;

@Service
public class DefaultCompanyService implements CompanyService
{
	private final CompanyRepository companyRepository;

	public DefaultCompanyService(final CompanyRepository companyRepository)
	{
		this.companyRepository = companyRepository;
	}

	@Override
	public Company findById(final Long id)
	{
		return companyRepository.findById(id)
		                        .orElseThrow(() -> new IllegalArgumentException("Company with id %d not found".formatted(id)));
	}

	@Override
	public List<Company> findAll()
	{
		return companyRepository.findAll();
	}
}
