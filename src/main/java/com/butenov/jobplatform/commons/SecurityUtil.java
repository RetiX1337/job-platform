package com.butenov.jobplatform.commons;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.users.model.Candidate;
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
			return user instanceof Candidate;
		}

		return false;
	}

	public boolean isRecruiterAuthorizedToModifyJob(final UserDetails userDetails, final Job job)
	{
		final User user = userService.findByEmail(userDetails.getUsername());
		return user instanceof final Recruiter recruiter && job.getCompany().equals(recruiter.getCompany());
	}

	public void validateRecruiterAuthorizedToModifyJob(final UserDetails userDetails, final Job job)
	{
		if (!isRecruiterAuthorizedToModifyJob(userDetails, job))
		{
			throw new AccessDeniedException("You cannot modify this job.");
		}
	}
}

