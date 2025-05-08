package com.butenov.jobplatform.jobapplications.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

	@PreAuthorize("@securityUtil.isRecruiter()")
	@PostMapping("/{applicationId}/accept")
	public String acceptApplication(@PathVariable final Long applicationId)
	{
		final JobApplication jobApplication = jobApplicationService.findById(applicationId);
		final Job job = jobApplication.getJob();

		securityUtil.validateRecruiterAuthorizedToModifyJob(job);

		jobApplicationService.acceptApplication(jobApplication);
		return "redirect:/applications/job/" + job.getId();
	}

	@PreAuthorize("@securityUtil.isRecruiter()")
	@PostMapping("/{applicationId}/reject")
	public String rejectApplication(@PathVariable final Long applicationId)
	{
		final JobApplication jobApplication = jobApplicationService.findById(applicationId);
		final Job job = jobApplication.getJob();

		securityUtil.validateRecruiterAuthorizedToModifyJob(job);

		jobApplicationService.rejectApplication(jobApplication);
		return "redirect:/applications/job/" + job.getId();
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

}

