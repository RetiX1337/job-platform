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
import org.springframework.web.bind.annotation.RequestMapping;

import com.butenov.jobplatform.commons.SecurityUtil;
import com.butenov.jobplatform.jobs.converter.JobConverter;
import com.butenov.jobplatform.jobs.dto.JobForm;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.jobs.service.JobService;
import com.butenov.jobplatform.skills.service.SkillService;
import com.butenov.jobplatform.users.model.Recruiter;
import com.butenov.jobplatform.users.model.User;
import com.butenov.jobplatform.users.service.UserService;


@Controller
@RequestMapping("/jobs")
public class JobController
{

	private final JobService jobService;
	private final JobConverter jobConverter;
	private final SkillService skillService;
	private final UserService userService;
	private final SecurityUtil securityUtil;

	public JobController(final JobService jobService, final JobConverter jobConverter,
	                     final SkillService skillService, final UserService userService, final SecurityUtil securityUtil)
	{
		this.jobService = jobService;
		this.jobConverter = jobConverter;
		this.skillService = skillService;
		this.userService = userService;
		this.securityUtil = securityUtil;
	}

	@GetMapping("/{id}")
	public String showJob(@PathVariable final Long id, final Model model) {
		final Job job = jobService.findById(id);
		final JobForm jobForm = jobConverter.convertToDto(job);

		model.addAttribute("job", jobForm);
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

		return saveJob(jobForm, model);
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
		return saveJob(jobForm, model);
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

	private String saveJob(final JobForm jobForm, final Model model)
	{
		try
		{
			final Job job = jobConverter.convertToEntity(jobForm);
			jobService.save(job);
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


