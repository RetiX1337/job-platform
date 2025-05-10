package com.butenov.jobplatform.companies.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.butenov.jobplatform.companies.model.Company;
import com.butenov.jobplatform.companies.service.CompanyService;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.jobs.service.JobService;
import com.butenov.jobplatform.recruiters.model.Recruiter;
import com.butenov.jobplatform.recruiters.service.RecruiterUtil;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {

	private final CompanyService companyService;
	private final JobService jobService;
	private final RecruiterUtil recruiterUtil;

	@GetMapping("/{companyId}")
	public String viewCompany(@PathVariable final Long companyId,
	                          final Model model) {
		final Company company = companyService.findById(companyId);
		return fillModelAndReturnView(model, company);
	}

	@PreAuthorize("@securityUtil.isRecruiter()")
	@GetMapping("/me")
	public String viewMyCompany(final Model model) {
		final Recruiter recruiter = recruiterUtil.getAuthenticatedRecruiter();
		final Company company = recruiter.getCompany();
		return fillModelAndReturnView(model, company);
	}

	private String fillModelAndReturnView(final Model model, final Company company)
	{
		final Pageable pageable = PageRequest.of(0, 5);
		final List<Job> latestJobs = jobService.findLatestJobsForCompany(company, pageable).getContent();

		model.addAttribute("company", company);
		model.addAttribute("latestJobs", latestJobs);
		return "companies/details";
	}
}

