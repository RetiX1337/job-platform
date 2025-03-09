package com.butenov.jobplatform.commons;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.butenov.jobplatform.users.model.Recruiter;
import com.butenov.jobplatform.users.model.User;
import com.butenov.jobplatform.users.service.UserService;

@Component
public class SecurityUtil
{

	private final UserService userService;

	public SecurityUtil(final UserService userService)
	{
		this.userService = userService;
	}

	public boolean isRecruiter()
	{
		final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof final UserDetails userDetails)
		{
			final User user = userService.findByEmail(userDetails.getUsername());
			return user instanceof Recruiter;
		}

		return false;
	}

	public boolean isCandidate()
	{
		final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof final UserDetails userDetails)
		{
			final User user = userService.findByEmail(userDetails.getUsername());
			return user instanceof Recruiter;
		}

		return false;
	}
}

