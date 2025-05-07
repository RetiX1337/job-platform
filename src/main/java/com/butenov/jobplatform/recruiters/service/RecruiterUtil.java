package com.butenov.jobplatform.recruiters.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.butenov.jobplatform.recruiters.model.Recruiter;
import com.butenov.jobplatform.users.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RecruiterUtil
{
	private final UserService userService;

	public Recruiter getAuthenticatedRecruiter()
	{
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		return (Recruiter) userService.findByEmail(userDetails.getUsername());
	}
}
