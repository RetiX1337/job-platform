package com.butenov.jobplatform.candidates.controller;

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

import com.butenov.jobplatform.candidates.dto.CandidateIntellectualSearchResult;
import com.butenov.jobplatform.candidates.dto.CandidateSearchCriteria;
import com.butenov.jobplatform.candidates.service.CandidateSearchService;
import com.butenov.jobplatform.jobs.model.Job;
import com.butenov.jobplatform.jobs.service.JobService;
import com.butenov.jobplatform.skills.model.Skill;
import com.butenov.jobplatform.skills.service.SkillService;

import lombok.AllArgsConstructor;

@RequestMapping("/candidates")
@Controller
@AllArgsConstructor
public class CandidateSearchController
{
	private final CandidateSearchService candidateSearchService;
	private final JobService jobService;
	private final SkillService skillService;

	@PreAuthorize("@securityUtil.isRecruiter()")
	@GetMapping("/search-intellectual")
	public String searchCandidatesIntellectual(
			@ModelAttribute final CandidateSearchCriteria criteria, @RequestParam(defaultValue = "0") final int page,
			@RequestParam(defaultValue = "10") final int size, final Model model, @RequestParam final Long jobId)
	{
		final Pageable pageable = PageRequest.of(page, size);
		final Job job = jobService.findById(jobId);
		final Page<CandidateIntellectualSearchResult> candidatesPage = candidateSearchService.findMostFittingCandidates(criteria, job, pageable);

		final List<Skill> allSkills = skillService.findAll();

		model.addAttribute("allSkills", allSkills);
		model.addAttribute("candidatesPage", candidatesPage);
		model.addAttribute("criteria", criteria);
		model.addAttribute("currentPage", page);
		model.addAttribute("job", job);
		model.addAttribute("totalPages", candidatesPage.getTotalPages());
		model.addAttribute("totalItems", candidatesPage.getTotalElements());

		return "candidates/search-intellectual";
	}
}
