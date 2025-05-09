package com.butenov.jobplatform.commons;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.recruiters.model.Recruiter;
import com.butenov.jobplatform.recruiters.service.RecruiterUtil;
import com.butenov.jobplatform.users.model.User;
import com.butenov.jobplatform.users.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SecurityUtil
{

	private final UserService userService;
	private final RecruiterUtil recruiterUtil;

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

	public boolean isRecruiterAuthorizedToModifyJob(final Job job)
	{
		if (isRecruiter())
		{
			final Recruiter recruiter = recruiterUtil.getAuthenticatedRecruiter();
			return job.getCompany().equals(recruiter.getCompany());
		}
		return false;
	}

	public void validateRecruiterAuthorizedToModifyJob(final Job job)
	{
		if (!isRecruiterAuthorizedToModifyJob(job))
		{
			throw new AccessDeniedException("You cannot modify this job.");
		}
	}

	public User getAuthenticatedUser()
	{
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		return userService.findByEmail(userDetails.getUsername());
	}
}

