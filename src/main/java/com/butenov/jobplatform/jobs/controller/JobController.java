package com.butenov.jobplatform.jobs.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.candidates.service.CandidateService;
import com.butenov.jobplatform.candidates.service.CandidateUtil;
import com.butenov.jobplatform.commons.SecurityUtil;
import com.butenov.jobplatform.jobs.converter.JobConverter;
import com.butenov.jobplatform.jobs.dto.JobForm;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.jobs.service.JobService;
import com.butenov.jobplatform.matching.service.JobCandidateMatchService;
import com.butenov.jobplatform.skills.service.SkillService;
import com.butenov.jobplatform.users.model.Recruiter;
import com.butenov.jobplatform.users.model.User;
import com.butenov.jobplatform.users.service.UserService;

import lombok.AllArgsConstructor;


@Controller
@RequestMapping("/jobs")
@AllArgsConstructor
public class JobController
{

	private final JobService jobService;
	private final JobConverter jobConverter;
	private final SkillService skillService;
	private final UserService userService;
	private final CandidateService candidateService;
	private final SecurityUtil securityUtil;
	private final JobCandidateMatchService jobCandidateMatchService;
	private final CandidateUtil candidateUtil;

	@GetMapping("/{id}")
	public String showJob(@PathVariable final Long id, final Model model)
	{
		final Job job = jobService.findById(id);

		model.addAttribute("job", job);
		if (securityUtil.isCandidate())
		{
			final Candidate candidate = candidateUtil.getAuthenticatedCandidate();
			model.addAttribute("jobCandidateMatch",
					jobCandidateMatchService.getJobMatchScore(job, candidate));
			model.addAttribute("bookmark", candidate.getBookmarkedJobs().contains(job));
		}
		return "jobs/details";
	}

	@GetMapping
	public String listJobs(final Model model)
	{
		model.addAttribute("jobs", jobService.findAll());
		return "jobs/list";
	}

	@PreAuthorize("@securityUtil.isRecruiter()")
	@GetMapping("/create")
	public String showCreateForm(final Model model)
	{
		return prepareJobForm(new JobForm(), model);
	}

	@PreAuthorize("@securityUtil.isRecruiter()")
	@PostMapping("/create")
	public String createJob(@ModelAttribute final JobForm jobForm, final Model model,
	                        @AuthenticationPrincipal final UserDetails userDetails)
	{
		final Recruiter recruiter = getAuthenticatedRecruiter(userDetails, model);
		if (recruiter == null) return "jobs/form";

		jobForm.setCompanyId(recruiter.getCompany().getId());

		return saveJob(jobForm, model, false);
	}

	@PreAuthorize("@securityUtil.isRecruiter()")
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable final Long id, final Model model,
	                           @AuthenticationPrincipal final UserDetails userDetails)
	{
		final Job job = jobService.findById(id);
		securityUtil.validateRecruiterAuthorizedToModifyJob(userDetails, job);

		return prepareJobForm(jobConverter.convertToDto(job), model);
	}

	@PreAuthorize("@securityUtil.isRecruiter()")
	@PostMapping("/edit/{id}")
	public String updateJob(@PathVariable final Long id, @ModelAttribute final JobForm jobForm, final Model model,
	                        @AuthenticationPrincipal final UserDetails userDetails)
	{
		final Job existingJob = jobService.findById(id);
		securityUtil.validateRecruiterAuthorizedToModifyJob(userDetails, existingJob);

		jobForm.setCompanyId(existingJob.getCompany().getId());
		return saveJob(jobForm, model, true);
	}

	@PreAuthorize("@securityUtil.isRecruiter()")
	@PostMapping("/delete/{id}")
	public String deleteJob(@PathVariable final Long id, @AuthenticationPrincipal final UserDetails userDetails)
	{
		final Job job = jobService.findById(id);
		securityUtil.validateRecruiterAuthorizedToModifyJob(userDetails, job);

		jobService.deleteById(id);
		return "redirect:/jobs";
	}

	@PreAuthorize("@securityUtil.isCandidate()")
	@PostMapping("/bookmark/{id}")
	public String bookmarkJob(@PathVariable final Long id, @RequestHeader("referer") final String referer)
	{
		final Candidate candidate = candidateUtil.getAuthenticatedCandidate();
		final Job job = jobService.findById(id);
		candidateService.toggleJobBookmark(candidate, job);
		return "redirect:" + referer;
	}

	private String prepareJobForm(final JobForm jobForm, final Model model)
	{
		model.addAttribute("jobForm", jobForm);
		model.addAttribute("skills", skillService.findAll());
		return "jobs/form";
	}

	private Recruiter getAuthenticatedRecruiter(@AuthenticationPrincipal final UserDetails userDetails, final Model model)
	{
		final User user = userService.findByEmail(userDetails.getUsername());

		if (!(user instanceof final Recruiter recruiter))
		{
			model.addAttribute("error", "Only recruiters can perform this action.");
			return null;
		}

		if (recruiter.getCompany() == null)
		{
			model.addAttribute("error", "You are not associated with any company.");
			return null;
		}

		return recruiter;
	}

	private String saveJob(final JobForm jobForm, final Model model, final boolean isUpdate)
	{
		try
		{
			final Job job = jobConverter.convertToEntity(jobForm);
			if (isUpdate)
			{
				jobService.update(job);
			}
			else
			{
				jobService.save(job);
			}
			return "redirect:/jobs";
		}
		catch (final Exception e)
		{
			model.addAttribute("error", "An error occurred while processing the job.");
			model.addAttribute("skills", skillService.findAll());
			return "jobs/form";
		}
	}
}


