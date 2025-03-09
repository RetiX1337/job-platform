package com.butenov.jobplatform.jobapplications.controller;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.butenov.jobplatform.jobapplications.model.JobApplication;
import com.butenov.jobplatform.jobapplications.service.JobApplicationService;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.jobs.service.JobService;
import com.butenov.jobplatform.users.model.Candidate;
import com.butenov.jobplatform.users.model.User;
import com.butenov.jobplatform.users.service.UserService;

@Controller
@RequestMapping("/applications")
public class JobApplicationController
{

	private final JobApplicationService jobApplicationService;
	private final JobService jobService;
	private final UserService userService;

	public JobApplicationController(JobApplicationService jobApplicationService, JobService jobService, UserService userService)
	{
		this.jobApplicationService = jobApplicationService;
		this.jobService = jobService;
		this.userService = userService;
	}

	@PreAuthorize("@securityUtil.isCandidate()")
	@PostMapping("/apply/{jobId}")
	public String applyForJob(@PathVariable final Long jobId, @AuthenticationPrincipal final UserDetails userDetails)
	{
		final User user = userService.findByEmail(userDetails.getUsername());

		if (!(user instanceof final Candidate candidate))
		{
			throw new AccessDeniedException("Only candidates can apply for jobs");
		}

		final Job job = jobService.findById(jobId);

		final JobApplication application = new JobApplication();
		application.setCandidate(candidate);
		application.setJob(job);

		jobApplicationService.save(application);

		return "redirect:/jobs/{jobId}";
	}


}

