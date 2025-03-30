package com.butenov.jobplatform.candidates.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.jobapplications.service.JobApplicationService;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.users.service.UserService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CandidateUtil
{
	private final UserService userService;
	private final JobApplicationService jobApplicationService;

	public boolean hasAlreadyApplied(final Job job)
	{
		final Candidate candidate = getAuthenticatedCandidate();
		return jobApplicationService.candidateHasAppliedForJob(job, candidate);
	}

	public Candidate getAuthenticatedCandidate()
	{
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		return (Candidate) userService.findByEmail(userDetails.getUsername());
	}
}
