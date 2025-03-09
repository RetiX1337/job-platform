package com.butenov.jobplatform.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.butenov.jobplatform.companies.service.CompanyService;
import com.butenov.jobplatform.security.dto.RecruiterRegistrationInfo;
import com.butenov.jobplatform.security.service.impl.RecruiterRegistrationService;

@Controller
public class RecruiterRegistrationController
{
	private final RecruiterRegistrationService recruiterRegistrationService;
	private final CompanyService companyService;

	public RecruiterRegistrationController(final RecruiterRegistrationService recruiterRegistrationService,
	                                       final CompanyService companyService)
	{
		this.recruiterRegistrationService = recruiterRegistrationService;
		this.companyService = companyService;
	}

	@GetMapping("/register/recruiter")
	public String registerRecruiter(final Model model)
	{
		return populateModelAndReturnRegisterPage(model);
	}

	@PostMapping("/register/recruiter")
	public String registerRecruiter(
			@ModelAttribute("recruiterRegistrationInfo") final RecruiterRegistrationInfo recruiterRegistrationInfo,
			final Model model)
	{
		try
		{
			recruiterRegistrationService.register(recruiterRegistrationInfo);
			return "redirect:/login";
		}
		catch (final Exception e)
		{
			model.addAttribute("registrationError", "An error occurred during registration. Please try again.");
			return populateModelAndReturnRegisterPage(model);
		}
	}

	private String populateModelAndReturnRegisterPage(final Model model)
	{
		model.addAttribute("recruiterRegistrationInfo", new RecruiterRegistrationInfo());
		model.addAttribute("companies", companyService.findAll());
		return "register_recruiter";
	}
}
