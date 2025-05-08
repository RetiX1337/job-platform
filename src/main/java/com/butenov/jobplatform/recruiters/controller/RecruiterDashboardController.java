package com.butenov.jobplatform.recruiters.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.butenov.jobplatform.candidates.dto.CandidateIntellectualSearchResult;
import com.butenov.jobplatform.candidates.dto.CandidateSearchCriteria;
import com.butenov.jobplatform.candidates.service.CandidateSearchService;
import com.butenov.jobplatform.companies.model.Company;
import com.butenov.jobplatform.jobapplications.model.JobApplication;
import com.butenov.jobplatform.jobapplications.service.JobApplicationService;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.jobs.service.JobService;
import com.butenov.jobplatform.recruiters.model.Recruiter;
import com.butenov.jobplatform.recruiters.service.RecruiterUtil;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/recruiters/dashboard")
public class RecruiterDashboardController
{
	private final JobService jobService;
	private final JobApplicationService jobApplicationService;
	private final RecruiterUtil recruiterUtil;
	private final CandidateSearchService candidateSearchService;

	@GetMapping
	public String getDashboard(final Model model)
	{
		final Recruiter recruiter = recruiterUtil.getAuthenticatedRecruiter();
		final Company company = recruiter.getCompany();

		final Page<Job> latestJobs = jobService.findLatestJobsForCompany(company, PageRequest.of(0, 5));
		final Page<JobApplication> latestApplications = jobApplicationService.findLatestApplicationsForRecruiter(recruiter,
				PageRequest.of(0, 5));

		final Optional<Job> latestJob = latestJobs.getContent()
		                                         .stream()
		                                         .findFirst();
		if (latestJob.isPresent())
		{
			final Page<CandidateIntellectualSearchResult> candidateSearchResults = candidateSearchService.findMostFittingCandidates(
					new CandidateSearchCriteria(), latestJob.get(), PageRequest.of(0, 5));
			model.addAttribute("latestJob", latestJob.get());
			model.addAttribute("candidateSearchResults", candidateSearchResults);
		}

		model.addAttribute("latestJobs", latestJobs);
		model.addAttribute("latestApplications", latestApplications.getContent());

		return "recruiters/dashboard";
	}
}
