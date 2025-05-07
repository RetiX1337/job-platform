package com.butenov.jobplatform.security.service.impl;

import java.util.Set;

import com.butenov.jobplatform.companies.model.Company;
import com.butenov.jobplatform.companies.service.CompanyService;
import com.butenov.jobplatform.security.dto.RecruiterRegistrationInfo;
import com.butenov.jobplatform.recruiters.model.Recruiter;
import com.butenov.jobplatform.users.model.Role;
import com.butenov.jobplatform.users.service.UserService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class RecruiterRegistrationService
{

	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	private final CompanyService companyService;

	public RecruiterRegistrationService(final UserService userService, final PasswordEncoder passwordEncoder,
	                                    final CompanyService companyService)
	{
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.companyService = companyService;
	}

	public void register(final RecruiterRegistrationInfo recruiterRegistrationInfo)
	{
		final Company company = companyService.findById(recruiterRegistrationInfo.getCompanyId());

		final String encodedPassword = passwordEncoder.encode(recruiterRegistrationInfo.getPassword());

		final Recruiter recruiter = new Recruiter();
		recruiter.setFirstName(recruiterRegistrationInfo.getFirstName());
		recruiter.setLastName(recruiterRegistrationInfo.getLastName());
		recruiter.setEmail(recruiterRegistrationInfo.getEmail());
		recruiter.setPassword(encodedPassword);
		recruiter.setRoles(Set.of(Role.ROLE_RECRUITER));
		recruiter.setCompany(company);

		userService.save(recruiter);
	}
}

