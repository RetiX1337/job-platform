package com.butenov.jobplatform.jobapplications.controller;

import java.util.List;

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

import com.butenov.jobplatform.commons.SecurityUtil;
import com.butenov.jobplatform.jobapplications.model.JobApplication;
import com.butenov.jobplatform.jobapplications.service.JobApplicationService;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.jobs.service.JobService;
import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.users.model.User;
import com.butenov.jobplatform.users.service.UserService;

@Controller
@RequestMapping("/applications")
public class JobApplicationController
{

	private final JobApplicationService jobApplicationService;
	private final JobService jobService;
	private final UserService userService;
	private final SecurityUtil securityUtil;

	public JobApplicationController(final JobApplicationService jobApplicationService, final JobService jobService,
	                                final UserService userService, final SecurityUtil securityUtil)
	{
		this.jobApplicationService = jobApplicationService;
		this.jobService = jobService;
		this.userService = userService;
		this.securityUtil = securityUtil;
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

		jobApplicationService.applyForJob(job, candidate);

		return "redirect:/jobs/{jobId}";
	}

	@PreAuthorize("@securityUtil.isRecruiter()")
	@GetMapping("/job/{jobId}")
	public String viewApplications(@PathVariable final Long jobId, final Model model,
	                               @AuthenticationPrincipal final UserDetails userDetails)
	{
		final Job job = jobService.findById(jobId);

		securityUtil.validateRecruiterAuthorizedToModifyJob(userDetails, job);

		final List<JobApplication> applications = jobApplicationService.findByJobId(jobId);
		model.addAttribute("job", job);
		model.addAttribute("jobApplications", applications);
		return "applications/list_applications_for_job";
	}

	@PreAuthorize("@securityUtil.isCandidate()")
	@GetMapping("/")
	public String viewCandidateApplications(final Model model, @AuthenticationPrincipal final UserDetails userDetails)
	{
		final User user = userService.findByEmail(userDetails.getUsername());

		final Candidate candidate = (Candidate) user;

		final List<JobApplication> applications = jobApplicationService.findByCandidateId(candidate.getId());
		model.addAttribute("jobApplications", applications);
		return "applications/list_applications_for_candidate";
	}

	@PreAuthorize("@securityUtil.isRecruiter()")
	@PostMapping("/{applicationId}/accept")
	public String acceptApplication(@PathVariable final Long applicationId, @AuthenticationPrincipal final UserDetails userDetails)
	{
		final JobApplication jobApplication = jobApplicationService.findById(applicationId);
		final Job job = jobApplication.getJob();

		securityUtil.validateRecruiterAuthorizedToModifyJob(userDetails, job);

		jobApplicationService.acceptApplication(jobApplication);
		return "redirect:/applications/job/" + job.getId();
	}

	@PreAuthorize("@securityUtil.isRecruiter()")
	@PostMapping("/{applicationId}/reject")
	public String rejectApplication(@PathVariable final Long applicationId, @AuthenticationPrincipal final UserDetails userDetails)
	{
		final JobApplication jobApplication = jobApplicationService.findById(applicationId);
		final Job job = jobApplication.getJob();

		securityUtil.validateRecruiterAuthorizedToModifyJob(userDetails, job);

		jobApplicationService.rejectApplication(jobApplication);
		return "redirect:/applications/job/" + job.getId();
	}

}

