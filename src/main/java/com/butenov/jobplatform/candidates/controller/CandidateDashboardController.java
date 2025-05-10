package com.butenov.jobplatform.candidates.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.butenov.jobplatform.candidates.model.Candidate;
import com.butenov.jobplatform.candidates.service.CandidateUtil;
import com.butenov.jobplatform.jobapplications.service.JobApplicationService;
import com.butenov.jobplatform.jobs.dto.JobSearchCriteria;
import com.butenov.jobplatform.jobs.service.JobSearchService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/candidates/dashboard")
public class CandidateDashboardController
{

	private final JobSearchService jobSearchService;
	private final JobApplicationService jobApplicationService;
	private final CandidateUtil candidateUtil;

	@PreAuthorize("@securityUtil.isCandidate()")
	@GetMapping
	public String showDashboard(final Model model)
	{
		final Candidate candidate = candidateUtil.getAuthenticatedCandidate();

		model.addAttribute("candidate", candidate);
		model.addAttribute("profile", candidate.getCandidateProfile());
		model.addAttribute("bookmarks", candidate.getBookmarkedJobs().stream()
		                                         .limit(5)
		                                         .toList());
		model.addAttribute("applications", jobApplicationService.findByCandidateId(candidate.getId()).stream()
		                                                        .limit(5)
		                                                        .toList());
		model.addAttribute("suggestedJobs",
				jobSearchService.findMostFittingJobs(new JobSearchCriteria(), candidate, Pageable.ofSize(5)));

		return "candidates/dashboard";
	}
}

