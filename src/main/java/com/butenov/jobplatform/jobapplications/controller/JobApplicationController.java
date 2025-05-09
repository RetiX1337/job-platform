package com.butenov.jobplatform.jobapplications.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.butenov.jobplatform.candidates.service.CandidateUtil;
import com.butenov.jobplatform.commons.SecurityUtil;
import com.butenov.jobplatform.jobapplications.model.JobApplication;
import com.butenov.jobplatform.jobapplications.service.JobApplicationService;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.jobs.service.JobService;
import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.recruiters.model.Recruiter;
import com.butenov.jobplatform.recruiters.service.RecruiterUtil;
import com.butenov.jobplatform.users.model.User;

import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/applications")
@AllArgsConstructor
public class JobApplicationController
{

	private final JobApplicationService jobApplicationService;
	private final JobService jobService;
	private final RecruiterUtil recruiterUtil;
	private final SecurityUtil securityUtil;
	private final CandidateUtil candidateUtil;

	@PreAuthorize("@securityUtil.isCandidate()")
	@PostMapping("/apply/{jobId}")
	public String applyForJob(@PathVariable final Long jobId)
	{
		final Candidate candidate = candidateUtil.getAuthenticatedCandidate();

		final Job job = jobService.findById(jobId);

		jobApplicationService.applyForJob(job, candidate);

		return "redirect:/jobs/{jobId}";
	}

	@PreAuthorize("@securityUtil.isRecruiter()")
	@GetMapping("/job/{jobId}")
	public String viewApplications(@PathVariable final Long jobId, final Model model)
	{
		final Job job = jobService.findById(jobId);

		securityUtil.validateRecruiterAuthorizedToModifyJob(job);

		final List<JobApplication> applications = jobApplicationService.findByJobId(jobId);
		model.addAttribute("job", job);
		model.addAttribute("jobApplications", applications);
		return "applications/list_applications_for_job";
	}

	@PreAuthorize("@securityUtil.isCandidate()")
	@GetMapping
	public String viewCandidateApplications(final Model model)
	{
		final Candidate candidate = candidateUtil.getAuthenticatedCandidate();

		final List<JobApplication> applications = jobApplicationService.findByCandidateId(candidate.getId());
		model.addAttribute("jobApplications", applications);
		return "applications/list_applications_for_candidate";
	}

	@GetMapping("/latest")
	public String getLatestApplications(final @RequestParam(defaultValue = "0") int page,
	                                    final @RequestParam(defaultValue = "10") int size,
	                                    final Model model)
	{
		final Pageable pageable = PageRequest.of(page, size);
		final Recruiter recruiter = recruiterUtil.getAuthenticatedRecruiter();

		final Page<JobApplication> latestApplications = jobApplicationService.findLatestApplicationsForRecruiter(recruiter,
				pageable);

		model.addAttribute("applications", latestApplications);
		return "applications/latest";
	}

	@PreAuthorize("@securityUtil.isCandidate() or @securityUtil.isRecruiter()")
	@GetMapping("/{applicationId}")
	public String viewApplicationDetails(@PathVariable final Long applicationId, final Model model)
	{
		final JobApplication application = jobApplicationService.findById(applicationId);
		final User user = securityUtil.getAuthenticatedUser();

		if (user instanceof Recruiter)
		{
			securityUtil.validateRecruiterAuthorizedToModifyJob(application.getJob());
		}
		else if (user instanceof final Candidate candidate && !application.getCandidate().getId().equals(candidate.getId()))
		{
			throw new AccessDeniedException("You are not authorized to view this application.");
		}

		model.addAttribute("statuses", JobApplication.Status.values());
		model.addAttribute("jobApplication", application);
		return "applications/view_application";
	}

	@PreAuthorize("@securityUtil.isRecruiter()")
	@PostMapping("/{applicationId}/update")
	public String updateApplication(@PathVariable final Long applicationId,
	                                @RequestParam final String status,
	                                @RequestParam final String feedback)
	{
		final JobApplication application = jobApplicationService.findById(applicationId);
		final Job job = application.getJob();

		securityUtil.validateRecruiterAuthorizedToModifyJob(job);

		application.setStatus(JobApplication.Status.valueOf(status));
		application.setFeedback(feedback);
		jobApplicationService.save(application);

		return "redirect:/applications/" + applicationId;
	}
}
