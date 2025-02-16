package com.butenov.jobplatform.users.security;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.butenov.jobplatform.users.security.dto.UserRegistrationInfo;
import com.butenov.jobplatform.users.security.services.RegistrationService;

@Controller
public class RegistrationController
{

	private final RegistrationService registrationService;

	public RegistrationController(final RegistrationService registrationService)
	{
		this.registrationService = registrationService;
	}

	@GetMapping("/register")
	public String register(final Model model)
	{
		model.addAttribute("userRegistrationInfo", new UserRegistrationInfo());
		return "register";
	}

	@PostMapping("/register")
	public String registerUser(@ModelAttribute("userRegistrationInfo") final UserRegistrationInfo userRegistrationInfo,
	                           final BindingResult result, final Model model)
	{
		if (result.hasErrors())
		{
			return "register";
		}

		try
		{
			registrationService.register(userRegistrationInfo);
		}
		catch (final Exception e)
		{
			model.addAttribute("registrationError", "An error occurred during registration. Please try again.");
			return "register";
		}
		return "redirect:/login";
	}
}
