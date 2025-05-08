package com.butenov.jobplatform.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.butenov.jobplatform.commons.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AuthenticationController
{
	private final SecurityUtil securityUtil;

	@GetMapping("/login")
	public String login()
	{
		return "login";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = {"/dashboard", "/"})
	public String dashboard()
	{
		if (securityUtil.isCandidate())
		{
			return "redirect:/candidates/dashboard";
		} else if (securityUtil.isRecruiter())
		{
			return "redirect:/recruiters/dashboard";
		}
		return "redirect:/error";
	}
}

