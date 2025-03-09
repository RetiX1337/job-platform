package com.butenov.jobplatform.security.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.butenov.jobplatform.security.dto.CandidateRegistrationInfo;
import com.butenov.jobplatform.security.service.impl.CandidateRegistrationService;

@Controller
public class CandidateRegistrationController
{
	private final CandidateRegistrationService candidateRegistrationService;

	public CandidateRegistrationController(final CandidateRegistrationService candidateRegistrationService)
	{
		this.candidateRegistrationService = candidateRegistrationService;
	}

	@GetMapping("/register/candidate")
	public String registerCandidate(final Model model)
	{
		return populateModelAndReturnRegisterPage(model);
	}

	@PostMapping("/register/candidate")
	public String registerCandidate(
			@ModelAttribute("candidateRegistrationInfo") final CandidateRegistrationInfo candidateRegistrationInfo,
			final Model model)
	{
		try
		{
			candidateRegistrationService.register(candidateRegistrationInfo);
			return "redirect:/login";
		}
		catch (final Exception e)
		{
			model.addAttribute("registrationError", "An error occurred during registration. Please try again.");
			return populateModelAndReturnRegisterPage(model);
		}
	}

	private static String populateModelAndReturnRegisterPage(final Model model)
	{
		model.addAttribute("candidateRegistrationInfo", new CandidateRegistrationInfo());
		return "register_candidate";
	}
}

