package com.butenov.jobplatform.companies.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {

	private final CompanyService companyService;
	private final JobService jobService;

	@GetMapping("/{companyId}")
	public String viewCompany(@PathVariable final Long companyId,
	                          final Model model) {
		final Company company = companyService.findById(companyId);
		final Pageable pageable = PageRequest.of(0, 5);
		final List<Job> latestJobs = jobService.findLatestJobsForCompany(company, pageable).getContent();

		model.addAttribute("company", company);
		model.addAttribute("latestJobs", latestJobs);
		return "companies/details";
	}
}

