package com.butenov.jobplatform.jobs.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.candidates.service.CandidateUtil;
import com.butenov.jobplatform.companies.model.Company;
import com.butenov.jobplatform.companies.service.CompanyService;
import com.butenov.jobplatform.jobs.dto.JobIntellectualSearchResult;
import com.butenov.jobplatform.jobs.dto.JobSearchCriteria;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.jobs.service.JobSearchService;
import com.butenov.jobplatform.skills.model.Skill;
import com.butenov.jobplatform.skills.service.SkillService;

import lombok.AllArgsConstructor;

@RequestMapping("/jobs")
@Controller
@AllArgsConstructor
public class JobSearchController
{
	private final JobSearchService jobSearchService;
	private final SkillService skillService;
	private final CompanyService companyService;
	private final CandidateUtil candidateUtil;

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

	@PreAuthorize("@securityUtil.isCandidate()")
	@GetMapping("/search-intellectual")
	public String searchJobsIntellectual(
			@ModelAttribute final JobSearchCriteria criteria, @RequestParam(defaultValue = "0") final int page,
			@RequestParam(defaultValue = "10") final int size, final Model model)
	{
		final Pageable pageable = PageRequest.of(page, size);
		final Candidate candidate = candidateUtil.getAuthenticatedCandidate();
		final Page<JobIntellectualSearchResult> jobPage = jobSearchService.findMostFittingJobs(criteria, candidate, pageable);

		final List<Skill> allSkills = skillService.findAll();
		final List<Company> allCompanies = companyService.findAll();

		model.addAttribute("allSkills", allSkills);
		model.addAttribute("allCompanies", allCompanies);
		model.addAttribute("jobPage", jobPage);
		model.addAttribute("criteria", criteria);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", jobPage.getTotalPages());
		model.addAttribute("totalItems", jobPage.getTotalElements());

		return "jobs/search-intellectual";
	}
}

