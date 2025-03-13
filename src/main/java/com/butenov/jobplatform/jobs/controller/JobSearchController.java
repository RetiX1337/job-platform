package com.butenov.jobplatform.jobs.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.butenov.jobplatform.companies.model.Company;
import com.butenov.jobplatform.companies.service.CompanyService;
import com.butenov.jobplatform.jobs.dto.JobSearchCriteria;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.jobs.service.JobSearchService;
import com.butenov.jobplatform.skills.model.Skill;
import com.butenov.jobplatform.skills.service.SkillService;

@RequestMapping("/jobs")
@Controller
public class JobSearchController
{
	private final JobSearchService jobSearchService;
	private final SkillService skillService;
	private final CompanyService companyService;

	public JobSearchController(final JobSearchService jobSearchService, final SkillService skillService,
	                           final CompanyService companyService)
	{
		this.jobSearchService = jobSearchService;
		this.skillService = skillService;
		this.companyService = companyService;
	}

	@GetMapping("/search")
	public String searchJobs(
			@ModelAttribute final JobSearchCriteria criteria, @RequestParam(defaultValue = "0") final int page,
			@RequestParam(defaultValue = "10") final int size, final Model model)
	{
		final Pageable pageable = PageRequest.of(page, size);
		final Page<Job> jobPage = jobSearchService.searchJobs(criteria, pageable);

		final List<Skill> allSkills = skillService.findAll();
		final List<Company> allCompanies = companyService.findAll();

		model.addAttribute("allSkills", allSkills);
		model.addAttribute("allCompanies", allCompanies);
		model.addAttribute("jobPage", jobPage);
		model.addAttribute("criteria", criteria);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", jobPage.getTotalPages());
		model.addAttribute("totalItems", jobPage.getTotalElements());

		return "jobs/search";
	}
}

